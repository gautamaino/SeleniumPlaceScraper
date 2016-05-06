package com.ainosoft.seleniumplacescraper.scraper;

import java.util.ArrayList;

import com.ainosoft.seleniumplacescraper.pojo.PlacesDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;
import com.ainosoft.seleniumplacescraper.util.PageState;

/**
 * 
 * @author tushar@ainosoft.com
 *
 */
public interface Scraper {

	public ArrayList<ProxyDetailsPojo> startScrapingFetchProxyList();

	public ArrayList<PlacesDetailsPojo> startScrapingFetchList();

	public ArrayList<PlacesDetailsPojo> reRunScraping(PageState pageState);

	public void setUrl(String url);

	public void setTextToSearch(String textToScrape);

}