package ca.uhn.fhir.rest.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ca.uhn.fhir.model.api.IResource;

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

	/**
	 * The return type for this search method. This generally does not need
	 * to be populated for a server implementation, since servers will return
	 * only one resource per class, but generally does need to be populated
	 * for client implementations. 
	 */
	// NB: Read, Search (maybe others) share this annotation, so update the javadocs everywhere
	Class<? extends IResource> type() default IResource.class;
	

}