package com.ainosoft.seleniumplacescraper.dao;
// default package
// Generated 4 May, 2016 4:16:02 PM by Hibernate Tools 3.4.0.CR1

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;
import com.ainosoft.seleniumplacescraper.util.HibernateUtil;

/**
 * Home object for domain model class ProxyDetails.
 * @see .ProxyDetails
 * @author Hibernate Tools
 */
public class ProxyDetailsDao {

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(this.getClass().getName());

	private final SessionFactory sessionFactory = getSessionFactory();

	/**
	 * This methods creates a session for connecting to the database
	 * @return
	 */
	protected SessionFactory getSessionFactory() {
		try {
			return HibernateUtil.getSessionFactory();
		} catch (Exception e) {
			logger.log(Level.SEVERE,"ProxyManager :: getSessionFactory() ::",e); 
		}
		return sessionFactory;
	}

	/**
	 * This methods saves a proxy pojo to database
	 * @param proxyPojo
	 * @return
	 */
	public ProxyDetailsPojo saveProxyPojo(ProxyDetailsPojo proxyPojo) {
		Session session=null;
		Transaction transaction= null;
		try {
			proxyPojo.setModifiedOn(new Date());
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			if(proxyPojo.getId()!=null && proxyPojo.getId()!=0){
				proxyPojo = (ProxyDetailsPojo) session.merge(proxyPojo);
			}
			else{
				Long savedId = (Long) session.save(proxyPojo);
				if(savedId!=0){
					proxyPojo.setId(savedId);
				}
				else{
					return null;
				}
			}
			transaction.commit();
			return proxyPojo;
		} catch (RuntimeException re) {
			logger.log(Level.SEVERE,"ProxyManager :: getSessionFactory() ::",re); 
			transaction.rollback();
			throw re;
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
		}
	}

	/**
	 * This method updates the status of a proxy if it is found to be invalid
	 * @param proxyPojoList
	 */
	public void updateProxyStatus(ArrayList<ProxyDetailsPojo> proxyPojoList) {
		Session session=null;
		Transaction transaction= null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			for(ProxyDetailsPojo proxyPojo : proxyPojoList){
				Long id = proxyPojo.getId();
				Query query = session.createSQLQuery("update ProxyDetails set status = 0 where id ="+id);
				query.executeUpdate();
			}			
			transaction.commit();
		} catch (RuntimeException re) {
			logger.log(Level.SEVERE,"ProxyManager :: updateProxyStatus() ::",re); 
			transaction.rollback();
			throw re;
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
		}
	}

	/**
	 * This method returns arraylist of valid proxies
	 * @return
	 * @throws Exception
	 */
	public List<ProxyDetailsPojo> getValidProxyList() throws Exception {
		Session session=null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			Query query = session.createQuery("from ProxyDetailsPojo where status = 1");
			query.setMaxResults(10);
			
			List<ProxyDetailsPojo> proxyDetailsPojoList = query.list();

			return proxyDetailsPojoList;
		}catch(Exception e){
			logger.log(Level.SEVERE,"ProxyManager :: updateProxyStatus() ::",e); 
			throw e;
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
		}
	}

	public void saveProxyListToDatabase(ArrayList<ProxyDetailsPojo> proxyPojoList) {
		Session session=null;
		Transaction transaction= null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			for (int i=0; i<proxyPojoList.size(); i++){
				for(ProxyDetailsPojo proxyPojo : proxyPojoList){
					session.save(proxyPojo);
					if( i % 20 == 0 ) { 
						// Same as the JDBC batch size flush a batch of inserts and release memory:
						session.flush();
						session.clear();
					}
				}
			}
			transaction.commit();
		} catch (Exception e) {
			logger.log(Level.SEVERE,"ProxyManager :: saveProxyListToDatabase() ::",e); 
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
		}
	}
}