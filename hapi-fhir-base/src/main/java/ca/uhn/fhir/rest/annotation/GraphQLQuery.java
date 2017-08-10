package ca.uhn.fhir.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should be placed on the parameter of a
 * {@link GraphQL @GraphQL} annotated method. The given
 * parameter will be populated with the specific graphQL
 * query being requested.
 *
 * <p>
 *    This parameter should be of type {@link String}
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface GraphQLQuery {
	// ignore
}
