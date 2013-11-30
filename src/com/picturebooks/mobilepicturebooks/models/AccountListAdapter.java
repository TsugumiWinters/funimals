package com.picturebooks.mobilepicturebooks.models;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.picturebooks.mobilepicturebooks.MenuActivity;
import com.picturebooks.mobilepicturebooks.R;

import database.DatabaseHelper;
import database_entities.UserInformation;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountListAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<UserInformation> list;
	private ArrayList<Field> drawables;

    public AccountListAdapter(Context context) {
    	this.context = context;	
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		dbHelper.openDataBase();
		list = new ArrayList<UserInformation>(dbHelper.getUserInformation());
		
		dbHelper.close();
	
	    drawables = new ArrayList<Field>();
	    Field[] fields = R.drawable.class.getFields();
	    for (Field field : fields) {
	        /* Take only those with name starting with "users" */
	        if (field.getName().startsWith("users")) {
	            try {
	            	if ( !(field.getName().contains("default") || field.getName().contains("add"))) {
	            		drawables.add(field);
	            	}
				} catch (Exception e) {}
	        }
	    }
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
		
		ViewHolder holder = null;
		
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.user_canvas, parentView, false);
			holder = new ViewHolder();
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					((MenuActivity) AccountListAdapter.this.context).clicked_openAccount(v);
				}
			});
		}
		
		holder.img = (ImageView) view.findViewById(R.id.canvas_img_user);
		holder.name = (TextView) view.findViewById(R.id.canvas_name);
		view.setTag(holder);
		
		UserInformation user = list.get(position);
		
		try {
			holder.img.setImageResource(drawables.get(position).getInt(null));

		} catch (Exception e) {
			Log.d("UserListAdapter", "Error: " + e.getMessage());
		}
		holder.img.setContentDescription(drawables.get(position).getName());
		holder.name.setText(user.getUsername());
		Log.e("dagain", "againgadddddd");
		return view;
	}
	
	class ViewHolder {
        TextView name;
        ImageView img;

    }
	
	public UserInformation getUser(String name) {
		for (UserInformation user : list) {
			if (user.getUsername().equals(name)) {
				return user;
			}
		}
		
		return null;
	}
	
	public void addUser(String name, int age, String level) {
		int grade = 0;
		if (!level.equals("Prep")) {
			grade = Integer.parseInt("" + level.charAt(level.length() - 1));
		}
		
		UserInformation user = new UserInformation();
		user.setUsername(name);
		user.setAge(age);
		user.setGrade(grade);
		
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		dbHelper.openDataBase();
		dbHelper.addUserInformation(user);
		dbHelper.close();
		list.add(user);
	}

}
