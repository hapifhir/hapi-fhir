package ca.uhn.fhir.jpa.api;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IEmpiResourceDaoSvc {
	IBaseResource readPatient(IIdType theId);

	IBaseResource readPerson(IIdType theId);

	IBaseResource readPractitioner(IIdType theId);
}
