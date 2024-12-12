package h01;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import fopbot.*;
import misc.NewRobots;
import misc.Protagonist;

/**
 * Task 1 of Hausuebung01 of "Funktionale und objektorientierte
 * Programmierkonzepte" WS 20/21. In this task students have to add
 * functionality to certain {@code Robot}s according to the sheets presented in
 * the lecture. This class initializes the world which has to be used and
 * presents which parts of code have to be changed. Depending on whether this is
 * the solution or the template the doc and included code might differ.
 * 
 * If this is the template do ONLY modify parts of the code which are commented
 * to be modified.
 * 
 * @author Alexander Mainka
 */

public class Task1 {

	private static NewRobots botGuard;
	private static Protagonist botProtagonist;

	private static NewRobots botPrime;
	private static NewRobots botEven;
	private static NewRobots botOdd;

	private final static String FILE_PATH = "res/%s.png";
	private final static String GUARD_PICTURE = "triangleguard";
	private final static String NONPROTAGONIST_PICTURE = "trianglegoal";

	// Size MUST be an even digit and Size MUST be greater than 3. This value should
	// not be changed.
	private final static int setSizeOfWorld = 10;

	// -------------- DO NOT MODIFY ANYTHING ABOVE THIS POINT -------------------

	// --------------- VERY IMPORTANT / SEHR WICHTIG -----------------------------
	// Number of Task you want to be working on. Should be either task 1 or task 2.
	// AENDERN SIE DIESE VARIABLE UNBEDINGT, JE NACH DEM, WELCHE AUFGABE SIE
	// BEARBEITEN MOECHTEN, ENTWEDER taskNumber = 1 ODER taskNumber = 2
	private static int taskNumber = 2;

	/**
	 * Method which makes Java code runnable as a program. For this task the main
	 * method handles initialization and preparation of the world and anything
	 * needed necessary to finish this task.
	 * 
	 * @param args generic arguments needed by definition to run a program
	 */
	public static void main(String[] args) {

		// You can change the game speed for testing purposes. Value is given in
		// milliseconds. Default is 100.
		int setGameSpeed = 100;

		initializeTask(taskNumber, setSizeOfWorld, setGameSpeed);
		if (taskNumber != 2) {
			botProtagonist = new Protagonist(0, 0, Direction.UP, 10);
		} else {
			botProtagonist = new Protagonist(0, 0, Direction.UP, 0);

		}

		// ACHTUNG: Es steht Ihnen frei, die Variablen zwischen der Zeile 43 bis zu
		// dieser aktuellen Zeile zu ändern, achten Sie jedoch akribisch darauf, dass
		// diese Variablen wieder auf ihren Ausgangswert zurückgesetzt sind, wenn Sie
		// Ihre Abgabe tätigen.
		// -------------- YOUR CODE BELONGS HERE ------------------------------------



		if (taskNumber != 2) {
			// ---------------------- YOUR CODE FOR TASK 1 BELONGS HERE --------------------
//			TODO
			int zaehler = 0;
			while (botProtagonist.isFrontClear()) {
				botProtagonist.move();
				zaehler++;
			}
			botProtagonist.turnLeft();
			botProtagonist.turnLeft();
			botProtagonist.turnLeft();
			for (int i=0;i<zaehler;i++) {
				botProtagonist.move();
			}
			botProtagonist.turnLeft();
			botProtagonist.move();
			botProtagonist.turnLeft();
			zaehler = 0;
			while (botProtagonist.isFrontClear()) {
				botProtagonist.move();
				zaehler++;
			}
			botProtagonist.turnLeft();
			botProtagonist.turnLeft();
			botProtagonist.turnLeft();
			for (int i=0;i<zaehler;i++) {
				botProtagonist.move();
			}
			for (int i=0; i<6;i++) {
				botProtagonist.putCoin();
				botPrime.pickCoin();
			}
			botProtagonist.turnLeft();
			botProtagonist.turnLeft();
			botProtagonist.turnLeft();
			
			
			zaehler = 0;
			while (botProtagonist.isFrontClear()) {
				botProtagonist.move();
				zaehler++;
			}
			botProtagonist.turnLeft();
			botProtagonist.turnLeft();
			botProtagonist.turnLeft();
			for (int i=0;i<zaehler;i++) {
				botProtagonist.move();
			}
			botProtagonist.turnLeft();
			botProtagonist.move();
			botProtagonist.turnLeft();
			zaehler = 0;
			while (botProtagonist.isFrontClear()) {
				botProtagonist.move();
				zaehler++;
			}
			botProtagonist.turnLeft();
			botProtagonist.turnLeft();
			botProtagonist.turnLeft();
			for (int i=0;i<zaehler;i++) {
				botProtagonist.move();
			}
			for (int i=0; i<3;i++) {
				botProtagonist.putCoin();
				botEven.pickCoin();
			}
			botProtagonist.turnLeft();
			botProtagonist.turnLeft();
			botProtagonist.turnLeft();
			
			
			zaehler = 0;
			while (botProtagonist.isFrontClear()) {
				botProtagonist.move();
				zaehler++;
			}
			botProtagonist.turnLeft();
			botProtagonist.turnLeft();
			botProtagonist.turnLeft();
			for (int i=0;i<zaehler;i++) {
				botProtagonist.move();
			}
			botProtagonist.turnLeft();
			botProtagonist.move();
			botProtagonist.turnLeft();
			zaehler = 0;
			while (botProtagonist.isFrontClear()) {
				botProtagonist.move();
				zaehler++;
			}
			botProtagonist.turnLeft();
			botProtagonist.turnLeft();
			botProtagonist.turnLeft();
			for (int i=0;i<zaehler;i++) {
				botProtagonist.move();
			}
			botProtagonist.putCoin();
			botProtagonist.turnLeft();
			botProtagonist.turnLeft();
			botProtagonist.turnLeft();
			botOdd.pickCoin();
		}

		if (taskNumber == 2) {
			// -------------------- YOUR CODE FOR TASK 2 BELONGS HERE -------------------
//			TODO
			int zaehler = 0;
			for (int j=0; j<3;j++) {
				while (botProtagonist.isFrontClear()) {
					botProtagonist.move();
					zaehler++;
				}
				botProtagonist.turnLeft();
				botProtagonist.turnLeft();
				botProtagonist.turnLeft();
				for (int i=0;i<zaehler;i++) {
					if (botProtagonist.isFrontClearOfGuard()) {
						botProtagonist.move();
					} else {
						botProtagonist.waitOneRound();
						zaehler++;
					}
				}
				botProtagonist.move();
				botProtagonist.turnLeft();
				botProtagonist.move();
				botProtagonist.turnLeft();
				botProtagonist.move();
				zaehler = 0;
				while (botProtagonist.isFrontClear()) {
					botProtagonist.move();
					zaehler++;
				}
				botProtagonist.turnLeft();
				botProtagonist.turnLeft();
				botProtagonist.turnLeft();
				for (int i=0;i<zaehler;i++) {
					if (botProtagonist.isFrontClear()) {
						botProtagonist.move();
					} else {
						botProtagonist.waitOneRound();
						zaehler++;
					}
				}
				botProtagonist.pickCoin();
				botProtagonist.turnLeft();
				botProtagonist.turnLeft();
				botProtagonist.turnLeft();
				zaehler = 0;
			}
			
		}

	}



	// ------------- DO NOT MODIFY ANYTHING BELOW THIS POINT ------------------

	/**
	 * This method initializes anything needed for this task. It expects the
	 * {@code size}, which has to be odd and greater than 3, of the world which
	 * translates to equally chosen width and height. Furthermore it sets the
	 * {@code delay} between actions within the world. It also sets either 2 only
	 * horizontal and 2 only vertical walls leading to the center of the map leaving
	 * an empty 2x2 field in the center without walls. Additionally according to
	 * either task 1 or task 2 robots, coins and a guard are placed and their visual
	 * presentations changed.
	 * 
	 * @param taskNumber number of the task to be used
	 * @param size       sets height and width of the world equal to size
	 * @param delay      sets the delay between actions in the world
	 */
	private static void initializeTask(int taskNumber, int size, int delay) {

		// Willingly chose not to use an exception to avoid confusion or concepts which
		// have not yet been used in the lecture
		if (size % 2 == 1 || size < 4) {
			System.out.print("World size MUST be an even digit and greater than 3. Task terminated early.");
			System.exit(-1);
		}
		World.setSize(size, size);
		World.setVisible(true);
		if (delay < 1) {
			delay = 100;
		}
		World.setDelay(delay);

		for (int i = 0; i < size / 2 - 1; i++) {
			World.placeVerticalWall(size / 2 - 1, i);
		}
		for (int i = size - 1; i > size / 2; i--) {
			World.placeVerticalWall(size / 2 - 1, i);
		}
		for (int i = 0; i < size / 2 - 1; i++) {
			World.placeHorizontalWall(i, size / 2 - 1);
		}
		for (int i = size - 1; i > size / 2; i--) {
			World.placeHorizontalWall(i, size / 2 - 1);
		}

		if (taskNumber != 2) {
			try {
				World.getGlobalWorld().setAndLoadRobotImagesById(NONPROTAGONIST_PICTURE,
						new FileInputStream(new File(String.format(FILE_PATH, NONPROTAGONIST_PICTURE))),
						new FileInputStream(new File(String.format(FILE_PATH, NONPROTAGONIST_PICTURE))), 0, 0);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			botPrime = new NewRobots(0, size - 1, NONPROTAGONIST_PICTURE);
			botEven = new NewRobots(size - 1, size - 1, NONPROTAGONIST_PICTURE);
			botOdd = new NewRobots(size - 1, 0, NONPROTAGONIST_PICTURE);
		} else {
			try {
				World.getGlobalWorld().setAndLoadRobotImagesById(GUARD_PICTURE,
						new FileInputStream(new File(String.format(FILE_PATH, GUARD_PICTURE))),
						new FileInputStream(new File(String.format(FILE_PATH, GUARD_PICTURE))), 0, 0);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			World.putCoins(0, World.getHeight() - 1, 1);
			World.putCoins(World.getWidth() - 1, World.getHeight() - 1, 1);
			World.putCoins(World.getWidth() - 1, 0, 1);
			botGuard = new NewRobots(size / 2, size / 2 - 1, GUARD_PICTURE);
		}

	}

	/**
	 * This method makes the guard in the center 2x2 field patrol in a circle by
	 * letting it take a step and turn once upon call.
	 */
	public static void makeGuardPatrol() {
		if (Task1.getTaskNumber() == 2) {
			botGuard.move();
			botGuard.turnLeft();
		}
	}

	public static NewRobots getBotGuard() {
		return botGuard;
	}

	public static int getTaskNumber() {
		return taskNumber;
	}

	public static void setTaskNumber(int taskNumber) {
		Task1.taskNumber = taskNumber;
	}

	public static Protagonist getBotProtagonist() {
		return botProtagonist;
	}

	public static NewRobots getBotPrime() {
		return botPrime;
	}

	public static NewRobots getBotEven() {
		return botEven;
	}

	public static NewRobots getBotOdd() {
		return botOdd;
	}

}
