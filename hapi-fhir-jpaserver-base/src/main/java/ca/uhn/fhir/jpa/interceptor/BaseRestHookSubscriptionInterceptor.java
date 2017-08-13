package ca.uhn.fhir.jpa.interceptor;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.provider.ServletSubRequestDetails;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

public abstract class BaseRestHookSubscriptionInterceptor extends ServerOperationInterceptorAdapter {
	protected static final Integer MAX_SUBSCRIPTION_RESULTS = 10000;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseRestHookSubscriptionInterceptor.class);
	protected ExecutorService myExecutor;
	private int myExecutorThreadCount = 1;

	protected abstract IFhirResourceDao<?> getSubscriptionDao();

	protected void checkSubscriptionCriterias(String theCriteria) {
		try {
			IBundleProvider results = executeSubscriptionCriteria(theCriteria, null);
		} catch (Exception e) {
			ourLog.warn("Invalid criteria when creating subscription", e);
			throw new InvalidRequestException("Invalid criteria: " + e.getMessage());
		}
	}

	@PostConstruct
	public void postConstruct() {
		try {
			myExecutor = new ThreadPoolExecutor(myExecutorThreadCount, myExecutorThreadCount,
				0L, TimeUnit.MILLISECONDS,	new LinkedBlockingQueue<Runnable>(1000));

			myExecutor = Executors.newFixedThreadPool(myExecutorThreadCount);
		} catch (Exception e) {
			throw new RuntimeException("Unable to get DAO from PROXY");
		}
	}

	private IBundleProvider executeSubscriptionCriteria(String theCriteria, IIdType idType) {
		String criteria = theCriteria;

		/*
		 * Run the subscriptions query and look for matches, add the id as part of the criteria 
		 * to avoid getting matches of previous resources rather than the recent resource
		 */
		if (idType != null) {
			criteria += "&_id=" + idType.getResourceType() + "/" + idType.getIdPart();
		}

		IBundleProvider results = getBundleProvider(criteria, true);
		return results;
	}

	/**
	 * Search based on a query criteria
	 *
	 * @param theCheckOnly Is this just a test that the search works
	 */
	protected IBundleProvider getBundleProvider(String theCriteria, boolean theCheckOnly) {
		RuntimeResourceDefinition responseResourceDef = getSubscriptionDao().validateCriteriaAndReturnResourceDefinition(theCriteria);
		SearchParameterMap responseCriteriaUrl = BaseHapiFhirDao.translateMatchUrl(getSubscriptionDao(), getSubscriptionDao().getContext(), theCriteria, responseResourceDef);

		RequestDetails req = new ServletSubRequestDetails();
		req.setSubRequest(true);

		IFhirResourceDao<? extends IBaseResource> responseDao = getSubscriptionDao().getDao(responseResourceDef.getImplementingClass());

		if (theCheckOnly) {
			responseCriteriaUrl.setLoadSynchronousUpTo(1);
		} else {
			responseCriteriaUrl.setLoadSynchronousUpTo(MAX_SUBSCRIPTION_RESULTS);
		}

		IBundleProvider responseResults = responseDao.search(responseCriteriaUrl, req);
		return responseResults;
	}

}
