package stone;

import main.TetrisGame;
import misc.Vector;
import robot.SquareRobot;

public abstract class Stone {

	///////////////
	// EXERCISES //
	///////////////

	/**
	 * Tries to rotate all square robots of this stone to the left.
	 * @return if the rotation was successful
	 */
	public boolean rotateAllLeft() {
		// TODO Exercise H2.2
		Vector[][] rotationVectors = new Vector[4][4]; //erste Ziffer Anzahl ROboter, zweite Ziffer ANzahl Drehungen
		for (int i = 0; i<this.robots.length; i++) {
			for (int j=0; j<this.robots[i].getRotationVectors().length; j++) {
				rotationVectors[i][j] = this.robots[i].getRotationVectors()[j];
			}
		}
		for (int i=0; i<this.robots.length;i++) {
			if (!this.robots[i].canMove(new Vector(rotationVectors[i][this.doneRotations].x, rotationVectors[i][this.doneRotations].y))) {
				return false;
			}
		}
		for (int i=0; i<this.robots.length;i++) {
			this.robots[i].rotateLeft();
			
		}
		if(doneRotations == 3) {
			doneRotations = 0;
		} else {
			doneRotations++;
		}
		return true;
	}

	/**
	 * Handles the given key input.
	 * @param key the key input
	 */
	public void handleKeyInput(byte key) {
		// TODO Exercise H3
		
		// TODO keine Eingabe bei Game Over!!
		if (this.getRelatedGame().isRunning()) {
			if (key == 0) {
				this.rotateAllLeft();
			} else if (key == 1) {
				this.moveAllLeft();
			} else if (key == 2) {
				this.moveAllDown();
			} else if (key == 3) {
				this.moveAllRight();
			} else if (key == 4) {
				while (this.moveAllDown()) {
					this.moveAllDown();
				}
			}
		}
	}

	// ---------------------------------------------------------------------------------------------------- //

	private final TetrisGame game;

	private SquareRobot[] robots;
	private int doneRotations = 0;
	
	public int getDoneRotations() {
		return doneRotations;
	}

	/**
	 * Constructs a {@link Stone}.
	 * @param game the game using this stone
	 */
	public Stone(TetrisGame game) {
		this.game = game;
	}

	/**
	 * Returns the game using this stone.
	 * @return the game using this stone
	 */
	public TetrisGame getRelatedGame() {
		return game;
	}

	/**
	 * Returns the square robot array of this stone.
	 * @return the square robot array of this stone
	 */
	public SquareRobot[] getSquareRobots() {
		return robots;
	}

	/**
	 * Tries to move all square robots of this stone by the given vector.
	 * @param vector the vector
	 * @return if the movement was successful
	 */
	public boolean moveAll(Vector vector) {
		for (SquareRobot square : robots)
			if (!square.canMove(vector))
				return false;
		for (SquareRobot square : robots)
			square.move(vector);
		return true;
	}

	/**
	 * Tries to move down all square robots of this stone.
	 * @return if the movement was successful
	 */
	public boolean moveAllDown() {
		return moveAll(Vector.of(0, -1));
	}

	/**
	 * Tries to move left all square robots of this stone.
	 * @return if the movement was successful
	 */
	public boolean moveAllLeft() {
		return moveAll(Vector.of(-1, 0));
	}

	/**
	 * Tries to move right all square robots of this stone.
	 * @return if the movement was successful
	 */
	public boolean moveAllRight() {
		return moveAll(Vector.of(1, 0));
	}

	/**
	 * Sets the array of square robots for this stone.
	 * @param if the array of square robots
	 */
	public void setSquareRobots(SquareRobot... robots) {
		this.robots = robots;
	}

}
