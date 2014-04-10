package ca.uhn.fhir.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Parameter annotation for the _since parameter, which indicates to the 
 * server that only results dated since the given instant will be returned.
 * 
 * @see History
 */
@Target(value=ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Since {
	//nothing
}
