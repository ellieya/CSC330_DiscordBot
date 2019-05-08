package edu.cuny.csi.csc330.discordbot.bot;

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
	private static int MAX_MAP_RANGE = 5; // Map range (Max Height/Width of the gameMap)
	private static int MAX_GAME_TURNS = 5; // Game ends after 5 turns
	private static int MAX_AP = 3; // Max action points for a user
	private static int MAX_PARTY = 2; // Max party members per player

	// Other private data members
	protected Map<Coordinate, Tile> gameMap = new HashMap<Coordinate, Tile>(); // The Game Map to be navigated by player
																				// units
	protected Map<Long, Player> playerMap = new HashMap<Long, Player>(); // Map of all players in the game (For
																			// searching)
	private ArrayList<Player> playerList = new ArrayList<Player>(); // List of all players in the game (For sorting)
	private Queue<Long> playerQueue = new LinkedList<Long>(); // List of Discord IDs of players who join
	private int turnCount = 1;

	private Game() {
	}

	/**
	 * 
	 * Player constructor takes playerQueue arguments and passes it through to the
	 * playerQueue, then to the playerList, then the map is generated
	 */
	public Game(Queue<Long> playerQueue) { // Game object has been created

		System.out.println("A new game has been created!");

		this.playerQueue.addAll(playerQueue); // Set playerQueue equal to queue from BotMain

		populatePlayerList(); // Populate list of players
		generateMap();

	} // End of Game (One argument constructor)

	/**
	 * 
	 * When end of the game is reached, will calculate and display results
	 * 
	 */
	protected String endGame() {

		String holder = "";

		// Final Scores
		int hawksScore = 0;
		int owlsScore = 0;
		int rootScore = 0;

		Iterator<Map.Entry<Coordinate, Tile>> itr = this.gameMap.entrySet().iterator();

		while (itr.hasNext()) // Iterate through all map tiles for ruling factions
		{
			Map.Entry<Coordinate, Tile> entry = itr.next();

			if (entry.getValue().getFaction().equals("Hawks")) { // Ruling faction is Hawks

				hawksScore++; // Increase Hawks score

			} else if (entry.getValue().getFaction().equals("Owls")) { // Ruling faction is Owls

				owlsScore++; // Increase Owls score

			} else if (entry.getValue().getFaction().equals("Root")) { // Ruling faction is Root

				rootScore++; // Increase Root score

			}

		} // End of map tile iteration

		if (hawksScore > owlsScore && hawksScore > rootScore) {

			holder += "Hawks win!"; // Hawks win

		} else if (owlsScore > hawksScore && owlsScore > rootScore) {

			holder += "Owls win!"; // Owls win

		} else if (rootScore > hawksScore && rootScore > owlsScore) {

			holder += "Root wins!"; // Root wins

		} else if (hawksScore >= owlsScore && hawksScore > rootScore) {

			holder += "Hawks and Owls win!"; // Hawks and Owls tie

		} else if (hawksScore > owlsScore && hawksScore >= rootScore) {

			holder += "Hawks and Root win!"; // Hawks and Root tie

		} else if (owlsScore > hawksScore && owlsScore >= rootScore) {

			holder += "Owls and Root win!"; // Owls and root tie

		} else {

			holder += "Everyone tied! You all win! (I guess?)"; // Everyone ties

		}

		holder += "\nFinal Score:" + "\nHawks: " + hawksScore + "\nOwls: " + owlsScore + "\nRoot: " + rootScore
				+ "\nThanks for playing!";

		// Final message
		return holder;

	} // End of endGame

	/**
	 * 
	 * Populates the player list using the queue supplied in the constructor.
	 * Factions are randomly generated, however there are certain restrictions to
	 * ensure that factions are balanced in-game
	 * 
	 */
	private void populatePlayerList() {

		// Used for determining player faction
		// Mostly random, this ensures we do not end up in a game with players of only
		// the same faction
		String playerFaction = "";
		int playerFactionID;
		boolean[] generatedFlag = { false, // 0 = all generated?
				false, // 1 = hawks generated?
				false, // 2 = owls generated?
				false // 3 = root generate?
		};

		Iterator<Long> itr = this.playerQueue.iterator(); // We're going to iterate through queue of Player IDs

		while (itr.hasNext()) { // While the queue still has IDs

			Player newPlayer = new Player(itr.next()); // Create new player from ID in queue

			// Make sure that at least 1 person in each faction
			// Generate.
			do {
				playerFaction = newPlayer.generateFaction(); // Generate player faction
				playerFactionID = newPlayer.getFactionID();
			}
			// Have all been generated? If not, then if it matches a previously flagged id,
			// regenerate.
			while ((!generatedFlag[0]) && generatedFlag[playerFactionID]);

			// Finalize generated faction
			generatedFlag[playerFactionID] = true;

			// Switch all generated flag when all flags are true
			if (generatedFlag[1] && generatedFlag[2] && generatedFlag[3])
				generatedFlag[0] = true;

			for (int i = 0; i < MAX_PARTY; i++) { // Generate units

				Unit tempUnit = new Unit(playerFaction, newPlayer.getFactionID(), newPlayer.getID());
				newPlayer.getParty().add(i, tempUnit);

			}

			if (!playerList.contains(newPlayer)) { // Make sure Player is not already in list

				this.playerList.add(newPlayer); // Add player to Game's playerList
				this.playerMap.put(newPlayer.getID(), newPlayer); // Add ID and player to Game's playerMap

				System.out.println("Player " + newPlayer.getID() + " has been added to the game!"); // Debug message

			} else { // Player already in the game

				System.out.println("Player " + itr.next() + " is already in-game."); // Prompt player input

			}

		} // End of Queue iteration

	} // End of populatePlayerList

	/**
	 * 
	 * When the end of the return is reached, we restore AP of all players and
	 * increment turnCount
	 * 
	 */
	protected void turnEnd() {

		System.out.println("I have been run - Pt: restoreAP");
		restoreAP(); // Restores the AP of all players in game

		turnCount++;
		System.out.println("I have been run - finish all function");

	} // End turnEnd

	/**
	 * 
	 * Restore AP of every player in-game
	 * 
	 */
	private void restoreAP() { // restoreAP for every Player in-game

		Iterator<Map.Entry<Long, Player>> itr = this.playerMap.entrySet().iterator();

		while (itr.hasNext()) // Iterate through Map and set AP back
		{
			Map.Entry<Long, Player> entry = itr.next();
			entry.getValue().setAp(MAX_AP);

		}

		this.playerList = new ArrayList(playerMap.values()); // Update playerList collection

	} // End restoreAP

	public int getTurnCount() {

		return turnCount;

	} // End getTurnCount

	protected boolean isGameDone() {
		return turnCount == (MAX_GAME_TURNS + 1);
	}

	/**
	 * 
	 * Takes argument of party member and x/y and moves player to designated tile
	 * while also updating the faction of the tile to match the unit's
	 * 
	 */
	protected boolean moveUnit(Long ID, int partyMember, int x, int y) {

		if (this.findPlayerById(ID).getAp() != 0) {
			String playerFaction = this.findPlayerById(ID).getFaction(); // Used to identify tile restrictions
			String direction = "null"; // Used to correct tile restrictions

			// Get unit's current position
			int playerX = this.findPlayerById(ID).getParty().get(partyMember).getPosition1();
			int playerY = this.findPlayerById(ID).getParty().get(partyMember).getPosition2();

			Coordinate tempCoordinate1 = new Coordinate(playerX, playerY); // Get current coordinates

			Coordinate tempCoordinate2 = new Coordinate(x, y); // Get Unit's new position

			// If the unit tries to move into another faction's territory and they are not
			// already next to it
			if (!this.gameMap.get(tempCoordinate2).getFaction().equals(playerFaction)
					&& !this.gameMap.get(tempCoordinate2).getFaction().equals("Unclaimed")
					&& ((Math.abs(x - playerX) > 1) || (Math.abs(y - playerY) > 1))) {

				// Move them back depending on the direction they were moving in until they are
				// back on their own territory
				while (!this.gameMap.get(tempCoordinate2).getFaction().equals(playerFaction)
						&& !this.gameMap.get(tempCoordinate2).getFaction().equals("Unclaimed")
						&& !direction.equals("none")) {

					// Direction they attempted to move in
					if (x - playerX > 0) { // Target - Original > 0 (Player moving right)

						direction = "right";
						x--; // Move one tile to the left

					} else if (x - playerX < 0) { // Target - Original < 0 (Player moving left)

						direction = "left";
						x++; // Move one tile to the right

					} else if (y - playerY > 0) { // Target - Original > 0 (Player moving down)

						direction = "down";
						y--; // Move one tile up

					} else if (y - playerY < 0) { // Target - Original < 0 (Player moving up)

						direction = "up";
						y++; // Move one tile down

					} else if (x - playerX > 0 && y - playerY > 0) { // Player moving down-right

						direction = "southeast";
						x--; // Move one tile to the left
						y--; // Move one tile up

					} else if (x - playerX > 0 && y - playerY < 0) { // Player moving up-right

						direction = "northeast";
						x--; // Move one tile to the left
						y++; // Move one tile down

					} else if (x - playerX < 0 && y - playerY > 0) { // Player moving down-left

						direction = "southwest";
						x++; // Move one tile to the right
						y--; // Move one tile up

					} else if (x - playerX < 0 && y - playerY < 0) { // Player moving up-left

						direction = "northwest";
						x++; // Move one tile to the right
						y++; // Move one tile down

					} else { // Player didn't move

						direction = "none";

					}
				}

				tempCoordinate2 = new Coordinate(x, y); // Push them back 1 tile in the opposite direction

			}

			// Remove Unit from original tile
			this.gameMap.get(tempCoordinate1).removeUnit(this.findPlayerById(ID).getParty().get(partyMember));

			this.findPlayerById(ID).getParty().get(partyMember).setPosition1(x); // Set position 1
			this.findPlayerById(ID).getParty().get(partyMember).setPosition2(y); // Set position 2

			this.playerList = new ArrayList(playerMap.values()); // Update playerList collection

			// Add Unit to target tile
			this.gameMap.get(tempCoordinate2).addUnit(this.findPlayerById(ID).getParty().get(partyMember));
			this.gameMap.get(tempCoordinate2).setFaction(playerFaction);

			// Decrement ap
			this.findPlayerById(ID).setAp(this.findPlayerById(ID).getAp() - 1);
			return true;
		} else {
			return false;
		}

	} // End moveUnit

	private void generateMap() { // Generate Map Tiles!

		int i = 1;
		int j = 1;

		for (i = 1; i <= MAX_MAP_RANGE; i++) { // Our map has dimensions 5 x 5

			for (j = 1; j <= MAX_MAP_RANGE; j++) {

				Tile tempTile = new Tile(i, j); // Create a temporary Tile object

				Coordinate tempCoordinate = new Coordinate(i, j);

				this.gameMap.put(tempCoordinate, tempTile); // Add Tile to map

			}
		}

	} // End generateMap

	/**
	 * 
	 * Return instance of a player based on ID
	 * 
	 */
	protected Player findPlayerById(Long ID) {

		return playerMap.get(ID); // Return the Player object mapped to specified ID

	} // End of findPlayerById

	public static void main(String[] args) { // Main for testing purposes

		Queue<Long> testQueue = new LinkedList<Long>(); // List of Discord IDs of players who join

		testQueue.add((long) 123456789); // Add ID to testQueue
		testQueue.add((long) 987654321); // Add ID to testQueue

		Game testGame = new Game(testQueue); // Create a new test game with Queue argument

		testGame.generateMap(); // Generate the game map

	} // End Main

}
