package diseasespreadings;

import misc.Identifiable;

/**
 * A representation of a person.
 */
public class Person implements Comparable<Person>, Identifiable {

	private final int ID;
	private final String name;

	/**
	 * Constructs a {@link Person} with the given properties.
	 * @param name the name of the person
	 * @param ID   the ID of the person
	 */
	public Person(String name, int ID) {
		this.name = name;
		this.ID = ID;
	}

	/**
	 * Returns the name of this person.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public final int compareTo(Person o) {
		// order by ID
		return getID() - o.getID();
	}

}
