package be.kdg.dao;

import be.kdg.model.Territory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Marlies on 27/02/2015.
 */

@Service("territoryDao")
public class TerritoryDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Territory getTerritoryById(int id) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Territory where id = :id");
        query.setParameter("id", id);
        return (Territory) query.uniqueResult();
    }

    public void updateTerritory(Territory territory) {
        sessionFactory.getCurrentSession().saveOrUpdate(territory);
    }

    public void removeTerritory(Territory territory) {
        sessionFactory.getCurrentSession().delete(territory);
    }
}
