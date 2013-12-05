package com.swiftshot.funimals.models.storyplanner.ontology.component;

import java.util.Vector;

import com.swiftshot.funimals.models.database.entities.Concept;
import com.swiftshot.funimals.models.database.entities.Ontology;
import com.swiftshot.funimals.models.storyplanner.ontology.OntologyException;


/**
 * This class holds the elements of the ontology paths/relationships which is designed like a tree.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */

public class OntologyTree {

	private Node rootConcept;
	private Node leafConcept;

	private Node currentParent;

	private Vector<Node> toBurst = new Vector<Node>();

	/**
	 * OntologyTree constructor which places the first concept as the root node and the second concept the leaf node
	 * @param concept1 first concept
	 * @param concept2 second concept
	 */
	public OntologyTree(Concept concept1, Concept concept2){
		rootConcept = new Node(null, concept1, 0, null, 0);
		System.out.println("level: " + rootConcept.getLevel() + " object: " + rootConcept.getConcept().getConcept());
		leafConcept = new Node(null, concept2, 0, null, 0);
	}

	/**
	 * Gets the root node of the ontology tree
	 * @return the root node of the ontology tree
	 */
	public Concept getRoot(){
		currentParent = rootConcept;
		return rootConcept.getConcept();
	}

	/**
	 * Inserts the concept child and adds it to the list of nodes to be burst (Assumes that the currentParent is always the parent of the inserted children)
	 * @param child concept added to the list of nodes to be burst
	 * @param relationship relationship of the child node to its parent node
	 */
	public void insertChild(Concept child, String relationship){
		toBurst.add(currentParent.addChild(currentParent, child, currentParent.getLevel() + 1, relationship));
	}

	/**
	 * Gets the current parent concept
	 * @return the current parent concept
	 */
	public Concept getCurrentParent(){
		return currentParent.getConcept();
	}

	/**
	 * Gets the next parent concept
	 * @return the next parent concept
	 * @throws OntologyException raised if an error occurred while getting the next parent concept
	 */
	public Concept getNextParent() throws OntologyException{
		for (int i = 0; i < toBurst.size(); i++) {
			System.out.println("toBurst[" + i + "]: " + toBurst.get(i).getConcept().getConcept());
		}
		try{
			currentParent = toBurst.remove(0);
		}catch(ArrayIndexOutOfBoundsException oe){
			throw new OntologyException();
		}
		return currentParent.getConcept();
	}

	/**
	 * Gets the current level of the parent
	 * @return the current level of the parent
	 */
	public int getCurrentLevel(){
		return currentParent.getLevel();
	}

	/**
	 * Gets the index of the parent relative to its siblings
	 * @return the index of the parent relative to its siblings
	 */
	public int getIndex(){
		return currentParent.getIndex();
	}

	/**
	 * Gets the completed paths of the ontology concepts
	 * @return vector which contains a set of ontology concepts
	 */
	public Vector<Vector<Ontology>> getPaths(){

		Vector<Vector<Ontology>> paths = new Vector<Vector<Ontology>>();

		getPaths(paths, rootConcept);

		return paths;
	}

	private void getPaths(Vector<Vector<Ontology>> paths, Node currentNode){

		System.out.println("inside getPaths");

		if(currentNode.getChildren() != null){
			Node child;
			for(int i=0; i<currentNode.getChildren().size(); i++){

				child = currentNode.getChildren().get(i);

				if(child.getConcept().getElementID().equalsIgnoreCase(leafConcept.getElementID())){
					Vector<Ontology> tempPath = new Vector<Ontology>();
					int level = child.getLevel();
					Node cNode = child;
					for(int j=0; j<level; j++){
						tempPath.insertElementAt(new Ontology(cNode.getParent().getConcept(), cNode.getConcept(), cNode.getRelation()), 0);
						cNode = cNode.getParent();
					}
					paths.add(tempPath);
				}
				else{
					getPaths(paths, child);
				}
			}
		}
	}

    private class Node {

    	private Vector<Node> children = new Vector<Node>();
    	private Node parent;
    	private Concept conceptObject;
    	private String relation;
    	private int level;
    	//index: position of the node with respect to its siblings
    	private int index;

    	public Node(Node parent, Concept conceptObject, int level, String relation, int index) {
    		this.parent = parent;
    		this.conceptObject = conceptObject;
    		this.relation = relation;
			this.level = level;
		}

    	public Node getParent(){
    		return parent;
    	}

    	public Concept getConcept(){
    		return conceptObject;
    	}

    	public int getIndex(){
    		return index;
    	}

		public int getLevel() {
			return level;
		}

		public void setChildren(Vector<Node> children) {
			this.children = children;
		}

    	public Vector<Node> getChildren(){
    		return children;
    	}

		public String getRelation() {
			return relation;
		}

		public void setRelation(String relation) {
			this.relation = relation;
		}

		public String getElementID() {
			return conceptObject.getElementID();
		}

		public String getOntoID() {
			return conceptObject.getOntologyID();
		}

		public Node addChild(Node parent, Concept conceptObject, int level, String relation){
			Node child = new Node(parent, conceptObject, level, relation, getChildren().size());
			System.out.println("level: " + child.getLevel() + " object: " + child.getConcept().getConcept());
			children.add(child);
			return child;
    	}
    }
}