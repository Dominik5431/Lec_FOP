package life.automaton.initializer;

import life.automaton.state.ArrayAutomatonState;
import java.util.Random;
import life.automaton.state.AutomatonState;

public class RandomInitializer implements StateInitializer {

	 private int height;
	 private int width;
	 private double rate;
	 
	 
	 /**
	  * Konstruktor der Klasse. Setzt Attribute fest.
	  * @param height
	  * @param width
	  * @param rate
	  */
	 public RandomInitializer(int height, int width, double rate) {
	        this.height = height;
	        this.width = width;
	        this.rate = rate;
	    }
	
	 /**
	  * Erstellt einen ArrayAutomatonState und bewirkt, dass ca. 30% der Zellen zufällig verteilt am Leben sind.
	  * @return state: Erstellten Status
	  */
	@Override
	public AutomatonState createState() {
		// TODO Auto-generated method stub
		Random random = new Random();
		var state = new ArrayAutomatonState(height, width);
		for (int i=0; i<state.getHeight(); i++) {
			for (int j=0; j<state.getWidth(); j++) {
				double ran = random.nextDouble();
				if (ran < rate) {
					state.giveBirth(i, j);
				}
			}
		}
		return state;
	}

}
