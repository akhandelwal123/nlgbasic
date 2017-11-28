package com.restjersey.nlg.sports.model;

public class SoccPlayer {
	
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


	private String shoots;
	private String goals;
	private String goalKeeper;
	public String getShoots() {
		return shoots;
	}


	public void setShoots(String shoots) {
		this.shoots = shoots;
	}


	public String getGoals() {
		return goals;
	}


	public void setGoals(String goals) {
		this.goals = goals;
	}


	public String getGoalKeeper() {
		return goalKeeper;
	}


	public void setGoalKeeper(String goalKeeper) {
		this.goalKeeper = goalKeeper;
	}

	
	public boolean hasGoalkeeper(String name) {
		if (getGoalKeeper().equalsIgnoreCase("Y")){
			return true;
		}
		return false;
	}
	

}
