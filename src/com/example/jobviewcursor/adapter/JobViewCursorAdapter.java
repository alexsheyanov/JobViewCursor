package com.example.jobviewcursor.adapter;

import com.example.jobviewcursor.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.TextView;

public class JobViewCursorAdapter extends CursorAdapter{
	
	private LayoutInflater linflater;
	private final static int GRAY = 1;
	private final static int WHITE = 2;

	public JobViewCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		linflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final int position = cursor.getPosition();
		
		final String title = cursor.getString(cursor.getColumnIndex("Title"));
		final String date = cursor.getString(cursor.getColumnIndex("Date"));
		final String description = cursor.getString(cursor.getColumnIndex("Description"));
		
		((TextView) view.findViewById(R.id.tv_title)).setText(title);
		((TextView) view.findViewById(R.id.tv_date)).setText(date);
		((TextView) view.findViewById(R.id.tv_description)).setText(description);
		view.setBackgroundColor(getItemViewType(position) == WHITE ? Color.LTGRAY : Color.WHITE);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view = linflater.inflate(R.layout.item,parent,false);
		return view;
	}
	@Override
	public int getViewTypeCount(){
		return 1;
	}
	@Override
	public int getItemViewType(int position){
		if(position %2 == 0){
			return WHITE;
		}else{
			return GRAY;
		}
	}
}
