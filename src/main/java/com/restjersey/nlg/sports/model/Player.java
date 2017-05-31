package com.restjersey.nlg.sports.model;

public class Player {
	
	private String id;
	private String name;
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	private String score;
	private String wickets;
	private String over;
	private String runout;
	private String sixes;
	
	
	public String getScore() {
		return score;
	}


	public void setScore(String score) {
		this.score = score;
	}


	public String getWickets() {
		return wickets;
	}


	public void setWickets(String wickets) {
		this.wickets = wickets;
	}


	public String getOver() {
		return over;
	}


	public void setOver(String over) {
		this.over = over;
	}


	public String getRunout() {
		return runout;
	}


	public void setRunout(String runout) {
		this.runout = runout;
	}


	public String getSixes() {
		return sixes;
	}


	public void setSixes(String sixes) {
		this.sixes = sixes;
	}


	

}
