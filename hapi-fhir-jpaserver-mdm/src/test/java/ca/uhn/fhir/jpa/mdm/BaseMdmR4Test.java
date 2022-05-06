package ca.uhn.fhir.jpa.mdm;

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkDao;
import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.config.MdmConsumerConfig;
import ca.uhn.fhir.jpa.mdm.config.MdmSubmitterConfig;
import ca.uhn.fhir.jpa.mdm.config.TestMdmConfigR4;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.mdm.matcher.IsLinkedTo;
import ca.uhn.fhir.jpa.mdm.matcher.IsMatchedToAGoldenResource;
import ca.uhn.fhir.jpa.mdm.matcher.IsPossibleDuplicateOf;
import ca.uhn.fhir.jpa.mdm.matcher.IsPossibleLinkedTo;
import ca.uhn.fhir.jpa.mdm.matcher.IsPossibleMatchWith;
import ca.uhn.fhir.jpa.mdm.matcher.IsSameGoldenResourceAs;
import ca.uhn.fhir.jpa.mdm.svc.MdmMatchLinkSvc;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.rules.svc.MdmResourceMatcherSvc;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
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
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MdmSubmitterConfig.class, MdmConsumerConfig.class, TestMdmConfigR4.class, SubscriptionProcessorConfig.class})
abstract public class BaseMdmR4Test extends BaseJpaR4Test {

	protected static final String PARTITION_1 = "PART-1";
	protected static final String PARTITION_2 = "PART-2";
	public static final String NAME_GIVEN_JANE = "Jane";
	public static final String NAME_GIVEN_PAUL = "Paul";
	public static final String TEST_NAME_FAMILY = "Doe";
	protected static final String TEST_ID_SYSTEM = "http://a.tv/";
	protected static final String JANE_ID = "ID.JANE.123";
	protected static final String PAUL_ID = "ID.PAUL.456";
	protected static final String FRANK_ID = "ID.FRANK.789";
	protected static final String DUMMY_ORG_ID = "Organization/mfr";
	protected static final String EID_1 = "123";
	protected static final String EID_2 = "456";

	private static final Logger ourLog = getLogger(BaseMdmR4Test.class);
	private static final ContactPoint TEST_TELECOM = new ContactPoint()
		.setSystem(ContactPoint.ContactPointSystem.PHONE)
		.setValue("555-555-5555");
	private static final String NAME_GIVEN_FRANK = "Frank";



	@Autowired
	protected IFhirResourceDao<Patient> myPatientDao;
	@Autowired
	protected IFhirResourceDao<Organization> myOrganizationDao;
	@Autowired
	protected IFhirResourceDao<Medication> myMedicationDao;
	@Autowired
	protected IFhirResourceDao<Practitioner> myPractitionerDao;
	@Autowired
	protected IFhirResourceDao<Observation> myObservationDao;
	@Autowired
	protected MdmResourceMatcherSvc myMdmResourceMatcherSvc;
	@Autowired
	protected IMdmLinkDao myMdmLinkDao;
	@Autowired
	protected MdmLinkDaoSvc myMdmLinkDaoSvc;
	@Autowired
	protected IJpaIdHelperService myIdHelperService;
	@Autowired
	protected IMdmSettings myMdmSettings;
	@Autowired
	protected MdmMatchLinkSvc myMdmMatchLinkSvc;
	@Autowired
	protected EIDHelper myEIDHelper;
	protected ServletRequestDetails myRequestDetails;
	@Autowired
	SearchParamRegistryImpl mySearchParamRegistry;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	protected PartitionSettings myPartitionSettings;
	@Autowired
	protected IPartitionLookupSvc myPartitionLookupSvc;

	@BeforeEach
	public void beforeSetRequestDetails() {
		myRequestDetails = new ServletRequestDetails(myInterceptorBroadcaster);
	}

	@AfterEach
	public void after() throws IOException {
		myMdmLinkDao.deleteAll();
		assertEquals(0, myMdmLinkDao.count());
	}

	protected void saveLink(MdmLink theMdmLink) {
		myMdmLinkDaoSvc.save(theMdmLink);
	}

	@Nonnull
	protected Patient createGoldenPatient() {
		return createPatient(new Patient(), true, false);
	}

	@Nonnull
	protected Patient createPatient() {
		return createPatient(new Patient());
	}

	@Nonnull
	protected Patient createGoldenPatient(Patient thePatient) {
		return createPatient(thePatient, true, false);
	}

	@Nonnull
	protected Patient createRedirectedGoldenPatient(Patient thePatient) {
		return createPatient(thePatient, true, true);
	}

	@Nonnull
	protected Patient createPatient(Patient thePatient, boolean theMdmManaged, boolean isRedirect) {
		if (theMdmManaged) {
			MdmResourceUtil.setMdmManaged(thePatient);
			if (isRedirect) {
				MdmResourceUtil.setGoldenResourceRedirected(thePatient);
			} else {
				MdmResourceUtil.setGoldenResource(thePatient);
			}
		}

		DaoMethodOutcome outcome = myPatientDao.create(thePatient);
		Patient patient = (Patient) outcome.getResource();
		patient.setId(outcome.getId());
		return patient;
	}

	@Nonnull
	protected Patient createPatient(Patient thePatient) {
		//Note that since our mdm-rules block on active=true, all patients must be active.
		thePatient.setActive(true);

		DaoMethodOutcome outcome = myPatientDao.create(thePatient);
		Patient patient = (Patient) outcome.getResource();
		patient.setId(outcome.getId());
		return patient;
	}

	@Nonnull
	protected Patient createPatientOnPartition(Patient thePatient, boolean theMdmManaged, boolean isRedirect, RequestPartitionId theRequestPartitionId) {
		if (theMdmManaged) {
			MdmResourceUtil.setMdmManaged(thePatient);
			if (isRedirect) {
				MdmResourceUtil.setGoldenResourceRedirected(thePatient);
			} else {
				MdmResourceUtil.setGoldenResource(thePatient);
			}
		}

		SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		systemRequestDetails.setRequestPartitionId(theRequestPartitionId);
		DaoMethodOutcome outcome = myPatientDao.create(thePatient, systemRequestDetails);
		Patient patient = (Patient) outcome.getResource();
		patient.setId(outcome.getId());
		patient.setUserData(Constants.RESOURCE_PARTITION_ID, theRequestPartitionId);
		return patient;
	}

	@Nonnull
	protected Patient createPatientOnPartition(Patient thePatient, RequestPartitionId theRequestPartitionId) {
		//Note that since our mdm-rules block on active=true, all patients must be active.
		thePatient.setActive(true);

		SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		systemRequestDetails.setRequestPartitionId(theRequestPartitionId);
		DaoMethodOutcome outcome = myPatientDao.create(thePatient, systemRequestDetails);
		Patient patient = (Patient) outcome.getResource();
		patient.setId(outcome.getId());
		return patient;
	}

	@Nonnull
	protected Medication createMedication(Medication theMedication) {
		DaoMethodOutcome outcome = myMedicationDao.create(theMedication);
		Medication medication = (Medication) outcome.getResource();
		medication.setId(outcome.getId());
		return medication;
	}

	@Nonnull
	protected Practitioner createPractitioner(Practitioner thePractitioner) {
		//Note that since our mdm-rules block on active=true, all patients must be active.
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
		assertEquals(theExpectedCount, myMdmLinkDao.count());
	}

	protected <T extends IAnyResource> T getGoldenResourceFromTargetResource(T theBaseResource) {
		String resourceType = theBaseResource.getIdElement().getResourceType();
		IFhirResourceDao relevantDao = myDaoRegistry.getResourceDao(resourceType);

		Optional<MdmLink> matchedLinkForTargetPid = myMdmLinkDaoSvc.getMatchedLinkForSourcePid(runInTransaction(() -> myIdHelperService.getPidOrNull(theBaseResource)));
		if (matchedLinkForTargetPid.isPresent()) {
			Long goldenResourcePid = matchedLinkForTargetPid.get().getGoldenResourcePid();
			return (T) relevantDao.readByPid(new ResourcePersistentId(goldenResourcePid));
		} else {
			return null;
		}
	}

	protected <T extends IBaseResource> T getTargetResourceFromMdmLink(MdmLink theMdmLink, String theResourceType) {
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theResourceType);
		return (T) resourceDao.readByPid(new ResourcePersistentId(theMdmLink.getGoldenResourcePid()));
	}

	protected Patient addExternalEID(Patient thePatient, String theEID) {
		thePatient.addIdentifier().setSystem(myMdmSettings.getMdmRules().getEnterpriseEIDSystemForResourceType("Patient")).setValue(theEID);
		return thePatient;
	}

	protected Patient clearExternalEIDs(Patient thePatient) {
		thePatient.getIdentifier().removeIf(theIdentifier -> theIdentifier.getSystem().equalsIgnoreCase(myMdmSettings.getMdmRules().getEnterpriseEIDSystemForResourceType("Patient")));
		return thePatient;
	}

	protected Patient createPatientAndUpdateLinks(Patient thePatient) {
		thePatient = createPatient(thePatient);
		myMdmMatchLinkSvc.updateMdmLinksForMdmSource(thePatient, createContextForCreate("Patient"));
		return thePatient;
	}

	protected Patient createPatientAndUpdateLinksOnPartition(Patient thePatient, RequestPartitionId theRequestPartitionId) {
		thePatient = createPatientOnPartition(thePatient, theRequestPartitionId);
		thePatient.setUserData(Constants.RESOURCE_PARTITION_ID, theRequestPartitionId);
		myMdmMatchLinkSvc.updateMdmLinksForMdmSource(thePatient, createContextForCreate("Patient"));
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

	protected Medication buildMedicationWithDummyOrganization() {
		return buildMedication(DUMMY_ORG_ID);
	}

	protected Medication createMedicationAndUpdateLinks(Medication theMedication) {
		theMedication = createMedication(theMedication);
		myMdmMatchLinkSvc.updateMdmLinksForMdmSource(theMedication, createContextForCreate("Medication"));
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
		ourLog.info("About to update links...");
		myMdmMatchLinkSvc.updateMdmLinksForMdmSource(thePatient, createContextForUpdate(thePatient.getIdElement().getResourceType()));
		return thePatient;
	}

	protected Practitioner createPractitionerAndUpdateLinks(Practitioner thePractitioner) {
		thePractitioner.setActive(true);
		DaoMethodOutcome daoMethodOutcome = myPractitionerDao.create(thePractitioner);
		thePractitioner.setId(daoMethodOutcome.getId());
		myMdmMatchLinkSvc.updateMdmLinksForMdmSource(thePractitioner, createContextForCreate("Practitioner"));
		return thePractitioner;
	}

	protected Practitioner createPractitionerAndUpdateLinksOnPartition(Practitioner thePractitioner, RequestPartitionId theRequestPartitionId) {
		thePractitioner.setActive(true);
		SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		systemRequestDetails.setRequestPartitionId(theRequestPartitionId);
		DaoMethodOutcome daoMethodOutcome = myPractitionerDao.create(thePractitioner, systemRequestDetails);
		thePractitioner.setId(daoMethodOutcome.getId());
		thePractitioner.setUserData(Constants.RESOURCE_PARTITION_ID, theRequestPartitionId);
		myMdmMatchLinkSvc.updateMdmLinksForMdmSource(thePractitioner, createContextForCreate("Practitioner"));
		return thePractitioner;
	}

	private Matcher<IAnyResource> wrapMatcherInTransaction(Supplier<Matcher<IAnyResource>> theFunction) {
		return new Matcher<IAnyResource>() {
			@Override
			public boolean matches(Object actual) {
				return runInTransaction(() -> theFunction.get().matches(actual));
			}

			@Override
			public void describeMismatch(Object actual, Description mismatchDescription) {
				runInTransaction(() -> theFunction.get().describeMismatch(actual, mismatchDescription));
			}

			@Override
			public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

			}

			@Override
			public void describeTo(Description description) {
				runInTransaction(() -> theFunction.get().describeTo(description));
			}
		};
	}

	protected Matcher<IAnyResource> sameGoldenResourceAs(IAnyResource... theBaseResource) {
		return wrapMatcherInTransaction(() -> IsSameGoldenResourceAs.sameGoldenResourceAs(myIdHelperService, myMdmLinkDaoSvc, theBaseResource));
	}

	protected Matcher<IAnyResource> linkedTo(IAnyResource... theBaseResource) {
		return wrapMatcherInTransaction(() -> IsLinkedTo.linkedTo(myIdHelperService, myMdmLinkDaoSvc, theBaseResource));
	}

	protected Matcher<IAnyResource> possibleLinkedTo(IAnyResource... theBaseResource) {
		return wrapMatcherInTransaction(() -> IsPossibleLinkedTo.possibleLinkedTo(myIdHelperService, myMdmLinkDaoSvc, theBaseResource));
	}

	protected Matcher<IAnyResource> possibleMatchWith(IAnyResource... theBaseResource) {
		return wrapMatcherInTransaction(() -> IsPossibleMatchWith.possibleMatchWith(myIdHelperService, myMdmLinkDaoSvc, theBaseResource));
	}

	protected Matcher<IAnyResource> possibleDuplicateOf(IAnyResource... theBaseResource) {
		return wrapMatcherInTransaction(() -> IsPossibleDuplicateOf.possibleDuplicateOf(myIdHelperService, myMdmLinkDaoSvc, theBaseResource));
	}

	protected Matcher<IAnyResource> matchedToAGoldenResource() {
		return wrapMatcherInTransaction(() -> IsMatchedToAGoldenResource.matchedToAGoldenResource(myIdHelperService, myMdmLinkDaoSvc));
	}

	protected Patient getOnlyGoldenPatient() {
		List<IBaseResource> resources = getAllGoldenPatients();
		assertEquals(1, resources.size());
		return (Patient) resources.get(0);
	}


	@Nonnull
	protected List<IBaseResource> getAllGoldenPatients() {
		return getPatientsByTag(MdmConstants.CODE_GOLDEN_RECORD);
	}

	@Nonnull
	protected List<IBaseResource> getAllRedirectedGoldenPatients() {
		return getPatientsByTag(MdmConstants.CODE_GOLDEN_RECORD_REDIRECTED);
	}

	@Nonnull
	private List<IBaseResource> getPatientsByTag(String theCode) {
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		//TODO GGG ensure that this tag search works effectively.
		map.add("_tag", new TokenParam(MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS, theCode));
		SystemRequestDetails systemRequestDetails = SystemRequestDetails.forAllPartitions();
		IBundleProvider bundle = myPatientDao.search(map, systemRequestDetails);
		return bundle.getResources(0, 999);
	}


	@Nonnull
	protected MdmLink createResourcesAndBuildTestMDMLink() {
		Patient sourcePatient = createGoldenPatient();
		Patient patient = createPatient();

		MdmLink mdmLink = myMdmLinkDaoSvc.newMdmLink();
		mdmLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		mdmLink.setMatchResult(MdmMatchResultEnum.MATCH);
		mdmLink.setGoldenResourcePid(runInTransaction(() -> myIdHelperService.getPidOrNull(sourcePatient)));
		mdmLink.setSourcePid(runInTransaction(() -> myIdHelperService.getPidOrNull(patient)));
		return mdmLink;
	}

//	protected void loadMdmSearchParameters() {
//		myMdmSearchParameterLoader.daoUpdateMdmSearchParameters();
//		mySearchParamRegistry.forceRefresh();
//	}

	protected void logAllLinks() {
		ourLog.info("Logging all MDM Links:");
		List<MdmLink> links = myMdmLinkDao.findAll();
		for (MdmLink link : links) {
			ourLog.info(link.toString());
		}
	}

	protected void assertLinksMatchResult(MdmMatchResultEnum... theExpectedValues) {
		assertFields(MdmLink::getMatchResult, theExpectedValues);
	}

	protected void assertLinksCreatedNewResource(Boolean... theExpectedValues) {
		assertFields(MdmLink::getHadToCreateNewGoldenResource, theExpectedValues);
	}

	protected void assertLinksMatchedByEid(Boolean... theExpectedValues) {
		assertFields(MdmLink::getEidMatch, theExpectedValues);
	}

	public SearchParameterMap buildGoldenResourceSearchParameterMap() {
		SearchParameterMap spMap = new SearchParameterMap();
		spMap.setLoadSynchronous(true);
		spMap.add("_tag", new TokenParam(MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS, MdmConstants.CODE_GOLDEN_RECORD));
		return spMap;
	}

	private <T> void assertFields(Function<MdmLink, T> theAccessor, T... theExpectedValues) {
		List<MdmLink> links = myMdmLinkDao.findAll();
		assertEquals(theExpectedValues.length, links.size());
		for (int i = 0; i < links.size(); ++i) {
			assertEquals(theExpectedValues[i], theAccessor.apply(links.get(i)), "Value at index " + i + " was not equal");
		}
	}


	protected void print(String message, IBaseResource... theResource) {
		if (StringUtils.isNotEmpty(message)) {
			ourLog.info(message);
		}

		for (IBaseResource r : theResource) {
			ourLog.info(myFhirContext.newJsonParser().encodeResourceToString(r));
		}
	}

	protected void print(IBaseResource... theResource) {
		print(null, theResource);
	}


	protected void printLinks() {
		myMdmLinkDao.findAll().forEach(mdmLink -> {
			ourLog.info(String.valueOf(mdmLink));
		});
	}

	protected DaoMethodOutcome createDummyOrganization() {
		Organization org = new Organization();
		org.setId(DUMMY_ORG_ID);
		return myOrganizationDao.update(org);
	}

	@Nonnull
	protected MdmTransactionContext buildUpdateLinkMdmTransactionContext() {
		MdmTransactionContext retval = new MdmTransactionContext();
		retval.setResourceType("Patient");
		retval.setRestOperation(MdmTransactionContext.OperationType.UPDATE_LINK);
		return retval;
	}
}
