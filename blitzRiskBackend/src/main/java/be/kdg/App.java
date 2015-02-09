package be.kdg;

import be.kdg.controllers.GameController;
import be.kdg.model.Game;
import be.kdg.model.Territory;
import be.kdg.model.User;
import be.kdg.dao.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        /*Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        User user = new User();
        user.setName("Joran");
        user.setEmail("jorandeboever@gmail.com");
        user.setPassword("joran");
        session.save(user);
        tx.commit();*/

        GameController controller = new GameController();
        List<User> lijst = new ArrayList<User>();
        User user1 = new User();
        user1.setName("een");
        User user2 = new User();
        user2.setName("twee");
        User user3 = new User();
        user3.setName("drie");
        User user4 = new User();
        user4.setName("vier");
        User user5 = new User();
        user5.setName("vijf");

        lijst.add(user1);
        lijst.add(user2);
        lijst.add(user3);
        lijst.add(user4);
      //  lijst.add(user5);

        Game game = controller.createNewGame(lijst);

        Set<Territory> territories = game.getTerritories();

        for(Territory terr : territories) {
            System.out.println(terr.getGameId());
            System.out.println(terr.getPlayer());

        }

    }
}
