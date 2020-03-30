package ca.uhn.fhir.jpa.empi;

import ca.uhn.fhir.empi.rules.svc.EmpiResourceComparatorSvc;
import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.empi.config.EmpiConfig;
import ca.uhn.fhir.jpa.empi.config.TestEmpiConfig;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.After;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {EmpiConfig.class, TestEmpiConfig.class})
abstract public class BaseEmpiR4Test extends BaseJpaR4Test {
	private static final Logger ourLog = getLogger(BaseEmpiR4Test.class);

	protected static final String TEST_ID_SYSTEM = "http://a.tv/";
	protected static final String JANE_ID = "ID.JANE.123";
	public static final String NAME_GIVEN_JANE = "Jane";
	protected static final String PAUL_ID = "ID.PAUL.456";
	public static final String NAME_GIVEN_PAUL = "Paul";
	public static final String TEST_NAME_FAMILY = "Doe";

	@Autowired
	protected IFhirResourceDao<Person> myPersonDao;
	@Autowired
	protected IFhirResourceDao<Patient> myPatientDao;
	@Autowired
	protected EmpiResourceComparatorSvc myEmpiResourceComparatorSvc;
	@Autowired
	protected IEmpiLinkDao myEmpiLinkDao;

	@After
	public void after() {
		//FIXME EMPI QUESTION I don't think the interceptor should be in charge of expunging everything in test, so I've just ripped it here for now to make the new Rule work nicely. Thoughts?
		// KHS: Technically yes.  However I have often caught errors in the expunge interceptor only because it was a part of test cleanup.  E.g. when we add the second table with FKs into resource tables.
		myEmpiLinkDao.deleteAll();
		super.after();
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
	protected Person buildPersonWithNameAndId(String theGivenName, String theId) {
		return buildPersonWithNameIdAndBirthday(theGivenName, theId, null);
	}

	@Nonnull
	protected Patient buildPatientWithNameIdAndBirthday(String theGivenName, String theId, Date theBirthday) {
		Patient patient = new Patient();
		patient.addName().addGiven(theGivenName);
		patient.addName().setFamily(TEST_NAME_FAMILY);
		patient.addIdentifier().setSystem(TEST_ID_SYSTEM).setValue(theId);
		patient.setBirthDate(theBirthday);
		DateType dateType = new DateType(theBirthday);
		dateType.setPrecision(TemporalPrecisionEnum.DAY);
		patient.setBirthDateElement(dateType);
		return patient;
	}

	@Nonnull
	protected Person buildPersonWithNameIdAndBirthday(String theGivenName, String theId, Date theBirthday) {
		Person person = new Person();
		person.addName().addGiven(theGivenName);
		person.addName().setFamily(TEST_NAME_FAMILY);
		person.addIdentifier().setSystem(TEST_ID_SYSTEM).setValue(theId);
		person.setBirthDate(theBirthday);
		DateType dateType = new DateType(theBirthday);
		dateType.setPrecision(TemporalPrecisionEnum.DAY);
		person.setBirthDateElement(dateType);
		return person;
	}

	@Nonnull
	protected Patient buildJanePatient() {
		return buildPatientWithNameAndId(NAME_GIVEN_JANE, JANE_ID);
	}

	@Nonnull
	protected Person buildJanePerson() {
		return buildPersonWithNameAndId(NAME_GIVEN_JANE, JANE_ID);
	}

	@Nonnull
	protected Patient buildPaulPatient() {
		return buildPatientWithNameAndId(NAME_GIVEN_PAUL, PAUL_ID);
	}

	@Nonnull
	protected Patient buildJaneWithBirthday(Date theToday) {
		return buildPatientWithNameIdAndBirthday(NAME_GIVEN_JANE, JANE_ID, theToday);
	}

	protected void assertLinkCount(long theExpectedCount) {
		assertEquals(theExpectedCount, myEmpiLinkDao.count());
	}


	/**
	 * A Matcher which allows us to check that a target patient/practitioner
	 * is linked to a set of patients/practitioners via a person.
	 */
	public Matcher<IBaseResource> isLinkedTo(IBaseResource... theBaseResource) {
		return new TypeSafeMatcher<IBaseResource>() {
			@Override
			protected boolean matchesSafely(IBaseResource theBaseResource) {
				return false;
				//FIXME GGG
			}

			@Override
			public void describeTo(Description theDescription) {
				//FIXME GGG
			}
		};
	}

	/**
	 * A simple matcher which allows us to check whether 2 resources (Patient/Practitioner) resolve to
	 * the same person in the EmpiLink table.
	 */
	public Matcher<IBaseResource> isSamePersonAs(IBaseResource theBaseResource) {
		return new TypeSafeMatcher<IBaseResource>() {
			@Override
			protected boolean matchesSafely(IBaseResource theIncomingResource) {
				Long personPid = getEmpiLink(theIncomingResource).getPersonPid();
				Long personPid1 = getEmpiLink(theBaseResource).getPersonPid();

				return personPid.equals(personPid1);

			}
			private EmpiLink getEmpiLink(IBaseResource thePatientOrPractitionerResource) {
				EmpiLink candidate = new EmpiLink();
				candidate.setTargetPid(thePatientOrPractitionerResource.getIdElement().getIdPartAsLong());
				Example<EmpiLink> example = Example.of(candidate);
				return myEmpiLinkDao.findOne(example)
					.orElseThrow(() -> new IllegalStateException("We didn't find a Person for resource with pid: " + thePatientOrPractitionerResource.getIdElement()));
			}

			@Override
			public void describeTo(Description theDescription) {
				//FIXME GGG
			}
		};
	}

}
