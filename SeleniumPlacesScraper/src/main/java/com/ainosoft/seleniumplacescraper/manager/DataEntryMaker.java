/**
 * 
 */
package com.ainosoft.seleniumplacescraper.manager;

import java.util.ArrayList;

import com.ainosoft.seleniumplacescraper.dao.ProxyDetailsDao;
import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;
import com.ainosoft.seleniumplacescraper.util.ScraperLogger;

/**
 * @author nalanda
 *
 */
public class DataEntryMaker implements Runnable {

	ArrayList<ProxyDetailsPojo> proxyPojoList;

	@SuppressWarnings("unused")
	private static ScraperLogger scraperLogger = new ScraperLogger("DataEntryMaker");

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
			ScraperLogger.log("DataEntryMaker :: run() ::",e); 
		}
	}
}