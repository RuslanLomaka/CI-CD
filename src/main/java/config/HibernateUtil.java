package config;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import space_travel.entity.Passenger;
import space_travel.entity.Planet;
import space_travel.entity.Ticket;

public class HibernateUtil {

    private static final HibernateUtil INSTANCE;

    static {
        INSTANCE = new HibernateUtil();
    }

    @Getter
    private  SessionFactory sessionFactory;

    private HibernateUtil() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Passenger.class)
                .addAnnotatedClass(Planet.class)
                .addAnnotatedClass(Ticket.class)
                .buildSessionFactory();
    }

    public static HibernateUtil getInstance() {
        return INSTANCE;
    }


    public void close() {
        sessionFactory.close();
    }

}
