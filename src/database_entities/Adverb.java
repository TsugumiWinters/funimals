package database_entities;

public class Adverb {
	String WordID;
	String Type;
	String Word;
	
	public Adverb(){}

	public Adverb(String wordID, String type, String word) {
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
