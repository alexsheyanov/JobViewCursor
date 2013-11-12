package com.example.jobviewcursor;

import com.example.jobviewcursor.adapter.JobViewCursorAdapter;
import com.example.jobviewcursor.database.BaseProvider;
import com.example.jobviewcursor.database.JobDataBase;
import com.example.jobviewcursor.service.JobViewService;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

public class MActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>{
	
	private ListView lv_Main;
	private JobViewCursorAdapter adapter;
	private String[] projection;
	private Cursor curs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//getContentResolver().delete(BaseProvider.JOBS_URI, null, null);
		
		projection = new String[]{JobDataBase.ROW_ID,JobDataBase.TITLE,JobDataBase.DATE,JobDataBase.DESCRIPTION};
		curs = getContentResolver().query(BaseProvider.JOBS_URI, projection, null, null, null);
		if(curs.getCount() == 0){
			startService(new Intent(this,JobViewService.class));
		}
		lv_Main = (ListView) findViewById(R.id.lv_Main);
		getSupportLoaderManager().initLoader(0, null, this);		
		adapter = new JobViewCursorAdapter(this,null, 0);
		lv_Main.setAdapter(adapter);
	}
	@Override
	protected void onResume(){
		super.onResume();
		getSupportLoaderManager().restartLoader(0, null, this);
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
		Log.d("MyDebug", "onCreateLoader");
		CursorLoader cursorLoader = new CursorLoader(this,BaseProvider.JOBS_URI, projection, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		adapter.swapCursor(cursor);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}
}
