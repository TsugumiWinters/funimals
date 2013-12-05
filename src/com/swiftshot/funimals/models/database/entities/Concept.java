package com.swiftshot.funimals.models.database.entities;

public class Concept {
	String OntologyID;
	String ElementID;
	String Concept;
	
	//added
	Object element;

	public Concept(){}

	public Concept(String ontologyID, String elementID, String concept) {
		OntologyID = ontologyID;
		ElementID = elementID;
		Concept = concept;
	}
	
	public Concept(String ontologyID){
		this.OntologyID = ontologyID;
	}

	public String getOntologyID() {
		return OntologyID;
	}

	public void setOntologyID(String ontologyID) {
		OntologyID = ontologyID;
	}

	public String getElementID() {
		return ElementID;
	}

	public void setElementID(String elementID) {
		ElementID = elementID;
	}

	public String getConcept() {
		return Concept;
	}

	public void setConcept(String concept) {
		Concept = concept;
	}
}
