package com.ainosoft.seleniumplacescraper.entrypoint;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ainosoft.seleniumplacescraper.dao.SpaceInformationDao;
import com.ainosoft.seleniumplacescraper.manager.ScraperManager;
import com.ainosoft.seleniumplacescraper.pojo.SpaceInformationPojo;

/**
 * @author tushar@ainosoft.com
 * This is the main entry point class to start scraping
 * It has one method initializeAndStart() which is used for
 * 1. Scraping proxy details 
 * 2. Scraping actual data
 */
public class ScraperEntryPoint {

	private static Logger logger = Logger.getLogger(ScraperEntryPoint.class.getName());

	public static void main(String[] args) {
		try {
			SpaceInformationDao spaceInfoDao = new SpaceInformationDao();

			//ProxyManager proxyManager = new ProxyManager();
			//proxyManager.initializeAndStart("http://www.ip-adress.com/proxy_list/", null, null);			

			List<SpaceInformationPojo> spaceInfoList = spaceInfoDao.getAllSpaceInformationPojoList();
			for (SpaceInformationPojo spaceInformationPojo : spaceInfoList) {
				Thread scraperManager = new Thread(new ScraperManager("https://www.google.co.in/maps", spaceInformationPojo));
				scraperManager.start();
			}
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,"ScraperEntryPoint :: main() :: Exception :: ",e);
		}
	}

}