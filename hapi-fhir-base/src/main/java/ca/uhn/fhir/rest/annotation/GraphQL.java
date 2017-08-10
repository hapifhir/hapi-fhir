package ca.uhn.fhir.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A method annotated with this annotation will be treated as a GraphQL implementation
 * method
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value= ElementType.METHOD)
public @interface GraphQL {
}
