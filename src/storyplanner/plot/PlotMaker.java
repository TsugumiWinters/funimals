/**
 *
 */
package storyplanner.plot;

import java.util.StringTokenizer;
import java.util.Vector;

import pbmain.Debugger;
import pbmain.PBException;
import pbmain.util.Parser;
import pbmain.util.ParserException;
import pictureeditor.component.InputContentRepresentation;
import sentencegenerator.component.Phrase;
import storyplanner.StoryElement;
import storyplanner.StoryPlannerException;
import storyplanner.component.StoryTree;

import com.picturebooks.mobilepicturebooks.ApplicationContextProvider;

import database.DatabaseHelper;
import database_entities.AuthorGoal;
import database_entities.CharacterGoal;
import database_entities.IGCharacter;
import database_entities.IGTheme;
import database_entities.StoryPlotTracker;
import database_entities.Word;

/**
 * This class executes the story elements of the plot that make up the story.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */

public class PlotMaker {

	private StoryTree storyTree;

	private Vector<StoryPlotTracker> plots = new Vector<StoryPlotTracker>();

	private DatabaseHelper dbHelper = new DatabaseHelper(ApplicationContextProvider.getContext());
	private InputContentRepresentation ICR;

	/**
	 * Creates a story tree which contains the elements of the story
	 * @param chosenTheme theme of the story
	 * @param icr input content representation
	 * @throws StoryPlannerException raised if an error occurred while creating the story tree
	 */
	public void createStoryTree(IGTheme chosenTheme, InputContentRepresentation icr) throws StoryPlannerException{

		PlannerParser.objectReference = 0;

		initializeTheme(chosenTheme);

		initializeICR(icr);

		for (int stage = 0; stage < plots.size(); stage++) {

			StoryPlotTracker plot = plots.get(stage);
			Vector<AuthorGoal> authorgoals = new Vector<AuthorGoal>();

			System.out.println("PLOTS STAGE: " + plots.elementAt(stage).getStage());
			System.out.println("PLOTS AUTHOR GOAL: " + plots.elementAt(stage).getAuthorGoalsString());
			
			for(int i=0; i<plot.getAuthorGoalsString().size(); i++){ //nag nunull pointer exception dito 50% of the time
				authorgoals.add(dbHelper.instantiateAuthorGoal(plot.getAuthorGoalsString().get(i)));
			}

			plot.setAuthorGoalsAG(authorgoals);
			addToStoryTree(plot);

			executeAuthorGoal(plot);
		}
	}

	/**
	 * Gets the completed story tree
	 * @return {@link storyplanner.component.StoryTree} created
	 */
	public StoryTree getStoryTree(){
		return storyTree;
	}

	private void initializeTheme(IGTheme theme) throws StoryPlannerException{

		storyTree = new StoryTree(theme);
		plots.add(dbHelper.instantiatePlot(theme.getProblem()));
		plots.add(dbHelper.instantiatePlot(theme.getRisingAction()));
		plots.add(dbHelper.instantiatePlot(theme.getSolution()));
		plots.add(dbHelper.instantiatePlot(theme.getClimax()));
		System.out.println("HERE I GO:");
		System.out.println("Problem: " + theme.getProblem());
		System.out.println("Problem: " + theme.getRisingAction());
		System.out.println("Problem: " + theme.getSolution());
		System.out.println("Problem: " + theme.getClimax());
		for (int i = 0; i<plots.size(); i++){
			System.out.println(plots.elementAt(i).getStage());
			System.out.println(plots.elementAt(i).getAuthorGoalsString());
		}
	}

	private void initializeICR(InputContentRepresentation icr) throws StoryPlannerException{
		this.ICR = icr;
		StoryElement.setICR(ICR);
	}

	private void executeAuthorGoal(StoryPlotTracker plot) throws StoryPlannerException{
		
		for(int i=0; i<plot.getAuthorGoalsAG().size(); i++){

			AuthorGoal currentAg = plot.getAuthorGoalsAG().get(i);
			addToStoryTree(currentAg);

			Vector<String> parsedCg = PlannerParser.parseCharacterGoal(currentAg.getGoal());
			for(int j=0; j<parsedCg.size(); j++){
				//executeGoal
				if(j==0){
					currentAg.setGoalCG(executeCharacterGoal(parsedCg.get(j), true));
					//update authorgoal
					storyTree.getCurrentAuthorGoal().setGoal(currentAg.getGoal());
				}
				else //specificity character goals
					if(ICR.getAge() == 8)
						executeCharacterGoal(parsedCg.get(j), true);
					else if(ICR.getAge() == 7){
						executeCharacterGoal(parsedCg.get(j), true);
						break;
					}
					else break;
			}

			parsedCg = new Vector<String>(PlannerParser.parseCharacterGoal(currentAg.getConsequence()));

			for(int j=0; j<parsedCg.size(); j++){
				//executeConsequence
				if(j==0){
					currentAg.setConsequenceCG(executeCharacterGoal(parsedCg.get(j), true));
					//update authorgoal
					storyTree.getCurrentAuthorGoal().setConsequence(currentAg.getConsequence());
				}
				else //specificity character goals
					if(ICR.getAge() == 8)
						executeCharacterGoal(parsedCg.get(j), true);
					else if(ICR.getAge() == 7){
						executeCharacterGoal(parsedCg.get(j), true);
						break;
					}
					else break;
			}
		}
	}

	private CharacterGoal executeCharacterGoal(String invocation, boolean addToTree) throws StoryPlannerException{

		if(!invocation.equalsIgnoreCase("null")){

			CharacterGoal cg = dbHelper.instantiateCharacterGoal(PlannerParser.getCharGoalID(invocation));

			PlannerParser.parseParameters(invocation, cg);

			selectAgens(cg);	//agens

			selectPatiens(cg);	//patiens

			setInstrument(cg);	//instrument

			setTarget(cg);		//target

			setAction(cg);		//action

			//FOR DEBUG PURPOSE:
			Debugger.printCharacterGoal(cg);

			if(addToTree)
				addToStoryTree(cg);

			if(cg.getTargetDB() != null && cg.getTargetDB().getID().contains(StoryElement.CHARGOALKEY)){
				storyTree.insertChildCharacterGoal((CharacterGoal)cg.getTargetDB());
			}
			else if(cg.getInstrumentDB() != null && cg.getInstrumentDB().getID().contains(StoryElement.CHARGOALKEY)){
				storyTree.insertChildCharacterGoal((CharacterGoal)cg.getInstrumentDB());
			}

			return cg;
		}
		return null;
	}

	private void selectAgens(CharacterGoal cg) throws StoryPlannerException{

		try{
			if(cg.getAgens() != null)
				if(cg.getAgens().indexOf(StoryElement.CHARGOALKEY) == 0){
					CharacterGoal innerCg = executeCharacterGoal(cg.getAgens(), false);
					cg.setAgensDB(innerCg);
				}
				else{
					String agens = PlannerParser.parseArgument(cg.getAgens(), cg, storyTree, ICR.getAge());
		//			System.out.println(agens);
					if(agens.indexOf(StoryElement.WORDKEY) == 0){
						cg.setAgensDB(Parser.parseWords(agens, ICR.getAge()));
		//				System.out.println("AGENS: " + ((Word)cg.getAgens()).getCorrespondingWord());
					}
					else if(agens.indexOf(StoryElement.CHARKEY) == 0){
						cg.setAgensDB(setRole(agens));
		//				System.out.println("AGENS: " + ((Character)cg.getAgens()).getName());
					}
					else if(agens.indexOf(StoryElement.CHARGOALKEY) == 0){
						cg.setAgensDB(storyTree.getCurrentCharacterGoal());
					}
				}
		}catch(ParserException e){
			throw new StoryPlannerException(e);
		}
	}

	private void selectPatiens(CharacterGoal cg) throws StoryPlannerException{

		try{
			if(cg.getPatiens() != null)
				if(cg.getPatiens().indexOf(StoryElement.CHARGOALKEY) == 0){
					CharacterGoal innerCg = executeCharacterGoal(cg.getPatiens(), false);
					cg.setPatiensDB(innerCg);
				}
				else{
					String patiens = PlannerParser.parseArgument(cg.getPatiens(), cg, storyTree, ICR.getAge());
		//			System.out.println(patiens);
					if(patiens.indexOf(StoryElement.WORDKEY) == 0){
						cg.setPatiensDB(Parser.parseWords(patiens, ICR.getAge()));
		//				System.out.println("PATIENS: " + ((Word)cg.getPatiens()).getCorrespondingWord());
					}
					else if(patiens.indexOf(StoryElement.CHARKEY) == 0){
						cg.setPatiensDB(setRole(patiens));
		//				System.out.println("PATIENS: " + ((Character)cg.getPatiens()).getName());
					}
					else if(patiens.indexOf(StoryElement.CHARGOALKEY) == 0){
						cg.setPatiensDB(storyTree.getCurrentCharacterGoal());
					}
				}
		}catch(ParserException e){
			throw new StoryPlannerException(e);
		}
	}

	private IGCharacter setRole(String charID) throws StoryPlannerException{

		IGCharacter character;
		String childRole;
		try {
			character = dbHelper.instantiateCharacter(charID);
			childRole = dbHelper.getWordId("child", ICR.getAge());
		} catch (PBException e) {
			throw new StoryPlannerException(e);
		}
		String characterRole = character.getRole();
		if(!characterRole.equalsIgnoreCase(childRole)){
			if(!characterRole.equalsIgnoreCase(ICR.getBackground().getRequiredRole())){
				if(character.getGender().equalsIgnoreCase("f"))
					character.setString("Mommy " + character.getString());
				else character.setString("Daddy " + character.getString());
			}
			else{
				try{
					character.setString(dbHelper.instantiateWord(characterRole, ICR.getAge()).getString() +
							" " + character.getString());
				} catch (PBException e) {
					throw new StoryPlannerException(e);
				}
			}
		}
		return character;
	}

	private void setInstrument(CharacterGoal cg) throws StoryPlannerException{

		try{
			if(cg.getInstrument() != null)
				if(cg.getInstrument().indexOf(StoryElement.CHARGOALKEY) == 0){
					CharacterGoal innerCg = executeCharacterGoal(cg.getInstrument(), false);
					cg.setInstrumentDB(innerCg);
				}
				else{
					String instrument = PlannerParser.parseArgument(cg.getInstrument(), cg, storyTree, ICR.getAge());
		//			System.out.println(instrument);
					if(instrument.indexOf(StoryElement.WORDKEY) == 0){
						cg.setInstrumentDB(Parser.parseWords(instrument, ICR.getAge()));
		//				System.out.println("INSTRUMENT: " + ((Word)cg.getInstrument()).getCorrespondingWord());
					}
					else if(instrument.indexOf(StoryElement.CHARKEY) == 0){
						cg.setInstrumentDB(dbHelper.instantiateCharacter(instrument));
		//				System.out.println("INSTRUMENT: " + ((Character)cg.getInstrument()).getName());
					}
					else if(instrument.indexOf(StoryElement.CHARGOALKEY) == 0){
						cg.setInstrumentDB(storyTree.getCurrentCharacterGoal());
					}
				}
		}catch(PBException e){
			throw new StoryPlannerException(e);
		}catch(ParserException e){
			throw new StoryPlannerException(e);
		}
	}

	private void setTarget(CharacterGoal cg) throws StoryPlannerException{

		try{
			if(cg.getTarget() != null)
				if(cg.getTarget().indexOf(StoryElement.CHARGOALKEY) == 0){
					CharacterGoal innerCg = executeCharacterGoal(cg.getTarget(), false);
					cg.setTargetDB(innerCg);
				}
				else{
					String target = PlannerParser.parseArgument(cg.getTarget(), cg, storyTree, ICR.getAge());
		//			System.out.println(target);
					if(target.indexOf(StoryElement.WORDKEY) == 0){
						cg.setTargetDB(Parser.parseWords(target, ICR.getAge()));
		//				System.out.println("TARGET: " + ((Word)cg.getTarget()).getCorrespondingWord());
					}
					else if(target.indexOf(StoryElement.CHARKEY) == 0){
						cg.setTargetDB(dbHelper.instantiateCharacter(target));
		//				System.out.println("TARGET: " + ((Character)cg.getTarget()).getName());
					}
					else if(target.indexOf(StoryElement.CHARGOALKEY) == 0){
						cg.setTargetDB(storyTree.getCurrentCharacterGoal());
					}
				}
		}catch(PBException e){
			throw new StoryPlannerException(e);
		}catch(ParserException e){
			throw new StoryPlannerException(e);
		}
	}

	private void setAction(CharacterGoal cg) throws StoryPlannerException{

		Phrase actionWords = new Phrase();
		StringTokenizer st = new StringTokenizer(cg.getAction(), " ");

		try{
			if(cg.isNegated()){
				actionWords.add(dbHelper.getWord("not", ICR.getAge()));
			}
			while(st.hasMoreTokens()){
				String nextToken = st.nextToken();
				if(dbHelper.instantiateWord(nextToken, ICR.getAge()).getString().equalsIgnoreCase("do")){
					if(cg.getTargetDB() instanceof Phrase)
						for(int i=0; i<((Phrase)cg.getTargetDB()).size(); i++){
							actionWords.add(((Phrase)cg.getTargetDB()).get(i));
						}
					else actionWords.add((Word)cg.getTargetDB());
					cg.setHideTarget(true);
				}
				else actionWords.add(dbHelper.instantiateWord(nextToken, ICR.getAge()));
			}
		}catch(PBException e){
			throw new StoryPlannerException(e);
		}

		if(actionWords.size() > 1)
			cg.setActionDB(actionWords);
		else cg.setActionDB(actionWords.get(0));
	}

	private void addToStoryTree(AuthorGoal ag){
		storyTree.insertAuthorGoal(ag);
		System.out.println("author goal " + ag.getID() + " added to story tree");
	}

	private void addToStoryTree(StoryPlotTracker plot){
		storyTree.insertPlot(plot);
		System.out.println("plot " + plot.getID()+ " added to story tree");
	}

	private void addToStoryTree(CharacterGoal cg){
		storyTree.insertCharacterGoal(cg);
		System.out.println("character goal " + cg.getID()+ " added to story tree");
	}

	/**
	 * Returns the completed story tree
	 * @return {@link storyplanner.component.StoryTree} created
	 */
	//for debugging purpose
	public StoryTree getTree(){
		return storyTree;
	}
}
