package edu.cuny.csi.csc330.discordbot.main;

public class HealthSkills {
	private potion healthPotion;
	private potion increaseAttack;
	private potion superHealthPotion;
	private int hpPoints;

	public HealthSkills() {

	}

	public HealthSkills(potion healthPotion, potion increaseAttack, potion superHealthPotion, int hpPoints) {
		this.healthPotion = healthPotion;
		this.increaseAttack = increaseAttack;
		this.superHealthPotion = superHealthPotion;
		this.hpPoints = hpPoints;
	}

	public potion getHealthPotion() {
		return healthPotion;
	}

	public void setHealthPotion(potion healthPotion) {
		this.healthPotion = healthPotion;
	}

	public potion getIncreaseAttack() {
		return increaseAttack;
	}

	public void setIncreaseAttack(potion increaseAttack) {
		this.increaseAttack = increaseAttack;
	}

	public potion getSuperHealthPotion() {
		return superHealthPotion;
	}

	public void setSuperHealthPotion(potion superHealthPotion) {
		this.superHealthPotion = superHealthPotion;
	}

	public int getHpPoints() {
		return hpPoints;
	}

	public void setHpPoints(int hpPoints) {
		this.hpPoints = hpPoints;
	}

	public void useHealthPotion() {
		hpPoints += healthPotionPower;
		numHealthPotions--;

	}

	public void useIncreaseAttack() {
		attackPower += IncreaseAttackPower;
		numIncreaseAttackPotions--;

	}

	public void useSuperHealthPotion() {
		hpPoints += superHealthPotionPower;
		numSuperHealthPotions--;

	}

}
