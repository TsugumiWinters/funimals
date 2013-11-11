package database_entities;

import pbmain.util.DBObject;

public class AuthorGoal implements DBObject {
	String AuthorGoalID,
		Name,
		Goal,
		Consequence;
	
	CharacterGoal goalCG;
	CharacterGoal consequenceCG;
	
	public AuthorGoal() {}
	
	public AuthorGoal(String authorGoalID, String name, String goal,
			String consequence) {
		AuthorGoalID = authorGoalID;
		Name = name;
		Goal = goal;
		Consequence = consequence;
	}

	public String getGoal() {
		return Goal;
	}

	public void setGoal(String goal) {
		Goal = goal;
	}

	public String getConsequence() {
		return Consequence;
	}

	public void setConsequence(String consequence) {
		Consequence = consequence;
	}
	
	public String getID() {
		return AuthorGoalID;
	}

	public void setID(String authorGoalID) {
		AuthorGoalID = authorGoalID;
	}

	public String getString() {
		return Name;
	}

	public void setString(String name) {
		Name = name;
	}
	
	//added
	public CharacterGoal getConsequenceCG(){
		return consequenceCG;
	}
	
	public void setConsequenceCG(CharacterGoal consequence){
		this.consequenceCG = consequence;
	}
	
	public CharacterGoal getGoalCG() {
		return goalCG;
	}

	public void setGoalCG(CharacterGoal goal) {
		goalCG = goal;
	}
	
}
