package com.ainosoft.seleniumplacescraper.entrypoint;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ainosoft.seleniumplacescraper.dao.SpaceInformationDao;
import com.ainosoft.seleniumplacescraper.manager.ScraperManager;
import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;
import com.ainosoft.seleniumplacescraper.pojo.SpaceInformationPojo;
import com.ainosoft.seleniumplacescraper.proxystore.ProxyStore;

/**
 * @author tushar@ainosoft.com
 * This is the main entry point class to start scraping
 * 1. Scraping proxy details 
 * 2. Scraping actual data
 */
public class ScraperEntryPoint {

	private static Logger logger = Logger.getLogger(ScraperEntryPoint.class.getName());
	private static ProxyStore  proxyStore = new ProxyStore();

	public static void main(String[] args) {
		SpaceInformationDao spaceInfoDao = null;
		try {
			spaceInfoDao = new SpaceInformationDao();
			
			ScheduledExecutorService threadSchedulerService = Executors.newScheduledThreadPool(1);
			threadSchedulerService.scheduleAtFixedRate(proxyStore, 0, 20, TimeUnit.MINUTES);
			
			Thread.sleep(15000);
			
			ArrayList<ProxyDetailsPojo> proxyDetailsPojoList = proxyStore.getAllproxyDetailsPojoList();
			
			List<SpaceInformationPojo> spaceInfoList = spaceInfoDao.getAllSpaceInformationPojoList();
			for (int i=0;i<spaceInfoList.size();i++) {
				if(proxyDetailsPojoList!=null){
					if(!(proxyDetailsPojoList.isEmpty())){
						if(!(spaceInfoList.get(i).getCategory_completion_status())){
							if(spaceInfoList.get(i).getSpaceType()!=null && spaceInfoList.get(i).getSpaceCity()!=null){
								Thread scraperManager = new Thread(new ScraperManager("https://www.google.co.in/maps", spaceInfoList.get(i),proxyDetailsPojoList.get(i), proxyDetailsPojoList));
								scraperManager.start();								
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,"ScraperEntryPoint :: main() :: Exception :: ",e);
		}
	}

}