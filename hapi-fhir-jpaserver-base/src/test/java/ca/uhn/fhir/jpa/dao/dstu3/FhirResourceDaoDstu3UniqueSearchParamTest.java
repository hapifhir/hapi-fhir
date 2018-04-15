package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.SearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.entity.ResourceIndexedCompositeStringUnique;
import ca.uhn.fhir.jpa.search.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.util.JpaConstants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;

@SuppressWarnings({"unchecked", "deprecation"})
@TestPropertySource(properties = {
	// Since scheduled tasks can cause searches, which messes up the
	// value returned by SearchBuilder.getLastHandlerMechanismForUnitTest()
	"scheduling_disabled=true"
})
public class FhirResourceDaoDstu3UniqueSearchParamTest extends BaseJpaDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3UniqueSearchParamTest.class);

	@After
	public void after() {
		myDaoConfig.setDefaultSearchParamsCanBeOverridden(new DaoConfig().isDefaultSearchParamsCanBeOverridden());
	}

	@Before
	public void before() {
		myDaoConfig.setDefaultSearchParamsCanBeOverridden(true);
	}

	private void createUniqueBirthdateAndGenderSps() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-gender");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setCode("gender");
		sp.setExpression("Patient.gender");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-birthdate");
		sp.setType(Enumerations.SearchParamType.DATE);
		sp.setCode("birthdate");
		sp.setExpression("Patient.birthDate");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-gender-birthdate");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition(new Reference("SearchParameter/patient-gender"));
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition(new Reference("SearchParameter/patient-birthdate"));
		sp.addExtension()
			.setUrl(JpaConstants.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp);

		mySearchParamRegsitry.forceRefresh();
	}

	private void createUniqueIndexCoverageBeneficiary() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/coverage-beneficiary");
		sp.setCode("beneficiary");
		sp.setExpression("Coverage.beneficiary");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Coverage");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/coverage-identifier");
		sp.setCode("identifier");
		sp.setExpression("Coverage.identifier");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Coverage");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/coverage-beneficiary-identifier");
		sp.setCode("coverage-beneficiary-identifier");
		sp.setExpression("Coverage.beneficiary");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Coverage");
		sp.addComponent()
			.setExpression("Coverage")
			.setDefinition(new Reference("/SearchParameter/coverage-beneficiary"));
		sp.addComponent()
			.setExpression("Coverage")
			.setDefinition(new Reference("/SearchParameter/coverage-identifier"));
		sp.addExtension()
			.setUrl(JpaConstants.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp);
		mySearchParamRegsitry.forceRefresh();
	}

	private void createUniqueNameAndManagingOrganizationSps() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-name");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("name");
		sp.setExpression("Patient.name");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-organization");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode("organization");
		sp.setExpression("Patient.managingOrganization");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-name-organization");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition(new Reference("SearchParameter/patient-name"));
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition(new Reference("SearchParameter/patient-organization"));
		sp.addExtension()
			.setUrl(JpaConstants.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp);

		mySearchParamRegsitry.forceRefresh();
	}

	@Test
	public void testDetectUniqueSearchParams() {
		createUniqueBirthdateAndGenderSps();
		List<JpaRuntimeSearchParam> params = mySearchParamRegsitry.getActiveUniqueSearchParams("Patient");

		assertEquals(1, params.size());
		assertEquals(params.get(0).isUnique(), true);
		assertEquals(2, params.get(0).getCompositeOf().size());
		// Should be alphabetical order
		assertEquals("birthdate", params.get(0).getCompositeOf().get(0).getName());
		assertEquals("gender", params.get(0).getCompositeOf().get(1).getName());
	}

	@Test
	public void testDuplicateUniqueValuesAreRejected() {
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		try {
			myPatientDao.create(pt1).getId().toUnqualifiedVersionless();
			fail();
		} catch (PreconditionFailedException e) {
			// good
		}

		Patient pt2 = new Patient();
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		IIdType id2 = myPatientDao.create(pt2).getId().toUnqualifiedVersionless();

		pt2 = new Patient();
		pt2.setId(id2);
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		pt2.setBirthDateElement(new DateType("2011-01-01"));
		try {
			myPatientDao.update(pt2);
			fail();
		} catch (PreconditionFailedException e) {
			// good
		}

	}

	@Test
	public void testIndexTransactionWithMatchUrl() {
		Patient pt2 = new Patient();
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		pt2.setBirthDateElement(new DateType("2011-01-02"));
		IIdType id2 = myPatientDao.create(pt2).getId().toUnqualifiedVersionless();

		Coverage cov = new Coverage();
		cov.getBeneficiary().setReference(id2.getValue());
		cov.addIdentifier().setSystem("urn:foo:bar").setValue("123");
		IIdType id3 = myCoverageDao.create(cov).getId().toUnqualifiedVersionless();

		createUniqueIndexCoverageBeneficiary();

		mySystemDao.markAllResourcesForReindexing();
		mySystemDao.performReindexingPass(1000);

		List<ResourceIndexedCompositeStringUnique> uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 1, uniques.size());
		assertEquals("Coverage/" + id3.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Coverage?beneficiary=Patient%2F" + id2.getIdPart() + "&identifier=urn%3Afoo%3Abar%7C123", uniques.get(0).getIndexString());


	}

	@Test
	public void testIndexTransactionWithMatchUrl2() {
		createUniqueIndexCoverageBeneficiary();

		String input = "{\n" +
			"  \"resourceType\": \"Bundle\",\n" +
			"  \"type\": \"transaction\",\n" +
			"  \"entry\": [\n" +
			"    {\n" +
			"      \"fullUrl\": \"urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3\",\n" +
			"      \"resource\": {\n" +
			"        \"resourceType\": \"Patient\",\n" +
			"        \"identifier\": [\n" +
			"          {\n" +
			"            \"use\": \"official\",\n" +
			"            \"type\": {\n" +
			"              \"coding\": [\n" +
			"                {\n" +
			"                  \"system\": \"http://hl7.org/fhir/v2/0203\",\n" +
			"                  \"code\": \"MR\"\n" +
			"                }\n" +
			"              ]\n" +
			"            },\n" +
			"            \"system\": \"FOOORG:FOOSITE:patientid:MR:R\",\n" +
			"            \"value\": \"007811959\"\n" +
			"          }\n" +
			"        ]\n" +
			"      },\n" +
			"      \"request\": {\n" +
			"        \"method\": \"PUT\",\n" +
			"        \"url\": \"/Patient?identifier=FOOORG%3AFOOSITE%3Apatientid%3AMR%3AR%7C007811959%2CFOOORG%3AFOOSITE%3Apatientid%3AMR%3AB%7C000929990%2CFOOORG%3AFOOSITE%3Apatientid%3API%3APH%7C00589363%2Chttp%3A%2F%2Fhl7.org%2Ffhir%2Fsid%2Fus-ssn%7C657-01-8133\"\n" +
			"      }\n" +
			"    },\n" +
			"    {\n" +
			"      \"fullUrl\": \"urn:uuid:b58ff639-11d1-4dac-942f-abf4f9a625d7\",\n" +
			"      \"resource\": {\n" +
			"        \"resourceType\": \"Coverage\",\n" +
			"        \"identifier\": [\n" +
			"          {\n" +
			"            \"system\": \"FOOORG:FOOSITE:coverage:planId\",\n" +
			"            \"value\": \"0403-010101\"\n" +
			"          }\n" +
			"        ],\n" +
			"        \"beneficiary\": {\n" +
			"          \"reference\": \"urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3\"\n" +
			"        }\n" +
			"      },\n" +
			"      \"request\": {\n" +
			"        \"method\": \"PUT\",\n" +
			"        \"url\": \"/Coverage?beneficiary=urn%3Auuid%3Ad2a46176-8e15-405d-bbda-baea1a9dc7f3&identifier=FOOORG%3AFOOSITE%3Acoverage%3AplanId%7C0403-010101\"\n" +
			"      }\n" +
			"    },\n" +
			"    {\n" +
			"      \"fullUrl\": \"urn:uuid:13f5da1a-6601-4c1a-82c9-41527be23fa0\",\n" +
			"      \"resource\": {\n" +
			"        \"resourceType\": \"Coverage\",\n" +
			"        \"contained\": [\n" +
			"          {\n" +
			"            \"resourceType\": \"RelatedPerson\",\n" +
			"            \"id\": \"1\",\n" +
			"            \"name\": [\n" +
			"              {\n" +
			"                \"family\": \"SMITH\",\n" +
			"                \"given\": [\n" +
			"                  \"FAKER\"\n" +
			"                ]\n" +
			"              }\n" +
			"            ]\n" +
			"          },\n" +
			"          {\n" +
			"            \"resourceType\": \"Organization\",\n" +
			"            \"id\": \"2\",\n" +
			"            \"name\": \"MEDICAID\"\n" +
			"          }\n" +
			"        ],\n" +
			"        \"identifier\": [\n" +
			"          {\n" +
			"            \"system\": \"FOOORG:FOOSITE:coverage:planId\",\n" +
			"            \"value\": \"0404-010101\"\n" +
			"          }\n" +
			"        ],\n" +
			"        \"policyHolder\": {\n" +
			"          \"reference\": \"#1\"\n" +
			"        },\n" +
			"        \"beneficiary\": {\n" +
			"          \"reference\": \"urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3\"\n" +
			"        },\n" +
			"        \"payor\": [\n" +
			"          {\n" +
			"            \"reference\": \"#2\"\n" +
			"          }\n" +
			"        ]\n" +
			"      },\n" +
			"      \"request\": {\n" +
			"        \"method\": \"PUT\",\n" +
			"        \"url\": \"/Coverage?beneficiary=urn%3Auuid%3Ad2a46176-8e15-405d-bbda-baea1a9dc7f3&identifier=FOOORG%3AFOOSITE%3Acoverage%3AplanId%7C0404-010101\"\n" +
			"      }\n" +
			"    }\n" +
			"  ]\n" +
			"}";

		Bundle inputBundle = myFhirCtx.newJsonParser().parseResource(Bundle.class, input);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(inputBundle));
		mySystemDao.transaction(mySrd, inputBundle);

		inputBundle = myFhirCtx.newJsonParser().parseResource(Bundle.class, input);
		mySystemDao.transaction(mySrd, inputBundle);

	}

	@Test
	public void testReplaceOneWithAnother() {
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualified();
		assertNotNull(id1);

		ourLog.info("** Replacing");

		pt1 = new Patient();
		pt1.setId(id1);
		pt1.setGender(Enumerations.AdministrativeGender.FEMALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		id1 = myPatientDao.update(pt1).getId().toUnqualified();
		assertNotNull(id1);
		assertEquals("2", id1.getVersionIdPart());

		Patient pt2 = new Patient();
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		pt2.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id2 = myPatientDao.create(pt2).getId().toUnqualifiedVersionless();

		SearchBuilder.resetLastHandlerMechanismForUnitTest();
		SearchParameterMap params = new SearchParameterMap();
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		params.add("birthdate", new DateParam("2011-01-01"));
		IBundleProvider results = myPatientDao.search(params);
		String searchId = results.getUuid();
		assertThat(toUnqualifiedVersionlessIdValues(results), containsInAnyOrder(id2.getValue()));
		assertEquals(SearchBuilder.HandlerTypeEnum.UNIQUE_INDEX, SearchBuilder.getLastHandlerMechanismForUnitTest());

	}

	@Test
	public void testSearchSynchronousUsingUniqueComposite() {
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		pt2.setBirthDateElement(new DateType("2011-01-02"));
		IIdType id2 = myPatientDao.create(pt2).getId().toUnqualifiedVersionless();

		SearchBuilder.resetLastHandlerMechanismForUnitTest();
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronousUpTo(100);
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		params.add("birthdate", new DateParam("2011-01-01"));
		IBundleProvider results = myPatientDao.search(params);
		assertThat(toUnqualifiedVersionlessIdValues(results), containsInAnyOrder(id1.getValue()));
		assertEquals(SearchBuilder.HandlerTypeEnum.UNIQUE_INDEX, SearchBuilder.getLastHandlerMechanismForUnitTest());
	}

	@Test
	public void testSearchUsingUniqueComposite() {
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();
		assertNotNull(id1);

		Patient pt2 = new Patient();
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		pt2.setBirthDateElement(new DateType("2011-01-02"));
		IIdType id2 = myPatientDao.create(pt2).getId().toUnqualifiedVersionless();

		SearchBuilder.resetLastHandlerMechanismForUnitTest();
		SearchParameterMap params = new SearchParameterMap();
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		params.add("birthdate", new DateParam("2011-01-01"));
		IBundleProvider results = myPatientDao.search(params);
		String searchId = results.getUuid();
		assertThat(toUnqualifiedVersionlessIdValues(results), containsInAnyOrder(id1.getValue()));
		assertEquals(SearchBuilder.HandlerTypeEnum.UNIQUE_INDEX, SearchBuilder.getLastHandlerMechanismForUnitTest());

		// Other order
		SearchBuilder.resetLastHandlerMechanismForUnitTest();
		params = new SearchParameterMap();
		params.add("birthdate", new DateParam("2011-01-01"));
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		results = myPatientDao.search(params);
		assertEquals(searchId, results.getUuid());
		String id1Value = id1.getValue();
		List<String> actualValues = toUnqualifiedVersionlessIdValues(results);
		assertThat(actualValues, containsInAnyOrder(id1Value));
		// Null because we just reuse the last search
		assertEquals(null, SearchBuilder.getLastHandlerMechanismForUnitTest());

		SearchBuilder.resetLastHandlerMechanismForUnitTest();
		params = new SearchParameterMap();
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		params.add("birthdate", new DateParam("2011-01-03"));
		results = myPatientDao.search(params);
		assertThat(toUnqualifiedVersionlessIdValues(results), empty());
		assertEquals(SearchBuilder.HandlerTypeEnum.UNIQUE_INDEX, SearchBuilder.getLastHandlerMechanismForUnitTest());

		SearchBuilder.resetLastHandlerMechanismForUnitTest();
		params = new SearchParameterMap();
		params.add("birthdate", new DateParam("2011-01-03"));
		results = myPatientDao.search(params);
		assertThat(toUnqualifiedVersionlessIdValues(results), empty());
		assertEquals(SearchBuilder.HandlerTypeEnum.STANDARD_QUERY, SearchBuilder.getLastHandlerMechanismForUnitTest());

	}

	@Test
	public void testUniqueValuesAreIndexed_DateAndToken() {
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		List<ResourceIndexedCompositeStringUnique> uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(1, uniques.size());
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?birthdate=2011-01-01&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale", uniques.get(0).getIndexString());
	}

	@Test
	public void testUniqueValuesAreIndexed_StringAndReference() {
		createUniqueNameAndManagingOrganizationSps();

		Organization org = new Organization();
		org.setId("Organization/ORG");
		org.setName("ORG");
		myOrganizationDao.update(org);

		Patient pt1 = new Patient();
		pt1.addName()
			.setFamily("FAMILY1")
			.addGiven("GIVEN1")
			.addGiven("GIVEN2")
			.addGiven("GIVEN2"); // GIVEN2 happens twice
		pt1.setManagingOrganization(new Reference("Organization/ORG"));
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		List<ResourceIndexedCompositeStringUnique> uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		Collections.sort(uniques);

		assertEquals(3, uniques.size());
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());

		assertEquals("Patient/" + id1.getIdPart(), uniques.get(1).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=GIVEN1&organization=Organization%2FORG", uniques.get(1).getIndexString());

		assertEquals("Patient/" + id1.getIdPart(), uniques.get(2).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=GIVEN2&organization=Organization%2FORG", uniques.get(2).getIndexString());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


}
