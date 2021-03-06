package sentencegenerator;

import java.util.Random;
import java.util.Vector;

import pbmain.PBException;
import pbmain.util.DBObject;
import sentencegenerator.component.DBLexicalObject;
import sentencegenerator.component.Phrase;
import sentencegenerator.component.sentence.AdjectivePhrase;
import sentencegenerator.component.sentence.Clause;
import sentencegenerator.component.sentence.NounPhrase;
import sentencegenerator.component.sentence.POSKeywords;
import sentencegenerator.component.sentence.PrepositionalPhrase;
import sentencegenerator.component.sentence.Sentence;
import sentencegenerator.component.sentence.VerbPhrase;
import simplenlg.features.Category;
import simplenlg.features.Form;
import simplenlg.features.Gender;
import simplenlg.features.NumberAgr;
import simplenlg.features.SModifierPosition;
import simplenlg.features.Tense;
import simplenlg.lexicon.Lexicon;
import simplenlg.realiser.NPPhraseSpec;
import simplenlg.realiser.PhraseFactory;
import simplenlg.realiser.PhraseSpec;
import simplenlg.realiser.SPhraseSpec;
import simplenlg.realiser.VPPhraseSpec;
import database_entities.CharacterGoal;
import database_entities.IGCharacter;
import database_entities.Word;

/**
 * This class processes each character goal in the input abstract story tree,
 * creates their simplenlg phrase specifications counterpart and finally,
 * converts them into natural language sentences which make up the output story.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
public class LASGenerator {

	private final String[] frontModifiers = {"From then on, ", "Afterwards, ", "From that day onwards, ", "After that day, "};

	private Vector<String> story = new Vector<String>();

	private Vector<String> referencedChars = new Vector<String>();

	private boolean isPatiensPostVerbMod = false, isError = false, isSubjectAVP = false;

	/**
	 * LASGenerator constructor
	 *
	 */
	public LASGenerator() {

		System.out.println("\nSurface Realiser:");
		System.out.println("----------------------------------------------");
	}

	/**
	 * Called if an error occurred while generating the story
	 * @param isError true if there's an error, false if otherwise
	 */
	public void setError(boolean isError){
		this.isError = isError;
	}

	/**
	 * Calls the class' different methods to generate sentences based on the character goals
	 * @param characterGoals vector of character goals to be converted into sentences
	 * @throws StoryGeneratorException raised if an error occurred while converting the character goals
	 * 			into sentences
	 */
	public void generateSentence(Vector<CharacterGoal> characterGoals) throws StoryGeneratorException{

		for (int i = 0; i < characterGoals.size(); i++) {

			System.out.println("JASMINE STRING: " + characterGoals.get(i).getString());		//debug

			SPhraseSpec sentence = processCharGoal(characterGoals.get(i));

			if(characterGoals.get(i).getID().equalsIgnoreCase("CGOLTITL")){
				sentence.setTense(Tense.PRESENT);
			}
			else if(i==characterGoals.size()-1 && !isError){
	    		int a = new Random().nextInt(frontModifiers.length);
	    		sentence.addFrontModifier(frontModifiers[a]);
	    		sentence.addModifier(SModifierPosition.PRE_VERB, "always");
			}

			story.add(SentenceElemFactory.realiseSentence(sentence));

			System.out.println("");		//debug
		}
	}

	/**
	 * Gets the story generated by the system
	 * @return the vector of sentences that make up the output story
	 */
	public Vector<String> getStory(){
		return story;
	}

	/**
	 * Used for debug purposes; Prints the contents of the simplenlg's representation of phrase in the console
	 * @param identifier used to identify the phrase specification
	 * @param phraseSpec the simplenlg's representation of phrase whose contents are printed
	 */
	private void debug(String identifier, PhraseSpec phraseSpec){
		System.out.print(identifier + ": ");
		SentenceElemFactory.realiseSentence(phraseSpec);
	}

	/**
	 * Used for debug purposes; Prints the contents of the vector of phrase specification in the console
	 * @param identifier used to identify the phrase specification
	 * @param phraseSpecs the vector of phrase specifications whose contents are printed
	 */
	private void debug(String identifier, Vector<PhraseSpec> phraseSpecs){
		System.out.print(identifier + ": ");
		for (int i = 0; i < phraseSpecs.size(); i++) {
			SentenceElemFactory.realiseSentence(phraseSpecs.get(i));
		}
	}

	/**
	 * Process the contents of each character goal
	 * @param cg the character goal being processed
	 * @return simplenlg's representation of a sentence
	 * @throws StoryGeneratorException raised if an error occurred while processing the character goals
	 */
	private SPhraseSpec processCharGoal(CharacterGoal cg) throws StoryGeneratorException{

		setPatiensPostVerbMod(false);
		setSubjectAVP(false);

		Sentence se = new Sentence();

		Vector<PhraseSpec> agensPS = realiseAgens(cg);
		se.setSubjects(agensPS);
		debug("Agens", agensPS);

		PhraseSpec actionPS = realiseAction(cg, agensPS, se);
		if(actionPS instanceof VPPhraseSpec){
			se.setVerbphrase((VPPhraseSpec)actionPS);
			se.setTense(((VPPhraseSpec)actionPS).getTense());
		}
		else{
			Vector mods = new Vector();
			mods.add(actionPS);
			mods.addAll(realisePatiens(cg));
			se.setModifiers(mods);
			se.setVerbphrase(null);
		}
		debug("Action", actionPS);

		PhraseSpec instrumentPS;
		if(cg.getInstrument() != null){
			System.out.println(cg.getInstrumentDB().getString());
			instrumentPS = realiseInstrument(cg);
			se.setPostmodifier(instrumentPS);
			debug("Instrument", instrumentPS);
		}

		PhraseSpec targetPS;
		if(cg.getTargetDB()!=null && !cg.isHideTarget()){
			targetPS = realiseTarget(cg, actionPS, se);
			Vector<Object> mod = new Vector<Object>();
			mod.add(targetPS);
			se.setModifiers(mod);
			debug("Target", targetPS);
		}

		return SentenceElemFactory.createSentence(se);
	}

	private static final int AGENS_TYPE = 1;
	private static final int PATIENS_TYPE = 2;

	/**
	 * Realises the agens or, in other words, the subject of the sentence
	 * @param cg current character goal being processed
	 * @return vector of simplenlg's representation of a phrase which contains the subject of the sentence
	 * @throws StoryGeneratorException raised if an error occurred
	 * 			while realising the agens of the sentence
	 */
	private Vector<PhraseSpec> realiseAgens(CharacterGoal cg) throws StoryGeneratorException{
		return realiseAgensOrPatiens(cg, AGENS_TYPE);
	}

	/**
	 * Realises the patiens or, in other words, direct object of the sentence
	 * @param cg current character goal being processed
	 * @return vector of simplenlg's representation of a phrase which contains the direct object of the sentence
	 * @throws StoryGeneratorException raised if an error occurred
	 * 			while realising the patiens of the sentence
	 */
	private Vector<PhraseSpec> realisePatiens(CharacterGoal cg) throws StoryGeneratorException{
		return realiseAgensOrPatiens(cg, PATIENS_TYPE);
	}

	/**
	 * Realises the agens or patiens of the sentence
	 * @param cg current character goal being processed
	 * @param identifies if the one being proccessed is the agens or the patiens
	 * @return vector of simplenlg's representation of a phrase which contains the agens or patiens of the sentence
	 * @throws StoryGeneratorException raised if an error occurred
	 * 			while realising the agens or patiens of the sentence
	 */
	private Vector<PhraseSpec> realiseAgensOrPatiens(CharacterGoal cg, int  type) throws StoryGeneratorException{  //add a parameter if agens or patiens

		DBObject actor = null;

		switch(type){
			case AGENS_TYPE: actor = cg.getAgensDB(); break;
			case PATIENS_TYPE: actor = cg.getPatiensDB();
		}

		Vector<PhraseSpec> subjects = new Vector<PhraseSpec>();
		if(actor instanceof IGCharacter){ 	// agens is character
			NounPhrase np = NounPhrase.createNounPhrase(cg, ((IGCharacter)actor).getString(), true, "char");
			np.setGender(ReferringExpressionGenerator.getGender((IGCharacter)actor));
			subjects.add(SentenceElemFactory.createNounPhrase(np));
		}
		else{ 		//agens is composed of a word or a phrase
			Word firstWord = getWord(actor, 0);
			String premodifier = "";

			if(firstWord.getType().equalsIgnoreCase("negation")){
				premodifier = firstWord.getString();
				firstWord = getWord(actor, 1);
			}
			if(firstWord.getPartOfSpeech().equalsIgnoreCase(POSKeywords.VERB)){
				setSubjectAVP(true);
				VerbPhrase vp = createVerbPhrase(cg, actor, false);
				NounPhrase np = NounPhrase.createNounPhrase(cg, new Lexicon().getIngForm(firstWord.getString()), true, "concept");	//NounPhrase.createNounPhrase(cg, new Lexicon().getIngForm(firstWord.getString()));
				np.setModifiers(vp.getComplement());
				np.setPremodifier(premodifier);
				np.setDeterminer("");
				subjects.add(SentenceElemFactory.createNounPhrase(np));
			}
			else{
				String actorTemp = actor.getString();
				if(firstWord.getPartOfSpeech().equalsIgnoreCase(POSKeywords.ADJECTIVE)){
					premodifier = firstWord.getString();
					actorTemp = actor.getString().substring(actor.getString().indexOf(" ") + 1);
				}
				NounPhrase np = NounPhrase.createNounPhrase(cg, actorTemp, firstWord.isSingular(), firstWord.getClassification());
				np.setPremodifier(premodifier);
				if(firstWord.getPartOfSpeech().equalsIgnoreCase(POSKeywords.PRONOUN)){
					np.setGender(ReferringExpressionGenerator.applyPronoun(firstWord, ""));
				}

				System.out.println("agens/patiens plural: " + firstWord.isSingular() + " " + np.isPlural());
				subjects.add(SentenceElemFactory.createNounPhrase(np));
			}
		}
		return subjects;
	}


	/**
	 * Realises the action or, in other words, the verb of the sentence
	 * @param cg current character goal being processed
	 * @param agensPS the phrase specification counterpart of the agens of the sentence
	 * @param sentence the sentence counterpart of the character goal
	 * @return the simplenlg's representation of a phrase which contains the verb of the sentence
	 * @throws StoryGeneratorException raised if an error occurred
	 * 			while realising the action of the sentence
	 */
	private PhraseSpec realiseAction(CharacterGoal cg, Vector<PhraseSpec> agensPS, Sentence sentence) throws StoryGeneratorException{

		Tense actionTense = Tense.PAST;
		Vector<PhraseSpec> complements = new Vector<PhraseSpec>();
		String prep = "", locPrep = "";

		if(cg.getActionDB().getString().contains("can")){
			String canPhrase = "";
			for (int i = 0; i < ((Phrase)cg.getActionDB()).size(); i++) {
				if(getWord((Phrase)cg.getActionDB(),i).getString().equalsIgnoreCase("can")){
					if(cg.isNegated()){
						canPhrase += "could not ";
					}
					else
						canPhrase += "could ";
				}
				else if(!getWord((Phrase)cg.getActionDB(),i).getString().equalsIgnoreCase("not"))
					canPhrase +=((Phrase)cg.getActionDB()).get(i).getString() + " ";
			}

			return PhraseFactory.makeStringPhraseSpec(Category.VERB, canPhrase);
		}

		if(agensPS.get(0) instanceof NPPhraseSpec){
			if(cg.getAgensDB() instanceof IGCharacter ||
					(cg.getAgensDB() instanceof Word && ((Word)cg.getAgensDB()).getPartOfSpeech().equalsIgnoreCase(POSKeywords.PRONOUN)) ||
					cg.getID().equalsIgnoreCase("CGOLTIME")){
				actionTense = Tense.PAST;
			}
			else if(!isSubjectAVP){
				actionTense = Tense.PAST;
			}
			else{
				actionTense = Tense.PRESENT;
			}
		}
		else if(agensPS.get(0) instanceof VPPhraseSpec){
			actionTense = Tense.PRESENT;
		}

		DBLexicalObject action = cg.getActionDB();
		VerbPhrase vp = new VerbPhrase();

		if(action instanceof Phrase){

			Phrase actionPhrase = (Phrase)action;

			boolean isNegated = false;

			if(actionPhrase.containsWord("not")){
				isNegated = true;
				actionPhrase.remove(actionPhrase.getIndex("not"));
			}

			if(actionPhrase.containsPOS(POSKeywords.PREPOSITION)){
				for (int i = 0; i < actionPhrase.size(); i++) {
					Word word = getWord(actionPhrase, i);
					if(word.getPartOfSpeech().equalsIgnoreCase(POSKeywords.PREPOSITION)){
						if(word.getType().equalsIgnoreCase("location")){
							if(i == actionPhrase.size()-1){
								locPrep = word.getString();
								actionPhrase.remove(i);
								i--;
							}
						}
						else if(word.getString().equalsIgnoreCase("to") ||
								word.getString().equalsIgnoreCase("before") ||
								word.getString().equalsIgnoreCase("on")){
							prep = word.getString();
							if(i == actionPhrase.size()-1){
								actionPhrase.remove(i);
								i--;
							}

						}
						else{
							prep = word.getString();
							actionPhrase.remove(i);
							i--;
						}
					}
				}
			}

			String modal = "";
			if(actionPhrase.containsWord("should")){
				modal += "should";
				actionPhrase.remove(actionPhrase.getIndex("should"));
				actionTense = Tense.PRESENT;
				if(actionPhrase.containsWord("have")){
					if(!modal.equalsIgnoreCase("")){
						modal+=" ";
					}
					modal += "have";
					actionPhrase.remove(actionPhrase.getIndex("have"));
					actionTense = Tense.PAST;
				}
			}


			if(actionPhrase.containsPOS(POSKeywords.ADVERB)){
				int adverbIndex = actionPhrase.getPOSIndex(POSKeywords.ADVERB);
				int verbIndex = actionPhrase.getPOSIndex(POSKeywords.VERB);
				if(adverbIndex > verbIndex){
						vp.setPostmodifier(actionPhrase.get(adverbIndex).getString());
				}
				else{
					vp.setPremodifier(actionPhrase.get(adverbIndex).getString());
				}
				actionPhrase.remove(adverbIndex);
			}

			int index = 1;	//verb
			if(actionPhrase.size() < 2)
				index = 0;
			String partOfSpeech = actionPhrase.get(index).getPartOfSpeech();
			if(partOfSpeech.equalsIgnoreCase(POSKeywords.ADJECTIVE)){
				partOfSpeech = actionPhrase.get(actionPhrase.size()-1).getPartOfSpeech();	//???
				if(index == actionPhrase.size()-1){
					AdjectivePhrase ap = new AdjectivePhrase(actionPhrase.get(index).getString());
					actionPhrase.remove(index);
					complements.add(SentenceElemFactory.createAdjPhrase(ap));
				}
				if(partOfSpeech.equalsIgnoreCase(POSKeywords.NOUN)){
					Vector<String> nounMods = new Vector<String>();
					for (int i = index; i < actionPhrase.size(); i++) {
						partOfSpeech = actionPhrase.get(i).getPartOfSpeech();
						if(partOfSpeech.equalsIgnoreCase(POSKeywords.ADJECTIVE)){
							nounMods.add(actionPhrase.get(i).getString());
						}
						else if(partOfSpeech.equalsIgnoreCase(POSKeywords.NOUN)){
							NounPhrase np = NounPhrase.createNounPhrase(cg, actionPhrase.get(i).getString(), actionPhrase.get(i).isSingular(), actionPhrase.get(i).getClassification());
							np.setModifiers(nounMods);
							np.setDeterminer("");
							complements.add(SentenceElemFactory.createNounPhrase(np));
							nounMods = new Vector<String>();
						}
						actionPhrase.remove(i);
						i--;
					}
				}
			}
			else if(partOfSpeech.equalsIgnoreCase(POSKeywords.NOUN)){
				Vector<String> nounMods = new Vector<String>();
				for (int i = 0; i < actionPhrase.size(); i++) {
					partOfSpeech = actionPhrase.get(i).getPartOfSpeech();
					if(partOfSpeech.equalsIgnoreCase(POSKeywords.ADJECTIVE)){
						nounMods.add(actionPhrase.get(i).getString());
						actionPhrase.remove(i);
						i--;
					}
					else if(partOfSpeech.equalsIgnoreCase(POSKeywords.NOUN)){
						NounPhrase np = NounPhrase.createNounPhrase(cg, actionPhrase.get(i).getString(), actionPhrase.get(i).isSingular(), actionPhrase.get(i).getClassification());
						np.setModifiers(nounMods);
						complements.add(SentenceElemFactory.createNounPhrase(np));
						nounMods.removeAllElements();
						actionPhrase.remove(i);
						i--;
					}
				}
			}
			else if(partOfSpeech.equalsIgnoreCase(POSKeywords.PREPOSITION)){
				partOfSpeech = actionPhrase.get(index + 1).getPartOfSpeech();
				if(partOfSpeech.equalsIgnoreCase(POSKeywords.ARTICLE))
					partOfSpeech = actionPhrase.get(index + 2).getPartOfSpeech();
				if(partOfSpeech.equalsIgnoreCase(POSKeywords.NOUN) || partOfSpeech.equalsIgnoreCase(POSKeywords.ADJECTIVE)){
					Vector<String> nounMods = new Vector<String>();
					Vector<PhraseSpec> comp = new Vector<PhraseSpec>();
					for (int i = index + 1; i < actionPhrase.size(); i++) {
						partOfSpeech = actionPhrase.get(i).getPartOfSpeech();
						if(partOfSpeech.equalsIgnoreCase(POSKeywords.ADJECTIVE)){
							nounMods.add(actionPhrase.get(i).getString());
						}
						else if(partOfSpeech.equalsIgnoreCase(POSKeywords.NOUN)){
							NounPhrase np = NounPhrase.createNounPhrase(cg, actionPhrase.get(i).getString(), actionPhrase.get(i).isSingular(), actionPhrase.get(i).getClassification());
							np.setModifiers(nounMods);
							comp.add(SentenceElemFactory.createNounPhrase(np));
							nounMods.removeAllElements();
						}
						actionPhrase.remove(i);
						i--;
					}
					PrepositionalPhrase pp = new PrepositionalPhrase(actionPhrase.get(index).getString());
					actionPhrase.remove(index);
					pp.setComplement(comp);
					complements.add(SentenceElemFactory.createPrepPhrase(pp));
				}
			}

			action = (Phrase)actionPhrase;

			vp.setVerb(action.getString());
			vp.setModal(modal);
			sentence.setPostVerbModifier(complements);
			vp.setNegated(isNegated);
			vp.setTense(actionTense);
			if(agensPS.size()>1){
				vp.setNumberAgr(NumberAgr.PLURAL);
			}
			else{
				if(((NPPhraseSpec)agensPS.get(0)).getNumber() == NumberAgr.PLURAL){
					vp.setNumberAgr(NumberAgr.PLURAL);
				}
				else
					vp.setNumberAgr(NumberAgr.SINGULAR);
			}

			vp.setProgressive(false);
		}
		else{
			action = (Word)action;
			vp.setVerb(action.getString());
			vp.setTense(actionTense);
			if(agensPS.size()>1){
				vp.setNumberAgr(NumberAgr.PLURAL);
			}
			else{
				if(((NPPhraseSpec)agensPS.get(0)).getNumber() == NumberAgr.PLURAL){
					vp.setNumberAgr(NumberAgr.PLURAL);
				}
				else
					vp.setNumberAgr(NumberAgr.SINGULAR);
			}
			vp.setProgressive(false);
		}

		if(cg.getPatiensDB() != null){
			Vector<PhraseSpec> patiens = realisePatiens(cg);
			debug("Patiens", patiens);							// debug purpose
			if(!prep.isEmpty()){
				PrepositionalPhrase pp = new PrepositionalPhrase(prep);
				pp.setComplement(patiens);
				if(complements.isEmpty()){
					complements.add(SentenceElemFactory.createPrepPhrase(pp));
					sentence.setPostVerbModifier(complements);
				}
				else vp.setPostmodifier(SentenceElemFactory.createPrepPhrase(pp));
			}
			else if(cg.getPatiensDB() instanceof IGCharacter){
				vp.setComplement(patiens);

			}
			else if(cg.getPatiensDB() instanceof Word || cg.getPatiensDB() instanceof Phrase){
				Word word = getWord(cg.getPatiensDB(), 0);
				if(word.getPartOfSpeech().equalsIgnoreCase(POSKeywords.PRONOUN))
					vp.setComplement(patiens);
				else{
					sentence.setPostVerbModifier(patiens);
					setPatiensPostVerbMod(true);
				}
			}
		}
		else if(!locPrep.isEmpty()){
			complements.add(SentenceElemFactory.createPrepPhrase(new PrepositionalPhrase(locPrep)));
			sentence.setPostVerbModifier(complements);
		}
		else if(cg.getPatiensDB() == null && cg.getTargetDB() != null && prep != null){
			PrepositionalPhrase pp = new PrepositionalPhrase(prep);
			if(complements.isEmpty())
			{
				complements.add(SentenceElemFactory.createPrepPhrase(pp));
			}
		}
		return SentenceElemFactory.createVerbPhrase(vp);
	}

	/**
	 * Realises the target or, in other words, the object of the verb of the sentence
	 * @param cg current character goal being processed
	 * @param agensPS the phrase specification counterpart of the agens of the sentence
	 * @param sentence the sentence counterpart of the character goal
	 * @return the simplenlg's representation of a phrase which contains the object of the verb of the sentence
	 * @throws StoryGeneratorException raised if an error occurred
	 * 			while realising the action of the sentence
	 */
	private PhraseSpec realiseTarget(CharacterGoal cg, PhraseSpec actionPS, Sentence sentence) throws StoryGeneratorException{

		DBObject target = cg.getTargetDB();
		PhraseSpec targetPS = null;
		Word firstWord = null;
		int firstWordIndex = 0;
		boolean isNegated = false;

		if(cg.getID().equalsIgnoreCase("CGOLASKP")){
			String targetConj = "";
			for (int i = 0; i < ((Phrase)target).size(); i++) {
				targetConj +=((Phrase)target).get(i).getString() + " ";
			}
			return PhraseFactory.makeStringPhraseSpec(Category.CONJUNCTION, targetConj);
		}
		else if(target instanceof Phrase){
			if(((Phrase)target).size() > 1){
				for (int i = 0; i < ((Phrase)target).size(); i++) {
					if(getWord(target, i).getString().equalsIgnoreCase("not"))
					{
						if(actionPS instanceof VPPhraseSpec)
						{
							((VPPhraseSpec)actionPS).setNegated(true);
							((Phrase)target).remove(getWord(target, i));
						}
					}
				}
				firstWord = getWord(target, firstWordIndex);
				if(cg.getTargetDB().getString().contains("be ")){
					VPPhraseSpec verb = new VPPhraseSpec("be");
					String complement = "";

					for (int i = 1; i < ((Phrase)target).size(); i++) {
						complement +=((Phrase)target).get(i).getString() + " ";
					}

					verb.setComplement(complement);
					verb.setForm(Form.INFINITIVE);
					sentence.setIndirectObject(verb);
					return verb;
				}
				else if(firstWord.getPartOfSpeech().equalsIgnoreCase(POSKeywords.VERB)){
					if(cg.getActionDB().getString().equalsIgnoreCase("be")){
						VerbPhrase vp = VerbPhrase.createVerbPhrase(cg, (Phrase)target, isNegated);
						vp.setVerb(new Lexicon().getIngForm(firstWord.getString()));
						vp.setTense(Tense.PRESENT);
						vp.setNumberAgr(NumberAgr.PLURAL);
						targetPS = SentenceElemFactory.createVerbPhrase(vp);
					}
					else if(!(cg.getActionDB().getString().equalsIgnoreCase("be"))){
						if(actionPS instanceof VPPhraseSpec){
							if(((VPPhraseSpec)actionPS).getTense().equals(Tense.PRESENT)){
								VerbPhrase vp = VerbPhrase.createVerbPhrase(cg, (Phrase)target, isNegated);
								vp.setVerb(new Lexicon().getIngForm(firstWord.getString()));
								vp.setNumberAgr(NumberAgr.PLURAL);
								vp.setTense(Tense.PRESENT);
								if(isPatiensPostVerbMod()){
									Vector<PhraseSpec> patiens = sentence.getPostVerbModifier();
									if(vp.getComplement()!= null)
										patiens.addAll(vp.getComplement());
									sentence.setPostVerbModifier(null);
									vp.setComplement(patiens);
								}
								if(cg.getAction().contains("WORD0117")){
									targetPS = SentenceElemFactory.createVerbPhrase(vp);
								}
								else{
									PrepositionalPhrase pp = new PrepositionalPhrase("to");
									pp.setComplement(SentenceElemFactory.createVerbPhrase(vp));
									targetPS = SentenceElemFactory.createPrepPhrase(pp);
								}
							}
							else{
								VerbPhrase vp = VerbPhrase.createVerbPhrase(cg, (Phrase)target, isNegated);
								vp.setNumberAgr(NumberAgr.PLURAL);
								vp.setTense(Tense.PRESENT);
								if(isPatiensPostVerbMod() && !cg.getTargetDB().getString().contains("be")){
									Vector<PhraseSpec> patiens = sentence.getPostVerbModifier();
									if(!vp.getComplement().isEmpty()){
										patiens.addAll(vp.getComplement());

									}
									sentence.setPostVerbModifier(null);
									vp.setComplement(patiens);

								}
								PrepositionalPhrase pp = new PrepositionalPhrase("to");
								pp.setComplement(SentenceElemFactory.createVerbPhrase(vp));
								targetPS = SentenceElemFactory.createPrepPhrase(pp);
							}
						}
					}
				}
				else if(firstWord.getPartOfSpeech().equalsIgnoreCase(POSKeywords.ADJECTIVE)){
					if(firstWordIndex != ((Phrase)target).size() - 1){
						if(getWord(target, firstWordIndex+1).getPartOfSpeech().equalsIgnoreCase(POSKeywords.PREPOSITION)){
							PrepositionalPhrase prep = PrepositionalPhrase.createPrepPhrase(cg, (Phrase)target, firstWordIndex+2);
							prep.setPreposition(getWord(target, firstWordIndex+1).getString());
							Vector<Object> mods = new Vector<Object>();
							mods.add(SentenceElemFactory.createPrepPhrase(prep));
							AdjectivePhrase ap = new AdjectivePhrase(firstWord.getString());
							ap.setModifiers(mods);
							targetPS = SentenceElemFactory.createAdjPhrase(ap);
						}
						else{
							Word word = firstWord;
							Vector<String> adj = new Vector<String>();
							while(word.getPartOfSpeech().equalsIgnoreCase(POSKeywords.ADJECTIVE) && firstWordIndex < ((Phrase)target).size()-1){
								adj.add(word.getString());
								firstWordIndex++;
								if(firstWordIndex < ((Phrase)target).size()-1)
									word = getWord(target, firstWordIndex);
							}
							word = getWord(target, ((Phrase)target).size()-1);			// gets the noun
							NounPhrase np = NounPhrase.createNounPhrase(cg, word.getString(), word.isSingular(), word.getClassification());
							np.setModifiers(adj);
							targetPS = SentenceElemFactory.createNounPhrase(np);
						}
					}
					else{
						AdjectivePhrase adjp = new AdjectivePhrase(getWord(target, firstWordIndex).getString());
						targetPS = SentenceElemFactory.createAdjPhrase(adjp);
					}
				}
				else if(firstWord.getPartOfSpeech().equalsIgnoreCase(POSKeywords.NOUN)){
					NounPhrase np = NounPhrase.createNounPhrase(cg, target.getString(), firstWord.isSingular(), firstWord.getClassification());
					targetPS = SentenceElemFactory.createNounPhrase(np);
				}
			}
		}
		else if(target instanceof Word){
			if(getWord(target, firstWordIndex).getPartOfSpeech().equalsIgnoreCase(POSKeywords.VERB))
			{
				if(cg.getActionDB().getString().equalsIgnoreCase("be")){
					VerbPhrase vp = new VerbPhrase();
					vp.setVerb(new Lexicon().getIngForm(getWord(target, firstWordIndex).getString()));
					vp.setTense(Tense.PRESENT);
					vp.setNumberAgr(NumberAgr.PLURAL);
					targetPS = SentenceElemFactory.createVerbPhrase(vp);
				}
				else{
					VerbPhrase vp = new VerbPhrase(getWord(target, firstWordIndex).getString());
					vp.setTense(Tense.PRESENT);
					vp.setNumberAgr(NumberAgr.PLURAL);
					if(isPatiensPostVerbMod()){
						Vector<PhraseSpec> patiens = sentence.getPostVerbModifier();
						sentence.setPostVerbModifier(null);
						vp.setComplement(patiens);
						setPatiensPostVerbMod(false);
					}
					if(cg.getAction().contains("WORD0117")){
						targetPS = SentenceElemFactory.createVerbPhrase(vp);
					}
					else{
						PrepositionalPhrase pp = new PrepositionalPhrase("to");
						pp.setComplement(SentenceElemFactory.createVerbPhrase(vp));
						targetPS = SentenceElemFactory.createPrepPhrase(pp);
					}
				}
			}
			else if(getWord(target, firstWordIndex).getPartOfSpeech().equalsIgnoreCase(POSKeywords.NOUN)){
					NounPhrase np = NounPhrase.createNounPhrase(cg, getWord(target, firstWordIndex).getString(), getWord(target, firstWordIndex).isSingular(), getWord(target, firstWordIndex).getClassification());
					targetPS = SentenceElemFactory.createNounPhrase(np);
			}
			else{
				AdjectivePhrase adjp = new AdjectivePhrase(getWord(target, firstWordIndex).getString());
				targetPS = SentenceElemFactory.createAdjPhrase(adjp);
			}
		}
		else if(target instanceof CharacterGoal){
			setPatiensPostVerbMod(false);
			setSubjectAVP(false);
			CharacterGoal goal = (CharacterGoal)target;
			Sentence se = new Sentence();

			if(goal.getAgensDB() instanceof IGCharacter){
				try {
					if(referencedChars.contains(((IGCharacter)goal.getAgensDB()).getID()))
						goal.setAgensDB(ReferringExpressionGenerator.getPronoun((IGCharacter)goal.getAgensDB()));
					else if(cg.getAgensDB() instanceof IGCharacter && ((IGCharacter)cg.getAgensDB()).getID().equalsIgnoreCase(((IGCharacter)goal.getAgensDB()).getID())
							|| cg.getPatiensDB() instanceof IGCharacter && ((IGCharacter)cg.getPatiensDB()).getID().equalsIgnoreCase(((IGCharacter)goal.getAgensDB()).getID()))
						goal.setAgensDB(ReferringExpressionGenerator.getPronoun((IGCharacter)goal.getAgensDB()));
					else
						referencedChars.add(((IGCharacter)goal.getAgensDB()).getID());
				} catch (PBException e) {
					throw new StoryGeneratorException(e);
				}
			}

			Vector<PhraseSpec> agensPSInt = realiseAgens(goal);
			se.setSubjects(agensPSInt);

			PhraseSpec actionPSInt = realiseAction(goal, agensPSInt, se);

			if(actionPSInt instanceof VPPhraseSpec){
				se.setVerbphrase((VPPhraseSpec)actionPSInt);
				se.setTense(((VPPhraseSpec)actionPSInt).getTense());
			}
			else{
				Vector mods = new Vector();
				mods.add(actionPSInt);
				mods.addAll(realisePatiens(goal));
				se.setModifiers(mods);
				se.setVerbphrase(null);
			}

			PhraseSpec instrumentPSInt = null;
			if(goal.getInstrument() != null){
				instrumentPSInt = realiseInstrument(goal);
				se.setPostmodifier(instrumentPSInt);
			}

			PhraseSpec targetPSInt;
			Vector<Object> mod = new Vector<Object>();
			if(goal.getTargetDB()!=null && !goal.isHideTarget()){
				targetPSInt = realiseTarget(goal, actionPSInt, se);
				mod.add(targetPSInt);
				se.setModifiers(mod);
			}

			targetPS = SentenceElemFactory.createClause(se);
		}
		return targetPS;
	}

	/**
	 * Realises the instrument of the sentence or, in other words, the object used when performing the action
	 * 			or a character the action is done with
	 * @param cg current character goal being processed
	 * @return the simplenlg's representation of a phrase which contains the instrument of the sentence
	 * @throws StoryGeneratorException raised if an error occurred
	 * 			while realising the instrument of the sentence
	 */
	private PhraseSpec realiseInstrument(CharacterGoal cg) throws StoryGeneratorException{

		DBObject instrument = cg.getInstrumentDB();

		if(instrument != null){
			if(instrument instanceof CharacterGoal){
				setPatiensPostVerbMod(false);
				setSubjectAVP(false);
				CharacterGoal goal = (CharacterGoal)instrument;
				Sentence se = new Sentence();

				Vector<PhraseSpec> agensPSInt = realiseAgens(goal);
				se.setSubjects(agensPSInt);

				PhraseSpec actionPSInt = realiseAction(goal, agensPSInt, se);
				if(actionPSInt instanceof VPPhraseSpec){
					se.setVerbphrase((VPPhraseSpec)actionPSInt);
					se.setTense(((VPPhraseSpec)actionPSInt).getTense());
				}
				else{
					Vector mods = new Vector();
					mods.add(actionPSInt);
					mods.addAll(realisePatiens(goal));
					se.setModifiers(mods);
					se.setVerbphrase(null);
				}

				PhraseSpec instrumentPSInt = null;
				if(goal.getInstrumentDB() != null){
					instrumentPSInt = realiseInstrument(goal);
					se.setPostmodifier(instrumentPSInt);
				}

				PhraseSpec targetPSInt;
				Vector<Object> mod = new Vector<Object>();
				if(goal.getTargetDB()!=null && !goal.isHideTarget()){
					targetPSInt = realiseTarget(goal, actionPSInt, se);
					mod.add(targetPSInt);
					se.setModifiers(mod);
				}
				return SentenceElemFactory.createClause(se);
			}
			else {
				Word firstWord = null;
				String premodifier = "";

				if(instrument instanceof DBLexicalObject)
					firstWord = getWord(instrument, 0);

				if(instrument instanceof IGCharacter || firstWord.getPartOfSpeech().equalsIgnoreCase(POSKeywords.NOUN)
						|| firstWord.getPartOfSpeech().equalsIgnoreCase(POSKeywords.ADJECTIVE)){
					String instTemp = instrument.getString();
					if(!(instrument instanceof IGCharacter) && firstWord.getPartOfSpeech().equalsIgnoreCase(POSKeywords.ADJECTIVE)){
						premodifier = firstWord.getString();
						instTemp = instrument.getString().substring(instrument.getString().indexOf(" ") + 1);
					}
					NounPhrase np = NounPhrase.createNounPhrase(cg, instTemp, true, "char");
					np.setPremodifier(premodifier);
					PrepositionalPhrase pp = new PrepositionalPhrase("with");
					pp.setComplement(SentenceElemFactory.createNounPhrase(np));
					return SentenceElemFactory.createPrepPhrase(pp);
				}
				else{
					boolean isNegated = false;

					if(firstWord.getType().equalsIgnoreCase("negation")){
						isNegated = true;
						((Phrase)instrument).remove(0);
						firstWord = ((Phrase)instrument).get(0);
					}

					if(firstWord.getPartOfSpeech().equalsIgnoreCase(POSKeywords.VERB)){
						String pronoun = cg.getAgensDB().getString();
						Gender gender= applyPronoun(cg.getAgensDB(), pronoun);

						VerbPhrase vp = createVerbPhrase(cg, instrument, isNegated);
						NounPhrase np = NounPhrase.createNounPhrase(cg, pronoun, true, "pronoun");
						np.setGender(gender);
						Clause cl = Clause.createClause(np, vp);

//						return SentenceElemFactory.createClause(cl);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Creates a Verb phrase given the verb and its constituents
	 * @param cg the current character goal being processed
	 * @param words words that constitute the verb phrase
	 * @param isNegated if the phrase should contain a negation word
	 * @return the Verb Phrase object created
	 * @throws StoryGeneratorException raised when an error occurred while creating a verb phrase
	 */
	private VerbPhrase createVerbPhrase(CharacterGoal cg, DBObject words, boolean isNegated) throws StoryGeneratorException{
		if (words instanceof Word)
			return VerbPhrase.createVerbPhrase((Word)words);
		else if(words instanceof Phrase)
			return VerbPhrase.createVerbPhrase(cg, (Phrase)words, isNegated);
		return null;
	}

	/**
	 * Gets the word at the specified index in the phrase
	 * @param words phrase searched for the word
	 * @param index index of the needed word
	 * @return word object at the specified index of the phrase
	 */
	private Word getWord(DBObject words, int index){
		if(words instanceof Phrase)
			return ((Phrase)words).get(index);
		return (Word)words;
	}

	/**
	 * Applies pronoun to the words
	 * @param agens phrase that contains words that should be applied with pronoun
	 * @param pronoun specific pronoun that word should be replaced with
	 * @return Gender of the pronoun applied
	 */
	private Gender applyPronoun(DBObject agens, String pronoun){
		if(agens instanceof IGCharacter)
			return ReferringExpressionGenerator.applyPronoun((IGCharacter)agens, pronoun);
		else if(agens instanceof Word)
			return ReferringExpressionGenerator.applyPronoun((Word)agens, pronoun);
		else
			return ReferringExpressionGenerator.applyPronoun((Phrase)agens, pronoun);
	}

	/**
	 * Gets the value of the variable which identifies if the patiens is a post verb modifier or not
	 * @return true if the patiens is a post verb modifier, false if otherwise
	 */
	private boolean isPatiensPostVerbMod() {
		return isPatiensPostVerbMod;
	}

	/**
	 * Sets the value of the variable which identifies if the patiens is a post verb modifier or not
	 * @param isPatiensPostVerbMod true if the patiens is a post verb modifier, false if otherwise
	 */
	private void setPatiensPostVerbMod(boolean isPatiensPostVerbMod) {
		this.isPatiensPostVerbMod = isPatiensPostVerbMod;
	}

	/**
	 * Gets the value of the variable which identifies if the agens is a verb phrase
	 * 		or, in other words, a gerund
	 * @return true if the agens is a verb phrase, false if otherwise
	 */
	private boolean isSubjectAVP() {
		return isSubjectAVP;
	}

	/**
	 * Sets the value of the variable which identifies if the the agens is a verb phrase
	 * 		or, in other words, a gerund
	 * @param isSubjectAVP true if the agens is a verb phrase, false if otherwise
	 */
	private void setSubjectAVP(boolean isSubjectAVP) {
		this.isSubjectAVP = isSubjectAVP;
	}
}
