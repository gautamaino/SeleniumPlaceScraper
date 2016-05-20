package com.ainosoft.seleniumplacescraper.scrapermanager;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.ainosoft.seleniumplacescraper.dao.SpaceInformationDao;
import com.ainosoft.seleniumplacescraper.pojo.SpaceInformationPojo;
import com.ainosoft.seleniumplacescraper.scraper.GoogleMapScraper;

/**
 * 
 * @author tushar@ainosoft.com
 *
 */
public class ScraperManagerTest {
	
	private String SERVER_IP = "91.121.194.6";
	private int PORT = 3128;
	//private FirefoxProfile profile = null;
	
	@Test
	public void testReRunScraping(){
		GoogleMapScraper googleMapScraper = null;
		SpaceInformationDao spaceInfoDao = null;
		SpaceInformationPojo spaceInformation = null;
		
		WebDriver fireFoxWebDriver = null;	
		FirefoxProfile profile = null;
		try {
			googleMapScraper = new GoogleMapScraper();
			spaceInfoDao = new SpaceInformationDao();
			
			profile = new FirefoxProfile();
			profile.setPreference("network.proxy.type", 1);
			profile.setPreference("network.proxy.http", SERVER_IP);
			profile.setPreference("network.proxy.http_port", PORT);
			
			fireFoxWebDriver = new FirefoxDriver(profile);
			googleMapScraper.setFireFoxWebDriver(fireFoxWebDriver);
			googleMapScraper.setUrl("https://www.google.co.in/maps");
			
			List<SpaceInformationPojo> spaceInfoList = spaceInfoDao.getAllSpaceInformationPojoList();
			if(spaceInfoList!=null){
				if(!spaceInfoList.isEmpty()){
					for (SpaceInformationPojo spaceInformationPojo : spaceInfoList) {
						spaceInformation = spaceInformationPojo;
					}
				}
			}
			googleMapScraper.setSpaceInfoPojo(spaceInformation);
			googleMapScraper.reRunScraping();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetValidProxy(){
		try {
			/*ScraperManager scraperManager = new ScraperManager();
			ProxyDetailsPojo proxyDetailsPojo = scraperManager.getValidProxy();
			
			assertTrue(proxyDetailsPojo.getIpAddress().equals("94.25.15.190"));*/
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
