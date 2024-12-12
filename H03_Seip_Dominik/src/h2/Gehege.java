package h2;

public class Gehege {
	String name;
	Tier[] tiere;
	
	/**
	 * Konstruktor für Klasse Gehege
	 * 
	 * @param name
	 * @param size
	 */
	
	public Gehege(String name, int size) {
		this.name = name;
		tiere = new Tier[size];
	}
	
	/**
	 * Diese Methode überprüft, ob ein Tier diesem Gehege hinzugefügt werden kann und falls ja, wird es hinzugefügt	
	 * 
	 * @param t
	 * @return true, falls es hinzugefügt werden kann, false, falls es nicht hinzugefügt werden kann.
	 */
	public boolean add(Tier t) {
		int taken=0;
		for (int i=0; i<tiere.length; i++) {
			if (tiere[i]!=null) {
				taken++;
			}
		}
		if (this.tiere.length <= taken) {
			System.out.println("zu klein");
			return false; 
		
		}
		boolean bool = true;
		for (int i=0; i<tiere.length; i++) {
			if (tiere[i]!=null) {
				if (!t.kompatibel(tiere[i]))
					bool=false;
			}
		}
		if (bool) {
			tiere[taken] = t;
			tiere[taken].umsiedeln(this);
			return true;
		} else {
			System.out.println("nicht kompatibel");
			return false;
		}
		
	}
	
}
