package ca.uhn.fhir.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ca.uhn.fhir.model.api.IResource;


/**
 * RESTful method annotation used for a method which provides
 * the FHIR "search" method.
 * 
 * @see See the <a href="http://hl7.org/implement/standards/fhir/http.html#search">FHIR Search</a> definition
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.METHOD)
public @interface Search {
	
	/**
	 * If specified, this the name for the Named Query
	 * 
	 * <p>
	 * See the FHIR specification section on 
	 * 	<a href="http://www.hl7.org/implement/standards/fhir/search.html#advanced">named queries</a>
	 * </p>
	 */
	String queryName() default "";
	
	/**
	 * The return type for this search method. This generally does not need
	 * to be populated for a server implementation, since servers will return
	 * only one resource per class, but generally does need to be populated
	 * for client implementations. 
	 */
	// NB: Read, Search (maybe others) share this annotation, so update the javadocs everywhere
	Class<? extends IResource> type() default IResource.class;
	
}