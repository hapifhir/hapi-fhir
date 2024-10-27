/*
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.api.model;

import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import java.util.List;

/**
 * This class is a replacement for {@link DaoMethodOutcome} for delete operations,
 * as they can perform their operation over multiple resources
 */
public class DeleteMethodOutcome extends MethodOutcome {

	private List<? extends IBasePersistedResource> myDeletedEntities;

	@Deprecated
	private long myExpungedResourcesCount;

	@Deprecated
	private long myExpungedEntitiesCount;

	public DeleteMethodOutcome() {}

	public DeleteMethodOutcome(IBaseOperationOutcome theBaseOperationOutcome) {
		super(theBaseOperationOutcome);
	}

	public List<? extends IBasePersistedResource> getDeletedEntities() {
		return myDeletedEntities;
	}

	/**
	 * Use {@literal ca.uhn.fhir.jpa.batch.writer.SqlExecutorWriter#ENTITY_TOTAL_UPDATED_OR_DELETED}
	 */
	@Deprecated
	public DeleteMethodOutcome setDeletedEntities(List<? extends IBasePersistedResource> theDeletedEntities) {
		myDeletedEntities = theDeletedEntities;
		return this;
	}

	/**
	 * Use {@literal ca.uhn.fhir.jpa.batch.listener.PidReaderCounterListener#RESOURCE_TOTAL_PROCESSED}
	 */
	@Deprecated
	public long getExpungedResourcesCount() {
		return myExpungedResourcesCount;
	}

	@Deprecated
	public DeleteMethodOutcome setExpungedResourcesCount(long theExpungedResourcesCount) {
		myExpungedResourcesCount = theExpungedResourcesCount;
		return this;
	}

	@Deprecated
	public long getExpungedEntitiesCount() {
		return myExpungedEntitiesCount;
	}

	@Deprecated
	public DeleteMethodOutcome setExpungedEntitiesCount(long theExpungedEntitiesCount) {
		myExpungedEntitiesCount = theExpungedEntitiesCount;
		return this;
	}
}
