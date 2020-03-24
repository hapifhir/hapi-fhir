package ca.uhn.fhir.jpa.empi.service;

import ca.uhn.fhir.jpa.api.IEmpiResourceDaoSvc;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpiResourceDaoR4SvcImpl implements IEmpiResourceDaoSvc {
	@Autowired
	private IFhirResourceDao<Patient> myPatientDao;
	@Autowired
	private IFhirResourceDao<Person> myPersonDao;
	@Autowired
	private IFhirResourceDao<Practitioner> myPractitionerDao;

	@Override
	public IBaseResource readPatient(IIdType theId) {
		return myPatientDao.read(theId);
	}

	@Override
	public IBaseResource readPerson(IIdType theId) {
		return myPersonDao.read(theId);
	}

	@Override
	public IBaseResource readPractitioner(IIdType theId) {
		return myPractitionerDao.read(theId);
	}
}
