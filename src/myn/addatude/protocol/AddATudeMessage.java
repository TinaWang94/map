/*
 * Classname : AddATudeMessage
 *
 * Version information : 1.0
 *
 * Date : 9/13/2015
 *
 * Copyright notice
 * 
 * Author : Tong Wang
 * 
 * Assignment : program1
 */
package myn.addatude.protocol;

import java.io.IOException;

/**
 * Represents generic portion of AddATude message
 * @author tong wang
 * 
 * */
public abstract class AddATudeMessage {
    /* initialize some constant strings for check purpose */
    public static final String ERRORMSG = "Invalid Id.";
    public static final String ERRORMSG2 = "Invalid operation.";
    public static final String InvalidHeader = "Invalid header.";
    /* mapId for message */
    protected int mapId;
    protected static String version;

    /**
     * check valiation for mapId
     * 
     * @param mapId
     *            - a mapId waits for check
     * @throws AddATudeException
     *             - validation failure
     * */
    protected void checkMapId(int mapId) throws AddATudeException {
        if (mapId < 0) {
            throw new AddATudeException(ERRORMSG);
        }

    }

    /**
     * check if the message is short
     * 
     * @param out
     *            - message output to write out
     * @param aString
     *            - message where stores in a string
     * @throws AddATudeException
     *             - validation failure
     * */
    protected void checkShortMsg(MessageOutput out, String aString)
            throws AddATudeException {
        try {
            out.write(aString.getBytes("UTF-8"));
        } catch (IOException e) {

            throw new AddATudeException("serialization output fails");
        }
    }

    /**
     * Deserializes message from byte source
     * 
     * @param in
     *            - deserialization input source
     * @return a - a specific AddATude message resulting from deserialization
     * @throws AddATudeException
     *             - if deserialization or validation failure
     * @throws IOException
     *             - IOException
     */
    static public AddATudeMessage decode(MessageInput in)
            throws AddATudeException, IOException {
        String aString = null;

        aString = in.readToSpace();
        version = aString;
        //System.out.println("header =" + aString);
        if (!ConstantVariable.HEADER.equals(aString)) {
            throw new AddATudeException(InvalidHeader);
        }

        aString = in.readToSpace();
        if (!aString.matches(ConstantVariable.CHECKID)) {
            throw new AddATudeException(ERRORMSG);
        }

        if (Integer.valueOf(aString) < 0) {
            throw new AddATudeException(ERRORMSG);
        }
        int id = Integer.valueOf(aString);
        String operation = in.readOperation();
        AddATudeMessage a;
        switch (operation) {
        case ConstantVariable.NEW:
            a = new AddATudeNewLocation(in, id);
            break;
        case ConstantVariable.ERROR:
            a = new AddATudeError(in, id);
            break;
        case ConstantVariable.RESPONSE:
            a = new AddATudeLocationResponse(in, id);
            break;
        case ConstantVariable.ALL:
            a = new AddATudeLocationRequest(in, id);
            break;
        default:
            throw new AddATudeException(ERRORMSG2);
        }

        in.readToEOLN();

        return a;
    }

    /**
     * Returns map ID
     * 
     * @return mapId - map ID of message
     */

    public int getMapId() {
        return mapId;
    }

    public String getVersion() {
        return version;
    }

    /**
     * Sets map ID
     * 
     * @param mapId
     *            - new map ID
     * @throws AddATudeException
     *             - if validation fails
     */
    public void setMapId(int mapId) throws AddATudeException {
        checkMapId(mapId);
        this.mapId = mapId;
    }

    /**
     * an empty constructor used by test
     */
    public AddATudeMessage() {

    }

    /**
     * abstract function used for Serializes message
     * 
     * @param out
     *            - serialization output destination
     * @throws AddATudeException
     *             - if serialization output fails
     */
    public void encode(MessageOutput out) throws AddATudeException {
        StringBuffer aBuf = new StringBuffer();
        aBuf.append(ConstantVariable.HEADER + " ");
        aBuf.append(mapId + " ");
        String aString = new String(aBuf);
        checkShortMsg(out, aString);

        encodeHelp(out);

        aString = ConstantVariable.EOLN;
        checkShortMsg(out, aString);
    }

    /**
     * abstract function declair toString function may be overrided by children
     * classes
     * 
     * @param out - messageOutput Stream
     * @throws AddATudeException
     *             - if serialization output fails
     * */

    abstract public void encodeHelp(MessageOutput out) throws AddATudeException;
    
    /**
     * toString function
     * 
     * @return human readable string output
     * 
     * */
    public String toString() {
        return null;

    }

    /**
     * abstract getOperation function may extend by children classes
     * 
     * @return operation- different from every children classes
     * 
     * */
    abstract public String getOperation();

    /**
     * override hashCode in class java.lang.Object
     * 
     * */
}