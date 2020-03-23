package ca.uhn.fhir.empi.api;

import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IEmpiLinkSvc {
    void createLink(IBaseResource thePerson, IBaseResource thePatient);
}
