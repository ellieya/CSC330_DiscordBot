package edu.cuny.csi.csc330.discordbot.bot;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Battle {

	private ArrayList<Player> playerList = new ArrayList<Player>(); // List of all players in the Battle
	private ArrayList<Unit> unitList = new ArrayList<Unit>(); // List of all units in the Battle

	private static int MAX_TURNS = 2; // Max battle turns for each instance

	Battle(ArrayList<Player> playerList, ArrayList<Unit> unitList) { // Takes players and units in the battle

		this.playerList.addAll(playerList);
		this.unitList.addAll(unitList);

	} // End constructor

	public void runBattle() {

		String currentFaction = "";
		int capacity;
		int targetHp;

		for (int i = 1; i <= MAX_TURNS; i++) {

			capacity = this.unitList.size(); // Capacity our battleQueue will have (Updates every turn)

			PriorityQueue<Unit> battleQueue = new PriorityQueue<Unit>(capacity, new UnitComparator()); //Updates every turn

			battleQueue.addAll(this.unitList); // Add all units in the list to the battleQueue (Updates every turn)
			
			while (!battleQueue.isEmpty()) { //Start turn

				currentFaction = battleQueue.peek().getFaction(); // Get faction of the current unit that's attacking

				int target = Randomizer.generateInt(1, capacity); // Generate a random unit index

				while (this.unitList.get(target).getFaction().equals(currentFaction)) { // If target is of the same
																						// faction

					target = Randomizer.generateInt(1, capacity); // Generate a new target

				}

				targetHp = calculateTargetHp(battleQueue.peek(), this.unitList.get(target)); // Calculate the target's
																								// hp
																								// after attack
				this.unitList.get(target).setCurHP(targetHp);

				if (this.unitList.get(target).getCurHP() <= 0) { // If the target unit has no hp left

					// Show death message
					System.out.println("Unit " + this.unitList.get(target).getName() + " of faction "
							+ this.unitList.get(target).getFaction() + " has died.");

					this.unitList.get(target).setDead(true); // The target unit is now dead
					battleQueue.remove(this.unitList.get(target)); // It will no longer be considered even for turn
																	// order
					this.unitList.remove(target); // Remove it from the unitList permanently

				}

				battleQueue.poll(); // Pop queue, next unit is scheduled to attack

			} // End turn

		} // End battle

	} // End runBattle

	public int calculateTargetHp(Unit unit1, Unit unit2) {

		int remainingHp = unit2.getCurHP();
		int damage = unit1.getAtk() - unit2.getDef();

		if (unit2.getDef() >= unit1.getAtk()) {

			damage = 1;

		}

		remainingHp = remainingHp - damage;

		return remainingHp;

	} // End calculateDmg

}
