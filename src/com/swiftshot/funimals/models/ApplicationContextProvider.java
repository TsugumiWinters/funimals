package com.swiftshot.funimals.models;

import android.app.Application;
import android.content.Context;
 
 
public class ApplicationContextProvider extends Application {
 
    /**
     * 
     * Keeps a reference of the application context
     */
    private static Context sContext;
    private String user;
	private int age;
	private int storyID;
	
	public int getStoryID() {
		return storyID;
	}
	
	public void setStoryID(int storyID) {
		this.storyID = storyID;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
    
    
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext(); 
    }
 
    /**
     * Returns the application context
     *
     * @return application context
     */
    public static Context getContext() {
        return sContext;
    }
}
