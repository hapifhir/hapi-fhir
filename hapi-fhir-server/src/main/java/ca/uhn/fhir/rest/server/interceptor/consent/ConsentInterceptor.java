package ca.uhn.fhir.rest.server.interceptor.consent;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationConstants;
import ca.uhn.fhir.rest.server.util.ICachedSearchDetails;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.IModelVisitor2;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.Constants.URL_TOKEN_METADATA;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_META;

/**
 * The ConsentInterceptor can be used to apply arbitrary consent rules and data access policies
 * on responses from a FHIR server.
 * <p>
 * See <a href="https://hapifhir.io/hapi-fhir/docs/security/consent_interceptor.html">Consent Interceptor</a> for
 * more information on this interceptor.
 * </p>
 */
@Interceptor(order = AuthorizationConstants.ORDER_CONSENT_INTERCEPTOR)
public class ConsentInterceptor {
	private static final AtomicInteger ourInstanceCount = new AtomicInteger(0);
	private final int myInstanceIndex = ourInstanceCount.incrementAndGet();
	private final String myRequestAuthorizedKey = ConsentInterceptor.class.getName() + "_" + myInstanceIndex + "_AUTHORIZED";
	private final String myRequestCompletedKey = ConsentInterceptor.class.getName() + "_" + myInstanceIndex + "_COMPLETED";
	private final String myRequestSeenResourcesKey = ConsentInterceptor.class.getName() + "_" + myInstanceIndex + "_SEENRESOURCES";

	private volatile List<IConsentService> myConsentService = Collections.emptyList();
	private IConsentContextServices myContextConsentServices = IConsentContextServices.NULL_IMPL;

	/**
	 * Constructor
	 */
	public ConsentInterceptor() {
		super();
	}

	/**
	 * Constructor
	 *
	 * @param theConsentService Must not be <code>null</code>
	 */
	public ConsentInterceptor(IConsentService theConsentService) {
		this(theConsentService, IConsentContextServices.NULL_IMPL);
	}

	/**
	 * Constructor
	 *
	 * @param theConsentService         Must not be <code>null</code>
	 * @param theContextConsentServices Must not be <code>null</code>
	 */
	public ConsentInterceptor(IConsentService theConsentService, IConsentContextServices theContextConsentServices) {
		setConsentService(theConsentService);
		setContextConsentServices(theContextConsentServices);
	}

	public void setContextConsentServices(IConsentContextServices theContextConsentServices) {
		Validate.notNull(theContextConsentServices, "theContextConsentServices must not be null");
		myContextConsentServices = theContextConsentServices;
	}

	/**
	 * @deprecated Use {@link #registerConsentService(IConsentService)} instead
	 */
	@Deprecated
	public void setConsentService(IConsentService theConsentService) {
		Validate.notNull(theConsentService, "theConsentService must not be null");
		myConsentService = Collections.singletonList(theConsentService);
	}

	/**
	 * Adds a consent service to the chain.
	 * <p>
	 * Thread safety note: This method can be called while the service is actively processing requestes
	 *
	 * @param theConsentService The service to register. Must not be <code>null</code>.
	 * @since 6.0.0
	 */
	public ConsentInterceptor registerConsentService(IConsentService theConsentService) {
		Validate.notNull(theConsentService, "theConsentService must not be null");
		List<IConsentService> newList = new ArrayList<>(myConsentService.size() + 1);
		newList.addAll(myConsentService);
		newList.add(theConsentService);
		myConsentService = newList;
		return this;
	}

	/**
	 * Removes a consent service from the chain.
	 * <p>
	 * Thread safety note: This method can be called while the service is actively processing requestes
	 *
	 * @param theConsentService The service to unregister. Must not be <code>null</code>.
	 * @since 6.0.0
	 */
	public ConsentInterceptor unregisterConsentService(IConsentService theConsentService) {
		Validate.notNull(theConsentService, "theConsentService must not be null");
		List<IConsentService> newList = myConsentService
			.stream()
			.filter(t -> t != theConsentService)
			.collect(Collectors.toList());
		myConsentService = newList;
		return this;
	}

	@Hook(value = Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED)
	public void interceptPreHandled(RequestDetails theRequestDetails) {
		if (isSkipServiceForRequest(theRequestDetails)) {
			return;
		}

		validateParameter(theRequestDetails.getParameters());

		for (IConsentService nextService : myConsentService) {
			ConsentOutcome outcome = nextService.startOperation(theRequestDetails, myContextConsentServices);
			Validate.notNull(outcome, "Consent service returned null outcome");

			switch (outcome.getStatus()) {
				case REJECT:
					throw toForbiddenOperationException(outcome);
				case PROCEED:
					continue;
				case AUTHORIZED:
					Map<Object, Object> userData = theRequestDetails.getUserData();
					userData.put(myRequestAuthorizedKey, Boolean.TRUE);
					return;
			}
		}
	}

	@Hook(value = Pointcut.STORAGE_PRECHECK_FOR_CACHED_SEARCH)
	public boolean interceptPreCheckForCachedSearch(RequestDetails theRequestDetails) {
		if (isRequestAuthorized(theRequestDetails)) {
			return true;
		}
		return false;
	}

	@Hook(value = Pointcut.STORAGE_PRESEARCH_REGISTERED)
	public void interceptPreSearchRegistered(RequestDetails theRequestDetails, ICachedSearchDetails theCachedSearchDetails) {
		if (!isRequestAuthorized(theRequestDetails)) {
			theCachedSearchDetails.setCannotBeReused();
		}
	}

	@Hook(value = Pointcut.STORAGE_PREACCESS_RESOURCES)
	public void interceptPreAccess(RequestDetails theRequestDetails, IPreResourceAccessDetails thePreResourceAccessDetails) {
		if (isRequestAuthorized(theRequestDetails)) {
			return;
		}
		if (isSkipServiceForRequest(theRequestDetails)) {
			return;
		}
		if (myConsentService.isEmpty()) {
			return;
		}

		// First check if we should be calling canSeeResource for the individual
		// consent services
		boolean[] processConsentSvcs = new boolean[myConsentService.size()];
		boolean processAnyConsentSvcs = false;
		for (int consentSvcIdx = 0; consentSvcIdx < myConsentService.size(); consentSvcIdx++) {
			IConsentService nextService = myConsentService.get(consentSvcIdx);

			boolean shouldCallCanSeeResource = nextService.shouldProcessCanSeeResource(theRequestDetails, myContextConsentServices);
			processAnyConsentSvcs |= shouldCallCanSeeResource;
			processConsentSvcs[consentSvcIdx] = shouldCallCanSeeResource;
		}

		if (!processAnyConsentSvcs) {
			return;
		}

		IdentityHashMap<IBaseResource, Boolean> authorizedResources = getAuthorizedResourcesMap(theRequestDetails);
		for (int resourceIdx = 0; resourceIdx < thePreResourceAccessDetails.size(); resourceIdx++) {
			IBaseResource nextResource = thePreResourceAccessDetails.getResource(resourceIdx);
			for (int consentSvcIdx = 0; consentSvcIdx < myConsentService.size(); consentSvcIdx++) {
				IConsentService nextService = myConsentService.get(consentSvcIdx);

				if (!processConsentSvcs[consentSvcIdx]) {
					continue;
				}

				ConsentOutcome outcome = nextService.canSeeResource(theRequestDetails, nextResource, myContextConsentServices);
				Validate.notNull(outcome, "Consent service returned null outcome");
				Validate.isTrue(outcome.getResource() == null, "Consent service returned a resource in its outcome. This is not permitted in canSeeResource(..)");

				boolean skipSubsequentServices = false;
				switch (outcome.getStatus()) {
					case PROCEED:
						break;
					case AUTHORIZED:
						authorizedResources.put(nextResource, Boolean.TRUE);
						skipSubsequentServices = true;
						break;
					case REJECT:
						thePreResourceAccessDetails.setDontReturnResourceAtIndex(resourceIdx);
						skipSubsequentServices = true;
						break;
				}

				if (skipSubsequentServices) {
					break;
				}
			}
		}
	}

	@Hook(value = Pointcut.STORAGE_PRESHOW_RESOURCES)
	public void interceptPreShow(RequestDetails theRequestDetails, IPreResourceShowDetails thePreResourceShowDetails) {
		if (isRequestAuthorized(theRequestDetails)) {
			return;
		}
		if (isAllowListedRequest(theRequestDetails)) {
			return;
		}
		if (isSkipServiceForRequest(theRequestDetails)) {
			return;
		}
		if (myConsentService.isEmpty()) {
			return;
		}

		IdentityHashMap<IBaseResource, Boolean> authorizedResources = getAuthorizedResourcesMap(theRequestDetails);

		for (int i = 0; i < thePreResourceShowDetails.size(); i++) {

			IBaseResource resource = thePreResourceShowDetails.getResource(i);
			if (resource == null || authorizedResources.putIfAbsent(resource, Boolean.TRUE) != null) {
				continue;
			}

			for (IConsentService nextService : myConsentService) {
				ConsentOutcome nextOutcome = nextService.willSeeResource(theRequestDetails, resource, myContextConsentServices);
				IBaseResource newResource = nextOutcome.getResource();

				switch (nextOutcome.getStatus()) {
					case PROCEED:
						if (newResource != null) {
							thePreResourceShowDetails.setResource(i, newResource);
							resource = newResource;
						}
						continue;
					case AUTHORIZED:
						if (newResource != null) {
							thePreResourceShowDetails.setResource(i, newResource);
						}
						continue;
					case REJECT:
						if (nextOutcome.getOperationOutcome() != null) {
							IBaseOperationOutcome newOperationOutcome = nextOutcome.getOperationOutcome();
							thePreResourceShowDetails.setResource(i, newOperationOutcome);
							authorizedResources.put(newOperationOutcome, true);
						} else {
							resource = null;
							thePreResourceShowDetails.setResource(i, null);
						}
						continue;
				}
			}
		}
	}

	@Hook(value = Pointcut.SERVER_OUTGOING_RESPONSE)
	public void interceptOutgoingResponse(RequestDetails theRequestDetails, ResponseDetails theResource) {
		if (theResource.getResponseResource() == null) {
			return;
		}
		if (isRequestAuthorized(theRequestDetails)) {
			return;
		}
		if (isAllowListedRequest(theRequestDetails)) {
			return;
		}
		if (isSkipServiceForRequest(theRequestDetails)) {
			return;
		}
		if (myConsentService.isEmpty()) {
			return;
		}

		IdentityHashMap<IBaseResource, Boolean> authorizedResources = getAuthorizedResourcesMap(theRequestDetails);

		// See outer resource
		if (authorizedResources.putIfAbsent(theResource.getResponseResource(), Boolean.TRUE) == null) {

			for (IConsentService next : myConsentService) {
				final ConsentOutcome outcome = next.willSeeResource(theRequestDetails, theResource.getResponseResource(), myContextConsentServices);
				if (outcome.getResource() != null) {
					theResource.setResponseResource(outcome.getResource());
				}

				// Clear the total
				if (theResource.getResponseResource() instanceof IBaseBundle) {
					BundleUtil.setTotal(theRequestDetails.getFhirContext(), (IBaseBundle) theResource.getResponseResource(), null);
				}

				switch (outcome.getStatus()) {
					case REJECT:
						if (outcome.getOperationOutcome() != null) {
							theResource.setResponseResource(outcome.getOperationOutcome());
						} else {
							theResource.setResponseResource(null);
							theResource.setResponseCode(Constants.STATUS_HTTP_204_NO_CONTENT);
						}
						// Return immediately
						return;
					case AUTHORIZED:
						// Don't check children, so return immediately
						return;
					case PROCEED:
						// Check children, so proceed
						break;
				}
			}
		}

		// See child resources
		IBaseResource outerResource = theResource.getResponseResource();
		FhirContext ctx = theRequestDetails.getServer().getFhirContext();
		IModelVisitor2 visitor = new IModelVisitor2() {
			@Override
			public boolean acceptElement(IBase theElement, List<IBase> theContainingElementPath, List<BaseRuntimeChildDefinition> theChildDefinitionPath, List<BaseRuntimeElementDefinition<?>> theElementDefinitionPath) {

				// Clear the total
				if (theElement instanceof IBaseBundle) {
					BundleUtil.setTotal(theRequestDetails.getFhirContext(), (IBaseBundle) theElement, null);
				}

				if (theElement == outerResource) {
					return true;
				}
				if (theElement instanceof IBaseResource) {
					IBaseResource resource = (IBaseResource) theElement;
					if (authorizedResources.putIfAbsent(resource, Boolean.TRUE) != null) {
						return true;
					}

					boolean shouldCheckChildren = true;
					for (IConsentService next : myConsentService) {
						ConsentOutcome childOutcome = next.willSeeResource(theRequestDetails, resource, myContextConsentServices);

						IBaseResource replacementResource = null;
						boolean shouldReplaceResource = false;

						switch (childOutcome.getStatus()) {
							case REJECT:
								replacementResource = childOutcome.getOperationOutcome();
								shouldReplaceResource = true;
								break;
							case PROCEED:
							case AUTHORIZED:
								replacementResource = childOutcome.getResource();
								shouldReplaceResource = replacementResource != null;
								shouldCheckChildren &= childOutcome.getStatus() == ConsentOperationStatusEnum.PROCEED;
								break;
						}

						if (shouldReplaceResource) {
							IBase container = theContainingElementPath.get(theContainingElementPath.size() - 2);
							BaseRuntimeChildDefinition containerChildElement = theChildDefinitionPath.get(theChildDefinitionPath.size() - 1);
							containerChildElement.getMutator().setValue(container, replacementResource);
							resource = replacementResource;
						}

					}

					return shouldCheckChildren;
				}

				return true;
			}

			@Override
			public boolean acceptUndeclaredExtension(IBaseExtension<?, ?> theNextExt, List<IBase> theContainingElementPath, List<BaseRuntimeChildDefinition> theChildDefinitionPath, List<BaseRuntimeElementDefinition<?>> theElementDefinitionPath) {
				return true;
			}
		};
		ctx.newTerser().visit(outerResource, visitor);

	}

	private IdentityHashMap<IBaseResource, Boolean> getAuthorizedResourcesMap(RequestDetails theRequestDetails) {
		return getAlreadySeenResourcesMap(theRequestDetails, myRequestSeenResourcesKey);
	}

	@Hook(value = Pointcut.SERVER_HANDLE_EXCEPTION)
	public void requestFailed(RequestDetails theRequest, BaseServerResponseException theException) {
		theRequest.getUserData().put(myRequestCompletedKey, Boolean.TRUE);
		for (IConsentService next : myConsentService) {
			next.completeOperationFailure(theRequest, theException, myContextConsentServices);
		}
	}

	@Hook(value = Pointcut.SERVER_PROCESSING_COMPLETED_NORMALLY)
	public void requestSucceeded(RequestDetails theRequest) {
		if (Boolean.TRUE.equals(theRequest.getUserData().get(myRequestCompletedKey))) {
			return;
		}
		for (IConsentService next : myConsentService) {
			next.completeOperationSuccess(theRequest, myContextConsentServices);
		}
	}

	private boolean isRequestAuthorized(RequestDetails theRequestDetails) {
		boolean retVal = false;
		if (theRequestDetails != null) {
			Object authorizedObj = theRequestDetails.getUserData().get(myRequestAuthorizedKey);
			retVal = Boolean.TRUE.equals(authorizedObj);
		}
		return retVal;
	}

	private boolean isSkipServiceForRequest(RequestDetails theRequestDetails) {
		return isMetadataPath(theRequestDetails) || isMetaOperation(theRequestDetails);
	}

	private boolean isAllowListedRequest(RequestDetails theRequestDetails) {
		return isMetadataPath(theRequestDetails) || isMetaOperation(theRequestDetails);
	}

	private boolean isMetaOperation(RequestDetails theRequestDetails) {
		return OPERATION_META.equals(theRequestDetails.getOperation());
	}

	private boolean isMetadataPath(RequestDetails theRequestDetails) {
		return URL_TOKEN_METADATA.equals(theRequestDetails.getRequestPath());
	}

	private void validateParameter(Map<String, String[]> theParameterMap) {
		if (theParameterMap != null) {
			if (theParameterMap.containsKey(Constants.PARAM_SEARCH_TOTAL_MODE) && Arrays.stream(theParameterMap.get("_total")).anyMatch("accurate"::equals)) {
				throw new InvalidRequestException(Msg.code(2037) + Constants.PARAM_SEARCH_TOTAL_MODE + "=accurate is not permitted on this server");
			}
			if (theParameterMap.containsKey(Constants.PARAM_SUMMARY) && Arrays.stream(theParameterMap.get("_summary")).anyMatch("count"::equals)) {
				throw new InvalidRequestException(Msg.code(2038) + Constants.PARAM_SUMMARY + "=count is not permitted on this server");
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static IdentityHashMap<IBaseResource, Boolean> getAlreadySeenResourcesMap(RequestDetails theRequestDetails, String theKey) {
		IdentityHashMap<IBaseResource, Boolean> alreadySeenResources = (IdentityHashMap<IBaseResource, Boolean>) theRequestDetails.getUserData().get(theKey);
		if (alreadySeenResources == null) {
			alreadySeenResources = new IdentityHashMap<>();
			theRequestDetails.getUserData().put(theKey, alreadySeenResources);
		}
		return alreadySeenResources;
	}

	private static ForbiddenOperationException toForbiddenOperationException(ConsentOutcome theOutcome) {
		IBaseOperationOutcome operationOutcome = null;
		if (theOutcome.getOperationOutcome() != null) {
			operationOutcome = theOutcome.getOperationOutcome();
		}
		return new ForbiddenOperationException("Rejected by consent service", operationOutcome);
	}
}
