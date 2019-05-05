package edu.cuny.csi.csc330.discordbot.game;

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
	private static int MAX_AP = 3; // Max action points for a user

	// Other private data members
	private Map<Coordinate, Tile> gameMap = new HashMap<Coordinate, Tile>(); // The Game Map to be navigated by player
																				// units
	private Set<Tile> gameSet = new HashSet<Tile>(); // Game Set for sorting

	private Map<Long, Player> playerMap = new HashMap<Long, Player>(); // Map of all players in the game (For searching)
	private ArrayList<Player> playerList = new ArrayList<Player>(); // List of all players in the game (For sorting)

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
				this.playerMap.put(newPlayer.getID(), newPlayer); // Add ID and player to Game's playerMap

			} else { // Player already in the game

				System.out.println("Player " + itr.next() + " is already in-game."); // Prompt player input

			}

		} // End of Queue iteration

	} // End of populatePlayerList

	public void turnEnd() {

		runAllMapEvents(); // Iterates through playerList for coordinates, check against map tile

		checkBattle(); // Iterates through map

		runBattle();

		restoreAP();

		this.turnCount++;

	} // End turnEnd

	public void runAllMapEvents() {

		int partySize = 0;
		int x;
		int y;

		Iterator<Map.Entry<Long, Player>> itr = this.playerMap.entrySet().iterator();

		while (itr.hasNext()) // Go through all player objects
		{

			Map.Entry<Long, Player> entry = itr.next();

			partySize = entry.getValue().getParty().size(); // Get size of party

			for (int i = 0; i < partySize; i++) {

				x = entry.getValue().getParty().get(i).getPosition1(); // Position 1 of unit
				y = entry.getValue().getParty().get(i).getPosition2(); // Position 2 of unit

				Coordinate tempCoordinate = new Coordinate(x, y);

				if (gameMap.get(tempCoordinate).isRest()) { // Check if tile the unit on is a rest tile

					int unitHp = entry.getValue().getParty().get(i).getCurHP(); // Get Unit's current HP

					double hpGain = Math.floor((entry.getValue().getParty().get(i).getHp()) / (2)); // Calculate
																									// HP Gain

					entry.getValue().getParty().get(i).setCurHP((int) (unitHp + hpGain)); // Set HP to current +
																							// gain
				} // Tile check end

			} // End of party iteration

		} // Done iterating through tiles

		this.playerList = new ArrayList(playerMap.values()); // Update playerList collection

	} // End runAllMapEvents

	public void checkBattle() {

		int partySize = 0;
		int partySize2 = 0;
		String friendlyFaction = "";
		boolean noEnemy = true;

		Iterator<Map.Entry<Long, Player>> itr = this.playerMap.entrySet().iterator();

		while (itr.hasNext()) // Iterate through players
		{
			Map.Entry<Long, Player> entry = itr.next();
			ArrayList<Player> battlePlayers = new ArrayList<Player>(); // Create new list of players
			ArrayList<Unit> battleUnits = new ArrayList<Unit>(); // Create new list of units

			partySize = entry.getValue().getParty().size(); // Get size of party

			for (int i = 1; i <= partySize; i++) { // Go through player's party

				int playerX = entry.getValue().getParty().get(i).getPosition1();
				int playerY = entry.getValue().getParty().get(i).getPosition2();

				// Get the coordinate the unit is on and all adjacent coordinates
				Coordinate tempCoordinate = new Coordinate(playerX, playerY);
				Coordinate adjCoordinate1 = new Coordinate(playerX, playerY - 1);
				Coordinate adjCoordinate2 = new Coordinate(playerX, playerY + 1);
				Coordinate adjCoordinate3 = new Coordinate(playerX + 1, playerY);
				Coordinate adjCoordinate4 = new Coordinate(playerX - 1, playerY);

				Tile unitTile1 = gameMap.get(tempCoordinate); // Get tile the unit is on
				Tile adjTile1 = gameMap.get(adjCoordinate1);
				Tile adjTile2 = gameMap.get(adjCoordinate2);
				Tile adjTile3 = gameMap.get(adjCoordinate3);
				Tile adjTile4 = gameMap.get(adjCoordinate4);

				// Compare other players and their units
				Iterator<Map.Entry<Long, Player>> itr2 = this.playerMap.entrySet().iterator();

				while (itr2.hasNext()) // Iterate through other players to compare
				{
					Map.Entry<Long, Player> entry2 = itr2.next();

					partySize2 = entry2.getValue().getParty().size(); // Get size of party

					for (int j = 1; j <= partySize2; j++) { // Go through player's party

						int playerX2 = entry2.getValue().getParty().get(i).getPosition1();
						int playerY2 = entry2.getValue().getParty().get(i).getPosition2();

						Coordinate tempCoordinate2 = new Coordinate(playerX2, playerY2);

						Tile unitTile2 = gameMap.get(tempCoordinate2); // Get tile the unit is on

						// If unit is on an adjacent tile to original unit or on the same tile
						if (unitTile2.equals(unitTile1) || unitTile2.equals(adjTile1) || unitTile2.equals(adjTile2)
								|| unitTile1.equals(adjTile3) || unitTile1.equals(adjTile4)) {

							if (!entry.getValue().getParty().get(i).isInBattle()) { // If the original unit is not
																					// already engaged in battle

								if (!entry.getValue().isInBattle()) { // If original player is not in battle
									entry.getValue().setInBattle(true);
									battlePlayers.add(entry.getValue()); // Add original player to the list

									friendlyFaction = entry.getValue().getFaction(); // Faction of original player

								}

								entry.getValue().getParty().get(i).setInBattle(true);
								battleUnits.add(entry.getValue().getParty().get(i)); // Add original unit to the list

							} // Add original unit case end

							if (!entry2.getValue().getParty().get(j).isInBattle()) { // If opposing unit is

								if (!entry2.getValue().isInBattle()) { // If opposing player is not in battle
									entry2.getValue().setInBattle(true);
									battlePlayers.add(entry2.getValue()); // Add opposing player to the list
								}

								entry2.getValue().getParty().get(j).setInBattle(true);
								battleUnits.add(entry2.getValue().getParty().get(j)); // Add opposing unit to the list

							}

						} // Adjacent case end

					} // End party2 iteration

				} // Stop iterating through other players

			} // End party iteration

			if (!battleUnits.isEmpty()) {
				for (int i = 1; i <= battleUnits.size(); i++) { // Make sure there is an enemy in units list

					if (!battleUnits.get(i).getFaction().equals(friendlyFaction)) { // If you find a unit of the
																					// opposite faction

						noEnemy = false; // Set noEnemy flag to false

					}
				}
			}

			if (!battleUnits.isEmpty() && noEnemy == false) { // If units were sent into battle and there is an enemy
																// faction present

				Battle newBattle = new Battle(battlePlayers, battleUnits); // Create a new battle instance
				this.battleQueue.add(newBattle); // Add it to the battleQueue

			}

			noEnemy = true; // Set flag back to true

		} // Stop comparing player to others, move onto the next one

	} // End checkBattle

	public void runBattle() {

	} // End runBattle

	public void restoreAP() { // restoreAP for every Player in-game

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

	public void moveUnit(Long ID, int partyMember, int x, int y) {

		this.findPlayerById(ID).getParty().get(partyMember).setPosition1(x); // Set position 1
		this.findPlayerById(ID).getParty().get(partyMember).setPosition1(y); // Set position 2

		this.playerList = new ArrayList(playerMap.values()); // Update playerList collection

	} // End moveUnit

	public void generateMap() { // Generate Map Tiles!

		int i = 1;
		int j = 1;

		for (i = 1; i <= MAP_RANGE; i++) { // Our map has dimensions 5 x 5

			for (j = 1; j <= MAP_RANGE; j++) {

				Tile tempTile = new Tile(i, j); // Create a temporary Tile object

				Coordinate tempCoordinate = new Coordinate(i, j);

				this.gameMap.put(tempCoordinate, tempTile); // Add Tile to map
				this.gameSet.add(tempTile); // Add Tile to set

			}
		}

	} // End generateMap

	public void printMap() { // Display the Map!

		List sortedKeys = new ArrayList(gameMap.keySet());
		Collections.sort(sortedKeys);

		sortedKeys.forEach(System.out::println);

	} // End printMap

	public Player findPlayerById(Long ID) {

		return playerMap.get(ID); // Return the Player object mapped to specified ID

	} // End of findPlayerById

	public static void main(String[] args) { // Main (will execute when class is called. Should generate all starting
												// instances of a new game.)

		Game testGame = new Game(); // Create a new test game

		testGame.generateMap(); // Generate the game map

		testGame.printMap(); // Display the game map

	} // End Main

}
