package database_entities;

import pbmain.util.DBObject;
import sentencegenerator.component.DBLexicalObject;

//added DBLexicalObject stuffs
public class IGTheme implements DBObject {
	String ThemeID,
		InitActivity,
		Lesson,
		MoralLesson,
		RelatedObjects,
		Problem,
		RisingAction,
		Solution,
		Climax,
		InitSettings,
		InitTime;
	
	DBLexicalObject lessonObject;
	
	public IGTheme() {}

	public IGTheme(String themeID, String initActivity, String lesson,
			String moralLesson, String relatedObjects, String problem,
			String risingAction, String solution, String climax,
			String initSettings, String initTime) {
		ThemeID = themeID;
		InitActivity = initActivity;
		Lesson = lesson;
		MoralLesson = moralLesson;
		RelatedObjects = relatedObjects;
		Problem = problem;
		RisingAction = risingAction;
		Solution = solution;
		Climax = climax;
		InitSettings = initSettings;
		InitTime = initTime;
	}
	
	public IGTheme(String themeID, String lesson, String moralLesson, String relatedObjects,
			String initSettings, String initTime) {
		ThemeID = themeID;
		Lesson = lesson;
		MoralLesson = moralLesson;
		RelatedObjects = relatedObjects;
		InitSettings = initSettings;
		InitTime = initTime;
	}
	/*
	public String getThemeID() {
		return ThemeID;
	}

	public void setThemeID(String themeID) {
		ThemeID = themeID;
	}
	 */
	public String getInitActivity() {
		return InitActivity;
	}

	public void setInitActivity(String initActivity) {
		InitActivity = initActivity;
	}
	/*
	public String getLesson() {
		return Lesson;
	}

	public void setLesson(String lesson) {
		Lesson = lesson;
	}
	*/
	public String getMoralLesson() {
		return MoralLesson;
	}

	public void setMoralLesson(String moralLesson) {
		MoralLesson = moralLesson;
	}

	public String getRelatedObjects() {
		return RelatedObjects;
	}

	public void setRelatedObjects(String relatedObjects) {
		RelatedObjects = relatedObjects;
	}

	public String getProblem() {
		return Problem;
	}

	public void setProblem(String problem) {
		Problem = problem;
	}

	public String getRisingAction() {
		return RisingAction;
	}

	public void setRisingAction(String risingAction) {
		RisingAction = risingAction;
	}

	public String getSolution() {
		return Solution;
	}

	public void setSolution(String solution) {
		Solution = solution;
	}

	public String getClimax() {
		return Climax;
	}

	public void setClimax(String climax) {
		Climax = climax;
	}

	public String getInitSettings() {
		return InitSettings;
	}

	public void setInitSettings(String initSettings) {
		InitSettings = initSettings;
	}

	public String getInitTime() {
		return InitTime;
	}

	public void setInitTime(String initTime) {
		InitTime = initTime;
	}

	public DBLexicalObject getLessonObject() {
		return lessonObject;
	}
	
	public void setLessonObject(DBLexicalObject lesson) {
		this.lessonObject = lesson;
	}
	
	//added
	public String getID() {
		return ThemeID;
	}

	public void setID(String themeID) {
		ThemeID = themeID;
	}
	
	public String getString() {
		return Lesson;
	}

	public void setString(String lesson) {
		Lesson = lesson;
	}
}
