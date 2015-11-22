package server.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

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
	
	public String findHashByUsername(String username)
	{
	    Criteria crit = getCurrentSession().createCriteria(User.class);
        crit.add(Restrictions.and(Restrictions.eq("username", username),Restrictions.eq("enabled", true)));
        List<User> users = crit.list();
        if (users == null || users.size() <= 0) {
            return null;
        }

        return users.get(0).getPassword();
	}
	
	public User findByEmailAndUsername(String email, String username)
	{
	    Criteria crit = getCurrentSession().createCriteria(User.class);
        crit.add(Restrictions.or(Restrictions.eq("email", email), Restrictions.eq("username", username)));
        List<User> users = crit.list();
        if (users == null || users.size() <= 0) {
            return null;
        }

        return users.get(0);
	}
	
	public User findByUsername(String username)
    {
        Criteria crit = getCurrentSession().createCriteria(User.class);
        crit.add(Restrictions.eq("username", username));
        List<User> users = crit.list();
        if (users == null || users.size() <= 0) {
            return null;
        }

        return users.get(0);
    }
	
	public User findByEmail(String email)
    {
        Criteria crit = getCurrentSession().createCriteria(User.class);
        crit.add(Restrictions.eq("email", email));
        List<User> users = crit.list();
        if (users == null || users.size() <= 0) {
            return null;
        }

        return users.get(0);
    }
}
