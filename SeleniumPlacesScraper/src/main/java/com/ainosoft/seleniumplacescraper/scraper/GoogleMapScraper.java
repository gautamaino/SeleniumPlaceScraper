package com.ainosoft.seleniumplacescraper.scraper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.ainosoft.seleniumplacescraper.dao.SpaceInformationDao;
import com.ainosoft.seleniumplacescraper.manager.DataEntryMakerForPlacesDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.PlacesDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.SpaceInformationPojo;
import com.ainosoft.seleniumplacescraper.util.ScraperLogger;


/**
 * 
 * @author tushar@ainosoft.com
 * This class is used to fetch and store data in database in fresh and re-run mode. 
 * Depending on situation the appropriate method gets called.
 */
public class GoogleMapScraper implements Scraper {

	private String url,searchText;
	private WebDriver fireFoxWebDriver = null;
	private	int time = 3000;
	private String city;

	private Logger logger = Logger.getLogger(this.getClass().getName());

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

				Thread.sleep(8000);
				List<WebElement> elementList = fireFoxWebDriver.findElements(By.xpath(".//*[@class='widget-pane-section-result']"));
				Thread.sleep(time);

				if(elementList!=null){
					if(!elementList.isEmpty()){

						Thread.sleep(time);
						elementList.get(i).click();
						Thread.sleep(time);

						size = elementList.size();


						placesDetailsPojo = getPlacesDetailsPojo();

						placesDetailsPojoList.add(placesDetailsPojo);

						Thread.sleep(time);

						try {
							//returning back to the first page
							fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/button")).click();
							Thread.sleep(time);
						} catch (NoSuchElementException e) {
							break;
						} 
					}
				}
			}
			
			dataEntryMakerForPlacesDetailsPojo = new Thread(new DataEntryMakerForPlacesDetailsPojo(placesDetailsPojoList));
			dataEntryMakerForPlacesDetailsPojo.start();

			Thread.sleep(time);

		} catch (Exception e) {

			logger.log(Level.SEVERE,"GoogleMapScraper :: startScrapingFetchList() :: Exception :: ",e);
		}
	}


	/**
	 * This method is use to re-run the scraper when exceptions occurres. 
	 * Which takes parameter as pagecount and depending on the pagecount
	 * that page gets opened and scraping gets started.
	 */
	@Override
	public void reRunScraping(SpaceInformationPojo spaceInfoPojo) {
		int pCount = 0;
		SpaceInformationDao spaceInfoDao = null;
		try {
			spaceInfoDao = new SpaceInformationDao();

			while(!(pCount == spaceInfoPojo.getPageCount())){
				try {
					pCount++;
					Thread.sleep(time);

					WebElement nextButton = fireFoxWebDriver.findElement(By.xpath(".//*[@id='widget-pane-section-pagination-button-next']"));
					nextButton.click();
					
					Thread.sleep(time);

				} catch (NoSuchElementException e) {
						pCount--;
						spaceInfoPojo.setPageCount(pCount);
						spaceInfoDao.updatePageCount(spaceInfoPojo);
						continue;
				}
			}

			startScrapingFetchList();

			Thread.sleep(time);

			try {
				WebElement nextButton = fireFoxWebDriver.findElement(By.xpath(".//*[@id='widget-pane-section-pagination-button-next']"));
				nextButton.click();
				Thread.sleep(time);
				pCount++;
			} catch (NoSuchElementException e) {
				spaceInfoPojo.setPageCount(pCount);
				spaceInfoDao.updatePageCount(spaceInfoPojo);
			}
			
			spaceInfoPojo.setPageCount(pCount);
			spaceInfoDao.updatePageCount(spaceInfoPojo);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,"GoogleMapScraper :: reRunScraping() :: Exception :: ",e);
		}
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
				//Thread.sleep(3000);
			} catch (NoSuchElementException e) {
				
			}

			String type = null;
			try {
				type = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[1]/div[2]/div[2]/div[2]/span/span[1]/button")).getText();
				//Thread.sleep(3000);
			} catch (NoSuchElementException e) {
					//break;
			}

			String email = null;
			try {
				email = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[6]/div/span[2]/span[1]/span[2]/span/a[2]")).getText();
				//Thread.sleep(3000);
			} catch (NoSuchElementException e) {
				
			}

			String address = null;
			try {
				address = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[5]/div/span[2]/span[1]/span[1]/span")).getText();
				//Thread.sleep(3000);
			} catch (NoSuchElementException e) {

			}

			Thread.sleep(time);

			String phoneNumber = null;
			try {
				phoneNumber = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[6]/div/span[2]/span[1]/a")).getText();
				//Thread.sleep(3000);
			} catch (NoSuchElementException e) {

			}

			String ratings = null;
			try {
				ratings = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[1]/div[2]/div[2]/div[1]/span[1]/span/span")).getText();
				//Thread.sleep(3000);
			} catch (NoSuchElementException e) {

			}

			String timing = null;
			try {
				timing = fireFoxWebDriver.findElement(By.className("widget-pane-section-info-hour-text")).getText();
				//Thread.sleep(3000);
			} catch (NoSuchElementException e) {

			}

			String imageURL = null;
			try {
				imageURL = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[1]/button[1]/img")).getAttribute("src");
				//Thread.sleep(3000);
			} catch (NoSuchElementException e) {

			}

			String diveWholeWeb = null;
			try {
				WebElement divWeb = fireFoxWebDriver.findElement(By.xpath(".//*[@class='widget-pane widget-pane-visible']"));
				diveWholeWeb = divWeb.getAttribute("innerHTML");
			} catch (NoSuchElementException e) {

			}

			Thread.sleep(time);

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
			placesDetailsPojo.setPlaceCity(searchText);
			placesDetailsPojo.setPlaceCity(city);
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
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,"GoogleMapScraper :: getPlacesDetailsPojo() :: Exception :: ",e);
		}
		return placesDetailsPojo;
	}



	public void setFireFoxWebDriver(WebDriver fireFoxWebDriver) {
		this.fireFoxWebDriver = fireFoxWebDriver;
	}


	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void setTextToSearch(String textToScrape) {
		this.searchText = textToScrape;
	}


	public void setCity(String city) {
		this.city = city;
	}


	@Override
	public ArrayList<ProxyDetailsPojo> startScrapingFetchProxyList() {
		// TODO Auto-generated method stub
		return null;
	}



}