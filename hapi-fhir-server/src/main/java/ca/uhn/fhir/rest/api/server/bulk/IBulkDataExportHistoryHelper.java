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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Date;
import java.util.List;

/**
 * Bulk export history retrieval helper
 */
public interface IBulkDataExportHistoryHelper {

	/**
	 * Retrieve history for indicated resource IDs
	 *
	 * @param theResourceType       the type of resources to fetch history for
	 * @param theIdList				the list of resource ids which history to fetch
	 * @param theRequestPartitionId partition ID for the request
	 * @return bundle provider containing historical versions of the resources
	 */
	IBundleProvider fetchHistoryForResourceIds(
			@Nonnull String theResourceType,
			@Nonnull List<IResourcePersistentId<?>> theIdList,
			RequestPartitionId theRequestPartitionId,
			@Nullable Date theRangeStartInclusive,
			@Nonnull Date theRangeEndInclusive);
}
