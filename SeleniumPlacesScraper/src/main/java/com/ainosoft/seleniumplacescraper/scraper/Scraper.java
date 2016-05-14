package com.ainosoft.seleniumplacescraper.scraper;

import java.util.ArrayList;

import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.SpaceInformationPojo;


/**
 * 
 * @author tushar@ainosoft.com
 *
 */
public interface Scraper {

	public void startScrapingFetchList();
	
	public ArrayList<ProxyDetailsPojo> startScrapingFetchProxyList();

	public void reRunScraping(SpaceInformationPojo spaceInfoPojo);

	public void setUrl(String url);

	public void setTextToSearch(String textToScrape);
}
