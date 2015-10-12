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
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
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
    private final String errorMap="Invalid map name.";
    private final String errorLocRec="Invalid LocationRecord list.";
    /**
     * check valiation of mapName
     * @param aName - map name waits for check
     * @throws AddATudeException -  if validation fails
     */
    private void checkMapName(String aName) throws AddATudeException {
        if(aName == null) {
            throw new AddATudeException(errorMap);
        } 
    }
    
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
        lr = new LinkedList<>();
        // TODO Auto-generated constructor stub
        checkMapName(mapName);  
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
    public void encodeHelp(MessageOutput out) throws AddATudeException {
        // TODO Auto-generated method stub
        
        StringBuffer aBuf = new StringBuffer();

        aBuf.append(operation+" ");
        aBuf.append(mapName.length()+" ");
        aBuf.append(mapName);
        aBuf.append(lrNum+" ");
        
        String aString = new String(aBuf);
        checkShortMsg(out,aString);
        
        for(LocationRecord x: lr ) {
            x.encode(out);
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
        aString = in.readToSpace();
        if(!aString.matches(ConstantVariable.CHECKID)) {
            throw new AddATudeException(errorMap);
        }
        length = Integer.valueOf(aString);
        //ii)get the character list of the name
        mapName=in.readByNum(length);
        
        //get the record num
        aString = in.readToSpace();
        if(!aString.matches(ConstantVariable.CHECKID)) {
            throw new AddATudeException(errorLocRec);
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
         checkMapName(mapName);  
         this.mapName=mapName;
     }
     
     /**
     * Adds new location
     * @param location - new location to add
     * @throws AddATudeException - if location record is null
     */
    public void addLocationRecord (LocationRecord location) throws AddATudeException {
        if(location==null) {
            throw new AddATudeException (errorLocRec);
        }
        lrNum++;
        lr.add(location);
     }
    
     public void setLocationRecordList(List<LocationRecord> lr) {
         this.lr=lr;
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
         result.append(mapName+ConstantVariable.EOLN);
         
         for(int i=0;i<lrNum;i++) {
             
             result.append(lr.get(i).toString());
         }
         return result.toString();
     }
    
     @Override
     public int hashCode(){
         return Objects.hash(mapId, lrNum, mapName, lr);
     }
     
     @Override
     public boolean equals(Object obj){
         if( !(obj instanceof AddATudeLocationResponse) ) {
             return false;
         }
         if(obj == this) {
             return true;
         }
         AddATudeLocationResponse newObj =  ((AddATudeLocationResponse)obj);
         if(newObj.mapId != mapId)
             return false;
         if(newObj.lrNum != lrNum)
             return false;
         if(newObj.mapName.compareTo(mapName) != 0)
             return false;
         for(int i = 0; i < lrNum; i++)
             if(!newObj.lr.get(i).equals(lr.get(i)))
                 return false;
         return true;
     }
}