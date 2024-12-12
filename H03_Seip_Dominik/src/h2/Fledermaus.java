package h2;

public class Fledermaus extends Saeugetier implements Fliegen{

	public Fledermaus(String name, Gehege gehege) {
		super(name, gehege);
	}
	/**
	 * Diese Methode bewirkt die Vorstellung des Tieres.
	 * @return satz:  Vorstellung
	 */
	public String toString() {
		if (gehege == null) {
			String satz = "Ich wurde noch nicht zugeordnet.";
			return satz;
		} else {
			String satz = "Ich bin " + this.getClass().getSimpleName() + " "+ this.name + ", wohne im Gehege " + this.gehege.name + " und bin ein " + this.tierart + ".";
			return satz;
		}
	}

	@Override
	/**
	 * Diese Methode simuliert das Fliegen der Fledermaus.
	 * 
	 * @return "kreuz und quer"
	 */
	public String fliegen() {
		return "kreuz und quer";
	}
	/**
	 * Diese Methode überprüft, ob zwei Tiere zusammenleben können.
	 * 
	 * @param t: Tier, das auf Kompatibilität überprüft werden soll.
	 * @return true/false: Tier kompatibel?
	 */
	public boolean kompatibel(Tier t) {
		if (t.getClass().getSimpleName() == "Leguan") {
			return false;
		}
		if (t.getClass().getSimpleName() == "Emu") {
			return false;
		}
		return true;
	}

}
