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

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryProvenanceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTagDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BaseTag;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryProvenanceEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTablePk;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTag;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Service for extracting metadata (tags, provenance) from resource entities.
 */
@Service
public class ResourceMetadataExtractorSvcImpl implements IResourceMetadataExtractorSvc {

	private final JpaStorageSettings myStorageSettings;
	private final IResourceHistoryTagDao myResourceHistoryTagDao;
	private final IResourceTagDao myResourceTagDao;
	private final IResourceHistoryProvenanceDao myResourceHistoryProvenanceDao;

	public ResourceMetadataExtractorSvcImpl(
			JpaStorageSettings theStorageSettings,
			IResourceHistoryTagDao theResourceHistoryTagDao,
			IResourceTagDao theResourceTagDao,
			IResourceHistoryProvenanceDao theResourceHistoryProvenanceDao) {
		myStorageSettings = theStorageSettings;
		myResourceHistoryTagDao = theResourceHistoryTagDao;
		myResourceTagDao = theResourceTagDao;
		myResourceHistoryProvenanceDao = theResourceHistoryProvenanceDao;
	}

	/**
	 * Extract tags from a resource history entity.
	 *
	 * @param theHistoryEntity The resource history entity
	 * @return Collection of tags associated with the resource
	 */
	public Collection<? extends BaseTag> getTags(ResourceHistoryTable theHistoryEntity) {
		Collection<? extends BaseTag> tagList = null;
		switch (myStorageSettings.getTagStorageMode()) {
			case NON_VERSIONED:
				if (theHistoryEntity.getResourceTable().isHasTags()) {
					tagList = theHistoryEntity.getResourceTable().getTags();
				}
				break;
			case INLINE:
				break;
			case VERSIONED:
			default:
				if (theHistoryEntity.isHasTags()) {
					tagList = theHistoryEntity.getTags();
				}
				break;
		}
		return tagList;
	}

	/**
	 * Extract tags from a resource entity.
	 *
	 * @param theResourceEntity The resource entity
	 * @return Collection of tags associated with the resource
	 */
	public Collection<? extends BaseTag> getTags(ResourceTable theResourceEntity) {
		Collection<? extends BaseTag> tagList = null;
		switch (myStorageSettings.getTagStorageMode()) {
			case VERSIONED:
			case NON_VERSIONED:
				if (theResourceEntity.isHasTags()) {
					tagList = theResourceEntity.getTags();
				} else {
					tagList = List.of();
				}
				break;
			case INLINE:
			default:
				break;
		}
		return tagList;
	}

	/**
	 * Batch extract tags for multiple resource history entities.
	 *
	 * @param theHistoryEntities Collection of resource history entities
	 * @return Map of resource IDs to their tags
	 */
	public Map<JpaPid, Collection<BaseTag>> getTagsBatch(Collection<ResourceHistoryTable> theHistoryEntities) {
		return switch (myStorageSettings.getTagStorageMode()) {
			case VERSIONED -> getPidToTagMapVersioned(theHistoryEntities);
			case NON_VERSIONED -> getPidToTagMapUnversioned(theHistoryEntities);
			case INLINE -> Map.of();
		};
	}

	@Nonnull
	private Map<JpaPid, Collection<BaseTag>> getPidToTagMapVersioned(
			Collection<ResourceHistoryTable> theHistoryEntities) {
		List<ResourceHistoryTablePk> idList = new ArrayList<>(theHistoryEntities.size());

		// -- find all resource has tags
		for (ResourceHistoryTable resource : theHistoryEntities) {
			if (resource.isHasTags()) {
				idList.add(resource.getId());
			}
		}

		Map<JpaPid, Collection<BaseTag>> tagMap = new HashMap<>();

		// -- no tags
		if (idList.isEmpty()) {
			return tagMap;
		}

		// -- get all tags for the idList
		Collection<ResourceHistoryTag> tagList = myResourceHistoryTagDao.findByVersionIds(idList);

		// -- build the map, key = resourceId, value = list of ResourceTag
		JpaPid resourceId;
		Collection<BaseTag> tagCol;
		for (ResourceHistoryTag tag : tagList) {

			resourceId = tag.getResourcePid();
			tagCol = tagMap.get(resourceId);
			if (tagCol == null) {
				tagCol = new ArrayList<>();
				tagCol.add(tag);
				tagMap.put(resourceId, tagCol);
			} else {
				tagCol.add(tag);
			}
		}

		return tagMap;
	}

	@Nonnull
	private Map<JpaPid, Collection<BaseTag>> getPidToTagMapUnversioned(
			Collection<ResourceHistoryTable> theHistoryTables) {
		List<JpaPid> idList = new ArrayList<>(theHistoryTables.size());

		// -- find all resource has tags
		for (ResourceHistoryTable resource : theHistoryTables) {
			if (resource.isHasTags()) {
				idList.add(resource.getResourceId());
			}
		}

		Map<JpaPid, Collection<BaseTag>> tagMap = new HashMap<>();

		// -- no tags
		if (idList.isEmpty()) {
			return tagMap;
		}

		// -- get all tags for the idList
		Collection<ResourceTag> tagList = myResourceTagDao.findByResourceIds(idList);

		// -- build the map, key = resourceId, value = list of ResourceTag
		JpaPid resourceId;
		Collection<BaseTag> tagCol;
		for (ResourceTag tag : tagList) {

			resourceId = tag.getResourceId();
			tagCol = tagMap.get(resourceId);
			if (tagCol == null) {
				tagCol = new ArrayList<>();
				tagCol.add(tag);
				tagMap.put(resourceId, tagCol);
			} else {
				tagCol.add(tag);
			}
		}

		return tagMap;
	}

	/**
	 * Extract provenance details from a resource history entity.
	 *
	 * @param theHistoryEntity The resource history entity
	 * @return Provenance details containing source URI and request ID
	 */
	public ProvenanceDetails getProvenanceDetails(ResourceHistoryTable theHistoryEntity) {
		String provenanceSourceUri = theHistoryEntity.getSourceUri();
		String provenanceRequestId = theHistoryEntity.getRequestId();
		if (isBlank(provenanceSourceUri)
				&& isBlank(provenanceRequestId)
				&& myStorageSettings.isAccessMetaSourceInformationFromProvenanceTable()) {
			Optional<ResourceHistoryProvenanceEntity> provenanceOpt = myResourceHistoryProvenanceDao.findById(
					theHistoryEntity.getId().asIdAndPartitionId());
			if (provenanceOpt.isPresent()) {
				ResourceHistoryProvenanceEntity provenance = provenanceOpt.get();
				return new ProvenanceDetails(provenance.getSourceUri(), provenance.getRequestId());
			}
		}
		return new ProvenanceDetails(provenanceSourceUri, provenanceRequestId);
	}
}
