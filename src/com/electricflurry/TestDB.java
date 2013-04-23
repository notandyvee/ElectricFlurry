package com.electricflurry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TestDB {
	public static final String KEY_ID = "id";
	public static final String KEY_USERNAME = "name";
	public static final String KEY_PHONE = "phone";
	public static final String KEY_FACEBOOK = "facebook";
	public static final String KEY_TWITTER = "twitter";
	public static final String KEY_GOOGLE = "google";
	private static final String TAG = "TestDB";
	
	private static final String DB_NAME = "ElectricFlurryDB";
	private static final String DB_TABLE = "users";
	private static final int DB_VERSION = 1;
	
	private static final String DB_CREATE = 
				"create table if not exists users ( id integer primary key autoincrement, "
				+ "name VARCHAR not null , phone VARCHAR, facebook VARCHAR, twitter VARCHAR, google VARCHAR);";

	private final Context context;
	
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public TestDB(Context context) {
		this.context = context;
		DBHelper = new DatabaseHelper(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper (Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			try {
				db.execSQL(DB_CREATE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + "which will destroy all old data.");
			db.execSQL("DROP TABLE IF EXISTS users");
			onCreate(db);
		}
		
	}
		public TestDB open() throws SQLException {
			db = DBHelper.getWritableDatabase();
			return this;
		}
		
		public void close() {
			DBHelper.close();
	}
	
		public long insertUser(String user, String phone, String facebook, String twitter, String google) {
			ContentValues values = new ContentValues();
			values.put(KEY_USERNAME, user);
			values.put(KEY_PHONE, phone);
			values.put(KEY_FACEBOOK, facebook);
			values.put(KEY_TWITTER, twitter);
			values.put(KEY_GOOGLE, google);
			
			return db.insert(DB_TABLE, null, values);
		}
		
		public Cursor getUser(long id) throws SQLException {
			Cursor c = db.query(true, DB_TABLE, new String[] {KEY_ID, KEY_USERNAME,
					KEY_PHONE, KEY_FACEBOOK, KEY_TWITTER, KEY_GOOGLE}, KEY_ID + "=" + id, null, null, null, null, null);
			if (c != null) {
				c.moveToFirst();
			}
			return c;
			}
		
		public boolean updateUser(long id, String user, String phone, String facebook, String twitter, String google) {
			ContentValues values = new ContentValues();
			values.put(KEY_USERNAME, user);
			values.put(KEY_PHONE, phone);
			values.put(KEY_FACEBOOK, facebook);
			values.put(KEY_TWITTER, twitter);
			values.put(KEY_GOOGLE, google);
			return db.update(DB_TABLE, values, KEY_ID + "=" + id, null) > 0;
		}
		}
