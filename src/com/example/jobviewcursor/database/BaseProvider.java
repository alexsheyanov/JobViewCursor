package com.example.jobviewcursor.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class BaseProvider extends ContentProvider{
	
	public static final String AUTHORITY = "com.example.jobviewcursor.provider.JobBase";
	public static final String JOB_PATH = "jobs";
	public static final Uri JOBS_URI = Uri.parse("content://"+AUTHORITY+"/"+JOB_PATH);
	public static final String JOBS_TYPE = "vnd.android.cursor.dir/vnd."+AUTHORITY+"."+JOB_PATH;
	public static final String JOBS_ITEM_TYPE = "vnd.android.cursor.item/vnd."+AUTHORITY+"."+JOB_PATH;

	public static final int URI_JOB = 1;
	public static final int URI_JOB_ID = 2;
	
	private JobDataBase jobDBHelper;
	private SQLiteDatabase jobBase;
	
	private static final UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, JOB_PATH, URI_JOB);
		uriMatcher.addURI(AUTHORITY, JOB_PATH+"/#", URI_JOB_ID);
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		jobBase = jobDBHelper.getWritableDatabase();
		switch(uriMatcher.match(uri)){
		case URI_JOB:
			count = jobBase.delete(JobDataBase.TABLE_NAME, selection, selectionArgs);
			break;
		case URI_JOB_ID:
			String id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)){
				selection = JobDataBase.ROW_ID + " = " + id;
				count = jobBase.delete(JobDataBase.TABLE_NAME, selection, selectionArgs);
			}else{
				selection = selection + " AND " + JobDataBase.ROW_ID + " = " + id;
				count = jobBase.delete(JobDataBase.TABLE_NAME, selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Bad Uri: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch(uriMatcher.match(uri)){
		case URI_JOB:
			return JOBS_TYPE;
		case URI_JOB_ID:
			return JOBS_ITEM_TYPE;
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if(uriMatcher.match(uri) != URI_JOB)
			throw new IllegalArgumentException("Bad Uri: " +uri);
		jobBase = jobDBHelper.getWritableDatabase();
		long rowId = jobBase.insert(JobDataBase.TABLE_NAME, null, values);
		Uri resultUri = ContentUris.withAppendedId(JOBS_URI, rowId);
		getContext().getContentResolver().notifyChange(resultUri, null);
		return resultUri;
	}

	@Override
	public boolean onCreate() {
		jobDBHelper = new JobDataBase(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		switch(uriMatcher.match(uri)){
		case URI_JOB:
			break;
		case URI_JOB_ID:
			String id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)){
				selection = JobDataBase.TABLE_NAME + " = " + id;
			}else{
				selection = selection + " AND " + JobDataBase.TABLE_NAME + " = " + id;
			}
			break;
			default:
				throw new IllegalArgumentException("Bad Uri: " + uri);
		}
		jobBase = jobDBHelper.getWritableDatabase();
		Cursor cursor = jobBase.query(JobDataBase.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		cursor.setNotificationUri(getContext().getContentResolver(), JOBS_URI);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
		jobBase = jobDBHelper.getWritableDatabase();
		switch(uriMatcher.match(uri)){
		case URI_JOB:
			count = jobBase.update(JobDataBase.TABLE_NAME, values, selection, selectionArgs);
			break;
		case URI_JOB_ID:
			String id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)){
				selection = JobDataBase.TABLE_NAME + " = " + id;
				count = jobBase.update(JobDataBase.TABLE_NAME, values, selection, selectionArgs);
			}else{
				selection = selection + " AND " + JobDataBase.TABLE_NAME + " = " + id;
				count = jobBase.update(JobDataBase.TABLE_NAME, values, selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Bad Uri: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
