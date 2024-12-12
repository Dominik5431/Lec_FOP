package life;

import java.awt.Color;

import life.ui.GameOfLifeLauncher;
import life.automaton.ModularAutomatonBuilder;
import life.automaton.neighborhood.DonutPadding;
import life.automaton.neighborhood.MooreNeighborhood;
import life.automaton.neighborhood.SamePadding;

// import life.automaton.ModularAutomatonBuilder;

// import life.automaton.rules.GameOfLifeRules;
// import life.automaton.rules.HighlifeRules;

// import life.automaton.neighborhood.MooreNeighborhood;
import life.automaton.neighborhood.VonNeumannNeighborhood;

import life.automaton.neighborhood.ZeroPadding;
// import life.automaton.neighborhood.SamePadding;
// import life.automaton.neighborhood.DonutPadding;
import life.automaton.rules.GameOfLifeRules;
import life.automaton.rules.HighlifeRules;
import life.reader.PlainTextFormat;
import life.reader.CellsFormat;
import life.reader.RLEFormat;

import life.ui.color.OnOffColorMap;
import life.ui.color.TransparencyColorMap;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
@SuppressWarnings("unused")
class GameOfLife {
    public static void main(String[] args) {
        var launcher = new GameOfLifeLauncher(new ModularAutomatonBuilder());

        launcher.registerNeighborhood(
            "Moore", new MooreNeighborhood());
        launcher.registerNeighborhood(
            "Von Neumann", new VonNeumannNeighborhood());

        launcher.registerPadding("Zero", new ZeroPadding());
        launcher.registerPadding("Same", new SamePadding());
        launcher.registerPadding("Donut", new DonutPadding());

        launcher.registerRules("Game of Life", new GameOfLifeRules());
        launcher.registerRules("Highlife", new HighlifeRules());

        launcher.registerColorMap(
            "Matrix", new OnOffColorMap(Color.GREEN, Color.BLACK));
        launcher.registerColorMap(
            "Snow", new OnOffColorMap(Color.WHITE, Color.BLACK));
        launcher.registerColorMap(
            "Rot", new TransparencyColorMap(Color.RED));
        launcher.registerColorMap(
            "Grün", new TransparencyColorMap(Color.GREEN));
        launcher.registerColorMap(
            "Blau", new TransparencyColorMap(Color.BLUE));

        launcher.registerFileFormat("txt", new PlainTextFormat());
        launcher.registerFileFormat("cells", new CellsFormat());
        launcher.registerFileFormat("rle", new RLEFormat());

        launcher.start();
    }
}
