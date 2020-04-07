package ca.uhn.fhir.empi.api;

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;

public interface IEmpiCandidateSearchSvc {
	Collection<IBaseResource> findCandidates(String thePatient, IBaseResource theResource);
}
