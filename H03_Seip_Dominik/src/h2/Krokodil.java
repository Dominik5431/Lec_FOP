package h2;

public class Krokodil extends Reptil implements Schwimmen{

	public Krokodil(String name, Gehege gehege) {
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

	public String schwimmen() {
		return "paddeln";
	}
	/**
	 * Diese Methode �berpr�ft, ob zwei Tiere zusammenleben k�nnen.
	 * 
	 * @param t: Tier, das auf Kompatibilit�t �berpr�ft werden soll.
	 * @return true/false: Tier kompatibel?
	 */
	public boolean kompatibel(Tier t) {
		if (!(t instanceof Fliegen)) {
			return false;
		}
		return true;
	}
}