package be.kdg.dao;

import be.kdg.model.Move;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Saves and retrieves moves in the database.
 */

@Service("moveDao")
public class MoveDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Move getMoveById(int id) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Move where id = :id");
        query.setParameter("id", id);
        return (Move) query.uniqueResult();
    }

    public void updateMove(Move move) {
        sessionFactory.getCurrentSession().saveOrUpdate(move);
    }

    public List<Move> findall() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Move.class);
        return criteria.list();
    }

    public void removeMove(Move move) {
        sessionFactory.getCurrentSession().delete(move);
    }
}
