package myn.notifi.protocol;

import java.io.DataInputStream;
import java.io.IOException;

public class NoTiFiACK extends NoTiFiMessage{
    private final int code=ConstVal.ACK;
    
    public NoTiFiACK(DataInputStream in) throws IllegalArgumentException, IOException{
       
    }
    public NoTiFiACK(int msgId) throws IllegalArgumentException {
        super(msgId);
    }
    public NoTiFiACK(DataInputStream in,int msgId,int version) throws IllegalArgumentException {
        super(msgId);
        this.version=version;
    }
    
    @Override
    public int getCode() {
        return code;
    }
   
    @Override
    public String toString(){
        return null;
    }
    
    @Override
    public byte encodeHeader() {
        return Parser.appendBit(ConstVal.version, code);
    }
    
    @Override
    public byte[] encodeHelper() {
        //no payload
        return null;
    }
   
}
