package edu.cuny.csi.csc330.discordbot.main;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.Iterator;

public class Game { // Almost everything goes here! The main Game Class

	// Constants
	private static int MAP_RANGE = 5; // Map range (Max Height/Width of the gameMap)
	private static int MAX_GAME_TURNS = 5; // Game ends after 5 turns
	private static int MAX_BATTLE_TURNS = 5;

	// Other private data members
	private Map<Tile, String> gameMap = new HashMap<Tile, String>(); // The Game Map to be navigated by player units
	private Set<Tile> gameSet = new HashSet<Tile>(); // Game Set for sorting
	private ArrayList<Player> playerList = new ArrayList<Player>(); // List of all players in the game. Player info
																	// stored here?
	private Queue<Long> playerQueue = new LinkedList<Long>(); // List of Discord IDs of players who join
	private Queue<Battle> battleQueue = new LinkedList<Battle>();
	private int turnCount;

	public Game() { // Game object has been created

		System.out.println("A new game has been created!");

	} // End of Game

	public Game(Queue<Long> playerQueue) { // Game object has been created

		System.out.println("A new game has been created!");

		this.playerQueue = playerQueue; // Set playerQueue equal to queue from BotMain

	} // End of Game (One argument constructor)

	public void init() throws InterruptedException {

		// Game object will run in 2 minutes! (Inherited from BotMain)?

	} // End of init

	public void populatePlayerList() {

		Iterator<Long> itr = playerQueue.iterator(); // We're going to iterate through queue of Player IDs

		while (itr.hasNext()) { // While the queue still has IDs

			Player newPlayer = new Player(itr.next()); // Create new player from ID in queue

			if (!playerList.contains(newPlayer)) { // Make sure Player is not already in list

				this.playerList.add(newPlayer); // Add player to Game's playerList

			} else { // Player already in the game

				System.out.println("Player " + itr.next() + " is already in-game."); // Prompt player input

			}

		} // End of Queue iteration

	} // End of populatePlayerList

	public void turnEnd() {

	} // End turnEnd

	public void runAllMapEvents() {

	} // End runAllMapEvents

	public void checkBattle() {

	} // End checkBattle

	public void runBattle() {

	} // End runBattle

	public void restoreAP() {

	} // End restoreAP

	public int getTurnCount() {

		return turnCount;

	} // End getTurnCount

	public void moveUnit() {

	} // End moveUnit

	public void generateMap() { // Generate Map Tiles!

		int i = 1;
		int j = 1;
		int z = 1;

		for (i = 1; i <= MAP_RANGE; i++) { // Our map has dimensions 5 x 5

			for (j = 1; j <= MAP_RANGE; j++) {

				Tile tempTile = new Tile(i, j); // Create a temporary Tile object

				this.gameMap.put(tempTile, "Unclaimed"); // Add Tile to map
				this.gameSet.add(tempTile); //Add Tile to set

			}
		}
		
	} // End generateMap

	public void printMap() { // Display the Map!
			
			List sortedKeys = new ArrayList(gameMap.keySet());
			Collections.sort(sortedKeys);
			
			sortedKeys.forEach(System.out::println);


	} // End printMap

	public static void main(String[] args) { // Main (will execute when class is called. Should generate all starting
												// instances of a new game.)

		Game testGame = new Game(); // Create a new test game

		testGame.generateMap(); // Generate the game map

		testGame.printMap(); // Display the game map

	} // End Main

}
