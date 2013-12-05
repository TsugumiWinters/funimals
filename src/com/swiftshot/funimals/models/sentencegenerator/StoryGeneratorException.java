package com.swiftshot.funimals.models.sentencegenerator;

/**
 * This class extends the {@link java.lang.Exception}
 * abstract class for StoryGeneratorException
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
public class StoryGeneratorException extends Exception {


	/**
	 * StoryGeneratorException constructor calls the extended class Exception
	 *
	 */
	public StoryGeneratorException() {
		super();
	}

	/**
	 * StoryGeneratorException constructor calls the extended class Exception
	 * with the specified error message and cause
	 *
	 * @param message error that occurred
	 * @param cause a Throwable cause
	 */
	public StoryGeneratorException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * StoryGeneratorException constructor calls the extended class Exception with the specified error message
	 *
	 * @param message error that occurred
	 */
	public StoryGeneratorException(String message) {
		super(message);
	}

	/**
	 * StoryGeneratorException constructor calls the extended class Exception with the specified cause
	 *
	 * @param cause a Throwable cause
	 */
	public StoryGeneratorException(Throwable cause) {
		super(cause);
	}

}
