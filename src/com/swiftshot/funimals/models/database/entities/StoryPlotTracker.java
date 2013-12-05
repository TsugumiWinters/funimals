package com.swiftshot.funimals.models.database.entities;

import java.util.Vector;

import com.swiftshot.funimals.models.pbmain.util.DBObject;


public class StoryPlotTracker implements DBObject {
	String PlotID,
		Name,
		Stage,
		AuthorGoals;	
	
	Vector<AuthorGoal> authorGoalsAG;
	Vector<String> authorGoalsString = new Vector<String>();
	
	public StoryPlotTracker() { }

	public StoryPlotTracker(String plotID, String name, String stage,
			Vector<String> authorGoals) {
		PlotID = plotID;
		Name = name;
		Stage = stage;
		this.authorGoalsString = authorGoals;
	}
	/*
	public String getPlotID() {
		return PlotID;
	}

	public void setPlotID(String plotID) {
		PlotID = plotID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}
	*/
	public String getStage() {
		return Stage;
	}

	public void setStage(String stage) {
		Stage = stage;
	}

	public String getAuthorGoals() {
		return AuthorGoals;
	}

	public void setAuthorGoals(String authorGoals) {
		AuthorGoals = authorGoals;
	}
	
	public String getID() {
		return PlotID;
	}

	public void setID(String plotID) {
		PlotID = plotID;
	}

	public String getString() {
		return Name;
	}

	public void setString(String name) {
		Name = name;
	}
	
	//added
	public Vector<AuthorGoal> getAuthorGoalsAG(){
		return authorGoalsAG;
	}
	
	public void setAuthorGoalsAG(Vector<AuthorGoal> authorGoals){
		this.authorGoalsAG = authorGoals;
	}
	
	public Vector<String> getAuthorGoalsString(){
		return authorGoalsString;
	}
	
	public void setAuthorGoalsString(Vector<String> authorGoals){
		this.authorGoalsString = authorGoals;
	}
	
	
}
