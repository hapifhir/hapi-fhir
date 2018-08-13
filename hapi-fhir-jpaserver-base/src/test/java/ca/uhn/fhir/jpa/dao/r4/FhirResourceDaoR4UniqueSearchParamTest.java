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
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@SuppressWarnings({"unchecked", "deprecation"})
@TestPropertySource(properties = {
	// Since scheduled tasks can cause searches, which messes up the
	// value returned by SearchBuilder.getLastHandlerMechanismForUnitTest()
	"scheduling_disabled=true"
})
public class FhirResourceDaoR4UniqueSearchParamTest extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4UniqueSearchParamTest.class);

	@After
	public void after() {
		myDaoConfig.setDefaultSearchParamsCanBeOverridden(new DaoConfig().isDefaultSearchParamsCanBeOverridden());
		myDaoConfig.setUniqueIndexesCheckedBeforeSave(new DaoConfig().isUniqueIndexesCheckedBeforeSave());
		myDaoConfig.setSchedulingDisabled(new DaoConfig().isSchedulingDisabled());
	}

	@Before
	public void before() {
		myDaoConfig.setDefaultSearchParamsCanBeOverridden(true);
		myDaoConfig.setSchedulingDisabled(true);
		SearchBuilder.resetLastHandlerMechanismForUnitTest();
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
			.setDefinition("SearchParameter/patient-gender");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-birthdate");
		sp.addExtension()
			.setUrl(JpaConstants.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp);

		mySearchParamRegsitry.forceRefresh();

		SearchBuilder.resetLastHandlerMechanismForUnitTest();
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
			.setDefinition("/SearchParameter/coverage-beneficiary");
		sp.addComponent()
			.setExpression("Coverage")
			.setDefinition("/SearchParameter/coverage-identifier");
		sp.addExtension()
			.setUrl(JpaConstants.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp);
		mySearchParamRegsitry.forceRefresh();
	}


	private void createUniqueIndexObservationSubject() {

		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/observation-subject");
		sp.setCode("observation-subject");
		sp.setExpression("Observation.subject");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/observation-uniq-subject");
		sp.setCode("observation-uniq-subject");
		sp.setExpression("Observation.subject");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		sp.addComponent()
			.setExpression("Observation")
			.setDefinition("/SearchParameter/observation-subject");
		sp.addExtension()
			.setUrl(JpaConstants.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp);
		mySearchParamRegsitry.forceRefresh();
	}


	private void createUniqueIndexPatientIdentifier() {

		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-identifier");
		sp.setCode("identifier");
		sp.setExpression("Patient.identifier");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-uniq-identifier");
		sp.setCode("patient-uniq-identifier");
		sp.setExpression("Patient.identifier");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("/SearchParameter/patient-identifier");
		sp.addExtension()
			.setUrl(JpaConstants.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp);
		mySearchParamRegsitry.forceRefresh();
	}


	private void createUniqueIndexPatientIdentifierCount1() {

		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-identifier");
		sp.setCode("first-identifier");
		sp.setExpression("Patient.identifier.first()");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-uniq-identifier");
		sp.setCode("patient-uniq-identifier");
		sp.setExpression("Patient.identifier");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("/SearchParameter/patient-identifier");
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
			.setDefinition("SearchParameter/patient-name");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-organization");
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
			.setDefinition("SearchParameter/obs-subject");
		sp.addComponent()
			.setExpression("Observation")
			.setDefinition("SearchParameter/obs-effective");
		sp.addComponent()
			.setExpression("Observation")
			.setDefinition("SearchParameter/obs-code");
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
		assertTrue(params.get(0).isUnique());
		assertEquals(2, params.get(0).getCompositeOf().size());
		// Should be alphabetical order
		assertEquals("birthdate", params.get(0).getCompositeOf().get(0).getName());
		assertEquals("gender", params.get(0).getCompositeOf().get(1).getName());
	}


	@Test
	public void testDoubleMatching() {
		createUniqueIndexPatientIdentifier();

		Patient pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setResource(pt)
			.setFullUrl("Patient")
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("/Patient")
			.setIfNoneExist("/Patient?identifier=urn|111,urn|222");
		mySystemDao.transaction(mySrd, input);

		myEntityManager.clear();

		pt = new Patient();
		pt.setActive(true);
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");

		input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setResource(pt)
			.setFullUrl("Patient")
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("/Patient")
			.setIfNoneExist("/Patient?identifier=urn|111,urn|222");
		mySystemDao.transaction(mySrd, input);

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus status) {
				List<ResourceIndexedCompositeStringUnique> all = myResourceIndexedCompositeStringUniqueDao.findAll();
				assertEquals(2, all.size());
			}
		});

	}

	@Test
	public void testDoubleMatchingPut() {
		createUniqueIndexPatientIdentifier();

		Patient pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setResource(pt)
			.setFullUrl("Patient")
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("/Patient?identifier=urn|111,urn|222");
		mySystemDao.transaction(mySrd, input);

		myEntityManager.clear();

		pt = new Patient();
		pt.setActive(true);
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");

		input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setResource(pt)
			.setFullUrl("Patient")
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("/Patient?identifier=urn|111,urn|222");
		mySystemDao.transaction(mySrd, input);

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus status) {
				List<ResourceIndexedCompositeStringUnique> all = myResourceIndexedCompositeStringUniqueDao.findAll();
				assertEquals(2, all.size());
			}
		});

	}

	@Test
	public void testDuplicateUniqueValuesAreReIndexed() {
		myDaoConfig.setSchedulingDisabled(true);

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
		assertThat(uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue(), either(equalTo("Observation/" + id2.getIdPart())).or(equalTo("Observation/" + id3.getIdPart())));
		assertEquals("Observation?code=foo%7Cbar&date=2011-01-01&subject=Patient%2F" + id1.getIdPart(), uniques.get(0).getIndexString());

		myResourceIndexedCompositeStringUniqueDao.deleteAll();

		assertEquals(1, mySearchParamRegsitry.getActiveUniqueSearchParams("Observation").size());

		assertEquals(7, mySystemDao.markAllResourcesForReindexing());
		mySystemDao.performReindexingPass(1000);
		mySystemDao.performReindexingPass(1000);

		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 1, uniques.size());
		assertThat(uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue(), either(equalTo("Observation/" + id2.getIdPart())).or(equalTo("Observation/" + id3.getIdPart())));
		assertEquals("Observation?code=foo%7Cbar&date=2011-01-01&subject=Patient%2F" + id1.getIdPart(), uniques.get(0).getIndexString());

	}

	@Test
	public void testDuplicateUniqueValuesAreRejectedWithChecking_TestingDisabled() {
		myDaoConfig.setUniqueIndexesCheckedBeforeSave(false);

		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		try {
			myPatientDao.create(pt1).getId().toUnqualifiedVersionless();
			fail();
		} catch (ResourceVersionConflictException e) {
			assertEquals("The operation has failed with a unique index constraint failure. This probably means that the operation was trying to create/update a resource that would have resulted in a duplicate value for a unique index.", e.getMessage());
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
	public void testIndexFirstMatchOnly() {
		createUniqueIndexPatientIdentifierCount1();

		Patient pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");
		myPatientDao.create(pt);

		pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");
		try {
			myPatientDao.create(pt);
			fail();
		} catch (PreconditionFailedException e) {
			// good
		}

		pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("333");
		pt.addIdentifier().setSystem("urn").setValue("222");
		myPatientDao.create(pt);

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus status) {
				List<ResourceIndexedCompositeStringUnique> all = myResourceIndexedCompositeStringUniqueDao.findAll();
				assertEquals(2, all.size());
			}
		});

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
	public void testNonTransaction() {
		createUniqueBirthdateAndGenderSps();

		Patient p = new Patient();
		p.setGender(Enumerations.AdministrativeGender.MALE);
		p.setBirthDateElement(new DateType("2001-01-01"));

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setResource(p)
			.setFullUrl("Patient")
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("/Patient")
			.setIfNoneExist("Patient?gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&birthdate=2001-01-01");

		Bundle output0 = mySystemDao.transaction(mySrd, input);
		Bundle output1 = mySystemDao.transaction(mySrd, input);
		assertEquals(output1.getEntry().get(0).getFullUrl(), output0.getEntry().get(0).getFullUrl());
		Bundle output2 = mySystemDao.transaction(mySrd, input);
		assertEquals(output2.getEntry().get(0).getFullUrl(), output0.getEntry().get(0).getFullUrl());

	}


	@Test
	public void testObservationSubject() {
		createUniqueIndexObservationSubject();

		Patient pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");
		IIdType ptid = myPatientDao.create(pt).getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.setSubject(new Reference(ptid));
		IIdType encid = myEncounterDao.create(enc).getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.setSubject(new Reference(ptid));
		obs.setContext(new Reference(encid));
		myObservationDao.create(obs);

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus status) {
				List<ResourceIndexedCompositeStringUnique> all = myResourceIndexedCompositeStringUniqueDao.findAll();
				assertEquals(all.toString(), 1, all.size());
			}
		});

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
		myPatientDao.create(pt2).getId().toUnqualifiedVersionless();

		SearchBuilder.resetLastHandlerMechanismForUnitTest();
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronousUpTo(100);
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		params.add("birthdate", new DateParam("2011-01-01"));
		IBundleProvider results = myPatientDao.search(params);
		assertThat(toUnqualifiedVersionlessIdValues(results), containsInAnyOrder(id1.getValue()));
		assertEquals(SearchBuilder.getLastHandlerParamsForUnitTest(), SearchBuilder.HandlerTypeEnum.UNIQUE_INDEX, SearchBuilder.getLastHandlerMechanismForUnitTest());
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
		myPatientDao.create(pt2).getId().toUnqualifiedVersionless();

		SearchBuilder.resetLastHandlerMechanismForUnitTest();
		SearchParameterMap params = new SearchParameterMap();
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		params.add("birthdate", new DateParam("2011-01-01"));
		IBundleProvider results = myPatientDao.search(params);
		String searchId = results.getUuid();
		assertThat(toUnqualifiedVersionlessIdValues(results), containsInAnyOrder(id1));
		assertEquals(SearchBuilder.getLastHandlerParamsForUnitTest(), SearchBuilder.HandlerTypeEnum.UNIQUE_INDEX, SearchBuilder.getLastHandlerMechanismForUnitTest());

		// Other order
		SearchBuilder.resetLastHandlerMechanismForUnitTest();
		params = new SearchParameterMap();
		params.add("birthdate", new DateParam("2011-01-01"));
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		results = myPatientDao.search(params);
		assertEquals(searchId, results.getUuid());
		assertThat(toUnqualifiedVersionlessIdValues(results), containsInAnyOrder(id1));
		// Null because we just reuse the last search
		assertNull(SearchBuilder.getLastHandlerMechanismForUnitTest());

		SearchBuilder.resetLastHandlerMechanismForUnitTest();
		params = new SearchParameterMap();
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		params.add("birthdate", new DateParam("2011-01-03"));
		results = myPatientDao.search(params);
		assertThat(toUnqualifiedVersionlessIdValues(results), empty());
		assertEquals(SearchBuilder.getLastHandlerParamsForUnitTest(), SearchBuilder.HandlerTypeEnum.UNIQUE_INDEX, SearchBuilder.getLastHandlerMechanismForUnitTest());

		SearchBuilder.resetLastHandlerMechanismForUnitTest();
		params = new SearchParameterMap();
		params.add("birthdate", new DateParam("2011-01-03"));
		results = myPatientDao.search(params);
		assertThat(toUnqualifiedVersionlessIdValues(results), empty());
		assertEquals(SearchBuilder.getLastHandlerParamsForUnitTest(), SearchBuilder.HandlerTypeEnum.STANDARD_QUERY, SearchBuilder.getLastHandlerMechanismForUnitTest());

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
		assertEquals(SearchBuilder.getLastHandlerParamsForUnitTest(), SearchBuilder.HandlerTypeEnum.UNIQUE_INDEX, SearchBuilder.getLastHandlerMechanismForUnitTest());
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
		assertEquals(SearchBuilder.getLastHandlerParamsForUnitTest(), SearchBuilder.HandlerTypeEnum.UNIQUE_INDEX, SearchBuilder.getLastHandlerMechanismForUnitTest());
		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(1, uniques.size());
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());

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
