package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentOutcome;
import ca.uhn.fhir.rest.server.interceptor.consent.IConsentContextServices;
import ca.uhn.fhir.rest.server.interceptor.consent.IConsentService;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

public class SearchNarrowingConsentService implements IConsentService {

	@Override
	public ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
		List<AllowedCodeInValueSet> postFilteringList = SearchNarrowingInterceptor.getPostFilteringList(theRequestDetails);
		if (postFilteringList.isEmpty()) {
			return ConsentOutcome.PROCEED;
		}

		

		return IConsentService.super.canSeeResource(theRequestDetails, theResource, theContextServices);
	}
}
