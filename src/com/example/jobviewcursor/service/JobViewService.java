package com.example.jobviewcursor.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.jobviewcursor.database.BaseProvider;
import com.example.jobviewcursor.database.JobDataBase;
import com.example.jobviewcursor.model.Job;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.RemoteException;
import android.util.Log;

public class JobViewService extends IntentService{
	
	private static final String url = "http://www.healthitjobs.com/" +
			"hit.healthitjobs.services_deploy/jobprocessservice.svc/" +
			"searchjobs?start=0&len=20&q=0:0:0:0:0:0:0:0";
	private InputStream inStream = null;
	private BufferedReader bufReader = null;
	private HttpURLConnection urlConnection = null;
	private String JSONString = null;
	private ArrayList<Job> jobs = null;

	public JobViewService() {
		super("JobViewService");	
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(urlConnection != null)
			urlConnection.disconnect();
		if(bufReader !=null)
			try {
				bufReader.close();
			}catch(IOException exc){
				exc.printStackTrace();
			}
		if(inStream !=null)
			try {
				inStream.close();
			}catch(IOException exc){
				exc.printStackTrace();
			}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if(httpConnectionStatus(this)){
			inStream = httpConnectionStream(url);
			JSONString = getJsonString(inStream);
			jobs = jsonParser(JSONString);
			putAllData(jobs);
			final Intent in = new Intent("com.example.jobviewcursor.receiver");
			in.putExtra("Status", 1);
			sendBroadcast(in);
		}else{
			final Intent in = new Intent("com.example.jobviewcursor.receiver");
			in.putExtra("Status", 2);
			sendBroadcast(in);
		}
	}
	
	private InputStream httpConnectionStream(String url){
		try{
			final URL httpUrl = new URL(url);
			urlConnection = (HttpURLConnection) httpUrl.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			return urlConnection.getInputStream();
		}catch(MalformedURLException exc){
			Log.d("MyDebug","httpException" + exc);
		}catch(IOException exc){
			Log.d("MyDebug","httpException" + exc);
		}
		return null;
	}
	
	private String getJsonString(InputStream inStream){
		bufReader = new BufferedReader(new InputStreamReader(inStream));
		final StringBuilder strBuild = new StringBuilder();
		String line = null;
		try{
			while((line = bufReader.readLine()) != null){
				strBuild.append(line);
			}
			return strBuild.toString();
		}catch(IOException exc){
			exc.printStackTrace();
		}
		return null;
	}
	
	private ArrayList<Job> jsonParser(String JSONString){
		final ArrayList<Job> jobs = new ArrayList<Job>();
		try {
			final JSONObject jObject = new JSONObject(JSONString);
			final JSONArray jArray = jObject.getJSONArray("Jobs");
			for(int i = 0;i<jArray.length();i++){
				jobs.add(convertData(jArray.getJSONObject(i)));
			}
			return jobs;
		}catch (JSONException exc) {
			exc.printStackTrace();
		}
		return null;
	}
	
	private Job convertData(JSONObject jObject) throws JSONException{
		final String title = jObject.getString("Title");
		final String date = jObject.getString("ExpiresDate");
		final String description = jObject.getString("Description");
		return new Job(title,date,description);
	}
	
	private void putAllData(ArrayList<Job> jobs){
		ArrayList<ContentProviderOperation> operation = new ArrayList<ContentProviderOperation>();
		try{
			for(Job job : jobs){
				operation.add(ContentProviderOperation.newInsert(BaseProvider.JOBS_URI)
						.withValue(JobDataBase.TITLE,job.getTitle())
						.withValue(JobDataBase.DATE,job.getDate())
						.withValue(JobDataBase.DESCRIPTION,job.getDescription())
						.build());
			}
			getContentResolver().applyBatch(BaseProvider.AUTHORITY, operation);
		}catch(RemoteException exc){
			exc.printStackTrace();
		}catch(OperationApplicationException exc){
			exc.printStackTrace();
		}
		
	}
	private Boolean httpConnectionStatus(Context context){
		final ConnectivityManager connectManager = 
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo netInfo = connectManager.getActiveNetworkInfo();
			if(netInfo == null || !netInfo.isConnected()){
				return false;
			}
			if(netInfo.isRoaming()){
				return false;
			}
		return true;
	}

}
