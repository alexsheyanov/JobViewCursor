package com.example.jobviewcursor.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class GlobalReceiver extends BroadcastReceiver{
	
	private final int SERVICE_FINISH = 1;
	private final int HTTP_STATUS_BAD = 2;
	private Toast toast;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		final int status = intent.getIntExtra("Status",0);
		switch(status){
		case SERVICE_FINISH:
			Log.d("MyDebug","GlobalReceiver status: " + status);
			toast = Toast.makeText(context, "Сервис успешно отработал", Toast.LENGTH_SHORT);
			toast.show();
			break;
		case HTTP_STATUS_BAD:
			toast = Toast.makeText(context, "Отсутствует подключение к интернет", Toast.LENGTH_SHORT);
			toast.show();
			break;
		}	
	}
}
