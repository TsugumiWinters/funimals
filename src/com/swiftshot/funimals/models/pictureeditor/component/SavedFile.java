package com.swiftshot.funimals.models.pictureeditor.component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

/**
 * This class implements {@link Serializable} in order to save a picture and a story
 * for the user's personal library
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
public class SavedFile implements Serializable {

	private static final long serialVersionUID = 1L;
	private String title = "";
	private Vector<String> story = new Vector<String>();
	private String backgroundID = "";
	private Vector<SavedObject> characters = new Vector<SavedObject>();
	private Vector<SavedObject> objects = new Vector<SavedObject>();

	private final String EXT = ".pbstory"; //change to the suggested file extension

	/**
	 * SavedFile blank constructor
	 */
	public SavedFile() {

	}

	/**
	 * Getter method for the story
	 * @return the vector of strings that make up the story
	 */
	public Vector<String> getStory() {
		return story;
	}

	/**
	 * Setter method for the story
	 * @param story the vector of strings that make up the story
	 */
	public void setStory(Vector<String> story) {
		this.story = story;
	}

	/**
	 * Getter method for the story title
	 * @return the title of the story
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter method for the story title
	 * @param title the title of the story
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter method for the id of the background of the picture to be saved.
	 * @return the id of the background
	 */
	public String getBackgroundID() {
		return backgroundID;
	}

	/**
	 * Setter method for the id of the background of the picture to be saved.
	 * @param background the id of the background
	 */
	public void setBackgroundID(String background) {
		this.backgroundID = background;
	}

	/**
	 * Getter method for the list of characters stickers placed in the picture to be saved.
	 * @return the vector of Character objects converted into {@link SavedObject}
	 */
	public Vector<SavedObject> getCharacters() {
		return characters;
	}

	/**
	 * Setter method for the list of characters stickers placed in the picture to be saved.
	 * @param characters the vector of Character {@link SavedObject}
	 */
	public void setCharacters(Vector<SavedObject> characters) {
		this.characters = characters;
	}

	/**
	 * Getter method for the list of object stickers placed in the picture to be saved.
	 * @return the vector of PBObject objects converted into {@link SavedObject}
	 */
	public Vector<SavedObject> getObjects() {
		return objects;
	}

	/**
	 * Setter method for the list of object stickers placed in the picture to be saved.
	 * @param objects the vector of PBObject objects converted into {@link SavedObject}
	 */
	public void setObjects(Vector<SavedObject> objects) {
		this.objects = objects;
	}

	/**
	 * Saves the contents of a SavedFile object into a file.
	 * @param saveFile contains the story and picture to be saved
	 * @param name the name of the user
	 * @param fileName the name of the file to be saved
	 * @return the file path of the saved file
	 */
	public String serializeFile(SavedFile saveFile, String name, String fileName)	{
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		String address = "";

		try {
			fos = new FileOutputStream("Stories/" + name + "/" + fileName + EXT);
			address = "/Stories/" + name + "/" + fileName + EXT;
			out = new ObjectOutputStream(fos);
			out.writeObject(saveFile);
			out.close();
		}
		catch(Exception ex)
			{
			 ex.printStackTrace();
			}
		return address;
	}

	/**
	 * Opens a file  and deserializes it into a SavedFile
	 * @param fileName the name of the file to be opened
	 * @return the corresponding SavedFile to the file opened.
	 */
	public SavedFile deserializeFile(String fileName) {
		SavedFile file = new SavedFile();
		FileInputStream fis = null;
		ObjectInputStream in = null;

		try	{
		fis = new FileInputStream(fileName);
		in = new ObjectInputStream(fis);
		file = (SavedFile)in.readObject();
		in.close();
		}
		catch(Exception ex)
		{
		 ex.printStackTrace();
		}
		System.out.println(file.getTitle());
		return file;
	}

}
