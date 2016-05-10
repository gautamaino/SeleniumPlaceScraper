package com.ainosoft.seleniumplacescraper.manager;

import java.util.ArrayList;
import java.util.List;

import com.ainosoft.seleniumplacescraper.dao.ProxyDetailsDao;
import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;
import com.ainosoft.seleniumplacescraper.scraper.ProxyScraper;
import com.ainosoft.seleniumplacescraper.util.ScraperLogger;

/**
 * @author nalanda@ainosoft.com
 * This is the Proxy Manager class which handle saving , retrieval and updation of proxy related 
 * methods
 */
public class ProxyManager implements Manager {

	@SuppressWarnings("unused")
	private static ScraperLogger scraperLogger = new ScraperLogger("ProxyManager");

	ProxyDetailsDao proxyDao = new ProxyDetailsDao();
	ProxyScraper proxyScraper = new ProxyScraper();

	/**
	 * This method accepts two parameters the site to scrape data from and the data to be scraped
	 */
	@Override
	public void initializeAndStart(String url, String textToSearch, String city) {
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
			ScraperLogger.log("ProxyManager :: initializeAndStart() ::",e); 
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
			ScraperLogger.log("ProxyManager :: getValidProxyList() ::",e); 
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
			ScraperLogger.log("ProxyManager :: updateProxyStatus() ::",e); 
		}
	}
}