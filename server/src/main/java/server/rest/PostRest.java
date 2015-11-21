package server.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.configuration.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import server.rest.request.BoxRequest;
import server.rest.request.RadiusRequest;
import server.rest.request.SubmitPostRequest;
import server.rest.response.DefaultResponse;
import server.rest.response.PostResponse;
import server.service.PostService;

@Component
@Path("/post")
public class PostRest {
    @Autowired
    private Configuration appConfig;
    @Autowired
    private PostService postService;
    
    @POST
    @Path("/submit")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    //{'description':'post text','latitude':0.0,'longitude':0.0,'poster':'userEmail','priority':0/1/2,'hash':'base64 SHA-1 HMAC'}
    //Hash of password (hash) generated with key - email
    public Response submit(SubmitPostRequest request)
    {
        DefaultResponse resp = postService.addPost(request);
        Response response = Response.status(Status.fromStatusCode(resp.getStatus())).entity(resp).build();
        return response;
    }
    
    @POST
    @Path("/box")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    //{'trLat':0.0,'trLon':0.0,'blLat':0.0,'blLon':0.0}
    public Response box(BoxRequest request)
    {
        PostResponse resp = postService.boxPosts(request);
        Response response = Response.status(Status.fromStatusCode(resp.getStatus())).entity(resp).build();
        return response;
    }
    
    @POST
    @Path("/radius")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    //{'lat':0.0,'lon':0.0,'distance':0.0}
    public Response radius(RadiusRequest request)
    {
        PostResponse resp = postService.distancePosts(request);
        Response response = Response.status(Status.fromStatusCode(resp.getStatus())).entity(resp).build();
        return response;
    }
}
