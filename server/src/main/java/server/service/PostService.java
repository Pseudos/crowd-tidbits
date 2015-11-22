package server.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import server.dao.PostDao;
import server.dao.UserDao;
import server.entity.Post;
import server.entity.User;
import server.rest.request.BoxRequest;
import server.rest.request.RadiusRequest;
import server.rest.request.SubmitPostRequest;
import server.rest.response.DefaultResponse;
import server.rest.response.PostResponse;
import server.util.HMAC;

public class PostService {
    @Autowired
    private PostDao postDao;
    
    @Autowired
    private UserDao userDao;
    
    protected Logger getLog() {
        return LoggerFactory.getLogger(getClass());
    }

    public DefaultResponse addPost(SubmitPostRequest request) {
        DefaultResponse response = new DefaultResponse();
        response.setSource("Crowd_tidbits");
        
        User poster = userDao.findByEmail(request.getPoster());
        if(poster==null)
        {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Request parameters incorrect");
            System.out.println("Returning: " + response.getStatus());
            return response;
        }
        
        String hash = HMAC.getHmac(request.getPoster(), poster.getPassword());
        getLog().debug("Comparing hashes {} and {}", hash, request.getHash());
        if(!hash.equals(request.getHash()))
        {
            getLog().debug("Hashes do not match!");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setMessage("Not authorized");
            System.out.println("Returning: " + response.getStatus());
            return response;
        }
        
        
        User pstr = userDao.findByEmail(poster.getEmail());
        
        Post post = new Post();
        post.setDescription(request.getDescription());
        post.setLatitude(request.getLatitude());
        post.setLongitude(request.getLongitude());
        post.setPriority(request.getPriority());
        post.setPoster(pstr);
        post.setPostTime(new Date());
        
        postDao.create(post);
        
        response.setStatus(HttpStatus.ACCEPTED.value());
        response.setMessage("Accepted");
        System.out.println("Returning: " + response.getStatus());
        return response;
    }

    public PostResponse boxPosts(BoxRequest request) {
        PostResponse response = new PostResponse();
        response.setSource("Crowd_tidbits");
        
        double trLat = request.getTrLat();
        double trLon = request.getTrLon();
        double blLat = request.getBlLat();
        double blLon = request.getBlLon();
        int priority = request.getPriority();
        
        List<Post> posts = postDao.getBoundingBox(trLat, trLon, blLat, blLon, priority);
        
        response.setNumPosts(posts.size());
        response.setMessage("");
        response.setPosts(posts);
        response.setStatus(HttpStatus.OK.value());
        
        System.out.println("Returning: " + response.getStatus());
        return response;
    }
    
    public PostResponse distancePosts(RadiusRequest request) {
        PostResponse response = new PostResponse();
        response.setSource("Crowd_tidbits");
        
        double lat = request.getLat();
        double lon = request.getLon();
        double distance = request.getDistance(); //km
        int priority = request.getPriority();
        
        List<Post> posts = postDao.getInRadius(lat, lon, distance, priority);
        
        response.setNumPosts(posts.size());
        response.setMessage("");
        response.setPosts(posts);
        response.setStatus(HttpStatus.OK.value());
        
        System.out.println("Returning: " + response.getStatus());
        return response;
    }
}
