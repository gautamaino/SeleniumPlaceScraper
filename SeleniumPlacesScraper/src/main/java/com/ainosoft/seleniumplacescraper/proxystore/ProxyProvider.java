package com.ainosoft.seleniumplacescraper.proxystore;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;

/**
 * This class is used to provide fresh copy of ProxyDetailPojo
 * @author tushar@ainosoft.com
 *
 */
public class ProxyProvider {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private ArrayList<ProxyDetailsPojo> allproxyDetailsPojoList = new ArrayList<ProxyDetailsPojo>();	
	private ArrayList<ProxyDetailsPojo> inUsedProxyList = new ArrayList<ProxyDetailsPojo>();	


	/**
	 * This method will take a proxyDetailsPojo from readyToUse list and add it to the inUsedProxyList.
	 * And will take first ProxyDetailPojo from inUsedProxyList and simply return it.
	 * @return ProxyDetailsPojo
	 */
	public ProxyDetailsPojo getNextProxy(){
		ProxyDetailsPojo proxyDetailsPojo = null;
		try {
			inUsedProxyList.add(allproxyDetailsPojoList.get(0));
			allproxyDetailsPojoList.remove(0);

			proxyDetailsPojo = inUsedProxyList.get(0);

		} catch (Exception e) {
			logger.log(Level.SEVERE,"ProxyProvider :: getNextProxy() :: Exception :: ",e);
		}
		return proxyDetailsPojo;
	}


	public ArrayList<ProxyDetailsPojo> getAllproxyDetailsPojoList() {
		return allproxyDetailsPojoList;
	}


	public void setAllproxyDetailsPojoList(ArrayList<ProxyDetailsPojo> allproxyDetailsPojoList) {
		this.allproxyDetailsPojoList = allproxyDetailsPojoList;
	}

}
