package ca.uhn.fhir.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ca.uhn.fhir.rest.api.SortSpec;

/**
 * For searches, a parameter may be annotated with the {@link Sort} annotation. The 
 * parameter should be of type {@link SortSpec}. 
 * 
 * <p>
 * Note that if you wish to chain
 * multiple sort parameters (i.e. a sub sort), you should use the {@link SortSpec#setChain(SortSpec)}
 * method. Multiple parameters should not be annotated with the Sort annotation.
 * </p>
 *   
 * @see Search
 */
@Target(value=ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sort {
	// nothing
}
