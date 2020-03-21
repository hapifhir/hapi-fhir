package ca.uhn.fhir.jpa.search;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.jpa.entity.TermConcept;

public class DeferConceptIndexingInterceptor  implements EntityIndexingInterceptor<TermConcept> {

	@Override
	public IndexingOverride onAdd(TermConcept theEntity) {
		if (theEntity.getIndexStatus() == null) {
			return IndexingOverride.SKIP;
		}
				
		return IndexingOverride.APPLY_DEFAULT;
	}
	
	@Override
	public IndexingOverride onCollectionUpdate(TermConcept theEntity) {
		return IndexingOverride.APPLY_DEFAULT;
	}


	@Override
	public IndexingOverride onDelete(TermConcept theEntity) {
		return IndexingOverride.APPLY_DEFAULT;
	}

	@Override
	public IndexingOverride onUpdate(TermConcept theEntity) {
		return onAdd(theEntity);
	}

}
