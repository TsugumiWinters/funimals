package com.picturebooks.mobilepicturebooks;

import java.util.ArrayList;

import com.picturebooks.mobilepicturebooks.R;
import com.picturebooks.mobilepicturebooks.R.id;
import com.picturebooks.mobilepicturebooks.R.layout;

import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

 
public class ImageAdapter extends BaseAdapter {
	private Context context;
	String[] stickers;
 
	String contentDesc;
	
	public ImageAdapter(Context context, ArrayList<String> stickers) {
		this.context = context;
		this.stickers = new String[stickers.size()];
		for(int index=0; index<stickers.size(); index++)
			this.stickers[index] = stickers.get(index);
	}
 
	public View getView(int position, View convertView, ViewGroup parent) {
 
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View gridView;
		
		if (convertView == null) {
 
			gridView = new View(context);
 
			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.sticker, null);
 
		} else {
			gridView = (View) convertView;
		}
 
		// set image based on selected text
		ImageView imageView = (ImageView) gridView
				.findViewById(R.id.sticker_image);

		imageView.setOnTouchListener(new MyOnTouchListener());
		
		String stickerName = stickers[position];

		int id = context.getResources().getIdentifier(stickerName, "drawable", context.getPackageName());
		imageView.setImageResource(id);
		imageView.setContentDescription(stickerName);
		contentDesc = imageView.getContentDescription().toString();
		return gridView;
	}

	private final class MyOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			ImageView image = (ImageView) view;
			int id;
			switch (event.getAction()) {
		    case MotionEvent.ACTION_DOWN:
		    	id = context.getResources().getIdentifier(view.getContentDescription().toString() + "_highlighted", "drawable", context.getPackageName());
		    	image.setImageResource(id);
		    	 Toast toast = Toast.makeText(context, view.getContentDescription().toString(), Toast.LENGTH_SHORT);
		
		    	 toast.show();
		    	return true;
		    case MotionEvent.ACTION_MOVE:
		    	ClipData data = ClipData.newPlainText("", "");
		        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
		        view.startDrag(data, shadowBuilder, view, 0);
		        
		        view.playSoundEffect(SoundEffectConstants.CLICK);
		        id = context.getResources().getIdentifier(view.getContentDescription().toString(), "drawable", context.getPackageName());
		    	image.setImageResource(id);
		        
		        view.setVisibility(View.INVISIBLE);
		        return true;
		    case MotionEvent.ACTION_UP:
		    	id = context.getResources().getIdentifier(view.getContentDescription().toString(), "drawable", context.getPackageName());
		    	image.setImageResource(id);
		    	return true;
		    	
		    default: break;
		    }
			
			return false;
		}
	  }
 
	@Override
	public int getCount() {
		return stickers.length;
	}
 
	@Override
	public Object getItem(int position) {
		return null;
	}
 
	@Override
	public long getItemId(int position) {
		return 0;
	}
 
}
