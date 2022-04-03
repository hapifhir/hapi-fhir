package ca.uhn.fhir.jpa.subscription.match.matcher.matching;

/*-
 * #%L
 * HAPI FHIR Subscription Server
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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.util.SubscriptionUtil;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DaoSubscriptionMatcher implements ISubscriptionMatcher {
	private final Logger ourLog = LoggerFactory.getLogger(DaoSubscriptionMatcher.class);

	@Autowired
	DaoRegistry myDaoRegistry;

	@Autowired
	MatchUrlService myMatchUrlService;

	@Autowired
	private FhirContext myCtx;

	@Override
	public InMemoryMatchResult match(CanonicalSubscription theSubscription, ResourceModifiedMessage theMsg) {
		IIdType id = theMsg.getPayloadId(myCtx);
		String criteria = theSubscription.getCriteriaString();

		// Run the subscriptions query and look for matches, add the id as part of the criteria to avoid getting matches of previous resources rather than the recent resource
		criteria += "&_id=" + id.toUnqualifiedVersionless().getValue();

		IBundleProvider results = performSearch(criteria, theSubscription);

		ourLog.debug("Subscription check found {} results for query: {}", results.size(), criteria);

		return InMemoryMatchResult.fromBoolean(results.size() > 0);
	}

	/**
	 * Search based on a query criteria
	 */
	private IBundleProvider performSearch(String theCriteria, CanonicalSubscription theSubscription) {
		IFhirResourceDao<?> subscriptionDao = myDaoRegistry.getSubscriptionDao();
		RuntimeResourceDefinition responseResourceDef = subscriptionDao.validateCriteriaAndReturnResourceDefinition(theCriteria);
		SearchParameterMap responseCriteriaUrl = myMatchUrlService.translateMatchUrl(theCriteria, responseResourceDef);

		IFhirResourceDao<? extends IBaseResource> responseDao = myDaoRegistry.getResourceDao(responseResourceDef.getImplementingClass());
		responseCriteriaUrl.setLoadSynchronousUpTo(1);

		return responseDao.search(responseCriteriaUrl, SubscriptionUtil.createRequestDetailForPartitionedRequest(theSubscription));
	}

}
