package edu.cuny.csi.csc330.discordbot.bot;

public class Unit {

	private int name; // Unit name/ID
	private String faction; // Faction the unit is apart of
	private int factionID; // Unit's faction ID
	private int position1; // Position 1 of the unit
	private int position2; // Position 2 of the unit
	private Long playerID; // Identifies what player the unit belongs to

	public Unit(String faction, int factionID, Long playerID) {

		this.faction = faction;
		this.factionID = factionID;
		this.playerID = playerID;
		generateCodeName();

		if (factionID == 1) { // Hawks start at (0,0)

			this.position1 = 1;
			this.position2 = 1;

		} else if (factionID == 2) { // Owls start at (5,5)

			this.position1 = 5;
			this.position2 = 5;

		} else if (factionID == 3) { // Root starts at (1,5)

			this.position1 = 1;
			this.position2 = 5;

		} else { // Default

			this.position1 = 1;
			this.position2 = 1;

		}

	}

	public int getName() {
		return name;
	}

	protected void setName(int name) {
		this.name = name;
	}

	public String getFaction() {
		return faction;
	}

	protected void setFaction(String faction) {
		this.faction = faction;
	}

	public int getFactionID() {
		return factionID;
	}

	protected void setFactionID(int factionID) {
		this.factionID = factionID;
	}

	public int getPosition1() {
		return position1;
	}

	protected void setPosition1(int position1) {
		this.position1 = position1;
	}

	public int getPosition2() {
		return position2;
	}

	protected void setPosition2(int position2) {
		this.position2 = position2;
	}

	public Long getPlayerID() {
		return playerID;
	}

	protected void setPlayerID(Long playerID) {
		this.playerID = playerID;
	}

	/**
	 * 
	 * Randomly generate a number or code name to identify a unit
	 * 
	 */
	public void generateCodeName() { // Generate code name for unit

		int nameGen = Randomizer.generateInt(1000, 9999); // Generate a number from 1000-9999
		this.name = nameGen; // Set Unit name/ID

	}

}
