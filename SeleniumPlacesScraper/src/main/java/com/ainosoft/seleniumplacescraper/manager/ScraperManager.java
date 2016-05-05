package com.ainosoft.seleniumplacescraper.manager;

import java.util.ArrayList;

import com.ainosoft.seleniumplacescraper.dao.PlacesDetailsDao;
import com.ainosoft.seleniumplacescraper.pojo.PlacesDetailsPojo;
import com.ainosoft.seleniumplacescraper.scraper.GoogleMapScraper;


/**
 * 
 * @author tushar@ainosoft.com
 *
 */
public class ScraperManager implements Manager{
	
	public void initializeAndStart(String url,String textToSearch){
		GoogleMapScraper googleMapScraper = null;
		PlacesDetailsDao placesDetailsDao = null;
		try {
			googleMapScraper = new GoogleMapScraper();
			placesDetailsDao = new PlacesDetailsDao();
			
			googleMapScraper.setTextToSearch(textToSearch);
			googleMapScraper.setUrl(url);
			
			
			ArrayList<PlacesDetailsPojo> placesDetailsPojoList = googleMapScraper.startScrapingFetchList();
			for (PlacesDetailsPojo placesDetailsPojo : placesDetailsPojoList) {
				placesDetailsDao.savePlacesDetailsPojo(placesDetailsPojo);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
