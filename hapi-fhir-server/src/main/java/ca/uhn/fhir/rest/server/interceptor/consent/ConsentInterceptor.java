package ca.uhn.fhir.rest.server.interceptor.consent;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Interceptor
public class ConsentInterceptor {
	private static final AtomicInteger ourInstanceCount = new AtomicInteger(0);
	private final String myRequestAuthorizedKey = ConsentInterceptor.class.getName() + "_" + ourInstanceCount.incrementAndGet() + "_AUTHORIZED";
	private final String myRequestCompletedKey = ConsentInterceptor.class.getName() + "_" + ourInstanceCount.incrementAndGet() + "_COMPLETED";

	// FIXME: ensure that searches aren't reused if consent applies

	private final IConsentService myConsentService;

	public ConsentInterceptor(IConsentService theConsentService) {
		Validate.notNull(theConsentService, "theConsentService must not be null");
		myConsentService = theConsentService;
	}

	@Hook(value = Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED)
	public void interceptPreHandled(RestOperationTypeEnum theOperationTypeEnum, IServerInterceptor.ActionRequestDetails theRequestDetails) {
		ConsentOutcome outcome = myConsentService.startOperation(theOperationTypeEnum, theRequestDetails);
		Validate.notNull(outcome, "Copnsent service returned null outcome");

		switch (outcome.getStatus()) {
			case REJECT:
				throw new ForbiddenOperationException("Rejected by consent service", outcome.getOperationOutcome());
			case PROCEED:
				break;
			case AUTHORIZED:
				Map<Object, Object> userData = theRequestDetails.getUserData();
				userData.put(myRequestAuthorizedKey, Boolean.TRUE);
				break;
		}
	}

	@Hook(value = Pointcut.STORAGE_PREACCESS_RESOURCES)
	public void interceptPreAccess(RequestDetails theRequestDetails, IPreResourceAccessDetails thePreResourceAccessDetails) {
		Object authorized = theRequestDetails.getUserData().get(myRequestAuthorizedKey);
		if (Boolean.TRUE.equals(authorized)) {
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

	@Hook(value = Pointcut.STORAGE_PRESHOW_RESOURCE)
	public void interceptPreShowResource(RequestDetails theRequestDetails, IPreResourceShowDetails thePreResourceShowDetails) {
		Object authorized = theRequestDetails.getUserData().get(myRequestAuthorizedKey);
		if (Boolean.TRUE.equals(authorized)) {
			return;
		}

		for (int i = 0; i < thePreResourceShowDetails.size(); i++) {
			IBaseResource nextResource = thePreResourceShowDetails.getResource(i);
			ConsentOutcome nextOutcome = myConsentService.seeResource(theRequestDetails, nextResource);
			switch (nextOutcome.getStatus()) {
				case REJECT:
					thePreResourceShowDetails.setResource(i, null);
					break;
				case PROCEED:
					IBaseResource updated = thePreResourceShowDetails.getResource(i);
					thePreResourceShowDetails.setResource(i, updated);
					break;
				case AUTHORIZED:
					break;
			}
		}
	}

	@Hook(value = Pointcut.SERVER_HANDLE_EXCEPTION)
	public void requestFailed(RequestDetails theRequest, BaseServerResponseException theException) {
		theRequest.getUserData().put(myRequestCompletedKey, Boolean.TRUE);
		myConsentService.completeOperationSuccess(theRequest);
	}

	@Hook(value = Pointcut.SERVER_PROCESSING_COMPLETED_NORMALLY)
	public void requestSucceeded(RequestDetails theRequest) {
		if (Boolean.TRUE.equals(theRequest.getUserData().get(myRequestCompletedKey))) {
			return;
		}
		myConsentService.completeOperationSuccess(theRequest);
	}
}
