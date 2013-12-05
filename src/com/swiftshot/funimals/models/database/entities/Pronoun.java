package com.swiftshot.funimals.models.database.entities;

public class Pronoun {
	String WordID;
	String Type = "";
	String Gender = "";
	String Word;
	
	public Pronoun(){}

	public Pronoun(String wordID, String type, String gender, String word) {
		WordID = wordID;
		Type = type;
		Gender = gender;
		Word = word;
	}

	public String getWordID() {
		return WordID;
	}

	public void setWordID(String wordID) {
		WordID = wordID;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getWord() {
		return Word;
	}

	public void setWord(String word) {
		Word = word;
	}
	
	
	
}
