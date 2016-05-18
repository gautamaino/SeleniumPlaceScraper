package com.ainosoft.seleniumplacescraper.scraper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.ainosoft.seleniumplacescraper.dao.SpaceInformationDao;
import com.ainosoft.seleniumplacescraper.manager.DataEntryMakerForPlacesDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.PlacesDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.SpaceInformationPojo;
import com.ainosoft.seleniumplacescraper.util.ProxyHolder;


/**
 * @author tushar@ainosoft.com
 * This class is used to fetch and store data in database in fresh and re-run mode. 
 * Depending on situation the appropriate method gets called.
 */
public class GoogleMapScraper implements Scraper {

	private Logger logger = Logger.getLogger(this.getClass().getName());	

	private WebDriver fireFoxWebDriver = null;
	private SpaceInformationPojo spaceInfoPojo;
	private String url;



	/**
	 * This method is use to re-run the scraper when exceptions occurres. 
	 * Which takes parameter as pagecount and depending on the pagecount
	 * that page gets opened and scraping gets started.
	 */
	@Override
	public void reRunScraping() {
		SpaceInformationDao spaceInfoDao = null;
		int pCount = 0;
		try {
			spaceInfoDao = new SpaceInformationDao();

			ProxyHolder proxyHolder = new ProxyHolder();
			proxyHolder.updateProxies();

			fireFoxWebDriver = getFireFoxDriver(proxyHolder);

			fireFoxWebDriver.get("http://whatismyipaddress.com/");
			Thread.sleep(8000);

			// Launch website
			fireFoxWebDriver.navigate().to(url);
			Thread.sleep(8000);

			// Maximize the browser
			//fireFoxWebDriver.manage().window().maximize();

			// Enter value 50 in the second number of the percent Calculator
			try {
				WebElement searchTextBox = fireFoxWebDriver.findElement(By.xpath(".//*[@id='searchboxinput']"));
				searchTextBox.sendKeys(spaceInfoPojo.getSpaceType()+" "+"in"+" "+spaceInfoPojo.getSpaceCity());

				Thread.sleep(9000);
			} catch (NoSuchElementException e) {

			}

			try {
				// Click search Button
				WebElement searchButton = fireFoxWebDriver.findElement(By.xpath(".//*[@id='searchbox']/div[1]/button"));
				searchButton.click();

				Thread.sleep(9000);
			} catch (NoSuchElementException e) {

			}

			while(!(pCount == spaceInfoPojo.getPageCount())){
				try {
					pCount++;
					Thread.sleep(9000);

					WebElement nextButton = fireFoxWebDriver.findElement(By.xpath(".//*[@id='widget-pane-section-pagination-button-next']"));
					nextButton.click();

					Thread.sleep(9000);

				} catch (NoSuchElementException e) {
					pCount--;
					spaceInfoPojo.setPageCount(pCount);
					spaceInfoDao.updatePageCount(spaceInfoPojo);
					continue;
				}
			}

			startScrapingFetchList();

			Thread.sleep(12000);

			try {
				WebElement nextButton = fireFoxWebDriver.findElement(By.xpath(".//*[@id='widget-pane-section-pagination-button-next']"));
				nextButton.click();
				Thread.sleep(9000);
				pCount++;
			} catch (NoSuchElementException e) {
				spaceInfoPojo.setPageCount(pCount);
				spaceInfoDao.updatePageCount(spaceInfoPojo);
			}

			spaceInfoPojo.setPageCount(pCount);
			spaceInfoDao.updatePageCount(spaceInfoPojo);

			Thread.sleep(9000);

			fireFoxWebDriver.close();
		} catch (Exception e) {
			logger.log(Level.SEVERE,"GoogleMapScraper :: reRunScraping() :: Exception :: ",e);
		}
	}


	/**
	 * This is overridden method, which starts scraping and stores data into database. 
	 * This method will get called in fresh mode for first time.
	 * This method is also gets called from re-run method.
	 */
	@Override
	public void startScrapingFetchList() {
		ArrayList<PlacesDetailsPojo> placesDetailsPojoList = new ArrayList<PlacesDetailsPojo>();
		Thread dataEntryMakerForPlacesDetailsPojo = null;
		int size = 1;
		try {
			logger.log(Level.INFO,"Scraping started...");

			for (int i = 0 ; i < size ; ++i) {
				PlacesDetailsPojo placesDetailsPojo ;

				List<WebElement> elementList = null;
				try {
					Thread.sleep(9000);
					elementList = fireFoxWebDriver.findElements(By.xpath(".//*[@class='widget-pane-section-result']"));
					Thread.sleep(12000);
				} catch (NoSuchElementException e1) {

				}

				if(elementList!=null){
					if(!elementList.isEmpty()){

						Thread.sleep(9000);
						elementList.get(i).click();
						Thread.sleep(9000);

						size = elementList.size();

						placesDetailsPojo = getPlacesDetailsPojo();

						placesDetailsPojoList.add(placesDetailsPojo);

						Thread.sleep(9000);

						try {
							//returning back to the first page
							fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/button")).click();
							Thread.sleep(9000);
						} catch (NoSuchElementException e) {
							break;
						} 
					}else{
						String finalPage = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[2]/div[1]/div[2]/span[1]")).getText();
						if(finalPage.equals("Make sure your search is spelled correctly.")){
							Thread.currentThread().interrupt();
							logger.log(Level.INFO,"Scraping Successfully Completed...");
						}
					}
				}else{
					String finalPage = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[2]/div[1]/div[2]/span[1]")).getText();
					if(finalPage.equals("Make sure your search is spelled correctly.")){
						Thread.currentThread().interrupt();
						logger.log(Level.INFO,"Scraping Successfully Completed...");
					}
				}
			}

			dataEntryMakerForPlacesDetailsPojo = new Thread(new DataEntryMakerForPlacesDetailsPojo(placesDetailsPojoList));
			dataEntryMakerForPlacesDetailsPojo.start();

			Thread.sleep(9000);

			String finalPage = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[2]/div[1]/div[2]/span[1]")).getText();
			if(finalPage.equals("Make sure your search is spelled correctly.")){
				Thread.currentThread().interrupt();
				logger.log(Level.INFO,"Scraping Successfully Completed...");
			}
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,"GoogleMapScraper :: startScrapingFetchList() :: Exception :: ",e);
		}
	}




	/**
	 * This method is use to get new instance of WebDriver, along with new profile creation.
	 * @return WebDriver
	 */
	public WebDriver getFireFoxDriver(ProxyHolder proxyHolder){
		WebDriver fireFoxWebDriver = null;	
		FirefoxProfile profile = null;
		ProxyDetailsPojo proxyDetailsPojo;
		try {
			int proxyCount = spaceInfoPojo.getPageCount();
			while(true){

				if(proxyCount==proxyHolder.getAllProxyList().size()){
					proxyDetailsPojo = proxyHolder.getNewProxy(proxyCount--);	
				}else if(proxyCount<proxyHolder.getAllProxyList().size()){
					proxyDetailsPojo = proxyHolder.getNewProxy(proxyCount);	
				}else{
					proxyDetailsPojo = proxyHolder.getNewProxy(0);
				}

				String serverIP = proxyDetailsPojo.getIpAddress();
				Integer port = Integer.parseInt(proxyDetailsPojo.getIpPort());

				boolean result = checkForValidIp(serverIP,port);
				if(result){
					profile = new FirefoxProfile();
					profile.setPreference("network.proxy.type", 1);
					profile.setPreference("network.proxy.http", serverIP);
					profile.setPreference("network.proxy.http_port", port);

					fireFoxWebDriver = new FirefoxDriver(profile);
					break;
				}else{
					proxyCount++;
					getFireFoxDriver(proxyHolder);
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,"ScraperManager :: getFireFoxDriver() :: Exception :: ",e);
		}
		return fireFoxWebDriver;
	}


	/**
	 * This method is use to get new instance of WebDriver, along with new profile creation.
	 * @return WebDriver
	 */
	public boolean checkForValidIp(String ipAddress,int port){
		WebDriver fireFoxWebDriver = null;	
		FirefoxProfile profile = null;
		boolean result = false;
		try {
			profile = new FirefoxProfile();
			profile.setPreference("network.proxy.type", 1);
			profile.setPreference("network.proxy.http", ipAddress);
			profile.setPreference("network.proxy.http_port", port);

			fireFoxWebDriver = new FirefoxDriver(profile);

			Thread.sleep(9000);

			// Launch website
			fireFoxWebDriver.navigate().to("https://www.google.co.in/");
			Thread.sleep(9000);

			try {
				String googleName = fireFoxWebDriver.findElement(By.xpath(".//*[@class='logo-subtext']")).getText();
				Thread.sleep(9000);

				if(googleName.equals("India")){
					result = true;
				}else{
					result = false;
				}
			} catch (NoSuchElementException e) {
				logger.log(Level.SEVERE,"ScraperManager :: checkForValidIp() :: Google name not found... ",e);
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE,"ScraperManager :: getFireFoxDriver() :: Exception :: ",e);
		}finally{
			fireFoxWebDriver.close();
		}
		return result;
	}


	/**
	 * This method fetches data and creates new PlacesDetailsPojo and returns it to startScraping().
	 * @return PlacesDetailsPojo
	 */
	public PlacesDetailsPojo getPlacesDetailsPojo(){
		PlacesDetailsPojo placesDetailsPojo = null;
		try {
			placesDetailsPojo = new PlacesDetailsPojo();

			//fetching some values
			String name = null;
			try {
				name = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[1]/div[2]/div[1]/h1")).getText();
				//logger.log(Level.INFO,"Type : "+name);
				Thread.sleep(5000);
			} catch (NoSuchElementException e) {

			}

			String type = null;
			try {
				type = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[1]/div[2]/div[2]/div[2]/span/span[1]/button")).getText();
				//logger.log(Level.INFO,"Type : "+type);
				//Thread.sleep(3000);
			} catch (NoSuchElementException e) {
				//break;
			}

			String email = null;
			try {
				email = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[6]/div/span[2]/span[1]/span[2]/span/a[2]")).getText();
				//logger.log(Level.INFO,"Email : "+email);
				//Thread.sleep(3000);
			} catch (NoSuchElementException e) {

			}

			String address = null;
			try {
				address = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[5]/div/span[2]/span[1]/span[1]/span")).getText();
				//logger.log(Level.INFO,"Address : "+address);
				Thread.sleep(5000);
			} catch (NoSuchElementException e) {

			}

			Thread.sleep(8000);

			String phoneNumber = null;
			try {
				phoneNumber = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[6]/div/span[2]/span[1]/a")).getText();
				//logger.log(Level.INFO,"PhoneNumber : "+phoneNumber);
				//Thread.sleep(3000);
			} catch (NoSuchElementException e) {

			}

			String ratings = null;
			try {
				ratings = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[1]/div[2]/div[2]/div[1]/span[1]/span/span")).getText();
				//logger.log(Level.INFO,"Ratings : "+ratings);
				//Thread.sleep(3000);
			} catch (NoSuchElementException e) {

			}

			String timing = null;
			try {
				timing = fireFoxWebDriver.findElement(By.className("widget-pane-section-info-hour-text")).getText();
				//logger.log(Level.INFO,"Timing : "+timing);
				Thread.sleep(3000);
			} catch (NoSuchElementException e) {

			}

			String imageURL = null;
			try {
				imageURL = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[1]/button[1]/img")).getAttribute("src");
				//logger.log(Level.INFO,"ImageURL : "+imageURL);
				//Thread.sleep(3000);
			} catch (NoSuchElementException e) {

			}

			String diveWholeWeb = null;
			try {
				WebElement divWeb = fireFoxWebDriver.findElement(By.xpath(".//*[@class='widget-pane widget-pane-visible']"));
				diveWholeWeb = divWeb.getAttribute("innerHTML");
				Thread.sleep(3000);
			} catch (NoSuchElementException e) {

			}

			Thread.sleep(9000);

			String url = fireFoxWebDriver.getCurrentUrl();


			StringBuffer latitude = new StringBuffer();
			StringBuffer longitude = new StringBuffer();

			try {
				int b = url.indexOf("@");
				int c = url.indexOf(",");
				int d = url.lastIndexOf(",");

				if(b!=0 && c!=0){
					if(url.substring(b+1,c)!=null){
						latitude.append(url.substring(b+1,c));	
					}
				}

				if(c!=0 && d!=0){
					if(url.substring(c+1, d)!=null){
						longitude.append(url.substring(c+1, d));	
					}
				}
			} catch (Exception e) {
				if(e.equals("StringIndexOutOfBoundException")){
					//break;	
				}
			}

			placesDetailsPojo.setPlaceName(name);
			placesDetailsPojo.setPlaceType(type);

			if(spaceInfoPojo.getSpaceType()!=null){
				placesDetailsPojo.setPlaceCity(spaceInfoPojo.getSpaceType());				
			}

			if(spaceInfoPojo.getSpaceCity()!=null){
				placesDetailsPojo.setPlaceCity(spaceInfoPojo.getSpaceCity());				
			}

			placesDetailsPojo.setPlaceAddress(address);
			placesDetailsPojo.setPlacePhoneNo(phoneNumber);
			placesDetailsPojo.setPlaceUrl(url);
			placesDetailsPojo.setPlaceWebsite(email);

			if(latitude.toString()!=null){
				placesDetailsPojo.setLatitude(latitude.toString());	
			}

			if(longitude.toString()!=null){
				placesDetailsPojo.setLongitude(longitude.toString());	
			}

			placesDetailsPojo.setRating(ratings);
			placesDetailsPojo.setTimings(timing);
			placesDetailsPojo.setWebElement(diveWholeWeb);
			placesDetailsPojo.setImage(imageURL);

			Thread.sleep(9000);

		} catch (Exception e) {
			logger.log(Level.SEVERE,"GoogleMapScraper :: getPlacesDetailsPojo() :: Exception :: ",e);
		}
		return placesDetailsPojo;
	}


	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public ArrayList<ProxyDetailsPojo> startScrapingFetchProxyList() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSpaceInfoPojo(SpaceInformationPojo spaceInfoPojo) {
		this.spaceInfoPojo = spaceInfoPojo;
	}

	public void setFireFoxWebDriver(WebDriver fireFoxWebDriver) {
		this.fireFoxWebDriver = fireFoxWebDriver;
	}

}