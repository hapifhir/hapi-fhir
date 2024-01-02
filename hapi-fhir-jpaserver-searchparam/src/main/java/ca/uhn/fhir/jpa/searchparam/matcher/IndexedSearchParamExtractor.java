/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
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
package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

public class IndexedSearchParamExtractor {
	@Autowired
	private FhirContext myContext;

	@Autowired
	private SearchParamExtractorService mySearchParamExtractorService;

	@Nonnull
	public ResourceIndexedSearchParams extractIndexedSearchParams(
			IBaseResource theResource, RequestDetails theRequest) {
		return extractIndexedSearchParams(theResource, theRequest, ISearchParamExtractor.ALL_PARAMS);
	}

	@Nonnull
	public ResourceIndexedSearchParams extractIndexedSearchParams(
			IBaseResource theResource, RequestDetails theRequest, ISearchParamExtractor.ISearchParamFilter filter) {
		ResourceTable entity = new ResourceTable();
		TransactionDetails transactionDetails = new TransactionDetails();
		String resourceType = myContext.getResourceType(theResource);
		entity.setResourceType(resourceType);
		ResourceIndexedSearchParams resourceIndexedSearchParams = ResourceIndexedSearchParams.withSets();
		mySearchParamExtractorService.extractFromResource(
				null,
				theRequest,
				resourceIndexedSearchParams,
				ResourceIndexedSearchParams.empty(),
				entity,
				theResource,
				transactionDetails,
				false,
				filter);
		return resourceIndexedSearchParams;
	}
}
