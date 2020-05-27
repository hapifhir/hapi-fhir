package ca.uhn.fhir.jpa.empi;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.empi.rules.svc.EmpiResourceComparatorSvc;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.dao.data.IEmpiLinkDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.config.EmpiConsumerConfig;
import ca.uhn.fhir.jpa.empi.config.EmpiSubmitterConfig;
import ca.uhn.fhir.jpa.empi.config.TestEmpiConfigR4;
import ca.uhn.fhir.jpa.empi.matcher.IsLinkedTo;
import ca.uhn.fhir.jpa.empi.matcher.IsMatchedToAPerson;
import ca.uhn.fhir.jpa.empi.matcher.IsPossibleDuplicateOf;
import ca.uhn.fhir.jpa.empi.matcher.IsPossibleLinkedTo;
import ca.uhn.fhir.jpa.empi.matcher.IsPossibleMatchWith;
import ca.uhn.fhir.jpa.empi.matcher.IsSamePersonAs;
import ca.uhn.fhir.jpa.empi.svc.EmpiMatchLinkSvc;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hamcrest.Matcher;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Practitioner;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {EmpiSubmitterConfig.class, EmpiConsumerConfig.class, TestEmpiConfigR4.class, SubscriptionProcessorConfig.class})
abstract public class BaseEmpiR4Test extends BaseJpaR4Test {
	private static final Logger ourLog = getLogger(BaseEmpiR4Test.class);

	protected static final String TEST_ID_SYSTEM = "http://a.tv/";
	protected static final String JANE_ID = "ID.JANE.123";
	public static final String NAME_GIVEN_JANE = "Jane";
	protected static final String PAUL_ID = "ID.PAUL.456";
	public static final String NAME_GIVEN_PAUL = "Paul";
	public static final String TEST_NAME_FAMILY = "Doe";
	private static final ContactPoint TEST_TELECOM = new ContactPoint()
		.setSystem(ContactPoint.ContactPointSystem.PHONE)
		.setValue("555-555-5555");

	@Autowired
	protected FhirContext myFhirContext;
	@Autowired
	protected IFhirResourceDao<Person> myPersonDao;
	@Autowired
	protected IFhirResourceDao<Patient> myPatientDao;
	@Autowired
	protected IFhirResourceDao<Practitioner> myPractitionerDao;
	@Autowired
	protected EmpiResourceComparatorSvc myEmpiResourceComparatorSvc;
	@Autowired
	protected IEmpiLinkDao myEmpiLinkDao;
	@Autowired
	protected EmpiLinkDaoSvc myEmpiLinkDaoSvc;
	@Autowired
	protected IdHelperService myIdHelperService;
	@Autowired
	protected IEmpiSettings myEmpiConfig;
	@Autowired
	protected EmpiMatchLinkSvc myEmpiMatchLinkSvc;
	@Autowired
	protected EIDHelper myEIDHelper;

	protected ServletRequestDetails myRequestDetails = new ServletRequestDetails(null);

	@After
	public void after() {
		myEmpiLinkDao.deleteAll();
		super.after();
	}

	@Nonnull
	protected Person createUnmanagedPerson() {
		return createPerson(new Person(), false);
	}

	@Nonnull
	protected Person createPerson() {
		return createPerson(new Person(), true);
	}

	@Nonnull
	protected Patient createPatient() {
		return createPatient(new Patient());
	}

	@Nonnull
	protected Person createPerson(Person thePerson) {
		return createPerson(thePerson, true);
	}

	@Nonnull
	protected Person createPerson(Person thePerson, boolean theEmpiManaged) {
		if (theEmpiManaged) {
			thePerson.getMeta().addTag().setSystem(EmpiConstants.SYSTEM_EMPI_MANAGED).setCode(EmpiConstants.CODE_HAPI_EMPI_MANAGED);
		}
		DaoMethodOutcome outcome = myPersonDao.create(thePerson);
		Person person = (Person) outcome.getResource();
		person.setId(outcome.getId());
		return person;
	}

	@Nonnull
	protected Patient createPatient(Patient thePatient) {
		//Note that since our empi-rules block on active=true, all patients must be active.
		thePatient.setActive(true);
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
	protected Practitioner buildPractitionerWithNameAndId(String theGivenName, String theId) {
		return buildPractitionerWithNameIdAndBirthday(theGivenName, theId, null);
	}

	@Nonnull
	protected Person buildPersonWithNameAndId(String theGivenName, String theId) {
		return buildPersonWithNameIdAndBirthday(theGivenName, theId, null);
	}


	@Nonnull
	protected Patient buildPatientWithNameIdAndBirthday(String theGivenName, String theId, Date theBirthday) {
		Patient patient = new Patient();
		patient.getNameFirstRep().addGiven(theGivenName);
		patient.getNameFirstRep().setFamily(TEST_NAME_FAMILY);
		patient.addIdentifier().setSystem(TEST_ID_SYSTEM).setValue(theId);
		patient.setBirthDate(theBirthday);
		patient.setTelecom(Collections.singletonList(TEST_TELECOM));
		DateType dateType = new DateType(theBirthday);
		dateType.setPrecision(TemporalPrecisionEnum.DAY);
		patient.setBirthDateElement(dateType);
		return patient;
	}

	@Nonnull
	protected Practitioner buildPractitionerWithNameIdAndBirthday(String theGivenName, String theId, Date theBirthday) {
		Practitioner practitioner = new Practitioner();
		practitioner.addName().addGiven(theGivenName);
		practitioner.addName().setFamily(TEST_NAME_FAMILY);
		practitioner.addIdentifier().setSystem(TEST_ID_SYSTEM).setValue(theId);
		practitioner.setBirthDate(theBirthday);
		practitioner.setTelecom(Collections.singletonList(TEST_TELECOM));
		DateType dateType = new DateType(theBirthday);
		dateType.setPrecision(TemporalPrecisionEnum.DAY);
		practitioner.setBirthDateElement(dateType);
		return practitioner;
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
	protected Practitioner buildJanePractitioner() {
		return buildPractitionerWithNameAndId(NAME_GIVEN_JANE, JANE_ID);
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

	protected Person getPersonFromTarget(IAnyResource theBaseResource) {
		Optional<EmpiLink> matchedLinkForTargetPid = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(myIdHelperService.getPidOrNull(theBaseResource));
		if (matchedLinkForTargetPid.isPresent()) {
			Long personPid = matchedLinkForTargetPid.get().getPersonPid();
			return (Person) myPersonDao.readByPid(new ResourcePersistentId(personPid));
		} else {
			return null;
		}
	}

	protected Person getPersonFromEmpiLink(EmpiLink theEmpiLink) {
		return (Person) myPersonDao.readByPid(new ResourcePersistentId(theEmpiLink.getPersonPid()));
	}

	protected Patient addExternalEID(Patient thePatient, String theEID) {
		thePatient.addIdentifier().setSystem(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem()).setValue(theEID);
		return thePatient;
	}

	protected Patient clearExternalEIDs(Patient thePatient) {
		thePatient.getIdentifier().removeIf(theIdentifier -> theIdentifier.getSystem().equalsIgnoreCase(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem()));
		return thePatient;
	}

	protected Patient createPatientAndUpdateLinks(Patient thePatient) {
		thePatient = createPatient(thePatient);
		myEmpiMatchLinkSvc.updateEmpiLinksForEmpiTarget(thePatient, createContextForCreate());
		return thePatient;
	}

	protected EmpiTransactionContext createContextForCreate() {
		EmpiTransactionContext ctx = new EmpiTransactionContext();
		ctx.setRestOperation(EmpiTransactionContext.OperationType.CREATE);
		ctx.setTransactionLogMessages(null);
		return ctx;
	}

	protected EmpiTransactionContext createContextForUpdate() {
		EmpiTransactionContext ctx = new EmpiTransactionContext();
		ctx.setRestOperation(EmpiTransactionContext.OperationType.UPDATE);
		ctx.setTransactionLogMessages(null);
		return ctx;
	}

	protected Patient updatePatientAndUpdateLinks(Patient thePatient) {
		thePatient = (Patient) myPatientDao.update(thePatient).getResource();
		myEmpiMatchLinkSvc.updateEmpiLinksForEmpiTarget(thePatient, createContextForUpdate());
		return thePatient;
	}

	protected Practitioner createPractitionerAndUpdateLinks(Practitioner thePractitioner) {
		thePractitioner.setActive(true);
		DaoMethodOutcome daoMethodOutcome = myPractitionerDao.create(thePractitioner);
		thePractitioner.setId(daoMethodOutcome.getId());
		myEmpiMatchLinkSvc.updateEmpiLinksForEmpiTarget(thePractitioner, createContextForCreate());
		return thePractitioner;
	}

	protected Matcher<IAnyResource> samePersonAs(IAnyResource... theBaseResource) {
		return IsSamePersonAs.samePersonAs(myIdHelperService, myEmpiLinkDaoSvc, theBaseResource);
	}

	protected Matcher<IAnyResource> linkedTo(IAnyResource... theBaseResource) {
		return IsLinkedTo.linkedTo(myIdHelperService, myEmpiLinkDaoSvc, theBaseResource);
	}

	protected Matcher<IAnyResource> possibleLinkedTo(IAnyResource... theBaseResource) {
		return IsPossibleLinkedTo.possibleLinkedTo(myIdHelperService, myEmpiLinkDaoSvc, theBaseResource);
	}

	protected Matcher<IAnyResource> possibleMatchWith(IAnyResource... theBaseResource) {
		return IsPossibleMatchWith.possibleMatchWith(myIdHelperService, myEmpiLinkDaoSvc, theBaseResource);
	}

	protected Matcher<IAnyResource> possibleDuplicateOf(IAnyResource... theBaseResource) {
		return IsPossibleDuplicateOf.possibleDuplicateOf(myIdHelperService, myEmpiLinkDaoSvc, theBaseResource);
	}

	protected Matcher<IAnyResource> matchedToAPerson() {
		return IsMatchedToAPerson.matchedToAPerson(myIdHelperService, myEmpiLinkDaoSvc);
	}

	protected Person getOnlyPerson() {
		List<IBaseResource> resources = getAllPersons();
		assertEquals(1, resources.size());
		return (Person) resources.get(0);
	}

	@NotNull
	protected List<IBaseResource> getAllPersons() {
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		IBundleProvider bundle = myPersonDao.search(map);
		return bundle.getResources(0, 999);
	}

	@Nonnull
	protected EmpiLink createResourcesAndBuildTestEmpiLink() {
		Person person = createPerson();
		Patient patient = createPatient();

		EmpiLink empiLink = new EmpiLink();
		empiLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		empiLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		empiLink.setPersonPid(myIdHelperService.getPidOrNull(person));
		empiLink.setTargetPid(myIdHelperService.getPidOrNull(patient));
		return empiLink;
	}
}
