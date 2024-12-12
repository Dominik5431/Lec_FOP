package life.automaton.neighborhood;

import life.automaton.state.AutomatonState;

public class MooreNeighborhood implements Neighborhood {

	/**
	 * /**
	 * Diese Methode überprüft, wie viele lebendige Nachbarn eine Zelle besitzt.
	 * 
	 * @param state: zu prüfender Zustand
	 * @param row: zu überprüfende Zeile
	 * @param col: zu überprüfende Spalte
	 * @param padding: zu benutzendes Padding-System
	 */
	 */
	@Override
	public int getNumberOfAliveNeighbors(AutomatonState state, int row, int col, Padding padding) {
		// TODO Auto-generated method stub
		int result = 0;
		for (int i=-1; i<=1; i++) {
			for (int j=-1; j<=1; j++) {
				if (i!=0 || j!=0) {
					result += padding.isAlive(state, row+i, col+j) ? 1 : 0;
				}
			}
		}
		return result;
	}

}
