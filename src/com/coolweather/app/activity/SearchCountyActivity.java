package com.coolweather.app.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.coolweather.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SearchCountyActivity extends Activity implements OnClickListener{
	
	/**
	* 用于显示城市名
	*/
	private TextView TitleNameCounty;
	/**
	* 用于查询城市代码按钮，查看城市xml文件
	*/
	private EditText editTextCountyName;
	/**
	* 用于查询城市代码按钮，查看城市xml文件
	*/
	private Button searchCodeButton;
	/**
	* 用于查询城市天气，跳转WeatherActivity
	*/
	private EditText editTextCountyCode;
	/**
	* 用于查询城市天气，跳转WeatherActivity
	*/
	private Button searchWeatherButton;
	/**
	* 用于标记查询的县城，并通过intent传给活动WeatherActivity，来查询天气
	*/
	private String countyname;
	/**
	* 用于显示城市的代码信息
	*/
	private TextView countyWeather;
	/**
	* 进入切换城市界面按钮
	*/
	private Button switchCity2;
	

	Handler mHandler;
	URL url;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.search_area);
		
		// 初始化各控件
		TitleNameCounty = (TextView) findViewById(R.id.search_county_name);
		editTextCountyName = (EditText) findViewById(R.id.place_edit);
		searchCodeButton = (Button) findViewById(R.id.search_button);
		countyWeather = (TextView) findViewById(R.id.county_weather);
		editTextCountyCode = (EditText) findViewById(R.id.countycode_edit);
		searchWeatherButton = (Button) findViewById(R.id.weathersearch_button);
		switchCity2 = (Button) findViewById(R.id.switch_city2);
		//获取SharedPreferences实例，读写文件，方式为独占
	//  sf = getSharedPreferences("/res/countycode_data/countycode_File",Context.MODE_PRIVATE);
		
		searchCodeButton.setOnClickListener(this);
		searchWeatherButton.setOnClickListener(this);
		switchCity2.setOnClickListener(this);

		
		
		
		mHandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.what==0x100){
			//		countyWeather.setText(code);
					Bundle b =msg.getData();
					countyWeather.append(b.getString("msg"));//setText()把以前的内容冲掉了,append()在以前的内容后面添加
					
				//	String countyname_FromSearch = code.getString("tem1","none");
				//	Toast.makeText(SearchCountyActivity.this, "天气代码获取完成", Toast.LENGTH_SHORT).show();
				}else if(msg.what==0x200){
					
				//	Toast.makeText(SearchCountyActivity.this,"天气获取失败", Toast.LENGTH_SHORT).show();
				}
			}
		};	//Handler end
	}//onCreat end

	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_button:
			countyname = editTextCountyName.getText().toString();//获取edittext中填写的内容
			TitleNameCounty.setText(countyname);//在首栏显示查询的城市
			new Thread(new Runnable(){                  //子线程
				@Override
				public void run() {
					try {
						//
						String str="http://flash.weather.com.cn/wmaps/xml/"+countyname+".xml";//获取城市的县级天气xml文件
						URL url_city=new URL(str);
						//open web connection and get return stream
						InputStream is =url_city.openStream(); 
					//	TitleNameCounty.setText(str);//在首栏显示查询的城市						
						//convert input data to bitmap, save
						URLConnection nn = url_city.openConnection();
						nn.setDoInput(true);//获取输入流，读取URL返回的资源
						nn.connect();//连接							
						BufferedReader br = new BufferedReader(new InputStreamReader(nn.getInputStream()));
						String msg="";
					//	code = br.readLine();//读取一行
					//	code = br.readLine();//再读取一行
						while (true) {
					          msg=br.readLine();//按行读取
					          
					          if (msg.isEmpty()) {
					        	  break;
					          }
					          /*
					          if (msg == null) {
					        	  break;
					          }*/
					          Bundle b=new Bundle();//Bundle用于打包数据
					          b.putString("msg", msg);//用putString(标记，数据)来将数据导入到Bundle对象中
					          Message m =new Message();
					          m.setData(b);//然后将Bundle对象导入到Message对象中
					          m.what = 0x100;
					          mHandler.sendMessage(m);//用handle发送一个message
					    }
					    br.close();
					    Toast.makeText(SearchCountyActivity.this, "天气代码获取完成", Toast.LENGTH_LONG).show();	
					//	String countyname_FromSearch = str.getString("cityname","");
						//inform handler to update GUI
						mHandler.sendEmptyMessage(0x100);
						
						is.close();
						
					} catch (Exception e) {
						mHandler.sendEmptyMessage(0x200);
					
					}
				}
			}).start();
			break;
		case R.id.weathersearch_button:
			String countycode_FromSearch = editTextCountyCode.getText().toString();//获取edittext中填写的内容
			Intent intent = new Intent(this, WeatherActivity.class);
			intent.putExtra("from_search_activity", true);//用于判断是否从seachCountyActivity转入WeatherActivity
			intent.putExtra("countycode_FromSearch", countycode_FromSearch);		
			startActivity(intent);	
			break;
		
		case R.id.switch_city2:
			Intent intent2 = new Intent(this, ChooseAreaActivity.class);
			startActivity(intent2);	
			break;
		}
	}
	
	
}

