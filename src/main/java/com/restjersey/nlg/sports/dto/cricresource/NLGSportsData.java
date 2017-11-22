package com.restjersey.nlg.sports.dto.cricresource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class below holds the data which is the news summary.
 * 
 * @author abhishek
 *
 */

public class NLGSportsData {
	
	/**
	 * summary of news.
	 */
	@io.swagger.annotations.ApiModelProperty(value = "data", notes = "summary of news", required = true)
	private	List<Map<String,String>> matchsummary = new ArrayList<Map<String,String>>() ;

	public List<Map<String, String>> getMatchsummary() {
		return matchsummary;
	}

	public void setMatchsummary(List<Map<String, String>> matchsummary) {
		this.matchsummary = matchsummary;
	}
	
	public void add(Map<String,String> mapOfNews) {
		matchsummary.add(mapOfNews);
	}

	
}
