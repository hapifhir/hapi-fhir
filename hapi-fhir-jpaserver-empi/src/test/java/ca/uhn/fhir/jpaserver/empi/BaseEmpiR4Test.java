package ca.uhn.fhir.jpaserver.empi;

import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.config.SearchParamR4Config;
import ca.uhn.fhir.jpaserver.empi.config.EmpiConfig;
import ca.uhn.fhir.jpaserver.empi.config.TestEmpiR4Config;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {EmpiConfig.class, SearchParamR4Config.class, TestEmpiR4Config.class})
abstract public class BaseEmpiR4Test {
	@Autowired
	DaoRegistry myDaoRegistry;

	protected IFhirResourceDao<Person> myPersonDao;
	private IFhirResourceDao<Patient> myPatientDao;
	protected Person myPerson;
	protected Patient myPatient;

	@Before
	public void before() {
		myPersonDao = myDaoRegistry.getResourceDao(Person.class);
		IIdType personId = myPersonDao.create(new Person()).getId().toUnqualifiedVersionless();
		myPerson = myPersonDao.read(personId);

		myPatientDao = myDaoRegistry.getResourceDao(Patient.class);
		IIdType patientId = myPatientDao.create(new Patient()).getId().toUnqualifiedVersionless();
	}
}
