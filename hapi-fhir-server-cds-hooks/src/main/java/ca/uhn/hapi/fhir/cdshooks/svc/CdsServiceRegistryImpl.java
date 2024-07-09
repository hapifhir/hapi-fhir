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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsMethod;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsServiceMethod;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsServiceRegistry;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsHooksExtension;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceFeedbackJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServicesJson;
import ca.uhn.hapi.fhir.cdshooks.serializer.CdsServiceRequestJsonDeserializer;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.ICdsCrServiceFactory;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery.ICrDiscoveryServiceFactory;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsPrefetchSvc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class CdsServiceRegistryImpl implements ICdsServiceRegistry {
	private static final Logger ourLog = LoggerFactory.getLogger(CdsServiceRegistryImpl.class);

	private CdsServiceCache myServiceCache;

	private final CdsHooksContextBooter myCdsHooksContextBooter;
	private final CdsPrefetchSvc myCdsPrefetchSvc;
	private final ObjectMapper myObjectMapper;
	private final ICdsCrServiceFactory myCdsCrServiceFactory;
	private final ICrDiscoveryServiceFactory myCrDiscoveryServiceFactory;

	public CdsServiceRegistryImpl(
			CdsHooksContextBooter theCdsHooksContextBooter,
			CdsPrefetchSvc theCdsPrefetchSvc,
			ObjectMapper theObjectMapper,
			ICdsCrServiceFactory theCdsCrServiceFactory,
			ICrDiscoveryServiceFactory theCrDiscoveryServiceFactory,
			FhirContext theFhirContext) {
		myCdsHooksContextBooter = theCdsHooksContextBooter;
		myCdsPrefetchSvc = theCdsPrefetchSvc;
		myObjectMapper = theObjectMapper;
		// registering this deserializer here instead of
		// CdsHooksObjectMapperFactory to avoid circular
		// dependency
		SimpleModule module = new SimpleModule();
		module.addDeserializer(
				CdsServiceRequestJson.class, new CdsServiceRequestJsonDeserializer(this, theFhirContext));
		myObjectMapper.registerModule(module);
		myCdsCrServiceFactory = theCdsCrServiceFactory;
		myCrDiscoveryServiceFactory = theCrDiscoveryServiceFactory;
	}

	@PostConstruct
	public void init() {
		myServiceCache = myCdsHooksContextBooter.buildCdsServiceCache();
	}

	@Override
	public CdsServicesJson getCdsServicesJson() {
		return myServiceCache.getCdsServicesJson();
	}

	@Override
	public CdsServiceResponseJson callService(String theServiceId, CdsServiceRequestJson theCdsServiceRequestJson) {
		ICdsServiceMethod serviceMethod = (ICdsServiceMethod) getCdsServiceMethodOrThrowException(theServiceId);
		myCdsPrefetchSvc.augmentRequest(theCdsServiceRequestJson, serviceMethod);
		Object response = serviceMethod.invoke(myObjectMapper, theCdsServiceRequestJson, theServiceId);
		return encodeServiceResponse(theServiceId, response);
	}

	@Override
	public CdsServiceFeedbackJson callFeedback(String theServiceId, CdsServiceFeedbackJson theCdsServiceFeedbackJson) {
		ICdsMethod feedbackMethod = getCdsFeedbackMethodOrThrowException(theServiceId);
		Object response = feedbackMethod.invoke(myObjectMapper, theCdsServiceFeedbackJson, theServiceId);
		return encodeFeedbackResponse(theServiceId, response);
	}

	@Override
	public void registerService(
			String theServiceId,
			Function<CdsServiceRequestJson, CdsServiceResponseJson> theServiceFunction,
			CdsServiceJson theCdsServiceJson,
			boolean theAllowAutoFhirClientPrefetch,
			String theModuleId) {
		if (theCdsServiceJson.getExtensionClass() == null) {
			theCdsServiceJson.setExtensionClass(CdsHooksExtension.class);
		}
		myServiceCache.registerDynamicService(
				theServiceId, theServiceFunction, theCdsServiceJson, theAllowAutoFhirClientPrefetch, theModuleId);
	}

	@Override
	public boolean registerCrService(String theServiceId) {
		try {
			myServiceCache.registerCrService(theServiceId, myCrDiscoveryServiceFactory, myCdsCrServiceFactory);
		} catch (Exception e) {
			ourLog.error("Error received during CR CDS Service registration: {}", e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public void unregisterService(String theServiceId, String theModuleId) {
		Validate.notNull(theServiceId);

		ICdsMethod activeService = myServiceCache.unregisterServiceMethod(theServiceId, theModuleId);
		if (activeService != null) {
			ourLog.info("Unregistered active service {}", theServiceId);
		}
	}

	@Override
	public CdsServiceJson getCdsServiceJson(String theServiceId) {
		CdsServiceJson cdsServiceJson = myServiceCache.getCdsServiceJson(theServiceId);
		if (cdsServiceJson == null) {
			throw new IllegalArgumentException(Msg.code(2536) + "No service with " + theServiceId + " is registered.");
		}
		return cdsServiceJson;
	}

	@Nonnull
	private ICdsMethod getCdsServiceMethodOrThrowException(String theId) {
		ICdsMethod retval = myServiceCache.getServiceMethod(theId);
		if (retval == null) {
			throw new ResourceNotFoundException(
					Msg.code(2391) + "No service with id " + theId + " is registered on this server");
		}
		return retval;
	}

	@Nonnull
	CdsServiceResponseJson encodeServiceResponse(String theServiceId, Object result) {
		if (result instanceof String) {
			return buildResponseFromString(theServiceId, result, (String) result);
		} else {
			return buildResponseFromImplementation(theServiceId, result);
		}
	}

	@Nonnull
	private ICdsMethod getCdsFeedbackMethodOrThrowException(String theId) {
		ICdsMethod retval = myServiceCache.getFeedbackMethod(theId);
		if (retval == null) {
			throw new ResourceNotFoundException(
					Msg.code(2392) + "No feedback service with id " + theId + " is registered on this server");
		}
		return retval;
	}

	@Nonnull
	CdsServiceFeedbackJson encodeFeedbackResponse(String theServiceId, Object theResponse) {
		if (theResponse instanceof String) {
			return buildFeedbackFromString(theServiceId, (String) theResponse);
		} else {
			return buildFeedbackFromImplementation(theServiceId, theResponse);
		}
	}

	private CdsServiceResponseJson buildResponseFromImplementation(String theServiceId, Object theResult) {
		try {
			return (CdsServiceResponseJson) theResult;
		} catch (ClassCastException e) {
			throw new ConfigurationException(
					Msg.code(2389)
							+ "Failed to cast Cds service response to CdsServiceResponseJson when calling CDS Hook Service "
							+ theServiceId + ". The type "
							+ theResult.getClass().getName()
							+ " cannot be casted to CdsServiceResponseJson",
					e);
		}
	}

	private CdsServiceResponseJson buildResponseFromString(String theServiceId, Object theResult, String theJson) {
		try {
			return myObjectMapper.readValue(theJson, CdsServiceResponseJson.class);
		} catch (JsonProcessingException e) {
			throw new ConfigurationException(
					Msg.code(2390) + "Failed to json deserialize Cds service response of type "
							+ theResult.getClass().getName() + " when calling CDS Hook Service " + theServiceId
							+ ".  Json: " + theJson,
					e);
		}
	}

	private CdsServiceFeedbackJson buildFeedbackFromImplementation(String theServiceId, Object theResponse) {
		try {
			return (CdsServiceFeedbackJson) theResponse;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					Msg.code(2537) + "Failed to cast feedback response CdsServiceFeedbackJson for service "
							+ theServiceId + ". " + e.getMessage());
		}
	}

	private CdsServiceFeedbackJson buildFeedbackFromString(String theServiceId, String theResponse) {
		try {
			return myObjectMapper.readValue(theResponse, CdsServiceFeedbackJson.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(Msg.code(2538) + "Failed to serialize json Cds Feedback response for service "
					+ theServiceId + ". " + e.getMessage());
		}
	}

	@VisibleForTesting
	void setServiceCache(CdsServiceCache theCdsServiceCache) {
		myServiceCache = theCdsServiceCache;
	}
}
