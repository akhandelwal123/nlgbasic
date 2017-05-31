package com.restjersey.nlg.sports;

import java.util.HashMap;
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
	private	Map<String,String>matchsummary = new HashMap<String,String>() ;

	public Map<String, String> getMatchSummary() {
		return matchsummary;
	}

	public void setMatchSummary(Map<String, String> mapOfNews) {
		this.matchsummary = mapOfNews;
	}
	
	
}
