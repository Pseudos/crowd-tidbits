package server.rest;

import javax.ws.rs.Path;

import org.apache.commons.configuration.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import server.service.PostService;

@Component
@Path("/post")
public class PostRest {
    @Autowired
    private Configuration appConfig;
    @Autowired
    private PostService postService;
}
