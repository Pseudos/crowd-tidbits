package server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import server.dao.UserDao;
import server.entity.User;
import server.rest.request.AuthenticateRequest;
import server.rest.response.DefaultResponse;
import server.util.HMAC;

/**
 * 
 * @author Sydney
 *
 */

public class UserService {
    @Autowired
    UserDao userDao;
    
    protected Logger getLog() {
        return LoggerFactory.getLogger(getClass());
    }
    
    public DefaultResponse authenticateUser(AuthenticateRequest request)
    {
        System.out.println("Authenticating user");
        DefaultResponse response = new DefaultResponse();
        response.setSource("Crowd_tidbits");
        
        response.setStatus(401);
        response.setMessage("Not authorized");
        
        String username = request.getUsername();
        String requestHash = request.getAuthHash();
        String timestamp = request.getTimestamp();
        
        //CHeck that all parameters are included
        //Could be done in one if statement, but readability is improved
        if (username == null || username.isEmpty()) {
            System.out.println("Returning: " + response.getStatus());
            return response;
        }
        if (requestHash == null || requestHash.isEmpty()) {
            System.out.println("Returning: " + response.getStatus());
            return response;
        }
        if (timestamp == null || timestamp.isEmpty()) {
            System.out.println("Returning: " + response.getStatus());
            return response;
        }
        
        getLog().debug("All parameters present");
        
        //Fetches password. Also checks that user exists
        User user = userDao.findByUsername(username);
        String password = user.getPassword();
        if(password==null)
        {
            getLog().debug("User doesn't exist");
            System.out.println("Returning: " + response.getStatus());
            return response;
        }
        
        getLog().debug("Generating hash of {} with key {}", password, timestamp);
        String hash = HMAC.getHmac(timestamp, password);
        getLog().debug("Comparing hashes {} and {}", hash, requestHash);
        if(!hash.equals(requestHash))
        {
            getLog().debug("Hashes do not match!");
            System.out.println("Returning: " + response.getStatus());
            return response;
        }
        
        response.setStatus(200);
        response.setMessage(user.getEmail());
        System.out.println("Returning: " + response.getStatus());
        return response;
    }

    public DefaultResponse registerUser(User request) {
        System.out.println("Registering user");
        DefaultResponse response = new DefaultResponse();
        response.setSource("Crowd_tidbits");
        
        String email = request.getEmail();
        String username = request.getUsername();

        //CHeck that all parameters are included
        //Could be done in one if statement, but readability is improved
        if (email == null || email.isEmpty()) {
            response.setMessage("The request does not contain all required parameters");
            response.setStatus(400);
            System.out.println("Returning: " + response.getStatus());
            return response;
        } 
        if (username == null || username.isEmpty()) {
            response.setMessage("The request does not contain all required parameters");
            response.setStatus(400);
            System.out.println("Returning: " + response.getStatus());
            return response;
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            response.setMessage("The request does not contain all required parameters");
            response.setStatus(400);
            System.out.println("Returning: " + response.getStatus());
            return response;
        }
        
        User exists = userDao.findByEmailAndUsername(email, username);
        if(exists!=null)
        {
            if(username.equalsIgnoreCase(exists.getUsername()))
            {
                response.setMessage("Username exists");
                response.setStatus(200);
            }
            else if(email.equalsIgnoreCase(exists.getEmail()))
            {
                response.setMessage("Email exists");
                response.setStatus(200);
            }
            System.out.println("Returning: " + response.getStatus());
            return response;
        }
        
        User create = new User();
        create.setEmail(email);
        create.setUsername(username);
        create.setEnabled(true);
        //Password from request is HMAC generated over username with password as key
        create.setPassword(request.getPassword());
        userDao.create(create);
        
        response.setMessage("Created");
        response.setStatus(202);
        System.out.println("Returning: " + response.getStatus());
        return response;
    }
}
