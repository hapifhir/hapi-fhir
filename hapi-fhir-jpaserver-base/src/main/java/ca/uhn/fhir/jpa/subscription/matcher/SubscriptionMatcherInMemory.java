package ca.uhn.fhir.jpa.subscription.matcher;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.index.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.dao.index.SearchParamExtractorService;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.subscription.ResourceModifiedMessage;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class SubscriptionMatcherInMemory implements ISubscriptionMatcher {

	@Autowired
	private FhirContext myContext;
	@Autowired
	private CriteriaResourceMatcher myCriteriaResourceMatcher;
	@Autowired
	private SearchParamExtractorService mySearchParamExtractorService;

	@Override
	public SubscriptionMatchResult match(String criteria, ResourceModifiedMessage msg) {
		return match(criteria, msg.getNewPayload(myContext));
	}

	SubscriptionMatchResult match(String criteria, IBaseResource resource) {
		ResourceTable entity = new ResourceTable();
		String resourceType = myContext.getResourceDefinition(resource).getName();
		entity.setResourceType(resourceType);
		ResourceIndexedSearchParams searchParams = new ResourceIndexedSearchParams();
		mySearchParamExtractorService.extractFromResource(searchParams, entity, resource);
		mySearchParamExtractorService.extractInlineReferences(resource);
		mySearchParamExtractorService.extractResourceLinks(searchParams, entity, resource, resource.getMeta().getLastUpdated(), false);
		RuntimeResourceDefinition resourceDefinition = myContext.getResourceDefinition(resource);
		return myCriteriaResourceMatcher.match(criteria, resourceDefinition, searchParams);
	}
}
