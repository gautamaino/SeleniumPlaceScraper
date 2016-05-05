/*package com.ainosoft.seleniumplacescraper.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Query;

import org.hibernate.SessionFactory;

import com.ainosoft.seleniumplacescraper.util.HibernateUtil;
import com.ainosoft.seleniumplacescraper.util.ScraperLogger;

*//**
 * @author tushar@ainosoft.com
 *//*
public class RestaurantDetailsDao {

	private static ScraperLogger scraperLogger = new ScraperLogger("googlemaps");

	private Logger logger=Logger.getLogger(this.getClass().getName());
	
	protected SessionFactory getSessionFactory() {
		try {
			return HibernateUtil.getSessionFactory();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "LdShapeFormatDao::getSessionFactory()",e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}
	
	
	*//**
	 * This method is used for retrieving all RestaurantDetailsPojo from database
	 * @return
	 *//*
	public List<RestaurantDetailsPojo> getAllRestaurantPojoList() {
		Session session=null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			Query query = session.createQuery("from RestaurantDetailsPojo");

			List<RestaurantDetailsPojo> restaurantPojoList = query.list();

			return restaurantPojoList;
		}catch(Exception e){
			logger.log(Level.SEVERE, "RestaurantDetailsDao :: getAllRestaurantPojoList() ::Exception ::",e);
			throw e;
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
		}
	}
	
	
	*//**
	 * This method is used for saving a RestaurantDetailsPojo to database
	 * @param restaurantDetailsPojo
	 * @return
	 *//*
	public RestaurantDetailsPojo saveRestaurantInfo(RestaurantDetailsPojo restaurantDetailsPojo) {
		Session session=null;
		Transaction transaction= null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			restaurantDetailsPojo = (RestaurantDetailsPojo) session.merge(restaurantDetailsPojo);

			transaction.commit();
			return restaurantDetailsPojo;
		} catch (RuntimeException re) {
			logger.log(Level.SEVERE, "RestaurantDetailsDao :: saveRestaurantInfo() ::Exception ::",re);
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
*/