package com.ainosoft.seleniumplacescraper.proxystore;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ainosoft.seleniumplacescraper.dao.ProxyDetailsDao;
import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;

/**
 * This is a thread will be called from main() method.
 * Which fetches data from database and accordingly updates the database whether it is working or not.
 * @author tushar@ainosoft.com
 *
 */
public class ProxyStore implements Runnable{

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private ThreadLocal<Integer> counter = new ThreadLocal<Integer>();
	private ThreadLocal<Integer> iterationCount = new ThreadLocal<Integer>();
	private ArrayList<ProxyDetailsPojo> allproxyDetailsPojoList = new ArrayList<ProxyDetailsPojo>();	

	@Override
	public void run() {
		ProxyDetailsDao proxyDetailsDao = null;
		ProxyProvider proxyProvider;
		int proxyCount = 0;
		int iterateCount = 0;
		try {
			proxyDetailsDao = new ProxyDetailsDao();
			proxyProvider = new ProxyProvider();

			logger.log(Level.INFO,"Updating proxies...");

			if(iterationCount.get()!=null){
				iterateCount = iterateCount + iterationCount.get();
			}

			if(counter.get()!=null){
				proxyCount = 0 + counter.get();	
			}

			if(proxyDetailsDao.getValidProxyList(proxyCount)!=null){
				if(!(proxyDetailsDao.getValidProxyList(proxyCount).isEmpty())){
					allproxyDetailsPojoList = proxyDetailsDao.getValidProxyList(proxyCount);					
				}
			}
			
			if(allproxyDetailsPojoList!=null){
				if(!(allproxyDetailsPojoList.isEmpty())){
					if(iterationCount.get()!=null){
						proxyCount = allproxyDetailsPojoList.size() * iterateCount;	
					}else{
						proxyCount = allproxyDetailsPojoList.size();
					}					
				}
			}

			counter.set(proxyCount);
			iterateCount++;
			iterationCount.set(iterateCount);

			if(allproxyDetailsPojoList!=null){
				if(!(allproxyDetailsPojoList.isEmpty())){
					proxyProvider.setAllproxyDetailsPojoList(allproxyDetailsPojoList);		
				}
			}

			logger.log(Level.INFO,"Proxies updated successfully...");

		} catch (Exception e) {
			String exceptionMethod = "ProxyStore :: run() :: Exception :: ";
			logger.log(Level.SEVERE,exceptionMethod,e);
		}
	}

	public ArrayList<ProxyDetailsPojo> getAllproxyDetailsPojoList() {
		return allproxyDetailsPojoList;
	}

	public void setAllproxyDetailsPojoList(
			ArrayList<ProxyDetailsPojo> allproxyDetailsPojoList) {
		this.allproxyDetailsPojoList = allproxyDetailsPojoList;
	}
	
	
}
