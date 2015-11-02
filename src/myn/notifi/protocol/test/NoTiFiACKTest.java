/*
 * Classname : NoTiFiACKTest
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

import myn.notifi.protocol.NoTiFiACK;
import myn.notifi.protocol.NoTiFiMessage;

import org.junit.Test;
/**
 * Generate Junit test case
 * 
 * */
public class NoTiFiACKTest {
    /**
     * Test IllegalArgumen tException
     * @throws IOException - if I/O problems
     * @throws IllegalArgumentException - if data fails validation
     */
    @Test (expected = IllegalArgumentException.class)
    public void testACK() throws IOException, IllegalArgumentException {
        NoTiFiACK m = (NoTiFiACK) NoTiFiMessage.decode(new byte [] {0x35, (byte) 0xff});
        assertEquals(1, m.getMsgId());
        assertEquals(5, m.getCode());
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
}
