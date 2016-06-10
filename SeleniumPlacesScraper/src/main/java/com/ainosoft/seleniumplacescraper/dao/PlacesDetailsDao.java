package com.ainosoft.seleniumplacescraper.dao;
// Generated 4 May, 2016 4:16:02 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
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

	private Logger logger = Logger.getLogger(this.getClass().getName());

	protected SessionFactory getSessionFactory() {
		try {
			return HibernateUtil.getSessionFactory();
		} catch (Exception e) {
			String exceptionMethod = "PlacesDetailsDao :: getSessionFactory() ::";
			logger.log(Level.SEVERE,exceptionMethod,e);
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

			@SuppressWarnings("unchecked")
			List<PlacesDetailsPojo> restaurantPojoList = query.list();

			return restaurantPojoList;
		}catch(Exception e){
			String exceptionMethod = "PlacesDetailsDao :: getAllPlacesDetailsPojoList() ::";
			logger.log(Level.SEVERE,exceptionMethod,e);
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
	public PlacesDetailsPojo savePlacesDetailsPojo(PlacesDetailsPojo placesDetailsPojo) {
		Session session=null;
		Transaction transaction= null;
		try {
			placesDetailsPojo.setModifiedOn(new Date());
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			if(placesDetailsPojo.getId()!=null && placesDetailsPojo.getId()!=0){
				placesDetailsPojo = (PlacesDetailsPojo) session.merge(placesDetailsPojo);
			}
			else{
				Long savedId = (Long) session.save(placesDetailsPojo);
				if(savedId!=0){
					placesDetailsPojo.setId(savedId);
				}
				else{
					return null;
				}
			}
			transaction.commit();
			return placesDetailsPojo;
		} catch (RuntimeException re) {
			String exceptionMethod = "PlacesDetailsDao :: savePlacesDetailsPojo() ::";
			logger.log(Level.SEVERE,exceptionMethod,re);
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