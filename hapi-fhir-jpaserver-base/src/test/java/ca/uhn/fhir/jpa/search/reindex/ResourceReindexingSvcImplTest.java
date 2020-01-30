package ca.uhn.fhir.jpa.search.reindex;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.*;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.data.IResourceReindexJobDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.entity.ResourceReindexJobEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


public class ResourceReindexingSvcImplTest extends BaseJpaTest {

	private static FhirContext ourCtx = FhirContext.forR4();

	@Mock
	private PlatformTransactionManager myTxManager;

	private ResourceReindexingSvcImpl mySvc;
	private DaoConfig myDaoConfig;

	@Mock
	private DaoRegistry myDaoRegistry;
	@Mock
	private IForcedIdDao myForcedIdDao;
	@Mock
	private IResourceReindexJobDao myReindexJobDao;
	@Mock
	private IResourceTableDao myResourceTableDao;
	@Mock
	private IFhirResourceDao myResourceDao;
	@Captor
	private ArgumentCaptor<Long> myIdCaptor;
	@Captor
	private ArgumentCaptor<PageRequest> myPageRequestCaptor;
	@Captor
	private ArgumentCaptor<String> myTypeCaptor;
	@Captor
	private ArgumentCaptor<Date> myLowCaptor;
	@Captor
	private ArgumentCaptor<Date> myHighCaptor;
	private ResourceReindexJobEntity mySingleJob;
	@Mock
	private ISearchParamRegistry mySearchParamRegistry;
	@Mock
	private TransactionStatus myTxStatus;
	@Mock
	private ISchedulerService mySchedulerService;

	@Override
	protected FhirContext getContext() {
		return ourCtx;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@Before
	public void before() {
		myDaoConfig = new DaoConfig();
		myDaoConfig.setReindexThreadCount(2);

		mySvc = new ResourceReindexingSvcImpl();
		mySvc.setContextForUnitTest(ourCtx);
		mySvc.setDaoConfigForUnitTest(myDaoConfig);
		mySvc.setDaoRegistryForUnitTest(myDaoRegistry);
		mySvc.setForcedIdDaoForUnitTest(myForcedIdDao);
		mySvc.setReindexJobDaoForUnitTest(myReindexJobDao);
		mySvc.setResourceTableDaoForUnitTest(myResourceTableDao);
		mySvc.setTxManagerForUnitTest(myTxManager);
		mySvc.setSearchParamRegistryForUnitTest(mySearchParamRegistry);
		mySvc.setSchedulerServiceForUnitTest(mySchedulerService);
		mySvc.start();

		when(myTxManager.getTransaction(any())).thenReturn(myTxStatus);
	}

	@Test
	public void testNoParallelReindexing() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		new Thread(()->{
			mySvc.getIndexingLockForUnitTest().lock();
			latch.countDown();
		}).start();
		latch.await(10, TimeUnit.SECONDS);
		mySvc.runReindexingPass();
	}

	@Test
	public void testReindexPassOnlyReturnsValuesAtLowThreshold() {
		mockNothingToExpunge();
		mockSingleReindexingJob(null);
		mockFetchFourResources();
		mockFinalResourceNeedsReindexing();

		mySingleJob.setThresholdLow(new Date(40 * DateUtils.MILLIS_PER_DAY));
		Date highThreshold = DateUtils.addMinutes(new Date(), -1);
		mySingleJob.setThresholdHigh(highThreshold);

		// Run the second pass, which should index no resources (meaning it's time to mark as deleted)
		mySvc.forceReindexingPass();
		verify(myResourceTableDao, never()).findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(any(), any(), any(), any());
		verify(myReindexJobDao, never()).markAsDeletedById(any());
		verify(myResourceTableDao, times(1)).findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(myPageRequestCaptor.capture(), myLowCaptor.capture(), myHighCaptor.capture());
		assertEquals(new Date(40 * DateUtils.MILLIS_PER_DAY), myLowCaptor.getAllValues().get(0));
		assertEquals(highThreshold, myHighCaptor.getAllValues().get(0));

		// Should mark the low threshold as 1 milli higher than the ne returned item
		verify(myReindexJobDao, times(1)).setThresholdLow(eq(123L), eq(new Date((40 * DateUtils.MILLIS_PER_DAY) + 1L)));
	}

	@Test
	public void testMarkAsDeletedIfNothingIndexed() {
		mockNothingToExpunge();
		mockSingleReindexingJob(null);
		mockFetchFourResources();
		// Mock resource fetch
		List<Long> values = Collections.emptyList();
		when(myResourceTableDao.findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(any(), any(), any())).thenReturn(new SliceImpl<>(values));

		mySingleJob.setThresholdLow(new Date(40 * DateUtils.MILLIS_PER_DAY));
		Date highThreshold = DateUtils.addMinutes(new Date(), -1);
		mySingleJob.setThresholdHigh(highThreshold);

		// Run the second pass, which should index no resources (meaning it's time to mark as deleted)
		mySvc.forceReindexingPass();
		verify(myResourceTableDao, never()).findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(any(), any(), any(), any());
		verify(myResourceTableDao, times(1)).findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(myPageRequestCaptor.capture(), myLowCaptor.capture(), myHighCaptor.capture());
		assertEquals(new Date(40 * DateUtils.MILLIS_PER_DAY), myLowCaptor.getAllValues().get(0));
		assertEquals(highThreshold, myHighCaptor.getAllValues().get(0));

		// This time we shouldn't update the threshold
		verify(myReindexJobDao, never()).setThresholdLow(any(), any());

		verify(myReindexJobDao, times(1)).markAsDeletedById(eq(123L));
	}

	@Test
	public void testExpungeDeletedJobs() {
		ResourceReindexJobEntity job = new ResourceReindexJobEntity();
		job.setIdForUnitTest(123L);
		job.setDeleted(true);
		when(myReindexJobDao.findAll(any(), eq(true))).thenReturn(Arrays.asList(job));

		mySvc.forceReindexingPass();

		verify(myReindexJobDao, times(1)).deleteById(eq(123L));
	}

	@Test
	public void testReindexPassAllResources() {
		mockNothingToExpunge();
		mockSingleReindexingJob(null);
		mockFourResourcesNeedReindexing();
		mockFetchFourResources();

		int count = mySvc.forceReindexingPass();
		assertEquals(4, count);

		// Make sure we reindexed all 4 resources
		verify(myResourceDao, times(4)).reindex(any(), any());

		// Make sure we updated the low threshold
		verify(myReindexJobDao, times(1)).setThresholdLow(myIdCaptor.capture(), myLowCaptor.capture());
		assertEquals(123L, myIdCaptor.getValue().longValue());
		assertEquals(40 * DateUtils.MILLIS_PER_DAY, myLowCaptor.getValue().getTime());

		// Make sure we didn't do anything unexpected
		verify(myReindexJobDao, times(1)).findAll(any(), eq(false));
		verify(myReindexJobDao, times(1)).findAll(any(), eq(true));
		verify(myReindexJobDao, times(1)).getReindexCount(any());
		verify(myReindexJobDao, times(1)).setReindexCount(any(), anyInt());
		verifyNoMoreInteractions(myReindexJobDao);

		verify(mySearchParamRegistry, times(1)).forceRefresh();
	}

	@Test
	public void testReindexPassPatients() {
		mockNothingToExpunge();
		mockSingleReindexingJob("Patient");
		// Mock resource fetch
		List<Long> values = Arrays.asList(0L, 1L, 2L, 3L);
		when(myResourceTableDao.findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(myPageRequestCaptor.capture(), myTypeCaptor.capture(), myLowCaptor.capture(), myHighCaptor.capture())).thenReturn(new SliceImpl<>(values));
		// Mock fetching resources
		long[] updatedTimes = new long[]{
			10 * DateUtils.MILLIS_PER_DAY,
			20 * DateUtils.MILLIS_PER_DAY,
			40 * DateUtils.MILLIS_PER_DAY,
			30 * DateUtils.MILLIS_PER_DAY,
		};
		String[] resourceTypes = new String[]{
			"Patient",
			"Patient",
			"Patient",
			"Patient"
		};
		List<IBaseResource> resources = Arrays.asList(
			new Patient().setId("Patient/0/_history/1"),
			new Patient().setId("Patient/1/_history/1"),
			new Patient().setId("Patient/2/_history/1"),
			new Patient().setId("Patient/3/_history/1")
		);
		mockWhenResourceTableFindById(updatedTimes, resourceTypes);
		when(myDaoRegistry.getResourceDao(eq("Patient"))).thenReturn(myResourceDao);
		when(myDaoRegistry.getResourceDao(eq(Patient.class))).thenReturn(myResourceDao);
		when(myDaoRegistry.getResourceDao(eq("Observation"))).thenReturn(myResourceDao);
		when(myDaoRegistry.getResourceDao(eq(Observation.class))).thenReturn(myResourceDao);
		when(myResourceDao.read(any(), any(), anyBoolean())).thenAnswer(t->{
			IIdType id = (IIdType) t.getArguments()[0];
			return resources.get(id.getIdPartAsLong().intValue());
		});


		int count = mySvc.forceReindexingPass();
		assertEquals(4, count);

		// Make sure we reindexed all 4 resources
		verify(myResourceDao, times(4)).reindex(any(), any());

		// Make sure we updated the low threshold
		verify(myReindexJobDao, times(1)).setThresholdLow(myIdCaptor.capture(), myLowCaptor.capture());
		assertEquals(123L, myIdCaptor.getValue().longValue());
		assertEquals(40 * DateUtils.MILLIS_PER_DAY, myLowCaptor.getValue().getTime());

		// Make sure we didn't do anything unexpected
		verify(myReindexJobDao, times(1)).findAll(any(), eq(false));
		verify(myReindexJobDao, times(1)).findAll(any(), eq(true));
		verify(myReindexJobDao, times(1)).getReindexCount(any());
		verify(myReindexJobDao, times(1)).setReindexCount(any(), anyInt());
		verifyNoMoreInteractions(myReindexJobDao);
	}

	@Test
	public void testReindexDeletedResource() {
		mockNothingToExpunge();
		mockSingleReindexingJob("Patient");
		// Mock resource fetch
		List<Long> values = Arrays.asList(0L);
		when(myResourceTableDao.findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(myPageRequestCaptor.capture(), myTypeCaptor.capture(), myLowCaptor.capture(), myHighCaptor.capture())).thenReturn(new SliceImpl<>(values));
		// Mock fetching resources
		long[] updatedTimes = new long[]{
			10 * DateUtils.MILLIS_PER_DAY
		};
		String[] resourceTypes = new String[]{
			"Patient",
		};
		List<IBaseResource> resources = Arrays.asList(
			new Patient().setId("Patient/0/_history/1")
		);
		mockWhenResourceTableFindById(updatedTimes, resourceTypes);
		when(myDaoRegistry.getResourceDao(eq("Patient"))).thenReturn(myResourceDao);
		when(myDaoRegistry.getResourceDao(eq(Patient.class))).thenReturn(myResourceDao);
		when(myDaoRegistry.getResourceDao(eq("Observation"))).thenReturn(myResourceDao);
		when(myDaoRegistry.getResourceDao(eq(Observation.class))).thenReturn(myResourceDao);
		when(myResourceDao.read(any(), any(), anyBoolean())).thenReturn(null);


		int count = mySvc.forceReindexingPass();
		assertEquals(0, count);

		verify(myResourceTableDao, times(1)).updateIndexStatus(eq(0L), eq(BaseHapiFhirDao.INDEX_STATUS_INDEXING_FAILED));
	}

	@Test
	public void testReindexThrowsError() {
		mockNothingToExpunge();
		mockSingleReindexingJob("Patient");
		List<Long> values = Arrays.asList(0L, 1L, 2L, 3L);
		when(myResourceTableDao.findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(myPageRequestCaptor.capture(), myTypeCaptor.capture(), myLowCaptor.capture(), myHighCaptor.capture())).thenReturn(new SliceImpl<>(values));
		when(myResourceTableDao.findById(anyLong())).thenThrow(new NullPointerException("A MESSAGE"));

		int count = mySvc.forceReindexingPass();
		assertEquals(0, count);

		// Make sure we didn't do anything unexpected
		verify(myReindexJobDao, times(1)).findAll(any(), eq(false));
		verify(myReindexJobDao, times(1)).findAll(any(), eq(true));
		verify(myReindexJobDao, times(1)).setSuspendedUntil(any());
		verifyNoMoreInteractions(myReindexJobDao);
	}

	private void mockWhenResourceTableFindById(long[] theUpdatedTimes, String[] theResourceTypes) {
		when(myResourceTableDao.findById(any())).thenAnswer(t -> {
			ResourceTable retVal = new ResourceTable();
			Long id = (Long) t.getArguments()[0];
			retVal.setId(id);
			retVal.setResourceType(theResourceTypes[id.intValue()]);
			retVal.setUpdated(new Date(theUpdatedTimes[id.intValue()]));
			return Optional.of(retVal);
		});
	}

	private void mockFetchFourResources() {
		// Mock fetching resources
		long[] updatedTimes = new long[]{
			10 * DateUtils.MILLIS_PER_DAY,
			20 * DateUtils.MILLIS_PER_DAY,
			40 * DateUtils.MILLIS_PER_DAY,
			30 * DateUtils.MILLIS_PER_DAY,
		};
		String[] resourceTypes = new String[]{
			"Patient",
			"Patient",
			"Observation",
			"Observation"
		};
		List<IBaseResource> resources = Arrays.asList(
			new Patient().setId("Patient/0/_history/1"),
			new Patient().setId("Patient/1/_history/1"),
			new Observation().setId("Observation/2/_history/1"),
			new Observation().setId("Observation/3/_history/1")
		);
		mockWhenResourceTableFindById(updatedTimes, resourceTypes);
		when(myDaoRegistry.getResourceDao(eq("Patient"))).thenReturn(myResourceDao);
		when(myDaoRegistry.getResourceDao(eq(Patient.class))).thenReturn(myResourceDao);
		when(myDaoRegistry.getResourceDao(eq("Observation"))).thenReturn(myResourceDao);
		when(myDaoRegistry.getResourceDao(eq(Observation.class))).thenReturn(myResourceDao);
		when(myResourceDao.read(any(), any(), anyBoolean())).thenAnswer(t->{
			IIdType id = (IIdType) t.getArguments()[0];
			return resources.get(id.getIdPartAsLong().intValue());
		});
	}

	private void mockFourResourcesNeedReindexing() {
		// Mock resource fetch
		List<Long> values = Arrays.asList(0L, 1L, 2L, 3L);
		when(myResourceTableDao.findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(any(), any(), any())).thenReturn(new SliceImpl<>(values));
	}

	private void mockFinalResourceNeedsReindexing() {
		// Mock resource fetch
		List<Long> values = Arrays.asList(2L); // the second-last one has the highest time
		when(myResourceTableDao.findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(any(), any(), any())).thenReturn(new SliceImpl<>(values));
	}

	private void mockSingleReindexingJob(String theResourceType) {
		// Mock the reindexing job
		mySingleJob = new ResourceReindexJobEntity();
		mySingleJob.setIdForUnitTest(123L);
		mySingleJob.setThresholdHigh(DateUtils.addMinutes(new Date(), 1));
		mySingleJob.setResourceType(theResourceType);
		when(myReindexJobDao.findAll(any(), eq(false))).thenReturn(Arrays.asList(mySingleJob));
	}

	private void mockNothingToExpunge() {
		// Nothing to expunge
		when(myReindexJobDao.findAll(any(), eq(true))).thenReturn(new ArrayList<>());
	}
}
