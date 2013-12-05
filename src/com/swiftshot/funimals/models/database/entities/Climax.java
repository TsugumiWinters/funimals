package com.swiftshot.funimals.models.database.entities;

public class Climax {
	int climaxID;
	String description;
	
	public Climax(int climaxID, String description) {
		this.climaxID = climaxID;
		this.description = description;
	}
	public int getClimaxID() {
		return climaxID;
	}
	public void setClimaxID(int climaxID) {
		this.climaxID = climaxID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
