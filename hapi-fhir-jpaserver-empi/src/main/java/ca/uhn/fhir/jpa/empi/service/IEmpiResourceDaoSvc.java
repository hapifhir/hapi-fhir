package ca.uhn.fhir.jpa.empi.service;

import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IEmpiResourceDaoSvc {
	IBaseResource readPatient(IIdType theId);

	IBaseResource readPerson(IIdType theId);

	IBaseResource readPractitioner(IIdType theId);

	DaoMethodOutcome updatePerson(IBaseResource thePerson);
}
