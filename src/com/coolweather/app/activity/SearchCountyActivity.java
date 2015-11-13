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
	* ������ʾ������
	*/
	private TextView TitleNameCounty;
	/**
	* ���ڲ�ѯ���д��밴ť���鿴����xml�ļ�
	*/
	private EditText editTextCountyName;
	/**
	* ���ڲ�ѯ���д��밴ť���鿴����xml�ļ�
	*/
	private Button searchCodeButton;
	/**
	* ���ڲ�ѯ������������תWeatherActivity
	*/
	private EditText editTextCountyCode;
	/**
	* ���ڲ�ѯ������������תWeatherActivity
	*/
	private Button searchWeatherButton;
	/**
	* ���ڱ�ǲ�ѯ���سǣ���ͨ��intent�����WeatherActivity������ѯ����
	*/
	private String countyname;
	/**
	* ������ʾ���еĴ�����Ϣ
	*/
	private TextView countyWeather;
	/**
	* �����л����н��水ť
	*/
	private Button switchCity2;
	

	Handler mHandler;
	URL url;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
		setContentView(R.layout.search_area);
		
		// ��ʼ�����ؼ�
		TitleNameCounty = (TextView) findViewById(R.id.search_county_name);
		editTextCountyName = (EditText) findViewById(R.id.place_edit);
		searchCodeButton = (Button) findViewById(R.id.search_button);
		countyWeather = (TextView) findViewById(R.id.county_weather);
		editTextCountyCode = (EditText) findViewById(R.id.countycode_edit);
		searchWeatherButton = (Button) findViewById(R.id.weathersearch_button);
		switchCity2 = (Button) findViewById(R.id.switch_city2);
		//��ȡSharedPreferencesʵ������д�ļ�����ʽΪ��ռ
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
					countyWeather.append(b.getString("msg"));//setText()����ǰ�����ݳ����,append()����ǰ�����ݺ������
					
				//	String countyname_FromSearch = code.getString("tem1","none");
				//	Toast.makeText(SearchCountyActivity.this, "���������ȡ���", Toast.LENGTH_SHORT).show();
				}else if(msg.what==0x200){
					
				//	Toast.makeText(SearchCountyActivity.this,"������ȡʧ��", Toast.LENGTH_SHORT).show();
				}
			}
		};	//Handler end
	}//onCreat end

	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_button:
			countyname = editTextCountyName.getText().toString();//��ȡedittext����д������
			TitleNameCounty.setText(countyname);//��������ʾ��ѯ�ĳ���
			new Thread(new Runnable(){                  //���߳�
				@Override
				public void run() {
					try {
						//
						String str="http://flash.weather.com.cn/wmaps/xml/"+countyname+".xml";//��ȡ���е��ؼ�����xml�ļ�
						URL url_city=new URL(str);
						//open web connection and get return stream
						InputStream is =url_city.openStream(); 
					//	TitleNameCounty.setText(str);//��������ʾ��ѯ�ĳ���						
						//convert input data to bitmap, save
						URLConnection nn = url_city.openConnection();
						nn.setDoInput(true);//��ȡ����������ȡURL���ص���Դ
						nn.connect();//����							
						BufferedReader br = new BufferedReader(new InputStreamReader(nn.getInputStream()));
						String msg="";
					//	code = br.readLine();//��ȡһ��
					//	code = br.readLine();//�ٶ�ȡһ��
						while (true) {
					          msg=br.readLine();//���ж�ȡ
					          
					          if (msg.isEmpty()) {
					        	  break;
					          }
					          /*
					          if (msg == null) {
					        	  break;
					          }*/
					          Bundle b=new Bundle();//Bundle���ڴ������
					          b.putString("msg", msg);//��putString(��ǣ�����)�������ݵ��뵽Bundle������
					          Message m =new Message();
					          m.setData(b);//Ȼ��Bundle�����뵽Message������
					          m.what = 0x100;
					          mHandler.sendMessage(m);//��handle����һ��message
					    }
					    br.close();
					    Toast.makeText(SearchCountyActivity.this, "���������ȡ���", Toast.LENGTH_LONG).show();	
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
			String countycode_FromSearch = editTextCountyCode.getText().toString();//��ȡedittext����д������
			Intent intent = new Intent(this, WeatherActivity.class);
			intent.putExtra("from_search_activity", true);//�����ж��Ƿ��seachCountyActivityת��WeatherActivity
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

