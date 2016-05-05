package com.ainosoft.seleniumplacescraper.dao;
// default package
// Generated 4 May, 2016 4:16:02 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;

import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;

/**
 * Home object for domain model class ProxyDetails.
 * @see .ProxyDetails
 * @author Hibernate Tools
 */
public class ProxyDetailsDao {

	private static final Log log = LogFactory.getLog(ProxyDetailsDao.class);

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

	public void persist(ProxyDetailsPojo transientInstance) {
		log.debug("persisting ProxyDetailsPojo instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ProxyDetailsPojo instance) {
		log.debug("attaching dirty ProxyDetailsPojo instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ProxyDetailsPojo instance) {
		log.debug("attaching clean ProxyDetailsPojo instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ProxyDetailsPojo persistentInstance) {
		log.debug("deleting ProxyDetailsPojo instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ProxyDetailsPojo merge(ProxyDetailsPojo detachedInstance) {
		log.debug("merging ProxyDetailsPojo instance");
		try {
			ProxyDetailsPojo result = (ProxyDetailsPojo) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ProxyDetailsPojo findById(java.lang.Long id) {
		log.debug("getting ProxyDetailsPojo instance with id: " + id);
		try {
			ProxyDetailsPojo instance = (ProxyDetailsPojo) sessionFactory
					.getCurrentSession().get("ProxyDetailsPojo", id);
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

	public List<ProxyDetailsPojo> findByExample(ProxyDetailsPojo instance) {
		log.debug("finding ProxyDetailsPojo instance by example");
		try {
			List<ProxyDetailsPojo> results = (List<ProxyDetailsPojo>) sessionFactory
					.getCurrentSession().createCriteria("ProxyDetails")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	private Criterion create(ProxyDetailsPojo instance) {
		// TODO Auto-generated method stub
		return null;
	}
}
