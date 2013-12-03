package com.picturebooks.funimals;

import android.app.Application;

public class MPBApplication extends Application {
	
	private String user = "Jeremy";
	private int age = 8;
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
	
}
