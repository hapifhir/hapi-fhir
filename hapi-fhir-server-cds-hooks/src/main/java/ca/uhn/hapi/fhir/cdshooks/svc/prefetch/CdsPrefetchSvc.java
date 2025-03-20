/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestJson;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.hapi.fhir.cdshooks.api.CdsPrefetchFailureMode;
import ca.uhn.hapi.fhir.cdshooks.api.CdsResolutionStrategyEnum;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsHooksDaoAuthorizationSvc;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsServiceMethod;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.prefetch.CdsHookPrefetchPointcutContextJson;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CdsPrefetchSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(CdsPrefetchSvc.class);
	private final CdsResolutionStrategySvc myCdsResolutionStrategySvc;
	private final CdsPrefetchDaoSvc myResourcePrefetchDao;
	private final CdsPrefetchFhirClientSvc myResourcePrefetchFhirClient;
	private final ICdsHooksDaoAuthorizationSvc myCdsHooksDaoAuthorizationSvc;
	private final FhirContext myFhirContext;

	@Nullable
	private final IInterceptorBroadcaster myInterceptorBroadcaster;

	public CdsPrefetchSvc(
			CdsResolutionStrategySvc theCdsResolutionStrategySvc,
			CdsPrefetchDaoSvc theResourcePrefetchDao,
			CdsPrefetchFhirClientSvc theResourcePrefetchFhirClient,
			ICdsHooksDaoAuthorizationSvc theCdsHooksDaoAuthorizationSvc,
			FhirContext theFhirContext,
			@Nullable IInterceptorBroadcaster theInterceptorBroadcaster) {
		myCdsResolutionStrategySvc = theCdsResolutionStrategySvc;
		myResourcePrefetchDao = theResourcePrefetchDao;
		myResourcePrefetchFhirClient = theResourcePrefetchFhirClient;
		myCdsHooksDaoAuthorizationSvc = theCdsHooksDaoAuthorizationSvc;
		myFhirContext = theFhirContext;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	public void augmentRequest(CdsServiceRequestJson theCdsServiceRequestJson, ICdsServiceMethod theServiceMethod) {
		CdsServiceJson serviceSpec = theServiceMethod.getCdsServiceJson();
		Set<String> missingPrefetch = findMissingPrefetch(serviceSpec, theCdsServiceRequestJson);
		// It is important to call handleOperationOutcomesSentByCdsClient after calculating the missing prefetches.
		// In case the failure mode is OMIT, and if the client sends an OperationOutcome as a prefetch key, that
		// prefetch will be cleared by handleOperationOutcomesSentByCdsClient, which may be wrongly interpreted as
		// missing.
		handleOperationOutcomesSentByCdsClient(serviceSpec, theCdsServiceRequestJson);
		if (missingPrefetch.isEmpty()) {
			return;
		}
		Set<CdsResolutionStrategyEnum> strategies =
				myCdsResolutionStrategySvc.determineResolutionStrategy(theServiceMethod, theCdsServiceRequestJson);
		String serviceId = theServiceMethod.getCdsServiceJson().getId();
		try {
			fetchMissingPrefetchElements(theCdsServiceRequestJson, serviceSpec, missingPrefetch, strategies);
		} catch (BaseServerResponseException e) {
			// Per the CDS Hooks specification
			throw new PreconditionFailedException(Msg.code(2385) + "Unable to fetch missing resource(s) with key(s) "
					+ missingPrefetch + " for CDS Hooks service " + serviceId + ": " + e.getMessage());
		}
	}

	/**
	 * This method checks if the CDS client sent an OperationOutcome as a prefetch resource.
	 * If that is the case, it handles the OperationOutcome according to the failure mode for that prefetch key.
	 * If the failure mode is FAIL, it throws a PreconditionFailedException.
	 * If the failure mode is OMIT, it removes the prefetch key from the request.
	 * If the failure mode is OPERATION_OUTCOME, it keeps the OperationOutcome in the prefetch.
	 * @param theServiceSpec the definition of the CDS service
	 * @param theCdsServiceRequestJson the CDS request
	 */
	private void handleOperationOutcomesSentByCdsClient(
			CdsServiceJson theServiceSpec, CdsServiceRequestJson theCdsServiceRequestJson) {
		Set<String> prefetchKeysToRemove = new HashSet<>();
		String serviceId = theServiceSpec.getId();
		for (String prefetchKey : theCdsServiceRequestJson.getPrefetchKeys()) {
			IBaseResource resource = theCdsServiceRequestJson.getPrefetch(prefetchKey);
			CdsPrefetchFailureMode failureMode = theServiceSpec.getPrefetchFailureMode(prefetchKey);
			if (resource instanceof IBaseOperationOutcome) {
				if (failureMode == CdsPrefetchFailureMode.FAIL) {
					String message = String.format(
							"The CDS service '%s' received an OperationOutcome resource for prefetch key '%s' "
									+ "but the service isn't configured to handle OperationOutcome",
							serviceId, prefetchKey);
					throw new PreconditionFailedException(Msg.code(2635) + message);
				} else if (failureMode == CdsPrefetchFailureMode.OMIT) {
					ourLog.info(
							"The CDS service '{}' received an OperationOutcome for the prefetch key '{}' and the failure mode is set to 'OMIT'. "
									+ "The prefetch key will be removed from the request.",
							serviceId,
							prefetchKey);
					prefetchKeysToRemove.add(prefetchKey);
				}
				// else the failure mode is OPERATION_OUTCOME, so we keep the OperationOutcome in the prefetch
			}
		}

		for (String prefetchKey : prefetchKeysToRemove) {
			theCdsServiceRequestJson.removePrefetch(prefetchKey);
		}
	}

	private void fetchMissingPrefetchElements(
			CdsServiceRequestJson theCdsServiceRequestJson,
			CdsServiceJson theServiceSpec,
			Set<String> theMissingPrefetch,
			Set<CdsResolutionStrategyEnum> theStrategies) {

		for (String key : theMissingPrefetch) {
			String template = theServiceSpec.getPrefetch().get(key);
			CdsResolutionStrategyEnum source = theServiceSpec.getSource().get(key);
			CdsPrefetchFailureMode failureMode = theServiceSpec.getPrefetchFailureMode(key);
			if (!theStrategies.contains(source)) {
				throw new PreconditionFailedException(
						Msg.code(2386) + "Unable to fetch missing resource(s) with source " + source);
			}
			if (source == CdsResolutionStrategyEnum.NONE) {
				if (theStrategies.contains(CdsResolutionStrategyEnum.FHIR_CLIENT)) {
					source = CdsResolutionStrategyEnum.FHIR_CLIENT;
				} else if (theStrategies.contains(CdsResolutionStrategyEnum.SERVICE)) {
					source = CdsResolutionStrategyEnum.SERVICE;
				} else if (theStrategies.contains(CdsResolutionStrategyEnum.DAO)) {
					source = CdsResolutionStrategyEnum.DAO;
				} else {
					// Per the CDS Hooks specification
					throw new PreconditionFailedException(
							Msg.code(2387) + "Unable to fetch missing resource(s) with source " + source);
				}
			}

			if (source == CdsResolutionStrategyEnum.SERVICE) {
				// The service will manage missing prefetch elements
				continue;
			}
			String url = PrefetchTemplateUtil.substituteTemplate(
					template, theCdsServiceRequestJson.getContext(), myResourcePrefetchDao.getFhirContext());
			ourLog.info("missing: {}.  Fetching with {}", theMissingPrefetch, url);
			IBaseResource resource;

			CdsHookPrefetchPointcutContextJson cdsHookPrefetchContext = new CdsHookPrefetchPointcutContextJson();
			cdsHookPrefetchContext.setTemplate(template);
			cdsHookPrefetchContext.setQuery(url);
			cdsHookPrefetchContext.setCdsResolutionStrategy(source);

			callCdsPrefetchRequestHooks(cdsHookPrefetchContext, theCdsServiceRequestJson);

			try {
				resource = prefetchResource(theCdsServiceRequestJson, key, url, source, failureMode);
			} catch (Exception e) {
				callCdsPrefetchFailedHooks(cdsHookPrefetchContext, theCdsServiceRequestJson, e);
				throw e;
			}

			// if the prefetch failed and the failure mode is OMIT, then the resource would be null,
			// ensure it is not null before calling the methods that require it
			if (resource != null) {
				callCdsPrefetchResponseHooks(cdsHookPrefetchContext, theCdsServiceRequestJson, resource);
				theCdsServiceRequestJson.addPrefetch(key, resource);
			}
		}
	}

	@Nullable
	private IBaseResource prefetchResource(
			CdsServiceRequestJson theCdsServiceRequestJson,
			String thePrefetchKey,
			String theUrl,
			CdsResolutionStrategyEnum theStrategy,
			CdsPrefetchFailureMode theFailureMode) {
		IBaseResource resource;
		try {
			if (theStrategy == CdsResolutionStrategyEnum.FHIR_CLIENT) {
				resource = myResourcePrefetchFhirClient.resourceFromUrl(theCdsServiceRequestJson, theUrl);
			} else if (theStrategy == CdsResolutionStrategyEnum.DAO) {
				resource = getResourceFromDaoWithPermissionCheck(theUrl);
			} else {
				throw new IllegalStateException("Unexpected strategy: " + theStrategy);
			}
		} catch (Exception e) {
			// create a switch to handle the failure mode
			switch (theFailureMode) {
				case OMIT:
					ourLog.info(
							"The prefetch failed for the prefetch key '{}', with the exception message: '{}'. "
									+ "The failure mode for the prefetch key is set to 'OMIT'. The prefetch key will be omitted from the request.",
							thePrefetchKey,
							e.getMessage());
					resource = null;
					break;
				case OPERATION_OUTCOME:
					resource = extractOrCreateOperationOutcomeFromException(e);
					break;
				case FAIL:
				default:
					throw e;
			}
		}
		return resource;
	}

	private IBaseOperationOutcome extractOrCreateOperationOutcomeFromException(Exception e) {
		IBaseOperationOutcome oo = null;
		// check first if we can get the OperationOutcome from the exception
		if (e instanceof BaseServerResponseException) {
			BaseServerResponseException serverException = (BaseServerResponseException) e;
			oo = serverException.getOperationOutcome();
		}

		if (oo == null) {
			// the exception doesn't contain an OperationOutcome, create one
			oo = OperationOutcomeUtil.newInstance(myFhirContext);
			OperationOutcomeUtil.addIssue(myFhirContext, oo, "error", e.getMessage(), null, "exception");
		}
		return oo;
	}

	private IBaseResource getResourceFromDaoWithPermissionCheck(String theUrl) {
		IBaseResource resource;
		resource = myResourcePrefetchDao.resourceFromUrl(theUrl);
		myCdsHooksDaoAuthorizationSvc.authorizePreShow(resource);
		return resource;
	}

	public Set<String> findMissingPrefetch(
			CdsServiceJson theServiceSpec, CdsServiceRequestJson theCdsServiceRequestJson) {
		Set<String> expectedPrefetchKeys = theServiceSpec.getPrefetch().keySet();
		Set<String> actualPrefetchKeys = theCdsServiceRequestJson.getPrefetchKeys();
		Set<String> retval = new HashSet<>(expectedPrefetchKeys);
		retval.removeAll(actualPrefetchKeys);
		return retval;
	}

	private void callCdsPrefetchRequestHooks(
			CdsHookPrefetchPointcutContextJson theCdsHookPrefetchContext,
			CdsServiceRequestJson theCdsServiceRequestJson) {
		if (myInterceptorBroadcaster != null && myInterceptorBroadcaster.hasHooks(Pointcut.CDS_HOOK_PREFETCH_REQUEST)) {
			HookParams params = new HookParams();
			params.add(CdsHookPrefetchPointcutContextJson.class, theCdsHookPrefetchContext);
			params.add(CdsServiceRequestJson.class, theCdsServiceRequestJson);
			myInterceptorBroadcaster.callHooks(Pointcut.CDS_HOOK_PREFETCH_REQUEST, params);
		}
	}

	private void callCdsPrefetchResponseHooks(
			CdsHookPrefetchPointcutContextJson theCdsHookPrefetchContext,
			CdsServiceRequestJson theCdsServiceRequestJson,
			IBaseResource theResource) {
		if (myInterceptorBroadcaster != null
				&& myInterceptorBroadcaster.hasHooks(Pointcut.CDS_HOOK_PREFETCH_RESPONSE)) {
			HookParams params = new HookParams();
			params.add(CdsHookPrefetchPointcutContextJson.class, theCdsHookPrefetchContext);
			params.add(CdsServiceRequestJson.class, theCdsServiceRequestJson);
			params.add(IBaseResource.class, theResource);

			myInterceptorBroadcaster.callHooks(Pointcut.CDS_HOOK_PREFETCH_RESPONSE, params);
		}
	}

	private void callCdsPrefetchFailedHooks(
			CdsHookPrefetchPointcutContextJson theCdsHookPrefetchContext,
			CdsServiceRequestJson theCdsServiceRequestJson,
			Exception theException) {
		if (myInterceptorBroadcaster != null && myInterceptorBroadcaster.hasHooks(Pointcut.CDS_HOOK_PREFETCH_FAILED)) {
			HookParams params = new HookParams();
			params.add(CdsHookPrefetchPointcutContextJson.class, theCdsHookPrefetchContext);
			params.add(CdsServiceRequestJson.class, theCdsServiceRequestJson);
			params.add(Exception.class, theException);

			myInterceptorBroadcaster.callHooks(Pointcut.CDS_HOOK_PREFETCH_FAILED, params);
		}
	}
}
