package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

@SuppressWarnings({"unchecked", "deprecation"})
public class FhirResourceDaoR4SortTest extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4SortTest.class);

	@After
	public final void after() {
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
	public void testSortOnId() throws Exception {
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
		assertThat(ids, contains(id1, id2, "Patient/AA", "Patient/AB"));

		map = new SearchParameterMap();
		map.setSort(new SortSpec("_id", SortOrderEnum.DESC));
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(ids, contains("Patient/AB", "Patient/AA", id2, id1));
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
		assertThat(ids, contains(id1, id2, "Patient/AB", "Patient/AA"));

	}

	@Test
	public void testSortOnSearchParameterWhereAllResourcesHaveAValue() throws Exception {
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
		assertThat(ids, contains("Patient/AA", "Patient/AB", "Patient/BA", "Patient/BB"));

		// Same SP as sort
		map = new SearchParameterMap();
		map.add(Patient.SP_ACTIVE, new TokenParam(null, "true"));
		map.setSort(new SortSpec("family", SortOrderEnum.ASC).setChain(new SortSpec("given", SortOrderEnum.ASC)));
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(ids, contains("Patient/AA", "Patient/AB", "Patient/BA", "Patient/BB"));

		// Different SP from sort
		map = new SearchParameterMap();
		map.add(Patient.SP_GENDER, new TokenParam(null, "male"));
		map.setSort(new SortSpec("family", SortOrderEnum.ASC).setChain(new SortSpec("given", SortOrderEnum.ASC)));
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(ids, contains("Patient/AA", "Patient/AB", "Patient/BA", "Patient/BB"));

		map = new SearchParameterMap();
		map.setSort(new SortSpec("gender").setChain(new SortSpec("family", SortOrderEnum.ASC).setChain(new SortSpec("given", SortOrderEnum.ASC))));
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		ourLog.info("IDS: {}", ids);
		assertThat(ids, contains("Patient/AA", "Patient/AB", "Patient/BA", "Patient/BB"));

		map = new SearchParameterMap();
		map.add(Patient.SP_ACTIVE, new TokenParam(null, "true"));
		map.setSort(new SortSpec("family", SortOrderEnum.ASC).setChain(new SortSpec("given", SortOrderEnum.ASC)));
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(ids, contains("Patient/AA", "Patient/AB", "Patient/BA", "Patient/BB"));
	}

	@SuppressWarnings("unused")
	@Test
	public void testSortOnSparselyPopulatedFields() {
//		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);

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
		assertThat(ids, contains(pid1, pid2, pid3, pid4, pid5));
		assertEquals(5, ids.size());

	}

	@Test
	public void testSortOnSparselyPopulatedSearchParameter() throws Exception {
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
		assertThat(ids, containsInAnyOrder("Patient/AA", "Patient/AB", "Patient/BA", "Patient/BB", "Patient/CA"));

		map = new SearchParameterMap();
		map.setSort(new SortSpec("gender").setChain(new SortSpec("family", SortOrderEnum.ASC).setChain(new SortSpec("given", SortOrderEnum.ASC))));
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		ourLog.info("IDS: {}", ids);
		assertThat(ids, contains("Patient/CA", "Patient/AA", "Patient/AB", "Patient/BA", "Patient/BB"));

		map = new SearchParameterMap();
		map.add(Patient.SP_ACTIVE, new TokenParam(null, "true"));
		map.setSort(new SortSpec("family", SortOrderEnum.ASC).setChain(new SortSpec("given", SortOrderEnum.ASC)));
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(ids, contains("Patient/AA", "Patient/AB", "Patient/BA", "Patient/BB"));
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
