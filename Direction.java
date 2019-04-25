package edu.cuny.csi.csc330.discordbot.main;
import edu.cuny.csi.csc330.discordbot.main.Randomizer;


public enum Direction {
	NONE, NORTH, EAST, SOUTH, WEST;

	// methods
	public Direction getFavorite() {
		return SOUTH; // It's getting cold! ...
	}

	public Direction getNextRandom() {
		 	
			int direction = Randomizer.generateInt(1, 8); 
		
			// 1 = south,  2 = west, 3 = north, 4 = east 
			if(direction == 1) { // south 
				 return SOUTH;
			}
			else if(direction == 2) {   // west 
				 return WEST; 
			}
			else if(direction == 3) {   // north 
				 return NORTH; 
			}
			else {    // east 
				return EAST; 
			} 
		
			
		}

	public static void main(String[] args) {

		int c = 0;
		while (c++ < 100) {
			System.out.println(Direction.NONE.getNextRandom());
		}

	}

}