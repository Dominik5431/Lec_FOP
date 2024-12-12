package memomap;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.Test;

import objects.Objekt;
import objects.Student;

import org.junit.jupiter.api.BeforeEach;

public class MemoMapTest {
	
	private MemoMap map;
	private Student student1;
	private Student student2;
	
	/**
	 * This method realizes a setup, which initializes some variables.
	 */
	@BeforeEach
	public void setUp() {
		map = new MemoMap(10);
		Student.students = new Student[100];
		student1 = new Student(10);
		student2 = new Student(19);

	}

	/**
	 * This method tests all Exceptions and the given error-messages of the method put.
	 */
	@Test
	public void testPut1() {
		//Ungültige Arraygröße
		IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> {new MemoMap(-1);});
		assertEquals(e1.getMessage(), "The capacity is invalid: -1");

		/*try {
			MemoMap map = new MemoMap(-1);
		} catch (IllegalArgumentException e) {
			assertEquals(e.getMessage(), "The capacity is invalid: -1");
		} */
			
		IndexOutOfBoundsException e2 = assertThrows(IndexOutOfBoundsException.class, () -> new Student(10));
		assertEquals("ID bereits vergeben.", e2.getMessage());
		
		//Übergebenes Objekt null
		NullPointerException e3 = assertThrows(NullPointerException.class, () -> map.put(null, "Test", false));
		assertEquals("The given object is null.", e3.getMessage());
		
		//UnsupportedOperationException
		WriteException e4 = assertThrows(WriteException.class, () -> map.put(new Objekt(), "Test", false));
		assertEquals("Cannot write memo: The object is not identifiable.", e4.getMessage());
		
		//Finalisierte Memo
		WriteException e5 = assertThrows(WriteException.class, () -> {map.put(student1, "Exmatrikuliert", true); map.put(student1, "Immatrikuliert", false);});
		assertEquals("Cannot write memo: The memo is finalized.", e5.getMessage());
		
		//-> hier stimmt was nicht mit der maximalen Kapazität
		
		//Zum Prüfen der maximalen Kapazität
		MemoMap smallMap = new MemoMap(1);
		WriteException e6 = assertThrows(WriteException.class, () -> {smallMap.put(student1, "Exmatrikuliert", true); smallMap.put(student2, "Matrikelnummer fehlt.", false);});
		assertEquals("Cannot write memo: There is not enough capacity.", e6.getMessage());
	}
	
	/**
	 * This method tests the functionality of put when given valide actual parameters
	 */
	@Test
	public void testPut2() throws NullPointerException, WriteException {
		map.put(student1, "Exmatrikuliert.",true);
		assertEquals(new MemoEntry(student1, "Exmatrikuliert.",true), map.entries[0]);
	}
	
	/**
	 * This method tests all Exceptions and the given error-messages of the method get.
	 */
	@Test
	public void testGet1() throws WriteException {
		//Ungültige ID:
		IndexOutOfBoundsException e1 = assertThrows(IndexOutOfBoundsException.class, () -> map.get(-1));
		assertEquals("The ID is invalid: -1", e1.getMessage());

		map.put(student1, "Exmatrikuliert", true);
		
		//object == null
		ReadException e2 = assertThrows(ReadException.class, () -> map.get(2));
		assertEquals("Cannot read memo: There is no memo assigned.", e2.getMessage());
		
	}
	/**
	 * This method tests the functionality of get when given valide actual parameters
	 */
	@Test
	public void testGet2() throws WriteException {
		map.put(student1, "Exmatrikuliert", true);
		assertEquals("Exmatrikuliert", map.get(10));
		
		
	}
	/**
	 * This method tests all Exceptions and the given error-messages of the method merge.
	 */
	@Test
	public void testMerge1() {
		Student student3 = new Student(5);
		Student student4 = new Student(2);
		Student student5 = new Student(18);
		MemoMap map2 = new MemoMap(5);
		try {
			map.put(student1, "Exmatrikuliert", true); 
			//map.put(student1, "Exmatrikuliert", false); 
			//map.put(student1, "Immatrikuliert", false);
			map.put(student2, "Matrikelnummer fehlt", false);
			map.put(student3, null, false);
			map.put(student5, "Bestanden", false);
			map2.put(student1, "2020", true);
			map2.put(student2, "nachgereicht", true);
			map2.put(student3, "Immatrikuliert", false);
			map2.put(student4, "Durchgefallen", false);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		//given map null
		NullPointerException e1 = assertThrows(NullPointerException.class, () -> map.merge(null));
		assertEquals("The given map is null.", e1.getMessage());
		
		//not enough capacity
		MemoMap map3 = new MemoMap(1);
		MemoMap map4 = new MemoMap(3);
		try {
			map3.put(student1, "Immatrikuliert", false);
			map4.put(student2, "Exmatrikuliert", true);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		WriteException e2 = assertThrows(WriteException.class, () -> map3.merge(map4));
		assertEquals("Cannot write memo: There is not enough capacity.", e2.getMessage());
		
		//ein nicht verbindbarer Eintrag
		WriteException e3 = assertThrows(WriteException.class, () -> map.merge(map2));
		assertEquals("Cannot write memo: There is at least one non-mergeable entry.", e3.getMessage());
		
		
	}
	/**
	 * This method tests the functionality of merge when given valide actual parameters
	 */
	@Test
	public void testMerge2() {
		Student student3 = new Student(5);
		Student student4 = new Student(2);
		Student student5 = new Student(18);
		MemoMap map2 = new MemoMap(5);
		try {
			//map.put(student1, "Exmatrikuliert", true); 
			//map.put(student1, "Exmatrikuliert", false); 
			map.put(student1, "Immatrikuliert", false);
			map.put(student2, "Matrikelnummer fehlt", false);
			map.put(student3, null, false);
			map.put(student5, "Bestanden", false);
			map2.put(student1, "2020", true);
			map2.put(student2, "nachgereicht", true);
			map2.put(student3, "Immatrikuliert", false);
			map2.put(student4, "Durchgefallen", false);
			map.merge(map2);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		assertEquals(new MemoEntry(student1, "Immatrikuliert 2020",true), map.entries[0]);
	}
	
}
