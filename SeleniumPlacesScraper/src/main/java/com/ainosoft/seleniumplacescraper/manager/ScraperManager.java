package com.ainosoft.seleniumplacescraper.manager;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.ainosoft.seleniumplacescraper.dao.PlacesDetailsDao;
import com.ainosoft.seleniumplacescraper.pojo.PlacesDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;
import com.ainosoft.seleniumplacescraper.scraper.GoogleMapScraper;
import com.ainosoft.seleniumplacescraper.scraper.ProxyScraper;
import com.ainosoft.seleniumplacescraper.util.ScraperLogger;


/**
 * 
 * @author tushar@ainosoft.com
 *
 */
public class ScraperManager implements Manager{

	private ScraperLogger scraperLogger = new ScraperLogger("googlemaps");

	private static int pageCount = 0;
	private boolean pageValidation = false;

	public void initializeAndStart(String url,String textToSearch){
		GoogleMapScraper googleMapScraper = null;
		PlacesDetailsDao placesDetailsDao = null;
		WebDriver fireFoxWebDriver = null;
		try {
			googleMapScraper = new GoogleMapScraper();
			placesDetailsDao = new PlacesDetailsDao();

			scraperLogger.log("-------------------------------------------------------------------------------");
			scraperLogger.log("Initializating scraping...");
			scraperLogger.log("-------------------------------------------------------------------------------");

			fireFoxWebDriver = getFireFoxDriver();

			//fireFoxWebDriver.get("http://whatismyipaddress.com/");
			//Thread.sleep(5000);

			// Launch website
			fireFoxWebDriver.navigate().to(url);
			//Thread.sleep(5000);

			// Maximize the browser
			//fireFoxWebDriver.manage().window().maximize();

			// Enter value 50 in the second number of the percent Calculator
			WebElement searchTextBox = fireFoxWebDriver.findElement(By.xpath(".//*[@id='searchboxinput']"));

			searchTextBox.sendKeys(textToSearch);

			Thread.sleep(5000);

			// Click search Button
			WebElement searchButton = fireFoxWebDriver.findElement(By.xpath(".//*[@id='searchbox']/div[1]/button"));

			searchButton.click();

			Thread.sleep(5000);

			boolean flag = false;
			while (true) {
				googleMapScraper.setFireFoxWebDriver(fireFoxWebDriver);

				if(flag){
					try {
						WebElement nextButton = fireFoxWebDriver.findElement(By.xpath(".//*[@id='widget-pane-section-pagination-button-next']"));
						nextButton.click();
						pageCount++;											
					} catch (Exception e) {
						if(e.equals("NoSuchElementException")){
							WebDriver fireFoxDriverForReRun = getFireFoxDriver();
							googleMapScraper.setFireFoxWebDriver(fireFoxDriverForReRun);
							
							ArrayList<PlacesDetailsPojo> placesDetailsPojoList1 = googleMapScraper.reRunScraping(pageCount);
							for (PlacesDetailsPojo placesDetailsPojo : placesDetailsPojoList1) {
								placesDetailsDao.savePlacesDetailsPojo(placesDetailsPojo);
							}
							flag = true;
							continue;
						}
					}
				}

				ArrayList<PlacesDetailsPojo> placesDetailsPojoList = googleMapScraper.startScrapingFetchList();
				for (PlacesDetailsPojo placesDetailsPojo : placesDetailsPojoList) {
					placesDetailsDao.savePlacesDetailsPojo(placesDetailsPojo);
				}									

				try {	
					flag = false;
					WebElement nextButton = fireFoxWebDriver.findElement(By.xpath(".//*[@id='widget-pane-section-pagination-button-next']"));
					nextButton.click();
					pageCount++;
				} catch (Exception e) {
					if(e.equals("NoSuchElementException")){
						WebDriver fireFoxDriverForReRun = getFireFoxDriver();
						googleMapScraper.setFireFoxWebDriver(fireFoxDriverForReRun);

						ArrayList<PlacesDetailsPojo> placesDetailsPojoList1 = googleMapScraper.reRunScraping(pageCount);
						for (PlacesDetailsPojo placesDetailsPojo : placesDetailsPojoList1) {
							placesDetailsDao.savePlacesDetailsPojo(placesDetailsPojo);
						}
						flag = true;
						continue;
					}
				}

				Thread.sleep(80000);
			}

		} catch (Exception e) {
			scraperLogger.log("ScraperManager :: initializeAndStart() :: Exception :: ",e);
		}
	}

	
	/**
	 * This method is use to get new instance of WebDriver.
	 * @return WebDriver
	 */
	public WebDriver getFireFoxDriver(){
		WebDriver fireFoxWebDriver = null;	
		FirefoxProfile profile = null;
		try {
			ProxyDetailsPojo proxyDetailsPojo = getValidProxy();

			String serverIP = proxyDetailsPojo.getIpAddress();
			Integer port = Integer.parseInt(proxyDetailsPojo.getIpPort());

			profile = new FirefoxProfile();
			profile.setPreference("network.proxy.type", 1);
			profile.setPreference("network.proxy.http", serverIP);
			profile.setPreference("network.proxy.http_port", port);

			fireFoxWebDriver = new FirefoxDriver(profile);
		} catch (Exception e) {
			scraperLogger.log("ScraperManager :: getFireFoxDriver() :: Exception :: ",e);
		}
		return fireFoxWebDriver;
	}


	/**
	 * This method is use to fetch list of Proxies from database, after fetching values it checks whether the IP address is valid or not.
	 * If it is valid then it returns that valid ProxyDetialsPojo
	 * @return ProxyDetailsPojo
	 */
	public ProxyDetailsPojo getValidProxy(){
		ProxyScraper proxyScraper = null;
		ProxyDetailsPojo proxyPojo = null;
		try {
			proxyScraper = new ProxyScraper();
			
			proxyScraper.setUrl("http://www.ip-adress.com/proxy_list/");
			ArrayList<ProxyDetailsPojo> proxyDetailPojoList = proxyScraper.startScrapingFetchProxyList();
			if(proxyDetailPojoList!=null){
				if(!proxyDetailPojoList.isEmpty()){
					for (ProxyDetailsPojo proxyDetailsPojo : proxyDetailPojoList) {
						String serverIP = proxyDetailsPojo.getIpAddress();
						Integer port = Integer.parseInt(proxyDetailsPojo.getIpPort());

						boolean result = proxyScraper.checkForValidIp(serverIP);
						if(result){
							proxyPojo = proxyDetailsPojo;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			scraperLogger.log("ScraperManager :: getValidProxy() :: Exception :: ",e);
		}
		return proxyPojo;
	}
}
