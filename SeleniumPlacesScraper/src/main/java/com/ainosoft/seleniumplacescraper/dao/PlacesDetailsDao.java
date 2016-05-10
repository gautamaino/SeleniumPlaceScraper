package com.ainosoft.seleniumplacescraper.dao;
// Generated 4 May, 2016 4:16:02 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.ainosoft.seleniumplacescraper.pojo.PlacesDetailsPojo;
import com.ainosoft.seleniumplacescraper.util.HibernateUtil;
import com.ainosoft.seleniumplacescraper.util.ScraperLogger;

/**
 * @author tushar@ainosoft.com
 */
public class PlacesDetailsDao {

	private ScraperLogger scraperLogger = new ScraperLogger("googlemaps");

	protected SessionFactory getSessionFactory() {
		try {
			return HibernateUtil.getSessionFactory();
		} catch (Exception e) {
			scraperLogger.log("PlacesDetailsDao :: getSessionFactory() :: Exception :: ",e);
		}
		return null;
	}


	/**
	 * This method is used for retrieving all PlacesDetailsDao from database
	 * @return List<PlacesDetailsPojo>
	 * @throws Exception 
	 */
	public List<PlacesDetailsPojo> getAllPlacesDetailsPojoList() throws Exception {
		Session session = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			Query query = session.createQuery("from PlacesDetailsPojo");

			List<PlacesDetailsPojo> restaurantPojoList = query.list();

			return restaurantPojoList;
		}catch(Exception e){
			scraperLogger.log("PlacesDetailsDao :: getAllPlacesDetailsPojoList() :: Exception :: ",e);
			throw e;
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
		}
	}


	/**
	 * This method is used for saving a PlacesDetailsDao to database
	 * @param restaurantDetailsPojo
	 * @return PlacesDetailsPojo
	 */
	public PlacesDetailsPojo savePlacesDetailsPojo(PlacesDetailsPojo proxyPojo) {
		Session session=null;
		Transaction transaction= null;
		try {
			proxyPojo.setModifiedOn(new Date());
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			if(proxyPojo.getId()!=null && proxyPojo.getId()!=0){
				proxyPojo = (PlacesDetailsPojo) session.merge(proxyPojo);
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
			scraperLogger.log("PlacesDetailsDao :: savePlacesDetailsPojo() :: Exception :: ",re);
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