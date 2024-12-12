package diseasespreadings.simulation;


import collections.MyItem;
import collections.MyList;
import collections.MySet;
import diseasespreadings.Contact;
import diseasespreadings.Person;
import diseasespreadings.PersonInfection;
import diseasespreadings.event.Infection;

/**
 * A Monte Carlo simulation of simulated disease spreadings.
 */
public class MCSISimulation implements Runnable {

	// -------------------------------------------------------------- Exercises -------------------------------------------------------------- //

	@Override
	/**
	 * Diese Methode startet die Monte-Carlo-Simulation
	 */
	public void run() {
		// TODO Exercise H3
		MCSISimulation mCSim = this;
		SISimulation sim;
		MyItem<SISimulation> actual = null;
		for (int i = 0; i < simulationCount; i++) {
			sim = mCSim.supply();
			
			if (i == 0) {
				simulations.setHead(new MyItem<SISimulation>(sim));
			} else {
				actual.setNext(new MyItem<SISimulation>(sim));
			}
			sim.run();
			actual = simulations.getHead();
			mCSim = new MCSISimulation(initialInfectee, contacts, simulationCount - 1);
		}	
	}
	/**
	 * Diese Methode überprüft, welche personen im Laufe der Simulation mehr als n-mal infiziert wurden.
	 * 
	 * @param n: Anzahl der benötigten Infektionen, um zurückgegeben zu werden
	 * @return: Liste an Personen, die häufiger als n-mal infiziert wurden.
	 */
	public MySet<Person> getPersons_AtLeastNInfections(int n) {
		//Idee: Liste mit Paar aus Person und Integer, wobei Integer die Anzahl an Infektionen beschreibt
		//Jede in einer Simulation infizierte Person wird in diese Liste eingefüt, falls noch nicht drin, bzw, falls bereits drin, wird der int Wert um eins erhöht.
		//Zum Schluss werden alle Personen zurückgegeben, deren Wert über n liegt -> mit filter und map
		//Zum Schluss nach ID sortiert.
		//Siehe hierfür insbesondere Klasse PersonInfection
		MySet<Person> result = new MySet<>();
		MyItem<SISimulation> actSim = getSimulations().getHead();
		MySet<PersonInfection> infPerson = new MySet<>();
		MyItem<PersonInfection> actPerson = null;
		boolean found = false;
		while (actSim != null) {
			MyItem<Infection> inf = actSim.getKey().getInfections().getHead();
			while (inf != null) {
				//Pro Simulation kann jede Person maximal einmal infiziert werden.
				if (infPerson.getHead() == null) {
					infPerson.setHead(new MyItem<PersonInfection>(new PersonInfection(inf.getKey().getInfectedPerson())));
					actPerson = infPerson.getHead();
				} else {	
					MyItem<PersonInfection> searchPerson = infPerson.getHead();
					found = false;
					while (searchPerson != null) {
						if (searchPerson.getKey().getPerson().getID() == inf.getKey().getInfectedPerson().getID()) {
							searchPerson.getKey().gettingInfected();
							found = true;
							break;
						}
						searchPerson = searchPerson.getNext();
					}
					if (!found) {
						actPerson.setNext(new MyItem<PersonInfection>(new PersonInfection(inf.getKey().getInfectedPerson())));
						actPerson = actPerson.getNext();
					}
					
				}
				inf = inf.getNext();
			}
			actSim = actSim.getNext();
			
		}
		infPerson.filter((PersonInfection p) -> p.getInfected()>=n ? true : false);
		//Übertragen der Personen auf result:
		actPerson = infPerson.getHead();
		MyItem<Person> actResult = null;
		while (actPerson != null) {
			if (result.getHead() == null) {
				result.setHead(new MyItem<>(actPerson.getKey().getPerson()));
				actResult = result.getHead();
			} else {
				actResult.setNext(new MyItem<>(actPerson.getKey().getPerson()));
				actResult = actResult.getNext();
			}
			actPerson = actPerson.getNext();
		}
		//Sortieren der Menge
		result = MySet.sort(result);
		return result;
	}
	
//	public static void main (String[] args) {
//		//Realisierung des Kontaktnetzwerks
//				Person p0 = new Person("0", 0);
//				Person p1 = new Person("1", 1);
//				Person p2 = new Person("2", 2);
//				Person p3 = new Person("3", 3);
//				Contact c01 = new Contact(p0, p1, 0.6, 11); //initial infizierte Person+
//				Contact c10 = new Contact(p1, p0, 0.6, 12);
//				Contact c02 = new Contact(p0, p2, 0.6, 13);
//				Contact c20 = new Contact(p2, p0, 0.6, 14);
//				Contact c12 = new Contact(p1, p2, 0.6, 15);
//				Contact c21 = new Contact(p2, p1, 0.6, 16);
//				Contact c23 = new Contact(p2, p3, 0.6, 17);
//				Contact c32 = new Contact(p3, p2, 0.6, 18);
//				MySet<Contact> contacts = new MySet<>(new MyItem<Contact>(c01));
//				MyItem<Contact> item = contacts.getHead();
//				item.setNext(new MyItem<Contact>(c10));
//				item = item.getNext();
//				item.setNext(new MyItem<Contact>(c02));
//				item = item.getNext();
//				item.setNext(new MyItem<Contact>(c20));
//				item = item.getNext();
//				item.setNext(new MyItem<Contact>(c12));
//				item = item.getNext();
//				item.setNext(new MyItem<Contact>(c21));
//				item = item.getNext();
//				item.setNext(new MyItem<Contact>(c23));
//				item = item.getNext();
//				item.setNext(new MyItem<Contact>(c32));
//				item = item.getNext();
//				
//				MCSISimulation sim = new MCSISimulation(p0, contacts, 20);
//				sim.run();
//				MySet<Person> result = sim.getPersons_AtLeastNInfections(1);
//	}
	

	// -------------------------------------------------------------- sesicrexE -------------------------------------------------------------- //

	private final Person initialInfectee;
	private final MySet<Contact> contacts;
	private final int simulationCount;

	private final MyList<SISimulation> simulations = new MyList<>();

	/**
	 * Constructs a Monte Carlo simulation whose single simulations use
	 * the given initially infected person and contact network (contact set).<br>
	 * Calling {@link MCSISimulation#run()} will create and run
	 * the given count of single simulations.
	 * @param initialInfectee the initially infected person
	 * @param contacts        the contact network
	 * @param simulationCount the count of single simulations
	 */
	public MCSISimulation(Person initialInfectee, MySet<Contact> contacts, int simulationCount) {
		this.initialInfectee = initialInfectee;
		this.contacts = contacts;
		this.simulationCount = simulationCount;
	}

	/**
	 * Constructs a single simulation and returns it.<br>
	 * <b>Use this method to construct a single simulation instead of using the constructor!</br>
	 * @return the single simulation
	 */
	public SISimulation supply() {
		return new SISimulation(initialInfectee, contacts);
	}

	/**
	 * Returns the count of single simulations.
	 * @return the count of single simulations
	 */
	public int getSimulationCount() {
		return simulationCount;
	}

	/**
	 * Returns a list containing all single simulations
	 * that were create by this Monte Carlo simulation.
	 * @return the list of all single simulations
	 */
	public MyList<SISimulation> getSimulations() {
		return simulations;
	}

	// Feel free to add further methods!

}
