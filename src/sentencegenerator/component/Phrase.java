
package sentencegenerator.component;

import java.util.Vector;

import database_entities.Word;

/**
 * This class implements the interface {@link sentencegenerator.component.DBLexicalObject}
 * interface and acts as container for a set of words.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
public class Phrase extends Vector<Word> implements DBLexicalObject{

	/**
	 * Phrase constructor that calls its super class
	 *
	 */
	public Phrase(){
		super();
	}

	private Phrase(Vector<Word> phrase){
		super(phrase);
	}

	/**
	 * Sets the contents of the Phrase object
	 * @param phrase set of words that will be placed inside the Phrase object
	 */
	public void setPhrase(Vector<Word> phrase){
		new Phrase(phrase);
	}

	/**
	 * Overrides the method getID in its super class;
	 * Gets the IDs of the words inside the Phrase
	 * object and places them in a single string
	 * @return String concatenated ids of the words inside the Phrase object
	 */
	@Override
	public String getID() {
		String ret = "";
		for(int i=0; i < this.size(); i++)
			if(i == this.size() - 1)
				ret += this.get(i).getID();
			else ret += this.get(i).getID() + " ";
		return ret;
	}

	/**
	 * Overrides the method getString in its super class;
	 * Gets the concatenated string values represented by the word objects inside the Phrase
	 * object
	 * @return String concatenated string values of the word objects inside the Phrase object
	 */
	@Override
	public String getString() {
		String ret = "";
		for(int i=0; i < this.size(); i++)
			if(i == this.size() - 1)
				ret += this.get(i).getString();	
			else ret += this.get(i).getString() + " ";
		return ret;
	}

	/**
	 * Overrides the method setID in its super class
	 * @param dbObjectID id of the phrase object
	 */
	@Override
	public void setID(String dbObjectID) {

	}

	/**
	 * Overrides the method setString in its super class
	 * @param dbObjectName string value of the phrase object
	 */
	@Override
	public void setString(String dbObjectName) {

	}

	/**
	 * Checks if the phrase object contains word/s whose part of speech match/es
	 * the specified part of speech
	 * @param partOfSpeech part of speech checked against the part of speech of the words
	 * 		inside the phrase object
	 * @return true if there's a word in the phrase object whose part of speech matches
	 * 		the specified part of speech; false if otherwise
	 */
	public boolean containsPOS(String partOfSpeech){
		for (int i = 0; i < this.size(); i++) {
			if(this.get(i).getPartOfSpeech().equalsIgnoreCase(partOfSpeech))
				return true;
		}
		return false;
	}

	/**
	 * Checks if the phrase object contains word/s that match/es
	 * the specified word
	 * @param word string value checked against the words inside the phrase object
	 * @return true if there's a word in the phrase object that matches
	 * 		the specified word; false if otherwise
	 */
	public boolean containsWord(String word){
		for (int i = 0; i < this.size(); i++) {
			if(this.get(i).getString().equalsIgnoreCase(word))
				return true;
		}
		return false;
	}

	/**
	 * Gets the index of the specified word in the phrase object
	 * @param word word whose index in the phrase object is needed
	 * @return the index of the word in the phrase object, -1 if the specified word is not in the phrase
	 */
	public int getIndex(String word){
		for (int i = 0; i < this.size(); i++) {
			if(this.get(i).getString().equalsIgnoreCase(word))
				return i;
		}
		return -1;
	}

	/**
	 * Gets the index of the first occurrence of the word in the phrase with the specified part of speech;
	 * @param partOfSpeech part of speech of the word whose index in the phrase object is needed
	 * @return the index of the first occurrence of the word with the specified part of speech,
	 * 		-1 if the phrase does not contain words with the specified part of speech
	 */
	public int getPOSIndex(String partOfSpeech){
		if(containsPOS(partOfSpeech)){
			for (int i = 0; i < this.size(); i++) {
				if(this.get(i).getPartOfSpeech().equalsIgnoreCase(partOfSpeech))
					return i;
			}
		}
		return -1;
	}
}
