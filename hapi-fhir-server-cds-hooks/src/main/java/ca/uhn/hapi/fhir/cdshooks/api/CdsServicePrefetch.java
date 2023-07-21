/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
