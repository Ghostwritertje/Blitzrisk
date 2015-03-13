package be.kdg.dao;

import be.kdg.model.Turn;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Save and retrieve turns
 */

@Service("TurnDao")
public class TurnDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Turn getTurnById(int id) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Turn where id = :id");
        query.setParameter("id", id);
        return (Turn) query.uniqueResult();
    }

    public void createTurn(Turn turn) {
        sessionFactory.getCurrentSession().save(turn);
    }

    public void updateTurn(Turn turn) {
        sessionFactory.getCurrentSession().update(turn);

    }

    public void removeTurn(Turn turn) {
        sessionFactory.getCurrentSession().delete(turn);
    }
}
