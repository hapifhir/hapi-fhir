/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.pid.IResourcePidStream;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Date;

public interface IGoldenResourceSearchSvc {

	/**
	 * Fetches a cursor of resource IDs for golden resources of the given type.
	 *
	 * @param theStart The start of the date range, must be inclusive.
	 * @param theEnd   The end of the date range, should be exclusive.
	 * @param theRequestPartitionId The request partition ID (may be <code>null</code> on nonpartitioned systems)
	 * @param theResourceType the type of resource.
	 */
	IResourcePidStream fetchGoldenResourceIdStream(
			Date theStart,
			Date theEnd,
			@Nullable RequestPartitionId theRequestPartitionId,
			@Nonnull String theResourceType);
}
