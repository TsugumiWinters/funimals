package database_entities;

public class Ontology {
	String OntologyID1,
		SemanticRelation,
		OntologyID2;
	String Category;
	
	Concept element1;
	Concept element2;
	
	public Ontology() {}

	public Ontology(String ontologyID1, String semanticRelation,
			String ontologyID2, String category) {
		OntologyID1 = ontologyID1;
		SemanticRelation = semanticRelation;
		OntologyID2 = ontologyID2;
		Category = category;
	}

	public String getOntologyID1() {
		return OntologyID1;
	}

	public void setOntologyID1(String ontologyID1) {
		OntologyID1 = ontologyID1;
	}

	public String getSemanticRelation() {
		return SemanticRelation;
	}

	public void setSemanticRelation(String semanticRelation) {
		SemanticRelation = semanticRelation;
	}

	public String getOntologyID2() {
		return OntologyID2;
	}

	public void setOntologyID2(String ontologyID2) {
		OntologyID2 = ontologyID2;
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}
	
	public Ontology(Concept element1, Concept element2, String semanticRelation){
		setElement1(element1);
		setElement2(element2);
		SemanticRelation = semanticRelation;
	}
	
	public Concept getElement1(){
		return element1;
	}
	
	public void setElement1(Concept element1){
		this.element1 = element1;
	}
	
	public Concept getElement2(){
		return element2;
	}
	
	public void setElement2(Concept element2){
		this.element2 = element2;
	}
}
