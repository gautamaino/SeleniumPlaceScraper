package com.ainosoft.seleniumplacescraper.util;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ainosoft.seleniumplacescraper.manager.ProxyManager;
import com.ainosoft.seleniumplacescraper.pojo.ProxyDetailsPojo;

/**
 * 
 * @author tushar@ainosoft.com
 *
 */
public class ProxyPool implements Runnable{

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private Set<ProxyDetailsPojo> validProxySet = new HashSet<ProxyDetailsPojo>();
	private ArrayList<ProxyDetailsPojo> validProxyList = new ArrayList<ProxyDetailsPojo>();
	private ArrayList<ProxyDetailsPojo> inValidProxyList = new ArrayList<ProxyDetailsPojo>();
	
	@Override
	public void run() {
		try {
			logger.log(Level.INFO,"----------------------Validating Proxies-----------------");
			
			ProxyManager proxyManager = new ProxyManager();
			
			ArrayList<ProxyDetailsPojo> proxyDetailsPojoList = proxyManager.getValidProxyList();
			if(proxyDetailsPojoList!=null){
				if(!proxyDetailsPojoList.isEmpty()){
					for (ProxyDetailsPojo proxyDetailsPojo : proxyDetailsPojoList) {
						if(proxyDetailsPojo.getIpAddress()!=null){
							boolean result = checkForValidIp(proxyDetailsPojo.getIpAddress());
							if(result){
								validProxySet.add(proxyDetailsPojo);
								validProxyList.addAll(validProxySet);
							}else{
								inValidProxyList.add(proxyDetailsPojo);
								proxyManager.updateProxyStatus(inValidProxyList);
							}
						}
					}					
				}
			}
		logger.log(Level.INFO,"---------------------Proxy Validation Completed For "+validProxyList.size()+" Values"+"--------------");
		} catch (Exception e) {
			logger.log(Level.SEVERE,"ProxyPool :: checkForValidIp() ::",e);
		}
	}
	
	/**
	 * This method is used for validating a proxy
	 * @param ipAddress
	 * @return boolean
	 */
	public boolean checkForValidIp(String ipAddress) {
		boolean connectionStatus = false;
		try {
			//here type proxy server ip
			InetAddress addr = InetAddress.getByName(ipAddress);

			// 1 second time for response
			connectionStatus = addr.isReachable(1000); 
		} catch (Exception e) {
			logger.log(Level.SEVERE,"ProxyPool :: checkForValidIp() ::",e);
		}
		return connectionStatus;
	}

	
	public ArrayList<ProxyDetailsPojo> getValidProxyList() {
		return validProxyList;
	}

	public void setValidProxyList(ArrayList<ProxyDetailsPojo> validProxyList) {
		this.validProxyList = validProxyList;
	}

	public ArrayList<ProxyDetailsPojo> getInValidProxyList() {
		return inValidProxyList;
	}

	public void setInValidProxyList(ArrayList<ProxyDetailsPojo> inValidProxyList) {
		this.inValidProxyList = inValidProxyList;
	}
	
	
	
}
