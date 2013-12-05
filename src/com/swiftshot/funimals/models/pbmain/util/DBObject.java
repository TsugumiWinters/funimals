package com.swiftshot.funimals.models.pbmain.util;

/**
 * This is an interface that
 * implements the setter and getter of the id and string counterpart of database
 * objects.
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */

public interface DBObject {

	/**
	 * Sets the id of the database object
	 *
	 * @param dbObjectID The id of the database object
	 *
	 */
	public void setID(String dbObjectID);

	/**
	 * Gets the id of the database object
	 *
	 * @return the id of the database object
	 */
	public String getID();

	/**
	 * Sets the name of the database object
	 *
	 * @param dbObjectName The name of the database object
	 */
	public void setString(String dbObjectName);

	/**
	 * Gets the name of the database object
	 *
	 * @return the name of the database object
	 */
	public String getString();

}
