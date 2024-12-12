package life.automaton.neighborhood;

import life.automaton.state.AutomatonState;

public class DonutPadding implements Padding {

	/**
	 * Diese Methode überprüft anhand des DonutPaddings, ob eine Zelle lebendig oder tot ist.
	 * 
	 * @param state: zu prüfender Zustand
	 * @param row: zu überprüfende Zeile
	 * @param col: zu überprüfende Spalte
	 */
	@Override
	public boolean isAlive(AutomatonState state, int row, int col) {
		// TODO Auto-generated method stub
		//Reihe zurückführen
		while (row >= state.getHeight()) {
			row -= state.getHeight();
		}
		while (row < 0) {
			row += state.getHeight();
		}
		//Spalten zurückführen
		while (col >= state.getWidth()) {
			col -= state.getWidth();
		}
		while (col < 0) {
			col += state.getWidth();
		}
		return state.isAlive(row, col);
	}

}
