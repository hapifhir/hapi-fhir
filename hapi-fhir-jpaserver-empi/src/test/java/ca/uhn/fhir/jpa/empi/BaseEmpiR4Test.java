package ca.uhn.fhir.jpa.empi;

import ca.uhn.fhir.empi.rules.svc.EmpiResourceComparatorSvc;
import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.empi.config.EmpiConfig;
import ca.uhn.fhir.jpa.empi.config.TestEmpiConfig;
import ca.uhn.fhir.jpa.helper.ResourceTableHelper;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {EmpiConfig.class, TestEmpiConfig.class})
abstract public class BaseEmpiR4Test extends BaseJpaR4Test {
	@Autowired
	protected IFhirResourceDao<Person> myPersonDao;
	@Autowired
	protected IFhirResourceDao<Patient> myPatientDao;
	@Autowired
	protected EmpiResourceComparatorSvc myEmpiResourceComparatorSvc;

	protected Person myPerson;
	protected Patient myPatient;
	protected IIdType myPersonId;
	protected IIdType myPatientId;
	protected Long myPersonPid;
	protected Long myPatientPid;

	@Before
	public void before() throws IOException {
		{
			DaoMethodOutcome outcome = myPersonDao.create(new Person());
			myPersonId = outcome.getId().toUnqualifiedVersionless();
			myPersonPid = ResourceTableHelper.getPidOrNull(outcome.getResource());
			myPerson = myPersonDao.read(myPersonId);
		}
		{
			DaoMethodOutcome outcome = myPatientDao.create(new Patient());
			myPatientId = outcome.getId().toUnqualifiedVersionless();
			myPatientPid = ResourceTableHelper.getPidOrNull(outcome.getResource());
			myPatient = myPatientDao.read(myPatientId);
		}
	}
}
