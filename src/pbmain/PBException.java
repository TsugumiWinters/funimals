
package pbmain;

/**
 * This class extends the {@link java.lang.Exception}
 * abstract class for PBException
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */

public class PBException extends Exception{

	/**
	 * PBException constructor that calls the extended class Exception
	 *
	 */
	public PBException() {
		super();
	}

	/**
	 * PBException constructor that calls the extended class Exception
	 * with the specified error message and cause
	 *
	 * @param message error that occurred
	 * @param cause a Throwable cause
	 */
	public PBException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * PBException constructor that calls the extended class Exception with the specified cause
	 *
	 * @param cause a Throwable cause
	 */
	public PBException(Throwable cause) {
		super(cause);
	}

	/**
	 *
	 * @param msg error that occurred
	 */
	public PBException(String msg){
		super(msg);
	}
}
