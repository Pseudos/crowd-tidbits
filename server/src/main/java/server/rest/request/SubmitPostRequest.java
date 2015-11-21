package server.rest.request;

import server.entity.User;

public class SubmitPostRequest {
    String description;
    double latitude;
    double longitude;
    String poster;
    int priority;
    String hash;
    
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }
    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    /**
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }
    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    /**
     * @return the poster
     */
    public String getPoster() {
        return poster;
    }
    /**
     * @param poster the poster to set
     */
    public void setPoster(String poster) {
        this.poster = poster;
    }
    
    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }
    /**
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    /**
     * @return the hash
     */
    public String getHash() {
        return hash;
    }
    /**
     * @param hash the hash to set
     */
    public void setHash(String hash) {
        this.hash = hash;
    }
}
