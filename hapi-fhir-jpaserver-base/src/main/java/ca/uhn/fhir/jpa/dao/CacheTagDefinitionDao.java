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

import ca.uhn.fhir.jpa.dao.data.ITagDefinitionDao;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.HashMap;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Repository
public class CacheTagDefinitionDao {
	private static final Logger ourLog = LoggerFactory.getLogger(CacheTagDefinitionDao.class);

	private final ITagDefinitionDao tagDefinitionDao;
	private final MemoryCacheService memoryCacheService;

	public CacheTagDefinitionDao(ITagDefinitionDao tagDefinitionDao, MemoryCacheService memoryCacheService) {
		this.tagDefinitionDao = tagDefinitionDao;
		this.memoryCacheService = memoryCacheService;
	}

	/**
	 * Returns a TagDefinition or null if the scheme, term, and label are all blank.
	 */
	protected TagDefinition getTagOrNull(
			TransactionDetails transactionDetails,
			TagTypeEnum tagType,
			String scheme,
			String term,
			String label,
			String version,
			Boolean userSelected) {

		if (isBlank(scheme) && isBlank(term) && isBlank(label)) {
			return null;
		}

		MemoryCacheService.TagDefinitionCacheKey key =
				toTagDefinitionMemoryCacheKey(tagType, scheme, term, version, userSelected);
		TagDefinition tagDefinition = memoryCacheService.getIfPresent(MemoryCacheService.CacheEnum.TAG_DEFINITION, key);

		if (tagDefinition == null) {
			HashMap<MemoryCacheService.TagDefinitionCacheKey, TagDefinition> resolvedTagDefinitions =
					transactionDetails.getOrCreateUserData("resolvedTagDefinitions", HashMap::new);

			tagDefinition = resolvedTagDefinitions.get(key);

			if (tagDefinition == null) {
				tagDefinition = getOrCreateTag(tagType, scheme, term, label, version, userSelected);

				TransactionSynchronization sync =
						new AddTagDefinitionToCacheAfterCommitSynchronization(key, tagDefinition);
				TransactionSynchronizationManager.registerSynchronization(sync);

				resolvedTagDefinitions.put(key, tagDefinition);
			}
		}

		return tagDefinition;
	}

	/**
	 * Gets or creates a TagDefinition entity.
	 */
	private TagDefinition getOrCreateTag(
			TagTypeEnum tagType, String scheme, String term, String label, String version, Boolean userSelected) {
		List<TagDefinition> result = tagDefinitionDao.findByTagTypeAndSchemeAndTermAndVersionAndUserSelected(
				tagType, scheme, term, version, userSelected, Pageable.ofSize(1));

		if (!result.isEmpty()) {
			return result.get(0);
		} else {
			// Create a new TagDefinition if no result is found
			TagDefinition newTag = new TagDefinition(tagType, scheme, term, label);
			newTag.setVersion(version);
			newTag.setUserSelected(userSelected);
			return tagDefinitionDao.save(newTag);
		}
	}

	@Nonnull
	private static MemoryCacheService.TagDefinitionCacheKey toTagDefinitionMemoryCacheKey(
			TagTypeEnum tagType, String scheme, String term, String version, Boolean userSelected) {
		return new MemoryCacheService.TagDefinitionCacheKey(tagType, scheme, term, version, userSelected);
	}

	private class AddTagDefinitionToCacheAfterCommitSynchronization implements TransactionSynchronization {
		private final TagDefinition tagDefinition;
		private final MemoryCacheService.TagDefinitionCacheKey key;

		public AddTagDefinitionToCacheAfterCommitSynchronization(
				MemoryCacheService.TagDefinitionCacheKey key, TagDefinition tagDefinition) {
			this.tagDefinition = tagDefinition;
			this.key = key;
		}

		@Override
		public void afterCommit() {
			memoryCacheService.put(MemoryCacheService.CacheEnum.TAG_DEFINITION, key, tagDefinition);
		}
	}
}
