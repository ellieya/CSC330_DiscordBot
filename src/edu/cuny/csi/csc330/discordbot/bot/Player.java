package edu.cuny.csi.csc330.discordbot.bot;

import java.util.ArrayList;

public class Player {

	private String name; //Player name
	private Long ID; //Player ID
	private int ap; //Player action points
	private String faction; //Faction the player is apart of
	private int factionID; //Faction ID
	protected ArrayList<Unit> party = new ArrayList<Unit>(); //Party of units 
	private boolean inBattle; //Is the Player in battle?

	public Player() {
		
	}
	
	public Player(Long ID) {

		this.name = "Player " + ID;
		this.ID = ID;
		Unit unit1 = new Unit(faction, factionID, ID);
		Unit unit2 = new Unit(faction, factionID, ID);
		this.party.add(unit1);
		this.party.get(0).setName(party.size());
		this.party.add(unit2);
		this.party.get(1).setName(party.size());
		this.ap = 3;
		this.inBattle = false;
		
	}
	
	public Player(String name, Long ID) {

		this.name = name;
		this.ID = ID;
		generateFaction();
		Unit unit1 = new Unit(faction, factionID, ID);
		Unit unit2 = new Unit(faction, factionID, ID);
		this.party.add(unit1);
		this.party.add(unit2);
		this.ap = 3;
		this.inBattle = false;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFaction() {
		return faction;
	}

	public void setFaction(String faction) {
		this.faction = faction;
	}
	
	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public int getAp() {
		return ap;
	}

	public void setAp(int ap) {
		this.ap = ap;
	}

	public int getFactionID() {
		return factionID;
	}

	public void setFactionID(int factionID) {
		this.factionID = factionID;
	}

	public ArrayList<Unit> getParty() {
		return party;
	}

	public void setParty(ArrayList<Unit> party) {
		this.party = party;
	}

	public boolean isInBattle() {
		return inBattle;
	}

	public void setInBattle(boolean inBattle) {
		this.inBattle = inBattle;
	}

	public String generateFaction() {
		
		int factionNum = Randomizer.generateInt(1, 3); //Generate a number from 1-3
		
		if(factionNum == 1) {
			
			this.faction = "Hawks"; //Player will be in Hawks
			this.factionID = 1; //Set ID
			
		} else if (factionNum == 2) {
			
			this.faction = "Owls"; //Player will be in Owls
			this.factionID = 2; //Set ID
			
		} else {
			
			this.faction = "Root"; //Player will be in Root
			this.factionID = 3; //Set ID
			
		}
		
		return this.faction;
		
	} //End of generateFaction
	
	
}
