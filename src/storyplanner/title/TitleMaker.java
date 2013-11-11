package storyplanner.title;

import pbmain.Debugger;
import pbmain.util.Parser;
import pbmain.util.ParserException;
import storyplanner.StoryElement;
import storyplanner.StoryPlannerException;
import storyplanner.component.StoryTree;

import com.picturebooks.mobilepicturebooks.ApplicationContextProvider;

import database.DatabaseHelper;
import database_entities.CharacterGoal;

/**
 * This class creates an appropriate title for the story.
 * 
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */

public class TitleMaker {

	public static final String titleCG = "CGOLTITL";
	private DatabaseHelper dbHelper = new DatabaseHelper(ApplicationContextProvider.getContext());

	/**
	 * Creates title for the story
	 * 
	 * @param storyTree
	 *            {@link storyplanner.component.StoryTree} which contains other
	 *            elements of the story
	 * @param age
	 *            age of the user
	 * @throws TitleMakerException
	 *             raised if an error occurred while creating the title of the
	 *             story
	 */
	public void createTitle(StoryTree storyTree, int age) throws TitleMakerException {

		try {
			CharacterGoal title = dbHelper.instantiateCharacterGoal(titleCG);
			title.setAgensDB(StoryElement.searchCharacter(title.getAgens()));
			title.setTargetDB(storyTree.getRoot().getLessonObject());
			title.setActionDB(Parser.parseWords(title.getAction(), age));
			storyTree.insertIntroGoals(title);

			Debugger.printCharacterGoal(title); // for debug purpose

		} catch (ParserException e) {
			throw new TitleMakerException(e);
		} catch (StoryPlannerException e) {
			throw new TitleMakerException(e);
		}
	}
}
