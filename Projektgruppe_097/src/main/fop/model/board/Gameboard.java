package fop.model.board;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import fop.model.cards.CardAnchor;
import fop.model.cards.GoalCard;
import fop.model.cards.PathCard;
import fop.model.cards.StartCard;
import fop.model.graph.Graph;

/**
 * 
 * Stellt das Wegelabyrinth als Liste von Karten und als Graph dar.
 *
 */
public class Gameboard {
	
	protected final Map<Position, PathCard> board = new HashMap<>();
	protected final Graph<BoardAnchor> graph = new Graph<>();
	
	/**
	 * Erstellt ein leeres Wegelabyrinth und platziert Start- sowie Zielkarten.
	 */
	public Gameboard() {
		clear();
	}
	
	/**
	 * Zum Debuggen kann hiermit der Graph ausgegeben werden.<br>
	 * Auf {@code http://webgraphviz.com/} kann der Code dargestellt werden.
	 */
	public void printGraph() {
		graph.toDotCode().forEach(System.out::println);
	}
	
	/**
	 * Leert das Wegelabyrinth.
	 */
	public void clear() {
		board.clear();
		graph.clear();
	}
	
	// add, remove //
	
	/**
	 * Setzt eine neue Wegekarte in das Wegelabyrinth.<br>
	 * Verbindet dabei alle Kanten des Graphen zu benachbarten Karten,
	 * sofern diese einen Knoten an der benachbarten Stelle besitzen.
	 * @param x x-Position im Wegelabyrinth
	 * @param y y-Position im Wegelabyrinth
	 * @param card die zu platzierende Wegekarte
	 */
	public void placeCard(int x, int y, PathCard card) {
		// TODO Aufgabe 4.1.4
		//Karte zum Board hinzufügen
		Position pos = Position.of(x, y);
		board.put(pos, card);
		//Knoten und Kanten zum Graph hinzufügen
		//Knoten hinzufügen
		for (CardAnchor anc : card.getGraph().vertices()) {
			BoardAnchor boardAnc = BoardAnchor.of(x, y, anc);
			graph.addVertex(boardAnc);
			//Kanten hinzufügen: Bereits existierende
			for (CardAnchor nxt : card.getGraph().getAdjacentVertices(anc)) {
				graph.addEdge(boardAnc, BoardAnchor.of(x, y, nxt));
			}
			//Kanten hinzufügen: Neue Kanten hinzufügen
			CardAnchor opposite = anc.getOppositeAnchor();
			graph.addEdge(boardAnc, BoardAnchor.of(anc.getAdjacentPosition(pos), opposite));
		}
		// stehen lassen
		// check for goal cards
		checkGoalCards();
	}
	
	/**
	 * Prüft, ob eine Zielkarte erreichbar ist und dreht diese gegebenenfalls um.
	 */
	private void checkGoalCards() {
		for (Entry<Position, PathCard> goal : board.entrySet().stream().filter(e -> e.getValue().isGoalCard()).collect(Collectors.toList())) {
			int x = goal.getKey().x();
			int y = goal.getKey().y();
			if (existsPathFromStartCard(x, y)) {
				GoalCard goalCard = (GoalCard) goal.getValue();
				if (goalCard.isCovered()) {
					// turn card
					goalCard.showFront();
					// generate graph to match all neighbor cards
					goalCard.generateGraph(card -> doesCardMatchItsNeighbors(x, y, card));
					// connect graph of card
					placeCard(x, y, goalCard);
				}
			}
		}
		
	}
	
	/**
	 * Entfernt die Wegekarte an der übergebenen Position.
	 * @param x x-Position im Wegelabyrinth
	 * @param y y-Position im Wegelabyrinth
	 * @return die Karte, die an der Position lag
	 */
	public PathCard removeCard(int x, int y) {
		// TODO Aufgabe 4.1.5
		PathCard removedCard = board.remove(Position.of(x, y));
		for (CardAnchor anc : CardAnchor.values()) {
			//Behandelt Knoten am entsprechenden Ankerpunkt der Karte
			BoardAnchor boardAnc = BoardAnchor.of(Position.of(x, y), anc);
//			for (BoardAnchor vert : graph.getAdjacentVertices(boardAnc)) {
//				graph.removeEdge(vert, boardAnc);
//			}
			graph.removeVertex(boardAnc);
		}
		return removedCard;
	}
	
	
	// can //
	
	/**
	 * Gibt genau dann {@code true} zurück, wenn die übergebene Karte an der übergebene Position platziert werden kann.
	 * @param x x-Position im Wegelabyrinth
	 * @param y y-Position im Wegelabyrinth
	 * @param card die zu testende Karte
	 * @return {@code true}, wenn die Karte dort platziert werden kann; sonst {@code false}
	 */
	public boolean canCardBePlacedAt(int x, int y, PathCard card) {
		return isPositionEmpty(x, y) && existsPathFromStartCard(x, y) && doesCardMatchItsNeighbors(x, y, card);
	}
	
	/**
	 * Gibt genau dann {@code true} zurück, wenn auf der übergebenen Position keine Karte liegt.
	 * @param x x-Position im Wegelabyrinth
	 * @param y y-Position im Wegelabyrinth
	 * @return {@code true}, wenn der Platz frei ist; sonst {@code false}
	 */
	private boolean isPositionEmpty(int x, int y) {
		// TODO Aufgabe 4.1.6
		return board.containsKey(Position.of(x, y));
	}
	
	/**
	 * Gibt genau dann {@code true} zurück, wenn die übergebene Position von einer Startkarte aus erreicht werden kann.
	 * @param x x-Position im Wegelabyrinth
	 * @param y y-Position im Wegelabyrinth
	 * @return {@code true}, wenn die Position erreichbar ist; sonst {@code false}
	 */
	private boolean existsPathFromStartCard(int x, int y) {
		// TODO Aufgabe 4.1.7
		//Finde Position der Startkarte
		Position startPos = null;
		PathCard startCard = null;
		for (Position pos : board.keySet()) {
			if (board.get(pos).isStartCard()) {
				startCard = board.get(pos);
				startPos = pos;
			}
		}
		//Wenn von irgendeinem CardAnchor der Startkarte zu irgendeinem CardAnchor der Zielkarte ein Pfad besteht, dann besthet zwischen beiden Karten ein Pfad
		for (CardAnchor anc : startCard.getGraph().vertices()) {
			for (CardAnchor anc2 : CardAnchor.values()) {
				//Überprüft, ob für Knoten der STartkarte Weg zu Knoten einer benachbarten Karte des Ziels mit Knoten in RIchtung der Zielposition existiert
				if (graph.hasPath(BoardAnchor.of(startPos, anc), BoardAnchor.of(anc2.getAdjacentPosition(Position.of(x, y)), anc2.getOppositeAnchor()))) {
					return true;
				}

			}
		}
		//Falls kein Pfad gefunden wurde -> return false;
		return false;
		
		
		// die folgende Zeile entfernen und durch den korrekten Wert ersetzen
//		return board.computeIfAbsent(CardAnchor.left.getAdjacentPosition(Position.of(x + 1, y)), p -> null) == null;
	}
	
	/**
	 * Gibt genau dann {@code true} zurück, wenn die übergebene Karte an der übergebene Position zu ihren Nachbarn passt.
	 * @param x x-Position im Wegelabyrinth
	 * @param y y-Position im Wegelabyrinth
	 * @param card die zu testende Karte
	 * @return {@code true}, wenn die Karte dort zu ihren Nachbarn passt; sonst {@code false}
	 */
	private boolean doesCardMatchItsNeighbors(int x, int y, PathCard card) {
		// TODO Aufgabe 4.1.8
		//Für alle CardAnchors der PathCard überprüfen:
		for (CardAnchor anc : card.getGraph().vertices()) {
			//Position der nächsten Karte (wird für alle CardAnchors wiederholt)
			Position posNext = anc.getAdjacentPosition(Position.of(x, y));
			//Art des gegenüberliegenden CardAnchors
			CardAnchor opposite = anc.getOppositeAnchor();
			//Überprüft ob gegenüberliegender CardAnchor bei benachbarter Karte existiert
			//Falls Wert null, existiert dort keine Karte -> Abfrage nicht nötig; analoges für Zielkarte
			if (board.get(posNext)!=null && !board.get(posNext).isGoalCard()) {
				if (!board.get(posNext).getGraph().vertices().contains(opposite)) {
					return false;
				}
			}
			
		}
		return true;
	}
	
	/**
	 * Gibt genau dann {@code true} zurück, wenn eine aufgedeckte Goldkarte im Wegelabyrinth liegt.
	 * @return {@code true} wenn eine Goldkarte aufgedeckt ist; sonst {@code false}
	 */
	public boolean isGoldCardVisible() {
		return board.values().stream().anyMatch(c -> c.isGoalCard() && ((GoalCard) c).getType() == GoalCard.Type.Gold && !((GoalCard) c).isCovered());
	}
	
	
	// get //
	
	public Map<Position, PathCard> getBoard() {
		return board;
	}
	
	public int getNumberOfAdjacentCards(int x, int y) {
		Set<Position> neighborPositions = Set.of(Position.of(x - 1, y), Position.of(x + 1, y), Position.of(x, y - 1), Position.of(x, y + 1));
		return (int) board.keySet().stream().filter(pos -> neighborPositions.contains(pos)).count();
	}
	
}
