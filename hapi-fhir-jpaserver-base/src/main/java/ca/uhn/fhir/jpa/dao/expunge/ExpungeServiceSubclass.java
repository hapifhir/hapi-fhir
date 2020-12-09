package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.springframework.beans.factory.annotation.Lookup;

//FIXME RZ
public class ExpungeServiceSubclass extends ExpungeService {

	@Lookup
	protected ExpungeOperation getExpungeOperation(String theResourceName, Long theResourceId, Long theVersion, ExpungeOptions theExpungeOptions, RequestDetails theRequestDetails) {
		return null;
	}

}
