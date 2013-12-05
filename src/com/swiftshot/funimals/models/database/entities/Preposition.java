package com.swiftshot.funimals.models.database.entities;

public class Preposition {
	String WordID;
	String Type = "";
	String Word;

	public Preposition(){}

	public Preposition(String wordID, String type, String word) {
		WordID = wordID;
		Type = type;
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

	public String getWord() {
		return Word;
	}

	public void setWord(String word) {
		Word = word;
	}
	
}
