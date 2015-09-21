/*
 * Classname : AddATudeException
 *
 * Version information : 1.0
 *
 * Date : 9/7/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 */
package myn.addatude.protocol;

/**
 * Purpose : customized execption for this LocationRecord class
 * @author Tong Wang
 * @version 1.0
 * */
public class AddATudeException extends Exception{
    /*auto given serialVersion ID*/
    private static final long serialVersionUID = 1L;
    /**
     * Constructor of this class. Set error message to the given message
     * along with the cause
     * 
     * @param message - error message
     * @param cause - cause of the error
     * 
     * */
    public AddATudeException(String message, Throwable cause) {
        super(message,cause);
    }
    /**
     * Constructor of this class. Set error message to the given message
     * @param message - error message
     * */
    public AddATudeException(String message) {       
        super(message);
    }

}