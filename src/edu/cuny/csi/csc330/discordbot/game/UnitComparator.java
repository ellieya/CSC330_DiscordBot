package edu.cuny.csi.csc330.discordbot.game;

import java.util.Comparator;

public class UnitComparator implements Comparator<Unit> {

	public UnitComparator() {
	}

	@Override
	public int compare(Unit unit1, Unit unit2) { //Returns which unit has higher speed value

		if (unit1.getSpd() > unit2.getSpd()) { // unit1 is faster

			return 1;

		} else if (unit1.getSpd() < unit2.getSpd()) { // unit2 is faster

			return -1;

		} else { // They are equal

			return 0;

		}

	}

}
