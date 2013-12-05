package com.swiftshot.funimals.models.database.entities;

import com.swiftshot.funimals.models.pbmain.util.DBObject;

public class IGCharacter implements DBObject {
	String IGCharacterID;
	String Name;
	String Gender;	
	String AnimalType;
	String Role;
	String MotherCharacterID = "";
	String FatherCharacterID = "";
	String ImageFilePath;

	public IGCharacter(){}
	
	public IGCharacter(String id, String name){
		IGCharacterID = id;
		Name = name;
	}

	public IGCharacter(String iGCharacterID, String name, String gender,
			String animalType, String role, String motherCharacterID,
			String fatherCharacterID, String imageFilePath) {
		IGCharacterID = iGCharacterID;
		Name = name;
		Gender = gender;
		AnimalType = animalType;
		Role = role;
		MotherCharacterID = motherCharacterID;
		FatherCharacterID = fatherCharacterID;
		ImageFilePath = imageFilePath;
	}
	
	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getType() {
		return AnimalType;
	}

	public void setType(String animalType) {
		AnimalType = animalType;
	}

	public String getRole() {
		return Role;
	}

	public void setRole(String role) {
		Role = role;
	}

	public String getMotherCharacterID() {
		return MotherCharacterID;
	}

	public void setMotherCharacterID(String motherCharacterID) {
		MotherCharacterID = motherCharacterID;
	}

	public String getFatherCharacterID() {
		return FatherCharacterID;
	}

	public void setFatherCharacterID(String fatherCharacterID) {
		FatherCharacterID = fatherCharacterID;
	}

	public String getImageFilePath() {
		return ImageFilePath;
	}

	public void setImageFilePath(String imageFilePath) {
		ImageFilePath = imageFilePath;
	}
	
	//DBObject
	public String getID() {
		return IGCharacterID;
	}

	public void setID(String iGCharacterID) {
		IGCharacterID = iGCharacterID;
	}

	public String getString() {
		return Name;
	}
	
	public void setString(String name){
		this.Name = name;
	}
	
}
