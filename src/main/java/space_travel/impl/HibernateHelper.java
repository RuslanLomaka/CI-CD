package space_travel.impl;

import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space_travel.exception.DataDeleteException;
import space_travel.exception.DataFetchException;
import space_travel.exception.DataRetrievalException;
import space_travel.exception.DataUpdateException;

import java.util.List;

public class HibernateHelper {
    private static final Logger logger = LoggerFactory.getLogger(HibernateHelper.class);
    private HibernateHelper() {}
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
            throw new DataUpdateException("Failed to update entity: " + entity, e);
        }
    }

    public static <T, I> void deleteEntityById(Class<T> entityClass, I i) {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            T entity = session.find(entityClass, i);
            if (entity != null) {
                session.remove(entity);
                transaction.commit();
            } else {
                transaction.rollback();
                logger.info("ℹ️ {} with K {} doesn't exist. Skipping delete.", entityClass.getSimpleName(), i);

            }
        } catch (Exception e) {
            throw new DataDeleteException("Failed to delete " + entityClass.getSimpleName() + " with ID " + i, e);

        }
    }

    public static <T, K> T findEntityById(Class<T> entityClass, K k) {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            T entity = session.find(entityClass, k);
            transaction.commit();
            if (entity != null) {
                return entity;
            } else {
                throw new IllegalArgumentException(entityClass.getSimpleName() + " with I " + k + " not found.");
            }
        } catch (Exception e) {
            throw new DataRetrievalException("Failed to find " + entityClass.getSimpleName() + " with ID " + k, e);

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
            throw new DataFetchException("Failed to fetch all " + entityClass.getSimpleName() + " entities", e);

        }
    }

    public static <T, V> T getEntityIfExists(Class<T> entityClass, V v) {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            return session.find(entityClass, v);
        }
    }


}