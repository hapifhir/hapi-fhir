package ca.uhn.fhir.jpa.dao.r4;

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
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;

@SuppressWarnings({"unchecked", "deprecation"})
public class FhirResourceDaoR4UniqueSearchParamTest extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4UniqueSearchParamTest.class);

	@After
	public void after() {
		myDaoConfig.setDefaultSearchParamsCanBeOverridden(new DaoConfig().isDefaultSearchParamsCanBeOverridden());
		myDaoConfig.setUniqueIndexesCheckedBeforeSave(new DaoConfig().isUniqueIndexesCheckedBeforeSave());
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

	private void createUniqueObservationSubjectDateCode() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/obs-subject");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode("subject");
		sp.setExpression("Observation.subject");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		sp.addTarget("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/obs-effective");
		sp.setType(Enumerations.SearchParamType.DATE);
		sp.setCode("date");
		sp.setExpression("Observation.effective");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/obs-code");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setCode("code");
		sp.setExpression("Observation.code");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/observation-subject-date-code");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		sp.setExpression("Observation.code");
		sp.addComponent()
			.setExpression("Observation")
			.setDefinition(new Reference("SearchParameter/obs-subject"));
		sp.addComponent()
			.setExpression("Observation")
			.setDefinition(new Reference("SearchParameter/obs-effective"));
		sp.addComponent()
			.setExpression("Observation")
			.setDefinition(new Reference("SearchParameter/obs-code"));
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
	public void testDuplicateUniqueValuesAreReIndexed() {

		Patient pt1 = new Patient();
		pt1.setActive(true);
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		/*
		 * Both of the following resources will match the unique index we'll
		 * create afterward. So we can create them both, but then when we create
		 * the unique index that matches them both that's a problem...
		 */

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("foo").setCode("bar");
		obs.setSubject(new Reference(pt1.getIdElement().toUnqualifiedVersionless().getValue()));
		obs.setEffective(new DateTimeType("2011-01-01"));
		IIdType id2 = myObservationDao.create(obs).getId().toUnqualifiedVersionless();

		obs = new Observation();
		obs.getCode().addCoding().setSystem("foo").setCode("bar");
		obs.setSubject(new Reference(pt1.getIdElement().toUnqualifiedVersionless().getValue()));
		obs.setEffective(new DateTimeType("2011-01-01"));
		IIdType id3 = myObservationDao.create(obs).getId().toUnqualifiedVersionless();

		ourLog.info("ID1: {}  - ID2: {}  - ID3: {}", id1, id2, id3);

		createUniqueObservationSubjectDateCode();

		mySystemDao.markAllResourcesForReindexing();
		mySystemDao.performReindexingPass(1000);
		mySystemDao.performReindexingPass(1000);

		List<ResourceIndexedCompositeStringUnique> uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 1, uniques.size());
		assertEquals("Observation/" + id2.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Observation?code=foo%7Cbar&date=2011-01-01&subject=Patient%2F" + id1.getIdPart(), uniques.get(0).getIndexString());

		myResourceIndexedCompositeStringUniqueDao.deleteAll();

		mySystemDao.markAllResourcesForReindexing();
		mySystemDao.performReindexingPass(1000);
		mySystemDao.performReindexingPass(1000);

		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 1, uniques.size());
		assertEquals("Observation/" + id2.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Observation?code=foo%7Cbar&date=2011-01-01&subject=Patient%2F" + id1.getIdPart(), uniques.get(0).getIndexString());

	}

	@Test
	public void testDuplicateUniqueValuesAreRejectedWithChecking_TestingDisabled() {
		myDaoConfig.setUniqueIndexesCheckedBeforeSave(false);

		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		try {
			myPatientDao.create(pt1).getId().toUnqualifiedVersionless();
			fail();
		} catch (JpaSystemException e) {
			// good
		}
	}

	@Test
	public void testDuplicateUniqueValuesAreRejectedWithChecking_TestingEnabled() {
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		try {
			myPatientDao.create(pt1).getId().toUnqualifiedVersionless();
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals("Can not create resource of type Patient as it would create a duplicate index matching query: Patient?birthdate=2011-01-01&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale (existing index belongs to Patient/" + id1.getIdPart() + ")", e.getMessage());
		}
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
		String id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless().getValue();

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
		assertThat(toUnqualifiedVersionlessIdValues(results), containsInAnyOrder(id1));
		assertEquals(SearchBuilder.HandlerTypeEnum.UNIQUE_INDEX, SearchBuilder.getLastHandlerMechanismForUnitTest());

		// Other order
		SearchBuilder.resetLastHandlerMechanismForUnitTest();
		params = new SearchParameterMap();
		params.add("birthdate", new DateParam("2011-01-01"));
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		results = myPatientDao.search(params);
		assertEquals(searchId, results.getUuid());
		assertThat(toUnqualifiedVersionlessIdValues(results), containsInAnyOrder(id1));
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
	public void testUniqueValuesAreIndexed_DateAndToken() {
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		List<ResourceIndexedCompositeStringUnique> uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 1, uniques.size());
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?birthdate=2011-01-01&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale", uniques.get(0).getIndexString());
	}

	@Test
	public void testUniqueValuesAreIndexed_RefAndDateAndToken() {
		createUniqueObservationSubjectDateCode();

		List<ResourceIndexedCompositeStringUnique> uniques;
		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 0, uniques.size());

		Patient pt1 = new Patient();
		pt1.setActive(true);
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("foo").setCode("bar");
		obs.setSubject(new Reference(pt1.getIdElement().toUnqualifiedVersionless().getValue()));
		obs.setEffective(new DateTimeType("2011-01-01"));
		IIdType id2 = myObservationDao.create(obs).getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.setActive(false);
		IIdType id3 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		ourLog.info("ID1: {}  -  ID2: {}   - ID3:  {}", id1, id2, id3);

		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 1, uniques.size());
		assertEquals("Observation/" + id2.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Observation?code=foo%7Cbar&date=2011-01-01&subject=Patient%2F" + id1.getIdPart(), uniques.get(0).getIndexString());
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

	@Test
	public void testUniqueValuesAreIndexed_Reference_UsingModifierSyntax() {
		createUniqueNameAndManagingOrganizationSps();
		List<ResourceIndexedCompositeStringUnique> uniques;

		Organization org = new Organization();
		org.setId("Organization/ORG");
		org.setName("ORG");
		myOrganizationDao.update(org);

		Patient pt1 = new Patient();
		pt1.addName().setFamily("FAMILY1");
		pt1.setManagingOrganization(new Reference("Organization/ORG"));

		SearchBuilder.resetLastHandlerMechanismForUnitTest();
		IIdType id1 = myPatientDao.update(pt1, "Patient?name=FAMILY1&organization:Organization=ORG").getId().toUnqualifiedVersionless();
		assertEquals(SearchBuilder.HandlerTypeEnum.UNIQUE_INDEX, SearchBuilder.getLastHandlerMechanismForUnitTest());
		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(1, uniques.size());
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());

		// Again with a change
		pt1 = new Patient();
		pt1.setActive(true);
		pt1.addName().setFamily("FAMILY1");
		pt1.setManagingOrganization(new Reference("Organization/ORG"));

		SearchBuilder.resetLastHandlerMechanismForUnitTest();
		id1 = myPatientDao.update(pt1, "Patient?name=FAMILY1&organization:Organization=ORG").getId().toUnqualifiedVersionless();
		assertEquals(SearchBuilder.HandlerTypeEnum.UNIQUE_INDEX, SearchBuilder.getLastHandlerMechanismForUnitTest());
		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(1, uniques.size());
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());

	}


	@Test
	public void testUniqueValuesAreIndexed_StringAndReference_UsingConditional() {
		createUniqueNameAndManagingOrganizationSps();
		List<ResourceIndexedCompositeStringUnique> uniques;

		Organization org = new Organization();
		org.setId("Organization/ORG");
		org.setName("ORG");
		myOrganizationDao.update(org);

		Patient pt1 = new Patient();
		pt1.addName().setFamily("FAMILY1");
		pt1.setManagingOrganization(new Reference("Organization/ORG"));
		IIdType id1 = myPatientDao.update(pt1, "Patient?name=FAMILY1&organization.name=ORG").getId().toUnqualifiedVersionless();

		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(1, uniques.size());
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());

		// Again

		pt1 = new Patient();
		pt1.addName().setFamily("FAMILY1");
		pt1.setManagingOrganization(new Reference("Organization/ORG"));
		id1 = myPatientDao.update(pt1, "Patient?name=FAMILY1&organization.name=ORG").getId().toUnqualifiedVersionless();

		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(1, uniques.size());
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());
	}

	@Test
	public void testUniqueValuesAreIndexed_StringAndReference_UsingConditionalInTransaction() {
		createUniqueNameAndManagingOrganizationSps();
		List<ResourceIndexedCompositeStringUnique> uniques;

		Organization org = new Organization();
		org.setId("Organization/ORG");
		org.setName("ORG");
		myOrganizationDao.update(org);

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);

		String orgId = "urn:uuid:" + UUID.randomUUID().toString();
		org = new Organization();
		org.setName("ORG");
		bundle
			.addEntry()
			.setResource(org)
			.setFullUrl(orgId)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("/Organization?name=ORG");

		Patient pt1 = new Patient();
		pt1.addName().setFamily("FAMILY1");
		pt1.setManagingOrganization(new Reference(orgId));
		bundle
			.addEntry()
			.setResource(pt1)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("/Patient?name=FAMILY1&organization=" + orgId.replace(":", "%3A"));

		Bundle resp = mySystemDao.transaction(mySrd, bundle);

		IIdType id1 = new IdType(resp.getEntry().get(1).getResponse().getLocation());

		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(1, uniques.size());
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());

		// Again

		bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);

		orgId = IdType.newRandomUuid().getValue();
		org = new Organization();
		org.setName("ORG");
		bundle
			.addEntry()
			.setResource(org)
			.setFullUrl(orgId)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("/Organization?name=ORG");

		pt1 = new Patient();
		pt1.addName().setFamily("FAMILY1");
		pt1.setManagingOrganization(new Reference(orgId));
		bundle
			.addEntry()
			.setResource(pt1)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("/Patient?name=FAMILY1&organization=" + orgId);

		resp = mySystemDao.transaction(mySrd, bundle);

		id1 = new IdType(resp.getEntry().get(1).getResponse().getLocation());

		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(1, uniques.size());
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());

	}

	@Test
	public void testUniqueValuesAreNotIndexedIfNotAllParamsAreFound_DateAndToken() {
		createUniqueBirthdateAndGenderSps();

		Patient pt;
		List<ResourceIndexedCompositeStringUnique> uniques;

		pt = new Patient();
		pt.setGender(Enumerations.AdministrativeGender.MALE);
		myPatientDao.create(pt).getId().toUnqualifiedVersionless();
		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 0, uniques.size());

		pt = new Patient();
		myPatientDao.create(pt).getId().toUnqualifiedVersionless();
		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 0, uniques.size());

		pt = new Patient();
		pt.setBirthDateElement(new DateType());
		pt.setGender(Enumerations.AdministrativeGender.MALE);
		myPatientDao.create(pt).getId().toUnqualifiedVersionless();
		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 0, uniques.size());

	}

	@Test
	public void testUniqueValuesAreNotIndexedIfNotAllParamsAreFound_StringAndReference() {
		createUniqueNameAndManagingOrganizationSps();

		Organization org = new Organization();
		org.setId("Organization/ORG");
		org.setName("ORG");
		myOrganizationDao.update(org);

		List<ResourceIndexedCompositeStringUnique> uniques;
		Patient pt;

		pt = new Patient();
		pt.setManagingOrganization(new Reference("Organization/ORG"));
		myPatientDao.create(pt).getId().toUnqualifiedVersionless();
		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 0, uniques.size());

		pt = new Patient();
		pt.addName()
			.setFamily("FAMILY1")
			.addGiven("GIVEN1")
			.addGiven("GIVEN2")
			.addGiven("GIVEN2"); // GIVEN2 happens twice
		myPatientDao.create(pt).getId().toUnqualifiedVersionless();
		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 0, uniques.size());

		pt = new Patient();
		pt.setActive(true);
		myPatientDao.create(pt).getId().toUnqualifiedVersionless();
		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 0, uniques.size());
	}

	@Test
	public void testUniqueValuesAreReIndexed() {
		createUniqueObservationSubjectDateCode();

		Patient pt1 = new Patient();
		pt1.setActive(true);
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("foo").setCode("bar");
		obs.setSubject(new Reference(pt1.getIdElement().toUnqualifiedVersionless().getValue()));
		obs.setEffective(new DateTimeType("2011-01-01"));
		IIdType id2 = myObservationDao.create(obs).getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.setActive(false);
		myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		mySystemDao.markAllResourcesForReindexing();
		mySystemDao.performReindexingPass(1000);

		List<ResourceIndexedCompositeStringUnique> uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 1, uniques.size());
		assertEquals("Observation/" + id2.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Observation?code=foo%7Cbar&date=2011-01-01&subject=Patient%2F" + id1.getIdPart(), uniques.get(0).getIndexString());

		myResourceIndexedCompositeStringUniqueDao.deleteAll();

		mySystemDao.markAllResourcesForReindexing();
		mySystemDao.performReindexingPass(1000);

		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 1, uniques.size());
		assertEquals("Observation/" + id2.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Observation?code=foo%7Cbar&date=2011-01-01&subject=Patient%2F" + id1.getIdPart(), uniques.get(0).getIndexString());

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


}
