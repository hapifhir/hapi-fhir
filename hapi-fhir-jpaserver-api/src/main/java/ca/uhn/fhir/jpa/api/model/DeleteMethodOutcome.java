package ca.uhn.fhir.jpa.api.model;

/*
 * #%L
 * HAPI FHIR JPA API
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.MethodOutcome;

import java.util.List;

/**
 * This class is a replacement for {@link DaoMethodOutcome} for delete operations,
 * as they can perform their operation over multiple resources
 */
public class DeleteMethodOutcome extends MethodOutcome {

	private List<ResourceTable> myDeletedEntities;
	private long myExpungedResourcesCount;
	private long myExpungedEntitiesCount;

	public List<ResourceTable> getDeletedEntities() {
		return myDeletedEntities;
	}

	public DeleteMethodOutcome setDeletedEntities(List<ResourceTable> theDeletedEntities) {
		myDeletedEntities = theDeletedEntities;
		return this;
	}

	public long getExpungedResourcesCount() {
		return myExpungedResourcesCount;
	}

	public DeleteMethodOutcome setExpungedResourcesCount(long theExpungedResourcesCount) {
		myExpungedResourcesCount = theExpungedResourcesCount;
		return this;
	}

	public long getExpungedEntitiesCount() {
		return myExpungedEntitiesCount;
	}

	public DeleteMethodOutcome setExpungedEntitiesCount(long theExpungedEntitiesCount) {
		myExpungedEntitiesCount = theExpungedEntitiesCount;
		return this;
	}
}
