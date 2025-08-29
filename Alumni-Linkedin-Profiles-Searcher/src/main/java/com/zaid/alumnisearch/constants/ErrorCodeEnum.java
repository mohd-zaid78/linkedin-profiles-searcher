package com.zaid.alumnisearch.constants;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {
	
	GENERIC_ERROR("failed","unable to process your request this time please try agin later "),
	AGENT_NOTRUN_ERROR("failed","agent is not run either due to internal phantombuster issue or maybe you not create the agent slot at phantombuster website first create phantom slot at phantombuster website using linkedin search expert and try again"),
	AGENT_RUNNING_ERROR("failed","phantombuster agent status is running thats why AlumniProfiles is not fetch, for getting AlumniProfiles data, Agent status should be finished, you have to  manually abort the agent by going at phantombuster website strictly under 70 seconds do this because thread is hold for 90 seconds only at code side because agent takes time in searching data and also should abort manually under 70 seconds by user then wait further for upto 20 seconds, thread is free under 20 sec and then you got your excepted AlumniProfiles."),
	ALUMNI_PROFILESDATA_JSONURL_NOTFOUND_ERROR("failed","Response got successfully from PhantomBuster fetch-output Api but in response there is no .json url  present where phantombuster stored the AlumniProfiles thats why we didn't get AlumniProfiles data its phantombuster side issue please try again"),
	ALUMNI_PROFILE_NOTMATCHED_WITH_USERCRITERIA_ERROR("Success","Request is Success but there is no Alumni Profile match with user criteria"),
	SERVER_ERROR("failed","server error");
	
	private final String status;
	private final String errorMessage;
	
	private ErrorCodeEnum(String status,String errorMessage) {
		this.status=status;
		this.errorMessage=errorMessage;
		
	}
	
	

}
