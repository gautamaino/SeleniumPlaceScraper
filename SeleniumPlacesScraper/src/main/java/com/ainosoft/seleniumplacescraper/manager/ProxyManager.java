package com.ainosoft.seleniumplacescraper.manager;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;
import com.ainosoft.seleniumplacescraper.scraper.ProxyScraper;

/**
 * @author nalanda@ainosoft.com
 *
 */
public class ProxyManager implements ProxyScraper {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void startProxyScraping() {
		try {
			ProxyScraperHelper proxyHelper = new ProxyScraperHelper();
			
			proxyHelper.startProxyScraping();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "ProxyManager :: startProxyScraping() ::",e);
		}
	}

	@Override
	public ArrayList<ProxyDetailsPojo> getListOfProxies() {
		try {

		} catch (Exception e) {
			logger.log(Level.SEVERE, "ProxyManager :: startProxyScraping() ::",e);
		}
		return null;
	}

	@Override
	public void getValidProxy() {
		try {

		} catch (Exception e) {
			logger.log(Level.SEVERE, "ProxyManager :: startProxyScraping() ::",e);
		}
	}
}