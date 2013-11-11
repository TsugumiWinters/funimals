package storyplanner.title;

/**
 * This class extends the {@link java.lang.Exception} abstract class for TitleMakerException
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */

public class TitleMakerException extends Exception {

	/**
	 * TitleMakerException constructor calls the extended class {@link java.lang.Exception}
	 *
	 */
	public TitleMakerException() {
		super();
	}

	/**
	 * TitleMakerException constructor calls the extended class Exception with the specified error message and cause
	 * @param message error that occurred
	 * @param cause a Throwable cause
	 */
	public TitleMakerException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * TitleMakerException constructor calls the extended class Exception with the specified error message
	 * @param message error that occurred
	 */
	public TitleMakerException(String message) {
		super(message);
	}

	/**
	 * TitleMakerException constructor calls the extended class Exception with the specified cause
	 * @param cause a Throwable cause
	 */
	public TitleMakerException(Throwable cause) {
		super(cause);
	}
}
