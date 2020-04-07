package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.empi.api.IEmpiCandidateSearchSvc;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

// FIXME EMPI test
@Lazy
@Service
public class EmpiProviderDstu3 {
	@Autowired
	IEmpiCandidateSearchSvc myEmpiCandidateSearchSvc;

	@Operation(name="$match", type = Patient.class)
	public Bundle match(@OperationParam(name="resource", min = 1, max = 1) Patient thePatient) {
		Collection<IBaseResource> matches = myEmpiCandidateSearchSvc.findCandidates("Patient", thePatient);

		Bundle retVal = new Bundle();
		retVal.setType(Bundle.BundleType.SEARCHSET);
		retVal.setId(UUID.randomUUID().toString());
		retVal.getMeta().setLastUpdatedElement(InstantType.now());

		for (IBaseResource next : matches) {
			retVal.addEntry().setResource((Resource) next);
		}

		return retVal;
	}
}
