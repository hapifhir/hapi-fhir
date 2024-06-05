package ca.uhn.fhir.jpa.dao.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"deprecation"})
public class FhirResourceDaoR4SortTest extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4SortTest.class);

	@AfterEach
	public final void after() {
		myStorageSettings.setIndexMissingFields(new JpaStorageSettings().getIndexMissingFields());
	}

	@Test
	public void testSearchWithEmptySort() {
		SearchParameterMap criteriaUrl = new SearchParameterMap();
		DateRangeParam range = new DateRangeParam();
		range.setLowerBound(new DateParam(ParamPrefixEnum.GREATERTHAN, 1000000));
		range.setUpperBound(new DateParam(ParamPrefixEnum.LESSTHAN, 2000000));
		criteriaUrl.setLastUpdated(range);
		criteriaUrl.setSort(new SortSpec(Constants.PARAM_LASTUPDATED, SortOrderEnum.ASC));
		IBundleProvider results = myObservationDao.search(criteriaUrl);
		assertEquals(0, results.size().intValue());
	}

	@Test
	public void testSortOnId() {
		// Numeric ID
		Patient p01 = new Patient();
		p01.setActive(true);
		p01.setGender(AdministrativeGender.MALE);
		p01.addName().setFamily("B").addGiven("A");
		String id1 = myPatientDao.create(p01).getId().toUnqualifiedVersionless().getValue();

		// Numeric ID
		Patient p02 = new Patient();
		p02.setActive(true);
		p02.setGender(AdministrativeGender.MALE);
		p02.addName().setFamily("B").addGiven("B");
		p02.addName().setFamily("Z").addGiven("Z");
		String id2 = myPatientDao.create(p02).getId().toUnqualifiedVersionless().getValue();

		// Forced ID
		Patient pAB = new Patient();
		pAB.setId("AB");
		pAB.setActive(true);
		pAB.setGender(AdministrativeGender.MALE);
		pAB.addName().setFamily("A").addGiven("B");
		myPatientDao.update(pAB);

		// Forced ID
		Patient pAA = new Patient();
		pAA.setId("AA");
		pAA.setActive(true);
		pAA.setGender(AdministrativeGender.MALE);
		pAA.addName().setFamily("A").addGiven("A");
		myPatientDao.update(pAA);

		SearchParameterMap map;
		List<String> ids;

		map = new SearchParameterMap();
		map.setSort(new SortSpec("_id", SortOrderEnum.ASC));
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(ids).containsExactly(id1, id2, "Patient/AA", "Patient/AB");

		map = new SearchParameterMap();
		map.setSort(new SortSpec("_id", SortOrderEnum.DESC));
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(ids).containsExactly("Patient/AB", "Patient/AA", id2, id1);
	}

	@Test
	public void testSortOnLastUpdated() throws Exception {
		// Numeric ID
		Patient p01 = new Patient();
		p01.setActive(true);
		p01.setGender(AdministrativeGender.MALE);
		p01.addName().setFamily("B").addGiven("A");
		String id1 = myPatientDao.create(p01).getId().toUnqualifiedVersionless().getValue();

		Thread.sleep(10);

		// Numeric ID
		Patient p02 = new Patient();
		p02.setActive(true);
		p02.setGender(AdministrativeGender.MALE);
		p02.addName().setFamily("B").addGiven("B");
		p02.addName().setFamily("Z").addGiven("Z");
		String id2 = myPatientDao.create(p02).getId().toUnqualifiedVersionless().getValue();

		Thread.sleep(10);

		// Forced ID
		Patient pAB = new Patient();
		pAB.setId("AB");
		pAB.setActive(true);
		pAB.setGender(AdministrativeGender.MALE);
		pAB.addName().setFamily("A").addGiven("B");
		myPatientDao.update(pAB);

		Thread.sleep(10);

		// Forced ID
		Patient pAA = new Patient();
		pAA.setId("AA");
		pAA.setActive(true);
		pAA.setGender(AdministrativeGender.MALE);
		pAA.addName().setFamily("A").addGiven("A");
		myPatientDao.update(pAA);

		SearchParameterMap map;
		List<String> ids;

		map = new SearchParameterMap();
		map.setSort(new SortSpec("_lastUpdated", SortOrderEnum.ASC));
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(ids).containsExactly(id1, id2, "Patient/AB", "Patient/AA");

	}

	@Test
	public void testSortOnSearchParameterWhereAllResourcesHaveAValue() {
		Patient pBA = new Patient();
		pBA.setId("BA");
		pBA.setActive(true);
		pBA.setGender(AdministrativeGender.MALE);
		pBA.addName().setFamily("B").addGiven("A");
		myPatientDao.update(pBA);

		Patient pBB = new Patient();
		pBB.setId("BB");
		pBB.setActive(true);
		pBB.setGender(AdministrativeGender.MALE);
		pBB.addName().setFamily("B").addGiven("B");
		pBB.addName().setFamily("Z").addGiven("Z");
		myPatientDao.update(pBB);

		Patient pAB = new Patient();
		pAB.setId("AB");
		pAB.setActive(true);
		pAB.setGender(AdministrativeGender.MALE);
		pAB.addName().setFamily("A").addGiven("B");
		myPatientDao.update(pAB);

		Patient pAA = new Patient();
		pAA.setId("AA");
		pAA.setActive(true);
		pAA.setGender(AdministrativeGender.MALE);
		pAA.addName().setFamily("A").addGiven("A");
		myPatientDao.update(pAA);

		SearchParameterMap map;
		List<String> ids;

		// No search param
		map = new SearchParameterMap();
		map.setSort(new SortSpec("family", SortOrderEnum.ASC).setChain(new SortSpec("given", SortOrderEnum.ASC)));
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(ids).containsExactly("Patient/AA", "Patient/AB", "Patient/BA", "Patient/BB");

		// Same SP as sort
		map = new SearchParameterMap();
		map.add(Patient.SP_ACTIVE, new TokenParam(null, "true"));
		map.setSort(new SortSpec("family", SortOrderEnum.ASC).setChain(new SortSpec("given", SortOrderEnum.ASC)));
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(ids).containsExactly("Patient/AA", "Patient/AB", "Patient/BA", "Patient/BB");

		// Different SP from sort
		map = new SearchParameterMap();
		map.add(Patient.SP_GENDER, new TokenParam(null, "male"));
		map.setSort(new SortSpec("family", SortOrderEnum.ASC).setChain(new SortSpec("given", SortOrderEnum.ASC)));
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(ids).containsExactly("Patient/AA", "Patient/AB", "Patient/BA", "Patient/BB");

		map = new SearchParameterMap();
		map.setSort(new SortSpec("gender").setChain(new SortSpec("family", SortOrderEnum.ASC).setChain(new SortSpec("given", SortOrderEnum.ASC))));
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		ourLog.info("IDS: {}", ids);
		assertThat(ids).containsExactly("Patient/AA", "Patient/AB", "Patient/BA", "Patient/BB");

		map = new SearchParameterMap();
		map.add(Patient.SP_ACTIVE, new TokenParam(null, "true"));
		map.setSort(new SortSpec("family", SortOrderEnum.ASC).setChain(new SortSpec("given", SortOrderEnum.ASC)));
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(ids).containsExactly("Patient/AA", "Patient/AB", "Patient/BA", "Patient/BB");
	}

	@SuppressWarnings("unused")
	@Test
	public void testSortOnSparselyPopulatedFields() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);

		IIdType pid1, pid2, pid3, pid4, pid5, pid6;
		{
			Patient p = new Patient();
			p.setId("pid1");
			p.setActive(true);
			pid1 = myPatientDao.update(p, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient p = new Patient();
			p.setId("pid2");
			p.addName().setFamily("A");
			pid2 = myPatientDao.update(p, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient p = new Patient();
			p.setId("pid3");
			p.addName().setFamily("B");
			pid3 = myPatientDao.update(p, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient p = new Patient();
			p.setId("pid4");
			p.addName().setFamily("B").addGiven("A");
			pid4 = myPatientDao.update(p, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient p = new Patient();
			p.setId("pid5");
			p.addName().setFamily("B").addGiven("B");
			pid5 = myPatientDao.update(p, mySrd).getId().toUnqualifiedVersionless();
		}

		SearchParameterMap map;
		List<IIdType> ids;

		map = new SearchParameterMap();
		map.setSort(new SortSpec(Patient.SP_FAMILY, SortOrderEnum.ASC).setChain(new SortSpec(Patient.SP_GIVEN, SortOrderEnum.ASC)));
		ids = toUnqualifiedVersionlessIds(myPatientDao.search(map));
		ourLog.info("** Got IDs: {}", ids);
		assertThat(ids).as(ids.toString()).containsExactly(pid2, pid4, pid5, pid3, pid1);
		assertThat(ids).hasSize(5);

	}

	@Test
	public void testSortOnSparselyPopulatedSearchParameter() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);

		Patient pCA = new Patient();
		pCA.setId("CA");
		pCA.setActive(false);
		pCA.getAddressFirstRep().addLine("A");
		pCA.addName().setFamily("C").addGiven("A");
		pCA.addName().setFamily("Z").addGiven("A");
		myPatientDao.update(pCA);

		Patient pBA = new Patient();
		pBA.setId("BA");
		pBA.setActive(true);
		pBA.setGender(AdministrativeGender.MALE);
		pBA.addName().setFamily("B").addGiven("A");
		myPatientDao.update(pBA);

		Patient pBB = new Patient();
		pBB.setId("BB");
		pBB.setActive(true);
		pBB.setGender(AdministrativeGender.MALE);
		pBB.addName().setFamily("B").addGiven("B");
		myPatientDao.update(pBB);

		Patient pAB = new Patient();
		pAB.setId("AB");
		pAB.setActive(true);
		pAB.setGender(AdministrativeGender.MALE);
		pAB.addName().setFamily("A").addGiven("B");
		myPatientDao.update(pAB);

		Patient pAA = new Patient();
		pAA.setId("AA");
		pAA.setActive(true);
		pAA.setGender(AdministrativeGender.MALE);
		pAA.addName().setFamily("A").addGiven("A");
		myPatientDao.update(pAA);

		SearchParameterMap map;
		List<String> ids;

		map = new SearchParameterMap();
		map.setSort(new SortSpec("gender"));
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		ourLog.info("IDS: {}", ids);
		assertThat(ids).containsExactlyInAnyOrder("Patient/AA", "Patient/AB", "Patient/BA", "Patient/BB", "Patient/CA");

		map = new SearchParameterMap();
		map.setSort(new SortSpec("gender").setChain(new SortSpec("family", SortOrderEnum.ASC).setChain(new SortSpec("given", SortOrderEnum.ASC))));
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		ourLog.info("IDS: {}", ids);
		assertThat(ids).containsExactly("Patient/AA", "Patient/AB", "Patient/BA", "Patient/BB", "Patient/CA");

		map = new SearchParameterMap();
		map.add(Patient.SP_ACTIVE, new TokenParam(null, "true"));
		map.setSort(new SortSpec("family", SortOrderEnum.ASC).setChain(new SortSpec("given", SortOrderEnum.ASC)));
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(ids).containsExactly("Patient/AA", "Patient/AB", "Patient/BA", "Patient/BB");
	}

	@Test
	public void testSortWithChainedSearch() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);

		Patient pCA = new Patient();
		pCA.setId("CA");
		pCA.addIdentifier().setSystem("PCA").setValue("PCA");
		myPatientDao.update(pCA);

		Observation obs1 = new Observation();
		obs1.setId("OBS1");
		obs1.getSubject().setReference("Patient/CA");
		obs1.setEffective(new DateTimeType("2000-01-01"));
		myObservationDao.update(obs1);

		Observation obs2 = new Observation();
		obs2.setId("OBS2");
		obs2.getSubject().setReference("Patient/CA");
		obs2.setEffective(new DateTimeType("2000-02-02"));
		myObservationDao.update(obs2);

		SearchParameterMap map;
		List<String> ids;

		runInTransaction(() -> ourLog.info("Dates:\n * {}", myResourceIndexedSearchParamDateDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * "))));

		myTestDaoSearch.assertSearchFinds(
			"chained search",
		"Observation?subject.identifier=PCA|PCA&_sort=-date",
			"OBS2", "OBS1"
			);
	}

	/**
	 * Define a composition SP for document Bundles, and sort by it.
	 * The chain is referencing the Bundle contents.
	 * @see https://smilecdr.com/docs/fhir_storage_relational/chained_searches_and_sorts.html#document-and-message-search-parameters
	 */
	@Test
	void testSortByCompositionSP() {
	    // given
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setId("bundle-composition-patient-birthdate");
		searchParameter.setCode("composition.patient.birthdate");
		searchParameter.setName("composition.patient.birthdate");
		searchParameter.setUrl("http://example.org/SearchParameter/bundle-composition-patient-birthdate");
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		searchParameter.addBase("Bundle");
		searchParameter.setType(Enumerations.SearchParamType.DATE);
		searchParameter.setExpression("Bundle.entry.resource.ofType(Patient).birthDate");
		doUpdateResource(searchParameter);

		mySearchParamRegistry.forceRefresh();

		Patient pat1 = buildResource("Patient", withId("pat1"), withBirthdate("2001-03-17"));
		doUpdateResource(pat1);
		Bundle pat1Bundle = buildCompositionBundle(pat1);
		String pat1BundleId = doCreateResource(pat1Bundle).getIdPart();

		Patient pat2 = buildResource("Patient", withId("pat2"), withBirthdate("2000-01-01"));
		doUpdateResource(pat2);
		Bundle pat2Bundle = buildCompositionBundle(pat2);
		String pat2BundleId = doCreateResource(pat2Bundle).getIdPart();

		// then
		myTestDaoSearch.assertSearchFinds("sort by contained date",
			"Bundle?_sort=composition.patient.birthdate", List.of(pat2BundleId, pat1BundleId));
		myTestDaoSearch.assertSearchFinds("reverse sort by contained date",
			"Bundle?_sort=-composition.patient.birthdate", List.of(pat1BundleId, pat2BundleId));
	}

	private static Bundle buildCompositionBundle(Patient pat11) {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.DOCUMENT);
		Composition composition = new Composition();
		composition.setType(new CodeableConcept().addCoding(new Coding().setCode("code").setSystem("http://example.org")));
		bundle.addEntry().setResource(composition);
		composition.getSubject().setReference(pat11.getIdElement().getValue());
		bundle.addEntry().setResource(pat11);
		return bundle;
	}


}
