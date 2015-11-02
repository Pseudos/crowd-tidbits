package server.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import server.entity.Post;

public class PostDao extends AbstractDao<Post>{
    public Post findPostById(Long id)
    {
        Post result = null;
        
        Criteria crit = getCurrentSession().createCriteria(Post.class);
        crit.add(Restrictions.eq("id", id));
        List<Post> list = crit.list();
        if(list.size()>0)
        {
            return list.get(0);
        }
        return null;
    }
    
    public List<Post> getBoundingBox(double trLat, double trLon, double blLat, double blLon)
    {
        Criteria crit = getCurrentSession().createCriteria(Post.class);
        crit.add(Restrictions.and(Restrictions.between("latitude", trLat, blLat),Restrictions.between("longitude",blLon,trLon)));
        List<Post> list = crit.list();
        
        if(list.size()>0)
        {
            return list;
        }
        return new ArrayList<Post>();
    }
    
    public List<Post> getInRadius(double lat, double lon, double distance)
    {
        //Create the bounding box
        double blLat, blLon, trLat, trLon;
        
        //Latitude: 1 deg = 110.574 km
        //Longitude: 1 deg = 111.320*cos(latitude) km
        
        double latDistance = distance/110.574;
        double lonDistance = distance/111.320*Math.cos(lat);
        
        blLat = lat - latDistance;
        trLat = lat + latDistance;
        blLon = lon - lonDistance;
        trLon = lon + lonDistance;
        
        return getBoundingBox(trLat, trLon, blLat, blLon);
    }
}
