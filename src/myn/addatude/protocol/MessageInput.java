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

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

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
     * read in everything from inputstream and print out to screen
     * for testing purpose
     * 
     * @throws IOException - input error
     */
    public void readAll() throws IOException {
        int bt = 0;
        StringBuffer aBuf = new StringBuffer();
        String aString = null;
        while((bt=in.read()) != -1) {
            aBuf.append((char)bt);
        }
        aString=aBuf.toString();
        System.out.println(aString+"\r\n");
    }
    /**
     * Constructs a new input source from an InputStream
     * @param in - byte input source 
     * */
    public MessageInput(InputStream in) {
        this.in = in;
    }
    public MessageInput(byte[] b) {
        InputStream i = new ByteArrayInputStream(b);
        this.in = i;
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
     * @throws IOException - read in error
     * */
    
    public int read(byte[] b, int off, int len) throws IOException {
        
        return in.read(b, off, len);
    }
    
    /**
     * Constructs read a MessageInput till hit a space
     * 
     * 
     * @return aString-the result string after decoding
     * 
     * @throws EOFException - if premature end of stream
     * @throws SocketException - connection error
     * */
    public  String readToSpace () throws EOFException, SocketException  {
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
            
            if(e instanceof SocketException) {
                throw new SocketException("Connection error.");
            }
            else {
                e.printStackTrace();
            }
        }
        if(bt == -1) {
            throw new EOFException("This message needs more information.");
        }
            
        return aString;
    }
    
    /**
     * Constructs read a MessageInput by byte
     * 
     * @param num- number of bytes we should read
     * 
     * @return aString-the result string after decoding
     * 
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
     * @throws SocketException - connection error
     * 
     * */
    public String readOperation() throws AddATudeException, EOFException, SocketException{     
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

        byte bt = 0;
        byte bt1 = 0;
        try {          
            bt=(byte) in.read();
            bt1=(byte) in.read();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
         
         if(bt != 13 || bt1 != 10) {
             throw new EOFException("End of line error");
         }

    }
}