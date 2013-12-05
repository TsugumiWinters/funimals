package com.swiftshot.funimals.models.database.entities;

public class Noun{	
	String WordID;
	String Classification;
	String isSingular;
	String Word;
	
	public Noun(){}

	public Noun(String wordID, String classification, String isSingular,
			String word) {
		WordID = wordID;
		Classification = classification;
		this.isSingular = isSingular;
		Word = word;
	}

	public String getWordID() {
		return WordID;
	}

	public void setWordID(String wordID) {
		WordID = wordID;
	}

	public String getClassification() {
		return Classification;
	}

	public void setClassification(String classification) {
		Classification = classification;
	}

	public String getIsSingular() {
		return isSingular;
	}

	public void setIsSingular(String isSingular) {
		this.isSingular = isSingular;
	}

	public String getWord() {
		return Word;
	}

	public void setWord(String word) {
		Word = word;
	}
	
	
	
}
