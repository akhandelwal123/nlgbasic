package com.restjersey.nlg.sports.socc.stats;

public class TeamStats {
	
	
	private boolean playedFirst;
	private String teamName;
	private String matchId;
	private String matchDate;
	private int totalShoots;
	private String goalKeeper;
	public int getTotalShoots() {
		return totalShoots;
	}
	public void setTotalShoots(int totalShoots) {
		this.totalShoots = totalShoots;
	}
	public int getTotalGoals() {
		return totalGoals;
	}
	public void setTotalGoals(int totalGoals) {
		this.totalGoals = totalGoals;
	}
	private int totalGoals;
	private String status;

	public String getMatchId() {
		return matchId;
	}
	public void setMatchId(String id) {
		this.matchId = id;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMatchDate() {
		return matchDate;
	}
	public void setMatchDate(String matchDate) {
		this.matchDate = matchDate;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public boolean isPlayedFirst() {
		return playedFirst;
	}
	public void setPlayedFirst(boolean playedFirst) {
		this.playedFirst = playedFirst;
	}
	public void setGoalKeeper(String goalkeeper) {
		this.goalKeeper=goalkeeper;
		
	}
	public String getGoalKeeper() {
		return goalKeeper;
	}
	


}
