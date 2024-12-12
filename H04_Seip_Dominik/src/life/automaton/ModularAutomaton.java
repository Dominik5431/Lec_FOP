package life.automaton;


import life.automaton.neighborhood.Neighborhood;
import life.automaton.neighborhood.Padding;
import life.automaton.rules.UpdateRules;
import life.automaton.state.ArrayAutomatonState;
import life.automaton.state.AutomatonState;

public class ModularAutomaton implements CellularAutomaton {
	
	private AutomatonState initialState;
	private AutomatonState currentState;
	private UpdateRules rules;
	private Neighborhood neighborhood;
	private Padding padding;

	public ModularAutomaton(AutomatonState initialState, UpdateRules rules, Neighborhood neighborhood, Padding padding) {
		this.initialState = initialState;
		this.currentState = initialState.copy();
		this.rules = rules;
		this.neighborhood = neighborhood;
		this.padding = padding;		
	}
	/**
	 * Überprüft, ob Methode im aktuellen Zustand am Leben ist.
	 * @return true/false: am Leben oder nicht
	 */
	@Override
	public boolean isAlive(int row, int col) {
		// TODO Auto-generated method stub
		return currentState.isAlive(row, col);
	}
	/**
	 * @return Höhe des Spielfelds
	 */
	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return currentState.getHeight();
	}
	/**
	 * @return Breite des Spielfelds
	 */

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return currentState.getWidth();
	}
	/**
	 * Diese Methode berechnet den nächsten Zustand
	 */

	@Override
	public void update() {
		// TODO Auto-generated method stub
		AutomatonState copy = currentState.copy();
		boolean living;
		for (int i = 0; i<currentState.getHeight(); i++) {
			for (int j= 0; j<currentState.getWidth(); j++) {
				if (copy.isAlive(i, j)) {
					living = rules.getNextCellState(padding.isAlive(currentState, i, j), neighborhood.getNumberOfAliveNeighbors(currentState, i, j, padding));
					if (!living) {
						copy.kill(i, j);
					}
				} else {
					living = rules.getNextCellState(padding.isAlive(currentState, i, j), neighborhood.getNumberOfAliveNeighbors(currentState, i, j, padding));
					if (living) { 
						copy.giveBirth(i, j);
					}
				}
				
			}
			
		}
		currentState = copy.copy();
	}
	
	/**
	 * Diese Methode setzt den Zustand auf den Anfangszustand zurück
	 */

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		currentState = initialState.copy();
	}

}
