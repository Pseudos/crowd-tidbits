/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Sydney
 * 
 * Abstract bean to provide common database functionality
 */

@Transactional
@Repository
public abstract class AbstractDao<T>{

    private Class<T> entityClass;
    @Autowired
	public SessionFactory sessionFactory;
    
    protected AbstractDao() {
        getLog().info(this.getClass().getSimpleName() + " constructor invoked");
    }

    @SuppressWarnings("unchecked")
    public T findByID(long id) {
        T t = (T)getCurrentSession().get(entityClass, id);
        return t;
    }
    
    public Session getCurrentSession ()
    {
    	return sessionFactory.getCurrentSession();
    }

    public AbstractDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void create(T entity) {
    	getCurrentSession().merge(entity);
    }

    protected Logger getLog() {
        return LoggerFactory.getLogger(getClass());
    }

    public void edit(T entity) {
    	getCurrentSession().merge(entity);
    }
    
    public void update (T entity)
    {
        getCurrentSession().update(entity);
    }

    public void delete(T entity) {
    	getCurrentSession().delete(entity);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        List<T> stuff = getCurrentSession().createCriteria(entityClass).list();
        if(stuff==null)
        {
            return new ArrayList<T>();
        }
        return stuff;
    }

    public void deleteAll(List<T> entityList) {

    }

    public Long count() {
        Criteria crit = getCurrentSession().createCriteria(entityClass);
        crit.setProjection(Projections.rowCount());
        return ((Number)crit.uniqueResult()).longValue();
    }
    
    public void save(T entity)
    {
    	getCurrentSession().save(entity);
    }

    public void refresh(T model) {
    	 getCurrentSession().refresh(model);
    }
}
