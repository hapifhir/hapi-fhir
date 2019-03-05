package ca.uhn.fhir.jpa.subscription.dbcache;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.provider.ServletSubRequestDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.SubscriptionActivatingInterceptor;
import ca.uhn.fhir.jpa.subscription.module.cache.ISubscriptionProvider;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DaoSubscriptionProvider implements ISubscriptionProvider {
	@Autowired
	DaoRegistry myDaoRegistry;
	@Autowired
	private SubscriptionActivatingInterceptor mySubscriptionActivatingInterceptor;

	@Override
	public IBundleProvider search(SearchParameterMap theMap) {
		IFhirResourceDao subscriptionDao = myDaoRegistry.getSubscriptionDao();
		return subscriptionDao.search(theMap);
	}

	@Override
	public boolean loadSubscription(IBaseResource theResource) {
		return mySubscriptionActivatingInterceptor.activateOrRegisterSubscriptionIfRequired(theResource);
	}
}
