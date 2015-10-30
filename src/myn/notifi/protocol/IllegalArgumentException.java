/*
 * Classname : IllegalArgumentException
 *
 * Version information : 1.0
 *
 * Date : 10/29/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 */
package myn.notifi.protocol;
/**
 * Purpose : customized execption for NoTiFi class
 * @author Tong Wang
 * @version 1.0
 * */
public class IllegalArgumentException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor of this class. Set error message to the given message
     * @param message - error message
     * */
    public IllegalArgumentException (String message) {       
        super(message);
    }
}
