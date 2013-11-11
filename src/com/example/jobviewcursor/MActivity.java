package com.example.jobviewcursor;

import com.example.jobviewcursor.adapter.JobViewCursorAdapter;
import com.example.jobviewcursor.database.JobDataBaseAdapter;
import com.example.jobviewcursor.service.JobViewService;

import android.os.Bundle;
import android.widget.ListView;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;

public class MActivity extends Activity {
	
	private static final String BR_ACT = "GET_CURSOR_FROM_DB";
	private ListView lv_Main;
	private JobDataBaseAdapter jdba;
	private JobViewCursorAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		
		startService(new Intent(this,JobViewService.class));
		
		lv_Main = (ListView) findViewById(R.id.lv_Main);
		jdba = new JobDataBaseAdapter(this);
		
		BroadcastReceiver brRec = new BroadcastReceiver() {

			public void onReceive(Context context, Intent intent) {
				int status = intent.getIntExtra("Status", 0);
				if(status == 1){
					jdba.openDB();
					Cursor cursor = jdba.getAllJobs();
					//String[] from = new String[]{"Title","Date","Description"};
					//int[] to = new int[]{R.id.tv_title,R.id.tv_date,R.id.tv_description};
					adapter = new JobViewCursorAdapter(MActivity.this, cursor, 0);
					lv_Main.setAdapter(adapter);
					jdba.closeDB();
					//cursor.close();
				}
			}
			
		};
		IntentFilter inFilter = new IntentFilter(BR_ACT);
		registerReceiver(brRec, inFilter);
	}
}
