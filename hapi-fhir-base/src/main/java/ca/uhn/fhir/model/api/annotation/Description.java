package ca.uhn.fhir.model.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.FIELD, ElementType.TYPE})
public @interface Description {

	/**
	 * Optional short name for this child 
	 */
	String shortDefinition() default "";
	
	/**
	 * Optional formal definition for this child
	 */
	String formalDefinition() default "";
	
}
