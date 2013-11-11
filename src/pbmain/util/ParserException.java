/**
 *
 */
package pbmain.util;

/**
 * This class extends the {@link java.lang.Exception}
 * abstract class for ParserException
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */
public class ParserException extends Exception{

	/**
	 * ParserException constructor that calls the extended class Exception
	 *
	 */
	public ParserException(){
		super("The keywords cannot be parsed.");
	}

	/**
	 * ParserException constructor calls the extended class Exception with the specified error message
	 *
	 * @param msg error that occurred
	 */
	public ParserException(String msg){
		super(msg);
	}

	/**
	 * ParserException constructor calls the extended class Exception
	 * with the specified error message and cause
	 *
	 * @param message error that occurred
	 * @param cause a Throwable cause
	 */
	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * ParserException constructor calls the extended class Exception with the specified cause
	 *
	 * @param cause a Throwable cause
	 */
	public ParserException(Throwable cause){
		super(cause);
	}
}
