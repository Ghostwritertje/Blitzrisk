package be.kdg.dao;

import be.kdg.model.Territory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Save and retrieve territories.
 */

@Service("territoryDao")
public class TerritoryDao {
    @Autowired
    private SessionFactory sessionFactory;

    public Territory getTerritoryById(int id) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Territory where id = :id");
        query.setParameter("id", id);
        return (Territory) query.uniqueResult();
    }

    public void updateTerritory(Territory territory) {
        sessionFactory.getCurrentSession().update(territory);
    }
    
    public void saveTerritory(Territory territory) {
        sessionFactory.getCurrentSession().save(territory);
    }

    public void removeTerritory(Territory territory) {
        sessionFactory.getCurrentSession().delete(territory);
    }
}
