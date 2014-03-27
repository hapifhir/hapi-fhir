package ca.uhn.fhir.rest.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

/**
 * RESTful method annotation to be used for the FHIR
 * <a href="http://hl7.org/implement/standards/fhir/http.html#create">create</a> method.
 * 
 * <p>
 * Create is used to save a new resource, allowing the server to assign a new ID and version ID.
 * </p>
 */
public @interface Create {
	// nothing
}