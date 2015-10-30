/*
 * Classname : ConstVal
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

/**
 * This class initial several useful constant values
 * 
 * */
public class ConstVal {
    /*length of a byte*/
    public static final int numLen = 1;
    /*length of a double*/
    public static final int numDou = 8;
    /*code for register*/
    public static final int reg = 0;
    /*code for addition*/
    public static final int add = 1;
    /*code for deletion*/
    public static final int del = 2;
    /*code for deRegister*/
    public static final int deReg = 3;
    /*code for error*/
    public static final int error = 4;
    /*code for ACK*/
    public static final int ACK = 5;
    /*version message for this assignment*/
    public static final int version = 3;
    /*regex for unsigned integer*/
    public static final String CHECKID = "0|[1-9][0-9]*";
}
