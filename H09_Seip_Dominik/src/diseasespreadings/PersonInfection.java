package diseasespreadings;

public class PersonInfection implements Comparable<PersonInfection>{
	/**
	 * Klasse, die eine Person und die Anzahl ihrer Infektionen �ber alle Simulationsschritte zusammenfasst.
	 */
	
	//Die Person:
	private Person person;
	//Anzahl der Infektionen
	private int infected;
	
	/**
	 * Konstruktor: Legt Person fest. Objekt wird erstellt, sobal die Person einmal infiziert ist.
	 * @param person
	 */
	public PersonInfection (Person person) {
		this.person = person;
		this.infected = 1;
	}
	
	/**
	 * Gibt Person zur�ck
	 * @return Person
	 */
	public Person getPerson() {
		return person;
	}
	
	/**
	 * Setzt Person fest
	 * @param person
	 */
	public void setPerson(Person person) {
		this.person = person;
	}
	/**
	 * Liefert Anzahl an Infektionen zur�ck
	 * @return Anzahl an Infektionen
	 */
	public int getInfected() {
		return infected;
	}
	/**
	 * Legt Anzahl an Infektionen fest.
	 * @param infected: Anzahl an Infektionen
	 */
	public void setInfected(int infected) {
		this.infected = infected;
	}
	/**
	 * Erh�ht Anzahl an Infektionen um eins
	 */
	public void gettingInfected() {
		infected++;
	}
	/**
	 * Vergleicht bzgl der IDs der zugeh�rigen Personen
	 * @param: Zu vergleichende PersonInfection
	 * @return: Vergleichswert
	 */
	@Override
	public int compareTo(PersonInfection persInf) {
		// TODO Auto-generated method stub
		return person.getID() - persInf.getPerson().getID();
	}
	
	
	

}
