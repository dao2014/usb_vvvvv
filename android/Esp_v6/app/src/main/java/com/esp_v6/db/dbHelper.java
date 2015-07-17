package com.esp_v6.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {

	private final static String DATABASE_NAME="my_db";
	private final static int DATABASE_VERSION=1;
	private final static String TABLE_NAME="rt_table";
	public final static String FIELD_ID="id";
	public final static String FIELD_T1="t1";
	public final static String FIELD_RT="rt";


	/**
	 * 初始化数据
	 * @param db
	 */
	public void addDate(SQLiteDatabase db){
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (1, -20, 75.0217)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (2, -19, 71.1822)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (4, -18, 67.567)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (5, -17, 64.1615)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (6, -16, 60.9521)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (7, -15, 57.9264)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (8, -14, 55.0724)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (9, -13, 52.3794)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (10, -12, 49.8373)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (11, -11, 47.4365)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (12, -10, 45.1683)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (13, -9, 43.0245)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (14, -8, 40.9975)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (15, -7, 39.0802)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (16, -6, 37.2659)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (17, -5, 35.5484)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (18, -4, 33.922)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (19, -3, 32.3812)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (20, -2, 30.921)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (21, -1, 29.5366)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (22, 0, 28.2237)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (23, 1, 26.9781)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (24, 2, 25.796)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (25, 3, 24.6736)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (26, 4, 23.6077)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (27, 5, 22.5949)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (28, 6, 21.6325)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (29, 7, 20.7174)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (30, 8, 19.8472)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (31, 9, 19.0193)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (32, 10, 18.2314)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (33, 11, 17.4814)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (34, 12, 16.7671)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (35, 13, 16.0868)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (36, 14, 15.4384)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (37, 15, 14.8205)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (38, 16, 14.2313)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (39, 17, 13.6694)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (40, 18, 13.1332)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (41, 19, 12.6216)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (42, 20, 12.1332)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (43, 21, 11.6668)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (44, 22, 11.2213)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (45, 23, 10.7957)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (46, 24, 10.3889)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (47, 25, 10)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (48, 26, 9.62813)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (49, 27, 9.27243)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (50, 28, 8.93211)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (51, 29, 8.6064)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (52, 30, 8.29461)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (53, 31, 7.99605)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (54, 32, 7.71009)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (55, 33, 7.43612)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (56, 34, 7.17358)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (57, 35, 6.92192)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (58, 36, 6.68064)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (59, 37, 6.44924)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (60, 38, 6.22727)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (61, 39, 6.01428)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (62, 40, 5.80987)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (63, 41, 5.61365)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (64, 42, 5.42523)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (65, 43, 5.24428)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (66, 44, 5.07044)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (67, 45, 4.9034)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (68, 46, 4.74286)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (69, 47, 4.58853)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (70, 48, 4.44014)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (71, 49, 4.29743)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (72, 50, 4.16014)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (73, 51, 4.02804)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (74, 52, 3.90092)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (75, 53, 3.77855)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (76, 54, 3.66073)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (77, 55, 3.54727)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (78, 56, 3.43798)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (79, 57, 3.33269)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (80, 58, 3.23124)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (81, 59, 3.13345)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (82, 60, 3.03919)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (83, 61, 2.9483)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (84, 62, 2.86064)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (85, 63, 2.77609)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (86, 64, 2.69452)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (87, 65, 2.61581)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (88, 66, 2.53984)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (89, 67, 2.4665)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (90, 68, 2.3957)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (91, 69, 2.32732)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (92, 70, 2.26128)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (93, 71, 2.19747)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (94, 72, 2.13583)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (95, 73, 2.07625)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (96, 74, 2.01866)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (97, 75, 1.96299)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (98, 76, 1.90916)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (99, 77, 1.8571)");
		db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (100, 78, 1.80674)");


	}
	
	public dbHelper(Context context)
	{
		super(context, DATABASE_NAME,null, DATABASE_VERSION);
	}
	
	
	 
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql="Create table "+TABLE_NAME+"("+FIELD_ID+" integer primary key autoincrement,"
		+FIELD_T1+" REAL,"+FIELD_RT+" REAL );";
		db.execSQL(sql);
		//db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (1, -, ?)");
		addDate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql=" DROP TABLE IF EXISTS "+TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}


	/**
	 * 查询 大约 RT 的最近的那个
	 * @param Rt
	 * @return
	 */
	public double selectMax(String Rt,SQLiteDatabase db){

		Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where rt <= ?  order by rt desc limit 1 ", new String[]{Rt});
		double t1s = 0.0;
		if(c.moveToFirst()) {
			t1s= c.getDouble(c.getColumnIndex("t1"));
		}
		return t1s;
	}

	/**
	 * 获取最小的只
	 * @param Rt
	 * @param db
	 * @return
	 */
	public double selectMin(String Rt,SQLiteDatabase db){

		Cursor c = db.rawQuery("select * from "+TABLE_NAME+" where rt >= ? desc limit 1",new String[]{Rt});
		double t1s = 0.0;
		if(c.moveToFirst()) {
			t1s= c.getDouble(c.getColumnIndex("t1"));

		}
		return t1s;
	}
	

	
	
}
