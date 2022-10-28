package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.search.PersistedJpaSearchFirstPageBundleProvider;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.hl7.fhir.r4.model.BodyStructure;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.EpisodeOfCare;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings({"unchecked", "Duplicates"})
public class FhirResourceDaoR4SearchIncludeTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4SearchIncludeTest.class);

	@AfterEach
	public void afterEach() {
		myDaoConfig.setMaximumIncludesToLoadPerPage(DaoConfig.DEFAULT_MAXIMUM_INCLUDES_TO_LOAD_PER_PAGE);
	}

	@Test
	public void testIncludesNotAppliedToIncludedResources() {
		createOrganizationWithReferencingEpisodesOfCare(10);

		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add("_id", new TokenParam("EOC-0"))
			.addInclude(new Include("*"))
			.addRevInclude(new Include("*").setRecurse(true));
		IBundleProvider results = myEpisodeOfCareDao.search(map);
		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids.toString(), ids, containsInAnyOrder("EpisodeOfCare/EOC-0", "Organization/ORG-0"));
	}

	@Test
	public void testRevIncludesPaged_SyncSearchWithCount() {
		createOrganizationWithReferencingEpisodesOfCare(10);

		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.setCount(10)
			.addInclude(new Include("*"))
			.addRevInclude(new Include("*").setRecurse(true));
		IBundleProvider results = myOrganizationDao.search(map);
		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		Collection<Matcher<String>> expected = IntStream.range(0, 10).mapToObj(t -> equalTo("EpisodeOfCare/EOC-" + t)).collect(Collectors.toList());
		expected.add(equalTo("Organization/ORG-0"));
		expected.add(equalTo("Organization/ORG-P"));
		assertThat(ids.toString(), ids, new IsIterableContainingInAnyOrder(expected));
	}

	@Test
	public void testRevIncludesPaged_SyncSearchWithoutCount() {
		createOrganizationWithReferencingEpisodesOfCare(10);
		myDaoConfig.setMaximumIncludesToLoadPerPage(5);

		logAllResourceLinks();

		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add("_id", new TokenParam("ORG-0"))
			.addInclude(new Include("*"))
			.addRevInclude(new Include("*").setRecurse(true));
		IBundleProvider results = myOrganizationDao.search(map);
		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids.toString(), ids, containsInAnyOrder(
			"EpisodeOfCare/EOC-0",
			"EpisodeOfCare/EOC-1",
			"EpisodeOfCare/EOC-2",
			"EpisodeOfCare/EOC-3",
			"EpisodeOfCare/EOC-4",
			"Organization/ORG-0"
		));
	}

	@Test
	public void testSearchWithIncludeSpecDoesNotCauseNPE() {
		createPatientWithReferencingCarePlan(1);

		// First verify it with the "." syntax
		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.addInclude(new Include("CarePlan.patient"));
		try {
			myCarePlanDao.search(map);
			fail();
		} catch (Exception e) {
			// good
		}

		// Next verify it with the ":" syntax
		SearchParameterMap map2 = SearchParameterMap.newSynchronous()
			.addInclude(new Include("CarePlan:patient"));
		try {
			IBundleProvider results = myCarePlanDao.search(map2);
			List<String> ids = toUnqualifiedVersionlessIdValues(results);
			assertThat(ids.toString(), ids, containsInAnyOrder("CarePlan/CP-1", "Patient/PAT-1"));
		} catch (Exception e) {
			fail();
		}
	}


	@Test
	public void testRevIncludeOnIncludedResource() {
		SearchParameter sp = new SearchParameter();
		sp.addBase("Procedure");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setCode("focalAccess");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setExpression("Procedure.extension('http://fkcfhir.org/fhir/cs/CS1MachNumber')");
		sp.addTarget("BodyStructure");
		mySearchParameterDao.create(sp, mySrd);
		mySearchParamRegistry.forceRefresh();

		BodyStructure bs = new BodyStructure();
		bs.setId("B51936689");
		bs.setDescription("Foo");
		myBodyStructureDao.update(bs, mySrd);

		Procedure p = new Procedure();
		p.setId("PRA8780542726");
		p.setStatus(org.hl7.fhir.r4.model.Procedure.ProcedureStatus.COMPLETED);
		myProcedureDao.update(p, mySrd);

		p = new Procedure();
		p.setId("PRA8780542785");
		p.addPartOf().setReference("Procedure/PRA8780542726");
		p.setStatus(org.hl7.fhir.r4.model.Procedure.ProcedureStatus.COMPLETED);
		p.addExtension("http://fkcfhir.org/fhir/cs/CS1MachNumber", new Reference("BodyStructure/B51936689"));
		myProcedureDao.update(p, mySrd);

		logAllResources();
		logAllResourceLinks();

		// Non synchronous
		SearchParameterMap map = new SearchParameterMap();
		map.add("_id", new TokenParam("PRA8780542726"));
		map.addRevInclude(new Include("Procedure:part-of"));
		map.addInclude(new Include("Procedure:focalAccess").asRecursive());
		IBundleProvider outcome = myProcedureDao.search(map, mySrd);
		assertEquals(PersistedJpaSearchFirstPageBundleProvider.class, outcome.getClass());
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(ids.toString(), ids, Matchers.containsInAnyOrder("Procedure/PRA8780542726", "Procedure/PRA8780542785", "BodyStructure/B51936689"));

		// Synchronous
		map = new SearchParameterMap().setLoadSynchronous(true);
		map.add("_id", new TokenParam("PRA8780542726"));
		map.addRevInclude(new Include("Procedure:part-of"));
		map.addInclude(new Include("Procedure:focalAccess").asRecursive());
		outcome = myProcedureDao.search(map, mySrd);
		assertEquals(SimpleBundleProvider.class, outcome.getClass());
		ids = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(ids.toString(), ids, Matchers.containsInAnyOrder("Procedure/PRA8780542726", "Procedure/PRA8780542785", "BodyStructure/B51936689"));
	}


	@Test
	public void testRevIncludesPaged_AsyncSearch() {
		int eocCount = 10;
		myDaoConfig.setMaximumIncludesToLoadPerPage(5);

		createOrganizationWithReferencingEpisodesOfCare(eocCount);

		SearchParameterMap map = new SearchParameterMap()
			.setCount(10)
			.addInclude(new Include("*"))
			.addRevInclude(new Include("*").setRecurse(true));
		IBundleProvider results = myOrganizationDao.search(map);
		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids.toString(), ids, containsInAnyOrder(
			"EpisodeOfCare/EOC-0",
			"EpisodeOfCare/EOC-1",
			"EpisodeOfCare/EOC-2",
			"EpisodeOfCare/EOC-3",
			"Organization/ORG-0",
			"Organization/ORG-P"
		));

	}

	@Test
	public void testRevIncludesPagedSyncSearch() {
		int eocCount = 10;
//		myDaoConfig.setMaximumIncludesToLoadPerPage(5);

		createOrganizationWithReferencingEpisodesOfCare(eocCount);

		SearchParameterMap map = new SearchParameterMap()
			.add("_id", new TokenParam("ORG-0"))
			.addRevInclude(EpisodeOfCare.INCLUDE_ORGANIZATION);
		myCaptureQueriesListener.clear();
		IBundleProvider results = myOrganizationDao.search(map);
		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(ids.toString(), ids, containsInAnyOrder(
			"EpisodeOfCare/EOC-0",
			"EpisodeOfCare/EOC-1",
			"EpisodeOfCare/EOC-2",
			"EpisodeOfCare/EOC-3",
			"EpisodeOfCare/EOC-4",
			"EpisodeOfCare/EOC-5",
			"EpisodeOfCare/EOC-6",
			"EpisodeOfCare/EOC-7",
			"EpisodeOfCare/EOC-8",
			"EpisodeOfCare/EOC-9",
			"Organization/ORG-0"
		));



	}

	private void createOrganizationWithReferencingEpisodesOfCare(int theEocCount) {
		Organization org = new Organization();
		org.setId("Organization/ORG-P");
		org.setName("ORG-P");
		myOrganizationDao.update(org);

		org = new Organization();
		org.setId("Organization/ORG-0");
		org.setName("ORG-0");
		org.setPartOf(new Reference("Organization/ORG-P"));
		myOrganizationDao.update(org);

		for (int i = 0; i < theEocCount; i++) {
			EpisodeOfCare eoc = new EpisodeOfCare();
			eoc.setId("EpisodeOfCare/EOC-" + i);
			eoc.getManagingOrganization().setReference("Organization/ORG-0");
			myEpisodeOfCareDao.update(eoc);
		}
	}

	private void createPatientWithReferencingCarePlan(int theCount) {
		org.hl7.fhir.r4.model.Patient patient = new Patient();
		patient.setId("Patient/PAT-1");
		myPatientDao.update(patient);

		for (int i = 1; i <= theCount; i++) {
			CarePlan carePlan = new CarePlan();
			carePlan.setId("CarePlan/CP-" + i);
			carePlan.getSubject().setReference("Patient/PAT-1");
			myCarePlanDao.update(carePlan);
		}
	}
}
