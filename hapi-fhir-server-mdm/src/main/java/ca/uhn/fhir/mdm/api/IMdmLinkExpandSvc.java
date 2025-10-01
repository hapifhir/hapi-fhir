/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.api;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface IMdmLinkExpandSvc {
	Set<String> expandMdmBySourceResource(RequestPartitionId theRequestPartitionId, IBaseResource theResource);

	Set<String> expandMdmBySourceResourceId(RequestPartitionId theRequestPartitionId, IIdType theId);

	/**
	 * Does the mdm expansion of a list of ids for a single resource type
	 * @param theRequestPartitionId the request partition to use
	 * @param theIds the list of patient ids to expand
	 * @return the mdm expanded set of patient ids (should include the original set as well as any linked patient ids)
	 */
	Set<String> expandMdmBySourceResourceIdsForSingleResourceType(RequestPartitionId theRequestPartitionId, Collection<IIdType> theIds);

	Set<String> expandMdmBySourceResourcePid(
			RequestPartitionId theRequestPartitionId, IResourcePersistentId<?> theSourceResourcePid);

	Set<String> expandMdmByGoldenResourceId(
			RequestPartitionId theRequestPartitionId, IResourcePersistentId<?> theGoldenResourcePid);

	Set<String> expandMdmByGoldenResourcePid(
			RequestPartitionId theRequestPartitionId, IResourcePersistentId<?> theGoldenResourcePid);

	Set<String> expandMdmByGoldenResourceId(RequestPartitionId theRequestPartitionId, IIdType theId);
}
