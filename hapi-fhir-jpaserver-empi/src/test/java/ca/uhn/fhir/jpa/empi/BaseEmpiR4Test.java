package ca.uhn.fhir.jpa.empi;

import ca.uhn.fhir.empi.rules.svc.EmpiResourceComparatorSvc;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.bulk.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeEverythingService;
import ca.uhn.fhir.jpa.empi.config.EmpiConfig;
import ca.uhn.fhir.jpa.empi.config.TestEmpiConfig;
import ca.uhn.fhir.jpa.empi.interceptor.EmpiInterceptor;
import ca.uhn.fhir.jpa.search.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.util.ExpungeOptions;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Nonnull;
import java.util.Date;

import static org.slf4j.LoggerFactory.getLogger;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {EmpiConfig.class, TestEmpiConfig.class})
abstract public class BaseEmpiR4Test extends BaseJpaR4Test {
	private static final Logger ourLog = getLogger(BaseEmpiR4Test.class);

	protected static final String TEST_ID_SYSTEM = "http://a.tv/";
	protected static final String JANE_ID = "JANE";
	
	@Autowired
	protected IFhirResourceDao<Person> myPersonDao;
	@Autowired
	protected IFhirResourceDao<Patient> myPatientDao;
	@Autowired
	protected EmpiResourceComparatorSvc myEmpiResourceComparatorSvc;
	@Autowired
	protected IInterceptorService myInterceptorService;
	@Autowired
	private EmpiInterceptor myEmpiInterceptor;

	@Before
	public void before() {
		myInterceptorService.registerInterceptor(myEmpiInterceptor);
	}

	@Nonnull
	protected Person createPerson() {
		return createPerson(new Person());
	}

	@Nonnull
	protected Patient createPatient() {
		return createPatient(new Patient());
	}

	@Nonnull
	protected Person createPerson(Person thePerson) {
		DaoMethodOutcome outcome = myPersonDao.create(thePerson);
		Person person = (Person) outcome.getResource();
		person.setId(outcome.getId());
		return person;
	}

	@Nonnull
	protected Patient createPatient(Patient thePatient) {
		DaoMethodOutcome outcome = myPatientDao.create(thePatient);
		Patient patient = (Patient) outcome.getResource();
		patient.setId(outcome.getId());
		return patient;
	}

	@Nonnull
	protected Patient buildPatientWithNameAndId(String theGivenName, String theId) {
		return buildPatientWithNameIdAndBirthday(theGivenName, theId, null);
	}

	@Nonnull
	protected Patient buildPatientWithNameIdAndBirthday(String theGivenName, String theId, Date theBirthday) {
		Patient patient = new Patient();
		patient.addName().addGiven(theGivenName);
		patient.addName().setFamily("Doe");
		patient.addIdentifier().setSystem(TEST_ID_SYSTEM).setValue(theId);
		patient.setBirthDate(theBirthday);
		DateType dateType = new DateType(theBirthday);
		dateType.setPrecision(TemporalPrecisionEnum.DAY);
		patient.setBirthDateElement(dateType);
		return patient;
	}
}
