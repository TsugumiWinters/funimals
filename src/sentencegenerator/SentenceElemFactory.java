package sentencegenerator;

import java.util.Vector;

import sentencegenerator.component.sentence.AdjectivePhrase;
import sentencegenerator.component.sentence.NounPhrase;
import sentencegenerator.component.sentence.PrepositionalPhrase;
import sentencegenerator.component.sentence.Sentence;
import sentencegenerator.component.sentence.VerbPhrase;
import simplenlg.features.NumberAgr;
import simplenlg.features.SModifierPosition;
import simplenlg.lexicon.Lexicon;
import simplenlg.realiser.AdjPhraseSpec;
import simplenlg.realiser.NPPhraseSpec;
import simplenlg.realiser.PPPhraseSpec;
import simplenlg.realiser.PhraseSpec;
import simplenlg.realiser.Realiser;
import simplenlg.realiser.SPhraseSpec;
import simplenlg.realiser.VPPhraseSpec;

import com.picturebooks.mobilepicturebooks.ApplicationContextProvider;

import database.DatabaseHelper;
import database_entities.CharacterGoal;
import database_entities.Word;

/**
 * This class converts the phrase specifications used by the system
 * into the phrase specifications the simplenlg surface realiser library accepts.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
public class SentenceElemFactory {

	/**
	 * Creates simplenlg's noun phrase specification given the system's noun phrase object
	 * @param np system's representation of noun phrase to be converted into simplenlg's noun phrase specification
	 * @return simplenlg's noun phrase specification
	 */
	public static NPPhraseSpec createNounPhrase(NounPhrase np){

		NPPhraseSpec nounPhrase = new NPPhraseSpec(np.getNoun());

		nounPhrase.setPossessive(np.isPossessive());
		nounPhrase.setGender(np.getGender());

		if(np.isPlural())
			nounPhrase.setNumber(NumberAgr.PLURAL);
		else nounPhrase.setNumber(NumberAgr.SINGULAR);

		if(np.getModifiers()!=null)
			for (int i = 0; i < np.getModifiers().size(); i++)
				nounPhrase.addModifier(np.getModifiers().get(i));

		if(np.getPremodifier()!=null)
			nounPhrase.addPremodifier(np.getPremodifier());

		if(np.getPostmodifiers()!=null)
			for (int i = 0; i < np.getPostmodifiers().size(); i++)
				nounPhrase.addPostmodifier(np.getPostmodifiers().get(i));

		if(np.getDeterminer()!=null)
			nounPhrase.setDeterminer(np.getDeterminer());

		return nounPhrase;
	}

	/**
	 * Creates simplenlg's adjective phrase specification given the system's adjective phrase object
	 * @param ap system's representation of adjective phrase to be converted into simplenlg's adjective phrase specification
	 * @return simplenlg's adjective phrase specification
	 */
	public static AdjPhraseSpec createAdjPhrase(AdjectivePhrase ap){

		AdjPhraseSpec adjPhrase = new AdjPhraseSpec(ap.getAdjective());

		if(ap.getModifiers()!=null)
			for (int i = 0; i < ap.getModifiers().size(); i++)
				adjPhrase.addModifier(ap.getModifiers().get(i));

		if(ap.getPremodifier()!=null)
			adjPhrase.addPremodifier(ap.getPremodifier());

		if(ap.getPostmodifier()!=null)
			adjPhrase.addPostmodifier(ap.getPostmodifier());

		return adjPhrase;
	}

	/**
	 * Creates simplenlg's verb phrase specification given the system's verb phrase object
	 * @param vp system's representation of verb phrase to be converted into simplenlg's verb phrase specification
	 * @return simplenlg's verb phrase specification
	 */
	public static VPPhraseSpec createVerbPhrase(VerbPhrase vp){

		VPPhraseSpec verbPhrase;

		if(vp.getModal() != null && !vp.getModal().isEmpty()){
			if(vp.getModal().contains("have")){
				vp.setVerb(new Lexicon().getPastParticiple(vp.getVerb()));
			}
			verbPhrase = new VPPhraseSpec(vp.getVerb());
			verbPhrase.setModal(vp.getModal());
		}
		else verbPhrase = new VPPhraseSpec(vp.getVerb());

		verbPhrase.setTense(vp.getTense());
		verbPhrase.setProgressive(vp.isProgressive());
		verbPhrase.setNumber(vp.getNumberAgr());
		verbPhrase.setNegated(vp.isNegated());

		if(vp.getParticle()!=null)
			verbPhrase.setParticle(vp.getParticle());

		if(vp.getComplement() != null){
			for (int i = 0; i < vp.getComplement().size(); i++){
				verbPhrase.addComplement(vp.getComplement().get(i));
			}
		}
		if(vp.getModifiers()!=null)
			for (int i = 0; i < vp.getModifiers().size(); i++)
				verbPhrase.addModifier(vp.getModifiers().get(i));

		if(vp.getPremodifier()!=null)
			verbPhrase.addPremodifier(vp.getPremodifier());

		if(vp.getPostmodifier()!=null)
			verbPhrase.addPostmodifier(vp.getPostmodifier());

		return verbPhrase;
	}

	/**
	 * Creates simplenlg's prepositional phrase specification given the system's prepositional phrase object
	 * @param pp system's representation of prepositional phrase to be converted into simplenlg's prepositional phrase specification
	 * @return simplenlg's prepositional phrase specification
	 */
	public static PPPhraseSpec createPrepPhrase(PrepositionalPhrase pp){

		PPPhraseSpec prepPhrase = new PPPhraseSpec(pp.getPreposition());

		if(pp.getComplement()!=null){
			if(pp.getComplement() instanceof Vector)
				setPPComplement(prepPhrase, (Vector)pp.getComplement());
			else setPPComplement(prepPhrase, pp.getComplement());
		}

		return prepPhrase;
	}

	/**
	 * Sets the complements of the given prepositional phrase
	 * @param prepPhrase the prepositional phrase whose complements will be set
	 * @param complement vector of complements to be attached to the given prepositional phrase
	 */
	private static void setPPComplement(PPPhraseSpec prepPhrase, Vector<Object> complement){

		for (int i = 0; i < complement.size(); i++) {
			prepPhrase.addComplement(complement.get(i));
		}
	}

	/**
	 * Sets the complement of the given prepositional phrase
	 * @param prepPhrase the prepositional phrase whose complement will be set
	 * @param complement the complement to be attached to the given prepositional phrase
	 */
	private static void setPPComplement(PPPhraseSpec prepPhrase, Object complement){

		prepPhrase.addComplement(complement);
	}

	/**
	 * Creates simplenlg's sentence phrase specification given the system's sentence object
	 * @param se system's representation of sentence to be converted into simplenlg's sentence phrase specification
	 * @return simplenlg's sentence phrase specification
	 */
	public static SPhraseSpec createClause(Sentence se){

		SPhraseSpec clause = new SPhraseSpec();

		for (int i = 0; i < se.getSubjects().size(); i++)
			clause.addSubject(se.getSubjects().get(i));

		if(se.getVerbphrase()!=null){
			clause.setVerbPhrase(se.getVerbphrase());
			clause.setTense(se.getTense());
		}

		if(se.getComplement()!= null)
			clause.setComplement(se.getComplement());

		if(se.getModifiers()!=null)
			for (int i = 0; i < se.getModifiers().size(); i++)
				clause.addModifier(se.getModifiers().get(i));

		if(se.getPremodifier()!=null)
			clause.addPremodifier(se.getPremodifier());

		if(se.getPostmodifier()!=null)
			clause.addPostmodifier(se.getPostmodifier());

		if(se.getPostVerbModifier()!=null){
			for (int i = 0; i < se.getPostVerbModifier().size(); i++){
			clause.addModifier(SModifierPosition.POST_VERB, se.getPostVerbModifier().get(i));
			}
		}

		return clause;
	}

	/**
	 * Creates simplenlg's sentence phrase specification given the system's sentence object
	 * @param se system's representation of sentence to be converted into simplenlg's sentence phrase specification
	 * @return simplenlg's sentence phrase specification
	 */
	public static SPhraseSpec createSentence(Sentence se) throws StoryGeneratorException{

		SPhraseSpec sentence = new SPhraseSpec();
		try{
		for (int i = 0; i < se.getSubjects().size(); i++)
			sentence.addSubject(se.getSubjects().get(i));

		if(se.getVerbphrase()!=null){
			sentence.setVerbPhrase(se.getVerbphrase());
			sentence.setTense(se.getTense());
		}

		if(se.getFrontModifier()!=null)
			sentence.addFrontModifier(se.getFrontModifier());

		if(se.getComplement()!=null)
			sentence.setComplement(se.getComplement());

		if(se.getPostVerbModifier()!=null){
			for (int i = 0; i < se.getPostVerbModifier().size(); i++){
				sentence.addModifier(SModifierPosition.POST_VERB, se.getPostVerbModifier().get(i));
			}
		}

		if(se.getModifiers()!=null)
			for (int i = 0; i < se.getModifiers().size(); i++)
				sentence.addModifier(se.getModifiers().get(i));

		if(se.getPremodifier()!=null)
			sentence.addPremodifier(se.getPremodifier());

		if(se.getPostmodifier()!=null)
			sentence.addPostmodifier(se.getPostmodifier());
		}catch(Exception e){
			throw new StoryGeneratorException(e);
		}
		return sentence;
	}

	/**
	 * Realises and converts simplenlg's phrase specification into a string sentence
	 * @param phraseSpec simplenlg's phrase specification to be converted
	 * @return string value of the created sentence
	 */
	public static String realiseSentence(PhraseSpec phraseSpec){
		Realiser r = new Realiser();
		String output;

		output = r.realiseDocument(phraseSpec);
		System.out.println(output);
		return output;
	}

	private static Vector<String> referencedNouns = new Vector<String>();
	private static DatabaseHelper dbHelper = new DatabaseHelper(ApplicationContextProvider.getContext());
	
	private final static int DEFAULTAGE = 6;

	/**
	 * Gets the appropriate determiner for the given noun
	 * @param charGoal the character goal which contains the noun whose determiner is needed
	 * @param noun the noun whose determiner is needed
	 * @return the appropriate determiner for the noun
	 */
	public static String determineDeterminer(CharacterGoal charGoal, String noun){

		String determiner = "";
		int age = DEFAULTAGE;

		try{
			String wordID = dbHelper.getWordId(noun, age);

			if(!isTimeOrLocation(wordID, age).isEmpty()){
				determiner = isTimeOrLocation(wordID, age);
			}

			Word nounWord = dbHelper.instantiateWord(wordID, age);

			if(determiner.isEmpty() && nounWord.getPartOfSpeech().equalsIgnoreCase("Noun")){
				if(referencedNouns.contains(noun) && nounWord.isSingular()){
					determiner = "the";
				}
				else{
					if(!nounWord.getClassification().equalsIgnoreCase("Mass")){
						if(!charGoal.getID().contains("TITL")){
							referencedNouns.add(noun);

							if (nounWord.isSingular()){
								if (noun.charAt(0) == 'a' || noun.charAt(0) == 'e' || noun.charAt(0) == 'i' ||
									noun.charAt(0) == 'o' || noun.charAt(0) == 'u')
									determiner = "an";
								else
									determiner = "a";
							}
						}
					}
				}
			}
		}catch(Exception e){
			System.out.println("noun is either a character or object");
		}
		return determiner;
	}

	/**
	 * Checks if the word refers to a time or location
	 * @param wordID id of the word
	 * @param age the user's age used to determine which words in the lexicon should be used
	 * @return empty string if the word does not refer to a time or location,
	 * 		else the determiner appropriate for the word
	 */
	private static String isTimeOrLocation(String wordID, int age){

		String secondConcept;
		try{
			String locationID = dbHelper.getWordId("location", age);
			String timeID = dbHelper.getWordId("time", age);
			secondConcept = dbHelper.getIntroConcepts(wordID, "isA", "things").get(0);
			if(secondConcept.equalsIgnoreCase(locationID) || secondConcept.equalsIgnoreCase(timeID))
				secondConcept = "the";
			else secondConcept = "";
		}catch(Exception e){
			secondConcept = "";
		}
		return secondConcept;
	}
}