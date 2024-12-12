package memomap;

import objects.Student;

/**
 * A {@link MemoMap} is a map that maps identifiable objects to strings that are
 * also called memos.
 * <p>
 * Each identifiable object can be mapped to at most one memo. A memo may be
 * {@code null}. If a memo is finalized, the memo can no longer be changed.
 * 
 * @version 1.0
 */
public class MemoMap {

	/**
	 * The array of {@link MemoEntry} objects.
	 */
	public final MemoEntry[] entries;

	/**
	 * Constructs a {@link MemoMap} with the given capacity.
	 * 
	 * @param capacity the capacity of the memo map
	 */
	public MemoMap(int capacity) throws IllegalArgumentException {
		if (capacity < 0)
			throw new IllegalArgumentException("The capacity is invalid: " + capacity);
		entries = new MemoEntry[capacity];
	}
	
	// main-method for debugging
	/*public static void main(String[] args) {
		MemoMap map = new MemoMap(10);
		Student.students = new Student[100];
		Student student1 = new Student(10);
		Student student2 = new Student(19);
		Student student3 = new Student(5);
		Student student4 = new Student(2);
		Student student5 = new Student(18);
		MemoMap map2 = new MemoMap(5);
		try {
			//map.put(student1, "Exmatrikuliert", true); 
			map.put(student1, "Exmatrikuliert", false); 
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
		try {
			map.merge(map2);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		int i = 0;
		
	} */
	
	/*
	 * This method puts a MemoEntry into the MemoEntry-Array entries from this map.
	 * 
	 * @param obj: The object of the memo entry which is to put
	 * @param memo: The string which is assigned to the object
	 * @param finalized: The boolean which sets if the entry is finalized
	 * @throws WriteException if the object does not support getID()
	 * @throws NullPointerException if the object is null
	 * @throws WriteException if the object is already finalized in entries
	 * @throws WriteException if there is not enough capacity in entries to put a new entry
	 *
	 */
	
	public void put(Identifiable obj, String memo, boolean finalized) throws WriteException, NullPointerException {
		if (obj == null)
			throw new NullPointerException("The given object is null.");
		try {
			obj.getID();
		} catch (UnsupportedOperationException e) {
			throw new WriteException("The object is not identifiable.");
		}
		boolean found = false;
		int constant = obj.getID()%entries.length -1;
		for (int i = obj.getID()%entries.length; i!=constant; i++) {
			if (entries[i]!= null) {
				if (entries[i].object.equals(obj)) {
					found = true;
					if (!entries[i].finalized) {
						entries[i].object = obj;
						entries[i].memo = memo;
						entries[i].finalized = finalized;
					} else {
						throw new WriteException("The memo is finalized.");
					}
					break;
				} 
			}
			if (i == entries.length -1) {
				i=-1;
				constant++;
			}
		}
		int position = -1;
		constant = obj.getID()%entries.length -1;
		if (!found) {
			for (int i = obj.getID()%entries.length; i!=constant; i++) {
				if (entries[i] == null) {
					entries[i]= new MemoEntry(obj, memo, finalized);
					position = i;
					break;
				}
				if (i == entries.length -1) {
					i=-1;
					constant++;
				}
			}
		}
		if (position == -1) {
			throw new WriteException("There is not enough capacity.");
		} 
				
	}
	
	
	/*
	 * This method gets a MemoEntry from entries and returns the assigned memo, which has the Id, the method is getting
	 * 
	 * @param id: The object with this id shall be found
	 * @throws IndexOutOfBoundsException if the id is invalid
	 * @throws ReadException if there could not be an assigned memo found
	 * @return the assigned memo from the object with the set id
	 */
	public String get(int id) throws ReadException, IndexOutOfBoundsException {
		if (id<0)
			throw new IndexOutOfBoundsException("The ID is invalid: " + id);
		int constant = id%entries.length -1;
		for (int i = id % entries.length; i != constant; i++) {
			if(entries[i]!=null) {
				if(entries[i].object.getID() == id) {
					if (entries[i].object != null) {
						return entries[i].memo;
					} else {
						throw new ReadException("There is no memo assigned.");
					}

				}
			}
			if (i == entries.length -1) {
				i=-1;
				constant++;
			}
		}
		throw new ReadException("There is no memo assigned.");
	}

	
	/*
	 * This method merges the entries of two MemoMaps.
	 * 
	 * @param map: The map which is going to be merged with {@this}
	 * @throws NullPointerException if the given map is null
	 * @throws WriteException if there is not enough capacity to put a new entry
	 * @throws WriteException if the method tries to alter a finalized entry
	 */
	public void merge(MemoMap map) throws WriteException, NullPointerException {
		if (map == null)
			throw new NullPointerException("The given map is null.");
		MemoEntry[] list = this.sortID(map.entries);
		//Damit bei Fehlerauftreten noch nichts verändert wurde, wird die Operation erstmal auf separatem Objekt ausgeführt
		MemoEntry[] copy = this.entries;
		for (int i = 0; i<list.length; i++) {
			if (list[i] == null) {
				continue;
			} else {
				int found = -1;
				for (int j = 0; j<copy.length; j++) {
					if (copy[j]!=null) {
						if(copy[j].object.getID() == list[i].object.getID()) {
							found = j;
						}
					}
					
				}
				if (found == -1) {
					//Hier wird der not enough capacity Fehler weitergereicht
					this.put(list[i].object, list[i].memo, list[i].finalized);
				} else if(copy[found].finalized) {
					throw new WriteException("There is at least one non-mergeable entry.");
				} else if (copy[found].memo == null) {
					copy[found].memo = list[i].memo;
					copy[found].finalized = list[i].finalized;
				} else {
					copy[found].memo = copy[found].memo + " " + list[i].memo;
					copy[found].finalized = list[i].finalized;
				}
				
			}
		}
		for (int i=0; i< copy.length; i++) {
			this.entries[i] = copy[i];
		}
	}
	
	/*
	 * This method sorts a list of {@MemoEntry}. The entries are sorted from low to high id of the saved objects.
	 * 
	 * @param entries: list to be sorted
	 * @return: sorted list
	 */
	public MemoEntry[] sortID(MemoEntry[] entries) {
		MemoEntry[] sortedArray = new MemoEntry[entries.length];
		MemoEntry act;
		int[] finished = new int[entries.length];
		for (int i = 0; i<entries.length; i++) {
			act = entries[0]; //Damit Code durch den Compiler geht -> sonst act may not have been initialized
			//Erstes Element setzen
			for (int k=0; k<entries.length; k++) {
				if (entries[k]!= null) {
					if (!enthalten(entries[k].object.getID(), finished)) {
						act=entries[k];
						break;
					}
				} else {
					act = null;
				}
			}
			//Auf kleineres Element überprüfen
			if (act != null) {
				for (int j = 0; j<entries.length; j++) {
					if (entries[j]!=null) {
						if (!enthalten(entries[j].object.getID(), finished)) {
							if (entries[j].object.getID() < act.object.getID()) {
								act = entries[j];
							}
						}
					}
				}
			}
			
			//Zuweisen
			sortedArray[i] = act;
			if (act != null) {
				finished[i] = act.object.getID();
			}
			
		}
		return sortedArray;
	}
	
	/*
	 * This method proves if an element n is in a list of the type of n.
	 * 
	 * @param n: The element from type integer
	 * @param list: The list which is proved for n
	 * @return: true if its in the list and otherwise false
	 */
	public boolean enthalten(int n, int[] list) {
		for (int i = 0; i < list.length; i++) {
			if(n==list[i])
				return true;
		}
		return false;
	}
	
	/*
	 * This method prints all memos which are assigned to the objects which have an id which is in the set list.
	 * 
	 * @param numbers: Array with IDs. The memos of the objects with this ids shall be outlined
	 */
	public void print(int[] numbers) {
		for (int i = 0; i<numbers.length; i++) {
			try {
				System.out.println(this.get(numbers[i]));
			} catch(ReadException exc) {
				System.out.println(exc.getMessage());
			} catch(IndexOutOfBoundsException exc) {				
			}
		}
	}
}


