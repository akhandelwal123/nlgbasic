package com.restjersey.nlg.sports.bask.stats;

public class TeamStats {
	
	
	private boolean playedFirst;
	private String teamName;
	private String matchId;
	private String matchDate;
	private int twoPointers;
	private int threepointers;
	private int totalfreethrows;
	private int successfreethrows;
	
	public int getTwoPointers() {
		return twoPointers;
	}
	public void setTwoPinters(int twoPinters) {
		this.twoPointers = twoPinters;
	}
	public int getThreepointers() {
		return threepointers;
	}
	public void setThreepointers(int threepointers) {
		this.threepointers = threepointers;
	}
	public int getTotalfreethrows() {
		return totalfreethrows;
	}
	public void setTotalfreethrows(int totalfreethrows) {
		this.totalfreethrows = totalfreethrows;
	}
	public int getSuccessfreethrows() {
		return successfreethrows;
	}
	public void setSuccessfreethrows(int successfreethrows) {
		this.successfreethrows = successfreethrows;
	}
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
	
	public int getTotalscore() {
		
		return 2*twoPointers + 3*threepointers + successfreethrows;
	}

}
