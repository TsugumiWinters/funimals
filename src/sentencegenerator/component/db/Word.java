package sentencegenerator.component.db;

import sentencegenerator.component.DBLexicalObject;

/**
 * This class implements the {@link sentencegenerator.component.DBLexicalObject}
 * interface for Word
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
public class Word implements DBLexicalObject {

	private String conceptID;
	private String correspondingWord;
	private String partOfSpeech;

	//for adverbs, prepositions and pronouns
	private String type = "";

	//for noun
	private String classification = "";
	private boolean isSingular = false;

	//for pronoun
	private String gender = "";

	/**
	 * Word Constructor that calls its super class
	 */
	public Word() {
		super();
	}

	/**
	 * Word Constructor that sets the wordId
	 * @param wordId id of the word
	 */
	public Word(String wordId){
		setID(wordId);
	}

	/**
	 * Gets the corresponding word of the Word object
	 * @return the corresponding word of the Word object
	 */
	public String getString() {
		return correspondingWord;
	}

	/**
	 * Sets the corresponding word of the Word object
	 * @param correspondingWord the corresponding word of the Word object
	 */
	public void setString(String correspondingWord) {
		if(correspondingWord != null)
			this.correspondingWord = correspondingWord;
	}

	/**
	 * Gets the conceptID of the Word object
	 * @return the conceptID of the Word object
	 */
	public String getID() {
		return conceptID;
	}

	/**
	 * Sets the conceptID of the Word object
	 * @param conceptId the conceptID of the Word object
	 */
	public void setID(String conceptId) {
		if(conceptId != null)
			conceptID = conceptId;
	}

	/**
	 * Gets the part of speech of the Word object
	 * @return the part of speech of the Word object
	 */
	public String getPartOfSpeech() {
		return partOfSpeech;
	}

	/**
	 * Sets the part of speech of the Word object
	 * @param partOfSpeech the part of speech of the Word object
	 */
	public void setPartOfSpeech(String partOfSpeech) {
		if(partOfSpeech != null)
			this.partOfSpeech = partOfSpeech;
	}

	/**
	 * Gets the type of the Word object
	 * @return the type of the Word object
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type of the Word object
	 * @param type the type of the Word object
	 */
	public void setType(String type) {
		if(type != null)
			this.type = type;
	}

	/**
	 * Gets the classification of the Word object
	 * @return the classification of the Word object
	 */
	public String getClassification() {
		return classification;
	}

	/**
	 * Sets the classification of the Word object
	 * @param classification the classification of the Word object
	 */
	public void setClassification(String classification) {
		if(classification != null)
			this.classification = classification;
	}

	/**
	 * Gets the singularity of the Word object
	 * @return the singularity of the Word object
	 */
	public boolean isSingular() {
		return isSingular;
	}

	/**
	 * Sets the singularity of the Word object
	 * @param isSingular the singularity of the Word object
	 */
	public void setSingular(boolean isSingular) {
		this.isSingular = isSingular;
	}

	/**
	 * Gets the gender of the Word object
	 * @return the gender of the Word object
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Sets the gender of the Word object
	 * @param gender the gender of the Word object
	 */
	public void setGender(String gender) {
		if(gender != null)
			this.gender = gender;
	}
}
