package ca.uhn.fhir.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * RESTful method annotation used for a method which provides
 * the FHIR "conformance" method.
 * 
 * @see See the <a href="http://hl7.org/implement/standards/fhir/http.html#conformance">FHIR HTTP Conformance</a> definition
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.METHOD)
public @interface Metadata {
	// nothing for now
}
