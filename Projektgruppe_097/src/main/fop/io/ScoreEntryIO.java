package fop.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import fop.model.ScoreEntry;


/**
 * 
 * Wird genutzt, um {@link ScoreEntry} Objekte zu schreiben und zu lesen.<br>
 * <br>
 * Es handelt sich um die Datei {@value #PATH}.<br>
 * Mit {@link #loadScoreEntries()} werden die Elemente gelesen.<br>
 * Mit {@link #writeScoreEntries(List)} werden die Elemente geschrieben.
 *
 */
public final class ScoreEntryIO {
	
	/** Der Pfad zur ScoreEntry Datei */
	private static String PATH = "highscores.txt";
	
	private ScoreEntryIO() {}
	
	/**
	 * Liest eine Liste von {@link ScoreEntry} Objekten aus der Datei {@value #PATH}.<br>
	 * Die Liste enthält die Elemente in der Reihenfolge, in der sie in der Datei vorkommen.<br>
	 * Ungültige Einträge werden nicht zurückgegeben.
	 * @return die ScoreEntry Objekte
	 */
	public static List<ScoreEntry> loadScoreEntries() {
		// TODO Aufgabe 4.2.2
		List<ScoreEntry> result = List.of();
		//Liest Datei aus und wandelt in Score-Entry-Objekte um
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(PATH));) {
			String line = bufferedReader.readLine();
		    while(line != null) {
		        result.add(ScoreEntry.read(line));
		        line = bufferedReader.readLine();
		    }
		} catch (FileNotFoundException e) {
			return List.of();
		} catch (IOException e) {
			return List.of();
		}
		return result;
	}
	
	/**
	 * Schreibt eine Liste von {@link ScoreEntry} Objekten in die Datei {@value #PATH}.<br>
	 * Die Elemente werden in der Reihenfolge in die Datei geschrieben, in der sie in der Liste vorkommen.
	 * @param scoreEntries die zu schreibenden ScoreEntry Objekte
	 */
	public static void writeScoreEntries(List<ScoreEntry> scoreEntries) {
		// TODO Aufgabe 4.2.2
		try (PrintWriter printWriter = new PrintWriter(PATH)) {
			for (ScoreEntry entry : scoreEntries) {
				entry.write(printWriter);
			}
		} catch (FileNotFoundException e) {
			
		}
		
	}
	
	/**
	 * Schreibt das übergebene {@link ScoreEntry} Objekt an der korrekten Stelle in die Datei {@value #PATH}.<br>
	 * Die Elemente sollen absteigend sortiert sein. Wenn das übergebene Element dieselbe Punktzahl wie ein
	 * Element der Datei hat, soll das übergebene Element danach eingefügt werden.
	 * @param scoreEntry das ScoreEntry Objekt, das hinzugefügt werden soll
	 */
	public static void addScoreEntry(ScoreEntry scoreEntry) {
		// TODO Aufgabe 4.2.3^
		//Laden der ScoreEntries
		List<ScoreEntry> scores = loadScoreEntries();
		//Einfügen des neuen Scores an der richtigen Stelle
		int index = -1;
		int i = 0;
		while (scores.get(i)!=null) {
			//Kleiner 0 um zu gewährleisten, dass hinter einem ELement mit gleichem Score eingefügt -> nochmal checken, ob < wirklich richt ist
			if (scoreEntry.compareTo(scores.get(i))<0) {
				index = i;
				break;
			}
		}
		scores.add(index, scoreEntry);
		//Schreiben der ScoreEntries
		writeScoreEntries(scores);
	}
	
}
