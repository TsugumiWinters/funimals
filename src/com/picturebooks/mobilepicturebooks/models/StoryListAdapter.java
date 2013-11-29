package com.picturebooks.mobilepicturebooks.models;

import java.util.ArrayList;

import com.picturebooks.mobilepicturebooks.ActiveUser;
import com.picturebooks.mobilepicturebooks.R;
import com.picturebooks.mobilepicturebooks.StoriesActivity;

import database.DatabaseHelper;
import database_entities.StoryFile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StoryListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<StoryFile> list;
	
	public StoryListAdapter(Context context) {
		this.context = context;
		
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		dbHelper.openDataBase();
		UserInformation user = ActiveUser.getActiveUser(context);
		
		list = new ArrayList<StoryFile>(dbHelper.getStoryFilesById(user.getName()));
		dbHelper.close();
	}
	
	@Override
	public int getCount() {
		return list.size();
	}
	
	@Override
	public Object getItem(int index) {
		return list.get(index);
	}
	
	@Override
	public long getItemId(int index) {
		return index;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parentView) {
		View view = convertView;
		
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.story_title, parentView, false);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					((StoriesActivity) context).clicked_lstStories(v);
				}
			});
		}
		
		((TextView) view).setText(list.get(position).getTitle());
		
		return view;
	}
	
}
