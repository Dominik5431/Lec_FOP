package h2;

public class Leguan extends Reptil{

	public Leguan(String name, Gehege gehege) {
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
	
	
	public boolean kompatibel(Tier t) {
		if (t.getClass().getSimpleName() == "Fledermaus") {
			return false;
		}
		if (t.getClass().getSimpleName() == "Krokodil") {
			return false;
		}
		return true;
	}

}
