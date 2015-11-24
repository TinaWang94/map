/*
 * Classname : NoTiFiMessageTest
 *
 * Version information : 1.0
 *
 * Date : 10/29/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 */
package myn.notifi.protocol.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.Inet4Address;

import myn.notifi.protocol.LocationRecord;
import myn.notifi.protocol.NoTiFiACK;
import myn.notifi.protocol.NoTiFiDeregister;
import myn.notifi.protocol.NoTiFiError;
import myn.notifi.protocol.NoTiFiLocationAddition;
import myn.notifi.protocol.NoTiFiLocationDeletion;
import myn.notifi.protocol.NoTiFiMessage;
import myn.notifi.protocol.NoTiFiRegister;

import org.junit.Test;

/**
 * Generate Junit test case
 * @author tong wang 
 * */
public class NoTiFiMessageTest {


    /**
     * Test decode of Registration
     * 
     * @throws IOException - if I/O problems
     * @throws IllegalArgumentException - if data fails validation
     */
    @Test
    public void testDecodeReg() throws  IOException, IllegalArgumentException {
        NoTiFiRegister m = (NoTiFiRegister) NoTiFiMessage.decode
                (new byte[] { 0x30, 0x01, 0x01, 0x00, 0x00, 0x7F, (byte) 0x88, 0x13 });
        assertEquals(1, m.getMsgId());
        assertEquals(0, m.getCode());
        assertEquals(Inet4Address.getByName("127.0.0.1"), m.getAddress());
        assertEquals(5000, m.getPort());
      }
    
    /**
     * Test decode of deRegistration
     * 
     * @throws IOException - if I/O problems
     * @throws IllegalArgumentException - if data fails validation
     */
    @Test
    public void testDecodeDeReg() throws  IOException, IllegalArgumentException {
        NoTiFiDeregister m = (NoTiFiDeregister) NoTiFiMessage.decode(new byte[] { 0x33, 0x01, 0x01, 0x00, 0x00, 0x7F, (byte) 0x88, 0x13 });
        assertEquals(1, m.getMsgId());
        assertEquals(3, m.getCode());
        assertEquals(Inet4Address.getByName("127.0.0.1"), m.getAddress());
        assertEquals(5000, m.getPort());
      }
    
    /**
     * Test decode of Error message
     * 
     * @throws IOException - if I/O problems
     * @throws IllegalArgumentException - if data fails validation
     */
    @Test
    public void testDecodeErr() throws IOException, IllegalArgumentException {
        NoTiFiError m = (NoTiFiError) NoTiFiMessage.decode(new byte [] {0x34, 0x7b, 0x42, 0x61, 0x64, 0x20, 0x65, 0x72, 0x72, 0x6f, 0x72 });
        assertEquals(123, m.getMsgId());
        assertEquals(4, m.getCode());
        assertEquals("Bad error", m.getErrorMessage());
    }
    
    /**
     * Test decode of ACK
     * 
     * @throws IOException - if I/O problems
     * @throws IllegalArgumentException - if data fails validation
     */
    @Test
    public void testDecodeACK() throws IOException, IllegalArgumentException {
        NoTiFiACK m = (NoTiFiACK) NoTiFiMessage.decode(new byte [] {0x35,0x01});
        assertEquals(1, m.getMsgId());
        assertEquals(5, m.getCode());
    }
    
    /**
     * Test IllegalArgument Exception of invalid code
     * 
     * @throws IOException - if I/O problems
     * @throws IllegalArgumentException - if data fails validation
     */
    @Test (expected = IllegalArgumentException.class)
    public void testInvalidCode() throws IOException, IllegalArgumentException{
        @SuppressWarnings("unused")
        NoTiFiMessage m =NoTiFiMessage.decode(new byte [] {0x36,0x01});
    }

    /**
     * Test decode of addition
     * 
     * @throws IOException - if I/O problems
     * @throws IllegalArgumentException - if data fails validation
     */
    @Test
    public void testDecodeAdd() throws IOException, IllegalArgumentException {
        NoTiFiLocationAddition m = (NoTiFiLocationAddition) NoTiFiMessage.decode(new byte [] 
                {0x31, 0x00, 0x00, 0x4D, (byte) 0xB7, 0x7A, 0x4E, 0x7A, (byte) 0xDF, 
                (byte) 0x8C, 0x3F, 0x40, 0x71, (byte) 0xE6, 0x57, 0x73, (byte) 0x80, 
                0x47, 0x58, (byte) 0xC0, 0x02, 0x42, 0x55, 0x06, 0x42, 0x41, 0x59, 0x4C, 0x4F, 0x52 });
        assertEquals(0, m.getMsgId());
        assertEquals(1, m.getCode());
        LocationRecord lr = m.getLocationRecord();
        assertEquals(77,lr.getUserId());
        assertEquals(-97.117215,lr.getLatitude(),0.0);
        assertEquals(31.550285,lr.getLongitude(),0.0);
        assertEquals("BU",lr.getLocationName());
        assertEquals("BAYLOR",lr.getLocationDescription());
    }
    /**
     * Test decode of deletion
     * 
     * @throws IOException - if I/O problems
     * @throws IllegalArgumentException - if data fails validation
     */
    @Test
    public void testDecodeDel() throws IOException, IllegalArgumentException {
        NoTiFiLocationDeletion m = (NoTiFiLocationDeletion) NoTiFiMessage.decode(new byte [] 
                {0x32, 0x00, 0x00, 0x4D, (byte) 0xB7, 0x7A, 0x4E, 0x7A, (byte) 0xDF, 
                (byte) 0x8C, 0x3F, 0x40, 0x71, (byte) 0xE6, 0x57, 0x73, (byte) 0x80, 
                0x47, 0x58, (byte) 0xC0, 0x02, 0x42, 0x55, 0x06, 0x42, 0x41, 0x59, 0x4C, 0x4F, 0x52 });
        assertEquals(0, m.getMsgId());
        assertEquals(2, m.getCode());
        LocationRecord lr = m.getLocationRecord();
        assertEquals(77,lr.getUserId());
        assertEquals(-97.117215,lr.getLatitude(),0.0);
        assertEquals(31.550285,lr.getLongitude(),0.0);
        assertEquals("BU",lr.getLocationName());
        assertEquals("BAYLOR",lr.getLocationDescription());
    }   
    
    /**
     * Test IllegalArgument Exception
     * 
     * @throws IOException - if I/O problems
     * @throws IllegalArgumentException - if data fails validation
     */
    @Test (expected = IllegalArgumentException.class)
    public void testSetMsgId() throws IOException, IllegalArgumentException {
        NoTiFiMessage m = new NoTiFiACK(-1);
        m.setMsgId(-1);
    }  
    
    /**
     * Test IllegalArgument Exception
     * @throws IOException - if I/O problems
     * @throws IllegalArgumentException - if data fails validation
     */
    @Test (expected = IllegalArgumentException.class)
    public void testSetMsgId2() throws IOException, IllegalArgumentException {
        NoTiFiMessage m = new NoTiFiACK(10);
        m.setMsgId(-1);
    }
    
    /**
     * Test Encode ACK
     * 
     * @throws IOException - if I/O problems
     * @throws IllegalArgumentException - if data fails validation
     */
    @Test
    public void testEncodeACK() throws IOException, IllegalArgumentException {
        byte[] b = new byte [] {0x35,0x01};
        NoTiFiMessage m = new NoTiFiACK(1);
        assertEquals(1, m.getMsgId());
        assertEquals(5, m.getCode());
        byte[] encode = m.encode();
        assertEquals(b.length,encode.length);
        for(int i = 0; i < b.length; i++){
            assertEquals(b[i],encode[i]);
        }
      
    }
    /**
     * Test Encode Error message
     * 
     * @throws IllegalArgumentException - if data fails validation
     * @throws IOException - if I/O problems
     */
    @Test 
    public void testEncodeErr() throws IllegalArgumentException, IOException{
        byte[] b = new byte [] {0x34, 0x7b, 0x42, 0x61, 0x64,
                0x20, 0x65, 0x72, 0x72, 0x6f, 0x72 };
        NoTiFiMessage m = new NoTiFiError(123,"Bad error");
        assertEquals(123, m.getMsgId());
        assertEquals(4, m.getCode());
        byte[] encode = m.encode();
        assertEquals(b.length,encode.length);
        for(int i = 0; i < b.length; i++){
            assertEquals(b[i],encode[i]);
        }
    }
    /**
     * Test Encode Registration
     * 
     * @throws IllegalArgumentException - if data fails validation
     * @throws IOException - if I/O problems
     */
    @Test
    public void testEncodeReg() throws IllegalArgumentException, IOException {
        byte[] b = new byte []{ 0x30, 0x01, 0x01, 0x00, 0x00, 0x7F, (byte) 0x88, 0x13 };
        NoTiFiRegister m = new NoTiFiRegister(1,(Inet4Address)Inet4Address.getByName("127.0.0.1"),5000);
        assertEquals(1, m.getMsgId());
        assertEquals(0, m.getCode());
        assertEquals(Inet4Address.getByName("127.0.0.1"), m.getAddress());
        assertEquals(5000, m.getPort());
        byte[] encode = m.encode();
        assertEquals(b.length,encode.length);
        for(int i = 0; i < b.length; i++){
            assertEquals(b[i],encode[i]);
        }
    }
    /**
     * test Encode deRegistration
     * 
     * @throws IllegalArgumentException - if data fails validation
     * @throws IOException - if I/O problems
     */
    @Test
    public void testEncodeDeReg() throws IllegalArgumentException, IOException {
        byte[] b = new byte []{ 0x33, 0x01, 0x01, 0x00, 0x00, 0x7F, (byte) 0x88, 0x13 };
        NoTiFiDeregister m = new NoTiFiDeregister(1,(Inet4Address)Inet4Address.getByName("127.0.0.1"),5000);
        assertEquals(1, m.getMsgId());
        assertEquals(3, m.getCode());
        assertEquals(Inet4Address.getByName("127.0.0.1"), m.getAddress());
        assertEquals(5000, m.getPort());
        byte[] encode = m.encode();
        assertEquals(b.length,encode.length);
        for(int i = 0; i < b.length; i++){
            assertEquals(b[i],encode[i]);
        }
        
    }
    
    /**
     * test Encode add a location record
     * 
     * @throws IllegalArgumentException - if data fails validation
     * @throws IOException - if I/O problems
     */
    @Test
    public void testEncodeAdd() throws IllegalArgumentException, IOException {
        byte[] b = new byte [] {0x31, 0x00, 0x00, 0x4D, (byte) 0xB7, 0x7A, 0x4E, 0x7A, (byte) 0xDF, 
                (byte) 0x8C, 0x3F, 0x40, 0x71, (byte) 0xE6, 0x57, 0x73, (byte) 0x80, 0x47, 0x58, 
                (byte) 0xC0, 0x02, 0x42, 0x55, 0x06, 0x42, 0x41, 0x59, 0x4C, 0x4F, 0x52 };
        LocationRecord lr = new LocationRecord(77,31.550285,-97.117215,"BU","BAYLOR");
        NoTiFiLocationAddition m = new NoTiFiLocationAddition (0,lr);
        assertEquals(0, m.getMsgId());
        assertEquals(1, m.getCode());
        LocationRecord lr2 = m.getLocationRecord();
        assertEquals(77,lr2.getUserId());
        assertEquals(-97.117215,lr2.getLatitude(),0.0);
        assertEquals(31.550285,lr2.getLongitude(),0.0);
        assertEquals("BU",lr2.getLocationName());
        assertEquals("BAYLOR",lr2.getLocationDescription());
        byte[] encode = m.encode();
        assertEquals(b.length,encode.length);
        for(int i = 0; i < b.length; i++){
            assertEquals(b[i],encode[i]);
        }
    }
    /**
     * test Encode Delete location record 
     * 
     * @throws IllegalArgumentException - if data fails validation
     * @throws IOException - if I/O problems
     */
    @Test
    public void testEncodeDel() throws IllegalArgumentException, IOException {
        byte[] b = new byte [] {0x32, 0x00, 0x00, 0x4D, (byte) 0xB7, 0x7A, 0x4E, 0x7A, (byte) 0xDF, 
                (byte) 0x8C, 0x3F, 0x40, 0x71, (byte) 0xE6, 0x57, 0x73, (byte) 0x80, 0x47, 0x58, 
                (byte) 0xC0, 0x02, 0x42, 0x55, 0x06, 0x42, 0x41, 0x59, 0x4C, 0x4F, 0x52 };
        LocationRecord lr = new LocationRecord(77,31.550285,-97.117215,"BU","BAYLOR");
        NoTiFiLocationDeletion m = new NoTiFiLocationDeletion (0,lr);
        assertEquals(0, m.getMsgId());
        assertEquals(2, m.getCode());
        LocationRecord lr2 = m.getLocationRecord();
        assertEquals(77,lr2.getUserId());
        assertEquals(-97.117215,lr2.getLatitude(),0.0);
        assertEquals(31.550285,lr2.getLongitude(),0.0);
        assertEquals("BU",lr2.getLocationName());
        assertEquals("BAYLOR",lr2.getLocationDescription());
        byte[] encode = m.encode();
        assertEquals(b.length,encode.length);
        for(int i = 0; i < b.length; i++){
            assertEquals(b[i],encode[i]);
        }
    }

}
