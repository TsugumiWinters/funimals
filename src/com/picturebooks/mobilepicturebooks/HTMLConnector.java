package com.picturebooks.mobilepicturebooks;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.apache.cordova.DroidGap;

import database.DatabaseHelper;
import database_entities.StoryFile;
import database_entities.UserInformation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.webkit.WebView;
//import android.widget.Toast;

public class HTMLConnector extends Activity{
	private WebView mAppView;
    private DroidGap mGap;
    public DatabaseHelper dbHelper;
    Vector<UserInformation> users = new Vector<UserInformation>();
    Vector<StoryFile> stories = new Vector<StoryFile>();
    UserInformation currentUser;
	public Context c;
	public int storyID;
	public int pageCode = 0;
	
    public HTMLConnector(DroidGap gap, WebView view, DatabaseHelper dbH, Context con)
    {
        mAppView = view;
        mGap = gap;
        dbHelper = dbH;
		c = con;
    }
    
    public HTMLConnector(DroidGap gap, WebView view, DatabaseHelper dbH, Context con, int p)
    {
        mAppView = view;
        mGap = gap;
        dbHelper = dbH;
		c = con;
		pageCode = p;
    }
    
    public int getPageCode() {
    	//Toast.makeText(c, "pageCode is: "+pageCode, Toast.LENGTH_LONG).show();
    	return pageCode;
    }

    public void changeActivity(String name, int age, int grade, int tutorial) {
    	currentUser = new UserInformation(name, age, grade);
        Intent intent = new Intent(mGap, PictureEditor.class);
    	intent.putExtra("username", name);
    	intent.putExtra("age", age);
    	intent.putExtra("tutorial", tutorial);
    	//if tutorial == 1, tutorial mode should be enabled, else none
        mGap.startActivity(intent);
    }
	
	/**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
	public void createDataBase() {
		try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addUserInformation(String name, int age, int grade) {
		UserInformation info = new UserInformation(name, age, grade);
		dbHelper.addUserInformation(info);
	}
	
	public int getNumUsers() {
		dbHelper.openDataBase();
		users = dbHelper.getUserInformation();
		return users.size();
	}
	
	public String getUserName(int i) {
		return users.get(i).getUsername();
	}
	
	public int getUserAge(int i) {
		return users.get(i).getAge();
	}
	
	public int getUserGrade(int i) {
		return users.get(i).getGrade();
	}
	
	public int getNumStories(String author) {
		dbHelper.openDataBase();
		stories = dbHelper.getStoryFilesById(author);
		//Toast.makeText(c, ""+author+" has "+stories.size()+" stories!", Toast.LENGTH_LONG).show();
		return stories.size();
	}
	
	public String getStoryTitle(int i) {
		return stories.get(i).getTitle();
	}
	
	public int retrieveStoryID(int i) {
		return stories.get(i).getStoryID();
	}
	
	public void setStoryID(int i, String user, int age, int isUserAuthor) {
		this.storyID = i;
		Intent intent = new Intent(mGap, PictureEditor.class);
    	intent.putExtra("username", user);
    	intent.putExtra("age", age);
    	intent.putExtra("loadStory", true);
    	intent.putExtra("storyID", this.storyID);
    	intent.putExtra("isUserAuthor", isUserAuthor);
        mGap.startActivity(intent);
	}
	
	public int getStoryID() {
		return this.storyID;
	}
	
	public String getSDCardDir() {
		String filePath;
		String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
		filePath = baseDir.concat(File.separator+"MobilePictureBooks"+File.separator+"SavedPictures"+File.separator);
		return filePath;
	}
	
	/*
	public void addStoryFile(StoryFile storyFile) {
		
	}
	
	public Vector<StoryFile> getStoryFiles() {
		
	}
	
	/*
	/**
     * Gets the maximum number of story files saved in the SaveFiles table in the database
     * @return the maximum number of story files saved in the SaveFiles table in the database
     * @throws PBException thrown when an error was encountered in the data retrieval
     *
    //from PictureEditorDatabase.java
    public int getMaxNumOfSavedFiles() throws PBException{
	
	/**
	 * Gets list of saved stories in a user's library.
	 * @param name the name of the library owner
	 * @return a vector containing all the story titles in the user's library
	 * @throws PBException thrown when an error was encountered in the data retrieval
     *
    //from PictureEditorDatabase.java
	public Vector<String> getSavedStories(String name) throws PBException{*/
}
