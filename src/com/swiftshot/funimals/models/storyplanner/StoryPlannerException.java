package com.swiftshot.funimals.models.storyplanner;

/**
 * This class extends the {@link java.lang.Exception} abstract class for StoryPlannerException
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */

public class StoryPlannerException extends Exception{

	/**
	 * StoryPlannerException constructor calls the extended class Exception
	 *
	 */
	public StoryPlannerException() {
		super();
	}

	/**
	 * StoryPlannerException constructor calls the extended class Exception with the specified error message and cause
	 * @param message error that occurred
	 * @param cause a Throwable cause
	 */
	public StoryPlannerException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * StoryPlannerException constructor calls the extended class Exception with the specified error message
	 * @param message error that occurred
	 */
	public StoryPlannerException(String message) {
		super(message);
	}

	/**
	 * StoryPlannerException constructor calls the extended class Exception with the specified cause
	 * @param cause a Throwable cause
	 */
	public StoryPlannerException(Throwable cause) {
		super(cause);
	}


}
