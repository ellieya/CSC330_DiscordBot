package edu.cuny.csi.csc330.discordbot.main;

import java.util.Set;
import java.util.TreeSet;

public class Tile {

	private int position1; //Position 1 of tile 
	private int position2; //Position 2 of tile
	private String faction; //Current ruling faction (Will always be Unclaimed by default)
	//List of players that are currently on the tile
	
	public Tile() {
		this.faction = "Unclaimed";
	}
	
	//Constructor
	public Tile(int position1, int position2) {
		super();
		this.position1 = position1;
		this.position2 = position2;
		this.faction = "Unclaimed";
	}
	
	
	//toString
	@Override
	public String toString() {
		return "Tile [position1=" + position1 + ", position2=" + position2 + ", faction=" + faction + "]";
	}

	//Getters/Setters
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
	public String getFaction() {
		return faction;	
	}	
	public void setFaction(String faction) {
		this.faction = faction;
	}
	
	//hashCode and equals
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
		if (faction == null) {
			if (other.faction != null)
				return false;
		} else if (!faction.equals(other.faction))
			return false;
		if (position1 != other.position1)
			return false;
		if (position2 != other.position2)
			return false;
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Implement a Testing main()   !!!!!!!!!!!!!!!!
		
		// Complete the methods implied through comments above 
		// Then uncomment the test code below, and run. 
		
		
		Tile corner1 = new Tile(6, 23); 
		Tile corner2 = new Tile(7, 41); 
		Tile corner3 = new Tile(1, 3); 
		Tile corner4 = new Tile(3, 4); 
		Tile corner5 = new Tile(7, 34); 
		
		System.out.println("Corner 1: " + corner1); 
		System.out.println("Corner 2: " + corner2); 
		
		if(corner1.equals(corner2 ) == false ) 
				System.out.println("Corners are not equal."); 
		
		
		// Test Sorting ... for lab5 req 
		Set<Tile> set = new TreeSet<Tile>(); 
		set.add(corner1);
		set.add(corner2);
		set.add(corner3);
		set.add(corner4);
		set.add(corner5);
		
		System.out.println(set);
		
		System.exit(0);
		 
		 

	}

}
