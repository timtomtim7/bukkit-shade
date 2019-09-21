package blue.sparse.bshade.math.blocks.util;

import org.bukkit.util.Vector;

public enum Axis {
	X,
	Y,
	Z;

	private final int[] axis = new int[3];

	Axis() {
		axis[ordinal()] = 1;
	}

	public int getX() {
		return axis[0];
	}

	public int getY() {
		return axis[1];
	}

	public int getZ() {
		return axis[2];
	}

	public Vector toVector() {
		return new Vector(axis[0], axis[1], axis[2]);
	}
}
