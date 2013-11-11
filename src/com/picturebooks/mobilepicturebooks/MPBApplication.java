package com.picturebooks.mobilepicturebooks;

import java.util.Vector;

import android.app.Application;

import database_entities.Adjective;
import database_entities.Adverb;
import database_entities.Article;
import database_entities.AuthorGoal;
import database_entities.Background;
import database_entities.CharacterGoal;
import database_entities.Concept;
import database_entities.ConceptMapper;
import database_entities.Conjunction;
import database_entities.IGCharacter;
import database_entities.IGObject;
import database_entities.IGTheme;
import database_entities.Noun;
import database_entities.Ontology;
import database_entities.Preposition;
import database_entities.Pronoun;
import database_entities.SemanticRelationRule;
import database_entities.StoryFile;
import database_entities.StoryPlotTracker;
import database_entities.UserInformation;
import database_entities.Verb;
import database_entities.Word;

public class MPBApplication extends Application {
	
	private String user = "Jeremy";
	private int age = 8;
	private int storyID;
	
	public int getStoryID() {
		return storyID;
	}
	
	public void setStoryID(int storyID) {
		this.storyID = storyID;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	

}
