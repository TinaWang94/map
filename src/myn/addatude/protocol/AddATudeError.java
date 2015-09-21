/*
 * Classname : AddATudeError
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
/**
 * 
 *  This class extends the message class
 *  and for set the error message
 * @author Tong Wang
 * @version 1.0
 * 
 * */
public class AddATudeError extends AddATudeMessage{

   
    /* variable errorMessage for error message*/
    private String errorMessage;
    private final String operation="ERROR";
    /**
     * an empty constructor used for testing
     * 
     * */
    public AddATudeError() {
        
    }
    /**
     * constructor AddATudeError using set values
     * @param mapId - ID for message map
     * @param errorMessage - error message
     * @throws AddATudeException - if validation fails
     * 
     * */

    public AddATudeError(int mapId,String errorMessage) throws AddATudeException {
        checkMapId(mapId);
        this.mapId=mapId;
        // TODO Auto-generated constructor stub
        if(errorMessage == null) {
            throw new AddATudeException ("Error message shouldn't be null");
        }
        this.errorMessage=errorMessage;
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
        aBuf.append(errorMessage.length()+" ");
        aBuf.append(errorMessage+EOLN);
        String aString = new String(aBuf);
        try {
            out.write(aString.getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
           throw new AddATudeException("serialization output fails");
        }
        
    }
    /**
     * AddATudeError decode the error message part
     * @param in-massageInput 
     * @param mapId - mapId extends from parent class
     * @throws EOFException - end of file error 
     * @throws AddATudeException - invalid of length of error message
     * 
     * */
    public AddATudeError(MessageInput in,int mapId) throws EOFException, AddATudeException {
        String aString = null;
        checkMapId(mapId);
        this.mapId=mapId;
        int length=0;
        
        //get message (this message should be a string)
        aString = readToSpace(in);
        if(!aString.matches(CHECK2)) {
            throw new AddATudeException("Length of error message doesn't match the given format.");
        }
        length = Integer.valueOf(aString);
        
        aString=readByNum(length,in);  
        errorMessage=aString;
    }
  
    /**
     * set values of error message 
     * @param errorMessage - a strin gof error message
     * @throws AddATudeException - throw exception when error message is null
     */
    public final void setErrorMessage (String errorMessage) throws AddATudeException {
        if(errorMessage == null) {
            throw new AddATudeException("Error message shouldn't be null");
        }
        this.errorMessage=errorMessage;
    }
    /**
     * get error message 
     * return error message
     * @return errorMssage-error message
     * */
    public final String getErrorMessage() {
        return errorMessage;
    }
    /**
     * override toString function
     * 
     * @return Human readable string output
     * */
    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        
        result.append("mapId="+mapId+", ");
        result.append("error message="+errorMessage+".");
        return result.toString();
    }
    
    /**
     * override getOperation function from parent class
     * @return operation- basicly "error" for this class
     * 
     * */
    @Override
    public String getOperation() {     
        return operation;
    }

}