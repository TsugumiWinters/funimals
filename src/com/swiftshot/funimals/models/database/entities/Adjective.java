package com.swiftshot.funimals.models.database.entities;

public class Adjective {
	String WordID;
	String Word;

	public Adjective(){}

	public Adjective(String wordID, String word) {
		WordID = wordID;
		Word = word;
	}

	public String getWordID() {
		return WordID;
	}

	public void setWordID(String wordID) {
		WordID = wordID;
	}

	public String getWord() {
		return Word;
	}

	public void setWord(String word) {
		Word = word;
	}
	
	
}
