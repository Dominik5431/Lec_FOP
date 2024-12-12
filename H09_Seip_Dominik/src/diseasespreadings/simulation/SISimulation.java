package diseasespreadings.simulation;

import java.util.Random;

import collections.MyItem;
import collections.MySet;
import diseasespreadings.Contact;
import diseasespreadings.Person;
import diseasespreadings.event.ContactInfection;
import diseasespreadings.event.Infection;
import diseasespreadings.event.InitialInfection;

/**
 * A simulation of simulated disease spreadings.
 */
public class SISimulation implements Runnable {

	// -------------------------------------------------------------- Exercises -------------------------------------------------------------- //

	/**
	 * Diese Methode überprüft, ob eine Person ansteckend ist, also ob individual in infections enthalten ist.
	 * 
	 * @param infections: Menge der Infektionen
	 * @param individual: Person, die auf Infektion untersucht werden muss
	 * @return: true, falls infektiös, sonst false
	 */
	
	public static boolean isSusceptible(MySet<Infection> infections, Person individual) {
		// TODO Exercise H2.2
		MySet<Infection> infect = infections.makeSubset((Infection inf) -> inf.getInfectedPerson().getID() == individual.getID() ? true : false);
		if (infections.getHead() == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Diese Methode überprüft, ob eine Person in einem Simulationsschritt infektiös ist.
	 * 
	 * @param infections: Menge aller Infektionen
	 * @param individual: Person, die auf Infektion untersucht werden soll
	 * @param step: Schritt, in dem die Person untersucht werden soll.
	 * @return: true, falls infektiös, sonst false
	 */
	public static boolean isInfectiousInStep(MySet<Infection> infections, Person individual, int step) {
		// TODO Exercise H2.3
		MySet<Infection> stepInfection = infections.makeSubset(Infection.filterByStep(step - 1));
		return isSusceptible(stepInfection, individual);
	}

	
	/**
	 * Diese Methode entfernt aus der Menge der gegebenen Kontakte einen zufälligen Kontakt
	 * 
	 * @param contacts: Menge der Kontakte
	 * @return Den entfernten Kontakt
	 */
	public static Contact removeRandomContact(MySet<Contact> contacts) {
		// TODO Exercise H2.4
		if (contacts.getHead() == null) {
			return null;
		}
		int size = 1;
		MyItem<Contact> contact = contacts.getHead();
		while (contact.getNext() != null) {
			size++;
			contact = contact.getNext();
		}
		double p = (double)size * Math.random();
		contact = contacts.getHead();
		int zaehler = 1;
		MyItem<Contact> vorheriger = contact;
		while(contact.getNext() != null) {
			if (zaehler >= p) {
				Contact result = new Contact(contact.getKey().getPersonA(), contact.getKey().getPersonB(), contact.getKey().getVirulence(), contact.getKey().getID());
				if (contacts.getHead().getKey().compareTo(contact.getKey()) == 0) {
					contacts.setHead(contact.getNext());
				} else {
					vorheriger.setNext(contact.getNext());
				}
				return result;
			} else {
				zaehler++;
				if (contacts.getHead().getKey().compareTo(contact.getKey()) == 0) {
					contact = contact.getNext();
				} else {
					contact = contact.getNext();
					vorheriger = vorheriger.getNext();
				}
			}
		}
		return null;
	}

	// -------------------------------------------------------------- sesicrexE -------------------------------------------------------------- //

	private final Person initialInfectee;
	private final MySet<Contact> contacts;
	private final MySet<Infection> infections = new MySet<>();

	/**
	 * Constructs a simulation using the given initially infected person
	 * and contact network (contact set).
	 * @param initialInfectee the initially infected person
	 * @param contacts        the contact network
	 */
	public SISimulation(Person initialInfectee, MySet<Contact> contacts) {
		this.initialInfectee = initialInfectee;
		this.contacts = contacts;
	}

	@Override
	public final void run() {
		// start with step 0
		int step = 0;
		// tail of infections + add initial infection to set of infections
		MyItem<Infection> tail = infections.head = new MyItem<>(new InitialInfection(initialInfectee));
		while (infections.head.stream().anyMatch(Infection.filterByStep(step))) {
			// in the last step there was at least one infection
			step++;
			// determine potential contacts
			MySet<Contact> potentialContacts = SISimulation.getPotentialContacts(contacts, infections, step);
			Contact contact;
			// remove random contact from set of potential contacts
			while ((contact = SISimulation.removeRandomContact(potentialContacts)) != null) {
				// there is a potential contact
				if (SISimulation.isSusceptible(infections, contact.getPersonB())) {
					// the target person of the potential contact is still susceptible
					if (contact.getVirulence() > Math.random()) {
						// successful infection -> add contact infection to set of infections
						tail.next = new MyItem<>(new ContactInfection(contact, step));
						tail = tail.next;
					}
				}
			}
			// now there is no potential contact
		}
	}

	/**
	 * Returns the set of potential contacts for the given contact network,
	 * set of occurred infections and step.
	 * @param  contacts   the contact network
	 * @param  infections the set of previous infections
	 * @param  step       the step
	 * @return            the set of potential contacts
	 */
	private static MySet<Contact> getPotentialContacts(MySet<Contact> contacts, MySet<Infection> infections, int step) {
		return new MySet<>(contacts.head.stream().filter(c -> SISimulation.isInfectiousInStep(infections, c.getPersonA(), step)).collect(MyItem.Collectors.toItemCascade()));
	}

	/**
	 * Returns the set of infections constructed by this simulation.
	 * @return the infection set
	 */
	public MySet<Infection> getInfections() {
		return infections;
	}

	// Feel free to add further methods here!

}
