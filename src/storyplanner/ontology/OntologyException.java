/**
 *
 */
package storyplanner.ontology;

import storyplanner.StoryPlannerException;

/**
 * This class extends the {@link storyplanner.StoryPlannerException} abstract class for OntologyException
 *
 * @author Joan Tiffany Siy
 * @author Candice Jean Solis
 * @author Emerald Tabirao
 * @author Arvin Jasper Hong
 */

public class OntologyException extends StoryPlannerException{

	/**
	 * OntologyException constructor calls the extended class {@link storyplanner.StoryPlannerException}
	 *
	 */
	public OntologyException(){
		super("The category indicated does not yield a path or a concept");
	}

	/**
	 * OntologyException constructor calls the extended class {@link storyplanner.StoryPlannerException} with the specified error message
	 * @param msg
	 */
	public OntologyException(String msg){
		super(msg);
	}

	/**
	 * OntologyException constructor calls the extended class Exception with the specified error message and cause
	 * @param message error that occurred
	 * @param cause a Throwable cause
	 */
	public OntologyException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * OntologyException constructor calls the extended class {@link storyplanner.StoryPlannerException} with the specified cause
	 * @param cause a Throwable cause
	 */
	public OntologyException(Throwable cause){
		super(cause);
	}
}
