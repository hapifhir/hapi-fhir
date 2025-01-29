package ca.uhn.fhir.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation may be placed on a operation provider method parameter or embedded parameter class to indicate that
 * it should be populated with the value of an REST header, which this is a single or multiple values.
 */
@Target(value = {ElementType.PARAMETER})
public @interface Header {
	String value();
}
