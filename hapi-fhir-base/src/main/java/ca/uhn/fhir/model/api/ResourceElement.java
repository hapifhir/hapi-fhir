package ca.uhn.fhir.model.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.FIELD})
public @interface ResourceElement {

	/**
	 * Constant value to supply for {@link #order()} when the order is defined
	 * elsewhere
	 */
	int ORDER_UNKNOWN = -1;

	String name();
	
	int order();

	int min() default 0;

	int max() default 1;

	ResourceChoiceElement choice() default @ResourceChoiceElement();
	
}
