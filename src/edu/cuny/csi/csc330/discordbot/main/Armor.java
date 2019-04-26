package edu.cuny.csi.csc330.discordbot.main;

public class Armor {

	private String armorName; //Name that will identify the weapon
	private int defensePower; //Add to player's base stat as an armor buff
	
	public Armor() { //Player does not have a weapon by default
		
		this.armorName = "none";
		
	}
	
	public Armor(String name) { //If a weapon is specified
		
		this.armorName = name;
		
	}
	
	public void setDefensePower() {
		
		if (this.armorName == "none") {
			
			this.defensePower = 0;
			
		} else {
			
			this.defensePower = 10;
			
		}
		
	}

}
