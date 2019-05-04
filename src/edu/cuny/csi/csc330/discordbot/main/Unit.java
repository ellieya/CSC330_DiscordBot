package edu.cuny.csi.csc330.discordbot.main;

public class Unit {

	private int name; //Unit name/ID
	private int hp; //Unit health
	private int spd; //Unit spd
	private int def; //Unit's defensive stat
	private int atk; //Unit's offensive stat
	private int maxHP; //Unit's maxHP
	private int curHP; //Unit's curHP
	private String faction; //Faction the unit is apart of
	private int factionID; //Unit's faction ID
	private int position1; //Position 1 of the unit
	private int position2; //Position 2 of the unit

	public Unit(String faction, int factionID) {
		
		this.faction = faction;
		this.factionID = factionID;
		generateStats();
		
		if (factionID == 1) { //Hawks start at (0,0)
			
			position1 = 1;
			position2 = 1;
			
		} else if(factionID == 2) { //Owls start at (5,5)
			
			position1 = 5;
			position2 = 5;
			
		} else if (factionID == 3) { //Root starts at (1,5)
			
			position1 = 1;
			position2 = 5;
			
		} else { //Default
			
			position1 = 1;
			position2 = 1;
			
		}
		
	}
	
	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public int getDef() {
		return def;
	}

	public void setDef(int defPower) {
		this.def = defPower;
	}

	public int getAtk() {
		return atk;
	}

	public void setAtk(int offPower) {
		this.atk = offPower;
	}

	public String getFaction() {
		return faction;
	}

	public void setFaction(String faction) {
		this.faction = faction;
	}
	
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getSpd() {
		return spd;
	}

	public void setSpd(int spd) {
		this.spd = spd;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	public int getCurHP() {
		return curHP;
	}

	public void setCurHP(int curHP) {
		this.curHP = curHP;
	}

	public int getFactionID() {
		return factionID;
	}

	public void setFactionID(int factionID) {
		this.factionID = factionID;
	}

	public int getPosition1() {
		return position1;
	}

	public void setPosition1(int position1) {
		this.position1 = position1;
	}

	public int getPosition2() {
		return position2;
	}

	public void setPosition2(int position2) {
		this.position2 = position2;
	}

	public void generateStats() { //Generate stats for unit and set them
		
		int atkGen = Randomizer.generateInt(5, 15); //Generate a number from 5-15
		this.atk = atkGen; //Set attack stat
		
		int defGen = Randomizer.generateInt(5, 15); //Generate a number from 5-15
		this.def = defGen; //Set defense stat
		
		int hpGen = Randomizer.generateInt(5, 15); //Generate a number from 5-15
		this.hp = hpGen; //Set HP stat
		
		this.maxHP = hp; //Same as hp initially
		this.curHP = hp; //Same as hp initially
		
		int spdGen = Randomizer.generateInt(5, 15); //Generate a number from 5-15
		this.spd = spdGen; //Set speed stat
		
		int nameGen = Randomizer.generateInt(1000, 9999); //Generate a number from 1000-9999
		this.name = nameGen; //Set Unit name/ID
		
	}
	
}
