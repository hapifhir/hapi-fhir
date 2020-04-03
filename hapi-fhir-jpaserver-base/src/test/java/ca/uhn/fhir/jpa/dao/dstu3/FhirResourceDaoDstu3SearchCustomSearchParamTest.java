package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Appointment.AppointmentStatus;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class FhirResourceDaoDstu3SearchCustomSearchParamTest extends BaseJpaDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3SearchCustomSearchParamTest.class);

	@Before
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	@After
	public void after() {
		myDaoConfig.setValidateSearchParameterExpressionsOnSave(new DaoConfig().isValidateSearchParameterExpressionsOnSave());
	}

	@Test
	public void testCreateInvalidNoBase() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.gender");
		fooSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("SearchParameter.base is missing", e.getMessage());
		}
	}

	@Test
	public void testCreateInvalidParamInvalidResourceName() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("PatientFoo.gender");
		fooSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("Invalid SearchParameter.expression value \"PatientFoo.gender\": Unknown resource name \"PatientFoo\" (this name is not known in FHIR version \"DSTU3\")", e.getMessage());
		}
	}

	@Test
	public void testCreateInvalidParamNoPath() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("SearchParameter.expression is missing", e.getMessage());
		}
	}

	@Test
	public void testCreateInvalidParamNoResourceName() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("gender");
		fooSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
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
		fooSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.gender");
		fooSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(null);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("SearchParameter.status is missing or invalid", e.getMessage());
		}

	}

	@Test
	public void testCustomReferenceParameter() throws Exception {
		SearchParameter sp = new SearchParameter();
		sp.addBase("Patient");
		sp.setCode("myDoctor");
		sp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.REFERENCE);
		sp.setTitle("My Doctor");
		sp.setExpression("Patient.extension('http://fmcna.com/myDoctor')");
		sp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		sp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(sp);

		mySearchParamRegistry.forceRefresh();

		org.hl7.fhir.dstu3.model.Practitioner pract = new org.hl7.fhir.dstu3.model.Practitioner();
		pract.setId("A");
		pract.addName().setFamily("PRACT");
		myPractitionerDao.update(pract);

		Patient pat = myFhirCtx.newJsonParser().parseResource(Patient.class, loadClasspath("/dstu3_custom_resource_patient.json"));
		IIdType pid = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		params.add("myDoctor", new ReferenceParam("A"));
		IBundleProvider outcome = myPatientDao.search(params);
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		ourLog.info("IDS: " + ids);
		assertThat(ids, contains(pid.getValue()));
	}

	@Test
	public void testExtensionWithNoValueIndexesWithoutFailure() {
		SearchParameter eyeColourSp = new SearchParameter();
		eyeColourSp.addBase("Patient");
		eyeColourSp.setCode("eyecolour");
		eyeColourSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		eyeColourSp.setTitle("Eye Colour");
		eyeColourSp.setExpression("Patient.extension('http://acme.org/eyecolour')");
		eyeColourSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		eyeColourSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
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
		attendingSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.REFERENCE);
		attendingSp.setTitle("Attending");
		attendingSp.setExpression("Patient.extension('http://acme.org/attending')");
		attendingSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		attendingSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
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
	public void testIndexFailsIfInvalidSearchParameterExists() {
		myDaoConfig.setValidateSearchParameterExpressionsOnSave(false);

		SearchParameter threadIdSp = new SearchParameter();
		threadIdSp.addBase("Communication");
		threadIdSp.setCode("has-attachments");
		threadIdSp.setType(Enumerations.SearchParamType.REFERENCE);
		threadIdSp.setExpression("Communication.payload[1].contentAttachment is not null");
		threadIdSp.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		threadIdSp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(threadIdSp, mySrd);
		mySearchParamRegistry.forceRefresh();

		Communication com = new Communication();
		com.setStatus(Communication.CommunicationStatus.INPROGRESS);
		try {
			myCommunicationDao.create(com, mySrd);
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), startsWith("Failed to extract values from resource using FHIRPath \"Communication.payload[1].contentAttachment is not null\": org.hl7.fhir"));
		}
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
			assertThat(e.getMessage(), startsWith("The expression \"Communication.payload[1].contentAttachment is not null\" can not be evaluated and may be invalid: "));
		}
	}

	/**
	 * See #863
	 */
	@Test
	public void testParamWithMultipleBasesReference() {
		SearchParameter sp = new SearchParameter();
		sp.setUrl("http://clinicalcloud.solutions/fhir/SearchParameter/request-reason");
		sp.setName("reason");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setCode("reason");
		sp.addBase("MedicationRequest");
		sp.addBase("ProcedureRequest");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setExpression("MedicationRequest.reasonReference | ProcedureRequest.reasonReference");
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

		ProcedureRequest pr = new ProcedureRequest();
		pr.addReasonReference().setReference(conditionId);
		myProcedureRequestDao.create(pr);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("reason", new ReferenceParam(conditionId));
		List<String> results = toUnqualifiedVersionlessIdValues(myMedicationRequestDao.search(map));
		assertThat(results, contains(mrId));
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
		sp.addBase("ProcedureRequest");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setExpression("MedicationRequest.reasonCode | ProcedureRequest.reasonCode");
		mySearchParameterDao.create(sp);
		mySearchParamRegistry.forceRefresh();

		MedicationRequest mr = new MedicationRequest();
		mr.addReasonCode().addCoding().setSystem("foo").setCode("bar");
		String mrId = myMedicationRequestDao.create(mr).getId().toUnqualifiedVersionless().getValue();

		ProcedureRequest pr = new ProcedureRequest();
		pr.addReasonCode().addCoding().setSystem("foo").setCode("bar");
		myProcedureRequestDao.create(pr);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("reason", new TokenParam("foo", "bar"));
		List<String> results = toUnqualifiedVersionlessIdValues(myMedicationRequestDao.search(map));
		assertThat(results, contains(mrId));
	}

	@Test
	public void testSearchForExtensionReferenceWithNonMatchingTarget() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("sibling");
		siblingSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.REFERENCE);
		siblingSp.setTitle("Sibling");
		siblingSp.setExpression("Patient.extension('http://acme.org/sibling')");
		siblingSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
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
	public void testSearchForExtensionReferenceWithTarget() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("sibling");
		siblingSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.REFERENCE);
		siblingSp.setTitle("Sibling");
		siblingSp.setExpression("Patient.extension('http://acme.org/sibling')");
		siblingSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
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
		siblingSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.REFERENCE);
		siblingSp.setTitle("Sibling");
		siblingSp.setExpression("Patient.extension('http://acme.org/sibling')");
		siblingSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
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
		eyeColourSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		eyeColourSp.setTitle("Eye Colour");
		eyeColourSp.setExpression("Patient.extension('http://acme.org/eyecolour')");
		eyeColourSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		eyeColourSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
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
		siblingSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
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
		siblingSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
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
		siblingSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.DATE);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
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
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("foobar");
		siblingSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.NUMBER);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
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
			.setValue(new DecimalType("2.1"));

		IIdType p2id = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

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
		siblingSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.NUMBER);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
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
		siblingSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.REFERENCE);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
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
		siblingSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.REFERENCE);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
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
		siblingSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.REFERENCE);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
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
		siblingSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.STRING);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
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
	public void testSearchOnMultivalue() throws FHIRException {
		SearchParameter displaySp = new SearchParameter();
		displaySp.addBase("MedicationStatement");
		displaySp.setCode("display");
		displaySp.setType(Enumerations.SearchParamType.STRING);
		displaySp.setTitle("Display");
		displaySp.setExpression("MedicationStatement.medication.as(CodeableConcept).coding.display");
		displaySp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		displaySp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(displaySp, mySrd);

		mySearchParamRegistry.forceRefresh();

		MedicationStatement ms1 = new MedicationStatement();
		ms1.setMedication(new CodeableConcept());
		ms1.getMedicationCodeableConcept().addCoding().setDisplay("AAA");
		String id1 = myMedicationStatementDao.create(ms1).getId().toUnqualifiedVersionless().getValue();

		MedicationStatement ms2 = new MedicationStatement();
		ms2.setMedication(new CodeableConcept());
		ms2.getMedicationCodeableConcept().addCoding().setDisplay("BBB");
		myMedicationStatementDao.create(ms2).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("display", new StringParam("AAA"));
		IBundleProvider results = myMedicationStatementDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(results), contains(id1));

	}

	@Test
	public void testSearchParameterDescendsIntoContainedResource() {
		SearchParameter sp = new SearchParameter();
		sp.addBase("Observation");
		sp.setCode("specimencollectedtime");
		sp.setType(Enumerations.SearchParamType.DATE);
		sp.setTitle("Observation Specimen Collected Time");
		sp.setExpression("Observation.specimen.resolve().receivedTime");
		sp.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
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
	public void testSearchParameterWithContainedMedication() {
		SearchParameter sp1 = new SearchParameter();
		sp1.setId("SearchParameter/medicationadministration-ingredient-medication");
		sp1.setUrl("http://hapifhir.io/fhir/StructureDefinition/sp-unique");
		sp1.setName("MEDICATIONADMINISTRATION-INGREDIENT-MEDICATION");
		sp1.setCode("medicationadministration-ingredient-medication");
		sp1.setExpression("MedicationAdministration.medication.resolve().code|MedicationAdministration.medication.resolve().ingredient.item.as(Reference).resolve().code|MedicationAdministration.medication.resolve().ingredient.item.as(CodeableConcept)");
		sp1.setType(Enumerations.SearchParamType.TOKEN);
		sp1.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp1.addBase("MedicationAdministration");
		sp1.setTitle("Custom search parameter for ingredient level medication");
		sp1.setDescription("This SP is using to find a MedicationAdministration by ingredient level medication");
		sp1.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(sp1));
		mySearchParameterDao.create(sp1);

		mySearchParamRegistry.forceRefresh();

		SearchParameter sp2 = new SearchParameter();
		sp2.setId("SearchParameter/medicationrequest-ingredient-medication");
		sp2.setUrl("http://hapifhir.io/fhir/StructureDefinition/sp-unique");
		sp2.setName("MEDICATIONREQUEST-INGREDIENT-MEDICATION");
		sp2.setCode("medicationrequest-ingredient-medication");
		sp2.setExpression("MedicationRequest.medication.resolve().code|MedicationRequest.medication.resolve().ingredient.item.as(Reference).resolve().code|MedicationRequest.medication.resolve().ingredient.item.as(CodeableConcept)");
		sp2.setType(Enumerations.SearchParamType.TOKEN);
		sp2.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp2.addBase("MedicationRequest");
		sp2.setTitle("Custom search parameter for ingredient level medication");
		sp2.setDescription("This SP is using to find a MedicationRequest by ingredient level medication");
		sp2.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(sp2));
		mySearchParameterDao.create(sp2);

		mySearchParamRegistry.forceRefresh();

		// MedicationAdministration resource with contained Medication and ingredient level Medication
		Medication administrationMedication = new Medication();

		administrationMedication.getCode().addCoding(new Coding()
			.setSystem("containedSystem")
			.setCode("containedCode")
			.setDisplay("medicationDisplay"));

		administrationMedication.addIngredient().setItem(new CodeableConcept()).getItemCodeableConcept().addCoding()
			.setSystem("medicationsystem")
			.setCode("99999")
			.setDisplay("Some medicine for Administration");

		MedicationAdministration medicationAdministration = new MedicationAdministration();
		medicationAdministration.addIdentifier()
			.setSystem("urn:hssc:srhs:epc:medadminid")
			.setValue("64552569-0-77");
		medicationAdministration.setStatus(MedicationAdministration.MedicationAdministrationStatus.COMPLETED);
		medicationAdministration.setMedication(new Reference());
		medicationAdministration.getMedicationReference().setResource(administrationMedication);

		String medAdmin = myFhirCtx.newJsonParser().encodeResourceToString(medicationAdministration);
		MedicationAdministration medicationAdministrationResource = (MedicationAdministration) myFhirCtx.newJsonParser().parseResource(medAdmin);

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(medicationAdministrationResource));
		IIdType medId = myMedicationAdministrationDao.create(medicationAdministrationResource, mySrd).getId().toUnqualifiedVersionless();

		// MedicationRequest resource with contained Medication and ingredient level Medication
		Medication requestMedication = new Medication();

		requestMedication.getCode().addCoding(new Coding()
			.setSystem("containedSystem")
			.setCode("containedCode")
			.setDisplay("medicationDisplay"));

		requestMedication.addIngredient().setItem(new CodeableConcept()).getItemCodeableConcept().addCoding()
			.setSystem("fake_system")
			.setCode("11111")
			.setDisplay("Some medicine for Request");

		MedicationRequest medicationRequest = new MedicationRequest();
		medicationRequest.addIdentifier()
			.setSystem("urn:hssc:musc:epc:medadminid")
			.setValue("1234354-3-43");
		medicationRequest.setStatus(MedicationRequest.MedicationRequestStatus.COMPLETED);
		medicationRequest.setMedication(new Reference());
		medicationRequest.getMedicationReference().setResource(requestMedication);

		String medRequest = myFhirCtx.newJsonParser().encodeResourceToString(medicationRequest);
		MedicationRequest medicationRequestResource = (MedicationRequest) myFhirCtx.newJsonParser().parseResource(medRequest);

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(medicationRequestResource));
		IIdType medReqId = myMedicationRequestDao.create(medicationRequestResource, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap administrationParam = new SearchParameterMap();
		administrationParam.add("medicationadministration-ingredient-medication", new TokenParam(administrationMedication.getCode().getCodingFirstRep()));
		administrationParam.add("medicationadministration-ingredient-medication", new TokenParam(administrationMedication.getIngredient().get(0).getItemCodeableConcept().getCodingFirstRep()));

		SearchParameterMap requestParam = new SearchParameterMap();
		requestParam.add("medicationrequest-ingredient-medication", new TokenParam(requestMedication.getCode().getCodingFirstRep()));
		requestParam.add("medicationrequest-ingredient-medication", new TokenParam(requestMedication.getIngredient().get(0).getItemCodeableConcept().getCodingFirstRep()));

		IBundleProvider outcomeForMedAdmin = myMedicationAdministrationDao.search(administrationParam);
		IBundleProvider outcomeForMedRequest = myMedicationRequestDao.search(requestParam);

		List<String> ids1 = toUnqualifiedVersionlessIdValues(outcomeForMedAdmin);
		List<String> ids2 = toUnqualifiedVersionlessIdValues(outcomeForMedRequest);

		ourLog.info("IDS: " + ids1);
		ourLog.info("IDS: " + ids2);

		assertThat(ids1, contains(medId.getValue()));
		assertThat(ids2, contains(medReqId.getValue()));
	}


	@Test
	public void testSearchWithCustomParam() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.gender");
		fooSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
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
		fooSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.gender");
		fooSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.DRAFT);
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

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
