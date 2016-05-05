package com.ainosoft.seleniumplacescraper.scraper;

import java.util.ArrayList;

import com.ainosoft.seleniumplacescraper.pojo.PlacesDetailsPojo;

/**
 * 
 * @author tushar@ainosoft.com
 *
 */
public class GoogleMapScraper implements Scraper {
	
	String url,searchText;

	public ArrayList<PlacesDetailsPojo> getScrapperDataList() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setUrl(String url) {
		// TODO Auto-generated method stub
		this.url = url;
	}

	public void setTextToSearch(String textToSearch) {
		// TODO Auto-generated method stub
		searchText = textToSearch;
	}
}