package com.ainosoft.seleniumplacescraper.dao;
// Generated 4 May, 2016 4:16:02 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.ainosoft.seleniumplacescraper.pojo.PlacesDetailsPojo;
import com.ainosoft.seleniumplacescraper.util.HibernateUtil;

/**
 * @author tushar@ainosoft.com
 */
public class PlacesDetailsDao {

	private Logger logger=Logger.getLogger(this.getClass().getName());
	
	protected SessionFactory getSessionFactory() {
		try {
			return HibernateUtil.getSessionFactory();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "LdShapeFormatDao::getSessionFactory()",e);
		}
		return null;
	}
	
	
	/**
	 * This method is used for retrieving all RestaurantDetailsPojo from database
	 * @return
	 * @throws Exception 
	 */
	public List<PlacesDetailsPojo> getAllPlacesDetailsPojoList() throws Exception {
		Session session=null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			Query query = session.createQuery("from PlacesDetailsPojo");

			List<PlacesDetailsPojo> restaurantPojoList = query.list();

			return restaurantPojoList;
		}catch(Exception e){
			logger.log(Level.SEVERE, "RestaurantDetailsDao :: getAllPlacesDetailsPojoList() ::Exception ::",e);
			throw e;
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
		}
	}
	
	
	/**
	 * This method is used for saving a RestaurantDetailsPojo to database
	 * @param restaurantDetailsPojo
	 * @return
	 */
	public PlacesDetailsPojo savePlacesDetailsPojo(PlacesDetailsPojo placesDetailsPojo) {
		Session session=null;
		Transaction transaction= null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			placesDetailsPojo = (PlacesDetailsPojo) session.merge(placesDetailsPojo);

			transaction.commit();
			return placesDetailsPojo;
		} catch (RuntimeException re) {
			logger.log(Level.SEVERE, "RestaurantDetailsDao :: savePlacesDetailsPojo() ::Exception ::",re);
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