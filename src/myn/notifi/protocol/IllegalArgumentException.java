package myn.notifi.protocol;

public class IllegalArgumentException extends Exception {
    private static final long serialVersionUID = 1L;
    /**
     * Constructor of this class. Set error message to the given message
     * along with the cause
     * 
     * @param message - error message
     * @param cause - cause of the error
     * 
     * */
    public IllegalArgumentException (String message, Throwable cause) {
        super(message,cause);
    }
    /**
     * Constructor of this class. Set error message to the given message
     * @param message - error message
     * */
    public IllegalArgumentException (String message) {       
        super(message);
    }
}
