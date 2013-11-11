package com.example.jobviewcursor.database;

import java.util.ArrayList;

import com.example.jobviewcursor.model.Job;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class JobDataBaseAdapter {
	
	private static final String DATABASE_NAME = "JobBase";
	private static final String TABLE_NAME = "Jobs";
	private static final int VERSION = 1;
	
	private JobDataBase jobDBHelper;
	private SQLiteDatabase jobBase;
	
	public JobDataBaseAdapter(Context context){
		jobDBHelper = new JobDataBase(context, DATABASE_NAME, null, VERSION);
	}
	public void openDB() throws SQLException{
		jobBase = jobDBHelper.getWritableDatabase();
	}
	public void closeDB(){
		if(jobBase != null){
			jobBase.close();
		}
	}
	public void insertData(ArrayList<Job> jobs){
		openDB();
		ContentValues contValues = new ContentValues();
		for(int i = 0; i<jobs.size();i++){
			contValues.put("Title", jobs.get(i).getTitle());
			contValues.put("Date", jobs.get(i).getDate());
			contValues.put("Description", jobs.get(i).getDescription());
			jobBase.insert("Jobs", null, contValues);
		}
		closeDB();
	}
	public Cursor getAllJobs(){
		Cursor cursor = null;
		cursor = jobBase.rawQuery("SELECT * from "+TABLE_NAME,null);
		if(cursor != null)
			cursor.moveToFirst();
		return cursor;
	}
	public void deleteItems(){
		openDB();
		jobBase.execSQL("delete from "+TABLE_NAME);
		closeDB();
	}
}
