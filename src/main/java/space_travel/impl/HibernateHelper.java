package space_travel.impl;

import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateHelper {
    private HibernateHelper() {};
    public static <T> void saveEntity(T entity) {
        Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
        Transaction transaction = null;
        try (session) {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public static <T> void updateEntity(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(entity); // merge: update or insert if detached
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Failed to update entity: " + entity, e);
        }
    }

    public static <T, ID> void deleteEntityById(Class<T> entityClass, ID id) {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            T entity = session.find(entityClass, id);
            if (entity != null) {
                session.remove(entity);
                transaction.commit();
            } else {
                transaction.rollback();
                throw new IllegalArgumentException(entityClass.getSimpleName() + " with ID " + id + " not found.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete " + entityClass.getSimpleName() + " with ID " + id, e);
        }
    }

    public static <T, ID> T findEntityById(Class<T> entityClass, ID id) {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            T entity = session.find(entityClass, id);
            transaction.commit();
            if (entity != null) {
                return entity;
            } else {
                throw new IllegalArgumentException(entityClass.getSimpleName() + " with ID " + id + " not found.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to find " + entityClass.getSimpleName() + " with ID " + id, e);
        }
    }

    public static <T> List<T> findAllEntities(Class<T> entityClass) {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            List<T> resultList = session
                    .createQuery("FROM " + entityClass.getSimpleName(), entityClass)
                    .getResultList();
            transaction.commit();
            return resultList;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch all " + entityClass.getSimpleName() + " entities", e);
        }
    }

    public static <T, ID> T getEntityIfExists(Class<T> entityClass, ID id) {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            return session.find(entityClass, id);
        }
    }


}