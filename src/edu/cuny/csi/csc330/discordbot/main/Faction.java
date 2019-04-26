package edu.cuny.csi.csc330.discordbot.main;

public class Faction {

	private String factionName;
	
	public Faction() {
		// TODO Auto-generated constructor stub
	}
	
	public Faction(String name) {
		this.factionName = name;
	}

	public String getFactionName() {
		return factionName;
	}

	public void setFactionName(String factionName) {
		this.factionName = factionName;
	}
	
}
