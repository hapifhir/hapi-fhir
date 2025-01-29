package ca.uhn.fhir.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import com.google.common.collect.Multimap;

/**
 * This annotation may be placed on a operation provider method parameter or embedded parameter class to indicate that
 * it should be populated with a {@link Multimap} containing all headers names and values in the request.
 */
@Target(value = {ElementType.PARAMETER})
public @interface Headers {
	String value();
}
