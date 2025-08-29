package com.zaid.alumnisearch.pojo;

import lombok.Data;

@Data
public class AgentStatusResponse {
	
	private String id;
	private long createdAt;
	private long launchedAt;
	private long endedAt;
	private String status;
	private String  launchType;
	private long retryNumber;
	private long exitCode;
	private String endType;

	
}
