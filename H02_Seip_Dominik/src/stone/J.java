package stone;

import main.TetrisGame;
import misc.Vector;
import robot.SquareColor;
import robot.SquareRobot;

public class J extends Stone {

	private static final SquareColor COLOR = SquareColor.BLUE;

	/**
	 * Constructs a stone of type {@link J}.
	 * @param x the x coordinate of the main robot
	 * @param y the y coordinate of the main robot
	 */
	public J(TetrisGame game, int x, int y) {
		super(game);
		SquareRobot[] robots = new SquareRobot[4];
		//////////// ROTATIONS ////////////
		// . . . | . 2 3 | 3 . . | . 0 . //
		// 0 1 2 | . 1 . | 2 1 0 | . 1 . //
		// . . 3 | . 0 . | . . . | 3 2 . //
		///////////////////////////////////
		robots[0] = new SquareRobot(this, x - 1, y, COLOR, Vector.of(1, -1), Vector.of(1, 1), Vector.of(-1, 1), Vector.of(-1, -1));
		robots[1] = new SquareRobot(this, x, y, COLOR, Vector.of(0, 0), Vector.of(0, 0), Vector.of(0, 0), Vector.of(0, 0));
		robots[2] = new SquareRobot(this, x + 1, y, COLOR, Vector.of(-1, 1), Vector.of(-1, -1), Vector.of(1, -1), Vector.of(1, 1));
		robots[3] = new SquareRobot(this, x + 1, y - 1, COLOR, Vector.of(0, 2), Vector.of(-2, 0), Vector.of(0, -2), Vector.of(2, 0));
		setSquareRobots(robots);
	}

}
