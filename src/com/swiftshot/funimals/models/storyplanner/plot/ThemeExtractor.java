package com.swiftshot.funimals.models.storyplanner.plot;

import java.util.Random;
import java.util.Vector;


import com.swiftshot.funimals.models.ApplicationContextProvider;
import com.swiftshot.funimals.models.database.DatabaseHelper;
import com.swiftshot.funimals.models.database.entities.IGObject;
import com.swiftshot.funimals.models.database.entities.IGTheme;
import com.swiftshot.funimals.models.pbmain.PBException;
import com.swiftshot.funimals.models.pbmain.util.Parser;
import com.swiftshot.funimals.models.pbmain.util.ParserException;
import com.swiftshot.funimals.models.pictureeditor.component.InputContentRepresentation;
import com.swiftshot.funimals.models.storyplanner.StoryPlannerException;


/**
 * This class extracts the appropriate theme for the story given the input picture created by the user.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */

public class ThemeExtractor {

	private DatabaseHelper dbHelper = new DatabaseHelper(ApplicationContextProvider.getContext());
	private InputContentRepresentation ICR;

	/**
	 * Selects a theme based on the input information given by the InputContentRepresentation.
	 *
	 * @param icr - the InputContentRepresentation object that encapsulates the input data
	 * @return the selected theme
	 * @throws ParserException 
	 */
	//added throws ParserException
    public IGTheme determineTheme(InputContentRepresentation icr) throws StoryPlannerException {

    	this.ICR = icr;
    	Vector<IGTheme> possibleThemes = dbHelper.getRelatedThemes(ICR.getBackground().getBackgroundID());

    	//for debugging purpose
    	System.out.println("possible themes");
    	for (int i = 0; i < possibleThemes.size(); i++) {
			System.out.println(possibleThemes.elementAt(i).getMoralLesson());
		}

    	IGTheme selectedTheme = selectTheme(possibleThemes, ICR.getObjects());
    	try {
    		System.out.println("STRING IS:" + selectedTheme.getString());
			selectedTheme.setLessonObject(Parser.parseWords(selectedTheme.getString(), ICR.getAge()));
		} catch (ParserException e) {
			throw new StoryPlannerException(e);
		}

    	return selectedTheme;
    }

    /**
     * Selects the most fitting theme based on the list of themes (possibleThemes) derived from the chosen background, and the input
     * objects (backgroundObjects). This function randomly chooses between the viable themes whenever there are tie-ups.
     *
     * @param possibleThemes - set of potential themes based on the chosen background
     * @param backgroundObjects - set of input objects
     * @return selected theme
     * @throws ParserException 
     */
    //added throws ParserException
    private IGTheme selectTheme(Vector<IGTheme> possibleThemes, Vector<IGObject> backgroundObjects) throws StoryPlannerException {

    	Vector<IGTheme> filteredThemes = new Vector<IGTheme>();
    	int highestIndex = filterThemes(filteredThemes, possibleThemes, backgroundObjects);

    	if(!filteredThemes.isEmpty())
    	{
    		int a = new Random().nextInt(filteredThemes.size());
    		System.out.println(a + " random number " + filteredThemes.size());
    		return filteredThemes.get(a);
    	}
    	else
    		return filteredThemes.get(highestIndex);
    }

    /**
     * Returns a value that statistically represents the number of input objects (inputObjects)that matches the objects required
     * by the given theme (themeObjects). The formula is given by #of matches / inputObjects.size()
     *
     * @param themeObjects - objects related to the given theme
     * @param inputObjects - objects inputted in the picture.
     * @return float value given by the # of matches / inputObjects.size()
     */
    private float computeMatch(Vector<IGObject> themeObjects, Vector<IGObject> inputObjects){

    	float match = 0;

    	for(int i=0; i<themeObjects.size(); i++){
    		for(int j=0; j<inputObjects.size(); j++){
    			if(inputObjects.get(j).getID().equalsIgnoreCase(themeObjects.get(i).getID()))
    				match++;
    		}
    	}

    	//for debugging purpose
    	System.out.println("# of obj match: " + match + " input obj size: " + inputObjects.size() + " computed match: " + match/inputObjects.size());

    	return match/inputObjects.size();				//changed from themeObjects to inputObjects
    }

    /**
     * Filters the potential list of themes based on the computed match of the input objects against the objects related to each possible
     * themes.
     *
     * @param filteredThemes - container for the filtered themes to be filled up by the function (initially empty)
     * @param possibleThemes - set of potential themes based on the chosen background
     * @param backgroundObjects - set of input objects
     * @return the index of the theme (in the filteredThemes vector) with the highest computed match value
     * @throws ParserException 
     */
    //added throws ParserException
    private int filterThemes(Vector<IGTheme> filteredThemes, Vector<IGTheme> possibleThemes, Vector<IGObject> backgroundObjects) throws StoryPlannerException {

    	Vector<IGObject> possibleThemeObjects;

    	float highestStat = 1;
    	int highestIndex = 0;

    	for (int i = 0; i < possibleThemes.size(); i++) {

    		try {
				possibleThemeObjects = dbHelper.getObjects(possibleThemes.get(i).getID(), "tableThemes", "RelatedObjects");
			} catch (PBException e) {
				throw new StoryPlannerException(e);
			}

    		//for debugging purpose
    		System.out.println("THEME: "  + possibleThemes.get(i).getMoralLesson());

    		float match = computeMatch(possibleThemeObjects, backgroundObjects);

    		if(i==0)
    		{
    			highestStat = match;
    			filteredThemes.add(possibleThemes.get(i));
    		}
    		else if(highestStat == match)
    		{
    			filteredThemes.add(possibleThemes.get(i));
    		}
    		else if(highestStat < match)
    		{
    			filteredThemes.removeAllElements();
    			highestStat = match;
    			filteredThemes.add(possibleThemes.get(i));
    			highestIndex = i;
    		}
    		else if(highestStat == 0)
    		{	filteredThemes.removeAllElements();
    			System.out.println("No theme matched");
    		}
		}

    	//for debugging purpose
    	for (int i = 0; i < filteredThemes.size(); i++) {
			System.out.println("filteredThemes[" + i + "]: " + filteredThemes.get(i).getMoralLesson());
		}

    	return highestIndex;
    }
    
  //added throws ParserException
    public IGTheme determineThemeRandom(InputContentRepresentation icr) throws StoryPlannerException {

    	this.ICR = icr;
    	Vector<IGTheme> possibleThemes = dbHelper.getRelatedThemes(ICR.getBackground().getBackgroundID());

    	//for debugging purpose
    	System.out.println("possible themes");
    	for (int i = 0; i < possibleThemes.size(); i++) {
			System.out.println(possibleThemes.elementAt(i).getMoralLesson());
		}

    	IGTheme selectedTheme = selectThemeRandom(possibleThemes, ICR.getObjects());
    	try {
    		System.out.println("STRING IS:" + selectedTheme.getString());
			selectedTheme.setLessonObject(Parser.parseWords(selectedTheme.getString(), ICR.getAge()));
		} catch (ParserException e) {
			throw new StoryPlannerException(e);
		}

    	return selectedTheme;
    }

    /**
     * Selects the most fitting theme based on the list of themes (possibleThemes) derived from the chosen background, and the input
     * objects (backgroundObjects). This function randomly chooses between the viable themes whenever there are tie-ups.
     *
     * @param possibleThemes - set of potential themes based on the chosen background
     * @param backgroundObjects - set of input objects
     * @return selected theme
     * @throws ParserException 
     */
    //added throws ParserException
    private IGTheme selectThemeRandom(Vector<IGTheme> possibleThemes, Vector<IGObject> backgroundObjects) throws StoryPlannerException {

    	Vector<IGTheme> filteredThemes = new Vector<IGTheme>();
    	return filterThemesRandom(filteredThemes, possibleThemes, backgroundObjects);
    }

   
    /**
     * Filters the potential list of themes based on the computed match of the input objects against the objects related to each possible
     * themes.
     *
     * @param filteredThemes - container for the filtered themes to be filled up by the function (initially empty)
     * @param possibleThemes - set of potential themes based on the chosen background
     * @param backgroundObjects - set of input objects
     * @return the index of the theme (in the filteredThemes vector) with the highest computed match value
     * @throws ParserException 
     */
    //added throws ParserException
    private IGTheme filterThemesRandom(Vector<IGTheme> filteredThemes, Vector<IGTheme> possibleThemes, Vector<IGObject> backgroundObjects) throws StoryPlannerException {

    	Vector<IGObject> possibleThemeObjects;

    	float highestStat = 1;
    	int highestIndex = 0;

    	for (int i = 0; i < possibleThemes.size(); i++) {

    		try {
				possibleThemeObjects = dbHelper.getObjects(possibleThemes.get(i).getID(), "tableThemes", "RelatedObjects");
			} catch (PBException e) {
				throw new StoryPlannerException(e);
			}

    		//for debugging purpose
    		System.out.println("THEME: "  + possibleThemes.get(i).getMoralLesson());
    		float match = computeMatch(possibleThemeObjects, backgroundObjects);

    		if(match != 0.0){
    			filteredThemes.add(possibleThemes.get(i));
    		}    		
	}

    	//for debugging purpose
    	for (int i = 0; i < filteredThemes.size(); i++) {
			System.out.println("filteredThemes[" + i + "]: " + filteredThemes.get(i).getMoralLesson());
    	}
    	
    	int a = new Random().nextInt(filteredThemes.size());
		System.out.println(a + " random number " + filteredThemes.size());
		return filteredThemes.get(a);
    }
}