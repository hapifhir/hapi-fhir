package ca.uhn.fhir.jpa.empi;

import ca.uhn.fhir.empi.jpalink.config.EmpiConfig;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.empi.config.EmpiTestConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {EmpiConfig.class, EmpiTestConfig.class})
abstract public class BaseEmpiR4Test extends BaseJpaR4Test {
	@Autowired
	protected IFhirResourceDao<Person> myPersonDao;

	protected ResourceTable myPersonEntity;
	protected ResourceTable myPatientEntity;
	protected Person myPerson;
	protected Patient myPatient;

	@Before
	public void before() {
		myPerson = new Person();
		myPersonEntity = (ResourceTable) myPersonDao.create(myPerson).getEntity();
		myPatient = new Patient();
		myPatientEntity = (ResourceTable) myPatientDao.create(myPatient).getEntity();
	}
}
