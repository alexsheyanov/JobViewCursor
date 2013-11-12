package com.example.jobviewcursor.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JobDataBase extends SQLiteOpenHelper{
	
	public static final String DATABASE_NAME = "JobBase";
	public static final String TABLE_NAME = "Jobs";
	public static final int VERSION = 1;
	public static final String ROW_ID = "_id";
	public static final String TITLE = "Title";
	public static final String DATE = "Date";
	public static final String DESCRIPTION = "Description";
	
	private static final String CREATE_DB = 
			"CREATE TABLE " + TABLE_NAME + "("+
			ROW_ID + " integer PRIMARY KEY autoincrement," +
			TITLE + "," +
			DATE + "," +
			DESCRIPTION +");";

	public JobDataBase(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_DB);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
