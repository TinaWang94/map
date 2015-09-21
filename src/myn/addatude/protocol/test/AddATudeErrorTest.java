/*
 * Classname : AddATudeErrorTest
 *
 * Version information : 1.0
 *
 * Date : 9/20/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 * 
 * Assignment : program1
 */
package myn.addatude.protocol.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.UnsupportedEncodingException;

import myn.addatude.protocol.AddATudeError;
import myn.addatude.protocol.AddATudeException;
import myn.addatude.protocol.AddATudeMessage;
import myn.addatude.protocol.MessageInput;

import org.junit.Test;
/**
 * test for AddATudeError
 * @author tong wang
 *
 */
public class AddATudeErrorTest {

    private AddATudeError a;
    
    /**
     * test decode of error message and toString function
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     * @throws UnsupportedEncodingException - unsupported encoding exception
     * 
     * */
    @Test
    public void testDecode() throws EOFException, AddATudeException, UnsupportedEncodingException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 ERROR 5 error\r\n".getBytes("ASCII")));
        
        @SuppressWarnings("static-access")
        AddATudeMessage b=a.decode(in);

        assertEquals("mapId=345, error message=error.", b.toString());
    }
    /**
     * invalid end of line format, should throw an exception
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     * @throws UnsupportedEncodingException - unsupported encoding exception
     * 
     * */
    @Test (expected = AddATudeException.class)
    public void testEofL() throws UnsupportedEncodingException, EOFException, AddATudeException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 ERROR 5 error \r\n".getBytes("ASCII")));
        
        @SuppressWarnings({ "static-access", "unused" })
        AddATudeMessage b=a.decode(in);

    }

    /**
     * invalid error message
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     * @throws UnsupportedEncodingException - unsupported encoding exception
     * 
     * */
    @Test (expected = AddATudeException.class)
    public void testInvalidErrorMsg() throws UnsupportedEncodingException, EOFException, AddATudeException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 ERROR 5 errr\r\n".getBytes("ASCII")));
        
        @SuppressWarnings({ "static-access", "unused" })
        AddATudeMessage b=a.decode(in);

    }
    /**
     * test get errorMessage function
     * 
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     * @throws UnsupportedEncodingException - unsupported encoding exception
     * */
    @Test          
    public void testGetErrorMsg() throws EOFException, AddATudeException, UnsupportedEncodingException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 ERROR 5 error\r\n".getBytes("ASCII")));
        @SuppressWarnings({ "static-access" })
        AddATudeMessage b=a.decode(in);
        assertEquals("error", ((AddATudeError)b).getErrorMessage());

    }
    /**
     * test set errorMessage function
     * it should throw an AddATudeException since null is invalid for error msg
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     * @throws UnsupportedEncodingException - unsupported encoding exception
     * */
    @Test (expected = AddATudeException.class)
    public void testSetErrorMessage() throws AddATudeException, EOFException, UnsupportedEncodingException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 ERROR 5 error\r\n".getBytes("ASCII")));
        @SuppressWarnings({ "static-access" })
        AddATudeMessage b=a.decode(in);
        ((AddATudeError)b).setErrorMessage(null);

    }
}
