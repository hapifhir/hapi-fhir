package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.IEmpiPersonMergerSvc;
import ca.uhn.fhir.empi.util.PersonHelper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpiPersonMergerSvcImpl implements IEmpiPersonMergerSvc {
	@Autowired
	PersonHelper myPersonHelper;

	@Override
	public IBaseResource mergePersons(IBaseResource thePersonToDelete, IBaseResource thePersonToKeep) {
		myPersonHelper.mergePersonFields(thePersonToDelete, thePersonToKeep);
		return thePersonToKeep;
	}

}
