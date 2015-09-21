/*
 * Classname : AddATudeMessage
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
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Represents generic portion of AddATude message
 *  
 * */
public abstract class AddATudeMessage {
    /*initialize some constant strings for check purpose */
    public static final String HEADER="ADDATUDEv1";
    public static final String CHECK = "^[-+]?[0-9]*\\.?[0-9]+$";
    public static final String CHECK2 = "0|[1-9][0-9]*";
    public static final String NEW = "NEW";
    public static final String EOLN = "\r\n";
    public static final String ALL = "ALL";
    public static final String ERROR = "ERROR";
    public static final String RESPONSE = "RESPONSE";
    /*mapId for message */
    protected  int mapId;
    
    protected void checkMapId(int mapId) throws AddATudeException {
        if(mapId < 0) {
            throw new AddATudeException ("Invalid mapId.");
        }
            
    }
    /**
     * A protected function.
     * purpose: read a MessageInput utill hit a space
     * 
     * @param in -input source
     * 
     * @return aString - the result string after decoding
     * 
     * @throws EOFException - if premature end of stream
     * 
     * */
    protected static String readToSpace (MessageInput in) throws EOFException  {
        int bt = 0;
        StringBuffer aBuf = new StringBuffer();
        String aString = null;
    
        try {
            while((bt=in.read()) != -1) {
                //32 is ASCII num of a single space                   
                if(bt == 32 ) {
                    aString=aBuf.toString();     
                    break;
                }
                else{      
                    //put the byte just read in buffer
                    aBuf.append((char)bt);
                }  
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bt == -1) {
            throw new EOFException("This message needs more information.");
        }
            
        return aString;
    }
    
    /**
     * Constructs read a MessageInput by byte
     * 
     * @param in -input source
     * @param num- number of bytes we should read
     * 
     * @return aString-the result string after decoding
     * 
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     * 
     * */
    protected String readByNum (int num, MessageInput in) throws EOFException  {
        int bt = 0;
        StringBuffer aBuf = new StringBuffer();
        String aString = null;
    
        for(int i=0;i<num;i++) {
            try {
                if((bt=in.read()) != -1 ) {
                    aBuf.append((char)bt);
                }
                else {
                    throw new EOFException("This location record needs more information.");
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                if(e instanceof EOFException) {
                    throw new EOFException("This location record needs more information.");
                }
                e.printStackTrace();
            }
        }
            aString=aBuf.toString();  
            return aString;
    }
    
    /**
     * An constructor using scanner
     * 
     * @param in - input by scanner
     * @param out - output by PrintStream
     */
    public AddATudeMessage(Scanner in, PrintStream out) {
        
    }
    
    
    
    /**
     * Deserializes message from byte source
     * 
     * @param in - deserialization input source
     * @return a - a specific AddATude message resulting from deserialization
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     */
    static public AddATudeMessage decode(MessageInput in) throws AddATudeException, EOFException{
        String aString = null;
        
        aString = readToSpace(in);
        if(!HEADER.equals(aString) ) {
            throw new AddATudeException("The format of header is incorrect");
        }
        aString = readToSpace(in);
        if(!aString.matches(CHECK2)) {
            throw new AddATudeException("mapId doesn't match the given format.");
        }

        if(Integer.valueOf(aString) < 0) {
            throw new AddATudeException("mapId should be a non-nagative number.");
        }
        int id=Integer.valueOf(aString);
        
        String operation = in.readOperation();
        AddATudeMessage a;
        switch(operation) {
        case NEW:
            a = new AddATudeNewLocation(in,id); 
            break;
        case ERROR:
            a=new AddATudeError(in,id);
            break;
        case RESPONSE:
            a = new AddATudeLocationResponse(in,id);
            break;
        case ALL:
            a = new AddATudeLocationRequest(in,id);
            break;
        default:
            throw new AddATudeException("Operation not found");
        } 
        
        byte[] b=new byte[2];
        int length=0;
        try {
            length = in.read(b,0,2);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(length != 2) {
            throw new AddATudeException(" Need EOLN symbol");
        }
        if(b[0] != 13 || b[1] != 10) {
            throw new AddATudeException("End of line format error");
        }
        
        return a;
    }
  
    
    /**
     * Returns map ID
     * @return mapId - map ID of message
     */
    
    public int getMapId () {
        return mapId;
    }
    
    /**
     * Sets map ID
     * 
     * @param mapId - new map ID
     * @throws AddATudeException - if validation fails
     */
    public void setMapId (int mapId) throws AddATudeException {
        checkMapId(mapId);
        this.mapId=mapId;
    }
    
    /**
     * an empty constructor used by test
     */
    public AddATudeMessage() {
        
    }
    
    /**
     * abstract function used for Serializes message
     * @param out - serialization output destination
     * @throws AddATudeException - if serialization output fails
     */
    abstract public void encode(MessageOutput out) throws AddATudeException ;
    /**
     * abstract function declair toString function
     * may be overrided by children classes
     * 
     * @return Human readable string output
     * */
    public String toString() {
        return null;
        
    }
    /**
     * abstract getOperation function may extend by children classes
     * @return operation- different from every children classes
     * 
     * */
    abstract public String getOperation();
}