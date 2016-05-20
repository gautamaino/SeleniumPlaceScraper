package com.ainosoft.seleniumplacescraper.manager;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.SpaceInformationPojo;
import com.ainosoft.seleniumplacescraper.scraper.GoogleMapScraper;


/**
 * 
 * @author tushar@ainosoft.com
 * This class manages scraping and proxy management on runtime.
 */
public class ScraperManager implements Runnable,Manager{

	private String url;
	private SpaceInformationPojo spaceInfoPojo = null;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private ProxyDetailsPojo proxyDetailsPojo;
	
	public ScraperManager(String url,SpaceInformationPojo spaceInfoPojo,ProxyDetailsPojo proxyDetailsPojo){
		this.url = url;
		this.spaceInfoPojo = spaceInfoPojo;
		this.proxyDetailsPojo = proxyDetailsPojo;
	}

	@Override
	public void run() {
		try {
			if(spaceInfoPojo.getSpaceType()!=null && spaceInfoPojo.getSpaceCity()!=null){
				initializeAndStart(url,spaceInfoPojo.getSpaceType(),spaceInfoPojo.getSpaceCity());	
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE,"ScraperManager :: run() :: Exception :: ",e);
		}
	}

	/**
	 * This method takes parameter as url, textToSearch, and city to scrape the data.
	 * This method is use to initialize and start the scraping in fresh mode if the pagecount is less than 1.
	 * @param proxyHolder 
	 */
	public void initializeAndStart(String url, String textToSearch, String city){
		GoogleMapScraper googleMapScraper = null;
		try {
			googleMapScraper = new GoogleMapScraper();
			
			logger.log(Level.INFO,"Initializing scraping...");

			while (true) {
				googleMapScraper.setUrl(this.url);
				googleMapScraper.setSpaceInfoPojo(spaceInfoPojo);
				googleMapScraper.setProxyDetailsPojo(proxyDetailsPojo);
				
				Thread.sleep(9000);
				
				googleMapScraper.reRunScraping();

				Thread.sleep(9000);
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE,"ScraperManager :: initializeAndStart() :: Exception :: ",e);
		}
	}
	
}
