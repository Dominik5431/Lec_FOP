package misc;

import fopbot.Direction;
import h01.Task1;

//---------------------- DO NOT MODIFY -----------------------------

/**
 * This class extends {@link fopbot.Robot} by overriding
 * {@link fopbot.Robot#move()}.
 * 
 * @author Alexander Mainka
 *
 */
public class Protagonist extends NewRobots {

	public Protagonist(int x, int y, Direction direction, int numberOfCoins) {
		super(x, y, direction, numberOfCoins);
	}

	/**
	 * In context of the task 2 {@link #move()} not only makes the robot move but
	 * also lets the guard take a step forward/
	 */
	@Override
	public void move() {
		super.move();
		if (Task1.getTaskNumber() == 2) {
			Task1.makeGuardPatrol();
		}
	}
}
