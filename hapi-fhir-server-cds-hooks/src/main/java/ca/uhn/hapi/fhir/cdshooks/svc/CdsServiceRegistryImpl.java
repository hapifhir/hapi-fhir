/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsMethod;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsServiceMethod;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsServiceRegistry;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceFeedbackJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServicesJson;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsPrefetchSvc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;

public class CdsServiceRegistryImpl implements ICdsServiceRegistry {
	private static final Logger ourLog = LoggerFactory.getLogger(CdsServiceRegistryImpl.class);

	private CdsServiceCache myServiceCache;

	private final CdsHooksContextBooter myCdsHooksContextBooter;
	private final CdsPrefetchSvc myCdsPrefetchSvc;
	private final ObjectMapper myObjectMapper;

	public CdsServiceRegistryImpl(
			CdsHooksContextBooter theCdsHooksContextBooter,
			CdsPrefetchSvc theCdsPrefetchSvc,
			ObjectMapper theObjectMapper) {
		myCdsHooksContextBooter = theCdsHooksContextBooter;
		myCdsPrefetchSvc = theCdsPrefetchSvc;
		myObjectMapper = theObjectMapper;
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

	private CdsServiceResponseJson encodeServiceResponse(String theServiceId, Object result) {
		String json;
		if (result instanceof String) {
			json = (String) result;
		} else {
			try {
				json = myObjectMapper.writeValueAsString(result);
			} catch (JsonProcessingException e) {
				throw new ConfigurationException(
						Msg.code(2389) + "Failed to json serialize Cds service response of type "
								+ result.getClass().getName() + " when calling CDS Hook Service " + theServiceId,
						e);
			}
		}
		try {
			return myObjectMapper.readValue(json, CdsServiceResponseJson.class);
		} catch (JsonProcessingException e) {
			throw new ConfigurationException(
					Msg.code(2390) + "Failed to json deserialize Cds service response of type "
							+ result.getClass().getName() + " when calling CDS Hook Service " + theServiceId
							+ ".  Json: " + json,
					e);
		}
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
	private ICdsMethod getCdsFeedbackMethodOrThrowException(String theId) {
		ICdsMethod retval = myServiceCache.getFeedbackMethod(theId);
		if (retval == null) {
			throw new ResourceNotFoundException(
					Msg.code(2392) + "No feedback service with id " + theId + " is registered on this server");
		}
		return retval;
	}

	@Override
	public String callFeedback(String theServiceId, CdsServiceFeedbackJson theCdsServiceFeedbackJson) {
		ICdsMethod feedbackMethod = getCdsFeedbackMethodOrThrowException(theServiceId);
		Object response = feedbackMethod.invoke(myObjectMapper, theCdsServiceFeedbackJson, theServiceId);

		return encodeFeedbackResponse(theServiceId, theCdsServiceFeedbackJson, response);
	}

	@Override
	public void registerService(
			String theServiceId,
			Function<CdsServiceRequestJson, CdsServiceResponseJson> theServiceFunction,
			CdsServiceJson theCdsServiceJson,
			boolean theAllowAutoFhirClientPrefetch,
			String theModuleId) {
		myServiceCache.registerDynamicService(
				theServiceId, theServiceFunction, theCdsServiceJson, theAllowAutoFhirClientPrefetch, theModuleId);
	}

	@Override
	public void unregisterService(String theServiceId, String theModuleId) {
		Validate.notNull(theServiceId);

		ICdsMethod activeService = myServiceCache.unregisterServiceMethod(theServiceId, theModuleId);
		if (activeService != null) {
			ourLog.info("Unregistered active service {}", theServiceId);
		}
	}

	private String encodeFeedbackResponse(
			String theServiceId, CdsServiceFeedbackJson theCdsServiceFeedbackJson, Object response) {
		if (response instanceof String) {
			return (String) response;
		} else {
			try {
				// Try to json encode the response
				return myObjectMapper.writeValueAsString(response);
			} catch (JsonProcessingException e) {
				try {
					ourLog.warn("Failed to deserialize response from {} feedback method", theServiceId, e);
					// Just send back what we received
					return myObjectMapper.writeValueAsString(theCdsServiceFeedbackJson);
				} catch (JsonProcessingException f) {
					ourLog.error("Failed to deserialize request parameter to {} feedback method", theServiceId, e);
					// Okay then...
					return "{}";
				}
			}
		}
	}
}
