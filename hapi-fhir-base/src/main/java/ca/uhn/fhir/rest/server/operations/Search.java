package ca.uhn.fhir.rest.server.operations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ca.uhn.fhir.model.api.IResource;

@Retention(RetentionPolicy.RUNTIME)

public @interface Search {

	/**
	 * Returns the resource type that is returned by the method annotated
	 * with this annotation
	 */
	Class<? extends IResource> value();
	
}