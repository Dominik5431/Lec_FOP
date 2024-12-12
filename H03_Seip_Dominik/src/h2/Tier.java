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
	 * Diese Methode �berpr�ft, ob zwei Tiere kompatibel sind. Sie wird in den abgeletieten Klassen �berschrieben.
	 * 
	 * @param t: Tier, das auf Kompatibilit�t �berpr�ft werden soll.
	 * @return true: hier standardm��ig
	 */
	public boolean kompatibel(Tier t) {
		return true;
	}
	
	/**
	 * �ndert zugeordnetes Gehege eines Tieres.
	 * @param gehege
	 */
	public void umsiedeln(Gehege gehege) {
		this.gehege = gehege;
	}
	
}
