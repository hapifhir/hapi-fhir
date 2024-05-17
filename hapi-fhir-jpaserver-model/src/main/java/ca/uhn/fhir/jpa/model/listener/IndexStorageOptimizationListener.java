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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Sets <code>SP_NAME, RES_TYPE, SP_UPDATED</code> column values to null for all HFJ_SPIDX tables
 * if storage setting {@link ca.uhn.fhir.jpa.model.entity.StorageSettings#isIndexStorageOptimized()} is enabled.
 * <p>
 * Using EntityListener to change HFJ_SPIDX column values right before insert/update to database as
 * <code>SP_NAME, RES_TYPE, SP_UPDATED</code> values could still be used after merge/persist is executed
 * on IndexedSearchParam entity but transaction is not yet commited.
 *</p>
 * See {@link ca.uhn.fhir.jpa.model.entity.StorageSettings#setIndexStorageOptimized(boolean)}
 */
public class IndexStorageOptimizationListener {

	@Autowired
	private StorageSettings myStorageSettings;

	@PrePersist
	@PreUpdate
	public void updateEntityFields(Object theEntity) {
		if (theEntity instanceof BaseResourceIndexedSearchParam && myStorageSettings.isIndexStorageOptimized()) {
			BaseResourceIndexedSearchParam resourceIndexedSearchParam = (BaseResourceIndexedSearchParam) theEntity;
			resourceIndexedSearchParam.optimizeIndexStorage();
		}
	}
}
