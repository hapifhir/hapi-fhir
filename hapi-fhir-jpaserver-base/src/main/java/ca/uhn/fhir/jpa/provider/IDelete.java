package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.instance.model.api.IAnyResource;

import javax.servlet.http.HttpServletRequest;

public interface IDelete<T extends IAnyResource> extends IExtendedResourceProvider<T> {

	@Delete
	default MethodOutcome delete(HttpServletRequest theRequest, @IdParam IdType theResource, @ConditionalUrlParam(supportsMultiple = true) String theConditional, RequestDetails theRequestDetails) {
		startRequest(theRequest);
		try {
			if (theConditional != null) {
				return getDao().deleteByUrl(theConditional, theRequestDetails);
			} else {
				return getDao().delete(theResource, theRequestDetails);
			}
		} finally {
			endRequest(theRequest);
		}
	}
}
