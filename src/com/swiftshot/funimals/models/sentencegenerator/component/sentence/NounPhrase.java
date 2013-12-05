
package com.swiftshot.funimals.models.sentencegenerator.component.sentence;

import java.util.Vector;

import com.swiftshot.funimals.models.database.entities.CharacterGoal;
import com.swiftshot.funimals.models.sentencegenerator.SentenceElemFactory;
import com.swiftshot.funimals.models.sentencegenerator.StoryGeneratorException;

import simplenlg.features.Gender;

/**
 * This class is used to interface with the noun phrase specification of
 * the simplenlg surface realiser library.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */

public class NounPhrase {

	private String noun;
	private String determiner;

	private Vector modifiers;
	private Vector postmodifiers;

	private boolean isPlural;
	private boolean isPossessive;

	private Object premodifier;
	private Gender gender;

	/**
	 * NounPhrase constructor that accepts and sets the noun of the phrase
	 * @param noun the noun of the phrase
	 */
	public NounPhrase(String noun){
		setNoun(noun);
	}

	/**
	 * Gets the noun of the phrase
	 * @return the noun of the phrase
	 */
	public String getNoun() {
		return noun;
	}

	/**
	 * Sets the noun of the phrase
	 * @param noun the noun of the phrase
	 */
	public void setNoun(String noun) {
		this.noun = noun;
	}

	/**
	 * Gets the determiner of the noun in the phrase
	 * @return the determiner of the noun in the phrase
	 */
	public String getDeterminer() {
		return determiner;
	}

	/**
	 * Sets the determiner of the noun in the phrase
	 * @param determiner the determiner of the noun in the phrase
	 */
	public void setDeterminer(String determiner) {
		this.determiner = determiner;
	}

	/**
	 * Gets the modifiers of the phrase
	 * @return the modifiers of the phrase
	 */
	public Vector getModifiers() {
		return modifiers;
	}

	/**
	 * Sets the modifiers of the phrase
	 * @param modifiers the modifiers of the phrase
	 */
	public void setModifiers(Vector modifiers) {
		this.modifiers = modifiers;
	}

	/**
	 * Gets the postmodifiers of the phrase
	 * @return the postmodifiers of the phrase
	 */
	public Vector getPostmodifiers() {
		return postmodifiers;
	}

	/**
	 * Sets the postmodifiers of the phrase
	 * @param postmodifiers the postmodifiers of the phrase
	 */
	public void setPostmodifiers(Vector postmodifiers) {
		this.postmodifiers = postmodifiers;
	}

	/**
	 * Gets the plurality of the noun in the phrase
	 * @return the plurality of the noun in the phrase
	 */
	public boolean isPlural() {
		return isPlural;
	}

	/**
	 * Sets the plurality of the noun in the phrase
	 * @param isPlural the plurality of the noun in the phrase
	 */
	public void setPlural(boolean isPlural) {
		this.isPlural = isPlural;
	}

	/**
	 * Checks if the noun in the phrase is in possessive form
	 * @return true if the noun in the phrase is in possessive form, false if otherwise
	 */
	public boolean isPossessive() {
		return isPossessive;
	}

	/**
	 * Sets if the noun in the phrase is in its possessive form or not
	 * @param isPossessive true if the noun in the phrase is in possessive form, false if otherwise
	 */
	public void setPossessive(boolean isPossessive) {
		this.isPossessive = isPossessive;
	}

	/**
	 * Gets the premodifier of the phrase
	 * @return the premodifier of the phrase
	 */
	public Object getPremodifier() {
		return premodifier;
	}

	/**
	 * Sets the premodifier of the prhase
	 * @param premodifier the premodifier of the phrase
	 */
	public void setPremodifier(Object premodifier) {
		this.premodifier = premodifier;
	}

	/**
	 * Gets the gender of the noun (feminine, masculine, neuter)
	 * @return the gender of the noun
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * Sets the gender of the noun (feminine, masculine, neuter)
	 * @param gender the gender of the noun
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/**
	 * Creates a default noun phrase. If there is a need to adjust the default instance,
	 * just reset a component by calling the setters of the instance returned
	 *
	 * @param noun noun of the phrase
	 * @return the noun phrase created given the information passed
	 */
	public static NounPhrase createNounPhrase(CharacterGoal cg, String noun, boolean isSingular, String classification) throws StoryGeneratorException{

		NounPhrase np = new NounPhrase(noun);
		np.setDeterminer(SentenceElemFactory.determineDeterminer(cg, noun));
		np.setGender(Gender.NEUTER);
		np.setModifiers(null);
		if(classification.equalsIgnoreCase("count")){
			if(isSingular){
				np.setPlural(false);
			}
			else
				np.setPlural(true);
		}

		np.setPossessive(false);
		np.setPostmodifiers(null);
		np.setPremodifier(null);

		return np;
	}
}
