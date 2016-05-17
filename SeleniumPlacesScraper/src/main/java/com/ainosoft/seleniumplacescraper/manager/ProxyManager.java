package com.ainosoft.seleniumplacescraper.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ainosoft.seleniumplacescraper.dao.ProxyDetailsDao;
import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;
import com.ainosoft.seleniumplacescraper.scraper.ProxyScraper;
import com.ainosoft.seleniumplacescraper.util.ProxyHolder;

/**
 * @author nalanda@ainosoft.com
 * This is the Proxy Manager class which handle saving , retrieval and updation of proxy related 
 * methods
 */
public class ProxyManager implements Manager {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	ProxyDetailsDao proxyDao = new ProxyDetailsDao();
	ProxyScraper proxyScraper = new ProxyScraper();

	/**
	 * This method accepts two parameters the site to scrape data from and the data to be scraped
	 */
	@Override
	public void initializeAndStart(String url, String textToSearch, String city,ProxyHolder proxyHolder) {
		try {
			proxyScraper.setUrl(url);

			ArrayList<ProxyDetailsPojo> proxyPojoList = proxyScraper.startScrapingFetchProxyList();
			if(proxyPojoList!=null){
				if(!proxyPojoList.isEmpty()){
					Thread dataEntrymaker = new Thread(new DataEntryMaker(proxyPojoList));
					dataEntrymaker.start();
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,"ProxyManager :: initializeAndStart() ::",e); 
		}
	}

	/**
	 * This method returns list of valid proxies from database
	 * @return
	 */
	public ArrayList<ProxyDetailsPojo> getValidProxyList(){
		ArrayList<ProxyDetailsPojo> proxyList = null;
		try {
			proxyList = new ArrayList<ProxyDetailsPojo>();
			List<ProxyDetailsPojo> validProxyList = proxyDao.getValidProxyList();
			for(ProxyDetailsPojo proxyPojo : validProxyList){

				proxyList.add(proxyPojo);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,"ProxyManager :: getValidProxyList() ::",e); 
		}
		return proxyList;
	}

	/**
	 * This method updates the status of proxy if it is found to be invalid i.e not reachable
	 * @param proxyPojoList
	 */
	public void updateProxyStatus(ArrayList<ProxyDetailsPojo> proxyPojoList){ 
		try {
			proxyDao.updateProxyStatus(proxyPojoList);
		} catch (Exception e) {
			logger.log(Level.SEVERE,"ProxyManager :: updateProxyStatus() ::",e); 
		}
	}
}