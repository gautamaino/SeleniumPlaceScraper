package com.ainosoft.seleniumplacescraper.dao;
// Generated 4 May, 2016 4:16:02 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;

import com.ainosoft.seleniumplacescraper.pojo.PlacesDetailsPojo;

/**
 * Home object for domain model class PlacesDetails.
 * @see .PlacesDetails
 * @author Hibernate Tools
 */
public class PlacesDetailsDao {

	private static final Log log = LogFactory.getLog(PlacesDetailsDao.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext()
			.lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(PlacesDetailsPojo transientInstance) {
		log.debug("persisting PlacesDetails instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(PlacesDetailsPojo instance) {
		log.debug("attaching dirty PlacesDetails instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PlacesDetailsPojo instance) {
		log.debug("attaching clean PlacesDetails instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(PlacesDetailsPojo persistentInstance) {
		log.debug("deleting PlacesDetails instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PlacesDetailsPojo merge(PlacesDetailsPojo detachedInstance) {
		log.debug("merging PlacesDetails instance");
		try {
			PlacesDetailsPojo result = (PlacesDetailsPojo) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PlacesDetailsPojo findById(java.lang.Long id) {
		log.debug("getting PlacesDetails instance with id: " + id);
		try {
			PlacesDetailsPojo instance = (PlacesDetailsPojo) sessionFactory
					.getCurrentSession().get("PlacesDetails", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<PlacesDetailsPojo> findByExample(PlacesDetailsPojo instance) {
		log.debug("finding PlacesDetails instance by example");
		try {
			List<PlacesDetailsPojo> results = (List<PlacesDetailsPojo>) sessionFactory
					.getCurrentSession().createCriteria("PlacesDetails")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	private Criterion create(PlacesDetailsPojo instance) {
		// TODO Auto-generated method stub
		return null;
	}
}