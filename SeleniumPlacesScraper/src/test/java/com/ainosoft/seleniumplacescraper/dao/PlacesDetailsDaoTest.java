package com.ainosoft.seleniumplacescraper.dao;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import com.ainosoft.seleniumplacescraper.pojo.PlacesDetailsPojo;

/**
 * 
 * @author tushar@ainosoft.com
 *
 */
public class PlacesDetailsDaoTest {
	private Logger logger=Logger.getLogger(this.getClass().getName());

	@Test
	public void testSaveRestaurantDetailsPojo(){
		try {
			PlacesDetailsDao placesDetailsDao = new PlacesDetailsDao();
			
			@SuppressWarnings("unused")
			byte[] bytes = { (byte) 204, 29, (byte) 207, (byte) 217 };

			@SuppressWarnings("unused")
			byte[] bytes1 = { (byte) 204, 29, (byte) 207, (byte) 217 };

			PlacesDetailsPojo placesDetailsPojo = new PlacesDetailsPojo();
			
			placesDetailsPojo.setPlaceAddress("abc");
			placesDetailsPojo.setPlaceWebsite("abc@xyz.com");
			placesDetailsPojo.setLatitude("111");
			placesDetailsPojo.setLongitude("222");
			placesDetailsPojo.setPlaceName("xyz");
			placesDetailsPojo.setPlacePhoneNo("555");
			placesDetailsPojo.setPlaceType("lmn");
			
			@SuppressWarnings("unused")
			PlacesDetailsPojo savedRestaurantDetailData = placesDetailsDao.savePlacesDetailsPojo(placesDetailsPojo);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "PlacesDetailsDao :: save() ::Exception ::",e);
		}
	}
	
	@Test
	public void testGetAllRestaurantDetailsPojo(){
		try {
			PlacesDetailsDao restaurantDetailsDao = new PlacesDetailsDao();
			List<PlacesDetailsPojo> placesDetailsPojoList = restaurantDetailsDao.getAllPlacesDetailsPojoList();
			
			if(!placesDetailsPojoList.isEmpty() && placesDetailsPojoList!=null){
				//assertTrue(placesDetailsPojoList.get(0).getId()==2);
				assertTrue(placesDetailsPojoList.get(0).getPlaceAddress().equals("abc"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
