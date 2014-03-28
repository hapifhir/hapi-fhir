package ca.uhn.fhir.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RESTful method annotation to be used for the FHIR
 * <a href="http://hl7.org/implement/standards/fhir/http.html#update">update</a> method.
 * 
 * <p>
 * Update is used to save an update to an existing resource (using its ID and optionally
 * a version ID). It also may allow a client to save a new resource using an ID of its choosing. 
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.METHOD)
public @interface Update {

}