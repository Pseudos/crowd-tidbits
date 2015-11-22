package server.rest.request;

public class BoxRequest {
    double trLat;
    double trLon;
    double blLat;
    double blLon;
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
     * @return the trLat
     */
    public double getTrLat() {
        return trLat;
    }
    /**
     * @param trLat
     *            the trLat to set
     */
    public void setTrLat(double trLat) {
        this.trLat = trLat;
    }
    
    /**
     * @return the trLon
     */
    public double getTrLon() {
        return trLon;
    }
    /**
     * @param trLon
     *            the trLon to set
     */
    public void setTrLon(double trLon) {
        this.trLon = trLon;
    }
    
    /**
     * @return the blLat
     */
    public double getBlLat() {
        return blLat;
    }
    /**
     * @param blLat
     *            the blLat to set
     */
    public void setBlLat(double blLat) {
        this.blLat = blLat;
    }
    
    /**
     * @return the blLon
     */
    public double getBlLon() {
        return blLon;
    }
    /**
     * @param blLon
     *            the blLon to set
     */
    public void setBlLon(double blLon) {
        this.blLon = blLon;
    }

}
