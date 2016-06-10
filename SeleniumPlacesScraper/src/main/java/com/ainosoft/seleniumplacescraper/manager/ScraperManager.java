package com.ainosoft.seleniumplacescraper.manager;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.SpaceInformationPojo;
import com.ainosoft.seleniumplacescraper.scraper.GoogleMapScraper;


/**
 * 
 * @author tushar@ainosoft.com
 * This class manages scraping and proxy management.
 */
public class ScraperManager implements Runnable,Manager{

	private String url;
	private SpaceInformationPojo spaceInfoPojo = null;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private ProxyDetailsPojo proxyDetailsPojo;
	private ArrayList<ProxyDetailsPojo> proxyDetailsPojoList;
	private int timer = 15000;
	
	public ScraperManager(String url,SpaceInformationPojo spaceInfoPojo,ProxyDetailsPojo proxyDetailsPojo,ArrayList<ProxyDetailsPojo> proxyDetailsPojoList){
		this.url = url;
		this.spaceInfoPojo = spaceInfoPojo;
		this.proxyDetailsPojo = proxyDetailsPojo;
		this.proxyDetailsPojoList = proxyDetailsPojoList;
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
				googleMapScraper.setProxyDetailsPojoList(proxyDetailsPojoList);
				
				Thread.sleep(timer);
				
				googleMapScraper.reRunScraping();

				if(googleMapScraper.isEndOfScraperFlag()){
					break;
				}
				
				Thread.sleep(timer);
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE,"ScraperManager :: initializeAndStart() :: Exception :: ",e);
		}
	}
	
}
