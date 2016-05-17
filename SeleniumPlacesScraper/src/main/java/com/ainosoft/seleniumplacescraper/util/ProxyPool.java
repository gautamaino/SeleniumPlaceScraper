package com.ainosoft.seleniumplacescraper.util;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.ainosoft.seleniumplacescraper.dao.ProxyDetailsDao;
import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;

/**
 * 
 * @author tushar@ainosoft.com
 *
 */
public class ProxyPool implements Runnable{

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private ConcurrentHashMap<String,ProxyDetailsPojo> validProxySet = new ConcurrentHashMap<String,ProxyDetailsPojo>();
	private ConcurrentHashMap<String,ProxyDetailsPojo> inValidProxySet = new ConcurrentHashMap<String,ProxyDetailsPojo>();
	
	
	public ProxyPool(ArrayList<ProxyDetailsPojo> validProxyList){
		for (ProxyDetailsPojo proxyDetailsPojo : validProxyList) {
			validProxySet.put(proxyDetailsPojo.getIpAddress(), proxyDetailsPojo);
		} 
	}
	
	@Override
	public void run() {
		ProxyDetailsDao proxyDetailsDao = null;
		try {
			
			logger.log(Level.INFO,"Proxy Pool is updating proxies...");

			proxyDetailsDao = new ProxyDetailsDao();

			if(validProxySet!=null){
				if(!validProxySet.isEmpty()){
					
					for (ProxyDetailsPojo proxyDetailsPojo : validProxySet.values()) {
						
						String ipAddress = proxyDetailsPojo.getIpAddress();
						int port = Integer.parseInt(proxyDetailsPojo.getIpPort());
						
						boolean result = checkForValidIp(ipAddress,port);
					
						if(result){
							validProxySet.put(proxyDetailsPojo.getIpAddress(),proxyDetailsPojo);
						}else{
							inValidProxySet.put(proxyDetailsPojo.getIpAddress(),proxyDetailsPojo);
						}
						
						Thread.sleep(8000);
					}							
				}
			}
			
			if(inValidProxySet!=null){
				if(!inValidProxySet.isEmpty()){
					ArrayList<ProxyDetailsPojo> inValidProxyList = new ArrayList<ProxyDetailsPojo>();
					for (ProxyDetailsPojo proxyDetailsPojo : inValidProxySet.values()) {
						inValidProxyList.add(proxyDetailsPojo);
					}
					proxyDetailsDao.updateProxyStatus(inValidProxyList);		
				}
			}
			
			logger.log(Level.INFO," "+inValidProxySet.size()+" proxies are updated...");
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,"ProxyPool :: run() ::",e);
		}finally{
			Thread.currentThread().interrupt();
		}
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
			
			//fireFoxWebDriver.get("http://whatismyipaddress.com/");
			//Thread.sleep(8000);

			// Launch website
			fireFoxWebDriver.navigate().to("https://www.google.co.in/");
			Thread.sleep(3000);
			
			try {
				String googleName = fireFoxWebDriver.findElement(By.xpath(".//*[@class='logo-subtext']")).getText();
				if(googleName.equals("India")){
					result = true;
				}else{
					result = false;
				}
			} catch (NoSuchElementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,"ScraperManager :: getFireFoxDriver() :: Exception :: ",e);
		}finally{
			fireFoxWebDriver.close();
		}
		return result;
	}

}
