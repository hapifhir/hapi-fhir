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
 * This service handles terminology work that has been queued to be processed later: the
 * deletion of CodeSystems and of CodeSystem versions, and the storage of queued ValueSets and
 * ConceptMaps.
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

	/**
	 * Queues a concept to be stored later by {@link #saveDeferred()}.
	 *
	 * @param theConcept the concept to store
	 * @deprecated Not called by HAPI FHIR, which stores the concepts of a CodeSystem in the
	 * transaction that stores the CodeSystem. Queued concepts are held in the memory of one server
	 * node, so the other nodes of a cluster cannot tell that they are still to be stored.
	 */
	@Deprecated(since = "9.2.0", forRemoval = true)
	void addConceptToStorageQueue(TermConcept theConcept);

	/**
	 * Queues a parent/child link between two concepts to be stored later by {@link #saveDeferred()}.
	 *
	 * @param theConceptLink the link to store
	 * @deprecated For the reason given on {@link #addConceptToStorageQueue(TermConcept)}
	 */
	@Deprecated(since = "9.2.0", forRemoval = true)
	void addConceptLinkToStorageQueue(TermConceptParentChildLink theConceptLink);

	/**
	 * Queues ConceptMaps to be stored later by {@link #saveDeferred()}.
	 *
	 * @param theConceptMaps the ConceptMaps to store
	 * @deprecated Called only by the five-argument {@code storeNewCodeSystemVersion} of
	 * {@link ITermCodeSystemStorageSvc}, which is deprecated
	 */
	@Deprecated(since = "9.2.0", forRemoval = true)
	void addConceptMapsToStorageQueue(List<ConceptMap> theConceptMaps);

	/**
	 * Queues ValueSets to be stored later by {@link #saveDeferred()}.
	 *
	 * @param theValueSets the ValueSets to store
	 * @deprecated For the reason given on {@link #addConceptMapsToStorageQueue(List)}
	 */
	@Deprecated(since = "9.2.0", forRemoval = true)
	void addValueSetsToStorageQueue(List<ValueSet> theValueSets);

	void deleteCodeSystemForResource(ResourceTable theCodeSystemResourceToDelete);

	void deleteCodeSystemVersion(TermCodeSystemVersion theCodeSystemVersion);

	void notifyJobEnded(String theId);

	/**
	 * This is mostly here for unit tests - Saves any and all deferred concepts and links
	 */
	void saveAllDeferred();

	void logQueueForUnitTest();

	boolean isJobsExecuting();

	/**
	 * Only to be used from tests - Disallow test timeouts on deferred tasks
	 */
	void disallowDeferredTaskTimeout();
}
