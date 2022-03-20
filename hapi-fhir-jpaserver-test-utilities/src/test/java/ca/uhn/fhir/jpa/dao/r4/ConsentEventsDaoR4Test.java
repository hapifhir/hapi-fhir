package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.IPointcut;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.apache.commons.collections4.ListUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestR4Config.class})
public class ConsentEventsDaoR4Test extends BaseJpaR4SystemTest {

	private static final Logger ourLog = LoggerFactory.getLogger(ConsentEventsDaoR4Test.class);
	private List<String> myObservationIds;
	private List<String> myPatientIds;
	private List<String> myObservationIdsOddOnly;
	private List<String> myObservationIdsEvenOnly;
	private List<String> myObservationIdsWithVersions;
	private List<String> myPatientIdsEvenOnly;

	@AfterEach
	public void after() {
		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
		myDaoConfig.setIndexMissingFields(new DaoConfig().getIndexMissingFields());
	}

	@BeforeEach
	public void before() throws ServletException {
		RestfulServer restfulServer = new RestfulServer();
		restfulServer.setPagingProvider(myPagingProvider);

		when(mySrd.getServer()).thenReturn(restfulServer);

		myDaoConfig.setSearchPreFetchThresholds(Arrays.asList(20, 50, 190));
	}

	@Test
	public void testSearchCountOnly() {
		create50Observations();

		AtomicInteger hitCount = new AtomicInteger(0);
		List<String> interceptedResourceIds = new ArrayList<>();
		IAnonymousInterceptor interceptor = new PreAccessInterceptorCounting(hitCount, interceptedResourceIds);
		mySrdInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

		// Perform a search
		SearchParameterMap map = new SearchParameterMap();
		map.setSort(new SortSpec(Observation.SP_IDENTIFIER, SortOrderEnum.ASC));
		IBundleProvider outcome = myObservationDao.search(map, mySrd);
		ourLog.info("Search UUID: {}", outcome.getUuid());

		// Fetch the first 10 (don't cross a fetch boundary)
		List<IBaseResource> resources = outcome.getResources(0, 10);
		List<String> returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		assertEquals(myObservationIds.subList(0, 10), returnedIdValues);
		assertEquals(1, hitCount.get());
		assertEquals(myObservationIds.subList(0, 20), interceptedResourceIds);

		// Fetch the next 30 (do cross a fetch boundary)
		outcome = myPagingProvider.retrieveResultList(mySrd, outcome.getUuid());
		resources = outcome.getResources(10, 40);
		returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		assertEquals(myObservationIds.subList(10, 40), returnedIdValues);
		assertEquals(2, hitCount.get());
		assertEquals(myObservationIds.subList(0, 50), interceptedResourceIds);
	}


	@Test
	public void testSearchAndBlockSome() {
		create50Observations();

		AtomicInteger hitCount = new AtomicInteger(0);
		List<String> interceptedResourceIds = new ArrayList<>();
		IAnonymousInterceptor interceptor = new PreAccessInterceptorCountingAndBlockOdd(hitCount, interceptedResourceIds);
		mySrdInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

		// Perform a search
		SearchParameterMap map = new SearchParameterMap();
		map.setSort(new SortSpec(Observation.SP_IDENTIFIER, SortOrderEnum.ASC));
		IBundleProvider outcome = myObservationDao.search(map, mySrd);
		ourLog.info("Search UUID: {}", outcome.getUuid());

		// Fetch the first 10 (don't cross a fetch boundary)
		List<IBaseResource> resources = outcome.getResources(0, 10);
		List<String> returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		assertEquals(myObservationIdsEvenOnly.subList(0, 10), returnedIdValues);
		assertEquals(1, hitCount.get());
		assertEquals(myObservationIds.subList(0, 20), interceptedResourceIds, "Wrong response from " + outcome.getClass());

		// Fetch the next 30 (do cross a fetch boundary)
		String searchId = outcome.getUuid();
		outcome = myPagingProvider.retrieveResultList(mySrd, searchId);
		resources = outcome.getResources(10, 40);
		returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		if (!myObservationIdsEvenOnly.subList(10, 25).equals(returnedIdValues)) {
			if (resources.size() != 1) {
				runInTransaction(() -> {
					Search search = mySearchEntityDao.findByUuidAndFetchIncludes(searchId).get();
					fail("Failed to load - " + mySearchResultDao.countForSearch(search.getId()) + " results in " + search);
				});
			}
		}
		assertEquals(myObservationIdsEvenOnly.subList(10, 25), returnedIdValues, "Wrong response from " + outcome.getClass());
		assertEquals(3, hitCount.get());
	}


	@Test
	public void testSearchAndBlockSome_LoadSynchronous() {
		create50Observations();

		AtomicInteger hitCount = new AtomicInteger(0);
		List<String> interceptedResourceIds = new ArrayList<>();
		IAnonymousInterceptor interceptor = new PreAccessInterceptorCountingAndBlockOdd(hitCount, interceptedResourceIds);
		mySrdInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

		// Perform a search
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.setSort(new SortSpec(Observation.SP_IDENTIFIER, SortOrderEnum.ASC));
		IBundleProvider outcome = myObservationDao.search(map, mySrd);
		ourLog.info("Search UUID: {}", outcome.getUuid());

		// Fetch the first 10 (don't cross a fetch boundary)
		List<IBaseResource> resources = outcome.getResources(0, 10);
		List<String> returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		assertEquals(myObservationIdsEvenOnly.subList(0, 10), returnedIdValues);
		assertEquals(1, hitCount.get());
		assertEquals(myObservationIds, interceptedResourceIds);

		// Fetch the next 30 (do cross a fetch boundary)
		resources = outcome.getResources(10, 40);
		returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		assertEquals(myObservationIdsEvenOnly.subList(10, 25), returnedIdValues);
		assertEquals(1, hitCount.get());
	}


	@Test
	public void testSearchAndBlockSomeOnRevIncludes() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);

		create50Observations();

		AtomicInteger hitCount = new AtomicInteger(0);
		List<String> interceptedResourceIds = new ArrayList<>();
		IAnonymousInterceptor interceptor = new PreAccessInterceptorCountingAndBlockOdd(hitCount, interceptedResourceIds);
		mySrdInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

		// Perform a search
		SearchParameterMap map = new SearchParameterMap();
		map.setSort(new SortSpec(Observation.SP_IDENTIFIER, SortOrderEnum.ASC));
		map.addRevInclude(IBaseResource.INCLUDE_ALL);
		IBundleProvider outcome = myPatientDao.search(map, mySrd);
		ourLog.info("Search UUID: {}", outcome.getUuid());

		// Fetch the first 10 (don't cross a fetch boundary)
		List<IBaseResource> resources = outcome.getResources(0, 100);
		List<String> returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		assertEquals(sort(myPatientIdsEvenOnly, myObservationIdsEvenOnly), sort(returnedIdValues));
		assertEquals(2, hitCount.get());

	}

	@Test
	public void testSearchAndBlockSomeOnRevIncludes_LoadSynchronous() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);
		create50Observations();

		AtomicInteger hitCount = new AtomicInteger(0);
		List<String> interceptedResourceIds = new ArrayList<>();
		IAnonymousInterceptor interceptor = new PreAccessInterceptorCountingAndBlockOdd(hitCount, interceptedResourceIds);
		mySrdInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

		// Perform a search
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.setSort(new SortSpec(Observation.SP_IDENTIFIER, SortOrderEnum.ASC));
		map.addRevInclude(IBaseResource.INCLUDE_ALL);

		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myPatientDao.search(map, mySrd);
		ourLog.info("Search UUID: {}", outcome.getUuid());

		// Fetch the first 10 (don't cross a fetch boundary)
		List<IBaseResource> resources = outcome.getResources(0, 100);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		List<String> returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		assertEquals(sort(myPatientIdsEvenOnly, myObservationIdsEvenOnly), sort(returnedIdValues));
		assertEquals(2, hitCount.get());

	}

	@Test
	public void testSearchAndBlockSomeOnIncludes() {
		create50Observations();

		AtomicInteger hitCount = new AtomicInteger(0);
		List<String> interceptedResourceIds = new ArrayList<>();
		IAnonymousInterceptor interceptor = new PreAccessInterceptorCountingAndBlockOdd(hitCount, interceptedResourceIds);
		mySrdInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

		// Perform a search
		SearchParameterMap map = new SearchParameterMap();
		map.addInclude(IBaseResource.INCLUDE_ALL);
		IBundleProvider outcome = myObservationDao.search(map, mySrd);
		ourLog.info("Search UUID: {}", outcome.getUuid());

		// Fetch the first 10 (don't cross a fetch boundary)
		List<IBaseResource> resources = outcome.getResources(0, 100);
		List<String> returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		assertEquals(sort(myPatientIdsEvenOnly, myObservationIdsEvenOnly), sort(returnedIdValues));

		// This should probably be 4
		assertEquals(5, hitCount.get());

	}

	@Test
	public void testSearchAndBlockNoneOnIncludes() {
		create50Observations();

		AtomicInteger hitCount = new AtomicInteger(0);
		List<String> interceptedResourceIds = new ArrayList<>();
		IAnonymousInterceptor interceptor = new PreAccessInterceptorCounting(hitCount, interceptedResourceIds);
		mySrdInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

		// Perform a search
		SearchParameterMap map = new SearchParameterMap();
		map.addInclude(IBaseResource.INCLUDE_ALL);
		IBundleProvider outcome = myObservationDao.search(map, mySrd);
		ourLog.info("Search UUID: {}", outcome.getUuid());

		// Fetch the first 10 (don't cross a fetch boundary)
		List<IBaseResource> resources = outcome.getResources(0, 100);
		List<String> returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		assertEquals(sort(myPatientIds, myObservationIds), sort(returnedIdValues));
		assertEquals(4, hitCount.get());

	}

	@Test
	public void testSearchAndBlockSomeOnIncludes_LoadSynchronous() {
		create50Observations();

		AtomicInteger hitCount = new AtomicInteger(0);
		List<String> interceptedResourceIds = new ArrayList<>();
		IAnonymousInterceptor interceptor = new PreAccessInterceptorCountingAndBlockOdd(hitCount, interceptedResourceIds);
		mySrdInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

		// Perform a search
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.addInclude(IBaseResource.INCLUDE_ALL);
		IBundleProvider outcome = myObservationDao.search(map, mySrd);
		ourLog.info("Search UUID: {}", outcome.getUuid());

		// Fetch the first 10 (don't cross a fetch boundary)
		List<IBaseResource> resources = outcome.getResources(0, 100);
		List<String> returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		assertEquals(sort(myPatientIdsEvenOnly, myObservationIdsEvenOnly), sort(returnedIdValues));
		assertEquals(2, hitCount.get());

	}

	@Test
	public void testHistoryAndBlockSome() {
		create50Observations();

		AtomicInteger hitCount = new AtomicInteger(0);
		List<String> interceptedResourceIds = new ArrayList<>();
		IAnonymousInterceptor interceptor = new PreAccessInterceptorCountingAndBlockOdd(hitCount, interceptedResourceIds);
		mySrdInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

		// Perform a history
		SearchParameterMap map = new SearchParameterMap();
		map.setSort(new SortSpec(Observation.SP_IDENTIFIER, SortOrderEnum.ASC));
		IBundleProvider outcome = myObservationDao.history(null, null, null, mySrd);
		ourLog.info("Search UUID: {}", outcome.getUuid());

		// Fetch the first 10 (don't cross a fetch boundary)
		List<IBaseResource> resources = outcome.getResources(0, 10);
		List<String> returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		ourLog.info("Returned values: {}", returnedIdValues);

		/*
		 * Note: Each observation in the observation list will appear twice in the actual
		 * returned results because we create it then update it in create50Observations()
		 */
		assertEquals(1, hitCount.get());
		assertEquals(myObservationIdsWithVersions.subList(90, myObservationIdsWithVersions.size()), sort(interceptedResourceIds));
		returnedIdValues.forEach(t -> assertTrue(new IdType(t).getIdPartAsLong() % 2 == 0));

	}

	@Test
	public void testReadAndBlockSome() {
		create50Observations();

		AtomicInteger hitCount = new AtomicInteger(0);
		List<String> interceptedResourceIds = new ArrayList<>();
		IAnonymousInterceptor interceptor = new PreAccessInterceptorCountingAndBlockOdd(hitCount, interceptedResourceIds);
		mySrdInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

		myObservationDao.read(new IdType(myObservationIdsEvenOnly.get(0)), mySrd);
		myObservationDao.read(new IdType(myObservationIdsEvenOnly.get(1)), mySrd);

		try {
			myObservationDao.read(new IdType(myObservationIdsOddOnly.get(0)), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}
		try {
			myObservationDao.read(new IdType(myObservationIdsOddOnly.get(1)), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

	}

	private void create50Observations() {
		myPatientIds = new ArrayList<>();
		myObservationIds = new ArrayList<>();
		myObservationIdsWithVersions = new ArrayList<>();

		Patient p = new Patient();
		p.setActive(true);
		IIdType pid0 = myPatientDao.create(p).getId().toUnqualifiedVersionless();
		myPatientIds.add(pid0.getValue());

		p = new Patient();
		p.setActive(true);
		IIdType pid1 = myPatientDao.create(p).getId().toUnqualifiedVersionless();
		myPatientIds.add(pid1.getValue());

		assertTrue((pid0.getIdPartAsLong() % 2) != (pid1.getIdPartAsLong() % 2));
		String evenPid = pid0.getIdPartAsLong() % 2 == 0 ? pid0.getValue() : pid1.getValue();
		String oddPid = pid0.getIdPartAsLong() % 2 == 0 ? pid1.getValue() : pid0.getValue();

		for (int i = 0; i < 50; i++) {
			final Observation obs1 = new Observation();
			obs1.setStatus(Observation.ObservationStatus.FINAL);
			obs1.addIdentifier().setSystem("urn:system").setValue("I" + leftPad("" + i, 5, '0'));
			IIdType obs1id = myObservationDao.create(obs1).getId().toUnqualifiedVersionless();
			myObservationIds.add(obs1id.toUnqualifiedVersionless().getValue());
			myObservationIdsWithVersions.add(obs1id.toUnqualifiedVersionless().getValue());

			obs1.setId(obs1id);
			if (obs1id.getIdPartAsLong() % 2 == 0) {
				obs1.getSubject().setReference(evenPid);
			} else {
				obs1.getSubject().setReference(oddPid);
			}
			myObservationDao.update(obs1);
			myObservationIdsWithVersions.add(obs1id.toUnqualifiedVersionless().getValue());

		}

		myPatientIdsEvenOnly =
			myPatientIds
				.stream()
				.filter(t -> Long.parseLong(t.substring(t.indexOf('/') + 1)) % 2 == 0)
				.collect(Collectors.toList());

		myObservationIdsEvenOnly =
			myObservationIds
				.stream()
				.filter(t -> Long.parseLong(t.substring(t.indexOf('/') + 1)) % 2 == 0)
				.collect(Collectors.toList());

		myObservationIdsOddOnly = ListUtils.removeAll(myObservationIds, myObservationIdsEvenOnly);
	}

	static class PreAccessInterceptorCounting implements IAnonymousInterceptor {
		private static final Logger ourLog = LoggerFactory.getLogger(PreAccessInterceptorCounting.class);

		private final AtomicInteger myHitCount;
		private final List<String> myInterceptedResourceIds;

		PreAccessInterceptorCounting(AtomicInteger theHitCount, List<String> theInterceptedResourceIds) {
			myHitCount = theHitCount;
			myInterceptedResourceIds = theInterceptedResourceIds;
		}

		@Override
		public void invoke(IPointcut thePointcut, HookParams theArgs) {
			myHitCount.incrementAndGet();

			IPreResourceAccessDetails accessDetails = theArgs.get(IPreResourceAccessDetails.class);

			assertThat(accessDetails.size(), greaterThan(0));

			List<String> currentPassIds = new ArrayList<>();
			for (int i = 0; i < accessDetails.size(); i++) {
				IBaseResource nextResource = accessDetails.getResource(i);
				if (nextResource != null) {
					currentPassIds.add(nextResource.getIdElement().toUnqualifiedVersionless().getValue());
				}
			}

			ourLog.info("Call to STORAGE_PREACCESS_RESOURCES with {} IDs: {}", currentPassIds.size(), currentPassIds);
			myInterceptedResourceIds.addAll(currentPassIds);
		}
	}

	static class PreAccessInterceptorCountingAndBlockOdd extends PreAccessInterceptorCounting {

		PreAccessInterceptorCountingAndBlockOdd(AtomicInteger theHitCount, List<String> theInterceptedResourceIds) {
			super(theHitCount, theInterceptedResourceIds);
		}


		@Override
		public void invoke(IPointcut thePointcut, HookParams theArgs) {
			super.invoke(thePointcut, theArgs);

			IPreResourceAccessDetails accessDetails = theArgs.get(IPreResourceAccessDetails.class);
			List<String> nonBlocked = new ArrayList<>();
			int count = accessDetails.size();

			List<String> ids = new ArrayList<>();
			for (int i = 0; i < accessDetails.size(); i++) {
				ids.add(accessDetails.getResource(i).getIdElement().toUnqualifiedVersionless().getValue());
			}
			ourLog.info("Invoking {} for {} results: {}", thePointcut, count, ids);

			for (int i = 0; i < count; i++) {
				IBaseResource resource = accessDetails.getResource(i);
				if (resource != null) {
					long idPart = resource.getIdElement().getIdPartAsLong();
					if (idPart % 2 == 1) {
						accessDetails.setDontReturnResourceAtIndex(i);
					} else {
						nonBlocked.add(resource.getIdElement().toUnqualifiedVersionless().getValue());
					}
				}
			}

			ourLog.info("Allowing IDs: {}", nonBlocked);

		}
	}

	private static List<String> sort(List<String>... theLists) {
		ArrayList<String> retVal = new ArrayList<>();
		for (List<String> next : theLists) {
			retVal.addAll(next);
		}
		retVal.sort((o0, o1) -> {
			long i0 = Long.parseLong(o0.substring(o0.indexOf('/') + 1));
			long i1 = Long.parseLong(o1.substring(o1.indexOf('/') + 1));
			return (int) (i0 - i1);
		});
		return retVal;
	}


}
