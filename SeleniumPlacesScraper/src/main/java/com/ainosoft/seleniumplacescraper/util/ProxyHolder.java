package com.ainosoft.seleniumplacescraper.util;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ainosoft.seleniumplacescraper.dao.ProxyDetailsDao;
import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;

/**
 * This class is just used to retrieve proxies from database, hold them and start the proxypool thread periodically in background.
 * @author tushar@ainosoft.com
 *
 */
public class ProxyHolder {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private ArrayList<ProxyDetailsPojo> allProxyList = new ArrayList<ProxyDetailsPojo>();

	/**
	 * This will fetch list of proxies from database and will run one thread in background which will 
	 * check periodically whether that proxies are working or not.
	 */
	public void updateProxies() {
		ProxyDetailsDao proxyDetailsDao = null;
		try {
			proxyDetailsDao = new ProxyDetailsDao();

			allProxyList = (ArrayList<ProxyDetailsPojo>) proxyDetailsDao.getValidProxyList();
			
			ScheduledExecutorService threadSchedulerService = Executors.newScheduledThreadPool(1);
			threadSchedulerService.scheduleAtFixedRate(new ProxyPool(allProxyList), 0, 15, TimeUnit.MINUTES);
			
			Thread.sleep(8000);

		} catch (Exception e) {
			logger.log(Level.SEVERE,"ProxyHolder :: updateProxies() ::",e);
		}
	}
	
	/**
	 * This will return the ProxyDetailsPojo based on proxyCount.
	 * @param proxyCount
	 * @return ProxyDetailsPojo
	 */
	public ProxyDetailsPojo getNewProxy(int proxyCount){
		ProxyDetailsPojo proxyDetailsPojo = null;
		try {
			proxyDetailsPojo = allProxyList.get(proxyCount);
		} catch (Exception e) {
			logger.log(Level.SEVERE,"ProxyHolder :: getNewProxy() ::",e);
		}
		return proxyDetailsPojo;
	}

	public ArrayList<ProxyDetailsPojo> getAllProxyList() {
		return allProxyList;
	}

}
