package com.coolweather.app.model;

/* id是自增长主键
 * City表建表语句
 * city_name城市名
 * city_code市级代号
 * province_id是City表关联Province表的外键
*/

/*使用get和set方法是为了程序的封装，为了其它的类可以使用（设置和获取）该类的私有方法。
 * 比如说反射或是其他一些需要结合配置文件才能起作用的工程，get set方法的作用就明显起来了。
通过get set方法，你可以让把变量私有化，只暴露方法。

*方式一：Code-->Generate
方式二：通过快捷键Alt+Insert
这时，在弹出的对话框中就可以选择你所需要的对应的东东了。
方式三：Shift+Alt+S 会弹出一个对话框 选择Generate Getters and Setters ..
*/

public class City {
	private int id;
	private String cityName;
	private String cityCode;
	private int provinceId;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCityName() {
		return cityName;
	}
	
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getCityCode() {
		return cityCode;
	}
	
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	public int getProvinceId() {
		return provinceId;
	}
	
	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
}

