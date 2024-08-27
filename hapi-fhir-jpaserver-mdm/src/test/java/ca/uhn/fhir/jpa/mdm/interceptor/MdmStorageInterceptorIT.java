package ca.uhn.fhir.jpa.mdm.interceptor;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperConfig;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.IMdmLinkCreateSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkUpdaterSvc;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.model.MdmCreateOrUpdateParams;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static ca.uhn.fhir.mdm.api.MdmConstants.CODE_GOLDEN_RECORD_REDIRECTED;
import static ca.uhn.fhir.mdm.api.MdmConstants.CODE_HAPI_MDM_MANAGED;
import static ca.uhn.fhir.mdm.api.MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS;
import static ca.uhn.fhir.mdm.api.MdmConstants.SYSTEM_MDM_MANAGED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.slf4j.LoggerFactory.getLogger;

@ContextConfiguration(classes = {MdmHelperConfig.class})
public class MdmStorageInterceptorIT extends BaseMdmR4Test {

	private static final Logger ourLog = getLogger(MdmStorageInterceptorIT.class);

	@RegisterExtension
	@Autowired
	public MdmHelperR4 myMdmHelper;
	@Autowired
	private IIdHelperService<JpaPid> myIdHelperService;
	@Autowired
	private IMdmLinkUpdaterSvc myMdmLinkUpdaterSvc;
	@Autowired
	private IMdmLinkCreateSvc myMdmCreateSvc;


	@Override
	public void beforeUnregisterAllSubscriptions() {
		// noop
	}

	@Test
	public void testCreatePractitioner() throws InterruptedException {
		myMdmHelper.createWithLatch(buildPractitionerWithNameAndId("somename", "some_id"));
		assertLinkCount(1);
	}

	private MdmLink getLinkByTargetId(IBaseResource theResource) {
		MdmLink example = new MdmLink();
		example.setSourcePid(theResource.getIdElement().getIdPartAsLong());
		return (MdmLink) myMdmLinkDao.findAll(Example.of(example)).get(0);
	}

	@Test
	public void testSearchExpandingInterceptorWorks() {
		SearchParameterMap subject = new SearchParameterMap("subject", new ReferenceParam("Patient/123").setMdmExpand(true)).setLoadSynchronous(true);
		myObservationDao.search(subject);
	}

	@Test
	public void testDeleteGoldenResourceDeletesLinks() throws InterruptedException {
		myMdmHelper.createWithLatch(buildPaulPatient());
		assertLinkCount(1);
		Patient sourcePatient = getOnlyGoldenPatient();
		myPatientDao.delete(sourcePatient.getIdElement());
		assertLinkCount(0);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	public void deleteResourcesByUrl_withMultipleDeleteCatchingSourceAndGoldenResource_deletesWithoutThrowing(boolean theIncludeOtherResources) throws InterruptedException {
		// setup
		boolean allowMultipleDelete = myStorageSettings.isAllowMultipleDelete();
		myStorageSettings.setAllowMultipleDelete(true);

		int linkCount = 0;
		int resourceCount = 0;
		myMdmHelper.createWithLatch(buildJanePatient());
		resourceCount += 2; // patient + golden
		linkCount++;

		// add some other resources to make it more complex
		if (theIncludeOtherResources) {
			Date birthday = new Date();
			Patient patient = new Patient();
			patient.getNameFirstRep().addGiven("yui");
			patient.setBirthDate(birthday);
			patient.setTelecom(Collections.singletonList(new ContactPoint()
				.setSystem(ContactPoint.ContactPointSystem.PHONE)
				.setValue("555-567-5555")));
			DateType dateType = new DateType(birthday);
			patient.addIdentifier().setSystem(TEST_ID_SYSTEM).setValue("ID.YUI.123");
			dateType.setPrecision(TemporalPrecisionEnum.DAY);
			patient.setBirthDateElement(dateType);
			patient.setActive(true);
			for (int i = 0; i < 2; i++) {
				String familyName = i == 0 ? "hirasawa" : "kotegawa";
				patient.getNameFirstRep().setFamily(familyName);
				myMdmHelper.createWithLatch(patient);
				resourceCount++;
				linkCount++; // every resource creation creates 1 link
			}
			resourceCount++; // for the Golden Resource

			// verify we have at least this many resources
			SearchParameterMap map = new SearchParameterMap();
			map.setLoadSynchronous(true);
			IBundleProvider provider = myPatientDao.search(map, new SystemRequestDetails());
			assertEquals(resourceCount, provider.size());

			// verify we have the links
			assertEquals(linkCount, myMdmLinkDao.count());
		}

		try {
			// test
			// filter will delete everything
			DeleteMethodOutcome outcome = myPatientDao.deleteByUrl("Patient?_lastUpdated=ge2024-01-01", new SystemRequestDetails());

			// validation
			assertNotNull(outcome);
			List<MdmLink> links = myMdmLinkDao.findAll();
			assertTrue(links.isEmpty());
			SearchParameterMap map = new SearchParameterMap();
			map.setLoadSynchronous(true);
			IBundleProvider provider = myPatientDao.search(map, new SystemRequestDetails());
			assertTrue(provider.getAllResources().isEmpty());
		} finally {
			myStorageSettings.setAllowMultipleDelete(allowMultipleDelete);
		}
	}

	@Test
	public void testGoldenResourceDeleted_whenOnlyMatchedResourceDeleted() throws InterruptedException {
		// Given
		Patient paulPatient = buildPaulPatient();
		myMdmHelper.createWithLatch(paulPatient);
		assertLinkCount(1);
		Patient goldenPatient = getOnlyGoldenPatient();

		// When
		myPatientDao.delete(paulPatient.getIdElement());

		// Then
		List<IBaseResource> resources = myPatientDao.search(new SearchParameterMap(), SystemRequestDetails.forAllPartitions()).getAllResources();
		assertThat(resources).isEmpty();
		assertLinkCount(0);

		try {
			myPatientDao.read(goldenPatient.getIdElement().toVersionless());
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Constants.STATUS_HTTP_404_NOT_FOUND, e.getStatusCode());
		}
	}

	@Test
	public void testGoldenResourceDeleted_andNewGoldenCreated_whenOnlyMatchDeletedButPossibleMatchExists() throws InterruptedException {
		// Given
		Patient paulPatient = buildPaulPatient();
		paulPatient.setActive(true);
		myMdmHelper.createWithLatch(paulPatient);

		Patient paulPatientPossibleMatch = buildPaulPatient();
		paulPatientPossibleMatch.getNameFirstRep().setFamily("DifferentName");
		myMdmHelper.createWithLatch(paulPatientPossibleMatch);
		assertLinksMatchResult(MdmMatchResultEnum.MATCH, MdmMatchResultEnum.POSSIBLE_MATCH);

		// When
		myPatientDao.delete(paulPatient.getIdElement());

		// Then
		List<IBaseResource> resources = myPatientDao.search(new SearchParameterMap(), SystemRequestDetails.forAllPartitions()).getAllResources();
		assertThat(resources).hasSize(2);

		assertLinksMatchResult(MdmMatchResultEnum.MATCH);
	}

	@Test
	public void testGoldenResourceDeleted_andNewGoldenCreated_whenOnlyMatchDeletedButMultiplePossibleMatchesExist() throws InterruptedException {
		// Given
		Patient paulPatient = buildPaulPatient();
		paulPatient.setActive(true);
		myMdmHelper.createWithLatch(paulPatient);

		Patient paulPatientPossibleMatch = buildPaulPatient();
		paulPatientPossibleMatch.setActive(true);
		paulPatientPossibleMatch.getNameFirstRep().setFamily("DifferentName");
		myMdmHelper.createWithLatch(paulPatientPossibleMatch);

		Patient paulPatientPossibleMatch2 = buildPaulPatient();
		paulPatientPossibleMatch2.setActive(true);
		paulPatientPossibleMatch2.getNameFirstRep().setFamily("AnotherPerson");
		myMdmHelper.createWithLatch(paulPatientPossibleMatch2);

		assertLinksMatchResult(MdmMatchResultEnum.MATCH, MdmMatchResultEnum.POSSIBLE_MATCH, MdmMatchResultEnum.POSSIBLE_MATCH);

		logAllTokenIndexes();

		// When
		myPatientDao.delete(paulPatient.getIdElement(), new SystemRequestDetails());

		logAllTokenIndexes();

		// Then
		List<IBaseResource> resources = myPatientDao.search(new SearchParameterMap(), SystemRequestDetails.forAllPartitions()).getAllResources();
		assertThat(resources).hasSize(3);

		assertLinksMatchResult(MdmMatchResultEnum.MATCH, MdmMatchResultEnum.POSSIBLE_MATCH);
	}

	@Test
	public void testDeleteSourceResource_whereGoldenResourceIsPossibleDuplicate() throws InterruptedException {
		// Given
		Patient paulPatient = buildPaulPatient();
		paulPatient.setActive(true);
		myMdmHelper.createWithLatch(paulPatient);

		Patient paulPatientPossibleMatch = buildPaulPatient();
		paulPatientPossibleMatch.setActive(true);
		paulPatientPossibleMatch.getNameFirstRep().setFamily("DifferentName");
		myMdmHelper.createWithLatch(paulPatientPossibleMatch);
		MdmCreateOrUpdateParams params = new MdmCreateOrUpdateParams();
		params.setMdmContext(getPatientUpdateLinkContext());
		params.setGoldenResource(getOnlyGoldenPatient());
		params.setSourceResource(paulPatientPossibleMatch);
		params.setMatchResult(MdmMatchResultEnum.NO_MATCH);
		myMdmLinkUpdaterSvc.updateLink(params);

		Patient paulPatientPossibleMatch2 = buildPaulPatient();
		paulPatientPossibleMatch2.setActive(true);
		paulPatientPossibleMatch2.getNameFirstRep().setFamily("AnotherPerson");
		myMdmHelper.createWithLatch(paulPatientPossibleMatch2);

		assertLinkCount(6);

		// When
		myPatientDao.delete(paulPatient.getIdElement());

		// Then
		/* Paul 1 MATCH to GR1 --> DELETED
		   Paul 2 NO_MATCH to GR1 --> DELETED
		   Paul 2 MATCH to GR2 --> KEPT
		   Paul 3 POSSIBLE_MATCH to GR1 --> DELETED
		   Paul 3 POSSIBLE_MATCH to GR2 --> KEPT
		   GR1 POSSIBLE_DUPLICATE GR2 --> DELETED */
		List<IBaseResource> resources = myPatientDao.search(new SearchParameterMap(), SystemRequestDetails.forAllPartitions()).getAllResources();
		assertThat(resources).hasSize(3);

		assertLinksMatchResult(MdmMatchResultEnum.MATCH, MdmMatchResultEnum.POSSIBLE_MATCH);
	}

	@Test
	public void testDeleteSourceResource_withNoMatchLink_whereGoldenResourceIsPossibleDuplicate() throws InterruptedException {
		// Given
		Patient paulPatient = buildPaulPatient();
		paulPatient.setActive(true);
		myMdmHelper.createWithLatch(paulPatient);

		Patient paulPatientPossibleMatch = buildPaulPatient();
		paulPatientPossibleMatch.setActive(true);
		paulPatientPossibleMatch.getNameFirstRep().setFamily("DifferentName");
		myMdmHelper.createWithLatch(paulPatientPossibleMatch);

		MdmCreateOrUpdateParams params = new MdmCreateOrUpdateParams();
		params.setGoldenResource(getOnlyGoldenPatient());
		params.setSourceResource(paulPatientPossibleMatch);
		params.setMdmContext(getPatientUpdateLinkContext());
		params.setMatchResult(MdmMatchResultEnum.NO_MATCH);
		myMdmLinkUpdaterSvc.updateLink(params);

		Patient paulPatientPossibleMatch2 = buildPaulPatient();
		paulPatientPossibleMatch2.setActive(true);
		paulPatientPossibleMatch2.getNameFirstRep().setFamily("AnotherPerson");
		myMdmHelper.createWithLatch(paulPatientPossibleMatch2);

		assertLinkCount(6);

		// When
		myPatientDao.delete(paulPatientPossibleMatch.getIdElement());

		// Then
		/* Paul 1 MATCH to GR1 --> DELETED
		   Paul 2 NO_MATCH to GR1 --> DELETED
		   Paul 2 MATCH to GR2 --> KEPT
		   Paul 3 POSSIBLE_MATCH to GR1 --> DELETED
		   Paul 3 POSSIBLE_MATCH to GR2 --> KEPT
		   GR1 POSSIBLE_DUPLICATE GR2 --> DELETED */
		List<IBaseResource> resources = myPatientDao.search(new SearchParameterMap(), SystemRequestDetails.forAllPartitions()).getAllResources();
		assertThat(resources).hasSize(3);

		assertLinksMatchResult(MdmMatchResultEnum.MATCH, MdmMatchResultEnum.POSSIBLE_MATCH);
	}

	@Test
	public void testGoldenResourceKept_whenAutoDeleteDisabled() throws InterruptedException {
		// Given
		myMdmSettings.setAutoExpungeGoldenResources(false);
		Patient paulPatient = buildPaulPatient();
		myMdmHelper.createWithLatch(paulPatient);
		assertLinkCount(1);
		Patient goldenPatient = getOnlyGoldenPatient();

		// When
		myPatientDao.delete(paulPatient.getIdElement());

		// Then
		try {
			myPatientDao.read(goldenPatient.getIdElement().toVersionless());
			fail();
		} catch (ResourceGoneException e) {
			assertLinkCount(0);
		} finally {
			myMdmSettings.setAutoExpungeGoldenResources(true);
		}
	}

	private MdmTransactionContext getPatientUpdateLinkContext() {
		MdmTransactionContext ctx = new MdmTransactionContext();
		ctx.setRestOperation(MdmTransactionContext.OperationType.UPDATE_LINK);
		ctx.setResourceType("Patient");
		return ctx;
	}

	@Test
	public void testCreatePatientWithMdmTagForbidden() throws InterruptedException {
		//Creating a golden resource with the MDM-MANAGED tag should fail
		Patient patient = new Patient();
		patient.getMeta().addTag(SYSTEM_MDM_MANAGED, CODE_HAPI_MDM_MANAGED, "User is managed by MDM");
		try {
			myMdmHelper.doCreateResource(patient, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertThat(e.getMessage()).startsWith("HAPI-0765: Cannot create or modify Resources that are managed by MDM.");
		}
	}

	@Test
	public void testCreatePatientWithGoldenRecordTagForbidden() throws InterruptedException {
		Patient patient = myMdmHelper.buildGoldenPatient();
		try {
			myMdmHelper.doCreateResource(patient, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertThat(e.getMessage()).startsWith("HAPI-0765: Cannot create or modify Resources that are managed by MDM.");
		}
	}

	@Test
	public void testCreateMedicationWithGoldenRecordRedirectTagForbidden() throws InterruptedException {
		Medication medication = new Medication();
		medication.getMeta().addTag(SYSTEM_GOLDEN_RECORD_STATUS, CODE_GOLDEN_RECORD_REDIRECTED, "Golden Record");
		try {
			myMdmHelper.doCreateResource(medication, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertThat(e.getMessage()).startsWith("HAPI-0765: Cannot create or modify Resources that are managed by MDM.");
		}
	}

	// TODO This test often fails in IntelliJ with the error message:
	// "The operation has failed with a version constraint failure. This generally means that two clients/threads were
	// trying to update the same resource at the same time, and this request was chosen as the failing request."
	@Test
	public void testCreatingGoldenResourceWithInsufficentMDMAttributesIsNotMDMProcessed() throws InterruptedException {
		myMdmHelper.doCreateResource(new Patient(), true);
		assertLinkCount(0);
	}

	@Test
	public void testCreatingPatientWithOneOrMoreMatchingAttributesIsMDMProcessed() throws InterruptedException {
		myMdmHelper.createWithLatch(buildPaulPatient());
		assertLinkCount(1);
	}

	@Test
	public void testCreateOrganizationWithMdmTagForbidden() throws InterruptedException {
		//Creating a organization with the MDM-MANAGED tag should fail
		Organization organization = new Organization();
		organization.getMeta().addTag(SYSTEM_MDM_MANAGED, CODE_HAPI_MDM_MANAGED, "User is managed by MDM");
		try {
			myMdmHelper.doCreateResource(organization, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertThat(e.getMessage()).startsWith("HAPI-0765: Cannot create or modify Resources that are managed by MDM.");
		}
	}

	@Test
	public void testUpdateOrganizationWithMdmTagForbidden() throws InterruptedException {
		//Creating a organization with the MDM-MANAGED tag should fail
		Organization organization = new Organization();
		myMdmHelper.doCreateResource(organization, true);
		organization.getMeta().addTag(SYSTEM_MDM_MANAGED, CODE_HAPI_MDM_MANAGED, "User is managed by MDM");
		try {
			myMdmHelper.doUpdateResource(organization, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("HAPI-0764: The HAPI-MDM tag on a resource may not be changed once created.", e.getMessage());
		}
	}

	@Test
	public void testGoldenResourceRecordsManagedByMdmAllShareSameTag() throws InterruptedException {
		myMdmHelper.createWithLatch(buildJanePatient());
		myMdmHelper.createWithLatch(buildPaulPatient());

		//TODO GGG MDM: this test is out of date, since we now are using golden record Patients
		IBundleProvider search = myPatientDao.search(buildGoldenResourceSearchParameterMap());
		List<IBaseResource> resources = search.getResources(0, search.size());

		for (IBaseResource r : resources) {
			assertNotNull(r.getMeta().getTag(SYSTEM_MDM_MANAGED, CODE_HAPI_MDM_MANAGED));
		}
	}

	@Test
	public void testNonMdmManagedGoldenResourceCannotHaveMdmManagedTagAddedToThem() {
		// GoldenResource created manually.
		Patient patient = new Patient();
		DaoMethodOutcome daoMethodOutcome = myMdmHelper.doCreateResource(patient, true);
		assertNotNull(daoMethodOutcome.getId());

		//Updating that patient to set them as MDM managed is not allowed.
		patient.getMeta().addTag(SYSTEM_MDM_MANAGED, CODE_HAPI_MDM_MANAGED, "User is managed by MDM");
		try {
			myMdmHelper.doUpdateResource(patient, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("HAPI-0764: The HAPI-MDM tag on a resource may not be changed once created.", e.getMessage());
		}
	}

	@Test
	public void testMdmManagedGoldenResourceCannotBeModifiedByGoldenResourceUpdateRequest() throws InterruptedException {
		// When MDM is enabled, only the MDM system is allowed to modify GoldenResource links of GoldenResources with the MDM-MANAGED tag.
		Patient patient = new Patient();
		IIdType patientId = myMdmHelper.createWithLatch(buildPaulPatient()).getDaoMethodOutcome().getId().toUnqualifiedVersionless();

		patient.setId(patientId);

		// Updating a Golden Resource Patient who was created via MDM should fail.
		MdmLink mdmLink = runInTransaction(() -> myMdmLinkDaoSvc.getMatchedLinkForSourcePid(myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), patient)).orElseThrow(() -> new IllegalStateException()));
		Long sourcePatientPid = mdmLink.getGoldenResourcePersistenceId().getId();
		Patient goldenResourcePatient = myPatientDao.readByPid(JpaPid.fromId(sourcePatientPid));
		goldenResourcePatient.setGender(Enumerations.AdministrativeGender.MALE);
		try {
			myMdmHelper.doUpdateResource(goldenResourcePatient, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertThat(e.getMessage()).startsWith("HAPI-0765: Cannot create or modify Resources that are managed by MDM.");
		}
	}

	@Test
	public void testMdmPointcutReceivesTransactionLogMessages() throws InterruptedException {
		MdmHelperR4.OutcomeAndLogMessageWrapper wrapper = myMdmHelper.createWithLatch(buildJanePatient());

		TransactionLogMessages mdmTransactionLogMessages = wrapper.getLogMessages();

		//There is no TransactionGuid here as there is no TransactionLog in this context.
		assertNull(mdmTransactionLogMessages.getTransactionGuid());

		List<String> messages = mdmTransactionLogMessages.getValues();
		assertEquals(false, messages.isEmpty());
	}

	@Test
	public void testWhenASingularPatientUpdatesExternalEidThatGoldenResourceEidIsUpdated() throws InterruptedException {
		Patient jane = addExternalEID(buildJanePatient(), "some_eid");
		MdmHelperR4.OutcomeAndLogMessageWrapper latch = myMdmHelper.createWithLatch(jane);
		jane.setId(latch.getDaoMethodOutcome().getId());
		clearExternalEIDs(jane);
		jane = addExternalEID(jane, "some_new_eid");

		MdmHelperR4.OutcomeAndLogMessageWrapper outcomeWrapper = myMdmHelper.updateWithLatch(jane);


		IAnyResource patient = getGoldenResourceFromTargetResource(jane);


		List<CanonicalEID> externalEids = myEIDHelper.getExternalEid(patient);
		assertThat(externalEids).hasSize(1);
		assertEquals(externalEids.get(0).getValue(), "some_new_eid");
	}

	@Test
	public void testWhenEidUpdatesAreDisabledForbidsUpdatesToEidsOnTargets() throws InterruptedException {
		setPreventEidUpdates(true);
		Patient jane = addExternalEID(buildJanePatient(), "some_eid");
		MdmHelperR4.OutcomeAndLogMessageWrapper latch = myMdmHelper.createWithLatch(jane);
		jane.setId(latch.getDaoMethodOutcome().getId());
		clearExternalEIDs(jane);
		jane = addExternalEID(jane, "some_new_eid");
		try {
			myMdmHelper.doUpdateResource(jane, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("HAPI-0763: While running with EID updates disabled, EIDs may not be updated on source resources", e.getMessage());
		}
		setPreventEidUpdates(false);
	}

	@Test
	public void testWhenMultipleEidsAreDisabledThatTheInterceptorRejectsCreatesWithThem() {
		setPreventMultipleEids(true);
		Patient patient = buildJanePatient();
		addExternalEID(patient, "123");
		addExternalEID(patient, "456");
		try {
			myMdmHelper.doCreateResource(patient, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("HAPI-0766: While running with multiple EIDs disabled, source resources may have at most one EID.", e.getMessage());
		}

		setPreventMultipleEids(false);
	}

	@Test
	public void testInterceptorHandlesNonMdmResources() {
		setPreventEidUpdates(true);

		//Create some arbitrary resource.
		SearchParameter fooSp = new SearchParameter();
		fooSp.setCode("foo");
		fooSp.addBase("Bundle");
		fooSp.setType(Enumerations.SearchParamType.REFERENCE);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Bundle.entry[0].resource.as(Composition).encounter");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);

		myMdmHelper.doCreateResource(fooSp, true);
		fooSp.setXpathUsage(SearchParameter.XPathUsageType.PHONETIC);
		myMdmHelper.doUpdateResource(fooSp, true);
	}

	@Test
	public void testPatientsWithNoEIDCanBeUpdated() throws InterruptedException {
		setPreventEidUpdates(true);
		Patient p = buildPaulPatient();
		MdmHelperR4.OutcomeAndLogMessageWrapper wrapper = myMdmHelper.createWithLatch(p);

		p.setId(wrapper.getDaoMethodOutcome().getId());
		p.setBirthDate(new Date());
		myMdmHelper.updateWithLatch(p);
		setPreventEidUpdates(false);
	}

	@Test
	public void testPatientsCanHaveEIDAddedInStrictMode() throws InterruptedException {
		setPreventEidUpdates(true);
		Patient p = buildPaulPatient();
		MdmHelperR4.OutcomeAndLogMessageWrapper messageWrapper = myMdmHelper.createWithLatch(p);
		p.setId(messageWrapper.getDaoMethodOutcome().getId());
		addExternalEID(p, "external eid");
		myMdmHelper.updateWithLatch(p);
		setPreventEidUpdates(false);
	}

	private void setPreventEidUpdates(boolean thePrevent) {
		((MdmSettings) myMdmSettings).setPreventEidUpdates(thePrevent);
	}

	private void setPreventMultipleEids(boolean thePrevent) {
		((MdmSettings) myMdmSettings).setPreventMultipleEids(thePrevent);
	}

}
