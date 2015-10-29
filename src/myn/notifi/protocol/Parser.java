package myn.notifi.protocol;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Parser {
    public static short convertShort(short num){
        
        return (short)(((num & 0xff00) >> 4) | (num & 0x00ff));
    }

    public static ByteBuffer Order (ByteBuffer buffer){
        byte [] b = buffer.array();
        int len = b.length;
        for(int i = 0;i<len/2;i++) {
            byte temp = b[i];
            b[i]=b[len-1-i];
            b[len-1-i]=temp;
        }
        buffer.clear();
        buffer.put(b);
        return buffer;
    }
    //for int , the number is depends on different situation
    public static int readInt(DataInput in,int num) throws IOException {
        if(num == 1)
            return in.readByte();
        ByteBuffer buffer = ByteBuffer.allocate(num);
        System.out.println("-----");
        for(int i=0;i<num;i++) {
            byte temp = in.readByte();
            System.out.println(temp);
            buffer=buffer.put(temp);
        } 
        buffer=Order(buffer);
        //System.out.println(buffer.array()[0]);
        //System.out.println(buffer.array()[1]);
        if(num == 2) {
            return buffer.getShort();
        }
        return buffer.getInt();
    }
    
    //num for double is 8 bytes
    public static double readDouble(DataInput in,int num) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(num);
        for(int i=0;i<num;i++) {
            buffer=buffer.put(in.readByte());
        } 
        buffer=Order(buffer);
        return buffer.getDouble();
    }
    public static String readString(DataInput in, int num ) throws IOException{
        StringBuffer buffer = new StringBuffer();
        for(int i=0 ; i<num ; i++) {
            buffer.append((char)in.readByte());
        }
        return buffer.toString();
    } 
    
    public static int readVer(byte b) {       
        return b>>4;
    }
    public static int readCode(byte b) {
        return (b<<4)/10000;
    }
    
    public static byte[] readAddr(DataInputStream in) throws IOException, IllegalArgumentException {
        //address is 4 bytes int
        int num =4;
        ByteBuffer buffer = ByteBuffer.allocate(num);
        for(int i=0;i<num;i++) {
            buffer=buffer.put(in.readByte());
        } 
        buffer=Order(buffer);

        return buffer.array();
    }
    
    public static String readError(DataInputStream in) throws IOException {
        
        StringBuffer aBuf = new StringBuffer();
        while(in.available()>0) {
            aBuf.append(in.readChar());
        }
        
        return aBuf.toString();
    }
    
    public static void writeDouble(DataOutput out, double dou,int num) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(num);
        buffer.putDouble(dou);
        buffer=Order(buffer);
        out.write(buffer.array());       
    }
    
    //i is versin and j is code
    public static byte appendBit(int i, int j) {
        //15 in binary is 00001111
        int a = 15;
        int temp;
        temp=(a&i)<<4;

        temp= temp | (j & a);
        ///////////////////////////////////////////////
        //im not sure
        return (byte)temp;
        
    }
    
    public static byte[] intToByte(int i, int num) {
        ByteBuffer bf = ByteBuffer.allocate(num);
        if(num == 2) {
            bf.putShort((short)i);
        }
        if(num == 4) {
            bf.putInt(i);
        }
        bf=Order(bf);
        return bf.array();
    }
    
    public static byte[] changeOrder (byte [] b) {
        ByteBuffer bf=ByteBuffer.allocate(b.length);
        bf=Order(bf);
        return bf.array();
    }
    
    public static void writeInt(DataOutput out,int i,int num) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(num);
        if(num == 1) {
            buffer.put((byte)i);
        }
        else if(num == 2) {
            buffer.putShort((short)i);
        }
        else if(num == 4) {
            buffer.putInt(i);
        }
        
        buffer=Order(buffer);
        out.write(buffer.array());
    }
    public static void writeString(DataOutput out,String aString) throws IOException {
        out.write(aString.getBytes());
    }
    
}
