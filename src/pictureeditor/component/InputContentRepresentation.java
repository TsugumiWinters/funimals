package pictureeditor.component;

import java.util.Vector;

import database_entities.Background;
import database_entities.IGCharacter;
import database_entities.IGObject;

/**
 * The abstract representation of the picture's contents. This class would serve as input to the Story Planner module.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
public class InputContentRepresentation{

	private Background bg = new Background();
	private Vector<IGCharacter> characters = new Vector<IGCharacter>();
	private Vector<IGObject> objects = new Vector<IGObject>();
	private String name;
	private int age;

	/**
	 * Blank InputContentRepresentation constructor
	 */
	public InputContentRepresentation() {

	}

	/**
	 * InputContentRepresentation constructor that initializes its attributes by copying from the
	 * ICR passed as parameter.
	 *
	 * @param copyICR
	 */
	public InputContentRepresentation(InputContentRepresentation copyICR){
		setBg(copyICR.getBackground());
		setCharacters(copyICR.getCharacters());
		setObjects(copyICR.getObjects());
		setName(copyICR.getName());
		setAge(copyICR.getAge());
	}

	/**
	 * Getter method for the age of the user who made the picture
	 * @return age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Setter method for the age of the user who made the picture
	 * @param age
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * Getter method for the background of the picture made by the user.
	 * @return {@link Background};
	 */
	public Background getBackground() {
		return bg;
	}

	/**
	 * Setter method for the background of the picture made by the user.
	 * @param bg
	 */
	public void setBg(Background bg) {
		this.bg = bg;
	}

	/**
	 * Getter method for the list of characters stickers placed by the user into the picture
	 * @return vector of {@link Character} objects
	 */
	public Vector<IGCharacter> getCharacters() {
		return characters;
	}

	/**
	 * Setter method for the list of characters stickers placed by the user into the picture
	 * @param charsInPic vector of {@link Character} objects
	 */
	public void setCharacters(Vector<IGCharacter> charsInPic) {
		this.characters = charsInPic;
	}

	/**
	 * Getter method for the name of the user who made the picture
	 * @return the name of the user
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for the name of the user who made the picture
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter method for the list of object stickers placed by the user into the picture
	 * @return vector of {@link PBObject} objects
	 */
	public Vector<IGObject> getObjects() {
		return objects;
	}

	/**
	 * Setter method for the list of object stickers placed by the user into the picture
	 * @param objectsInPic vector of {@link PBObject} objects
	 */
	public void setObjects(Vector<IGObject> objectsInPic) {
		this.objects = objectsInPic;
	}

}