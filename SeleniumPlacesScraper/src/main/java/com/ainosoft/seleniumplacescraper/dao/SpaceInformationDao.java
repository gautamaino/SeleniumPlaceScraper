package com.ainosoft.seleniumplacescraper.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.ainosoft.seleniumplacescraper.pojo.SpaceInformationPojo;
import com.ainosoft.seleniumplacescraper.util.HibernateUtil;
// default package
// Generated 10 May, 2016 3:01:13 PM by Hibernate Tools 3.4.0.CR1


/**
 * @author tushar@ainosoft.com
 */
public class SpaceInformationDao {

	private Logger logger = Logger.getLogger(this.getClass().getName());


	protected SessionFactory getSessionFactory() {
		try {
			return HibernateUtil.getSessionFactory();
		} catch (Exception e) {
			logger.log(Level.SEVERE,"SpaceInformationDao :: getSessionFactory() :: Exception :: ",e);
		}
		return null;
	}


	/**
	 * This method is used for retrieving all PlacesDetailsDao from database
	 * @return List<SpaceInformationPojo>
	 * @throws Exception 
	 */
	public List<SpaceInformationPojo> getAllSpaceInformationPojoList() throws Exception {
		Session session = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			Query query = session.createQuery("from SpaceInformationPojo");

			@SuppressWarnings("unchecked")
			List<SpaceInformationPojo> restaurantPojoList =  query.list();

			return restaurantPojoList;
		}catch(Exception e){
			logger.log(Level.SEVERE,"SpaceInformationDao :: getAllSpaceInformationPojoList() :: Exception :: ",e);
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
	 * @param SpaceInformationPojo
	 * @return SpaceInformationPojo
	 */
	public SpaceInformationPojo saveSpaceInformationPojo(SpaceInformationPojo spaceInfo) {
		Session session=null;
		Transaction transaction= null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			if(spaceInfo.getId()!=null && spaceInfo.getId()!=0){
				spaceInfo = (SpaceInformationPojo) session.merge(spaceInfo);
			}
			else{
				Long savedId = (Long) session.save(spaceInfo);
				if(savedId!=0){
					spaceInfo.setId(savedId);
				}
				else{
					return null;
				}
			}
			transaction.commit();
			return spaceInfo;
		} catch (RuntimeException re) {
			logger.log(Level.SEVERE,"SpaceInformationDao :: saveSpaceInformationPojo() :: Exception :: ",re);
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
	 * @param spaceInfoPojo
	 */
	public void updatePageCount(SpaceInformationPojo spaceInfoPojo) {
		Session session=null;
		Transaction transaction= null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Long id = spaceInfoPojo.getId();
			Query query = session.createSQLQuery("update space_information set page_count = "+spaceInfoPojo.getPageCount()+" where id ="+id);
			query.executeUpdate();
			
			transaction.commit();
		} catch (RuntimeException re) {
			logger.log(Level.SEVERE,"SpaceInformationDao :: updatePageCount() ::",re); 
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
