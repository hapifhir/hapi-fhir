package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Patch;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.servlet.http.HttpServletRequest;

public interface IPatch<T extends IAnyResource> extends IResourceProvider<T> {

	@Patch
	default DaoMethodOutcome patch(HttpServletRequest theRequest, @IdParam IIdType theId, RequestDetails theRequestDetails, @ResourceParam String theBody, PatchTypeEnum thePatchType) {
		startRequest(theRequest);
		try {
			return getDao().patch(theId, thePatchType, theBody, theRequestDetails);
		} finally {
			endRequest(theRequest);
		}
	}

}
