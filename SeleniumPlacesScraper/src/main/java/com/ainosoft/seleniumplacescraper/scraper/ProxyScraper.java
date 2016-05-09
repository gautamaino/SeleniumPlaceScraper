package com.ainosoft.seleniumplacescraper.scraper;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.ainosoft.seleniumplacescraper.dao.ProxyDetailsDao;
import com.ainosoft.seleniumplacescraper.pojo.PlacesDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;
import com.ainosoft.seleniumplacescraper.util.ScraperLogger;


/**
 * @author nalanda@ainosoft.com
 */
public class ProxyScraper implements Scraper{

	@SuppressWarnings("unused")
	private static ScraperLogger scraperLogger = new ScraperLogger("ProxyScraper");

	private String url;

	WebDriver driver;

	int time = 3000;

	List<WebElement> proxyList = new ArrayList<WebElement>(); 

	ArrayList<ProxyDetailsPojo> proxyPojoList;
	ArrayList<ProxyDetailsPojo> invalidProxyList;

	ProxyDetailsDao proxyDao = new ProxyDetailsDao();

	FirefoxProfile profile;

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	public FirefoxProfile createProfile(){
		invalidProxyList = new ArrayList<ProxyDetailsPojo>();
		try {
			List<ProxyDetailsPojo> validProxyList = proxyDao.getValidProxyList();
			if(validProxyList!=null){
				if(!validProxyList.isEmpty()){
					if(validProxyList.get(0) instanceof ProxyDetailsPojo){
						for(ProxyDetailsPojo proxyPojo : validProxyList){
							String serverIp = proxyPojo.getIpAddress();
							Integer serverPort = Integer.parseInt(proxyPojo.getIpPort());

							boolean status = checkForValidIp(serverIp);
							if(status){
								profile = new FirefoxProfile();  

								profile.setPreference("network.proxy.type",1);
								profile.setPreference("network.proxy.http",serverIp); 
								profile.setPreference("network.proxy.http_port",serverPort); 
								profile.setPreference("browser.privatebrowsing.autostart",true);
								break;
							}else{
								invalidProxyList.add(proxyPojo);
								proxyDao.updateProxyStatus(invalidProxyList);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			ScraperLogger.log("ProxyManager :: createProfile() ::",e); 
		}
		return profile;
	}

	@Override
	public ArrayList<ProxyDetailsPojo> startScrapingFetchProxyList() {
		try {
			proxyPojoList = new ArrayList<ProxyDetailsPojo>();

			FirefoxProfile proxyProfile = createProfile();

			driver = new FirefoxDriver(proxyProfile);
			driver.get("http://whatismyipaddress.com/");
			Thread.sleep(time);

			driver.get("https://www.google.com/");
			Thread.sleep(time);

			driver.navigate().to(url);
			Thread.sleep(time);

			List<WebElement> oddWebElementsList = driver.findElements(By.className("odd"));
			Thread.sleep(time);
			for(WebElement oddWebElement : oddWebElementsList){				
				proxyList.add(oddWebElement);				
			}

			List<WebElement> evenWebElementsList = driver.findElements(By.className("even"));
			Thread.sleep(time);
			for(WebElement evenWebElement : evenWebElementsList){
				proxyList.add(evenWebElement);
			}

			for(WebElement webElement : proxyList){
				String text = webElement.getText();
				String ipAddress = text.substring(0,text.indexOf(' '));

				String[] textArray = ipAddress.split(":");

				boolean status = checkForValidIp(textArray[0]);

				if(status){
					ProxyDetailsPojo proxyPojoToSave = new ProxyDetailsPojo();
					proxyPojoToSave.setIpAddressAndPort(ipAddress);
					proxyPojoToSave.setIpAddress(textArray[0]);
					proxyPojoToSave.setIpPort(textArray[1]);
					proxyPojoToSave.setUrl(url);
					proxyPojoToSave.setStatus(true);

					proxyPojoList.add(proxyPojoToSave);

				}
			}
		} catch (Exception e) {
			ScraperLogger.log("ProxyManager :: startScrapingFetchProxyList() ::",e); 
		}finally{
			if(driver!=null){
				driver.close();
				driver.quit();
			}
		}
		return proxyPojoList;
	}

	public boolean checkForValidIp(String ipAddress) {
		boolean connectionStatus = false;
		try {
			//here type proxy server ip
			InetAddress addr = InetAddress.getByName(ipAddress);

			// 1 second time for response
			connectionStatus = addr.isReachable(1000); 
		} catch (Exception e) {
			ScraperLogger.log("ProxyManager :: checkForValidIp() ::",e);
		}
		return connectionStatus;
	}

	@Override
	public ArrayList<PlacesDetailsPojo> startScrapingFetchList() {
		return null;
	}

	@Override
	public ArrayList<PlacesDetailsPojo> reRunScraping(int pageCount) {
		return null;
	}

	@Override
	public void setTextToSearch(String textToScrape) {
	}
}