/*
 * Classname : NoTiFiError
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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;


/**
 * Purpose of this class:
 * 
 * Represents an error
 * 
 * */

public class NoTiFiError extends NoTiFiMessage {
    /*constants for code and error message*/
    private final String errorMsg = "Invalid error message";
    private final int code=ConstVal.error;
    /*member vareiable*/
    private String errorMessage;
    
    /**
     * check valiaton for error message 
     * 
     * @param msg - message to be checked
     * @throws IllegalArgumentException - Serializes message for data
     */
    private void checkMsg (String msg) throws IllegalArgumentException {
        if(msg == null) {
            throw new IllegalArgumentException (errorMsg);
        }
    }
    
    /**
     * Constructs NoTiFi error from input stream
     * 
     * @param in - deserialization input source
     * @throws IllegalArgumentException - Serializes message for data
     * @throws IOException - if I/O problem
     */
    public NoTiFiError(DataInputStream in) throws IllegalArgumentException, IOException{
        
    }
    /**
     * Constructs NoTiFi error from values
     * 
     * @param msgId  - message ID
     * @param errorMessage  - error message
     * @throws IllegalArgumentException - Serializes message for data
     */
    public NoTiFiError(int msgId,String errorMessage) throws IllegalArgumentException {
        super(msgId);
        checkMsg(errorMessage);
        this.errorMessage=errorMessage;
    }
    /**
     * Constructs NoTiFi error from values (dataInput)
     * 
     * @param in - dataInput
     * @param msgId - message id
     * @param version - version info
     * @throws IOException - if I/O problem
     * @throws IllegalArgumentException - Serializes message for data
     */
    public NoTiFiError(DataInputStream in,int msgId,int version)
            throws IOException, IllegalArgumentException {
        super(msgId);
        this.version=version;
        errorMessage=Parser.readError(in);
    }
    /**
     * Get operation code
     * 
     * @return - operation code
     */
    @Override
    public int getCode() {
        return code;
    }
    
    /**
     * Get error message
     * 
     * @return error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    /**
     * Set error message
     * 
     * @param errorMessage - error message
     * @throws IllegalArgumentException - Serializes message for data
     */
    public void setErrorMessage(String errorMessage) throws IllegalArgumentException {
        checkMsg(errorMessage);
        this.errorMessage=errorMessage;
    }
    
    /**
     * toString in class NoTiFiMessage
     * 
     * @return human readable result string
     * */
    @Override
    public String toString() {
        return "Error messgage: "+errorMessage;
    }
    /**
     * impliment abstract function from parent class,
     * encode version and code info
     * 
     * @return Serializes message for header
     */
    @Override
    public byte[] encodeHeader() {
        byte [] b= new byte[1];
        b[0]=Parser.appendBit(ConstVal.version, code);
        return b;
    }
    /**
     * impliment abstract function from parent class, encode data.
     * Type of data depends on code
     * 
     * @return - Serializes message for data
     */
    @Override
    public byte[] encodeHelper() {
        return errorMessage.getBytes();
    } 
    @Override
    public boolean equals(Object obj){
        if( !(obj instanceof NoTiFiError) ) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        
        return ((NoTiFiError)obj).errorMessage.compareTo(errorMessage) == 0 
                && ((NoTiFiError)obj).code == code;
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(code, errorMessage);
    }
}

