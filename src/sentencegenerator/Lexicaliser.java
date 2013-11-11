package sentencegenerator;

import java.util.Vector;

import pbmain.util.DBObject;
import pbmain.util.Parser;
import database_entities.CharacterGoal;

/**
 * This class lexicalises each character goal in the input abstract story tree
 * by converting the word and phrase objects into their string counterparts.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
public class Lexicaliser{

	/**
	 * Applies lexicalisation to each character goal
	 * @param charGoals vector of character goals to be lexicalised
	 */
	public void applyLexicaliser(Vector<CharacterGoal> charGoals){
		for (int i = 0; i < charGoals.size(); i++) {
			lexicalise(charGoals.get(i));
		}
	}

	/**
	 * Performs lexicalisation to a character goal
	 * @param charGoal the character goal to be lexicalised
	 */
	public void lexicalise(CharacterGoal charGoal){

		charGoal.setAgensVector(lexicalise(charGoal.getAgensDB()));

		charGoal.setPatiensVector(lexicalise(charGoal.getPatiensDB()));

		charGoal.setActionVector(lexicalise(charGoal.getActionDB()));

		charGoal.setTargetVector(lexicalise(charGoal.getTargetDB()));

		charGoal.setInstrumentVector(lexicalise(charGoal.getInstrumentDB()));
	}

	/**
	 * Lexicalise a single object
	 * @param obj word or phrase object to be lexicalised
	 * @return resulting vector of strings after lexicalising the object
	 */
	private Vector<String> lexicalise(DBObject obj){

		if(obj != null){
			return Parser.parseString(obj.getString(), " ");
		}
		return null;
	}
}