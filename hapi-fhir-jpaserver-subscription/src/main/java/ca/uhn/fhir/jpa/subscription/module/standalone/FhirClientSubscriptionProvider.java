package ca.uhn.fhir.jpa.subscription.module.standalone;

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
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.module.cache.ISubscriptionProvider;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionRegistry;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FhirClientSubscriptionProvider implements ISubscriptionProvider {
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;

	IGenericClient myClient;

	@Autowired
	public FhirClientSubscriptionProvider(IGenericClient theClient) {
		myClient = theClient;
	}

	@Override
	public IBundleProvider search(SearchParameterMap theMap) {
		FhirContext fhirContext = myClient.getFhirContext();

		String searchURL = ResourceTypeEnum.SUBSCRIPTION.getCode() + theMap.toNormalizedQueryString(myFhirContext);

		IBaseBundle bundle = myClient
			.search()
			.byUrl(searchURL)
			.cacheControl(new CacheControlDirective().setNoCache(true))
			.execute();

		return new SimpleBundleProvider(BundleUtil.toListOfResources(fhirContext, bundle));
	}

	@Override
	public boolean loadSubscription(IBaseResource theResource) {
		return mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(theResource);
	}
}
