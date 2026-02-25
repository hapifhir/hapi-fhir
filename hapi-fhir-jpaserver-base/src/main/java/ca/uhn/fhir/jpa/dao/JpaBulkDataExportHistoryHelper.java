/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.bulk.IBulkDataExportHistoryHelper;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class JpaBulkDataExportHistoryHelper implements IBulkDataExportHistoryHelper {

	@Autowired
	private PersistedJpaBundleProviderFactory myBundleProviderFactory;

	@Override
	public IBundleProvider fetchHistoryForResourceIds(
			@Nonnull String theResourceType,
			@Nonnull List<IResourcePersistentId<?>> theIdList,
			RequestPartitionId theRequestPartitionId,
			@Nullable Date theRangeStartInclusive,
			@Nonnull Date theRangeEndInclusive) {

		return myBundleProviderFactory.historyFromResourceIds(
				theResourceType, theIdList, theRequestPartitionId, theRangeStartInclusive, theRangeEndInclusive);
	}
}
