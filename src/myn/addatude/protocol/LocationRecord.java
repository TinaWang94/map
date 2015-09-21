/*
 * Classname : LocationRecord
 *
 * Version information : 1.0
 *
 * Date : 9/7/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 */
package myn.addatude.protocol;

import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Objects;
import java.util.Scanner;

/**
 * LocationRecord is a class to store location information.
 * It takes MessageInput and MessageOutput to decode and encode
 * by using specific protocol specificaions. 
 * MessageInput and MessageOutput are mediators for
 * inputstream and outputstream,
 * 
 * @version 1.0
 * @author Tong Wang
 * */

public class LocationRecord {
    public static final String CHECK = "^[-+]?[0-9]*\\.?[0-9]+$";
    public static final String CHECK2 = "0|[1-9][0-9]*";
    /*user id, int type*/
    private int userId;
    /*longtitude, string type*/
    private String longitude;
    /*latitude, string type*/
    private String latitude;
    /*locationName, string type*/
    private String locationName;
    /*locationDescription, string type*/
    private String locationDescription;

    
    /**
     * Constructs location record with set values
     * 
     * @param userId - ID for user
     * @param longitude - position of location
     * @param latitude - position of location
     * @param locationName - name of location
     * @param locationDescription - description of location
     * @throws AddATudeException -validation failure
     *          
     * */
    
    public LocationRecord(int userId, String longitude, String latitude,
            String locationName, String locationDescription) throws AddATudeException  {
        if(userId < 0) {
            throw new AddATudeException("userId should be a non-nagative number.");
        }
        if(longitude == null) {
            throw new AddATudeException("Longitude shouldn't be null.");
        }
        if(latitude == null) {
            throw new AddATudeException("Latitude shouldn't be null.");
        }
        if(!longitude.matches(CHECK)) {
            throw new AddATudeException("Longitude doesn't match the given format.");
        }
        if(!latitude.matches(CHECK)) {
            throw new AddATudeException("Latitude doesn't match the given format.");
        }
        if(locationName == null) {
            throw new AddATudeException("Location name shouldn't be null.");
        }
        if(locationDescription == null) {
            throw new AddATudeException("Location Description shouldn't be null.");
        }
        this.userId = userId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.locationName = locationName;
        this.locationDescription = locationDescription;
        
    }
    
    /**
     * Constructs location record using deserialization
     * 
     * @param in - deserialization input source
     * 
     * @throws AddATudeException - if deserialization or validation failure
     * @throws EOFException - if premature end of stream
     * 
     * */
    
    public LocationRecord(MessageInput in) throws AddATudeException,EOFException{    
        /*count is the record for different situations*/
        int count = 1;
        /*used for read in location name and description*/
        int length = 0;
        
        String aString = null;
        //get id,longitude, latitude and length of name
        for(int i = 0; i<4 ; i++) {
            aString = readToSpace(in);
            length = parse(count++,aString,length);
        }
        //get location name
        aString = readByNum(length,in);
        length=parse(count++,aString,length);
        //get length of location description
        aString = readToSpace(in);
        length = parse(count++,aString,length);
        //get location description
        aString = readByNum(length,in);
        length=parse(count,aString,length);
        
        if(count < 7) {
            throw new EOFException("This location record needs more information.");
        }
            

    }
    /**
     * Constructs read a MessageInput till hit a space
     * 
     * @param in -input source
     * 
     * @return aString-the result string after decoding
     * 
     * @throws EOFException - if premature end of stream
     * 
     * */
    private String readToSpace (MessageInput in) throws EOFException  {
        int bt = 0;
        StringBuffer aBuf = new StringBuffer();
        String aString = null;
    
        try {
            while((bt=in.read()) != -1) {
                //32 is ASCII num of a single space                   
                if(bt == 32 ) {
                    aString=aBuf.toString();     
                    break;
                }
                else{      
                    //put the byte just read in buffer
                    aBuf.append((char)bt);
                }  
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bt == -1) {
            throw new EOFException("This location record needs more information.");
        }
            
        return aString;
    }
    /**
     * Constructs read a MessageInput by byte
     * 
     * @param in -input source
     *        num- number of bytes we should read
     * 
     * @return aString-the result string after decoding
     * 
     * @throws AddATudeException - if deserialization or validation failure
     *         java.io.EOFException - if premature end of stream
     * 
     * */
    private String readByNum (int num, MessageInput in) throws EOFException  {
        int bt = 0;
        StringBuffer aBuf = new StringBuffer();
        String aString = null;
    
        for(int i=0;i<num;i++) {
            try {
                if((bt=in.read()) != -1 ) {
                    aBuf.append((char)bt);
                }
                else {
                    throw new EOFException("This location record needs more information.");
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                if(e instanceof EOFException) {
                    throw new EOFException("This location record needs more information.");
                }
                e.printStackTrace();
            }
        }
            aString=aBuf.toString();  
            return aString;
    }
    
    /**
     * This function using 6 situations to decode the messafe information
     * 
     * @param count - record which situation should we deal with
     *        aString - the string to set proper value to related varables 
     *        length - used only in two cases. Used as the character count
     * @return length when needed, otherwise return a unrelated integer value
     * @throws AddATudeException - if validation fails
     * 
     * */
    
    private int parse (int count, String aString, int length) throws AddATudeException{
        int res = -1;
        switch(count) {
            /*get userId in the first consition*/
            case 1: 
                
                if(!aString.matches(CHECK2)) {
                    throw new AddATudeException("userId doesn't match the given format.");
                }
                if(Integer.valueOf(aString) < 0) {
                    throw new AddATudeException("userId should be a non-nagative number.");
                }
                userId = Integer.valueOf(aString);
                
                break;
            /*get longitude in second situation*/
            case 2:
                if(!aString.matches(CHECK)) {
                    throw new AddATudeException("Longitude doesn't match the given format.");
                }
                longitude = aString;
                
                break;
            /*get latutude in third situation*/
            case 3:
                if(!aString.matches(CHECK)) {
                    throw new AddATudeException("Latitude doesn't match the given format.");
                }
                latitude = aString;
                
                break;
            /*get the character count for laocation name by returing its value
              it should be used in next situation*/
            case 4:
                if(!aString.matches(CHECK2)) {
                    throw new AddATudeException("Length of longitude doesn't match the given format.");
                }
                res = Integer.valueOf(aString);
                break;
            /*get location name */
            case 5:

                locationName = aString;
                break;
            /*and the character count for the location description
             the count is returned and should be used in next situation*/
            case 6:
                if(!aString.matches(CHECK2)) {
                    throw new AddATudeException("Length of latitude doesn't match the given format.");
                }
                res = Integer.valueOf(aString);
                break;
            /*get location description*/
            case 7:
                if(length != aString.length()) {
                    throw new AddATudeException("Character count doesn't match the description.");
                }
                locationDescription = aString;
                
                    
                break;
            /*default situation, return a irrelative number*/
            default:
                throw new AddATudeException("The format of this location record is wrong.");
        }
        return res;
    }
    
    /**
     * Constructs location record using user input
     * @param in - user input source
     * @param out - user output (prompt) destination
     * @throws   AddATudeException - if validation fails
     * 
     * */
    
    public LocationRecord(Scanner in, PrintStream out) throws AddATudeException{
        
    }
    
    /**
     * Serializes location record
     * 
     * @param out - serialization output destination
     * @throws AddATudeException - if serialization output fails
     * 
     * */
    
    public void encode(MessageOutput out) throws AddATudeException {
        StringBuffer aBuf = new StringBuffer();
        aBuf.append(userId);
        aBuf.append(" "+longitude);
        aBuf.append(" "+latitude+" ");
        aBuf.append(locationName.length());
        aBuf.append(" "+locationName);
        aBuf.append(locationDescription.length());
        aBuf.append(" "+locationDescription); 
        String aString = new String(aBuf);
        try {
            out.write(aString.getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new AddATudeException("serialization output fails");
        }
    }
    
    /**
     * override equals in class java.lang.Object
     * 
     * @param obj - an object
     * @return true if matchs, false otherwise
     * 
     * */
    
    @Override
    public boolean equals(Object obj) {
        
        if( !(obj instanceof LocationRecord) ) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        return Objects.equals(((LocationRecord)obj).getUserId(), getUserId())
                && Objects.equals(((LocationRecord)obj).getLatitude(), getLatitude())
                && Objects.equals(((LocationRecord)obj).getLongitude(),getLongitude())
                && Objects.equals(((LocationRecord)obj).getLocationName(), getLocationName())
                && Objects.equals(((LocationRecord)obj).getLocationDescription()
                        , getLocationDescription());
    }
    
    /**
     * This function returns latitude
     * @return latitude
     * 
     * */
    
    public String getLatitude() {
        return latitude;
    }
    
    /**
     * return LocationDescription
     * @return LocationDescription
     * 
     * */
    
    public String getLocationDescription() {
        return locationDescription;
    }
    
    /**
     * return locationName
     * 
     * @return locationName
     * 
     * */
    
    public String getLocationName() {
        return locationName;
    }
    
    /**
     * return longitude
     * 
     * @return longitude
     * 
     * */
    
    public String getLongitude() {
        return longitude;
    }
    
    /**
     * return user Id
     * @return userId
     * */
    
    public int getUserId() {
        return userId;
    }
    
    /**
     * override hashCode in class java.lang.Object
     * 
     * */
    
    @Override
    public int hashCode() {
        return Objects.hash(userId,latitude,longitude
                ,locationName, locationDescription);
    } 
    
    
    
    /**
     * set Latitude
     * @param latitude - latitude 
     * @throws AddATudeException - invalid
     */
    public void setLatitude(String latitude) throws AddATudeException {
        if(latitude == null) {
            throw new AddATudeException("Latitude shouldn't be null.");
        }
        this.latitude =latitude; 
        if(!latitude.matches(CHECK)) {
            throw new AddATudeException("Latitude doesn't match the given format.");
        }
    }
    
    /**
     * set location description
     * @param locationDescription - new location description
     *  
     * @throws AddATudeException 
     *  - if validation fails
     * */
    
    public void setLocationDescription(String locationDescription) throws AddATudeException {
        
        if(locationDescription == null) {
            throw new AddATudeException("Location description shouldn't be null.");
        }this.locationDescription = locationDescription;
    }
    
    /**
     * set location name
     * @param locationName - new location name
     * @throws AddATudeException  -if validation fails
     * 
     * */
    
    public void setLocationName(String locationName) throws AddATudeException {
        if(locationName == null) {
            throw new AddATudeException("Location name shouldn't be null.");
        }
        this.locationName = locationName;
    }
    
    /**
     * set Longitude
     * @param longitude - new longitude
     * @throws  AddATudeException if vailation fails
     * 
     * */
    
    public void setLongitude(String longitude) throws AddATudeException{
        if(longitude == null) {
            throw new AddATudeException("Longitude shouldn't be null.");
        }
        if(!longitude.matches(CHECK)) {
            throw new AddATudeException("Longitude doesn't match the given format.");
        }
        this.longitude = longitude;
        
    }
    
    /**
     * set location userid
     * @param userId - new location userId
     * @throws AddATudeException - if validation fails
     * */
    
    public void setUserId(int userId) throws AddATudeException{
        if(userId < 0 ) {
            throw new AddATudeException("Invalid userId.");
        }
        this.userId = userId;
        
    }
    
    /**
     * Override toString in class java.lang.Object
     * The toString() should print a reasonable,
     * human-readable representation of the LocationRecord attributes,
     * including longitude, etc.
     * 
     * @return a human readable string indicates the result
     * */
    
    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        
        result.append("User "+userId+":");
        result.append("location name="+locationDescription+".");
        result.append("longitude="+longitude+", ");
        result.append("latitude="+latitude+", ");
        result.append("location="+locationName+", ");
        
        return result.toString();
    }
    
}