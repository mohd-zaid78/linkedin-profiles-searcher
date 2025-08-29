package com.zaid.alumnisearch.pojo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlumniProfile {
	
	@JsonAlias("fullName") 
    private String name;
	
	@JsonAlias("jobTitle") 
    private String currentRole;
	
	@JsonAlias("school") 
    private String university;
	
	@JsonAlias("location") 
    private String location;
	
	@JsonAlias("headline") 
    private String linkedinHeadline;
	
	@JsonAlias("schoolDateRange") 
    private String year;

    
}