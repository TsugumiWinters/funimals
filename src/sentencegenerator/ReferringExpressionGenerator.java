package sentencegenerator;

import java.util.Vector;

import pbmain.PBException;
import pbmain.util.DBObject;
import pbmain.util.Parser;
import pbmain.util.ParserException;
import sentencegenerator.component.Phrase;
import sentencegenerator.component.sentence.POSKeywords;
import simplenlg.features.Gender;

import com.picturebooks.mobilepicturebooks.ApplicationContextProvider;

import database.DatabaseHelper;
import database_entities.CharacterGoal;
import database_entities.IGCharacter;
import database_entities.IGObject;
import database_entities.Word;

/**
 * This class performs REG (Referring Expression Generator) which replaces subsequent references to entities
 * with either a pronoun or a noun phrase referring expression that is sufficient
 * for the reader (system user) to identify which entity is being referred to.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
public class ReferringExpressionGenerator {

	private String previousAgens = "";
	private String previousPatiens = "";

	private int ctr = 0;
	
	private static DatabaseHelper dbHelper = new DatabaseHelper(ApplicationContextProvider.getContext());

	/**
	 * Applies REG to each character goal
	 * @param storyLine vector of character goals to be processed
	 * @param age the user's age used to determine which words in the lexicon should be used
	 * @throws StoryGeneratorException raised when an error occurred while performing REG
	 */
	public void applyREG(Vector<CharacterGoal> storyLine, int age) throws StoryGeneratorException{

		String currentAgens = "", currentPatiens = "";

		for (int i = 0; i < storyLine.size(); i++) {

			if(storyLine.get(i).getTargetDB() instanceof CharacterGoal){
				Vector<CharacterGoal> target = new Vector<CharacterGoal>();
				target.add((CharacterGoal)storyLine.get(i).getTargetDB());
								
				applyREG(target, age);
			}
			else {
				currentAgens = storyLine.get(i).getAgensDB().getID();

				if(storyLine.get(i).getPatiensDB() != null){
					currentPatiens = storyLine.get(i).getPatiensDB().getID();
				}
				else currentPatiens = "";

				if(ctr < 1){
					if(currentAgens.equalsIgnoreCase(previousAgens)){
						currentAgens = insertPronounForAgens(currentAgens, age);
						ctr++;
					}
					else if(currentAgens.equalsIgnoreCase(previousPatiens)){
						//check if gender is same with the previousAgens
						if(previousAgens.contains("CH") && previousPatiens.contains("CH")){
							IGCharacter prevAgens, prevPatiens;
							try {
								prevAgens = dbHelper.instantiateCharacter(previousAgens);
								prevPatiens = dbHelper.instantiateCharacter(previousPatiens);
							} catch (PBException e) {
								throw new StoryGeneratorException(e);
							}
							if(!prevAgens.getGender().equalsIgnoreCase(prevPatiens.getGender())){
								currentAgens = insertPronounForAgens(currentAgens, age);	//apply REG
							}
							else{
								previousAgens = currentAgens;
							}
						}
					}
					else{
						previousAgens = currentAgens;
					}
					if(!currentPatiens.isEmpty() && currentPatiens.equalsIgnoreCase(previousPatiens)){
						currentPatiens = insertPronounForPatiens(currentPatiens, age);
						ctr++;
					}
					else if(currentPatiens.equalsIgnoreCase(previousAgens)){
						//check if gender is same with the previousPatiens
						if(previousAgens.contains("CH") && previousPatiens.contains("CH")){
							IGCharacter prevAgens, prevPatiens;
							try {
								prevAgens = dbHelper.instantiateCharacter(previousAgens);
								prevPatiens = dbHelper.instantiateCharacter(previousPatiens);
							} catch (PBException e) {
								throw new StoryGeneratorException(e);
							}
							if(!prevAgens.getGender().equalsIgnoreCase(prevPatiens.getGender())){
								currentPatiens = insertPronounForPatiens(currentPatiens, age);	//apply REG
							}
							else{
								previousPatiens = currentPatiens;
							}
						}
					}
					else{
						previousPatiens = currentPatiens;
					}
				}
				else{
					ctr = 0;
					previousAgens = currentAgens;
					previousPatiens = currentPatiens;
				}

				resetAgens(storyLine.get(i), currentAgens, age);
				if(currentPatiens != null && !currentPatiens.isEmpty()){
					resetPatiens(storyLine.get(i), currentPatiens, age);
				}
			}
		}
	}

	/**
	 * Changes the agens of the character goal with the appropriate pronoun or retains its original value
	 * @param currentCg current character goal being processed
	 * @param currentAgens the value of agens after it is processed
	 * @param age the user's age used to determine which words in the lexicon should be used
	 * @throws StoryGeneratorException raised when an error occurred during the process
	 */
	private void resetAgens(CharacterGoal currentCg, String currentAgens, int age) throws StoryGeneratorException{

		try{
			if(currentAgens.contains("WORD")){
				if(currentAgens.contains(" ")){
					currentCg.setAgensDB(Parser.parseWords(currentAgens, age));
				}
				else{
					currentCg.setAgensDB(dbHelper.instantiateWord(currentAgens, age));
				}
			}
			else if(currentAgens.contains("CH")){
				currentCg.setAgensDB(addAnimalType((IGCharacter)currentCg.getAgensDB()));
			}
			else if(currentAgens.contains("OB")){
				currentCg.setAgensDB(dbHelper.instantiateObject(currentAgens));
			}
		}catch(PBException e){
			throw new StoryGeneratorException(e);
		}catch(ParserException e){
			throw new StoryGeneratorException(e);
		}
	}

	/**
	 * Changes the patiens of the character goal with the appropriate pronoun or retains its original value
	 * @param currentCg current character goal being processed
	 * @param currentPatiens the value of patiens after it is processed
	 * @param age the user's age used to determine which words in the lexicon should be used
	 * @throws StoryGeneratorException raised when an error occurred during the process
	 */
	private void resetPatiens(CharacterGoal currentCg, String currentPatiens, int age) throws StoryGeneratorException{

		try{
			if(currentPatiens.contains("WORD")){
				if(currentPatiens.contains(" ")){
					currentCg.setPatiensDB(Parser.parseWords(currentPatiens, age));
				}
				else{
					currentCg.setPatiensDB(dbHelper.instantiateWord(currentPatiens, age));
				}
			}
			else if(currentPatiens.contains("CH")){
				currentCg.setPatiensDB(addAnimalType((IGCharacter)currentCg.getPatiensDB()));
			}
			else if(currentPatiens.contains("OB")){
				currentCg.setPatiensDB(dbHelper.instantiateObject(currentPatiens));
			}
		}catch(PBException e){
			throw new StoryGeneratorException(e);
		}catch(ParserException e){
			throw new StoryGeneratorException(e);
		}
	}

	private Vector<String> charList = new Vector<String>();

	/**
	 * Appends the animal type of the character to its name
	 * @param character the character whose name is processed
	 * @return the character object after its name is processed
	 * @throws StoryGeneratorException raised when an error occurred during the process
	 */
	private IGCharacter addAnimalType(IGCharacter character) throws StoryGeneratorException{

		String charID = "";
		if(character != null){
			charID = character.getID();
			String roleID;
			try {
				roleID = dbHelper.getWordId("child", 4);
			} catch (PBException e) {
				throw new StoryGeneratorException(e);
			}
			if(!charList.contains(charID) && roleID.equalsIgnoreCase(character.getRole())){
				character.setString(character.getString() + " the " + character.getType());
				charList.add(charID);
			}
		}
		return character;
	}

	/**
	 * Gets the pronoun appropriate for the agens of the character goal
	 * @param currentAgens the current agens being processed
	 * @param age the user's age used to determine which words in the lexicon should be used
	 * @return appropriate pronoun for the agens
	 * @throws StoryGeneratorException raised when an error occurred during the process
	 */
	private String insertPronounForAgens(String currentAgens, int age) throws StoryGeneratorException{

		try{
			if(currentAgens.contains("WORD") || currentAgens.contains("OB")){

				String andID = dbHelper.getWordId("and", age);

				if(currentAgens.contains(" ") && currentAgens.contains(andID)){
					currentAgens = dbHelper.getWordId("they", age);
				}
				else{
					currentAgens = dbHelper.getWordId("it", age);
				}
			}
			else if(currentAgens.contains("CH")){
				if(dbHelper.instantiateCharacter(currentAgens).getGender().equalsIgnoreCase("f")){
					currentAgens = dbHelper.getWordId("she", age);
				}
				else if(dbHelper.instantiateCharacter(currentAgens).getGender().equalsIgnoreCase("m")){
					currentAgens = dbHelper.getWordId("he", age);
				}
			}
		}catch(PBException e){
			throw new StoryGeneratorException(e);
		}
		return currentAgens;
	}

	/**
	 * Gets the pronoun appropriate for the patiens of the character goal
	 * @param currentPatiens the current patiens being processed
	 * @param age the user's age used to determine which words in the lexicon should be used
	 * @return appropriate pronoun for the patiens
	 * @throws StoryGeneratorException raised when an error occurred during the process
	 */
	private String insertPronounForPatiens(String currentPatiens, int age) throws StoryGeneratorException{

		try{
			if(currentPatiens.contains("WORD") || currentPatiens.contains("OB")){
				String andID = dbHelper.getWordId("and", age);
				if(currentPatiens.contains(" ") && currentPatiens.contains(andID)){
					currentPatiens = dbHelper.getWordId("them", age);
				}
				else{
					currentPatiens = dbHelper.getWordId("it", age);
				}
			}
			else if(currentPatiens.contains("CH")){
				if(dbHelper.instantiateCharacter(currentPatiens).getGender().equalsIgnoreCase("f")){
					currentPatiens = dbHelper.getWordId("her", age);
				}
				else if(dbHelper.instantiateCharacter(currentPatiens).getGender().equalsIgnoreCase("m")){
					currentPatiens = dbHelper.getWordId("him", age);
				}
			}
		}catch(PBException e){
			throw new StoryGeneratorException(e);
		}
		return currentPatiens;
	}

	/**
	 * Gets the appropriate pronoun for character objects
	 * @param obj the character whose appropriate pronoun is determined
	 * @param pronoun the appropriate pronoun for a character which is either "he" or "she"
	 * @return the Gender of the character which can either be "masculine" or "feminine"
	 */
	private static Gender applyPronoun(IGCharacter obj, String pronoun){
		if(obj.getGender().equalsIgnoreCase("f")){
			pronoun = "she";
			return Gender.FEMININE;
		}
		else{
			pronoun = "he";
			return Gender.MASCULINE;
		}
	}

	/**
	 * Gets the appropriate pronoun for objects
	 * @param obj the object whose appropriate pronoun is determined
	 * @param pronoun the appropriate pronoun for an object which is "it"
	 * @return the Gender of the object which is "neuter"
	 */
	private static Gender applyPronoun(IGObject obj, String pronoun){
		pronoun = "it";
		return Gender.NEUTER;
	}

	/**
	 * Gets the appropriate pronoun for word objects
	 * @param obj the object whose appropriate pronoun is determined
	 * @param pronoun the appropriate pronoun for an object which is "it"
	 * @return the Gender of the object which is "neuter"
	 */
	private static Gender applyPronoun(Word obj, String pronoun){
		if(obj.getPartOfSpeech().equalsIgnoreCase(POSKeywords.PRONOUN)){
			pronoun = obj.getString();
			if(pronoun.equalsIgnoreCase("he") || pronoun.equalsIgnoreCase("him"))
				return Gender.MASCULINE;
			else if(pronoun.equalsIgnoreCase("she") || pronoun.equalsIgnoreCase("her"))
				return Gender.FEMININE;
			else return Gender.NEUTER;
		}
		else{
			pronoun = "it";
			return Gender.NEUTER;
		}
	}

	/**
	 * Gets the appropriate pronoun for a group of objects
	 * @param obj the group of objects whose appropriate pronoun is determined
	 * @param pronoun the appropriate pronoun for the group of objects which is "they"
	 * @return the Gender of the group of objects which is "neuter"
	 */
	private static Gender applyPronoun(Phrase obj, String pronoun){
		pronoun = "they";
		return Gender.NEUTER;
	}

	/**
	 * Gets the appropriate pronoun for the passed object (can be a character, an object, or a group of objects)
	 * @param obj the object whose appropriate pronoun is determined
	 * @param pronoun the appropriate pronoun for the passed object
	 * @return the Gender of the object
	 */
	public static Gender applyPronoun(DBObject obj, String pronoun){
		if(obj instanceof IGCharacter)
			return applyPronoun((IGCharacter)obj, pronoun);
		else if(obj instanceof Word)
			return applyPronoun((Word)obj, pronoun);
		else if(obj instanceof Phrase)
			return applyPronoun((Phrase)obj, pronoun);
		else
			return applyPronoun((IGObject)obj, pronoun);
	}

	/**
	 * Gets the gender of the character passed
	 * @param actor the character whose gender is determined
	 * @return the gender of the character
	 */
	public static Gender getGender(IGCharacter actor){
		return applyPronoun(actor, "");
	}

	/**
	 * Gets the word object of the appropriate pronoun for the character
	 * @param obj the character whose appropriate pronoun is determined
	 * @return the word object representing the pronoun
	 * @throws PBException raised when an error occurred while getting the word representation of the pronoun
	 */
	private static Word getPronoun(IGCharacter obj) throws PBException{
		if(obj.getGender().equalsIgnoreCase("f")){
			return dbHelper.getWord("she", 5);
		}
		else{
			return dbHelper.getWord("he", 5);
		}
	}

	/**
	 * Gets the word object of the appropriate pronoun for the word
	 * @param obj the word whose appropriate pronoun is determined
	 * @return the word object representing the pronoun
	 * @throws PBException raised when an error occurred while getting the word representation of the pronoun
	 */
	private static Word getPronoun(Word obj) throws PBException{
		if(obj.getPartOfSpeech().equalsIgnoreCase(POSKeywords.PRONOUN)){
			return obj;
		}
		else{
			return dbHelper.getWord("it", 5);
		}
	}

	/**
	 * Gets the word object of the appropriate pronoun for the object
	 * @param obj the object whose appropriate pronoun is determined
	 * @return the word object representing the pronoun
	 * @throws PBException raised when an error occurred while getting the word representation of the pronoun
	 */
	private static Word getPronoun(IGObject obj) throws PBException{
		return dbHelper.getWord("it", 5);
	}

	/**
	 * Gets the word object of the appropriate pronoun for group of objects
	 * @param obj the group of objects whose appropriate pronoun is determined
	 * @return the word object representing the pronoun
	 * @throws PBException raised when an error occurred while getting the word representation of the pronoun
	 */
	private static Word getPronoun(Phrase obj) throws PBException{
		return dbHelper.getWord("they", 5);
	}

	/**
	 * Gets the appropriate pronoun, in its Word object representation, for the passed object
	 * 		(can be a character, an object, or a group of objects)
	 * @param obj the object whose appropriate pronoun is determined
	 * @return the Word object representation of the pronoun
	 * @throws PBException raised when an error occurred during the process
	 */
	public static Word getPronoun(DBObject obj) throws PBException{
		if(obj instanceof Word)
			return getPronoun((Word)obj);
		else if(obj instanceof IGCharacter)
			return getPronoun((IGCharacter)obj);
		else if(obj instanceof Phrase)
			return getPronoun((Phrase)obj);
		else
			return getPronoun((IGObject)obj);
	}

}
