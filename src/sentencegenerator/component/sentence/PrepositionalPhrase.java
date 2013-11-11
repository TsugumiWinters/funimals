
package sentencegenerator.component.sentence;

import java.util.Vector;

import sentencegenerator.SentenceElemFactory;
import sentencegenerator.StoryGeneratorException;
import sentencegenerator.component.Phrase;
import simplenlg.features.NumberAgr;
import simplenlg.features.Tense;
import simplenlg.realiser.PhraseSpec;
import database_entities.CharacterGoal;

/**
 * This class is used to interface with the prepositional phrase specification of
 * the simplenlg surface realiser library.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
public class PrepositionalPhrase {

	private String preposition;

	private Object complement;

	/**
	 * PrepositionalPhrase constructor that accepts and sets the preposition of the phrase
	 * @param preposition the preposition of the phrase
	 */
	public PrepositionalPhrase(String preposition){
		setPreposition(preposition);
	}

	/**
	 * Gets the preposition of the phrase
	 * @return the preposition of the phrase
	 */
	public String getPreposition() {
		return preposition;
	}

	/**
	 * Sets the preposition of the phrase
	 * @param preposition the preposition of the phrase
	 */
	public void setPreposition(String preposition) {
		this.preposition = preposition;
	}

	/**
	 * Gets the complement of the phrase
	 * @return the complement of the phrase
	 */
	public Object getComplement() {
		return complement;
	}

	/**
	 * Sets the complement of the phrase
	 * @param complement the complement of the phrase
	 */
	public void setComplement(Object complement) {
		this.complement = complement;
	}

	/**
	 * Creates a default prepositional phrase . If there is a need to adjust the default instance,
	 * just reset a component by calling the setters of the instance returned
	 *
	 * @param cg the character goal to which phrase belongs
	 * @param instrument contains the words that would make up the phrase
	 * @param index starting index in the phrase
	 * @return the prepositional phrase created given the information passed
	 * @throws StoryGeneratorException raised when an error occurred while creating the prepositional phrase
	 */
	public static PrepositionalPhrase createPrepPhrase(CharacterGoal cg, Phrase instrument, int index) throws StoryGeneratorException{
		Vector<PhraseSpec> compPrep = new Vector<PhraseSpec>();
		Vector<String> modifiers = new Vector<String>(), mods = new Vector<String>();;
		PrepositionalPhrase prep = null;
		boolean isNegated = false;

		String pos = instrument.get(index).getPartOfSpeech();

		if(pos.equalsIgnoreCase(POSKeywords.NOUN) || pos.equalsIgnoreCase(POSKeywords.PRONOUN) || pos.equalsIgnoreCase(POSKeywords.ARTICLE)){
			if(pos.equalsIgnoreCase(POSKeywords.ARTICLE))
				index++;
			if(index+1 < instrument.size() && instrument.get(index+1).getPartOfSpeech().equalsIgnoreCase(POSKeywords.ADVERB))
				mods.add(instrument.get(index+1).getString());
			NounPhrase np = NounPhrase.createNounPhrase(cg, instrument.get(index).getString(), instrument.get(index).isSingular(), instrument.get(index).getClassification());
			np.setPostmodifiers(mods);
			compPrep.add(SentenceElemFactory.createNounPhrase(np));
			prep = new PrepositionalPhrase(instrument.get(index-1).getString());
			prep.setComplement(compPrep);
		}
		else if(pos.equalsIgnoreCase(POSKeywords.ADJECTIVE)){
			String succPOS = pos;
			while(succPOS.equalsIgnoreCase(POSKeywords.ADJECTIVE) && index<instrument.size()-1){
				modifiers.add(instrument.get(index).getString());
				index++;
				if(index<instrument.size()-1)
					succPOS = instrument.get(index).getPartOfSpeech();
			}
			if(instrument.get(index).getPartOfSpeech().equalsIgnoreCase(POSKeywords.NOUN)){
				NounPhrase np = NounPhrase.createNounPhrase(cg, instrument.get(index).getString(), instrument.get(index).isSingular(), instrument.get(index).getClassification());
				np.setModifiers(modifiers);
				compPrep.add(SentenceElemFactory.createNounPhrase(np));
				prep = new PrepositionalPhrase(instrument.get(index-1).getString());
				prep.setComplement(compPrep);
			}
			else{
				AdjectivePhrase ap = new AdjectivePhrase(instrument.get(index).getString());
				compPrep.add(SentenceElemFactory.createAdjPhrase(ap));
				prep = new PrepositionalPhrase(instrument.get(index-1).getString());
				prep.setComplement(compPrep);
			}
		}
		else if(pos.equalsIgnoreCase(POSKeywords.VERB))
		{
			System.out.println("may verb inside prep");
			Phrase ins = new Phrase();
			for (int i = index; i < instrument.size(); i++) {
				ins.add(instrument.get(i));
				if(instrument.get(i).getString().equalsIgnoreCase("not"))
					isNegated = true;
			}
			VerbPhrase vp = VerbPhrase.createVerbPhrase(cg, ins, isNegated);		// need to consider index
			vp.setTense(Tense.PRESENT);
			vp.setNumberAgr(NumberAgr.PLURAL);
			prep = new PrepositionalPhrase(instrument.get(index-1).getString());
			prep.setComplement(SentenceElemFactory.createVerbPhrase(vp));
		}
		return prep;
	}
}
