package edu.cuny.csi.csc330.discordbot.main;

public class DefensiveSkills {

	private Armor suit;
	private Armor shield;
	private Armor helmet;
	private int damgeTaken;

	public DefensiveSkills() {

	}

	public DefensiveSkills(Armor suit, Armor shield, Armor helmet, int damgeTaken) {
		this.suit = suit;
		this.shield = shield;
		this.helmet = helmet;
		this.damgeTaken = damgeTaken;
	}

	public Armor getSuit() {
		return suit;
	}

	public void setSuit(Armor suit) {
		this.suit = suit;
	}

	public Armor getShield() {
		return shield;
	}

	public void setShield(Armor shield) {
		this.shield = shield;
	}

	public Armor getHelmet() {
		return helmet;
	}

	public void setHelmet(Armor helmet) {
		this.helmet = helmet;
	}

	public int getDamgeTaken() {
		return damgeTaken;
	}

	public void setDamgeTaken(int damgeTaken) {
		this.damgeTaken = damgeTaken;
	}

}