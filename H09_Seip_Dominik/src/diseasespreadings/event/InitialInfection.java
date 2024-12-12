package diseasespreadings.event;

import diseasespreadings.Person;

/**
 * An initial infection of a person in a simulated disease spreading.
 */
public final class InitialInfection extends Infection {

	/** The person who was infected initially. **/
	private final Person infectee;

	/**
	 * Constructs an {@link InitialInfection}
	 * for the given initially infected person.
	 * @param infectee the initially infected person
	 */
	public InitialInfection(Person infectee) {
		this.infectee = infectee;
	}

	@Override
	public Person getInfectedPerson() {
		return infectee;
	}

	@Override
	public int getStep() {
		// initial infections always occur in step 0
		return 0;
	}

}
