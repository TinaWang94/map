/*
 * Classname : AddATudeNewLocationTest
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
 * 
 * Description:  Test Represents an AddATude new location 
 *      and provides serialization/deserialization
 */
package myn.addatude.protocol.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;

import myn.addatude.protocol.AddATudeException;
import myn.addatude.protocol.AddATudeMessage;
import myn.addatude.protocol.AddATudeNewLocation;
import myn.addatude.protocol.MessageInput;

import org.junit.Test;
/**
 * test for AddATudeNewLocation class
 * @author tong wang
 *
 */
public class AddATudeNewLocationTest {
    private AddATudeNewLocation a = null;

    /**
     * test new location record
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     */
    @Test
    public void testNewLocationRecord() throws AddATudeException, IOException {
        MessageInput in = new MessageInput(new ByteArrayInputStream
                ("ADDATUDEv1 345 NEW 1 1.2 3.4 2 BU6 Baylor\r\n".getBytes("ASCII")));
        
        @SuppressWarnings("static-access")
        AddATudeMessage b=a.decode(in);
        assertEquals("NEW", b.getOperation());
        assertEquals("mapId=345.new LocationRecord=userId=1, longitude=1.2,"
                + " latitude=3.4, location=BU, location name=Baylor.", b.toString());
    }
    
    /**
     * invalid end of line format, should throw an exception
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     * @throws UnsupportedEncodingException - unsupported encoding exception
     * @throws SocketException - connection error
     * 
     * */
    
    @Test (expected = AddATudeException.class)
    public void testEofL() throws UnsupportedEncodingException, EOFException, AddATudeException, SocketException {
        MessageInput in = new MessageInput(new ByteArrayInputStream
                ("ADDATUDEv1 345 NEW 1 1.2 3.4 2 BU6 Baylor \r\n".getBytes("ASCII")));
        
        @SuppressWarnings({ "static-access", "unused" })
        AddATudeMessage b=a.decode(in);

    }
    
    /**
     * test setLocationRecord function
     * it should throw an AddATudeException since null is invalid for LocationRecord
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     * @throws UnsupportedEncodingException - unsupported encoding exception
     * @throws SocketException - connection error
     * */
    @Test (expected = AddATudeException.class)
    public void testSetLocationRecord() throws AddATudeException, EOFException, UnsupportedEncodingException, SocketException {
        MessageInput in = new MessageInput(new ByteArrayInputStream
                ("ADDATUDEv1 345 NEW 1 1.2 3.4 2 BU6 Baylor\r\n".getBytes("ASCII")));
        @SuppressWarnings({ "static-access" })
        AddATudeMessage b=a.decode(in);
        ((AddATudeNewLocation)b).setLocationRecord(null);

    }
    /**
     * test GetLocationRecord function
     * 
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     * @throws UnsupportedEncodingException - unsupported encoding exception
     * @throws SocketException - connection error
     * 
     * */
    @Test 
    public void testGetLocationRecord() throws AddATudeException, EOFException, UnsupportedEncodingException, SocketException {
        MessageInput in = new MessageInput(new ByteArrayInputStream
                ("ADDATUDEv1 345 NEW 1 1.2 3.4 2 BU6 Baylor\r\n".getBytes("ASCII")));
        @SuppressWarnings({ "static-access" })
        AddATudeMessage b=a.decode(in);
        assertEquals("userId=1, longitude=1.2, latitude=3.4, location=BU, location name=Baylor.", 
                ((AddATudeNewLocation)b).getLocationRecord().toString());

    }

    /**
     * test equals function
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     * @throws UnsupportedEncodingException - unsupported
     * @throws SocketException - connection error
     */
    @Test 
    public void testEquals() throws AddATudeException, EOFException, UnsupportedEncodingException, SocketException {
        MessageInput in = new MessageInput(new ByteArrayInputStream
                ("ADDATUDEv1 345 NEW 1 1.2 3.4 2 BU6 Baylor\r\n".getBytes("ASCII")));
        @SuppressWarnings({ "static-access" })
        AddATudeMessage b=a.decode(in);
        assertEquals(true, 
                ((AddATudeNewLocation)b).equals(b));

    }

}
