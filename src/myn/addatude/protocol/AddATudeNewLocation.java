/*
 * Classname : AddATudeNewLocation
 *
 * Version information : 1.0
 *
 * Date : 9/13/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 * 
 * Assignment : program1
 * 
 * Description: Represents an AddATude new location 
 *      and provides serialization/deserialization
 */
package myn.addatude.protocol;

import java.io.EOFException;


/**
 * Represents an AddATude new location and provides serialization/deserialization
 * 
 * */

public class AddATudeNewLocation extends AddATudeMessage {

    private LocationRecord location;
    private final String operation="NEW";
    
    /**
     * check valiation of location record
     * @param lr - a location record waits for check
     * @throws AddATudeException - if validation fails
     */
    private void checkLocation(LocationRecord lr) throws AddATudeException {
        if(lr == null) {
            throw new AddATudeException ("Location recoed should not be null.");
        }
    }
    /**
     * Constructs new location using set values
     * @param mapId - ID for message map
     * @param location - new location
     * @throws AddATudeException - if validation fails
     */
    public AddATudeNewLocation (int mapId, LocationRecord location) throws AddATudeException {
        checkMapId(mapId);
        checkLocation(location);
        this.mapId=mapId;
        this.location=location;
    } 
    
    /**
     * Constructs new location using user input
     * Used in parent class for decoding
     * @param in - user input source
     * @param mapId - ID for message map
     * @throws AddATudeException - if validation fails
     * @throws EOFException - if premature end of stream
     */
    public  AddATudeNewLocation(MessageInput in,int mapId ) throws AddATudeException, EOFException {
        checkMapId(mapId);
        this.mapId=mapId;
        
        location=new LocationRecord(in);        
    }
    /**
     * Deserializes message from byte source
     * extends from parent class, used for encode new operation
     * 
     * @param out - serialization output destination
     * @throws AddATudeException - if serialization output fails
     * */
    @Override
    public void encode(MessageOutput out) throws AddATudeException {
        StringBuffer aBuf = new StringBuffer();
        aBuf.append(HEADER+" ");
        aBuf.append(mapId+" ");
        aBuf.append(operation+" ");
        String aString = new String(aBuf);
        checkShortMsg(out,aString);
        location.encode(out);
        aString=EOLN;
        checkShortMsg(out,aString);
        
    }   
    
    /**
     * Sets location
     * @param location - new location
     * @throws AddATudeException - if null error message
     */
    public final  void setLocationRecord (LocationRecord location) throws AddATudeException{
        checkLocation(location);       
        this.location=location;       
    }
    
    /**
     * Returns location
     * @return location - location record
     */
    public final LocationRecord getLocationRecord() {
        return location;
    }
    /**
     * override getOperation function from parent class
     * @return operation- basicly "error" for this class
     * 
     * */
    @Override
    public String getOperation() {
        // TODO Auto-generated method stub
        return operation;
    }
    /**
     * override toString function
     * 
     * @return Human readable string output
     * */
    
    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        
        result.append("mapId="+mapId+"."); 
        result.append("new LocationRecord="+location.toString());
        return result.toString();
    }
}