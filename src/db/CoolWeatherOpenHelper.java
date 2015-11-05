package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;  


/*SQLiteOpenHelper是SQLiteDatabse的一个帮助类，用来管理数据的创建和版本更新。
 * 一般的用法是定义一个类继承SQLiteOpenHelper，并实现两个回调方法，
 * OnCreate(SQLiteDatabase db)和onUpgrade(SQLiteDatabse, int oldVersion, int newVersion)来创建和更新数据库
*/
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	/**
	 * * Province表建表语句
	 * id是自增长主键
	 * province_name省名
	 * province_code省级代号
	 * */
	public static final String CREATE_PROVINCE = "create table Province ("
	 + "id integer primary key autoincrement, "
	+ "province_name text, "
	 + "province_code text)";
	
	
	
	/**
	 * * City表建表语句
	 * city_name城市名
	 * city_code市级代号
	 * province_id是City表关联Province表的外键
	 * */
	public static final String CREATE_CITY = "create table City ("
	 + "id integer primary key autoincrement, "
	+ "city_name text, "
	 + "city_code text, "
	+ "province_id integer)";

	
	
	
	/**
	 * * County表建表语句
	 * county_name县名
	 * county_code县级代号
	 * city_idCounty表关联City表的外键
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
     * 当数据库首次创建时执行该方法，一般将创建表等初始化操作放在该方法中执行. 
     * 重写onCreate方法，调用execSQL方法创建表 
     * */ 
	@Override
	public void onCreate(SQLiteDatabase db) {     //在onCreate()方法中执行创建
		db.execSQL(CREATE_PROVINCE); // 创建Province表
		db.execSQL(CREATE_CITY); // 创建City表
		db.execSQL(CREATE_COUNTY); // 创建County表
		}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}