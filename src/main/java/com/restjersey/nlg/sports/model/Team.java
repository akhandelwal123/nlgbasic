package com.restjersey.nlg.sports.model;

import java.util.ArrayList;
import java.util.List;

public class Team {
	
	private String id;
	private String name;
	private List<Player> players = new ArrayList<Player>();
	
	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	public void addPlayers(Player player) {
		this.players.add(player);
	}
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

}
