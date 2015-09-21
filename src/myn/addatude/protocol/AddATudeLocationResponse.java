/*
 * Classname : AddATudeLocationResponse
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
 */
package myn.addatude.protocol;

import java.io.EOFException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
/**
 * Purpose:
 * Represents an AddATude location response message
 * and provides serialization/deserialization
 * 
 * @author tong wang
 * 
 * */
public class AddATudeLocationResponse extends AddATudeMessage{
    /*number of locatio Record*/
    private int lrNum;
    /*location record list*/
    private List<LocationRecord> lr;
    /*map name*/
    private String mapName;
    /*operation name, should be "response" in this class*/
    private final String operation="RESPONSE";
    
    
    /**
     * Constructs location response using set values
     * @param mapId - ID for message map
     * @param mapName - name of location
     * @throws AddATudeException - if validation fails
     */
    public AddATudeLocationResponse(int mapId,String mapName)
            throws AddATudeException {
        checkMapId(mapId);
        this.mapId=mapId;
        // TODO Auto-generated constructor stub
        if(mapName == null) {
            throw new AddATudeException("Name of map shouldn't be null");
        }   
        this.mapName=mapName;       
    }

    /**
     * override encode function in class AddATudeMessage
     * 
     * @param out - serialization output destination
     * 
     * @throws AddATudeException - if serialization output fails
     * */
    @Override
    public void encode(MessageOutput out) throws AddATudeException {
        // TODO Auto-generated method stub
        
        StringBuffer aBuf = new StringBuffer();
        aBuf.append(HEADER+" ");
        aBuf.append(mapId+" ");
        aBuf.append(operation+" ");
        aBuf.append(mapName.length()+" ");
        aBuf.append(lrNum+" ");
        
        String aString = new String(aBuf);
        try {
            out.write(aString.getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new AddATudeException ("serialization output fails");
        }
        
        for(LocationRecord x: lr ) {
            x.encode(out);
        }
        
        aString=EOLN;
        try {
            out.write(aString.getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new AddATudeException ("serialization output fails");
        }
    }
    
    /**
     * AddATudeError decode the location response part
     * @param in-massageInput 
     * @param mapId - mapId extends from parent class
     * @throws EOFException - end of file error 
     * @throws AddATudeException - invalid of length of error message
     * 
     * */
    public AddATudeLocationResponse(MessageInput in,int mapId) throws AddATudeException, EOFException {
        String aString = null;
        checkMapId(mapId);
        this.mapId=mapId;
        int length = 0;    
        
        //get map name
        //i)get length of map name 
        aString = readToSpace(in);
        if(!aString.matches(CHECK2)) {
            throw new AddATudeException("Length of map name doesn't match the given format.");
        }
        length = Integer.valueOf(aString);
        //ii)get the character list of the name
        mapName=readByNum(length,in);
        
        //get the record num
        aString = readToSpace(in);
        if(!aString.matches(CHECK2)) {
            throw new AddATudeException("LocationRecord list's count doesn't match the given format.");
        }
        
        length = Integer.valueOf(aString);
        lrNum=length;
        //get LocationRecord List
        //Potential error
        if(lr == null) {
            lr = new LinkedList<>();
        }
        for(int i = 0;i<length;i++) {
            lr.add(new LocationRecord(in));
        }
    }
    
     /**
     * get number of location record 
     * @return lrNum - number of location record
     */
     public int getLrNum() {
         return lrNum;
     }
     
     /**
     * Returns map name
     * @return mapName - map name
     */
    
     public String getMapName() {
         return mapName;
     }
    
     /**
     * Returns list of map locations
     * @return lr - map locations
     */
    
     public List<LocationRecord> getLocationRecordList() {
         return lr;
     } 

     /**
     * Sets map name
     * @param mapName -  new name
     * @throws AddATudeException - if map name is null
     */
     public final void setMapName(String mapName) throws AddATudeException {
         if(mapName == null) {
             throw new AddATudeException("Name of map shouldn't be null");
         }
         this.mapName=mapName;
     }
     
     /**
     * Adds new location
     * @param location - new location to add
     * @throws AddATudeException - if location record is null
     */
    public void addLocationRecord (LocationRecord location) throws AddATudeException {
        if(location==null) {
            throw new AddATudeException ("LocationRecord shouldn't be null");
        }
        lrNum++;
        lr.add(location);
     }
     
    /**
     * override getOperation function from parent class
     * @return operation- basicly "response" for this class
     * 
     * */
     @Override
     public String getOperation() {
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
        
         result.append("mapId="+mapId+" - "); 
         result.append(mapName+EOLN);
         
         for(int i=0;i<lrNum;i++) {
             
             result.append(lr.get(i).toString());
         }
         return result.toString();
     }
    
}