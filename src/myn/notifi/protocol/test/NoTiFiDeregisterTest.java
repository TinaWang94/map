/*
 * Classname : NoTiFiDeregisterTest
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

import myn.notifi.protocol.IllegalArgumentException;
import myn.notifi.protocol.NoTiFiDeregister;
import myn.notifi.protocol.NoTiFiMessage;

import org.junit.Test;
/**
 * Generate Junit test case
 * 
 * */
public class NoTiFiDeregisterTest {
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

}
