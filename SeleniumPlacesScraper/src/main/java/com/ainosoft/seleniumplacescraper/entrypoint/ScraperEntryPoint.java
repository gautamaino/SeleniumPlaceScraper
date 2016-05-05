package com.ainosoft.seleniumplacescraper.entrypoint;

import com.ainosoft.seleniumplacescraper.manager.ScraperManager;

/**
 * 
 * @author tushar@ainosoft.com
 *
 */
public class ScraperEntryPoint {

	public static void main(String[] args) {
		try {
			ScraperManager scraperManager = new ScraperManager();
			scraperManager.initializeAndStart("https://www.google.co.in/maps", "restaurants in pune");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
