package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.api.IEmpiMatchSvc;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.stereotype.Service;

@Service
public class EmpiMatchSvcImpl implements IEmpiMatchSvc {
	@Override
	public void updatePatientLinks(IBaseResource theResource) {
		// FIXME EMPI implement
	}
}
