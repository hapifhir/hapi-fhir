package ca.uhn.fhir.jpa.empi;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.model.MdmTransactionContext;
import ca.uhn.fhir.empi.rules.svc.EmpiResourceMatcherSvc;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.empi.util.EmpiUtil;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.data.IEmpiLinkDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.config.EmpiConsumerConfig;
import ca.uhn.fhir.jpa.empi.config.EmpiSearchParameterLoader;
import ca.uhn.fhir.jpa.empi.config.EmpiSubmitterConfig;
import ca.uhn.fhir.jpa.empi.config.TestEmpiConfigR4;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.empi.matcher.IsLinkedTo;
import ca.uhn.fhir.jpa.empi.matcher.IsMatchedToAPerson;
import ca.uhn.fhir.jpa.empi.matcher.IsPossibleDuplicateOf;
import ca.uhn.fhir.jpa.empi.matcher.IsPossibleLinkedTo;
import ca.uhn.fhir.jpa.empi.matcher.IsPossibleMatchWith;
import ca.uhn.fhir.jpa.empi.matcher.IsSameSourceResourceAs;
import ca.uhn.fhir.jpa.empi.svc.EmpiMatchLinkSvc;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matcher;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {EmpiSubmitterConfig.class, EmpiConsumerConfig.class, TestEmpiConfigR4.class, SubscriptionProcessorConfig.class})
abstract public class BaseEmpiR4Test extends BaseJpaR4Test {
	private static final Logger ourLog = getLogger(BaseEmpiR4Test.class);

	public static final String NAME_GIVEN_JANE = "Jane";
	public static final String NAME_GIVEN_PAUL = "Paul";
	public static final String TEST_NAME_FAMILY = "Doe";
	protected static final String TEST_ID_SYSTEM = "http://a.tv/";
	protected static final String JANE_ID = "ID.JANE.123";
	protected static final String PAUL_ID = "ID.PAUL.456";
	private static final ContactPoint TEST_TELECOM = new ContactPoint()
		.setSystem(ContactPoint.ContactPointSystem.PHONE)
		.setValue("555-555-5555");
	private static final String NAME_GIVEN_FRANK = "Frank";
	protected static final String FRANK_ID = "ID.FRANK.789";

	@Autowired
	protected FhirContext myFhirContext;
	@Autowired
	protected IFhirResourceDao<Patient> myPatientDao;
	@Autowired
	protected IFhirResourceDao<Organization> myOrganizationDao;
	@Autowired
	protected IFhirResourceDao<Medication> myMedicationDao;
	@Autowired
	protected IFhirResourceDao<Practitioner> myPractitionerDao;
	@Autowired
	protected EmpiResourceMatcherSvc myEmpiResourceMatcherSvc;
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
	@Autowired
	EmpiSearchParameterLoader myEmpiSearchParameterLoader;
	@Autowired
	SearchParamRegistryImpl mySearchParamRegistry;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	protected ServletRequestDetails myRequestDetails;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@BeforeEach
	public void beforeSetRequestDetails() {
		myRequestDetails = new ServletRequestDetails(myInterceptorBroadcaster);
	}

	@Override
	@AfterEach
	public void after() throws IOException {
		myEmpiLinkDao.deleteAll();
		assertEquals(0, myEmpiLinkDao.count());
		super.after();
	}

	protected void saveLink(EmpiLink theEmpiLink) {
		myEmpiLinkDaoSvc.save(theEmpiLink);
	}

	@Nonnull
	protected Patient createUnmanagedSourceResource() {
		return createGoldenPatient(new Patient(), false);
	}

	@Nonnull
	protected Patient createGoldenPatient() {
		return createGoldenPatient(new Patient(), true);
	}

	@Nonnull
	protected Patient createPatient() {
		return createPatient(new Patient());
	}

	@Nonnull
	protected Patient createGoldenPatient(Patient thePatient) {
		return createGoldenPatient(thePatient, true);
	}

	@Nonnull
	protected Patient createGoldenPatient(Patient thePatient, boolean theEmpiManaged) {
		EmpiUtil.setEmpiManaged(thePatient);
		EmpiUtil.setGoldenResource(thePatient);
		DaoMethodOutcome outcome = myPatientDao.create(thePatient);
		Patient patient = (Patient) outcome.getResource();
		patient.setId(outcome.getId());
		return patient;
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
	protected Medication createMedication(Medication theMedication) {
		//Note that since our empi-rules block on active=true, all patients must be active.
		DaoMethodOutcome outcome = myMedicationDao.create(theMedication);
		Medication medication = (Medication) outcome.getResource();
		medication.setId(outcome.getId());
		return medication;
	}

	@Nonnull
	protected Practitioner createPractitioner(Practitioner thePractitioner) {
		//Note that since our empi-rules block on active=true, all patients must be active.
		thePractitioner.setActive(true);
		DaoMethodOutcome daoMethodOutcome = myPractitionerDao.create(thePractitioner);
		thePractitioner.setId(daoMethodOutcome.getId());
		return thePractitioner;
	}

	@Nonnull
	protected Patient buildPatientWithNameAndId(String theGivenName, String theId) {
		return buildPatientWithNameIdAndBirthday(theGivenName, theId, null);
	}

	@Nonnull
	protected Practitioner buildPractitionerWithNameAndId(String theGivenName, String theId) {
		return buildPractitionerWithNameIdAndBirthday(theGivenName, theId, null);
	}

	/**
	 * Use {@link #buildPatientWithNameAndId(String, String)} instead
	 */
	@Deprecated
	@Nonnull
	protected Patient buildSourcePaitentWithNameAndId(String theGivenName, String theId) {
		return buildSourcePatientWithNameIdAndBirthday(theGivenName, theId, null);
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

	/**
	 * Use {@link #buildPatientWithNameAndId(String, String)} instead.
	 */
	@Deprecated
	@Nonnull
	protected Patient buildSourcePatientWithNameIdAndBirthday(String theGivenName, String theId, Date theBirthday) {
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
	protected Patient buildJanePatient() {
		return buildPatientWithNameAndId(NAME_GIVEN_JANE, JANE_ID);
	}

	@Nonnull
	protected Practitioner buildJanePractitioner() {
		return buildPractitionerWithNameAndId(NAME_GIVEN_JANE, JANE_ID);
	}

	/**
	 * Use {@link #buildJanePatient()} instead
	 */
	@Nonnull
	@Deprecated
	protected Patient buildJaneSourcePatient() {
		return buildSourcePaitentWithNameAndId(NAME_GIVEN_JANE, JANE_ID);
	}

	@Nonnull
	protected Patient buildPaulPatient() {
		return buildPatientWithNameAndId(NAME_GIVEN_PAUL, PAUL_ID);
	}

	@Nonnull
	protected Patient buildFrankPatient() {
		return buildPatientWithNameAndId(NAME_GIVEN_FRANK, FRANK_ID);
	}

	@Nonnull
	protected Patient buildJaneWithBirthday(Date theToday) {
		return buildPatientWithNameIdAndBirthday(NAME_GIVEN_JANE, JANE_ID, theToday);
	}

	protected void assertLinkCount(long theExpectedCount) {
		assertEquals(theExpectedCount, myEmpiLinkDao.count());
	}

	protected IAnyResource getGoldenResourceFromTargetResource(IAnyResource theBaseResource) {
		String resourceType = theBaseResource.getIdElement().getResourceType();
		IFhirResourceDao relevantDao = myDaoRegistry.getResourceDao(resourceType);

		Optional<EmpiLink> matchedLinkForTargetPid = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(myIdHelperService.getPidOrNull(theBaseResource));
		if (matchedLinkForTargetPid.isPresent()) {
			Long sourceResourcePid = matchedLinkForTargetPid.get().getSourceResourcePid();
			return (IAnyResource) relevantDao.readByPid(new ResourcePersistentId(sourceResourcePid));
		} else {
			return null;
		}
	}

	protected <T extends IBaseResource> T getTargetResourceFromEmpiLink(EmpiLink theEmpiLink, String theResourceType) {
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theResourceType);
		return (T) resourceDao.readByPid(new ResourcePersistentId(theEmpiLink.getSourceResourcePid()));
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
		myEmpiMatchLinkSvc.updateEmpiLinksForEmpiTarget(thePatient, createContextForCreate("Patient"));
		return thePatient;
	}

	protected Medication buildMedication(String theManufacturerReference) {
		Medication medication = new Medication();
		medication.setManufacturer(new Reference(theManufacturerReference));
		CodeableConcept codeableConcept = new CodeableConcept();
		codeableConcept.addCoding().setSystem("zoop").setCode("boop");
		medication.setCode(codeableConcept);
		return medication;
	}
	protected Medication createMedicationAndUpdateLinks(Medication theMedication) {
		theMedication = createMedication(theMedication);
		myEmpiMatchLinkSvc.updateEmpiLinksForEmpiTarget(theMedication, createContextForCreate("Medication"));
		return theMedication;
	}

	protected MdmTransactionContext createContextForCreate(String theResourceType) {
		MdmTransactionContext ctx = new MdmTransactionContext();
		ctx.setRestOperation(MdmTransactionContext.OperationType.CREATE_RESOURCE);
		ctx.setResourceType(theResourceType);
		ctx.setTransactionLogMessages(null);
		return ctx;
	}

	protected MdmTransactionContext createContextForUpdate(String theResourceType) {
		MdmTransactionContext ctx = new MdmTransactionContext();
		ctx.setRestOperation(MdmTransactionContext.OperationType.UPDATE_RESOURCE);
		ctx.setTransactionLogMessages(null);
		ctx.setResourceType(theResourceType);
		return ctx;
	}

	protected Patient updatePatientAndUpdateLinks(Patient thePatient) {
		thePatient = (Patient) myPatientDao.update(thePatient).getResource();
		myEmpiMatchLinkSvc.updateEmpiLinksForEmpiTarget(thePatient, createContextForUpdate(thePatient.getIdElement().getResourceType()));
		return thePatient;
	}

	protected Practitioner createPractitionerAndUpdateLinks(Practitioner thePractitioner) {
		thePractitioner.setActive(true);
		DaoMethodOutcome daoMethodOutcome = myPractitionerDao.create(thePractitioner);
		thePractitioner.setId(daoMethodOutcome.getId());
		myEmpiMatchLinkSvc.updateEmpiLinksForEmpiTarget(thePractitioner, createContextForCreate("Practitioner"));
		return thePractitioner;
	}

	protected Matcher<IAnyResource> sameSourceResourceAs(IAnyResource... theBaseResource) {
		return IsSameSourceResourceAs.sameSourceResourceAs(myIdHelperService, myEmpiLinkDaoSvc, theBaseResource);
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

	protected Patient getOnlyGoldenPatient() {
		List<IBaseResource> resources = getAllGoldenPatients();
		assertEquals(1, resources.size());
		return (Patient) resources.get(0);
	}


	@Nonnull
	protected List<IBaseResource> getAllGoldenPatients() {
		return getPatientsByTag(EmpiConstants.CODE_GOLDEN_RECORD);
	}

	@Nonnull
	protected List<IBaseResource> getAllRedirectedGoldenPatients() {
		return getPatientsByTag(EmpiConstants.CODE_GOLDEN_RECORD_REDIRECTED);
	}

	@NotNull
	private List<IBaseResource> getPatientsByTag(String theCode) {
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		//TODO GGG ensure that this tag search works effectively.
		map.add("_tag", new TokenParam(EmpiConstants.SYSTEM_GOLDEN_RECORD_STATUS, theCode));
		IBundleProvider bundle = myPatientDao.search(map);
		return bundle.getResources(0, 999);
	}


	@Nonnull
	protected EmpiLink createResourcesAndBuildTestEmpiLink() {
		Patient sourcePatient = createGoldenPatient();
		Patient patient = createPatient();

		EmpiLink empiLink = myEmpiLinkDaoSvc.newEmpiLink();
		empiLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		empiLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		empiLink.setGoldenResourcePid(myIdHelperService.getPidOrNull(sourcePatient));
		empiLink.setTargetPid(myIdHelperService.getPidOrNull(patient));
		return empiLink;
	}

	protected void loadEmpiSearchParameters() {
		myEmpiSearchParameterLoader.daoUpdateEmpiSearchParameters();
		mySearchParamRegistry.forceRefresh();
	}

	protected void logAllLinks() {
		ourLog.info("Logging all EMPI Links:");
		List<EmpiLink> links = myEmpiLinkDao.findAll();
		for (EmpiLink link : links) {
			ourLog.info(link.toString());
		}
	}

	protected void assertLinksMatchResult(EmpiMatchResultEnum... theExpectedValues) {
		assertFields(EmpiLink::getMatchResult, theExpectedValues);
	}

	protected void assertLinksCreatedNewResource(Boolean... theExpectedValues) {
		assertFields(EmpiLink::getHadToCreateNewResource, theExpectedValues);
	}

	protected void assertLinksMatchedByEid(Boolean... theExpectedValues) {
		assertFields(EmpiLink::getEidMatch, theExpectedValues);
	}

	private <T> void assertFields(Function<EmpiLink, T> theAccessor, T... theExpectedValues) {
		List<EmpiLink> links = myEmpiLinkDao.findAll();
		assertEquals(theExpectedValues.length, links.size());
		for (int i = 0; i < links.size(); ++i) {
			assertEquals(theExpectedValues[i], theAccessor.apply(links.get(i)), "Value at index " + i + " was not equal");
		}
	}


	protected void print(String message, IBaseResource ... theResource) {
		if (StringUtils.isNotEmpty(message)) {
			ourLog.info(message);
		}

		for (IBaseResource r : theResource) {
			ourLog.info(myFhirContext.newJsonParser().encodeResourceToString(r));
		}
	}

	protected void print(IBaseResource ... theResource) {
		print(null, theResource);
	}



	protected void printResources(String theResourceType) {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResourceType);
		IBundleProvider search = dao.search(new SearchParameterMap());
		search.getResources(0, search.size()).forEach(r -> {
			print(r);
		});
	}


	protected void printLinks() {
		myEmpiLinkDao.findAll().forEach(empiLink -> {
			ourLog.info(String.valueOf(empiLink));
		});
	}

}
