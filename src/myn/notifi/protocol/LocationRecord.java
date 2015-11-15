/*
 * Classname : LocationRecord
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
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/**
 * LocationRecord is a class to store location information.
 * may be used in location addition/deletion
 * Using DateInput/DataOutput
 * 
 * @version 1.0
 * @author Tong Wang
 * */

public class LocationRecord {
    /*two constant value setting error message for exception*/
    public static final String CHECKSTR = "^[-+]?[0-9]*\\.?[0-9]+$";
    private final String errorMsg = "Invalid id";
    private final String errorMsg2 = "Invalid length";
    private final String errorMsg3 = "Invalid String value";
    private final String errorMsg4 = "Invalid Double value";
    private final int range = 65535;
    private final int range2 = 255;
    /*user id, int type*/
    private int userId;
    /*longtitude, double*/
    private double longitude;
    /*laitude, double*/
    private double latitude;
    /*location name & description String*/
    private String locationName;
    private String locationDescription;
    

    /**
     * Check validation for id. Should be an unsigned integer
     * 
     * @param id - in to be checked
     * @throws IllegalArgumentException - if data value problem
     */
    private void checkId(int id,int num) throws IllegalArgumentException  {
        if(num == 2) {
            if(id < 0||id>range) {
                throw new IllegalArgumentException(errorMsg);
            }
        }
        if(num == 1) {
            if(id < 0||id>range2) {
                throw new IllegalArgumentException(errorMsg2);
            }
        }
    }
    
    /**
     * Check validation for string. Should not be null
     * @param str - a string to be checked
     * @throws IllegalArgumentException - if data value problem
     */
    private void checkString(String str) throws IllegalArgumentException {
        if(str == null) {
            throw new IllegalArgumentException(errorMsg3);
        }

    }
   private void checkDouble(String dou) throws IllegalArgumentException {
       if(dou ==null) {
           throw new IllegalArgumentException(errorMsg3);
       }
       if(!dou.matches(CHECKSTR)) {
           throw new IllegalArgumentException(errorMsg4);
       }
   }
   
   

    /**
     * Constructs location record using deserialization
     * 
     * @param in - deserialization input source
     * 
     * @throws IOException - if I/O problem
     * @throws IllegalArgumentException - if data value problem
     */
    public LocationRecord(DataInput in) throws IOException, IllegalArgumentException {
        
        int temp = in.readShort();
        if(temp < 0) 
            temp += ConstVal.range2;
        checkId(temp,2);
        userId = temp;
        longitude =  Parser.readDouble(in, ConstVal.numDou);
        latitude = Parser.readDouble(in, ConstVal.numDou);
        int length = Parser.readInt(in,1);
        checkId(length,1);
        locationName = Parser.readString(in, length);
        length = Parser.readInt(in, 1);
        checkId(length,1);
        locationDescription = Parser.readString(in, length);
    }
    
    /**
     * Constructs location record with set values
     * 
     * @param userId - ID for user
     * @param longitude - position of location
     * @param latitude - position of location
     * @param locationName - name of location
     * @param locationDescription - description of location
     * @throws IllegalArgumentException - if data value problem
     */
    public LocationRecord(int userId, double longitude, double latitude,
            String locationName, String locationDescription) throws IllegalArgumentException {
        checkId(userId,2);

        checkString(locationName);
        checkString(locationDescription);
        checkId(locationName.length(),1);
        checkId(locationDescription.length(),1);
        
        this.userId=userId;
        this.longitude=longitude;
        this.latitude=latitude;
        this.locationName=locationName;
        this.locationDescription=locationDescription;
        
    }
    /**
     * Constructs location record with set values
     * 
     * @param userId - ID for user
     * @param longitude - position of location
     * @param latitude - position of location
     * @param locationName - name of location
     * @param locationDescription - description of location
     * @throws IllegalArgumentException - if data value problem
     */
    public LocationRecord(int userId, String longitude, String latitude,String locationName,String locationDescription) {
        checkId(userId,2);
        checkDouble(longitude);
        checkDouble(latitude);
        checkString(locationName);
        checkString(locationDescription);
        this.userId=userId;
        this.locationDescription=locationDescription;
        this.locationName=locationName;
        this.latitude=Double.parseDouble(latitude);
        this.longitude=Double.parseDouble(longitude);
        
    }
    /**
     * Serializes location record
     * 
     * @param out - serialization output destination
     * @throws IOException - if I/O problem
     */
    public void encode(DataOutput out) throws IOException {
        out.writeShort((short)userId);
        Parser.writeDouble(out, longitude, ConstVal.numDou);
        Parser.writeDouble(out, latitude, ConstVal.numDou);
        byte temp = (byte)locationName.length();
        Parser.writeInt(out, temp, 1);
        Parser.writeString(out, locationName);
        temp=(byte)locationDescription.length();
        Parser.writeInt(out,temp,1);
        Parser.writeString(out, locationDescription);
        
    }
    
    /**
     * equals function
     * 
     * return true if equals; false if not
     * */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof LocationRecord)) {
            return false;
        }
        if(obj ==this) {
            return true;
        }
        return ((LocationRecord)obj).userId == userId 
                &&  ((LocationRecord)obj).latitude==latitude
                && ((LocationRecord)obj).longitude==longitude
                && ((LocationRecord)obj).locationName.compareTo(locationName) == 0
                && ((LocationRecord)obj).locationDescription.compareTo(locationDescription) == 0;
    }
    
    /**
     * Returns latitude
     * 
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }
    
    /**
     * Returns location description
     * 
     * @return location description
     */
    public String getLocationDescription() {
        return locationDescription;
    }
    
    /**
     * Returns location name
     * 
     * @return location name
     */
    public String getLocationName() {
        return locationName;
    }
    
    /**
     * Returns longitude
     * 
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }
    
    /**
     * Returns user ID
     * 
     * @return user ID
     */
    public int getUserId() {
        return userId;
    }
    
    /**
     * override hashCode in class java.lang.Object
     * 
     * @return hashcode
     * */
    @Override
    public int hashCode() {
        return Objects.hash(userId,latitude,longitude,
                locationName,locationDescription);
    }
    
    /**
     * Sets latitude
     * 
     * @param latitude - new latitude
     */
    public void setLatitude(double latitude) {
        this.latitude=latitude;
    }
    
    /**
     * Sets location description
     * 
     * @param locationDescription - new location description
     * @throws IllegalArgumentException - if data value problem
     */
    public void setLocationDescription(String locationDescription) throws IllegalArgumentException {       
        checkString(locationDescription);
        checkId(locationDescription.length(),1);
        this.locationDescription = locationDescription;
    }
    
    /**
     * Sets location name
     * 
     * @param locationName - new location name
     * @throws IllegalArgumentException - if data value problem
     */
    public void setLocationName(String locationName) throws IllegalArgumentException {
        checkString(locationName);
        checkId(locationName.length(),1);
        this.locationName = locationName;
    }
    
    /**
     * Sets longitude
     * 
     * @param longitude - New longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    /**
     * Sets user ID
     * 
     * @param userId - new user ID
     * @throws IllegalArgumentException - if data value problem
     */
    public void setUserId(int userId) throws IllegalArgumentException {
        checkId(userId,2);
        this.userId = userId;
    }
    
    /**
     * toString function
     * @return human readable result string
     * */
    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        
        result.append("User "+userId+":");
        result.append(locationName+" - ");
        result.append(locationDescription);
        result.append(" at (");
        result.append(longitude+", ");
        result.append(latitude+")\r\n");     
        return result.toString();
    } 
} 
