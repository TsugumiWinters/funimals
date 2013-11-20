package pbmain;

import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import pbmain.util.DBObject;
import pbmain.util.ParserException;
import pictureeditor.component.InputContentRepresentation;
import sentencegenerator.LASGenerator;
import sentencegenerator.ReferringExpressionGenerator;
import sentencegenerator.StoryGeneratorException;
import sentencegenerator.component.Phrase;
import storyplanner.StoryPlannerException;
import storyplanner.component.StoryTree;
import storyplanner.introduction.IntroMaker;
import storyplanner.introduction.IntroMakerException;
import storyplanner.plot.PlotMaker;
import storyplanner.plot.ThemeExtractor;
import storyplanner.title.TitleMaker;
import storyplanner.title.TitleMakerException;

import com.picturebooks.mobilepicturebooks.ApplicationContextProvider;

import database.DatabaseHelper;
import database_entities.Background;
import database_entities.CharacterGoal;
import database_entities.IGCharacter;
import database_entities.IGObject;
import database_entities.IGTheme;
import database_entities.Word;


/*
 * This class serves as the dummy Controller class for the StoryPlanner module
 */
public class Debugger {

	private static DatabaseHelper dbHelper = new DatabaseHelper(ApplicationContextProvider.getContext());
	
	public static InputContentRepresentation createICR(int age, String name, Background selectedBg,
			Vector<IGCharacter> inputChars, Vector<IGCharacter> inputExtras,
			Vector<IGObject> inputObjects){

		InputContentRepresentation ICR = new InputContentRepresentation();
		ICR.setAge(age);
		ICR.setName(name);
		ICR.setBg(selectedBg);
		Vector<IGCharacter> chars = new Vector<IGCharacter>();
		for (int i = 0; i < inputChars.size(); i++) {
			chars.add(inputChars.elementAt(i));
		}
		for (int i = 0; i < inputExtras.size(); i++) {
			chars.add(inputExtras.elementAt(i));
		}
		ICR.setCharacters(chars);
		ICR.setObjects(inputObjects);

		return ICR;
	}

	//added throws ParserException
	public static void main(String[] args) throws ParserException {

		Vector<IGCharacter> inputChars = new Vector<IGCharacter>();
		Vector<IGCharacter> inputExtras = new Vector<IGCharacter>();
		Vector<IGObject> inputObjects = new Vector<IGObject>();
		Background bg = null;
		
		IGCharacter adultChar = new IGCharacter();

		Scanner sc = new Scanner(System.in);

		System.out.print("age [6 - 8]: ");
		int age = sc.nextInt();
		System.out.println("[1]Bathroom\n[2]Bedroom\n[3]Classroom\n[4]Clinic\n[5]Dining room\n[6]Mall\n[7]Market\n[8]Outdoors\n[9]Playground");
		System.out.print("background [1 - 9]: ");

		InputContentRepresentation ICR;

		try{
			switch(sc.nextInt()){
			default:
			case 1://bathroom background
				ICR = bathroomBG(age, bg, inputChars, inputExtras, inputObjects);
				break;
			case 2://bedroom background
				ICR = bedroomBG(age, bg, inputChars, inputExtras, inputObjects);
				break;
			case 3://classroom background
				ICR = classroomBG(age, bg, inputChars, inputExtras, inputObjects);
				break;
			case 4://clinic background
				ICR = clinicBG(age, bg, inputChars, inputExtras, inputObjects);
				break;
			case 5://dining room background
				ICR = diningRoomBG(age, bg, inputChars, inputExtras, inputObjects);
				break;
			case 6://mall background
				ICR = mallBG(age, bg, inputChars, inputExtras, inputObjects);
				break;
			case 7://market background
				ICR = marketBG(age, bg, inputChars, inputExtras, inputObjects);
				break;
			case 8://outdoors background
				ICR = outdoorBG(age, bg, inputChars, inputExtras, inputObjects);
				break;
			case 9://playground background
				ICR = playgroundBG(age, bg, inputChars, inputExtras, inputObjects);
			}

			IGTheme chosenTheme = new ThemeExtractor().determineTheme(ICR);
			System.out.println("Chosen theme: " + chosenTheme.getMoralLesson() + " " + chosenTheme.getID());

			StoryTree storyTree = new StoryTree(chosenTheme);
			PlotMaker pm = new PlotMaker(adultChar);
			pm.createStoryTree(chosenTheme, ICR);
			storyTree = pm.getStoryTree();
			System.out.println("\nplot maker completed successfully.\n");
			new IntroMaker().createIntro(storyTree, ICR.getAge(), ICR.getCharacters().get(0));
			System.out.println("\nintro maker completed successfully.\n");
			new TitleMaker().createTitle(storyTree, ICR.getAge());
			System.out.println("\ntitle maker completed successfully.\n");

//			printStoryTree(storyTree);

			Vector<CharacterGoal> storyLine = storyTree.getAllCharacterGoals();
			System.out.println("List of character goals:");
			for (int i = 0; i < storyLine.size(); i++) {
				System.out.println(((CharacterGoal)storyLine.get(i)).getID());
			}

			System.out.println("\nApplied REG");
			new ReferringExpressionGenerator().applyREG(storyLine, ICR.getAge());
			LASGenerator lg = new LASGenerator();
			lg.generateSentence(storyLine);
			System.out.println("Final Output");
			System.out.println("---------------------------------");
			System.out.println(lg.getStory().elementAt(0) + "\n");
			for (int i = 1; i < lg.getStory().size(); i++) {
				System.out.println(lg.getStory().elementAt(i));

			}


		}catch(PBException ep){
			System.out.println("Picture Editor module Exception");
			ep.printStackTrace();
		}catch(StoryPlannerException ep){
			System.out.println("Story Planner module: Plot Maker Exception");
			ep.printStackTrace();
		}catch(IntroMakerException ep){
			System.out.println("Story Planner module: Intro Maker Exception");
			ep.printStackTrace();
		}catch(TitleMakerException ep){
			System.out.println("Story Planner module: Title Maker Exception");
			ep.printStackTrace();
		}catch(StoryGeneratorException ep){
			System.out.println("Story Generation module Exception");
			ep.printStackTrace();
		}
	}

	//added throws ParserException
	static InputContentRepresentation bathroomBG(int age, Background bg, Vector<IGCharacter> inputChars,
			Vector<IGCharacter> inputExtras, Vector<IGObject> inputObjects) throws PBException, ParserException{

		bg = dbHelper.instantiateBackground("BG0001");

		System.out.println("TWO POSSIBLE THEMES");
		//available objects: OB027;OB019;OB034

//		switch(new Random().nextInt(2)){
//		case 0: //take bath
//			inputChars.add(dbMain.instantiateCharacter("CH001"));
//
//			inputExtras.add(dbMain.instantiateCharacter("CH021"));
//
//			inputObjects.add(dbMain.instantiateObject("OB034"));
//			inputObjects.add(dbMain.instantiateObject("OB019"));

//			break;
//		case 1: //brush teeth
			inputChars.add(dbHelper.instantiateCharacter("CH002"));

			inputExtras.add(dbHelper.instantiateCharacter("CH022"));

			inputObjects.add(dbHelper.instantiateObject("OB027"));
//		}

		return createICR(age, "Candice", bg, inputChars, inputExtras, inputObjects);
	}

	//added throws ParserException
	static InputContentRepresentation bedroomBG(int age, Background bg, Vector<IGCharacter> inputChars,
			Vector<IGCharacter> inputExtras, Vector<IGObject> inputObjects) throws PBException, ParserException{

		bg = dbHelper.instantiateBackground("BG0002");

		System.out.println("SIX POSSIBLE THEMES");
		//available objects: OB013;OB018;OB024;OB028;OB029;OB030;OB031;OB017;OB001;OB025;OB016

//		switch(new Random().nextInt(6)){
//		case 0: //be brave OB017;OB016
//			inputChars.add(dbMain.instantiateCharacter("CH003"));
//
//			inputExtras.add(dbMain.instantiateCharacter("CH023"));
//
//			inputObjects.add(dbMain.instantiateObject("OB017"));
//			inputObjects.add(dbMain.instantiateObject("OB016"));
//
//			break;
////		case 1: //be neat OB005;OB013;OB018;OB024;OB029;OB030;OB031;OB032;OB028
			inputChars.add(dbHelper.instantiateCharacter("CH004"));

			inputExtras.add(dbHelper.instantiateCharacter("CH024"));

			inputObjects.add(dbHelper.instantiateObject("OB018"));
			inputObjects.add(dbHelper.instantiateObject("OB024"));
			inputObjects.add(dbHelper.instantiateObject("OB029"));
			inputObjects.add(dbHelper.instantiateObject("OB030"));
			inputObjects.add(dbHelper.instantiateObject("OB031"));
			inputObjects.add(dbHelper.instantiateObject("OB028"));
			inputObjects.add(dbHelper.instantiateObject("OB013"));

//			break;
//		case 2: //sleep early (want much) OB017;OB001
//			inputChars.add(dbMain.instantiateCharacter("CH005"));
//
//			inputExtras.add(dbMain.instantiateCharacter("CH025"));
//
//			inputObjects.add(dbMain.instantiateObject("OB017"));
//			inputObjects.add(dbMain.instantiateObject("OB001"));

//			break;
//		case 3: //sleep early (defy-not do rule) OB017;OB001
//			inputChars.add(dbMain.instantiateCharacter("CH006"));
//
//			inputExtras.add(dbMain.instantiateCharacter("CH026"));
//
//			inputObjects.add(dbMain.instantiateObject("OB017"));
//			inputObjects.add(dbMain.instantiateObject("OB001"));
//
//			break;
//		case 4: //be careful OB016;OB015
//			inputChars.add(dbMain6n.instantiateObject("OB016"));

//			break;
//		case 5: //be honest OB016;OB015
//			inputChars.add(dbMain.instantiateCharacter("CH016"));
//
//			inputExtras.add(dbMain.instantiateCharacter("CH036"));
//
//			inputObjects.add(dbMain.instantiateObject("OB016"));
//		}

		return createICR(age, "Candice", bg, inputChars, inputExtras, inputObjects);
	}

	//added throws ParserException
	static InputContentRepresentation classroomBG(int age, Background bg, Vector<IGCharacter> inputChars,
			Vector<IGCharacter> inputExtras, Vector<IGObject> inputObjects) throws PBException, ParserException{

		bg = dbHelper.instantiateBackground("BG0003");

		System.out.println("ONE THEME");
		//available objects: OB013;OB018;OB024;OB028;OB029;OB030;OB031;OB006;OB012;OB003

		//be brave OB013;OB018;OB024;OB028;OB029;OB030;OB031;OB006;OB012;OB003
		inputChars.add(dbHelper.instantiateCharacter("CH008"));

		inputExtras.add(dbHelper.instantiateCharacter("CH028"));

		inputObjects.add(dbHelper.instantiateObject("OB013"));
		inputObjects.add(dbHelper.instantiateObject("OB018"));
		inputObjects.add(dbHelper.instantiateObject("OB024"));
		inputObjects.add(dbHelper.instantiateObject("OB028"));
		inputObjects.add(dbHelper.instantiateObject("OB029"));
		inputObjects.add(dbHelper.instantiateObject("OB030"));
		inputObjects.add(dbHelper.instantiateObject("OB031"));
		inputObjects.add(dbHelper.instantiateObject("OB006"));
		inputObjects.add(dbHelper.instantiateObject("OB012"));
		inputObjects.add(dbHelper.instantiateObject("OB003"));

		return createICR(age, "Candice", bg, inputChars, inputExtras, inputObjects);
	}

	//added throws ParserException
	static InputContentRepresentation clinicBG(int age, Background bg, Vector<IGCharacter> inputChars,
			Vector<IGCharacter> inputExtras, Vector<IGObject> inputObjects) throws PBException, ParserException{

		bg = dbHelper.instantiateBackground("BG0004");

		System.out.println("ONE THEME");
		//available objects: OB026;OB022;OB035

		//be brave OB022;OB026;OB035
		inputChars.add(dbHelper.instantiateCharacter("CH009"));

		inputExtras.add(dbHelper.instantiateCharacter("CH029"));

		inputObjects.add(dbHelper.instantiateObject("OB022"));
		inputObjects.add(dbHelper.instantiateObject("OB026"));
		inputObjects.add(dbHelper.instantiateObject("OB035"));

		return createICR(age, "Candice", bg, inputChars, inputExtras, inputObjects);
	}

	//added throws ParserException
	static InputContentRepresentation diningRoomBG(int age, Background bg, Vector<IGCharacter> inputChars,
			Vector<IGCharacter> inputExtras, Vector<IGObject> inputObjects)throws PBException, ParserException{

		bg = dbHelper.instantiateBackground("BG0005");

		System.out.println("FOUR POSSIBLE THEMES");
		//available objects: OB002;OB004;OB007;OB011;OB009;OB010;OB014;OB020;OB015;OB012;OB036;OB037

//		switch(new Random().nextInt(4)){
//		case 0: //eat healthy foods OB002;OB004;OB007;OB009;OB010;OB011;OB014;OB036;OB037
//			inputChars.add(dbMain.instantiateCharacter("CH010"));
//
//			inputExtras.add(dbMain.instantiateCharacter("CH030"));
//
//			inputObjects.add(dbMain.instantiateObject("OB002"));
//			inputObjects.add(dbMain.instantiateObject("OB004"));
//			inputObjects.add(dbMain.instantiateObject("OB007"));
//			inputObjects.add(dbMain.instantiateObject("OB009"));
//			inputObjects.add(dbMain.instantiateObject("OB010"));
//			inputObjects.add(dbMain.instantiateObject("OB011"));
//			inputObjects.add(dbMain.instantiateObject("OB014"));
//			inputObjects.add(dbMain.instantiateObject("OB036"));
//			inputObjects.add(dbMain.instantiateObject("OB037"));
//
//			break;
//		case 1: //eat on time OB002;OB004;OB007;OB011;OB014;OB036;OB037
			inputChars.add(dbHelper.instantiateCharacter("CH011"));

			inputExtras.add(dbHelper.instantiateCharacter("CH031"));

			inputObjects.add(dbHelper.instantiateObject("OB002"));
			inputObjects.add(dbHelper.instantiateObject("OB004"));
			inputObjects.add(dbHelper.instantiateObject("OB007"));
			inputObjects.add(dbHelper.instantiateObject("OB011"));
			inputObjects.add(dbHelper.instantiateObject("OB014"));
			inputObjects.add(dbHelper.instantiateObject("OB036"));
			inputObjects.add(dbHelper.instantiateObject("OB037"));
//
//			break;
//		case 2: //be honest OB016;OB015
//			inputChars.add(dbMain.instantiateCharacter("CH017"));
//
//			inputExtras.add(dbMain.instantiateCharacter("CH037"));
//
//			inputObjects.add(dbMain.instantiateObject("OB015"));

//			break;
//		case 3: //be careful OB016;OB015
//			inputChars.add(dbMain.instantiateCharacter("CH018"));
//
//			inputExtras.add(dbMain.instantiateCharacter("CH028"));
//
//			inputObjects.add(dbMain.instantiateObject("OB015"));
//		}

		return createICR(age, "Candice", bg, inputChars, inputExtras, inputObjects);
	}

	//added throws ParserException
	static InputContentRepresentation mallBG(int age, Background bg, Vector<IGCharacter> inputChars,
			Vector<IGCharacter> inputExtras, Vector<IGObject> inputObjects) throws PBException, ParserException{

		bg = dbHelper.instantiateBackground("BG0006");

		System.out.println("ONE THEME");
		//available objects: OB013;OB018;OB024;OB028;OB029;OB030;OB031;OB033

		//ask permission OB005;OB013;OB018;OB024;OB029;OB030;OB031;OB032;OB028
		inputChars.add(dbHelper.instantiateCharacter("CH012"));

		inputExtras.add(dbHelper.instantiateCharacter("CH032"));

		inputObjects.add(dbHelper.instantiateObject("OB013"));
		inputObjects.add(dbHelper.instantiateObject("OB018"));
		inputObjects.add(dbHelper.instantiateObject("OB024"));
		inputObjects.add(dbHelper.instantiateObject("OB029"));
		inputObjects.add(dbHelper.instantiateObject("OB028"));
		inputObjects.add(dbHelper.instantiateObject("OB030"));
		inputObjects.add(dbHelper.instantiateObject("OB031"));

		return createICR(age, "Candice", bg, inputChars, inputExtras, inputObjects);
	}

	//added throws ParserException
	static InputContentRepresentation marketBG(int age, Background bg, Vector<IGCharacter> inputChars,
			Vector<IGCharacter> inputExtras, Vector<IGObject> inputObjects) throws PBException, ParserException{

		bg = dbHelper.instantiateBackground("BG0007");

		System.out.println("ONE THEME");
		//available objects: OB002;OB004;OB007;OB011;OB014;OB009;OB033;OB037

		//ask permission OB005;OB013;OB018;OB024;OB029;OB030;OB031;OB032;OB028
		inputChars.add(dbHelper.instantiateCharacter("CH012"));

		inputExtras.add(dbHelper.instantiateCharacter("CH032"));

		inputObjects.add(dbHelper.instantiateObject("OB010"));
		inputObjects.add(dbHelper.instantiateObject("OB009"));

		return createICR(age, "Candice", bg, inputChars, inputExtras, inputObjects);
	}

	//added throws ParserException
	static InputContentRepresentation outdoorBG(int age, Background bg, Vector<IGCharacter> inputChars,
			Vector<IGCharacter> inputExtras, Vector<IGObject> inputObjects) throws PBException, ParserException{

		bg = dbHelper.instantiateBackground("BG0008");

		System.out.println("ONE THEME");
		//available objects: OB005;OB013;OB018;OB024;OB028;OB029;OB030;OB031

		//learn to share OB005;OB013;OB018;OB024;OB029;OB030;OB031;OB032;OB028
		inputChars.add(dbHelper.instantiateCharacter("CH013"));

		inputExtras.add(dbHelper.instantiateCharacter("CH033"));

		inputObjects.add(dbHelper.instantiateObject("OB005"));
		inputObjects.add(dbHelper.instantiateObject("OB013"));
		inputObjects.add(dbHelper.instantiateObject("OB018"));
		inputObjects.add(dbHelper.instantiateObject("OB024"));
		inputObjects.add(dbHelper.instantiateObject("OB028"));
		inputObjects.add(dbHelper.instantiateObject("OB029"));
		inputObjects.add(dbHelper.instantiateObject("OB030"));
		inputObjects.add(dbHelper.instantiateObject("OB031"));

		return createICR(age, "Candice", bg, inputChars, inputExtras, inputObjects);
	}

	//added throws ParserException
	static InputContentRepresentation playgroundBG(int age, Background bg, Vector<IGCharacter> inputChars,
			Vector<IGCharacter> inputExtras, Vector<IGObject> inputObjects) throws PBException, ParserException{

		bg = dbHelper.instantiateBackground("BG0009");

		System.out.println("TWO POSSIBLE THEMES");
		//available objects: OB013;OB018;OB024;OB028;OB029;OB030;OB031;OB032;OB023

		switch(new Random().nextInt(2)){
		case 0: //learn to share OB005;OB013;OB018;OB024;OB029;OB030;OB031;OB032;OB028
			inputChars.add(dbHelper.instantiateCharacter("CH014"));

			inputExtras.add(dbHelper.instantiateCharacter("CH034"));

			inputObjects.add(dbHelper.instantiateObject("OB013"));
			inputObjects.add(dbHelper.instantiateObject("OB018"));
			inputObjects.add(dbHelper.instantiateObject("OB024"));
			inputObjects.add(dbHelper.instantiateObject("OB028"));
			inputObjects.add(dbHelper.instantiateObject("OB029"));
			inputObjects.add(dbHelper.instantiateObject("OB030"));
			inputObjects.add(dbHelper.instantiateObject("OB031"));
			inputObjects.add(dbHelper.instantiateObject("OB032"));

			break;
		case 1: //learn to share OB021;OB023;OB032
			inputChars.add(dbHelper.instantiateCharacter("CH015"));

			inputExtras.add(dbHelper.instantiateCharacter("CH035"));

			inputObjects.add(dbHelper.instantiateObject("OB023"));
			inputObjects.add(dbHelper.instantiateObject("OB032"));
		}

		return createICR(age, "Candice", bg, inputChars, inputExtras, inputObjects);
	}

	public static void printStoryTree(StoryTree tree){
		//function "traverse" in StoryTree.java to see the hierarchy of nodes
		System.out.println("\nStory Tree breadth first traversal");
		Vector<DBObject> sequence = tree.traverseStoryTree();
		for(int i=0; i<sequence.size(); i++){
			System.out.println(sequence.get(i).getID());
		}
	}

	//for debugging purpose
	public static void printCharacterGoal(CharacterGoal cg){

		System.out.println("\nCHARACTER GOAL ID: " + cg.getID());
		System.out.print("Action:");
		if (cg.getActionDB() instanceof Phrase){
			Phrase action = (Phrase)cg.getActionDB();
			for(int i=0; i<action.size(); i++){
				System.out.print(" " + action.get(i).getString());
			}
		}
		else System.out.print(" " + ((Word)cg.getActionDB()).getString());
		System.out.println("\nAgens: " + cg.getAgensDB().getString());
		if(cg.getPatiensDB()!= null)
			System.out.println("Patiens: " + cg.getPatiensDB().getString());
		if(cg.getTargetDB()!= null)
			System.out.println("Target: " + cg.getTargetDB().getString());
		/*added*/
		if(cg.getString() != null)
			System.out.println("Goal: " + cg.getString());
		if(cg.getTarget()!= null)
			System.out.println("TargetString: " + cg.getTarget());
		/*end of added*/
		if(cg.getInstrumentDB()!= null)
			System.out.println("Instrument: " + cg.getInstrumentDB().getString());
		System.out.println("isNegated: " + cg.isNegated());
		System.out.println("hideTarget: " + cg.isHideTarget());
	}

	public static void printStringValues(CharacterGoal cg){

		System.out.println("\nCHARACTER GOAL ID: " + cg.getID());
		if(cg.getAgensVector()!=null)
		{
			System.out.print("Agens:");
			for(int i=0; i<cg.getAgensVector().size(); i++){
				System.out.print(" " + cg.getAgensVector().get(i));
			}
			System.out.println();
		}
		if(cg.getActionVector()!=null)
		{
			System.out.print("Action:");
			for(int i=0; i<cg.getActionVector().size(); i++){
				System.out.print(" " + cg.getActionVector().get(i));
			}
			System.out.println();
		}
		if(cg.getPatiensVector()!=null)
		{
			System.out.print("Patiens:");
			for(int i=0; i<cg.getPatiensVector().size(); i++){
				System.out.print(" " + cg.getPatiensVector().get(i));
			}
			System.out.println();
		}
		if(cg.getInstrumentVector()!=null)
		{
			System.out.print("Instrument:");
			if(cg.getInstrumentDB().getClass().getName().equalsIgnoreCase("DBObjects.CharacterGoal"))
				printStringValues((CharacterGoal)cg.getInstrumentDB());
			else
			{
				for(int i=0; i<cg.getInstrumentVector().size(); i++){
					System.out.print(" " + cg.getInstrumentVector().get(i));
				}
				System.out.println();
			}
		}
		if(cg.getTargetVector()!=null)
		{
			System.out.print("Target:");
			if(cg.getTarget().getClass().getName().equalsIgnoreCase("DBObjects.CharacterGoal"))
				printStringValues((CharacterGoal)cg.getTargetDB());
			else
			{
				for(int i=0; i<cg.getTargetVector().size(); i++)
					System.out.print(" " + cg.getTargetVector().get(i));
				System.out.println();
			}
		}
	}
}
