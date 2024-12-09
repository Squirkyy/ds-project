package de.luh.vss.chat.common;

import java.io.DataInputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum representing the different types of messages that can be exchanged in the chat system.
 */
public enum MessageType {
        /**
         * Error response message type.
         */
    	ERROR_RESPONSE(0, Message.ErrorResponse.class),

        /**
         * Register request message type.
         */
	REGISTER_REQUEST(1, Message.RegisterRequest.class),

        /**
         * Register response message type.
         */
        REGISTER_RESPONSE(2, Message.RegisterResponse.class),

        /**
         * Chat message type.
         */
        CHAT_MESSAGE(4, Message.ChatMessage.class);

    private final int msgType;

    private Constructor << ? extends Message > constr;

    /**
     * A lookup table to map message type integers to their corresponding MessageType instances.
     */
    private static final Map < Integer, MessageType > lookup = new HashMap < Integer, MessageType > ();
    static {
        for (final MessageType mt: MessageType.values()) {
            lookup.put(mt.msgType, mt);
        }
    }

    /**
     * Constructs a MessageType instance.
     * 
     * @param msgType the integer representation of the message type
     * @param cls     the class corresponding to this message type
     */
    private MessageType(int msgType, final Class << ? extends Message > cls) {
        this.msgType = msgType;
        try {
            this.constr = cls.getConstructor(DataInputStream.class);
        } catch (NoSuchMethodException | SecurityException e) {
            System.err.println("Error while registering message type. " +
                "Constructor from DataInputStream not present or accessible");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Returns the integer representation of the message type.
     * 
     * @return the integer value of the message type
     */
    int msgType() {
        return msgType;
    }

    /**
     * Retrieves the MessageType instance corresponding to a given integer value.
     * 
     * @param val the integer value representing a message type
     * @return the corresponding MessageType instance, or null if no match is found
     */
    public static MessageType fromInt(final int val) {
        return lookup.get(val);
    }

    /**
     * Parses a message from its integer type and input stream.
     * 
     * @param val the integer representation of the message type
     * @param in  the input stream containing serialized message data
     * @return the parsed Message instance
     * @throws ReflectiveOperationException if an error occurs during message instantiation
     */
    public static Message fromInt(final int val, final DataInputStream in ) throws ReflectiveOperationException {
        final MessageType mt = fromInt(val);
        if (mt == null) {
            throw new IllegalStateException("Unknown message type " + val);
        }
        return mt.constr.newInstance( in );
    }

}
