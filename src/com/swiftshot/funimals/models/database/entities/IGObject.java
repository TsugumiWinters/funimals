package com.swiftshot.funimals.models.database.entities;

import com.swiftshot.funimals.models.pbmain.util.DBObject;

public class IGObject implements DBObject {
	String IGObjectID;
	String Name;
	String ImageFilePath;
	String ObjectWord;

	public IGObject(){}

	public IGObject(String iGObjectID, String name, String imageFilePath,
			String objectWord) {
		IGObjectID = iGObjectID;
		Name = name;
		ImageFilePath = imageFilePath;
		ObjectWord = objectWord;
	}

	public String getImageFilePath() {
		return ImageFilePath;
	}

	public void setImageFilePath(String imageFilePath) {
		ImageFilePath = imageFilePath;
	}

	public String getObjectWord() {
		return ObjectWord;
	}

	public void setObjectWord(String objectWord) {
		ObjectWord = objectWord;
	}
	
	//DBObject
	public String getID() {
		return IGObjectID;
	}

	public void setID(String iGObjectID) {
		IGObjectID = iGObjectID;
	}

	public String getString() {
		return Name;
	}

	public void setString(String name) {
		Name = name;
	}
}
