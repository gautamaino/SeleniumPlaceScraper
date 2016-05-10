package com.ainosoft.seleniumplacescraper;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import com.ainosoft.seleniumplacescraper.util.HibernateUtil;


/**
 * 
 * @author tushar@ainosoft.com
 *
 */

public class HibernateUtilTest {

	private static Logger logger = Logger.getLogger(HibernateUtil.class.getName());

	/**
	 * This method is use to check whether getSessionFactory() method is working properly or not
	 */

	@Test
	public void testGetSessionFactory() {
		try{
			assertTrue(HibernateUtil.getSessionFactory() != null);
		}catch(Exception e){
			logger.log(Level.SEVERE, "HibernateUtilTest :: testGetSessionFactory()", e);
		}
	}
}
