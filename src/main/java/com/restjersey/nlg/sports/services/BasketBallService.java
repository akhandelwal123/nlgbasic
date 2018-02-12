package com.restjersey.nlg.sports.services;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.restjersey.nlg.sports.constant.NLGSportsConstants;
import com.restjersey.nlg.sports.bask.stats.TeamStats;
import com.restjersey.nlg.sports.model.Game;
import com.restjersey.nlg.sports.model.Player;
import com.restjersey.nlg.sports.model.Team;

public class BasketBallService {

	
	private Game objGame;
	private Team objTeam;
	private Player objPlayer;
	private List<Game> games = new ArrayList<Game>();
	private List<Team> teams = new ArrayList<Team>();
	//private List<Player> players = new ArrayList<Player>();
	private Map<String , List<Player>> mapObjteamPlayers =  new HashMap<String, List<Player>>();
	
	private TeamStats teamstatsA = new TeamStats();
	private TeamStats teamstatsB = new TeamStats();
	TeamStats currentTeam;
	private static int count = 1;
	
	private Map <String, String> maxPoints = new HashMap<String , String>();
	
	
	private List<StringBuilder> objListStrBuilder = new ArrayList<>();
	private StringBuilder objStrBuilder = new StringBuilder();
	public List<Team> getTeams() {
		return teams;
	}


	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}


	public void add (Team team){
		teams.add(team);
	}

	public void add(Game game) {
		games.add(game);
	}

	
	//load all the configuration files except gameplayer.json
	public void loadAllJsonFiles() {
		ClassLoader classLoader = getClass().getClassLoader();
		JSONParser parser = new JSONParser();
		File file = new File(classLoader.getResource("game.json").getFile());
		//loading the gae data
		loadGame(parser, file);
		file = new File(classLoader.getResource("baskteam.json").getFile());
		//loading the team data
		loadTeam(parser, file);
		file = new File(classLoader.getResource("baskplayer.json").getFile());
		//loading the player info
		loadPlayer(parser, file);

	}


	
	/*load all the player for the particular team into the object and creates a mapping between
	team and players.*/
	private void loadPlayer(JSONParser parser, File file) {
		try {
			if (file != null && file.length() != 0) {
				Object obj = parser.parse(new FileReader(file));
				JSONObject jsonObject = (JSONObject) obj;
				JSONObject jsonObjectPlayer = (JSONObject) jsonObject.get("player");
				for (Team teamObj : teams) {
					JSONArray team =  (JSONArray) jsonObjectPlayer.get(teamObj.getId());
				 
					for (int i=0 ; i < team.size() ; i++) {
						JSONObject jsonObj = (JSONObject) team.get(i);
						String id = (String) jsonObj.get("pId");
			            String name = (String) jsonObj.get("name");
			            objPlayer = new Player();
			            objPlayer.setName(name);
			            objPlayer.setId(id);
			           // teamObj.getPlayers().add(objPlayer);
			            teamObj.addPlayers(objPlayer);
			            
			            //map of team vs list of the player (name and id only)
			            mapObjteamPlayers.put(teamObj.getId(),teamObj.getPlayers());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}


	//get the team details id and name and store as a object
	private void loadTeam(JSONParser parser, File file) {
		try {
			if (file != null && file.length() != 0) {
				Object obj = parser.parse(new FileReader(file));
				JSONObject jsonObject = (JSONObject) obj;
				JSONArray team =  (JSONArray) jsonObject.get("team");
				 
				for (int i=0 ; i < team.size() ; i++) {
					JSONObject jsonObj = (JSONObject) team.get(i);
					String id = (String) jsonObj.get("id");
		            String name = (String) jsonObj.get("name");
		            objTeam = new Team();
		            objTeam.setName(name);
		            objTeam.setId(id);
		            
		            //total number of teams participates (name & id)
		            teams.add(objTeam);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	//loading the game data
	private void loadGame(JSONParser parser, File file) {
		try {
			if (file != null && file.length() != 0) {
				Object obj = parser.parse(new FileReader(file));
				JSONObject jsonObject = (JSONObject) obj;
				JSONArray game =  (JSONArray) jsonObject.get("game");
				 
				for (int i=0 ; i < game.size() ; i++) {
					JSONObject jsonObj = (JSONObject) game.get(i);
					String id = (String) jsonObj.get("id");
		            String date = (String) jsonObj.get("date");
		            objGame = new Game();
		            objGame.setDate(date);
		            objGame.setId(id);
		            
		            //list of games played (id and date)
		            games.add(objGame);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// this is loaded separately as this is the details of the actual games being played.
	// using this one we have to generate the news.
	private JSONObject loadPlayerGame() {
		ClassLoader classLoader = getClass().getClassLoader();
		JSONParser parser = new JSONParser();
		File file = new File(classLoader.getResource("baskplayergame.json").getFile());
		try {
			if (file != null && file.length() != 0) {
				Object obj = parser.parse(new FileReader(file));
				JSONObject jsonObject = (JSONObject) obj;
				JSONObject jsonObjectPlayerGame = (JSONObject) jsonObject.get("player_game");
				return jsonObjectPlayerGame;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	// generating the facts using playergame.json
	public List<StringBuilder> generateFacts() {
		
		//getting the playergame object
		JSONObject jsonPlayerGameObj = loadPlayerGame();
		
		if(jsonPlayerGameObj != null) {
			
			//loading data for 1st game . This is hardcoded as of now . Need to make it dynamic.
			for (int k=1 ; k<=jsonPlayerGameObj.size() ;k++) {
			JSONObject jsonGame = (JSONObject) jsonPlayerGameObj.get("g"+k);
			teamstatsA.setMatchId("g"+k);
			teamstatsB.setMatchId("g"+k);
			teamstatsA.setMatchDate(games.get(k-1).getDate());
			teamstatsB.setMatchDate(games.get(k-1).getDate());
			//int count = 1;
			for (Team team : teams){
				if (count  == 2) {
					 currentTeam = teamstatsB;
					 setPointersAndThrows(jsonGame, team);
				}
				else if (count  == 1){
					currentTeam = teamstatsA;
					setPointersAndThrows(jsonGame, team);
				}
				
			}
			
			//generate dynamic language as News based on facts
			objListStrBuilder.add(generateNews());
			}
/*			if (mapOfSixes.containsKey("six")) {
				objStrBuilder.append(NLGSportsConstants.MAXSIXES.replace("PPP", mapOfSixes.get("six").split(",")[0]).replace("SSS", mapOfSixes.get("six").split(",")[1]));
			}*/
			return objListStrBuilder;
		}
		return null;
	}


	private void setPointersAndThrows(JSONObject jsonGame, Team team) {
		if (jsonGame.get(team.getId()) != null){
			//setting the team names
			currentTeam.setTeamName(team.getName());
			
			//set stats by Both teams
			setStatsBothTeams(team.getId(),jsonGame);
			
			//set who played first
			if (team.getId().equalsIgnoreCase((String) jsonGame.get("playedFirst"))) {
				currentTeam.setPlayedFirst(true);
			}
			
		}
	}


	private StringBuilder generateNews() {
		StringBuilder strNews = new StringBuilder();
		strNews.append(NLGSportsConstants.PLAYED.replace("teamA", teamstatsA.getTeamName()).replace("teamB", teamstatsB.getTeamName()).replace("date", teamstatsA.getMatchDate()));
		if (teamstatsA.getSuccessfreethrows() <= 0) {
		strNews.append(NLGSportsConstants.POINTS.replace("TTT", teamstatsA.getTeamName()).replace("PPP", String.valueOf(teamstatsA.getTwoPointers()*2 + teamstatsA.getThreepointers()*3 + teamstatsA.getSuccessfreethrows())).replace("TWO", String.valueOf(teamstatsA.getTwoPointers())).replace("THREE", String.valueOf(teamstatsA.getThreepointers())));
		}
		else {
			strNews.append(NLGSportsConstants.POINTSANDTHROWS.replace("TTT", teamstatsA.getTeamName()).replace("PPP", String.valueOf(teamstatsA.getTwoPointers()*2 + teamstatsA.getThreepointers()*3 + teamstatsA.getSuccessfreethrows())).replace("TWO", String.valueOf(teamstatsA.getTwoPointers())).replace("THREE", String.valueOf(teamstatsA.getThreepointers())).replace("XXX", String.valueOf(teamstatsA.getSuccessfreethrows())));	
		}
		if (teamstatsB.getSuccessfreethrows() <= 0) {
		strNews.append(NLGSportsConstants.POINTS.replace("TTT", teamstatsB.getTeamName()).replace("PPP", String.valueOf(teamstatsB.getTwoPointers()*2 + teamstatsB.getThreepointers()*3 + teamstatsB.getSuccessfreethrows())).replace("TWO", String.valueOf(teamstatsB.getTwoPointers())).replace("THREE", String.valueOf(teamstatsB.getThreepointers())));
		}
		else  {
			strNews.append(NLGSportsConstants.POINTSANDTHROWS.replace("TTT", teamstatsA.getTeamName()).replace("PPP", String.valueOf(teamstatsA.getTwoPointers()*2 + teamstatsA.getThreepointers()*3 + teamstatsA.getSuccessfreethrows())).replace("TWO", String.valueOf(teamstatsA.getTwoPointers())).replace("THREE", String.valueOf(teamstatsA.getThreepointers())).replace("XXX", String.valueOf(teamstatsA.getSuccessfreethrows())));
		}
		//setting the winning news by runs or wickets
		setWinningNews(strNews);
		
		if (maxPoints.containsKey("maxpoints")) {
			strNews.append(NLGSportsConstants.MAXPOINTS.replace("PPP", maxPoints.get("maxpoints").split(",")[0]).replace("SSS", maxPoints.get("maxpoints").split(",")[1]));
		}
		count =1;
		return strNews;
		
	}


	private void setWinningNews(StringBuilder strNews) {
			if (teamstatsA.getTotalscore() > teamstatsB.getTotalscore()) {
				strNews.append(NLGSportsConstants.WINNINPOINTS.replace("TTT", teamstatsA.getTeamName()).replace("AAA", String.valueOf(teamstatsA.getTotalscore()+"-"+teamstatsB.getTotalscore())));
			}else {
				strNews.append(NLGSportsConstants.WINNINPOINTS.replace("TTT", teamstatsB.getTeamName()).replace("AAA", String.valueOf(teamstatsA.getTotalscore()+"-"+teamstatsB.getTotalscore())));
			}
	
	}


	//setting the details(totalscore , totalwickets , totalovers ) of both the team for a particular match one after another.
	private void setStatsBothTeams(String teamId, JSONObject jsonGame) {
		JSONObject job =  (JSONObject) jsonGame.get(teamId);
		if (mapObjteamPlayers.get(teamId)!= null) {
			List<Player> players = mapObjteamPlayers.get(teamId);
			int twpointers = 0, threepointers = 0, totalFreeThrows = 0 , successFreeThrows = 0;
			for (int i=0; i<players.size() ; i++){
				if (job.get(players.get(i).getId())!= null){
					JSONObject jsonObjectPlayerDetails = (JSONObject)job.get(players.get(i).getId());
					twpointers = twpointers + getValue(jsonObjectPlayerDetails,"twopointers");
					threepointers = threepointers + getValue(jsonObjectPlayerDetails,"threepointers");
					totalFreeThrows = totalFreeThrows + (StringUtils.isAnyBlank((String) jsonObjectPlayerDetails.get("totalfreethrows")) ? 0 : Integer.valueOf((String)jsonObjectPlayerDetails.get("totalfreethrows")));
					successFreeThrows = successFreeThrows + getValue(jsonObjectPlayerDetails,"successfreethrows");
					
					
					//setting the maximum points by any player
					setMaxPointScored(jsonObjectPlayerDetails,"twopointers","threepointers","successfreethrows", players.get(i).getName());
			}
		}
			currentTeam.setTwoPinters(twpointers);
			currentTeam.setThreepointers(threepointers);
			currentTeam.setTotalfreethrows(totalFreeThrows);
			currentTeam.setSuccessfreethrows(successFreeThrows);
		}
		count = count+1;
	}


	//identify the player with the maximum sixes
	private void setMaxPointScored(JSONObject jsonObjectPlayerDetails,String two,String three, String one, String playerName) {
			if (maxPoints.containsKey("maxpoints")) {
				if (Integer.valueOf(maxPoints.get("maxpoints").split(",")[1]) < Integer.valueOf(getValue(jsonObjectPlayerDetails, one)+getValue(jsonObjectPlayerDetails, two)*2+getValue(jsonObjectPlayerDetails, three)*3)){
					int total = getValue(jsonObjectPlayerDetails, one)+getValue(jsonObjectPlayerDetails, two)*2+getValue(jsonObjectPlayerDetails, three)*3;
					maxPoints.put("maxpoints", playerName+","+ total);
				}
			}
			else {
				int total = getValue(jsonObjectPlayerDetails, one)+getValue(jsonObjectPlayerDetails, two)*2+getValue(jsonObjectPlayerDetails, three)*3;
				maxPoints.put("maxpoints", playerName+","+ total);
			}
	}
	
	private int getValue(JSONObject jsonObjectPlayerDetails, String key) {
		return StringUtils.isAnyBlank((String) jsonObjectPlayerDetails.get(key)) ? 0 : Integer.valueOf((String)jsonObjectPlayerDetails.get(key));
	}

}
