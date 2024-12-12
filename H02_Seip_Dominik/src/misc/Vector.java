package misc;

import java.awt.Point;

public class Vector extends Point {

	private static final long serialVersionUID = 0L;

	/**
	 * Constructs a {@link Vector}.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public Vector(int x, int y) {
		super(x, y);
	}
	
	/**
	 * Constructs and returns a {@link Vector} for the given coordinates.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public static Vector of(int x, int y) {
		return new Vector(x, y);
	}
	
}
