/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.rest.api.server.SystemRequestDetails;

/**
 * This interface is passed into individual job steps and provides utility methods helpful for step execution
 *
 * @since 8.8.0
 */
public interface IJobStepExecutionServices {

	/**
	 * Instantiate a new {@link SystemRequestDetails} object that can be passed into other HAPI FHIR API methods
	 *
	 * @since 8.8.0
	 */
	SystemRequestDetails newRequestDetails(IJobInstance theJobInstance);
}
