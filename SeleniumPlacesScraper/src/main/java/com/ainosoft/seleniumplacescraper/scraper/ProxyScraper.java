package com.ainosoft.seleniumplacescraper.scraper;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

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

	String serverIp = "46.165.228.164";
	int serverPort = 3128 , time = 3000;

	List<WebElement> proxyList = new ArrayList<WebElement>(); 

	ArrayList<ProxyDetailsPojo> proxyPojoList ;

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public ArrayList<ProxyDetailsPojo> startScrapingFetchProxyList() {
		try {
			proxyPojoList = new ArrayList<ProxyDetailsPojo>();

			FirefoxProfile profile = new FirefoxProfile();  

			profile.setPreference("network.proxy.type",1);
			profile.setPreference("network.proxy.http",serverIp); 
			profile.setPreference("network.proxy.http_port",serverPort); 
			profile.setPreference("browser.privatebrowsing.autostart",true);

			driver = new FirefoxDriver(profile);
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

				if(status == true){
					ProxyDetailsPojo proxyPojo = new ProxyDetailsPojo();
					proxyPojo.setIpAddressAndPort(ipAddress);
					proxyPojo.setIpAddress(textArray[0]);
					proxyPojo.setIpPort(textArray[1]);
					proxyPojo.setUrl(url);
					proxyPojo.setStatus(true);

					proxyPojoList.add(proxyPojo);
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