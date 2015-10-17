/*
    * Classname : LocationRecordTest
 *
 * Version information : 1.0
 *
 * Date : 9/7/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 */
package myn.addatude.protocol.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;

import myn.addatude.protocol.AddATudeException;
import myn.addatude.protocol.LocationRecord;
import myn.addatude.protocol.MessageInput;

import org.junit.Test;
/**
 * 
 * Purpose : A JUnit4 test class that test 
 *          the LocationRecord class.
 * @author Tong Wang
 * @version 1.0
 * 
 * */

public class LocationRecordTest {
    /*local variable lr*/
    private LocationRecord lr;

    /**
     * Initialize a LocationRecord variable and a MessageInput variable
     * for later testing use.
     * @throws AddATudeException -  if validation failure
     * @throws IOException - if premature end of stream 
     * */ 
    
    public LocationRecordTest() throws AddATudeException,IOException {
        try {
            MessageInput in = new MessageInput(new ByteArrayInputStream("1 1.2 3.4 2 BU6 Baylor".getBytes("ASCII")));
            lr = new LocationRecord(in);
            
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Test for Encode function 
     * by using local location record variable lr
     * 
     * */ 
    
    @Test
    public void testEncode() {
        
        assertEquals(1, lr.getUserId());
        assertEquals("1.2", lr.getLongitude());
        assertEquals("3.4", lr.getLatitude());
        assertEquals("BU", lr.getLocationName());
        assertEquals("Baylor", lr.getLocationDescription());

    }
    
    /**
     * Test for getLongitude function 
     * by using local location record variable lr
     * 
     * */ 
    
    @Test
    public void testGetLatitude() {
        assertEquals("3.4", lr.getLatitude());
    }
    
    /**
     * Test for getLocationDescription function 
     * by using local location record variable lr
     * 
     * */    
    
    @Test
    public void testGetLocationDescription() {
        assertEquals("Baylor", lr.getLocationDescription());
    }
    
    /**
     * Test for getLocationName function 
     * by using local location record variable lr
     * 
     * */    
    
    @Test
    public void testGetLocationName() {
        assertEquals("BU", lr.getLocationName());
    }
    
    /**
     * Test for getLongitude function 
     * by using local location record variable lr
     * 
     * */
    
    @Test
    public void testGetLongitude() {
        assertEquals("1.2", lr.getLongitude());
    }
    
    /**
     * Test for getUserId function 
     * by using local location record variable lr
     * 
     * */
    @Test
    public void testGetUserId() {
        assertEquals(1, lr.getUserId());
    }
    
    /**
     * This function can test the invalid userId exception.
     * -1 is invalid for id since it should be an unsigned integer
     * So EAddATudeException should be thrown.
     * 
     * @throws AddATudeException -  if validation failure
     * */
    
    @Test(expected = AddATudeException.class)
    public void testConstructorException1() throws AddATudeException {
        int id = -1;
        String longitude = "@";
        String latitude = "@@";
        @SuppressWarnings("unused")
        LocationRecord lrs = new LocationRecord(id,longitude,latitude,null,null);
       
    }
    
    /**
     * This function can test the invalid location name exception.
     * is invalid for location name and description
     * So EAddATudeException should be thrown.
     * 
     * @throws AddATudeException -  if validation failure
     * */
    
    @Test(expected = AddATudeException.class)
    public void testConstructorException2() throws AddATudeException {
        int id = 1;
        String longitude = "@";
        String latitude = "@@";
        @SuppressWarnings("unused")
        LocationRecord lrs = new LocationRecord(id,longitude,latitude,null,null);
       
    }
    
    /**
     * This function can test the eof exception.
     * There is no location description in he input string
     * so it is the situation which premature end of stream
     * EOFException should be thrown
     * 
     * @throws EOFException - if premature end of stream 
     * @throws SocketException - connection error
     * */
    
    @Test(expected = EOFException.class)
    public void testConstructorException3() throws EOFException, SocketException {
        MessageInput in2 = null;
        try {
            in2 = new MessageInput(new ByteArrayInputStream("1 1.2 3.4 2 Bu4".getBytes("ASCII")));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        try {
            @SuppressWarnings("unused")
            LocationRecord lrs = new LocationRecord(in2);
        } catch (AddATudeException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
    }
    /**
     * This function can test the set function.
     * Since set functions are void return, so we can
     * test them by throwing exceptions
     * 
     * null is invalid for name, so exception should be thrown
     * 
     * @throws AddATudeException -  if validation failure
     * @throws EOFException - if premature end of stream 
     * @throws UnsupportedEncodingException - unsupported encoding exception
     * @throws SocketException - connection error
     * */
    
    @Test(expected = AddATudeException.class)
    public void testSetLocationNameException() 
            throws AddATudeException, UnsupportedEncodingException, EOFException, SocketException {
        MessageInput in = new MessageInput
                (new ByteArrayInputStream("1 1.2 3.4 2 BU6 Baylor".getBytes("ASCII")));
        LocationRecord lr2 = new LocationRecord(in);
        
        lr2.setLocationName(null);
    }
    
    /**
     * Test the equals and toString function
     * the toString function should react properly and the equal function
     * should return false since the two record are different.
     * 
     * Also, this function can test if this class can handle multiple records
     *  from one inputstream.
     *  
     * @throws AddATudeException -  if validation failure
     * @throws EOFException - if premature end of stream 
     * @throws UnsupportedEncodingException - unsupported encoding exception
     * @throws SocketException - connection error
     */
    
    @Test
    public void testEqualsAndToString() throws EOFException, AddATudeException, UnsupportedEncodingException, SocketException {
        
        MessageInput in = new MessageInput
                (new ByteArrayInputStream("1 1.2 3.4 2 BU6 Baylor2 2.2 2.4 2 bu6 baylor".getBytes("ASCII")));
        LocationRecord lr2 = new LocationRecord(in);
        assertEquals("userId=1, longitude=1.2, latitude=3.4, location=BU, location name=Baylor."
                ,lr2.toString());
        LocationRecord lr3 = new LocationRecord(in);
        
        assertEquals(false, lr2.equals(lr3));
        
        
    }

}