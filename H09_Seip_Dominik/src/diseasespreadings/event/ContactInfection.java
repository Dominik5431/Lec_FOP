package diseasespreadings.event;

import diseasespreadings.Contact;
import diseasespreadings.Person;

/**
 * A contact infection is an infection in a simulated disease spreading
 * that was caused by a simulated contact.
 */
public class ContactInfection extends Infection {

	/** The step in which the infection has occurred. **/
	private final int step;

	/** The contact by which the infection was transmitted. **/
	private final Contact contact;

	/**
	 * Constructs an {@link ContactInfection}
	 * for the given contact and simulation step.
	 * @param contact the contact
	 * @param step    the simulation step
	 */
	public ContactInfection(Contact contact, int step) {
		this.step = step;
		this.contact = contact;
	}

	/**
	 * Returns the contact by which the infection was transmitted.
	 * @return the infectious contact
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * Returns the person who has infected.
	 * @return the person who has infected
	 */
	public Person getInfector() {
		return getContact().getPersonA();
	}

	@Override
	public Person getInfectedPerson() {
		return getContact().getPersonB();
	}

	@Override
	public int getStep() {
		return step;
	}

}
