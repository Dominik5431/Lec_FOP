package Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fop.model.cards.CardAnchor;
import fop.model.graph.Graph;


public class GraphTest {

	Graph<Integer> g1;
	HashMap<Integer, Set<Integer>> h1;
	Graph<CardAnchor> g2;
	
	@BeforeEach
	void setUp() {
		g1 = new Graph<>();
		g2 = new Graph<>();
	}
	
	@Test
	void testAddVertex() {
		g1.addVertex(1);
		assertTrue(g1.vertices().contains(1));
		g2.addVertex(CardAnchor.right);
		assertTrue(g2.vertices().contains(CardAnchor.right));
	}
	
	@Test
	void testAddEdge() {
		for (int i = 0; i<10; i++) {
			g1.addVertex(i);
		}
		g1.addEdge(1, 3);
		assertTrue(g1.hasEdge(1, 3));
		assertTrue(g1.hasEdge(3, 1));
	}
	
	@Test
	void testRemoveVertex() {
		for (int i = 0; i<10; i++) {
			g1.addVertex(i);
		}
		g1.removeVertex(3);
		assertTrue(!g1.hasVertex(3));
	}
	
	@Test
	void testRemoveEdge() {
		for (int i = 0; i<10; i++) {
			g1.addVertex(i);
		}
		g1.addEdge(1, 3);
		g1.removeEdge(1, 3);
		assertTrue(!g1.hasEdge(1, 3));
	}
	
	@Test
	void testHasPath() {
		for (int i = 0; i<10; i++) {
			g1.addVertex(i);
		}
		g1.addEdge(1, 3);
		g1.addEdge(3, 2);
		g1.addEdge(2, 7);
		g1.addEdge(7, 8);
		assertTrue(g1.hasPath(1, 8));
	}
	
}
