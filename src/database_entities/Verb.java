package database_entities;

public class Verb {
	String WordID;
	String Word;

	public Verb(){}

	public Verb(String wordID, String word) {
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
