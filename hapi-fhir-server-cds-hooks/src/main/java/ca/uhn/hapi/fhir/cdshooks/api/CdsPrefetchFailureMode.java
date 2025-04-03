/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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

/**
 * Enum representing different modes of handling CDS Hooks prefetch failures.
 * This applies
 *  - if a failure occurs when auto-prefetching a resource, or
 *  - if a CDS Client sends an OperationOutcome as a prefetch resource. With version 2.0 of the CDS Hooks specification, CDS Clients are
 * allowed to send an OperationOutcome as a prefetch if they encounter a failure prefetching a resource.
 */
public enum CdsPrefetchFailureMode {
	/**
	 * The CDS Hooks request will fail.
	 */
	FAIL,
	/**
	 * The prefetch resource will be omitted from the CDS Request.
	 * i.e. {@link ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestJson} object will not contain the failed prefetch key.
	 */
	OMIT,
	/**
	 * The prefetch key will map to OperationOutcome resource. That is, if the CDS Client sends an OperationOutcome,that will be passed on
	 * as is. If the auto-prefetch fails, and then an OperationOutcome will be either extracted from the FHIR server's response, or created
	 * if one is not available in the response.
	 */
	OPERATION_OUTCOME
}
