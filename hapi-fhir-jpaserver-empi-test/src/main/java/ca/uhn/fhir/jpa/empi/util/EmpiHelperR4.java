package ca.uhn.fhir.jpa.empi.util;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.springframework.beans.factory.annotation.Autowired;

public class EmpiHelperR4 extends BaseEmpiHelper {
	@Autowired
	protected IFhirResourceDao<Patient> myPatientDao;
	@Autowired
	protected IFhirResourceDao<Person> myPersonDao;

	public DaoMethodOutcome createWithLatch(Patient thePatient, boolean isExternalHttpRequest) throws InterruptedException {
		myAfterEmpiLatch.setExpectedCount(1);
		DaoMethodOutcome daoMethodOutcome = doCreatePatient(thePatient, isExternalHttpRequest);
		myAfterEmpiLatch.awaitExpected();
		return daoMethodOutcome;
	}

	public DaoMethodOutcome createWithLatch(Patient thePatient) throws InterruptedException {
		return createWithLatch(thePatient, true);
	}

	public DaoMethodOutcome createWithLatch(Person thePerson, boolean isExternalHttpRequest) throws InterruptedException {
		myAfterEmpiLatch.setExpectedCount(1);
		DaoMethodOutcome daoMethodOutcome = doCreatePerson(thePerson, isExternalHttpRequest);
		myAfterEmpiLatch.awaitExpected();
		return daoMethodOutcome;
	}

	public DaoMethodOutcome createWithLatch(Person thePerson) throws InterruptedException {
		return createWithLatch(thePerson, true);
	}

	public DaoMethodOutcome updateWithLatch(Person thePerson) throws InterruptedException {
		return updateWithLatch(thePerson, true);
	}
	public DaoMethodOutcome updateWithLatch(Person thePerson, boolean isExternalHttpRequest) throws InterruptedException {
		myAfterEmpiLatch.setExpectedCount(1);
		DaoMethodOutcome daoMethodOutcome =  doUpdatePerson(thePerson, isExternalHttpRequest);
		myAfterEmpiLatch.awaitExpected();
		return daoMethodOutcome;
	}
	public DaoMethodOutcome doCreatePatient(Patient thePatient, boolean isExternalHttpRequest) {
		return isExternalHttpRequest ? myPatientDao.create(thePatient, myMockSrd): myPatientDao.create(thePatient);
	}
	public DaoMethodOutcome doUpdatePatient(Patient thePatient, boolean isExternalHttpRequest) {
		return isExternalHttpRequest ? myPatientDao.update(thePatient, myMockSrd): myPatientDao.update(thePatient);
	}

	public DaoMethodOutcome doCreatePerson(Person thePerson, boolean isExternalHttpRequest) {
		return isExternalHttpRequest ? myPersonDao.create(thePerson, myMockSrd): myPersonDao.create(thePerson);
	}
	public DaoMethodOutcome doUpdatePerson(Person thePerson, boolean isExternalHttpRequest) {
		return isExternalHttpRequest ? myPersonDao.update(thePerson, myMockSrd): myPersonDao.update(thePerson);
	}

}
