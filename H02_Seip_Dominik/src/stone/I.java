package stone;

import main.TetrisGame;
import misc.Vector;
import robot.SquareColor;
import robot.SquareRobot;

public class I extends Stone {

	private static final SquareColor COLOR = SquareColor.YELLOW;

	/**
	 * Constructs a stone of type {@link I}.
	 * @param x the x coordinate of the main robot
	 * @param y the y coordinate of the main robot
	 */
	public I(TetrisGame game, int x, int y) {
		super(game);
		SquareRobot[] robots = new SquareRobot[4];
		//////////////// ROTATIONS ////////////////
		// . . . . | . . 3 . | . . . . | . . 0 . //
		// 0 1 2 3 | . . 2 . | 3 2 1 0 | . . 1 . //
		// . . . . | . . 1 . | . . . . | . . 2 . //
		// . . . . | . . 0 . | . . . . | . . 3 . //
		///////////////////////////////////////////
		robots[0] = new SquareRobot(this, x - 2, y, COLOR, Vector.of(2, -2), Vector.of(1, 2), Vector.of(-1, 1), Vector.of(-2, -1));
		robots[1] = new SquareRobot(this, x - 1, y, COLOR, Vector.of(1, -1), Vector.of(0, 1), Vector.of(0, 0), Vector.of(-1, 0));
		robots[2] = new SquareRobot(this, x, y, COLOR, Vector.of(0, 0), Vector.of(-1, 0), Vector.of(1, -1), Vector.of(0, 1));
		robots[3] = new SquareRobot(this, x + 1, y, COLOR, Vector.of(-1, 1), Vector.of(-2, -1), Vector.of(2, -2), Vector.of(1, 2));
		setSquareRobots(robots);
	}

}
