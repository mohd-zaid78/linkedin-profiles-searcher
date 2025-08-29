package com.zaid.alumnisearch.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaid.alumnisearch.constants.ErrorCodeEnum;
import com.zaid.alumnisearch.exception.LinkedinsAlumniProfileExcpetion;
import com.zaid.alumnisearch.http.HttpRequest;
import com.zaid.alumnisearch.http.HttpServiceEngine;
import com.zaid.alumnisearch.pojo.AgentStatusResponse;
import com.zaid.alumnisearch.pojo.AlumniProfile;
import com.zaid.alumnisearch.pojo.AlumniResponse;
import com.zaid.alumnisearch.pojo.SearchRequest;
import com.zaid.alumnisearch.service.helper.AgentDataJsonHelper;
import com.zaid.alumnisearch.service.helper.CheckAgentStatusHelper;
import com.zaid.alumnisearch.service.helper.FetchAgentDataHelper;
import com.zaid.alumnisearch.service.helper.LaunchPhantomAgentHelper;
import com.zaid.alumnisearch.utils.JsonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlumniServiceImpl implements AlumniService {

	@Value("${launchAgentRequestBody.id}")
	private String agentId;

	private final JsonUtils jsonUtils;
	private final HttpServiceEngine httpServiceEngine;
	private final LaunchPhantomAgentHelper launchPhantomAgentHelper;
	private final CheckAgentStatusHelper checkAgentStatusHelper;
	private final FetchAgentDataHelper fetchAgentDataHelper;
	private final AgentDataJsonHelper agentDataJsonHelper;

	@Override
	public ResponseEntity<AlumniResponse> searchAlumniProfile(SearchRequest request) {

		// preparing HttpRequest for calling agentlaunch api endpoint and launching the
		// phantom
		HttpRequest launchAgentHttpRequest = launchPhantomAgentHelper.prepareHttpRequestForAgentLaunch(request);

		// making the external api call for launching the phantom
		ResponseEntity<String> phantomAgentLaunched = httpServiceEngine.makeHttpCall(launchAgentHttpRequest);
		String responseBody = phantomAgentLaunched.getBody();

		// getting the json pasticular field value which containerId
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = null;
		;
		try {
			root = mapper.readTree(responseBody);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		String containerId = root.get("containerId").asText();
		log.info("container Id" + containerId);

		if (phantomAgentLaunched != null) {

			// preparing HttpRequest for calling containersFetch endpoint and checking the
			// phantom status
			HttpRequest agentStatusCheckHttpRequest = checkAgentStatusHelper
					.prepareHttpRequestForAgentStatus(containerId);

			// making the external api call for checking status of the phantom
			ResponseEntity<String> agentStatusFetched = httpServiceEngine.makeHttpCall(agentStatusCheckHttpRequest);
			String agentStatusJsonBody = agentStatusFetched.getBody();

			AgentStatusResponse agentStatusResponse = jsonUtils.fromJson(agentStatusJsonBody,
					AgentStatusResponse.class);
			if (agentStatusResponse.getStatus().equals("running")) {

				try {
					// Hold the thread
					Thread.sleep(90000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					log.error("Thread was interrupted while waiting for agent to fetch data", e);
				}

			}

			// preparing HttpRequest for calling containersFetch endpoint and checking the
			// phantom status
			agentStatusCheckHttpRequest = checkAgentStatusHelper.prepareHttpRequestForAgentStatus(containerId);

			// making the external api call for checking status of the phantom
			agentStatusFetched = httpServiceEngine.makeHttpCall(agentStatusCheckHttpRequest);
			agentStatusJsonBody = agentStatusFetched.getBody();

			agentStatusResponse = jsonUtils.fromJson(agentStatusJsonBody, AgentStatusResponse.class);
			if (agentStatusResponse.getStatus().equals("finished")) {

				// preparing HttpRequest for calling containersfetchoutput endpoint and fetching
				// the phantom Alumni data
				HttpRequest fetchAgentDataHttpRequest = fetchAgentDataHelper
						.prepareHttpRequestForFetchAgentData(containerId);

				// making the external api call for fetching Alumniprofiles
				ResponseEntity<String> fetchAgentDataResponse = httpServiceEngine
						.makeHttpCall(fetchAgentDataHttpRequest);
				String fetchDataResponseBody = fetchAgentDataResponse.getBody();
				log.info("fetched" + fetchDataResponseBody);

				// Get the field name from json object
				JSONObject obj = new JSONObject(fetchDataResponseBody);
				String output = obj.getString("output");

				// Regex to match only .json URL
				Pattern pattern = Pattern.compile("https?://[^\\s]+\\.json");
				Matcher matcher = pattern.matcher(output);

				String jsonUrl = null;
				if (matcher.find()) {
					jsonUrl = matcher.group(); // .json URL
				}

				log.info("json exact url check  " + jsonUrl);

				if (jsonUrl != null && jsonUrl.contains(".json")) {
					System.out.println("Extracted JSON URL: " + jsonUrl);

					// preparing HttpRequest for getting the data present in .json file at aws s3
					HttpRequest agentJsonDataHttpRequest = agentDataJsonHelper
							.prepareHttpRequestForAgentDataJson(jsonUrl);

					// making the external call for fetching actual AlumniProfiles objects in an
					// array
					ResponseEntity<String> alumniProfileData = httpServiceEngine.makeHttpCall(agentJsonDataHttpRequest);

					ObjectMapper objectMapper = new ObjectMapper();
					List<AlumniProfile> alumniProfiles = null;

					try {
						alumniProfiles = objectMapper.readValue(alumniProfileData.getBody(),
								new TypeReference<List<AlumniProfile>>() {
								});
					} catch (Exception e) {
						e.printStackTrace();
					}

					log.info("AlumniResponse is {} ", alumniProfiles);

					// apply fitering on fetched AlumniProfiles
					List<AlumniProfile> filteredProfiles = alumniProfiles.stream()
							.filter(p -> request.getDesignation() == null || (p.getCurrentRole() != null && p
									.getCurrentRole().toLowerCase().contains(request.getDesignation().toLowerCase())))
							.filter(p -> request.getUniversity() == null || (p.getUniversity() != null
									&& p.getUniversity().toLowerCase().contains(request.getUniversity().toLowerCase())))
							.filter(p -> request.getPassoutYear() == null
									|| (p.getYear() != null && p.getYear().contains(request.getPassoutYear())))
							.toList();

					log.info("Filtered AlumniResponse is {} ", filteredProfiles);

					if (filteredProfiles != null && !filteredProfiles.isEmpty()) {
						AlumniResponse alumniResponse = new AlumniResponse();
						alumniResponse.setStatus("success");
						alumniResponse.setData(filteredProfiles);
						return ResponseEntity.ok(alumniResponse);

					} else {
						log.error("Request is Success but there is no Alumni Profile match with user criteria");
						throw new LinkedinsAlumniProfileExcpetion(
								ErrorCodeEnum.ALUMNI_PROFILE_NOTMATCHED_WITH_USERCRITERIA_ERROR.getStatus(),
								ErrorCodeEnum.ALUMNI_PROFILE_NOTMATCHED_WITH_USERCRITERIA_ERROR.getErrorMessage(),
								HttpStatus.OK);
					}

				} else {
					log.error("Response got successfully from PhantomBuster fetch-output Api but in response there is no .json url "
							+ " present where phantombuster stored the AlumniProfiles thats why we didn't get AlumniProfiles data"
							+ " its phantombuster side issue please try again");
						throw new LinkedinsAlumniProfileExcpetion(
								ErrorCodeEnum.ALUMNI_PROFILESDATA_JSONURL_NOTFOUND_ERROR.getStatus(),
								ErrorCodeEnum.ALUMNI_PROFILESDATA_JSONURL_NOTFOUND_ERROR.getErrorMessage(),
								HttpStatus.NOT_FOUND);
				}
			} else {

				throw new LinkedinsAlumniProfileExcpetion(ErrorCodeEnum.AGENT_RUNNING_ERROR.getStatus(),
						ErrorCodeEnum.AGENT_RUNNING_ERROR.getErrorMessage(), HttpStatus.REQUEST_TIMEOUT);

			}

		}

		throw new LinkedinsAlumniProfileExcpetion(ErrorCodeEnum.AGENT_NOTRUN_ERROR.getStatus(),
				ErrorCodeEnum.AGENT_NOTRUN_ERROR.getErrorMessage(), HttpStatus.SERVICE_UNAVAILABLE);

	}
}