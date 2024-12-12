package life.ui;

import java.util.Map;
import java.util.TreeMap;
import java.util.Hashtable;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.MutableComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;

import javax.swing.SwingConstants;

import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

import life.automaton.CellularAutomatonBuilder;

import life.automaton.initializer.StateInitializer;
import life.automaton.initializer.FileInitializer;

import life.automaton.rules.UpdateRules;

import life.automaton.neighborhood.Neighborhood;
import life.automaton.neighborhood.Padding;
import life.reader.AutomatonFileFormat;
import life.reader.AutomatonFileReader;

import life.ui.color.CellColorMap;

import static javax.swing.GroupLayout.Alignment;

/**
 * Ein Dialog zum Starten des Game of Life und ähnlichen Simulationen.
 *
 * @author Kim Berninger
 * @version 1.0.2
 */
public class GameOfLifeLauncher extends JFrame {
    private static final long serialVersionUID = -2577615374907129635L;

    private static final String WINDOW_TITLE = "Game of Life";

    private static final String RANDOM_BUTTON_LABEL = "Zufällige Simulation";
    private static final String RANDOM_SLIDER_LABEL = "Bevölkerungsdichte:";
    private static final String RANDOM_HEIGHT_LABEL = "Höhe:";
    private static final String RANDOM_WIDTH_LABEL = "Breite:";

    private static final String RULES_LABEL = "Regeln:";
    private static final String NEIGHBORHOOD_LABEL = "Nachbarschaft:";
    private static final String PADDING_LABEL = "Randbehandlung:";
    private static final String COLOR_MAP_LABEL = "Farbschema:";

    private final CellularAutomatonBuilder builder;

    private final AutomatonFileReader stateReader;

    private final Map<String, UpdateRules> rulesLookup;
    private final Map<String, Neighborhood> neighborhoodLookup;
    private final Map<String, Padding> paddingLookup;
    private final Map<String, CellColorMap> colorMapLookup;

    private final MutableComboBoxModel<String> rulesModel;
    private final MutableComboBoxModel<String> neighborhoodModel;
    private final MutableComboBoxModel<String> paddingModel;
    private final MutableComboBoxModel<String> colorMapModel;

    private final SpinnerNumberModel heightModel;
    private final SpinnerNumberModel widthModel;
    private final BoundedRangeModel rateModel;

    /**
     * Erzeugt einen {@code GameOfLifeLauncher}.
     *
     * @param builder der Builder, mit dem die zellulären Automaten erzeugt
     *                werden sollen, deren Simulationen dieser Dialog startet
     *
     * @see #start()
     */
    public GameOfLifeLauncher(CellularAutomatonBuilder builder) {
        super(WINDOW_TITLE);

        this.builder = builder;

        stateReader = new AutomatonFileReader();

        rulesLookup = new TreeMap<>();
        neighborhoodLookup = new TreeMap<>();
        paddingLookup = new TreeMap<>();
        colorMapLookup = new TreeMap<>();

        rulesModel = new DefaultComboBoxModel<>();
        neighborhoodModel = new DefaultComboBoxModel<>();
        paddingModel = new DefaultComboBoxModel<>();
        colorMapModel = new DefaultComboBoxModel<>();

        heightModel = new SpinnerNumberModel(64, 1, 400, 1);
        widthModel = new SpinnerNumberModel(128, 1, 400, 1);
        rateModel = new DefaultBoundedRangeModel(20, 0, 0, 100);

        var contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

        contentPane.add(createLauncherPanel());
        contentPane.add(new JSeparator(SwingConstants.VERTICAL));
        contentPane.add(createOptionsPanel());
    }

    /**
     * Fügt der UI einen neuen Regelsatz zur Auswahl hinzu.
     * Falls bereits ein Regelsatz mit demselben Namen existiert, wird dieser
     * durch den neuen ersetzt.
     * 
     * @param name  der Name des neuen Regelsatzes
     * @param rules der Regelsatz, der hinzugefügt werden soll
     */
    public void registerRules(String name, UpdateRules rules) {
        rulesLookup.put(name, rules);
        rulesModel.addElement(name);
    }

    /**
     * Fügt der UI eine neue Nachbarschaftsermittlung zur Auswahl hinzu.
     * Falls bereits eine Nachbarschaftsermittlung mit demselben Namen
     * existiert, wird diese durch die neue ersetzt.
     * 
     * @param name         der Name der neuen Nachbarschaftsermittlung
     * @param neighborhood die Nachbarschaftsermittlung, die hinzugefügt werden
     *                     soll
     */
    public void registerNeighborhood(String name, Neighborhood neighborhood) {
        neighborhoodLookup.put(name, neighborhood);
        neighborhoodModel.addElement(name);
    }

    /**
     * Fügt der UI eine neue Padding-Strategie zur Auswahl hinzu.
     * Falls bereits eine Padding-Strategie mit demselben Namen existiert, wird
     * diese durch die neue ersetzt.
     * 
     * @param name    der Name der neuen Padding-Strategie
     * @param padding die Padding-Strategie, die hinzugefügt werden soll
     */
    public void registerPadding(String name, Padding padding) {
        paddingLookup.put(name, padding);
        paddingModel.addElement(name);
    }

    /**
     * Fügt der UI ein neues Farbschema zur Auswahl hinzu.
     * Falls bereits ein Farbschema mit demselben Namen existiert, wird dieses
     * durch das neue ersetzt.
     * 
     * @param name     der Name des neuen Farbschemas
     * @param colorMap das Farbschema, das hinzugefügt werden soll
     */
    public void registerColorMap(String name, CellColorMap colorMap) {
        colorMapLookup.put(name, colorMap);
        colorMapModel.addElement(name);
    }

    /**
     * Fügt dem {@link AutomatonFileReader}, der verwendet wird um Zustände von
     * zellulären Automaten einzulesen, ein neues Dateiformat hinzu.
     * Falls bereits ein Format mit derselben Dateiendung existiert, wird dieses
     * durch das neue ersetzt.
     *
     * @param extension die Endung der der Dateien mit dem neuen Format
     * @param format    das {@code AutomatonFileFormat}, das verwendet wird um
     *                  die entsprechenden Dateien einzulesen
     */
    public void registerFileFormat(
        String extension, AutomatonFileFormat format
    ) {
        stateReader.registerFormat(extension, format);
    }

    /**
     * Startet das Programm und zeigt das Dialogfenster dieses
     * {@code GameOfLifeLauncher}s an.
     */
    public void start() {
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void launchSimulation(StateInitializer initializer) {
        builder.addState(initializer.createState());

        builder.addRules(rulesLookup.get(rulesModel.getSelectedItem()));

        builder.addNeighborhood(
            neighborhoodLookup.get(neighborhoodModel.getSelectedItem()));

        builder.addPadding(paddingLookup.get(paddingModel.getSelectedItem()));

        var automaton = builder.buildAutomaton();
        var colorMap = colorMapLookup.get(colorMapModel.getSelectedItem());

        var frame = new GameOfLifeFrame(automaton, colorMap);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createLauncherPanel() {
        var launcherPanel = new JPanel();
        launcherPanel.setLayout(
            new BoxLayout(launcherPanel, BoxLayout.PAGE_AXIS));

        launcherPanel.add(createRandomLauncherPanel());
        launcherPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        launcherPanel.add(createFileLauncherPanel());

        return launcherPanel;
    }

    private JPanel createRandomLauncherPanel() {
        var heightLabel = new JLabel(RANDOM_HEIGHT_LABEL);
        var widthLabel = new JLabel(RANDOM_WIDTH_LABEL);

        var heightSpinner = new JSpinner(heightModel);
        var widthSpinner = new JSpinner(widthModel);

        var rateLabel = new JLabel(RANDOM_SLIDER_LABEL);

        var rateSlider = new JSlider(rateModel);
        rateSlider.setLabelTable(new Hashtable<>(Map.of(
            rateModel.getMinimum(),
            new JLabel("0 %"),
            (rateModel.getMaximum() - rateModel.getMinimum()) / 2,
            new JLabel("50 %"),
            rateModel.getMaximum(),
            new JLabel("100 %"))));
        rateSlider.setPaintLabels(true);
        rateSlider.setMajorTickSpacing(10);
        rateSlider.setPaintTicks(true);

        var randomLauncherButton = new JButton(RANDOM_BUTTON_LABEL);

        randomLauncherButton.addActionListener(e -> {
            var height = heightModel.getNumber().intValue();
            var width = widthModel.getNumber().intValue();

            var rate = (double)
                (rateModel.getValue() - rateModel.getMinimum()) /
                (rateModel.getMaximum() - rateModel.getMinimum());

            try {
                var initializer = Class
                    .forName("life.automaton.initializer.RandomInitializer")
                    .asSubclass(StateInitializer.class)
                    .getConstructor(int.class, int.class, double.class)
                    .newInstance(height, width, rate);

                launchSimulation(initializer);
            } catch (ReflectiveOperationException ex) {
                System.err.println(
                    "RandomInitializer konnte nicht erstellt werden");
            }

        });

        var panel = new JPanel();
        var layout = new GroupLayout(panel);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(heightLabel)
                    .addComponent(heightSpinner))
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(widthLabel)
                    .addComponent(widthSpinner)))
            .addGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(rateLabel)
                .addComponent(rateSlider))
            .addComponent(randomLauncherButton));

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(heightLabel)
                .addComponent(widthLabel))
            .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(heightSpinner)
                .addComponent(widthSpinner))
            .addComponent(rateLabel)
            .addComponent(rateSlider)
            .addComponent(randomLauncherButton));

        panel.setLayout(layout);

        return panel;
    }

    private JPanel createFileLauncherPanel() {
        var fileChooserButton = new JButton("Datei öffnen");

        fileChooserButton.addActionListener(e -> {
            JFileChooser fileChooser;
            try {
                var statesDirectory = Path.of(
                    getClass().getResource("").toURI())
                    .getParent().resolve("states");
                fileChooser = new JFileChooser(statesDirectory.toFile());
            } catch (URISyntaxException e1) {
                fileChooser = new JFileChooser();
            }

            var extensionFilter = new FileNameExtensionFilter(
                "Automaton State Text Files",
                stateReader.getSupportedFormats());
            fileChooser.setFileFilter(extensionFilter);

            var returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    launchSimulation(
                        new FileInitializer(
                            stateReader,
                            fileChooser.getSelectedFile()));
                } catch (UncheckedIOException ex) {
                    JOptionPane.showMessageDialog(this,
                        String.format(
                            "Die Datei %s kann nicht gelesen werden (%s)",
                            fileChooser.getSelectedFile().getName(),
                            ex.getMessage()),
                        "Fehler beim Öffnen der Datei",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        var panel = new JPanel(new BorderLayout());
        panel.add(fileChooserButton, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createOptionsPanel() {
        var panel = new JPanel();
        var layout = new GroupLayout(panel);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        var rulesLabel = new JLabel(RULES_LABEL);
        var neighborhoodLabel = new JLabel(NEIGHBORHOOD_LABEL);
        var paddingLabel = new JLabel(PADDING_LABEL);
        var colorMapLabel = new JLabel(COLOR_MAP_LABEL);

        var rulesComboBox = new JComboBox<>(rulesModel);
        var neighborhoodComboBox = new JComboBox<>(neighborhoodModel);
        var paddingComboBox = new JComboBox<>(paddingModel);
        var colorMapComboBox = new JComboBox<>(colorMapModel);

        var horizontalGroup = layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(rulesLabel).addComponent(neighborhoodLabel)
                .addComponent(paddingLabel).addComponent(colorMapLabel))
            .addGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(rulesComboBox).addComponent(neighborhoodComboBox)
                .addComponent(paddingComboBox).addComponent(colorMapComboBox));

        var verticalGroup = layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(rulesLabel).addComponent(rulesComboBox))
            .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(neighborhoodLabel)
                .addComponent(neighborhoodComboBox))
            .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(paddingLabel).addComponent(paddingComboBox))
            .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(colorMapLabel).addComponent(colorMapComboBox));

        layout.setHorizontalGroup(horizontalGroup);
        layout.setVerticalGroup(verticalGroup);

        panel.setLayout(layout);

        return panel;
    }
}
