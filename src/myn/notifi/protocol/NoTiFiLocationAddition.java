/*
 * Classname : NoTiFiLocationAddition
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

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;


/**
 * Purpose of this class:
 * Represents a location addition message
 * */

public class NoTiFiLocationAddition extends NoTiFiMessage{
    /*constant for code and error message*/
    private final String errorMsg = "Invalid locationRecord.";
    private final int code=ConstVal.add;
    /*member variable*/
    private LocationRecord locationRecord;
    
    
    /**
     * check valiation for location record
     * 
     * @param lr - location record to be checked
     * @throws IllegalArgumentException - Serializes message for data
     */
    private void checkLR(LocationRecord lr) throws IllegalArgumentException {
        if(lr == null)
            throw new IllegalArgumentException(errorMsg);
    }
    
    /**
     * Constructs NoTiFi location addition from input stream
     * 
     * @param in  - deserialization input source
     * @throws IllegalArgumentException - Serializes message for data
     * @throws IOException - if I/O problem
     */
    public NoTiFiLocationAddition(DataInputStream in) throws IllegalArgumentException, IOException{
        
    }
    
    /**
     * Constructs NoTiFi location addition from values
     * 
     * @param msgId - message ID
     * @param locationRecord - location record
     * @throws IllegalArgumentException - if message ID invalid
     */
    public NoTiFiLocationAddition(int msgId, LocationRecord locationRecord) 
            throws IllegalArgumentException{
        super(msgId);
        checkLR(locationRecord);
        this.locationRecord=locationRecord;
    }

    /**
     * Constructs NoTiFi location addition from values and dataInput
     * 
     * @param in - dataInput 
     * @param msgId - message ID
     * @param version- version info
     * @throws IOException - if I/O problem
     * @throws IllegalArgumentException - if message ID invalid
     */
    public NoTiFiLocationAddition(DataInputStream in,int msgId, int version)
            throws IOException, IllegalArgumentException{
        super(msgId);
        this.version=version;
        locationRecord=new LocationRecord(in);
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
     * Get location record
     * 
     * @return location record
     */
    public LocationRecord getLocationRecord() {
        return locationRecord;
    }
    /**
     * 
     * Set location record
     * @param locationRecord  - location record
     * @throws IllegalArgumentException - if location record is null
     */
    public void setLocation(LocationRecord locationRecord) throws IllegalArgumentException {
        checkLR(locationRecord);
        this.locationRecord = locationRecord;
    }
    
    /**
     * toString in class NoTiFiMessage
     * 
     * @return human readable result string
     * */
    @Override
    public String toString() {
        return "Location Addition: "+locationRecord.toString();
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
     * @throws IOException - if I/O problem
     */
    @Override
    public byte[] encodeHelper() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        DataOutput out = new DataOutputStream(outStream);
        locationRecord.encode(out);
        return outStream.toByteArray();
    } 
    /**
     * override hashCode in class java.lang.Object
     * 
     * */
    @Override
    public int hashCode() {
        
        return Objects.hash(code,locationRecord);
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

        if( !(obj instanceof NoTiFiLocationAddition) ) {
            return false;
        }
        
        if(obj == this) {
            return true;
        }
        if (! super.equals(obj)) return false;
        return ((NoTiFiLocationAddition)obj).code == code 
                &&((NoTiFiLocationAddition)obj).locationRecord.equals(locationRecord);

    }
}
