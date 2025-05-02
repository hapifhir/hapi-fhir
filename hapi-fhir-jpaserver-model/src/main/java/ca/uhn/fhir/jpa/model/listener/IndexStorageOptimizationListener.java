/*
 * #%L
 * HAPI FHIR JPA Model
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
package ca.uhn.fhir.jpa.model.listener;

import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Sets <code>SP_NAME, RES_TYPE</code> column values to null for all HFJ_SPIDX tables
 * if storage setting {@link ca.uhn.fhir.jpa.model.entity.StorageSettings#isIndexStorageOptimized()} is enabled.
 * Sets <code>SP_UPDATED</code> column value to null for all HFJ_SPIDX tables.
 * <p>
 * Using EntityListener to change HFJ_SPIDX column values right before insert/update to database.
 * </p>
 * See {@link ca.uhn.fhir.jpa.model.entity.StorageSettings#setIndexStorageOptimized(boolean)}
 */
public class IndexStorageOptimizationListener {

	public IndexStorageOptimizationListener(@Autowired StorageSettings theStorageSettings) {
		this.myStorageSettings = theStorageSettings;
	}

	private final StorageSettings myStorageSettings;

	@PrePersist
	@PreUpdate
	public void optimizeSearchParams(Object theEntity) {
		if (theEntity instanceof BaseResourceIndexedSearchParam) {
			if (myStorageSettings.isIndexStorageOptimized()) {
				((BaseResourceIndexedSearchParam) theEntity).optimizeIndexStorage();
			}
			((BaseResourceIndexedSearchParam) theEntity).setUpdated(null);
		}
	}
}
