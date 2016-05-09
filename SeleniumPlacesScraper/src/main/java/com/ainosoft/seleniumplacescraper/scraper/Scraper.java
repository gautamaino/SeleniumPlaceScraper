package com.ainosoft.seleniumplacescraper.scraper;

import java.util.ArrayList;

import com.ainosoft.seleniumplacescraper.pojo.PlacesDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;

/**
 * 
 * @author tushar@ainosoft.com
 *
 */
public interface Scraper {

	public ArrayList<PlacesDetailsPojo> startScrapingFetchList();
	
	public ArrayList<ProxyDetailsPojo> startScrapingFetchProxyList();
	
	public ArrayList<PlacesDetailsPojo> reRunScraping(int pageCount);
	
	public void setUrl(String url);
	
	public void setTextToSearch(String textToScrape);

}
