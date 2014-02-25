package ca.uhn.fhir.model.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation for a primitive setter method that can be used to
 * indicate a "simple setter" method on a resource or composite type  
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.CONSTRUCTOR})
public @interface SimpleSetter {
	// nothing for now
	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(value= {ElementType.PARAMETER})
	public @interface Parameter {
		String name();
	}	
	
}
