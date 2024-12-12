package misc;

import fopbot.Direction;
import fopbot.Robot;
import fopbot.World;
import h01.Task1;

// ---------------------- DO NOT MODIFY -----------------------------
/**
 * This class extends {@link fopbot.Robot} and provides new features, e.g. to
 * check whether the guard in task 2 is not in front of a robot, i.e. in the
 * direction it is currently facing or to wait a round, i.e. forcing the guard
 * in task 2 to take a step.
 * 
 * 
 * @author Alexander Mainka
 */

public class NewRobots extends Robot {

	public NewRobots(int x, int y, Direction direction, int numberOfCoins) {
		super(x, y, direction, numberOfCoins);
	}

	/**
	 * Constructor to initialize a {@link misc.NewRobots} and additionally change
	 * its visual presentation.
	 * 
	 * @param x       x coordinate used to determine the position of the robot
	 * @param y       y coordinate used to determine the position of the robot
	 * @param imageID path to the file which is used for the new visual presentation
	 */
	public NewRobots(int x, int y, String imageID) {
		super(x, y);
		this.setImageId(imageID);
	}

	/**
	 * This method checks whether the front, i.e. the direction the robot is facing,
	 * is clear of the guard. Also returns false when facing the outer bounds of the world. 
	 * 
	 * @return true if front of robot is clear otherwise false
	 */
	public boolean isFrontClearOfGuard() {
		if (Task1.getTaskNumber() == 2) {
			int x = getX();
			int y = getY();
			int guardX = Task1.getBotGuard().getX();
			int guardY = Task1.getBotGuard().getY();
			Direction direction = getDirection();
			switch (direction) {
			case UP:
				if (y == World.getGlobalWorld().getHeight() - 1) {
					return false;
				}
				if (guardY == y + 1 && guardX == x) {
					return false;
				}
				break;
			case LEFT:
				if (x == 0) {
					return false;
				}
				if (guardX == x - 1 && guardY == y) {
					return false;
				}
			case DOWN:
				if (y == 0) {
					return false;
				}
				if (guardY == y - 1 && guardX == x) {
					return false;
				}
				break;
			case RIGHT:
				if (x == World.getGlobalWorld().getWidth() - 1) {
					return false;
				}
				if (guardX == x + 1 && guardY == y) {
					return false;
				}
				break;
			}
		}
		return true;
	}
	
	/**
	 * This method checks whether the front, i.e. the direction the robot is facing,
	 * is clear of the guard.
	 * 
	 * @return true if front of robot is clear otherwise false
	 */
	public boolean isFrontClearOfGuardOnly() {
		if (Task1.getTaskNumber() == 2) {
			int x = getX();
			int y = getY();
			int guardX = Task1.getBotGuard().getX();
			int guardY = Task1.getBotGuard().getY();
			Direction direction = getDirection();
			switch (direction) {
			case UP:
				if (guardY == y + 1 && guardX == x) {
					return false;
				}
				break;
			case LEFT:
				if (guardX == x - 1 && guardY == y) {
					return false;
				}
			case DOWN:
				if (guardY == y - 1 && guardX == x) {
					return false;
				}
				break;
			case RIGHT:
				if (guardX == x + 1 && guardY == y) {
					return false;
				}
				break;
			}
		}
		return true;
	}

	/**
	 * Makes the guard take a step in the world if present.
	 */
	public void waitOneRound() {
		// Conceptually this functionality should be placed in class Task1, preferably
		// as a static method
		if (Task1.getTaskNumber() == 2) {

			Task1.makeGuardPatrol();
		}
	}

}
