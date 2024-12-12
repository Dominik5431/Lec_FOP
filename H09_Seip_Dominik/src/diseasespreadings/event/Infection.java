package diseasespreadings.event;

import java.util.function.Predicate;

import diseasespreadings.Person;
import misc.Identifiable;

/**
 * An infection of a person in a simulated disease spreading.
 */
public abstract class Infection implements Comparable<Infection>, Identifiable {

	// ------------------------------- Exercises ------------------------------- //

	/**
	 * Diese Methode gibt ein Predicate zurück, dass bezüglich des Simulationsschrittes filtert
	 * @param step: Der Simulationsschritt, nach dem gefiltert werden soll
	 * @return: Predikat
	 */
	public static final Predicate<Infection> filterByStep(int step) {
		// TODO Exercise H2.1
		return (Infection inf) -> (inf.getStep() == step) ? true : false;
	}


	// ------------------------------- sesicrexE ------------------------------- //

	private static int C;
	private final int ID = Infection.C++;

	/**
	 * Returns the person who was infected.
	 * @return the infected person
	 */
	public abstract Person getInfectedPerson();

	/**
	 * Returns the simulation step in which this infection occurred.
	 * @return the simulation step
	 */
	public abstract int getStep();

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public final int compareTo(Infection o) {
		if (getStep() != o.getStep())
			// unequal step -> compare by step
			return getStep() - o.getStep() < 0 ? -1 : 1;
		if (getID() != o.getID())
			// unequal ID -> compare by ID
			return getID() - o.getID() < 0 ? -1 : 1;
		// equal step and ID
		return 0;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		Infection other = (Infection) object;
		if (ID != other.ID)
			return false;
		return true;
	}

}
