package server.rest.request;

/**
 * 
 * @author Sydney
 *
 */

public class AuthenticateRequest {
    String authHash;
    String username;
    String timestamp;
    /**
     * @return the authHash
     */
    public String getAuthHash() {
        return authHash;
    }
    /**
     * @param authHash the authHash to set
     */
    public void setAuthHash(String authHash) {
        this.authHash = authHash;
    }
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * @return the timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }
    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    
}
