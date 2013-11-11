package database_entities;

public class Article {
	String WordID;
	String Word;
	
	public Article(){}

	public Article(String wordID, String word) {
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
