package com.ainosoft.seleniumplacescraper.scraper;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.ainosoft.seleniumplacescraper.pojo.PlacesDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;
import com.ainosoft.seleniumplacescraper.util.ScraperLogger;


/**
 * 
 * @author tushar@ainosoft.com
 *
 */
public class GoogleMapScraper implements Scraper {

	private ScraperLogger scraperLogger = new ScraperLogger("googlemaps");
	
	private String url,searchText;
	private WebDriver fireFoxWebDriver = null;
	private	int pCount = 0;
	private String city="";
	
	@Override
	public ArrayList<PlacesDetailsPojo> startScrapingFetchList() {
		ArrayList<PlacesDetailsPojo> placesDetailsPojoList = new ArrayList<PlacesDetailsPojo>();
		int size = 1;
		try {

			scraperLogger.log("Scraping started...");

			for (int i = 0 ; i < size ; ++i) {
				PlacesDetailsPojo placesDetailsPojo = new PlacesDetailsPojo();

				Thread.sleep(5000);
				List<WebElement> elementList = fireFoxWebDriver.findElements(By.xpath(".//*[@class='widget-pane-section-result']"));
				Thread.sleep(5000);

				if(elementList!=null){
					if(!elementList.isEmpty()){

						Thread.sleep(5000);
						elementList.get(i).click();
						Thread.sleep(5000);

						size = elementList.size();

						placesDetailsPojo = getPlacesDetailsPojo();

						placesDetailsPojoList.add(placesDetailsPojo);

						Thread.sleep(5000);

						try {
							//returning back to the first page
							fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/button")).click();
							Thread.sleep(5000);
						} catch (Exception e) {
							if(e.equals("NoSuchElementException")){
								break;
							}
						}

					}
				}
			}
		} catch (Exception e) {
			scraperLogger.log("GoogleMapScraper :: startScrapingFetchList() :: Exception :: ",e);
		}
		return placesDetailsPojoList;
	}


	@Override
	public ArrayList<PlacesDetailsPojo> reRunScraping(int pageCount) {
		ArrayList<PlacesDetailsPojo> scrapeFetchedList = null;
		try {

			scraperLogger.log("Rerunning scraping...");

			//driver.get("http://whatismyipaddress.com/");
			Thread.sleep(5000);

			// Launch website
			fireFoxWebDriver.navigate().to(url);
			Thread.sleep(5000);

			// Maximize the browser
			//fireFoxWebDriver.manage().window().maximize();

			// Enter value 50 in the second number of the percent Calculator
			WebElement searchTextBox = fireFoxWebDriver.findElement(By.xpath(".//*[@id='searchboxinput']"));

			searchTextBox.sendKeys(searchText);

			Thread.sleep(5000);

			// Click search Button
			WebElement searchButton = fireFoxWebDriver.findElement(By.xpath(".//*[@id='searchbox']/div[1]/button"));

			searchButton.click();

			Thread.sleep(5000);

			while(!(pCount == pageCount)){
				try {
					pCount++;
					WebElement nextButton = fireFoxWebDriver.findElement(By.xpath(".//*[@id='widget-pane-section-pagination-button-next']"));
					nextButton.click();
				} catch (Exception e) {
					if(e.equals("NoSuchElementException")){
						continue;
					}
				}
			}

			scrapeFetchedList = startScrapingFetchList();

		} catch (Exception e) {
			scraperLogger.log("GoogleMapScraper :: reRunScraping() :: Exception :: ",e);
		}
		return scrapeFetchedList;
	}

	
	
	public PlacesDetailsPojo getPlacesDetailsPojo(){
		PlacesDetailsPojo placesDetailsPojo = null;
		try {
			placesDetailsPojo = new PlacesDetailsPojo();
			
			//fetching some values
			String name = null;
			try {
				name = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[1]/div[2]/div[1]/h1")).getText();
				//Thread.sleep(3000);
			} catch (Exception e) {
				if(e.equals("NoSuchElementException")){
					//break;
				}
			}

			String type = null;
			try {
				type = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[1]/div[2]/div[2]/div[2]/span/span[1]/button")).getText();
				//Thread.sleep(3000);
			} catch (Exception e) {
				if(e.equals("NoSuchElementException")){
					//break;
				}
			}

			String email = null;
			try {
				email = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[6]/div/span[2]/span[1]/span[2]/span/a[2]")).getText();
				//Thread.sleep(3000);
			} catch (Exception e) {
				if(e.equals("NoSuchElementException")){
					//break;
				}
			}

			String address = null;
			try {
				address = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[5]/div/span[2]/span[1]/span[1]/span")).getText();
				//Thread.sleep(3000);
			} catch (Exception e) {
				if(e.equals("NoSuchElementException")){
					//break;
				}
			}

			String phoneNumber = null;
			try {
				phoneNumber = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[6]/div/span[2]/span[1]/a")).getText();
				//Thread.sleep(3000);
			} catch (Exception e) {
				if(e.equals("NoSuchElementException")){
					//break;
				}
			}

			String ratings = null;
			try {
				ratings = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[1]/div[2]/div[2]/div[1]/span[1]/span/span")).getText();
				//Thread.sleep(3000);
			} catch (Exception e) {
				if(e.equals("NoSuchElementException")){
					//break;
				}
			}

			String timing = null;
			try {
				timing = fireFoxWebDriver.findElement(By.className("widget-pane-section-info-hour-text")).getText();
				//Thread.sleep(3000);
			} catch (Exception e) {
				if(e.equals("NoSuchElementException")){
					//break;
				}
			}

			String imageURL = null;
			try {
				imageURL = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[1]/button[1]/img")).getAttribute("src");
				//Thread.sleep(3000);
			} catch (Exception e) {
				if(e.equals("NoSuchElementException")){
					//break;
				}
			}

			String diveWholeWeb = null;
			try {
				WebElement divWeb = fireFoxWebDriver.findElement(By.xpath(".//*[@class='widget-pane widget-pane-visible']"));
				diveWholeWeb = divWeb.getAttribute("innerHTML");
			} catch (Exception e) {
				if(e.equals("NoSuchElementException")){
					//break;
				}
			}

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
			// TODO: handle exception
		}
		return placesDetailsPojo;
	}
	public void setFireFoxWebDriver(WebDriver fireFoxWebDriver) {
		this.fireFoxWebDriver = fireFoxWebDriver;
	}
	
	public void setpCount(int pCount) {
		this.pCount = pCount;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void setTextToSearch(String textToScrape) {
		this.searchText = textToScrape;
	}

	@Override
	public ArrayList<ProxyDetailsPojo> startScrapingFetchProxyList() {
		// TODO Auto-generated method stub
		return null;
	}


	public void setCity(String city) {
		this.city = city;
	}



}