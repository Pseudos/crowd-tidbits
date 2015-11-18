package server.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
    Date postTime;
    
    @PrePersist
    protected void onCreate() {
      postTime = new Date();
    }
    
    /**
     * @return the postTime
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Date getPostTime() {
        return postTime;
    }
    /**
     * @param postTime the postTime to set
     */
    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }
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
}
