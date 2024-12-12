package memomap;

public class ReadException extends RuntimeException{
	
	/**
	 * Constructs a {@link ReadException}
	 * 
	 * @param message: The message the Exception shall have.
	 */
	public ReadException (String message) {
		super("Cannot read memo: " + message);
	}

}
