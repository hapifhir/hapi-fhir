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
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

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
		assertThat(toUnqualifiedVersionlessIdValues(results), containsInAnyOrder(id1.getValue()));
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
