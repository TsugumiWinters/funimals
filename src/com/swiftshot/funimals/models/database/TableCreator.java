package com.swiftshot.funimals.models.database;

public class TableCreator {
	public static final String TABLE_STORY_FILES = "tableStoryFiles";
	public static final String TABLE_SAVED_STICKERS = "tableSavedStickers";
	public static final String TABLE_USER_INFORMATION = "tableUserInformation";
	
	public static final String TABLE_CONCEPT_MAPPER = "tableConceptMapper";
	public static final String TABLE_WORDS = "tableWords";
	public static final String TABLE_ADJECTIVES = "tableAdjectives";
	public static final String TABLE_CONJUNCTIONS = "tableConjunctions";
	public static final String TABLE_NOUNS = "tableNouns";
	public static final String TABLE_ARTICLES = "tableArticles";
	public static final String TABLE_PRONOUNS = "tablePronouns";
	public static final String TABLE_ADVERBS = "tableAdverbs";
	public static final String TABLE_VERBS = "tableVerbs";
	public static final String TABLE_PREPOSITIONS = "tablePrepositions";
	
	public static final String TABLE_ONTOLOGY = "tableOntology";
	public static final String TABLE_CONCEPTS = "tableConcepts";
	public static final String TABLE_SEMANTIC_RELATION_RULES = "tableSemanticRelationRules";
	
	public static final String TABLE_STORY_PLOT_TRACKER = "tableStoryPlotTracker";
	public static final String TABLE_AUTHOR_GOALS = "tableAuthorGoals";
	public static final String TABLE_CHARACTER_GOALS = "tableCharacterGoals";
	
	public static final String TABLE_IGOBJECTS = "tableIGObjects";
	public static final String TABLE_BACKGROUNDS = "tableBackgrounds";
	public static final String TABLE_THEMEOBJECT_RELATION = "tableThemeObjectRelation";
	public static final String TABLE_BACKGROUNDTHEME_RELATION = "tableBackgroundThemeRelation";
	public static final String TABLE_BACKGROUNDCHARACTER_RELATION = "tableBackgroundCharacterRelation";
	public static final String TABLE_IGCHARACTERS = "tableIGCharacters";
	public static final String TABLE_THEMES = "tableThemes";
	public static final String TABLE_THEMECLIMAX_RELATION = "tableThemeClimaxRelation";
	public static final String TABLE_CLIMAX = "tableClimax";
	public static final String TABLE_THEMETIME_RELATION = "tableThemeTimeRelation";
	public static final String TABLE_THEMESETTING_RELATION = "tableThemeSettingRelation";
	public static final String TABLE_TIME = "tableTime";
	public static final String TABLE_SETTINGS = "tableSettings";
	
	
	public static final String CREATE_STORY_FILES =
			"CREATE TABLE " + TABLE_STORY_FILES + "(" +
			"StoryID INTEGER PRIMARY KEY, " +
			"Username TEXT NOT NULL, " +
			"StoryTitle TEXT NOT NULL, " +
			"FilePath TEXT NOT NULL, " +
			"FOREIGN KEY(Username) REFERENCES " + TABLE_USER_INFORMATION + "(Username) " + 
			")";
	
	public static final String CREATE_USER_INFORMATION =
			"CREATE TABLE " + TABLE_USER_INFORMATION + "(" +
			"Username TEXT PRIMARY KEY NOT NULL," +
			"Age INTEGER NOT NULL" +
			"Grade INTEGER NOT NULL" + 
			")";
	
	public static final String CREATE_CONCEPT_MAPPER =
			"CREATE TABLE " + TABLE_CONCEPT_MAPPER + "(" +
			"ConceptID TEXT PRIMARY KEY, " +
			"AgeSixWordID TEXT NOT NULL, " +
			"AgeSevenWordID TEXT NOT NULL, " +
			"AgeEightWordID TEXT NOT NULL, " +
			"PartOfSpeech TEXT NOT NULL, " +
			"Word TEXT NOT NULL, " +
			"FOREIGN KEY(AgeSixWordID) REFERENCES " + TABLE_WORDS + 
				"(WordID), " +
			"FOREIGN KEY(AgeSevenWordID) REFERENCES " + TABLE_WORDS +
				"(WordID), " +
			"FOREIGN KEY(AgeEightWordID) REFERENCES " + TABLE_WORDS +
				"(WordID)" +
			")";
	
	public static final String CREATE_WORDS =
			"CREATE TABLE " + TABLE_WORDS + "(" +
			"WordID TEXT PRIMARY KEY, " +
			"Word TEXT NOT NULL," +
			"PartOfSpeech TEXT NOT NULL, " +
			"Classification TEXT" +
			")";
	
	public static final String CREATE_ADJECTIVES =
			"CREATE TABLE " + TABLE_ADJECTIVES + "(" +
			"WordID TEXT PRIMARY KEY, " +
			"Word TEXT NOT NULL" +
			")";
	
	public static final String CREATE_ARTICLES =
			"CREATE TABLE " + TABLE_ARTICLES + "(" +
			"WordID TEXT PRIMARY KEY, " +
			"Word TEXT NOT NULL" +
			")";
	
	public static final String CREATE_CONJUNCTIONS =
			"CREATE TABLE " + TABLE_CONJUNCTIONS + "(" +
			"WordID TEXT PRIMARY KEY, " +
			"Word TEXT NOT NULL" +
			")";
	
	public static final String CREATE_NOUNS = 
			"CREATE TABLE " + TABLE_NOUNS + "(" +
			"WordID TEXT PRIMARY KEY, " +
			"Classification TEXT NOT NULL, " +
			"isSingular INTEGER NOT NULL," +
			"Word TEXT NOT NULL" + 
			")";
	
	public static final String CREATE_PRONOUNS =
			"CREATE TABLE " + TABLE_PRONOUNS + "(" +
			"WordID TEXT PRIMARY KEY, " +
			"Type TEXT NOT NULL, " +
			"Gender TEXT NOT NULL, " +
			"WORD TEXT NOT NULL" +
			")";	
	
	public static final String CREATE_ADVERBS = 
			"CREATE TABLE " +  TABLE_ADVERBS + "(" +
			"WordID TEXT PRIMARY KEY, " +
			"Type TEXT NOT NULL, " +
			"Word TEXT NOT NULL" +
			")";
	
	public static final String CREATE_VERBS = 
			"CREATE TABLE " + TABLE_VERBS + "(" +
			"WordID TEXT PRIMARY KEY, " +
			"Word TEXT NOT NULL" +
			")";
	
	public static final String CREATE_PREPOSITIONS = 
			"CREATE TABLE " + TABLE_PREPOSITIONS + "(" +
			"WordID TEXT PRIMARY KEY, " +
			"Type TEXT NOT NULL, " +
			"Word TEXT NOT NULL" +
			")";
	

	
	public static final String CREATE_SEMANTIC_RELATION_RULES = 
			"CREATE TABLE " + TABLE_SEMANTIC_RELATION_RULES + "(" +
			"SemanticRelation TEXT NOT NULL, " +
			"Action TEXT NOT NULL, " +
			"Agens TEXT NOT NULL, " +
			"Patiens TEXT NOT NULL, " +
			"Target TEXT NOT NULL, " +
			"Instrument TEXT NOT NULL, " +
			"Notes TEXT NOT NULL" +
			")";	
	
	public static final String CREATE_ONTOLOGY = 
			"CREATE TABLE " + TABLE_ONTOLOGY + "(" +
			"OntologyID1 TEXT, " +
			"SemanticRelation TEXT, " +
			"OntologyID2 TEXT, " +
			"Category TEXT" +
			")";

	public static final String CREATE_STORY_PLOT_TRACKER =
			"CREATE TABLE " + TABLE_STORY_PLOT_TRACKER + "(" +
					"PlotID TEXT PRIMARY KEY, " +
					"Name TEXT NOT NULL, " +
					"Stage TEXT NOT NULL, " +
					"AuthorGoals TEXT NOT NULL" +
					")";
	
	public static final String CREATE_AUTHOR_GOALS =
			"CREATE TABLE " + TABLE_AUTHOR_GOALS + "(" +
					"AuthorGoalID TEXT PRIMARY KEY, " +
					"Name TEXT NOT NULL, " +
					"Goal TEXT NOT NULL, " +
					"Consequence TEXT NOT NULL" +
					")";
	
	public static final String CREATE_CHARACTER_GOALS = 
			"CREATE TABLE " + TABLE_CHARACTER_GOALS + "(" +
					"CharacterGoalID TEXT PRIMARY KEY, " +
					"Name TEXT NOT NULL, " +
					"Action TEXT NOT NULL, " +
					"Agens TEXT NOT NULL, " +
					"Patiens TEXT NOT NULL, " +
					"isNegated TEXT NOT NULL" +
					")";

	public static final String CREATE_IGOBJECTS = 
			"CREATE TABLE " + TABLE_IGOBJECTS + "(" +
			"IGObjectID TEXT PRIMARY KEY,  " +
			"Name TEXT NOT NULL, " +
			"ImageFilePath TEXT NOT NULL, " +
			"ObjectWord TEXT NOT NULL" +
			")";
	
	public static final String CREATE_BACKGROUNDS = 
			"CREATE TABLE " + TABLE_BACKGROUNDS + "(" +
			"BackgroundID TEXT PRIMARY KEY,  " +
			"Name TEXT NOT NULL, " +
			"Theme TEXT NOT NULL, " +
			"BackgroundWord TEXT NOT NULL, " +
			"ObjectsIncluded TEXT NOT NULL, " +
			"CharactersIncluded TEXT NOT NULL, " +
			"RequiredRole TEXT NOT NULL, " +
			"ImageFilePath TEXT NOT NULL" +
			")";

	
	public static final String CREATE_IGCHARACTERS = 
			"CREATE TABLE " + TABLE_IGCHARACTERS + "(" +
			"CharacterID TEXT PRIMARY KEY, " +
			"Name TEXT NOT NULL, " +
			"Gender TEXT NOT NULL, " +
			"AnimalType TEXT NOT NULL, " +
			"Role TEXT NOT NULL, " +
			"MotherCharacterID INTEGER NOT NULL, " +
			"FatherCharacterID INTEGER NOT NULL, " +
			"ImageFilePath TEXT NOT NULL, " +
			"FOREIGN KEY(Role) REFERENCES " + TABLE_WORDS +
				"(WordID), " +
			"FOREIGN KEY(MotherCharacterID) REFERENCES " + TABLE_IGCHARACTERS +
				"(CharacterID), " +
			"FOREIGN KEY(FatherCharacterID) REFERENCES " + TABLE_IGCHARACTERS +
				"(CharacterID)" +
			")";
	
	public static final String CREATE_CONCEPTS =
			"CREATE TABLE " + TABLE_CONCEPTS + "(" +
					"OntologyID TEXT PRIMARY KEY,  " +
					"ElementID TEXT NOT NULL, " +
					"Concept TEXT NOT NULL" +
					")";
	
	public static final String CREATE_THEMES = 
			"CREATE TABLE " + TABLE_THEMES + "(" +
					"ThemeID TEXT PRIMARY KEY,  " +
					"InitActivity TEXT NOT NULL, " +
					"Lesson TEXT NOT NULL," +
					"MoralLesson TEXT NOT NULL," +
					"RelatedObjects TEXT NOT NULL," +
					"Problem TEXT NOT NULL," +
					"RisingAction TEXT NOT NULL," +
					"Solution TEXT NOT NULL," +
					"Climax TEXT NOT NULL," +
					"InitSettings TEXT NOT NULL," +
					"InitTime TEXT NOT NULL" +
					")";
	
}
