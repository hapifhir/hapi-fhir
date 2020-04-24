package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.TestUtil;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Appointment.AppointmentStatus;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.util.collections.ListUtil;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FhirResourceDaoR4SearchCustomSearchParamTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4SearchCustomSearchParamTest.class);

	@After
	public void after() {
		myDaoConfig.setValidateSearchParameterExpressionsOnSave(new DaoConfig().isValidateSearchParameterExpressionsOnSave());
	}

	@Before
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
		myModelConfig.setDefaultSearchParamsCanBeOverridden(new ModelConfig().isDefaultSearchParamsCanBeOverridden());
	}


	@Test
	public void testBundleComposition() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.setCode("foo");
		fooSp.addBase("Bundle");
		fooSp.setType(Enumerations.SearchParamType.REFERENCE);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Bundle.entry[0].resource.as(Composition).encounter");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(fooSp));

		mySearchParameterDao.create(fooSp, mySrd);
		mySearchParamRegistry.forceRefresh();

		Encounter enc = new Encounter();
		enc.setStatus(Encounter.EncounterStatus.ARRIVED);
		String encId = myEncounterDao.create(enc).getId().toUnqualifiedVersionless().getValue();

		Composition composition = new Composition();
		composition.getEncounter().setReference(encId);

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.DOCUMENT);
		bundle.addEntry().setResource(composition);

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
		String bundleId = myBundleDao.create(bundle).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("foo", new ReferenceParam(encId));
		IBundleProvider results = myBundleDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(results), hasItems(bundleId));

	}


	@Test
	public void testCreateInvalidNoBase() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.gender");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("SearchParameter.base is missing", e.getMessage());
		}
	}

	@Test
	@Ignore
	public void testCreateInvalidParamInvalidResourceName() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("PatientFoo.gender");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("Invalid SearchParameter.expression value \"PatientFoo.gender\": Unknown resource name \"PatientFoo\" (this name is not known in FHIR version \"R4\")", e.getMessage());
		}
	}

	@Test
	public void testCreateInvalidParamNoPath() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("SearchParameter.expression is missing", e.getMessage());
		}
	}

	@Test
	@Ignore
	public void testCreateInvalidParamNoResourceName() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("gender");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("Invalid SearchParameter.expression value \"gender\". Must start with a resource name", e.getMessage());
		}
	}

	@Test
	public void testCreateInvalidParamParamNullStatus() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.gender");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(null);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("SearchParameter.status is missing or invalid", e.getMessage());
		}

	}

	@Test
	public void testCreateSearchParameterOnSearchParameterDoesntCauseEndlessReindexLoop() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.setCode("foo");
		fooSp.addBase("SearchParameter");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("SearchParameter.code");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);

		mySearchParameterDao.create(fooSp, mySrd);

		assertEquals(1, myResourceReindexingSvc.forceReindexingPass());
		myResourceReindexingSvc.forceReindexingPass();
		myResourceReindexingSvc.forceReindexingPass();
		myResourceReindexingSvc.forceReindexingPass();
		myResourceReindexingSvc.forceReindexingPass();
		assertEquals(0, myResourceReindexingSvc.forceReindexingPass());

	}

	@Test
	public void testCustomReferenceParameter() throws Exception {
		SearchParameter sp = new SearchParameter();
		sp.addBase("Patient");
		sp.setCode("myDoctor");
		sp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		sp.setTitle("My Doctor");
		sp.setExpression("Patient.extension('http://fmcna.com/myDoctor').value.as(Reference)");
		sp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		sp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(sp);

		mySearchParamRegistry.forceRefresh();

		org.hl7.fhir.r4.model.Practitioner pract = new org.hl7.fhir.r4.model.Practitioner();
		pract.setId("A");
		pract.addName().setFamily("PRACT");
		myPractitionerDao.update(pract);

		Patient pat = myFhirCtx.newJsonParser().parseResource(Patient.class, loadClasspath("/r4/custom_resource_patient.json"));
		IIdType pid = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		params.add("myDoctor", new ReferenceParam("A"));
		IBundleProvider outcome = myPatientDao.search(params);
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		ourLog.info("IDS: " + ids);
		assertThat(ids, Matchers.contains(pid.getValue()));
	}


	@Test
	public void testIndexIntoBundle() {
		SearchParameter sp = new SearchParameter();
		sp.addBase("Bundle");
		sp.setCode("messageid");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setTitle("Message ID");
		sp.setExpression("Bundle.entry.resource.as(MessageHeader).id");
		sp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		sp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(sp));
		mySearchParameterDao.create(sp);

		mySearchParamRegistry.forceRefresh();

		MessageHeader messageHeader = new MessageHeader();
		messageHeader.setId("123");
		messageHeader.setDefinition("Hello");
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.MESSAGE);
		bundle.addEntry()
			.setResource(messageHeader);

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
		myBundleDao.create(bundle);

		SearchParameterMap params = new SearchParameterMap();
		params.add("messageid", new TokenParam("123"));
		IBundleProvider outcome = myBundleDao.search(params);
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		ourLog.info("IDS: " + ids);
		assertThat(ids, not(empty()));
	}


	@Test
	public void testExtensionWithNoValueIndexesWithoutFailure() {
		SearchParameter eyeColourSp = new SearchParameter();
		eyeColourSp.addBase("Patient");
		eyeColourSp.setCode("eyecolour");
		eyeColourSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		eyeColourSp.setTitle("Eye Colour");
		eyeColourSp.setExpression("Patient.extension('http://acme.org/eyecolour')");
		eyeColourSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		eyeColourSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(eyeColourSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient p1 = new Patient();
		p1.setActive(true);
		p1.addExtension().setUrl("http://acme.org/eyecolour").addExtension().setUrl("http://foo").setValue(new StringType("VAL"));
		IIdType p1id = myPatientDao.create(p1).getId().toUnqualifiedVersionless();

	}

	@Test
	public void testIncludeExtensionReferenceAsRecurse() {
		SearchParameter attendingSp = new SearchParameter();
		attendingSp.addBase("Patient");
		attendingSp.setCode("attending");
		attendingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		attendingSp.setTitle("Attending");
		attendingSp.setExpression("Patient.extension('http://acme.org/attending')");
		attendingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		attendingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		attendingSp.getTarget().add(new CodeType("Practitioner"));
		IIdType spId = mySearchParameterDao.create(attendingSp, mySrd).getId().toUnqualifiedVersionless();

		mySearchParamRegistry.forceRefresh();

		Practitioner p1 = new Practitioner();
		p1.addName().setFamily("P1");
		IIdType p1id = myPractitionerDao.create(p1).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.addName().setFamily("P2");
		p2.addExtension().setUrl("http://acme.org/attending").setValue(new Reference(p1id));
		IIdType p2id = myPatientDao.create(p2).getId().toUnqualifiedVersionless();

		Appointment app = new Appointment();
		app.addParticipant().getActor().setReference(p2id.getValue());
		IIdType appId = myAppointmentDao.create(app).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.addInclude(new Include("Appointment:patient", true));
		map.addInclude(new Include("Patient:attending", true));
		results = myAppointmentDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(appId.getValue(), p2id.getValue(), p1id.getValue()));

	}



	@Test
	public void testOverrideAndDisableBuiltInSearchParametersWithOverridingDisabled() {
		myModelConfig.setDefaultSearchParamsCanBeOverridden(false);

		SearchParameter memberSp = new SearchParameter();
		memberSp.setCode("member");
		memberSp.addBase("Group");
		memberSp.setType(Enumerations.SearchParamType.REFERENCE);
		memberSp.setExpression("Group.member.entity");
		memberSp.setStatus(Enumerations.PublicationStatus.RETIRED);
		mySearchParameterDao.create(memberSp, mySrd);

		SearchParameter identifierSp = new SearchParameter();
		identifierSp.setCode("identifier");
		identifierSp.addBase("Group");
		identifierSp.setType(Enumerations.SearchParamType.TOKEN);
		identifierSp.setExpression("Group.identifier");
		identifierSp.setStatus(Enumerations.PublicationStatus.RETIRED);
		mySearchParameterDao.create(identifierSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient p = new Patient();
		p.addName().addGiven("G");
		IIdType pid = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		Group g = new Group();
		g.addIdentifier().setSystem("urn:foo").setValue("bar");
		g.addMember().getEntity().setReferenceElement(pid);
		myGroupDao.create(g);

		assertThat(myResourceLinkDao.findAll(), not(empty()));
		assertThat(ListUtil.filter(myResourceIndexedSearchParamTokenDao.findAll(), new ListUtil.Filter<ResourceIndexedSearchParamToken>() {
			@Override
			public boolean isOut(ResourceIndexedSearchParamToken object) {
				return !object.getResourceType().equals("Group") || object.isMissing();
			}
		}), not(empty()));
	}

	@Test
	public void testOverrideAndDisableBuiltInSearchParametersWithOverridingEnabled() {
		myModelConfig.setDefaultSearchParamsCanBeOverridden(true);

		SearchParameter memberSp = new SearchParameter();
		memberSp.setCode("member");
		memberSp.addBase("Group");
		memberSp.setType(Enumerations.SearchParamType.REFERENCE);
		memberSp.setExpression("Group.member.entity");
		memberSp.setStatus(Enumerations.PublicationStatus.RETIRED);
		mySearchParameterDao.create(memberSp, mySrd);

		SearchParameter identifierSp = new SearchParameter();
		identifierSp.setCode("identifier");
		identifierSp.addBase("Group");
		identifierSp.setType(Enumerations.SearchParamType.TOKEN);
		identifierSp.setExpression("Group.identifier");
		identifierSp.setStatus(Enumerations.PublicationStatus.RETIRED);
		mySearchParameterDao.create(identifierSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient p = new Patient();
		p.addName().addGiven("G");
		IIdType pid = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		Group g = new Group();
		g.addIdentifier().setSystem("urn:foo").setValue("bar");
		g.addMember().getEntity().setReferenceElement(pid);
		myGroupDao.create(g);

		assertThat(myResourceLinkDao.findAll(), empty());
		assertThat(ListUtil.filter(myResourceIndexedSearchParamTokenDao.findAll(), new ListUtil.Filter<ResourceIndexedSearchParamToken>() {
			@Override
			public boolean isOut(ResourceIndexedSearchParamToken object) {
				return !object.getResourceType().equals("Group") || object.isMissing();
			}
		}), empty());
	}

	/**
	 * See #863
	 */
	@Test
	public void testParamWithMultipleBases() {
		SearchParameter sp = new SearchParameter();
		sp.setUrl("http://clinicalcloud.solutions/fhir/SearchParameter/request-reason");
		sp.setName("reason");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setCode("reason");
		sp.addBase("MedicationRequest");
		sp.addBase("ServiceRequest");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setExpression("MedicationRequest.reasonReference | ServiceRequest.reasonReference");
		sp.addTarget("Condition");
		sp.addTarget("Observation");
		mySearchParameterDao.create(sp);
		mySearchParamRegistry.forceRefresh();

		Condition condition = new Condition();
		condition.getCode().setText("A condition");
		String conditionId = myConditionDao.create(condition).getId().toUnqualifiedVersionless().getValue();

		MedicationRequest mr = new MedicationRequest();
		mr.addReasonReference().setReference(conditionId);
		String mrId = myMedicationRequestDao.create(mr).getId().toUnqualifiedVersionless().getValue();

		ServiceRequest pr = new ServiceRequest();
		pr.addReasonReference().setReference(conditionId);
		myServiceRequestDao.create(pr);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("reason", new ReferenceParam(conditionId));
		List<String> results = toUnqualifiedVersionlessIdValues(myMedicationRequestDao.search(map));
		assertThat(results.toString(), results, contains(mrId));
	}

	/**
	 * See #863
	 */
	@Test
	public void testParamWithMultipleBasesToken() {
		SearchParameter sp = new SearchParameter();
		sp.setUrl("http://clinicalcloud.solutions/fhir/SearchParameter/request-reason");
		sp.setName("reason");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setCode("reason");
		sp.addBase("MedicationRequest");
		sp.addBase("ServiceRequest");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setExpression("MedicationRequest.reasonCode | ServiceRequest.reasonCode");
		mySearchParameterDao.create(sp);
		mySearchParamRegistry.forceRefresh();

		MedicationRequest mr = new MedicationRequest();
		mr.addReasonCode().addCoding().setSystem("foo").setCode("bar");
		String mrId = myMedicationRequestDao.create(mr).getId().toUnqualifiedVersionless().getValue();

		ServiceRequest pr = new ServiceRequest();
		pr.addReasonCode().addCoding().setSystem("foo").setCode("bar");
		myServiceRequestDao.create(pr);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("reason", new TokenParam("foo", "bar"));
		List<String> results = toUnqualifiedVersionlessIdValues(myMedicationRequestDao.search(map));
		assertThat(results, contains(mrId));
	}

	@Test
	public void testRejectSearchParamWithInvalidExpression() {
		SearchParameter threadIdSp = new SearchParameter();
		threadIdSp.addBase("Communication");
		threadIdSp.setCode("has-attachments");
		threadIdSp.setType(Enumerations.SearchParamType.REFERENCE);
		threadIdSp.setExpression("Communication.payload[1].contentAttachment is not null");
		threadIdSp.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		threadIdSp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		try {
			mySearchParameterDao.create(threadIdSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), startsWith("Invalid SearchParameter.expression value \"Communication.payload[1].contentAttachment is not null\""));
		}
	}

	@Test
	public void testSearchForExtensionReferenceWithNonMatchingTarget() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("sibling");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		siblingSp.setTitle("Sibling");
		siblingSp.setExpression("Patient.extension('http://acme.org/sibling')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		siblingSp.getTarget().add(new CodeType("Organization"));
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient p1 = new Patient();
		p1.addName().setFamily("P1");
		IIdType p1id = myPatientDao.create(p1).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.addName().setFamily("P2");
		p2.addExtension().setUrl("http://acme.org/sibling").setValue(new Reference(p1id));
		IIdType p2id = myPatientDao.create(p2).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		// Search by ref
		map = new SearchParameterMap();
		map.add("sibling", new ReferenceParam(p1id.getValue()));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, empty());

		// Search by chain
		map = new SearchParameterMap();
		map.add("sibling", new ReferenceParam("name", "P1"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, empty());

	}

	@Test
	public void testSearchForExtensionReferenceWithTwoPaths() {
		Practitioner p1 = new Practitioner();
		p1.addName().setFamily("P1");
		IIdType p1id = myPractitionerDao.create(p1).getId().toUnqualifiedVersionless();

		Practitioner p2 = new Practitioner();
		p2.addName().setFamily("P2");
		IIdType p2id = myPractitionerDao.create(p2).getId().toUnqualifiedVersionless();

		SearchParameter sp = new SearchParameter();
		sp.addBase("DiagnosticReport");
		sp.setCode("fooBar");
		sp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		sp.setTitle("FOO AND BAR");
		sp.setExpression("DiagnosticReport.extension('http://foo') | DiagnosticReport.extension('http://bar')");
		sp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		sp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(sp, mySrd);

		mySearchParamRegistry.forceRefresh();

		DiagnosticReport dr1 = new DiagnosticReport();
		dr1.addExtension("http://foo", new Reference(p1id.getValue()));
		IIdType dr1id = myDiagnosticReportDao.create(dr1).getId().toUnqualifiedVersionless();

		DiagnosticReport dr2 = new DiagnosticReport();
		dr2.addExtension("http://bar", new Reference(p2id.getValue()));
		IIdType dr2id = myDiagnosticReportDao.create(dr2).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		// Find one
		map = new SearchParameterMap();
		map.add(sp.getCode(), new ReferenceParam(p1id.getValue()));
		results = myDiagnosticReportDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, containsInAnyOrder(dr1id.getValue()));

		// Find both
		map = new SearchParameterMap();
		map.add(sp.getCode(), new ReferenceOrListParam().addOr(new ReferenceParam(p1id.getValue())).addOr(new ReferenceParam(p2id.getValue())));
		results = myDiagnosticReportDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, containsInAnyOrder(dr1id.getValue(), dr2id.getValue()));

	}

	@Test
	public void testSearchForExtensionReferenceWithTarget() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("sibling");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		siblingSp.setTitle("Sibling");
		siblingSp.setExpression("Patient.extension('http://acme.org/sibling')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		siblingSp.getTarget().add(new CodeType("Patient"));
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient p1 = new Patient();
		p1.addName().setFamily("P1");
		IIdType p1id = myPatientDao.create(p1).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.addName().setFamily("P2");
		p2.addExtension().setUrl("http://acme.org/sibling").setValue(new Reference(p1id));
		IIdType p2id = myPatientDao.create(p2).getId().toUnqualifiedVersionless();

		Appointment app = new Appointment();
		app.addParticipant().getActor().setReference(p2id.getValue());
		IIdType appid = myAppointmentDao.create(app).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		// Search by ref
		map = new SearchParameterMap();
		map.add("sibling", new ReferenceParam(p1id.getValue()));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));

		// Search by chain
		map = new SearchParameterMap();
		map.add("sibling", new ReferenceParam("name", "P1"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));

		// Search by two level chain
		map = new SearchParameterMap();
		map.add("patient", new ReferenceParam("sibling.name", "P1"));
		results = myAppointmentDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, containsInAnyOrder(appid.getValue()));

	}

	@Test
	public void testSearchForExtensionReferenceWithoutTarget() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("sibling");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		siblingSp.setTitle("Sibling");
		siblingSp.setExpression("Patient.extension('http://acme.org/sibling')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient p1 = new Patient();
		p1.addName().setFamily("P1");
		IIdType p1id = myPatientDao.create(p1).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.addName().setFamily("P2");
		p2.addExtension().setUrl("http://acme.org/sibling").setValue(new Reference(p1id));

		IIdType p2id = myPatientDao.create(p2).getId().toUnqualifiedVersionless();
		Appointment app = new Appointment();
		app.addParticipant().getActor().setReference(p2id.getValue());
		IIdType appid = myAppointmentDao.create(app).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		// Search by ref
		map = new SearchParameterMap();
		map.add("sibling", new ReferenceParam(p1id.getValue()));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));

		// Search by chain
		map = new SearchParameterMap();
		map.add("sibling", new ReferenceParam("name", "P1"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));

		// Search by two level chain
		map = new SearchParameterMap();
		map.add("patient", new ReferenceParam("sibling.name", "P1"));
		results = myAppointmentDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, containsInAnyOrder(appid.getValue()));

	}

	@Test
	public void testSearchForExtensionToken() {
		SearchParameter eyeColourSp = new SearchParameter();
		eyeColourSp.addBase("Patient");
		eyeColourSp.setCode("eyecolour");
		eyeColourSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		eyeColourSp.setTitle("Eye Colour");
		eyeColourSp.setExpression("Patient.extension('http://acme.org/eyecolour')");
		eyeColourSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		eyeColourSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(eyeColourSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient p1 = new Patient();
		p1.setActive(true);
		p1.addExtension().setUrl("http://acme.org/eyecolour").setValue(new CodeType("blue"));
		IIdType p1id = myPatientDao.create(p1).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.setActive(true);
		p2.addExtension().setUrl("http://acme.org/eyecolour").setValue(new CodeType("green"));
		IIdType p2id = myPatientDao.create(p2).getId().toUnqualifiedVersionless();

		// Try with custom gender SP
		SearchParameterMap map = new SearchParameterMap();
		map.add("eyecolour", new TokenParam(null, "blue"));
		IBundleProvider results = myPatientDao.search(map);
		List<String> foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p1id.getValue()));

	}

	@Test
	public void testSearchForExtensionTwoDeepCodeableConcept() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("foobar");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		siblingSp.getTarget().add(new CodeType("Organization"));
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient patient = new Patient();
		patient.addName().setFamily("P2");
		Extension extParent = patient
			.addExtension()
			.setUrl("http://acme.org/foo");
		extParent
			.addExtension()
			.setUrl("http://acme.org/bar")
			.setValue(new CodeableConcept().addCoding(new Coding().setSystem("foo").setCode("bar")));

		IIdType p2id = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.add("foobar", new TokenParam("foo", "bar"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));
	}

	@Test
	public void testSearchForExtensionTwoDeepCoding() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("foobar");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		siblingSp.getTarget().add(new CodeType("Organization"));
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient patient = new Patient();
		patient.addName().setFamily("P2");
		Extension extParent = patient
			.addExtension()
			.setUrl("http://acme.org/foo");
		extParent
			.addExtension()
			.setUrl("http://acme.org/bar")
			.setValue(new Coding().setSystem("foo").setCode("bar"));

		IIdType p2id = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.add("foobar", new TokenParam("foo", "bar"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));
	}

	@Test
	public void testSearchForExtensionTwoDeepDate() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("foobar");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.DATE);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Appointment apt = new Appointment();
		apt.setStatus(AppointmentStatus.ARRIVED);
		IIdType aptId = myAppointmentDao.create(apt).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.addName().setFamily("P2");
		Extension extParent = patient
			.addExtension()
			.setUrl("http://acme.org/foo");

		extParent
			.addExtension()
			.setUrl("http://acme.org/bar")
			.setValue(new DateType("2012-01-02"));

		IIdType p2id = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.add("foobar", new DateParam("2012-01-02"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));
	}

	@Test
	public void testSearchForExtensionTwoDeepDecimal() {
		final SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("foobar");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.NUMBER);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);

		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				mySearchParameterDao.create(siblingSp, mySrd);
			}
		});

		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				mySearchParamRegistry.forceRefresh();
			}
		});

		final Patient patient = new Patient();
		patient.addName().setFamily("P2");
		Extension extParent = patient
			.addExtension()
			.setUrl("http://acme.org/foo");
		extParent
			.addExtension()
			.setUrl("http://acme.org/bar")
			.setValue(new DecimalType("2.1"));

		IIdType p2id = txTemplate.execute(new TransactionCallback<IIdType>() {
			@Override
			public IIdType doInTransaction(TransactionStatus theArg0) {
				return myPatientDao.create(patient).getId().toUnqualifiedVersionless();
			}
		});

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.add("foobar", new NumberParam("2.1"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));
	}

	@Test
	public void testSearchForExtensionTwoDeepNumber() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("foobar");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.NUMBER);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient patient = new Patient();
		patient.addName().setFamily("P2");
		Extension extParent = patient
			.addExtension()
			.setUrl("http://acme.org/foo");
		extParent
			.addExtension()
			.setUrl("http://acme.org/bar")
			.setValue(new IntegerType(5));

		IIdType p2id = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.add("foobar", new NumberParam("5"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));
	}

	@Test
	public void testSearchForExtensionTwoDeepReference() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("foobar");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		siblingSp.getTarget().add(new CodeType("Appointment"));
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Appointment apt = new Appointment();
		apt.setStatus(AppointmentStatus.ARRIVED);
		IIdType aptId = myAppointmentDao.create(apt).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.addName().setFamily("P2");
		Extension extParent = patient
			.addExtension()
			.setUrl("http://acme.org/foo");

		extParent
			.addExtension()
			.setUrl("http://acme.org/bar")
			.setValue(new Reference(aptId.getValue()));

		IIdType p2id = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.add("foobar", new ReferenceParam(aptId.getValue()));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));
	}

	@Test
	public void testSearchForExtensionTwoDeepReferenceWithoutType() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("foobar");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Appointment apt = new Appointment();
		apt.setStatus(AppointmentStatus.ARRIVED);
		IIdType aptId = myAppointmentDao.create(apt).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.addName().setFamily("P2");
		Extension extParent = patient
			.addExtension()
			.setUrl("http://acme.org/foo");

		extParent
			.addExtension()
			.setUrl("http://acme.org/bar")
			.setValue(new Reference(aptId.getValue()));

		IIdType p2id = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.add("foobar", new ReferenceParam(aptId.getValue()));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));
	}

	@Test
	public void testSearchForExtensionTwoDeepReferenceWrongType() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("foobar");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		siblingSp.getTarget().add(new CodeType("Observation"));
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Appointment apt = new Appointment();
		apt.setStatus(AppointmentStatus.ARRIVED);
		IIdType aptId = myAppointmentDao.create(apt).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.addName().setFamily("P2");
		Extension extParent = patient
			.addExtension()
			.setUrl("http://acme.org/foo");

		extParent
			.addExtension()
			.setUrl("http://acme.org/bar")
			.setValue(new Reference(aptId.getValue()));

		IIdType p2id = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.add("foobar", new ReferenceParam(aptId.getValue()));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, empty());
	}

	@Test
	public void testSearchForExtensionTwoDeepString() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("foobar");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.STRING);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient patient = new Patient();
		patient.addName().setFamily("P2");
		Extension extParent = patient
			.addExtension()
			.setUrl("http://acme.org/foo");
		extParent
			.addExtension()
			.setUrl("http://acme.org/bar")
			.setValue(new StringType("HELLOHELLO"));

		IIdType p2id = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.add("foobar", new StringParam("hello"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));
	}

	@Test
	public void testSearchForStringOnIdentifier() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.STRING);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.identifier.value");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		IIdType spId = mySearchParameterDao.create(fooSp, mySrd).getId().toUnqualifiedVersionless();

		mySearchParamRegistry.forceRefresh();

		Patient pat = new Patient();
		pat.addIdentifier().setSystem("FOO123").setValue("BAR678");
		pat.setGender(AdministrativeGender.MALE);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Patient pat2 = new Patient();
		pat.setGender(AdministrativeGender.FEMALE);
		myPatientDao.create(pat2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		// Partial match
		map = new SearchParameterMap();
		map.add("foo", new StringParam("bar"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(patId.getValue()));

		// Non match
		map = new SearchParameterMap();
		map.add("foo", new StringParam("zzz"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, empty());

	}

	@Test
	public void testSearchForStringOnIdentifierWithSpecificSystem() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.STRING);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.identifier.where(system = 'http://AAA').value");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		IIdType spId = mySearchParameterDao.create(fooSp, mySrd).getId().toUnqualifiedVersionless();

		mySearchParamRegistry.forceRefresh();

		Patient pat = new Patient();
		pat.addIdentifier().setSystem("http://AAA").setValue("BAR678");
		pat.setGender(AdministrativeGender.MALE);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Patient pat2 = new Patient();
		pat2.addIdentifier().setSystem("http://BBB").setValue("BAR678");
		pat2.setGender(AdministrativeGender.FEMALE);
		myPatientDao.create(pat2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		// Partial match
		map = new SearchParameterMap();
		map.add("foo", new StringParam("bar"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(patId.getValue()));

		// Non match
		map = new SearchParameterMap();
		map.add("foo", new StringParam("zzz"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, empty());

	}

	@Test
	public void testSearchParameterDescendsIntoContainedResource() {
		SearchParameter sp = new SearchParameter();
		sp.addBase("Observation");
		sp.setCode("specimencollectedtime");
		sp.setType(Enumerations.SearchParamType.DATE);
		sp.setTitle("Observation Specimen Collected Time");
		sp.setExpression("Observation.specimen.resolve().receivedTime");
		sp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		sp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(sp));
		mySearchParameterDao.create(sp);

		mySearchParamRegistry.forceRefresh();

		Specimen specimen = new Specimen();
		specimen.setId("#FOO");
		specimen.setReceivedTimeElement(new DateTimeType("2011-01-01"));
		Observation o = new Observation();
		o.setId("O1");
		o.getContained().add(specimen);
		o.setStatus(Observation.ObservationStatus.FINAL);
		o.setSpecimen(new Reference("#FOO"));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(o));
		myObservationDao.update(o);

		specimen = new Specimen();
		specimen.setId("#FOO");
		specimen.setReceivedTimeElement(new DateTimeType("2011-01-03"));
		o = new Observation();
		o.setId("O2");
		o.getContained().add(specimen);
		o.setStatus(Observation.ObservationStatus.FINAL);
		o.setSpecimen(new Reference("#FOO"));
		myObservationDao.update(o);

		SearchParameterMap params = new SearchParameterMap();
		params.add("specimencollectedtime", new DateParam("2011-01-01"));
		IBundleProvider outcome = myObservationDao.search(params);
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		ourLog.info("IDS: " + ids);
		assertThat(ids, contains("Observation/O1"));
	}

	@Test
	public void testSearchWithCustomParam() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.gender");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		IIdType spId = mySearchParameterDao.create(fooSp, mySrd).getId().toUnqualifiedVersionless();

		mySearchParamRegistry.forceRefresh();

		Patient pat = new Patient();
		pat.setGender(AdministrativeGender.MALE);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Patient pat2 = new Patient();
		pat.setGender(AdministrativeGender.FEMALE);
		IIdType patId2 = myPatientDao.create(pat2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		// Try with custom gender SP
		map = new SearchParameterMap();
		map.add("foo", new TokenParam(null, "male"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(patId.getValue()));

		// Try with normal gender SP
		map = new SearchParameterMap();
		map.add("gender", new TokenParam(null, "male"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(patId.getValue()));

		// Delete the param
		mySearchParameterDao.delete(spId, mySrd);

		mySearchParamRegistry.forceRefresh();
		myResourceReindexingSvc.forceReindexingPass();

		// Try with custom gender SP
		map = new SearchParameterMap();
		map.add("foo", new TokenParam(null, "male"));
		try {
			myPatientDao.search(map).size();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Unknown search parameter foo for resource type Patient", e.getMessage());
		}
	}

	@Test
	public void testSearchWithCustomParamDraft() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.gender");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.DRAFT);
		mySearchParameterDao.create(fooSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient pat = new Patient();
		pat.setGender(AdministrativeGender.MALE);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Patient pat2 = new Patient();
		pat.setGender(AdministrativeGender.FEMALE);
		IIdType patId2 = myPatientDao.create(pat2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		// Try with custom gender SP (should find nothing)
		map = new SearchParameterMap();
		map.add("foo", new TokenParam(null, "male"));
		try {
			myPatientDao.search(map).size();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Unknown search parameter foo for resource type Patient", e.getMessage());
		}

		// Try with normal gender SP
		map = new SearchParameterMap();
		map.add("gender", new TokenParam(null, "male"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(patId.getValue()));

	}

	@Test
	public void testCustomCodeableConcept() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("ChargeItem");
		fooSp.setName("Product");
		fooSp.setCode("product");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("Product within a ChargeItem");
		fooSp.setExpression("ChargeItem.product.as(CodeableConcept)");
		fooSp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(fooSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		ChargeItem ci = new ChargeItem();
		ci.setProduct(new CodeableConcept());
		ci.getProductCodeableConcept().addCoding().setCode("1");
		myChargeItemDao.create(ci);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("product", new TokenParam(null, "1"));
		IBundleProvider results = myChargeItemDao.search(map);
		assertEquals(1, results.size().intValue());


	}


	@Test
	public void testCompositeWithInvalidTarget() {
		SearchParameter sp = new SearchParameter();
		sp.addBase("Patient");
		sp.setCode("myDoctor");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setTitle("My Doctor");
		sp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		sp.addComponent()
			.setDefinition("http://foo");
		mySearchParameterDao.create(sp);

		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_WARNING, interceptor);

		try {
			mySearchParamRegistry.forceRefresh();

			ArgumentCaptor<HookParams> paramsCaptor = ArgumentCaptor.forClass(HookParams.class);
			verify(interceptor, times(1)).invoke(any(), paramsCaptor.capture());

			StorageProcessingMessage msg = paramsCaptor.getValue().get(StorageProcessingMessage.class);
			assertThat(msg.getMessage(), containsString("refers to unknown component foo, ignoring this parameter"));

		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}




	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
