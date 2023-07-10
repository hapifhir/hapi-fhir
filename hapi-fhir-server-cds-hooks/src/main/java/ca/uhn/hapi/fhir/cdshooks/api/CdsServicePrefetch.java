package ca.uhn.hapi.fhir.cdshooks.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An object containing a key/value pair of FHIR queries that a Cds Service is requesting that the CDS Client prefetch
 * and provide on each service call.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CdsServicePrefetch {
	/**
	 * The type of data being requested
	 */
	String value();

	/**
	 * The FHIR Query that can be used to request the required data
	 */
	String query();

	/**
	 * The strategy used for this query, defaults to the service-wide strategy
	 */
	CdsResolutionStrategyEnum source() default CdsResolutionStrategyEnum.NONE;

}
