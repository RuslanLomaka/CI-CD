import org.hibernate.Session;
import org.hibernate.Transaction;
import passenger.Passenger;
import storage.hibernate.HibernateUtil;

public class App {
    public static void main(String[] args) {
        HibernateUtil h = HibernateUtil.getInstance();
        Session session = h.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Passenger p = new Passenger();
        p.setPassengerId(1);
        p.setName("Jack");
        p.setPassport("1234");

        session.persist(p);
        tx.commit();
        session.close();
    }
}