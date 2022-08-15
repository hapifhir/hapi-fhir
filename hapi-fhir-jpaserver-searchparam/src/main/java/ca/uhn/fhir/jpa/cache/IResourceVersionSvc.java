package ca.uhn.fhir.jpa.cache;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * This interface is used by the {@link IResourceChangeListenerCacheRefresher} to read resources matching the provided
 * search parameter map in the repository and compare them to caches stored in the {@link IResourceChangeListenerRegistry}.
 */
public interface IResourceVersionSvc {
	@Nonnull
	ResourceVersionMap getVersionMap(RequestPartitionId theRequestPartitionId, String theResourceName, SearchParameterMap theSearchParamMap);

	@Nonnull
	default ResourceVersionMap getVersionMap(String theResourceName, SearchParameterMap theSearchParamMap) {
		return getVersionMap(RequestPartitionId.allPartitions(), theResourceName, theSearchParamMap);
	}

	ResourcePersistentIdMap getLatestVersionIdsForResourceIds(RequestPartitionId thePartition, List<IIdType> theIds);
}
