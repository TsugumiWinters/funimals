package com.swiftshot.funimals.models.pbmain.util;

import java.util.StringTokenizer;
import java.util.Vector;

import android.content.Context;

import com.swiftshot.funimals.models.ApplicationContextProvider;
import com.swiftshot.funimals.models.database.DatabaseHelper;
import com.swiftshot.funimals.models.database.entities.Word;
import com.swiftshot.funimals.models.pbmain.PBException;
import com.swiftshot.funimals.models.sentencegenerator.component.DBLexicalObject;
import com.swiftshot.funimals.models.sentencegenerator.component.Phrase;


/**
 * This class parses keywords used by the system.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
public class Parser {


	/**
	 * Converts the given string of wordIDs into either a Phrase object or a Word object
	 *
	 * @param wordID string of wordID or wordIDs
	 * @param age age of the user used to identify which word in the lexicon should be used
	 *
	 * @return DBLexicalObject correspondng Phrase or Word object of the wordIDs
	 *
	 * @throws ParserException raised if there's an error occured while parsing the data
	 */
	public static DBLexicalObject parseWords(String wordID, int age) throws ParserException{

		Context ourContext = ApplicationContextProvider.getContext();

		Phrase words = new Phrase();

		StringTokenizer st = new StringTokenizer(wordID, " ");
		try{
			while(st.hasMoreTokens()){
				words.add(new DatabaseHelper(ourContext).instantiateWord(st.nextToken(), age));
			}
		}catch(PBException e){
			throw new ParserException(e);
		}

		if(words.size() == 1)
			return (Word)words.get(0);
		else return (Phrase)words;
	}

//	public static String getID(DBObject obj){
//		return obj.getID();
//	}
//
//	public static String getString(DBObject obj){
//		return obj.getString();
//	}
//
//	public static String getID(Phrase words){
//		String ret = "";
//		for(int i=0; i < words.size(); i++)
//			ret += words.get(i).getID() + " ";
//		return ret;
//	}
//
//	public static String getString(Phrase words){
//		String ret = "";
//		for(int i=0; i < words.size(); i++)
//			ret += words.get(i).getString() + " ";
//		return ret;
//	}

	/**
	 *  Tokenize the input string using the delimiter specified
	 *
	 *  @param arg String to be parsed
	 *  @param delimiter delimeter used to parse the string
	 *
	 *  @return Vector of parsed string
	 */
	public static Vector<String> parseString(String arg, String delimiter){

		StringTokenizer st = new StringTokenizer(arg, delimiter);
		Vector<String> retString = new Vector<String>();

		while(st.hasMoreTokens()){
			retString.add(st.nextToken());
		}

		return retString;
	}

	/**
	 * Converts the given vector of strings into a single string
	 *
	 * @param arg Vector of strings to be converted
	 *
	 * @return string created by concatenating the strings in the vector
	 */
	public static String convertToString(Vector<String> arg){

		String retString = "";

		for(int i=0; i<arg.size(); i++)
			if(i < arg.size() - 1)
				retString += arg.get(i) + " ";
			else retString += arg.get(i);

		return retString;
	}
}
