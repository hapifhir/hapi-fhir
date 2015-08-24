package ca.uhn.fhir.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * On a method which returns resource(s), a parameter of type
 * <code>Set&lt;String&gt;</code> with this annotation will be passed the
 * contents of the <code>_elements</code> parameter 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Elements {
	// just a marker
}
