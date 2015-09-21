package server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import server.dao.UserDao;

public class UserService {
    @Autowired
    UserDao userDao;
    
    protected Logger getLog() {
        return LoggerFactory.getLogger(getClass());
    }
}