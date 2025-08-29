package com.zaid.alumnisearch.launchagent.reqbody;

import lombok.Data;

@Data
public class Argument {
	
	private String sessionCookie;
	private String search;
	private int numberOfResults;
	private int maxPages;
	 
	

}
