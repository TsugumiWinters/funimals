package com.swiftshot.funimals;

import java.util.ArrayList;
import java.util.Locale;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;

 
public class ImageAdapter extends BaseAdapter {
	private Context context;
	private String[] stickers;
	
	private Bitmap icon;
	private Drawable bg;
	private BitmapFactory.Options options;
	
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
			gridView = inflater.inflate(R.layout.sticker, null);
 
		} else {
			gridView = (View) convertView;
		}
		
		// set image based on selected text
		ImageView imageView = (ImageView) gridView.findViewById(R.id.sticker_image);

		imageView.setOnTouchListener(new MyOnTouchListener());
		
		String stickerName = stickers[position];
 	
		int id = context.getResources().getIdentifier(stickerName, "drawable", context.getPackageName());
		options = new BitmapFactory.Options();
		options.inPurgeable = true;
        icon = BitmapFactory.decodeResource(context.getResources(),id, options);
		bg = new BitmapDrawable(context.getResources(), icon);
		
		imageView.setImageDrawable(bg);
	
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
		    	if(PictureEditorActivity.createdStory) {
		    		
		    	    String[] name = view.getContentDescription().toString().split("_");
		    	
		    		if(view.getContentDescription().toString().startsWith("a")) {
		    			TranslateAnimation animation = new TranslateAnimation(0.0f, 30.0f, 0.0f, 0.0f);  
			    	    animation.setDuration(500);
			    	    animation.setRepeatCount(2);
			    	    animation.setRepeatMode(2);
			    	    image.startAnimation(animation);
			    	    
		    	    	if(view.getContentDescription().toString().contains("woman"))
		    	    		PictureEditorActivity.tts.speak("Mommy " + name[2], TextToSpeech.QUEUE_FLUSH, null);
		    	    	else
		    	    		PictureEditorActivity.tts.speak("Daddy " + name[2], TextToSpeech.QUEUE_FLUSH, null);
		    		}
		    		else if(view.getContentDescription().toString().startsWith("k")) {
		    			TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 30.0f);  
			    	    animation.setDuration(500);
			    	    animation.setRepeatCount(2);
			    	    animation.setRepeatMode(2);
			    	    image.startAnimation(animation);
			    	    
			    	    PictureEditorActivity.tts.speak(name[2], TextToSpeech.QUEUE_FLUSH, null);
		    		}
		    	    	
		    	 id = context.getResources().getIdentifier(view.getContentDescription().toString() + "_highlighted", "drawable", context.getPackageName());
		    	 options.inPurgeable = true;
		         icon = BitmapFactory.decodeResource(context.getResources(),id, options);
				 bg = new BitmapDrawable(context.getResources(), icon);
				 image.setImageDrawable(bg);
		         
		    	 return true;
		    	}
		    case MotionEvent.ACTION_MOVE:
		    	if(!PictureEditorActivity.createdStory) {
		    		ClipData data = ClipData.newPlainText("", "");
			        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
			        view.startDrag(data, shadowBuilder, view, 0);
			        options.inPurgeable = true;
			        id = context.getResources().getIdentifier(view.getContentDescription().toString(), "drawable", context.getPackageName());
			        icon = BitmapFactory.decodeResource(context.getResources(),id, options);
					bg = new BitmapDrawable(context.getResources(), icon);
					image.setImageDrawable(bg);
			        
			        view.setVisibility(View.INVISIBLE);
			        return true;
		    	}		    	
		    case MotionEvent.ACTION_UP:
		    	if(PictureEditorActivity.createdStory) {
		    	 id = context.getResources().getIdentifier(view.getContentDescription().toString(), "drawable", context.getPackageName());

		         icon = BitmapFactory.decodeResource(context.getResources(),id, options);
		 	     bg = new BitmapDrawable(context.getResources(), icon);
		 		 image.setImageDrawable(bg);
	
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
