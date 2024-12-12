package memomap;

/**
 * Each class that implements the {@link Identifiable} interface indicates that
 * each object of the class has a numerical identifier by which it can be
 * identified definitely.
 * 
 * @version 1.0
 */
public interface Identifiable {

	/**
	 * Returns the numerical identifier of this object.<br>
	 * This function is injective.
	 * 
	 * @return the identifier of this object
	 * @throws UnsupportedOperationException if this object has no identifier
	 */
	int getID();

}
