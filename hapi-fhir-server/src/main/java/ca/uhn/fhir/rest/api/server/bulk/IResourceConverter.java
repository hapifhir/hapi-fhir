/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.api.server.bulk;

import jakarta.annotation.Nonnull;

public interface IResourceConverter {
	/**
	 * Consumes the list of resources (already expanded; never a null or empty list; only one resource type)
	 * and the BulkExportJobParameters (defining things like export format, as well
	 * as search parameters).
	 * @param theResources - expanded resources to convert to writeable data.
	 *                      Resources are for reading and implementers shouldn't mutate it.
	 *                      The list will never be null or empty.
	 *                      All resources in the provided list will be of the same resource type
	 *                     (ie, never mixed types).
	 * @param theJobParameters - the bulk export parameters
	 * @return - the ConvertedFiles
	 */
	@Nonnull
	ConvertedFiles consume(
			@Nonnull BulkExportResourceList theResources, @Nonnull BulkExportJobParameters theJobParameters);
}
