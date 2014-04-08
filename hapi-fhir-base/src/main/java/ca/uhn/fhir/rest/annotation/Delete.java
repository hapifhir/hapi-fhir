package ca.uhn.fhir.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ca.uhn.fhir.model.api.IResource;

/**
 * RESTful method annotation to be used for the FHIR
 * <a href="http://hl7.org/implement/standards/fhir/http.html#delete">delete</a> method.
 * 
 * <p>
 * Delete is used to remove an existing resource, meaning that any attempts to
 * do a non-version-specific read of that resource will fail.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.METHOD)
public @interface Delete {
	
	Class<? extends IResource> resourceType() default NotSpecified.class;
	
	
	interface NotSpecified extends IResource{
		// nothing
	}
	
}