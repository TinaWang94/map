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
import java.net.SocketException;
import java.util.Objects;


/**
 * Represents an AddATude new location and provides serialization/deserialization
 * @author tong wang
 * */

public class AddATudeNewLocation extends AddATudeMessage {

    private LocationRecord location;
    private final String operation="NEW";
    public final String errorLoc = "Invalid Locationrecord";
    
    /**
     * check valiation of location record
     * @param lr - a location record waits for check
     * @throws AddATudeException - if validation fails
     */
    private void checkLocation(LocationRecord lr) throws AddATudeException {
        if(lr == null) {
            throw new AddATudeException (errorLoc);
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
     * @throws SocketException - connection error
     */
    public  AddATudeNewLocation(MessageInput in,int mapId ) throws AddATudeException, EOFException, SocketException {
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
    public void encodeHelp(MessageOutput out) throws AddATudeException {
        StringBuffer aBuf = new StringBuffer();
        aBuf.append(operation+" ");
        String aString = new String(aBuf);
        checkShortMsg(out,aString);
        location.encode(out);
     
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

    /**
     * override hashCode in class java.lang.Object
     * 
     * */
    @Override
    public int hashCode() {
        
        return Objects.hash(mapId,location);
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
        if( !(obj instanceof AddATudeNewLocation) ) {
            return false;
        }
        
        if(obj == this) {
            return true;
        }
        AddATudeNewLocation newObj =  ((AddATudeNewLocation)obj);
        if(newObj.mapId != mapId)
            return false;
        if(!newObj.location.equals(location) )
            return false;
        return true;
    }

}