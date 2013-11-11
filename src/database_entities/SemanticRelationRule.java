package database_entities;

public class SemanticRelationRule {
	String SemanticRelation,
		Action = "",
		Agens = "",
		Patiens = "",
		Target = "",
		Instrument = "",
		Notes = "";

	public SemanticRelationRule() {}
	
	public SemanticRelationRule(String semanticRelation, String action,
			String agens, String patiens, String target, String instrument,
			String notes) {
		SemanticRelation = semanticRelation;
		Action = action;
		Agens = agens;
		Patiens = patiens;
		Target = target;
		Instrument = instrument;
		Notes = notes;
	}

	public String getSemanticRelation() {
		return SemanticRelation;
	}

	public void setSemanticRelation(String semanticRelation) {
		SemanticRelation = semanticRelation;
	}

	public String getAction() {
		return Action;
	}

	public void setAction(String action) {
		Action = action;
	}

	public String getAgens() {
		return Agens;
	}

	public void setAgens(String agens) {
		Agens = agens;
	}

	public String getPatiens() {
		return Patiens;
	}

	public void setPatiens(String patiens) {
		Patiens = patiens;
	}

	public String getTarget() {
		return Target;
	}

	public void setTarget(String target) {
		Target = target;
	}

	public String getInstrument() {
		return Instrument;
	}

	public void setInstrument(String instrument) {
		Instrument = instrument;
	}

	public String getNotes() {
		return Notes;
	}

	public void setNotes(String notes) {
		Notes = notes;
	}
}

