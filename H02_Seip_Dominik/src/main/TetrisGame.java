package main;

import java.awt.Point;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import fopbot.Robot;

import fopbot.World;
import robot.SquareRobot;
import stone.I;
import stone.J;
import stone.L;
import stone.O;
import stone.S;
import stone.Stone;
import stone.T;
import stone.Z;

public class TetrisGame implements Runnable {
	

	///////////////
	// EXERCISES //
	///////////////

	/**
	 * Clears all completed rows and returns the count of rows cleared by this call.
	 * 
	 * @return the count of rows cleared by this call
	 */
	public int clearRows() {
		// TODO Exercise H4
		int deletedRows = 0;
		//Array mit allen Robotern
		SquareRobot[] robots = new SquareRobot[stones.length * 4];
		int z = 0;
		int zaehler = 0;
		while (stones[z] != null) {
			for (int j=0; j<stones[z].getSquareRobots().length; j++) {
				if (stones[z].getSquareRobots()[j].isTurnedOn()) {
					robots[zaehler] = stones[z].getSquareRobots()[j];
					zaehler++;
				}
			}
			z++;
		}
		boolean[] taken = new boolean[World.getWidth()];
		for (int i=0; i<World.getWidth();i++) {
			taken[i]=false;
		}
		int k=0;
		boolean rowTaken = false;
		int heightProof = World.getHeight();
		for (int i=0; i<heightProof;i++) {
			for (int j=0; j<World.getWidth();j++) {
				k=0;
				while (robots[k] != null) {
					if (!robots[k].isTurnedOff()) {
						if (robots[k].getX() == j && robots[k].getY() == i) {
							taken[j] = true;
						}
					}
					k++;
				}
			}
			rowTaken = allBooleansAreTrue(taken);
			
			if (rowTaken) {
				//Reihe komplett voll?
				deletedRows++;
				rowTaken = false;
				if (checkDirection(i)) {
					gecheckteRows++;
				}
				for (int j=0; j<World.getWidth(); j++) {
					k=0;
					while (robots[k] != null) {
						if (!robots[k].isTurnedOff()) {
							if (robots[k].getX() == j && robots[k].getY() == i) {
								robots[k].turnOff();
							}
						}
						k++;
					}
				}
				//Anderen Reihen nach unten verschieben
				k=0;
				while (robots[k] != null) {
					if (!robots[k].isTurnedOff()) {
						if (robots[k].getY() > i) {
							robots[k].moveDown();
						}
					}	
					k++;
				}
				heightProof--;
				i--;
			}
			for (int l=0; l<World.getWidth();l++) {
				taken[l]=false;
			}
		}
		return deletedRows; 
	}
	
	

	/**
	 * Returns the count of points scored in the given iteration.
	 * 
	 * @param iteration the iteration
	 * @return the count of points scored in the given iteration
	 */
	public int getPoints(int iteration) {
		// TODO Exercise H5
		SquareRobot[] robots = new SquareRobot[stones.length * 4];
		int z = 0;
		int zaehler = 0;
		int points = 0;
		int rows=0;
		while (stones[z] != null) {
			for (int j=0; j<stones[z].getSquareRobots().length; j++) {
				robots[zaehler] = stones[z].getSquareRobots()[j];
				zaehler++;
			}
			z++;
		}
		SquareRobot[] iterationTurnedOffRobots = new SquareRobot[World.getHeight() * World.getWidth()];
		
		z=0;
		zaehler = 0;
		while (robots[z] != null) {
			if (robots[z].getTurnOffIteration() == iteration) {
				iterationTurnedOffRobots[zaehler] = robots[z];
				zaehler++;
			}
			z++;
		}
		if (iterationTurnedOffRobots[7] != null) {
			zaehler++;
		}
		int k=0;
		boolean[] taken = new boolean[World.getWidth()];
		boolean allTaken = false;
		for (int i=0; i< World.getHeight(); i++) {
			for (int j=0; j<World.getWidth(); j++) {
				z=0;
				while (iterationTurnedOffRobots[z] != null) {
					if (iterationTurnedOffRobots[z].getX() == j && iterationTurnedOffRobots[z].getY() == i) {
						taken[j] = true;
						k=z;
						while (iterationTurnedOffRobots[k+1] != null) {
							iterationTurnedOffRobots[k] = iterationTurnedOffRobots[k+1];
							k++;
						}
						iterationTurnedOffRobots[k] = null;
						break;
					}
					z++;
				}
			}
			
			allTaken = allBooleansAreTrue(taken);
			if (allTaken) {
				rows +=1;
				allTaken=false;
				i--;
				
			}
			for (int l=0; l<taken.length;l++) {
				taken[l] = false;
			}
			if (i== World.getHeight()-1) {
				if (iterationTurnedOffRobots[0] != null) {
					i=0;
				}
			}
			
		}
		points = calculatePoints(rows + gecheckteRows);
		gecheckteRows = 0;
		return points;
	}
	
	private boolean allBooleansAreTrue (boolean[] array) {
		  for (boolean bool : array) {
		    if (!bool) {
		    	return false;
		    }
		  }
		  return true;
		}
	//Punkte berechnen
	private int calculatePoints(int rows) {
		if (rows==0) {
			return 0;
		} else if (rows==1){
			return 1000;
		} else {
			return 2* calculatePoints(rows-1);
			
		}
		
	}
	
	//Prüft für eine übergebene Reihe, ob alle Roboter gleich ausgerichtet sind.
	public boolean checkDirection (int row) {
		SquareRobot[] robots = new SquareRobot[stones.length * 4];
		int z = 0;
		int zaehler = 0;
		while (stones[z] != null) {
			for (int j=0; j<stones[z].getSquareRobots().length; j++) {
				if (stones[z].getSquareRobots()[j].getY() == row) {
					if (stones[z].getSquareRobots()[j].isTurnedOn()) {
						robots[zaehler] = stones[z].getSquareRobots()[j];
						zaehler++;
					}
				}
			}
			z++;
		}
		int i=0;
		while (robots[i] != null) {
			if (robots[i].getDirection() != robots[0].getDirection()) {
				return false;
			}
			i++;
		}
		return true;
	}

	// ----------------------------------------------------------------------------------------------------
	// //
	
	private int gecheckteRows = 0;

	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	private final Random random;

	private final int width, height;
	private final Stone[] stones;
	private int iterations = 0, rows = 0, points = 0;
	private boolean running = true;

	/**
	 * Constructs a {@link TetrisGame}.
	 * 
	 * @param width      the width of the world
	 * @param height     the height of the world
	 * @param stoneCount the count of stones
	 * @param seed       the seed of the game or {@code null} for random seed
	 */
	public TetrisGame(int width, int height, int stoneCount, Integer seed) {
		World.setSize(this.width = width, this.height = height);
		MyWorld.setTetrisGame(this);
		this.stones = new Stone[stoneCount];
		random = seed != null ? new Random(seed) : new Random();
	}

	/**
	 * Returns the width of the world.
	 * 
	 * @return the width of the world
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the world.
	 * 
	 * @return the height of the world
	 */
	public int getHeight() {
		return height;
	}
	

	/**
	 * Returns the array containing all stones.
	 * 
	 * @return the stone array
	 */
	public Stone[] getStoneArray() {
		return stones;
	}

	/**
	 * Adds the specified number of points to the score of this game.
	 * 
	 * @param amount the amount of points
	 */
	public void addPoints(int amount) {
		this.points += amount;
	}

	/**
	 * Adds the specified amount of removed rows to the score of this game.
	 * 
	 * @param rowAmount the amount of removed rows
	 */
	public void addRows(int rowAmount) {
		this.rows += rowAmount;
	}

	/**
	 * Returns the total amount of points scored in this game.
	 * 
	 * @return the total amount of points
	 */
	public int getTotalPoints() {
		return points;
	}

	/**
	 * Returns the total amount of removed rows scored in this game.
	 * 
	 * @return the total amount of removed rows
	 */
	public int getTotalRows() {
		return rows;
	}

	/**
	 * Returns the number of the current iteration.
	 * 
	 * @return the number of the current iteration
	 */
	public int getIteration() {
		return iterations;
	}

	/**
	 * Handles the given key input.
	 * 
	 * @param key the key input
	 */
	public void handleInput(byte key) {
		executor.execute(() -> stones[iterations].handleKeyInput(key));
	}

	/**
	 * Returns whether this game is running.
	 * 
	 * @return {@code true} if this game is running or {@code false} if not
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Sets whether this game is running or not.
	 * 
	 * @param isRunning whether this game is running or not
	 */
	public void setRunning(boolean isRunning) {
		this.running = isRunning;
	}

	/**
	 * Returns the count of stones in this game.
	 * 
	 * @return the count of stones
	 */
	public int getStoneCount() {
		return iterations + 1;
	}

	/**
	 * Generates a stone of a randomly selected type and returns it.
	 * 
	 * @return the stone
	 */
	public Stone generateRandomStone() {
		int number = random.nextInt(7);
		switch (number) {
		case 0:
			return new I(this, getWidth() / 2, getHeight() - 1);
		case 1:
			return new J(this, getWidth() / 2, getHeight() - 1);
		case 2:
			return new L(this, getWidth() / 2, getHeight() - 1);
		case 3:
			return new O(this, getWidth() / 2, getHeight() - 1);
		case 4:
			return new S(this, getWidth() / 2, getHeight() - 1);
		case 5:
			return new T(this, getWidth() / 2, getHeight() - 1);
		default:
			return new Z(this, getWidth() / 2, getHeight() - 1);
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void run() {
		while (isRunning()) {
			executor.submit(this::doStep);
			try {
				int time = 500 - getStoneCount() * 2;
				if (time < 250)
					time = 250;
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void doStep() {
		try {
			if (!isRunning())
				return;
			if (stones[iterations] == null) {
				stones[iterations] = generateRandomStone();
				return;
			}
			boolean movable = stones[iterations].moveAllDown();
			if (!movable) {
				addRows(clearRows());
				addPoints(getPoints(getIteration()));
				if (iterations + 1 >= stones.length) {
					setRunning(false);
					World.getGlobalWorld().getGuiPanel().repaint();
				} else {
					stones[++iterations] = generateRandomStone();
					if (checkCollisions()) {
						setRunning(false);
						for (SquareRobot square : stones[iterations].getSquareRobots())
							square.turnOff();
						iterations--;
					}
				}
			}
		} catch (RuntimeException exception) {
			exception.printStackTrace();
		}
	}

	private boolean checkCollisions() {
		long collisions = Arrays.stream(stones)
				// filter non-null objects
				.filter(Objects::nonNull)
				// map to square robots
				.flatMap(s -> Arrays.stream(s.getSquareRobots()))
				// filter turned on robots
				.filter(SquareRobot::isTurnedOn)
				// collect occurrences
				.collect(Collectors.groupingBy(r -> new Point(r.getX(), r.getY()), Collectors.counting()))
				// count collisions
				.values().stream().filter(c -> c > 1).count();
		if (collisions > 0)
			System.err.println("Collision -> Game Over");
		return collisions > 0;
	}

}
