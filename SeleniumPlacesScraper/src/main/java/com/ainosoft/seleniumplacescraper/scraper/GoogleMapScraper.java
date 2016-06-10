package com.ainosoft.seleniumplacescraper.scraper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.ainosoft.seleniumplacescraper.dao.SpaceInformationDao;
import com.ainosoft.seleniumplacescraper.manager.DataEntryMakerForPlacesDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.PlacesDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.SpaceInformationPojo;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;


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
	private ProxyDetailsPojo proxyDetailsPojo = null;
	private ArrayList<ProxyDetailsPojo> proxyDetailsPojoList;
	private boolean endOfScraperFlag = false;
	private int timer = 19000;



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

			Thread.sleep(timer);

			fireFoxWebDriver = getFireFoxDriver();

			fireFoxWebDriver.get("http://whatismyipaddress.com/");
			Thread.sleep(timer);

			// Launch website
			fireFoxWebDriver.navigate().to(url);
			Thread.sleep(timer);

			// Maximize the browser
			//fireFoxWebDriver.manage().window().maximize();

			try {
				Thread.sleep(timer);

				WebElement searchTextBox = fireFoxWebDriver.findElement(By.xpath(".//*[@id='searchboxinput']"));

				if(spaceInfoPojo.getSpaceType()!=null && spaceInfoPojo.getSpaceCity()!=null){
					searchTextBox.sendKeys(spaceInfoPojo.getSpaceType()+" "+"in"+" "+spaceInfoPojo.getSpaceCity());	
				}

				Thread.sleep(timer);
			} catch (NoSuchElementException e) {

			}

			try {
				Thread.sleep(timer);

				// Click search Button
				WebElement searchButton = fireFoxWebDriver.findElement(By.xpath(".//*[@id='searchbox']/div[1]/button"));
				searchButton.click();

				Thread.sleep(timer);
			} catch (NoSuchElementException e) {

			}

			while(!(pCount == spaceInfoPojo.getPageCount())){
				try {
					pCount++;
					Thread.sleep(timer);

					WebElement nextButton = fireFoxWebDriver.findElement(By.xpath(".//*[@id='widget-pane-section-pagination-button-next']"));
					nextButton.click();

					Thread.sleep(timer);

				} catch (NoSuchElementException e) {
					pCount--;
					spaceInfoPojo.setPageCount(pCount);
					spaceInfoDao.updatePageCount(spaceInfoPojo);
					continue;
				}
			}

			Thread.sleep(timer);

			startScrapingFetchList();

			Thread.sleep(timer);

			try {
				WebElement nextButton = fireFoxWebDriver.findElement(By.xpath(".//*[@id='widget-pane-section-pagination-button-next']"));
				nextButton.click();
				Thread.sleep(timer);
				pCount++;
			} catch (NoSuchElementException e) {
				spaceInfoPojo.setPageCount(pCount);
				spaceInfoDao.updatePageCount(spaceInfoPojo);
			}

			spaceInfoPojo.setPageCount(pCount);
			spaceInfoDao.updatePageCount(spaceInfoPojo);

			Thread.sleep(timer);

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
					Thread.sleep(timer);
					elementList = fireFoxWebDriver.findElements(By.xpath(".//*[@class='widget-pane-section-result']"));
					Thread.sleep(timer);
				} catch (NoSuchElementException e1) {

				}

				if(elementList!=null){
					if(!elementList.isEmpty()){

						Thread.sleep(timer);
						elementList.get(i).click();
						Thread.sleep(timer);

						size = elementList.size();

						placesDetailsPojo = getPlacesDetailsPojo();

						placesDetailsPojoList.add(placesDetailsPojo);

						Thread.sleep(timer);

						try {
							//returning back to the page which contains list
							fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/button")).click();
							Thread.sleep(timer);
						} catch (NoSuchElementException e) {
							break;
						} 
					}else{
						try {
							Thread.sleep(timer);

							String finalPage = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[2]/div[1]/div[2]/span[1]")).getText();
							if(finalPage.equals("Make sure your search is spelled correctly.")){
								SpaceInformationDao spaceInformationDao = new SpaceInformationDao();
								spaceInfoPojo.setCategory_completion_status(true);
								spaceInformationDao.updateCategoryCompletionStatus(spaceInfoPojo);

								endOfScraperFlag = true;
								Thread.currentThread().interrupt();
								logger.log(Level.INFO,"Scraping Successfully Completed...");
							}
						} catch (NoSuchElementException e) {
							
						}
					}
				}else{
					try {
						Thread.sleep(timer);

						String finalPage = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[2]/div[1]/div[2]/span[1]")).getText();
						if(finalPage.equals("Make sure your search is spelled correctly.")){
							SpaceInformationDao spaceInformationDao = new SpaceInformationDao();
							spaceInfoPojo.setCategory_completion_status(true);
							spaceInformationDao.updateCategoryCompletionStatus(spaceInfoPojo);

							endOfScraperFlag = true;
							Thread.currentThread().interrupt();
							logger.log(Level.INFO,"Scraping Successfully Completed...");
						}
					} catch (NoSuchElementException e) {
	
					}
				}
			}

			dataEntryMakerForPlacesDetailsPojo = new Thread(new DataEntryMakerForPlacesDetailsPojo(placesDetailsPojoList));
			dataEntryMakerForPlacesDetailsPojo.start();

			Thread.sleep(timer);

			try {
				Thread.sleep(timer);

				String finalPageString = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[2]/div[1]/div[2]/span[1]")).getText();
				if(finalPageString.equals("Make sure your search is spelled correctly.")){

					SpaceInformationDao spaceInformationDao = new SpaceInformationDao();
					spaceInfoPojo.setCategory_completion_status(true);
					spaceInformationDao.updateCategoryCompletionStatus(spaceInfoPojo);

					endOfScraperFlag = true;
					Thread.currentThread().interrupt();
					logger.log(Level.INFO,"Scraping Successfully Completed...");
				}
			} catch (NoSuchElementException e) {

			}

		} catch (Exception e) {
			logger.log(Level.SEVERE,"GoogleMapScraper :: startScrapingFetchList() :: Exception :: ",e);
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

			Thread.sleep(timer);

			while(true){

				String serverIP = proxyDetailsPojo.getIpAddress();
				Integer port = Integer.parseInt(proxyDetailsPojo.getIpPort());

				logger.log(Level.INFO,"IP is : "+serverIP+" PORT is :"+port);

				boolean result = checkForValidIp(serverIP,port);
				if(result){
					profile = new FirefoxProfile();
					profile.setPreference("network.proxy.type", 1);
					profile.setPreference("network.proxy.http", serverIP);
					profile.setPreference("network.proxy.http_port", port);

					// Setup firefox binary to start in Xvfb        
			        String Xport = System.getProperty("lmportal.xvfb.id", ":1");
			        final File firefoxPath = new File(System.getProperty("lmportal.deploy.firefox.path", "/usr/bin/firefox"));

			        FirefoxBinary firefoxBinary = new FirefoxBinary(firefoxPath);
			        firefoxBinary.setEnvironmentProperty("DISPLAY", Xport);

					fireFoxWebDriver = new FirefoxDriver(firefoxBinary,profile);
					break;
				}else{
					getNextProxy(serverIP);	
					continue;
				}
			}

			Thread.sleep(timer);

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
		WebDriver fireFoxWebDriverToCheck = null;	
		FirefoxProfile profile = null;
		boolean result = false;
		try {
			profile = new FirefoxProfile();
			profile.setPreference("network.proxy.type", 1);
			profile.setPreference("network.proxy.http", ipAddress);
			profile.setPreference("network.proxy.http_port", port);

			// Setup firefox binary to start in Xvfb        
	        String Xport = System.getProperty("lmportal.xvfb.id", ":1");
	        final File firefoxPath = new File(System.getProperty("lmportal.deploy.firefox.path", "/usr/bin/firefox"));

	        FirefoxBinary firefoxBinary = new FirefoxBinary(firefoxPath);
	        firefoxBinary.setEnvironmentProperty("DISPLAY", Xport);

			fireFoxWebDriverToCheck = new FirefoxDriver(firefoxBinary,profile);

			Thread.sleep(timer);

			// Launch website
			fireFoxWebDriverToCheck.navigate().to("https://www.google.co.in/");
			Thread.sleep(timer);

			try {
				String googleName = fireFoxWebDriverToCheck.findElement(By.xpath(".//*[@class='logo-subtext']")).getText();
				Thread.sleep(timer);

				if(googleName.equals("India")){
					result = true;
				}else{
					result = false;
				}
			} catch (NoSuchElementException e) {
				logger.log(Level.SEVERE,"ScraperManager :: checkForValidIp() :: Google name not found... ",e);
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE,"ScraperManager :: checkForValidIp() :: Exception :: ",e);
		}finally{
			fireFoxWebDriverToCheck.close();
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
				Thread.sleep(timer);
			} catch (NoSuchElementException e) {

			}

			String type = null;
			try {
				type = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[1]/div[2]/div[2]/div[2]/span/span[1]/button")).getText();
				Thread.sleep(timer);
			} catch (NoSuchElementException e) {
				//break;
			}

			String email = null;
			try {
				email = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[6]/div/span[2]/span[1]/span[2]/span/a[2]")).getText();
				Thread.sleep(timer);
			} catch (NoSuchElementException e) {

			}

			String address = null;
			try {
				address = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[5]/div/span[2]/span[1]/span[1]/span")).getText();
				Thread.sleep(timer);
			} catch (NoSuchElementException e) {

			}

			Thread.sleep(timer);

			String phoneNumber = null;
			try {
				phoneNumber = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[6]/div/span[2]/span[1]/a")).getText();
				Thread.sleep(timer);
			} catch (NoSuchElementException e) {

			}

			String ratings = null;
			try {
				ratings = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[1]/div[2]/div[2]/div[1]/span[1]/span/span")).getText();
				Thread.sleep(timer);
			} catch (NoSuchElementException e) {

			}

			String mainTitleImageURL = null;
			try {
				mainTitleImageURL = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[1]/button[1]/img")).getAttribute("src");
				Thread.sleep(timer);
			} catch (NoSuchElementException e) {

			}

			String wholeDiv = null;
			try {
				WebElement divWeb = fireFoxWebDriver.findElement(By.xpath(".//*[@class='widget-pane widget-pane-visible']"));
				wholeDiv = divWeb.getAttribute("innerHTML");
				Thread.sleep(timer);
			} catch (NoSuchElementException e) {

			}
			
			/***************************************From this line it will scrape data for timing***********************************/
			
			ArrayList<String> timeTableList  = getTimeTableList();
			StringBuffer timings = new StringBuffer();
			if(timeTableList!=null){
				if(!(timeTableList.isEmpty())){
					for (String timetable : timeTableList) {
						if(timetable!=""){
							timings.append("####"+timetable);	
						}
					}					
				}
			}
			placesDetailsPojo.setTimings(timings.toString());
			
			/***************************************From this line it will scrape data for reviews***********************************/
			
			ArrayList<String> reviewList = new ArrayList<String>();

			try {
				List<WebElement> listOfReviews = fireFoxWebDriver.findElements(By.xpath(".//*[@class='widget-pane-section-review ripple-container']"));
				for (WebElement reviews:listOfReviews){
					String review = reviews.getText();
					reviewList.add(review);
				}
			} catch (NoSuchElementException e) {
				
			}

			StringBuffer allReviews = new StringBuffer();
			if(reviewList!=null){
				if(!(reviewList.isEmpty())){
					for (String review : reviewList) {
						if(review!=""){
							allReviews.append("&&"+review);	
						}
					}					
				}
			}


			placesDetailsPojo.setReviews(allReviews.toString());
			Thread.sleep(timer);

			String url = fireFoxWebDriver.getCurrentUrl();

			StringBuffer latitude = new StringBuffer();
			StringBuffer longitude = new StringBuffer();

			try {
				int onAtSymbol = url.indexOf("@");
				int onCommaOperator = url.indexOf(",");
				int onSecondCommaOperator = url.lastIndexOf(",");

				if(onAtSymbol!=0 && onCommaOperator!=0){
					if(url.substring(onAtSymbol+1,onCommaOperator)!=null){
						latitude.append(url.substring(onAtSymbol+1,onCommaOperator));	
					}
				}

				if(onCommaOperator!=0 && onSecondCommaOperator!=0){
					if(url.substring(onCommaOperator+1, onSecondCommaOperator)!=null){
						longitude.append(url.substring(onCommaOperator+1, onSecondCommaOperator));	
					}
				}
			} catch (Exception e) {
				if(e.equals("StringIndexOutOfBoundException")){
						
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
			placesDetailsPojo.setWebElement(wholeDiv);
			placesDetailsPojo.setImage(mainTitleImageURL);

			Thread.sleep(timer);

		} catch (Exception e) {
			logger.log(Level.SEVERE,"GoogleMapScraper :: getPlacesDetailsPojo() :: Exception :: ",e);
		}
		return placesDetailsPojo;
	}


	public void getNextProxy(String ipAddress){
		try {
			for (int i = 0; i < proxyDetailsPojoList.size(); i++) {
				String ip = proxyDetailsPojoList.get(i).getIpAddress();
				if(!(ipAddress.equals(ip))){
					proxyDetailsPojo = proxyDetailsPojoList.get(i);	
					break;
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,"GoogleMapScraper :: getNextProxy() :: Exception :: ",e);
		}
	}

	/**
	 * this method will return list of timetable with days and it's timing
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getTimeTableList() {
		ArrayList<String> timeTableList = new ArrayList<String>();
		try {
			/*String timing = null;
			try {
				timing = fireFoxWebDriver.findElement(By.className("widget-pane-section-info-hour-text")).getText();
				//logger.log(Level.INFO,"Timing : "+timing);
				Thread.sleep(timer);
			} catch (NoSuchElementException e) {

			}*/
			
			try {
				Thread.sleep(timer);

				WebElement timingDropDownButton = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[1]/button"));
				timingDropDownButton.click();

				Thread.sleep(timer);
			}catch (NoSuchElementException exception) {
				
			}catch(WebDriverException we){
				
			}catch(ElementNotFoundException e){
				
			}
			
			String today = null;
			try {
				Thread.sleep(3000);
				today = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[1]/th/div[1]")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			String todayTimingFirst = null;
			try {
				Thread.sleep(3000);
				todayTimingFirst = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[1]/td/ul/li[1]")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			try {
				todayTimingFirst = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[1]/td/ul/li")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			String todayTimingSecond = null;
			try {
				todayTimingSecond = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[1]/td/ul/li[2]")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			if((today!="" && today!=null) && (todayTimingFirst!="" && todayTimingFirst!=null) && todayTimingSecond!="" && todayTimingSecond!=null){
				String str = "$$"+today+"&&"+todayTimingFirst+"&&"+todayTimingSecond+"&&";
				timeTableList.add(str);
			}else if((today!="" && today!=null) && (todayTimingFirst!="" && todayTimingFirst!=null)){
				String str = "$$"+today+"&&"+todayTimingFirst+"&&";
				timeTableList.add(str);
			}
			
			
			String day1 = null;
			try {
				Thread.sleep(3000);
				day1 = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[2]/th/div[1]")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			String day1TimingFirst = null;
			try {
				day1TimingFirst = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[2]/td/ul/li[1]")).getText();
			} catch (NoSuchElementException e) {
				
			}

			try {
				day1TimingFirst = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[2]/td/ul/li")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			String day1TimingSecond = null;
			try {
				day1TimingSecond = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[2]/td/ul/li[2]")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			if((day1!="" && day1!=null) && (day1TimingFirst!="" && day1TimingFirst!=null) && day1TimingSecond!="" && day1TimingSecond!=null){
				String str = "$$"+day1+"&&"+day1TimingFirst+"&&"+day1TimingSecond+"&&";
				timeTableList.add(str);
			}else if((day1!="" && day1!=null) && (day1TimingFirst!="" && day1TimingFirst!=null)){
				String str = "$$"+day1+"&&"+day1TimingFirst+"&&";
				timeTableList.add(str);
			}
			
			
			String day2 = null;
			try {
				day2 = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[3]/th/div[1]")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			String day2TimingFirst = null;
			try {
				day2TimingFirst = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[3]/td/ul/li[1]")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			try {
				day2TimingFirst = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[3]/td/ul/li")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			String day2TimingSecond = null;
			try {
				Thread.sleep(3000);
				day2TimingSecond = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[3]/td/ul/li[2]")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			if((day2!="" && day2!=null) && (day2TimingFirst!="" && day2TimingFirst!=null) && day2TimingSecond!="" && day2TimingSecond!=null){
				String str = "$$"+day2+"&&"+day2TimingFirst+"&&"+day2TimingSecond+"&&";
				timeTableList.add(str);
			}else if((day2!="" && day2!=null) && (day2TimingFirst!="" && day2TimingFirst!=null)){
				String str = "$$"+day2+"&&"+day2TimingFirst+"&&";
				timeTableList.add(str);
			}
			
			
			String day3 = null;
			try {
				day3 = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[4]/th/div[1]")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			String day3TimingFirst = null;
			try {
				day3TimingFirst = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[4]/td/ul/li[1]")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			try {
				Thread.sleep(3000);
				day3TimingFirst = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[4]/td/ul/li")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			String day3TimingSecond = null;
			try {
				day3TimingSecond = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[4]/td/ul/li[2]")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			if((day3!="" && day3!=null) && (day3TimingFirst!="" && day3TimingFirst!=null) && day3TimingSecond!="" && day3TimingSecond!=null){
				String str = "$$"+day3+"&&"+day3TimingFirst+"&&"+day3TimingSecond+"&&";
				timeTableList.add(str);
			}else if((day3!="" && day3!=null) && (day3TimingFirst!="" && day3TimingFirst!=null)){
				String str = "$$"+day3+"&&"+day3TimingFirst+"&&";
				timeTableList.add(str);
			}
			
			String day4 = null;
			try {
				day4 = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[5]/th/div[1]")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			String day4TimingFirst = null;
			try {
				day4TimingFirst = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[5]/td/ul/li[1]")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			try {
				day4TimingFirst = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[5]/td/ul/li")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			String day4TimingSecond = null;
			try {
				Thread.sleep(3000);
				day4TimingSecond = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[5]/td/ul/li[2]")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			if((day4!="" && day4!=null) && (day4TimingFirst!="" && day4TimingFirst!=null) && day4TimingSecond!="" && day4TimingSecond!=null){
				String str = "$$"+day4+"&&"+day4TimingFirst+"&&"+day4TimingSecond+"&&";
				timeTableList.add(str);
			}else if((day4!="" && day4!=null) && (day4TimingFirst!="" && day4TimingFirst!=null)){
				String str = "$$"+day4+"&&"+day4TimingFirst+"&&";
				timeTableList.add(str);
			}
			
			
			String day5 = null;
			try {
				day5 = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[6]/th/div[1]")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			String day5TimingFirst = null;
			try {
				day5TimingFirst = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[6]/td/ul/li[1]")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			try {
				day5TimingFirst = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[6]/td/ul/li")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			String day5TimingSecond = null;
			try {
				day5TimingSecond = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[6]/td/ul/li[2]")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			if((day5!="" && day5!=null) && (day5TimingFirst!="" && day5TimingFirst!=null) &&  day5TimingSecond!="" && day5TimingSecond!=null){
				String str = "$$"+day5+"&&"+day5TimingFirst+"&&"+day5TimingSecond+"&&";
				timeTableList.add(str);
			}else if((day5!="" && day5!=null) && (day5TimingFirst!="" && day5TimingFirst!=null)){
				String str = "$$"+day5+"&&"+day5TimingFirst+"&&";
				timeTableList.add(str);
			}
			
			
			String day6 = null;
			try {
				Thread.sleep(3000);
				day6 = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[7]/th/div[1]")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			String day6TimingFirst = null;
			try {
				day6TimingFirst = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[7]/td/ul/li[1]")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			try {
				day6TimingFirst = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[7]/td/ul/li")).getText();
			} catch (NoSuchElementException e) {
				
			}
			
			String day6TimingSecond = null;
			try {
				day6TimingSecond = fireFoxWebDriver.findElement(By.xpath(".//*[@id='pane']/div/div[1]/div/div[8]/div[3]/table/tbody/tr[7]/td/ul/li[2]")).getText();
			} catch (NoSuchElementException e) {
				
			}

			if((day6!="" && day6!=null) && (day6TimingFirst!="" && day6TimingFirst!=null) && day6TimingSecond!="" && day6TimingSecond!=null){
				String str = "$$"+day6+"&&"+day6TimingFirst+"&&"+day6TimingSecond+"&&";
				timeTableList.add(str);
			}else if((day6!="" && day6!=null) && (day6TimingFirst!="" && day6TimingFirst!=null)){
				String str = "$$"+day6+"&&"+day6TimingFirst+"&&";
				timeTableList.add(str);
			}

		}catch(ElementNotVisibleException e){
			
		}catch(ElementNotFoundException e){
			
		}catch(Exception e) {
			logger.log(Level.SEVERE,"GoogleMapScraper :: getTimeTableList() :: ",e);
		}
		return timeTableList;
	}
	
	
	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public ArrayList<ProxyDetailsPojo> startScrapingFetchProxyList() {
		
		return null;
	}

	public void setSpaceInfoPojo(SpaceInformationPojo spaceInfoPojo) {
		this.spaceInfoPojo = spaceInfoPojo;
	}

	public void setFireFoxWebDriver(WebDriver fireFoxWebDriver) {
		this.fireFoxWebDriver = fireFoxWebDriver;
	}

	public void setProxyDetailsPojo(ProxyDetailsPojo proxyDetailsPojo) {
		this.proxyDetailsPojo = proxyDetailsPojo;
	}

	public void setProxyDetailsPojoList(ArrayList<ProxyDetailsPojo> proxyDetailsPojoList) {
		this.proxyDetailsPojoList = proxyDetailsPojoList;
	}

	public boolean isEndOfScraperFlag() {
		return endOfScraperFlag;
	}

	public void setEndOfScraperFlag(boolean endOfScraperFlag) {
		this.endOfScraperFlag = endOfScraperFlag;
	}


}
