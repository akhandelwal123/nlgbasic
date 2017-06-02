package com.restjersey.nlg.sports;

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

import com.restjersey.nlg.sports.model.Game;
import com.restjersey.nlg.sports.model.Player;
import com.restjersey.nlg.sports.model.Team;
import com.restjersey.nlg.sports.stats.TeamStats;

public class SportsService {

	
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
	
	private Map <String, String> mapOfSixes = new HashMap<String , String>();
	
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
		file = new File(classLoader.getResource("team.json").getFile());
		//loading the team data
		loadTeam(parser, file);
		file = new File(classLoader.getResource("player.json").getFile());
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
		File file = new File(classLoader.getResource("playergame.json").getFile());
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
	public StringBuilder generateFacts() {
		
		//getting the playergame object
		JSONObject jsonPlayerGameObj = loadPlayerGame();
		
		if(jsonPlayerGameObj != null) {
			
			//loading data for 1st game . This is hardcoded as of now . Need to make it dynamic.
			JSONObject jsonGame = (JSONObject) jsonPlayerGameObj.get("g1");
			teamstatsA.setMatchId("g1");
			teamstatsB.setMatchId("g1");
			teamstatsA.setMatchDate(games.get(0).getDate());
			teamstatsB.setMatchDate(games.get(0).getDate());
			//int count = 1;
			for (Team team : teams){
				if (count  == 2) {
					 currentTeam = teamstatsB;
					 setOverWicketRuns(jsonGame, team);
				}
				else if (count  == 1){
					currentTeam = teamstatsA;
					setOverWicketRuns(jsonGame, team);
				}
				
			}
			
			//generate dynamic language as News based on facts
			return generateNews();
		}
		return null;
	}


	private void setOverWicketRuns(JSONObject jsonGame, Team team) {
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
		//strNews.append(NLGSportsConstants.SUMMARY);
		strNews.append(NLGSportsConstants.PLAYED.replace("teamA", teamstatsA.getTeamName()).replace("teamB", teamstatsB.getTeamName()).replace("date", teamstatsA.getMatchDate()));
		strNews.append(NLGSportsConstants.SCORE.replace("TTT", teamstatsA.getTeamName()).replace("RRR", String.valueOf(teamstatsA.getTotalscore())).replace("WWW", String.valueOf(teamstatsB.getTotalwickets())).replace("OOO", String.valueOf(teamstatsB.getTotalOvers())));
		strNews.append(NLGSportsConstants.SCORE.replace("TTT", teamstatsB.getTeamName()).replace("RRR", String.valueOf(teamstatsB.getTotalscore())).replace("WWW", String.valueOf(teamstatsA.getTotalwickets())).replace("OOO", String.valueOf(teamstatsA.getTotalOvers())));
		
		//setting the winning news by runs or wickets
		setWinningNews(strNews);
		
		if (mapOfSixes.containsKey("six")) {
			strNews.append(NLGSportsConstants.MAXSIXES.replace("PPP", mapOfSixes.get("six").split(",")[0]).replace("SSS", mapOfSixes.get("six").split(",")[1]));
		}
		count =1;
		return strNews;
		
	}


	private void setWinningNews(StringBuilder strNews) {
		if ( teamstatsA.isPlayedFirst() == true) {
			if (teamstatsA.getTotalscore() > teamstatsB.getTotalscore()) {
				strNews.append(NLGSportsConstants.WINNINGRUNS.replace("TTT", teamstatsA.getTeamName()).replace("RRR", String.valueOf(teamstatsA.getTotalscore()-teamstatsB.getTotalscore())));
			}else {
				strNews.append(NLGSportsConstants.WINNINGWICKETS.replace("TTT", teamstatsB.getTeamName()).replace("WWW", String.valueOf(11-teamstatsB.getTotalwickets())));
			}
		}
		else if (teamstatsB.getTotalscore() > teamstatsA.getTotalscore()) {
			strNews.append(NLGSportsConstants.WINNINGRUNS.replace("TTT", teamstatsB.getTeamName()).replace("RRR", String.valueOf(teamstatsB.getTotalscore()-teamstatsA.getTotalscore())));
		}else {
			strNews.append(NLGSportsConstants.WINNINGWICKETS.replace("TTT", teamstatsA.getTeamName()).replace("WWW", String.valueOf(11-teamstatsA.getTotalwickets())));
		}
	}


	//setting the details(totalscore , totalwickets , totalovers ) of both the team for a particular match one after another.
	private void setStatsBothTeams(String teamId, JSONObject jsonGame) {
		JSONObject job =  (JSONObject) jsonGame.get(teamId);
		if (mapObjteamPlayers.get(teamId)!= null) {
			List<Player> players = mapObjteamPlayers.get(teamId);
			int totalscore = 0, totalwickets = 0, totalovers = 0;
			for (int i=0; i<players.size() ; i++){
				if (job.get(players.get(i).getId())!= null){
					JSONObject jsonObjectPlayerDetails = (JSONObject)job.get(players.get(i).getId());
					totalscore = totalscore + (jsonObjectPlayerDetails.get("score").toString().trim().equals("null") ? 0 : Integer.valueOf((String)jsonObjectPlayerDetails.get("score")));
					totalwickets = totalwickets + (StringUtils.isAnyBlank((String) jsonObjectPlayerDetails.get("wickets")) ? 0 : Integer.valueOf((String)jsonObjectPlayerDetails.get("wickets")));
					totalovers = totalovers + (StringUtils.isAnyBlank((String) jsonObjectPlayerDetails.get("overs")) ? 0 : Integer.valueOf((String)jsonObjectPlayerDetails.get("overs")));
					
					//setting the maximum sixes
					setMaxSIxes((String)jsonObjectPlayerDetails.get("sixes") , players.get(i).getName());
			}
		}
			currentTeam.setTotalscore(totalscore);
			currentTeam.setTotalwickets(totalwickets);
			currentTeam.setTotalOvers(totalovers);
		}
		count = count+1;
	}


	//identify the player with the maximum sixes
	private void setMaxSIxes(String object, String playerName) {
		if (StringUtils.isNotBlank(object)) {
			if (mapOfSixes.containsKey("six")) {
				if (Integer.valueOf(mapOfSixes.get("six").split(",")[1]) < Integer.valueOf(object.toString())){
					mapOfSixes.put("six", playerName+","+object.toString());
				}
			}
			else {
				mapOfSixes.put("six", playerName+","+object.toString());
			}
		}
	}

}
