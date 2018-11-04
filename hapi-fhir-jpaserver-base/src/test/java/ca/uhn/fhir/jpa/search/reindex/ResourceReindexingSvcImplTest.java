package ca.uhn.fhir.jpa.search.reindex;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.data.IResourceReindexJobDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.entity.ResourceReindexJobEntity;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
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

import java.util.*;

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
		mySvc.start();
	}

	@Test
	public void testMarkJobsPastThresholdAsDeleted() {
		mockNothingToExpunge();
		mockSingleReindexingJob(null);
		mockFourResourcesNeedReindexing();
		mockFetchFourResources();

		mySingleJob.setThresholdHigh(DateUtils.addMinutes(new Date(), -1));

		mySvc.forceReindexingPass();

		verify(myResourceTableDao, never()).findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(any(), any(), any());
		verify(myResourceTableDao, never()).findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(any(), any(), any(), any());
		verify(myReindexJobDao, times(1)).markAsDeletedById(myIdCaptor.capture());

		assertEquals(123L, myIdCaptor.getValue().longValue());
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
		verifyNoMoreInteractions(myReindexJobDao);
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
			new Patient().setId("Patient/0"),
			new Patient().setId("Patient/1"),
			new Patient().setId("Patient/2"),
			new Patient().setId("Patient/3")
		);
		mockWhenResourceTableFindById(updatedTimes, resourceTypes);
		when(myDaoRegistry.getResourceDao(eq("Patient"))).thenReturn(myResourceDao);
		when(myDaoRegistry.getResourceDao(eq(Patient.class))).thenReturn(myResourceDao);
		when(myDaoRegistry.getResourceDao(eq("Observation"))).thenReturn(myResourceDao);
		when(myDaoRegistry.getResourceDao(eq(Observation.class))).thenReturn(myResourceDao);
		when(myResourceDao.toResource(any(), anyBoolean())).thenAnswer(t -> {
			ResourceTable table = (ResourceTable) t.getArguments()[0];
			Long id = table.getId();
			return resources.get(id.intValue());
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
			new Patient().setId("Patient/0"),
			new Patient().setId("Patient/1"),
			new Observation().setId("Observation/2"),
			new Observation().setId("Observation/3")
		);
		mockWhenResourceTableFindById(updatedTimes, resourceTypes);
		when(myDaoRegistry.getResourceDao(eq("Patient"))).thenReturn(myResourceDao);
		when(myDaoRegistry.getResourceDao(eq(Patient.class))).thenReturn(myResourceDao);
		when(myDaoRegistry.getResourceDao(eq("Observation"))).thenReturn(myResourceDao);
		when(myDaoRegistry.getResourceDao(eq(Observation.class))).thenReturn(myResourceDao);
		when(myResourceDao.toResource(any(), anyBoolean())).thenAnswer(t -> {
			ResourceTable table = (ResourceTable) t.getArguments()[0];
			Long id = table.getId();
			return resources.get(id.intValue());
		});
	}

	private void mockFourResourcesNeedReindexing() {
		// Mock resource fetch
		List<Long> values = Arrays.asList(0L, 1L, 2L, 3L);
		when(myResourceTableDao.findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(myPageRequestCaptor.capture(), myLowCaptor.capture(), myHighCaptor.capture())).thenReturn(new SliceImpl<>(values));
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
