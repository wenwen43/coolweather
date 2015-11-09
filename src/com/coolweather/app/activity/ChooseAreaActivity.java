package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/* ArrayAdapter只能显示基本的字符串 
* SimpleAdapter能够显示自定义视图*/
public class ChooseAreaActivity extends Activity {
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList = new ArrayList<String>();//初始化ArryAdapter
	/**
	* 省列表
	*/
	private List<Province> provinceList;
	/**
	* 市列表
	*/
	private List<City> cityList;
	/**
	* 县列表
	*/
	private List<County> countyList;
	/**
	* 选中的省份
	*/
	private Province selectedProvince;
	/**
	* 选中的城市
	*/
	private City selectedCity;
	/**
	* 当前选中的级别
	*/
	private int currentLevel;
	
	/*
	 * 是否从WeatherAcitivity中跳转过来
	 **/
	private boolean isFromWeatherActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isFromWeatherActivity=getIntent().getBooleanExtra("from_weather_activity", false);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		//已经选择了城市且不是从WeatherActivity跳转过来，才会直接跳转到WeatherActivity
		if (prefs.getBoolean("city_selected", false) && !isFromWeatherActivity) {
			Intent intent = new Intent(this, WeatherActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);//初始化ArrayAdapter，将其设置为ListView的适配器
		listView.setAdapter(adapter);//将ArrayAdapter的值映射到ListView中
		coolWeatherDB = CoolWeatherDB.getInstance(this);//获取CoolWeatherDB的实例
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,long arg3) {
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(index);
					queryCities();
					} else if (currentLevel == LEVEL_CITY) {
						selectedCity = cityList.get(index);
						queryCounties();
					} else if (currentLevel == LEVEL_COUNTY) {
						String countyCode = countyList.get(index).getCountyCode();
						Intent intent = new Intent(ChooseAreaActivity.this,WeatherActivity.class);
						intent.putExtra("county_code", countyCode);
						startActivity(intent);
						finish();
					}
				}
			});
		queryProvinces(); // 加载省级数据
		}
	
	/**
	* 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询。
	*/
	private void queryProvinces() {
		provinceList = coolWeatherDB.loadProvinces();//从数据库读取到省级数据
		if (provinceList.size() > 0) {      //若读取到省级数据，就直接将数据显示到界面上
			dataList.clear();
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
			} else {
				queryFromServer(null, "province");//若没有读取到，就调用queryFromServer()方法来从服务器上查询数据
			}
	}

	/**
	* 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询。
	*/
	private void queryCities() {
		cityList = coolWeatherDB.loadCities(selectedProvince.getId());
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityName());
				}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
			} else {
				queryFromServer(selectedProvince.getProvinceCode(), "city");
				}
	}

	/**
	* 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询。
	*/
	private void queryCounties() {
		countyList = coolWeatherDB.loadCounties(selectedCity.getId());
		if (countyList.size() > 0) {
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyName());
				}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
			} else {
				queryFromServer(selectedCity.getCityCode(), "county");
				}
		}


	/**
	* 根据传入的代号和类型从服务器上查询省市县数据。
	*/
	private void queryFromServer(final String code, final String type) {
		String address;
		//根据传入的参数拼装查询地址
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
			} else {
				address = "http://www.weather.com.cn/data/list3/city.xml";
			}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {       //调用HttpUtil的sendHttpRequest()方法来向服务器发送请求，相应的数据回调到onFinish()方法中

			@Override
			public void onFinish(String response) {
				boolean result = false;
				if ("province".equals(type)) {
					result = Utility.handleProvincesResponse(coolWeatherDB,response);//调用Utility的handleProvincesResponse()方法来解析和处理服务器返回的数据，并存储到数据库中
					} else if ("city".equals(type)) {
						result = Utility.handleCitiesResponse(coolWeatherDB,
								response, selectedProvince.getId());
						} else if ("county".equals(type)) {
							result = Utility.handleCountiesResponse(coolWeatherDB,
									response, selectedCity.getId());
							}
				if (result) {
					// 通过runOnUiThread()方法回到主线程处理逻辑（因为queryProvinces()方法牵扯到UI操作，因此必须在主线程中调用）
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();//因为数据库已存在了数据，因此调用queryProvinces()就会直接将数据显示到界面上
								} else if ("city".equals(type)) {
									queryCities();
									} else if ("county".equals(type)) {
										queryCounties();
										}
							}
						});
					}
				}
			@Override
			public void onError(Exception e) {
				// 通过runOnUiThread()方法回到主线程处理逻辑
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this,"加载失败", Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
		}
	
	
	/**
	* 显示进度对话框
	*/
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
			}
		progressDialog.show();
		}
	/**
	* 关闭进度对话框
	*/
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			}
		}
	/**
	 * * 捕获Back按键，根据当前的级别来判断，此时应该返回市列表、省列表、还是直接退出。
	 * */
	@Override
	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTY) {
			queryCities();
			} else if (currentLevel == LEVEL_CITY) {
				queryProvinces();
				} else {
					if (isFromWeatherActivity) {
						Intent intent = new Intent(this, WeatherActivity.class);
						startActivity(intent);
					}
					finish();
					}
		}
	
}
