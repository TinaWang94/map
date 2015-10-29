package myn.notifi.protocol.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.Inet4Address;

import myn.notifi.protocol.NoTiFiMessage;
import myn.notifi.protocol.NoTiFiRegister;
import myn.notifi.protocol.IllegalArgumentException;

import org.junit.Test;

public class NoTiFiMessageTest {


    @Test
    public void testDecode() throws  IOException, IllegalArgumentException {
        NoTiFiRegister m = (NoTiFiRegister) NoTiFiMessage.decode(new byte[] { 0x30, 0x01, 0x01, 0x00, 0x00, 0x7F, (byte) 0x88, 0x13 });
        assertEquals(1, m.getMsgId());
        assertEquals(0, m.getCode());
        assertEquals(Inet4Address.getByName("127.0.0.1"), m.getAddress());
        assertEquals(5000, m.getPort());
      }

}
