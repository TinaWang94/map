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
import java.io.IOException;


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

    
    /**
     * test decode of error message and toString function
     * @throws AddATudeException - if deserialization or validation failure
     * @throws IOException - IOException
     * 
     * */
    @Test
    public void testDecode() throws AddATudeException, IOException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 ERROR 5 error\r\n".getBytes("ASCII")));
        
        AddATudeMessage b=AddATudeMessage.decode(in);

        assertEquals("Error: error", b.toString());
    }
    /**
     * invalid end of line format, should throw an exception
     * @throws AddATudeException - if deserialization or validation failure
     * @throws IOException -IOException
     * 
     * */
    @Test (expected = AddATudeException.class)
    public void testEofL() throws AddATudeException, IOException  {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 ERROR 5 error \r\n".getBytes("ASCII")));
        
        @SuppressWarnings("unused")
        AddATudeMessage b=AddATudeMessage.decode(in);

    }

    /**
     * invalid error message
     * @throws AddATudeException - if deserialization or validation failure
     * @throws IOException - IOException
     * 
     * */
    @Test (expected = EOFException.class)
    public void testInvalidErrorMsg() throws AddATudeException, IOException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 ERROR 5 errr\r\n".getBytes("ASCII")));
        
        @SuppressWarnings("unused")
        AddATudeMessage b=AddATudeMessage.decode(in);

    }
    /**
     * test get errorMessage function
     * 
     * @throws AddATudeException - if deserialization or validation failure
     * @throws IOException - IOException
     * */
    @Test          
    public void testGetErrorMsg() throws AddATudeException, IOException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 ERROR 5 error\r\n".getBytes("ASCII")));

        AddATudeMessage b=AddATudeMessage.decode(in);
        assertEquals("error", ((AddATudeError)b).getErrorMessage());

    }
    /**
     * test set errorMessage function
     * it should throw an AddATudeException since null is invalid for error msg
     * @throws AddATudeException - if deserialization or validation failure
     * @throws IOException - IOException
     * */ 
    @Test (expected = AddATudeException.class)
    public void testSetErrorMessage() throws AddATudeException, IOException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 ERROR 5 error\r\n".getBytes("ASCII")));
        AddATudeMessage b=AddATudeMessage.decode(in);
        ((AddATudeError)b).setErrorMessage(null);

    }
}
