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
	* 用于显示城市名
	*/
	private TextView cityNameText;
	/**
	* 用于显示发布时间
	*/
	private TextView publishText;
	/**
	* 用于显示天气描述信息
	*/
	private TextView weatherDespText;
	/**
	* 用于显示气温1
	*/
	private TextView temp1Text;
	/**
	* 用于显示气温2
	*/
	private TextView temp2Text;
	/**
	* 用于显示当前日期
	*/
	private TextView currentDateText;
	/**
	* 切换城市按钮
	*/
	private Button switchCity;
	/**
	* 更新天气按钮
	*/
	private Button refreshWeather;
	/**
	* 进入搜索界面按钮按钮
	*/
	private Button searchCounty;
	/*
	 * 是否从SearchCountyAcitivity中跳转过来
	 **/
	private boolean isFromSearchCountyAcitivity;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.weather_layout);
		// 初始化各控件
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
		
     	String countyCode_FromChoose = getIntent().getStringExtra("county_code");//从Intent中取出县级代号
		String countyCode_FromSearch = getIntent().getStringExtra("countycode_FromSearch");//从Intent中取出县级代号
		isFromSearchCountyAcitivity=getIntent().getBooleanExtra("from_search_activity", false);//用于判断是否从seachCountyActivity转入WeatherActivity
		if(isFromSearchCountyAcitivity){    //如果是从SearchCountyAcitivity跳转过来的，就从SearchCountyAcitivity活动中取得城市代号
			if (!TextUtils.isEmpty(countyCode_FromSearch)) {
				// 有县级代号时就去查询天气，调用queryWeatherCode()
				publishText.setText("同步中...");
				weatherInfoLayout.setVisibility(View.INVISIBLE);
				cityNameText.setVisibility(View.INVISIBLE);
				queryWeatherCode(countyCode_FromSearch);
				} else {
					// 没有县级代号时就直接显示本地天气，调用showWeather()
					showWeather();
				}
		}else if(!isFromSearchCountyAcitivity){//如果不是从SearchCountyAcitivity跳转过来的，就从ChooseAreaAcitivity活动中取得城市代号
			if (!TextUtils.isEmpty(countyCode_FromChoose)) {
				// 有县级代号时就去查询天气，调用queryWeatherCode()
				publishText.setText("同步中...");
				weatherInfoLayout.setVisibility(View.INVISIBLE);
				cityNameText.setVisibility(View.INVISIBLE);
				queryWeatherCode(countyCode_FromChoose);
				} else {
					// 没有县级代号时就直接显示本地天气，调用showWeather()
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
			publishText.setText("同步中...");
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
	* 查询县级代号所对应的天气代号。
	*/
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city" + countyCode +".xml";//拼装地址
		queryFromServer(address, "countyCode");//查询县级代号所对应的天气代号
		}
	
	/**
	* 查询天气代号所对应的天气。
	*/
	private void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode +".html";//拼装地址
		queryFromServer(address, "weatherCode");//queryFromServer查询天气代号所对应的天气信息
	}
	
	/**
	* 根据传入的地址（如县级代号、天气代号等）和类型去向服务器查询天气代号或者天气信息。
	* 服务器返回的数据仍然会回调到onFinish()方法中，
	* 这里对返回的数据进行解析，然后将解析出来的天气代号传入到queryWeatherInfo()方法中。
	*/
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				if ("countyCode".equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						// 从服务器返回的数据中解析出天气代号
						String[] array = response.split("\\|");
						if (array != null && array.length == 2) {
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
							}
						}
					} else if ("weatherCode".equals(type)) {
						// 处理服务器返回的天气信息
						Utility.handleWeatherResponse(WeatherActivity.this, response);
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								showWeather();//将天气信息显示到界面上
								}
							});
						}
				}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						publishText.setText("同步失败");
						}
					});
				}
			});
		}
	/**
	* 从SharedPreferences文件中读取存储的天气信息，并显示到界面上。
	*/
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText( prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("今天" + prefs.getString("publish_time", "") + "发布");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}
}
