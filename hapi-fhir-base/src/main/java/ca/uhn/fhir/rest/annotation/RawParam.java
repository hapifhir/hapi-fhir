package ca.uhn.fhir.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * On a {@link Search} method, a parameter marked with this annotation 
 * will receive search parameters not captured by other parameters. 
 * <p>
 * Parameters with this annotation must be of type
 * {@code Map<String, List<String>>}
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.PARAMETER)
public @interface RawParam {
	// nothing
}
