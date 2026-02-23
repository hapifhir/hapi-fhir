package ca.uhn.fhir.jpa.mdm;

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.config.MdmConsumerConfig;
import ca.uhn.fhir.jpa.mdm.config.MdmSubmitterConfig;
import ca.uhn.fhir.jpa.mdm.config.TestMdmConfigR4;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.mdm.helper.MdmLinkHelper;
import ca.uhn.fhir.jpa.mdm.matcher.GoldenResourceMatchingAssert;
import ca.uhn.fhir.jpa.mdm.svc.MdmMatchLinkSvc;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.IMdmLinkUpdaterSvc;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.mdm.rules.svc.MdmResourceMatcherSvc;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.annotation.Nonnull;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	MdmSubmitterConfig.class,
	MdmConsumerConfig.class,
	TestMdmConfigR4.class,
	SubscriptionProcessorConfig.class
})
abstract public class BaseMdmR4Test extends BaseResourceProviderR4Test {

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
	protected IFhirResourceDaoPatient<Patient> myPatientDao;
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
	protected IMdmLinkDao<JpaPid, MdmLink> myMdmLinkDao;
	@Autowired
	protected MdmLinkDaoSvc<JpaPid, MdmLink> myMdmLinkDaoSvc;
	@Autowired
	protected IIdHelperService<JpaPid> myIdHelperService;
	@Autowired
	protected MdmSettings myMdmSettings;
	@Autowired
	protected MdmMatchLinkSvc myMdmMatchLinkSvc;
	@Autowired
	protected EIDHelper myEIDHelper;
	protected ServletRequestDetails myRequestDetails;
	@Autowired
	SearchParamRegistryImpl mySearchParamRegistry;
	@Autowired
	protected IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	protected PartitionSettings myPartitionSettings;
	@Autowired
	protected IPartitionLookupSvc myPartitionLookupSvc;

	@Autowired
	protected IMdmLinkUpdaterSvc myMdmLinkUpdaterSvc;
	@Autowired
	protected MdmLinkHelper myLinkHelper;

	@BeforeEach
	public void beforeSetRequestDetails() {
		myRequestDetails = new ServletRequestDetails(myInterceptorBroadcaster);
	}

	@Override
	public void beforeUnregisterAllSubscriptions() {
		//no-op
	}

	@AfterEach
	public void after() throws IOException {
		myMdmLinkDao.deleteAll();
		assertEquals(0, myMdmLinkDao.count());
	}

	protected void saveLink(MdmLink theMdmLink) {
		myMdmLinkDaoSvc.save(theMdmLink);
	}

	protected GoldenResourceMatchingAssert mdmAssertThat(IAnyResource theResource) {
		return GoldenResourceMatchingAssert.assertThat(theResource, myIdHelperService, myMdmLinkDaoSvc);
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
		return createPatientWithUpdate(
			thePatient, theMdmManaged, isRedirect, false
		);
	}

	protected Patient createPatientWithUpdate(
		Patient thePatient,
		boolean theMdmManaged,
		boolean isRedirect,
		boolean theUseUpdateBool
	) {
		if (theMdmManaged) {
			MdmResourceUtil.setMdmManaged(thePatient);
			if (isRedirect) {
				MdmResourceUtil.setGoldenResourceRedirected(thePatient);
			} else {
				MdmResourceUtil.setGoldenResource(thePatient);
			}
		}

		Patient patient;
		if (theUseUpdateBool) {
			DaoMethodOutcome outcome = myPatientDao.update(thePatient);
			patient = (Patient) outcome.getResource();
		} else {
			DaoMethodOutcome outcome = myPatientDao.create(thePatient);
			patient = (Patient) outcome.getResource();
			patient.setId(outcome.getId());
		}
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

	public Patient createPatientOnPartition(
		Patient thePatient,
		boolean theMdmManaged,
		boolean isRedirect,
		RequestPartitionId theRequestPartitionId
	) {
		return createPatientOnPartition(
			thePatient, theMdmManaged, isRedirect, theRequestPartitionId, false
		);
	}

	@Nonnull
	protected Patient createPatientOnPartition(
		Patient thePatient,
		boolean theMdmManaged,
		boolean isRedirect,
		RequestPartitionId theRequestPartitionId,
		boolean theDoUpdate
	) {
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

		Patient patient;
		if (theDoUpdate) {
			DaoMethodOutcome outcome = myPatientDao.update(thePatient, systemRequestDetails);
			patient = (Patient) outcome.getResource();
			patient.setId(outcome.getId());
		} else {
			DaoMethodOutcome outcome = myPatientDao.create(thePatient, systemRequestDetails);
			patient = (Patient) outcome.getResource();
			patient.setId(outcome.getId());
		}
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

		Optional<MdmLink> matchedLinkForTargetPid = myMdmLinkDaoSvc.getMatchedLinkForSourcePid(runInTransaction(() -> myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theBaseResource)));
		if (matchedLinkForTargetPid.isPresent()) {
			JpaPid jpaPid = matchedLinkForTargetPid.get().getGoldenResourcePersistenceId();
			return (T) relevantDao.readByPid(jpaPid);
		} else {
			return null;
		}
	}

	protected <T extends IBaseResource> T getTargetResourceFromMdmLink(IMdmLink theMdmLink, String theResourceType) {
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theResourceType);
		return (T) resourceDao.readByPid(theMdmLink.getGoldenResourcePersistenceId());
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

		MdmLink mdmLink = (MdmLink) myMdmLinkDaoSvc.newMdmLink();
		mdmLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		mdmLink.setMatchResult(MdmMatchResultEnum.MATCH);
		mdmLink.setGoldenResourcePersistenceId(runInTransaction(() -> myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), sourcePatient)));
		mdmLink.setSourcePersistenceId(runInTransaction(() -> myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), patient)));
		return mdmLink;
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

	protected void assertLinksMatchScore(Double... theExpectedValues) {
		assertFields(MdmLink::getScore, theExpectedValues);
	}

	protected void assertLinksMatchVector(Long... theExpectedValues) {
		assertFields(MdmLink::getVector, theExpectedValues);
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
			ourLog.debug(myFhirContext.newJsonParser().encodeResourceToString(r));
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

	@Nonnull
	protected MdmTransactionContext buildUpdateResourceMdmTransactionContext() {
		MdmTransactionContext retval = new MdmTransactionContext();
		retval.setResourceType("Patient");
		retval.setRestOperation(MdmTransactionContext.OperationType.UPDATE_RESOURCE);
		return retval;
	}

	protected MdmLink createGoldenPatientAndLinkToSourcePatient(Long thePatientPid, MdmMatchResultEnum theMdmMatchResultEnum) {
		Patient patient = createPatient();

		MdmLink mdmLink = (MdmLink) myMdmLinkDaoSvc.newMdmLink();
		mdmLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		mdmLink.setMatchResult(theMdmMatchResultEnum);
		mdmLink.setCreated(new Date());
		mdmLink.setUpdated(new Date());
		mdmLink.setGoldenResourcePersistenceId(JpaPid.fromId(thePatientPid));
		mdmLink.setSourcePersistenceId(runInTransaction(()->myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), patient)));
		return myMdmLinkDao.save(mdmLink);
	}

	protected MdmLink createGoldenPatientAndLinkToSourcePatient(MdmMatchResultEnum theMdmMatchResultEnum, MdmLinkSourceEnum theMdmLinkSourceEnum, String theVersion, Date theCreateTime, Date theUpdateTime, boolean theLinkCreatedNewResource, Long theVector) {
		final Patient goldenPatient = createPatient();
		final Patient sourcePatient = createPatient();

		final MdmLink mdmLink = (MdmLink) myMdmLinkDaoSvc.newMdmLink();
		mdmLink.setLinkSource(theMdmLinkSourceEnum);
		mdmLink.setMatchResult(theMdmMatchResultEnum);
		mdmLink.setCreated(theCreateTime);
		mdmLink.setUpdated(theUpdateTime);
		mdmLink.setVersion(theVersion);
		mdmLink.setGoldenResourcePersistenceId(runInTransaction(()->myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), goldenPatient)));
		mdmLink.setSourcePersistenceId(runInTransaction(()->myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), sourcePatient)));
		mdmLink.setHadToCreateNewGoldenResource(theLinkCreatedNewResource);
		mdmLink.setVector(theVector);

		return myMdmLinkDao.save(mdmLink);
	}

	protected IBaseResource createResourceWithId(IBaseResource theResource, String theId, Enumerations.ResourceType theResourceType){
		theResource.setId(theId);
		DaoMethodOutcome daoMethodOutcome = null;
		switch (theResourceType){
			case PATIENT:
				((Patient) theResource).setActive(true);
				daoMethodOutcome = myPatientDao.update((Patient) theResource, new SystemRequestDetails());
				break;
			case PRACTITIONER:
				((Practitioner) theResource).setActive(true);
				daoMethodOutcome = myPractitionerDao.update((Practitioner) theResource, new SystemRequestDetails());
				break;
			default:
				throw new NotImplementedException("This method haven't been setup for: " + theResourceType);
		}
		theResource.setId(daoMethodOutcome.getId());
		return theResource;
	}
}
