package diseasespreadings;

import misc.Identifiable;

/**
 * An representation of contact between a source and a target person.
 */
public class Contact implements Comparable<Contact>, Identifiable {

	private final int ID;
	private final Person pA, pB;
	private final double virulence;

	/**
	 * Constructs a {@link Contact} using the given properties.
	 * @param pA        the source person of the contact
	 * @param pB        the target person of the contact
	 * @param virulence the infection probability of the contact
	 * @param ID        the ID of the contact
	 */
	public Contact(Person pA, Person pB, double virulence, int ID) {
		this.pA = pA;
		this.pB = pB;
		this.ID = ID;
		this.virulence = virulence;
	}

	/**
	 * Returns the source person of this contact.
	 * @return the source person
	 */
	public Person getPersonA() {
		return pA;
	}

	/**
	 * Returns the target person of this contact.
	 * @return the target person
	 */
	public Person getPersonB() {
		return pB;
	}

	/**
	 * Returns the infection probability of this contact.
	 * @return the infection probability
	 */
	public double getVirulence() {
		return virulence;
	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public final int compareTo(Contact o) {
		// order by ID
		return getID() - o.getID();
	}

}
