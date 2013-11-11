package database_entities;

public class Conjunction {
	String WordID;
	String Word;
	
	public Conjunction(){}

	public Conjunction(String wordID, String word) {
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
