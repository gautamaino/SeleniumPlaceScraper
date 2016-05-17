package com.ainosoft.seleniumplacescraper.util;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PageState implements Serializable{

	private int pageNo;

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
}
