package com.ainosoft.seleniumplacescraper.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author tushar@ainosoft.com
 */
public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static Configuration config;

	private static Logger logger = Logger.getLogger(HibernateUtil.class.getName());

	public HibernateUtil() {

	}

	static {
		try {
			
			config = new Configuration().configure("/hibernate.cfg.xml");
			
		} catch (final Throwable th) {
			logger.log(Level.SEVERE, "from HibernateUtil static block", th);
		}
	}

	public static SessionFactory getSessionFactory() {
		try{
			if (sessionFactory != null)
				return sessionFactory;
			else {
				sessionFactory = config.buildSessionFactory();
				return sessionFactory;
			}
		}catch(Exception e){
			logger.log(Level.SEVERE, "from HibernateUtil :: getSessionFactory()", e);
		}
		return sessionFactory;
	}
}