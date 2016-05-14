package com.ainosoft.seleniumplacescraper.manager;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

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
	private int time = 3000;
	private ProxyDetailsPojo proxyDetailsPojo = null;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

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
	 */
	public void initializeAndStart(String url,String textToSearch,String city){
		GoogleMapScraper googleMapScraper = null;
		WebDriver fireFoxWebDriver = null;
		try {
			googleMapScraper = new GoogleMapScraper();

			logger.log(Level.INFO,"Initializating scraping...");

			fireFoxWebDriver = getFireFoxDriver();

			//fireFoxWebDriver.get("http://whatismyipaddress.com/");
			//Thread.sleep(5000);

			// Launch website
			fireFoxWebDriver.navigate().to(url);
			Thread.sleep(time);

			// Maximize the browser
			//fireFoxWebDriver.manage().window().maximize();

			// Enter value 50 in the second number of the percent Calculator
			try {
				WebElement searchTextBox = fireFoxWebDriver.findElement(By.xpath(".//*[@id='searchboxinput']"));

				searchTextBox.sendKeys(spaceInfoPojo.getSpaceType()+" "+"in"+" "+spaceInfoPojo.getSpaceCity());

				Thread.sleep(time);
			} catch (NoSuchElementException e) {
				initializeAndStart(url,textToSearch,city);
			}

			try {
				// Click search Button
				WebElement searchButton = fireFoxWebDriver.findElement(By.xpath(".//*[@id='searchbox']/div[1]/button"));

				searchButton.click();

				Thread.sleep(time);
			} catch (NoSuchElementException e) {
				initializeAndStart(url,textToSearch,city);
			}

			
			while (true) {
				googleMapScraper.setFireFoxWebDriver(fireFoxWebDriver);
				googleMapScraper.setCity(city);
				googleMapScraper.setTextToSearch(textToSearch);

				googleMapScraper.reRunScraping(spaceInfoPojo);

				Thread.sleep(time);
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE,"ScraperManager :: initializeAndStart() :: Exception :: ",e);
		}
	}


	/**
	 * This method is use to get new instance of WebDriver, along with new profile creation.
	 * @return WebDriver
	 */
	public WebDriver getFireFoxDriver(){
		WebDriver fireFoxWebDriver = null;	
		FirefoxProfile profile = null;
		try {
			//ProxyDetailsPojo proxyDetailsPojo = getValidProxy();

			String serverIP = proxyDetailsPojo.getIpAddress();
			Integer port = Integer.parseInt(proxyDetailsPojo.getIpPort());

			profile = new FirefoxProfile();
			profile.setPreference("network.proxy.type", 1);
			profile.setPreference("network.proxy.http", serverIP);
			profile.setPreference("network.proxy.http_port", port);

			fireFoxWebDriver = new FirefoxDriver(profile);
		} catch (Exception e) {
			logger.log(Level.SEVERE,"ScraperManager :: getFireFoxDriver() :: Exception :: ",e);
		}
		return fireFoxWebDriver;
	}


/*	*//**
	 * This method is use to fetch list of Proxies from database, after fetching values it checks whether the IP address is valid or not.
	 * If it is valid then it returns that valid ProxyDetialsPojo
	 * @return ProxyDetailsPojo
	 *//*
	public ProxyDetailsPojo getValidProxy(){
		ProxyManager proxyManger = null;
		try {
			proxyManger = new ProxyManager();

			ArrayList<ProxyDetailsPojo> proxyDetailPojoList = proxyManger.getValidProxyList();
			if(proxyDetailPojoList!=null){
				if(!proxyDetailPojoList.isEmpty()){
					for (ProxyDetailsPojo proxyDetailsPojo : proxyDetailPojoList) {
						ProxyDetailsPojo proxyPojo = new ProxyDetailsPojo();	
						proxyPojo = proxyDetailsPojo;
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,"ScraperManager :: getValidProxy() :: Exception :: ",e);
		}
		return proxyPojo;
	}*/


}
