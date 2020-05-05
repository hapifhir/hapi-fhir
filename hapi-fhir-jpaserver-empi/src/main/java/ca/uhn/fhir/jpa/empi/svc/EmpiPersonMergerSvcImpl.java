package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.IEmpiPersonMergerSvc;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.stereotype.Service;

@Service
public class EmpiPersonMergerSvcImpl implements IEmpiPersonMergerSvc {
	@Override
	public IBaseResource mergePersons(IBaseResource thePersonToDelete, IBaseResource thePersonToKeep) {
		return thePersonToKeep;
	}
}
