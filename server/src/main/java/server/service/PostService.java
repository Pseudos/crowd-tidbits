package server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import server.dao.PostDao;

public class PostService {
    @Autowired
    private PostDao postDao;
    
    protected Logger getLog() {
        return LoggerFactory.getLogger(getClass());
    }
}
