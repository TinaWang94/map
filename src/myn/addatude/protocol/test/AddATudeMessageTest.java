/*
 * Classname : AddATudeMessageTest
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


import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import myn.addatude.protocol.AddATudeException;
import myn.addatude.protocol.AddATudeMessage;
import myn.addatude.protocol.MessageInput;

import org.junit.Test;

/**
 * test for AddATudeMessage
 * @author tong wang
 *
 */
public class AddATudeMessageTest {

    private AddATudeMessage a;

    /**
     * test the request message
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     */
    @Test
    public void testRequest() throws AddATudeException, IOException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 ALL \r\n".getBytes("ASCII")));
        
        @SuppressWarnings("static-access")
        AddATudeMessage b=a.decode(in);
        assertEquals("ALL", b.getOperation());
        
    }
    
    /**
     * test the header of message since there is a given header for message
     * It will throw an exception since the format of header is wrong 
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     */
    @Test (expected = AddATudeException.class)
    public void testHeader() throws AddATudeException, IOException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATDEv1 345 ALL \r\n".getBytes("ASCII")));
        @SuppressWarnings({ "static-access", "unused" })
        AddATudeMessage b=a.decode(in);

    }
    /**
     * test the mapId
     * It will throw an exception since mapId should be non-nagative value 
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     */
    @Test (expected = AddATudeException.class)
    public void testMapId() throws AddATudeException, IOException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 -1 ALL \r\n".getBytes("ASCII")));
        @SuppressWarnings({ "static-access", "unused" })
        AddATudeMessage b=a.decode(in);

    }
    
    /**
     * test setmapId
     * It will throw an exception since mapId should be non-nagative value 
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     */
    @Test (expected = AddATudeException.class)
    public void testSetMapId() throws AddATudeException, IOException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 ALL \r\n".getBytes("ASCII")));
        @SuppressWarnings({ "static-access" })
        AddATudeMessage b=a.decode(in);
        b.setMapId(-1);

    }
    
    /**
     * test setmapId
     * It will throw an exception since mapId should be non-nagative value 
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     * @throws UnsupportedEncodingException - unsupported encoding exception
     */
    @Test (expected = EOFException.class)
    public void testEoF() throws AddATudeException, EOFException, UnsupportedEncodingException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 AL".getBytes("ASCII")));
        @SuppressWarnings({ "static-access" })
        AddATudeMessage b=a.decode(in);
        b.setMapId(-1);

    }
    
    
    
    /**
     * test short decode
     * It will throw an exception since message is short
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     * @throws UnsupportedEncodingException - unsupported encoding exception
     */
    @Test (expected = EOFException.class)
    public void testShort() throws AddATudeException, EOFException, UnsupportedEncodingException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDA".getBytes("ASCII")));
        @SuppressWarnings({ "static-access" })
        AddATudeMessage b=a.decode(in);
        b.setMapId(-1);

    }
    
    /**
     * test getMapId
     * 
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     */
    @Test 
    public void testGetMapId() throws AddATudeException, IOException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 ALL \r\n".getBytes("ASCII")));
        @SuppressWarnings({ "static-access" })
        AddATudeMessage b=a.decode(in);
        assertEquals(345,b.getMapId());

    }
    
    /**
    * test response message
    * @throws AddATudeException - if deserialization or validation failure
    * @throws EOFException - if premature end of stream
    */
    @Test
    public void testResponse() throws AddATudeException, IOException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 RESPONSE 2 BU1 1 1.2 3.4 2 BU6 Baylor\r\n".getBytes("ASCII")));
        
        @SuppressWarnings("static-access")
        AddATudeMessage b=a.decode(in);
        assertEquals("RESPONSE", b.getOperation());
        assertEquals("mapId=345 - BU\r\n"
                + "User 1:location name=Baylor."
                + "longitude=1.2, latitude=3.4, location=BU, ", b.toString());

    }
    
    /**
     * test error message
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     */
    @Test
    public void testError() throws AddATudeException, IOException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 ERROR 5 error\r\n".getBytes("ASCII")));
        
        @SuppressWarnings("static-access")
        AddATudeMessage b=a.decode(in);

        assertEquals("mapId=345, error message=error.", b.toString());
    }
    
    /**
     * test new location record
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     */
    @Test
    public void testNewLocationRecord() throws AddATudeException, IOException {
        MessageInput in = new MessageInput(new ByteArrayInputStream("ADDATUDEv1 345 NEW 1 1.2 3.4 2 BU6 Baylor\r\n".getBytes("ASCII")));
        
        @SuppressWarnings("static-access")
        AddATudeMessage b=a.decode(in);
        assertEquals("NEW", b.getOperation());
        assertEquals("mapId=345.new LocationRecord=User 1:location name=Baylor."
                + "longitude=1.2, latitude=3.4, location=BU, ", b.toString());
    }

}