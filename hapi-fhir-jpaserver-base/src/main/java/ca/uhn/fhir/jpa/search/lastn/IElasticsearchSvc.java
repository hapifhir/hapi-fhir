package ca.uhn.fhir.jpa.search.lastn;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.search.lastn.json.CodeJson;
import ca.uhn.fhir.jpa.search.lastn.json.ObservationJson;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface IElasticsearchSvc {

	/**
	 * Returns identifiers for the last most recent N observations that meet the specified criteria.
	 *
	 * @param theSearchParameterMap SearchParameterMap containing search parameters used for filtering the last N observations. Supported parameters include Subject, Patient, Code, Category and Max (the parameter used to determine N).
	 * @param theFhirContext        Current FhirContext.
	 * @param theMaxResultsToFetch  The maximum number of results to return for the purpose of paging.
	 * @return
	 */
	List<String> executeLastN(SearchParameterMap theSearchParameterMap, FhirContext theFhirContext, Integer theMaxResultsToFetch);

	/**
	 * Returns index document for a single Observation
	 *
	 * @param theDocumentID Identifier of Observation resource.
	 * @return
	 */
	ObservationJson getObservationDocument(String theDocumentID);

	/**
	 * Returns index document for a single Observation Code that either has a coding that matches a specified Code value and system or that has a specified text value.
	 *
	 * @param theCodeSystemHash A hash string constructed from a Code value and Code system used to match to an Observation Code.
	 * @param theText           A text value used to match to an Observation Code.
	 * @return
	 */
	CodeJson getObservationCodeDocument(String theCodeSystemHash, String theText);

	/**
	 * Creates or updates index for an Observation Resource.
	 *
	 * @param theDocumentId          Identifier for Observation resource.
	 * @param theObservationDocument Indexing document for Observation.
	 * @return True if Observation indexed successfully.
	 */
	Boolean createOrUpdateObservationIndex(String theDocumentId, ObservationJson theObservationDocument);

	/**
	 * Creates or updates index for an Observation Code.
	 *
	 * @param theCodeableConceptID       Identifier for Observation resource.
	 * @param theObservationCodeDocument Indexing document for Observation.
	 * @return True if Observation Code indexed successfully.
	 */
	Boolean createOrUpdateObservationCodeIndex(String theCodeableConceptID, CodeJson theObservationCodeDocument);

	/**
	 * Deletes index for an Observation Resource.
	 *
	 * @param theDocumentId Identifier for Observation resource.
	 */
	void deleteObservationDocument(String theDocumentId);

	/**
	 * Invoked when shutting down.
	 */
	void close() throws IOException;

	/**
	 * Returns inlined observation resource stored along with index mappings for matched identifiers
	 *
	 * @param thePids
	 * @return Resources list or empty if nothing found
	 */
	List<IBaseResource> getObservationResources(Collection<ResourcePersistentId> thePids);

}
