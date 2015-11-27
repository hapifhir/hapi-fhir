package ca.uhn.fhir.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * INTERNAL API (do not use): REST method annotation for the method called when a client requests a page.
 * <p>
 * This annotation is currently intended as an internal part of HAPI's API. At some point we 
 * will hopefully provide a way to create alternate implementations of the GetPage mewthod. If
 * you would like to help out or have ideas, please get in touch!
 * </p>
 */
@Target(value= ElementType.METHOD)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface GetPage {
	// nothing
}
