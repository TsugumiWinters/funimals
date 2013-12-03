package com.picturebooks.funimals.models;

import java.util.List;
import database_entities.StoryFile;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressWarnings("hiding")
public class StoryAdapter<StoryFile> extends ArrayAdapter<StoryFile> {

	public StoryAdapter(Context context, int textViewResourceId,
			List<StoryFile> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		
		((TextView)view).setTextSize((float) 35.0);
		
		if (position % 2 == 1) {
			((TextView)view).setTextColor(Color.GRAY);
		} else {
			((TextView)view).setTextColor(Color.DKGRAY);
		}
		
		return view;
	}
}
