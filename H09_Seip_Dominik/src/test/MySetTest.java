package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import collections.*;
import diseasespreadings.Person;


public class MySetTest {
	
	private MySet<Integer> set1;
	private MySet<Person> set2;
	private MySet<Person> set3;
	
	@BeforeEach
	public void setUp() {
		set1 = new MySet<Integer>();
		set1.setHead(new MyItem<Integer>(1));
		MyItem<Integer> actItem = set1.getHead();
		for (int i = 2; i<50; i++) {
			actItem.setNext(new MyItem<Integer>(i));
			actItem = actItem.getNext();
		}
		set2 = new MySet<>();
		set3 = new MySet<Person>();
	}

	
	@Test
	public void testFilter() {
		int i = 0;
		for (MyItem<Integer> actItem = set1.getHead(); actItem != null; actItem = actItem.getNext()) {
			i++;
			assertEquals(Integer.valueOf(i), actItem.getKey());
		}
		set1.filter((x) -> x%3 == 0 ? true : false);
		i=0;
		for (MyItem<Integer> actItem = set1.getHead(); actItem != null; actItem = actItem.getNext()) {
			i += 3;
			assertEquals(Integer.valueOf(i), actItem.getKey());
		}
		set1.filter((x) -> x<10 ? true: false);
		i=0;
		for (MyItem<Integer> actItem = set1.getHead(); actItem != null; actItem = actItem.getNext()) {
			i+=3;
			assertEquals(Integer.valueOf(i), actItem.getKey());
		}
		assertTrue(!(i>=10));
//		set2.filter((x) -> x.getID()<100);
		assertEquals(null, set2.getHead());
	}
	
	@Test
	public void testMakeSubset() {
		MySet<Integer> result = set1.makeSubset((x) -> x%3 == 0? true : false);
		int i= 0;
		for (MyItem<Integer> actItem = result.getHead(); actItem != null; actItem = actItem.getNext()) {
			i+=3;
			assertEquals(Integer.valueOf(i), actItem.getKey());
		}
		result = result.makeSubset((x) -> x<10?true: false);
		i=0;
		for (MyItem<Integer> actItem = result.getHead(); actItem != null; actItem = actItem.getNext()) {
			i+=3;
			assertEquals(Integer.valueOf(i), actItem.getKey());
		}
		assertTrue(!(i>=10));
		result = result.makeSubset((x) -> x>20?true:false);
		assertEquals(null, result.getHead());
		MySet<Person> result2 = set2.makeSubset((x) -> x.getID()<100);
		assertEquals(null, result2.getHead());
	}
	
	@Test
	public void testMakeIntersection() {
		set1 = new MySet<Integer>();
		set1.setHead(new MyItem<Integer>(1));
		MyItem<Integer> actItem = set1.getHead();
		for (int i = 2; i<20; i++) {
			actItem.setNext(new MyItem<Integer>(i));
			actItem = actItem.getNext();
		}
		MySet<Integer> set4 = new MySet<Integer>();
		set4.setHead(new MyItem<Integer>(2));
		actItem = set4.getHead();
		for (int i = 4; i<50; i+=2) {
			actItem.setNext(new MyItem<Integer>(i));
			actItem = actItem.getNext();
		}
		MySet<Integer> set5 = new MySet<Integer>();
		set5.setHead(new MyItem<Integer>(3));
		actItem = set5.getHead();
		for (int i = 3; i<50; i+=3) {
			actItem.setNext(new MyItem<Integer>(i));
			actItem = actItem.getNext();
		}
		MyList<MySet<Integer>> list = new MyList<>();
		list.setHead(new MyItem<MySet<Integer>>(set1));
		list.getHead().setNext(new MyItem<MySet<Integer>>(set4));
		list.getHead().getNext().setNext(new MyItem<MySet<Integer>>(set5));
		MySet<Integer> result = MySet.makeIntersection(list);
		int i=0;
		for (MyItem<Integer> act = result.getHead(); act != null; act = act.getNext()) {
			i+=6;
			assertEquals(Integer.valueOf(i), act.getKey());
		}
		MySet<Person> setP = new MySet<>();
		MyList<MySet<Person>> listP = new MyList<>();
		listP.setHead(new MyItem<MySet<Person>>(set2));
		listP.getHead().setNext(new MyItem<MySet<Person>>(set3));
		listP.getHead().getNext().setNext(new MyItem<MySet<Person>>(setP));
		MySet<Person> result2 = MySet.makeIntersection(listP);
		assertTrue(result2.getHead() == null);
	}

}
