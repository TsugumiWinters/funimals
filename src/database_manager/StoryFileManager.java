package database_manager;

import android.database.sqlite.SQLiteDatabase;

public class StoryFileManager {
	
	private SQLiteDatabase database, writableDatabase;

	public SQLiteDatabase getWritableDatabase() {
		return writableDatabase;
	}

	public void setWritableDatabase(SQLiteDatabase writableDatabase) {
		this.writableDatabase = writableDatabase;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}
	
	
}
