package com.ainosoft.seleniumplacescraper.manager;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.SpaceInformationPojo;
import com.ainosoft.seleniumplacescraper.scraper.GoogleMapScraper;
import com.ainosoft.seleniumplacescraper.util.ProxyHolder;


/**
 * 
 * @author tushar@ainosoft.com
 * This class manages scraping and proxy management on runtime.
 */
public class ScraperManager implements Runnable,Manager{

	private String url;
	private SpaceInformationPojo spaceInfoPojo = null;
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public ScraperManager(String url,SpaceInformationPojo spaceInfoPojo){
		this.url = url;
		this.spaceInfoPojo = spaceInfoPojo;
	}

	@Override
	public void run() {
		try {
			if(spaceInfoPojo.getSpaceType()!=null && spaceInfoPojo.getSpaceCity()!=null){
				ProxyHolder proxyHolder = new ProxyHolder();
				proxyHolder.updateProxies();
				initializeAndStart(url,spaceInfoPojo.getSpaceType(),spaceInfoPojo.getSpaceCity(),proxyHolder);	
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
	public void initializeAndStart(String url,String textToSearch,String city, ProxyHolder proxyHolder){
		GoogleMapScraper googleMapScraper = null;
		try {
			googleMapScraper = new GoogleMapScraper();
			
			logger.log(Level.INFO,"Initializing scraping...");

			while (true) {
				googleMapScraper.setUrl(this.url);
				googleMapScraper.setProxyHolder(proxyHolder);
				googleMapScraper.setSpaceInfoPojo(spaceInfoPojo);
				
				googleMapScraper.reRunScraping();

				Thread.sleep(8000);
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE,"ScraperManager :: initializeAndStart() :: Exception :: ",e);
		}
	}
	
}
