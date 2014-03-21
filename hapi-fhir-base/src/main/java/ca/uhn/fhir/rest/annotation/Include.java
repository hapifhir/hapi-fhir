package ca.uhn.fhir.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method parameter which is used to indicate a parameter that will
 * be populated with the "_include" values for a search param.  
 * <p>
 * Only one parameter may be annotated with this annotation, and that
 * parameter should be one of the following:
 * </p> 
 * <ul>
 * <li><code>Collection&lt;PathSpecification&gt;</code></li> 
 * <li><code>List&lt;PathSpecification&gt;</code></li> 
 * <li><code>Set&lt;PathSpecification&gt;</code></li> 
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.PARAMETER})
public @interface Include {

	/**
	 * Optional parameter, if provided the server will only allow the values
	 * within the given set. If an _include parameter is passed to the server
	 * which does not match any allowed values the server will return an error. 
	 */
	String[] allow() default {};
	
}
