package ca.uhn.fhir.jpa.searchparam.matcher;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceLinkExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndexedSearchParamExtractor {
	@Autowired
	private FhirContext myContext;
	@Autowired
	private SearchParamExtractorService mySearchParamExtractorService;
	@Autowired
	private ResourceLinkExtractor myResourceLinkExtractor;
	@Autowired
	private InlineResourceLinkResolver myInlineResourceLinkResolver;

	public ResourceIndexedSearchParams extractIndexedSearchParams(IBaseResource theResource, RequestDetails theRequest) {
		ResourceTable entity = new ResourceTable();
		String resourceType = myContext.getResourceDefinition(theResource).getName();
		entity.setResourceType(resourceType);
		ResourceIndexedSearchParams resourceIndexedSearchParams = new ResourceIndexedSearchParams();
		mySearchParamExtractorService.extractFromResource(resourceIndexedSearchParams, entity, theResource);
		myResourceLinkExtractor.extractResourceLinks(resourceIndexedSearchParams, entity, theResource, theResource.getMeta().getLastUpdated(), myInlineResourceLinkResolver, false, theRequest);
		return resourceIndexedSearchParams;
	}
}
