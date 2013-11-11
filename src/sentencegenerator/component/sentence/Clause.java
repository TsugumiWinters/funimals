
package sentencegenerator.component.sentence;

import java.util.Vector;


import simplenlg.features.Tense;
import simplenlg.realiser.PhraseSpec;
import simplenlg.realiser.VPPhraseSpec;
import sentencegenerator.SentenceElemFactory;

/**
 * This class is used to interface with the clause phrase specification of
 * the simplenlg surface realiser library.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
public class Clause {

	private PhraseSpec subject;
	private VPPhraseSpec verbPhrase;

	private Vector<Object> modifiers;

	private Object complement;
	private Object premodifier;
	private Object postmodifier;

	private Tense tense;

	/**
	 * Clause constructor
	 */
	public Clause(){

	}

	/**
	 * Gets the subject of the clause
	 * @return the subject of the clause
	 */
	public PhraseSpec getSubject() {
		return subject;
	}

	/**
	 * Sets the subject of the clause
	 * @param subject the subject of the clause
	 */
	public void setSubject(PhraseSpec subject) {
		this.subject = subject;
	}

	/**
	 * Sets the subjects of the clause (if more than one)
	 * @param subject the subjects of the clause
	 */
	public void setSubject(Vector<PhraseSpec> subject) {
		for (int i = 0; i < subject.size(); i++) {
			this.setSubject(subject.get(i));
		}
	}

	/**
	 * Gets the verb phrase of the clause
	 * @return the verb phrase of the clause
	 */
	public VPPhraseSpec getVerbPhrase() {
		return verbPhrase;
	}

	/**
	 * Sets the verb phrase of the clause
	 * @param verbPhrase the verb phrase of the clause
	 */
	public void setVerbPhrase(VPPhraseSpec verbPhrase) {
		this.verbPhrase = verbPhrase;
	}

	/**
	 * Gets the complement of the clause
	 * @return the complement of the clause
	 */
	public Object getComplement() {
		return complement;
	}

	/**
	 * Sets the complement of the clause
	 * @param complement the complement of the clause
	 */
	public void setComplement(Object complement) {
		this.complement = complement;
	}

	/**
	 * Gets the modifiers of the clause
	 * @return the modifiers of the clause
	 */
	public Vector<Object> getModifiers() {
		return modifiers;
	}

	/**
	 * Sets the modifiers of the clause
	 * @param modifiers the modifiers of the clause
	 */
	public void setModifiers(Vector<Object> modifiers) {
		this.modifiers = modifiers;
	}

	/**
	 * Gets the premodifier of the clause
	 * @return the premodifier of the clause
	 */
	public Object getPremodifier() {
		return premodifier;
	}

	/**
	 * Sets the premodifier of the clause
	 * @param premodifier the premodifier of the clause
	 */
	public void setPremodifier(Object premodifier) {
		this.premodifier = premodifier;
	}

	/**
	 * Gets the postmodifier of the clause
	 * @return the postmodifier of the clause
	 */
	public Object getPostmodifier() {
		return postmodifier;
	}

	/**
	 * Sets the postmodifier of the clause
	 * @param postmodifier the postmodifier of the clause
	 */
	public void setPostmodifier(Object postmodifier) {
		this.postmodifier = postmodifier;
	}

	/**
	 * Gets the tense of the clause
	 * @return the tense of the clause
	 */
	public Tense getTense() {
		return tense;
	}

	/**
	 * Sets the tense of the clause
	 * @param tense the tense of the clause
	 */
	public void setTense(Tense tense) {
		this.tense = tense;
	}

	/**
	 * Creates a default clause. If there is a need to adjust the default instance,
	 * just reset a component by calling the setters of the instance returned
	 *
	 * @return the clause created given the information passed
	 */
	public static Clause createClause(NounPhrase np, VerbPhrase vp){

		Clause cl = new Clause();
		cl.setComplement(null);
		cl.setModifiers(null);
		cl.setPostmodifier(null);
		cl.setPremodifier(null);
		cl.setSubject(SentenceElemFactory.createNounPhrase(np));
		cl.setVerbPhrase(SentenceElemFactory.createVerbPhrase(vp));
		cl.setTense(Tense.PAST);

		return cl;
	}

	/**
	 * Creates a default clause. If there is a need to adjust the default instance,
	 * just reset a component by calling the setters of the instance returned
	 *
	 * @return the clause created given the information passed
	 */
	public static Clause createClause(Vector<PhraseSpec> np, VPPhraseSpec vp){

		Clause cl = new Clause();
		cl.setComplement(null);
		cl.setModifiers(null);
		cl.setPostmodifier(null);
		cl.setPremodifier(null);
		for (int i = 0; i < np.size(); i++) {
			cl.setSubject(np.get(i));
		}
		cl.setVerbPhrase(vp);
		cl.setTense(Tense.PAST);

		return cl;
	}
}
