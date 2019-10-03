package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IAnyResource;

import javax.servlet.http.HttpServletRequest;

public interface ICreate<T extends IAnyResource> extends IExtendedResourceProvider<T> {

	@Create
	default MethodOutcome create(HttpServletRequest theRequest, @ResourceParam T theResource, @ConditionalUrlParam String theConditional, RequestDetails theRequestDetails) {
		startRequest(theRequest);
		try {
			if (theConditional != null) {
				return getDao().create(theResource, theConditional, theRequestDetails);
			} else {
				return getDao().create(theResource, theRequestDetails);
			}
		} finally {
			endRequest(theRequest);
		}
	}
}
