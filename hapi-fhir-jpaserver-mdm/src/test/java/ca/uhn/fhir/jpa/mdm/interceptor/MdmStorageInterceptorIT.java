package ca.uhn.fhir.jpa.mdm.interceptor;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperConfig;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

import static ca.uhn.fhir.mdm.api.MdmConstants.CODE_GOLDEN_RECORD;
import static ca.uhn.fhir.mdm.api.MdmConstants.CODE_GOLDEN_RECORD_REDIRECTED;
import static ca.uhn.fhir.mdm.api.MdmConstants.CODE_HAPI_MDM_MANAGED;
import static ca.uhn.fhir.mdm.api.MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS;
import static ca.uhn.fhir.mdm.api.MdmConstants.SYSTEM_MDM_MANAGED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.slf4j.LoggerFactory.getLogger;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ContextConfiguration(classes = {MdmHelperConfig.class})
public class MdmStorageInterceptorIT extends BaseMdmR4Test {

	private static final Logger ourLog = getLogger(MdmStorageInterceptorIT.class);

	@RegisterExtension
	@Autowired
	public MdmHelperR4 myMdmHelper;
	@Autowired
	private IJpaIdHelperService myIdHelperService;

	@Test
	public void testCreatePractitioner() throws InterruptedException {
		myMdmHelper.createWithLatch(buildPractitionerWithNameAndId("somename", "some_id"));
		assertLinkCount(1);
	}

	private MdmLink getLinkByTargetId(IBaseResource theResource) {
		MdmLink example = new MdmLink();
		example.setSourcePid(theResource.getIdElement().getIdPartAsLong());
		return myMdmLinkDao.findAll(Example.of(example)).get(0);
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

	@Test
	public void testCreatePatientWithMdmTagForbidden() throws InterruptedException {
		//Creating a golden resource with the MDM-MANAGED tag should fail
		Patient patient = new Patient();
		patient.getMeta().addTag(SYSTEM_MDM_MANAGED, CODE_HAPI_MDM_MANAGED, "User is managed by MDM");
		try {
			myMdmHelper.doCreateResource(patient, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertThat(e.getMessage(), startsWith("Cannot create or modify Resources that are managed by MDM."));
		}
	}

	@Test
	public void testCreatePatientWithGoldenRecordTagForbidden() throws InterruptedException {
		Patient patient = new Patient();
		patient.getMeta().addTag(SYSTEM_GOLDEN_RECORD_STATUS, CODE_GOLDEN_RECORD, "Golden Record");
		try {
			myMdmHelper.doCreateResource(patient, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertThat(e.getMessage(), startsWith("Cannot create or modify Resources that are managed by MDM."));
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
			assertThat(e.getMessage(), startsWith("Cannot create or modify Resources that are managed by MDM."));
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
			assertThat(e.getMessage(), startsWith("Cannot create or modify Resources that are managed by MDM."));
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
			assertEquals("The HAPI-MDM tag on a resource may not be changed once created.", e.getMessage());
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
			assertThat(r.getMeta().getTag(SYSTEM_MDM_MANAGED, CODE_HAPI_MDM_MANAGED), is(notNullValue()));
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
			assertEquals("The HAPI-MDM tag on a resource may not be changed once created.", e.getMessage());
		}
	}

	@Test
	public void testMdmManagedGoldenResourceCannotBeModifiedByGoldenResourceUpdateRequest() throws InterruptedException {
		// When MDM is enabled, only the MDM system is allowed to modify GoldenResource links of GoldenResources with the MDM-MANAGED tag.
		Patient patient = new Patient();
		IIdType patientId = myMdmHelper.createWithLatch(buildPaulPatient()).getDaoMethodOutcome().getId().toUnqualifiedVersionless();

		patient.setId(patientId);

		// Updating a Golden Resource Patient who was created via MDM should fail.
		MdmLink mdmLink = runInTransaction(() -> myMdmLinkDaoSvc.getMatchedLinkForSourcePid(myIdHelperService.getPidOrNull(patient)).orElseThrow(() -> new IllegalStateException()));
		Long sourcePatientPid = mdmLink.getGoldenResourcePid();
		Patient goldenResourcePatient = myPatientDao.readByPid(new ResourcePersistentId(sourcePatientPid));
		goldenResourcePatient.setGender(Enumerations.AdministrativeGender.MALE);
		try {
			myMdmHelper.doUpdateResource(goldenResourcePatient, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertThat(e.getMessage(), startsWith("Cannot create or modify Resources that are managed by MDM."));
		}
	}

	@Test
	public void testMdmPointcutReceivesTransactionLogMessages() throws InterruptedException {
		MdmHelperR4.OutcomeAndLogMessageWrapper wrapper = myMdmHelper.createWithLatch(buildJanePatient());

		TransactionLogMessages mdmTransactionLogMessages = wrapper.getLogMessages();

		//There is no TransactionGuid here as there is no TransactionLog in this context.
		assertThat(mdmTransactionLogMessages.getTransactionGuid(), is(nullValue()));

		List<String> messages = mdmTransactionLogMessages.getValues();
		assertThat(messages.isEmpty(), is(false));
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
		assertThat(externalEids, hasSize(1));
		assertThat("some_new_eid", is(equalTo(externalEids.get(0).getValue())));
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
			assertThat(e.getMessage(), is(equalTo("While running with EID updates disabled, EIDs may not be updated on source resources")));
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
			assertThat(e.getMessage(), is(equalTo("While running with multiple EIDs disabled, source resources may have at most one EID.")));
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
