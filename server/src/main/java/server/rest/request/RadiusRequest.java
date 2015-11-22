package server.rest.request;

public class RadiusRequest {
    double distance;
    double lat;
    double lon;
    int priority;
    
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
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }
    /**
     * @param distance the distance to set
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }
    
    /**
     * @return the lat
     */
    public double getLat() {
        return lat;
    }
    /**
     * @param lat the lat to set
     */
    public void setLat(double lat) {
        this.lat = lat;
    }
    
    /**
     * @return the lon
     */
    public double getLon() {
        return lon;
    }
    /**
     * @param lon the lon to set
     */
    public void setLon(double lon) {
        this.lon = lon;
    }
}
