package memomap;

public class WriteException extends Exception {
	
	/**
	 * Constructs a {@link WriteException}
	 * 
	 * @param message: The message the Exception shall have.
	 */
	public WriteException(String message) {
		super("Cannot write memo: " + message);
	}

}
