package edu.cuny.csi.csc330.discordbot.main;

public class Skills {

	private String name;
	private String skillType;
	private int damage;
	private int expRequired;

	public Skills() {

	}

	public Skills(String name, String skillType, int damage, int expRequired) {
		this.name = name;
		this.skillType = skillType;
		this.damage = damage;
		this.expRequired = expRequired;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSkillType() {
		return skillType;
	}

	public void setSkillType(String skillType) {
		this.skillType = skillType;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getExpRequired() {
		return expRequired;
	}

	public void setExpRequired(int expRequired) {
		this.expRequired = expRequired;
	}

	public void initializeSkill() {
		
		new skill = newSkill;
		numSkills++;
		
		
	}

}
