package h2;

public abstract class Tier {
	public static boolean constructorCalled = false;
	protected String name;
	protected Gehege gehege;
	
	/**
	 * Konstruktor Tier
	 * 
	 * @param name
	 * @param gehege
	 */
	public Tier(String name, Gehege gehege) {
		constructorCalled = true;
		this.name = name;
		if(gehege != null && gehege.add(this)) this.gehege = gehege;
	}
	
	/**
	 * Diese Methode überprüft, ob zwei Tiere kompatibel sind. Sie wird in den abgeletieten Klassen überschrieben.
	 * 
	 * @param t: Tier, das auf Kompatibilität überprüft werden soll.
	 * @return true: hier standardmäßig
	 */
	public boolean kompatibel(Tier t) {
		return true;
	}
	
	/**
	 * Ändert zugeordnetes Gehege eines Tieres.
	 * @param gehege
	 */
	public void umsiedeln(Gehege gehege) {
		this.gehege = gehege;
	}
	
}
