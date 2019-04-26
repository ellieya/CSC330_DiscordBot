package edu.cuny.csi.csc330.discordbot.main;

public class OffensiveSkills {

	Weapon sword;
	Weapon fire;
	Weapon chain;

	public OffensiveSkills() {

	}

	public OffensiveSkills(Weapon sword, Weapon fire, Weapon chain) {
		this.sword = sword;
		this.fire = fire;
		this.chain = chain;
	}

	public Weapon getSword() {
		return sword;
	}

	public void setSword(Weapon sword) {
		this.sword = sword;
	}

	public Weapon getFire() {
		return fire;
	}

	public void setFire(Weapon fire) {
		this.fire = fire;
	}

	public Weapon getChain() {
		return chain;
	}

	public void setChain(Weapon chain) {
		this.chain = chain;
	}

	public void swingSword() {

	}

	public void throwFire() {

	}

	public void swingChain() {

	}

}
