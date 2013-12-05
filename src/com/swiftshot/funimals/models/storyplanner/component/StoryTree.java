/*
 * Latest update: February 25, 2008
 */

package com.swiftshot.funimals.models.storyplanner.component;

import java.util.Vector;

import com.swiftshot.funimals.models.database.entities.AuthorGoal;
import com.swiftshot.funimals.models.database.entities.CharacterGoal;
import com.swiftshot.funimals.models.database.entities.IGTheme;
import com.swiftshot.funimals.models.database.entities.StoryPlotTracker;
import com.swiftshot.funimals.models.pbmain.util.DBObject;


/**
 * This is the tree that would hold the arrangement of story events of the theme chosen for the picture.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
public class StoryTree {

    private Node root;
    private Node currentPlot;
    private Node currentAuthorGoal;
    private Node currentCharacterGoal;

    /**
     * StoryTree constructor that sets the given theme as the root node of the tree.
     * @param theme the theme of the story
     */
	public StoryTree(IGTheme theme){
		root = new Node(null, 0, theme);
	}

	/**
	 * Getter method for the root of the story tree.
	 * @return the theme which is the root of the story tree
	 */
	public IGTheme getRoot(){
		return (IGTheme)root.getObject();
	}

	/**
	 * Getter method for all of the character goals in the story tree
	 * @return the vector of character goals in the story tree
	 */
	public Vector<CharacterGoal> getAllCharGoals(){
		Vector<CharacterGoal> characterGoals = new Vector<CharacterGoal>();
		Vector<Node> plots = root.getChildren(), authorGoals, charGoals;

		for (int k = 0; k < plots.size(); k++) {
			authorGoals = plots.get(k).getChildren();
			for (int i = 0; i < authorGoals.size(); i++) {
				charGoals = authorGoals.get(i).getChildren();
				for (int j = 0; j < charGoals.size(); j++) {
					characterGoals.add((CharacterGoal) charGoals.get(j).getObject());
				}
			}
		}
		return characterGoals;
	}

	/**
	 * Getter method for the first character goal of the first author goal in the tree, found after the root.
	 * @return the first character goal in the tree.
	 */
	public CharacterGoal getFirstCharGoal(){
		return (CharacterGoal) root.children.get(0).children.get(0).children.get(0).getObject();
	}

	/**
	 * Inserts the initial time and location character goals before the problem character goals.
	 * @param charGoal the character goal that indicates the initial setting of the story.
	 */
	public void insertIntroGoals(CharacterGoal charGoal){
		
		Node authorGoal = getFirstAuthorGoal(), aGoal = null;
		for (int i = 0; i < authorGoal.getChildren().size(); i++) {
			aGoal = authorGoal.getChildren().get(i);
			aGoal.setIndex(aGoal.getIndex() + 1);
		}
		authorGoal.children.insertElementAt(new Node(authorGoal, 0, charGoal), 0);
	}

	/**
	 * Getter method for the first author goal in the story tree
	 * @return the node containing the first author goal in the story tree.
	 */
	public Node getFirstAuthorGoal(){
		return root.getChildren().get(0).getChildren().get(0);
	}

	/**
	 * Getter method for the current plot element being processed;
	 * @return the {@link Plot} element being processed.
	 */
	public StoryPlotTracker getCurrentPlot(){
		return (StoryPlotTracker)currentPlot.getObject();
	}

	/**
	 * Getter method for the current author goal being processed.
	 * @return the {@link AuthorGoal} being processed.
	 */
	public AuthorGoal getCurrentAuthorGoal(){
		return (AuthorGoal)currentAuthorGoal.getObject();
	}

	/**
	 * Getter method for the current character goal being processed.
	 * @return the {@link CharacterGoal} being processed.
	 */
	public CharacterGoal getCurrentCharacterGoal(){
		return (CharacterGoal)currentCharacterGoal.getObject();
	}

	/**
	 * Inserts a {@link Plot} below the root of the story tree.
	 * @param sp the story plot to be inserted
	 */
	public void insertPlotInFront(StoryPlotTracker sp){
		currentPlot = root.setChild(sp, 0);
	}

	/**
	 * Inserts a {@link Plot} after the last plot in the story tree and sets it as the current plot being processed.
	 * @param sp the story plot to be inserted
	 */
	public void insertPlot(StoryPlotTracker sp){
		currentPlot = root.setChild(sp);
	}

	/**
	 * Inserts an {@link AuthorGoal} as the child of the current plot in the story tree and sets it as the current
	 * author goal being processed.
	 * @param ag the author goal to be inserted
	 */
	public void insertAuthorGoal(AuthorGoal ag){
		currentAuthorGoal = currentPlot.setChild(ag);
	}

	/**
	 * Inserts an {@link CharacterGoal} as the child of the current author goal in the story tree and sets it as the current
	 * character goal being processed.
	 * @param cg the character goal to be inserted
	 */
	public void insertCharacterGoal(CharacterGoal cg){
		currentCharacterGoal = currentAuthorGoal.setChild(cg);
	}

	/**
	 * Inserts a character as a child of a character goal. Used only on cases when a character goal invokes
	 * another character goal as its clause.
	 * @param cg the character goal to be inserted
	 */
	public void insertChildCharacterGoal(CharacterGoal cg){
		currentCharacterGoal.setChild(cg);
	}

	/**
	 * Checks if the story tree contains the {@link DBObject} passed as parameter
	 * @param object the {@link DBObject} to search for
	 * @return true if the {@link DBObject} is in the tree, false if not.
	 */
	public boolean contains(DBObject object){

		boolean isFound = false;
		Node searchNode = root;
		Vector<Node> toBeSearched = new Vector<Node>();

		do{
			if(searchNode.getObject().equals(object)){
				isFound = true;
			}
			else{
				toBeSearched.addAll(searchNode.getChildren());
				searchNode = toBeSearched.remove(0);
			}
		}while(!isFound && !toBeSearched.isEmpty());

		return isFound;
	}

	/**
	 * Getter method for the previously processed plot.
	 * @return the previously processed plot
	 * @throws Exception thrown if the current plot is not preceded by another plot.
	 */
	public StoryPlotTracker getPreviousPlot() throws Exception{
		return getPreviousPlot((StoryPlotTracker)currentPlot.getObject());
	}

	/**
	 * Gets the plot preceding the plot sent as parameter
	 * @return the plot preceding the plot sent as parameter
	 * @throws Exception thrown if the plot sent as parameter is not preceded by another plot.
	 */
	public StoryPlotTracker getPreviousPlot(StoryPlotTracker thisPlot) throws Exception{

		StoryPlotTracker previousPlot = null;
		Node thisNode, parentNode, previousNode;
		int index;
		try{
			thisNode = searchNode(thisPlot);
			index = thisNode.getIndex();
			parentNode = thisNode.getParent();
			previousNode = parentNode.getChildren().get(index-1);
			previousPlot = (StoryPlotTracker)previousNode.getObject();
		}catch(Exception e){
			throw(e);
		}

		return previousPlot;
	}

	/**
	 * Getter method for the previously processed author goal.
	 * @return the previously processed author goal
	 * @throws Exception thrown if the current author goal is not preceded by another author goal.
	 */
	public AuthorGoal getPreviousAuthorGoal() throws Exception{
		return getPreviousAuthorGoal((AuthorGoal)currentAuthorGoal.getObject());
	}

	//exception if a previous author goal does not exist
	/**
	 * Gets the author goal preceding the author goal sent as parameter
	 * @return the author goal preceding the author goal sent as parameter
	 * @throws Exception thrown if the author goal sent as parameter is not preceded by another author goal.
	 */
	public AuthorGoal getPreviousAuthorGoal(AuthorGoal thisAG) throws Exception{

		AuthorGoal previousAG = null;
		Node thisNode, parentNode, previousNode;
		int index;

		try{
			thisNode = searchNode(thisAG);
			index = thisNode.getIndex();
			parentNode = thisNode.getParent();
			if(index > 0)
				previousNode = parentNode.getChildren().get(index-1);
			else
				previousNode = searchNode(getPreviousPlot((StoryPlotTracker)parentNode.getObject())).getChildren().lastElement();
			previousAG = (AuthorGoal)previousNode.getObject();
		}catch (Exception e){
			throw(e);
		}

		return previousAG;
	}

	/**
	 * Getter method for the previously processed character goal.
	 * @return the previously processed character goal
	 * @throws Exception thrown if the current character goal is not preceded by another character goal.
	 */
	public CharacterGoal getPreviousCharacterGoal() throws Exception{
		return getPreviousCharacterGoal((CharacterGoal)currentCharacterGoal.getObject());
	}

	/**
	 * Gets the character goal preceding character goal plot sent as parameter
	 * @return the character goal preceding the character goal sent as parameter
	 * @throws Exception thrown if the character goal sent as parameter is not preceded by another character goal.
	 */
	public CharacterGoal getPreviousCharacterGoal(CharacterGoal thisCG) throws Exception{

		CharacterGoal previousCG = null;
		Node thisNode, parentNode, previousNode;
		int index;

		try{
			thisNode = searchNode(thisCG);
			index = thisNode.getIndex();
			parentNode = thisNode.getParent();
			if(index > 0)
				previousNode = parentNode.getChildren().get(index-1);
			else
				previousNode = searchNode(getPreviousAuthorGoal((AuthorGoal)parentNode.getObject())).getChildren().lastElement();
			previousCG = (CharacterGoal)previousNode.getObject();
		}catch(Exception e){
			throw(e);
		}

		return previousCG;
	}

	/**
	 * Gets all the story plots in the tree.
	 * @return a vector containing all the {@link Plot} objects in the story tree
	 * @throws Exception thrown when there are no {@link Plot} objects in the tree
	 */
	public Vector<StoryPlotTracker> getPlots() throws Exception{

		Vector<StoryPlotTracker> plots = new Vector<StoryPlotTracker>();
		Vector<Node> children;

		try{
			children = searchNode(root.getObject()).getChildren();
			for(int i=0; i<children.size(); i++){
				plots.add((StoryPlotTracker)children.get(i).getObject());
			}
		}catch(Exception e){
			throw(e);
		}

		return plots;
	}

	/**
	 * Gets all the author goals in the plot currently being processed.
	 * @return a vector containing all the {@link AuthorGoal} objects in the current plot
	 * @throws Exception thrown when there are no {@link AuthorGoal} objects in the current plot
	 */
	public Vector<AuthorGoal> getAuthorGoals() throws Exception{
		return getAuthorGoals((StoryPlotTracker)currentPlot.getObject());
	}

	/**
	 * Gets the author goals of the specified story {@link Plot}.
	 * @param plot to search in
	 * @return a vector containing all the {@link AuthorGoal} objects of the plot specified
	 * @throws Exception thrown when there are no {@link AuthorGoal} objects in the plot specified
	 */
	public Vector<AuthorGoal> getAuthorGoals(StoryPlotTracker plot) throws Exception{

		Vector<AuthorGoal> goals = new Vector<AuthorGoal>();
		Vector<Node> children;

		try{
			children = searchNode(plot).getChildren();
			for(int i=0; i<children.size(); i++){
				goals.add((AuthorGoal)children.get(i).getObject());
			}
		}catch(Exception e){
			throw(e);
		}

		return goals;
	}

	/**
	 * Gets all the character goals in the author goal currently being processed.
	 * @return a vector containing all the {@link CharacterGoal} objects in the current author goal
	 * @throws Exception thrown when there are no {@link CharacterGoal} objects in current author goal
	 */
	public Vector<CharacterGoal> getCharacterGoals() throws Exception{
		return getCharacterGoals((AuthorGoal)currentAuthorGoal.getObject());
	}

	/**
	 * Gets the character goals of the specified story {@link Plot}.
	 * @param ag the author goal to be search in
	 * @return a vector containing all the {@link CharacterGoal} objects of the author goal specified
	 * @throws Exception thrown when there are no {@link CharacterGoal} objects in the author goal specified
	 */
	public Vector<CharacterGoal> getCharacterGoals(AuthorGoal ag) throws Exception{

		Vector<CharacterGoal> goals = new Vector<CharacterGoal>();
		Vector<Node> children;

		try{
			children = searchNode(ag).getChildren();
			for(int i=0; i<children.size(); i++){
				goals.add((CharacterGoal)children.get(i).getObject());
			}
		}catch(Exception e){
			throw(e);
		}

		return goals;
	}

	/**
	 * Gets all the character goals in the story tree.
	 * @return a vector containing all the {@link CharacterGoal} objects in the story tree
	 * @throws Exception thrown when there are no {@link CharacterGoal} objects in the tree
	 */
	public Vector<CharacterGoal> getAllCharacterGoals(){

		Vector<CharacterGoal> allCG = new Vector<CharacterGoal>();

		boolean found = false;
		Node traverseNode = root;
		Node returnNode = null;

		returnNode = currentCharacterGoal;
		Vector<Node> toBeSearched = new Vector<Node>();

		while(!found){
			if(traverseNode.equals(returnNode))
				found = true;
			else{
				toBeSearched.addAll(traverseNode.getChildren());
				traverseNode = toBeSearched.remove(0);
				if(traverseNode.getObject().getClass().getSimpleName().equalsIgnoreCase("CharacterGoal")){
//					if(!traverseNode.getChildren().isEmpty()){
//						for (int j = 0; j < traverseNode.getChildren().size(); j++) {
//							allCG.add((CharacterGoal)traverseNode.getChildren().get(j).getObject());
//						}
//					}
					//inner clauses are added before the outer clause
					allCG.add((CharacterGoal)traverseNode.getObject());
				}
			}
		}

		return allCG;
	}

	/**
	 * Searches and returns the tree node containing the specified {@link DBObject}
	 * @param object to search for
	 * @return the tree {@link Node} containing the specified {@link DBObject}
	 * @throws Exception thrown when the {@link DBObject} is not found in any of the nodes in the story tree
	 */
	private Node searchNode(DBObject object) throws Exception{

		boolean found = false;
		Node searchNode = root;
		Node returnNode = null;
		Vector<Node> toBeSearched = new Vector<Node>();

		while(!found){
			if(searchNode.getObject().equals(object)){
				returnNode = searchNode;
				found = true;
			}
			else{
				toBeSearched.addAll(searchNode.getChildren());
				searchNode = toBeSearched.remove(0);
			}
		}
		return returnNode;
	}

	/**
	 * Goes through the whole tree from top to bottom.
	 * @return vector of containing all the {@link DBObject} in the tree.
	 */
	public Vector<DBObject> traverseStoryTree(){

		boolean found = false;
		Node searchNode = root;

		Node returnNode = null;

		try {
			returnNode = searchNode(getCurrentCharacterGoal());
		} catch (Exception e) {
			e.printStackTrace();
		}

		Vector<Node> toBeSearched = new Vector<Node>();
		Vector<DBObject> searchedNode = new Vector<DBObject>();

		while(!found){
			if(searchNode.equals(returnNode))
				found = true;
			else{
				toBeSearched.addAll(searchNode.getChildren());
				searchNode = toBeSearched.remove(0);
				searchedNode.add(searchNode.getObject());
			}
		}

		return searchedNode;
	}

	/**
	 * Private inner class for the Nodes of the {@link StoryTree}
	 *
	 * @author Joan Tiffany Siy
	 * @author Candice Jean Solis
	 * @author Emerald Tabirao
	 * @author Arvin Jasper Hong
	 */
    private class Node {

    	/**
    	 * The object to be stored
    	 */
    	private DBObject object;
    	/**
    	 * The parent node
    	 */
    	private Node parent;
    	/**
    	 * The index of the node relative to its siblings
    	 */
    	private int index;
    	/**
    	 * The children of the node
    	 */
    	private Vector<Node> children = new Vector<Node>();
    	/**
    	 * Denotes if the node is a leaf or not
    	 */
    	private boolean isLeaf = false;

    	/**
    	 * Node constructor class and initializes some of its attributes based on the parameters.
    	 * @param parent the parent of the node
    	 * @param index the index of the node relative to its siblings
    	 * @param theme the object to be stored in the node.
    	 */
    	public Node(Node parent, int index, DBObject theme){
    		this.parent = parent;
    		this.index = index;
    		this.object = theme;
    	}

    	/**
    	 * Adds the node as the child of a {@link Plot}.
    	 * @param sp the {@link Plot} to add the node to
    	 * @return the node added as the child of the plot
    	 */
    	public Node setChild(StoryPlotTracker sp){

    		int size = children.size();
    		Node child = new Node(this, size, sp);
    		children.add(child);
    		return child;
    	}

    	/**
    	 * Adds the node as the child of a {@link Plot} at the specified index.
    	 * @param sp the {@link Plot} to add the node to
    	 * @param pos the index indicating where to add the child
    	 * @return the node added as the child of the plot
    	 */
    	public Node setChild(StoryPlotTracker sp, int pos){

    		Node child = new Node(this, pos, sp);
    		children.add(child);
    		return child;
    	}

    	/**
    	 * Adds the node as the child of a {@link AuthorGoal}.
    	 * @param ag the {@link AuthorGoal} to add the node to
    	 * @return the node added as the child of the author goal
    	 */
    	public Node setChild(AuthorGoal ag){

    		int size = children.size();
    		Node child = new Node(this, size, ag);
    		children.add(child);
    		return child;
    	}

    	/**
    	 * Adds the node as the child of a {@link CharacterGoal}.
    	 * @param ag the {@link CharacterGoal} to add the node to
    	 * @return the node added as the child of the character goal
    	 */
    	public Node setChild(CharacterGoal cg){

    		int size = children.size();
    		Node child = new Node(this, size, cg);
    		child.setIsLeaf();
    		children.add(child);

    		return child;
    	}

    	/**
    	 * Getter method for the children of the node
    	 * @return vector of {@link Node}
    	 */
    	public Vector<Node> getChildren(){

    		return children;
    	}

    	/**
    	 * Getter method for the object stored in the node.
    	 * @return the object stored in the node
    	 */
    	public DBObject getObject(){

    		return object;
    	}

    	/**
    	 * Getter method for the node's parent node.
    	 * @return the node's parent node
    	 */
    	public Node getParent(){

    		return parent;
    	}

    	/**
    	 * Getter method for the index of the node relative to its siblings.
    	 * @return
    	 */
    	public int getIndex(){

    		return index;
    	}

    	/**
    	 * Sets the node as a leaf node.
    	 */
    	public void setIsLeaf(){

    		isLeaf = true;
    	}

    	/**
    	 * Checks if the node is a leaf node or not.
    	 * @return true if the node is a leaf node; false if not.
    	 */
    	public boolean isLeaf(){

    		return isLeaf;
    	}

    	/**
    	 * Setter method for the index of the node relative to its siblings.
    	 * @param index
    	 */
    	public void setIndex(int index){
    		this.index = index;
    	}
    }
}
