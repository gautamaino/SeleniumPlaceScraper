/**
 * 
 */
package com.ainosoft.seleniumplacescraper.dao;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;

/**
 * @author nalanda
 *
 */
public class ProxyDetailsDaoTest {

	ProxyDetailsDao proxyDao = new ProxyDetailsDao();

	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * Test method for {@link com.ainosoft.seleniumplacescraper.dao.ProxyDetailsDao#saveProxyPojo(com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo)}.
	 */
	@Test
	public void testSaveProxyPojo() {
		try {
			ProxyDetailsPojo proxyPojo = new ProxyDetailsPojo();
			proxyPojo.setIpAddress("111.111.111.111");
			proxyPojo.setIpPort("1111");
			proxyPojo.setIpAddressAndPort("111.111.111.111:1111");
			proxyPojo.setUrl("abc@def.com");

			ProxyDetailsPojo proxyPojoToSave = proxyDao.saveProxyPojo(proxyPojo);

			assertTrue(proxyPojoToSave.getIpAddress().equals("111.111.111.111"));
			assertTrue(proxyPojoToSave.getIpPort().equals("1111"));
			assertTrue(proxyPojoToSave.getIpAddressAndPort().equals("111.111.111.111:1111"));
			assertTrue(proxyPojoToSave.getUrl().equals("abc@def.com"));
		} catch (Exception e) {
			logger.log(Level.SEVERE,"ProxyDetailsDaoTest :: getSessionFactory() ::",e);
		}
	}

	/**
	 * Test method for {@link com.ainosoft.seleniumplacescraper.dao.ProxyDetailsDao#getValidProxyList()}.
	 */
	@Test
	public void testGetValidProxyList() {
		try {
			ProxyDetailsDao proxyDao = new ProxyDetailsDao();

			List<ProxyDetailsPojo> validProxyList = proxyDao.getValidProxyList();

			assertTrue(validProxyList!=null);
			assertTrue(!validProxyList.isEmpty());
			assertTrue(validProxyList.size() == 101);
		} catch (Exception e) {
			logger.log(Level.SEVERE,"ProxyDetailsDaoTest :: testUpdateProxyStatus() ::",e);
		}
	}

	/**
	 * Test method for {@link com.ainosoft.seleniumplacescraper.dao.ProxyDetailsDao#updateProxyStatus(java.util.ArrayList)}.
	 */
	@Test
	public void testUpdateProxyStatus() {
		try {
			ProxyDetailsPojo proxyPojo1 = new ProxyDetailsPojo();
			proxyPojo1.setIpAddress("111.111.111.111");
			proxyPojo1.setIpPort("1111");
			proxyPojo1.setIpAddressAndPort("111.111.111.111:1111");
			proxyPojo1.setUrl("abc@def.com");
			proxyPojo1.setStatus(true);
			ProxyDetailsPojo proxyPojoToSave1 = proxyDao.saveProxyPojo(proxyPojo1);

			ProxyDetailsPojo proxyPojo2 = new ProxyDetailsPojo();
			proxyPojo2.setIpAddress("222.222.222.222");
			proxyPojo2.setIpPort("2222");
			proxyPojo2.setIpAddressAndPort("222.222.222.222:2222");
			proxyPojo2.setUrl("abc@def.com");
			proxyPojo2.setStatus(true);
			ProxyDetailsPojo proxyPojoToSave2 = proxyDao.saveProxyPojo(proxyPojo2);

			ProxyDetailsPojo proxyPojo3 = new ProxyDetailsPojo();
			proxyPojo3.setIpAddress("333.333.333.333");
			proxyPojo3.setIpPort("3333");
			proxyPojo3.setIpAddressAndPort("333.333.333.333.:3333");
			proxyPojo3.setUrl("abc@def.com");
			proxyPojo3.setStatus(true);
			ProxyDetailsPojo proxyPojoToSave3 = proxyDao.saveProxyPojo(proxyPojo3);			

			ArrayList<ProxyDetailsPojo> proxyPojoList = new ArrayList<ProxyDetailsPojo>();
			proxyPojoList.add(proxyPojoToSave1);
			proxyPojoList.add(proxyPojoToSave2);
			proxyPojoList.add(proxyPojoToSave3);

			proxyDao.updateProxyStatus(proxyPojoList);			
		} catch (Exception e) {
			logger.log(Level.SEVERE,"ProxyDetailsDaoTest :: testUpdateProxyStatus() ::",e);
		}
	}
}