package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.EpisodeOfCare;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hl7.fhir.r4.model.ResourceType.Patient;
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
			IBundleProvider results = myCarePlanDao.search(map);
			List<String> ids = toUnqualifiedVersionlessIdValues(results);
			assertThat(ids.toString(), ids, containsInAnyOrder("CarePlan/CP-1"));
		} catch (Exception e) {
			fail();
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
