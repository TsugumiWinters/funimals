package database_entities;

public class ConceptMapper {
	String ConceptID,
		AgeSixWordID,
		AgeSevenWordID,
		AgeEightWordID;
	String PartOfSpeech;
	String Word;

	public ConceptMapper(){}

	public ConceptMapper(String conceptID, String ageSixWordID,
			String ageSevenWordID, String ageEightWordID, String partOfSpeech,
			String word) {
		ConceptID = conceptID;
		AgeSixWordID = ageSixWordID;
		AgeSevenWordID = ageSevenWordID;
		AgeEightWordID = ageEightWordID;
		PartOfSpeech = partOfSpeech;
		Word = word;
	}

	public String getConceptID() {
		return ConceptID;
	}

	public void setConceptID(String conceptID) {
		ConceptID = conceptID;
	}

	public String getAgeSixWordID() {
		return AgeSixWordID;
	}

	public void setAgeSixWordID(String ageSixWordID) {
		AgeSixWordID = ageSixWordID;
	}

	public String getAgeSevenWordID() {
		return AgeSevenWordID;
	}

	public void setAgeSevenWordID(String ageSevenWordID) {
		AgeSevenWordID = ageSevenWordID;
	}

	public String getAgeEightWordID() {
		return AgeEightWordID;
	}

	public void setAgeEightWordID(String ageEightWordID) {
		AgeEightWordID = ageEightWordID;
	}

	public String getPartOfSpeech() {
		return PartOfSpeech;
	}

	public void setPartOfSpeech(String partOfSpeech) {
		PartOfSpeech = partOfSpeech;
	}

	public String getWord() {
		return Word;
	}

	public void setWord(String word) {
		Word = word;
	}
	
	
}
