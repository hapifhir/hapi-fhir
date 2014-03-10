package ca.uhn.fhir.model.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.TYPE})
public @interface ResourceDef {

	int ORDER_NOT_SPECIFIED = -1;

	String name();
	
	String profile() default "";
	
	int identifierOrder() default ORDER_NOT_SPECIFIED;
	
}
