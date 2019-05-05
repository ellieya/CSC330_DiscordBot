package edu.cuny.csi.csc330.discordbot.game;

public class Coordinate implements Comparable<Coordinate> {
	private int x;
	private int y;

	Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return x + "-" + y;
	}

	public int compareTo(Coordinate anotherCoordinate) {

		if ((x == anotherCoordinate.x) && (y == anotherCoordinate.y)) { // Equals

			return 0;

		} else if ((x < anotherCoordinate.x) || (this.x == anotherCoordinate.x && this.y < anotherCoordinate.y)) { // Less
																													// than

			return -1;

		} else { // Greater than

			return 1;

		}

	}

}
