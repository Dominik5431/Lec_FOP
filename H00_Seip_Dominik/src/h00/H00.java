package h00;

import static fopbot.Direction.*;
import java.util.concurrent.ThreadLocalRandom;

import fopbot.Direction;
import fopbot.Robot;
import fopbot.World;

public class H00 {

	/**
	 * @return a random odd integer between 3 and 9 (both inclusive)
	 */
	public static int getRandomOddWorldSize() {
		return 3 + ThreadLocalRandom.current().nextInt(4) * 2;
	}

	public static void main(String[] args) {
		int size = getRandomOddWorldSize();
		World.setSize(size, size);
		World.setDelay(200);
		World.setVisible(true);
		System.out.println("Size of world: " + size + "x" + size);
		rightRobot(size);
		leftRobot(size);
	}

	public static void rightRobot(int size) {
		// TODO implement H2.1
		Robot r1 = new Robot(size - 1, size/2, UP,1);
		int steps = size - 1 - r1.getY();
		for (int i=0; i<steps; i++) {
			r1.move();
			r1.turnLeft();
			r1.move();
			r1.turnLeft();
			r1.turnLeft();
			r1.turnLeft();
		}
		r1.putCoin();
		r1.turnLeft();
		r1.turnLeft();
		r1.turnLeft();
		for (int i=0; i<steps; i++) {
			r1.move();
			r1.turnLeft();
			r1.turnLeft();
			r1.turnLeft();
			r1.move();
			r1.turnLeft();
		}
		r1.turnLeft();
	}

	public static void leftRobot(int size) {
		// TODO implement H2.2
		Robot r2 = new Robot(0, size/2, UP, 0);
		while (r2.isFrontClear()) {
			r2.move();
			r2.turnLeft();
			r2.turnLeft();
			r2.turnLeft();
			r2.move();
			r2.turnLeft();
		}
		r2.pickCoin();;
		r2.turnLeft();
		while (r2.isFrontClear()) {
			r2.move();
			r2.turnLeft();
			r2.move();
			r2.turnLeft();
			r2.turnLeft();
			r2.turnLeft();
		}
		r2.putCoin();
		r2.turnLeft();
		r2.turnLeft();
		r2.turnLeft();
	}
}
