package edu.cuny.csi.csc330.discordbot.bot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Tile implements Comparable<Tile> {

	private int position1; // Position 1 of tile
	private int position2; // Position 2 of tile
	private String faction; // Current ruling faction (Will always be Unclaimed by default)
	private ArrayList<Unit> unitList = new ArrayList<Unit>(); // List of all units currently on tile

	public Tile() {
		this.faction = "Unclaimed";
	}

	// Constructor
	public Tile(int position1, int position2) {
		super();
		this.position1 = position1;
		this.position2 = position2;
		this.faction = "Unclaimed";
	}

	// toString
	@Override
	public String toString() {
		return "Tile: " + position1 + "-" + position2 + "  Faction: " + faction;
	}

	// Getters/Setters
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

	public String getFaction() {
		return faction;
	}

	protected void setFaction(String faction) {
		this.faction = faction;
	}

	public ArrayList<Unit> getUnitList() {
		return unitList;
	}

	protected void setUnitList(ArrayList<Unit> unitList) {
		this.unitList = unitList;
	}

	protected void removeUnit(Unit unit) {
		this.unitList.remove(unit);
	}

	protected void addUnit(Unit unit) {
		this.unitList.add(unit);
	}

	// hashCode and equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((faction == null) ? 0 : faction.hashCode());
		result = prime * result + position1;
		result = prime * result + position2;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (position1 != other.position1)
			return false;
		if (position2 != other.position2)
			return false;
		return true;
	}

	public int compareTo(Tile anotherTile) {

		if ((this.position1 == anotherTile.position1) && (this.position2 == anotherTile.position2)) { // Equals

			return 0;

		} else if ((this.position1 < anotherTile.position1)
				|| (this.position1 == anotherTile.position1 && this.position2 < anotherTile.position2)) { // Less than

			return -1;

		} else { // Greater than

			return 1;

		}

	}
}
