package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.hl7.fhir.r4.model.EpisodeOfCare;
import org.hl7.fhir.r4.model.Organization;
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
}
