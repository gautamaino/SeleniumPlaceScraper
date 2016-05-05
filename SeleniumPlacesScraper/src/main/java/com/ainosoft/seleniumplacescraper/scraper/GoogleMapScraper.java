package com.ainosoft.seleniumplacescraper.scraper;

import java.util.ArrayList;

import com.ainosoft.seleniumplacescraper.pojo.PlacesDetailsPojo;
import com.ainosoft.seleniumplacescraper.util.PageState;

/**
 * 
 * @author tushar@ainosoft.com
 *
 */
public class GoogleMapScraper implements Scraper {
	
	private String url,searchText;

	@Override
	public ArrayList<PlacesDetailsPojo> startScrapingFetchList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void setTextToSearch(String textToScrape) {
		this.searchText = textToScrape;
	}

	@Override
	public ArrayList<PlacesDetailsPojo> reRunScraping(PageState pageState) {
		// TODO Auto-generated method stub
		return null;
	}

}