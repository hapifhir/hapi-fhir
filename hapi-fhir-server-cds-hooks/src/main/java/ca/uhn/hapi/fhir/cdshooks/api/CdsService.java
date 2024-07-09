/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.hapi.fhir.cdshooks.api;

import ca.uhn.hapi.fhir.cdshooks.api.json.CdsHooksExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as a CDS Hooks service.  A method annotated with `@CdsService(value="example-service")` is accessed
 * at a path like `<a href="https://example.com/cds-services/example-service">Example Service</a>`
 *
 * @see <a href="https://cds-hooks.hl7.org/ballots/2020Sep/">Version 1.1 of the CDS Hooks Specification</a>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CdsService {
	/**
	 * The {id} portion of the URL to this service which is available at
	 * {baseUrl}/cds-services/{id}
	 */
	String value();

	/**
	 * The hook this service should be invoked on
	 */
	String hook();

	/**
	 * The human-friendly name of this service.
	 */
	String title() default "";

	/**
	 * The description of this service.
	 */
	String description();

	/**
	 * An object containing key/value pairs of FHIR queries that this service is requesting that the CDS Client prefetch
	 * and provide on each service call. The key is a string that describes the type of data being requested and the value
	 * is a string representing the FHIR query.
	 */
	CdsServicePrefetch[] prefetch();

	/**
	 * @return true if Smile CDR CDS Hooks can populate missing prefetch elements using the Fhir Server specified in the
	 * request before calling the method.  If false, then it is expected that the service will use the FHIR Server details and retrieve missing
	 * prefetch elements itself.
	 */
	boolean allowAutoFhirClientPrefetch() default false;

	/**
	 * An arbitrary string which will be used to store stringify JSON
	 */
	String extension() default "";

	Class<? extends CdsHooksExtension> extensionClass() default CdsHooksExtension.class;
}
