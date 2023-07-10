package ca.uhn.hapi.fhir.cdshooks.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as a CDS Hooks feedback service.  A method annotated with `@CdsServiceFeedback(value="my-service")` is
 * accessed at a path like `https://example.com/cds-services/example-service/feedback`.
 *
 * @see <a href="https://cds-hooks.hl7.org/ballots/2020Sep/">Version 1.1 of the CDS Hooks Specification</a>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CdsServiceFeedback {
	/**
	 * The {id} portion of the URL to this service which is available at
	 * {baseUrl}/cds-services/{id}/feedback
	 */
	String value();
}
