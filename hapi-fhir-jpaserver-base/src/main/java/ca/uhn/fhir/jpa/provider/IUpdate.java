package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.servlet.http.HttpServletRequest;

public interface IUpdate<T extends IAnyResource> extends IExtendedResourceProvider<T> {

	@Update
	default MethodOutcome update(HttpServletRequest theRequest, @ResourceParam T theResource, @IdParam IIdType theId, @ConditionalUrlParam String theConditional, RequestDetails theRequestDetails) {
		startRequest(theRequest);
		try {
			if (theConditional != null) {
				return getDao().update(theResource, theConditional, theRequestDetails);
			} else {
				return getDao().update(theResource, theRequestDetails);
			}
		} finally {
			endRequest(theRequest);
		}
	}
}
