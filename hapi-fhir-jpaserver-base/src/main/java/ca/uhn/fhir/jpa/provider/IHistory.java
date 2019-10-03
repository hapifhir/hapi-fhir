package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.rest.annotation.At;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public interface IHistory<T extends IAnyResource> extends IExtendedResourceProvider<T> {

	@History
	default IBundleProvider getHistoryForResourceInstance(
		HttpServletRequest theRequest,
		@IdParam IIdType theId,
		@Since Date theSince,
		@At DateRangeParam theAt,
		RequestDetails theRequestDetails) {

		startRequest(theRequest);
		try {
			DateRangeParam sinceOrAt = processSinceOrAt(theSince, theAt);
			return getDao().history(theId, sinceOrAt.getLowerBoundAsInstant(), sinceOrAt.getUpperBoundAsInstant(), theRequestDetails);
		} finally {
			endRequest(theRequest);
		}
	}

	DateRangeParam processSinceOrAt(Date theSince, DateRangeParam theAt);
}
