/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.cdshooks.svc;

import ca.uhn.hapi.fhir.cdshooks.api.ICdsMethod;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsServiceMethod;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServicesJson;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.ICdsCrServiceFactory;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery.ICrDiscoveryServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.CDS_CR_MODULE_ID;

public class CdsServiceCache {
	static final Logger ourLog = LoggerFactory.getLogger(CdsServiceCache.class);
	final Map<String, ICdsMethod> myServiceMap = new LinkedHashMap<>();
	final Map<String, ICdsMethod> myFeedbackMap = new LinkedHashMap<>();
	final CdsServicesJson myCdsServiceJson = new CdsServicesJson();

	public void registerService(
			String theServiceId,
			Object theServiceBean,
			Method theMethod,
			CdsServiceJson theCdsServiceJson,
			boolean theAllowAutoFhirClientPrefetch) {
		final CdsServiceMethod cdsServiceMethod =
				new CdsServiceMethod(theCdsServiceJson, theServiceBean, theMethod, theAllowAutoFhirClientPrefetch);
		myServiceMap.put(theServiceId, cdsServiceMethod);
		myCdsServiceJson.addService(theCdsServiceJson);
	}

	public void registerDynamicService(
			String theServiceId,
			Function<CdsServiceRequestJson, CdsServiceResponseJson> theMethod,
			CdsServiceJson theCdsServiceJson,
			boolean theAllowAutoFhirClientPrefetch,
			String theModuleId) {
		if (!isCdsServiceAlreadyRegistered(theServiceId, theModuleId)) {
			final CdsDynamicPrefetchableServiceMethod cdsDynamicPrefetchableServiceMethod =
					new CdsDynamicPrefetchableServiceMethod(
							theCdsServiceJson, theMethod, theAllowAutoFhirClientPrefetch);
			myServiceMap.put(theServiceId, cdsDynamicPrefetchableServiceMethod);
			myCdsServiceJson.addService(theCdsServiceJson);
		}
	}

	public void registerCrService(
			String theServiceId,
			ICrDiscoveryServiceFactory theDiscoveryServiceFactory,
			ICdsCrServiceFactory theCrServiceFactory) {
		if (!isCdsServiceAlreadyRegistered(theServiceId, CDS_CR_MODULE_ID)) {
			CdsServiceJson cdsServiceJson =
					theDiscoveryServiceFactory.create(theServiceId).resolveService();
			if (cdsServiceJson != null) {
				final CdsCrServiceMethod cdsCrServiceMethod =
						new CdsCrServiceMethod(cdsServiceJson, theCrServiceFactory);
				myServiceMap.put(theServiceId, cdsCrServiceMethod);
				myCdsServiceJson.addService(cdsServiceJson);
				ourLog.info("Created service for {}", theServiceId);
			}
		}
	}

	public void registerFeedback(String theServiceId, Object theServiceBean, Method theMethod) {
		final CdsFeedbackMethod cdsFeedbackMethod = new CdsFeedbackMethod(theServiceBean, theMethod);
		myFeedbackMap.put(theServiceId, cdsFeedbackMethod);
	}

	public ICdsMethod getServiceMethod(String theId) {
		return myServiceMap.get(theId);
	}

	public ICdsMethod getFeedbackMethod(String theId) {
		return myFeedbackMap.get(theId);
	}

	public CdsServicesJson getCdsServicesJson() {
		return myCdsServiceJson;
	}

	public ICdsMethod unregisterServiceMethod(String theServiceId, String theModuleId) {
		if (myServiceMap.containsKey(theServiceId)) {
			final ICdsMethod serviceMethod = myServiceMap.get(theServiceId);
			myServiceMap.remove(theServiceId);
			if (serviceMethod instanceof ICdsServiceMethod) {
				myCdsServiceJson.removeService(((ICdsServiceMethod) serviceMethod).getCdsServiceJson());
			}
			return serviceMethod;
		} else {
			ourLog.error(
					"CDS service with serviceId: {} for moduleId: {}, is not registered. Nothing to remove!",
					theServiceId,
					theModuleId);
			return null;
		}
	}

	private boolean isCdsServiceAlreadyRegistered(String theServiceId, String theModuleId) {
		boolean result = myServiceMap.containsKey(theServiceId);
		if (result) {
			ourLog.error(
					"CDS service with serviceId: {} for moduleId: {}, already exists. It will not be overwritten!",
					theServiceId,
					theModuleId);
		}
		return result;
	}

	CdsServiceJson getCdsServiceJson(String theString) {
		return myCdsServiceJson.getServices().stream()
				.filter(x -> x.getId().equals(theString))
				.findFirst()
				.orElse(null);
	}
}
