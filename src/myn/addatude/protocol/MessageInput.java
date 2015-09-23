/*
 * Classname : MessageInput
 *
 * Version information : 1.0
 *
 * Date : 9/7/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 */

package myn.addatude.protocol;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * Purpose : Deserialization input source for AddATude messages
 * @author Tong Wang
 * @version 1.0
 * 
 * */

public class MessageInput {
    /*treat message input as a mediator of a inputstream*/
    private InputStream in;
    
    /**
     * Constructs a new input source from an InputStream
     * @param in - byte input source
     * */
    
    public MessageInput(InputStream in) {
        this.in = in;
    }
    
    /**
     * Reads the next byte of data from the MessageInput.
     * @return the total number of bytes read into the buffer,
     *         or -1 if there is no more data because the end
     *         of the stream has been reached.
     * @throws IOException - read in error
     * */
    
    public int read() throws IOException {
        return in.read();
    }
    
    /**
     * Reads some number of bytes from the MessageInput stream
     *  and stores them into the buffer array b.
     *  @param b - the buffer into which the data is read.
     *  @return the total number of bytes read into the buffer,
     *          or -1 if there is no more data because the end
     *          of the stream has been reached.
     *  @throws IOException - read in error
     * */
    
    public int read (byte[] b) throws IOException{
        return in.read(b);
    }
    
    /**
     * Reads up to len bytes of data from the input stream 
     * into an array of bytes.
     * @param b - the buffer into which the data is read.
     * @param off - the start offset in array b at which the data is written.
     * @param len - the maximum number of bytes to read.
     * @return the total number of bytes read into the buffer,
     *          or -1 if there is no more data because the end
     *          of the stream has been reached.
     *@throws IOException - read in error
     * */
    
    public int read(byte[] b, int off, int len) throws IOException {
        
        return in.read(b, off, len);
    }
    
    /**
     * Constructs read a MessageInput till hit a space
     * 
     * @param in -input source
     * 
     * @return aString-the result string after decoding
     * 
     * @throws EOFException - if premature end of stream
     * 
     * */
    public  String readToSpace () throws EOFException  {
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
    public String readByNum (int num) throws EOFException  {
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
     * Constructs read a operation from message
     * 
     * 
     * @return aString-operation
     * 
     * @throws EOFException - if premature end of stream
     * @throws AddATudeException - invalid format
     * 
     * */
    public String readOperation() throws AddATudeException, EOFException{     
        String aString;
        aString = readToSpace();
        return aString;
        
    }
    /**
     * Construct a function to read to 
     * the end of line from message
     * 
     * @throws EOFException - if premature end of stream
     * @throws AddATudeException - invalid format
     * 
     * */
    public void readToEOLN() throws AddATudeException, EOFException {
        byte []b = new byte[2];
        int length = 0;
        try {
            length=read(b,0,2);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
         if(length != 2) {
             throw new EOFException ("Need EOLN symbol");
         }
         //13 is ASCII code for \r and 10 is ASCII code for \n
         if(b[0] != 13 || b[1] != 10) {
             throw new AddATudeException("End of line format error");
         }

    }
}