package ca.uhn.fhir.empi.provider;

import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;
import java.util.UUID;

// FIXME EMPI test
public class EmpiProviderDstu3 {
	private final IEmpiMatchFinderSvc myEmpiMatchFinderSvc;

	/**
	 * Constructor
	 *
	 * Note that this is not a spring bean. Any necessary injections should
	 * happen in the constructor
	 */
	public EmpiProviderDstu3(IEmpiMatchFinderSvc theEmpiMatchFinderSvc) {
		myEmpiMatchFinderSvc = theEmpiMatchFinderSvc;
	}

	@Operation(name="$match", type = Patient.class)
	public Bundle match(@OperationParam(name="resource", min = 1, max = 1) Patient thePatient) {
		if (thePatient == null) {
			throw new InvalidRequestException("resource may not be null");
		}
		Collection<IBaseResource> matches = myEmpiMatchFinderSvc.findMatches("Patient", thePatient);

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
