package life.automaton.neighborhood;

import life.automaton.state.AutomatonState;

public class SamePadding implements Padding {

	/**
	 * Diese Methode überprüft anhand des SamePaddings, ob eine Zelle lebendig oder tot ist.
	 * 
	 * @param state: zu prüfender Zustand
	 * @param row: zu überprüfende Zeile
	 * @param col: zu überprüfende Spalte
	 */
	@Override
	public boolean isAlive(AutomatonState state, int row, int col) {
		// TODO Auto-generated method stub
		//Reihe zurückführen
		if (row >= state.getHeight()) {
			row = state.getHeight() -1;
		} else if (row < 0) {
			row = 0;
		}
		//Spalten zurückführen
		if (col >= state.getWidth()) {
			col = state.getWidth() - 1;
		} else if (col < 0) {
			col = 0;
		}
		return state.isAlive(row, col);
	}

}
