package com.ainosoft.seleniumplacescraper.dao;
// default package
// Generated 4 May, 2016 4:16:02 PM by Hibernate Tools 3.4.0.CR1

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;
import com.ainosoft.seleniumplacescraper.util.HibernateUtil;
import com.ainosoft.seleniumplacescraper.util.ScraperLogger;

/**
 * Home object for domain model class ProxyDetails.
 * @see .ProxyDetails
 * @author Hibernate Tools
 */
public class ProxyDetailsDao {

	@SuppressWarnings("unused")
	private static ScraperLogger scraperLogger = new ScraperLogger("ProxyDetailsDao");

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return HibernateUtil.getSessionFactory();
		} catch (Exception e) {
			ScraperLogger.log("ProxyManager :: getSessionFactory() ::",e); 
		}
		return sessionFactory;
	}

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
			ScraperLogger.log("ProxyManager :: getSessionFactory() ::",re); 
			transaction.rollback();
			throw re;
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ProxyDetailsPojo> getValidProxyList() {
		ArrayList<ProxyDetailsPojo> proxyDetailsPojoList = null;
		Session session=null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			Query query = session.createSQLQuery("select * from ProxyDetails where status = 1");
			proxyDetailsPojoList = (ArrayList<ProxyDetailsPojo>) query.list();
		}catch(Exception e){
			ScraperLogger.log("ProxyManager :: getValidProxyList() ::",e); 
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
		}
		return (ArrayList<ProxyDetailsPojo>) proxyDetailsPojoList;
	}

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
			ScraperLogger.log("ProxyManager :: updateProxyStatus() ::",re); 
			transaction.rollback();
			throw re;
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
		}
	}
}