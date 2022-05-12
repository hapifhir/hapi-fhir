package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.dstu3.model.Appointment;
import org.hl7.fhir.dstu3.model.Appointment.AppointmentStatus;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Communication;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.MedicationAdministration;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.MedicationStatement;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.ProcedureRequest;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.hl7.fhir.dstu3.model.Specimen;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirResourceDaoDstu3SearchCustomSearchParamTest extends BaseJpaDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3SearchCustomSearchParamTest.class);

	@BeforeEach
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	@AfterEach
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
			assertEquals(Msg.code(1113) + "SearchParameter.base is missing", e.getMessage());
		}
	}

	@Test
	public void testCreateSpWithMultiplePaths(){
		SearchParameter sp = new SearchParameter();
		sp.setCode("telephone-unformatted");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setExpression("Patient.telecom.where(system='phone' or system='email')");
		sp.getBase().add(new CodeType("Patient"));
		sp.setType(Enumerations.SearchParamType.TOKEN);

		DaoMethodOutcome daoMethodOutcome = mySearchParameterDao.create(sp);
		assertThat(daoMethodOutcome.getId(), is(notNullValue()));

		sp.setExpression("Patient.telecom.where(system='phone') or Patient.telecome.where(system='email')");
		sp.setCode("telephone-unformatted-2");
		daoMethodOutcome = mySearchParameterDao.create(sp);
		assertThat(daoMethodOutcome.getId(), is(notNullValue()));

		sp.setExpression("Patient.telecom.where(system='phone' or system='email') | Patient.telecome.where(system='email')");
		sp.setCode("telephone-unformatted-3");
		daoMethodOutcome = mySearchParameterDao.create(sp);
		assertThat(daoMethodOutcome.getId(), is(notNullValue()));

		sp.setExpression("Patient.telecom.where(system='phone' or system='email') | Patient.telecom.where(system='email') or Patient.telecom.where(system='mail' | system='phone')");
		sp.setCode("telephone-unformatted-3");
		daoMethodOutcome = mySearchParameterDao.create(sp);
		assertThat(daoMethodOutcome.getId(), is(notNullValue()));
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
			assertEquals(Msg.code(1118) + "Invalid SearchParameter.expression value \"PatientFoo.gender\": " + Msg.code(1684) + "Unknown resource name \"PatientFoo\" (this name is not known in FHIR version \"DSTU3\")", e.getMessage());
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
			assertEquals(Msg.code(1114) + "SearchParameter.expression is missing", e.getMessage());
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
			assertEquals(Msg.code(1117) + "Invalid SearchParameter.expression value \"gender\". Must start with a resource name.", e.getMessage());
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
			assertEquals(Msg.code(1112) + "SearchParameter.status is missing or invalid", e.getMessage());
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

		Patient pat = myFhirContext.newJsonParser().parseResource(Patient.class, ClasspathUtil.loadResource("/dstu3_custom_resource_patient.json"));
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
		assertThat(foundResources, containsInAnyOrder(appId.getValue(), p2id.getValue(), p1id.getValue()));

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
			assertThat(e.getMessage(), startsWith(Msg.code(504) + "Failed to extract values from resource using FHIRPath \"Communication.payload[1].contentAttachment is not null\": org.hl7.fhir"));
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
			assertThat(e.getMessage(), startsWith(Msg.code(1119) + "The expression \"Communication.payload[1].contentAttachment is not null\" can not be evaluated and may be invalid: "));
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
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(sp));
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
			assertEquals(Msg.code(1223) + "Unknown search parameter \"foo\" for resource type \"Patient\". Valid search parameters for this search are: [_id, _lastUpdated, active, address, address-city, address-country, address-postalcode, address-state, address-use, animal-breed, animal-species, birthdate, death-date, deceased, email, family, gender, general-practitioner, given, identifier, language, link, name, organization, phone, phonetic, telecom]", e.getMessage());
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
			assertEquals(Msg.code(1223) + "Unknown search parameter \"foo\" for resource type \"Patient\". Valid search parameters for this search are: [_id, _lastUpdated, active, address, address-city, address-country, address-postalcode, address-state, address-use, animal-breed, animal-species, birthdate, death-date, deceased, email, family, gender, general-practitioner, given, identifier, language, link, name, organization, phone, phonetic, telecom]", e.getMessage());
		}

		// Try with normal gender SP
		map = new SearchParameterMap();
		map.add("gender", new TokenParam(null, "male"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(patId.getValue()));

	}

	@Test
	public void testProgramaticallyContainedByReferenceAreStillResolvable() {
		SearchParameter sp = new SearchParameter();
		sp.setUrl("http://hapifhir.io/fhir/StructureDefinition/sp-unique");
		sp.setName("MEDICATIONADMINISTRATION-INGREDIENT-MEDICATION");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setCode("medicationadministration-ingredient-medication");
		sp.addBase("MedicationAdministration");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setExpression("MedicationAdministration.medication.resolve().ingredient.item.as(Reference).resolve().code");
		mySearchParameterDao.create(sp);
		mySearchParamRegistry.forceRefresh();

		Medication ingredient = new Medication();
		ingredient.getCode().addCoding().setSystem("system").setCode("code");

		Medication medication = new Medication();
		medication.addIngredient().setItem(new Reference(ingredient));

		MedicationAdministration medAdmin = new MedicationAdministration();
		medAdmin.setMedication(new Reference(medication));

		myMedicationAdministrationDao.create(medAdmin);
		
		runInTransaction(()->{
			List<ResourceIndexedSearchParamToken> tokens = myResourceIndexedSearchParamTokenDao
				.findAll()
				.stream()
				.filter(t -> t.getParamName().equals("medicationadministration-ingredient-medication"))
				.collect(Collectors.toList());
			ourLog.info("Tokens:\n * {}", tokens.stream().map(t->t.toString()).collect(Collectors.joining("\n * ")));
			assertEquals(1, tokens.size(), tokens.toString());
			assertEquals(false, tokens.get(0).isMissing());

		});

		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add("medicationadministration-ingredient-medication", new TokenParam("system","code"));
		myCaptureQueriesListener.clear();
		IBundleProvider search = myMedicationAdministrationDao.search(map);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(1, search.sizeOrThrowNpe());

	}



}
