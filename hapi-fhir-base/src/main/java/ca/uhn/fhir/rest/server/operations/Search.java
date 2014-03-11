package ca.uhn.fhir.rest.server.operations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ca.uhn.fhir.model.api.IResource;

@Retention(RetentionPolicy.RUNTIME)

public @interface Search {
	
	/**
	 * The return type for this search method. This generally does not need
	 * to be populated for a server implementation, since servers will return
	 * only one resource per class, but generally does need to be populated
	 * for client implementations. 
	 */
	// NB: Read, Search (maybe others) share this annotation, so update the javadocs everywhere
	Class<? extends IResource> type() default IResource.class;
	
}