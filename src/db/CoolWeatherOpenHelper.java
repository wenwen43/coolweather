package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;  


/*SQLiteOpenHelper��SQLiteDatabse��һ�������࣬�����������ݵĴ����Ͱ汾���¡�
 * һ����÷��Ƕ���һ����̳�SQLiteOpenHelper����ʵ�������ص�������
 * OnCreate(SQLiteDatabase db)��onUpgrade(SQLiteDatabse, int oldVersion, int newVersion)�������͸������ݿ�
*/
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	/**
	 * * Province�������
	 * id������������
	 * province_nameʡ��
	 * province_codeʡ������
	 * */
	public static final String CREATE_PROVINCE = "create table Province ("
	 + "id integer primary key autoincrement, "
	+ "province_name text, "
	 + "province_code text)";
	
	
	
	/**
	 * * City�������
	 * city_name������
	 * city_code�м�����
	 * province_id��City�����Province������
	 * */
	public static final String CREATE_CITY = "create table City ("
	 + "id integer primary key autoincrement, "
	+ "city_name text, "
	 + "city_code text, "
	+ "province_id integer)";

	
	
	
	/**
	 * * County�������
	 * county_name����
	 * county_code�ؼ�����
	 * city_idCounty�����City������
	 * */
	public static final String CREATE_COUNTY = "create table County ("
	 + "id integer primary key autoincrement, "
	+ "county_name text, "
	 + "county_code text, "
	+ "city_id integer)";

	
	public CoolWeatherOpenHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
		}

	
	 /** 
     * �����ݿ��״δ���ʱִ�и÷�����һ�㽫������ȳ�ʼ���������ڸ÷�����ִ��. 
     * ��дonCreate����������execSQL���������� 
     * */ 
	@Override
	public void onCreate(SQLiteDatabase db) {     //��onCreate()������ִ�д���
		db.execSQL(CREATE_PROVINCE); // ����Province��
		db.execSQL(CREATE_CITY); // ����City��
		db.execSQL(CREATE_COUNTY); // ����County��
		}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}