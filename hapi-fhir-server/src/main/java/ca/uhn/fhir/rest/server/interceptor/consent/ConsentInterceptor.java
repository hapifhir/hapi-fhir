package ca.uhn.fhir.rest.server.interceptor.consent;

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
import ca.uhn.fhir.util.IModelVisitor2;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

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

	private final IConsentService myConsentService;

	public ConsentInterceptor(IConsentService theConsentService) {
		Validate.notNull(theConsentService, "theConsentService must not be null");
		myConsentService = theConsentService;
	}

	@Hook(value = Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED)
	public void interceptPreHandled(RequestDetails theRequestDetails) {
		ConsentOutcome outcome = myConsentService.startOperation(theRequestDetails);
		Validate.notNull(outcome, "Copnsent service returned null outcome");

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
			ConsentOutcome nextOutcome = myConsentService.canSeeResource(theRequestDetails, nextResource);
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

			ConsentOutcome nextOutcome = myConsentService.seeResource(theRequestDetails, nextResource);
			switch (nextOutcome.getStatus()) {
				case PROCEED:
					if (nextOutcome.getResource() != null) {
						thePreResourceShowDetails.setResource(i, nextOutcome.getResource());
					}
					break;
				case AUTHORIZED:
					break;
				case REJECT:
					theRequestDetails.getFhirContext().newTerser().clear(nextResource);
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
		IdentityHashMap<IBaseResource, Boolean> alreadySeenResources = getAlreadySeenResourcesMap(theRequestDetails);

		// See outer resource
		if (alreadySeenResources.putIfAbsent(theResource.getResponseResource(), Boolean.TRUE) == null) {
			final ConsentOutcome outcome = myConsentService.seeResource(theRequestDetails, theResource.getResponseResource());
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
				if (theElement == outerResource) {
					return true;
				}
				if (theElement instanceof IBaseResource) {
					if (alreadySeenResources.putIfAbsent((IBaseResource) theElement, Boolean.TRUE) != null) {
						return true;
					}
					ConsentOutcome childOutcome = myConsentService.seeResource(theRequestDetails, (IBaseResource) theElement);

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
		myConsentService.completeOperationFailure(theRequest, theException);
	}

	@Hook(value = Pointcut.SERVER_PROCESSING_COMPLETED_NORMALLY)
	public void requestSucceeded(RequestDetails theRequest) {
		if (Boolean.TRUE.equals(theRequest.getUserData().get(myRequestCompletedKey))) {
			return;
		}
		myConsentService.completeOperationSuccess(theRequest);
	}

	private boolean isRequestAuthorized(RequestDetails theRequestDetails) {
		Object authorizedObj = theRequestDetails.getUserData().get(myRequestAuthorizedKey);
		return Boolean.TRUE.equals(authorizedObj);
	}

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
