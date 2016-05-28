package ca.uhn.fhir.model.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation may be used on a resource type to specify an order for
 * the child names. This annotation is intended for situations where the 
 * class hierarchy makes it impossible to specify the order using only 
 * the {@link Child#order()} property
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.TYPE})
public @interface ChildOrder {
	String[] names();
}
