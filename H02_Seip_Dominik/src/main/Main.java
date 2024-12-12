package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.TreeMap;

import fopbot.GuiPanel;
import fopbot.World;
import robot.SquareColor;

/*
 * Version 1.1.2
 */
public class Main {

	// Stone Generation Seed
	private static final Integer SEED = 7;

	// World Width
	private static final int WIDTH = 8;

	// World Height
	private static final int HEIGHT = 12;

	// Maximum Stone Count
	private static final int STONE_COUNT = 10000;

	// DO NOT CHANGE
	private static final String INFO_MESSAGE = "%d BLOCKS \u00B7 %d ROWS \u00B7 %d POINTS \u00B7 GAME %s";
	private static final String FILE_PATH = "res/%s.png";
	private static final Map<Integer, Byte> KEY_MAP = new TreeMap<>();
	private static final TetrisGame GAME_INSTANCE = new TetrisGame(WIDTH, HEIGHT, STONE_COUNT, SEED);

	public static void main(String... arguments) {
		try {
			Main.initializeBot();
			Main.initalizeImages();
			Main.initializeKeys();
			World.setVisible(true);
			Thread thread = new Thread(GAME_INSTANCE);
			thread.start();
		} catch (FileNotFoundException e) {
			System.err.println("could not load graphics");
		}
	}

	private static void initializeBot() {
		World.reset();
		World.setSize(WIDTH, HEIGHT);
		World.setDelay(0);
		GuiPanel panel = new GuiPanel(World.getGlobalWorld()) {

			private static final long serialVersionUID = 0L;

			@Override
			public void paintComponent(Graphics graphics) {
				Graphics2D g = (Graphics2D) graphics;
				int tetrominos = GAME_INSTANCE.getStoneCount();
				int rows = GAME_INSTANCE.getTotalRows();
				int points = GAME_INSTANCE.getTotalPoints();
				String state = GAME_INSTANCE.isRunning() ? "RUNNING" : "OVER";
				String text = String.format(INFO_MESSAGE, tetrominos, rows, points, state);
				int fontHeight = g.getFontMetrics().getHeight();
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, getWidth(), fontHeight);
				g.setColor(Color.WHITE);
				g.drawString(text, 24, g.getFontMetrics().getAscent());
				AffineTransform t = AffineTransform.getTranslateInstance(0, fontHeight);
				t.scale(1d * getWidth() / getUnscaledSize().width,
						1d * (getHeight() - fontHeight) / getUnscaledSize().height);
				g.setTransform(t);
				super.draw(g);
			}

		};

		Dimension size = panel.getPreferredSize();
		size.height += panel.getFontMetrics(panel.getFont()).getHeight();
		World.getGlobalWorld().setGuiPanel(panel);
		World.setVisible(true);
		World.getGlobalWorld().getGuiPanel().addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				GAME_INSTANCE.handleInput(KEY_MAP.getOrDefault(e.getKeyCode(), (byte) -1));
			}

		});

		World.getGlobalWorld().getGuiPanel().setFocusable(true);
		World.getGlobalWorld().getGuiPanel().requestFocusInWindow();
	}

	private static void initalizeImages() throws FileNotFoundException {
		for (SquareColor color : SquareColor.values()) {
			World.getGlobalWorld().setAndLoadRobotImagesById(color.name(),
					new FileInputStream(new File(String.format(FILE_PATH, color.name().toLowerCase()))),
					new FileInputStream(new File(String.format(FILE_PATH, "empty"))), 0, 0);
		}
	}

	private static void initializeKeys() {
		KEY_MAP.put(KeyEvent.VK_W, (byte) 0);
		KEY_MAP.put(KeyEvent.VK_A, (byte) 1);
		KEY_MAP.put(KeyEvent.VK_S, (byte) 2);
		KEY_MAP.put(KeyEvent.VK_D, (byte) 3);
		KEY_MAP.put(KeyEvent.VK_UP, (byte) 0);
		KEY_MAP.put(KeyEvent.VK_LEFT, (byte) 1);
		KEY_MAP.put(KeyEvent.VK_DOWN, (byte) 2);
		KEY_MAP.put(KeyEvent.VK_RIGHT, (byte) 3);
		KEY_MAP.put(KeyEvent.VK_SPACE, (byte) 4);
	}
	
	

}
