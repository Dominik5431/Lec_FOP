package robot;

import fopbot.Robot;
import fopbot.World;
import main.Main;
import main.MyWorld;
import misc.Vector;
import stone.Stone;

public class SquareRobot extends Robot {

	///////////////
	// EXERCISES //
	///////////////
	
	/**
	 * Returns an array containing all possible movements of this square robot.
	 * @return an array containing all possible movements of this square robot
	 */
	public Vector[] getPossibleMovements() {		
		// TODO Exercise H1.1
		//Auslesen der relevanten Größen
		int maxLeft = this.getX();
		int maxRight = World.getWidth() - this.getX() - 1;
		int maxUp = World.getHeight() - this.getY() - 1;
		int maxDown = this.getY();
		int elements = (maxLeft + maxRight + 1)*(maxUp + maxDown +1);
		Vector[] checkVectors = new Vector[elements];
		Vector[] possibleMovementsTemp= new Vector[World.getWidth()*World.getHeight() - 1]; 
		
		//Einordnen der Vektoren, die zu überprüfen sind
		int left = -maxLeft;
		int up = maxUp;
		for (int i = 0; i<elements + 1; i++) {
			checkVectors[i] = new Vector(left,up);
			up--;
			if (up == -maxDown) {
				up = maxUp;
				left++;
			}
		}
		
		//Überprüfen der Bewegungen
		int zaehler = 0;
		for (int i = 0; i<=elements; i++) {
			if(!this.canMove(checkVectors[i]))
				continue;
			if (this.canMove(checkVectors[i]))
				possibleMovementsTemp[zaehler] = checkVectors[i];
				zaehler++;
		}
		//Einordnen in finalen Array
		int size = 0;
		while (possibleMovementsTemp[size] != null) {
			if (possibleMovementsTemp[size] != new Vector(0,0)) {
				size++;
			}
		}
		Vector[] possibleMovements = new Vector[size];
		for (int i=0; i<size; i++) {
			if (possibleMovementsTemp[i] != new Vector(0,0)) {
				possibleMovements[i] = possibleMovementsTemp[i];
			}
		}
		return possibleMovements;
	}

	/**
	 * Returns if this square robot can be moved by the given vector.
	 * @param vector the vector
	 * @return if this square robot can be move by the given vector
	 */
	public boolean canMove(Vector vector) {
		// TODO Exercise H1.2
		//Vektor aus Welt hinaus?
		if (this.getX() + vector.x < 0 || this.getX() + vector.x > World.getWidth()-1 || this.getY() + vector.y < 0 || this.getY() + vector.y > World.getHeight()-1) {			
			return false;
		}
		Stone[] stones = MyWorld.getTetrisGame().getStoneArray();
		int i=0;
		while (stones[i] != null) {
			SquareRobot[] robots = stones[i].getSquareRobots();
			//this in stones[i] enthalten?
			if (stones[i] == this.getRelatedStone()) {
				i++;
				continue;
			}
			for (int j=0; j<robots.length; j++) {
				if (this.getX() + vector.x == robots[j].getX() && this.getY() + vector.y == robots[j].getY()) {
					if (robots[j] != this && !robots[j].isTurnedOff()) {
						return false;
					}
				}
			}
			i++;
		}
		return true;
	}

	/**
	 * Rotates this square robot to the left.
	 */
	public void rotateLeft() {
		// TODO Exercise H2.1
		this.turnLeft();
		Vector[] rotationVectors = this.getRotationVectors();
		this.setField(this.getX() + rotationVectors[this.getRelatedStone().getDoneRotations()].x, this.getY() + rotationVectors[this.getRelatedStone().getDoneRotations()].y);
	}
	
	// ---------------------------------------------------------------------------------------------------- //
	
	private int turnOffIteration = -1;
	private final Stone relatedStone;
	private final Vector[] rotationVectors;
	

	/**
	 * Constructs a {@link SquareRobot}.
	 * @param stone           the related stone
	 * @param x               the x coordinate
	 * @param y               the y coordinate
	 * @param color           the color
	 * @param rotationVectors the rotation vectors
	 */
	public SquareRobot(Stone stone, int x, int y, SquareColor color, Vector... rotationVectors) {
		super(x, y);
		this.relatedStone = stone;
		this.rotationVectors = rotationVectors;
		setImageId(color.toString());
	}

	/**
	 * Returns the related stone of this square robot.
	 * @return the related stone of this square robot
	 */
	public Stone getRelatedStone() {
		return relatedStone;
	}

	/**
	 * Returns the rotation vector array of this square robot.
	 * @return the rotation vector array of this square robot.
	 */
	public Vector[] getRotationVectors() {
		return rotationVectors;
	}

	/**
	 * Returns the iteration in which this robot was turned off.<br>
	 * If the robot was not turned off, {@code -1} will be returned.
	 * @return the iteration in which this robot was turned off or {@code -1} if this robot was not turned off
	 */
	public int getTurnOffIteration() {
		return turnOffIteration;
	}

	/**
	 * Moves this robot by the given vector.
	 * @param vector the vector
	 */
	public void move(Vector vector) {
		setX(getX() + vector.x);
		setY(getY() + vector.y);
	}

	/**
	 * Moves this robot by the vector (0,-1).
	 */
	public void moveDown() {
		move(Vector.of(0, -1));
	}

	/**
	 * Moves this robot by the vector (-1,0).
	 */
	public void moveLeft() {
		move(Vector.of(-1, 0));
	}

	/**
	 * Moves this robot by the vector (1, 0).
	 */
	public void moveRight() {
		move(Vector.of(1, 0));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void turnOff() {
		if (isTurnedOff())
			return;
		super.turnOff();
		turnOffIteration = getRelatedStone().getRelatedGame().getIteration();
	}

}
