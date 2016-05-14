package com.ainosoft.seleniumplacescraper.dao;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.ainosoft.seleniumplacescraper.pojo.SpaceInformationPojo;

public class SpaceInformationTest {

	@Test
	public void testSaveSpaceInformationPojo(){
		try {
			SpaceInformationDao spaceInformationDao = new SpaceInformationDao();

			SpaceInformationPojo  spaceInformationPojo = new SpaceInformationPojo();

			spaceInformationPojo.setPageCount(3);
			spaceInformationPojo.setSpaceCity("Pune");
			spaceInformationPojo.setSpaceType("Restaurant");

			SpaceInformationPojo savedSpaceInformationPojo = spaceInformationDao.saveSpaceInformationPojo(spaceInformationPojo);

			if(savedSpaceInformationPojo!=null){
				//assertTrue(placesDetailsPojoList.get(0).getId()==2);
				assertTrue(savedSpaceInformationPojo.getSpaceCity().equals("Pune"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Test
	public void testUpdateSpaceInformationPojo(){
		try {
			SpaceInformationDao spaceInformationDao = new SpaceInformationDao();

			SpaceInformationPojo  spaceInformationPojo = new SpaceInformationPojo();

			spaceInformationPojo.setPageCount(8);
			spaceInformationPojo.setSpaceCity("Pune");
			spaceInformationPojo.setSpaceType("Restaurant");
			spaceInformationPojo.setId(1L);

			spaceInformationDao.updatePageCount(spaceInformationPojo);

			if(spaceInformationPojo!=null){
				//assertTrue(placesDetailsPojoList.get(0).getId()==2);
				assertTrue(spaceInformationPojo.getPageCount()==8);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Test
	public void testGetSpaceInformationPojoList(){
		try {
			SpaceInformationDao spaceInformationDao = new SpaceInformationDao();

			List<SpaceInformationPojo> spaceInformationList = spaceInformationDao.getAllSpaceInformationPojoList();

			if(spaceInformationList!=null){
				//assertTrue(placesDetailsPojoList.get(0).getId()==2);
				assertTrue(spaceInformationList.get(0).getPageCount()==1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
