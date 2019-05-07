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
	protected static int MAX_BATTLE_TURNS = 5;
	private static int MAX_AP = 3; // Max action points for a user

	// Other private data members
	protected Map<Coordinate, Tile> gameMap = new HashMap<Coordinate, Tile>(); // The Game Map to be navigated by player
																				// units
	protected Set<Tile> gameSet = new HashSet<Tile>(); // Game Set for sorting

	protected Map<Long, Player> playerMap = new HashMap<Long, Player>(); // Map of all players in the game (For
																			// searching)
	protected ArrayList<Player> playerList = new ArrayList<Player>(); // List of all players in the game (For sorting)

	protected Queue<Long> playerQueue = new LinkedList<Long>(); // List of Discord IDs of players who join
	protected Queue<Battle> battleQueue = new LinkedList<Battle>();
	protected int turnCount = 1;

	private Game() {
	}

	public Game(Queue<Long> playerQueue) { // Game object has been created

		System.out.println("A new game has been created!");

		this.playerQueue.addAll(playerQueue); // Set playerQueue equal to queue from BotMain

		populatePlayerList(); // Populate list of players
		generateMap();

	} // End of Game (One argument constructor)

	public String endGame() {

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

			System.out.println("Hawks win!"); // Hawks win

		} else if (owlsScore > hawksScore && owlsScore > rootScore) {

			System.out.println("Owls win!"); // Owls win

		} else if (rootScore > hawksScore && rootScore > owlsScore) {

			System.out.println("Root wins!"); // Root wins

		} else if (hawksScore >= owlsScore && hawksScore > rootScore) {

			System.out.println("Hawks and Owls win!"); // Hawks and Owls tie

		} else if (hawksScore > owlsScore && hawksScore >= rootScore) {

			System.out.println("Hawks and Root win!"); // Hawks and Root tie

		} else if (owlsScore > hawksScore && owlsScore >= rootScore) {

			System.out.println("Owls and Root win!"); // Owls and root tie

		} else {

			System.out.println("Everyone tied! You all win! (I guess?)"); // Everyone ties

		}

		// Final message
		return "Final Score:" + "\nHawks: " + hawksScore + "\nOwls: " + owlsScore + "\nRoot: " + rootScore
				+ "\nThanks for playing!";

	} // End of endGame

	public void init() throws InterruptedException {

		// Game object will run in 2 minutes! (Inherited from BotMain)?

	} // End of init

	public void populatePlayerList() {

		Iterator<Long> itr = this.playerQueue.iterator(); // We're going to iterate through queue of Player IDs

		while (itr.hasNext()) { // While the queue still has IDs

			Player newPlayer = new Player(itr.next()); // Create new player from ID in queue

			if (!playerList.contains(newPlayer)) { // Make sure Player is not already in list

				this.playerList.add(newPlayer); // Add player to Game's playerList
				this.playerMap.put(newPlayer.getID(), newPlayer); // Add ID and player to Game's playerMap

				System.out.println("Player " + newPlayer.getID() + " has been added to the game!"); // Debug message

			} else { // Player already in the game

				System.out.println("Player " + itr.next() + " is already in-game."); // Prompt player input

			}

		} // End of Queue iteration

	} // End of populatePlayerList

	public void turnEnd() {

		System.out.println("I have been run - Pt: checkBattle");
		// checkBattle(); // Iterates through map, pushes eligible situations onto the
		// queue

		System.out.println("I have been run - Pt: runBattle");
		runBattle(); // Run battles that are in the queue

		System.out.println("I have been run - Pt: restoreAP");
		restoreAP(); // Restores the AP of all players in game

		System.out.println("I have been run - Pt: updateMap");
		updateMap(); // Update map tiles current ruling factions

		turnCount++;
		System.out.println("I have been run - finish all function");

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
			Unit unitHolder;

			for (int i = 1; i <= partySize; i++) {

				unitHolder = entry.getValue().getParty().get(i);

				// TODO Revise position1 and position2 to Coordinate datatype
				x = unitHolder.getPosition1(); // Position 1 of unit
				y = unitHolder.getPosition2(); // Position 2 of unit

				Coordinate tempCoordinate = new Coordinate(x, y);

				// TODO if we have time we should adjust map event flags on Tile into a boolean
				// array
				if (gameMap.get(tempCoordinate).isRest()) { // Check if tile the unit on is a rest tile
					unitHolder.setCurHP(MapTileEvents.restEvent(unitHolder));
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

						int playerX2 = entry2.getValue().getParty().get(j).getPosition1();
						int playerY2 = entry2.getValue().getParty().get(j).getPosition2();

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

		if (!battleQueue.isEmpty()) { // If battleQueue is not empty

			Iterator<Battle> entry = this.battleQueue.iterator();

			while (entry.hasNext()) { // Iterate through all battles on the queue

				// Run the battle
				entry.next().runBattle();

			}

		}

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

	public boolean isGameDone() {
		return turnCount == (MAX_GAME_TURNS + 1);
	}

	public void moveUnit(Long ID, int partyMember, int x, int y) {

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
		this.findPlayerById(ID).getParty().get(partyMember).setPosition1(y); // Set position 2

		this.playerList = new ArrayList(playerMap.values()); // Update playerList collection

		// Add Unit to target tile
		this.gameMap.get(tempCoordinate2).addUnit(this.findPlayerById(ID).getParty().get(partyMember));

	} // End moveUnit

	public void generateMap() { // Generate Map Tiles!

		int i = 1;
		int j = 1;

		for (i = 1; i <= MAX_MAP_RANGE; i++) { // Our map has dimensions 5 x 5

			for (j = 1; j <= MAX_MAP_RANGE; j++) {

				Tile tempTile = new Tile(i, j); // Create a temporary Tile object

				Coordinate tempCoordinate = new Coordinate(i, j);

				this.gameMap.put(tempCoordinate, tempTile); // Add Tile to map
				this.gameSet.add(tempTile); // Add Tile to set

			}
		}

	} // End generateMap

	public void printMap() { // Display the Map!

		List sortedValues = new ArrayList(gameMap.values());
		Collections.sort(sortedValues);

		sortedValues.forEach(System.out::println);

	} // End printMap

	public void updateMap() { // Call after each player moves

		int capacity;
		String factionClaim = "";
		boolean opposingFaction;
		boolean matchingFactionFound;
		boolean multipleOpposingFaction;

		Iterator<Map.Entry<Coordinate, Tile>> itr = this.gameMap.entrySet().iterator();

		while (itr.hasNext()) // Iterate through all tiles and update ruling factions
		{
			Map.Entry<Coordinate, Tile> entry = itr.next();

			ArrayList<Unit> unitsOnTile = new ArrayList<Unit>(); // Create new list

			unitsOnTile.addAll(entry.getValue().getUnitList()); // Add all units on tile to list

			// Help determine what faction the tile will have after update
			factionClaim = "";
			opposingFaction = false;
			matchingFactionFound = false;
			multipleOpposingFaction = false;
			capacity = unitsOnTile.size();

			for (int i = 1; i <= capacity; i++) { // Compare all unit factions on the tile to the tile's faction

				// If the unit is not the same faction as the tile
				if (!unitsOnTile.get(i).getFaction().equals(entry.getValue().getFaction())) {

					// If no opposing factions have been found get
					if (opposingFaction == false) {

						factionClaim = unitsOnTile.get(i).getFaction(); // Set the faction who is claiming the tile
						opposingFaction = true; // There is now an opposing faction

						// If an opposing faction was already found and the unit is not of this faction
					} else if (opposingFaction == true && !unitsOnTile.get(i).getFaction().equals(factionClaim)) {

						multipleOpposingFaction = true; // There are multiple opposing factions

					}

				}

				// If the unit is the same faction as the tile
				if (unitsOnTile.get(i).getFaction().equals(entry.getValue().getFaction())) {

					matchingFactionFound = true; // There is a faction on the tile who previously claimed it

				}

			} // End of unit compare loop

			// Update tile (If there is an opposing faction and no other factions are
			// present)
			if (opposingFaction == true && multipleOpposingFaction == false && matchingFactionFound == false) {

				entry.getValue().setFaction(factionClaim);

			} // If these conditions aren't met, the tile will stay the same

		} // End of map iteration

	} // End updateMap

	public Player findPlayerById(Long ID) {

		return playerMap.get(ID); // Return the Player object mapped to specified ID

	} // End of findPlayerById

	public static void main(String[] args) { // Main for testing purposes

		Queue<Long> testQueue = new LinkedList<Long>(); // List of Discord IDs of players who join

		testQueue.add((long) 123456789); // Add ID to testQueue
		testQueue.add((long) 987654321); // Add ID to testQueue

		Game testGame = new Game(testQueue); // Create a new test game with Queue argument

		testGame.generateMap(); // Generate the game map

		testGame.printMap(); // Display the game map

	} // End Main

}
