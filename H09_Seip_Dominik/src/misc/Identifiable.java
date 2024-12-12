package misc;

/**
 * Each class that implements the {@link Identifiable} interface indicates that
 * each object of the class has a numerical identifier by which it can be
 * identified definitely.
 */
public interface Identifiable {

	/**
	 * Returns the numerical identifier of this object.
	 * @return the identifier of this object
	 */
	int getID();

}
