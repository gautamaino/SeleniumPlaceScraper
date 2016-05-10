package com.ainosoft.seleniumplacescraper.entrypoint;

import com.ainosoft.seleniumplacescraper.manager.ScraperManager;
import com.ainosoft.seleniumplacescraper.util.ScraperLogger;

/**
 * @author tushar@ainosoft.com
 * This is the main entry point class to start scraping
 * It has one method initializeAndStart() which is used for
 * 1. Scraping proxy details 
 * 2. Scraping actual data
 */
public class ScraperEntryPoint {

	private static ScraperLogger scraperLogger = new ScraperLogger("googlemaps");

	public static void main(String[] args) {
		try {
			ScraperManager scraperManager = new ScraperManager();
			scraperManager.initializeAndStart("https://www.google.co.in/maps", "restaurants","Pune");

			/*ProxyManager proxyManager = new ProxyManager();
			proxyManager.initializeAndStart("http://www.ip-adress.com/proxy_list/", null);*/			
		} catch (Exception e) {
			scraperLogger.log("ScraperEntryPoint :: main() :: Exception :: ",e);
		}
	}
}