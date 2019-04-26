package edu.cuny.csi.csc330.discordbot.main;

public class Player {

	private String name; //Player name
	private int hp; //Player health
	private int ap; //Player action points
	private int spd; //Player spd
	private int defPower; //Player's defensive stat
	private int offPower; //Player's offensive stat
	private Faction faction; //Faction the player is apart of

	public Player() {
		
	}
	
	public Player(String name) {

		this.name = name;
		this.defPower = 10;
		this.offPower = 10;
		generateFaction();
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDefPower() {
		return defPower;
	}

	public void setDefPower(int defPower) {
		this.defPower = defPower;
	}

	public int getOffPower() {
		return offPower;
	}

	public void setOffPower(int offPower) {
		this.offPower = offPower;
	}

	public Faction getFaction() {
		return faction;
	}

	public void setFaction(Faction faction) {
		this.faction = faction;
	}
	
	public void generateFaction() {
		
		int factionNum = Randomizer.generateInt(1, 3); //Generate a number from 1-3
		
		if(factionNum == 1) {
			
			this.faction = new Faction("Hawks"); //Player will be in Hawks
			
		} else if (factionNum == 2) {
			
			this.faction = new Faction("Owls"); //Player will be in Owls
			
		} else {
			
			this.faction = new Faction("Root"); //Player will be in Root
			
		}
		
	} //End of generateFaction
	
	
}
