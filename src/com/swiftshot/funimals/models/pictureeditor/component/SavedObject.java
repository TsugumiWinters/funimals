package com.swiftshot.funimals.models.pictureeditor.component;

import java.io.Serializable;

/**
 * This class implements {@link Serializable} in order to save sticker information into a file.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
public class SavedObject implements Serializable{
	private String objectID;
	private int xCoordinate;
	private int yCoordinate;

	/**
	 * SavedObject constructor that calls its super class.
	 */
	public SavedObject() {
		super();
	}

	/**
	 * Getter method for the id of the sticker to be saved.
	 * @return the id of the sticker
	 */
	public String getObjectID() {
		return objectID;
	}

	/**
	 * Setter method for the id of the sticker to be saved.
	 * @param objectID the id of the sticker
	 */
	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}

	/**
	 * Getter method for the x coordinate of the sticker in the picture to be saved.
	 * @return the x coordinate of the sticker
	 */
	public int getXCoordinate() {
		return xCoordinate;
	}

	/**
	 * Setter method for the x coordinate of the sticker in the picture to be saved.
	 * @param coordinate the x coordinate of the sticker
	 */
	public void setXCoordinate(int coordinate) {
		xCoordinate = coordinate;
	}

	/**
	 * Getter method for the y coordinate of the sticker in the picture to be saved.
	 * @return the y coordinate of the sticker
	 */
	public int getYCoordinate() {
		return yCoordinate;
	}

	/**
	 * Setter method for the y coordinate of the sticker in the picture to be saved.
	 * @param coordinate the y coordinate of the sticker
	 */
	public void setYCoordinate(int coordinate) {
		yCoordinate = coordinate;
	}

}
