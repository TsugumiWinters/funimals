package com.swiftshot.funimals.models.pbmain.util;

/**
 * This class handles the connection of the system to the Lexicon Database.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
import com.swiftshot.funimals.models.pbmain.PBException;
import com.swiftshot.funimals.models.sentencegenerator.component.db.Word;



public class PBLexicon {


	/**
	 * Instantiates Word object given the conceptID and the user's age
	 *
	 * @param conceptID id of the word
	 * @param age age of the user used to identify which word in the lexicon should be used
	 *
	 * @return instantiated Word object {@link com.swiftshot.funimals.models.sentencegenerator.component.db.Word}
	 *
	 * @throws PBException raised if there's no word found in the lexicon that corresponds to the conceptID
	 */
    public Word instantiateWord(String conceptID, int age) throws PBException{

    	Word word = new Word(conceptID);

   
    	return word;
    }

}

