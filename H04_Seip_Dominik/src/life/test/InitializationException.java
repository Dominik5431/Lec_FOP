package life.test;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
class InitializationException extends Exception {
    private static final long serialVersionUID = 3528384111338234590L;

    InitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
