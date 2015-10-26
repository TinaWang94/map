/*
 * Classname : AddATudeLocationRequest
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




/**
 * Represents an AddATude location request 
 * and provides serialization/deserialization
 * @author tong wang
 *
 */

public class AddATudeLocationRequest extends AddATudeMessage {
    /*initialize operation. In this class it should be ALL*/
    private final String operation="ALL";
    
    /**
     * Constructs location request using set values
     * @param mapId -  ID for message map
     * 
     * @throws AddATudeException - if validation fails
     */
    public AddATudeLocationRequest(int mapId) throws AddATudeException {
        checkMapId(mapId);
        this.mapId=mapId;
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
        StringBuffer aBuf = new StringBuffer();

        aBuf.append(operation+" ");
        String aString = new String(aBuf);
        checkShortMsg(out,aString);

    }
    /**
     * AddATudeError decode the request part
      * @param in-massageInput 
     * @param mapId - mapId extends from parent class
     * @throws AddATudeException - invalid of length of error message
     */
    public AddATudeLocationRequest(MessageInput in,int mapId) throws AddATudeException{
        checkMapId(mapId);
        this.mapId=mapId;
    }
   
    /**
     * override getOperation function from parent class
     * @return operation- basicly "all" for this class
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
        
        result.append("mapId="+mapId+","); 
        result.append("operation="+operation+"."); 
        return result.toString();
    }
    
}