package database_entities;

import sentencegenerator.component.DBLexicalObject;

public class Word implements DBLexicalObject {
	String WordID;
	private String Word;
	String PartOfSpeech;
	
	//for adverbs, prepositions, and pronouns
	String type = "";
	
	//for noun
	String Classification = "";
	boolean isSingular = false;
	
	//for pronoun
	String gender = "";

	public Word(){
		super();
	}
	
	public Word(String wordID){
		WordID = wordID;
	}

	public Word(String wordID, String word, String partOfSpeech,
			String classification) {
		WordID = wordID;
		setWord(word);
		PartOfSpeech = partOfSpeech;
		Classification = classification;
	}

	public Word(String wordID, String word, String partOfSpeech) {
		WordID = wordID;
		setWord(word);
		PartOfSpeech = partOfSpeech;
	}

	public String getID() {
		return WordID;
	}

	public void setID(String wordID) {
		if(wordID != null)
			WordID = wordID;
	}

	public String getPartOfSpeech() {
		return PartOfSpeech;
	}

	public void setPartOfSpeech(String partOfSpeech) {
		if(partOfSpeech != null)
			this.PartOfSpeech = partOfSpeech;
	}

	public String getClassification() {
		return Classification;
	}

	public void setClassification(String classification) {
		if(classification != null)
			Classification = classification;
	}
	
	//added
	public String getString() {
		return getWord();
	}
	
	public void setString(String correspondingWord) {
		if(correspondingWord != null)
			this.setWord(correspondingWord);
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		if(type != null)
			this.type = type;
	}
	
	public boolean isSingular() {
		return isSingular;
	}
	
	public void setSingular(boolean isSingular) {
		this.isSingular = isSingular;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		if(gender != null)
			this.gender = gender;
	}

	public String getWord() {
		return Word;
	}

	public void setWord(String word) {
		Word = word;
	}
}
