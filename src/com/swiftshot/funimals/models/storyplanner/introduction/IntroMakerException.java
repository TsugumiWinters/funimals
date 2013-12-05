package com.swiftshot.funimals.models.storyplanner.introduction;

/**
 * This class extends the {@link java.lang.Exception}  abstract class for IntroMakerException
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */

public class IntroMakerException extends Exception {

	/**
	 * IntroMakerException constructor calls the extended class Exception
	 *
	 */
	public IntroMakerException() {
		super();
	}

	/**
	 * IntroMakerException constructor calls the extended class Exception with the specified error message and cause
	 * @param message error that occurred
	 * @param cause a Throwable cause
	 */
	public IntroMakerException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * IntroMakerException constructor calls the extended class Exception with the specified error message
	 * @param message error that occurred
	 */
	public IntroMakerException(String message) {
		super(message);
	}

	/**
	 * IntroMakerException constructor calls the extended class Exception with the specified cause
	 * @param cause a Throwable cause
	 */
	public IntroMakerException(Throwable cause) {
		super(cause);
	}

}
