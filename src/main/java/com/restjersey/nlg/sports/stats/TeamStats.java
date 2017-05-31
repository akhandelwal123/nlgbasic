package com.restjersey.nlg.sports.stats;

public class TeamStats {
	
	
	private boolean playedFirst;
	private String teamName;
	private String matchId;
	private String matchDate;
	private int totalScore;
	private int totalWickets;
	private int totalOvers;
	private String status;
	
	/*public void add(String team){
		this.teamNames.add(team);
	}
	
	public String getTeamName () {
		return this.teamNames.
	}*/
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
	public int getTotalscore() {
		return totalScore;
	}
	public void setTotalscore(int totalscore) {
		this.totalScore = totalscore;
	}
	public int getTotalwickets() {
		return totalWickets;
	}
	public void setTotalwickets(int totalwickets) {
		this.totalWickets = totalwickets;
	}
	public int getTotalOvers() {
		return totalOvers;
	}
	public void setTotalOvers(int totalOvers) {
		this.totalOvers = totalOvers;
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

}
