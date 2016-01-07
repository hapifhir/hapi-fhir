package ca.uhn.fhir.jpa.search;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import org.hibernate.search.indexes.interceptor.EntityIndexingInterceptor;
import org.hibernate.search.indexes.interceptor.IndexingOverride;

import ca.uhn.fhir.jpa.entity.ResourceTable;

/**
 * Only store non-deleted resources
 */
public class IndexNonDeletedInterceptor implements EntityIndexingInterceptor<ResourceTable> {

	@Override
	public IndexingOverride onAdd(ResourceTable entity) {
		if (entity.getDeleted() == null) {
			if (entity.getIndexStatus() != null) {
				return IndexingOverride.APPLY_DEFAULT;
			}
		}
		return IndexingOverride.SKIP;
	}

	@Override
	public IndexingOverride onUpdate(ResourceTable entity) {
		if (entity.getIndexStatus() == null) {
			return IndexingOverride.SKIP;
		}
		if (entity.getDeleted() == null) {
			return IndexingOverride.UPDATE;
		}
		return IndexingOverride.REMOVE;
	}

	@Override
	public IndexingOverride onDelete(ResourceTable entity) {
		return IndexingOverride.APPLY_DEFAULT;
	}

	@Override
	public IndexingOverride onCollectionUpdate(ResourceTable entity) {
		return IndexingOverride.APPLY_DEFAULT;
	}
}
