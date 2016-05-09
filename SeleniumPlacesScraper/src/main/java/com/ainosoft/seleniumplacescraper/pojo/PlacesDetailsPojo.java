package com.ainosoft.seleniumplacescraper.pojo;
// default package
// Generated 5 May, 2016 3:02:05 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PlacesDetails generated by hbm2java
 */
@Entity
@Table(name = "PlacesDetails", catalog = "SeleniumPlacesScraper")
public class PlacesDetailsPojo implements java.io.Serializable {

	private Long id;
	private String placeName;
	private String placeType;
	private String placeCity;
	private String placeAddress;
	private String placeWebsite;
	private String placePhoneNo;
	private String longitude;
	private String latitude;
	private String placeUrl;
	private String image;
	private String rating;
	private String webElement;
	private String timings;
	private Date createdOn;
	private Date modifiedOn;

	public PlacesDetailsPojo() {
	}

	public PlacesDetailsPojo(String placeName, String placeType, String placeCity,
			String placeAddress, String placeWebsite, String placePhoneNo,
			String longitude, String latitude, String placeUrl, String image,
			String rating, String webElement, String timings, Date createdOn,
			Date modifiedOn) {
		this.placeName = placeName;
		this.placeType = placeType;
		this.placeCity = placeCity;
		this.placeAddress = placeAddress;
		this.placeWebsite = placeWebsite;
		this.placePhoneNo = placePhoneNo;
		this.longitude = longitude;
		this.latitude = latitude;
		this.placeUrl = placeUrl;
		this.image = image;
		this.rating = rating;
		this.webElement = webElement;
		this.timings = timings;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "place_name", length = 100)
	public String getPlaceName() {
		return this.placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	@Column(name = "place_type", length = 45)
	public String getPlaceType() {
		return this.placeType;
	}

	public void setPlaceType(String placeType) {
		this.placeType = placeType;
	}

	@Column(name = "place_city", length = 45)
	public String getPlaceCity() {
		return this.placeCity;
	}

	public void setPlaceCity(String placeCity) {
		this.placeCity = placeCity;
	}

	@Column(name = "place_address")
	public String getPlaceAddress() {
		return this.placeAddress;
	}

	public void setPlaceAddress(String placeAddress) {
		this.placeAddress = placeAddress;
	}

	@Column(name = "place_website")
	public String getPlaceWebsite() {
		return this.placeWebsite;
	}

	public void setPlaceWebsite(String placeWebsite) {
		this.placeWebsite = placeWebsite;
	}

	@Column(name = "place_phone_no", length = 45)
	public String getPlacePhoneNo() {
		return this.placePhoneNo;
	}

	public void setPlacePhoneNo(String placePhoneNo) {
		this.placePhoneNo = placePhoneNo;
	}

	@Column(name = "longitude", length = 50)
	public String getLongitude() {
		return this.longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@Column(name = "latitude", length = 50)
	public String getLatitude() {
		return this.latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	@Column(name = "place_url")
	public String getPlaceUrl() {
		return this.placeUrl;
	}

	public void setPlaceUrl(String placeUrl) {
		this.placeUrl = placeUrl;
	}

	@Column(name = "image", length = 1000)
	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Column(name = "rating", length = 45)
	public String getRating() {
		return this.rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	@Column(name = "web_element", length = 65535)
	public String getWebElement() {
		return this.webElement;
	}

	public void setWebElement(String webElement) {
		this.webElement = webElement;
	}

	@Column(name = "timings", length = 50)
	public String getTimings() {
		return this.timings;
	}

	public void setTimings(String timings) {
		this.timings = timings;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", length = 19)
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_on", length = 19)
	public Date getModifiedOn() {
		return this.modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

}
