package com.swiftshot.funimals.models.database.entities;

public class Background {
	String BackgroundID,
		Name,
		Theme,
		BackgroundWord,
		CharactersIncluded,
		ObjectsIncluded,
		RequiredRole = "",
		ImageFilePath;
	
	public Background() {}
	
	public Background(String backgroundID, String name, String theme,
			String backgroundWord, String objectsIncluded,
			String charactersIncluded, String requiredRole, String imageFilePath) {
		BackgroundID = backgroundID;
		Name = name;
		Theme = theme;
		BackgroundWord = backgroundWord;
		ObjectsIncluded = objectsIncluded;
		CharactersIncluded = charactersIncluded;
		RequiredRole = requiredRole;
		ImageFilePath = imageFilePath;
	}

	public String getBackgroundID() {
		return BackgroundID;
	}

	public void setBackgroundID(String backgroundID) {
		BackgroundID = backgroundID;
	}

	public String getString() {
		return Name;
	}

	public void setString(String name) {
		Name = name;
	}

	public String getTheme() {
		return Theme;
	}

	public void setTheme(String theme) {
		Theme = theme;
	}

	public String getBackgroundWord() {
		return BackgroundWord;
	}

	public void setBackgroundWord(String backgroundWord) {
		BackgroundWord = backgroundWord;
	}

	public String getObjectsIncluded() {
		return ObjectsIncluded;
	}

	public void setObjectsIncluded(String objectsIncluded) {
		ObjectsIncluded = objectsIncluded;
	}

	public String getCharactersIncluded() {
		return CharactersIncluded;
	}

	public void setCharactersIncluded(String charactersIncluded) {
		CharactersIncluded = charactersIncluded;
	}

	public String getRequiredRole() {
		return RequiredRole;
	}

	public void setRequiredRole(String requiredRole) {
		RequiredRole = requiredRole;
	}

	public String getImageFilePath() {
		return ImageFilePath;
	}

	public void setImageFilePath(String imageFilePath) {
		ImageFilePath = imageFilePath;
	}
}
