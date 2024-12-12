package life.automaton.rules;

public class HighlifeRules implements UpdateRules {

	/**
	 * @return: true/false: Wird die Zelle im nächsten Zustand lebendig oder tot sein (Überprüfung anhand der HighLife Regeln)?
	 */
	@Override
	public boolean getNextCellState(boolean isAlive, int aliveNeighbors) {
		// TODO Auto-generated method stub
		return (((aliveNeighbors == 3) || (aliveNeighbors == 6)) && !isAlive) || (isAlive && ((aliveNeighbors == 2) || (aliveNeighbors == 3)));
	}

}
