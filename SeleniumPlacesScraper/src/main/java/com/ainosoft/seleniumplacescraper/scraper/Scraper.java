package com.ainosoft.seleniumplacescraper.scraper;

import java.util.ArrayList;

import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;


/**
 * 
 * @author tushar@ainosoft.com
 *
 */
public interface Scraper {

	public void startScrapingFetchList();
	
	public ArrayList<ProxyDetailsPojo> startScrapingFetchProxyList();

	public void reRunScraping();

	public void setUrl(String url);

}
