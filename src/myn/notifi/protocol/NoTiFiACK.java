/*
 * Classname : NoTiFiACK
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
 * Purpose of the class:
 * 
 * Represents an ACK
 * 
 * */

public class NoTiFiACK extends NoTiFiMessage{
    /*initial constant value: code for ACK*/
    private final int code=ConstVal.ACK;
    
    /**
     * Constructs NoTiFi ACK from input stream
     * 
     * @param in - deserialization input source
     * @throws IllegalArgumentException - if data fails validation
     * @throws IOException - if I/O problems
     */
    public NoTiFiACK(DataInputStream in) throws IllegalArgumentException, IOException{
       
    }
    
    /**
     * Constructs NoTiFi ACK from values
     * 
     * @param msgId - message ID
     * @throws IllegalArgumentException - if data fails validation
     */
    public NoTiFiACK(int msgId) throws IllegalArgumentException {
        super(msgId);
    }
    
    /**
     * Constructs NoTiFi ACK from values(dataInput)
     * 
     * @param in - dataInput
     * @param msgId - MSG ID
     * @param version - version info
     * @throws IllegalArgumentException - if data fails validation
     */
    public NoTiFiACK(DataInputStream in,int msgId,int version) throws IllegalArgumentException {
        super(msgId);
        this.version=version;
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
     * toString in class NoTiFiMessage
     * 
     * @return human readable result string
     * */
    @Override
    public String toString(){
        return "This is an ACK";
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
        return new byte[0];
    }
    @Override
    public int hashCode() {
        
        return Objects.hash(code);
    }
    /**
     * override equals in class java.lang.Object
     * 
     * @param obj - an object
     * @return true if matchs, false otherwise
     * 
     * */
    @Override
    public boolean equals(Object obj) {
        if( !(obj instanceof NoTiFiACK) ) {
            System.out.println("---here");
            return false;
        }        
        if(obj == this) {
            return true;
        }
        if (! super.equals(obj)) return false;
        return ((NoTiFiACK)obj).code == code;

    }
}
