package ca.uhn.fhir.jpaserver.empi.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class EmpiResourceDaoSvc {
	@Autowired
	FhirContext myFhirContext;
	@Autowired
	private DaoRegistry myDaoRegistry;

	private IFhirResourceDao<?> myPatientDao;
	private IFhirResourceDao<?> myPersonDao;
	private IFhirResourceDao<?> myPractitionerDao;

	@PostConstruct
	public void initDaos() {
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				myPatientDao = myDaoRegistry.getResourceDao(Patient.class);
				myPersonDao = myDaoRegistry.getResourceDao(Person.class);
				myPractitionerDao = myDaoRegistry.getResourceDao(Practitioner.class);
				break;
			default:
				// FIXME EMPI
				throw new UnsupportedOperationException("Version not supported: " + myFhirContext.getVersion().getVersion());
		}
	}

	public IBaseResource readPatient(IIdType theId) {
		return myPatientDao.read(theId);
	}

	public IBaseResource readPerson(IIdType theId) {
		return myPersonDao.read(theId);
	}

	public IBaseResource readPractitioner(IIdType theId) {
		return myPractitionerDao.read(theId);
	}
}
