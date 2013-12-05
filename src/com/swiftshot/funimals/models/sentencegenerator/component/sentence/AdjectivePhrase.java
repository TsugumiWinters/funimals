
package com.swiftshot.funimals.models.sentencegenerator.component.sentence;

import java.util.Vector;

/**
 * This class is used to interface with the adjective phrase specification of
 * the simplenlg surface realiser library.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
public class AdjectivePhrase {

	private String adjective;

	private Vector<Object> modifiers;

	private Object premodifier;
	private Object postmodifier;

	/**
	 * AdjectivePhrase constructor that accepts and sets the adjective of the phrase
	 * @param adjective the adjective of the phrase
	 */
	public AdjectivePhrase(String adjective){
		setAdjective(adjective);
	}

	/**
	 * Gets the adjective of the phrase
	 * @return the adjective of the phrase
	 */
	public String getAdjective() {
		return adjective;
	}

	/**
	 * Sets the adjective of the phrase
	 * @param adjective the adjective of the phrase
	 */
	public void setAdjective(String adjective) {
		this.adjective = adjective;
	}

	/**
	 * Gets the modifiers of the phrase
	 * @return the modifiers of the phrase
	 */
	public Vector<Object> getModifiers() {
		return modifiers;
	}

	/**
	 * Sets the modifiers of the phrase
	 * @param modifiers the modifiers of the phrase
	 */
	public void setModifiers(Vector<Object> modifiers) {
		this.modifiers = modifiers;
	}

	/**
	 * Gets the premodifier of the phrase
	 * @return the premodifier of the phrase
	 */
	public Object getPremodifier() {
		return premodifier;
	}

	/**
	 * Sets the premodifier of the phrase
	 * @param premodifier the premodifier of the phrase
	 */
	public void setPremodifier(Object premodifier) {
		this.premodifier = premodifier;
	}

	/**
	 * Gets the postmodifier of the phrase
	 * @return the postmodifier of the phrase
	 */
	public Object getPostmodifier() {
		return postmodifier;
	}

	/**
	 * Sets the postmodifier of the phrase
	 * @param postmodifier the postmodifier of the phrase
	 */
	public void setPostmodifier(Object postmodifier) {
		this.postmodifier = postmodifier;
	}
}
