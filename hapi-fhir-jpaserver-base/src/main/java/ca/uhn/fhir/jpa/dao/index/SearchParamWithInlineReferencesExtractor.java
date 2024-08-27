/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.extractor.BaseSearchParamWithInlineReferencesExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamWithInlineReferencesExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import com.google.common.annotations.VisibleForTesting;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class SearchParamWithInlineReferencesExtractor extends BaseSearchParamWithInlineReferencesExtractor<JpaPid>
		implements ISearchParamWithInlineReferencesExtractor {

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	@Autowired
	private SearchParamExtractorService mySearchParamExtractorService;

	@VisibleForTesting
	public void setSearchParamExtractorService(SearchParamExtractorService theSearchParamExtractorService) {
		mySearchParamExtractorService = theSearchParamExtractorService;
	}

	public void populateFromResource(
			RequestPartitionId theRequestPartitionId,
			ResourceIndexedSearchParams theParams,
			TransactionDetails theTransactionDetails,
			ResourceTable theEntity,
			IBaseResource theResource,
			ResourceIndexedSearchParams theExistingParams,
			RequestDetails theRequest,
			boolean thePerformIndexing) {
		if (thePerformIndexing) {
			// Perform inline match URL substitution
			extractInlineReferences(theRequest, theResource, theTransactionDetails);
		}

		mySearchParamExtractorService.extractFromResource(
				theRequestPartitionId,
				theRequest,
				theParams,
				theExistingParams,
				theEntity,
				theResource,
				theTransactionDetails,
				thePerformIndexing,
				ISearchParamExtractor.ALL_PARAMS);
	}
}
