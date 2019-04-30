package edu.cuny.csi.csc330.discordbot.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Game { // Almost everything goes here! The main Game Class

	//Constants
	private static int MAP_RANGE = 5; //Map range (Max Height/Width of the gameMap)
	private static int MAX_GAME_TURNS = 5; //Game ends after 5 turns
	
	//Other private data members
	private Map<Tile, String> gameMap = new HashMap<Tile, String>(); //The Game Map to be navigated by player units
	private ArrayList<Player> playerList = new ArrayList<Player>(); //List of all players in the game. Player info stored here?
	private Queue<Long> playerQueue = new LinkedList<Long>(); //List of Discord IDs of players who join
	private Queue<Battle> battleQueue = new LinkedList<Battle>();
	private int turnCount;

	
	public Game() { //!init
		
		System.out.println("A new game has been created!");
		
		
	}
	
	public void init() {
		
		
		
	}
	
	public void playerJoin(Long ID) { //!join
		
		String playerName; //Name player will be addressed by in-game
		
		System.out.println("Player " + ID + " has joined the game!");
		
		this.playerQueue.add(ID); //Add the player's Discord ID to the queue
		
		System.out.println("Hello Player " + ID + "! What would you like to be called?"); //Prompt player input
		
		playerName = System.console().readLine(); //Read in the Player's name
		
		if(playerName != null && !playerName.isEmpty()) {
		
		Player newPlayer = new Player(playerName, ID); //Create the new Player object
		this.playerList.add(newPlayer); //Add player to Game's playerList
		
		} else { //Player's name not entered, will create a default name instead
			
		Player newPlayer = new Player(ID); //Create the new Player object (Name will appear as "Player " + ID)
		this.playerList.add(newPlayer); //Add player to Game's playerList
			
		}
		
		
	}
	
	// Generate map tiles
	public void generateMap() {

		int i = 1;
		int j = 1;

		for (i = 1; i <= MAP_RANGE; i++) { // Our map has dimensions 5 x 5

			for (j = 1; i <= MAP_RANGE; j++) {

				Tile tempTile = new Tile(i, j); // Create a temporary Tile object

				this.gameMap.put(tempTile, "Unclaimed"); // Add Tile to map

			}
		}

	} // End generateMap

	public static void main(String[] args) { // Main (will execute when class is called. Should generate all starting
												// instances of a new game.)
		
		
		
		
		
		
		
	} // End Main

}
