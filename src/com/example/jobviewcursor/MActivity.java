package com.example.jobviewcursor;

import com.example.jobviewcursor.adapter.JobViewCursorAdapter;
import com.example.jobviewcursor.database.BaseProvider;
import com.example.jobviewcursor.database.JobDataBase;
import com.example.jobviewcursor.service.JobViewService;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

public class MActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>{
	
	private static final String BR_ACT = "GET_CURSOR_FROM_DB";
	private ListView lv_Main;
	private BroadcastReceiver brRec;
	private JobViewCursorAdapter adapter;
	private String[] projection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		projection = new String[]{JobDataBase.ROW_ID,JobDataBase.TITLE,JobDataBase.DATE,JobDataBase.DESCRIPTION};
		startService(new Intent(this,JobViewService.class));
		
		lv_Main = (ListView) findViewById(R.id.lv_Main);
		brRec = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				int status = intent.getIntExtra("Status", 0);
				if(status == 1){
					Toast toast = Toast.makeText(MActivity.this, "Сервис успешно отработал", Toast.LENGTH_SHORT);
					toast.show();
				}
			}		
		};
		IntentFilter inFilter = new IntentFilter(BR_ACT);
		registerReceiver(brRec, inFilter);
		getSupportLoaderManager().initLoader(0, null, this);		
		adapter = new JobViewCursorAdapter(this,null, 0);
	}
	@Override
	protected void onResume(){
		super.onResume();
		getSupportLoaderManager().restartLoader(0, null, this);
		lv_Main.setAdapter(adapter);
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		unregisterReceiver(brRec);
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
