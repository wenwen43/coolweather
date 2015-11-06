package com.coolweather.app.model;

/* id是自增长主键
 * County表建表语句
 * county_name县名
 * county_code县级代号
 * city_idCounty表关联City表的外键
*/


public class County {
	private int id;
	private String countyName;
	private String countyCode;
	private int cityId;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCountyName() {
		return countyName;
	}
	
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	
	public String getCountyCode() {
		return countyCode;
	}
	
	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}
	
	public int getCityId() {
		return cityId;
	}
	
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
}

