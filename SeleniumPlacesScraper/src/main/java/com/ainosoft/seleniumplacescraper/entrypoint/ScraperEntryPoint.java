package com.ainosoft.seleniumplacescraper.entrypoint;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ainosoft.seleniumplacescraper.dao.SpaceInformationDao;
import com.ainosoft.seleniumplacescraper.manager.ProxyManager;
import com.ainosoft.seleniumplacescraper.manager.ScraperManager;
import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.SpaceInformationPojo;
import com.ainosoft.seleniumplacescraper.util.ProxyPool;

/**
 * @author tushar@ainosoft.com
 * This is the main entry point class to start scraping
 * It has one method initializeAndStart() which is used for
 * 1. Scraping proxy details 
 * 2. Scraping actual data
 */
public class ScraperEntryPoint {

	private static Logger logger = Logger.getLogger(ScraperEntryPoint.class.getName());

	public static void main(String[] args) {
		try {
			SpaceInformationDao spaceInfoDao = new SpaceInformationDao();
			ProxyPool proxyPool = new ProxyPool();
			ProxyManager proxyManager = new ProxyManager();
			
/*			ScheduledExecutorService threadSchedulerService = Executors.newScheduledThreadPool(1);
			threadSchedulerService.scheduleAtFixedRate(new ProxyPool(), 0, 1, TimeUnit.MINUTES);*/
			
			//Thread.sleep(2000);
			
			//ProxyManager proxyManager = new ProxyManager();
			//proxyManager.initializeAndStart("http://www.ip-adress.com/proxy_list/", null, null);			

			ArrayList<ProxyDetailsPojo> proxyDetailPojoList = proxyManager.getValidProxyList();
			List<SpaceInformationPojo> spaceInfoList = spaceInfoDao.getAllSpaceInformationPojoList();
			if(proxyDetailPojoList != null){
				if(!proxyDetailPojoList.isEmpty()){
					for (int i= 0; i<spaceInfoList.size();i++) {
							Thread scraperManager = new Thread(new ScraperManager("https://www.google.co.in/maps", spaceInfoList.get(i),proxyDetailPojoList.get(i)));
							scraperManager.start();														
					}
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,"ScraperEntryPoint :: main() :: Exception :: ",e);
		}
	}
	
}