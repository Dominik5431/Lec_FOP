package collections;

import static org.junit.Assert.assertEquals;

import java.util.function.Predicate;

import diseasespreadings.Person;

/**
 * A generic construct used for linked sets.<br>
 * <br>
 * The item cascade that is referenced by this construct
 * must be sorted according to the natural order of the elements.
 * The item cascade may contain each item at most once!
 *
 * @param <T> the type of keys
 */
public class MySet<T extends Comparable<? super T>> {

	// -------------------------------------------------------------- Exercises -------------------------------------------------------------- //

	/**
	 * Diese Methode filtert eine Liste bezüglich eines übergebenen Prädikats.
	 * 
	 * @param predicate: Predikat, hinsichtlich dem die Liste gefiltert wird
	 */
	public void filter(Predicate<? super T> predicate) {
		// TODO Exercise H1.1
		boolean finished = false;
		if (this.getHead() == null) {
			return;
		}
		MyItem<T> item = this.getHead();
		while (!finished) {
			if (item != null) {
				if (!predicate.test(item.getKey())) {
					item = item.getNext();
					if (item == null) {
						this.setHead(null);
					}
				} else {
					this.setHead(item);
					finished = true;
				}	
			} else {
				this.setHead(null);
				return;
			}
					
		}
		while (item.getNext() != null) {
			if (!predicate.test(item.getNext().getKey())) {
				if (item.getNext().getNext() == null) {
					item.setNext(null);
				} else {
					item.setNext(item.getNext().getNext());
				}
			} else {
				item = item.getNext();
			}
		}
		
	}

	/**
	 * Diese Methode erstellt eine Teilmenge von Objekten aus der ursprünglichen Menge, die einem übergebenen Prädikat genügen.
	 * 
	 * @param predicate: Prädikat, nach dem die Liste gefiltert wird.
	 * @return Erstellte Teilmenge
	 */
	public MySet<T> makeSubset(Predicate<? super T> predicate) {
		// TODO Exercise H1.2
		//Kopiert Liste:
		MyItem<T> item = this.getHead();
		MySet<T> result = new MySet<T>();
		if (this.getHead() == null) {
			return new MySet<T>(null);
		}
		boolean finished = false;
		while (!finished) {
			if (!(item == null)) {
				if (!predicate.test(item.getKey())) {
					item = item.getNext();
					if (item == null) {
						result.setHead(null);
					}
				} else {
					result.setHead(new MyItem<T>(item.getKey()));
					finished = true;
				}	
			} else {
				return new MySet<T>(null);
			}
			
		}
		MyItem<T> entry = result.getHead();
		while (item.getNext() != null) {
			if (predicate.test(item.getNext().getKey())) {
				entry.setNext(new MyItem<T>(item.getNext().getKey()));
				item = item.getNext();
				entry = entry.getNext();
			} else {
				if (item.getNext().getNext() == null) {
					entry.setNext(null);
					break;
				}
				item = item.getNext();
				
			}
			
		}
		return result;
	}
	
	
	/**
	 * Diese Methode erstellt die Schnittmenge einer Liste von Mengen.
	 * 
	 * @param <T>: generischer Typparameter des Elements
	 * @param sets: Liste von Mengen mit Elementen, aus denen die Schnittmenge gebildet werden soll
	 * @return Schnittmenge
	 */
	public static <T extends Comparable<? super T>> MySet<T> makeIntersection(MyList<MySet<T>> sets) {
		// TODO Exercise H1.3
		if (sets.getHead().getKey().getHead() == null) {
			return new MySet<T>(null);
		}
		if (sets.getHead().getKey() == null) {
			return new MySet<T>(null);
		}
		if (sets.getHead() == null) {
			return null;
		}
		if (sets.getHead().getNext() == null) {
			return sets.getHead().getKey();
		}
		//Idee: Paarweise Schnittmengenbildung bis nur noch eine Menge übrig ist.
		//Schnittmenge der ersten beiden Mengen.
		MySet<T> result = new MySet<>();
		MyItem<MySet<T>> set = sets.getHead();
		MySet<T> first = set.getKey();
		MySet<T> second = set.getNext().getKey();
		result = makeIntersectionHelper(first, second);
		//Schnittmenge mit restlichen Mengen
		set = set.getNext();
		while (set.getNext() != null) {
			result = makeIntersectionHelper(result, set.getNext().getKey());
			set=set.getNext();
		}
		return result;
	}
	
	
	/**
	 * Diese Methode berechnet die Schnittmenge zweier Mengen. Insb. ist sie hier dazu gedacht, die Schnittmenge der ersten n-Elemente mit dem n+1-ten ELement zu bilden.
	 * 
	 * @param <T>: generischer Typparameter des Elements
	 * @param actIntersect: bisherige Schnittmenge aus den ersten n-Elementen von sets aus der Methode makeIntersection, bevor makeIntersectionHelper aufgerufen wird.
	 * @param set: Menge, die in der neuen Schnittmenge beachtet werden soll
	 * @return Schnittmenge von actIntersect und set
	 */
	public static <T extends Comparable<? super T>> MySet<T> makeIntersectionHelper(MySet<T> actIntersect, MySet<T> set) {
		MySet<T> result = new MySet<T>();
		MyItem<T> act1 = actIntersect.getHead();
		MyItem<T> act2 = set.getHead();
		MyItem<T> actRes = null;
		boolean head = false;
		while (act1 != null) {
			while (act2 != null) {
				if (act1.getKey().compareTo(act2.getKey()) < 0) {
					act2 = set.getHead();
					break;
				} else if(act1.getKey().compareTo(act2.getKey()) > 0) {
					act2 = act2.getNext();
				} else if (act1.getKey().compareTo(act2.getKey()) == 0) {
					if (!head) {
						result.setHead(act1);
						actRes = act1;
						head = !head;
					} else {
						actRes.setNext(act1);
						actRes = actRes.getNext();
					}
					act2 = act2.getNext();
					break;
				}
			}
			act1 = act1.getNext();
			act2 = set.getHead();
		}
		actRes.setNext(null);
		return result;
	}
	
	/**
	 * Diese Methode sortiert eine Menge.
	 * 
	 * @param <T>: Generischer Paramameter: Art der Objekte, die in der Menge gespeichert werden
	 * @param set: Zu sortierende Menge
	 * @return sortierte Menge
	 */
	public static <T extends Comparable<? super T>> MySet<T> sort(MySet<T> set) {
		return set;
	}
	

	// -------------------------------------------------------------- sesicrexE -------------------------------------------------------------- //

	public MyItem<T> head;

	/**
	 * Constructs an empty {@link MySet}.
	 */
	public MySet() {
		this(null);
	}

	/**
	 * Constructs a {@link MySet} using the given item as the head item.
	 * @param head the head item
	 */
	public MySet(MyItem<T> head) {
		this.head = head;
	}

	/**
	 * Sets a new head item of this set.
	 * @param head the new head item
	 */
	public void setHead(MyItem<T> head) {
		this.head = head;
	}

	/**
	 * Returns the head item of this set.
	 * @return the head item
	 */
	public MyItem<T> getHead() {
		return head;
	}

	// Feel free to add further methods here!

}
