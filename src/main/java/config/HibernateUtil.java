package config;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import space_travel.entity.Client;
import space_travel.entity.Planet;
import space_travel.entity.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("java:S6548") // "Avoid using the singleton pattern"
public class HibernateUtil {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static final HibernateUtil INSTANCE = new HibernateUtil();


    @Getter
    private  SessionFactory sessionFactory;
    public static HibernateUtil getInstance() {
        return INSTANCE;
    }
    private HibernateUtil() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(Planet.class)
                .addAnnotatedClass(Ticket.class)
                .buildSessionFactory();
        logger.info("Hibernate SessionFactory created.");
    }

    public void close() {
        sessionFactory.close();
        logger.info("Hibernate SessionFactory closed.");
    }

}
