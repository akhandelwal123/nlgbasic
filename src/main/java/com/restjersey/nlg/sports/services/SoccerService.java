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
import com.restjersey.nlg.sports.socc.stats.TeamStats;
import com.restjersey.nlg.sports.model.Game;
import com.restjersey.nlg.sports.model.SoccPlayer;
import com.restjersey.nlg.sports.model.Team;

public class SoccerService {

	
	private Game objGame;
	private Team objTeam;
	private SoccPlayer objPlayer;
	private List<Game> games = new ArrayList<Game>();
	private List<Team> teams = new ArrayList<Team>();
	//private List<Player> players = new ArrayList<Player>();
	private Map<String , List<SoccPlayer>> mapObjteamPlayers =  new HashMap<String, List<SoccPlayer>>();
	
	
	private TeamStats teamstatsA = new TeamStats();
	private TeamStats teamstatsB = new TeamStats();
	TeamStats currentTeam;
	private static int count = 1;
	
	private Map <String, String> maxGoals = new HashMap<String , String>();
	
	
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
		file = new File(classLoader.getResource("soccteam.json").getFile());
		//loading the team data
		loadTeam(parser, file);
		file = new File(classLoader.getResource("soccplayer.json").getFile());
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
			            objPlayer = new SoccPlayer();
			            objPlayer.setName(name);
			            objPlayer.setId(id);
			           // teamObj.getPlayers().add(objPlayer);
			            teamObj.addSoccPlayers(objPlayer);
			            
			            //map of team vs list of the player (name and id only)
			            mapObjteamPlayers.put(teamObj.getId(),teamObj.getSoccplayers());
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
		File file = new File(classLoader.getResource("soccplayergame.json").getFile());
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
					 setGoalsAndStrikes(jsonGame, team);
				}
				else if (count  == 1){
					currentTeam = teamstatsA;
					setGoalsAndStrikes(jsonGame, team);
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


	private void setGoalsAndStrikes(JSONObject jsonGame, Team team) {
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
		strNews.append(NLGSportsConstants.GOALSANDSHOTS.replace("TTT", teamstatsA.getTeamName()).replace("GGG", String.valueOf(teamstatsA.getTotalGoals())).replace("SSS", String.valueOf(teamstatsA.getTotalShoots())));	
		strNews.append(NLGSportsConstants.GOALSANDSHOTS.replace("TTT", teamstatsB.getTeamName()).replace("GGG", String.valueOf(teamstatsB.getTotalGoals())).replace("SSS", String.valueOf(teamstatsB.getTotalShoots())));
		//setting the winning news by runs or wickets
		setWinningNews(strNews);
		
		if (maxGoals.containsKey("maxgoals")) {
			strNews.append(NLGSportsConstants.MAXGOALS.replace("PPP", maxGoals.get("maxgoals").split(",")[0]).replace("SSS", maxGoals.get("maxgoals").split(",")[1]));
		}
		count =1;
		return strNews;
		
	}


	private void setWinningNews(StringBuilder strNews) {
			if (teamstatsA.getTotalGoals()> teamstatsB.getTotalGoals()) {
				strNews.append(NLGSportsConstants.WINNINGGOALS.replace("TTT", teamstatsA.getTeamName()).replace("GGG", String.valueOf(teamstatsA.getTotalGoals()+"-"+teamstatsB.getTotalGoals())));
			}else if (teamstatsA.getTotalGoals() < teamstatsB.getTotalGoals()){
				strNews.append(NLGSportsConstants.WINNINGGOALS.replace("TTT", teamstatsB.getTeamName()).replace("GGG", String.valueOf(teamstatsB.getTotalGoals()+"-"+teamstatsA.getTotalGoals())));
			}else {
				strNews.append(NLGSportsConstants.SOCCTIE.replace("AAA", teamstatsA.getTeamName()).replace("BBB", teamstatsB.getTeamName()));
			}
	
	}


	//setting the details(totalshoots , totalgoals ) of both the team for a particular match one after another.
	private void setStatsBothTeams(String teamId, JSONObject jsonGame) {
		JSONObject job =  (JSONObject) jsonGame.get(teamId);
		if (mapObjteamPlayers.get(teamId)!= null) {
			List<SoccPlayer> players = mapObjteamPlayers.get(teamId);
			int totalShoots = 0, totalGoals = 0;
			for (int i=0; i<players.size() ; i++){
				if (job.get(players.get(i).getId())!= null){
					JSONObject jsonObjectPlayerDetails = (JSONObject)job.get(players.get(i).getId());
					totalShoots = totalShoots + getValue(jsonObjectPlayerDetails,"shoots");
					totalGoals = totalGoals + getValue(jsonObjectPlayerDetails,"goals");
					
					//setMax Goals
					setMaxGoalsScored(jsonObjectPlayerDetails,"goals", players.get(i).getName());
					setGoalKeeper(jsonObjectPlayerDetails,"goalkeeper",players.get(i));

			}
		}
			currentTeam.setTotalShoots(totalShoots);
			currentTeam.setTotalGoals(totalGoals);
			
				
		}
		count = count+1;
	}


	private void setGoalKeeper(JSONObject jsonObjectPlayerDetails, String goalkeeper, SoccPlayer soccPlayer) {
		if (jsonObjectPlayerDetails.get(goalkeeper).equals("Y")){
			currentTeam.setGoalKeeper(soccPlayer.getName());
		}
		
	}


	//identify the player with the maximum goals
	private void setMaxGoalsScored(JSONObject jsonObjectPlayerDetails,String goal,String playerName) {
			if (maxGoals.containsKey("maxgoals")) {
				if (Integer.valueOf(maxGoals.get("maxgoals").split(",")[1]) < Integer.valueOf(getValue(jsonObjectPlayerDetails, goal))){
					int total = getValue(jsonObjectPlayerDetails, goal);
					maxGoals.put("maxgoals", playerName+","+ total);
				}
			}
			else {
				int total = getValue(jsonObjectPlayerDetails, goal);
				maxGoals.put("maxgoals", playerName+","+ total);
			}
	}
	
	private int getValue(JSONObject jsonObjectPlayerDetails, String key) {
		return StringUtils.isAnyBlank((String) jsonObjectPlayerDetails.get(key)) ? 0 : Integer.valueOf((String)jsonObjectPlayerDetails.get(key));
	}

}
