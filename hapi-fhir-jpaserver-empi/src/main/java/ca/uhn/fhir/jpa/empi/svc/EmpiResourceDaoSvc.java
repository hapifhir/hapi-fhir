package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Lazy
@Service
public class EmpiResourceDaoSvc {
	@Autowired
	DaoRegistry myDaoRegistry;

	private IFhirResourceDao myPatientDao;
	private IFhirResourceDao myPersonDao;
	private IFhirResourceDao myPractitionerDao;

	@PostConstruct
	public void postConstruct() {
		myPatientDao = myDaoRegistry.getResourceDao("Patient");
		myPersonDao = myDaoRegistry.getResourceDao("Person");
		myPractitionerDao = myDaoRegistry.getResourceDao("Practitioner");
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

	public DaoMethodOutcome updatePerson(IBaseResource thePerson) {
		return myPersonDao.update(thePerson);
	}
}
