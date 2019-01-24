package ca.uhn.fhir.jpa.subscription.module.matcher;

/*-
 * #%L
 * HAPI FHIR Subscription Server
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
import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

public class InMemorySubscriptionMatcher implements ISubscriptionMatcher {

	@Autowired
	private FhirContext myContext;
	@Autowired
	private CriteriaResourceMatcher myCriteriaResourceMatcher;
	@Autowired
	private SearchParamExtractorService mySearchParamExtractorService;
	@Autowired
	private ResourceLinkExtractor myResourceLinkExtractor;
	@Autowired
	private InlineResourceLinkResolver myInlineResourceLinkResolver;

	@Override
	public SubscriptionMatchResult match(CanonicalSubscription theSubscription, ResourceModifiedMessage theMsg) {
		try {
			return match(theSubscription.getCriteriaString(), theMsg.getNewPayload(myContext));
		} catch (Exception e) {
			throw new InternalErrorException("Failure processing resource ID[" + theMsg.getId(myContext) + "] for subscription ID[" + theSubscription.getIdElementString() + "]: " + e.getMessage(), e);
		}
	}

	SubscriptionMatchResult match(String criteria, IBaseResource resource) {
		ResourceTable entity = new ResourceTable();
		String resourceType = myContext.getResourceDefinition(resource).getName();
		entity.setResourceType(resourceType);
		ResourceIndexedSearchParams searchParams = new ResourceIndexedSearchParams();
		mySearchParamExtractorService.extractFromResource(searchParams, entity, resource);
		myResourceLinkExtractor.extractResourceLinks(searchParams, entity, resource, resource.getMeta().getLastUpdated(), myInlineResourceLinkResolver, false);
		return myCriteriaResourceMatcher.match(criteria, resource, searchParams);
	}
}
