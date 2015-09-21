package server.dao;

import server.entity.User;

/**
 * DAO for the user entity
 * 
 * @author Sydney
 * 
 */
public class UserDao extends AbstractDao<User> {
	public UserDao() {
		super(User.class);
	}
}
