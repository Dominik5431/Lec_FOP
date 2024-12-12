package life.automaton;

import life.automaton.neighborhood.Neighborhood;
import life.automaton.neighborhood.Padding;
import life.automaton.rules.UpdateRules;
import life.automaton.state.AutomatonState;

/**
 * Dieses Interface definiert die Methoden zur sukzessiven Komposition von
 * zellulären Automaten mit frei wählbaren Regeln, Nachbarschaften und Paddings.
 *
 * @author Kim Berninger
 * @version 1.0.2
 *
 * @see CellularAutomaton
 * @see AutomatonState
 * @see UpdateRules
 * @see Neighborhood
 * @see Padding
 */
public interface CellularAutomatonBuilder {
    /**
     * Legt den Anfangszustand der Automaten, die mit diesem Builder erzeugt
     * werden sollen, fest.
     *
     * @param state der Anfangszustand der zu erzeugenden Automaten
     *
     * @return diesen Builder
     */
    CellularAutomatonBuilder addState(AutomatonState state);

    /**
     * Legt das Regelwerk der Automaten, die mit diesem Builder erzeugt werden
     * sollen, fest.
     *
     * @param rules das Regelwerk der zu erzeugenden Automaten
     *
     * @return diesen Builder
     */
    CellularAutomatonBuilder addRules(UpdateRules rules);

    /**
     * Leght die Nachbarschaftsermittlung der Automaten, die mit diesem Builder
     * erzeugt werden sollen, fest.
     *
     * @param neighborhood die Nachbarschaftsermittlung der zu erzeugenden
     *                     Automaten
     *
     * @return diesen Builder
     */
    CellularAutomatonBuilder addNeighborhood(Neighborhood neighborhood);

    /**
     * Leght die Padding-Strategie der Automaten, die mit diesem Builder erzeugt
     * werden sollen, fest.
     *
     * @param padding die Padding-Strategie der zu erzeugenden Automaten
     *
     * @return diesen Builder
     */
    CellularAutomatonBuilder addPadding(Padding padding);

    /**
     * Erzeugt einen {@link CellularAutomaton} mit den Komponenten, die zuvor zu
     * diesem Builder hinzugefügt wurden.
     *
     * @return den fertig zusammgengebauten Automaten
     */
    CellularAutomaton buildAutomaton();
}
