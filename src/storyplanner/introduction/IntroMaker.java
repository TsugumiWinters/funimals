package storyplanner.introduction;

import java.util.Random;
import java.util.Vector;

import pbmain.Debugger;
import pbmain.PBException;
import pbmain.util.Parser;
import pbmain.util.ParserException;
import sentencegenerator.component.DBLexicalObject;
import sentencegenerator.component.Phrase;
import storyplanner.StoryElement;
import storyplanner.component.StoryTree;
import storyplanner.ontology.OntologyException;

import com.picturebooks.funimals.ApplicationContextProvider;

import database.DatabaseHelper;
import database_entities.CharacterGoal;
import database_entities.IGCharacter;

/**
 * This class creates an appropriate instroduction for the story.
 * 
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
public class IntroMaker {

	static final String placeCG = "CGOLPLCE";
	static final String timeCG = "CGOLTIME";

	private StoryTree storyTree;
	private int age;

	private DatabaseHelper dbHelper = new DatabaseHelper(ApplicationContextProvider.getContext());
	private Random rand = new Random();

	/**
	 * Creates an introduction for the story
	 * 
	 * @param storyTree
	 *            {@link storyplanner.component.StoryTree} which contains other
	 *            elements of the story
	 * @param age
	 *            age of the user
	 * @param mainChar
	 *            main character of the story
	 * @throws IntroMakerException
	 *             raised if an error occurred while creating the introduction
	 *             of the story
	 */
	public void createIntro(StoryTree storyTree, int age, IGCharacter mainChar)
			throws IntroMakerException {

		this.storyTree = storyTree;
		this.age = age;

		selectPlace(mainChar);
		selectTime();
	}

	private void selectPlace(IGCharacter mainChar) throws IntroMakerException {

		String action = storyTree.getFirstCharGoal().getTargetDB().getID();
		Vector<String> candidatePlaces = null;
		System.out.println("init activity: " + action);
		
		do {
			try {
				candidatePlaces = dbHelper.getIntroConcepts(action,
						StoryElement.PLACE_REL, StoryElement.PLACE_CAT);
			} catch (OntologyException e) {/* do nothing */
			}
			if (candidatePlaces == null)
				action = storyTree.getRoot().getInitActivity();

		} while (candidatePlaces == null);

		String placeId = filterCandidatePlaces(candidatePlaces);

		DBLexicalObject vPlace = null;
		try {
			vPlace = Parser.parseWords(placeId, this.age);
		} catch (ParserException e) {
			throw new IntroMakerException(e);
		}

		Phrase actionWord = new Phrase();
		addLinkingVerb(actionWord);
		addPreposition(actionWord, placeId);
		
		CharacterGoal place = new CharacterGoal(placeCG, actionWord, mainChar,
				null, vPlace, null, "place");

		storyTree.insertIntroGoals(place);

		Debugger.printCharacterGoal(place); // for debugging purpose
	}

	private void selectTime() throws IntroMakerException {

		Vector<String> candidateTimes = Parser.parseString(storyTree.getRoot()
				.getInitTime(), ";");

		String timeId = candidateTimes.get(rand.nextInt(candidateTimes.size()));

		DBLexicalObject vTime = null;
		try {
			vTime = Parser.parseWords(timeId, this.age);
		} catch (ParserException e) {
			throw new IntroMakerException(e);
		}

		Phrase actionWord = new Phrase();
		addLinkingVerb(actionWord);

		DBLexicalObject descriptionWord = addDescription(timeId);

		CharacterGoal time = new CharacterGoal(timeCG, actionWord, vTime, null,
				descriptionWord, null, "time");

		storyTree.insertIntroGoals(time);

		Debugger.printCharacterGoal(time); // for debugging purpose
	}

	// to be used by selectPlace() function
	// theme access based kung indoor or outdoor
	private String filterCandidatePlaces(Vector<String> candidatePlaces)
			throws IntroMakerException {

		Vector<String> filteredPlaces = new Vector<String>();

		try {

			Vector<String> categories = Parser.parseString(storyTree.getRoot()
					.getInitSettings(), ";");

			for (int i = 0; i < candidatePlaces.size(); i++) {
				Vector<String> introConcepts = dbHelper.getIntroConcepts(
						candidatePlaces.get(i), StoryElement.LOC_REL,
						StoryElement.LOC_CAT);
				for (int j = 0; j < introConcepts.size(); j++) {
					for (int k = 0; k < categories.size(); k++) {
						if (categories.get(k).equalsIgnoreCase(
								introConcepts.get(j))) {
							filteredPlaces.add(candidatePlaces.get(i));
						}
					}
				}
			}
		} catch (Exception e) {
			throw new IntroMakerException(e);
		}

		try {
			return filteredPlaces.get(rand.nextInt(filteredPlaces.size()));
		} catch (Exception e) {
			return candidatePlaces.get(0);
		}
	}

	// to be used by selectPlace() function
	private void addPreposition(Phrase actionWord, String placeId)
			throws IntroMakerException {

		try {
			// .get(0) because this instance always returns a single string
			String category = dbHelper.getIntroConcepts(placeId,
					StoryElement.LOC_REL, StoryElement.PLACE_CAT).get(0);

			if (category.equalsIgnoreCase(dbHelper.getWordId("specific", age)))
				actionWord.add(dbHelper.instantiateWord(
						dbHelper.getWordId("in", age), age));
			else
				actionWord.add(dbHelper.instantiateWord(
						dbHelper.getWordId("at", age), age));
		} catch (PBException e) {
			throw new IntroMakerException(e);
		} catch (OntologyException e) {
			throw new IntroMakerException(e);
		}
	}

	// to be used by selectTime() function
	private DBLexicalObject addDescription(String timeId)
			throws IntroMakerException {
		DBLexicalObject ret = null;
		try {
			Vector<String> description = dbHelper.getIntroConcepts(timeId,
					StoryElement.TIME_REL, StoryElement.TIME_CAT);
			System.out.println("Possible Descriptions " + description.size());
			ret = Parser.parseWords(
					description.get(rand.nextInt(description.size())), age);
		} catch (ParserException e) {
			throw new IntroMakerException(e);
		} catch (OntologyException e) {
			throw new IntroMakerException(e);
		}
		return ret;
	}

	// to be used by selectPlace() and selectTime() functions
	private void addLinkingVerb(Phrase word) throws IntroMakerException {
		try {
			word.add(dbHelper.instantiateWord(dbHelper.getWordId("be", age),
					age));
		} catch (PBException e) {
			throw new IntroMakerException(e);
		}
	}

}
