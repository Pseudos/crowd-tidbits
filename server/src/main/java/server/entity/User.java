package server.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.envers.Audited;

/**
 * The user entity
 * 
 * @author Sydney
 * 
 */
@Entity(name = "user")
@Audited
public class User {
    private String username;
    private String email;
    private String password;
    private boolean enabled;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Id
    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getEnabled() {
        return enabled;
    }
}
