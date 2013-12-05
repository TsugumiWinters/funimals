
package com.swiftshot.funimals.models.sentencegenerator.component.sentence;

import java.util.Vector;

import com.swiftshot.funimals.models.database.entities.CharacterGoal;
import com.swiftshot.funimals.models.database.entities.Word;
import com.swiftshot.funimals.models.sentencegenerator.SentenceElemFactory;
import com.swiftshot.funimals.models.sentencegenerator.StoryGeneratorException;
import com.swiftshot.funimals.models.sentencegenerator.component.Phrase;

import simplenlg.features.NumberAgr;
import simplenlg.features.Tense;
import simplenlg.realiser.PhraseSpec;

/**
 * This class is used to interface with the verb phrase specification of
 * the simplenlg surface realiser library.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
public class VerbPhrase {

	private String verb;

	private Vector<PhraseSpec> complement;
	private Vector<PhraseSpec> modifiers;

	private Object premodifier;
	private Object postmodifier;

	private boolean isProgressive;
	private boolean isNegated;

	private String modal;
	private String particle;

	private Tense tense;
	private NumberAgr numberAgr;

	/**
	 * VerbPhrase constructor
	 */
	public VerbPhrase(){}

	/**
	 * VerbPhrase constructor that accepts and sets the verb of the phrase
	 * @param verb the verb of the phrase
	 */
	public VerbPhrase(String verb){
		setVerb(verb);
	}

	/**
	 * Gets the verb of the phrase
	 * @return the verb of the phrase
	 */
	public String getVerb() {
		return verb;
	}

	/**
	 * Sets the verb of the phrase
	 * @param verb the verb of the phrase
	 */
	public void setVerb(String verb) {
		this.verb = verb;
	}

	/**
	 * Gets the complements of the phrase
	 * @return the complements of the phrase
	 */
	public Vector<PhraseSpec> getComplement() {
		return complement;
	}

	/**
	 * Sets the complements of the phrase
	 * @param complement the complement of the phrase
	 */
	public void setComplement(Vector<PhraseSpec> complement) {
		this.complement = complement;
	}

	/**
	 * Gets the modifiers of the phrase
	 * @return the modifiers of the phrase
	 */
	public Vector<PhraseSpec> getModifiers() {
		return modifiers;
	}

	/**
	 * Sets the modifiers of the phrase
	 * @param modifiers the modifiers of the phrase
	 */
	public void setModifiers(Vector<PhraseSpec> modifiers) {
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
	 * Sets the premodifier of the prhase
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

	/**
	 * Gets the tense of the phrase
	 * @return the tense of the phrase
	 */
	public Tense getTense() {
		return tense;
	}

	/**
	 * Sets the tense of the phrase
	 * @param tense the tense of the phrase
	 */
	public void setTense(Tense tense) {
		this.tense = tense;
	}

	/**
	 * Sets the number argument of the phrase
	 * @return the number argument of the phrase
	 */
	public NumberAgr getNumberAgr() {
		return numberAgr;
	}

	/**
	 * Sets the number argument of the phrase
	 * @param numberAgr the number argument of the phrase
	 */
	public void setNumberAgr(NumberAgr numberAgr) {
		this.numberAgr = numberAgr;
	}

	/**
	 * Checks if the verb in the phrase is in progressive form
	 * @return true if the verb in the phrase is in progressive form, false if otherwise
	 */
	public boolean isProgressive() {
		return isProgressive;
	}

	/**
	 * Sets if the verb in the phrase is in its progressive form or not
	 * @param isProgressive true if the verb in the phrase is in progressive form, false if otherwise
	 */
	public void setProgressive(boolean isProgressive) {
		this.isProgressive = isProgressive;
	}

	/**
	 * Checks if the verb phrase is negated
	 * @return the negation of the verb phrase
	 */
	public boolean isNegated() {
		return isNegated;
	}

	/**
	 * Sets the negation of the verb phrase
	 * @param isNegated the negation of the verb phrase
	 */
	public void setNegated(boolean isNegated) {
		this.isNegated = isNegated;
	}

	/**
	 * Gets the modal of the phrase
	 * @return the modal of the phrase
	 */
	public String getModal() {
		return modal;
	}

	/**
	 * Sets the modal of the phrase
	 * @param modal the modal of the phrase
	 */
	public void setModal(String modal) {
		this.modal = modal;
	}

	/**
	 * Gets the particle in the phrase
	 * @return the particle in the phrase
	 */
	public String getParticle() {
		return particle;
	}

	/**
	 * Sets the particle in the phrase
	 * @param particle the particle in the phrase
	 */
	public void setParticle(String particle) {
		this.particle = particle;
	}

	/**
	 * Creates a default verb phrase for a word. If there is a need to adjust the default instance,
	 * just reset a component by calling the setters of the instance returned
	 *
	 * @return the verb phrase created given the information passed
	 */
	public static VerbPhrase createVerbPhrase(Word verb){

		VerbPhrase vp = new VerbPhrase(verb.getString());

		vp.setComplement(null);
		vp.setModal("");
		vp.setModifiers(null);
		vp.setNegated(false);
		vp.setNumberAgr(NumberAgr.SINGULAR);
		vp.setParticle("");
		vp.setPostmodifier(null);
		vp.setPremodifier(null);
		vp.setProgressive(false);
		vp.setTense(Tense.PAST);

		return vp;
	}

	/**
	 * Creates a default verb phrase for phrases. If there is a need to adjust the default instance,
	 * just reset a component by calling the setters of the instance returned
	 *
	 * @return the verb phrase created given the information passed
	 */
	public static VerbPhrase createVerbPhrase(CharacterGoal cg, Phrase verb, boolean isNegated) throws StoryGeneratorException{

		Vector<PhraseSpec> complements = new Vector<PhraseSpec>();
		Vector<String> modifiers = new Vector<String>();
		String postModifier = null;

		if(verb.size() > 1){
			String secondPOS = verb.get(1).getPartOfSpeech();

			if(verb.size()-1 == 1 && (secondPOS.equalsIgnoreCase(POSKeywords.NOUN) || secondPOS.equalsIgnoreCase(POSKeywords.PRONOUN))){
				NounPhrase np = NounPhrase.createNounPhrase(cg, verb.get(1).getString(), verb.get(1).isSingular(), verb.get(1).getClassification());
				np.setDeterminer("");
				complements.add(SentenceElemFactory.createNounPhrase(np));
			}
			else if(verb.size()-1 >= 1 && (secondPOS.equalsIgnoreCase("Noun")))
				for (int i = 1; i < verb.size(); i++){
					NounPhrase np = NounPhrase.createNounPhrase(cg, verb.get(i).getString(), verb.get(i).isSingular(), verb.get(i).getClassification());
					complements.add(SentenceElemFactory.createNounPhrase(np));
				}
			else if(verb.size()-1 >= 1 && secondPOS.equalsIgnoreCase("Adjective")){
				int index = 1;
				String succPOS = secondPOS;
				while(succPOS.equalsIgnoreCase("Adjective") && index < verb.size() - 1){
					modifiers.add(verb.get(index).getString());
					index++;
					if(index < verb.size())
						succPOS = verb.get(index).getPartOfSpeech();
				}
				NounPhrase np = NounPhrase.createNounPhrase(cg, verb.get(index).getString(), verb.get(index).isSingular(), verb.get(index).getClassification());
				np.setModifiers(modifiers);
				complements.add(SentenceElemFactory.createNounPhrase(np));

				for (int i = index+1; i < verb.size(); i++) {
					np = NounPhrase.createNounPhrase(cg, verb.get(i).getString(), verb.get(i).isSingular(), verb.get(i).getClassification());
					complements.add(SentenceElemFactory.createNounPhrase(np));
				}
			}
			else if(secondPOS.equalsIgnoreCase("Adjective")){
				AdjectivePhrase ap = new AdjectivePhrase(verb.get(1).getString());
				complements.add(SentenceElemFactory.createAdjPhrase(ap));
			}
			else if(secondPOS.equalsIgnoreCase(POSKeywords.ADVERB)){		// added (verb + adverb)
				int index = 1;
				postModifier = verb.get(index).getString();
				if(index+1<verb.size() && verb.get(index+1).getPartOfSpeech().equalsIgnoreCase(POSKeywords.NOUN))
				{
					NounPhrase np = NounPhrase.createNounPhrase(cg, verb.get(index+1).getString(), verb.get(index+1).isSingular(), verb.get(index+1).getClassification());
					complements.add(SentenceElemFactory.createNounPhrase(np));
				}
			}
			else if(secondPOS.equalsIgnoreCase(POSKeywords.PREPOSITION)){			// added (verb + preposition)
				int index = 2;
				PrepositionalPhrase prep = PrepositionalPhrase.createPrepPhrase(cg, verb, index);

				complements.add(SentenceElemFactory.createPrepPhrase(prep));
			}
		}

		VerbPhrase vp = new VerbPhrase(verb.get(0).getString());
		vp.setComplement(complements);
		vp.setTense(Tense.PAST);
		vp.setNumberAgr(NumberAgr.SINGULAR);
		if(postModifier!=null)
			vp.setPostmodifier(postModifier);
		vp.setProgressive(false);
		vp.setNegated(isNegated);

		return vp;
	}

}
