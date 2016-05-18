package com.ainosoft.seleniumplacescraper.manager;


/**
 * @author nalanda@ainosoft.com
 * This is Manager interface for scraping data 
 * It has only one method which id used to initialize and start scraping
 */
public interface Manager{

	/**
	 * This method starts the scraper by taking to parameters
	 * @param url: the site from which we have to scraper data
	 * @param textToSearch: Text related to a particular topic for searching on the site provided 
	 *                      via URL
	 */
	public void initializeAndStart(String url,String textToSearch,String city);
}