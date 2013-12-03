package com.swiftshot.funimals;

import java.util.ArrayList;

import com.swiftshot.funimals.R;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;

 
public class ImageAdapter extends BaseAdapter {
	private Context context;
	String[] stickers;
	
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
		ImageView imageView = (ImageView) gridView.findViewById(R.id.sticker_image);

		imageView.setOnTouchListener(new MyOnTouchListener());
		
		String stickerName = stickers[position];
 	
		int id = context.getResources().getIdentifier(stickerName, "drawable", context.getPackageName());
		
		imageView.setImageResource(id);
		imageView.setContentDescription(stickerName);

		return gridView;
	}

	public final class MyOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			final ImageView image = (ImageView) view;
			int id;
			switch (event.getAction()) {
		    case MotionEvent.ACTION_DOWN:
		    	if(PictureEditor.createdStory) {
		    	
		    		if(view.getContentDescription().toString().startsWith("a")) {
		    			TranslateAnimation animation = new TranslateAnimation(0.0f, 30.0f, 0.0f, 0.0f);  
			    	    animation.setDuration(500);
			    	    animation.setRepeatCount(2);
			    	    animation.setRepeatMode(2);
			    	    image.startAnimation(animation);
		    		}
		    		else if(view.getContentDescription().toString().startsWith("k")) {
		    			TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 30.0f);  
			    	    animation.setDuration(500);
			    	    animation.setRepeatCount(2);
			    	    animation.setRepeatMode(2);
			    	    image.startAnimation(animation);
		    		}
		    	    	
		    	id = context.getResources().getIdentifier(view.getContentDescription().toString() + "_highlighted", "drawable", context.getPackageName());
		    	image.setImageResource(id);
		    			    	
		    	 return true;
		    	}
		    case MotionEvent.ACTION_MOVE:
		    	if(!PictureEditor.createdStory) {
		    		ClipData data = ClipData.newPlainText("", "");
			        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
			        view.startDrag(data, shadowBuilder, view, 0);
			        
			        view.playSoundEffect(SoundEffectConstants.CLICK);
			        id = context.getResources().getIdentifier(view.getContentDescription().toString(), "drawable", context.getPackageName());
			    	image.setImageResource(id);
			        
			        view.setVisibility(View.INVISIBLE);
			        return true;
		    	}		    	
		    case MotionEvent.ACTION_UP:
		    	if(PictureEditor.createdStory) {
		    	 id = context.getResources().getIdentifier(view.getContentDescription().toString(), "drawable", context.getPackageName());
		    	 image.setImageResource(id);
		    	 return true;    	
		    	}
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
