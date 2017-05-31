package com.restjersey.nlg.sports.stats;

import java.util.HashMap;
import java.util.Map;

public class MatchStats {
	
	private Map <String , StringBuilder> matchNews =  new HashMap<String, StringBuilder>();

	public Map <String , StringBuilder> getMatchNews() {
		return matchNews;
	}

	public void setMatchNews(Map <String , StringBuilder> matchNews) {
		this.matchNews = matchNews;
	}
	
	
}
