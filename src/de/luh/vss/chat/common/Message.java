package de.luh.vss.chat.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;

import de.luh.vss.chat.common.User.UserId;

/**
 * Abstract class representing a generic Message structure for the chat system.
 */
public abstract class Message {


    /**
     * Represents a registration request message.
     */
    public static class RegisterRequest extends Message {

        private final UserId id;
        private final InetAddress address;
        private final int port;

        /**
         * Constructs a new RegisterRequest message.
         * 
         * @param id      the unique user ID
         * @param address the IP address of the user
         * @param port    the UDP port of the user
         */
        public RegisterRequest(final UserId id, final InetAddress address, final int port) {
            this.id = id;
            this.address = address;
            this.port = port;
        }
        /**
         * Constructs a RegisterRequest message from a data input stream.
         * 
         * @param in the input stream containing serialized message data
         * @throws IOException if an I/O error occurs
         */
        public RegisterRequest(final DataInputStream in ) throws IOException {
            this.id = new UserId( in .readInt());
            this.address = InetAddress.getByName( in .readUTF());
            this.port = in .readInt();
        }

        @Override
        public MessageType getMessageType() {
            return MessageType.REGISTER_REQUEST;
        }

        @Override
        public void toStream(final DataOutputStream out) throws IOException {
            out.writeInt(MessageType.REGISTER_REQUEST.msgType());
            out.writeInt(id.id());
            out.writeUTF(address.getCanonicalHostName());
            out.writeInt(port);
        }

        /**
         * Returns the user ID.
         * 
         * @return the user ID
         */
        public UserId getUserId() {
            return id;
        }

        /**
         * Returns the user's IP address.
         * 
         * @return the IP address
         */
        public InetAddress getUDPAddress() {
            return address;
        }


        /**
         * Returns the user's UDP port.
         * 
         * @return the UDP port
         */
        public int getUDPPort() {
            return port;
        }

        @Override
        public String toString() {
            return "REGISTER_REQUEST (" + id + ", " + address.getCanonicalHostName() + ":" + port + ")";
        }

    }
    /**
     * Represents a response to a registration request.
     */
    public static class RegisterResponse extends Message {
        /**
         * Constructs an empty RegisterResponse message.
         */
        public RegisterResponse() {

        }

        /**
         * Constructs a RegisterResponse message from a data input stream.
         * 
         * @param in the input stream containing serialized message data
         */
        public RegisterResponse(final DataInputStream in ) {

        }

        @Override
        public MessageType getMessageType() {
            return MessageType.REGISTER_RESPONSE;
        }

        @Override
        public void toStream(final DataOutputStream out) throws IOException {
            out.writeInt(MessageType.REGISTER_RESPONSE.msgType());
        }

        @Override
        public String toString() {
            return "REGISTER_RESPONSE ()";
        }

    }

    /**
     * Represents an error response message.
     */
    public static class ErrorResponse extends Message {

        private final String errorMsg;

        /**
         * Constructs an ErrorResponse message from an exception.
         * 
         * @param e the exception that caused the error
         */
        public ErrorResponse(final Exception e) {
            this.errorMsg = e.getMessage();
        }

        /**
         * Constructs an ErrorResponse message from a data input stream.
         * 
         * @param in the input stream containing serialized message data
         * @throws IOException if an I/O error occurs
         */
        public ErrorResponse(final DataInputStream in ) throws IOException {
            errorMsg = in .readUTF();
        }

        /**
         * Constructs an ErrorResponse message from a string.
         * 
         * @param e the error message string
         */
        public ErrorResponse(final String e) {
            this.errorMsg = e;
        }

        @Override
        public void toStream(final DataOutputStream out) throws IOException {
            out.writeInt(MessageType.ERROR_RESPONSE.msgType());
            out.writeUTF(errorMsg);
        }

        @Override
        public MessageType getMessageType() {
            return MessageType.ERROR_RESPONSE;
        }

        @Override
        public String toString() {
            return "ERROR_RESPONSE (" + errorMsg + ")";
        }

    }

    /**
     * Represents a chat message.
     */
    public static class ChatMessage extends Message {

        private final UserId recipient;
        private final String msg;
        /**
         * Constructs a ChatMessage.
         * 
         * @param recipient the recipient's user ID
         * @param msg       the message content
         */
        public ChatMessage(final UserId recipient, final String msg) {
            this.recipient = recipient;
            this.msg = msg;
        }
        /**
         * Constructs a ChatMessage from a data input stream.
         * 
         * @param in the input stream containing serialized message data
         * @throws IOException if an I/O error occurs
         */
        public ChatMessage(final DataInputStream in ) throws IOException {
            this.recipient = new UserId( in .readInt());
            this.msg = in .readUTF();
        }

        @Override
        public void toStream(final DataOutputStream out) throws IOException {
            out.writeInt(MessageType.CHAT_MESSAGE.msgType());
            out.writeInt(recipient.id());
            out.writeUTF(msg);
        }

        @Override
        public MessageType getMessageType() {
            return MessageType.CHAT_MESSAGE;
        }

        /**
         * Returns the recipient's user ID.
         * 
         * @return the recipient's user ID
         */
        public UserId getRecipient() {
            return recipient;
        }

        /**
         * Returns the message content.
         * 
         * @return the message content
         */
        public String getMessage() {
            return msg;
        }

        @Override
        public String toString() {
            return "CHAT_MESSAGE (to " + recipient + ": '" + msg + "')";
        }
    }
    /**
     * Parses a message from a data input stream.
     * 
     * @param in the input stream containing serialized message data
     * @return the parsed Message instance
     * @throws IOException                  if an I/O error occurs
     * @throws ReflectiveOperationException if an error occurs during reflection
     */
    public static Message parse(final DataInputStream in ) throws IOException, ReflectiveOperationException {
        return MessageType.fromInt( in .readInt(), in );
    }
    /**
     * Serializes the message to a data output stream.
     * 
     * @param out the output stream to write to
     * @throws IOException if an I/O error occurs
     */
    public abstract void toStream(final DataOutputStream out) throws IOException;

    /**
     * Returns the type of the message.
     * 
     * @return the MessageType
     */
    public abstract MessageType getMessageType();
}
