package com.coolweather.app.model;

/* id������������
 * City�������
 * city_name������
 * city_code�м�����
 * province_id��City�����Province������
*/

/*ʹ��get��set������Ϊ�˳���ķ�װ��Ϊ�������������ʹ�ã����úͻ�ȡ�������˽�з�����
 * ����˵�����������һЩ��Ҫ��������ļ����������õĹ��̣�get set���������þ����������ˡ�
ͨ��get set������������ðѱ���˽�л���ֻ��¶������

*��ʽһ��Code-->Generate
��ʽ����ͨ����ݼ�Alt+Insert
��ʱ���ڵ����ĶԻ����оͿ���ѡ��������Ҫ�Ķ�Ӧ�Ķ����ˡ�
��ʽ����Shift+Alt+S �ᵯ��һ���Ի��� ѡ��Generate Getters and Setters ..
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

