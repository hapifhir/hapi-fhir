/*
 * #%L
 * HAPI FHIR JPA Model
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
package ca.uhn.fhir.jpa.model.listener;

import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.model.search.ISearchParamHashIdentityRegistry;
import ca.uhn.fhir.rest.server.util.IndexedSearchParam;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

/**
 * Sets <code>SP_NAME, RES_TYPE, SP_UPDATED</code> column values to null for all HFJ_SPIDX tables
 * if storage setting {@link ca.uhn.fhir.jpa.model.entity.StorageSettings#isIndexStorageOptimized()} is enabled.
 * <p>
 * Using EntityListener to change HFJ_SPIDX column values right before insert/update to database.
 * </p>
 * <p>
 * As <code>SP_NAME, RES_TYPE</code> values could still be used after merge/persist to database, we are restoring
 * them from <code>HASH_IDENTITY</code> value.
 *</p>
 * See {@link ca.uhn.fhir.jpa.model.entity.StorageSettings#setIndexStorageOptimized(boolean)}
 */
public class IndexStorageOptimizationListener {

	public IndexStorageOptimizationListener(
			@Autowired StorageSettings theStorageSettings, @Autowired ApplicationContext theApplicationContext) {
		this.myStorageSettings = theStorageSettings;
		this.myApplicationContext = theApplicationContext;
	}

	private final StorageSettings myStorageSettings;
	private final ApplicationContext myApplicationContext;

	@PrePersist
	@PreUpdate
	public void optimizeSearchParams(Object theEntity) {
		if (myStorageSettings.isIndexStorageOptimized() && theEntity instanceof BaseResourceIndexedSearchParam) {
			((BaseResourceIndexedSearchParam) theEntity).optimizeIndexStorage();
		}
	}

	@PostLoad
	@PostPersist
	@PostUpdate
	public void restoreSearchParams(Object theEntity) {
		if (myStorageSettings.isIndexStorageOptimized() && theEntity instanceof BaseResourceIndexedSearchParam) {
			restoreSearchParams((BaseResourceIndexedSearchParam) theEntity);
		}
	}

	/**
	 * As <code>SP_NAME, RES_TYPE</code> values could still be used after merge/persist to database (mostly by tests),
	 * we are restoring them from <code>HASH_IDENTITY</code> value.
	 * Note that <code>SP_NAME, RES_TYPE</code> values are not recovered if
	 * {@link ca.uhn.fhir.jpa.model.entity.StorageSettings#isIndexOnContainedResources()} or
	 * {@link ca.uhn.fhir.jpa.model.entity.StorageSettings#isIndexOnContainedResourcesRecursively()}
	 * settings are enabled.
	 */
	private void restoreSearchParams(BaseResourceIndexedSearchParam theResourceIndexedSearchParam) {
		// getting ISearchParamHashIdentityRegistry from the App Context as it is initialized after EntityListeners
		ISearchParamHashIdentityRegistry searchParamRegistry =
				myApplicationContext.getBean(ISearchParamHashIdentityRegistry.class);
		Optional<IndexedSearchParam> indexedSearchParamOptional =
				searchParamRegistry.getIndexedSearchParamByHashIdentity(
						theResourceIndexedSearchParam.getHashIdentity());

		if (indexedSearchParamOptional.isPresent()) {
			theResourceIndexedSearchParam.setResourceType(
					indexedSearchParamOptional.get().getResourceType());
			theResourceIndexedSearchParam.restoreParamName(
					indexedSearchParamOptional.get().getParameterName());
		}
	}
}
