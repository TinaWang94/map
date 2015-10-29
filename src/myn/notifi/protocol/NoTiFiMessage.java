package myn.notifi.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class NoTiFiMessage {

    private final static String errorMsg = "Invalid id";
    private  static final String errorMsg2 = "Invalid code";
    protected int version;    
    protected int msgId;
    
    protected static void checkId(int id) throws IllegalArgumentException  {
        if(id < 0) {
            throw new IllegalArgumentException(errorMsg);
        }
    }
    
    
    public NoTiFiMessage(DataInputStream in) throws IOException, IllegalArgumentException {
        
        byte b = in.readByte();
        checkId(b);
        int temp = Parser.readVer(b);
        checkId(temp);
        version=temp;
        temp=Parser.readInt(in, 1);
        checkId(temp);
        msgId=temp;
    }
    
    public NoTiFiMessage(int msgId) throws IllegalArgumentException {
        checkId(msgId);
        this.msgId=msgId;
    }
    public NoTiFiMessage() throws IllegalArgumentException, IOException {
        
    }
    static public NoTiFiMessage decode (byte[] pkt) throws IOException, IllegalArgumentException {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(pkt));
        
        byte b = in.readByte();
        checkId(b);
        int temp = Parser.readVer(b);
        checkId(temp);
        int version=temp;
        
        temp=Parser.readCode(b);
        checkId(temp);
        int code = temp;
        
        temp=Parser.readInt(in, 1);
        checkId(temp);
        int msgId=temp;
        
        
        
        NoTiFiMessage msg=null;
        switch(code) {
        case ConstVal.reg:
            msg=new NoTiFiRegister(in,msgId,version);
            break;
        case ConstVal.add:
            msg=new NoTiFiLocationAddition(in,msgId,version);
            break;
        case ConstVal.del:
            msg=new NoTiFiLocationDeletion(in,msgId,version);
            break;
        case ConstVal.deReg:
            msg=new NoTiFiDeregister(in,msgId,version);
            break;
        case ConstVal.error:
            msg=new NoTiFiError(in,msgId,version);
            break;
        case ConstVal.ACK:
            msg=new NoTiFiACK(in,msgId,version);
            break;
        default:
            throw new IllegalArgumentException(errorMsg2);
        }
        
        return msg;
    } 
    
    public byte[] encode() throws IOException {
        
        ByteArrayOutputStream out = new ByteArrayOutputStream( );
        
        ByteBuffer bf=ByteBuffer.allocate(2).put(encodeHeader());
        /////////////////////////////////////////////////////////////
        //cast may cause problem
        bf=bf.putShort((short)msgId);
        
        out.write(bf.array());
        out.write(encodeHelper());
        
        
        return out.toByteArray();
    }
    
    abstract public byte encodeHeader();
    abstract public byte[] encodeHelper() throws IOException;
    
    public abstract int getCode();

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) throws IllegalArgumentException {
        checkId(msgId);
        this.msgId=msgId;
    }

    public String toString() {
        return null;
    } 
    
}
