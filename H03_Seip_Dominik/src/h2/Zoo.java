package h2;

public class Zoo {
	public static Tier[] tiere;
	public static Gehege[] gehege;
	
	/**
	 * Main Methode
	 * @param args
	 */
	public static void main (String[] args) {
		tiere = new Tier[100];
		Gehege terrarium = new Gehege("Terrarium", 10);
		Gehege aquarium = new Gehege("Aquarium", 12);	
		Gehege land = new Gehege("Krokodilland", 3);
		Leguan legu = new Leguan ("Klaus", terrarium);
		Delfin flippo = new Delfin("Flippo", aquarium);
		Krokodil kroko = new Krokodil("Kroko", land);
		Krokodil kroko2 = new Krokodil("Krokos", null);
		tiere[0] = legu;
		tiere[1] = flippo;
		tiere[2] = kroko;
		tiere[3] = kroko2;
		System.out.println(vorstellen());
		aquarium.add(tiere[0]);
		System.out.println(vorstellen());
		land.add(tiere[3]);
		System.out.println(vorstellen());
		System.out.println(flugshow());
		System.out.println(schwimmtraining());
		
	}
	/**
	 * Diese Methode bewirkt, dass die Tiere sich vorstellen.
	 * @return text: Vorstellung
	 */
	public static String vorstellen() {
		//TODO implement
		String text = new String(); 
		for (int i=0; i<tiere.length; i++) {
			if (tiere[i]!=null) {
				text = text + tiere[i].toString() + "\n" ;
	
			}
		}
		return text;
	}
	/**
	 * Diese Methode bewirkt, dass alle Tiere, die fliegen können, ihre Flugkünste zur Schau stellen.
	 * @return text: Akustische Visualisierung des Fliegens
	 */
	public static String flugshow() {
		String text = new String();
		for (int i = 0; i<tiere.length; i++) {
			if (tiere[i]!=null) {
				if (tiere[i] instanceof Fliegen) {
					Fliegen fliegen = (Fliegen) tiere[i];
					text = text + fliegen.fliegen() + "\n";
				}
			}
		}
		return text;
	}
	
	/**
	 * Diese Methode bewirkt, dass alle Tiere, die schwimmen können, ihre Schwimmkünste zur Schau stellen.
	 * @return text: Akustische Visualisierung des FSchwimmens
	 */
	public static String schwimmtraining() {
		String text = new String();
		for (int i = 0; i<tiere.length; i++) {
			if (tiere[i]!=null) {
				if (tiere[i] instanceof Schwimmen) {
					Schwimmen schwimmen = (Schwimmen) tiere[i];
					text = text + schwimmen.schwimmen() + "\n";
				}
			}
		}
		return text;
	}
	
}
