package database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.Vector;

import pbmain.PBException;
import pbmain.util.Parser;
import pbmain.util.ParserException;
import storyplanner.StoryPlannerException;
import storyplanner.ontology.OntologyException;
import storyplanner.ontology.component.OntologyTree;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.picturebooks.funimals.ApplicationContextProvider;

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
import database_entities.SavedSticker;
import database_entities.SemanticRelationRule;
import database_entities.StoryFile;
import database_entities.StoryPlotTracker;
import database_entities.UserInformation;
import database_entities.Verb;
import database_entities.Word;

public class DatabaseHelper extends SQLiteOpenHelper {

	//The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.picturebooks.funimals/databases/";

    private static String DB_NAME = "funimals.db";
 
    private SQLiteDatabase database; 
 
    //private final Context myContext;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DatabaseHelper(Context context) {
    	super(ApplicationContextProvider.getContext(), DB_NAME, null, 1);
    	//super(context, DB_NAME, null, 1);
        //this.myContext = context;
    }	
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
    		System.out.println("Database already exist.");
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
        	System.out.println("Preparing to copy database.");
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
    			e.printStackTrace();
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		System.out.println("Checking database.");
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
 
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
    		System.out.println("Database doesn't exist yet");
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    public void copyDataBase() throws IOException{
 
    	System.out.println("Copying database.");
    	//Open your local db as the input stream
    	InputStream myInput = ApplicationContextProvider.getContext().getAssets().open(DB_NAME); //changed from myContext
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
 
    }
 /*
    @Override
	public synchronized void close() {
 
    	    if(database != null)
    		    database.close();
 
    	    super.close();
 
	}
 */
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
 
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
       // to you to create adapters for your views.
	

	public void addStoryFile(StoryFile storyFile) //same as saveFile
	{
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		
		cv.put("_id", storyFile.getStoryID());
		cv.put("Username", storyFile.getUsername());
		cv.put("Title", storyFile.getTitle());
		cv.put("Story", storyFile.getStory());
		cv.put("Background", storyFile.getBackground());
		database.insert(TableCreator.TABLE_STORY_FILES, null, cv);
		database.close();		
	}
	
	public void updateStoryFile(StoryFile storyFile) //same as saveFile
	{
		SQLiteDatabase database = this.getWritableDatabase();
		String updateQuery =
				"UPDATE " + TableCreator.TABLE_STORY_FILES +
				" SET Title = '" + storyFile.getTitle() + "'," +
				" Story = '" + storyFile.getStory() + "'," +
				" Background = '" + storyFile.getBackground() + "'" +
				" where _id = '" + storyFile.getStoryID() + "'";
		Log.d("StoryFilesSQL", "StoryFilesSQL " + updateQuery);
		database.execSQL(updateQuery);
		database.close();		
		
	}
	
	
	public StoryFile findStoryFileById(int storyID)
	{
		SQLiteDatabase database = this.getWritableDatabase();
		StoryFile sf = new StoryFile();
		
		String selectQuery =
				"SELECT * FROM " + TableCreator.TABLE_STORY_FILES + " where _id = '" + storyID + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				sf.setStoryID(cursor.getInt(0));
				sf.setUsername(cursor.getString(1));
				sf.setTitle(cursor.getString(2));
				sf.setStory(cursor.getString(3));
				sf.setBackground(cursor.getString(4));
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return sf;
	}
	
	public StoryFile findStoryFileByTitle(String username, String title) {
		SQLiteDatabase database = this.getWritableDatabase();
		StoryFile sf = new StoryFile();
		
		String selectQuery =
				"SELECT * FROM " + TableCreator.TABLE_STORY_FILES +
				" WHERE Username = '" + username + "' AND Title = '" + title + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				sf.setStoryID(cursor.getInt(0));
				sf.setUsername(cursor.getString(1));
				sf.setTitle(cursor.getString(2));
				sf.setStory(cursor.getString(3));
				sf.setBackground(cursor.getString(4));
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return sf;
	}
	
	public int getMaxStoryId()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		int max=0;
		
		String selectQuery =
				"SELECT MAX(_id) FROM " + TableCreator.TABLE_STORY_FILES;
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				max = cursor.getInt(0);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return max;
	}
	
	public Vector<StoryFile> getStoryFiles()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<StoryFile> storyFiles = new Vector<StoryFile>();
		
		String selectQuery = "SELECT * FROM " + TableCreator.TABLE_STORY_FILES;
		Cursor cursor = database.rawQuery(selectQuery, null);

		
		if(cursor.moveToFirst()){
			do{
				StoryFile sf = new StoryFile();
				sf.setStoryID(cursor.getInt(0));
				sf.setUsername(cursor.getString(1));
				sf.setTitle(cursor.getString(2));
				sf.setStory(cursor.getString(3));
				sf.setBackground(cursor.getString(4));
				Log.d("StoryFiles", sf.getTitle());
				storyFiles.add(sf);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return storyFiles;
	}	
	
	public Vector<StoryFile> getStoryFilesById(String username)
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<StoryFile> storyFiles = new Vector<StoryFile>();
		
		String selectQuery = "SELECT * FROM " + TableCreator.TABLE_STORY_FILES + " where Username = '" + username + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);

		
		if(cursor.moveToFirst()){
			do{
				StoryFile sf = new StoryFile();
				sf.setStoryID(cursor.getInt(0));
				sf.setUsername(cursor.getString(1));
				sf.setTitle(cursor.getString(2));
				sf.setStory(cursor.getString(3));
				sf.setBackground(cursor.getString(4));
				Log.d("StoryFiles", sf.getTitle());
				storyFiles.add(sf);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return storyFiles;
	}
	
	/*public void InitializeStoryFiles()
	{
		addStoryFile(new StoryFile(0, "Vin", "Sample Story Title", "//filepath"));
		addStoryFile(new StoryFile(1, "Sab", "2nd Sample Story Title", "2nd //filepath"));
		addStoryFile(new StoryFile(2, "Dene", "2nd Sample Story Title", "2nd //filepath"));
		addStoryFile(new StoryFile(3, "Jeremy", "2nd Sample Story Title", "2nd //filepath"));
		addStoryFile(new StoryFile(4, "Jasmine", "2nd Sample Story Title", "2nd //filepath"));
	}*/
	
	public void addUserInformation(UserInformation info)
	{
		SQLiteDatabase Database = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		
		cv.put("_id", info.getUsername());
		cv.put("Age", info.getAge());
		cv.put("Grade", info.getGrade());
		
		Database.insert(TableCreator.TABLE_USER_INFORMATION, null, cv);
	
		Database.close();
	}
	
	public Vector<UserInformation> getUserInformation()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<UserInformation> infos = new Vector<UserInformation>();
		
		//String selectQuery = "SELECT * FROM " + TableCreator.TABLE_USER_INFORMATION;
		String selectQuery = "SELECT * FROM tableUserInformation";
		Cursor cursor = database.rawQuery(selectQuery, null);

		
		if(cursor.moveToFirst()){
			do{
				UserInformation info = new UserInformation();
				info.setUsername(cursor.getString(0));
				info.setAge(cursor.getInt(1));
				info.setGrade(cursor.getInt(2));
				infos.add(info);
				Log.d("UserInformation", info.getUsername());
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return infos;
	}
	
	/*public void InitializeUserInformation()
	{
		addUserInformation(new UserInformation("Vin", 8, 2));
		addUserInformation(new UserInformation("Sab", 8, 2));
		addUserInformation(new UserInformation("Dene", 7, 1));
		addUserInformation(new UserInformation("Jeremy", 7, 3));
		addUserInformation(new UserInformation("Jasmine", 6, 0));
	}*/
	
	public String findWordByID(String wordID)
	{
		SQLiteDatabase database = this.getWritableDatabase();
		String word = "";
		
		String selectQuery =
				"SELECT Word FROM tableWords where _id = '" + wordID + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				word = cursor.getString(0);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return word;
	}
	
	public String findPartOfSpeechByWord(String word)
	{
		SQLiteDatabase database = this.getWritableDatabase();
		String partOfSpeech = "";
		
		String selectQuery =
				"SELECT PartOfSpeech FROM tableWords where Word = '" + word + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				partOfSpeech = cursor.getString(0);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return partOfSpeech;
	}
	
	public String findDefinitionByWord(String word)
	{
		SQLiteDatabase database = this.getWritableDatabase();
		String definition = "";
		
		String selectQuery =
				"SELECT Definition FROM tableWordDefinitions where _id = '" + word + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				definition = cursor.getString(0);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return definition;
	}
	
	public void addWord(Word word)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("WordID", word.getID());
		cv.put("Word", word.getString());
		cv.put("PartOfSpeech", word.getPartOfSpeech());
		cv.put("Classification", word.getClassification());
		
		database.insert(TableCreator.TABLE_WORDS, null, cv);
	}
	
	public Vector<Word> getWords()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<Word> words = new Vector<Word>();
		
		String selectQuery = "SELECT * FROM tableWords";
		Cursor cursor = database.rawQuery(selectQuery, null);

		
		if(cursor.moveToFirst()){
			do{
				Word w = new Word();
				w.setID(cursor.getString(0));
				w.setString(cursor.getString(1));
				w.setPartOfSpeech(cursor.getString(2));
				w.setClassification(cursor.getString(3));
				Log.d("Words", w.getString());
				words.add(w);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return words;
	}
	
	public void InitializeWords()
	{
		addWord(new Word("00000001","happy","Adjective"));
		addWord(new Word("00000002","glad","Adjective"));
		addWord(new Word("00000003","merry","Adjective"));
		addWord(new Word("00000004","sad","Adjective"));
		addWord(new Word("00000005","lonely","Adjective"));
		addWord(new Word("00000006","upset","Adjective"));
		addWord(new Word("00000007","pretty","Adjective"));
		addWord(new Word("00000008","beautiful","Adjective"));
		addWord(new Word("00000009","cold","Adjective"));
		addWord(new Word("00000010","chilly","Adjective"));
		addWord(new Word("00000011","bad","Adjective"));
		addWord(new Word("00000012","naughty","Adjective"));
		addWord(new Word("00000013","kind","Adjective"));
		addWord(new Word("00000014","polite","Adjective"));
		addWord(new Word("00000015","courteous","Adjective"));
		addWord(new Word("00000016","cool","Adjective"));
		addWord(new Word("00000017","right","Adjective"));
		addWord(new Word("00000018","correct","Adjective"));
		addWord(new Word("00000019","exact","Adjective"));
		addWord(new Word("00000020","fast","Adjective"));
		addWord(new Word("00000021","quick","Adjective"));
		addWord(new Word("00000022","care","Noun", "Mass"));
		addWord(new Word("00000023","choice","Noun, Count"));
		addWord(new Word("00000024","piece","Noun","Count"));
		addWord(new Word("00000025","part","Noun","Count"));
		addWord(new Word("00000026","portion","Noun","Count"));
		addWord(new Word("00000027","know","Verb"));
		addWord(new Word("00000028","windy","Adjective"));
		addWord(new Word("00000029","rainy","Adjective"));
		addWord(new Word("00000030","sunny","Adjective"));
		addWord(new Word("00000031","good","Adjective"));
		addWord(new Word("00000032","afraid","Adjective"));
		addWord(new Word("00000033","afternoon","Noun","Count"));
		addWord(new Word("00000034","garden","Noun","Count"));
		addWord(new Word("00000035","time","Noun","Count"));
		addWord(new Word("00000036","bath","Noun","Count"));
		addWord(new Word("00000037","play","Verb"));
		addWord(new Word("00000038","round","Adjective"));
		addWord(new Word("00000039","his","Pronoun"));
		addWord(new Word("00000040","kick","Verb"));
		addWord(new Word("00000041","around","Adverb"));
		addWord(new Word("00000042","truck","Noun","Count"));
		addWord(new Word("00000043","pretend","Verb"));
		addWord(new Word("00000044","drive","Verb"));
		addWord(new Word("00000045","forest","Noun","Count"));
		addWord(new Word("00000047","forget","Verb"));
		addWord(new Word("00000048","call","Verb"));
		addWord(new Word("00000049","want","Verb"));
		addWord(new Word("00000050","decide","Verb"));
		addWord(new Word("00000051","hide","Verb"));
		addWord(new Word("00000052","tree","Noun","Count"));
		addWord(new Word("00000053","arm","Noun","Count"));
		addWord(new Word("00000054","start","Verb"));
		addWord(new Word("00000055","feel","Verb"));
		addWord(new Word("00000056","itchy","Adjective"));
		addWord(new Word("00000057","leg","Noun","Count"));
		addWord(new Word("00000058","please","Verb"));
		addWord(new Word("00000059","sure","Adjective"));
		addWord(new Word("00000060","find","Verb"));
		addWord(new Word("00000061","there","Pronoun"));
		addWord(new Word("00000062","wait","Verb"));
		addWord(new Word("00000063","scratch","Verb"));
		addWord(new Word("00000064","skin","Noun","Mass"));
		addWord(new Word("00000065","red","Adjective"));
		addWord(new Word("00000066","cry","Verb;Adjective"));
		addWord(new Word("00000067","carry","Verb"));
		addWord(new Word("00000068","bathroom","Noun","Count"));
		addWord(new Word("00000069","listen","Verb"));
		addWord(new Word("00000070","remove","Verb"));
		addWord(new Word("00000071","make","Verb"));
		addWord(new Word("00000072","bathtub","Noun","Count"));
		addWord(new Word("00000073","bubble","Noun","Count"));
		addWord(new Word("00000074","water","Noun","Mass"));
		addWord(new Word("00000075","give","Verb"));
		addWord(new Word("00000076","duck","Noun","Count"));
		addWord(new Word("00000077","nice","Adjective"));
		addWord(new Word("00000078","look","Verb"));
		addWord(new Word("00000079","stop","Verb"));
		addWord(new Word("00000080","it","Pronoun"));
		addWord(new Word("00000081","he","Pronoun"));
		addWord(new Word("00000082","they","Pronoun"));
		addWord(new Word("00000083","your","Pronoun"));
		addWord(new Word("00000084","she","Pronoun"));
		addWord(new Word("00000137","feet","Noun","Count"));
		addWord(new Word("00000138","soothe","Verb"));
		addWord(new Word("00000139","smell","Verb"));
		addWord(new Word("00000140","dirt","Noun","Mass"));
		addWord(new Word("00000141","itch","Noun","Count"));
		addWord(new Word("00000142","discomfort","Noun","Count"));
		addWord(new Word("00000143","reaction","Noun","Count"));
		addWord(new Word("00000144","dirty","Adjective"));
		addWord(new Word("00000145","comfort","Verb;Noun"));
		addWord(new Word("00000146","angry","Adjective"));
		addWord(new Word("00000157","soap","Noun","Count"));
		addWord(new Word("00000159","arm","Noun","Count"));
		addWord(new Word("00000160","leg","Noun","Count"));
		addWord(new Word("00000164","talk","Verb"));
		addWord(new Word("00000167","do","Verb"));
		addWord(new Word("00000171","experience","Verb"));
		addWord(new Word("00000174","be","Verb"));
		addWord(new Word("00000175","was","Verb"));
		addWord(new Word("00000176","being","Verb"));
		addWord(new Word("00000177","been","Verb"));
		addWord(new Word("00000178","see","Verb"));
		addWord(new Word("00000182","child","Noun","Count"));
		addWord(new Word("00000183","adult","Noun","Count"));
		addWord(new Word("00000184","policeman","Noun","Count"));
		addWord(new Word("00000185","teacher","Noun","Count"));
		addWord(new Word("00000186","vendor","Noun","Count"));
		addWord(new Word("00000187","dentist","Noun","Count"));
		addWord(new Word("00000188","nurse","Noun","Count"));
		addWord(new Word("00000189","salesman","Noun","Count"));
		addWord(new Word("00000190","mother","Noun","Count"));
		addWord(new Word("00000191","father","Noun","Count"));
		addWord(new Word("00000192","boy","Noun","Count"));
		addWord(new Word("00000193","girl","Noun","Count"));
		addWord(new Word("00000194","toy","Noun","Count"));
		addWord(new Word("00000195","tell","Verb"));
		addWord(new Word("00000196","react","Verb"));
		addWord(new Word("00000197","result","Verb"));
		addWord(new Word("00000198","fight","Verb"));
		addWord(new Word("00000199","hurt","Verb;Adjective"));
		addWord(new Word("00000200","scold","Verb"));
		addWord(new Word("00000201","guilt","Noun","Mass"));
		addWord(new Word("00000202","apology","Noun","Count"));
		addWord(new Word("00000203","forgive","Verb"));
		addWord(new Word("00000204","share","Verb"));
		addWord(new Word("00000205","fun","Adjective"));
		addWord(new Word("00000206","apologize","Verb"));
		addWord(new Word("00000207","insist","Verb"));
		addWord(new Word("00000208","rubber ducky","Noun","Count"));
		addWord(new Word("00000209","through","Preposition"));
		addWord(new Word("00000210","in","Preposition"));
		addWord(new Word("00000211","inside","Preposition"));
		addWord(new Word("00000212","forward","Adverb"));
		addWord(new Word("00000213","with","Preposition"));
		addWord(new Word("00000214","to","Preposition"));
		addWord(new Word("00000215","into","Preposition"));
		addWord(new Word("00000216","for","Preposition"));
		addWord(new Word("00000217","not","Adverb"));
		addWord(new Word("00000218","commendation","Adjective"));
		addWord(new Word("00000219","ask","Verb"));
		addWord(new Word("00000220","a","Article"));
		addWord(new Word("00000221","an","Article"));
		addWord(new Word("00000222","the","Article"));
		addWord(new Word("00000223","is","Verb"));
		addWord(new Word("00000224","are","Verb"));
		addWord(new Word("00000225","were","Verb"));
		addWord(new Word("00000226","am","Verb"));
		addWord(new Word("00000227","have","Verb"));
		addWord(new Word("00000228","has","Verb"));
		addWord(new Word("00000229","had","Verb"));
		addWord(new Word("00000230","will","Verb"));
		addWord(new Word("00000231","shall","Verb"));
		addWord(new Word("00000232","would","Verb"));
		addWord(new Word("00000233","should","Verb"));
		addWord(new Word("00000234","at","Preposition"));
		addWord(new Word("00000235","on","Preposition"));
		addWord(new Word("00000236","since","Preposition"));
		addWord(new Word("00000237","towards","Preposition"));
		addWord(new Word("00000238","shared","Verb"));
		addWord(new Word("00000239","sharing","Verb"));
		addWord(new Word("00000240","living room","Noun","Count"));
		addWord(new Word("00000241","bedroom","Noun","Count"));
		addWord(new Word("00000242","indoor","Adjective"));
		addWord(new Word("00000243","outdoor","Adjective"));
		addWord(new Word("00000244","one","Adjective;Noun;Pronoun"));
		addWord(new Word("00000245","day","Noun","Count"));
		addWord(new Word("00000246","after","Preposition"));
		addWord(new Word("00000247","school","Noun","Count"));
		addWord(new Word("00000248","morning","Noun","Count"));
		addWord(new Word("00000249","finish","Verb"));
		addWord(new Word("00000250","night","Noun","Count"));
		addWord(new Word("00000251","evening","Noun","Count"));
		addWord(new Word("00000252","bright","Adjective"));
		addWord(new Word("00000253","fair","Adjective"));
		addWord(new Word("00000254","warm","Adjective"));
		addWord(new Word("00000255","specific","Adjective"));
		addWord(new Word("00000256","generic","Adjective"));
		addWord(new Word("00000257","cause","Verb"));
		addWord(new Word("00000258","get","Verb"));
		addWord(new Word("00000259","of","Preposition"));
		addWord(new Word("00000260","near","Adverb"));
		addWord(new Word("00000261","use","Verb"));
		addWord(new Word("00000262","guilty","Adjective"));
		addWord(new Word("00000263","ball","Noun","Count"));
		addWord(new Word("00000264","become","Verb"));
		addWord(new Word("00000265","clean","Adjective"));
		addWord(new Word("00000266","clinic","Noun","Count"));
		addWord(new Word("00000267","mall","Noun","Count"));
		addWord(new Word("00000268","market","Noun","Count"));
		addWord(new Word("00000269","playground","Noun","Count"));
		addWord(new Word("00000270","dining room","Noun","Count"));
		addWord(new Word("00000271","learn","Verb"));
		addWord(new Word("00000272","private","Adjective"));
		addWord(new Word("00000273","public","Adjective"));
		addWord(new Word("00000274","alarm ","Noun","Count"));
		addWord(new Word("00000275","clock","Noun","Count"));
		addWord(new Word("00000276","apple","Noun","Count"));
		addWord(new Word("00000277","backpack","Noun","Count"));
		addWord(new Word("00000278","banana","Noun","Count"));
		addWord(new Word("00000279","beach","Noun","Count"));
		addWord(new Word("00000280","book","Noun","Count"));
		addWord(new Word("00000281","brocolli","Noun","Count"));
		addWord(new Word("00000282","brush","Noun;Verb","Count"));
		addWord(new Word("00000283","cake","Noun","Count"));
		addWord(new Word("00000284","candy","Noun","Count"));
		addWord(new Word("00000285","carrot","Noun","Count"));
		addWord(new Word("00000286","chair","Noun","Count"));
		addWord(new Word("00000287","doll","Noun","Count"));
		addWord(new Word("00000288","fried","Adjective"));
		addWord(new Word("00000289","chicken","Noun","Count"));
		addWord(new Word("00000290","glass","Noun","Count"));
		addWord(new Word("00000291","bread","Noun","Count"));
		addWord(new Word("00000292","lamp","Noun","Count"));
		addWord(new Word("00000293","pillow","Noun","Count"));
		addWord(new Word("00000294","salt","Noun","Mass"));
		addWord(new Word("00000295","pepper","Noun","Count"));
		addWord(new Word("00000296","seesaw","Noun","Count"));
		addWord(new Word("00000297","stethoscope","Noun","Count"));
		addWord(new Word("00000298","swing","Noun","Count"));
		addWord(new Word("00000299","set","Noun","Count"));
		addWord(new Word("00000300","tea","Noun","Mass"));
		addWord(new Word("00000301","television","Noun","Count"));
		addWord(new Word("00000302","thermometer","Noun","Count"));
		addWord(new Word("00000303","toothpaste","Noun","Mass"));
		addWord(new Word("00000304","toothbrush","Noun","Count"));
		addWord(new Word("00000305","blocks","Noun","Count"));
		addWord(new Word("00000306","car","Noun","Count"));
		addWord(new Word("00000307","horse","Noun","Count"));
		addWord(new Word("00000308","tricycle","Noun","Count"));
		addWord(new Word("00000309","wallet","Noun","Count"));
		addWord(new Word("00000310","weighing scale","Noun","Count"));
		addWord(new Word("00000312","spaghetti","Noun","Mass"));
		addWord(new Word("00000313","and","Conjunction"));
		addWord(new Word("00000331","him","Pronoun"));
		addWord(new Word("00000332","her","Pronoun"));
		addWord(new Word("00000333","them","Pronoun"));
		addWord(new Word("00000334","tooth","Noun","Count"));
		addWord(new Word("00000335","careful","Adjective"));
		addWord(new Word("00000336","eat","Verb"));
		addWord(new Word("00000337","brave","Adjective"));
		addWord(new Word("00000338","sleep","Verb"));
		addWord(new Word("00000339","early","Adjective"));
		addWord(new Word("00000340","honest","Adjective"));
		addWord(new Word("00000341","permission","Noun","Count"));
		addWord(new Word("00000342","neat","Adjective"));
		addWord(new Word("00000343","healthy","Adjective"));
		addWord(new Word("00000344","food","Noun","Count"));
		addWord(new Word("00000345","location","Noun","Count"));
		addWord(new Word("00000346","take","Verb"));
		addWord(new Word("00000347","cleanliness","Noun","Mass"));
		addWord(new Word("00000348","irritated","Adjective"));
		addWord(new Word("00000349","irritate","Verb"));
		addWord(new Word("00000350","scared","Adjective"));
		addWord(new Word("00000351","run","Verb"));
		addWord(new Word("00000352","away","Adverb"));
		addWord(new Word("00000353","come","Verb"));
		addWord(new Word("00000354","out","Adverb"));
		addWord(new Word("00000355","sorry","Adjective"));
		addWord(new Word("00000356","up","Adverb"));
		addWord(new Word("00000357","breakable","Adjective"));
		addWord(new Word("00000358","object","Noun","Count"));
		addWord(new Word("00000359","break","Verb"));
		addWord(new Word("00000360","prevent","Verb"));
		addWord(new Word("00000361","accident","Noun","Mass"));
		addWord(new Word("00000362","can","Verb"));
		addWord(new Word("00000363","from","Preposition"));
		addWord(new Word("00000364","remind","Verb"));
		addWord(new Word("00000365","help","Verb"));
		addWord(new Word("00000366","mess","Noun","Count"));
		addWord(new Word("00000367","problem","Noun","Count"));
		addWord(new Word("00000368","lost","Adjective"));
		addWord(new Word("00000369","lesson","Noun","Count"));
		addWord(new Word("00000370","messy","Adjective"));
		addWord(new Word("00000371","scatter","Verb"));
		addWord(new Word("00000372","create","Verb"));
		addWord(new Word("00000373","disorder","Noun","Count"));
		addWord(new Word("00000374","obey","Verb"));
		addWord(new Word("00000375","continue","Verb"));
		addWord(new Word("00000376","too","Adverb"));
		addWord(new Word("00000377","much","Adjective;Adverb"));
		addWord(new Word("00000378","like","Verb"));
		addWord(new Word("00000379","consequence","Noun","Count"));
		addWord(new Word("00000380","prepare","Verb"));
		addWord(new Word("00000381","meal","Noun","Count"));
		addWord(new Word("00000382","spare","Verb"));
		addWord(new Word("00000383","thank","Verb"));
		addWord(new Word("00000384","before","Preposition"));
		addWord(new Word("00000385","mealtime","Noun","Count"));
		addWord(new Word("00000386","junk food","Noun","Count"));
		addWord(new Word("00000387","full","Adjective"));
		addWord(new Word("00000388","stomach","Noun","Count"));
		addWord(new Word("00000389","lose","Verb"));
		addWord(new Word("00000390","appetite","Noun","Count"));
		addWord(new Word("00000391","skip","Verb"));
		addWord(new Word("00000392","stomachache","Noun","Count"));
		addWord(new Word("00000393","solution","Noun","Count"));
		addWord(new Word("00000394","benefit","Noun","Count"));
		addWord(new Word("00000395","candy","Noun","Count"));
		addWord(new Word("00000396","sweet","Adjective"));
		addWord(new Word("00000397","toothache","Noun","Count"));
		addWord(new Word("00000398","pain","Noun","Count"));
		addWord(new Word("00000399","medicine","Noun","Count"));
		addWord(new Word("00000400","heal","Verb"));
		addWord(new Word("00000401","better","Adjective"));
		addWord(new Word("00000402","fruit","Noun","Count"));
		addWord(new Word("00000403","vegetable","Noun","Count"));
		addWord(new Word("00000404","strong","Adjective"));
		addWord(new Word("00000405","next","Adjective"));
		addWord(new Word("00000406","sweet","Noun","Count"));
		addWord(new Word("00000407","cavity","Noun","Count"));
		addWord(new Word("00000408","chip","Noun","Count"));
		addWord(new Word("00000409","strength","Noun","Count"));
		addWord(new Word("00000410","watch","Verb"));
		addWord(new Word("00000411","late","Adjective;Adverb"));
		addWord(new Word("00000412","headache","Noun","Count"));
		addWord(new Word("00000413","dizzy","Adjective"));
		addWord(new Word("00000414","rest","Noun;Verb","Count"));
		addWord(new Word("00000415","throw","Verb"));
		addWord(new Word("00000416","dizziness","Noun","Mass"));
		addWord(new Word("00000417","save","Verb"));
		addWord(new Word("00000418","go","Verb"));
		addWord(new Word("00000419","bed","Noun","Count"));
		addWord(new Word("00000420","seek","Verb"));
		addWord(new Word("00000421","leave","Verb"));
		addWord(new Word("00000422","introduce","Verb"));
		addWord(new Word("00000423","class","Noun","Count"));
		addWord(new Word("00000424","introduction","Noun","Count"));
		addWord(new Word("00000425","friendship","Noun","Count"));
		addWord(new Word("00000426","friend","Noun","Count"));
		addWord(new Word("00000427","activity","Noun","Count"));
		addWord(new Word("00000428","emotion","Noun","Count"));
		addWord(new Word("00000429","say","Verb"));
		addWord(new Word("00000430","goodbye","Noun","Count"));
		addWord(new Word("00000431","virtue","Noun","Count"));
		addWord(new Word("00000432","admirable","Adjective"));
		addWord(new Word("00000433","smile","Verb;Noun"));
		addWord(new Word("00000434","shy","Adjective"));
		addWord(new Word("00000435","others","Pronoun"));
		addWord(new Word("00000436","new","Adjective"));
		addWord(new Word("00000437","playmate","Noun","Count"));
		addWord(new Word("00000438","game","Noun","Count"));
		addWord(new Word("00000439","switch","Verb;Noun"));
		addWord(new Word("00000440","off","Adverb"));
		addWord(new Word("00000441","light","Noun","Count"));
		addWord(new Word("00000442","dark","Adjective"));
		addWord(new Word("00000443","sing","Verb"));
		addWord(new Word("00000444","lullaby","Noun","Count"));
		addWord(new Word("00000445","story","Noun","Count"));
		addWord(new Word("00000446","home","Noun","Count"));
		addWord(new Word("00000447","check","Verb"));
		addWord(new Word("00000448","positive","Adjective"));
		addWord(new Word("00000449","reward","Noun;Verb","Count"));
		addWord(new Word("00000451","need","Verb"));
		addWord(new Word("00000452","sick","Adjective"));
		addWord(new Word("00000453","nervous","Adjective"));
		addWord(new Word("00000454","hear","Verb"));
		addWord(new Word("00000455","heartbeat","Noun","Count"));
		addWord(new Word("00000456","temperature","Noun","Count"));
		addWord(new Word("00000457","ice cream","Noun","Mass"));
		addWord(new Word("00000458","classroom","Noun","Count"));
		addWord(new Word("00000459","left","Verb;Noun"));
		addWord(new Word("00000460","without","Preposition"));
		addWord(new Word("00000461","buy","Verb"));
		addWord(new Word("00000462","toy","Noun","Count"));
		addWord(new Word("00000463","store","Noun;Verb","Count"));
		addWord(new Word("00000464","confectionary","Noun","Count"));
		addWord(new Word("00000465","stand","Verb;Noun"));
		addWord(new Word("00000466","search","Noun","Count"));
		addWord(new Word("00000467","feeling","Noun","Count"));
		addWord(new Word("00000468","worried","Adjective"));
		addWord(new Word("00000469","important","Adjective"));
		addWord(new Word("00000471","method","Noun","Count"));
		addWord(new Word("00000472","destruction","Noun","Mass"));
		addWord(new Word("00000473","destroyed","Adjective"));
		addWord(new Word("00000474","broken","Adjective"));
		addWord(new Word("00000475","careless","Adjective"));
		addWord(new Word("00000476","punishment","Noun","Count"));
		addWord(new Word("00000477","punished","Adjective"));
		addWord(new Word("00000478","allow","Verb"));
		addWord(new Word("00000479","today","Noun;Adverb","Count"));
		addWord(new Word("00000480","grounded","Noun;Verb"));
		addWord(new Word("00000481","room","Noun","Count"));
		addWord(new Word("00000482","lie","Verb;Noun"));
		addWord(new Word("00000483","salty","Adjective"));
		addWord(new Word("00000484","complain","Verb"));
		addWord(new Word("00000485","grumble","Verb"));
		addWord(new Word("00000486","scary","Adjective"));
		addWord(new Word("00000487","surprised","Adjective"));
		addWord(new Word("00000488","shocked","Adjective"));
		addWord(new Word("00000489","aside","Adverb"));
		addWord(new Word("00000490","food","Noun","Count"));
		addWord(new Word("00000491","bedtime","Adjective"));
		addWord(new Word("00000492","too much","Adverb"));
		addWord(new Word("00000493","comforted","Adjective"));
		addWord(new Word("00000494","allowed","Adjective"));
		addWord(new Word("00000495","stay put","Verb"));
		addWord(new Word("00000496","put","Verb"));
		addWord(new Word("00000497","if","Conjunction"));
		addWord(new Word("00000498","everything","Pronoun"));
		addWord(new Word("00000499","okay","Adjective"));
		addWord(new Word("00000500","how","Adverb"));
		addWord(new Word("00000501","was","Verb"));
		addWord(new Word("00000502","delicious","Adjective"));
		addWord(new Word("00000503","yummy","Adjective"));
		addWord(new Word("00000504","delightful","Adjective"));
		addWord(new Word("00000506","green","Adjective"));
		addWord(new Word("00000507","yellow","Adjective"));
		addWord(new Word("00000508","big","Adjective"));
		addWord(new Word("00000509","large","Adjective"));
		addWord(new Word("00000510","huge","Adjective"));
		addWord(new Word("00000511","small","Adjective"));
		addWord(new Word("00000512","little","Adjective"));
		addWord(new Word("00000513","fragrant","Adjective"));
		addWord(new Word("00000514","slippery","Adjective"));
		addWord(new Word("00000515","soft","Adjective"));
		addWord(new Word("00000516","fluffy","Adjective"));
		addWord(new Word("00000517","colorful","Adjective"));
	}
	
	public void addVerb(Verb verb)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("WordID", verb.getWordID());
		cv.put("Word", verb.getWord());
		
		database.insert(TableCreator.TABLE_VERBS, null, cv);
	}
	
	public Vector<Verb> getVerbs()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<Verb> verbs = new Vector<Verb>();
		
		String selectQuery = "SELECT * FROM tableVerbs";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				Verb v = new Verb();
				v.setWordID(cursor.getString(0));
				v.setWord(cursor.getString(1));
				Log.d("Verbs", v.getWord());
				verbs.add(v);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return verbs;
	}
	
	public void InitializeVerbs()
	{
		addVerb(new Verb("WORD0012","know"));
		addVerb(new Verb("WORD0022","play"));
		addVerb(new Verb("WORD0025","kick"));
		addVerb(new Verb("WORD0028","pretend"));
		addVerb(new Verb("WORD0029","drive"));
		addVerb(new Verb("WORD0032","forget"));
		addVerb(new Verb("WORD0033","call"));
		addVerb(new Verb("WORD0034","want"));
		addVerb(new Verb("WORD0035","decide"));
		addVerb(new Verb("WORD0036","hide"));
		addVerb(new Verb("WORD0039","start"));
		addVerb(new Verb("WORD0040","feel"));
		addVerb(new Verb("WORD0043","please"));
		addVerb(new Verb("WORD0045","find"));
		addVerb(new Verb("WORD0047","wait"));
		addVerb(new Verb("WORD0048","scratch"));
		addVerb(new Verb("WORD0051","cry"));
		addVerb(new Verb("WORD0052","carry"));
		addVerb(new Verb("WORD0054","listen"));
		addVerb(new Verb("WORD0055","remove"));
		addVerb(new Verb("WORD0056","make"));
		addVerb(new Verb("WORD0060","give"));
		addVerb(new Verb("WORD0063","look"));
		addVerb(new Verb("WORD0064","stop"));
		addVerb(new Verb("WORD0071","soothe"));
		addVerb(new Verb("WORD0072","smell"));
		addVerb(new Verb("WORD0078","comfort"));
		addVerb(new Verb("WORD0080","talk"));
		addVerb(new Verb("WORD0081","do"));
		addVerb(new Verb("WORD0082","experience"));
		addVerb(new Verb("WORD0083","be"));
		addVerb(new Verb("WORD0084","see"));
		addVerb(new Verb("WORD0098","tell"));
		addVerb(new Verb("WORD0099","react"));
		addVerb(new Verb("WORD0100","result"));
		addVerb(new Verb("WORD0101","fight"));
		addVerb(new Verb("WORD0102","hurt"));
		addVerb(new Verb("WORD0103","scold"));
		addVerb(new Verb("WORD0106","forgive"));
		addVerb(new Verb("WORD0107","share"));
		addVerb(new Verb("WORD0109","apologize"));
		addVerb(new Verb("WORD0110","insist"));
		addVerb(new Verb("WORD0122","ask"));
		addVerb(new Verb("WORD0126","is"));
		addVerb(new Verb("WORD0127","are"));
		addVerb(new Verb("WORD0128","were"));
		addVerb(new Verb("WORD0129","am"));
		addVerb(new Verb("WORD0130","has"));
		addVerb(new Verb("WORD0131","have"));
		addVerb(new Verb("WORD0132","had"));
		addVerb(new Verb("WORD0133","will"));
		addVerb(new Verb("WORD0134","shall"));
		addVerb(new Verb("WORD0135","would"));
		addVerb(new Verb("WORD0136","should"));
		addVerb(new Verb("WORD0141","sharing"));
		addVerb(new Verb("WORD0151","finish"));
		addVerb(new Verb("WORD0161","cause"));
		addVerb(new Verb("WORD0162","get"));
		addVerb(new Verb("WORD0166","use"));
		addVerb(new Verb("WORD0169","become"));
		addVerb(new Verb("WORD0176","learn"));
		addVerb(new Verb("WORD0242","eat"));
		addVerb(new Verb("WORD0244","sleep"));
		addVerb(new Verb("WORD0252","take"));
		addVerb(new Verb("WORD0255","irritate"));
		addVerb(new Verb("WORD0257","run"));
		addVerb(new Verb("WORD0259","come"));
		addVerb(new Verb("WORD0265","break"));
		addVerb(new Verb("WORD0266","prevent"));
		addVerb(new Verb("WORD0268","can"));
		addVerb(new Verb("WORD0270","remind"));
		addVerb(new Verb("WORD0271","help"));
		addVerb(new Verb("WORD0277","scatter"));
		addVerb(new Verb("WORD0278","create"));
		addVerb(new Verb("WORD0280","obey"));
		addVerb(new Verb("WORD0281","continue"));
		addVerb(new Verb("WORD0284","like"));
		addVerb(new Verb("WORD0286","prepare"));
		addVerb(new Verb("WORD0288","spare"));
		addVerb(new Verb("WORD0289","thank"));
		addVerb(new Verb("WORD0295","lose"));
		addVerb(new Verb("WORD0297","skip"));
		addVerb(new Verb("WORD0306","heal"));
		addVerb(new Verb("WORD0316","watch"));
		addVerb(new Verb("WORD0321","throw"));
		addVerb(new Verb("WORD0323","save"));
		addVerb(new Verb("WORD0324","go"));
		addVerb(new Verb("WORD0326","seek"));
		addVerb(new Verb("WORD0327","leave"));
		addVerb(new Verb("WORD0328","introduce"));
		addVerb(new Verb("WORD0335","say"));
		addVerb(new Verb("WORD0349","sing"));
		addVerb(new Verb("WORD0353","check"));
		addVerb(new Verb("WORD0357","need"));
		addVerb(new Verb("WORD0360","hear"));
		addVerb(new Verb("WORD0365","left"));
		addVerb(new Verb("WORD0367","buy"));
		addVerb(new Verb("WORD0384","allow"));
		addVerb(new Verb("WORD0396","clean"));
		addVerb(new Verb("WORD0398","rest"));
		addVerb(new Verb("WORD0399","smile"));
		addVerb(new Verb("WORD0400","switch"));
		addVerb(new Verb("WORD0402","reward"));
		addVerb(new Verb("WORD0404","store"));
		addVerb(new Verb("WORD0405","stand"));
		addVerb(new Verb("WORD0406","search"));
		addVerb(new Verb("WORD0409","lie"));
		addVerb(new Verb("WORD0410","brush"));
		addVerb(new Verb("WORD0412","grumble"));
		addVerb(new Verb("WORD0422","stay"));
		addVerb(new Verb("WORD0423","put"));
		addVerb(new Verb("WORD0428","was"));
		addVerb(new Verb("WORD0439","set"));
	}
	
	public void addPronoun(Pronoun pronoun)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("WordID", pronoun.getWordID());
		cv.put("Type", pronoun.getType());
		cv.put("Gender", pronoun.getGender());	
		cv.put("Word", pronoun.getWord());		
		
		database.insert(TableCreator.TABLE_PRONOUNS, null, cv);
	}
	
	public Vector<Pronoun> getPronouns()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<Pronoun> pronouns = new Vector<Pronoun>();
		
		String selectQuery = "SELECT * FROM tablePronouns";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				Pronoun p = new Pronoun();
				p.setWordID(cursor.getString(0));
				p.setType(cursor.getString(1));
				p.setGender(cursor.getString(2));
				p.setWord(cursor.getString(3));
				Log.d("Pronouns", p.getWord());
				pronouns.add(p);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return pronouns;
	}
	
	private void InitializePronouns() {
		addPronoun(new Pronoun("WORD0024","","M","his"));
		addPronoun(new Pronoun("WORD0046","","","there"));
		addPronoun(new Pronoun("WORD0065","","","it"));
		addPronoun(new Pronoun("WORD0066","","M","he"));
		addPronoun(new Pronoun("WORD0067","","","they"));
		addPronoun(new Pronoun("WORD0068","","","your"));
		addPronoun(new Pronoun("WORD0069","","F","she"));
		addPronoun(new Pronoun("WORD0237","","M","him"));
		addPronoun(new Pronoun("WORD0238","","F","her"));
		addPronoun(new Pronoun("WORD0239","","","them"));
		addPronoun(new Pronoun("WORD0341","","","others"));
		addPronoun(new Pronoun("WORD0394","","","one"));
		addPronoun(new Pronoun("WORD0425","","","everything"));
	}
	
	public void addPreposition(Preposition preposition)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("WordID", preposition.getWordID());
		cv.put("Type", preposition.getType());	
		cv.put("Word", preposition.getWord());	
		
		database.insert(TableCreator.TABLE_PREPOSITIONS, null, cv);
	}
	
	public Vector<Preposition> getPrepositions()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<Preposition> prepositions = new Vector<Preposition>();
		
		String selectQuery = "SELECT * FROM tablePrepositions";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				Preposition p = new Preposition();
				p.setWordID(cursor.getString(0));
				p.setType(cursor.getString(1));
				p.setWord(cursor.getString(2));
				Log.d("Prepositions", p.getWord());
				prepositions.add(p);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return prepositions;
	}	

	public void InitializePrepositions()
	{
		addPreposition(new Preposition("0112","","through"));
		addPreposition(new Preposition("0113","location","in"));
		addPreposition(new Preposition("0114","","inside"));
		addPreposition(new Preposition("0116","","with"));
		addPreposition(new Preposition("0117","","to"));
		addPreposition(new Preposition("0118","","into"));
		addPreposition(new Preposition("0119","","for"));
		addPreposition(new Preposition("0137","location","at"));
		addPreposition(new Preposition("0138","location","on"));
		addPreposition(new Preposition("0139","","since"));
		addPreposition(new Preposition("0140","","towards"));
		addPreposition(new Preposition("0148","","after"));
		addPreposition(new Preposition("0163","","of"));
		addPreposition(new Preposition("0269","","from"));
		addPreposition(new Preposition("0290","","before"));
		addPreposition(new Preposition("0366","","without"));
	}
	
	public void addNoun(Noun noun)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("WordID", noun.getWordID());
		cv.put("Classification", noun.getClassification());
		cv.put("isSingular", noun.getIsSingular());
		cv.put("Word", noun.getWord());
		
		database.insert(TableCreator.TABLE_NOUNS, null, cv);
	}	
	
	public Vector<Noun> getNouns()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<Noun> nouns = new Vector<Noun>();
		
		String selectQuery = "SELECT * FROM tableNouns";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				Noun n = new Noun();
				n.setWordID(cursor.getString(0));
				n.setClassification(cursor.getString(1));
				n.setIsSingular(cursor.getString(2));
				n.setWord(cursor.getString(3));
				Log.d("Nouns", n.getWord());
				nouns.add(n);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return nouns;
	}
	
	public void InitializeNouns()
	{
		addNoun(new Noun("WORD0009","Mass","Yes","care"));
		addNoun(new Noun("WORD0010","Count","Yes","choice"));
		addNoun(new Noun("WORD0011","Count","Yes","piece"));
		addNoun(new Noun("WORD0018","Count","Yes","afternoon"));
		addNoun(new Noun("WORD0019","Count","Yes","garden"));
		addNoun(new Noun("WORD0020","Count","Yes","time"));
		addNoun(new Noun("WORD0021","Count","Yes","bath"));
		addNoun(new Noun("WORD0027","Count","Yes","truck"));
		addNoun(new Noun("WORD0030","Count","Yes","forest"));
		addNoun(new Noun("WORD0037","Count","No","tree"));
		addNoun(new Noun("WORD0038","Count","No","arm"));
		addNoun(new Noun("WORD0042","Count","No","leg"));
		addNoun(new Noun("WORD0049","Mass","No","skin"));
		addNoun(new Noun("WORD0053","Count","Yes","bathroom"));
		addNoun(new Noun("WORD0057","Count","Yes","bathtub"));
		addNoun(new Noun("WORD0058","Count","No","bubble"));
		addNoun(new Noun("WORD0059","Mass","No","water"));
		addNoun(new Noun("WORD0061","Count","Yes","duck"));
		addNoun(new Noun("WORD0070","Count","Yes","feet"));
		addNoun(new Noun("WORD0073","Mass","No","dirt"));
		addNoun(new Noun("WORD0074","Mass","Yes","itch"));
		addNoun(new Noun("WORD0075","Mass","No","discomfort"));
		addNoun(new Noun("WORD0076","Count","Yes","reaction"));
		addNoun(new Noun("WORD0085","Count","Yes","child"));
		addNoun(new Noun("WORD0086","Count","Yes","adult"));
		addNoun(new Noun("WORD0087","Count","Yes","policeman"));
		addNoun(new Noun("WORD0088","Count","Yes","teacher"));
		addNoun(new Noun("WORD0089","Count","Yes","vendor"));
		addNoun(new Noun("WORD0090","Count","Yes","dentist"));
		addNoun(new Noun("WORD0091","Count","Yes","nurse"));
		addNoun(new Noun("WORD0092","Count","Yes","salesman"));
		addNoun(new Noun("WORD0093","Count","Yes","mother"));
		addNoun(new Noun("WORD0094","Count","Yes","father"));
		addNoun(new Noun("WORD0095","Count","Yes","boy"));
		addNoun(new Noun("WORD0096","Count","Yes","girl"));
		addNoun(new Noun("WORD0097","Count","No","toy"));
		addNoun(new Noun("WORD0104","Mass","No","guilt"));
		addNoun(new Noun("WORD0105","Count","Yes","apology"));
		addNoun(new Noun("WORD0111","Count","Yes","rubber ducky"));
		addNoun(new Noun("WORD0142","Count","Yes","living room"));
		addNoun(new Noun("WORD0143","Count","Yes","bedroom"));
		addNoun(new Noun("WORD0147","Count","Yes","day"));
		addNoun(new Noun("WORD0149","Count","Yes","school"));
		addNoun(new Noun("WORD0150","Count","Yes","morning"));
		addNoun(new Noun("WORD0152","Count","Yes","night"));
		addNoun(new Noun("WORD0153","Count","Yes","evening"));
		addNoun(new Noun("WORD0165","Count","Yes","part"));
		addNoun(new Noun("WORD0168","Count","Yes","ball"));
		addNoun(new Noun("WORD0171","Count","Yes","clinic"));
		addNoun(new Noun("WORD0172","Count","Yes","mall"));
		addNoun(new Noun("WORD0173","Count","Yes","market"));
		addNoun(new Noun("WORD0174","Count","Yes","playground"));
		addNoun(new Noun("WORD0175","Count","Yes","dining room"));
		addNoun(new Noun("WORD0179","Count","Yes","alarm "));
		addNoun(new Noun("WORD0180","Count","Yes","clock"));
		addNoun(new Noun("WORD0181","Count","Yes","apple"));
		addNoun(new Noun("WORD0182","Count","Yes","backpack"));
		addNoun(new Noun("WORD0183","Count","No","banana"));
		addNoun(new Noun("WORD0184","Count","Yes","beach"));
		addNoun(new Noun("WORD0185","Count","Yes","book"));
		addNoun(new Noun("WORD0186","Count","Yes","brocollis"));
		addNoun(new Noun("WORD0187","Count","Yes","brush"));
		addNoun(new Noun("WORD0188","Count","Yes","cake"));
		addNoun(new Noun("WORD0189","Count","No","candy"));
		addNoun(new Noun("WORD0190","Count","No","carrot"));
		addNoun(new Noun("WORD0191","Count","Yes","chair"));
		addNoun(new Noun("WORD0192","Count","Yes","doll"));
		addNoun(new Noun("WORD0194","Count","Yes","chicken"));
		addNoun(new Noun("WORD0195","Count","Yes","glass"));
		addNoun(new Noun("WORD0196","Count","Yes","bread"));
		addNoun(new Noun("WORD0197","Count","Yes","lamp"));
		addNoun(new Noun("WORD0198","Count","Yes","pillow"));
		addNoun(new Noun("WORD0199","Count","Yes","salt"));
		addNoun(new Noun("WORD0200","Count","Yes","pepper"));
		addNoun(new Noun("WORD0201","Count","Yes","seesaw"));
		addNoun(new Noun("WORD0202","Count","Yes","stethoscope"));
		addNoun(new Noun("WORD0203","Count","Yes","swing"));
		addNoun(new Noun("WORD0204","Count","Yes","set"));
		addNoun(new Noun("WORD0205","Count","Yes","tea"));
		addNoun(new Noun("WORD0206","Count","Yes","television"));
		addNoun(new Noun("WORD0207","Count","Yes","thermometer"));
		addNoun(new Noun("WORD0208","Count","Yes","toothpaste "));
		addNoun(new Noun("WORD0209","Count","Yes","toothbrush"));
		addNoun(new Noun("WORD0210","Count","No","block"));
		addNoun(new Noun("WORD0211","Count","Yes","car"));
		addNoun(new Noun("WORD0212","Count","Yes","horse"));
		addNoun(new Noun("WORD0213","Count","Yes","tricycle"));
		addNoun(new Noun("WORD0214","Count","Yes","wallet"));
		addNoun(new Noun("WORD0215","Count","Yes","weighing scale"));
		addNoun(new Noun("WORD0216","Count","Yes","spaghetti"));
		addNoun(new Noun("WORD0219","Count","Yes","soap"));
		addNoun(new Noun("WORD0240","Count","No","tooth"));
		addNoun(new Noun("WORD0247","Count","Yes","permission"));
		addNoun(new Noun("WORD0250","Count","Yes","food"));
		addNoun(new Noun("WORD0251","Count","Yes","location"));
		addNoun(new Noun("WORD0253","Mass","Yes","cleanliness"));
		addNoun(new Noun("WORD0264","Count","Yes","object"));
		addNoun(new Noun("WORD0267","Count","Yes","accident"));
		addNoun(new Noun("WORD0272","Count","Yes","mess"));
		addNoun(new Noun("WORD0273","Count","Yes","problem"));
		addNoun(new Noun("WORD0275","Count","Yes","lesson"));
		addNoun(new Noun("WORD0279","Count","Yes","disorder"));
		addNoun(new Noun("WORD0285","Count","Yes","consequence"));
		addNoun(new Noun("WORD0287","Count","Yes","meal"));
		addNoun(new Noun("WORD0289","Count","Yes","junk food"));
		addNoun(new Noun("WORD0291","Count","Yes","mealtime"));
		addNoun(new Noun("WORD0294","Count","No","stomach"));
		addNoun(new Noun("WORD0296","Mass","No","appetite"));
		addNoun(new Noun("WORD0298","Count","Yes","stomachache"));
		addNoun(new Noun("WORD0299","Count","Yes","solution"));
		addNoun(new Noun("WORD0300","Count","Yes","benefit"));
		addNoun(new Noun("WORD0301","Count","Yes","candy"));
		addNoun(new Noun("WORD0303","Count","Yes","toothache"));
		addNoun(new Noun("WORD0304","Mass","No","pain"));
		addNoun(new Noun("WORD0305","Count","Yes","medicine"));
		addNoun(new Noun("WORD0308","Count","No","fruit"));
		addNoun(new Noun("WORD0309","Count","No","vegetable"));
		addNoun(new Noun("WORD0312","Count","No","sweet"));
		addNoun(new Noun("WORD0313","Count","Yes","cavity"));
		addNoun(new Noun("WORD0314","Count","No","chip"));
		addNoun(new Noun("WORD0315","Mass","No","strength"));
		addNoun(new Noun("WORD0318","Count","Yes","headache"));
		addNoun(new Noun("WORD0320","Count","Yes","rest"));
		addNoun(new Noun("WORD0322","Mass","No","dizziness"));
		addNoun(new Noun("WORD0325","Count","Yes","bed"));
		addNoun(new Noun("WORD0329","Count","Yes","class"));
		addNoun(new Noun("WORD0330","Count","Yes","introduction"));
		addNoun(new Noun("WORD0331","Count","Yes","friendship"));
		addNoun(new Noun("WORD0332","Count","No","friend"));
		addNoun(new Noun("WORD0333","Count","Yes","activity"));
		addNoun(new Noun("WORD0334","Count","Yes","emotion"));
		addNoun(new Noun("WORD0336","Count","Yes","goodbye"));
		addNoun(new Noun("WORD0337","Count","Yes","virtue"));
		addNoun(new Noun("WORD0339","Count","Yes","smile"));
		addNoun(new Noun("WORD0343","Count","No","playmate"));
		addNoun(new Noun("WORD0344","Count","No","game"));
		addNoun(new Noun("WORD0345","Count","Yes","switch"));
		addNoun(new Noun("WORD0347","Count","No","light"));
		addNoun(new Noun("WORD0350","Count","Yes","lullaby"));
		addNoun(new Noun("WORD0351","Count","Yes","story"));
		addNoun(new Noun("WORD0352","Count","Yes","home"));
		addNoun(new Noun("WORD0355","Count","Yes","reward"));
		addNoun(new Noun("WORD0361","Count","No","heartbeat"));
		addNoun(new Noun("WORD0362","Count","Yes","temperature"));
		addNoun(new Noun("WORD0363","Count","Yes","ice cream"));
		addNoun(new Noun("WORD0364","Count","Yes","classroom"));
		addNoun(new Noun("WORD0368","Count","Yes","toy"));
		addNoun(new Noun("WORD0369","Count","Yes","store"));
		addNoun(new Noun("WORD0370","Count","Yes","confectionary"));
		addNoun(new Noun("WORD0371","Count","Yes","stand"));
		addNoun(new Noun("WORD0372","Count","Yes","search"));
		addNoun(new Noun("WORD0373","Count","Yes","feeling"));
		addNoun(new Noun("WORD0377","Count","Yes","method"));
		addNoun(new Noun("WORD0378","Mass","No","destruction"));
		addNoun(new Noun("WORD0382","Count","Yes","punishment"));
		addNoun(new Noun("WORD0385","Count","No","today"));
		addNoun(new Noun("WORD0387","Count","Yes","room"));
		addNoun(new Noun("WORD0388","Count","Yes","lie"));
		addNoun(new Noun("WORD0390","Count","Yes","cry"));
		addNoun(new Noun("WORD0391","Count","No","comfort"));
		addNoun(new Noun("WORD0392","Count","Yes","experience"));
		addNoun(new Noun("WORD0395","Count","Yes","one"));
		addNoun(new Noun("WORD0403","Mass","No","left"));
		addNoun(new Noun("WORD0408","Count","Yes","ground"));
		addNoun(new Noun("WORD0409","Count","Yes","result"));
		addNoun(new Noun("WORD0416","Count","No","foods"));
		addNoun(new Noun("WORD0419","Mass","No","dark"));
	}
	
	public void addConjunction(Conjunction conjunction)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("WordID", conjunction.getWordID());
		cv.put("Word", conjunction.getWord());
		
		database.insert(TableCreator.TABLE_CONJUNCTIONS, null, cv);
	}
	public Vector<Conjunction> getConjunctions()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<Conjunction> conjunctions = new Vector<Conjunction>();
		
		String selectQuery = "SELECT * FROM tableConjunctions";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				Conjunction c = new Conjunction();
				c.setWordID(cursor.getString(0));
				c.setWord(cursor.getString(1));
				Log.d("Conjunctions", c.getWord());
				conjunctions.add(c);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return conjunctions;
	}
	
	public void InitializeConjunctions()
	{
		addConjunction(new Conjunction("WORD0218","and"));
		addConjunction(new Conjunction("WORD0424","if"));
	}
	
	public void addConceptMapper(ConceptMapper cm)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("ConceptID", cm.getConceptID());
		cv.put("AgeSixWordID", cm.getAgeSixWordID());
		cv.put("AgeSevenWordID", cm.getAgeSevenWordID());
		cv.put("AgeEightWordID", cm.getAgeEightWordID());
		cv.put("PartOfSpeech", cm.getPartOfSpeech());	
		cv.put("Word", cm.getWord());	
		
		database.insert(TableCreator.TABLE_CONCEPT_MAPPER, null, cv);
	}
	public Vector<ConceptMapper> getConceptMappers()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<ConceptMapper> conceptMappers = new Vector<ConceptMapper>();
		
		String selectQuery = "SELECT * FROM tableConceptMapper";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				ConceptMapper cm = new ConceptMapper();
				cm.setConceptID(cursor.getString(0));
				cm.setAgeSixWordID(cursor.getString(1));
				cm.setAgeSevenWordID(cursor.getString(2));
				cm.setAgeEightWordID(cursor.getString(3));
				cm.setPartOfSpeech(cursor.getString(4));
				cm.setWord(cursor.getString(5));
				Log.d("ConceptMapper", cm.getPartOfSpeech()+ ": " + findWordByID(cm.getAgeSixWordID())+"-"+findWordByID(cm.getAgeSevenWordID())+"-"+findWordByID(cm.getAgeEightWordID()));
				conceptMappers.add(cm);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return conceptMappers;
	}
	
	public void InitializeConceptMapper()
	{
		addConceptMapper(new ConceptMapper("WORD0001","00000001","00000002","00000003","Adjective","happy, glad, merry"));
		addConceptMapper(new ConceptMapper("WORD0002","00000004","00000005","00000006","Adjective","sad, lonely, upset"));
		addConceptMapper(new ConceptMapper("WORD0003","00000007","00000007","00000008","Adjective","pretty, beautiful"));
		addConceptMapper(new ConceptMapper("WORD0004","00000016","00000009","00000010","Adjective","cool, cold, chilly"));
		addConceptMapper(new ConceptMapper("WORD0005","00000011","00000011","00000012","Adjective","bad, bad, naughty"));
		addConceptMapper(new ConceptMapper("WORD0006","00000013","00000014","00000015","Adjective","kind, polite, corteous"));
		addConceptMapper(new ConceptMapper("WORD0007","00000017","00000018","00000019","Adjective","right, correct, exact"));
		addConceptMapper(new ConceptMapper("WORD0008","00000020","00000020","00000021","Adjective","fast, quick"));
		addConceptMapper(new ConceptMapper("WORD0009","00000022","00000022","00000022","Noun","care"));
		addConceptMapper(new ConceptMapper("WORD0010","00000023","00000023","00000023","Noun","choice"));
		addConceptMapper(new ConceptMapper("WORD0011","00000024","00000025","00000026","Noun","piece"));
		addConceptMapper(new ConceptMapper("WORD0012","00000027","00000027","00000027","Verb","know"));
		addConceptMapper(new ConceptMapper("WORD0013","00000028","00000028","00000028","Adjective","windy"));
		addConceptMapper(new ConceptMapper("WORD0014","00000029","00000029","00000029","Adjective","rainy"));
		addConceptMapper(new ConceptMapper("WORD0015","00000030","00000030","00000030","Adjective","sunny"));
		addConceptMapper(new ConceptMapper("WORD0016","00000031","00000031","00000031","Adjective","good"));
		addConceptMapper(new ConceptMapper("WORD0017","00000032","00000032","00000032","Adjective","afraid"));
		addConceptMapper(new ConceptMapper("WORD0018","00000033","00000033","00000033","Noun","afternoon"));
		addConceptMapper(new ConceptMapper("WORD0019","00000034","00000034","00000034","Noun","garden"));
		addConceptMapper(new ConceptMapper("WORD0020","00000035","00000035","00000035","Noun","time"));
		addConceptMapper(new ConceptMapper("WORD0021","00000036","00000036","00000036","Noun","bath"));
		addConceptMapper(new ConceptMapper("WORD0022","00000037","00000037","00000037","Verb","play"));
		addConceptMapper(new ConceptMapper("WORD0023","00000038","00000038","00000038","Adjective","round"));
		addConceptMapper(new ConceptMapper("WORD0024","00000039","00000039","00000039","Pronoun","his"));
		addConceptMapper(new ConceptMapper("WORD0025","00000040","00000040","00000040","Verb","kick"));
		addConceptMapper(new ConceptMapper("WORD0026","00000041","00000041","00000041","Adverb","around"));
		addConceptMapper(new ConceptMapper("WORD0027","00000042","00000042","00000042","Noun","truck"));
		addConceptMapper(new ConceptMapper("WORD0028","00000043","00000043","00000043","Verb","pretend"));
		addConceptMapper(new ConceptMapper("WORD0029","00000044","00000044","00000044","Verb","drive"));
		addConceptMapper(new ConceptMapper("WORD0030","00000045","00000045","00000045","Noun","forest"));
		addConceptMapper(new ConceptMapper("WORD0032","00000047","00000047","00000047","Verb","forget"));
		addConceptMapper(new ConceptMapper("WORD0033","00000048","00000048","00000048","Verb","call"));
		addConceptMapper(new ConceptMapper("WORD0034","00000049","00000049","00000049","Verb","want"));
		addConceptMapper(new ConceptMapper("WORD0035","00000050","00000050","00000050","Verb","decide"));
		addConceptMapper(new ConceptMapper("WORD0036","00000051","00000051","00000051","Verb","hide"));
		addConceptMapper(new ConceptMapper("WORD0037","00000052","00000052","00000052","Noun","tree"));
		addConceptMapper(new ConceptMapper("WORD0038","00000053","00000053","00000053","Noun","arm"));
		addConceptMapper(new ConceptMapper("WORD0039","00000054","00000054","00000054","Verb","start"));
		addConceptMapper(new ConceptMapper("WORD0040","00000055","00000055","00000055","Verb","feel"));
		addConceptMapper(new ConceptMapper("WORD0041","00000056","00000056","00000056","Adjective","itchy"));
		addConceptMapper(new ConceptMapper("WORD0042","00000057","00000057","00000057","Noun","leg"));
		addConceptMapper(new ConceptMapper("WORD0043","00000058","00000058","00000058","Verb","please"));
		addConceptMapper(new ConceptMapper("WORD0044","00000059","00000059","00000059","Adjective","sure"));
		addConceptMapper(new ConceptMapper("WORD0045","00000060","00000060","00000060","Verb","find"));
		addConceptMapper(new ConceptMapper("WORD0046","00000061","00000061","00000061","Pronoun","there"));
		addConceptMapper(new ConceptMapper("WORD0047","00000062","00000062","00000062","Verb","wait"));
		addConceptMapper(new ConceptMapper("WORD0048","00000063","00000063","00000063","Verb","scratch"));
		addConceptMapper(new ConceptMapper("WORD0049","00000064","00000064","00000064","Noun","skin"));
		addConceptMapper(new ConceptMapper("WORD0050","00000065","00000065","00000065","Adjective","red"));
		addConceptMapper(new ConceptMapper("WORD0051","00000066","00000066","00000066","Verb","cry"));
		addConceptMapper(new ConceptMapper("WORD0052","00000067","00000067","00000067","Verb","carry"));
		addConceptMapper(new ConceptMapper("WORD0053","00000068","00000068","00000068","Noun","bathroom"));
		addConceptMapper(new ConceptMapper("WORD0054","00000069","00000069","00000069","Verb","listen"));
		addConceptMapper(new ConceptMapper("WORD0055","00000070","00000070","00000070","Verb","remove"));
		addConceptMapper(new ConceptMapper("WORD0056","00000071","00000071","00000071","Verb","make"));
		addConceptMapper(new ConceptMapper("WORD0057","00000072","00000072","00000072","Noun","bathtub"));
		addConceptMapper(new ConceptMapper("WORD0058","00000073","00000073","00000073","Noun","bubble"));
		addConceptMapper(new ConceptMapper("WORD0059","00000074","00000074","00000074","Noun","water"));
		addConceptMapper(new ConceptMapper("WORD0060","00000075","00000075","00000075","Verb","give"));
		addConceptMapper(new ConceptMapper("WORD0061","00000076","00000076","00000076","Noun","duck"));
		addConceptMapper(new ConceptMapper("WORD0062","00000077","00000077","00000077","Adjective","nice"));
		addConceptMapper(new ConceptMapper("WORD0063","00000078","00000078","00000078","Verb","look"));
		addConceptMapper(new ConceptMapper("WORD0064","00000079","00000079","00000079","Verb","stop"));
		addConceptMapper(new ConceptMapper("WORD0065","00000080","00000080","00000080","Pronoun","it"));
		addConceptMapper(new ConceptMapper("WORD0066","00000081","00000081","00000081","Pronoun","he"));
		addConceptMapper(new ConceptMapper("WORD0067","00000082","00000082","00000082","Pronoun","they"));
		addConceptMapper(new ConceptMapper("WORD0068","00000083","00000083","00000083","Pronoun","your"));
		addConceptMapper(new ConceptMapper("WORD0069","00000084","00000084","00000084","Pronoun","she"));
		addConceptMapper(new ConceptMapper("WORD0070","00000137","00000137","00000137","Noun","feet"));
		addConceptMapper(new ConceptMapper("WORD0071","00000138","00000138","00000138","Verb","soothe"));
		addConceptMapper(new ConceptMapper("WORD0072","00000139","00000139","00000139","Verb","smell"));
		addConceptMapper(new ConceptMapper("WORD0073","00000140","00000140","00000140","Noun","dirt"));
		addConceptMapper(new ConceptMapper("WORD0074","00000141","00000141","00000141","Noun","itch"));
		addConceptMapper(new ConceptMapper("WORD0075","00000142","00000142","00000142","Noun","discomfort"));
		addConceptMapper(new ConceptMapper("WORD0076","00000143","00000143","00000143","Noun","reaction"));
		addConceptMapper(new ConceptMapper("WORD0077","00000144","00000144","00000144","Adjective","dirty"));
		addConceptMapper(new ConceptMapper("WORD0078","00000145","00000145","00000145","Verb","comfort"));
		addConceptMapper(new ConceptMapper("WORD0079","00000146","00000146","00000146","Adjective","angry"));
		addConceptMapper(new ConceptMapper("WORD0080","00000164","00000164","00000164","Verb","talk"));
		addConceptMapper(new ConceptMapper("WORD0081","00000167","00000167","00000167","Verb","do"));
		addConceptMapper(new ConceptMapper("WORD0082","00000171","00000171","00000171","Verb","experience"));
		addConceptMapper(new ConceptMapper("WORD0083","00000174","00000174","00000174","Verb","be"));
		addConceptMapper(new ConceptMapper("WORD0084","00000178","00000178","00000178","Verb","see"));
		addConceptMapper(new ConceptMapper("WORD0085","00000182","00000182","00000182","Noun","child"));
		addConceptMapper(new ConceptMapper("WORD0086","00000183","00000183","00000183","Noun","adult"));
		addConceptMapper(new ConceptMapper("WORD0087","00000184","00000184","00000184","Noun","policeman"));
		addConceptMapper(new ConceptMapper("WORD0088","00000185","00000185","00000185","Noun","teacher"));
		addConceptMapper(new ConceptMapper("WORD0089","00000186","00000186","00000186","Noun","vendor"));
		addConceptMapper(new ConceptMapper("WORD0090","00000187","00000187","00000187","Noun","dentist"));
		addConceptMapper(new ConceptMapper("WORD0091","00000188","00000188","00000188","Noun","nurse"));
		addConceptMapper(new ConceptMapper("WORD0092","00000189","00000189","00000189","Noun","salesman"));
		addConceptMapper(new ConceptMapper("WORD0093","00000190","00000190","00000190","Noun","mother"));
		addConceptMapper(new ConceptMapper("WORD0094","00000191","00000191","00000191","Noun","father"));
		addConceptMapper(new ConceptMapper("WORD0095","00000192","00000192","00000192","Noun","boy"));
		addConceptMapper(new ConceptMapper("WORD0096","00000193","00000193","00000193","Noun","girl"));
		addConceptMapper(new ConceptMapper("WORD0097","00000194","00000194","00000194","Noun","toy"));
		addConceptMapper(new ConceptMapper("WORD0098","00000195","00000195","00000195","Verb","tell"));
		addConceptMapper(new ConceptMapper("WORD0099","00000196","00000196","00000196","Verb","react"));
		addConceptMapper(new ConceptMapper("WORD0100","00000197","00000197","00000197","Verb","result"));
		addConceptMapper(new ConceptMapper("WORD0101","00000198","00000198","00000198","Verb","fight"));
		addConceptMapper(new ConceptMapper("WORD0102","00000199","00000199","00000199","Verb","hurt"));
		addConceptMapper(new ConceptMapper("WORD0103","00000200","00000200","00000200","Verb","scold"));
		addConceptMapper(new ConceptMapper("WORD0104","00000201","00000201","00000201","Noun","guilt"));
		addConceptMapper(new ConceptMapper("WORD0105","00000202","00000202","00000202","Noun","apology"));
		addConceptMapper(new ConceptMapper("WORD0106","00000203","00000203","00000203","Verb","forgive"));
		addConceptMapper(new ConceptMapper("WORD0107","00000204","00000204","00000204","Verb","share"));
		addConceptMapper(new ConceptMapper("WORD0108","00000205","00000205","00000205","Adjective","fun"));
		addConceptMapper(new ConceptMapper("WORD0109","00000206","00000206","00000206","Verb","apologize"));
		addConceptMapper(new ConceptMapper("WORD0110","00000207","00000207","00000207","Verb","insist"));
		addConceptMapper(new ConceptMapper("WORD0111","00000208","00000208","00000208","Noun","rubber ducky"));
		addConceptMapper(new ConceptMapper("WORD0112","00000209","00000209","00000209","Preposition","through"));
		addConceptMapper(new ConceptMapper("WORD0113","00000210","00000210","00000210","Preposition","in"));
		addConceptMapper(new ConceptMapper("WORD0114","00000211","00000211","00000211","Preposition","inside"));
		addConceptMapper(new ConceptMapper("WORD0115","00000212","00000212","00000212","Adverb","forward"));
		addConceptMapper(new ConceptMapper("WORD0116","00000213","00000213","00000213","Preposition","with"));
		addConceptMapper(new ConceptMapper("WORD0117","00000214","00000214","00000214","Preposition","to"));
		addConceptMapper(new ConceptMapper("WORD0118","00000215","00000215","00000215","Preposition","into"));
		addConceptMapper(new ConceptMapper("WORD0119","00000216","00000216","00000216","Preposition","for"));
		addConceptMapper(new ConceptMapper("WORD0120","00000217","00000217","00000217","Adverb","not"));
		addConceptMapper(new ConceptMapper("WORD0121","00000218","00000218","00000218","Adjective","commendation"));
		addConceptMapper(new ConceptMapper("WORD0122","00000219","00000219","00000219","Verb","ask"));
		addConceptMapper(new ConceptMapper("WORD0123","00000220","00000220","00000220","Article","a"));
		addConceptMapper(new ConceptMapper("WORD0124","00000221","00000221","00000221","Article","an"));
		addConceptMapper(new ConceptMapper("WORD0125","00000222","00000222","00000222","Article","the"));
		addConceptMapper(new ConceptMapper("WORD0126","00000223","00000223","00000223","Verb","is"));
		addConceptMapper(new ConceptMapper("WORD0127","00000224","00000224","00000224","Verb","are"));
		addConceptMapper(new ConceptMapper("WORD0128","00000225","00000225","00000225","Verb","were"));
		addConceptMapper(new ConceptMapper("WORD0129","00000226","00000226","00000226","Verb","am"));
		addConceptMapper(new ConceptMapper("WORD0130","00000227","00000227","00000227","Verb","have"));
		addConceptMapper(new ConceptMapper("WORD0131","00000228","00000228","00000228","Verb","has"));
		addConceptMapper(new ConceptMapper("WORD0132","00000229","00000229","00000229","Verb","had"));
		addConceptMapper(new ConceptMapper("WORD0133","00000230","00000230","00000230","Verb","will"));
		addConceptMapper(new ConceptMapper("WORD0134","00000231","00000231","00000231","Verb","shall"));
		addConceptMapper(new ConceptMapper("WORD0135","00000232","00000232","00000232","Verb","would"));
		addConceptMapper(new ConceptMapper("WORD0136","00000233","00000233","00000233","Verb","should"));
		addConceptMapper(new ConceptMapper("WORD0137","00000234","00000234","00000234","Preposition","at"));
		addConceptMapper(new ConceptMapper("WORD0138","00000235","00000235","00000235","Preposition","on"));
		addConceptMapper(new ConceptMapper("WORD0139","00000236","00000236","00000236","Preposition","since"));
		addConceptMapper(new ConceptMapper("WORD0140","00000237","00000237","00000237","Preposition","towards"));
		addConceptMapper(new ConceptMapper("WORD0141","00000239","00000239","00000239","Verb","sharing"));
		addConceptMapper(new ConceptMapper("WORD0142","00000240","00000240","00000240","Noun","living room"));
		addConceptMapper(new ConceptMapper("WORD0143","00000241","00000241","00000241","Noun","bedroom"));
		addConceptMapper(new ConceptMapper("WORD0144","00000242","00000242","00000242","Adjective","indoor"));
		addConceptMapper(new ConceptMapper("WORD0145","00000243","00000243","00000243","Adjective","outdoor"));
		addConceptMapper(new ConceptMapper("WORD0146","00000244","00000244","00000244","Adjective","one"));
		addConceptMapper(new ConceptMapper("WORD0147","00000245","00000245","00000245","Noun","day"));
		addConceptMapper(new ConceptMapper("WORD0148","00000246","00000246","00000246","Preposition","after"));
		addConceptMapper(new ConceptMapper("WORD0149","00000247","00000247","00000247","Noun","school"));
		addConceptMapper(new ConceptMapper("WORD0150","00000248","00000248","00000248","Noun","morning"));
		addConceptMapper(new ConceptMapper("WORD0151","00000249","00000249","00000249","Verb","finish"));
		addConceptMapper(new ConceptMapper("WORD0152","00000250","00000250","00000250","Noun","night"));
		addConceptMapper(new ConceptMapper("WORD0153","00000251","00000251","00000251","Noun","evening"));
		addConceptMapper(new ConceptMapper("WORD0154","00000252","00000252","00000252","Adjective","bright"));
		addConceptMapper(new ConceptMapper("WORD0155","00000253","00000253","00000253","Adjective","fair"));
		addConceptMapper(new ConceptMapper("WORD0156","00000254","00000254","00000254","Adjective","warm"));
		addConceptMapper(new ConceptMapper("WORD0157","00000016","00000009","00000010","Adjective","cool, cold, chilly"));
		addConceptMapper(new ConceptMapper("WORD0158","00000016","00000009","00000010","Adjective","cool, cold, chilly"));
		addConceptMapper(new ConceptMapper("WORD0159","00000255","00000255","00000255","Adjective","specific"));
		addConceptMapper(new ConceptMapper("WORD0160","00000256","00000256","00000256","Adjective","generic"));
		addConceptMapper(new ConceptMapper("WORD0161","00000257","00000257","00000257","Verb","cause"));
		addConceptMapper(new ConceptMapper("WORD0162","00000258","00000258","00000258","Verb","get"));
		addConceptMapper(new ConceptMapper("WORD0163","00000259","00000259","00000259","Preposition","of"));
		addConceptMapper(new ConceptMapper("WORD0164","00000260","00000260","00000260","Adverb","near"));
		addConceptMapper(new ConceptMapper("WORD0165","00000025","00000025","00000025","Noun","part"));
		addConceptMapper(new ConceptMapper("WORD0166","00000261","00000261","00000261","Verb","use"));
		addConceptMapper(new ConceptMapper("WORD0167","00000262","00000262","00000262","Adjective","guilty"));
		addConceptMapper(new ConceptMapper("WORD0168","00000263","00000263","00000263","Noun","ball"));
		addConceptMapper(new ConceptMapper("WORD0169","00000264","00000264","00000264","Verb","become"));
		addConceptMapper(new ConceptMapper("WORD0170","00000265","00000265","00000265","Adjective","clean"));
		addConceptMapper(new ConceptMapper("WORD0171","00000266","00000266","00000266","Noun","clinic"));
		addConceptMapper(new ConceptMapper("WORD0172","00000267","00000267","00000267","Noun","mall"));
		addConceptMapper(new ConceptMapper("WORD0173","00000268","00000268","00000268","Noun","market"));
		addConceptMapper(new ConceptMapper("WORD0174","00000269","00000269","00000269","Noun","playground"));
		addConceptMapper(new ConceptMapper("WORD0175","00000270","00000270","00000270","Noun","dining room"));
		addConceptMapper(new ConceptMapper("WORD0176","00000271","00000271","00000271","Verb","learn"));
		addConceptMapper(new ConceptMapper("WORD0177","00000272","00000272","00000272","Adjective","private"));
		addConceptMapper(new ConceptMapper("WORD0178","00000273","00000273","00000273","Adjective","public"));
		addConceptMapper(new ConceptMapper("WORD0179","00000274","00000274","00000274","Noun","alarm "));
		addConceptMapper(new ConceptMapper("WORD0180","00000275","00000275","00000275","Noun","clock"));
		addConceptMapper(new ConceptMapper("WORD0181","00000276","00000276","00000276","Noun","apple"));
		addConceptMapper(new ConceptMapper("WORD0182","00000277","00000277","00000277","Noun","backpack"));
		addConceptMapper(new ConceptMapper("WORD0183","00000278","00000278","00000278","Noun","banana"));
		addConceptMapper(new ConceptMapper("WORD0184","00000279","00000279","00000279","Noun","beach"));
		addConceptMapper(new ConceptMapper("WORD0185","00000280","00000280","00000280","Noun","book"));
		addConceptMapper(new ConceptMapper("WORD0186","00000281","00000281","00000281","Noun","brocolli"));
		addConceptMapper(new ConceptMapper("WORD0187","00000282","00000282","00000282","Noun","brush"));
		addConceptMapper(new ConceptMapper("WORD0188","00000283","00000283","00000283","Noun","cake"));
		addConceptMapper(new ConceptMapper("WORD0189","00000284","00000284","00000284","Noun","candy"));
		addConceptMapper(new ConceptMapper("WORD0190","00000285","00000285","00000285","Noun","carrot"));
		addConceptMapper(new ConceptMapper("WORD0191","00000286","00000286","00000286","Noun","chair"));
		addConceptMapper(new ConceptMapper("WORD0192","00000287","00000287","00000287","Noun","doll"));
		addConceptMapper(new ConceptMapper("WORD0193","00000288","00000288","00000288","Adjective","fried"));
		addConceptMapper(new ConceptMapper("WORD0194","00000289","00000289","00000289","Noun","chicken"));
		addConceptMapper(new ConceptMapper("WORD0195","00000290","00000290","00000290","Noun","glass"));
		addConceptMapper(new ConceptMapper("WORD0196","00000291","00000291","00000291","Noun","bread"));
		addConceptMapper(new ConceptMapper("WORD0197","00000292","00000292","00000292","Noun","lamp"));
		addConceptMapper(new ConceptMapper("WORD0198","00000293","00000293","00000293","Noun","pillow"));
		addConceptMapper(new ConceptMapper("WORD0199","00000294","00000294","00000294","Noun","salt"));
		addConceptMapper(new ConceptMapper("WORD0200","00000295","00000295","00000295","Noun","pepper"));
		addConceptMapper(new ConceptMapper("WORD0201","00000296","00000296","00000296","Noun","seesaw"));
		addConceptMapper(new ConceptMapper("WORD0202","00000297","00000297","00000297","Noun","stethoscope"));
		addConceptMapper(new ConceptMapper("WORD0203","00000298","00000298","00000298","Noun","swing"));
		addConceptMapper(new ConceptMapper("WORD0204","00000299","00000299","00000299","Noun","set"));
		addConceptMapper(new ConceptMapper("WORD0205","00000300","00000300","00000300","Noun","tea"));
		addConceptMapper(new ConceptMapper("WORD0206","00000301","00000301","00000301","Noun","television"));
		addConceptMapper(new ConceptMapper("WORD0207","00000302","00000302","00000302","Noun","thermometer"));
		addConceptMapper(new ConceptMapper("WORD0208","00000303","00000303","00000303","Noun","toothpaste "));
		addConceptMapper(new ConceptMapper("WORD0209","00000304","00000304","00000304","Noun","toothbrush"));
		addConceptMapper(new ConceptMapper("WORD0210","00000305","00000305","00000305","Noun","block"));
		addConceptMapper(new ConceptMapper("WORD0211","00000306","00000306","00000306","Noun","car"));
		addConceptMapper(new ConceptMapper("WORD0212","00000307","00000307","00000307","Noun","horse"));
		addConceptMapper(new ConceptMapper("WORD0213","00000308","00000308","00000308","Noun","tricycle"));
		addConceptMapper(new ConceptMapper("WORD0214","00000309","00000309","00000309","Noun","wallet"));
		addConceptMapper(new ConceptMapper("WORD0215","00000310","00000310","00000310","Noun","weighing scale"));
		addConceptMapper(new ConceptMapper("WORD0217","00000312","00000312","00000312","Noun","spaghetti"));
		addConceptMapper(new ConceptMapper("WORD0218","00000313","00000313","00000313","Conjuction","and"));
		addConceptMapper(new ConceptMapper("WORD0219","00000157","00000157","00000157","Noun","soap"));
		addConceptMapper(new ConceptMapper("WORD0237","00000331","00000331","00000331","Pronoun","him"));
		addConceptMapper(new ConceptMapper("WORD0238","00000332","00000332","00000332","Pronoun","her"));
		addConceptMapper(new ConceptMapper("WORD0239","00000333","00000333","00000333","Pronoun","them"));
		addConceptMapper(new ConceptMapper("WORD0240","00000334","00000334","00000334","Noun","teeth"));
		addConceptMapper(new ConceptMapper("WORD0241","00000335","00000335","00000335","Adjective","careful"));
		addConceptMapper(new ConceptMapper("WORD0242","00000336","00000336","00000336","Verb","eat"));
		addConceptMapper(new ConceptMapper("WORD0243","00000337","00000337","00000337","Adjective","brave"));
		addConceptMapper(new ConceptMapper("WORD0244","00000338","00000338","00000338","Verb","sleep"));
		addConceptMapper(new ConceptMapper("WORD0245","00000339","00000339","00000339","Adjective","early"));
		addConceptMapper(new ConceptMapper("WORD0246","00000340","00000340","00000340","Adjective","honest"));
		addConceptMapper(new ConceptMapper("WORD0247","00000341","00000341","00000341","Noun","permission"));
		addConceptMapper(new ConceptMapper("WORD0248","00000342","00000342","00000342","Adjective","neat"));
		addConceptMapper(new ConceptMapper("WORD0249","00000343","00000343","00000343","Adjective","healthy"));
		addConceptMapper(new ConceptMapper("WORD0250","00000344","00000344","00000344","Noun","food"));
		addConceptMapper(new ConceptMapper("WORD0251","00000345","00000345","00000345","Noun","location"));
		addConceptMapper(new ConceptMapper("WORD0252","00000346","00000346","00000346","Verb","take"));
		addConceptMapper(new ConceptMapper("WORD0253","00000347","00000347","00000347","Noun","cleanliness"));
		addConceptMapper(new ConceptMapper("WORD0254","00000348","00000348","00000348","Adjective","irritated"));
		addConceptMapper(new ConceptMapper("WORD0255","00000349","00000349","00000349","Verb","irritate"));
		addConceptMapper(new ConceptMapper("WORD0256","00000350","00000350","00000350","Adjective","scared"));
		addConceptMapper(new ConceptMapper("WORD0257","00000351","00000351","00000351","Verb","run"));
		addConceptMapper(new ConceptMapper("WORD0258","00000352","00000352","00000352","Adverb","away"));
		addConceptMapper(new ConceptMapper("WORD0259","00000353","00000353","00000353","Verb","come"));
		addConceptMapper(new ConceptMapper("WORD0260","00000354","00000354","00000354","Adverb","out"));
		addConceptMapper(new ConceptMapper("WORD0261","00000355","00000355","00000355","Adjective","sorry"));
		addConceptMapper(new ConceptMapper("WORD0262","00000356","00000356","00000356","Adverb","up"));
		addConceptMapper(new ConceptMapper("WORD0263","00000357","00000357","00000357","Adjective","breakable"));
		addConceptMapper(new ConceptMapper("WORD0264","00000358","00000358","00000358","Noun","object"));
		addConceptMapper(new ConceptMapper("WORD0265","00000359","00000359","00000359","Verb","break"));
		addConceptMapper(new ConceptMapper("WORD0266","00000360","00000360","00000360","Verb","prevent"));
		addConceptMapper(new ConceptMapper("WORD0267","00000361","00000361","00000361","Noun","accident"));
		addConceptMapper(new ConceptMapper("WORD0268","00000362","00000362","00000362","Verb","can"));
		addConceptMapper(new ConceptMapper("WORD0269","00000363","00000363","00000363","Preposition","from"));
		addConceptMapper(new ConceptMapper("WORD0270","00000364","00000364","00000364","Verb","remind"));
		addConceptMapper(new ConceptMapper("WORD0271","00000365","00000365","00000365","Verb","help"));
		addConceptMapper(new ConceptMapper("WORD0272","00000366","00000366","00000366","Noun","mess"));
		addConceptMapper(new ConceptMapper("WORD0273","00000367","00000367","00000367","Noun","problem"));
		addConceptMapper(new ConceptMapper("WORD0274","00000368","00000368","00000368","Adjective","lost"));
		addConceptMapper(new ConceptMapper("WORD0275","00000369","00000369","00000369","Noun","lesson"));
		addConceptMapper(new ConceptMapper("WORD0276","00000370","00000370","00000370","Adjective","messy"));
		addConceptMapper(new ConceptMapper("WORD0277","00000371","00000371","00000371","Verb","scatter"));
		addConceptMapper(new ConceptMapper("WORD0278","00000372","00000372","00000372","Verb","create"));
		addConceptMapper(new ConceptMapper("WORD0279","00000373","00000373","00000373","Noun","disorder"));
		addConceptMapper(new ConceptMapper("WORD0280","00000374","00000374","00000374","Verb","obey"));
		addConceptMapper(new ConceptMapper("WORD0281","00000375","00000375","00000375","Verb","continue"));
		addConceptMapper(new ConceptMapper("WORD0282","00000376","00000376","00000376","Adverb","too"));
		addConceptMapper(new ConceptMapper("WORD0283","00000377","00000377","00000377","Adverb","much"));
		addConceptMapper(new ConceptMapper("WORD0284","00000378","00000378","00000378","Verb","like"));
		addConceptMapper(new ConceptMapper("WORD0285","00000379","00000379","00000379","Noun","consequence"));
		addConceptMapper(new ConceptMapper("WORD0286","00000380","00000380","00000380","Verb","prepare"));
		addConceptMapper(new ConceptMapper("WORD0287","00000381","00000381","00000381","Noun","meal"));
		addConceptMapper(new ConceptMapper("WORD0288","00000382","00000382","00000382","Verb","spare"));
		addConceptMapper(new ConceptMapper("WORD0289","00000383","00000383","00000383","Verb","thank"));
		addConceptMapper(new ConceptMapper("WORD0290","00000384","00000384","00000384","Preposition","before"));
		addConceptMapper(new ConceptMapper("WORD0291","00000385","00000385","00000385","Noun","mealtime"));
		addConceptMapper(new ConceptMapper("WORD0292","00000386","00000386","00000386","Noun","junk food"));
		addConceptMapper(new ConceptMapper("WORD0293","00000387","00000387","00000387","Adjective","full"));
		addConceptMapper(new ConceptMapper("WORD0294","00000388","00000388","00000388","Noun","stomach"));
		addConceptMapper(new ConceptMapper("WORD0295","00000389","00000389","00000389","Verb","lose"));
		addConceptMapper(new ConceptMapper("WORD0296","00000390","00000390","00000390","Noun","appetite"));
		addConceptMapper(new ConceptMapper("WORD0297","00000391","00000391","00000391","Verb","skip"));
		addConceptMapper(new ConceptMapper("WORD0298","00000392","00000392","00000392","Noun","stomachache"));
		addConceptMapper(new ConceptMapper("WORD0299","00000393","00000393","00000393","Noun","solution"));
		addConceptMapper(new ConceptMapper("WORD0300","00000394","00000394","00000394","Noun","benefit"));
		addConceptMapper(new ConceptMapper("WORD0301","00000395","00000395","00000395","Noun","candy"));
		addConceptMapper(new ConceptMapper("WORD0302","00000396","00000396","00000396","Adjective","sweet"));
		addConceptMapper(new ConceptMapper("WORD0303","00000397","00000397","00000397","Noun","toothache"));
		addConceptMapper(new ConceptMapper("WORD0304","00000398","00000398","00000398","Noun","pain"));
		addConceptMapper(new ConceptMapper("WORD0305","00000399","00000399","00000399","Noun","medicine"));
		addConceptMapper(new ConceptMapper("WORD0306","00000400","00000400","00000400","Verb","heal"));
		addConceptMapper(new ConceptMapper("WORD0307","00000401","00000401","00000401","Adjective","better"));
		addConceptMapper(new ConceptMapper("WORD0308","00000402","00000402","00000402","Noun","fruit"));
		addConceptMapper(new ConceptMapper("WORD0309","00000403","00000403","00000403","Noun","vegetable"));
		addConceptMapper(new ConceptMapper("WORD0310","00000404","00000404","00000404","Adjective","strong"));
		addConceptMapper(new ConceptMapper("WORD0311","00000405","00000405","00000405","Adjective","next"));
		addConceptMapper(new ConceptMapper("WORD0312","00000406","00000406","00000406","Noun","sweet"));
		addConceptMapper(new ConceptMapper("WORD0313","00000407","00000407","00000407","Noun","cavity"));
		addConceptMapper(new ConceptMapper("WORD0314","00000408","00000408","00000408","Noun","chip"));
		addConceptMapper(new ConceptMapper("WORD0315","00000409","00000409","00000409","Noun","strength"));
		addConceptMapper(new ConceptMapper("WORD0316","00000410","00000410","00000410","Verb","watch"));
		addConceptMapper(new ConceptMapper("WORD0317","00000411","00000411","00000411","Adjective","late"));
		addConceptMapper(new ConceptMapper("WORD0318","00000412","00000412","00000412","Noun","headache"));
		addConceptMapper(new ConceptMapper("WORD0319","00000413","00000413","00000413","Adjective","dizzy"));
		addConceptMapper(new ConceptMapper("WORD0320","00000414","00000414","00000414","Noun","rest"));
		addConceptMapper(new ConceptMapper("WORD0321","00000415","00000415","00000415","Verb","throw"));
		addConceptMapper(new ConceptMapper("WORD0322","00000416","00000416","00000416","Noun","dizziness"));
		addConceptMapper(new ConceptMapper("WORD0323","00000417","00000417","00000417","Verb","save"));
		addConceptMapper(new ConceptMapper("WORD0324","00000418","00000418","00000418","Verb","go"));
		addConceptMapper(new ConceptMapper("WORD0325","00000419","00000419","00000419","Noun","bed"));
		addConceptMapper(new ConceptMapper("WORD0326","00000420","00000420","00000420","Verb","seek"));
		addConceptMapper(new ConceptMapper("WORD0327","00000421","00000421","00000421","Verb","leave"));
		addConceptMapper(new ConceptMapper("WORD0328","00000422","00000422","00000422","Verb","introduce"));
		addConceptMapper(new ConceptMapper("WORD0329","00000423","00000423","00000423","Noun","class"));
		addConceptMapper(new ConceptMapper("WORD0330","00000424","00000424","00000424","Noun","introduction"));
		addConceptMapper(new ConceptMapper("WORD0331","00000425","00000425","00000425","Noun","friendship"));
		addConceptMapper(new ConceptMapper("WORD0332","00000426","00000426","00000426","Noun","friend"));
		addConceptMapper(new ConceptMapper("WORD0333","00000427","00000427","00000427","Noun","activity"));
		addConceptMapper(new ConceptMapper("WORD0334","00000428","00000428","00000428","Noun","emotion"));
		addConceptMapper(new ConceptMapper("WORD0335","00000429","00000429","00000429","Verb","say"));
		addConceptMapper(new ConceptMapper("WORD0336","00000430","00000430","00000430","Noun","goodbye"));
		addConceptMapper(new ConceptMapper("WORD0337","00000431","00000431","00000431","Noun","virtue"));
		addConceptMapper(new ConceptMapper("WORD0338","00000432","00000432","00000432","Adjective","admirable"));
		addConceptMapper(new ConceptMapper("WORD0339","00000433","00000433","00000433","Noun","smile"));
		addConceptMapper(new ConceptMapper("WORD0340","00000434","00000434","00000434","Adjective","shy"));
		addConceptMapper(new ConceptMapper("WORD0341","00000435","00000435","00000435","Pronoun","others"));
		addConceptMapper(new ConceptMapper("WORD0342","00000436","00000436","00000436","Adjective","new"));
		addConceptMapper(new ConceptMapper("WORD0343","00000437","00000437","00000437","Noun","playmate"));
		addConceptMapper(new ConceptMapper("WORD0344","00000438","00000438","00000438","Noun","game"));
		addConceptMapper(new ConceptMapper("WORD0345","00000439","00000439","00000439","Noun","switch"));
		addConceptMapper(new ConceptMapper("WORD0346","00000440","00000440","00000440","Adverb","off"));
		addConceptMapper(new ConceptMapper("WORD0347","00000441","00000441","00000441","Noun","light"));
		addConceptMapper(new ConceptMapper("WORD0348","00000442","00000442","00000442","Adjective","dark"));
		addConceptMapper(new ConceptMapper("WORD0349","00000443","00000443","00000443","Verb","sing"));
		addConceptMapper(new ConceptMapper("WORD0350","00000444","00000444","00000444","Noun","lullaby"));
		addConceptMapper(new ConceptMapper("WORD0351","00000445","00000445","00000445","Noun","story"));
		addConceptMapper(new ConceptMapper("WORD0352","00000446","00000446","00000446","Noun","home"));
		addConceptMapper(new ConceptMapper("WORD0353","00000447","00000447","00000447","Verb","check"));
		addConceptMapper(new ConceptMapper("WORD0354","00000448","00000448","00000448","Adjective","positive"));
		addConceptMapper(new ConceptMapper("WORD0355","00000449","00000449","00000449","Noun","reward"));
		addConceptMapper(new ConceptMapper("WORD0357","00000451","00000451","00000451","Verb","need"));
		addConceptMapper(new ConceptMapper("WORD0358","00000452","00000452","00000452","Adjective","sick"));
		addConceptMapper(new ConceptMapper("WORD0359","00000453","00000453","00000453","Adjective","nervous"));
		addConceptMapper(new ConceptMapper("WORD0360","00000454","00000454","00000454","Verb","hear"));
		addConceptMapper(new ConceptMapper("WORD0361","00000455","00000455","00000455","Noun","heartbeat"));
		addConceptMapper(new ConceptMapper("WORD0362","00000456","00000456","00000456","Noun","temperature"));
		addConceptMapper(new ConceptMapper("WORD0363","00000457","00000457","00000457","Noun","ice cream"));
		addConceptMapper(new ConceptMapper("WORD0364","00000458","00000458","00000458","Nouns","classroom"));
		addConceptMapper(new ConceptMapper("WORD0365","00000459","00000459","00000459","Verb","left"));
		addConceptMapper(new ConceptMapper("WORD0366","00000460","00000460","00000460","Preposition","without"));
		addConceptMapper(new ConceptMapper("WORD0367","00000461","00000461","00000461","Verb","buy"));
		addConceptMapper(new ConceptMapper("WORD0368","00000462","00000462","00000462","Noun","toy"));
		addConceptMapper(new ConceptMapper("WORD0369","00000463","00000463","00000463","Noun","store"));
		addConceptMapper(new ConceptMapper("WORD0370","00000464","00000464","00000464","Noun","confectionary"));
		addConceptMapper(new ConceptMapper("WORD0371","00000465","00000465","00000465","Noun","stand"));
		addConceptMapper(new ConceptMapper("WORD0372","00000466","00000466","00000466","Noun","search"));
		addConceptMapper(new ConceptMapper("WORD0373","00000467","00000467","00000467","Noun","feeling"));
		addConceptMapper(new ConceptMapper("WORD0374","00000468","00000468","00000468","Adjective","worried"));
		addConceptMapper(new ConceptMapper("WORD0375","00000469","00000469","00000469","Adjective","important"));
		addConceptMapper(new ConceptMapper("WORD0377","00000471","00000471","00000471","Noun","method"));
		addConceptMapper(new ConceptMapper("WORD0378","00000472","00000472","00000472","Noun","destruction"));
		addConceptMapper(new ConceptMapper("WORD0379","00000473","00000473","00000473","Adjective","destroyed"));
		addConceptMapper(new ConceptMapper("WORD0380","00000474","00000474","00000474","Adjective","broken"));
		addConceptMapper(new ConceptMapper("WORD0381","00000475","00000475","00000475","Adjective","careless"));
		addConceptMapper(new ConceptMapper("WORD0382","00000476","00000476","00000476","Noun","punishment"));
		addConceptMapper(new ConceptMapper("WORD0383","00000477","00000477","00000477","Adjective","punished"));
		addConceptMapper(new ConceptMapper("WORD0384","00000478","00000478","00000478","Verb","allow"));
		addConceptMapper(new ConceptMapper("WORD0385","00000479","00000479","00000479","Noun","today"));
		addConceptMapper(new ConceptMapper("WORD0386","00000480","00000480","00000480","Adjective","grounded"));
		addConceptMapper(new ConceptMapper("WORD0387","00000481","00000481","00000481","Noun","room"));
		addConceptMapper(new ConceptMapper("WORD0388","00000482","00000482","00000482","Noun","lie"));
		addConceptMapper(new ConceptMapper("WORD0389","00000483","00000483","00000483","Adjective","salty"));
		addConceptMapper(new ConceptMapper("WORD0390","00000066","00000066","00000066","Noun","cry"));
		addConceptMapper(new ConceptMapper("WORD0391","00000145","00000145","00000145","Noun","comfort"));
		addConceptMapper(new ConceptMapper("WORD0392","00000171","00000171","00000171","Noun","experience"));
		addConceptMapper(new ConceptMapper("WORD0393","00000199","00000199","00000199","Adjective","hurt"));
		addConceptMapper(new ConceptMapper("WORD0394","00000244","00000244","00000244","Pronoun","one"));
		addConceptMapper(new ConceptMapper("WORD0395","00000244","00000244","00000244","Noun","one"));
		addConceptMapper(new ConceptMapper("WORD0396","00000265","00000265","00000265","Verb","clean"));
		addConceptMapper(new ConceptMapper("WORD0397","00000377","00000377","00000377","Adjective","much"));
		addConceptMapper(new ConceptMapper("WORD0398","00000414","00000414","00000414","Verb","rest"));
		addConceptMapper(new ConceptMapper("WORD0399","00000433","00000433","00000433","Verb","smile"));
		addConceptMapper(new ConceptMapper("WORD0400","00000439","00000439","00000439","Verb","switch"));
		addConceptMapper(new ConceptMapper("WORD0401","00000411","00000411","00000411","Adverb","late"));
		addConceptMapper(new ConceptMapper("WORD0402","00000449","00000449","00000449","Verb","reward"));
		addConceptMapper(new ConceptMapper("WORD0403","00000459","00000459","00000459","Noun","left"));
		addConceptMapper(new ConceptMapper("WORD0404","00000463","00000463","00000463","Verb","store"));
		addConceptMapper(new ConceptMapper("WORD0405","00000465","00000465","00000465","Verb","stand"));
		addConceptMapper(new ConceptMapper("WORD0406","00000466","00000466","00000466","Verb","search"));
		addConceptMapper(new ConceptMapper("WORD0407","00000479","00000479","00000479","Adverb","today"));
		addConceptMapper(new ConceptMapper("WORD0408","00000480","00000480","00000480","Noun","ground"));
		addConceptMapper(new ConceptMapper("WORD0409","00000482","00000482","00000482","Verb","lie"));
		addConceptMapper(new ConceptMapper("WORD0410","00000282","00000282","00000282","Verb","brush"));
		addConceptMapper(new ConceptMapper("WORD0411","00000197","00000197","00000197","Noun","result"));
		addConceptMapper(new ConceptMapper("WORD0412","00000484","00000484","00000485","Verb","complain;grumble"));
		addConceptMapper(new ConceptMapper("WORD0413","00000486","00000486","00000486","Adjective","scary"));
		addConceptMapper(new ConceptMapper("WORD0414","00000487","00000487","00000488","Adjective","surprised;shocked"));
		addConceptMapper(new ConceptMapper("WORD0415","00000489","00000489","00000489","Adverb","aside"));
		addConceptMapper(new ConceptMapper("WORD0416","00000490","00000490","00000490","Noun","foods"));
		addConceptMapper(new ConceptMapper("WORD0417","00000491","00000491","00000491","Adjective","bedtime"));
		addConceptMapper(new ConceptMapper("WORD0418","00000492","00000492","00000492","Adverb","too much"));
		addConceptMapper(new ConceptMapper("WORD0419","00000442","00000442","00000442","Noun","dark"));
		addConceptMapper(new ConceptMapper("WORD0420","00000493","00000493","00000493","Adjective","comforted"));
		addConceptMapper(new ConceptMapper("WORD0421","00000494","00000494","00000494","Adjective","allowed"));
		addConceptMapper(new ConceptMapper("WORD0422","00000495","00000495","00000495","Verb","stay"));
		addConceptMapper(new ConceptMapper("WORD0423","00000496","00000496","00000496","Verb","put"));
		addConceptMapper(new ConceptMapper("WORD0424","00000497","00000497","00000497","Conjunction","if"));
		addConceptMapper(new ConceptMapper("WORD0425","00000498","00000498","00000498","Pronoun","everything"));
		addConceptMapper(new ConceptMapper("WORD0426","00000499","00000499","00000499","Adjective","okay"));
		addConceptMapper(new ConceptMapper("WORD0427","00000500","00000500","00000500","Adverb","how"));
		addConceptMapper(new ConceptMapper("WORD0428","00000501","00000501","00000501","Verb","was"));
		addConceptMapper(new ConceptMapper("WORD0429","00000503","00000502","00000504","Adjective","yummy;delicious;delightful"));
		addConceptMapper(new ConceptMapper("WORD0430","00000506","00000506","00000506","Adjective","green"));
		addConceptMapper(new ConceptMapper("WORD0431","00000507","00000507","00000507","Adjective","yellow"));
		addConceptMapper(new ConceptMapper("WORD0432","00000508","00000509","00000510","Adjective","big;large;huge"));
		addConceptMapper(new ConceptMapper("WORD0433","00000511","00000512","00000512","Adjective","small;little"));
		addConceptMapper(new ConceptMapper("WORD0434","00000513","00000513","00000513","Adjective","fragrant"));
		addConceptMapper(new ConceptMapper("WORD0435","00000514","00000514","00000514","Adjective","slippery"));
		addConceptMapper(new ConceptMapper("WORD0436","00000515","00000515","00000515","Adjective","soft"));
		addConceptMapper(new ConceptMapper("WORD0437","00000516","00000516","00000516","Adjective","fluffy"));
		addConceptMapper(new ConceptMapper("WORD0438","00000517","00000517","00000517","Adjective","colorful"));
		addConceptMapper(new ConceptMapper("WORD0439","00000299","00000299","00000299","Verb","set"));	
	}
	
	public void addArticle(Article article)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("WordID", article.getWordID());
		cv.put("Word", article.getWord());
		
		database.insert(TableCreator.TABLE_ARTICLES, null, cv);
	}
	
	public Vector<Article> getArticles()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<Article> articles = new Vector<Article>();
		
		String selectQuery = "SELECT * FROM tableArticles";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				Article article = new Article();
				article.setWordID(cursor.getString(0));
				article.setWord(cursor.getString(1));
				Log.d("Articles", article.getWord());
				articles.add(article);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return articles;
	}	
	
	public void InitializeArticles()
	{
		addArticle(new Article("WORD0123","a"));
		addArticle(new Article("WORD0124","an"));
		addArticle(new Article("WORD0125","the"));
	}
	
	public void addAdverb(Adverb adverb)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("WordID", adverb.getWordID());
		cv.put("Type", adverb.getType());
		cv.put("Word", adverb.getWord());
		
		database.insert(TableCreator.TABLE_ADVERBS, null, cv);
	}	
	public Vector<Adverb> getAdverbs()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<Adverb> adverbs = new Vector<Adverb>();
		
		String selectQuery = "SELECT * FROM tableAdverbs";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				Adverb adv = new Adverb();
				adv.setWordID(cursor.getString(0));
				adv.setType(cursor.getString(1));
				adv.setWord(cursor.getString(2));
				Log.d("Adverbs", adv.getWord());
				adverbs.add(adv);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return adverbs;
	}	
	
	public void InitializeAdverbs()
	{
		addAdverb(new Adverb("WORD0026","","around"));
		addAdverb(new Adverb("WORD0115","","forward"));
		addAdverb(new Adverb("WORD0120","negation","not"));
		addAdverb(new Adverb("WORD0164","","near"));
		addAdverb(new Adverb("WORD0258","","away"));
		addAdverb(new Adverb("WORD0260","","out"));
		addAdverb(new Adverb("WORD0262","","up"));
		addAdverb(new Adverb("WORD0282","","too"));
		addAdverb(new Adverb("WORD0283","","much"));
		addAdverb(new Adverb("WORD0346","","off"));
		addAdverb(new Adverb("WORD0401","","late"));
		addAdverb(new Adverb("WORD0407","","today"));
		addAdverb(new Adverb("WORD0415","","aside"));
		addAdverb(new Adverb("WORD0418","","too much"));
		addAdverb(new Adverb("WORD0427","","how"));
	}
	
	public void addAdjective(Adjective adjective)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("WordID", adjective.getWordID());
		cv.put("Word", adjective.getWord());
		
		database.insert(TableCreator.TABLE_ADJECTIVES, null, cv);
	}
	
	public Vector<Adjective> getAdjectives()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<Adjective> adjectives = new Vector<Adjective>();
		
		String selectQuery = "SELECT * FROM tableAdjectives";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				Adjective adj = new Adjective();
				adj.setWordID(cursor.getString(0));
				adj.setWord(cursor.getString(1));
				Log.d("Adjectives", adj.getWord());
				adjectives.add(adj);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return adjectives;
	}	
	
	public void InitializeAdjectives()
	{
		addAdjective(new Adjective("WORD0001","happy, glad, merry"));
		addAdjective(new Adjective("WORD0002","sad, lonely, upset"));
		addAdjective(new Adjective("WORD0003","pretty, beautiful"));
		addAdjective(new Adjective("WORD0004","cool, cold, chilly"));
		addAdjective(new Adjective("WORD0005","bad, bad, naughty"));
		addAdjective(new Adjective("WORD0006","kind, polite, corteous"));
		addAdjective(new Adjective("WORD0007","right, correct, exact"));
		addAdjective(new Adjective("WORD0008","fast, quick"));
		addAdjective(new Adjective("WORD0013","windy"));
		addAdjective(new Adjective("WORD0014","rainy"));
		addAdjective(new Adjective("WORD0015","sunny"));
		addAdjective(new Adjective("WORD0016","good"));
		addAdjective(new Adjective("WORD0017","afraid"));
		addAdjective(new Adjective("WORD0023","round"));
		addAdjective(new Adjective("WORD0041","itchy"));
		addAdjective(new Adjective("WORD0044","sure"));
		addAdjective(new Adjective("WORD0050","red"));
		addAdjective(new Adjective("WORD0062","nice"));
		addAdjective(new Adjective("WORD0077","dirty"));
		addAdjective(new Adjective("WORD0079","angry"));
		addAdjective(new Adjective("WORD0108","fun"));
		addAdjective(new Adjective("WORD0121","commendation"));
		addAdjective(new Adjective("WORD0144","indoor"));
		addAdjective(new Adjective("WORD0145","outdoor"));
		addAdjective(new Adjective("WORD0146","one"));
		addAdjective(new Adjective("WORD0154","bright"));
		addAdjective(new Adjective("WORD0155","fair"));
		addAdjective(new Adjective("WORD0156","warm"));
		addAdjective(new Adjective("WORD0157","cool, cold, chilly"));
		addAdjective(new Adjective("WORD0158","cool, cold, chilly"));
		addAdjective(new Adjective("WORD0159","specific"));
		addAdjective(new Adjective("WORD0160","generic"));
		addAdjective(new Adjective("WORD0167","guilty"));
		addAdjective(new Adjective("WORD0170","clean"));
		addAdjective(new Adjective("WORD0177","private"));
		addAdjective(new Adjective("WORD0178","public"));
		addAdjective(new Adjective("WORD0193","fried"));
		addAdjective(new Adjective("WORD0241","careful"));
		addAdjective(new Adjective("WORD0243","brave"));
		addAdjective(new Adjective("WORD0245","early"));
		addAdjective(new Adjective("WORD0246","honest"));
		addAdjective(new Adjective("WORD0248","neat"));
		addAdjective(new Adjective("WORD0249","healthy"));
		addAdjective(new Adjective("WORD0254","irritated"));
		addAdjective(new Adjective("WORD0256","scared"));
		addAdjective(new Adjective("WORD0261","sorry"));
		addAdjective(new Adjective("WORD0263","breakable"));
		addAdjective(new Adjective("WORD0274","lost"));
		addAdjective(new Adjective("WORD0276","messy"));
		addAdjective(new Adjective("WORD0293","full"));
		addAdjective(new Adjective("WORD0302","sweet"));
		addAdjective(new Adjective("WORD0307","better"));
		addAdjective(new Adjective("WORD0310","strong"));
		addAdjective(new Adjective("WORD0311","next"));
		addAdjective(new Adjective("WORD0317","late"));
		addAdjective(new Adjective("WORD0319","dizzy"));
		addAdjective(new Adjective("WORD0338","admirable"));
		addAdjective(new Adjective("WORD0340","shy"));
		addAdjective(new Adjective("WORD0342","new"));
		addAdjective(new Adjective("WORD0348","dark"));
		addAdjective(new Adjective("WORD0354","positive"));
		addAdjective(new Adjective("WORD0358","sick"));
		addAdjective(new Adjective("WORD0359","nervous"));
		addAdjective(new Adjective("WORD0374","worried"));
		addAdjective(new Adjective("WORD0375","important"));
		addAdjective(new Adjective("WORD0379","destroyed"));
		addAdjective(new Adjective("WORD0380","broken"));
		addAdjective(new Adjective("WORD0381","careless"));
		addAdjective(new Adjective("WORD0383","punished"));
		addAdjective(new Adjective("WORD0389","salty"));
		addAdjective(new Adjective("WORD0393","hurt"));
		addAdjective(new Adjective("WORD0397","much"));
		addAdjective(new Adjective("WORD0413","scary"));
		addAdjective(new Adjective("WORD0414","surprised;shocked"));
		addAdjective(new Adjective("WORD0417","bedtime"));
		addAdjective(new Adjective("WORD0420","comforted"));
		addAdjective(new Adjective("WORD0426","okay"));
		addAdjective(new Adjective("WORD0429","yummy;delicious;delightful"));
		addAdjective(new Adjective("WORD0430","green"));
		addAdjective(new Adjective("WORD0431","yellow"));
		addAdjective(new Adjective("WORD0432","big;large;huge"));
		addAdjective(new Adjective("WORD0433","small;little"));
		addAdjective(new Adjective("WORD0434","fragrant"));
		addAdjective(new Adjective("WORD0435","slippery"));
		addAdjective(new Adjective("WORD0436","soft"));
		addAdjective(new Adjective("WORD0437","fluffy"));
		addAdjective(new Adjective("WORD0438","colorful"));

	}
	
	public void addCharacterGoal(CharacterGoal cg)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("CharacterGoalID", cg.getID());
		cv.put("Name", cg.getString());
		cv.put("Action", cg.getAction());
		cv.put("Agens", cg.getAgens());
		cv.put("Patiens", cg.getPatiens());
		cv.put("isNegated", cg.isNegated());
		
		database.insert(TableCreator.TABLE_CHARACTER_GOALS, null, cv);
	}	
	public Vector<CharacterGoal> getCharacterGoals()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<CharacterGoal> cgs = new Vector<CharacterGoal>();
		
		String selectQuery = "SELECT * FROM tableCharacterGoals";
		Cursor cursor = database.rawQuery(selectQuery, null);

		if(cursor.moveToFirst()){
			do{
				CharacterGoal cg = new CharacterGoal();
				String bool;
				cg.setID(cursor.getString(0));
				cg.setString(cursor.getString(1));
				cg.setAction(cursor.getString(2));
				cg.setAgens(cursor.getString(3));
				cg.setPatiens(cursor.getString(4));
				bool = cursor.getString(5);
				if(bool.equals("0")){
					bool = "false";
				} else if(bool.equals("1")){
					bool = "true";
				}
				cg.setNegated(Boolean.valueOf(bool));
				Log.d("CG", cg.getID() + " " + cg.getString());
				cgs.add(cg);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return cgs;
	}	
	
	public void InitializeCharacterGoals()
	{
		addCharacterGoal(new CharacterGoal("CGOL0001","secondary character talks to main character","WORD0098","%adult%","%child%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0002","main character talks to secondary character","WORD0098","%child%","%adult%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0003","secondary character talks to another secondary character","WORD0098","%child2%","%adult%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0004","character wants","WORD0034","%child%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0005","character does not want","WORD0034","%child%","",Boolean.valueOf(true)));
		addCharacterGoal(new CharacterGoal("CGOL0006","character does","WORD0081","%child%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0007","character does not do","WORD0081","%child%","",Boolean.valueOf(true)));
		addCharacterGoal(new CharacterGoal("CGOL0008","character feels","WORD0040","%child%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0009","character reacts","WORD0099","%child%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0010","secondary character sees main character","WORD0084","%adult%","%child%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0011","main character sees secondary character","WORD0084","%child%","%adult%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0012","secondary character sees another secondary character","WORD0084","%child2%","%adult%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0013","result of the lesson","WORD0100 WORD0117","%lesson%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0014","lesson is","WORD0083","%lesson%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0015","secondary character insists main character","WORD0110","%adult%","%child%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0016","main character insists secondary character","WORD0110","%child%","%adult%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0017","secondary character insists another secondary character","WORD0110","%child2%","%adult%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0018","secondary character does with main character","WORD0081 WORD0116","%adult%","%child%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0019","main character does with secondary character","WORD0081 WORD0116","%child%","%adult%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0020","secondary character does with another secondary character","WORD0081 WORD0116","%child2%","%adult%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0021","secondary character does to main character","WORD0081 WORD0117","%adult%","%child%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0022","main character does to secondary character","WORD0081 WORD0117","%child%","%adult%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0023","secondary character does to another secondary character","WORD0081 WORD0117","%child2%","%adult%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0024","secondary character does for main character","WORD0081 WORD0119","%adult%","%child%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0025","main character does for secondary character","WORD0081 WORD0119","%child%","%adult%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0026","secondary character does for another secondary character","WORD0081 WORD0119","%child2%","%adult%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0027","character is","WORD0083","%child%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0028","secondary character asks main character","WORD0122","%adult%","%child%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0029","main character asks secondary character","WORD0122","%child%","%adult%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0030","secondary character asks another secondary character","WORD0122","%child2%","%adult%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0031","character can do","WORD0268 WORD0081","%child%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0032","character cannot do","WORD0268 WORD0081","%child%","",Boolean.valueOf(true)));
		addCharacterGoal(new CharacterGoal("CGOL0033","character should have","WORD0136 WORD0130 WORD0081","%child%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0034","secondary character does from main character","WORD0081 WORD0269","%adult%","%child%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0035","main character does from secondary character","WORD0081 WORD0269","%child%","%adult%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0036","secondary character does from another secondary character","WORD0081 WORD0269","%child2%","%adult%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0037","main character sees object","WORD0084","%child%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0038","Object is","WORD0083","%object%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0039","secondary character reminds main character","WORD0270","%adult%","%child%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0040","main character help secondary character","WORD0271","%child%","%adult%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0041","character should have not","WORD0136 WORD0130 WORD0081","%child%","",Boolean.valueOf(true)));
		addCharacterGoal(new CharacterGoal("CGOL0042","object should have","WORD0136 WORD0130 WORD0081","%object%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0043","object should have not","WORD0136 WORD0130 WORD0081","%object%","",Boolean.valueOf(true)));
		addCharacterGoal(new CharacterGoal("CGOL0044","character has","WORD0130","%child%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0045","character continues doing something","WORD0281","%child%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0046","character \"do\" too much","WORD0081 WORD0418","%child%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0047","character not \"do\" too much","WORD0081 WORD0418","%child%","",Boolean.valueOf(true)));
		addCharacterGoal(new CharacterGoal("CGOL0048","character likes doing something","WORD0284","%child%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0049","main character gives secondary character","WORD0060","%child%","%adult%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0050","secondary character gives main character","WORD0060","%adult%","%child%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0051","character does on the next day","WORD0081 WORD0125 WORD0311 WORD0147","%child%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0052","character went with secondary character","WORD0324 WORD0116 WORD0117","%child%","%adult%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0053","character went","WORD0324 WORD0117","%child%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0054","secondary character introduces to main character","WORD0328 WORD0117","%adult%","%child%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0055","Object is not","WORD0083","%object%","",Boolean.valueOf(true)));
		addCharacterGoal(new CharacterGoal("CGOL0056","character should not \"do\" too much","WORD0136 WORD0081 WORD0418","%child%","",Boolean.valueOf(true)));
		addCharacterGoal(new CharacterGoal("CGOL0057","character should \"do\"","WORD0136 WORD0081","%child%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOL0058","character should not \"do\"","WORD0136 WORD0081","%child%","",Boolean.valueOf(true)));
		addCharacterGoal(new CharacterGoal("CGOL0059","character sees","WORD0084","%child%","",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOLASKP","ask if everything is okay","WORD0122","%adult%","%child%",Boolean.valueOf(false)));
		addCharacterGoal(new CharacterGoal("CGOLTITL","title","WORD0176","%child%","",Boolean.valueOf(false)));
	}
	
	public void addIGCharacter(IGCharacter igc)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("CharacterID", igc.getID());
		cv.put("Name", igc.getString());
		cv.put("Gender", igc.getGender());
		cv.put("AnimalType", igc.getType());
		cv.put("Role", igc.getRole());
		cv.put("MotherCharacterID", igc.getMotherCharacterID());
		cv.put("FatherCharacterID", igc.getFatherCharacterID());
		cv.put("ImageFilePath", igc.getImageFilePath());
		
		database.insert(TableCreator.TABLE_IGCHARACTERS, null, cv);
	}
	
	public Vector<IGCharacter> getIGCharacters()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<IGCharacter> IGCharacters = new Vector<IGCharacter>();
		
		String selectQuery = "SELECT * FROM tableIGCharacters";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				IGCharacter igc = new IGCharacter();
				igc.setID(cursor.getString(0));
				igc.setString(cursor.getString(1));
				igc.setGender(cursor.getString(2));
				igc.setType(cursor.getString(3));
				igc.setRole(cursor.getString(4));
				igc.setMotherCharacterID(cursor.getString(5));
				igc.setFatherCharacterID(cursor.getString(6));
				igc.setImageFilePath(cursor.getString(7));
				Log.d("IGCharacters", igc.getString());
				IGCharacters.add(igc);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return IGCharacters;
	}	
	
	public void InitializeIGCharacters()
	{
		addIGCharacter(new IGCharacter("CH001","Calvin","m","cat","WORD0085","CH022","CH021","/Images/Stickers/Characters/Children/catBoyImage.png"));
		addIGCharacter(new IGCharacter("CH002","Cathy","f","cat","WORD0085","CH022","CH021","/Images/Stickers/Characters/Children/catGirlImage.png"));
		addIGCharacter(new IGCharacter("CH003","Roy","m","chicken","WORD0085","CH024","CH023","/Images/Stickers/Characters/Children/chickenBoyImage.png"));
		addIGCharacter(new IGCharacter("CH004","Henny","f","chicken","WORD0085","CH024","CH023","/Images/Stickers/Characters/Children/chickenGirlImage.png"));
		addIGCharacter(new IGCharacter("CH005","Daniel","m","dog","WORD0085","CH026","CH025","/Images/Stickers/Characters/Children/dogBoyImage.png"));
		addIGCharacter(new IGCharacter("CH006","Denise","f","dog","WORD0085","CH026","CH025","/Images/Stickers/Characters/Children/dogGirlImage.png"));
		addIGCharacter(new IGCharacter("CH007","Edward","m","elephant","WORD0085","CH028","CH027","/Images/Stickers/Characters/Children/elephantBoyImage.png"));
		addIGCharacter(new IGCharacter("CH008","Ellen","f","elephant","WORD0085","CH028","CH027","/Images/Stickers/Characters/Children/elephantGirlImage.png"));
		addIGCharacter(new IGCharacter("CH009","George","m","giraffe","WORD0085","CH030","CH029","/Images/Stickers/Characters/Children/giraffeBoyImage.png"));
		addIGCharacter(new IGCharacter("CH010","Geena","f","giraffe","WORD0085","CH030","CH029","/Images/Stickers/Characters/Children/giraffeGirlImage.png"));
		addIGCharacter(new IGCharacter("CH011","Leo","m","lion","WORD0085","CH032","CH031","/Images/Stickers/Characters/Children/lionBoyImage.png"));
		addIGCharacter(new IGCharacter("CH012","Lenny","f","lion","WORD0085","CH032","CH031","/Images/Stickers/Characters/Children/lionGirlImage.png"));
		addIGCharacter(new IGCharacter("CH013","Porky","m","pig","WORD0085","CH034","CH033","/Images/Stickers/Characters/Children/pigBoyImage.png"));
		addIGCharacter(new IGCharacter("CH014","Pinky","f","pig","WORD0085","CH034","CH033","/Images/Stickers/Characters/Children/pigGirlImage.png"));
		addIGCharacter(new IGCharacter("CH015","Robbie","m","rabbit","WORD0085","CH036","CH035","/Images/Stickers/Characters/Children/rabbitBoyImage.png"));
		addIGCharacter(new IGCharacter("CH016","Rizzy","f","rabbit","WORD0085","CH036","CH035","/Images/Stickers/Characters/Children/rabbitGirlImage.png"));
		addIGCharacter(new IGCharacter("CH017","Simon","m","sheep","WORD0085","CH038","CH037","/Images/Stickers/Characters/Children/sheepBoyImage.png"));
		addIGCharacter(new IGCharacter("CH018","Sally","f","sheep","WORD0085","CH038","CH037","/Images/Stickers/Characters/Children/sheepGirlImage.png"));
		addIGCharacter(new IGCharacter("CH019","Toby","m","turtle","WORD0085","CH040","CH039","/Images/Stickers/Characters/Children/turtleBoyImage.png"));
		addIGCharacter(new IGCharacter("CH020","Trixie","f","turtle","WORD0085","CH040","CH039","/Images/Stickers/Characters/Children/turtleGirlImage.png"));
		addIGCharacter(new IGCharacter("CH021","Sam","m","cat","WORD0086","","","/Images/Stickers/Characters/Adult/catManImage.png"));
		addIGCharacter(new IGCharacter("CH022","Sara","f","cat","WORD0088","","","/Images/Stickers/Characters/Adult/catWomanImage.png"));
		addIGCharacter(new IGCharacter("CH023","Robert","m","chicken","WORD0086","","","/Images/Stickers/Characters/Adult/chickenManImage.png"));
		addIGCharacter(new IGCharacter("CH024","Hannah","f","chicken","WORD0086","","","/Images/Stickers/Characters/Adult/chickenWomanImage.png"));
		addIGCharacter(new IGCharacter("CH025","Andre","m","dog","WORD0090","","","/Images/Stickers/Characters/Adult/dogManImage.png"));
		addIGCharacter(new IGCharacter("CH026","Debbie","f","dog","WORD0086","","","/Images/Stickers/Characters/Adult/dogWomanImage.png"));
		addIGCharacter(new IGCharacter("CH027","Andy","m","elephant","WORD0087","","","/Images/Stickers/Characters/Adult/elephantManImage.png"));
		addIGCharacter(new IGCharacter("CH028","Edna","f","elephant","WORD0091","","","/Images/Stickers/Characters/Adult/elephantWomanImage.png"));
		addIGCharacter(new IGCharacter("CH029","Bill","m","giraffe","WORD0086","","","/Images/Stickers/Characters/Adult/giraffeManImage.png"));
		addIGCharacter(new IGCharacter("CH030","Gloria","f","giraffe","WORD0086","","","/Images/Stickers/Characters/Adult/giraffeWomanImage.png"));
		addIGCharacter(new IGCharacter("CH031","Lucas","m","lion","WORD0089","","","/Images/Stickers/Characters/Adult/lionManImage.png"));
		addIGCharacter(new IGCharacter("CH032","Lalaine","f","lion","WORD0086","","","/Images/Stickers/Characters/Adult/lionWomanImage.png"));
		addIGCharacter(new IGCharacter("CH033","Philip","m","pig","WORD0092","","","/Images/Stickers/Characters/Adult/pigManImage.png"));
		addIGCharacter(new IGCharacter("CH034","Patricia","f","pig","WORD0086","","","/Images/Stickers/Characters/Adult/pigWomanImage.png"));
		addIGCharacter(new IGCharacter("CH035","Owen","m","rabbit","WORD0090","","","/Images/Stickers/Characters/Adult/rabbitManImage.png"));
		addIGCharacter(new IGCharacter("CH036","Francine","f","rabbit","WORD0086","","","/Images/Stickers/Characters/Adult/rabbitWomanImage.png"));
		addIGCharacter(new IGCharacter("CH037","Gary","m","sheep","WORD0086","","","/Images/Stickers/Characters/Adult/sheepManImage.png"));
		addIGCharacter(new IGCharacter("CH038","Audrey","f","sheep","WORD0091","","","/Images/Stickers/Characters/Adult/sheepWomanImage.png"));
		addIGCharacter(new IGCharacter("CH039","Nicolas","m","turtle","WORD0086","","","/Images/Stickers/Characters/Adult/turtleManImage.png"));
		addIGCharacter(new IGCharacter("CH040","Michelle","f","turtle","WORD0086","","","/Images/Stickers/Characters/Adult/turtleWomanImage.png"));
	}
	
	public void addIGObject(IGObject igo)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("IGObjectID", igo.getID());
		cv.put("Name", igo.getString());
		cv.put("ImageFilePath", igo.getImageFilePath());
		cv.put("ObjectWord", igo.getObjectWord());
		
		database.insert(TableCreator.TABLE_IGOBJECTS, null, cv);
	}
	
	public Vector<IGObject> getIGObjects()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<IGObject> IGObjects = new Vector<IGObject>();
		
		String selectQuery = "SELECT * FROM tableIGObjects";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				IGObject igo = new IGObject();				
				igo.setID(cursor.getString(0));
				igo.setString(cursor.getString(1));
				igo.setImageFilePath(cursor.getString(2));
				igo.setObjectWord(cursor.getString(3));
				Log.d("IGObjects", igo.getObjectWord());
				IGObjects.add(igo);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return IGObjects;
	}	
	
	public void InitializeIGObjects()
	{
		addIGObject(new IGObject("OB001","WORD0179 WORD0180","/Images/Stickers/Objects/alarm_clock.png","alarm clock"));
		addIGObject(new IGObject("OB002","WORD0181","/Images/Stickers/Objects/apple.png","apple"));
		addIGObject(new IGObject("OB003","WORD0182","/Images/Stickers/Objects/backpack.png","backpack"));
		addIGObject(new IGObject("OB004","WORD0183","/Images/Stickers/Objects/bananas.png","bananas"));
		addIGObject(new IGObject("OB005","WORD0184 WORD0168","/Images/Stickers/Objects/beachball.png","beachball"));
		addIGObject(new IGObject("OB006","WORD0185","/Images/Stickers/Objects/book.png","book"));
		addIGObject(new IGObject("OB007","WORD0186","/Images/Stickers/Objects/brocollis.png","brocollis"));
		addIGObject(new IGObject("OB008","WORD0187","/Images/Stickers/Objects/brush.png","brush"));
		addIGObject(new IGObject("OB009","WORD0188","/Images/Stickers/Objects/cake.png","cake"));
		addIGObject(new IGObject("OB010","WORD0189","/Images/Stickers/Objects/candies.png","candies"));
		addIGObject(new IGObject("OB011","WORD0190","/Images/Stickers/Objects/carrots.png","carrots"));
		addIGObject(new IGObject("OB012","WORD0191","/Images/Stickers/Objects/chair.png","chair"));
		addIGObject(new IGObject("OB013","WORD0192","/Images/Stickers/Objects/doll.png","doll"));
		addIGObject(new IGObject("OB014","WORD0193 WORD0194","/Images/Stickers/Objects/fried_chicken.png","fried chicken"));
		addIGObject(new IGObject("OB015","WORD0195 WORD0163 WORD0059","/Images/Stickers/Objects/glass_of_water.png","glass of water"));
		addIGObject(new IGObject("OB016","WORD0197","/Images/Stickers/Objects/lamp.png","lamp"));
		addIGObject(new IGObject("OB017","WORD0198","/Images/Stickers/Objects/pillow.png","pillow"));
		addIGObject(new IGObject("OB018","WORD0168","/Images/Stickers/Objects/redBall.png","ball"));
		addIGObject(new IGObject("OB019","WORD0111","/Images/Stickers/Objects/rubber_ducky.png","rubber ducky"));
		addIGObject(new IGObject("OB020","WORD0199 WORD0218 WORD0200","/Images/Stickers/Objects/salt_and_pepper.png","salt and pepper"));
		addIGObject(new IGObject("OB021","WORD0201","/Images/Stickers/Objects/seesaw.png","seesaw"));
		addIGObject(new IGObject("OB022","WORD0202","/Images/Stickers/Objects/stethoscope.png","stethoscope"));
		addIGObject(new IGObject("OB023","WORD0203 WORD0204","/Images/Stickers/Objects/swingset.png","swing set"));
		addIGObject(new IGObject("OB024","WORD0205 WORD0204","/Images/Stickers/Objects/tea set.png","tea set"));
		addIGObject(new IGObject("OB025","WORD0206","/Images/Stickers/Objects/television.png","television"));
		addIGObject(new IGObject("OB026","WORD0207","/Images/Stickers/Objects/thermometer.png","thermometer"));
		addIGObject(new IGObject("OB027","WORD0209 WORD0218 WORD0208","/Images/Stickers/Objects/toothpaste_toothbrush.png","toothbrush and toothpaste"));
		addIGObject(new IGObject("OB028","WORD0368 WORD0210","/Images/Stickers/Objects/toy_blocks.png","toy blocks"));
		addIGObject(new IGObject("OB029","WORD0368 WORD0211","/Images/Stickers/Objects/toy_car.png","toy car"));
		addIGObject(new IGObject("OB030","WORD0368 WORD0212","/Images/Stickers/Objects/toy_horse.png","toy horse"));
		addIGObject(new IGObject("OB031","WORD0368 WORD0027","/Images/Stickers/Objects/toy_truck.png","toy truck"));
		addIGObject(new IGObject("OB032","WORD0213","/Images/Stickers/Objects/tricycle.png","tricycle"));
		addIGObject(new IGObject("OB033","WORD0214","/Images/Stickers/Objects/wallet.png","wallet"));
		addIGObject(new IGObject("OB034","WORD0219","/Images/Stickers/Objects/soap.png","soap"));
		addIGObject(new IGObject("OB035","WORD0215","/Images/Stickers/Objects/weighing_scale.png","weighing scale"));
		addIGObject(new IGObject("OB036","WORD0217","/Images/Stickers/Objects/spaghetti.png","spaghetti"));
		addIGObject(new IGObject("OB037","WORD0196","/Images/Stickers/Objects/bread.png","bread"));
	}
	
	public void addAuthorGoal(AuthorGoal ag)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("AuthorGoalID", ag.getID());
		cv.put("Name", ag.getString());
		cv.put("Goal", ag.getGoal());
		cv.put("Consequence", ag.getConsequence());
		
		database.insert(TableCreator.TABLE_AUTHOR_GOALS, null, cv);
	}
	
	public Vector<AuthorGoal> getAuthorGoals()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<AuthorGoal> authorGoals = new Vector<AuthorGoal>();
		
		String selectQuery = "SELECT * FROM tableAuthorGoals";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				AuthorGoal ag = new AuthorGoal();				
				ag.setID(cursor.getString(0));
				ag.setString(cursor.getString(1));
				ag.setGoal(cursor.getString(2));
				ag.setConsequence(cursor.getString(3));
				Log.d("AuthorGoals", ag.getID());
				authorGoals.add(ag);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return authorGoals;
	}	
	
	public void InitializeAuthorGoals()
	{
		addAuthorGoal(new AuthorGoal("AUTH0001","Inform Rule","CGOL0006(Target:%activity%);","CGOL0001(Target:%lesson%);"));
		addAuthorGoal(new AuthorGoal("AUTH0002","Ignore Rule","CGOL0005(Target:%lesson%);","CGOL0045(Target:%activity%);"));
		addAuthorGoal(new AuthorGoal("AUTH0003","Discomforted Character","CGOL0007(Target:%lesson%);","CGOL0008(Target:%ontoAction(%negate(%lesson%)%,WORD0075)%);CGOL0008(Target:%ontoAction(%goal.target%,WORD0393)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0004","Discover Misconduct","CGOL0006(Target:%ontoEvent(%goal.target%,WORD0076)%);","CGOL0059(Agens:%job%,Target:CGOL0027(Agens:%child%,Target:%goal.target%));CGOLASKP(Target:WORD0424 WORD0425 WORD0428 WORD0426);"));
		addAuthorGoal(new AuthorGoal("AUTH0005","Inform Lesson","CGOL0001(Target:%lesson%);","CGOL0004(Target:%lesson%);"));
		addAuthorGoal(new AuthorGoal("AUTH0006","Do Lesson","CGOL0006(Target:%lesson%,Instrument:%object%);","CGOL0013(Target:%ontoAction(%lesson%,WORD0391)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0007","Realize Benefit","CGOL0014(Target:%ontoThings(%lesson%,WORD0121)%);","CGOL0027(Target:WORD0001);"));
		addAuthorGoal(new AuthorGoal("AUTH0008","Resolution","CGOL0006(Target:%lesson%);","null"));
		addAuthorGoal(new AuthorGoal("AUTH0009","Inform Rule","CGOL0006(Target:%ontoAction(%instrument%)%,Instrument:%object%);","CGOL0028(Agens:%child2%,Target:%lesson%);"));
		addAuthorGoal(new AuthorGoal("AUTH0010","Ignore Rule","CGOL0005(Target:%lesson%);","CGOL0045(Target:%ontoAction(%instrument%)%,Instrument:%object%);"));
		addAuthorGoal(new AuthorGoal("AUTH0011","Character Fights","CGOL0015(Agens:%child2%,Target:%lesson%);","CGOL0019(Patiens:%child2%,Target:WORD0101);"));
		addAuthorGoal(new AuthorGoal("AUTH0012","Hurt Someone","CGOL0008(Agens:%child2%,Target:%ontoAction(%goal.target%,WORD0393)%);","CGOL0006(Agens:%child2%,Target:%ontoEvent(%goal.target%,WORD0076)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0013","Discover Misconduct","CGOL0003(Target:CGOL0005(Target:%lesson%));","CGOL0001(Target:%lesson%);"));
		addAuthorGoal(new AuthorGoal("AUTH0014","Discomforted Character","CGOL0008(Target:%ontoThings(%negate(%lesson%)%,WORD0075)%);","CGOL0022(Patiens:%child2%,Target:WORD0109);CGOL0006(Agens:%child2%,Patiens:%child%,Target:%ontoEvent(%goal.target%)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0015","Do Lesson","CGOL0019(Patiens:%child2%,Target:%lesson%);","CGOL0014(Target:%ontoThings(%lesson%,WORD0121)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0016","Inform Rule","CGOL0001(Target:CGOL0058(Target:%negate(%lesson%)%));","CGOL0004(Target:%ontoEvent(%negate(%lesson%)%)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0017","Ignore Rule","CGOL0006(Target:%ontoEvent(%goal.target%,WORD0272)%);CGOL0013(Agens:%goal.target%,Target:%ontoGeneric(%goal.target%,WORD0273)%);","CGOL0004(Target:%ontoGoal(%ontoThings(%object%)%)%,Patiens:%object%);"));
		addAuthorGoal(new AuthorGoal("AUTH0018","Discomforted Character","CGOL0032(Target:%goal.target%,Patiens:%goal.patiens%);CGOL0038(Agens:%goal.patiens%,Target:WORD0274);","CGOL0008(Target:WORD0002);"));
		addAuthorGoal(new AuthorGoal("AUTH0019","Ignore Rule","CGOL0006(Target:%goal.target%);","CGOL0006(Target:%ontoGeneric(%goal.target%,WORD0273)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0020","Inform Reason","CGOL0002(Target:CGOL0032(Target:%ontoGoal(%ontoThings(%object%)%)%,Patiens:%object%));","CGOL0001(Target:CGOL0033(Target:%ontoGoal(WORD0275)%));CGOL0022(Target:WORD0109);CGOL0006(Agens:%adult%,Patiens:%child%,Target:%ontoEvent(%goal.target%)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0021","Fix Problem","CGOL0040(Agens:%adult%,Patiens:%child%,Target:%ontoEvent(%lesson%)%);","CGOL0006(Target:WORD0045,Patiens:WORD0274 WORD0097);CGOL0027(Target:WORD0001,Instrument:CGOL0006(Target:WORD0045,Patiens:WORD0274 WORD0097));"));
		addAuthorGoal(new AuthorGoal("AUTH0022","Realize fault","CGOL0001(Target:CGOL0041(Target:%negate(%lesson%)%));","CGOL0014(Target:%ontoThings(%lesson%,WORD0121)%);CGOL0027(Target:WORD0001);"));
		addAuthorGoal(new AuthorGoal("AUTH0023","Discomforted Character","CGOL0006(Agens:%adult%,Target:WORD0286,Patiens:%object%);CGOL0038(Agens:WORD0065,Target:WORD0291);CGOL0004(Target:%ontoGoal(WORD0250)%,Patiens:%object%);","CGOL0032(Target:%ontoGoal(WORD0250)%,Patiens:%object%);CGOL0006(Target:%negate(%lesson%)%);CGOL0006(Target:%ontoAction(%goal.target%,WORD0273)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0024","Inform Rule","CGOL0006(Patiens:%object%,Target:%activity%);CGOL0059(Agens:%adult%,Target:CGOL0006(Patiens:%object%,Target:%activity%));","CGOL0001(Target:%lesson%);"));
		addAuthorGoal(new AuthorGoal("AUTH0025","Ignore Rule","CGOL0045(Patiens:%object%,Target:%activity%);","CGOL0006(Patiens:%object%,Target:%ontoAction(%patiens%,WORD0377 WORD0163 WORD0378)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0026","Discomforted Character","CGOL0006(Target:%ontoGeneric(%goal.target%,WORD0334)%);","CGOL0035(Target:%ontoGoal(%goal.target%)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0027","Discover Misconduct","CGOL0037(Agens:%adult%,Target:CGOL0038(Target:%ontoGeneric(WORD0377 WORD0163 WORD0378,WORD0285)%));","CGOL0006(Agens:%adult%,Patiens:%child%,Target:WORD0033);CGOL0006(Patiens:%adult%,Target:%ontoGoal(%goal.target%)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0028","Inform Lesson","CGOL0001(Target:CGOL0033(Target:%ontoGoal(WORD0275)%));","CGOL0008(Target:WORD0261);CGOL0022(Target:WORD0109);CGOL0006(Agens:%adult%,Patiens:%child%,Target:%ontoEvent(%goal.target%)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0029","Do Lesson","CGOL0006(Agens:%adult%,Patiens:%object%,Target:%ontoGeneric(WORD0377 WORD0163 WORD0378,WORD0333)%);","CGOL0040(Target:%goal.target%);"));
		addAuthorGoal(new AuthorGoal("AUTH0030","Realize Benefit","CGOL0039(Target:%lesson%);","CGOL0014(Target:%ontoThings(%lesson%,WORD0121)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0031","Discover Misconduct","CGOL0008(Target:WORD0002);CGOLASKP(Target:WORD0424 WORD0425 WORD0428 WORD0426);","CGOL0002(Target:CGOL0006(Target:%negate(%lesson%)%));CGOL0002(Target:CGOL0006(Target:%ontoAction(%negate(%lesson%)%,WORD0273)%));"));
		addAuthorGoal(new AuthorGoal("AUTH0032","Fix Problem","CGOL0001(Target:CGOL0013(Agens:%negate(%lesson%)%,Target:%ontoAction(%negate(%lesson%)%,WORD0273)%));","CGOL0001(Target:CGOL0006(Agens:%adult%,Target:WORD0439 WORD0415,Patiens:%object%));CGOL0006(Patiens:%adult%,Target:WORD0289);"));
		addAuthorGoal(new AuthorGoal("AUTH0033","Resolution","CGOL0007(Target:%negate(%lesson%)%);","null"));
		addAuthorGoal(new AuthorGoal("AUTH0034","Inform Rule","CGOL0006(Target:%activity%,Patiens:%ontoEvent(%negate(%lesson%)%)%);","CGOL0001(Target:CGOL0056(Target:%activity%,Patiens:%goal.patiens%));"));
		addAuthorGoal(new AuthorGoal("AUTH0035","Ignore Rule","CGOL0048(Target:%activity%,Patiens:%ontoThings(%ontoEvent(%negate(%lesson%)%)%)%);","CGOL0045(Target:%goal.target%,Patiens:%goal.patiens%);"));
		addAuthorGoal(new AuthorGoal("AUTH0036","Discomforted Character","CGOL0046(Target:%goal.target%,Patiens:%goal.patiens%);","CGOL0044(Target:%ontoThings(%goal.patiens%,WORD0273)%);CGOL0008(Target:%ontoAction(%goal.target%,WORD0075)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0037","Inform Lesson","CGOL0002(Target:CGOL0046(Target:%activity%,Patiens:%ontoEvent(%negate(%lesson%)%)%));","CGOL0001(Target:CGOL0033(Target:%ontoGoal(WORD0275)%));CGOL0022(Target:WORD0109);CGOL0006(Agens:%adult%,Patiens:%child%,Target:%ontoEvent(%goal.target%)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0038","Do Lesson","CGOL0050(Target:%ontoEvent(WORD0040 WORD0393)%);CGOL0006(Target:%ontoAction(%goal.target%,WORD0299)%);CGOL0006(Patiens:%adult%,Target:WORD0289);","CGOL0001(Target:CGOL0057(Target:%lesson%));CGOL0014(Target:%ontoGeneric(%lesson%,WORD0300)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0039","Adult takes child to a place","CGOL0019(Target:%activity%);","CGOL0021(Agens:%adult%,Target:%ontoEvent(%activity%)%);CGOL0006(Agens:%adult%,Target:WORD0327);"));
		addAuthorGoal(new AuthorGoal("AUTH0040","Child is discomforted","CGOL0006(Target:%negate(%lesson%)%);CGOL0005(Target:WORD0324 WORD0258,Patiens:%adult%);","CGOL0006(Target:%ontoEvent(%negate(%lesson%)%,WORD0076)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0041","Discover discomfort","CGOL0059(Agens:%job%,Target:CGOL0027(Agens:%child%,Target:%goal.target%));CGOLASKP(Agens:%job%,Target:WORD0424 WORD0425 WORD0428 WORD0426);CGOL0002(Patiens:%job%,Target:CGOL0006(Target:%negate(%lesson%)%));","CGOL0001(Agens:%job%,Target:%lesson%);CGOL0014(Target:%ontoThings(%lesson%,WORD0121)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0042","Fix problem","CGOL0004(Target:%lesson%);","CGOL0006(Target:%lesson%);"));
		addAuthorGoal(new AuthorGoal("AUTH0043","Child Reacts","CGOL0006(Target:%ontoGeneric(%goal.target%,WORD0333)%);","CGOL0027(Target:%ontoEvent(%goal.target%,WORD0334)%);CGOL0059(Agens:%job%,Target:CGOL0027(Agens:%child%,Target:%goal.target%));"));
		addAuthorGoal(new AuthorGoal("AUTH0044","Child Confirms","CGOLASKP(Agens:%job%,Target:WORD0427 WORD0428 WORD0425);","CGOL0002(Patiens:%job%,Target:CGOL0008(Target:WORD0307));CGOL0001(Agens:%job%,Target:CGOL0057(Target:%lesson%));"));
		addAuthorGoal(new AuthorGoal("AUTH0045","Adult switches off the bedroom light","CGOL0006(Agens:%adult%,Target:WORD0400 WORD0346 WORD0347);","CGOL0038(Agens:WORD0143,Target:%ontoEvent(%goal.target%)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0046","Child is introduced to the class","CGOL0054(Agens:%job%,Patiens:%child%Target:WORD0329);","CGOL0006(Target:%ontoAction(WORD0330)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0047","Adult comforts the child","CGOL0013(Target:%ontoAction(%lesson%,WORD0244)%);","CGOL0021(Target:%ontoEvent(WORD0244,WORD0391)%);CGOL0027(Target:WORD0420);"));
		addAuthorGoal(new AuthorGoal("AUTH0048","Adult takes child to a place","CGOL0019(Target:%activity%);CGOL0006(Target:%ontoGeneric(%object%)%);","CGOL0008(Target:%ontoGeneric(%negate(%lesson%)%,WORD0334)%);CGOL0005(Target:%activity%);"));
		addAuthorGoal(new AuthorGoal("AUTH0049","Child is frightened","CGOL0037(Target:%object%);","CGOL0006(Target:%negate(%lesson%)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0050","Child reacts","CGOL0004(Target:WORD0324 WORD0352);","CGOL0006(Target:%ontoEvent(%negate(%lesson%)%,WORD0076)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0051","Explain the purpose of the object","CGOL0001(Agens:%job%,Target:CGOL0055(Target:%ontoThings(%object%)%));","CGOL0006(Agens:%job%,Target:%ontoFunction(%object%,WORD0333)%,Instrument:%object%);"));
		addAuthorGoal(new AuthorGoal("AUTH0052","Check-up","CGOL0006(Agens:%job%,Patiens:%child%,Target:WORD0353 WORD0262);","CGOL0001(Target:CGOL0006(Target:%ontoAction(%goal.target%,WORD0354 WORD0411)%));"));
		addAuthorGoal(new AuthorGoal("AUTH0053","Child is rewarded","CGOL0006(Agens:%job%,Patiens:%child%,Target:WORD0402,Instrument:%ontoThings(WORD0355)%);","CGOL0027(Target:%ontoEvent(%goal.target%,WORD0334)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0054","Inform Rule","CGOL0027(Target:%activity%);","CGOL0001(Target:%lesson%);"));
		addAuthorGoal(new AuthorGoal("AUTH0055","Do Lesson","CGOL0051(Target:%lesson%,Instrument:%object%);","CGOL0013(Target:%ontoAction(%lesson%,WORD0391)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0056","Result of lesson is friendship","CGOL0013(Target:%ontoGoal(%lesson%,WORD0331)%);","CGOL0044(Target:WORD0332);"));
		addAuthorGoal(new AuthorGoal("AUTH0057","Inform Rule","CGOL0027(Target:%activity%);","CGOL0001(Target:CGOL0056(Target:%activity%));"));
		addAuthorGoal(new AuthorGoal("AUTH0058","Ignore Rule","CGOL0001(Target:%lesson%);","CGOL0048(Target:%activity%);"));
		addAuthorGoal(new AuthorGoal("AUTH0059","Ignore Rule - do another activity","CGOL0045(Target:%goal.target%);","CGOL0046(Target:%goal.target%);"));
		addAuthorGoal(new AuthorGoal("AUTH0060","Inform fault","CGOL0002(Target:CGOL0046(Target:%activity%));CGOL0002(Target:CGOL0007(Target:%lesson%));CGOL0002(Target:CGOL0008(Target:%ontoAction(%negate(%lesson%)%,WORD0075)%));","CGOL0001(Target:CGOL0033(Target:%ontoGoal(WORD0275)%));CGOL0022(Target:WORD0109);CGOL0006(Agens:%adult%,Patiens:%child%,Target:%ontoEvent(%goal.target%)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0061","Inform Rule","CGOL0006(Patiens:%object%,Target:%activity%);","CGOL0006(Patiens:%object%,Target:%ontoAction(%patiens%,WORD0377 WORD0163 WORD0378)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0062","Ignore Rule","CGOL0006(Target:%ontoGeneric(%goal.target%,WORD0334)%);","CGOL0037(Agens:%adult%,Target:CGOL0038(Target:%ontoGeneric(WORD0377 WORD0163 WORD0378,WORD0285)%));"));
		addAuthorGoal(new AuthorGoal("AUTH0063","Discomforted Character","CGOL0002(Target:CGOL0006(Agens:%child2%,Patiens:%object%,Target:%ontoAction(%patiens%,WORD0377 WORD0163 WORD0378)%));CGOL0002(Agens:%child2%,Target:CGOL0007(Agens:%child2%,Patiens:%object%,Target:%ontoAction(%patiens%,WORD0377 WORD0163 WORD0378)%));","CGOL0027(Agens:%child2%,Target:WORD0002);CGOL0001(Patiens:%child2%,Target:CGOL0027(Agens:%child2%,Target:%ontoGeneric(%ontoAction(%object%,WORD0377 WORD0163 WORD0378)%,WORD0382)%));"));
		addAuthorGoal(new AuthorGoal("AUTH0064","Discover Misconduct","CGOL0006(Agens:%child2%,Target:%ontoEvent(WORD0162 WORD0383,WORD0076)%);","CGOL0008(Target:%ontoAction(%negate(%lesson%)%,WORD0075)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0065","Inform Lesson","CGOL0002(Target:CGOL0006(Patiens:%object%,Target:%ontoAction(%patiens%,WORD0377 WORD0163 WORD0378)%));","CGOL0001(Target:CGOL0033(Target:%lesson%));"));
		addAuthorGoal(new AuthorGoal("AUTH0066","Do Lesson","CGOL0022(Target:WORD0109);CGOL0006(Agens:%adult%,Patiens:%child%,Target:%ontoEvent(%goal.target%)%);","CGOL0022(Patiens:%child2%,Target:WORD0109);CGOL0006(Agens:%child2%,Patiens:%child%,Target:%ontoEvent(%goal.target%)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0067","Realize Benefit","CGOL0001(Target:%lesson%);","CGOL0001(Target:CGOL0014(Target:%ontoThings(%lesson%,WORD0121)%));"));
		addAuthorGoal(new AuthorGoal("AUTH0068","Inform Rule","CGOL0019(Target:%activity%);","CGOL0001(Target:%ontoGeneric(%lesson%)%);CGOL0004(Target:%ontoGeneric(%background%,WORD0333)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0069","Ignore Rule","CGOL0037(Target:%object%);CGOL0004(Target:%object%);","CGOL0053(Target:%ontoSpatial(%ontoThings(%object%)%)%);CGOL0006(Target:%ontoGeneric(%goal.target%,WORD0333)%);CGOL0006(Target:WORD0327,Patiens:%adult%);"));
		addAuthorGoal(new AuthorGoal("AUTH0070","Discomforted Character","CGOL0032(Target:WORD0045,Patiens:%adult%);CGOL0027(Target:%negate(%goal.target%)%);","CGOL0006(Target:%ontoEvent(%negate(WORD0045)%,WORD0334)%);"));
		addAuthorGoal(new AuthorGoal("AUTH0071","Discover Misconduct","CGOL0006(Target:%ontoEvent(%goal.target%,WORD0076)%);","CGOL0059(Agens:%job%,Target:CGOL0027(Target:%goal.target%));CGOLASKP(Agens:%job%,Target:WORD0424 WORD0425 WORD0428 WORD0426);"));
		addAuthorGoal(new AuthorGoal("AUTH0072","Inform Lesson","CGOL0002(Patiens:%job%,Target:CGOL0027(Target:WORD0274));","CGOL0040(Agens:%job%,Patiens:%child%);CGOL0025(Target:%ontoEvent(WORD0045)%);CGOL0024(Target:%goal.target%);"));
		addAuthorGoal(new AuthorGoal("AUTH0073","Do Lesson","CGOL0012(Agens:%job%,Target:%ontoAction(%ontoEvent(WORD0045)%,WORD0076)%);CGOL0010(Instrument:%job%);","CGOL0001(Target:CGOL0033(Target:%lesson%));"));
		addAuthorGoal(new AuthorGoal("AUTH0074","Realize Benefit","CGOL0022(Target:WORD0109);","CGOL0006(Patiens:%job%,Target:WORD0289);"));
		addAuthorGoal(new AuthorGoal("AUTH0075","Resolution","CGOL0001(Target:CGOL0014(Target:%ontoThings(%lesson%,WORD0121)%));","null"));
		addAuthorGoal(new AuthorGoal("AUTH0076","Inform Rule","CGOL0006(Target:%activity%,Instrument:%ontoThings(%ontoEvent(%activity%)%)%);","CGOL0001(Target:%lesson%);"));
		addAuthorGoal(new AuthorGoal("AUTH0077","Ignore Rule","CGOL0001(Target:%lesson%);","CGOL0005(Target:%lesson%);"));
	}
	
	public void addBackground(Background bg)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("BackgroundID", bg.getBackgroundID());
		cv.put("Name", bg.getString());
		cv.put("Theme", bg.getTheme());
		cv.put("BackgroundWord", bg.getBackgroundWord());
		cv.put("ObjectsIncluded", bg.getObjectsIncluded());
		cv.put("CharactersIncluded", bg.getCharactersIncluded());
		cv.put("RequiredRole", bg.getRequiredRole());
		cv.put("ImageFilePath", bg.getImageFilePath());
		
		database.insert(TableCreator.TABLE_BACKGROUNDS, null, cv);
	}
	
	public Vector<Background> getBackgrounds()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<Background> backgrounds = new Vector<Background>();
		
		String selectQuery = "SELECT * FROM tableBackgrounds";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				Background bg = new Background();				
				bg.setBackgroundID(cursor.getString(0));
				bg.setString(cursor.getString(1));
				bg.setTheme(cursor.getString(2));
				bg.setBackgroundWord(cursor.getString(3));
				bg.setObjectsIncluded(cursor.getString(4));
				bg.setCharactersIncluded(cursor.getString(5));
				bg.setRequiredRole(cursor.getString(6));
				bg.setImageFilePath(cursor.getString(7));
				Log.d("Backgrounds", bg.getBackgroundWord());
				backgrounds.add(bg);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return backgrounds;
	}	
	
	public void InitializeBackgrounds()
	{
		addBackground(new Background("BG0000051","WORD0053","THME0001;THME0002","Bathroom","OB019;OB034;OB027","CH021;CH022;CH023;CH024;CH025;CH026;CH027;CH028;CH029;CH030;CH031;CH032;CH033;CH034;CH035;CH036;CH037;CH038;CH039;CH040","","/Images/Stickers/Backgrounds/bgBathroom.png"));
		addBackground(new Background("BG0002","WORD0143","THME0006;THME0005;THME0008;THME0014;THME0003;THME0012","Bedroom","OB001;OB013;OB016;OB017;OB018;OB024;OB028;OB029;OB030;OB031","CH021;CH022;CH023;CH024;CH025;CH026;CH027;CH028;CH029;CH030;CH031;CH032;CH033;CH034;CH035;CH036;CH037;CH038;CH039;CH040","","/Images/Stickers/Backgrounds/bgBedroom.png"));
		addBackground(new Background("BG0003","WORD0364","THME0015;THME0009","Classroom","OB003;OB006;OB013;OB018;OB028;OB030","CH021;CH022;CH023;CH024;CH025;CH026;CH027;CH028;CH029;CH030;CH031;CH032;CH033;CH034;CH035;CH036;CH037;CH038;CH039;CH040","WORD0088","/Images/Stickers/Backgrounds/bgClassroom.png"));
		addBackground(new Background("BG0004","WORD0171","THME0007","Clinic","OB022;OB026;OB026","CH021;CH022;CH023;CH024;CH025;CH026;CH027;CH028;CH029;CH030;CH031;CH032;CH033;CH034;CH035;CH036;CH037;CH038;CH039;CH040","WORD0090;WORD0091","/Images/Stickers/Backgrounds/bgDentist.png"));
		addBackground(new Background("BG0005","WORD0175","THME0004;THME0013;THME0003;THME0012","DiningRoom","OB002;OB004;OB007;OB009;OB010;OB011;OB015;OB036;OB037","CH021;CH022;CH023;CH024;CH025;CH026;CH027;CH028;CH029;CH030;CH031;CH032;CH033;CH034;CH035;CH036;CH037;CH038;CH039;CH040","","/Images/Stickers/Backgrounds/bgDiningRoom.png"));
		addBackground(new Background("BG0006","WORD0172","THME0011","Mall","OB005;OB013;OB018;OB024;OB029;OB030;OB031;OB032","CH021;CH022;CH023;CH024;CH025;CH026;CH027;CH028;CH029;CH030;CH031;CH032;CH033;CH034;CH035;CH036;CH037;CH038;CH039;CH040","WORD0092;WORD0087","/Images/Stickers/Backgrounds/bgMall.png"));
		addBackground(new Background("BG0007","WORD0173","THME0011","Market","OB009;OB010","CH021;CH022;CH023;CH024;CH025;CH026;CH027;CH028;CH029;CH030;CH031;CH032;CH033;CH034;CH035;CH036;CH037;CH038;CH039;CH040","WORD0089;WORD0087","/Images/Stickers/Backgrounds/bgMarket.png"));
		addBackground(new Background("BG0008","WORD0145","THME0009","Outdoors","OB005;OB013;OB018;OB024;OB028;OB029;OB030;OB031","CH021;CH022;CH023;CH024;CH025;CH026;CH027;CH028;CH029;CH030;CH031;CH032;CH033;CH034;CH035;CH036;CH037;CH038;CH039;CH040","WORD0087","/Images/Stickers/Backgrounds/bgOutdoors.png"));
		addBackground(new Background("BG0009","WORD0174","THME0009;THME0010","Playground","OB013;OB018;OB023;OB024;OB028;OB029;OB030;OB031;OB032","CH021;CH022;CH023;CH024;CH025;CH026;CH027;CH028;CH029;CH030;CH031;CH032;CH033;CH034;CH035;CH036;CH037;CH038;CH039;CH040","","/Images/Stickers/Backgrounds/bgPlayground.png"));
	}
	
	public void addConcept(Concept c)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("OntologyID", c.getOntologyID());
		cv.put("ElementID", c.getElementID());
		cv.put("Concept", c.getConcept());
		
		database.insert(TableCreator.TABLE_CONCEPTS, null, cv);
	}
	
	public Vector<Concept> getConcepts()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<Concept> concepts = new Vector<Concept>();
		
		String selectQuery = "SELECT * FROM tableConcepts";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				Concept c = new Concept();				
				c.setOntologyID(cursor.getString(0));
				c.setElementID(cursor.getString(1));
				c.setConcept(cursor.getString(2));
				Log.d("Concepts", c.getConcept());
				concepts.add(c);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return concepts;
	}
	
	public void InitializeConcepts()
	{
		addConcept(new Concept("ONTO0001","WORD0022","play"));
		addConcept(new Concept("ONTO0002","WORD0097","toys"));
		addConcept(new Concept("ONTO0003","WORD0168","ball"));
		addConcept(new Concept("ONTO0004","WORD0120 WORD0252 WORD0021","not take bath"));
		addConcept(new Concept("ONTO0005","WORD0169 WORD0077","become dirty"));
		addConcept(new Concept("ONTO0006","WORD0073","dirt"));
		addConcept(new Concept("ONTO0007","WORD0041","itchy"));
		addConcept(new Concept("ONTO0008","WORD0075","discomfort"));
		addConcept(new Concept("ONTO0009","WORD0048","scratch"));
		addConcept(new Concept("ONTO0010","WORD0050 WORD0049","red skin"));
		addConcept(new Concept("ONTO0011","WORD0051","cry"));
		addConcept(new Concept("ONTO0012","WORD0076","reaction"));
		addConcept(new Concept("ONTO0013","WORD0252 WORD0021","take bath"));
		addConcept(new Concept("ONTO0014","WORD0072 WORD0062","smell nice"));
		addConcept(new Concept("ONTO0015","WORD0071 WORD0041 WORD0049","soothe itchy skin"));
		addConcept(new Concept("ONTO0016","WORD0391","comfort"));
		addConcept(new Concept("ONTO0017","WORD0022 WORD0116","play with"));
		addConcept(new Concept("ONTO0018","WORD0101","fight"));
		addConcept(new Concept("ONTO0019","WORD0393","hurt"));
		addConcept(new Concept("ONTO0020","WORD0005","bad"));
		addConcept(new Concept("ONTO0021","WORD0120 WORD0107","not share"));
		addConcept(new Concept("ONTO0022","WORD0103","scold"));
		addConcept(new Concept("ONTO0023","WORD0104","guilt"));
		addConcept(new Concept("ONTO0024","WORD0109","apologize"));
		addConcept(new Concept("ONTO0025","WORD0106","forgive"));
		addConcept(new Concept("ONTO0026","WORD0107","share"));
		addConcept(new Concept("ONTO0027","WORD0108","fun"));
		addConcept(new Concept("ONTO0028","WORD0110","insist"));
		addConcept(new Concept("ONTO0029","WORD0121","commendation"));
		addConcept(new Concept("ONTO0030","WORD0016","good"));
		addConcept(new Concept("ONTO0031","WORD0019","garden"));
		addConcept(new Concept("ONTO0032","WORD0142","living room"));
		addConcept(new Concept("ONTO0033","WORD0143","bedroom"));
		addConcept(new Concept("ONTO0034","WORD0177 WORD0144","private indoor"));
		addConcept(new Concept("ONTO0035","WORD0177 WORD0145","private outdoor"));
		addConcept(new Concept("ONTO0036","WORD0147","day"));
		addConcept(new Concept("ONTO0037","WORD0018","afternoon"));
		addConcept(new Concept("ONTO0038","WORD0149","school"));
		addConcept(new Concept("ONTO0039","WORD0150","morning"));
		addConcept(new Concept("ONTO0040","WORD0015","sunny"));
		addConcept(new Concept("ONTO0041","WORD0151","finish"));
		addConcept(new Concept("ONTO0042","WORD0152","night"));
		addConcept(new Concept("ONTO0043","WORD0153","evening"));
		addConcept(new Concept("ONTO0044","WORD0154","bright"));
		addConcept(new Concept("ONTO0045","WORD0155","fair"));
		addConcept(new Concept("ONTO0046","WORD0156","warm"));
		addConcept(new Concept("ONTO0047","WORD0157","chilly"));
		addConcept(new Concept("ONTO0048","WORD0158","cold"));
		addConcept(new Concept("ONTO0049","WORD0013","windy"));
		addConcept(new Concept("ONTO0050","WORD0014","rainy"));
		addConcept(new Concept("ONTO0051","WORD0159","specific"));
		addConcept(new Concept("ONTO0052","WORD0160","generic"));
		addConcept(new Concept("ONTO0053","WORD0167","guilty"));
		addConcept(new Concept("ONTO0054","WORD0053","bathroom"));
		addConcept(new Concept("ONTO0055","WORD0171","clinic"));
		addConcept(new Concept("ONTO0056","WORD0175","dining room"));
		addConcept(new Concept("ONTO0057","WORD0172","mall"));
		addConcept(new Concept("ONTO0058","WORD0173","market"));
		addConcept(new Concept("ONTO0059","WORD0174","playground"));
		addConcept(new Concept("ONTO0060","WORD0178 WORD0144","public indoor"));
		addConcept(new Concept("ONTO0061","WORD0178 WORD0145","public outdoor"));
		addConcept(new Concept("ONTO0062","WORD0251","location"));
		addConcept(new Concept("ONTO0063","WORD0020","time"));
		addConcept(new Concept("ONTO0064","WORD0192","doll"));
		addConcept(new Concept("ONTO0065","WORD0205 WORD0204","tea set"));
		addConcept(new Concept("ONTO0066","WORD0368 WORD0027","toy truck"));
		addConcept(new Concept("ONTO0067","WORD0368 WORD0210","toy blocks"));
		addConcept(new Concept("ONTO0068","WORD0368 WORD0211","toy car"));
		addConcept(new Concept("ONTO0069","WORD0368 WORD0212","toy horse"));
		addConcept(new Concept("ONTO0070","WORD0213","tricycle"));
		addConcept(new Concept("ONTO0071","WORD0203 WORD0204","swing set"));
		addConcept(new Concept("ONTO0072","WORD0055 WORD0073","remove dirt"));
		addConcept(new Concept("ONTO0073","WORD0254 WORD0049","irritated skin"));
		addConcept(new Concept("ONTO0074","WORD0197","lamp"));
		addConcept(new Concept("ONTO0075","WORD0195 WORD0163 WORD0059","glass of water"));
		addConcept(new Concept("ONTO0076","WORD0265","break"));
		addConcept(new Concept("ONTO0077","WORD0377 WORD0163 WORD0378","method of destruction"));
		addConcept(new Concept("ONTO0078","WORD0265 WORD0264","break object"));
		addConcept(new Concept("ONTO0079","WORD0083 WORD0248","be neat"));
		addConcept(new Concept("ONTO0080","WORD0083 WORD0276","be messy"));
		addConcept(new Concept("ONTO0081","WORD0277","scatter"));
		addConcept(new Concept("ONTO0082","WORD0278 WORD0272","create mess"));
		addConcept(new Concept("ONTO0083","WORD0272","mess"));
		addConcept(new Concept("ONTO0084","WORD0279","disorder"));
		addConcept(new Concept("ONTO0085","WORD0273","problem"));
		addConcept(new Concept("ONTO0086","WORD0045","find"));
		addConcept(new Concept("ONTO0087","WORD0002","sad"));
		addConcept(new Concept("ONTO0088","WORD0275","lesson"));
		addConcept(new Concept("ONTO0089","WORD0054","listen"));
		addConcept(new Concept("ONTO0090","WORD0280","obey"));
		addConcept(new Concept("ONTO0091","WORD0396 WORD0262","clean up"));
		addConcept(new Concept("ONTO0092","WORD0242 WORD0138 WORD0020","eat on time"));
		addConcept(new Concept("ONTO0093","WORD0242 WORD0290 WORD0291","eat before mealtime"));
		addConcept(new Concept("ONTO0094","WORD0242 WORD0292","eat junk food"));
		addConcept(new Concept("ONTO0095","WORD0242","eat"));
		addConcept(new Concept("ONTO0096","WORD0040 WORD0293","feel full"));
		addConcept(new Concept("ONTO0097","WORD0293 WORD0294","full stomach"));
		addConcept(new Concept("ONTO0098","WORD0285","consequence"));
		addConcept(new Concept("ONTO0099","WORD0250","food"));
		addConcept(new Concept("ONTO0100","WORD0295 WORD0296","lose appetite"));
		addConcept(new Concept("ONTO0101","WORD0297 WORD0287","skip meal"));
		addConcept(new Concept("ONTO0102","WORD0298","stomachache"));
		addConcept(new Concept("ONTO0103","WORD0242 WORD0249 WORD0416","eat healthy foods"));
		addConcept(new Concept("ONTO0104","WORD0189","candies"));
		addConcept(new Concept("ONTO0105","WORD0302","sweet"));
		addConcept(new Concept("ONTO0106","WORD0303","toothache"));
		addConcept(new Concept("ONTO0107","WORD0292","junk food"));
		addConcept(new Concept("ONTO0108","WORD0304","pain"));
		addConcept(new Concept("ONTO0109","WORD0305","medicine"));
		addConcept(new Concept("ONTO0110","WORD0306","heal"));
		addConcept(new Concept("ONTO0111","WORD0299","solution"));
		addConcept(new Concept("ONTO0112","WORD0040 WORD0307","feel better"));
		addConcept(new Concept("ONTO0113","WORD0242 WORD0308 WORD0218 WORD0309","eat fruits and vegetabls"));
		addConcept(new Concept("ONTO0114","WORD0083 WORD0310","be strong"));
		addConcept(new Concept("ONTO0115","WORD0249","healthy"));
		addConcept(new Concept("ONTO0116","WORD0300","benefit"));
		addConcept(new Concept("ONTO0117","WORD0242 WORD0312","eat sweets"));
		addConcept(new Concept("ONTO0118","WORD0312","sweets"));
		addConcept(new Concept("ONTO0119","WORD0410 WORD0240","brush teeth"));
		addConcept(new Concept("ONTO0120","WORD0120 WORD0410 WORD0240","not brush teeth"));
		addConcept(new Concept("ONTO0121","WORD0130 WORD0313","have cavity"));
		addConcept(new Concept("ONTO0122","WORD0130 WORD0303","have toothache"));
		addConcept(new Concept("ONTO0123","WORD0313","cavity"));
		addConcept(new Concept("ONTO0124","WORD0055 WORD0313","remove cavity"));
		addConcept(new Concept("ONTO0125","WORD0055 WORD0303","remove toothache"));
		addConcept(new Concept("ONTO0126","WORD0071 WORD0304","soothe pain"));
		addConcept(new Concept("ONTO0127","WORD0314","chips"));
		addConcept(new Concept("ONTO0128","WORD0040 WORD0393","feel hurt"));
		addConcept(new Concept("ONTO0129","WORD0060 WORD0315","give strength"));
		addConcept(new Concept("ONTO0130","WORD0244 WORD0245","sleep early"));
		addConcept(new Concept("ONTO0131","WORD0244 WORD0317","sleep late"));
		addConcept(new Concept("ONTO0132","WORD0318","headache"));
		addConcept(new Concept("ONTO0133","WORD0319","dizzy"));
		addConcept(new Concept("ONTO0134","WORD0398","rest"));
		addConcept(new Concept("ONTO0135","WORD0120 WORD0040 WORD0319","not feel dizzy"));
		addConcept(new Concept("ONTO0136","WORD0321 WORD0262","throw up"));
		addConcept(new Concept("ONTO0137","WORD0071 WORD0318","soothe headache"));
		addConcept(new Concept("ONTO0138","WORD0055 WORD0322","remove dizziness"));
		addConcept(new Concept("ONTO0139","WORD0324 WORD0117 WORD0149","go to school"));
		addConcept(new Concept("ONTO0140","WORD0335 WORD0336","say goodbye"));
		addConcept(new Concept("ONTO0141","WORD0083 WORD0243","be brave"));
		addConcept(new Concept("ONTO0142","WORD0083 WORD0256","be scared"));
		addConcept(new Concept("ONTO0143","WORD0337","virtue"));
		addConcept(new Concept("ONTO0144","WORD0338","admirable"));
		addConcept(new Concept("ONTO0145","WORD0330","introduction"));
		addConcept(new Concept("ONTO0146","WORD0339","smile"));
		addConcept(new Concept("ONTO0147","WORD0040 WORD0340","feel shy"));
		addConcept(new Concept("ONTO0148","WORD0022 WORD0116 WORD0341","play with others"));
		addConcept(new Concept("ONTO0149","WORD0056 WORD0332","make friends"));
		addConcept(new Concept("ONTO0150","WORD0331","friendship"));
		addConcept(new Concept("ONTO0151","WORD0130 WORD0342 WORD0343","have new playmates"));
		addConcept(new Concept("ONTO0152","WORD0022 WORD0344","play games"));
		addConcept(new Concept("ONTO0153","WORD0333","activity"));
		addConcept(new Concept("ONTO0155","WORD0001","happy"));
		addConcept(new Concept("ONTO0156","WORD0334","emotion"));
		addConcept(new Concept("ONTO0157","WORD0400 WORD0346 WORD0347","switch off light"));
		addConcept(new Concept("ONTO0158","WORD0348","dark"));
		addConcept(new Concept("ONTO0159","WORD0083 WORD0120 WORD0017 WORD0163 WORD0419","\"be\" not afraid of the dark"));
		addConcept(new Concept("ONTO0160","WORD0244 WORD0116 WORD0347 WORD0346","sleep with lights off"));
		addConcept(new Concept("ONTO0161","WORD0244","sleep"));
		addConcept(new Concept("ONTO0162","WORD0349 WORD0350","sing lullaby"));
		addConcept(new Concept("ONTO0163","WORD0098 WORD0417 WORD0351","tell bedtime story"));
		addConcept(new Concept("ONTO0164","WORD0202","stethoscope"));
		addConcept(new Concept("ONTO0165","WORD0207","thermometer"));
		addConcept(new Concept("ONTO0166","WORD0357 WORD0353 WORD0262","need check up"));
		addConcept(new Concept("ONTO0167","WORD0083 WORD0358","\"be\" sick"));
		addConcept(new Concept("ONTO0168","WORD0359","nervous"));
		addConcept(new Concept("ONTO0169","WORD0360 WORD0361","hear heartbeat"));
		addConcept(new Concept("ONTO0170","WORD0353 WORD0362","check temperature"));
		addConcept(new Concept("ONTO0171","WORD0353 WORD0262","check up"));
		addConcept(new Concept("ONTO0172","WORD0083 WORD0249","\"be\" healthy"));
		addConcept(new Concept("ONTO0173","WORD0354 WORD0411","positive result"));
		addConcept(new Concept("ONTO0174","WORD0355","reward (noun)"));
		addConcept(new Concept("ONTO0175","WORD0363","ice cream"));
		addConcept(new Concept("ONTO0176","WORD0122 WORD0247","ask permission"));
		addConcept(new Concept("ONTO0177","WORD0422 WORD0423","stay put"));
		addConcept(new Concept("ONTO0178","WORD0367 WORD0097","buy toys"));
		addConcept(new Concept("ONTO0179","WORD0367 WORD0189","buy candies"));
		addConcept(new Concept("ONTO0180","WORD0368 WORD0369","toy store"));
		addConcept(new Concept("ONTO0181","WORD0370","confectionary"));
		addConcept(new Concept("ONTO0182","WORD0301 WORD0371","candy stand"));
		addConcept(new Concept("ONTO0183","WORD0063 WORD0137 WORD0342 WORD0097","look at new toys"));
		addConcept(new Concept("ONTO0184","WORD0022 WORD0116 WORD0097","play with toys"));
		addConcept(new Concept("ONTO0185","WORD0063 WORD0137 WORD0302","look at sweets"));
		addConcept(new Concept("ONTO0186","WORD0274","lost"));
		addConcept(new Concept("ONTO0187","WORD0406","search"));
		addConcept(new Concept("ONTO0188","WORD0373 WORD0374","feeling worried"));
		addConcept(new Concept("ONTO0189","WORD0375","important"));
		addConcept(new Concept("ONTO0190","WORD0036 WORD0258","hide away"));
		addConcept(new Concept("ONTO0191","WORD0379 WORD0264","destroyed object"));
		addConcept(new Concept("ONTO0192","WORD0380","broken"));
		addConcept(new Concept("ONTO0193","WORD0033","call"));
		addConcept(new Concept("ONTO0194","WORD0324 WORD0117","go to"));
		addConcept(new Concept("ONTO0195","WORD0396 WORD0262","clean up"));
		addConcept(new Concept("ONTO0196","WORD0083 WORD0241","be careful"));
		addConcept(new Concept("ONTO0197","WORD0083 WORD0381","be careless"));
		addConcept(new Concept("ONTO0198","WORD0162 WORD0383","get punished"));
		addConcept(new Concept("ONTO0199","WORD0120 WORD0421 WORD0117 WORD0022 WORD0407","not allowed to play today"));
		addConcept(new Concept("ONTO0200","WORD0386","grounded"));
		addConcept(new Concept("ONTO0201","WORD0382","punishment"));
		addConcept(new Concept("ONTO0202","WORD0083 WORD0246","be honest"));
		addConcept(new Concept("ONTO0203","WORD0388","lie"));
		addConcept(new Concept("ONTO0204","WORD0324 WORD0117 WORD0325","go to bed"));
		addConcept(new Concept("ONTO0205","WORD0335 WORD0016 WORD0152","say good night"));
		addConcept(new Concept("ONTO0206","WORD0022 WORD0164","play near"));
		addConcept(new Concept("ONTO0207","WORD0324 WORD0117 WORD0171","go to clinic"));
		addConcept(new Concept("ONTO0208","WORD0316 WORD0206","watch television"));
		addConcept(new Concept("ONTO0209","WORD0324 WORD0117 WORD0125 WORD0172","go to the mall"));
		addConcept(new Concept("ONTO0210","WORD0332","friends"));
		addConcept(new Concept("ONTO0211","WORD0389","salty"));
		addConcept(new Concept("ONTO0212","WORD0181","apple"));
		addConcept(new Concept("ONTO0213","WORD0183","bananas"));
		addConcept(new Concept("ONTO0214","WORD0186","brocollis"));
		addConcept(new Concept("ONTO0215","WORD0188","cake"));
		addConcept(new Concept("ONTO0216","WORD0180","carrots"));
		addConcept(new Concept("ONTO0217","WORD0193 WORD0194","fried chicken"));
		addConcept(new Concept("ONTO0218","WORD0217","spaghetti"));
		addConcept(new Concept("ONTO0219","WORD0196","bread"));
		addConcept(new Concept("ONTO0220","WORD0184 WORD0168","beach ball"));
		addConcept(new Concept("ONTO0221","WORD0402","reward (Verb)"));
		addConcept(new Concept("ONTO0222","WORD0413","scary"));
		addConcept(new Concept("ONTO0223","WORD0412","complain"));
		addConcept(new Concept("ONTO0224","WORD0190","carrot"));
		addConcept(new Concept("ONTO0225","WORD0429","delicious"));
		addConcept(new Concept("ONTO0226","WORD0003","pretty"));
		addConcept(new Concept("ONTO0227","WORD0263","breakable"));
		addConcept(new Concept("ONTO0228","WORD0050","red"));
		addConcept(new Concept("ONTO0229","WORD0431","yellow"));
		addConcept(new Concept("ONTO0230","WORD0430","green"));
		addConcept(new Concept("ONTO0231","WORD0111","rubber ducky"));
		addConcept(new Concept("ONTO0232","WORD0432","big"));
		addConcept(new Concept("ONTO0233","WORD0433","small"));
		addConcept(new Concept("ONTO0234","WORD0219","soap"));
		addConcept(new Concept("ONTO0235","WORD0434","fragrant"));
		addConcept(new Concept("ONTO0236","WORD0435","slippery"));
		addConcept(new Concept("ONTO0237","WORD0198","pillow"));
		addConcept(new Concept("ONTO0238","WORD0436","soft"));
		addConcept(new Concept("ONTO0239","WORD0437","fluffy"));
		addConcept(new Concept("ONTO0240","WORD0201","seesaw"));
		addConcept(new Concept("ONTO0241","WORD0438","colorful"));

	}
	
	public void addOntology(Ontology o)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("OntologyID1", o.getOntologyID1());
		cv.put("SemanticRelation", o.getSemanticRelation());
		cv.put("OntologyID2", o.getOntologyID2());
		cv.put("Category", o.getCategory());
		
		database.insert(TableCreator.TABLE_ONTOLOGY, null, cv);
	}
	
	public Vector<Ontology> getOntologies()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<Ontology> ontologies = new Vector<Ontology>();
		
		String selectQuery = "SELECT * FROM tableOntology";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				Ontology o = new Ontology();				
				o.setOntologyID1(cursor.getString(0));
				o.setSemanticRelation(cursor.getString(1));
				o.setOntologyID2(cursor.getString(2));
				o.setCategory(cursor.getString(3));
				Log.d("Ontology", o.getOntologyID1());
				ontologies.add(o);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return ontologies;
	}	
	
	public void InitializeOntology()
	{
		addOntology(new Ontology("ONTO0001","eventRequiresObject","ONTO0002","event"));
		addOntology(new Ontology("ONTO0001","locationOf","ONTO0031","spatial"));
		addOntology(new Ontology("ONTO0001","locationOf","ONTO0032","spatial"));
		addOntology(new Ontology("ONTO0001","locationOf","ONTO0033","spatial"));
		addOntology(new Ontology("ONTO0001","locationOf","ONTO0059","spatial"));
		addOntology(new Ontology("ONTO0001","locationOf","ONTO0180","spatial"));
		addOntology(new Ontology("ONTO0002","canDo","ONTO0081","generic"));
		addOntology(new Ontology("ONTO0002","capableOf","ONTO0017","action"));
		addOntology(new Ontology("ONTO0002","desiresEvent","ONTO0086","goal"));
		addOntology(new Ontology("ONTO0002","exampleOf","ONTO0003","things"));
		addOntology(new Ontology("ONTO0002","exampleOf","ONTO0064","things"));
		addOntology(new Ontology("ONTO0002","exampleOf","ONTO0065","things"));
		addOntology(new Ontology("ONTO0002","exampleOf","ONTO0066","things"));
		addOntology(new Ontology("ONTO0002","exampleOf","ONTO0067","things"));
		addOntology(new Ontology("ONTO0002","exampleOf","ONTO0068","things"));
		addOntology(new Ontology("ONTO0002","exampleOf","ONTO0069","things"));
		addOntology(new Ontology("ONTO0002","exampleOf","ONTO0070","things"));
		addOntology(new Ontology("ONTO0002","exampleOf","ONTO0071","things"));
		addOntology(new Ontology("ONTO0002","exampleOf","ONTO0220","things"));
		addOntology(new Ontology("ONTO0002","locationOf","ONTO0180","spatial"));
		addOntology(new Ontology("ONTO0002","usedfor","ONTO0001","function"));
		addOntology(new Ontology("ONTO0003","capableOf","ONTO0001","action"));
		addOntology(new Ontology("ONTO0003","isA","ONTO0002","things"));
		addOntology(new Ontology("ONTO0003","propertyOf","ONTO0228","description"));
		addOntology(new Ontology("ONTO0004","capableOf","ONTO0005","action"));
		addOntology(new Ontology("ONTO0005","effectOf","ONTO0007","action"));
		addOntology(new Ontology("ONTO0007","isA","ONTO0008","things"));
		addOntology(new Ontology("ONTO0007","lastSubeventOf","ONTO0009","action"));
		addOntology(new Ontology("ONTO0007","lastSubeventOf","ONTO0128","event"));
		addOntology(new Ontology("ONTO0008","effectOf","ONTO0019","action"));
		addOntology(new Ontology("ONTO0008","lastSubeventOf","ONTO0011","event"));
		addOntology(new Ontology("ONTO0009","effectOf","ONTO0010","action"));
		addOntology(new Ontology("ONTO0009","effectOf","ONTO0073","action"));
		addOntology(new Ontology("ONTO0010","effectOf","ONTO0008","action"));
		addOntology(new Ontology("ONTO0011","isA","ONTO0012","things"));
		addOntology(new Ontology("ONTO0013","capableOf","ONTO0015","action"));
		addOntology(new Ontology("ONTO0013","capableOf","ONTO0072","action"));
		addOntology(new Ontology("ONTO0013","negate","ONTO0004","negate"));
		addOntology(new Ontology("ONTO0013","propertyOf","ONTO0027","things"));
		addOntology(new Ontology("ONTO0014","isA","ONTO0016","things"));
		addOntology(new Ontology("ONTO0015","lastSubeventOf","ONTO0014","event"));
		addOntology(new Ontology("ONTO0018","effectOf","ONTO0019","action"));
		addOntology(new Ontology("ONTO0018","propertyOf","ONTO0020","things"));
		addOntology(new Ontology("ONTO0019","isA","ONTO0008","things"));
		addOntology(new Ontology("ONTO0019","lastSubeventOf","ONTO0011","event"));
		addOntology(new Ontology("ONTO0020","effectOf","ONTO0053","action"));
		addOntology(new Ontology("ONTO0021","propertyOf","ONTO0020","things"));
		addOntology(new Ontology("ONTO0024","lastSubeventOf","ONTO0025","event"));
		addOntology(new Ontology("ONTO0026","negate","ONTO0021","negate"));
		addOntology(new Ontology("ONTO0026","propertyOf","ONTO0027","things"));
		addOntology(new Ontology("ONTO0027","isA","ONTO0016","things"));
		addOntology(new Ontology("ONTO0027","isA","ONTO0029","things"));
		addOntology(new Ontology("ONTO0030","isA","ONTO0029","things"));
		addOntology(new Ontology("ONTO0031","isA","ONTO0062","things"));
		addOntology(new Ontology("ONTO0031","propertyOf","ONTO0035","things"));
		addOntology(new Ontology("ONTO0031","propertyOf","ONTO0051","spatial"));
		addOntology(new Ontology("ONTO0032","isA","ONTO0062","things"));
		addOntology(new Ontology("ONTO0032","propertyOf","ONTO0034","things"));
		addOntology(new Ontology("ONTO0032","propertyOf","ONTO0051","spatial"));
		addOntology(new Ontology("ONTO0033","isA","ONTO0062","things"));
		addOntology(new Ontology("ONTO0033","propertyOf","ONTO0034","things"));
		addOntology(new Ontology("ONTO0033","propertyOf","ONTO0051","spatial"));
		addOntology(new Ontology("ONTO0034","conceptuallyRelatedTo","ONTO0036","time"));
		addOntology(new Ontology("ONTO0034","conceptuallyRelatedTo","ONTO0037","time"));
		addOntology(new Ontology("ONTO0034","conceptuallyRelatedTo","ONTO0038","time"));
		addOntology(new Ontology("ONTO0034","conceptuallyRelatedTo","ONTO0042","time"));
		addOntology(new Ontology("ONTO0034","conceptuallyRelatedTo","ONTO0043","time"));
		addOntology(new Ontology("ONTO0035","conceptuallyRelatedTo","ONTO0036","time"));
		addOntology(new Ontology("ONTO0035","conceptuallyRelatedTo","ONTO0039","time"));
		addOntology(new Ontology("ONTO0036","conceptuallyRelatedTo","ONTO0040","time"));
		addOntology(new Ontology("ONTO0036","conceptuallyRelatedTo","ONTO0044","time"));
		addOntology(new Ontology("ONTO0036","conceptuallyRelatedTo","ONTO0045","time"));
		addOntology(new Ontology("ONTO0036","conceptuallyRelatedTo","ONTO0049","time"));
		addOntology(new Ontology("ONTO0036","isA","ONTO0063","things"));
		addOntology(new Ontology("ONTO0037","conceptuallyRelatedTo","ONTO0040","time"));
		addOntology(new Ontology("ONTO0037","conceptuallyRelatedTo","ONTO0045","time"));
		addOntology(new Ontology("ONTO0037","conceptuallyRelatedTo","ONTO0049","time"));
		addOntology(new Ontology("ONTO0037","isA","ONTO0063","things"));
		addOntology(new Ontology("ONTO0038","conceptuallyRelatedTo","ONTO0041","time"));
		addOntology(new Ontology("ONTO0038","isA","ONTO0062","things"));
		addOntology(new Ontology("ONTO0038","propertyOf","ONTO0052","spatial"));
		addOntology(new Ontology("ONTO0038","propertyOf","ONTO0060","things"));
		addOntology(new Ontology("ONTO0039","conceptuallyRelatedTo","ONTO0040","time"));
		addOntology(new Ontology("ONTO0039","conceptuallyRelatedTo","ONTO0044","time"));
		addOntology(new Ontology("ONTO0039","conceptuallyRelatedTo","ONTO0046","time"));
		addOntology(new Ontology("ONTO0039","isA","ONTO0063","things"));
		addOntology(new Ontology("ONTO0042","conceptuallyRelatedTo","ONTO0046","time"));
		addOntology(new Ontology("ONTO0042","conceptuallyRelatedTo","ONTO0047","time"));
		addOntology(new Ontology("ONTO0042","conceptuallyRelatedTo","ONTO0048","time"));
		addOntology(new Ontology("ONTO0042","conceptuallyRelatedTo","ONTO0049","time"));
		addOntology(new Ontology("ONTO0042","conceptuallyRelatedTo","ONTO0050","time"));
		addOntology(new Ontology("ONTO0042","isA","ONTO0063","things"));
		addOntology(new Ontology("ONTO0043","conceptuallyRelatedTo","ONTO0046","time"));
		addOntology(new Ontology("ONTO0043","conceptuallyRelatedTo","ONTO0047","time"));
		addOntology(new Ontology("ONTO0043","isA","ONTO0063","things"));
		addOntology(new Ontology("ONTO0053","isA","ONTO0008","things"));
		addOntology(new Ontology("ONTO0054","isA","ONTO0062","things"));
		addOntology(new Ontology("ONTO0054","propertyOf","ONTO0034","things"));
		addOntology(new Ontology("ONTO0054","propertyOf","ONTO0051","spatial"));
		addOntology(new Ontology("ONTO0055","isA","ONTO0062","things"));
		addOntology(new Ontology("ONTO0055","propertyOf","ONTO0051","spatial"));
		addOntology(new Ontology("ONTO0055","propertyOf","ONTO0060","things"));
		addOntology(new Ontology("ONTO0056","isA","ONTO0062","things"));
		addOntology(new Ontology("ONTO0056","propertyOf","ONTO0034","things"));
		addOntology(new Ontology("ONTO0056","propertyOf","ONTO0051","spatial"));
		addOntology(new Ontology("ONTO0057","canDo","ONTO0178","generic"));
		addOntology(new Ontology("ONTO0057","canDo","ONTO0179","generic"));
		addOntology(new Ontology("ONTO0057","isA","ONTO0062","things"));
		addOntology(new Ontology("ONTO0057","propertyOf","ONTO0052","spatial"));
		addOntology(new Ontology("ONTO0057","propertyOf","ONTO0060","things"));
		addOntology(new Ontology("ONTO0058","canDo","ONTO0179","generic"));
		addOntology(new Ontology("ONTO0058","isA","ONTO0062","things"));
		addOntology(new Ontology("ONTO0058","propertyOf","ONTO0052","spatial"));
		addOntology(new Ontology("ONTO0058","propertyOf","ONTO0060","things"));
		addOntology(new Ontology("ONTO0059","isA","ONTO0062","things"));
		addOntology(new Ontology("ONTO0059","propertyOf","ONTO0052","spatial"));
		addOntology(new Ontology("ONTO0059","propertyOf","ONTO0061","things"));
		addOntology(new Ontology("ONTO0064","capableOf","ONTO0001","action"));
		addOntology(new Ontology("ONTO0064","isA","ONTO0002","things"));
		addOntology(new Ontology("ONTO0064","propertyOf","ONTO0226","description"));
		addOntology(new Ontology("ONTO0065","capableOf","ONTO0001","action"));
		addOntology(new Ontology("ONTO0065","isA","ONTO0002","things"));
		addOntology(new Ontology("ONTO0065","propertyOf","ONTO0233","description"));
		addOntology(new Ontology("ONTO0066","capableOf","ONTO0001","action"));
		addOntology(new Ontology("ONTO0066","isA","ONTO0002","things"));
		addOntology(new Ontology("ONTO0066","propertyOf","ONTO0233","description"));
		addOntology(new Ontology("ONTO0067","capableOf","ONTO0001","action"));
		addOntology(new Ontology("ONTO0067","isA","ONTO0002","things"));
		addOntology(new Ontology("ONTO0067","propertyOf","ONTO0241","description"));
		addOntology(new Ontology("ONTO0068","capableOf","ONTO0001","action"));
		addOntology(new Ontology("ONTO0068","isA","ONTO0002","things"));
		addOntology(new Ontology("ONTO0068","propertyOf","ONTO0233","description"));
		addOntology(new Ontology("ONTO0069","capableOf","ONTO0001","action"));
		addOntology(new Ontology("ONTO0069","isA","ONTO0002","things"));
		addOntology(new Ontology("ONTO0070","capableOf","ONTO0001","action"));
		addOntology(new Ontology("ONTO0070","isA","ONTO0002","things"));
		addOntology(new Ontology("ONTO0070","propertyOf","ONTO0232","description"));
		addOntology(new Ontology("ONTO0071","capableOf","ONTO0001","action"));
		addOntology(new Ontology("ONTO0071","isA","ONTO0002","things"));
		addOntology(new Ontology("ONTO0071","propertyOf","ONTO0232","description"));
		addOntology(new Ontology("ONTO0072","effectOf","ONTO0014","action"));
		addOntology(new Ontology("ONTO0073","effectOf","ONTO0008","action"));
		addOntology(new Ontology("ONTO0074","capableOf","ONTO0076","action"));
		addOntology(new Ontology("ONTO0074","propertyOf","ONTO0227","description"));
		addOntology(new Ontology("ONTO0075","capableOf","ONTO0076","action"));
		addOntology(new Ontology("ONTO0075","propertyOf","ONTO0227","description"));
		addOntology(new Ontology("ONTO0076","conceptuallyRelatedTo","ONTO0078","generic"));
		addOntology(new Ontology("ONTO0076","isA","ONTO0077","things"));
		addOntology(new Ontology("ONTO0077","conceptuallyRelatedTo","ONTO0191","generic"));
		addOntology(new Ontology("ONTO0078","effectOf","ONTO0142","action"));
		addOntology(new Ontology("ONTO0078","lastSubeventOf","ONTO0198","event"));
		addOntology(new Ontology("ONTO0079","eventForGoalEvent","ONTO0091","event"));
		addOntology(new Ontology("ONTO0079","negate","ONTO0080","negate"));
		addOntology(new Ontology("ONTO0079","propertyOf","ONTO0030","things"));
		addOntology(new Ontology("ONTO0080","eventForGoalEvent","ONTO0001","event"));
		addOntology(new Ontology("ONTO0081","lastSubeventOf","ONTO0082","event"));
		addOntology(new Ontology("ONTO0082","conceptuallyRelatedTo","ONTO0083","generic"));
		addOntology(new Ontology("ONTO0083","lastSubeventOf","ONTO0084","event"));
		addOntology(new Ontology("ONTO0084","isA","ONTO0085","things"));
		addOntology(new Ontology("ONTO0086","firstSubeventOf","ONTO0187","event"));
		addOntology(new Ontology("ONTO0086","negate","ONTO0186","negate"));
		addOntology(new Ontology("ONTO0087","lastSubeventOf","ONTO0011","event"));
		addOntology(new Ontology("ONTO0088","desiresEvent","ONTO0089","goal"));
		addOntology(new Ontology("ONTO0088","desiresEvent","ONTO0090","goal"));
		addOntology(new Ontology("ONTO0092","negate","ONTO0093","negate"));
		addOntology(new Ontology("ONTO0092","propertyOf","ONTO0030","things"));
		addOntology(new Ontology("ONTO0093","effectOf","ONTO0100","action"));
		addOntology(new Ontology("ONTO0093","eventForGoalEvent","ONTO0094","event"));
		addOntology(new Ontology("ONTO0094","conceptuallyRelatedTo","ONTO0095","generic"));
		addOntology(new Ontology("ONTO0094","conceptuallyRelatedTo","ONTO0107","generic"));
		addOntology(new Ontology("ONTO0094","eventRequiresObject","ONTO0107","event"));
		addOntology(new Ontology("ONTO0095","lastSubeventOf","ONTO0096","event"));
		addOntology(new Ontology("ONTO0095","locationOf","ONTO0032","spatial"));
		addOntology(new Ontology("ONTO0095","locationOf","ONTO0056","spatial"));
		addOntology(new Ontology("ONTO0095","locationOf","ONTO0182","spatial"));
		addOntology(new Ontology("ONTO0096","effectOf","ONTO0100","action"));
		addOntology(new Ontology("ONTO0099","desiresEvent","ONTO0095","goal"));
		addOntology(new Ontology("ONTO0100","isA","ONTO0085","things"));
		addOntology(new Ontology("ONTO0102","effectOf","ONTO0019","action"));
		addOntology(new Ontology("ONTO0102","isA","ONTO0008","things"));
		addOntology(new Ontology("ONTO0102","isA","ONTO0085","things"));
		addOntology(new Ontology("ONTO0103","conceptuallyRelatedTo","ONTO0113","generic"));
		addOntology(new Ontology("ONTO0103","negate","ONTO0094","negate"));
		addOntology(new Ontology("ONTO0103","propertyOf","ONTO0030","things"));
		addOntology(new Ontology("ONTO0104","isA","ONTO0181","things"));
		addOntology(new Ontology("ONTO0104","propertyOf","ONTO0105","things"));
		addOntology(new Ontology("ONTO0104","propertyOf","ONTO0225","description"));
		addOntology(new Ontology("ONTO0105","effectOf","ONTO0106","action"));
		addOntology(new Ontology("ONTO0105","locationOf","ONTO0181","spatial"));
		addOntology(new Ontology("ONTO0106","effectOf","ONTO0019","action"));
		addOntology(new Ontology("ONTO0106","effectOf","ONTO0108","action"));
		addOntology(new Ontology("ONTO0106","isA","ONTO0085","things"));
		addOntology(new Ontology("ONTO0106","lastSubeventOf","ONTO0011","event"));
		addOntology(new Ontology("ONTO0107","desiresEvent","ONTO0095","goal"));
		addOntology(new Ontology("ONTO0107","exampleOf","ONTO0104","things"));
		addOntology(new Ontology("ONTO0107","exampleOf","ONTO0127","things"));
		addOntology(new Ontology("ONTO0107","exampleOf","ONTO0215","things"));
		addOntology(new Ontology("ONTO0107","locationOf","ONTO0058","spatial"));
		addOntology(new Ontology("ONTO0108","capableOf","ONTO0011","action"));
		addOntology(new Ontology("ONTO0108","effectOf","ONTO0019","action"));
		addOntology(new Ontology("ONTO0108","isA","ONTO0008","things"));
		addOntology(new Ontology("ONTO0108","lastSubeventOf","ONTO0011","event"));
		addOntology(new Ontology("ONTO0109","effectOf","ONTO0112","action"));
		addOntology(new Ontology("ONTO0112","isA","ONTO0111","things"));
		addOntology(new Ontology("ONTO0113","canDo","ONTO0129","generic"));
		addOntology(new Ontology("ONTO0115","conceptuallyRelatedTo","ONTO0116","generic"));
		addOntology(new Ontology("ONTO0117","eventRequiresObject","ONTO0118","event"));
		addOntology(new Ontology("ONTO0117","locationOf","ONTO0032","spatial"));
		addOntology(new Ontology("ONTO0117","locationOf","ONTO0056","spatial"));
		addOntology(new Ontology("ONTO0117","locationOf","ONTO0057","spatial"));
		addOntology(new Ontology("ONTO0117","locationOf","ONTO0058","spatial"));
		addOntology(new Ontology("ONTO0117","locationOf","ONTO0059","spatial"));
		addOntology(new Ontology("ONTO0117","locationOf","ONTO0182","spatial"));
		addOntology(new Ontology("ONTO0118","exampleOf","ONTO0104","things"));
		addOntology(new Ontology("ONTO0119","capableOf","ONTO0124","action"));
		addOntology(new Ontology("ONTO0119","negate","ONTO0120","negate"));
		addOntology(new Ontology("ONTO0119","propertyOf","ONTO0027","things"));
		addOntology(new Ontology("ONTO0120","capableOf","ONTO0121","action"));
		addOntology(new Ontology("ONTO0121","conceptuallyRelatedTo","ONTO0123","generic"));
		addOntology(new Ontology("ONTO0122","conceptuallyRelatedTo","ONTO0106","generic"));
		addOntology(new Ontology("ONTO0123","effectOf","ONTO0108","action"));
		addOntology(new Ontology("ONTO0124","effectOf","ONTO0126","action"));
		addOntology(new Ontology("ONTO0125","effectOf","ONTO0126","action"));
		addOntology(new Ontology("ONTO0126","isA","ONTO0016","things"));
		addOntology(new Ontology("ONTO0127","propertyOf","ONTO0211","things"));
		addOntology(new Ontology("ONTO0128","conceptuallyRelatedTo","ONTO0019","generic"));
		addOntology(new Ontology("ONTO0128","eventRequiresObject","ONTO0109","event"));
		addOntology(new Ontology("ONTO0129","propertyOf","ONTO0115","description"));
		addOntology(new Ontology("ONTO0130","capableOf","ONTO0135","action"));
		addOntology(new Ontology("ONTO0130","conceptuallyRelatedTo","ONTO0134","generic"));
		addOntology(new Ontology("ONTO0130","negate","ONTO0131","negate"));
		addOntology(new Ontology("ONTO0130","propertyOf","ONTO0030","things"));
		addOntology(new Ontology("ONTO0131","effectOf","ONTO0132","action"));
		addOntology(new Ontology("ONTO0132","capableOf","ONTO0133","action"));
		addOntology(new Ontology("ONTO0133","effectOf","ONTO0019","action"));
		addOntology(new Ontology("ONTO0133","effectOf","ONTO0136","action"));
		addOntology(new Ontology("ONTO0133","isA","ONTO0008","things"));
		addOntology(new Ontology("ONTO0133","lastSubeventOf","ONTO0223","event"));
		addOntology(new Ontology("ONTO0134","canDo","ONTO0129","generic"));
		addOntology(new Ontology("ONTO0135","conceptuallyRelatedTo","ONTO0138","generic"));
		addOntology(new Ontology("ONTO0136","isA","ONTO0019","things"));
		addOntology(new Ontology("ONTO0137","isA","ONTO0016","things"));
		addOntology(new Ontology("ONTO0138","capableOf","ONTO0137","action"));
		addOntology(new Ontology("ONTO0139","firstSubeventOf","ONTO0140","event"));
		addOntology(new Ontology("ONTO0139","locationOf","ONTO0038","spatial"));
		addOntology(new Ontology("ONTO0141","capableOf","ONTO0159","action"));
		addOntology(new Ontology("ONTO0141","desiresEvent","ONTO0148","goal"));
		addOntology(new Ontology("ONTO0141","isA","ONTO0143","things"));
		addOntology(new Ontology("ONTO0141","negate","ONTO0142","negate"));
		addOntology(new Ontology("ONTO0141","propertyOf","ONTO0030","things"));
		addOntology(new Ontology("ONTO0142","capableOf","ONTO0168","generic"));
		addOntology(new Ontology("ONTO0142","desiresEvent","ONTO0190","goal"));
		addOntology(new Ontology("ONTO0142","isA","ONTO0156","things"));
		addOntology(new Ontology("ONTO0142","lastSubeventOf","ONTO0011","event"));
		addOntology(new Ontology("ONTO0143","propertyOf","ONTO0144","things"));
		addOntology(new Ontology("ONTO0144","isA","ONTO0029","things"));
		addOntology(new Ontology("ONTO0145","capableOf","ONTO0146","action"));
		addOntology(new Ontology("ONTO0145","capableOf","ONTO0147","action"));
		addOntology(new Ontology("ONTO0148","capableOf","ONTO0149","action"));
		addOntology(new Ontology("ONTO0149","effectOfIsState","ONTO0150","action"));
		addOntology(new Ontology("ONTO0150","lastSubeventOf","ONTO0151","event"));
		addOntology(new Ontology("ONTO0151","eventRequiresObject","ONTO0152","event"));
		addOntology(new Ontology("ONTO0152","eventForGoalEvent","ONTO0155","event"));
		addOntology(new Ontology("ONTO0152","isA","ONTO0153","things"));
		addOntology(new Ontology("ONTO0155","isA","ONTO0156","things"));
		addOntology(new Ontology("ONTO0157","lastSubeventOf","ONTO0158","event"));
		addOntology(new Ontology("ONTO0158","capableOf","ONTO0159","action"));
		addOntology(new Ontology("ONTO0159","lastSubeventOf","ONTO0160","event"));
		addOntology(new Ontology("ONTO0160","conceptuallyRelatedTo","ONTO0161","generic"));
		addOntology(new Ontology("ONTO0161","firstSubeventOf","ONTO0162","event"));
		addOntology(new Ontology("ONTO0161","firstSubeventOf","ONTO0163","event"));
		addOntology(new Ontology("ONTO0162","isA","ONTO0016","things"));
		addOntology(new Ontology("ONTO0163","isA","ONTO0016","things"));
		addOntology(new Ontology("ONTO0164","conceptuallyRelatedTo","ONTO0166","generic"));
		addOntology(new Ontology("ONTO0164","conceptuallyRelatedTo","ONTO0167","generic"));
		addOntology(new Ontology("ONTO0164","propertyOf","ONTO0222","things"));
		addOntology(new Ontology("ONTO0164","usedFor","ONTO0169","function"));
		addOntology(new Ontology("ONTO0165","conceptuallyRelatedTo","ONTO0166","generic"));
		addOntology(new Ontology("ONTO0165","conceptuallyRelatedTo","ONTO0167","generic"));
		addOntology(new Ontology("ONTO0165","usedFor","ONTO0170","function"));
		addOntology(new Ontology("ONTO0168","isA","ONTO0156","things"));
		addOntology(new Ontology("ONTO0169","isA","ONTO0153","things"));
		addOntology(new Ontology("ONTO0170","isA","ONTO0153","things"));
		addOntology(new Ontology("ONTO0171","capableOf","ONTO0172","action"));
		addOntology(new Ontology("ONTO0172","isA","ONTO0173","things"));
		addOntology(new Ontology("ONTO0174","exampleOf","ONTO0104","things"));
		addOntology(new Ontology("ONTO0174","exampleOf","ONTO0175","things"));
		addOntology(new Ontology("ONTO0176","conceptuallyRelatedTo","ONTO0177","generic"));
		addOntology(new Ontology("ONTO0176","propertyOf","ONTO0189","things"));
		addOntology(new Ontology("ONTO0178","isA","ONTO0153","things"));
		addOntology(new Ontology("ONTO0179","isA","ONTO0153","things"));
		addOntology(new Ontology("ONTO0180","canDo","ONTO0183","generic"));
		addOntology(new Ontology("ONTO0180","canDo","ONTO0184","generic"));
		addOntology(new Ontology("ONTO0181","canDo","ONTO0185","generic"));
		addOntology(new Ontology("ONTO0181","locationOf","ONTO0182","spatial"));
		addOntology(new Ontology("ONTO0182","canDo","ONTO0185","generic"));
		addOntology(new Ontology("ONTO0183","isA","ONTO0153","things"));
		addOntology(new Ontology("ONTO0184","isA","ONTO0153","things"));
		addOntology(new Ontology("ONTO0185","isA","ONTO0153","things"));
		addOntology(new Ontology("ONTO0186","lastSubeventOf","ONTO0142","event"));
		addOntology(new Ontology("ONTO0187","effectOf","ONTO0188","action"));
		addOntology(new Ontology("ONTO0188","isA","ONTO0012","things"));
		addOntology(new Ontology("ONTO0189","isA","ONTO0029","things"));
		addOntology(new Ontology("ONTO0191","desiresEvent","ONTO0195","goal"));
		addOntology(new Ontology("ONTO0191","propertyOf","ONTO0192","things"));
		addOntology(new Ontology("ONTO0192","isA","ONTO0098","things"));
		addOntology(new Ontology("ONTO0193","desiresEvent","ONTO0194","goal"));
		addOntology(new Ontology("ONTO0195","isA","ONTO0153","things"));
		addOntology(new Ontology("ONTO0196","negate","ONTO0197","negate"));
		addOntology(new Ontology("ONTO0196","propertyOf","ONTO0189","things"));
		addOntology(new Ontology("ONTO0198","lastSubeventOf","ONTO0011","event"));
		addOntology(new Ontology("ONTO0198","lastSubeventOf","ONTO0199","event"));
		addOntology(new Ontology("ONTO0198","lastSubeventOf","ONTO0200","event"));
		addOntology(new Ontology("ONTO0199","isA","ONTO0201","things"));
		addOntology(new Ontology("ONTO0199","lastSubeventOf","ONTO0011","event"));
		addOntology(new Ontology("ONTO0200","isA","ONTO0201","things"));
		addOntology(new Ontology("ONTO0200","lastSubeventOf","ONTO0011","event"));
		addOntology(new Ontology("ONTO0202","negate","ONTO0203","negate"));
		addOntology(new Ontology("ONTO0202","propertyOf","ONTO0030","things"));
		addOntology(new Ontology("ONTO0203","effectOf","ONTO0053","action"));
		addOntology(new Ontology("ONTO0204","firstSubeventOf","ONTO0205","event"));
		addOntology(new Ontology("ONTO0204","locationOf","ONTO0032","spatial"));
		addOntology(new Ontology("ONTO0204","locationOf","ONTO0033","spatial"));
		addOntology(new Ontology("ONTO0204","locationOf","ONTO0054","spatial"));
		addOntology(new Ontology("ONTO0206","locationOf","ONTO0032","spatial"));
		addOntology(new Ontology("ONTO0206","locationOf","ONTO0033","spatial"));
		addOntology(new Ontology("ONTO0206","locationOf","ONTO0056","spatial"));
		addOntology(new Ontology("ONTO0207","locationOf","ONTO0055","spatial"));
		addOntology(new Ontology("ONTO0207","locationOf","ONTO0057","spatial"));
		addOntology(new Ontology("ONTO0208","locationOf","ONTO0032","spatial"));
		addOntology(new Ontology("ONTO0208","locationOf","ONTO0033","spatial"));
		addOntology(new Ontology("ONTO0209","locationOf","ONTO0057","spatial"));
		addOntology(new Ontology("ONTO0209","locationOf","ONTO0058","spatial"));
		addOntology(new Ontology("ONTO0209","locationOf","ONTO0180","spatial"));
		addOntology(new Ontology("ONTO0210","conceptuallyRelatedTo","ONTO0150","generic"));
		addOntology(new Ontology("ONTO0211","effectOf","ONTO0102","action"));
		addOntology(new Ontology("ONTO0212","isA","ONTO0099","things"));
		addOntology(new Ontology("ONTO0212","propertyOf","ONTO0115","description"));
		addOntology(new Ontology("ONTO0212","propertyOf","ONTO0225","description"));
		addOntology(new Ontology("ONTO0213","isA","ONTO0099","things"));
		addOntology(new Ontology("ONTO0213","propertyOf","ONTO0115","description"));
		addOntology(new Ontology("ONTO0213","propertyOf","ONTO0225","description"));
		addOntology(new Ontology("ONTO0214","isA","ONTO0099","things"));
		addOntology(new Ontology("ONTO0214","propertyOf","ONTO0115","description"));
		addOntology(new Ontology("ONTO0214","propertyOf","ONTO0230","description"));
		addOntology(new Ontology("ONTO0215","isA","ONTO0107","things"));
		addOntology(new Ontology("ONTO0215","propertyOf","ONTO0105","things"));
		addOntology(new Ontology("ONTO0215","propertyOf","ONTO0225","description"));
		addOntology(new Ontology("ONTO0216","isA","ONTO0099","things"));
		addOntology(new Ontology("ONTO0216","propertyOf","ONTO0115","things"));
		addOntology(new Ontology("ONTO0217","isA","ONTO0099","things"));
		addOntology(new Ontology("ONTO0218","isA","ONTO0099","things"));
		addOntology(new Ontology("ONTO0218","propertyOf","ONTO0225","description"));
		addOntology(new Ontology("ONTO0219","isA","ONTO0099","things"));
		addOntology(new Ontology("ONTO0219","propertyOf","ONTO0225","description"));
		addOntology(new Ontology("ONTO0220","capableOf","ONTO0001","action"));
		addOntology(new Ontology("ONTO0220","isA","ONTO0002","things"));
		addOntology(new Ontology("ONTO0221","eventForGoalEvent","ONTO0155","event"));
		addOntology(new Ontology("ONTO0223","isA","ONTO0012","things"));
		addOntology(new Ontology("ONTO0224","isA","ONTO0099","things"));
		addOntology(new Ontology("ONTO0225","locationOf","ONTO0181","spatial"));
		addOntology(new Ontology("ONTO0226","desiresEvent","ONTO0086","goal"));
		addOntology(new Ontology("ONTO0226","locationOf","ONTO0180","spatial"));
		addOntology(new Ontology("ONTO0231","propertyOf","ONTO0229","description"));
		addOntology(new Ontology("ONTO0234","propertyOf","ONTO0235","description"));
		addOntology(new Ontology("ONTO0234","propertyOf","ONTO0236","description"));
		addOntology(new Ontology("ONTO0237","propertyOf","ONTO0238","description"));
		addOntology(new Ontology("ONTO0237","propertyOf","ONTO0239","description"));
		addOntology(new Ontology("ONTO0240","propertyOf","ONTO0232","description"));

	}
	
	public void addSemanticRelationRule(SemanticRelationRule srr)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("SemanticRelation", srr.getSemanticRelation());
		cv.put("Action", srr.getAction());
		cv.put("Agens", srr.getAgens());
		cv.put("Patiens", srr.getPatiens());
		cv.put("Target", srr.getTarget());
		cv.put("Instrument", srr.getInstrument());
		cv.put("Notes", srr.getNotes());
		
		database.insert(TableCreator.TABLE_SEMANTIC_RELATION_RULES, null, cv);
	}
	
	public Vector<SemanticRelationRule> getSemanticRelationRules()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<SemanticRelationRule> srrs = new Vector<SemanticRelationRule>();
		
		String selectQuery = "SELECT * FROM tableSemanticRelationRules";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				SemanticRelationRule srr = new SemanticRelationRule();				
				srr.setSemanticRelation(cursor.getString(0));
				srr.setAction(cursor.getString(1));
				srr.setAgens(cursor.getString(2));
				srr.setPatiens(cursor.getString(3));
				srr.setTarget(cursor.getString(4));
				srr.setInstrument(cursor.getString(5));
				srr.setNotes(cursor.getString(6));
				Log.d("SRRs", srr.getSemanticRelation());
				srrs.add(srr);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return srrs;
	}	
	
	public void InitializeSemanticRelationRules()
	{
		addSemanticRelationRule(new SemanticRelationRule("canDo","C2","C1","","","",""));
		addSemanticRelationRule(new SemanticRelationRule("capableOf","C2V","PrevAgens","","C2C","","2nd concept must be a verb phrase: Verb(C2V) + Complement (C2C)"));
		addSemanticRelationRule(new SemanticRelationRule("conceptuallyRelatedTo","","","","","","disregarded during onto access"));
		addSemanticRelationRule(new SemanticRelationRule("desiresEvent","WORD0034","PrevAgens","","C2","",""));
		addSemanticRelationRule(new SemanticRelationRule("desiresNotEvent","WORD0120 WORD0034","PrevAgens","","C2","",""));
		addSemanticRelationRule(new SemanticRelationRule("effectOf","WORD0161","C1","","C2","","2nd concept must be a verb phrase"));
		addSemanticRelationRule(new SemanticRelationRule("effectOfIsState","WORD0161","C1","","C2","","2nd concept must be a noun phrase"));
		addSemanticRelationRule(new SemanticRelationRule("eventForGoalEvent","C1 WORD0117 C2","PrevAgens","","","",""));
		addSemanticRelationRule(new SemanticRelationRule("eventForGoalState","C1 WORD0117 WORD0162 C2","PrevAgens","","","",""));
		addSemanticRelationRule(new SemanticRelationRule("eventRequiresObject","C1","PrevAgens","","C2","",""));
		addSemanticRelationRule(new SemanticRelationRule("firstSubeventOf","C2","PrevAgens","","","","must come first"));
		addSemanticRelationRule(new SemanticRelationRule("isA","","","","","","disregarded during onto access"));
		addSemanticRelationRule(new SemanticRelationRule("lastSubeventOf","C2","PrevAgens","","","","disregard first concept"));
		addSemanticRelationRule(new SemanticRelationRule("locationOf","WORD0083 locPrep WORD0125","C1","","C2","",""));
		addSemanticRelationRule(new SemanticRelationRule("madeOf","WORD0083 WORD0056 WORD0163","C1","","C2","",""));
		addSemanticRelationRule(new SemanticRelationRule("oftenNear","WORD0083 WORD0164","C1","","C2","",""));
		addSemanticRelationRule(new SemanticRelationRule("partOf","WORD0083 WORD0165 WORD0163","C2","","C1","",""));
		addSemanticRelationRule(new SemanticRelationRule("propertyOf","WORD0083","C1","","C2","",""));
		addSemanticRelationRule(new SemanticRelationRule("usedFor","WORD0166 C1 WORD0117 C2","PrevAgens","","","","2nd concept must always be a verb phrase"));
	}
	
	public void addStoryPlotTracker(StoryPlotTracker spt)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("PlotID", spt.getID());
		cv.put("Name", spt.getString());
		cv.put("Stage", spt.getStage());
		cv.put("AuthorGoals", spt.getAuthorGoals());
		
		database.insert(TableCreator.TABLE_STORY_PLOT_TRACKER, null, cv);
	}
	
	public Vector<StoryPlotTracker> getStoryPlotTracker()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<StoryPlotTracker> spts = new Vector<StoryPlotTracker>();
		
		String selectQuery = "SELECT * FROM tableStoryPlotTracker";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				StoryPlotTracker spt = new StoryPlotTracker();				
				spt.setID(cursor.getString(0));
				spt.setString(cursor.getString(1));
				spt.setStage(cursor.getString(2));
				spt.setAuthorGoals(cursor.getString(3));
				Log.d("SPTs", spt.getString());
				spts.add(spt);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return spts;
	}
/*	
	public void	InitializeStoryPlotTracker()
	{
		addStoryPlotTracker(new StoryPlotTracker("SPAT0001","Defy - not do rule [brush teeth]","Problem","AUTH0001;AUTH0002"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0002","Experience consequence","Rising Action","AUTH0003;AUTH0004"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0003","Do the lesson","Solution","AUTH0005;AUTH0006"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0004","Learn the benefit","Climax","AUTH0007;AUTH0008"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0005","Defy - not do rule [be selfish]","Problem","AUTH0009;AUTH0010"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0006","Experience consequence","Rising Action","AUTH0011;AUTH0012"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0007","Do the lesson","Solution","AUTH0013;AUTH0014"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0008","Learn the benefit","Climax","AUTH0015;AUTH0008"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0009","Defy - [break object]","Problem","AUTH0024;AUTH0025"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0010","Experience consequence","Rising Action","AUTH0026;AUTH0027"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0011","Do the lesson","Solution","AUTH0028;AUTH0029"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0012","Learn the benefit","Climax","AUTH0030;AUTH0008"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0013","Defy - break rule [be neat]","Problem","AUTH0016;AUTH0017"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0014","Experience consequence","Rising Action","AUTH0018;AUTH0004"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0015","Do the lesson","Solution","AUTH0020;AUTH0021"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0016","Learn fault","Climax","AUTH0022;AUTH0008"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0017","Realize the benefit","Climax","AUTH0007;AUTH0008"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0018","Defy - break rule [eat on time]","Problem","AUTH0016;AUTH0019"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0019","Experience consequence","Rising Action","AUTH0023;AUTH0031"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0020","Inform lesson","Solution","AUTH0020;AUTH0032"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0021","Learn fault","Climax","AUTH0022;AUTH0033"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0022","Learn the benefit","Climax","AUTH0007;AUTH0033"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0023","Want much [eat healthy foods]","Problem","AUTH0034;AUTH0077;AUTH0035"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0024","Experience consequence","Rising Action","AUTH0036;AUTH0004"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0025","Inform Lesson","Solution","AUTH0037;AUTH0038"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0026","Scared child [be brave (school)]","Problem","AUTH0039;AUTH0040"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0027","Discomforted child","Rising Action","AUTH0041;AUTH0046;AUTH0042"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0028","Inform Lesson","Solution","AUTH0056;AUTH0043;AUTH0044"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0029","Scared child [be brave (lights off)]","Problem","AUTH0039;AUTH0045"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0030","Discomforted child","Rising Action","AUTH0040;AUTH0041"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0031","Inform Lesson","Solution","AUTH0042;AUTH0047;AUTH0044"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0032","Scared child","Problem","AUTH0048;AUTH0049"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0033","Discomforted child","Rising Action","AUTH0050;AUTH0041"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0034","Soothe child","Solution","AUTH0051;AUTH0042;AUTH0052;AUTH0053"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0035","Defy - not do rule [sleep early]","Problem","AUTH0054;AUTH0002"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0036","Do the lesson","Solution","AUTH0005;AUTH0055"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0037","Want much [sleep early]","Problem","AUTH0057;AUTH0058;AUTH0059"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0038","Experience consequence","Rising Action","AUTH0003;AUTH0004;AUTH0060"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0039","Inform Lesson","Solution","AUTH0038;AUTH0055"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0040","Learn the benefit","Climax","AUTH0030;AUTH0033"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0041","Child wander off without permission","Problem","AUTH0068;AUTH0069"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0042","Discomforted child","Rising Action","AUTH0070;AUTH0071"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0043","Inform Lesson","Solution","AUTH0072;AUTH0073"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0044","Learn the benefit","Climax","AUTH0074;AUTH0075"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0045","Learn the benefit","Climax","AUTH0074;AUTH0008"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0046","Lie - break object","Problem","AUTH0061;AUTH0062"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0047","Discomforted child","Rising Action","AUTH0063;AUTH0064"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0048","Do the lesson","Solution","AUTH0065;AUTH0066"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0049","Learn the benefit","Climax","AUTH0067;AUTH0033"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0050","Learn the benefit","Climax","AUTH0067;AUTH0008"));
		addStoryPlotTracker(new StoryPlotTracker("SPAT0051","Defy - not do rule [take a bath || brush teeth]","Problem","AUTH0076;AUTH0002"));
	}
*/
	
	public void addTheme(IGTheme t)
	{
		ContentValues cv = new ContentValues();
		cv.put("ThemeID", t.getID());
		cv.put("InitActivity", t.getInitActivity());
		cv.put("Lesson", t.getString());
		cv.put("MoralLesson", t.getMoralLesson());
		cv.put("RelatedObjects", t.getRelatedObjects());
		cv.put("Problem", t.getProblem());
		cv.put("RisingAction", t.getRisingAction());
		cv.put("Solution", t.getSolution());
		cv.put("Climax", t.getClimax());
		cv.put("InitSettings", t.getInitSettings());
		cv.put("InitTime", t.getInitTime());
		database.insert(TableCreator.TABLE_THEMES, null, cv);
	}
	
	public Vector<IGTheme> getIGThemes()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<IGTheme> themes = new Vector<IGTheme>();
		
		String selectQuery = "SELECT * FROM tableThemes";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				IGTheme t = new IGTheme();	
				t.setID(cursor.getString(0));
				t.setInitActivity(cursor.getString(1));
				t.setString(cursor.getString(2));
				t.setMoralLesson(cursor.getString(3));
				t.setRelatedObjects(cursor.getString(4));
				t.setProblem(cursor.getString(5));
				t.setRisingAction(cursor.getString(6));
				t.setSolution(cursor.getString(7));
				t.setClimax(cursor.getString(8));
				t.setInitSettings(cursor.getString(9));
				t.setInitTime(cursor.getString(10));
				Log.d("Themes", t.getID());
				themes.add(t);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return themes;
	}
	
	public void	InitializeThemes()
	{
		addTheme(new IGTheme("THME0001","WORD0022","WORD0252 WORD0021","TAKE_BATH","OB034;OB019","SPAT0051","SPAT0002","SPAT0003","SPAT0004","WORD0177 WORD0144;WORD0177 WORD0145","WORD0147;WORD0150;WORD0018;WORD0153"));
		addTheme(new IGTheme("THME0002","WORD0242 WORD0312","WORD0410 WORD0240","BRUSH_TEETH","OB027","SPAT0001","SPAT0002","SPAT0003","SPAT0004;SPAT0008","WORD0177 WORD0144;WORD0177 WORD0145","WORD0150;WORD0152"));
		addTheme(new IGTheme("THME0003","WORD0022 WORD0164","WORD0083 WORD0241","BE_CAREFUL","OB016;OB015","SPAT0009","SPAT0010","SPAT0011","SPAT0012;SPAT0040","WORD0177 WORD0144","WORD0147;WORD0150;WORD0018;WORD0153"));
		addTheme(new IGTheme("THME0004","WORD0242","WORD0242 WORD0249 WORD0416","EAT_HEALTHY_FOODS","OB002;OB004;OB007;OB009;OB010;OB011;OB014;OB037","SPAT0023","SPAT0024","SPAT0025","SPAT0017","WORD0177 WORD0144;WORD0178 WORD0144","WORD0147;WORD0150;WORD0018;WORD0153"));
		addTheme(new IGTheme("THME0005","WORD0022","WORD0083 WORD0248","BE_NEAT","OB005;OB013;OB018;OB024;OB029;OB030;OB031;OB032;OB028","SPAT0013","SPAT0014","SPAT0015","SPAT0016;SPAT0017","WORD0177 WORD0144;WORD0177 WORD0145","WORD0147;WORD0150;WORD0018;WORD0153"));
		addTheme(new IGTheme("THME0006","WORD0324 WORD0117 WORD0325","WORD0083 WORD0243","BE_BRAVE (sleep alone)","OB017;OB016","SPAT0029","SPAT0030","SPAT0031","SPAT0017","WORD0177 WORD0144","WORD0153;WORD0152"));
		addTheme(new IGTheme("THME0007","WORD0324 WORD0117 WORD0171","WORD0083 WORD0243","BE_BRAVE (dentist)","OB022;OB026;OB035","SPAT0032","SPAT0033","SPAT0034","SPAT0017","WORD0178 WORD0144","WORD0147;WORD0150;WORD0018"));
		addTheme(new IGTheme("THME0008","WORD0022;WORD0316 WORD0206","WORD0244 WORD0245","SLEEP_EARLY (want much)","OB017;OB001","SPAT0037","SPAT0038","SPAT0039","SPAT0004;SPAT0022","WORD0177 WORD0144","WORD0153;WORD0152"));
		addTheme(new IGTheme("THME0009","WORD0022","WORD0107","LEARN_TO_SHARE","OB005;OB013;OB018;OB024;OB029;OB030;OB031;OB032;OB028","SPAT0005;SPAT0001","SPAT0006","SPAT0007","SPAT0008","WORD0178 WORD0145","WORD0147;WORD0150;WORD0018"));
		addTheme(new IGTheme("THME0010","WORD0022","WORD0107","LEARN_TO_SHARE","OB021;OB023;OB032","SPAT0005;SPAT0001","SPAT0006","SPAT0007","SPAT0008","WORD0178 WORD0145","WORD0147;WORD0150;WORD0018"));
		addTheme(new IGTheme("THME0011","WORD0324 WORD0117 WORD0125 WORD0172","WORD0122 WORD0247","LEARN_TO_ASK_PERMISSION","OB005;OB013;OB018;OB024;OB029;OB030;OB031;OB032;OB028;OB009;OB010","SPAT0041","SPAT0042","SPAT0043","SPAT0044;SPAT0045","WORD0178 WORD0144","WORD0147;WORD0150;WORD0018"));
		addTheme(new IGTheme("THME0012","WORD0022 WORD0164","WORD0083 WORD0246","BE_HONEST","OB016;OB015","SPAT0046","SPAT0047","SPAT0048","SPAT0050","WORD0177 WORD0144","WORD0147;WORD0150;WORD0018;WORD0153"));
		addTheme(new IGTheme("THME0013","WORD0242","WORD0242 WORD0138 WORD0020","EAT_ON_TIME","OB002;OB014;OB036;OB037;OB009;OB010","SPAT0018","SPAT0019","SPAT0020","SPAT0016;SPAT0017;SPAT0021;SPAT0022","WORD0177 WORD0144;WORD0178 WORD0144","WORD0147;WORD0150;WORD0018;WORD0153"));
		addTheme(new IGTheme("THME0014","WORD0022;WORD0316 WORD0206","WORD0244 WORD0245","SLEEP_EARLY (defy-not do rule)","OB017;OB001","SPAT0035","SPAT0002","SPAT0036","SPAT0004","WORD0177 WORD0144","WORD0153;WORD0152"));
		addTheme(new IGTheme("THME0015","WORD0324 WORD0117 WORD0149","WORD0083 WORD0243","BE_BRAVE (school)","OB013;OB018;OB028;OB030;OB006;OB012;OB003","SPAT0026","SPAT0027","SPAT0028","SPAT0017","WORD0178 WORD0145","WORD0147;WORD0150;WORD0018;WORD0153"));

	}

	/*
	 * added
	 * added by Jasmine starting here
	 * added
	 */
	
	/**
	 * Instantiates the contents of a {@link storyplanner.component.db.Theme} given the themeId
	 *
	 * @param themeId the id of the theme to be instantiated
	 * @return the object Theme
	 * @throws StoryPlannerException raised if an error occurred while instantiating a Theme object
	 */
	//from StoryPlannerDatabase.java
	public IGTheme instantiateTheme(String themeID) throws StoryPlannerException{

		SQLiteDatabase database = this.getWritableDatabase();
		IGTheme temp = null;

		String selectQuery = "SELECT * FROM tableThemes WHERE _id = '" + themeID + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);

		String problem = "", risingAction = "", solution = "", climax = "", initActivity = "";
    	Vector<String> tempVector = new Vector<String>();
		
		if(cursor.moveToFirst()){
			do{
				temp = new IGTheme(cursor.getString(0), cursor.getString(2), cursor.getString(3),
						cursor.getString(4), cursor.getString(9), cursor.getString(10));
	           	problem = cursor.getString(5);
	           	risingAction = cursor.getString(6);
	           	solution = cursor.getString(7);
	           	climax = cursor.getString(8);
	           	initActivity = cursor.getString(1);
			}while(cursor.moveToNext());						
		}
		
		Random rand = new Random();
    	tempVector = Parser.parseString(problem, ";");
    	temp.setProblem(tempVector.get(rand.nextInt(tempVector.size())));
    	tempVector = Parser.parseString(risingAction, ";");
    	temp.setRisingAction(tempVector.get(rand.nextInt(tempVector.size())));
    	tempVector = Parser.parseString(solution, ";");
    	temp.setSolution(tempVector.get(rand.nextInt(tempVector.size())));
    	tempVector = Parser.parseString(climax, ";");
    	temp.setClimax(tempVector.get(rand.nextInt(tempVector.size())));
    	tempVector = Parser.parseString(initActivity, ";");
    	temp.setInitActivity(tempVector.get(rand.nextInt(tempVector.size())));
		
    	cursor.close();
    	database.close();
    	return temp;
	}
	
	//from StoryPlannerDatabase.java
	//added throws ParserException
	public Vector<IGTheme> getRelatedThemes(String backgroundID) {
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<String> object = new Vector<String>();
		Vector<IGTheme> themes = new Vector<IGTheme>();
		
		String selectQuery = "SELECT * FROM tableBackgrounds WHERE _id = ?" ;
	    String selectionArgs [] = new String [] { backgroundID};
		//String selectQuery = "SELECT * FROM tableBackgrounds WHERE _id = '" + backgroundID + "'";
		Cursor cursor = database.rawQuery(selectQuery, selectionArgs);
		
		if(cursor.moveToFirst()){
			do{
				try {
					object = parse(cursor.getString(2));
				} catch (ParserException e) {
					e.printStackTrace();
				} //Theme
			}while(cursor.moveToNext());		
			
			for(int i=0;i<object.size();i++){
        		IGTheme t;
				try {
					t = instantiateTheme(object.elementAt(i));
					themes.add(t);
				} catch (StoryPlannerException e) {
					e.printStackTrace();
				}
        		
        	}
					
		}
		
		cursor.close();
		database.close();		
		return themes;
	}
	
	/**
     * Parses a string into a vector of strings
     * @param toParse the string to be parsed
     * @return Vector of strings parsed
     * @throws ParserException raised if an error occurred while parsing the string
     */
	//from StoryPlannerDatabase.java and PictureEditorDatabase.java
	private Vector<String> parse(String toParse) throws ParserException{

    	Vector<String> parsedObj = new Vector<String>();
    	int start = 0;
    	try{
    		for (int i = 0; i < toParse.length(); i++) {
        		if(toParse.charAt(i) == ';'){
        			parsedObj.addElement(toParse.substring(start, i));
        			start = i+1;
        		}
        		else if(i == toParse.length()-1)
        			parsedObj.addElement(toParse.substring(start, toParse.length()));
    		}
    	}catch(ArrayIndexOutOfBoundsException e){
    		throw new ParserException("Cannot parse the arguments",e);
    	}
    	return parsedObj;
    }
    
    /**
     * Instantiates the contents of a {@link storyplanner.component.db.Plot} given the plotId
     *
     * @param plotID id of the plot to be instantiated
     * @return the {@link storyplanner.component.db.Plot} instantiated
     * @throws StoryPlannerException raised if an error occurred while instantiating the object Plot
     */
    //from StoryPlannerDatabase.java
    public StoryPlotTracker instantiatePlot(String plotID) throws StoryPlannerException {

    	SQLiteDatabase database = this.getWritableDatabase();
    	StoryPlotTracker spt = null;

		String selectQuery = "SELECT * FROM tableStoryPlotTracker WHERE _id = '" + plotID + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);

		if(cursor.moveToFirst()){
			do{
				try {
					spt = new StoryPlotTracker(cursor.getString(0), cursor.getString(1), cursor.getString(2), parse(cursor.getString(3)));
				} catch (ParserException e) {
					e.printStackTrace();
				}
			}while(cursor.moveToNext());						
		}
		cursor.close();
    	database.close();
		return spt;
    }

    /**
     * Instantiates the contents of an {@link storyplanner.component.db.AuthorGoal} given the authorGoalID
     * @param authorGoalID id of the author goal to be instantiated
     * @return the {@link storyplanner.component.db.AuthorGoal} instantiated
     * @throws StoryPlannerException raised if an error occurred while instantiating the object AuthorGoal
     */
    //from StoryPlannerDatabase.java
    public AuthorGoal instantiateAuthorGoal(String authorGoalID) throws StoryPlannerException{

    	SQLiteDatabase database = this.getWritableDatabase();
    	AuthorGoal ag = null;

		String selectQuery = "SELECT * FROM tableAuthorGoals WHERE _id = '" + authorGoalID + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);

		if(cursor.moveToFirst()){
			do{
				ag = new AuthorGoal();
				ag.setID(cursor.getString(0));
				ag.setString(cursor.getString(1));
				ag.setGoal(cursor.getString(2));
				ag.setConsequence(cursor.getString(3));
				Log.d("AuthorGoals", ag.getID());;
			}while(cursor.moveToNext());						
		}		
		cursor.close();
    	database.close();
		return ag;
    }

    /**
     * Instantiates the contents of a {@link storyplanner.component.db.CharacterGoal} given the characterGoalId
     * @param characterGoalID the id of the characterGoal to be instantiated
     * @return {@link storyplanner.component.db.CharacterGoal} instantiated
     * @throws StoryPlannerException raised if an error occurred while instantiating the object CharacterGoal
     */
    //from StoryPlannerDatabase.java
    public CharacterGoal instantiateCharacterGoal(String characterGoalID) throws StoryPlannerException{
    	
    	SQLiteDatabase database = this.getWritableDatabase();
    	CharacterGoal cg = null;

		String selectQuery = "SELECT * FROM tableCharacterGoals WHERE _id = '" + characterGoalID + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);

		if(cursor.moveToFirst()){
			do{
				String bool;
				cg = new CharacterGoal();
				cg.setID(cursor.getString(0));
				cg.setString(cursor.getString(1));
				cg.setAction(cursor.getString(2));
				cg.setAgens(cursor.getString(3));
				cg.setPatiens(cursor.getString(4));
				bool = cursor.getString(5);
				if(bool.equals("0")){
					bool = "false";
				} else if(bool.equals("1")){
					bool = "true";
				}
				cg.setNegated(Boolean.valueOf(bool));
				Log.d("CharacterGoals", cg.getID());
				
			}while(cursor.moveToNext());						
		}		
		cursor.close();
    	database.close();
		return cg;
    }

    /**
     * Instantiates the contents of a {@link storyplanner.component.db.SemanticRule} given the semantic relation
     * @param semanticRel name of the semantic rule to be instantiated
     * @return the {@link storyplanner.component.db.SemanticRule} instantiated
     * @throws StoryPlannerException raised if an error occurred while instantiating the object SemanticRule
     */
    //from StoryPlannerDatabase.java
    public SemanticRelationRule instantiateSemanticRule(String semanticRel) throws StoryPlannerException{

    	SQLiteDatabase database = this.getWritableDatabase();
    	SemanticRelationRule srr = null;

		String selectQuery = "SELECT * FROM tableSemanticRelationRules WHERE SemanticRelation = '" + semanticRel + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);

		if(cursor.moveToFirst()){
			do{
				srr = new SemanticRelationRule();
				srr.setSemanticRelation(cursor.getString(0));
				srr.setAction(cursor.getString(1));
				srr.setAgens(cursor.getString(2));
				srr.setPatiens(cursor.getString(3));
				srr.setTarget(cursor.getString(4));
				srr.setInstrument(cursor.getString(5));
				srr.setNotes(cursor.getString(6));
				Log.d("SRRs", srr.getSemanticRelation());
			}while(cursor.moveToNext());						
		}
		cursor.close();
    	database.close();
    	return srr;
	}
    
    /**
     * Gets all the characters from the Characters table in the database except for the character with
     * the id specified in the parameter
     *
     * @param avoidedCharID the id of the character to be excluded in the retrieval
     * @return a vector containing the {@link Character} objects
     * @throws PBException thrown when an error was encountered in the data retrieval
     */
    //from PictureEditorDatabase.java
    public Vector<IGCharacter> getCharacters(String avoidedCharID) throws PBException
    {
    	SQLiteDatabase database = this.getWritableDatabase();
		Vector<IGCharacter> IGCharacters = new Vector<IGCharacter>();
		
		String selectQuery = "SELECT * FROM tableIGCharacters WHERE Role = 'WORD0085' AND NOT _id = '" + avoidedCharID + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				IGCharacter igc = new IGCharacter();
				igc.setID(cursor.getString(0));
				igc.setString(cursor.getString(1));
				igc.setGender(cursor.getString(2));
				igc.setType(cursor.getString(3));
				igc.setRole(cursor.getString(4));
				igc.setMotherCharacterID(cursor.getString(5));
				igc.setFatherCharacterID(cursor.getString(6));
				igc.setImageFilePath(cursor.getString(7));
				Log.d("IGCharacters", igc.getString());
				IGCharacters.add(igc);
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return IGCharacters;
    }
    
    /**
     * Gets all the objects in the database given the name of the table and column to search in.
     *
     * @param id of the column to get the objects list from
     * @param tableName the name of the table to retrieve data from (i.e. "Background" for the Backgrounds table, "Theme" for the Themes table)
     * @param columnName the name of the column containing the objects list
     * @return a vector containing the {@link PBObject} objects referred to in the list
     * @throws PBException thrown when an error was encountered in the data retrieval
     * @throws ParserException 
     */
    //from PictureEditorDatabase.java
    public Vector<IGObject> getObjects(String id, String tableName, String columnName) throws PBException
    {
    	SQLiteDatabase database = this.getWritableDatabase();
		Vector<IGObject> IGObjects = new Vector<IGObject>();
		Vector<String> objIDs = new Vector<String>();
		String columnID = "_id";
				
		String selectQuery = "SELECT " + columnName + " FROM " + tableName +" WHERE " + columnID + " = '" + id + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		int index = cursor.getColumnIndex(columnName);

		if(cursor.moveToFirst()){
			
			do{
					try {
						objIDs = parse(cursor.getString(index));
					} catch (ParserException e) {
						e.printStackTrace();
					}
				
				for (int i = 0; i < objIDs.size(); i++) {
            		String selectQuery2 = "SELECT * FROM tableIGObjects WHERE _id = '" + objIDs.elementAt(i) + "'";
            		Cursor cursor2 = database.rawQuery(selectQuery2, null);

            		while(cursor2.moveToNext())
                    {
                    	IGObjects.add(new IGObject(cursor2.getString(0), cursor2.getString(1), cursor2.getString(2), cursor2.getString(3)));
                    	//Log.d("IGObjects", IGObjects.getObjectWord());
                    }
				}
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return IGObjects;
    }
    
    /**
	 * Creates a {@link Background} object given the information
	 * retrieved from the Backgrounds table in the database
	 * @param bgId the database id of the background
	 * @return the {@link Background} object created that contains the information retrieved from the database
	 * @throws PBException thrown when an error was encountered in the data retrieval
     * @throws ParserException 
     */
    //from PictureEditorDatabase.java
    //added throws ParserException
	public Background instantiateBackground(String bgId) throws PBException, ParserException{

		SQLiteDatabase database = this.getWritableDatabase();
		Background bgInitial = new Background();
		
		String selectQuery = "SELECT * FROM tableBackgrounds WHERE _id = '" + bgId + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				bgInitial = new Background(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
						cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
				Log.d("Backgrounds", bgInitial.getBackgroundWord());
				
				if(bgInitial.getRequiredRole() != null && parse(bgInitial.getRequiredRole()).size()> 0){
            		Vector<String> requiredRoles = parse(bgInitial.getRequiredRole());
            		int randomizer = requiredRoles.size();
	            	randomizer *= Math.random();
	            	bgInitial.setRequiredRole(requiredRoles.get(randomizer));
            	}
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return bgInitial;
	}
	
	/**
	 * Creates a {@link Character} object given the information
	 * retrieved from the Characters table in the database
	 * @param charId the database id of the character
	 * @return the {@link Character} object created that contains the information retrieved from the database
	 * @throws PBException thrown when an error was encountered in the data retrieval
     */
	//from PictureEditorDatabase.java
    public IGCharacter instantiateCharacter(String charID) throws PBException{

    	SQLiteDatabase database = this.getWritableDatabase();
		IGCharacter tempChar = new IGCharacter();
		
		String selectQuery = "SELECT * FROM tableIGCharacters WHERE _id = '" + charID + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				tempChar = new IGCharacter(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
						cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
				Log.d("IGCharacters", tempChar.getString());
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return tempChar;
    }

    /**
	 * Creates a {@link Character} object that has the role specified. The function would randomly choose between characters
	 * if more than one character has the same role.
	 *
	 * @param RoleID the {@link Word} id of the character's role
	 * @return the {@link Character} object created that contains the information retrieved from the database
	 * @throws PBException thrown when an error was encountered in the data retrieval
     */
    //from PictureEditorDatabase.java
    public IGCharacter instantiateCharacterViaRole(String RoleID) throws PBException{

    	SQLiteDatabase database = this.getWritableDatabase();
		Vector<IGCharacter> tempChars = new Vector<IGCharacter>();
		
		String selectQuery = "SELECT * FROM tableIGCharacters WHERE Role = '" + RoleID + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				tempChars.add(new IGCharacter(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
						cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7)));
				//Log.d("IGCharacters", tempChars.getName());
			}while(cursor.moveToNext());			
		
			int randomizer = tempChars.size();
	    	randomizer *= Math.random();
	        return tempChars.get(randomizer);
	        
		}
		cursor.close();
		database.close();
        return null;
    }
    

    /**
	 * Creates a {@link PBObject} object given the information
	 * retrieved from the Characters table in the database
	 * @param objectId the database id of the object
	 * @return the {@link PBObject} object created that contains the information retrieved from the database
	 * @throws PBException thrown when an error was encountered in the data retrieval
     */
    //from PictureEditorDatabase.java
    public IGObject instantiateObject(String objectID) throws PBException{

    	SQLiteDatabase database = this.getWritableDatabase();
		IGObject tempObject = new IGObject();
		
		String selectQuery = "SELECT * FROM tableIGObjects WHERE _id = '" + objectID + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				tempObject = new IGObject(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();		
		return tempObject;
    }
    
    /**
     * Gets the maximum number of story files saved in the SaveFiles table in the database
     * @return the maximum number of story files saved in the SaveFiles table in the database
     * @throws PBException thrown when an error was encountered in the data retrieval
     */
    //from PictureEditorDatabase.java
    public int getMaxNumOfSavedFiles() throws PBException{

		int max = 0;
		SQLiteDatabase database = this.getWritableDatabase();
		
		String selectQuery = "SELECT MAX(StoryID) AS \"maxNum\" FROM tableStoryFiles";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				max = cursor.getInt(0);
			}while(cursor.moveToNext());			
		}
		cursor.close();
        database.close();
		return max+1;
	}
    
    /**
	 * Gets list of saved stories in a user's library.
	 * @param name the name of the library owner
	 * @return a vector containing all the story titles in the user's library
	 * @throws PBException thrown when an error was encountered in the data retrieval
     */
    //from PictureEditorDatabase.java
	public Vector<String> getSavedStories(String name) throws PBException{
		
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<String> temp = new Vector<String>();

		String selectQuery = "SELECT StoryTitle FROM tableStoryFiles WHERE _id = '" + name + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				String entry = cursor.getString(0);
				temp.add(entry);
			}while(cursor.moveToNext());			
		}
		cursor.close();
        database.close();
    	return temp;
	}
	
	/**
	 * Gets the file path of a story file from the SaveFiles table in the database.
	 * @param ID the id of the column
	 * @return the file path of the story file
	 * @throws PBException thrown when an error was encountered in the data retrieval
     */
	//from PictureEditorDatabase.java
	public String getURL(int storyID) throws PBException{
		
		SQLiteDatabase database = this.getWritableDatabase();
		String temp = "";
		String selectQuery = "SELECT FilePath FROM tableStoryFiles WHERE _id = '" + storyID + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				temp = cursor.getString(0);
			}while(cursor.moveToNext());			
		}
		cursor.close();
        database.close();
    	return temp;
	}

	/**
	 * Gets the file path of the image of a character sticker.
	 * @param ID the database id of the character
	 * @return the file path of the image
	 * @throws PBException thrown when an error was encountered in the data retrieval
     */
	//from PictureEditorDatabase.java
    public String getCharacterImage(String characterID) throws PBException{

    	SQLiteDatabase database = this.getWritableDatabase();
		String temp = "";
		String selectQuery = "SELECT ImageFilePath FROM tableIGCharacters WHERE _id = '" + characterID + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				temp = cursor.getString(0);
			}while(cursor.moveToNext());			
		}
		cursor.close();
        database.close();
        return temp;
    }
	
    /**
	 * Gets the file path of the image of an object sticker.
	 * @param ID the database id of the object
	 * @return the file path of the image
	 * @throws PBException thrown when an error was encountered in the data retrieval
     */
    //from PictureEditorDatabase.java
    public String getObjectImage(String objectID) throws PBException{

    	SQLiteDatabase database = this.getWritableDatabase();
		String temp = "";
		String selectQuery = "SELECT ImageFilePath FROM tableIGObjects WHERE _id = '" + objectID + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				temp = cursor.getString(0);
			}while(cursor.moveToNext());			
		}
		cursor.close();
        database.close();
        return temp;
    }
    
    /**
     * Gets all the adult characters related to a specified background.
     *
     * @param bgId the id of the background to get the list of adult characters from
     * @return a vector containing the {@link Character} objects
     * @throws PBException thrown when an error was encountered in the data retrieval
     */
    /*
    public Vector<Character> getExtras(String bgId) throws PBException
    {
    	Vector<Character> extras = new Vector<Character>();

    	try
        {
        	con = pbdb.connect();
        	if(con != null)
            {
        		PreparedStatement ps = con.prepareStatement("SELECT CharsIncluded from Backgrounds where BackgroundID = ?");
        		ps.setString(1, bgId);

            	ResultSet rs = ps.executeQuery();
            	Vector<String> charIDs = new Vector<String>();
            	while(rs.next())
            		charIDs = parse(rs.getString("CharsIncluded"));

            	for (int i = 0; i < charIDs.size(); i++) {
            		ps = con.prepareStatement("SELECT * from Characters where CharID = ?");
            		ps.setString(1, charIDs.elementAt(i));

            		rs = ps.executeQuery();
                    ImageIcon stickerPic;
                	Character temp;
                	while(rs.next())
                    {
                    	stickerPic = new ImageIcon(getClass().getResource(rs.getString("ImageFilePath")));
                    	temp = new Character(rs.getString("CharID"), rs.getString("CharName"));
                    	temp.setGender(rs.getString("Gender"));
                    	temp.setType(rs.getString("AnimalType"));
                    	temp.setRole(rs.getString("Role"));
                    	temp.setCharMother(rs.getString("CharMother"));
                    	temp.setCharFather(rs.getString("CharFather"));
                    	temp.setLblSticker(stickerPic);
                    	extras.add(temp);
                    }
				}
            }
        }catch(SQLException e) {
        	throw new PBException("Cannot get characters from the database",e);
        }catch(ParserException e) {
        	throw new PBException(e);
        }finally {
        	pbdb.disconnect();
        }
        return extras;
   }
*/
    
    /**
	 * Instantiates Word object given the conceptID and the user's age
	 *
	 * @param conceptID id of the word
	 * @param age age of the user used to identify which word in the lexicon should be used
	 *
	 * @return instantiated Word object {@link sentencegenerator.component.db.Word}
	 *
	 * @throws PBException raised if there's no word found in the lexicon that corresponds to the conceptID
	 */
    //from PBLexicon.java
    public Word instantiateWord(String conceptID, int age) throws PBException{

    	SQLiteDatabase database = this.getWritableDatabase();
    	Word word = new Word(conceptID);
		String selectQuery = "SELECT Word FROM tableWords WHERE _id = '" + convertToWordID(conceptID, age) + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				word.setString(cursor.getString(0));
			}while(cursor.moveToNext());			
		}

    	addPartOfSpeech(word);
    	addFeatures(word);
    	
    	cursor.close();
    	database.close();

    	return word;
    }
    
    /**
     * Sets the part of speech of the given word
     *
     * @param word Word object whose part of speech is being searched
     *
     * @throws PBException raised if an error occurred while retrieving or setting the word's part of speech
     */
    //from PBLexicon.java
    private void addPartOfSpeech(Word word) throws PBException{

    	SQLiteDatabase database = this.getWritableDatabase();
		String selectQuery = "SELECT PartOfSpeech FROM tableConceptMapper WHERE _id = '" + word.getID() + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				word.setPartOfSpeech(cursor.getString(0));
			}while(cursor.moveToNext());			
		}
    }
    
    /**
     * Sets the features of the given word
     *
     * @param word Word object whose part of speech is being searched
     *
     * @throws PBException raised if an error occurred while retrieving or setting the word's features
     */
    //from PBLexicon.java
    //pos not all in UPPERCASE
    private void addFeatures(Word word) throws PBException{
    	
    	SQLiteDatabase database = this.getWritableDatabase();
    	String pos = word.getPartOfSpeech();
    	
    	System.out.println(word.getPartOfSpeech());

		if(pos.equalsIgnoreCase("Adverb") || pos.equalsIgnoreCase("Preposition")){
			String selectQuery = "SELECT Type FROM table" + pos + "s WHERE _id = '" + word.getID() + "'";
			Cursor cursor = database.rawQuery(selectQuery, null);

			if(cursor.moveToFirst()){
				do{
					word.setType(cursor.getString(0));
				}while(cursor.moveToNext());			
			}
		}
    	else if(pos.equalsIgnoreCase("Noun")){
    		String selectQuery = "SELECT Classification, isSingular FROM table" + pos + "s WHERE _id = '" + word.getID() + "'";
			Cursor cursor = database.rawQuery(selectQuery, null);

			if(cursor.moveToFirst()){
				do{
					String bool;
					word.setClassification(cursor.getString(0));
					bool = cursor.getString(1);
					if(bool.equals("0")){
						bool = "false";
					} else if(bool.equals("1")){
						bool = "true";
					}
					word.setSingular(Boolean.valueOf(bool));
				}while(cursor.moveToNext());			
			}
    	}
    	else if(pos.equalsIgnoreCase("Pronoun")){
    		String selectQuery = "SELECT Type, Gender FROM table" + pos + "s WHERE _id = '" + word.getID() + "'";
			Cursor cursor = database.rawQuery(selectQuery, null);

			if(cursor.moveToFirst()){
				do{
					word.setType(cursor.getString(0));
					word.setGender(cursor.getString(1));
				}while(cursor.moveToNext());			
			}
    	}
    }
    
    /**
     * Converts the conceptID into its corresponding wordID
     *
     * @param conceptID conceptID to be converted
     * @param age age of the user used to identify which word in the lexicon should be used
     *
     * @throws PBException raised if an error occurred while converting the conceptID into wordID
     */
    //from PBLexicon.java
    //case Change Number
    private String convertToWordID(String conceptID, int age) throws PBException{

    	String columnName;
    	switch(age){
	    	case 6: columnName = "AgeSixWordID"; break;
	    	case 8: columnName = "AgeEightWordID"; break;
	    	case 7:
	    	default: columnName = "AgeSevenWordID"; break;
    	}

    	String id = null;
    	
    	SQLiteDatabase database = this.getWritableDatabase();
		String selectQuery = "SELECT " + columnName + " FROM tableConceptMapper WHERE _id = '" + conceptID + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				id = cursor.getString(0);
			}while(cursor.moveToNext());			
		}
		return id;
    }
    
    /**
     * Instantiate Word object given the word string
     *
     * @param stringWord the string word to be instantiated
     * @param age age of the user used to identify which word in the lexicon should be used
     *
     * @return Word object instantiated {@link sentencegenerator.component.db.Word}
     *
     * @throws PBException raised if there's no word found in the lexicon that corresponds to the conceptID
     */
    //from PBLexicon
    public Word getWord(String stringWord, int age) throws PBException{
    	return instantiateWord(getWordId(stringWord, age), age);
    }
    
    /**
     * Gets the Id of the given word
     *
     * @param word string word whose word id is being searched
     * @param age age of the user used to identify which word in the lexicon should be used
     *
     * @return id of the word
     *
     * @throws PBException raised if an error occurred while retrieving the id of the word
     */
    //from PBLexicon.java
    public String getWordId(String word, int age) throws PBException{

    	String columnName;
        switch(age){
        case 6: columnName = "AgeSixWordID"; break;
    	case 8: columnName = "AgeEightWordID"; break;
    	case 7:
    	default: columnName = "AgeSevenWordID"; break;
		}

        String id = null;
      
        SQLiteDatabase database = this.getWritableDatabase();
		String selectQuery = "SELECT c._id FROM tableConceptMapper c, tableWords w WHERE w._id = c." + columnName + " and w._id = (SELECT _id FROM tableWords WHERE Word = '" + word + "')";
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				id = cursor.getString(0);
			}while(cursor.moveToNext());			
		}
		
    	return id;
    }
    
    /**
	 * Instantiate {@link storyplanner.ontology.component.db.Concept} given the its id
	 * @param elementID id of the concept to be instantiated
	 * @return {@link storyplanner.ontology.component.db.Concept} instantiated
	 * @throws OntologyException raised if an error occurred while instantiating the concept
	 */
    //from OntologyAccess.java
	public Concept instantiateConcept(String elementID) throws OntologyException{
		Concept concept = null;
		SQLiteDatabase database = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM tableConcepts WHERE ElementID = '" + elementID + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);

		if(cursor.moveToFirst()){
			do{
				concept = new Concept(cursor.getString(0), cursor.getString(1), cursor.getString(2));
				Log.d("Concepts", concept.getOntologyID());
			}while(cursor.moveToNext());						
		}
    	return concept;
	}

	/**
	 * Gets the relationship existing between the two concepts under the specified category
	 * @param item1 first concept
	 * @param item2 second concept
	 * @param category category under which the relationship of the two concepts should fall
	 * @return ontology relationship/s found
	 * @throws OntologyException raised if an error occurred while searching for the relationship between the two concepts
	 */
    //from OntologyAccess.java
    public Vector<Ontology> getRelationship(String item1, String item2, String category) throws OntologyException {

		OntologyTree derivedTree = ontoSearch(item1, item2, category);
		Vector<Vector<Ontology>> paths = derivedTree.getPaths();
		Vector<Ontology> chosenPath = null;

		if(!paths.isEmpty())
		{
			Random rand = new Random();
    		int a = rand.nextInt(paths.size());
    		System.out.println(a + " random number " + paths.size());
    		chosenPath = paths.get(a);
    	}

		System.out.println("\ncontents of chosen path");

		for (int i = 0; i < chosenPath.size(); i++) {
			System.out.println(chosenPath.get(i).getElement1().getConcept() + " : " +
					chosenPath.get(i).getSemanticRelation() + " : " +
					chosenPath.get(i).getElement2().getConcept());
		}
		trimUnnecessary(chosenPath);
		return chosenPath;
	}

    /**
     * Gets the concept directly related to the specified concept
     * @param item1 concept whose direct relationship would be searched
     * @param category category under which the direct relationship of the two concepts should fall
     * @return the id of the concept directly related to the specified concept
     * @throws OntologyException raised if an error occurred while searching for the concept directly related to the specified concept
     */
    //from OntologyAccess.java
    public String getConcept(String item1, String category) throws OntologyException{
		return getElementID(ontoSearch(item1, category));
	}

    /**
     * Gets the concepts needed for the introduction of the story
     * @param item1 introduction concept
     * @param relationship specific relationship to be search for the introduction
     * @param category category under which the relationship of the concepts should fall
     * @return set of concepts for the introduction of the story
     * @throws OntologyException raised if an error occurred while searching for the concepts needed for the introduction of the story
     */
    //from OntologyAccess.java
    public Vector<String> getIntroConcepts(String item1, String relationship, String category) throws OntologyException{
    	return introSearch(item1, relationship, category);
    }

    //from OntologyAccess.java
    private Vector<String> introSearch(String item1, String relationship, String category) throws OntologyException{

    	SQLiteDatabase database = this.getWritableDatabase();
    	Vector<String> ontoID2 = new Vector<String>();
    	
    	try {
	    	Concept concept = instantiateConcept(item1);
	    	String selectQuery = "SELECT OntologyID2 FROM tableOntology WHERE OntologyID1 = '" + concept.getOntologyID() + "'" + 
	    			" AND SemanticRelation = '" + relationship  + "'" + " AND Category = '" + category + "'";
			Cursor cursor = database.rawQuery(selectQuery, null);
	
			if(cursor.moveToFirst()){
				do{
					ontoID2.add(cursor.getString(0));
					//Log.d("Concept", concept.getOntologyID());
				}while(cursor.moveToNext());
			}
			
			for (int i = 0; i < ontoID2.size(); i++)
	    		ontoID2.add(getElementID(ontoID2.remove(0)));
    	}catch(NullPointerException e){
    		throw new OntologyException(e);
    	}
	    return ontoID2;
    }  
    
    //from OntologyAccess.java
	private String ontoSearch(String Item1, String category)  throws OntologyException {

    	Vector<String> ontoID2s = new Vector<String>();
    	
    	try {
    		SQLiteDatabase database = this.getWritableDatabase();
    		Concept concept = instantiateConcept(Item1);

    		String selectQuery = "SELECT OntologyID2 FROM tableOntology WHERE OntologyID1 = '" + concept.getOntologyID() + "'"+ 
	    			" AND Category = '" + category.toLowerCase() + "'";
			Cursor cursor = database.rawQuery(selectQuery, null);
			
			if(cursor.moveToFirst()){
				do{
					ontoID2s.add(cursor.getString(0));
					//Log.d("Concept", concept.getOntologyID());
				}while(cursor.moveToNext());
			}

			int randomizer = ontoID2s.size();			
	    	randomizer *= Math.random();
	    	
		    return ontoID2s.get(randomizer);
    	}catch(SQLException e){
    		throw new OntologyException("Cannot search for an ontology concept", e);
    	}catch(OntologyException e){
    		throw e;
    	}catch(Exception e){
    		
    		try {
    			Item1 = Item1.substring(Item1.indexOf(" ") + 1);
				System.out.println("&&&&&&&&&&&" + Item1);
				return ontoSearch(Item1, category); //this line causes stackoverflowerror
    		}catch(Exception ne2){
    			throw new OntologyException("parameter do not exist in the ontology " + Item1);
    		}
    	}
    	//}finally{
    	//	close();
    	//}
    	//return null;
	}

	//from OntologyAccess.java
	private OntologyTree ontoSearch(String Item1, String Item2, String category) throws OntologyException {
		int targetLevel = -1;
		try{
			Concept startConcept = instantiateConcept(Item1);
			Concept endConcept = instantiateConcept(Item2);
			System.out.println("start: " + startConcept.getOntologyID() + " end:" + endConcept.getOntologyID());
			OntologyTree ontoTree = new OntologyTree(startConcept, endConcept);

			if(!searchPL(ontoTree, ontoTree.getRoot(), endConcept, category)){

				ontoTree.getNextParent();
				while(targetLevel!=ontoTree.getCurrentLevel()){
					if(searchPL(ontoTree, ontoTree.getCurrentParent(), endConcept)){
						targetLevel = ontoTree.getCurrentLevel() + 1;
					}
					ontoTree.getNextParent();
				}
			}
			return ontoTree;

		}catch(NullPointerException ne){
			try{
				System.out.println("&&&&&&&&&&&" + Item1);
				Item1 = Item1.substring(Item1.indexOf(" ") + 1);
				System.out.println("&&&&&&&&&&&" + Item1);
				return ontoSearch(Item1, Item2, category);
			}catch(Exception ne2){
				throw new OntologyException("parameters do not exist in the ontology " + Item1 + " " + Item2);
			}
		}
	}

	//from OntologyAccess.java
	//for first searchPL call
	private boolean searchPL(OntologyTree ontoTree, Concept concept1, Concept concept2, String category) throws OntologyException {

		SQLiteDatabase database = this.getWritableDatabase();
		Vector<Concept> children = new Vector<Concept>();
		Vector<String> relations = new Vector<String>();
		Vector<String> categories = new Vector<String>();
		boolean ret = false;
		
    	String selectQuery = "SELECT * FROM tableOntology WHERE OntologyID1 = '" + concept1.getOntologyID() + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);

		if(cursor.moveToFirst()){
			do{
				cursor.getString(0);
				relations.add(cursor.getString(1));
				children.add(new Concept(cursor.getString(2)));
				categories.add(cursor.getString(3));
				//Log.d("Concept", concept1.getOntologyID());
			}while(cursor.moveToNext());
		}

    	//update concept to have an element object
    	for(int i = 0; i < relations.size(); i++){
    		Concept concept = children.get(i);
    		concept = instantiateConcept(getElementID(concept.getOntologyID()));
    		if(category.equalsIgnoreCase(categories.get(i))){		//will insert only in the tree if it is of the same category
    			if(concept.getOntologyID().equalsIgnoreCase(concept2.getOntologyID())){
    				ret = true;
    			}
        		ontoTree.insertChild(concept, relations.get(i));
    		}
    	}
	    return ret;
	}

	//from OntologyAccess.java
	//	for subsequent searchPL call
	private boolean searchPL(OntologyTree ontoTree, Concept concept1, Concept concept2) throws OntologyException{

		SQLiteDatabase database = this.getWritableDatabase();
		Vector<Concept> children = new Vector<Concept>();
		Vector<String> relations = new Vector<String>();
		boolean ret = false;
		
    	String selectQuery = "SELECT * FROM tableOntology WHERE OntologyID1 = '" + concept1.getOntologyID() + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);

		if(cursor.moveToFirst()){
			do{
				cursor.getString(0);
				relations.add(cursor.getString(1));
				children.add(new Concept(cursor.getString(2)));
				cursor.getString(3);
				//Log.d("Concept", concept1.getOntologyID());
			}while(cursor.moveToNext());
		}

    	//update concept to have an elementID
    	for(int i = 0; i < relations.size(); i++){
    		Concept concept = children.get(i);
    		concept = instantiateConcept(getElementID(concept.getOntologyID()));
    		if(concept.getOntologyID().equalsIgnoreCase(concept2.getOntologyID())){
				ret = true;
			}
    		ontoTree.insertChild(concept, relations.get(i));
    	}
	    return ret;
	}

	//from OntologyAccess.java
	private void trimUnnecessary(Vector<Ontology> path){

		System.out.println("\ntrimmed nodes");

		for(int i=0; i<path.size(); i++){
			if(path.get(i).getSemanticRelation().equalsIgnoreCase("isA") ||
					path.get(i).getSemanticRelation().equalsIgnoreCase("conceptuallyRelatedTo")){	//relationships that must be taken for granted
				System.out.println(path.get(i).getElement1().getConcept() + " : " +
						path.get(i).getSemanticRelation() + " : " + path.get(i).getElement2().getConcept());
				path.remove(i);
				i--;
			}
		}
	}

	//from OntologyAccess.java
	private String getElementID(String id) throws OntologyException{

		SQLiteDatabase database = this.getWritableDatabase();
		String temp = null;
		String selectQuery = "SELECT ElementID FROM tableConcepts WHERE _id = '" + id + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);

		if(cursor.moveToFirst()){
			do{
				temp = cursor.getString(0);
				//Log.d("Concepts", concept.getOntologyID());
			}while(cursor.moveToNext());						
		}
    	return temp;
	}
	
	
	/**
	 * Gets all the registered users of the system.
	 *
	 * @return a vector containing the strings of the user names
	 * @throws PBException thrown when an error was encountered in the data retrieval
	 */
	//from PictureEditorDatabase
	public Vector<String> getUsers() 
    {
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<String> userNames = new Vector<String>();
		
		String selectQuery = "SELECT _id FROM tableUserInformation";
		Cursor cursor = database.rawQuery(selectQuery, null);

		if(cursor.moveToFirst()){
			do{
				userNames.add(cursor.getString(0));
				//Log.d("UserInformation", cursor.getString(0)); 
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();
        return userNames;
    }
	
	public Vector<Integer> getAges() 
    {
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<Integer> userAges = new Vector<Integer>();
		
		String selectQuery = "SELECT Age FROM tableUserInformation";
		Cursor cursor = database.rawQuery(selectQuery, null);

		if(cursor.moveToFirst()){
			do{
				userAges.add(cursor.getInt(0));
				//Log.d("UserInformation", cursor.getString(0)); 
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();
        return userAges;
    }
	
	public Vector<Integer> getGrades() 
    {
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<Integer> userGrades = new Vector<Integer>();
		
		String selectQuery = "SELECT Grade FROM tableUserInformation";
		Cursor cursor = database.rawQuery(selectQuery, null);

		if(cursor.moveToFirst()){
			do{
				userGrades.add(cursor.getInt(0));
				//Log.d("UserInformation", cursor.getString(0)); 
			}while(cursor.moveToNext());			
		}
		
		cursor.close();
		database.close();
        return userGrades;
    }

//added for Picture Editor
	public Background getBackground(String backgroundWord) {
		SQLiteDatabase database = this.getWritableDatabase();
		Background bg = new Background();
		
		String selectQuery = "SELECT * FROM tableBackgrounds WHERE BackgroundWord = ?" ;
	    String selectionArgs [] = new String [] { backgroundWord};
		Cursor cursor = database.rawQuery(selectQuery, selectionArgs);
		
		if(cursor.moveToFirst()){
			do{
				bg.setBackgroundID(cursor.getString(0));
				bg.setString(cursor.getString(1));
				bg.setTheme(cursor.getString(2));
				bg.setBackgroundWord(cursor.getString(3));
				bg.setObjectsIncluded(cursor.getString(4));
				bg.setCharactersIncluded(cursor.getString(5));
				bg.setRequiredRole(cursor.getString(6));
				bg.setImageFilePath(cursor.getString(7));
				Log.d("Backgrounds", bg.getBackgroundWord());
			}while(cursor.moveToNext());			
		}
		cursor.close();
		database.close();		
		return bg;
	}

	public IGCharacter getCharacter(String name) {
		SQLiteDatabase database = this.getWritableDatabase();
		IGCharacter character = new IGCharacter();
		
		String selectQuery = "SELECT * FROM tableIGCharacters WHERE Name = ?" ;
	    String selectionArgs [] = new String [] { name};
		Cursor cursor = database.rawQuery(selectQuery, selectionArgs);
		
		if(cursor.moveToFirst()){
			do{
				character.setID(cursor.getString(0));
				character.setString(cursor.getString(1));
				character.setGender(cursor.getString(2));
				character.setType(cursor.getString(3));
				character.setRole(cursor.getString(4));
				character.setMotherCharacterID(cursor.getString(5));
				character.setFatherCharacterID(cursor.getString(6));
				character.setImageFilePath(cursor.getString(7));
				Log.d("IGCharacters", character.getString());
			}while(cursor.moveToNext());			
		}
		cursor.close();
		database.close();		
		return character;
	}
	
	public IGObject getObject(String objectWord) {
		SQLiteDatabase database = this.getWritableDatabase();
		IGObject igo = new IGObject();
		
		String selectQuery = "SELECT * FROM tableIGObjects WHERE ObjectWord = ?" ;
	    String selectionArgs [] = new String [] {objectWord};
		Cursor cursor = database.rawQuery(selectQuery, selectionArgs);
		
		if(cursor.moveToFirst()){
			do{
				igo.setID(cursor.getString(0));
				igo.setString(cursor.getString(1));
				igo.setImageFilePath(cursor.getString(2));
				igo.setObjectWord(cursor.getString(3));
				Log.d("IGObjects", igo.getObjectWord());
			}while(cursor.moveToNext());			
		}
		cursor.close();
		database.close();		
		return igo;
	}
	
	public void addSavedSticker(SavedSticker ss)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("StoryID", ss.getStoryID());
		cv.put("Type", ss.getType());
		cv.put("Name", ss.getName());
		cv.put("x", ss.getX());
		cv.put("y", ss.getY());
		
		database.insert(TableCreator.TABLE_SAVED_STICKERS, null, cv);
	}
	
	public Vector<SavedSticker> getSavedStickers(int StoryID) {
		SQLiteDatabase database = this.getWritableDatabase();
		Vector<SavedSticker> vectorSS = new Vector<SavedSticker>();
		SavedSticker ss;
		
		String selectQuery = "SELECT * FROM " + TableCreator.TABLE_SAVED_STICKERS + " WHERE StoryID = " + StoryID ;
		Cursor cursor = database.rawQuery(selectQuery, null);

		Log.d("StickersSQL", "StickersSQL " + selectQuery);
		if(cursor.moveToFirst()){
			do{
				ss = new SavedSticker();
				ss.setStoryID(cursor.getInt(0));
				ss.setType(cursor.getString(1));
				ss.setName(cursor.getString(2));
				ss.setX(cursor.getFloat(3));
				ss.setY(cursor.getFloat(4));
				Log.d("SavedStickers", "SavedStickers " + ss.getStoryID() + " " + ss.getType() + " " + ss.getName() + " " + ss.getX() + " " + ss.getY());
				vectorSS.add(ss);
			}while(cursor.moveToNext());			
		}
		cursor.close();
		database.close();		
		return vectorSS;
	}
	

	public void deleteExistingStickers(int StoryID) //same as saveFile
	{
		SQLiteDatabase database = this.getWritableDatabase();
		String deleteQuery =
				"DELETE from " + TableCreator.TABLE_SAVED_STICKERS +
				" WHERE StoryID = '" + StoryID + "'";
		Log.d("StickersSQL", "StickersSQL " + deleteQuery);
		database.execSQL(deleteQuery);
		database.close();		
		
	}
}

/* NOT YET CONVERTED FROM PICTUREEDITORDATABASE.JAVA:
	public Vector<Character> getExtras(String bgId) throws PBException
	public int getAge(String name) throws PBException
*/