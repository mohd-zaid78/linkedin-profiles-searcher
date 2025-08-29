package com.zaid.alumnisearch.pojo;
import java.util.List;

import lombok.Data;

@Data
public class AlumniResponse {
	
    private String status;
    private List<AlumniProfile> data;

    
}
