/*
 * Classname : AddATudeLocationRecordRequestTest
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
import java.io.IOException;


import myn.addatude.protocol.AddATudeException;
import myn.addatude.protocol.AddATudeLocationRequest;
import myn.addatude.protocol.AddATudeMessage;
import myn.addatude.protocol.MessageInput;

import org.junit.Test;

/**
 * test for AddATudeLocationRequest
 * @author tong wang
 *
 */
public class AddATudeLocationRequestTest {

    private AddATudeLocationRequest a = null;
    /**
     * test decode of request and toString function
     * @throws AddATudeException - if deserialization or validation failure
     * @throws IOException - IOException
     * 
     * */
    @Test
    public void testDecode() throws AddATudeException, IOException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 ALL \r\n".getBytes("ASCII")));
        
        @SuppressWarnings("static-access")
        AddATudeMessage b=a.decode(in);
        assertEquals("mapId=345,operation=ALL.", b.toString());
    }
    
    /**
     * invalid end of line format, should throw an exception
     * @throws AddATudeException - if deserialization or validation failure
     * @throws IOException - IOException
     * 
     * */
    @Test (expected = AddATudeException.class)
    public void testEofL() throws AddATudeException, IOException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 ALL  \r\n".getBytes("ASCII")));
        
        @SuppressWarnings({ "static-access", "unused" })
        AddATudeMessage b=a.decode(in);

    }

}
