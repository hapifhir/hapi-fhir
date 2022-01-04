package ca.uhn.fhir.jpa.term.api;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

	boolean isStorageQueueEmpty();

	/**
	 * This is mostly for unit tests - we can disable processing of deferred concepts
	 * by changing this flag
	 */
	void setProcessDeferred(boolean theProcessDeferred);

	void addConceptToStorageQueue(TermConcept theConcept);

	void addConceptLinkToStorageQueue(TermConceptParentChildLink theConceptLink);

	void addConceptMapsToStorageQueue(List<ConceptMap> theConceptMaps);

	void addValueSetsToStorageQueue(List<ValueSet> theValueSets);

	void deleteCodeSystemForResource(ResourceTable theCodeSystemResourceToDelete);

	void deleteCodeSystemVersion(TermCodeSystemVersion theCodeSystemVersion);

	/**
	 * This is mostly here for unit tests - Saves any and all deferred concepts and links
	 */
	void saveAllDeferred();

	void logQueueForUnitTest();

}
