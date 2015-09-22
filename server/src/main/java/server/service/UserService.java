package server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import server.dao.UserDao;
import server.entity.User;
import server.rest.response.DefaultResponse;

public class UserService {
    @Autowired
    UserDao userDao;
    
    protected Logger getLog() {
        return LoggerFactory.getLogger(getClass());
    }

    public DefaultResponse registerUser(User request) {
        DefaultResponse response = new DefaultResponse();
        
        int status = 404;
        response.setMessage("NOT_FOUND");
        
        String email = request.getEmail();
        String username = request.getUsername();

        //CHeck that all parameters are included
        //Could be done in one if statement, but readability is improved
        if (email == null || email.isEmpty()) {
            status = 400;
            response.setMessage("The request does not contain all required parameters");
            response.setStatus(status);
            return response;
        } 
        if (username == null || username.isEmpty()) {
            status = 400;
            response.setMessage("The request does not contain all required parameters");
            response.setStatus(status);
            return response;
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            status = 400;
            response.setMessage("The request does not contain all required parameters");
            response.setStatus(status);
            return response;
        }
        
        User exists = userDao.findByEmailOrUsername(email, username);
        if(exists!=null)
        {
            if(username.equalsIgnoreCase(exists.getUsername()))
            {
                status = 200;
                response.setMessage("Username exists");
                response.setStatus(status);
            }
            else if(email.equalsIgnoreCase(exists.getEmail()))
            {
                status = 200;
                response.setMessage("Email exists");
                response.setStatus(status);
            }
            return response;
        }
        
        User create = new User();
        create.setEmail(email);
        create.setUsername(username);
        create.setEnabled(true);
        //Password from request is HMAC generated over username with password as key
        create.setPassword(request.getPassword());
        userDao.create(create);
        status = 202;
        response.setMessage("Created");
        response.setStatus(status);
        return response;
    }
}
