package edu.cuny.csi.csc330.discordbot.main;

public class Weapon {

	private String weaponName; //Name that will identify the weapon
	private int attackPower; //Add to player's base stat as a weapons buff
	
	public Weapon() { //Player does not have a weapon by default
		
		this.weaponName = "none";
		
	}
	
	public Weapon(String name) { //If a weapon is specified
		
		this.weaponName = name;
		
	}
	
	public void setAttackPower() {
		
		if (this.weaponName == "none") {
			
			this.attackPower = 0;
			
		} else {
			
			this.attackPower = 10;
			
		}
		
	}

}
