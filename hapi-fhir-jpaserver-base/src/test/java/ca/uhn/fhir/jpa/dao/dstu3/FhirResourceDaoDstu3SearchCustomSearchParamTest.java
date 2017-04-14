package ca.uhn.fhir.jpa.dao.dstu3;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.TestUtil;

public class FhirResourceDaoDstu3SearchCustomSearchParamTest extends BaseJpaDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3SearchCustomSearchParamTest.class);

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
	public void testCreateInvalidParamMismatchedResourceName() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.gender or Observation.code");
		fooSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("Invalid SearchParameter.expression value \"Observation.code\". All paths in a single SearchParameter must match the same resource type", e.getMessage());
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
			assertEquals("SearchParameter.status is missing or invalid: null", e.getMessage());
		}

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

		mySearchParamRegsitry.forceRefresh();

		Patient p1 = new Patient();
		p1.setActive(true);
		p1.addExtension().setUrl("http://acme.org/eyecolour").addExtension().setUrl("http://foo").setValue(new StringType("VAL"));
		IIdType p1id = myPatientDao.create(p1).getId().toUnqualifiedVersionless();

	}

	@Test
	public void testSearchForExtension() {
		SearchParameter eyeColourSp = new SearchParameter();
		eyeColourSp.addBase("Patient");
		eyeColourSp.setCode("eyecolour");
		eyeColourSp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		eyeColourSp.setTitle("Eye Colour");
		eyeColourSp.setExpression("Patient.extension('http://acme.org/eyecolour')");
		eyeColourSp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		eyeColourSp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(eyeColourSp, mySrd);

		mySearchParamRegsitry.forceRefresh();

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

		mySearchParamRegsitry.forceRefresh();

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

		mySearchParamRegsitry.forceRefresh();
		mySystemDao.performReindexingPass(100);

		// Try with custom gender SP
		map = new SearchParameterMap();
		map.add("foo", new TokenParam(null, "male"));
		IBundleProvider res = myPatientDao.search(map);
		assertEquals(0, res.size());
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

		mySearchParamRegsitry.forceRefresh();

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
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, empty());

		// Try with normal gender SP
		map = new SearchParameterMap();
		map.add("gender", new TokenParam(null, "male"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(patId.getValue()));

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

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
