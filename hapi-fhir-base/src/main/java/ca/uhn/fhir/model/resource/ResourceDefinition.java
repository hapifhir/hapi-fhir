package ca.uhn.fhir.model.resource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.TYPE})
public @interface ResourceDefinition {

	int ORDER_NOT_SPECIFIED = -1;

	String name();
	
	int identifierOrder() default ORDER_NOT_SPECIFIED;
	
}
