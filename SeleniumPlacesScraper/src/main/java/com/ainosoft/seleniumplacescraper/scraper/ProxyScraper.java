package com.ainosoft.seleniumplacescraper.scraper;

import java.util.ArrayList;

import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;

/**
 * @author nalanda@ainosoft.com
 *
 */
public interface ProxyScraper {

	public void startProxyScraping();
	
	public ArrayList<ProxyDetailsPojo> getListOfProxies();
	
	public void getValidProxy();
}
