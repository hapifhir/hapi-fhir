package ca.uhn.fhir.jpa.empi;

import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.rules.svc.EmpiResourceComparatorSvc;
import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.empi.config.EmpiCtxConfig;
import ca.uhn.fhir.jpa.empi.config.TestEmpiConfigR4;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.jpa.empi.svc.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {EmpiCtxConfig.class, TestEmpiConfigR4.class})
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
	@Autowired
	protected EmpiLinkDaoSvc myEmpiLinkDaoSvc;

	@After
	public void after() {
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
	public Matcher<IBaseResource> linkedTo(IBaseResource... theBaseResource) {

		return new TypeSafeMatcher<IBaseResource>() {
			private List<Long> baseResourcePersonPids;
			private Long incomingResourcePersonPid;

			@Override
			protected boolean matchesSafely(IBaseResource theIncomingResource) {
				EmpiLink link = new EmpiLink();
				link.setTargetPid(theIncomingResource.getIdElement().getIdPartAsLong());
				link.setMatchResult(EmpiMatchResultEnum.MATCH);
				Example<EmpiLink> exampleLink = Example.of(link);
				Optional<EmpiLink> one = myEmpiLinkDao.findOne(exampleLink);
				//There is no person for this patient, so obviously they are not linked to anybody.
				if (!one.isPresent()){
					return false;
				} else {
					incomingResourcePersonPid = one.get().getPersonPid();
				}

				//OK, lets grab all the person pids of the resources passed in via the constructor.
				baseResourcePersonPids = Arrays.stream(theBaseResource)
					.map(base -> getPersonPidFromResource(base))
					.collect(Collectors.toList());

				//The resources are linked if all person pids match the incoming person pid.
				return baseResourcePersonPids.stream()
					.allMatch(pid -> pid.equals(incomingResourcePersonPid));
			}

			private Long getPersonPidFromResource(IBaseResource theResource) {
				IIdType idElement = theResource.getIdElement();
				if (theResource.getIdElement().getResourceType().equalsIgnoreCase("Person")) {
					return idElement.getIdPartAsLong();
				} else {
					Optional<EmpiLink> linkByPersonPidAndTargetPid = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(idElement.getIdPartAsLong());
					return linkByPersonPidAndTargetPid.get().getPersonPid();
				}

			}

			@Override
			public void describeTo(Description theDescription) {
			}
		};
	}

	/**
	 * A simple matcher which allows us to check whether 2 resources (Patient/Practitioner) resolve to
	 * the same person in the EmpiLink table.
	 */
	public Matcher<IBaseResource> samePersonAs(IBaseResource theBaseResource) {
		return new TypeSafeMatcher<IBaseResource>() {
			private Long personIdToMatch;
			private Long incomingPersonId;

			@Override
			protected boolean matchesSafely(IBaseResource theIncomingResource) {
				if (isPatientOrPractitioner(theIncomingResource)) {
					incomingPersonId= getEmpiLink(theIncomingResource).getPersonPid();
				} else if (isPerson(theIncomingResource)) {
					incomingPersonId = theIncomingResource.getIdElement().getIdPartAsLong();
				} else {
					throw new IllegalArgumentException("Resources of type " + theIncomingResource.getIdElement().getResourceType()+" cannot be persons!");
				}

				if (isPatientOrPractitioner(theBaseResource)) {
					personIdToMatch = getEmpiLink(theBaseResource).getPersonPid();
				} else if (isPerson(theBaseResource)) {
					personIdToMatch = theBaseResource.getIdElement().getIdPartAsLong();
				} else {
					throw new IllegalArgumentException("Resources of type " + theIncomingResource.getIdElement().getResourceType() + " cannot be persons!");
				}


				return incomingPersonId.equals(personIdToMatch);

			}

			private boolean isPerson(IBaseResource theIncomingResource) {
				return (theIncomingResource.getIdElement().getResourceType().equalsIgnoreCase("Person"));
			}

			private boolean isPatientOrPractitioner(IBaseResource theResource) {
				String resourceType = theResource.getIdElement().getResourceType();
				return (resourceType.equalsIgnoreCase("Patient") || resourceType.equalsIgnoreCase("Practitioner"));
			}
			private EmpiLink getEmpiLink(IBaseResource thePatientOrPractitionerResource) {
				Optional<EmpiLink> matchLinkForTarget = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(thePatientOrPractitionerResource.getIdElement().getIdPartAsLong());
				if (matchLinkForTarget.isPresent()) {
					return matchLinkForTarget.get();
				} else {
					throw new IllegalStateException("We didn't find a related Person for resource with pid: " + thePatientOrPractitionerResource.getIdElement());
				}
			}

			@Override
			public void describeTo(Description theDescription) {
				theDescription.appendText("patient/practitioner linked to Person/" +personIdToMatch);
			}

			@Override
			protected void describeMismatchSafely(IBaseResource item, Description mismatchDescription) {
				super.describeMismatchSafely(item, mismatchDescription);
				mismatchDescription.appendText(" was actually linked to Person/" +incomingPersonId);
			}
		};
	}


}
