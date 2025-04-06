package sandbox;

import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.hibernate.Transaction;
import spaceTravel.passenger.Passenger;
import storage.hibernate.HibernateUtil;



public class App {

    public static void migrate(){
        Flyway.configure()
                .dataSource("jdbc:h2:./spacetrain", "", "")
                .baselineOnMigrate(true)
                .load()
                .migrate();
    }

    public static void main(String[] args) {
        //App.migrate();
        HibernateUtil h = HibernateUtil.getInstance();
        Session session = h.getSessionFactory().openSession();
//        Transaction tx = session.beginTransaction();
//        Passenger p = new Passenger();
//        p.setName("Rose");
//        p.setPassport("2234");
//        session.persist(p);
//        tx.commit();

        session.close();

    }


}