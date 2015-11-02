package server.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

@Entity(name="post")
@Audited
public class Post {
    Long id;
    String description;
    double latitude;
    double longitude;
    User poster;
    int priority;
    byte[] image;
    
    /**
     * @return the id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    
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
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH })
    @JoinColumn(name = "email")
    public User getPoster() {
        return poster;
    }
    /**
     * @param poster the poster to set
     */
    public void setPoster(User poster) {
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
     * @return the image
     */
    public byte[] getImage() {
        return image;
    }
    /**
     * @param image the image to set
     */
    public void setImage(byte[] image) {
        this.image = image;
    }
}
