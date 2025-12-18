/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BaseTag;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;

import java.util.Collection;
import java.util.Map;

@SuppressWarnings("java:S1452") // Generic wildcard types are necessary here due to entity method return types
public interface IResourceMetadataExtractorSvc {

	/**
	 * Provenance details containing source URI and request ID from Resource.meta.source
	 */
	record ProvenanceDetails(String provenanceSourceUri, String provenanceRequestId) {}

	Collection<? extends BaseTag> getTags(ResourceHistoryTable theHistoryEntity);

	Collection<? extends BaseTag> getTags(ResourceTable theResourceEntity);

	Map<JpaPid, Collection<BaseTag>> getTagsBatch(Collection<ResourceHistoryTable> theHistoryEntities);

	ProvenanceDetails getProvenanceDetails(ResourceHistoryTable theHistoryEntity);
}
