/**
 * 
 */
package com.ainosoft.seleniumplacescraper.manager;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ainosoft.seleniumplacescraper.dao.ProxyDetailsDao;
import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;

/**
 * @author nalanda
 *
 */
public class DataEntryMaker implements Runnable {

	ArrayList<ProxyDetailsPojo> proxyPojoList;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	public DataEntryMaker(ArrayList<ProxyDetailsPojo> proxyPojoList) {
		this.proxyPojoList = proxyPojoList;
	}

	/**
	 * This is a data entry maker class which creates another thread for writing data to database
	 * while reading data from the site 
	 */
	@Override
	public void run() {
		try {
			ProxyDetailsDao proxyDao = new ProxyDetailsDao();
			if(proxyPojoList!=null){
				if(!proxyPojoList.isEmpty()){
					for(ProxyDetailsPojo proxyPojo : proxyPojoList){
						proxyDao.saveProxyPojo(proxyPojo);
					}
					//proxyDao.saveProxyListToDatabase(proxyPojoList);
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,"DataEntryMaker :: run() ::",e); 
		}
	}
}