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
import ca.uhn.fhir.jpa.searchparam.registry.BaseSearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FhirClientSearchParamProvider implements ISearchParamProvider {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirClientSearchParamProvider.class);

	private IGenericClient myClient;

	@Autowired
	public FhirClientSearchParamProvider(IGenericClient theClient) {
		myClient = theClient;
	}

	@Override
	public IBundleProvider search(SearchParameterMap theParams) {
		FhirContext fhirContext = myClient.getFhirContext();

		IBaseBundle bundle = myClient
			.search()
			.forResource(ResourceTypeEnum.SEARCHPARAMETER.getCode())
			.cacheControl(new CacheControlDirective().setNoCache(true))
			.execute();

		return new SimpleBundleProvider(BundleUtil.toListOfResources(fhirContext, bundle));
	}

	@Override
	public <SP extends IBaseResource> int refreshCache(BaseSearchParamRegistry<SP> theSearchParamRegistry, long theRefreshInterval) {
		return theSearchParamRegistry.doRefresh(theRefreshInterval);
	}
}
