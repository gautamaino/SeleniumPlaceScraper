package com.ainosoft.seleniumplacescraper.manager;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ainosoft.seleniumplacescraper.dao.PlacesDetailsDao;
import com.ainosoft.seleniumplacescraper.pojo.PlacesDetailsPojo;
import com.ainosoft.seleniumplacescraper.util.ScraperLogger;

/**
 * 
 * @author tushar@ainosoft.com
 * This class is use to save data into database.
 */
public class DataEntryMakerForPlacesDetailsPojo implements Runnable {

	private ArrayList<PlacesDetailsPojo> placesDetailsPojoList = null;
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public DataEntryMakerForPlacesDetailsPojo(ArrayList<PlacesDetailsPojo> placesDetailsPojoList){
		this.placesDetailsPojoList = placesDetailsPojoList;
	}
	
	@Override
	public void run() {
		PlacesDetailsDao placesDetailsDao = null;
		try {
			placesDetailsDao = new PlacesDetailsDao();
			
			if(placesDetailsPojoList!=null){
				if(!placesDetailsPojoList.isEmpty()){
					for (PlacesDetailsPojo placesDetailsPojo : placesDetailsPojoList) {
						placesDetailsDao.savePlacesDetailsPojo(placesDetailsPojo);
					}					
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,"DataEntryMakerForPlacesDetailsPojo :: run() :: Exception :: ",e);
		}
		
	}

}
