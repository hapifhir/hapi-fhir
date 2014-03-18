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
 * parameter should be of type Collection, List, or Set.
 * </p> 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.PARAMETER})
public @interface Include {

}
