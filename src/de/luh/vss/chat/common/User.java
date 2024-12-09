package de.luh.vss.chat.common;

import java.net.SocketAddress;

/**
 * Represents a user in the chat system, including their unique identifier and endpoint address.
 */
public class User {

    private final UserId userId;
    private final SocketAddress endpoint;

    /**
     * Constructs a new User instance.
     * 
     * @param userId   the unique identifier of the user
     * @param endpoint the network endpoint address of the user
     */
    public User(UserId userId, SocketAddress endpoint) {
        this.userId = userId;
        this.endpoint = endpoint;
    }

    /**
     * Retrieves the unique identifier of the user.
     * 
     * @return the user's unique identifier
     */
    public UserId getUserId() {
        return userId;
    }

    /**
     * Retrieves the network endpoint address of the user.
     * 
     * @return the user's endpoint address
     */
    public SocketAddress getEndpoint() {
        return endpoint;
    }

    /**
     * Represents a unique identifier for a user.
     */
    public record UserId(int id) {
        /**
         * Represents a special broadcast user ID.
         */
        public static UserId BROADCAST = new UserId(0);

        /**
         * Constructs a UserId instance.
         * 
         * @param id the unique identifier value
         * @throws IllegalArgumentException if the ID is not in the range 0-9999
         */
        public UserId(int id) {
            if (id < 0 || id > 9999)
                throw new IllegalArgumentException("wrong user ID");
            this.id = id;
        }

    }

}
