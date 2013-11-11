package com.example.jobviewcursor.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class JobDataBase extends SQLiteOpenHelper{
	
	private static final String DATABASE_NAME = "JobBase";
	private static final String TABLE_NAME = "Jobs";
	private static final int VERSION = 1;
	private static final String ROW_ID = "_id";
	private static final String TITLE = "Title";
	private static final String DATE = "Date";
	private static final String DESCRIPTION = "Description";
	
	private static final String CREATE_DB = 
			"CREATE TABLE " + TABLE_NAME + "("+
			ROW_ID + " integer PRIMARY KEY autoincrement," +
			TITLE + "," +
			DATE + "," +
			DESCRIPTION +");";

	public JobDataBase(Context context, String name, CursorFactory factory,
			int version) {
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
