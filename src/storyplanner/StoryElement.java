package storyplanner;

import java.util.Vector;

import pbmain.PBException;
import pictureeditor.component.InputContentRepresentation;
import android.content.Context;

import com.picturebooks.mobilepicturebooks.ApplicationContextProvider;
import com.picturebooks.mobilepicturebooks.PictureEditor;

import database.DatabaseHelper;
import database_entities.IGCharacter;
import database_entities.IGObject;


public class StoryElement {

	public static Context ourContext = ApplicationContextProvider.getContext();
	
	//keywords
	/**
	 * Keyword for the main character of the story
	 */
	public static final String MAINCHAR = "%child%";
	/**
	 * Keyword for the secondary character of the story
	 */
	public static final String SECONDARYCHAR= "%child2%";
	/**
	 * Keyword for the adult of the story
	 */
	public static final String ADULT = "%adult%";
	/**
	 * Keyword for the adult with specialization
	 */
	public static final String ADULTWITHJOB = "%job%";
	/**
	 * Keyword for the lesson of the story
	 */
	public static final String LESSON = "%lesson%";
	/**
	 * Keyword for the object in the story
	 */
	public static final String OBJECT = "%object%";
	/**
	 * Keyword for the activity in the story
	 */
	public static final String ACTIVITY = "%activity%";
	/**
	 * Keyword for the background/setting of the story
	 */
	public static final String BACKGROUND = "%background%";

	/**
	 * Keyword for the parsed main character used by the system
	 */
	public static final String PARSEDMAINCHAR = "child";
	/**
	 * Keyword for the parsed secondary character used by the system
	 */
	public static final String PARSEDSECONDARYCHAR= "child2";
	/**
	 * Keyword for the parsed adult used by the system
	 */
	public static final String PARSEDADULT = "adult";
	/**
	 * Keyword for the parsed adult with job used by the system
	 */
	public static final String PARSEDADULTWITHJOB = "job";
	/**
	 * Keyword for the parsed lesson used by the system
	 */
	public static final String PARSEDLESSON = "lesson";
	/**
	 * Keyword for the parsed object used by the system
	 */
	public static final String PARSEDOBJECT = "object";
	/**
	 * Keyword for the parsed activity used by the system
	 */
	public static final String PARSEDACTIVITY = "activity";
	/**
	 * Keyword for the parsed background used by the system
	 */
	public static final String PARSEDBACKGROUND = "background";

	//character goal parameters
	/**
	 * Keyword target used as a paramater of a character goal
	 */
	public static final String TARGET = "%target%";
	/**
	 * Keyword instrument used as a paramater of a character goal
	 */
	public static final String INSTRUMENT = "%instrument%";
	/**
	 * Keyword agens used as a paramater of a character goal
	 */
	public static final String AGENS = "%agens%";
	/**
	 * Keyword patiens used as a paramater of a character goal
	 */
	public static final String PATIENS = "%patiens%";

	/**
	 * Keyword parsed target used as a paramater of a character goal
	 */
	public static final String PARSEDTARGET = "target";
	/**
	 * Keyword parsed instrument used as a paramater of a character goal
	 */
	public static final String PARSEDINSTRUMENT = "instrument";
	/**
	 * Keyword parsed agens used as a paramater of a character goal
	 */
	public static final String PARSEDAGENS = "agens";
	/**
	 * Keyword parsed patiens used as a paramater of a character goal
	 */
	public static final String PARSEDPATIENS = "patiens";

	/**
	 * Keyword agens used as a paramater
	 */
	public static final String PARAMAGENS= "Agens:";
	/**
	 * Keyword patiens used as a paramater
	 */
	public static final String PARAMPATIENS= "Patiens:";
	/**
	 * Keyword target used as a paramater
	 */
	public static final String PARAMTARGET= "Target:";
	/**
	 * Keyword instrument used as a paramater
	 */
	public static final String PARAMINSTRUMENT= "Instrument:";

	/**
	 * Array of keywords used as paramaters
	 */
	public static final String[] PARAMETERS = {PARAMAGENS, PARAMPATIENS, PARAMTARGET, PARAMINSTRUMENT};

	//partial keywords
	/**
	 * Keyword negate used by the system
	 */
	public static final String NEGATE = "negate";

	/**
	 * Keyword for the previous character goal used by the system
	 */
	public static final String PREVCGKEY = "goal.";
	/**
	 * Keyword for the character goal used by the system
	 */
	public static final String CHARGOALKEY= "CGOL";
	/**
	 * Keyword for the word objects
	 */
	public static final String WORDKEY = "WORD";
	/**
	 * Keyword for the objects
	 */
	public static final String OBJKEY = "OB";
	/**
	 * Keyword for the characters
	 */
	public static final String CHARKEY = "CH";
	/**
	 * Keyword for the ontology access
	 */
	public static final String ONTOKEY = "onto";

	//for StoryRandomizer.java
	/**
	 * Keyword for the first concept used in the {@link storyplanner.plot.StoryRandomizer}
	 */
	public static final String CONCEPT1 = "C1";
	/**
	 * Keyword for the second concept used in the {@link storyplanner.plot.StoryRandomizer}
	 */
	public static final String CONCEPT2 = "C2";
	/**
	 * Keyword for the location used in the {@link storyplanner.plot.StoryRandomizer}
	 */
	public static final String LOCPREP = "locPrep";
	/**
	 * Keyword for the previous agens used in the {@link storyplanner.plot.StoryRandomizer}
	 */
	public static final String PREVAGENS = "PrevAgens";
	/**
	 * Keyword for the previous patiens used in the {@link storyplanner.plot.StoryRandomizer}
	 */
	public static final String PREVPATIENS = "PrevPatiens";

	//for IntroMaker.java
	/**
	 * Keyword for the place relationship used in the {@link storyplanner.introduction.IntroMaker}
	 */
	public static final String PLACE_REL = "locationOf";
	/**
	 * Keyword for the place category used in the {@link storyplanner.introduction.IntroMaker}
	 */
	public static final String PLACE_CAT = "spatial";
	/**
	 * Keyword for the location relationship used in the {@link storyplanner.introduction.IntroMaker}
	 */
	public static final String LOC_REL = "propertyOf";
	/**
	 * Keyword for the location category used in the {@link storyplanner.introduction.IntroMaker}
	 */
	public static final String LOC_CAT = "things";
	/**
	 * Keyword for the time relationship used in the {@link storyplanner.introduction.IntroMaker}
	 */
	public static final String TIME_REL = "conceptuallyRelatedTo";
	/**
	 * Keyword for the time category used in the {@link storyplanner.introduction.IntroMaker}
	 */
	public static final String TIME_CAT = "time";

	/**
	 * Input Content Representation
	 */
	public static InputContentRepresentation ICR;
	private static String childWordID;

	/**
	 * Sets the ICR and initialize some data needed
	 * @param icr icr passed
	 * @throws StoryPlannerException raised if an error occurred while initializing the data needed
	 */
	public static void setICR(InputContentRepresentation icr) throws StoryPlannerException{
		ICR = icr;
		initialize();
	}

	private static void initialize() throws StoryPlannerException{
		try{
			childWordID = new DatabaseHelper(ourContext).getWordId("child", ICR.getAge());
		}catch(PBException e){
			throw new StoryPlannerException(e);
		}
	}

	/**
	 * Searches for a character with the specified role
	 * @param role role of the character to be searched
	 * @return {@link pictureeditor.component.db.Character} searched
	 * @throws StoryPlannerException raised if an error occurred while searching for a character
	 */
	public static IGCharacter searchCharacter(String role) throws StoryPlannerException{

		System.out.println("Role: " + role);
		
		IGCharacter currentChar = null;
		boolean isFound = false;

		if(role.equalsIgnoreCase(MAINCHAR)){
			//searches for the first character with child role
			for(int i=0; i<ICR.getCharacters().size() && !isFound; i++){
				currentChar = ICR.getCharacters().get(i);
				System.out.println("cur role: " + currentChar.getRole());
				if(currentChar.getRole().equalsIgnoreCase(childWordID))
					isFound = true;
				System.out.println("isFound: " + isFound);
			}
		}
		else if(role.equalsIgnoreCase(SECONDARYCHAR)){
			//searches for the next character with child role
			boolean isNext = false;
			for(int i=0; i<ICR.getCharacters().size() && !isFound; i++){
				currentChar = ICR.getCharacters().get(i);
				if(currentChar.getRole().equalsIgnoreCase(childWordID) && isNext)
					isFound = true;
				else if(currentChar.getRole().equalsIgnoreCase(childWordID) && !isNext)
					isNext = true;
			}
			if(!isFound){
				currentChar = searchCharacterBeyondInput(role);
			}
		}
		else currentChar = searchCharacter(role, ICR.getBackground().getRequiredRole());

		System.out.println("CurrectChar: " + currentChar.getString());
		
		return currentChar;
	}

	/**
	 * Searches for a character with the specified role and job
	 * @param role role of the character to be searched
	 * @param job job of the character to be searched
	 * @return {@link pictureeditor.component.db.Character} searched
	 * @throws StoryPlannerException raised if an error occurred while searching for a character
	 */
	public static IGCharacter searchCharacter(String role, String job) throws StoryPlannerException{

		IGCharacter currentChar = null;
		boolean isFound = false;

		if(role.equalsIgnoreCase(ADULT)){
			//searches for the first character with a role other than child role
			for(int i=0; i<ICR.getCharacters().size() && !isFound; i++){
				currentChar = (IGCharacter)ICR.getCharacters().get(i);
				if(!currentChar.getRole().equalsIgnoreCase(childWordID) && !currentChar.getRole().equalsIgnoreCase(job))
					isFound = true;
			}
			if(!isFound){
				currentChar = searchCharacterBeyondInput(role); //ilagay ung child character to know that must search for the character's parent
			}
		}
		else if(role.equalsIgnoreCase(ADULTWITHJOB)){
			//searches for the first character with a job role
			for(int i=0; i<ICR.getCharacters().size() && !isFound; i++){
				currentChar = (IGCharacter)ICR.getCharacters().get(i);
				if(currentChar.getRole().equalsIgnoreCase(job))
					isFound = true;
			}
			if(!isFound){
				currentChar = searchCharacterBeyondInput(role);	//ilagay ung required role
			}
		}

		return currentChar;
	}

	private static IGCharacter searchCharacterBeyondInput(String role) throws StoryPlannerException{

		System.out.println("No needed " + role + " character exists in the picture.");

		DatabaseHelper dbMain = new DatabaseHelper(ourContext);
		IGCharacter searchedChar = null;

		try{
			if(role.equalsIgnoreCase(ADULT)){
				//adult character must be a parent
				IGCharacter mainChar = searchCharacter(MAINCHAR);
				if(!dbMain.instantiateCharacter(mainChar.getMotherCharacterID()).getRole().equalsIgnoreCase(ICR.getBackground().getRequiredRole()))
					//searchedChar = dbMain.instantiateCharacter(mainChar.getMotherCharacterID());
					searchedChar = PictureEditor.adultChar;
				else //searchedChar = dbMain.instantiateCharacter(mainChar.getFatherCharacterID());
				searchedChar = PictureEditor.adultChar;
			}
			else if(role.equalsIgnoreCase(ADULTWITHJOB)){
				//adult character has a role
				searchedChar = dbMain.instantiateCharacterViaRole(ICR.getBackground().getRequiredRole());
				if(searchedChar == null)
					searchedChar = searchCharacter(StoryElement.ADULT);
			}
			else if(role.equalsIgnoreCase(SECONDARYCHAR)){
				//randomize selection of child character other than the main character
				//PictureEditorDatabase ped = new PictureEditorDatabase();
				Vector<IGCharacter> candidate = null;
				candidate = dbMain.getCharacters(searchCharacter(MAINCHAR).getID());
				int selected = candidate.size();
				selected *= Math.random();
				searchedChar = candidate.get(selected);
			}
		} catch (PBException e) {
			throw new StoryPlannerException(e);
		}

		Vector<IGCharacter> inputChars = ICR.getCharacters();
		inputChars.add(searchedChar);
		ICR.setCharacters(inputChars);

		System.out.println("added character: " + searchedChar.getString());

		return searchedChar;
	}

	/**
	 * Gets the objects to be included in the story
	 * @param cardinality the number of the objects needed in the story
	 * @return set of {@link pictureeditor.component.db.PBObject}
	 */
	public static Vector<IGObject> getPBObject(int cardinality){

		if(cardinality == 1){
			Vector<IGObject> obj = new Vector<IGObject>();
			obj.add(ICR.getObjects().firstElement());
			return obj;
		}
		return ICR.getObjects();
	}
}
