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

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.provider.ServletSubRequestDetails;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionInterceptor;
import ca.uhn.fhir.jpa.subscription.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;

public class SubscriptionMatcherDatabase implements ISubscriptionMatcher {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionMatcherDatabase.class);

	private final IFhirResourceDao<?> mySubscriptionDao;
	
	private final BaseSubscriptionInterceptor mySubscriptionInterceptor;
	
	public SubscriptionMatcherDatabase(IFhirResourceDao<?> theSubscriptionDao, BaseSubscriptionInterceptor theSubscriptionInterceptor) {
		mySubscriptionDao = theSubscriptionDao;
		mySubscriptionInterceptor = theSubscriptionInterceptor;
	}
	
	@Override
	public boolean match(String criteria, ResourceModifiedMessage msg) {
		IIdType id = msg.getId(getContext());
		String resourceType = id.getResourceType();
		String resourceId = id.getIdPart();

		// run the subscriptions query and look for matches, add the id as part of the criteria to avoid getting matches of previous resources rather than the recent resource
		criteria += "&_id=" + resourceType + "/" + resourceId;

		IBundleProvider results = performSearch(criteria);

		ourLog.debug("Subscription check found {} results for query: {}", results.size(), criteria);

		return results.size() > 0;
	}
	
	/**
	 * Search based on a query criteria
	 */
	protected IBundleProvider performSearch(String theCriteria) {
		RuntimeResourceDefinition responseResourceDef = mySubscriptionDao.validateCriteriaAndReturnResourceDefinition(theCriteria);
		SearchParameterMap responseCriteriaUrl = BaseHapiFhirDao.translateMatchUrl(mySubscriptionDao, getContext(), theCriteria, responseResourceDef);

		RequestDetails req = new ServletSubRequestDetails();
		req.setSubRequest(true);

		IFhirResourceDao<? extends IBaseResource> responseDao = mySubscriptionInterceptor.getDao(responseResourceDef.getImplementingClass());
		responseCriteriaUrl.setLoadSynchronousUpTo(1);

		IBundleProvider responseResults = responseDao.search(responseCriteriaUrl, req);
		return responseResults;
	}
	
	public FhirContext getContext() {
		return mySubscriptionDao.getContext();
	}
}
