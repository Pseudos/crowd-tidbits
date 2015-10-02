package server.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import server.entity.User;
import server.rest.request.AuthenticateRequest;
import server.rest.response.DefaultResponse;
import server.service.UserService;

/**
 * 
 * @author Sydney
 *
 */

@Component
@Path("/user")
public class UserRest {

    @Autowired
    private Configuration appConfig;
    @Autowired
    private UserService userService;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    @POST
    @Path("/authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticate(AuthenticateRequest request)
    {
        DefaultResponse resp = userService.authenticateUser(request);
        Response response = Response.status(Status.fromStatusCode(resp.getStatus())).entity(resp).build();
        return response;
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(User request)
    {
        DefaultResponse resp = userService.registerUser(request);
        Response response = Response.status(Status.fromStatusCode(resp.getStatus())).entity(resp).build();
        return response;
        
    }
    
    protected Logger getLog() {
        return LoggerFactory.getLogger(getClass());
    }
}
