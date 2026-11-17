/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.term.api;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;

/**
 * This service handles processing "deferred" concept writes, meaning concepts that have neen
 * queued for storage because there are too many of them to handle in a single transaction.
 */
public interface ITermDeferredStorageSvc {

	void saveDeferred();

	/**
	 * @deprecated Use {@link #isStorageQueueEmpty(boolean)} instead
	 */
	@Deprecated
	default boolean isStorageQueueEmpty() {
		return isStorageQueueEmpty(true);
	}

	/**
	 * If you are calling this in a loop or something similar, consider the impact of
	 * setting {@literal theIncludeExecutingJobs} to true. When that parameter is set
	 * to true, each call to this method results in a database lookup!
	 */
	boolean isStorageQueueEmpty(boolean theIncludeExecutingJobs);

	/**
	 * This is mostly for unit tests - we can disable processing of deferred concepts
	 * by changing this flag
	 */
	void setProcessDeferred(boolean theProcessDeferred);

	void addConceptToStorageQueue(TermConcept theConcept);

	void addConceptLinkToStorageQueue(TermConceptParentChildLink theConceptLink);

	void addConceptMapsToStorageQueue(List<ConceptMap> theConceptMaps);

	void addValueSetsToStorageQueue(List<ValueSet> theValueSets);

	/**
	 * Marks the terminology data of a deleted CodeSystem resource as deleted and creates the batch job that removes
	 * it.
	 *
	 * @param theCodeSystemResourceToDelete the entity of the CodeSystem resource being deleted
	 * @throws IllegalArgumentException if terminology data is found and no transaction is active on the calling thread
	 */
	void deleteCodeSystemForResource(ResourceTable theCodeSystemResourceToDelete);

	/**
	 * Creates the batch job that removes a CodeSystem version and its concepts.
	 *
	 * @param theCodeSystemVersion the stored version to remove
	 * @throws IllegalArgumentException if no transaction is active on the calling thread
	 */
	void deleteCodeSystemVersion(TermCodeSystemVersion theCodeSystemVersion);

	/**
	 * @deprecated Does nothing: {@link #isJobsExecuting()} reads the state of the delete jobs from the job tables.
	 */
	@Deprecated(since = "9.2.0")
	default void notifyJobEnded(String theId) {}

	/**
	 * This is mostly here for unit tests - Saves any and all deferred concepts and links
	 */
	void saveAllDeferred();

	void logQueueForUnitTest();

	/**
	 * @return {@literal true} if a batch job that removes a CodeSystem or a CodeSystem version has not ended yet,
	 * whichever node created it. Each call results in a database lookup.
	 */
	boolean isJobsExecuting();

	/**
	 * Only to be used from tests - Disallow test timeouts on deferred tasks
	 */
	void disallowDeferredTaskTimeout();
}
