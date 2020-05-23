package ca.uhn.fhir.rest.server.interceptor.consent;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.rest.server.util.ICachedSearchDetails;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.IModelVisitor2;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.*;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Interceptor
public class ConsentInterceptor {
	private static final AtomicInteger ourInstanceCount = new AtomicInteger(0);
	private final int myInstanceIndex = ourInstanceCount.incrementAndGet();
	private final String myRequestAuthorizedKey = ConsentInterceptor.class.getName() + "_" + myInstanceIndex + "_AUTHORIZED";
	private final String myRequestCompletedKey = ConsentInterceptor.class.getName() + "_" + myInstanceIndex + "_COMPLETED";
	private final String myRequestSeenResourcesKey = ConsentInterceptor.class.getName() + "_" + myInstanceIndex + "_SEENRESOURCES";

	private IConsentService myConsentService;
	private IConsentContextServices myContextConsentServices;

	/**
	 * Constructor
	 */
	public ConsentInterceptor() {
		super();
	}

	/**
	 * Constructor
	 *
	 * @param theConsentService         Must not be <code>null</code>
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

	public void setConsentService(IConsentService theConsentService) {
		Validate.notNull(theConsentService, "theConsentService must not be null");
		myConsentService = theConsentService;
	}

	@Hook(value = Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED)
	public void interceptPreHandled(RequestDetails theRequestDetails) {
		ConsentOutcome outcome = myConsentService.startOperation(theRequestDetails, myContextConsentServices);
		Validate.notNull(outcome, "Consent service returned null outcome");

		switch (outcome.getStatus()) {
			case REJECT:
				throw toForbiddenOperationException(outcome);
			case PROCEED:
				break;
			case AUTHORIZED:
				Map<Object, Object> userData = theRequestDetails.getUserData();
				userData.put(myRequestAuthorizedKey, Boolean.TRUE);
				break;
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

		for (int i = 0; i < thePreResourceAccessDetails.size(); i++) {
			IBaseResource nextResource = thePreResourceAccessDetails.getResource(i);
			ConsentOutcome nextOutcome = myConsentService.canSeeResource(theRequestDetails, nextResource, myContextConsentServices);
			switch (nextOutcome.getStatus()) {
				case PROCEED:
					break;
				case AUTHORIZED:
					break;
				case REJECT:
					thePreResourceAccessDetails.setDontReturnResourceAtIndex(i);
					break;
			}
		}
	}

	@Hook(value = Pointcut.STORAGE_PRESHOW_RESOURCES)
	public void interceptPreShow(RequestDetails theRequestDetails, IPreResourceShowDetails thePreResourceShowDetails) {
		if (isRequestAuthorized(theRequestDetails)) {
			return;
		}
		IdentityHashMap<IBaseResource, Boolean> alreadySeenResources = getAlreadySeenResourcesMap(theRequestDetails);

		for (int i = 0; i < thePreResourceShowDetails.size(); i++) {
			IBaseResource nextResource = thePreResourceShowDetails.getResource(i);
			if (alreadySeenResources.putIfAbsent(nextResource, Boolean.TRUE) != null) {
				continue;
			}

			ConsentOutcome nextOutcome = myConsentService.willSeeResource(theRequestDetails, nextResource, myContextConsentServices);
			switch (nextOutcome.getStatus()) {
				case PROCEED:
					if (nextOutcome.getResource() != null) {
						thePreResourceShowDetails.setResource(i, nextOutcome.getResource());
					}
					break;
				case AUTHORIZED:
					break;
				case REJECT:
					if (nextOutcome.getResource() != null) {
						IBaseResource newResource = nextOutcome.getResource();
						thePreResourceShowDetails.setResource(i, newResource);
						alreadySeenResources.put(newResource, true);
					} else if (nextOutcome.getOperationOutcome() != null) {
						IBaseOperationOutcome newOperationOutcome = nextOutcome.getOperationOutcome();
						thePreResourceShowDetails.setResource(i, newOperationOutcome);
						alreadySeenResources.put(newOperationOutcome, true);
					} else {
						String resourceId = nextResource.getIdElement().getValue();
						thePreResourceShowDetails.setResource(i, null);
						nextResource.setId(resourceId);
					}
					break;
			}
		}
	}

	private IdentityHashMap<IBaseResource, Boolean> getAlreadySeenResourcesMap(RequestDetails theRequestDetails) {
		return getAlreadySeenResourcesMap(theRequestDetails, myRequestSeenResourcesKey);
	}

	@Hook(value = Pointcut.SERVER_OUTGOING_RESPONSE)
	public void interceptOutgoingResponse(RequestDetails theRequestDetails, ResponseDetails theResource) {
		if (theResource.getResponseResource() == null) {
			return;
		}
		if (isRequestAuthorized(theRequestDetails)) {
			return;
		}

		IdentityHashMap<IBaseResource, Boolean> alreadySeenResources = getAlreadySeenResourcesMap(theRequestDetails);

		// See outer resource
		if (alreadySeenResources.putIfAbsent(theResource.getResponseResource(), Boolean.TRUE) == null) {
			final ConsentOutcome outcome = myConsentService.willSeeResource(theRequestDetails, theResource.getResponseResource(), myContextConsentServices);
			if (outcome.getResource() != null) {
				theResource.setResponseResource(outcome.getResource());
			}

			switch (outcome.getStatus()) {
				case REJECT:
					if (outcome.getOperationOutcome() != null) {
						theResource.setResponseResource(outcome.getOperationOutcome());
					} else {
						theResource.setResponseResource(null);
						theResource.setResponseCode(Constants.STATUS_HTTP_204_NO_CONTENT);
					}
					return;
				case AUTHORIZED:
					// Don't check children
					return;
				case PROCEED:
					// Check children
					break;
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
					if (alreadySeenResources.putIfAbsent((IBaseResource) theElement, Boolean.TRUE) != null) {
						return true;
					}
					ConsentOutcome childOutcome = myConsentService.willSeeResource(theRequestDetails, (IBaseResource) theElement, myContextConsentServices);

					IBaseResource replacementResource = null;
					boolean shouldReplaceResource = false;
					boolean shouldCheckChildren = false;

					switch (childOutcome.getStatus()) {
						case REJECT:
							replacementResource = childOutcome.getOperationOutcome();
							shouldReplaceResource = true;
							break;
						case PROCEED:
						case AUTHORIZED:
							replacementResource = childOutcome.getResource();
							shouldReplaceResource = replacementResource != null;
							shouldCheckChildren = childOutcome.getStatus() == ConsentOperationStatusEnum.PROCEED;
							break;
					}

					if (shouldReplaceResource) {
						IBase container = theContainingElementPath.get(theContainingElementPath.size() - 2);
						BaseRuntimeChildDefinition containerChildElement = theChildDefinitionPath.get(theChildDefinitionPath.size() - 1);
						containerChildElement.getMutator().setValue(container, replacementResource);
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

	@Hook(value = Pointcut.SERVER_HANDLE_EXCEPTION)
	public void requestFailed(RequestDetails theRequest, BaseServerResponseException theException) {
		theRequest.getUserData().put(myRequestCompletedKey, Boolean.TRUE);
		myConsentService.completeOperationFailure(theRequest, theException, myContextConsentServices);
	}

	@Hook(value = Pointcut.SERVER_PROCESSING_COMPLETED_NORMALLY)
	public void requestSucceeded(RequestDetails theRequest) {
		if (Boolean.TRUE.equals(theRequest.getUserData().get(myRequestCompletedKey))) {
			return;
		}
		myConsentService.completeOperationSuccess(theRequest, myContextConsentServices);
	}

	private boolean isRequestAuthorized(RequestDetails theRequestDetails) {
		boolean retVal = false;
		if (theRequestDetails != null) {
			Object authorizedObj = theRequestDetails.getUserData().get(myRequestAuthorizedKey);
			retVal = Boolean.TRUE.equals(authorizedObj);
		}
		return retVal;
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
