package storyplanner.plot;

import java.util.StringTokenizer;
import java.util.Vector;

import pbmain.Debugger;
import pbmain.util.DBObject;
import pbmain.util.ParserException;
import storyplanner.StoryElement;
import storyplanner.StoryPlannerException;
import storyplanner.component.StoryTree;
import storyplanner.ontology.OntologyException;

import com.swiftshot.funimals.ApplicationContextProvider;

import database.DatabaseHelper;
import database_entities.CharacterGoal;
import database_entities.Ontology;

/**
 * This class performs parsing of the different story elements in the story plot.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */

public class PlannerParser {

	private static DatabaseHelper dbHelper = new DatabaseHelper(ApplicationContextProvider.getContext());
	
	/**
	 * Number of reference objects in the story
	 */
	public static int objectReference = 0;

	/**
	 * Converts the contents of the string representation of character goal into vector of strings
	 * @param cgString string representation of a character goal
	 * @return resulting vector of strings after parsing
	 */
	public static Vector<String> parseCharacterGoal(String cgString){

		Vector<String> parsedCg = new Vector<String>();

		StringTokenizer st = new StringTokenizer(cgString, ";");

		while(st.hasMoreTokens()){
			parsedCg.add(st.nextToken());
		}

		return parsedCg;
	}

	/**
	 * Parses the parameters of the given character goal
	 * @param arguments string representation of a character goal
	 * @param cg {@link storyplanner.component.db.CharacterGoal} which would contain the parsed parameters
	 * @throws StoryPlannerException raised if an error occurred while parsing the parameters of the character goal
	 */
	public static void parseParameters(String arguments, CharacterGoal cg) throws StoryPlannerException{

		try{
			arguments = arguments.substring(arguments.indexOf('(') + 1, arguments.length() - 1);	//Strip parentheses off the arguments

			String toBeSaved = "";

			while(!arguments.isEmpty()){

				for (String currentParameter : StoryElement.PARAMETERS) {

					if(arguments.indexOf(currentParameter) == 0){

						toBeSaved = arguments.substring(currentParameter.length());

						if(toBeSaved.indexOf("CGOL") == 0){
							toBeSaved = toBeSaved.substring(0, getParameterScope(toBeSaved, toBeSaved.indexOf("(")) + 1);
						}
						else if(toBeSaved.indexOf("%") == 0){
							if(toBeSaved.indexOf(StoryElement.ONTOKEY) == 1 || toBeSaved.indexOf(StoryElement.NEGATE) == 1){
								toBeSaved = toBeSaved.substring(0, getParameterScope(toBeSaved, toBeSaved.indexOf("(")) + 2); // +1 for the '%'
							}
							else {
								toBeSaved = toBeSaved.substring(0, toBeSaved.indexOf("%", 1) + 1);
							}
						}
						else { //WORD
							if(toBeSaved.contains(":")) // means there is still a remaining parameter
								toBeSaved = toBeSaved.substring(0, toBeSaved.indexOf(","));
						}

						arguments = arguments.substring(currentParameter.length() + toBeSaved.length());

						if(arguments.indexOf(',') == 0)
							arguments = arguments.substring(1);

						if(currentParameter.equalsIgnoreCase(StoryElement.PARAMAGENS)){
							cg.setAgens(toBeSaved);
							System.out.println("Agens: " + toBeSaved);
						}
						else if(currentParameter.equalsIgnoreCase(StoryElement.PARAMPATIENS)){
							cg.setPatiens(toBeSaved);
							System.out.println("Patiens: " + toBeSaved);
						}
						else if(currentParameter.equalsIgnoreCase(StoryElement.PARAMTARGET)){
							cg.setTarget(toBeSaved);
							System.out.println("Target: " + toBeSaved);
						}
						else if(currentParameter.equalsIgnoreCase(StoryElement.PARAMINSTRUMENT)){
							cg.setInstrument(toBeSaved);
							System.out.println("Instrument: " + toBeSaved);
						}
					}
				}
			}

		}catch(StringIndexOutOfBoundsException se){
			throw new StoryPlannerException(new ParserException());
		}
	}

	/*
	 * to be used by the parseParameter function
	 */
	private static int getParameterScope(String args, int index){

		int parenCtr = 0;

		while(index < args.length()){

			if(args.charAt(index) == '(')
				parenCtr++;
			else if(args.charAt(index) == ')')
				parenCtr--;
			if(parenCtr == 0)
				return index;

			index++;
		}

		return index;
	}

	/**
	 * Gets the id of the character goal
	 * @param cgString string representation of the character goal
	 * @return the id of the character goal
	 */
	public static String getCharGoalID(String cgString){
		return cgString.substring(0, cgString.indexOf('('));
	}

	private static int age;
	private static CharacterGoal charGoal;

	/**
	 * Parses the arguments of the given string representation (arg) of the character goal
	 * @param arg arguments to be parsed
	 * @param cg character goal where to put the parsed arguments
	 * @param st story tree where all the elements of the story are placed
	 * @param age age of the user
	 * @return the parsed argument
	 * @throws StoryPlannerException raised if an error occurred while parsing the specified argument
	 */
	public static String parseArgument(String arg, CharacterGoal cg, StoryTree st, int a) throws StoryPlannerException{

		age = a;
		charGoal = cg;

		return parseArgument(arg, st);
	}

	//note: dapat ang marereturn nia lagi ay isang WORD ID or CHAR ID
	private static String parseArgument(String arg, StoryTree storyTree) throws StoryPlannerException{

		if(arg.indexOf(StoryElement.WORDKEY) == 0){
			return arg;
		}
		else if(arg.charAt(0) != '%'){
			return deriveParsedValue(arg, storyTree);								//the parsed parameter value that is yet to be derived
		}
		else if(arg.charAt(0) == '%'){
			arg = arg.substring(1, arg.length()-1);									//eliminate the enclosing '%'
//			System.out.println("eliminated %: " + arg);
			if(arg.indexOf(StoryElement.ONTOKEY) == 0){

				String category = arg.substring(StoryElement.ONTOKEY.length(), arg.indexOf("("));
				String firstArg = "", secondArg = "";

				arg = arg.substring(arg.indexOf("(") + 1, arg.lastIndexOf(")"));	//eliminate the enclosing parentheses

				if(arg.contains(",")){												//2 arguments: searching for a path
					if(arg.indexOf("%" + StoryElement.ONTOKEY) == 0)
						firstArg = arg.substring(0, getParameterScope(arg, arg.indexOf("(")) + 2);
					else firstArg = arg.substring(0, arg.indexOf(","));
					if(arg.indexOf("%" + StoryElement.ONTOKEY) == arg.indexOf(",", firstArg.length()) + 1)
						secondArg = arg.substring(arg.indexOf(",") + 1, getParameterScope(arg, arg.indexOf("(")) + 2);
					else secondArg = arg.substring(arg.indexOf(",", firstArg.length()) + 1);
				}
				else{																//1 argument: searching for a concept
					firstArg = arg;
					secondArg = "";
				}
				return deriveOntologyAccess(firstArg, secondArg, category, storyTree);	//ontology access
			}
			else if(arg.indexOf(StoryElement.PREVCGKEY) == 0){
//				System.out.println("currentcg: " + storyTree.getCurrentCharacterGoal().getID());
				arg = arg.substring(StoryElement.PREVCGKEY.length());

				return derivePrevParamValues(arg, storyTree);						//get previous character goal parameter value
			}
			else if(arg.indexOf(StoryElement.NEGATE) == 0){							//the item must be negated

				String relationship = arg.substring(0, arg.indexOf("("));
				arg = arg.substring(arg.indexOf("(") + 1, arg.indexOf(")"));

				if(arg.indexOf("%") == 0)
					arg = parseArgument(arg, storyTree);

				return deriveOntologyAccess(arg, "", relationship, storyTree);
			}
			else if(arg.indexOf(StoryElement.CHARGOALKEY) == 0){
				return arg;
			}
			else return parseArgument(arg, storyTree);
		}
		else return null;
	}

	/**
	 * This is the function where a keyword, which needs further value derivation (e.g. parameter enclosed in '%'), is added. This function
	 * serves as the venue for getting the equivalent value of the keyword.
	 *
	 * @param arg - parsed parameter (not enclosed in '%'...'%')
	 * @param storyTree - the current story tree
	 * @return equivalent value derived from the parsed parameter
	 * @throws ParserException
	 */
	private static String deriveParsedValue(String arg, StoryTree storyTree) throws StoryPlannerException{

		try{
			if(arg.indexOf(StoryElement.PARSEDTARGET) == 0){
				return deriveCurrParamValues(charGoal.getTargetDB());
			}
			else if(arg.indexOf(StoryElement.PARSEDINSTRUMENT) == 0){
				return deriveCurrParamValues(charGoal.getInstrumentDB());
			}
			else if(arg.indexOf(StoryElement.PARSEDAGENS) == 0){
				return deriveCurrParamValues(charGoal.getAgensDB());
			}
			else if(arg.indexOf(StoryElement.PARSEDPATIENS) == 0){
				return deriveCurrParamValues(charGoal.getPatiensDB());
			}
			else if(arg.equalsIgnoreCase(StoryElement.PARSEDLESSON)){
				return storyTree.getRoot().getLessonObject().getID();
			}
			else if(arg.equalsIgnoreCase(StoryElement.PARSEDOBJECT)){
				String description = "";
				if(objectReference == 0){
					objectReference++;
					try{
						description = dbHelper.getIntroConcepts(StoryElement.getPBObject(1).get(0).getString(), "propertyOf", "description").get(0);
						return description + " " + StoryElement.getPBObject(1).get(0).getString();					//(assumption: isang object lang) what if madami?
					}catch(Exception e){
						return StoryElement.getPBObject(1).get(0).getString();
					}
				}
				return StoryElement.getPBObject(1).get(0).getString();
			}
			else if(arg.equalsIgnoreCase(StoryElement.PARSEDMAINCHAR)){
				return StoryElement.searchCharacter(StoryElement.MAINCHAR).getID();
			}
			else if(arg.equalsIgnoreCase(StoryElement.PARSEDSECONDARYCHAR)){
				return StoryElement.searchCharacter(StoryElement.SECONDARYCHAR).getID();
			}
			else if(arg.equalsIgnoreCase(StoryElement.PARSEDADULT)){
				return StoryElement.searchCharacter(StoryElement.ADULT).getID();
			}
			else if(arg.equalsIgnoreCase(StoryElement.PARSEDADULTWITHJOB)){
				return StoryElement.searchCharacter(StoryElement.ADULTWITHJOB).getID();
			}
			else if(arg.equalsIgnoreCase(StoryElement.PARSEDACTIVITY)){
				return storyTree.getRoot().getInitActivity();
			}
			else if(arg.equalsIgnoreCase(StoryElement.PARSEDBACKGROUND)){
				return StoryElement.ICR.getBackground().getString();
			}
			else return "";
		}catch(Exception e){
			throw new StoryPlannerException(e);
		}
	}

	/**
	 * * This function accesses the ontology (knowledge) of the system. The ontology access is of two types:
	 * 		1. searching for a path - requires two ontology parameters
	 * 		2. searching for a concept - requires only one ontology parameter
	 *
	 * When searching for a path, this function generates dynamic character goals based on the semantic relationships that connects
	 * paths and concepts leading to the target concept (the second parameter). The concept found before the target concept is the
	 * one returned by this function.
	 *
	 * When searching for a concept, this function returns the target concept itself (via a semantic relationship), and does not
	 * create dynamic character goals.
	 *
	 * @param firstArg - first ontology parameter
	 * @param secondArg - second ontology parameter
	 * @param category - semantic category in which the ontology would start searching
	 * @param storyTree the current story tree
	 * @return the value(s) yielded by the ontology access
	 */
	private static String deriveOntologyAccess(String firstArg, String secondArg, String category, StoryTree storyTree) throws StoryPlannerException{

		String ret = "";

		if(firstArg.contains("%"))
			firstArg = parseArgument(firstArg, storyTree);
		else if (!secondArg.equalsIgnoreCase("") && secondArg.contains("%"))
			secondArg = parseArgument(secondArg, storyTree);

		System.out.println("\ncategory: " + category + "\nfirst: " + firstArg + "\nsecond: " + secondArg);

		if(secondArg.equalsIgnoreCase("")){
			System.out.println("system is accessing the ontology for a concept");
			ret = dbHelper.getConcept(firstArg, category);
		}
		else{
			System.out.println("system is accessing the ontology for a path");
			Vector<Ontology> path = dbHelper.getRelationship(firstArg, secondArg, category);

			//If more than one ontoRelationship, create new character goal(s) to accommodate the path.
			//The current character goal is the recently inserted character goal, prior to the currently executed character goal
			StoryRandomizer sr = null;
			try{
				sr = new StoryRandomizer(storyTree.getCurrentCharacterGoal().getAgensDB(), storyTree.getCurrentCharacterGoal().getPatiensDB());
			}catch(Exception e){}

			for(int i = 0; i < path.size() - 1; i++){
				CharacterGoal addedCg = sr.createCharacterGoal(path.get(i), age);
				storyTree.insertCharacterGoal(addedCg);

				Debugger.printCharacterGoal(addedCg);
			}

			try{
				//The last path's concept is taken as the value of the invoking parameter
				//dbLexicon.connect();
				ret = path.get(path.size()-1).getElement2().getElementID();
				//dbLexicon.disconnect();
			}catch(ArrayIndexOutOfBoundsException ae){
				throw new OntologyException("All paths gathered are disregarded in " + storyTree.getCurrentAuthorGoal().getID() + " " + charGoal.getID());
			}
		}

		if(ret == null)
			throw new OntologyException();
		return ret;
	}

	/**
	 * This is the function that derives the current character goal's parameter (indicated by arg) value.
	 *
	 * @param obj - object value of the parameter
	 * @return equivalent value derived from the parsed parameter
	 */
	private static String deriveCurrParamValues(DBObject obj){

		if(!obj.getID().contains(StoryElement.WORDKEY))
			return obj.getString();
		return obj.getID();
	}

	/**
	 * This is the function that derives the previous character goal's parameter (indicated by arg) value.
	 *
	 * @param arg - parsed parameter (does not include the partial keyword 'goal.')
	 * @param storyTree - the current story tree
	 * @return equivalent value derived from the parsed parameter
	 */
	private static String derivePrevParamValues(String arg, StoryTree storyTree){

		if(arg.indexOf(StoryElement.PARSEDTARGET) == 0){						//get previously inserted character goal's target
			return storyTree.getCurrentCharacterGoal().getTargetDB().getID();
		}
		else if(arg.indexOf(StoryElement.PARSEDINSTRUMENT) == 0){				//get previously inserted character goal's instrument
			return storyTree.getCurrentCharacterGoal().getInstrumentDB().getID();
		}
		else if(arg.indexOf(StoryElement.PARSEDAGENS) == 0){					//get previously inserted character goal's agens
			return storyTree.getCurrentCharacterGoal().getAgensDB().getID();
		}
		else if(arg.indexOf(StoryElement.PARSEDPATIENS) == 0){					//get previously inserted character goal's patiens
			return storyTree.getCurrentCharacterGoal().getPatiensDB().getID();
		}
		else System.out.println("no parameter " + arg + "exists");
		return null;
	}

}
