/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.api.server.storage;

import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.util.List;

// Tag interface for Spring auto-wiring
public interface IDeleteExpungeJobSubmitter {
	/**
	 * @param theBatchSize     For each pass, when synchronously searching for resources, limit the number of matching resources to this number
	 * @param theUrlsToProcess A list of strings of the form "/Patient?active=true"
	 * @return The Batch2 JobId that was started to run this batch job
	 */
	String submitJob(
			Integer theBatchSize,
			List<String> theUrlsToProcess,
			boolean theCascade,
			Integer theCascadeMaxRounds,
			RequestDetails theRequest);
}
