/**
 *
 */
package storyplanner.plot;

import java.util.StringTokenizer;

import pbmain.PBException;
import pbmain.util.DBObject;
import pbmain.util.Parser;
import pbmain.util.ParserException;
import storyplanner.StoryElement;
import storyplanner.StoryPlannerException;

import com.picturebooks.funimals.ApplicationContextProvider;

import database.DatabaseHelper;
import database_entities.CharacterGoal;
import database_entities.Ontology;
import database_entities.SemanticRelationRule;

/**
 * This class handles the creation of dynamic character goals that can be included in the story when the ontology returns more than one relationship/path.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */

public class StoryRandomizer {

	private static int ctr = 0;
	private int age;
	private DatabaseHelper dbHelper = new DatabaseHelper(ApplicationContextProvider.getContext());

	private DBObject agens;
	private DBObject patiens;

	/**
	 * StoryRandomizer constructor that sets the agens and patiens of the dynamic character goal
	 * @param agens agens of the dynamic character goal
	 * @param patiens patiens of the dynamic character goal
	 */
	public StoryRandomizer(DBObject agens, DBObject patiens){
		this.agens = agens;
		this.patiens = patiens;
	}

	/**
	 * Creates a dynamic character goal
	 * @param rel ontology relationship that triggered the creation of dynamic character goal
	 * @param age age of the user
	 * @return {@link storyplanner.component.db.CharacterGoal} created
	 * @throws StoryPlannerException raised if an error occurred while creating the dynamic character goal
	 */
	public CharacterGoal createCharacterGoal(Ontology rel, int age) throws StoryPlannerException {

		CharacterGoal addedCg = new CharacterGoal();
		this.age = age;

		addedCg.setID("CGOLN" + getID());

		SemanticRelationRule rule = dbHelper.instantiateSemanticRule(rel.getSemanticRelation());

		setAction(addedCg, rule, rel);

		setAgens(addedCg, rule, rel);

		setPatiens(addedCg, rule, rel);

		setTarget(addedCg, rule, rel);

		setInstrument(addedCg, rule, rel);

		addedCg.setNegated(false);

		ctr++;

		return addedCg;
	}

	private void setAction(CharacterGoal addedCg, SemanticRelationRule rule, Ontology rel) throws StoryPlannerException {

		try{
			addedCg.setActionDB(Parser.parseWords(parseArgument(rule.getAction(), rel), age));
		}catch(ParserException e){
			throw new StoryPlannerException(e);
		}
	}

	private void setAgens(CharacterGoal addedCg, SemanticRelationRule rule, Ontology rel) throws StoryPlannerException {

		try{
			if(rule.getAgens() != null){
				String parsed = parseArgument(rule.getAgens(), rel);
				if(parsed.indexOf(StoryElement.CHARKEY) == 0){
					addedCg.setAgensDB(dbHelper.instantiateCharacter(parsed));
				}
				else if(parsed.indexOf(StoryElement.WORDKEY) == 0){
					addedCg.setAgensDB(Parser.parseWords(parsed, age));
				}
				else System.out.println("unidentified agens keyword");
			}
		}catch(PBException e){
			throw new StoryPlannerException(e);
		}catch(ParserException e){
			throw new StoryPlannerException(e);
		}
	}

	private void setPatiens(CharacterGoal addedCg, SemanticRelationRule rule, Ontology rel) throws StoryPlannerException {

		try{
			if(rule.getPatiens() != null){
				String parsed = parseArgument(rule.getPatiens(), rel);
				if(parsed.indexOf(StoryElement.CHARKEY) == 0){
					addedCg.setPatiensDB(dbHelper.instantiateCharacter(parsed));
				}
				else if(parsed.indexOf(StoryElement.WORDKEY) == 0){
					addedCg.setPatiensDB(Parser.parseWords(parsed, age));
				}
				else System.out.println("unidentified patiens keyword");
			}
		}catch(PBException e){
			throw new StoryPlannerException(e);
		}catch(ParserException e){
			throw new StoryPlannerException(e);
		}
	}

	private void setTarget(CharacterGoal addedCg, SemanticRelationRule rule, Ontology rel) throws StoryPlannerException  {

		try{
			if(rule.getTarget() != null){
				String parsed = parseArgument(rule.getTarget(), rel);
				if(parsed.indexOf(StoryElement.CHARKEY) == 0){
					addedCg.setTargetDB(dbHelper.instantiateCharacter(parsed));
				}
				else if(parsed.indexOf(StoryElement.WORDKEY) == 0){
					addedCg.setTargetDB(Parser.parseWords(parsed, age));
				}
				else System.out.println("unidentified target keyword");
			}
		}catch(PBException e){
			throw new StoryPlannerException(e);
		}catch(ParserException e){
			throw new StoryPlannerException(e);
		}
	}

	private void setInstrument(CharacterGoal addedCg, SemanticRelationRule rule, Ontology rel) throws StoryPlannerException {

		try{
			if(rule.getInstrument() != null){
				String parsed = parseArgument(rule.getInstrument(), rel);
				if(parsed.indexOf(StoryElement.CHARKEY) == 0){
					addedCg.setInstrumentDB(dbHelper.instantiateCharacter(parsed));
				}
				else if(parsed.indexOf(StoryElement.WORDKEY) == 0){
					addedCg.setInstrumentDB(Parser.parseWords(parsed, age));
				}
				else System.out.println("unidentified instrument keyword");
			}
		}catch(PBException e){
			throw new StoryPlannerException(e);
		}catch(ParserException e){
			throw new StoryPlannerException(e);
		}
	}

	private String parseArgument(String arg, Ontology rel) throws StoryPlannerException {

		String remainingArgs = "";

		if(arg.contains(" ")){
			remainingArgs = arg.substring(arg.indexOf(" ") + 1, arg.length());
			arg = arg.substring(0, arg.indexOf(" "));
		}

		if(arg.indexOf(StoryElement.CONCEPT1) == 0){
			arg = arg.substring(StoryElement.CONCEPT1.length());
			if(!remainingArgs.isEmpty())
				if(arg.isEmpty()) 														//additional condition
					return rel.getElement1().getElementID() + " " + parseArgument(remainingArgs, rel);
				else return handleCV(arg, rel.getElement1().getElementID()) + " " + parseArgument(remainingArgs, rel);
			else
				if(!arg.isEmpty()) 														//additional condition
					return handleCV(arg, rel.getElement1().getElementID());
				else return rel.getElement1().getElementID();
		}
		else if(arg.indexOf(StoryElement.CONCEPT2) == 0){
			arg = arg.substring(StoryElement.CONCEPT2.length());
			if(!remainingArgs.isEmpty())
				if(arg.isEmpty()) 														//additional condition
					return rel.getElement2().getElementID() + " " + parseArgument(remainingArgs, rel);
				else return handleCV(arg, rel.getElement2().getElementID()) + " " + parseArgument(remainingArgs, rel);
			else
				if(!arg.isEmpty()) 														//additional condition
					return handleCV(arg, rel.getElement2().getElementID());
				else return rel.getElement2().getElementID();
		}
		else if(arg.indexOf(StoryElement.WORDKEY) == 0){
			if(!remainingArgs.isEmpty())
				return arg + " " + parseArgument(remainingArgs, rel);
			else
				return arg;
		}
		else if(arg.indexOf(StoryElement.PREVAGENS) == 0){
			if(!remainingArgs.isEmpty())
				return agens.getID() + parseArgument(remainingArgs, rel);
			else
				return agens.getID();
		}
		else if(arg.indexOf(StoryElement.PREVPATIENS) == 0){
			if(!remainingArgs.isEmpty())
				return patiens.getID() + parseArgument(remainingArgs, rel);
			else
				return patiens.getID();
		}
		else if(arg.indexOf(StoryElement.LOCPREP) == 0){

			String ret = "";
			String concept = dbHelper.getConcept(rel.getElement2().getElementID(), "spatial");

			try{
				if(concept.equalsIgnoreCase(dbHelper.getWordId("specific", age)))
					ret = dbHelper.getWordId("in", age);
				else ret = dbHelper.getWordId("at", age);
			}catch(PBException e){
				throw new StoryPlannerException(e);
			}

			if(!remainingArgs.isEmpty())
				return ret + parseArgument(remainingArgs, rel);
			return ret;
		}
		else System.out.println("Error in interpreting a semantic rule");

		return "";
	}

	private String handleCV(String arg, String elementID) throws StoryPlannerException{

		StringTokenizer st = new StringTokenizer(elementID, " ");
		String ret = "";

		try{
			while(st.hasMoreTokens()){
				String nextToken = st.nextToken();
				if(arg.equalsIgnoreCase("C") && !dbHelper.instantiateWord(nextToken, age).getPartOfSpeech().equalsIgnoreCase("Verb"))	//dbLexicon.getPartOfSpeech(nextToken, age).contains("Verb"))
					ret += dbHelper.instantiateWord(nextToken, age).getID() + " ";
				else if(arg.equalsIgnoreCase("V") && dbHelper.instantiateWord(nextToken, age).getPartOfSpeech().equalsIgnoreCase("Verb"))
					ret += dbHelper.instantiateWord(nextToken, age).getID() + " ";
			}
		}catch(PBException e){
			throw new StoryPlannerException(e);
		}

		if(ret.isEmpty())
			System.out.println("Error in interpreting a semantic rule");

		return ret;
	}

	private String getID(){

		String identifier = "";

		int remainder = 3 - Integer.toString(ctr).length();
		for(int i=0; i<remainder; i++){
			identifier += "0";
		}
		identifier += ctr;

		return identifier;
	}
}
