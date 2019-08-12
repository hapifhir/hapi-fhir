package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.config.TestR4Config;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.ListUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestR4Config.class})
public class ConsentEventsDaoR4Test extends BaseJpaR4SystemTest {

	private static final Logger ourLog = LoggerFactory.getLogger(ConsentEventsDaoR4Test.class);
	private List<String> myObservationIds;
	private List<String> myPatientIds;
	private InterceptorService myInterceptorService;
	private List<String> myObservationIdsOddOnly;
	private List<String> myObservationIdsEvenOnly;
	private List<String> myObservationIdsEvenOnlyBackwards;
	private List<String> myObservationIdsBackwards;
	private List<String> myPatientIdsEvenOnly;

	@After
	public void after() {
		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
	}

	@Override
	@Before
	public void before() throws ServletException {
		super.before();

		RestfulServer restfulServer = new RestfulServer();
		restfulServer.setPagingProvider(myPagingProvider);

		myInterceptorService = new InterceptorService();
		when(mySrd.getInterceptorBroadcaster()).thenReturn(myInterceptorService);
		when(mySrd.getServer()).thenReturn(restfulServer);

		myDaoConfig.setSearchPreFetchThresholds(Arrays.asList(20, 50, 190));
	}

	@Test
	public void testSearchCountOnly() {
		create50Observations();

		AtomicInteger hitCount = new AtomicInteger(0);
		List<String> interceptedResourceIds = new ArrayList<>();
		IAnonymousInterceptor interceptor = new PreAccessInterceptorCounting(hitCount, interceptedResourceIds);
		myInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

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
		myInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

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
		assertEquals(myObservationIds.subList(0, 20), interceptedResourceIds);

		// Fetch the next 30 (do cross a fetch boundary)
		resources = outcome.getResources(10, 40);
		returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		assertEquals(myObservationIdsEvenOnly.subList(10, 25), returnedIdValues);
		assertEquals(2, hitCount.get());
	}


	@Test
	public void testSearchAndBlockSome_LoadSynchronous() {
		create50Observations();

		AtomicInteger hitCount = new AtomicInteger(0);
		List<String> interceptedResourceIds = new ArrayList<>();
		IAnonymousInterceptor interceptor = new PreAccessInterceptorCountingAndBlockOdd(hitCount, interceptedResourceIds);
		myInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

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
		create50Observations();

		AtomicInteger hitCount = new AtomicInteger(0);
		List<String> interceptedResourceIds = new ArrayList<>();
		IAnonymousInterceptor interceptor = new PreAccessInterceptorCountingAndBlockOdd(hitCount, interceptedResourceIds);
		myInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

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
		create50Observations();

		AtomicInteger hitCount = new AtomicInteger(0);
		List<String> interceptedResourceIds = new ArrayList<>();
		IAnonymousInterceptor interceptor = new PreAccessInterceptorCountingAndBlockOdd(hitCount, interceptedResourceIds);
		myInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

		// Perform a search
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
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
	public void testSearchAndBlockSomeOnIncludes() {
		create50Observations();

		AtomicInteger hitCount = new AtomicInteger(0);
		List<String> interceptedResourceIds = new ArrayList<>();
		IAnonymousInterceptor interceptor = new PreAccessInterceptorCountingAndBlockOdd(hitCount, interceptedResourceIds);
		myInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

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
		myInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

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
		myInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

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
		myInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

		// Perform a history
		SearchParameterMap map = new SearchParameterMap();
		map.setSort(new SortSpec(Observation.SP_IDENTIFIER, SortOrderEnum.ASC));
		IBundleProvider outcome = myObservationDao.history(null, null, mySrd);
		ourLog.info("Search UUID: {}", outcome.getUuid());

		// Fetch the first 10 (don't cross a fetch boundary)
		List<IBaseResource> resources = outcome.getResources(0, 10);
		List<String> returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		/*
		 * Note: Each observation in the observation list will appear twice in the actual
		 * returned results because we create it then update it in create50Observations()
		 */
		assertEquals(sort(myObservationIdsEvenOnlyBackwards.subList(0, 3), myObservationIdsEvenOnlyBackwards.subList(0, 3)), sort(returnedIdValues));
		assertEquals(1, hitCount.get());
		assertEquals(sort(myObservationIdsBackwards.subList(0, 5), myObservationIdsBackwards.subList(0, 5)), sort(interceptedResourceIds));

	}

	@Test
	public void testReadAndBlockSome() {
		create50Observations();

		AtomicInteger hitCount = new AtomicInteger(0);
		List<String> interceptedResourceIds = new ArrayList<>();
		IAnonymousInterceptor interceptor = new PreAccessInterceptorCountingAndBlockOdd(hitCount, interceptedResourceIds);
		myInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);

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

			obs1.setId(obs1id);
			if (obs1id.getIdPartAsLong() % 2 == 0) {
				obs1.getSubject().setReference(evenPid);
			} else {
				obs1.getSubject().setReference(oddPid);
			}
			myObservationDao.update(obs1);
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
		myObservationIdsBackwards = Lists.reverse(myObservationIds);
		myObservationIdsEvenOnlyBackwards = Lists.reverse(myObservationIdsEvenOnly);
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
		public void invoke(Pointcut thePointcut, HookParams theArgs) {
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
		public void invoke(Pointcut thePointcut, HookParams theArgs) {
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
