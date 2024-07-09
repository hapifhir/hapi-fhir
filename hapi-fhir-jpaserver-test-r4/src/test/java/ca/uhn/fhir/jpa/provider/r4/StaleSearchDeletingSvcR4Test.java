package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchResult;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.search.cache.DatabaseSearchCacheSvcImpl;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleLinkComponent;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.AopTestUtils;

import java.util.Date;
import java.util.UUID;

import static ca.uhn.fhir.util.TestUtil.sleepAtLeast;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class StaleSearchDeletingSvcR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(StaleSearchDeletingSvcR4Test.class);
	@Autowired
	private ISearchDao mySearchEntityDao;
	@Autowired
	private ISearchResultDao mySearchResultDao;

	@Override
	@AfterEach()
	public void after() throws Exception {
		super.after();
		DatabaseSearchCacheSvcImpl staleSearchDeletingSvc = AopTestUtils.getTargetObject(mySearchCacheSvc);
		staleSearchDeletingSvc.setCutoffSlackForUnitTest(DatabaseSearchCacheSvcImpl.SEARCH_CLEANUP_JOB_INTERVAL_MILLIS);
		DatabaseSearchCacheSvcImpl.setMaximumResultsToDeleteInOneStatement(DatabaseSearchCacheSvcImpl.DEFAULT_MAX_RESULTS_TO_DELETE_IN_ONE_STMT);
		DatabaseSearchCacheSvcImpl.setMaximumResultsToDeleteInOnePassForUnitTest(DatabaseSearchCacheSvcImpl.DEFAULT_MAX_RESULTS_TO_DELETE_IN_ONE_PAS);
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		DatabaseSearchCacheSvcImpl staleSearchDeletingSvc = AopTestUtils.getTargetObject(mySearchCacheSvc);
		staleSearchDeletingSvc.setCutoffSlackForUnitTest(0);
	}

	@Test
	public void testEverythingInstanceWithContentFilter() throws Exception {

		for (int i = 0; i < 20; i++) {
			Patient pt1 = new Patient();
			pt1.addName().setFamily("Everything").addGiven("Arthur");
			myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();
		}

		IClientExecutable<IQuery<Bundle>, Bundle> search = myClient
			.search()
			.forResource(Patient.class)
			.where(Patient.NAME.matches().value("Everything"))
			.returnBundle(Bundle.class);

		Bundle resp1 = search.execute();

		for (int i = 0; i < 20; i++) {
			search.execute();
		}

		BundleLinkComponent nextLink = resp1.getLink("next");
		assertNotNull(nextLink);
		String nextLinkUrl = nextLink.getUrl();
		assertThat(nextLinkUrl).isNotBlank();

		Bundle resp2 = myClient.search().byUrl(nextLinkUrl).returnBundle(Bundle.class).execute();
		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp2));

		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();

		myClient.search().byUrl(nextLinkUrl).returnBundle(Bundle.class).execute();

		Thread.sleep(20);
		myStorageSettings.setExpireSearchResultsAfterMillis(10);
		myStorageSettings.setReuseCachedSearchResultsForMillis(null);
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();

		try {
			myClient.search().byUrl(nextLinkUrl).returnBundle(Bundle.class).execute();
			fail();
		} catch (ResourceGoneException e) {
			assertThat(e.getMessage()).contains("does not exist and may have expired");
		}
	}

	@Test
	public void testDeleteVeryLargeSearch() {
		DatabaseSearchCacheSvcImpl.setMaximumResultsToDeleteInOneStatement(10);
		DatabaseSearchCacheSvcImpl.setMaximumResultsToDeleteInOnePassForUnitTest(10);

		runInTransaction(() -> {
			Search search = new Search();
			search.setStatus(SearchStatusEnum.FINISHED);
			search.setUuid(UUID.randomUUID().toString());
			search.setCreated(DateUtils.addDays(new Date(), -10000));
			search.setSearchType(SearchTypeEnum.SEARCH);
			search.setResourceType("Patient");
			search = mySearchEntityDao.save(search);

			ResourceTable resource = new ResourceTable();
			resource.setPublished(new Date());
			resource.setUpdated(new Date());
			resource.setResourceType("Patient");
			resource = myResourceTableDao.saveAndFlush(resource);

			for (int i = 0; i < 50; i++) {
				SearchResult sr = new SearchResult(search);
				sr.setOrder(i);
				sr.setResourcePid(resource.getId());
				mySearchResultDao.save(sr);
			}
		});

		// we are able to delete this in one pass.
		runInTransaction(() -> assertEquals(1, mySearchEntityDao.count()));
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		runInTransaction(() -> assertEquals(0, mySearchEntityDao.count()));

	}

	@Test
	public void testDeleteVerySmallSearch() {
		DatabaseSearchCacheSvcImpl.setMaximumResultsToDeleteInOneStatement(10);

        runInTransaction(() -> {
			Search search = new Search();
			search.setStatus(SearchStatusEnum.FINISHED);
			search.setUuid(UUID.randomUUID().toString());
			search.setCreated(DateUtils.addDays(new Date(), -10000));
			search.setSearchType(SearchTypeEnum.SEARCH);
			search.setResourceType("Patient");
			mySearchEntityDao.save(search);
		});

		// It should take one pass to delete the search fully
		runInTransaction(() -> {
			assertEquals(1, mySearchEntityDao.count());
		});

		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();

		runInTransaction(() -> {
			assertEquals(0, mySearchEntityDao.count());
		});
	}

	@Test
	public void testDontDeleteSearchBeforeExpiry() {
		DatabaseSearchCacheSvcImpl.setMaximumResultsToDeleteInOneStatement(10);

        runInTransaction(() -> {
			Search search = new Search();

			// Expires in one second, so it should not be deleted right away,
			// but it should be deleted if we try again after one second...
			search.setExpiryOrNull(DateUtils.addMilliseconds(new Date(), 1000));

			search.setStatus(SearchStatusEnum.FINISHED);
			search.setUuid(UUID.randomUUID().toString());
			search.setCreated(DateUtils.addDays(new Date(), -10000));
			search.setSearchType(SearchTypeEnum.SEARCH);
			search.setResourceType("Patient");
			mySearchEntityDao.save(search);

		});

		// Should not delete right now
		runInTransaction(() -> assertEquals(1, mySearchEntityDao.count()));
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		runInTransaction(() -> assertEquals(1, mySearchEntityDao.count()));

		sleepAtLeast(1100);

		// Now it's time to delete
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		runInTransaction(() -> assertEquals(0, mySearchEntityDao.count()));

	}


}
