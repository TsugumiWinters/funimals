
package com.swiftshot.funimals.models.sentencegenerator.component.sentence;

import java.util.Vector;

import simplenlg.features.Tense;
import simplenlg.realiser.PhraseSpec;
import simplenlg.realiser.VPPhraseSpec;

/**
 * This class is used to interface with the sentence object of
 * the simplenlg surface realiser library.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
public class Sentence {

	private Vector<PhraseSpec> subjects;

	private VPPhraseSpec verbphrase;
	private Vector<Object> modifiers;

	private Object complement;
	private Object frontModifier;
	private Object premodifier;
	private Object postmodifier;
	private Vector<PhraseSpec> postVerbModifier;
	private VPPhraseSpec indirectObject;

	private Tense tense;

	/**
	 * Sentence constructor
	 */
	public Sentence(){

	}

	/**
	 * Gets the subjects of the sentence
	 * @return the subjects of the sentence
	 */
	public Vector<PhraseSpec> getSubjects() {
		return subjects;
	}

	/**
	 * Sets the subjects of the sentence
	 * @param subjects the subjects of the sentence
	 */
	public void setSubjects(Vector<PhraseSpec> subjects) {
		this.subjects = subjects;
	}

	/**
	 * Gets the verb phrase of the sentence
	 * @return the verb phrase of the sentence
	 */
	public VPPhraseSpec getVerbphrase() {
		return verbphrase;
	}

	/**
	 * Sets the verb phrase of the sentence
	 * @param verbphrase the verb phrase of the sentence
	 */
	public void setVerbphrase(VPPhraseSpec verbphrase) {
		this.verbphrase = verbphrase;
	}

	/**
	 * Gets the modifiers of the sentence
	 * @return the modifiers of the sentence
	 */
	public Vector<Object> getModifiers() {
		return modifiers;
	}

	/**
	 * Sets the modifiers of the sentence
	 * @param modifiers the modifiers of the sentence
	 */
	public void setModifiers(Vector<Object> modifiers) {
		this.modifiers = modifiers;
	}

	/**
	 * Gets the complement of the sentence
	 * @return the complement of the sentence
	 */
	public Object getComplement() {
		return complement;
	}

	/**
	 * Sets the complement of the sentence
	 * @param complement the complement of the sentence
	 */
	public void setComplement(Object complement) {
		this.complement = complement;
	}

	/**
	 * Gets the frontmodifier of the sentence
	 * @return the frontmodifier of the sentence
	 */
	public Object getFrontModifier() {
		return frontModifier;
	}

	/**
	 * Sets the frontmodifier of the sentence
	 * @param frontModifier the frontmodifier of the sentence
	 */
	public void setFrontModifier(Object frontModifier) {
		this.frontModifier = frontModifier;
	}

	/**
	 * Gets the premodifier of the sentence
	 * @return the premodifier of the sentence
	 */
	public Object getPremodifier() {
		return premodifier;
	}

	/**
	 * Sets the premodifier of the sentence
	 * @param premodifier the premodifier of the sentence
	 */
	public void setPremodifier(Object premodifier) {
		this.premodifier = premodifier;
	}

	/**
	 * Gets the postmodifier of the sentence
	 * @return the postmodifier of the sentence
	 */
	public Object getPostmodifier() {
		return postmodifier;
	}

	/**
	 * Sets the postmodifier of the sentence
	 * @param postmodifier the postmodifier of the sentence
	 */
	public void setPostmodifier(Object postmodifier) {
		this.postmodifier = postmodifier;
	}

	/**
	 * Gets the tense of the sentence
	 * @return the tense of the sentence
	 */
	public Tense getTense() {
		return tense;
	}

	/**
	 * Sets the tense of the sentence
	 * @param tense the tense of the sentence
	 */
	public void setTense(Tense tense) {
		this.tense = tense;
	}

	/**
	 * Gets the postverbmodifiers of the sentence
	 * @return the postverbmodifiers of the sentence
	 */
	public Vector<PhraseSpec> getPostVerbModifier() {
		return postVerbModifier;
	}

	/**
	 * Sets the postverbmodifiers of the sentence
	 * @param postVerbModifier the postverbmodifiers of the sentence
	 */
	public void setPostVerbModifier(Vector<PhraseSpec> postVerbModifier) {
		this.postVerbModifier = postVerbModifier;
	}

	/**
	 * Gets the indirect object of the sentence
	 * @return the indirect object of the sentence
	 */
	public VPPhraseSpec getIndirectObject() {
		return indirectObject;
	}

	/**
	 * Sets the indirect object of the sentence
	 * @param indirectObject the indirect object of the sentence
	 */
	public void setIndirectObject(VPPhraseSpec indirectObject) {
		this.indirectObject = indirectObject;
	}
}
