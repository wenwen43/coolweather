package com.coolweather.app.activity;

import com.coolweather.app.R;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener{

	private LinearLayout weatherInfoLayout;
	/**
	* ������ʾ������
	*/
	private TextView cityNameText;
	/**
	* ������ʾ����ʱ��
	*/
	private TextView publishText;
	/**
	* ������ʾ����������Ϣ
	*/
	private TextView weatherDespText;
	/**
	* ������ʾ����1
	*/
	private TextView temp1Text;
	/**
	* ������ʾ����2
	*/
	private TextView temp2Text;
	/**
	* ������ʾ��ǰ����
	*/
	private TextView currentDateText;
	/**
	* �л����а�ť
	*/
	private Button switchCity;
	/**
	* ����������ť
	*/
	private Button refreshWeather;
	/**
	* �����������水ť��ť
	*/
	private Button searchCounty;
	/*
	 * �Ƿ��SearchCountyAcitivity����ת����
	 **/
	private boolean isFromSearchCountyAcitivity;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
		setContentView(R.layout.weather_layout);
		// ��ʼ�����ؼ�
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		switchCity = (Button) findViewById(R.id.switch_city);
     	refreshWeather = (Button) findViewById(R.id.refresh_weather);
     	searchCounty = (Button) findViewById(R.id.search_city);
		
     	String countyCode_FromChoose = getIntent().getStringExtra("county_code");//��Intent��ȡ���ؼ�����
		String countyCode_FromSearch = getIntent().getStringExtra("countycode_FromSearch");//��Intent��ȡ���ؼ�����
		isFromSearchCountyAcitivity=getIntent().getBooleanExtra("from_search_activity", false);//�����ж��Ƿ��seachCountyActivityת��WeatherActivity
		if(isFromSearchCountyAcitivity){    //����Ǵ�SearchCountyAcitivity��ת�����ģ��ʹ�SearchCountyAcitivity���ȡ�ó��д���
			if (!TextUtils.isEmpty(countyCode_FromSearch)) {
				// ���ؼ�����ʱ��ȥ��ѯ����������queryWeatherCode()
				publishText.setText("ͬ����...");
				weatherInfoLayout.setVisibility(View.INVISIBLE);
				cityNameText.setVisibility(View.INVISIBLE);
				queryWeatherCode(countyCode_FromSearch);
				} else {
					// û���ؼ�����ʱ��ֱ����ʾ��������������showWeather()
					showWeather();
				}
		}else if(!isFromSearchCountyAcitivity){//������Ǵ�SearchCountyAcitivity��ת�����ģ��ʹ�ChooseAreaAcitivity���ȡ�ó��д���
			if (!TextUtils.isEmpty(countyCode_FromChoose)) {
				// ���ؼ�����ʱ��ȥ��ѯ����������queryWeatherCode()
				publishText.setText("ͬ����...");
				weatherInfoLayout.setVisibility(View.INVISIBLE);
				cityNameText.setVisibility(View.INVISIBLE);
				queryWeatherCode(countyCode_FromChoose);
				} else {
					// û���ؼ�����ʱ��ֱ����ʾ��������������showWeather()
					showWeather();
				}
		}
			

			
		
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		searchCounty.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.search_city:			
			Intent intent2 = new Intent(this, SearchCountyActivity.class);
			//intent2.putExtra("from_search_activity2", true);
			startActivity(intent2);
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("ͬ����...");
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			String weatherCode = prefs.getString("weather_code", "");
			if (!TextUtils.isEmpty(weatherCode)) {
				queryWeatherInfo(weatherCode);
			}
		 break;
		 default:
		 break;
		 }
	}


	/**
	* ��ѯ�ؼ���������Ӧ���������š�
	*/
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city" + countyCode +".xml";//ƴװ��ַ
		queryFromServer(address, "countyCode");//��ѯ�ؼ���������Ӧ����������
		}
	
	/**
	* ��ѯ������������Ӧ��������
	*/
	private void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode +".html";//ƴװ��ַ
		queryFromServer(address, "weatherCode");//queryFromServer��ѯ������������Ӧ��������Ϣ
	}
	
	/**
	* ���ݴ���ĵ�ַ�����ؼ����š��������ŵȣ�������ȥ���������ѯ�������Ż���������Ϣ��
	* ���������ص�������Ȼ��ص���onFinish()�����У�
	* ����Է��ص����ݽ��н�����Ȼ�󽫽����������������Ŵ��뵽queryWeatherInfo()�����С�
	*/
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				if ("countyCode".equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						// �ӷ��������ص������н�������������
						String[] array = response.split("\\|");
						if (array != null && array.length == 2) {
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
							}
						}
					} else if ("weatherCode".equals(type)) {
						// ������������ص�������Ϣ
						Utility.handleWeatherResponse(WeatherActivity.this, response);
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								showWeather();//��������Ϣ��ʾ��������
								}
							});
						}
				}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						publishText.setText("ͬ��ʧ��");
						}
					});
				}
			});
		}
	/**
	* ��SharedPreferences�ļ��ж�ȡ�洢��������Ϣ������ʾ�������ϡ�
	*/
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText( prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("����" + prefs.getString("publish_time", "") + "����");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}
}
