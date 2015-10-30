/*
 * Classname : Parser
 *
 * Version information : 1.0
 *
 * Date : 10/29/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 */
package myn.notifi.protocol;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * This class provides basic functions
 * for converting big-Endian to little endian
 * 
 * Convert int, short, double from big endian to little endian
 * and the corresponding read/write functions
 * 
 * */

public class Parser {
    /**
     *  Byte swap a single short value.
     *  
     * @param num - number to byte swap
     * @return Byte swapped representation.
     */
    public static short swapShort(short num){
        
        int b1 = num & 0xff;
        int b2 = (num >> 8) & 0xff;

        return (short) (b1 << 8 | b2 << 0);
    }
    
    /**
     * Byte swap a single short value.
     * 
     * @param num - number to byte swap
     * @return Byte swapped representation.
     */
    public static int swapInt(int num ) {
        int b1 = (num >>  0) & 0xff;
        int b2 = (num >>  8) & 0xff;
        int b3 = (num >> 16) & 0xff;
        int b4 = (num >> 24) & 0xff;

        return b1 << 24 | b2 << 16 | b3 << 8 | b4 << 0;
    }
    
    /**
     * Byte swap a single long value. 
     * Used in double swapped
     * 
     * @param num - number to byte swap
     * @return Byte swapped representation.
     */
    public static long swapLong (long num) {
        long b1 = (num >>  0) & 0xff;
        long b2 = (num >>  8) & 0xff;
        long b3 = (num >> 16) & 0xff;
        long b4 = (num >> 24) & 0xff;
        long b5 = (num >> 32) & 0xff;
        long b6 = (num >> 40) & 0xff;
        long b7 = (num >> 48) & 0xff;
        long b8 = (num >> 56) & 0xff;

        return b1 << 56 | b2 << 48 | b3 << 40 | b4 << 32 |
               b5 << 24 | b6 << 16 | b7 <<  8 | b8 <<  0;
    }
    
    /**
     * Byte swap a single double value. 
     * 
     * @param num - number to byte swap
     * @return Byte swapped representation.
     */
    public static double swapDouble(double num) {
        
        long longValue = Double.doubleToLongBits (num);
        longValue = swapLong (longValue);
        return Double.longBitsToDouble (longValue);
    }
 
    /**
     * Return an int value from big endian to little endian
     * 
     * @param in - dataInput
     * @param num - the byte for the "integer"-- 1-byte 2-short 4-int
     * @return Byte swapped representation.
     * @throws IOException - io fails
     */
    public static int readInt(DataInput in,int num) throws IOException {
        if(num == 1)
            return in.readByte();
        if(num == 2) 
            return swapShort(in.readShort());      
        if(num == 4) 
            return swapInt(in.readInt());
        return -1;        
    }
    
    /**
     * Return a double value from big endian to little endian
     * 
     * @param in - dataInput
     * @param num - bytes for a double value 
     * @return Byte swapped representation.
     * @throws IOException - if I/O problem 
     */
    public static double readDouble(DataInput in,int num) throws IOException {
        return swapDouble(in.readDouble());
    }
    
    /**
     * Return a string from dataInput( For String, big/little endian does nor matter)
     * 
     * @param in - dataInput
     * @param num - length of the string to convert
     * @return - the result String
     * @throws IOException - io fails
     */
    public static String readString(DataInput in, int num ) throws IOException{
        StringBuffer buffer = new StringBuffer();
        for(int i=0 ; i<num ; i++) {
            buffer.append((char)in.readByte());
        }
        return buffer.toString();
    } 
    
    /**
     * Return a int value, store the version information
     * Need shift bit since version has only 4 bit
     * 
     * @param b - the byte which store version info in its highest 4 bit
     * 
     * @return version in int type
     */
    public static int readVer(byte b) {       
        return b>>4;
    }
    
    /**
     * Return a int value, store the code information
     * 
     * Need shift bit since code has only 4 bit
     * @param b - the byte which store version info in its lowest 4 bit
     * @return code in int type
     */
    public static int readCode(byte b) {
        return (b&0x0f);
    }
    
    /**
     * Read ip address from data input
     * 
     * @param in - dataInputStream
     * @return - address stored in a byte array
     * @throws IOException - I/O problem
     */
    public static byte[] readAddr(DataInputStream in) throws IOException {
        //address is 4 bytes int
        int len = 4;
        byte []b = new byte[len];
        
        for(int i = 0; i<len; i++) {
            b[len-1-i] = in.readByte(); 
        }
        return b;
    }
    
    /**
     * Return a string from dataInputStream
     * Used for getting error message since the length of the string is unknown.
     * 
     * @param in - dataInput
     * @return A string -error message
     * @throws IOException - I/O problem 
     */
    public static String readError(DataInputStream in) throws IOException {
        
        StringBuffer aBuf = new StringBuffer();
        while(in.available()>0) {

            aBuf.append((char)in.readByte());
        }       
        return aBuf.toString();
    }
    
    /**
     * Write a double in little endian to dataOutput
     * 
     * @param out - DataOutput
     * @param dou - the double to write out
     * @param num - 8 bytes for a double 
     * @throws IOException - if I/O problem 
     */
    public static void writeDouble(DataOutput out, double dou,int num) throws IOException {
        out.writeDouble(swapDouble(dou));      
    }
    
    //i is versin and j is code
    /**
     * encode version and code info to a single byte
     * 
     * @param i - version info
     * @param j - code info
     * @return - a byte contains version and code info
     */
    public static byte appendBit(int i, int j) {
        //15 in binary is 00001111
        int a = 15;
        int temp;
        temp=(a&i)<<4;

        temp= temp | (j & a);

        return (byte)temp;
        
    }
    
    /**
     * Convert integer to an byte array in little endian
     * 
     * @param i - the integer to be converted
     * @param num - bytes of the integer--1-byte 2-short 4-int
     * @return resule byte array
     */
    public static byte[] intToByte(int i, int num) {
                
        ByteBuffer bf = ByteBuffer.allocate(num);
        //short
        if(num == 1) {
            bf.put((byte) i);
        }
        if(num == 2) {
            bf.putShort(swapShort((short)i));
        }
        //int
        else if(num == 4) {
            bf.putInt(swapInt(i));
        }        
        return bf.array();
        
    }
    
    /**
     * Reverse order for a byte array
     * @param b - an byte array to be reversed
     * @return - result byte array
     */
    public static byte[] changeOrder (byte [] b) {
        
        byte [] a = new byte[b.length];
        for(int i=0;i<b.length;i++) {
            a[i] = b[b.length-1-i];
        }
        return a;
    }
    
    /**
     * Write out an integer to dataOutoput in little endian
     * 
     * @param out dataOutput
     * @param i - integer to write out
     * @param num - bytes info for the integer 1-byte 2-short 4-int
     * @throws IOException - if I/O problem
     */
    public static void writeInt(DataOutput out,int i,int num) throws IOException {

        if(num == 1) {
            out.writeByte(i);
        }
        else if(num == 2) {
            out.writeShort(swapShort((short)i));
        }
        else if(num == 4) {
            out.writeInt(swapInt(i));
        }
        
    }
    /**
     * Write out a string to dataOutput 
     * 
     * @param out - dataOutput
     * @param aString - the string to write out
     * @throws IOException - if I/O problem
     */
    public static void writeString(DataOutput out,String aString) throws IOException {
        out.write(aString.getBytes());
    }
    
}
