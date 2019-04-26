package edu.cuny.csi.csc330.discordbot.main;

import java.util.HashMap;
import java.util.Map;

public class Game { // Called when a new game is started

	private Map<Tile, String> gameMap = new HashMap<Tile, String>();

	// Need to get a list of every player currently in the server

	// Generate CSV file with basic player info

	// Generate map tiles
	public void generateMap() {

		int i = 1;
		int j = 1;

		for (i = 1; i <= 8; i++) { // Our map has dimensions 8 x 8

			for (j = 1; i <= 8; j++) {

				Tile tempTile = new Tile(i, j); // Create a temporary Tile object

				this.gameMap.put(tempTile, "Unclaimed"); // Add Tile to map

			}
		}

	} // End generateMap

	public Game() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) { // Main (will execute when class is called. Should generate all starting
												// instances of a new game.)
		
		Player player = new Player("Billy Bob");
		
		

	} // End Main

}
