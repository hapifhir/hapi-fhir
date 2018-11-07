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
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.dao.index.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.service.MatchUrlService;
import ca.uhn.fhir.jpa.subscription.ResourceModifiedMessage;
import ca.uhn.fhir.model.api.IQueryParameterType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Lazy
public class SubscriptionMatcherInMemory implements ISubscriptionMatcher {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionMatcherInMemory.class);

	@Autowired
	private FhirContext myContext;
	@Autowired
	private AutowireCapableBeanFactory beanFactory;
	@Autowired
	private CriteriaResourceMatcher myCriteriaResourceMatcher;

	@Override
	public SubscriptionMatchResult match(String criteria, ResourceModifiedMessage msg) {
		return match(criteria, msg.getNewPayload(myContext));
	}

	SubscriptionMatchResult match(String criteria, IBaseResource resource) {
		ResourceTable entity = new ResourceTable();
		String resourceType = myContext.getResourceDefinition(resource).getName();
		entity.setResourceType(resourceType);
		ResourceIndexedSearchParams searchParams = beanFactory.getBean(ResourceIndexedSearchParams.class);
		searchParams.extractFromResource(entity, resource);
		searchParams.extractInlineReferences(resource);
		searchParams.extractResourceLinks(entity, resource, resource.getMeta().getLastUpdated(), false);
		RuntimeResourceDefinition resourceDefinition = myContext.getResourceDefinition(resource);
		return myCriteriaResourceMatcher.match(criteria, resourceDefinition, searchParams);
	}
}
