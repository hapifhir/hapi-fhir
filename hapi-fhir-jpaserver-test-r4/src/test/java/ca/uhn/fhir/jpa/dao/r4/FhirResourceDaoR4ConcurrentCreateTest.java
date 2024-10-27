package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IPointcut;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.data.IResourceSearchUrlDao;
import ca.uhn.fhir.jpa.interceptor.UserRequestRetryVersionConflictsInterceptor;
import ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.search.ResourceSearchUrlSvc;
import ca.uhn.fhir.jpa.search.SearchUrlJobMaintenanceSvcImpl;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.test.concurrency.PointcutLatch;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;


public class FhirResourceDaoR4ConcurrentCreateTest extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4ConcurrentCreateTest.class);

	private static final boolean IS_SEARCH_URL_DUPLICATE_ACROSS_PARTITIONS_ENABLED_FALSE = false;

	ThreadGaterPointcutLatch myThreadGaterPointcutLatchInterceptor;
	UserRequestRetryVersionConflictsInterceptor myUserRequestRetryVersionConflictsInterceptor;
	ResourceConcurrentSubmitterSvc myResourceConcurrentSubmitterSvc;

	@Autowired
	SearchUrlJobMaintenanceSvcImpl mySearchUrlJobMaintenanceSvc;

	@Autowired
	IResourceSearchUrlDao myResourceSearchUrlDao;

	@Autowired
	ResourceSearchUrlSvc myResourceSearchUrlSvc;

	Callable<String> myResource;

	@BeforeEach
	public void beforeEach(){
		myThreadGaterPointcutLatchInterceptor = new ThreadGaterPointcutLatch("gaterLatch");
		myUserRequestRetryVersionConflictsInterceptor = new UserRequestRetryVersionConflictsInterceptor();

		// this pointcut is AFTER the match url has resolved, but before commit.
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, myThreadGaterPointcutLatchInterceptor);
		myInterceptorRegistry.registerInterceptor(myUserRequestRetryVersionConflictsInterceptor);
		myResourceConcurrentSubmitterSvc = new ResourceConcurrentSubmitterSvc();
		myResource = buildResourceAndCreateCallable();

		List<ResourceSearchUrlEntity> all = myResourceSearchUrlDao.findAll();
		assertThat(all).hasSize(0);
	}

	@AfterEach
	public void afterEach() {
		myResourceConcurrentSubmitterSvc.shutDown();
	}

	@Override
	@AfterEach
	public void afterResetInterceptors() {
		super.afterResetInterceptors();
		myInterceptorRegistry.unregisterInterceptor(myThreadGaterPointcutLatchInterceptor);
		myInterceptorRegistry.unregisterInterceptor(myUserRequestRetryVersionConflictsInterceptor);
	}

	@Test
	public void testMultipleThreads_attemptingToCreatingTheSameResource_willCreateOnlyOneResource() throws InterruptedException, ExecutionException {
		// given
		final int numberOfThreadsAttemptingToCreateDuplicates = 2;
		int expectedResourceCount = myResourceTableDao.findAll().size() + 1;

		myThreadGaterPointcutLatchInterceptor.setExpectedCount(numberOfThreadsAttemptingToCreateDuplicates);

		// when
		// create a situation where multiple threads will try to create the same resource;
		for (int i = 0; i < numberOfThreadsAttemptingToCreateDuplicates; i++){
			myResourceConcurrentSubmitterSvc.submitResource(myResource);
		}

		// let's wait for all executor threads to wait (block) at the pointcut
		myThreadGaterPointcutLatchInterceptor.awaitExpected();

		// we get here only if latch.countdown has reach 0, ie, all executor threads have reached the pointcut
		// so notify them all to allow execution to proceed.
		myThreadGaterPointcutLatchInterceptor.doNotifyAll();
		
		List<String> errorList = myResourceConcurrentSubmitterSvc.waitForThreadsCompletionAndReturnErrors();

		// then
		assertThat(errorList).hasSize(0);
		// red-green before the fix, the size was 'numberOfThreadsAttemptingToCreateDuplicates'
		assertThat(myResourceTableDao.findAll()).hasSize(expectedResourceCount);

	}

	@Test
	public void testRemoveStaleEntries_withNonStaleAndStaleEntries_willOnlyDeleteStaleEntries(){
		// given
		long tenMinutes = 10 * DateUtils.MILLIS_PER_HOUR;

		final ResourceTable resTable1 = myResourceTableDao.save(createResTable());
		final ResourceTable resTable2 = myResourceTableDao.save(createResTable());
		final ResourceTable resTable3 = myResourceTableDao.save(createResTable());
		final ResourceTable resTable4 = myResourceTableDao.save(createResTable());

		Date tooOldBy10Minutes = cutOffTimeMinus(tenMinutes);
		ResourceSearchUrlEntity tooOld1 = ResourceSearchUrlEntity.from("Observation?identifier=20210427133226.444", resTable1, IS_SEARCH_URL_DUPLICATE_ACROSS_PARTITIONS_ENABLED_FALSE).setCreatedTime(tooOldBy10Minutes);
		ResourceSearchUrlEntity tooOld2 = ResourceSearchUrlEntity.from("Observation?identifier=20210427133226.445", resTable2, IS_SEARCH_URL_DUPLICATE_ACROSS_PARTITIONS_ENABLED_FALSE).setCreatedTime(tooOldBy10Minutes);

		Date tooNewBy10Minutes = cutOffTimePlus(tenMinutes);
		ResourceSearchUrlEntity tooNew1 = ResourceSearchUrlEntity.from("Observation?identifier=20210427133226.446", resTable3, IS_SEARCH_URL_DUPLICATE_ACROSS_PARTITIONS_ENABLED_FALSE).setCreatedTime(tooNewBy10Minutes);
		ResourceSearchUrlEntity tooNew2 =ResourceSearchUrlEntity.from("Observation?identifier=20210427133226.447", resTable4, IS_SEARCH_URL_DUPLICATE_ACROSS_PARTITIONS_ENABLED_FALSE).setCreatedTime(tooNewBy10Minutes);

		myResourceSearchUrlDao.saveAll(asList(tooOld1, tooOld2, tooNew1, tooNew2));

		// when
		mySearchUrlJobMaintenanceSvc.removeStaleEntries();

		// then
		List<Long> resourcesPids = getStoredResourceSearchUrlEntitiesPids();
		assertThat(resourcesPids).containsExactlyInAnyOrder(resTable3.getResourceId(), resTable4.getResourceId());
	}

	@Test
	public void testRemoveStaleEntries_withNoEntries_willNotGenerateExceptions(){

		mySearchUrlJobMaintenanceSvc.removeStaleEntries();

	}

	@Test
	public void testMethodDeleteByResId_withEntries_willDeleteTheEntryIfExists(){

		// given
		long nonExistentResourceId = 99l;

		final ResourceTable resTable1 = myResourceTableDao.save(createResTable());
		final ResourceTable resTable2 = myResourceTableDao.save(createResTable());

		ResourceSearchUrlEntity entry1 = ResourceSearchUrlEntity.from("Observation?identifier=20210427133226.444", resTable1, IS_SEARCH_URL_DUPLICATE_ACROSS_PARTITIONS_ENABLED_FALSE);
		ResourceSearchUrlEntity entry2 = ResourceSearchUrlEntity.from("Observation?identifier=20210427133226.445", resTable2, IS_SEARCH_URL_DUPLICATE_ACROSS_PARTITIONS_ENABLED_FALSE);
		myResourceSearchUrlDao.saveAll(asList(entry1, entry2));

		// when
		myResourceSearchUrlSvc.deleteByResId(entry1.getResourcePid());
		myResourceSearchUrlSvc.deleteByResId(nonExistentResourceId);

		// then
		List<Long> resourcesPids = getStoredResourceSearchUrlEntitiesPids();
		assertThat(resourcesPids).containsExactlyInAnyOrder(resTable2.getResourceId());

	}

	private List<Long> getStoredResourceSearchUrlEntitiesPids(){
		List<ResourceSearchUrlEntity> remainingSearchUrlEntities = myResourceSearchUrlDao.findAll();
		return remainingSearchUrlEntities.stream().map(ResourceSearchUrlEntity::getResourcePid).collect(Collectors.toList());
	}

	private Date cutOffTimePlus(long theAdjustment) {
		long currentTimeMillis = System.currentTimeMillis();
		long offset = currentTimeMillis - SearchUrlJobMaintenanceSvcImpl.OUR_CUTOFF_IN_MILLISECONDS + theAdjustment;
		return new Date(offset);
	}

	@Nonnull
	private static ResourceTable createResTable() {
		final ResourceTable resourceTable = new ResourceTable();
		resourceTable.setResourceType("Patient");
		resourceTable.setPublished(new Date());
		resourceTable.setUpdated(new Date());
		return resourceTable;
	}

	private Date cutOffTimeMinus(long theAdjustment) {
		return cutOffTimePlus(-theAdjustment);
	}

	private Callable<String> buildResourceAndCreateCallable() {
		return () -> {

			Identifier identifier = new Identifier().setValue("20210427133226.444+0800");
			Observation obs = new Observation().addIdentifier(identifier);

			RequestDetails requestDetails = new SystemRequestDetails();
			requestDetails.setRetry(true);
			requestDetails.setMaxRetries(3);

			try {
				ourLog.info("Creating resource");
				DaoMethodOutcome outcome = myObservationDao.create(obs, "identifier=20210427133226.444%2B0800", requestDetails);
			} catch (Throwable t) {
				ourLog.info("create threw an exception {}", t.getMessage());
				fail();
			}
			return null;
		};
		
	}

	/**
	 * PointcutLatch that will force an executing thread to wait (block) until being notified.
	 *
	 * This class can be used to replicate race conditions. It provides a mechanism to block a predefined number of
	 * executing threads at a pointcut.  When all expected threads have reached the pointcut, the race condition is
	 * created by invoking the {@link #doNotifyAll()} method that will mark all waiting threads as being ready for execution.
	 */
	public static class ThreadGaterPointcutLatch extends PointcutLatch {
		public ThreadGaterPointcutLatch(String theName) {
			super(theName);
		}

		@Override
		public void invoke(IPointcut thePointcut, HookParams theArgs)  {
			doInvoke(thePointcut, theArgs);
		}

		private synchronized void doInvoke(IPointcut thePointcut, HookParams theArgs){
			super.invoke(thePointcut, theArgs);
			try {
				String threadName = Thread.currentThread().getName();
				ourLog.info(String.format("I'm thread %s and i'll going to sleep", threadName));
				wait(10*1000);
				ourLog.info(String.format("I'm thread %s and i'm waking up", threadName));
			} catch (InterruptedException theE) {
				throw new RuntimeException(theE);
			}
		}

		public synchronized void doNotifyAll(){
			notifyAll();
		}

	}

	public static class ResourceConcurrentSubmitterSvc{
		ExecutorService myPool;
		List<Future<String>> myFutures = new ArrayList<>();
		public List<String> waitForThreadsCompletionAndReturnErrors() throws ExecutionException, InterruptedException {

			List<String> errorList = new ArrayList<>();

			for (Future<String> next : myFutures) {
				String nextError = next.get();
				if (StringUtils.isNotBlank(nextError)) {
					errorList.add(nextError);
				}
			}
			return errorList;
		}

		private ExecutorService getExecutorServicePool(){
			if(Objects.isNull(myPool)){
				int maxThreadsUsed = TestR4Config.ourMaxThreads - 1;
				myPool = Executors.newFixedThreadPool(Math.min(maxThreadsUsed, 5));
			}

			return myPool;
		}

		public void shutDown(){
			getExecutorServicePool().shutdown();
		}

		public void submitResource(Callable<String> theResourceRunnable) {
			Future<String> future = getExecutorServicePool().submit(theResourceRunnable);
			myFutures.add(future);
		}
	}

}
