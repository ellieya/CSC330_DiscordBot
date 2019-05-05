package edu.cuny.csi.csc330.discordbot.bot;

public class MapTileEvents {

	public MapTileEvents() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * Returns updated HP value
	 */
	public static int restEvent(Unit someUnit) {
		
		int unitHp = someUnit.getCurHP(); // Get Unit's current HP
		int hpGain = (int) Math.floor(someUnit.getHp() / 2); // Calculate HP gain
		
		return unitHp + hpGain; // Return HP: current + gain
		
		}

}
