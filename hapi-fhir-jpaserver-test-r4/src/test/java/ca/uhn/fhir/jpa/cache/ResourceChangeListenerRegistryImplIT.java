package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.test.concurrency.IPointcutLatch;
import ca.uhn.test.concurrency.PointcutLatch;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceChangeListenerRegistryImplIT extends BaseJpaR4Test {
	private static final long TEST_REFRESH_INTERVAL = DateUtils.MILLIS_PER_DAY;
	@Autowired
	ResourceChangeListenerRegistryImpl myResourceChangeListenerRegistry;
	@Autowired
	ResourceChangeListenerCacheRefresherImpl myResourceChangeListenerCacheRefresher;

	private final static String RESOURCE_NAME = "Patient";
	private TestCallback myMaleTestCallback = new TestCallback("MALE");
	private TestCallback myFemaleTestCallback = new TestCallback("FEMALE");

	@BeforeEach
	public void before() {
		myMaleTestCallback.clear();
	}

	@AfterEach
	public void after() {
		myResourceChangeListenerRegistry.clearListenersForUnitTest();
		myResourceChangeListenerRegistry.clearCachesForUnitTest();
	}

	@Test
	public void testRegisterListener() throws InterruptedException {
		assertEquals(0, myResourceChangeListenerRegistry.getResourceVersionCacheSizeForUnitTest());

		IResourceChangeListenerCache cache = myResourceChangeListenerRegistry.registerResourceResourceChangeListener(RESOURCE_NAME, SearchParameterMap.newSynchronous(), myMaleTestCallback, TEST_REFRESH_INTERVAL);

		Patient patient = createPatientWithInitLatch(null, myMaleTestCallback);
		assertEquals(1, myResourceChangeListenerRegistry.getResourceVersionCacheSizeForUnitTest());

		IdDt patientId = new IdDt(patient.getIdElement().toUnqualifiedVersionless());

		patient.setActive(false);
		patient.setGender(Enumerations.AdministrativeGender.FEMALE);
		myPatientDao.update(patient);

		myMaleTestCallback.setExpectedCount(1);
		ResourceChangeResult result = cache.forceRefresh();
		myMaleTestCallback.awaitExpected();

		assertResult(result, 0, 1, 0);
		assertEquals(2L, myMaleTestCallback.getUpdateResourceId().getVersionIdPartAsLong());
		assertEquals(1, myResourceChangeListenerRegistry.getResourceVersionCacheSizeForUnitTest());

		// Calling forceRefresh with no changes does not call listener
		result = cache.forceRefresh();
		assertResult(result, 0, 0, 0);

		myMaleTestCallback.setExpectedCount(1);
		myPatientDao.delete(patientId.toVersionless());
		result = cache.forceRefresh();
		assertResult(result, 0, 0, 1);
		myMaleTestCallback.awaitExpected();
		assertEquals(patientId, myMaleTestCallback.getDeletedResourceId());
		assertEquals(0, myResourceChangeListenerRegistry.getResourceVersionCacheSizeForUnitTest());
	}

	@Test
	public void testNonInMemorySearchParamCannotBeRegistered() {
		try {
			SearchParameterMap map = new SearchParameterMap();
			map.setLastUpdated(new DateRangeParam("1965", "1970"));
			myResourceChangeListenerRegistry.registerResourceResourceChangeListener(RESOURCE_NAME, map, myMaleTestCallback, TEST_REFRESH_INTERVAL);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(482) + "SearchParameterMap SearchParameterMap[] cannot be evaluated in-memory: Parameter: <_lastUpdated> Reason: Standard parameters not supported.  Only search parameter maps that can be evaluated in-memory may be registered.", e.getMessage());
		}
	}

	private void assertResult(ResourceChangeResult theResult, long theExpectedCreated, long theExpectedUpdated, long theExpectedDeleted) {
		assertEquals(theExpectedCreated, theResult.created, "created results");
		assertEquals(theExpectedUpdated, theResult.updated, "updated results");
		assertEquals(theExpectedDeleted, theResult.deleted, "deleted results");
	}

	private void assertEmptyResult(ResourceChangeResult theResult) {
		assertResult(theResult, 0, 0, 0);
	}

	private Patient createPatientWithInitLatch(Enumerations.AdministrativeGender theGender, TestCallback theTestCallback) throws InterruptedException {
		Patient patient = new Patient();
		patient.setActive(true);
		if (theGender != null) {
			patient.setGender(theGender);
		}
		theTestCallback.setInitExpectedCount(1);
		IdDt patientId = createPatientAndRefreshCache(patient, theTestCallback, 1);
		theTestCallback.awaitInitExpected();

		List<IIdType> resourceIds = theTestCallback.getInitResourceIds();
		assertThat(resourceIds, hasSize(1));
		IIdType resourceId = resourceIds.get(0);
		assertEquals(patientId.toString(), resourceId.toString());
		assertEquals(1L, resourceId.getVersionIdPartAsLong());

		return patient;
	}

	private IdDt createPatientAndRefreshCache(Patient thePatient, TestCallback theTestCallback, long theExpectedCount) throws InterruptedException {
		IIdType retval = myPatientDao.create(thePatient).getId();
		ResourceChangeResult result = myResourceChangeListenerCacheRefresher.forceRefreshAllCachesForUnitTest();
		assertResult(result, theExpectedCount, 0, 0);
		return new IdDt(retval);
	}

	@Test
	public void testRegisterPolling() throws InterruptedException {
		IResourceChangeListenerCache cache = myResourceChangeListenerRegistry.registerResourceResourceChangeListener(RESOURCE_NAME, SearchParameterMap.newSynchronous(), myMaleTestCallback, TEST_REFRESH_INTERVAL);

		Patient patient = createPatientWithInitLatch(null, myMaleTestCallback);
		IdDt patientId = new IdDt(patient.getIdElement());

		// Pretend we're on a different process in the cluster and so our cache doesn't have the cache yet
		myResourceChangeListenerRegistry.clearCachesForUnitTest();
		myMaleTestCallback.setExpectedCount(1);
		ResourceChangeResult result = cache.forceRefresh();
		assertResult(result, 1, 0, 0);
		List<HookParams> calledWith = myMaleTestCallback.awaitExpected();
		ResourceChangeEvent resourceChangeEvent = (ResourceChangeEvent) PointcutLatch.getLatchInvocationParameter(calledWith);
		assertEquals(patientId, resourceChangeEvent.getCreatedResourceIds().get(0));
	}

	@Test
	public void testRegisterInterceptorFor2Patients() throws InterruptedException {
		IResourceChangeListenerCache cache = myResourceChangeListenerRegistry.registerResourceResourceChangeListener(RESOURCE_NAME, createSearchParameterMap(Enumerations.AdministrativeGender.MALE), myMaleTestCallback, TEST_REFRESH_INTERVAL);

		createPatientWithInitLatch(Enumerations.AdministrativeGender.MALE, myMaleTestCallback);

		myMaleTestCallback.clear();

		Patient patientFemale = new Patient();
		patientFemale.setActive(true);
		patientFemale.setGender(Enumerations.AdministrativeGender.FEMALE);

		// NOTE: This scenario does not invoke the myTestCallback listener so just call the DAO directly
		IIdType patientIdFemale = new IdDt(myPatientDao.create(patientFemale).getId());
		ResourceChangeResult result = cache.forceRefresh();
		assertEmptyResult(result);
		assertNotNull(patientIdFemale.toString());
		assertNull(myMaleTestCallback.getResourceChangeEvent());
	}

	@Test
	public void testRegister2InterceptorsFor2Patients() throws InterruptedException {
		myResourceChangeListenerRegistry.registerResourceResourceChangeListener(RESOURCE_NAME, createSearchParameterMap(Enumerations.AdministrativeGender.MALE), myMaleTestCallback, TEST_REFRESH_INTERVAL);
		createPatientWithInitLatch(Enumerations.AdministrativeGender.MALE, myMaleTestCallback);
		myMaleTestCallback.clear();

		myResourceChangeListenerRegistry.registerResourceResourceChangeListener(RESOURCE_NAME, createSearchParameterMap(Enumerations.AdministrativeGender.FEMALE), myFemaleTestCallback, TEST_REFRESH_INTERVAL);
		createPatientWithInitLatch(Enumerations.AdministrativeGender.FEMALE, myFemaleTestCallback);
	}

	@Test
	public void testRegisterPollingFor2Patients() throws InterruptedException {
		IResourceChangeListenerCache cache = myResourceChangeListenerRegistry.registerResourceResourceChangeListener(RESOURCE_NAME, createSearchParameterMap(Enumerations.AdministrativeGender.MALE), myMaleTestCallback, TEST_REFRESH_INTERVAL);

		Patient patientMale = createPatientWithInitLatch(Enumerations.AdministrativeGender.MALE, myMaleTestCallback);
		IdDt patientIdMale = new IdDt(patientMale.getIdElement());

		Patient patientFemale = new Patient();
		patientFemale.setActive(true);
		patientFemale.setGender(Enumerations.AdministrativeGender.FEMALE);

		// NOTE: This scenario does not invoke the myTestCallback listener so just call the DAO directly
		IIdType patientIdFemale = new IdDt(myPatientDao.create(patientFemale).getId());
		ResourceChangeResult result = cache.forceRefresh();
		assertEmptyResult(result);
		assertNotNull(patientIdFemale.toString());
		assertNull(myMaleTestCallback.getResourceChangeEvent());

		// Pretend we're on a different process in the cluster and so our cache doesn't have the cache yet
		myResourceChangeListenerRegistry.clearCachesForUnitTest();
		myMaleTestCallback.setExpectedCount(1);
		result = cache.forceRefresh();
		// We should still only get one matching result
		assertResult(result, 1, 0, 0);
		List<HookParams> calledWith = myMaleTestCallback.awaitExpected();
		ResourceChangeEvent resourceChangeEvent = (ResourceChangeEvent) PointcutLatch.getLatchInvocationParameter(calledWith);
		assertEquals(patientIdMale, resourceChangeEvent.getCreatedResourceIds().get(0));
	}

	@Test
	public void twoListenersSameMap() throws InterruptedException {
		assertEquals(0, myResourceChangeListenerRegistry.getResourceVersionCacheSizeForUnitTest());

		SearchParameterMap searchParameterMap = createSearchParameterMap(Enumerations.AdministrativeGender.MALE);
		IResourceChangeListenerCache cache = myResourceChangeListenerRegistry.registerResourceResourceChangeListener(RESOURCE_NAME, searchParameterMap, myMaleTestCallback, TEST_REFRESH_INTERVAL);
		assertEquals(0, myResourceChangeListenerRegistry.getResourceVersionCacheSizeForUnitTest());

		createPatientWithInitLatch(Enumerations.AdministrativeGender.MALE, myMaleTestCallback);
		assertEquals(1, myResourceChangeListenerRegistry.getResourceVersionCacheSizeForUnitTest());

		TestCallback otherTestCallback = new TestCallback("OTHER_MALE");
		IResourceChangeListenerCache otherCache = myResourceChangeListenerRegistry.registerResourceResourceChangeListener(RESOURCE_NAME, searchParameterMap, otherTestCallback, TEST_REFRESH_INTERVAL);

		assertEquals(1, myResourceChangeListenerRegistry.getResourceVersionCacheSizeForUnitTest());

		otherCache.forceRefresh();
		assertEquals(2, myResourceChangeListenerRegistry.getResourceVersionCacheSizeForUnitTest());

		myResourceChangeListenerRegistry.unregisterResourceResourceChangeListener(myMaleTestCallback);
		assertEquals(1, myResourceChangeListenerRegistry.getResourceVersionCacheSizeForUnitTest());

		myResourceChangeListenerRegistry.unregisterResourceResourceChangeListener(otherTestCallback);
		assertEquals(0, myResourceChangeListenerRegistry.getResourceVersionCacheSizeForUnitTest());
	}

	private SearchParameterMap createSearchParameterMap(Enumerations.AdministrativeGender theGender) {
		return SearchParameterMap.newSynchronous().add(Patient.SP_GENDER, new TokenParam(null, theGender.toCode()));
	}

	private static class TestCallback implements IResourceChangeListener, IPointcutLatch {
		private static final Logger ourLog = LoggerFactory.getLogger(TestCallback.class);
		private final PointcutLatch myHandleLatch;
		private final PointcutLatch myInitLatch;
		private final String myName;

		private IResourceChangeEvent myResourceChangeEvent;
		private Collection<IIdType> myInitResourceIds;

		public TestCallback(String theName) {
			myName = theName;
			myHandleLatch = new PointcutLatch(theName + " ResourceChangeListener handle called");
			myInitLatch = new PointcutLatch(theName + " ResourceChangeListener init called");
		}

		@Override
		public void handleChange(IResourceChangeEvent theResourceChangeEvent) {
			ourLog.info("{} TestCallback.handleChange() called with {}", myName, theResourceChangeEvent);
			myResourceChangeEvent = theResourceChangeEvent;
			myHandleLatch.call(theResourceChangeEvent);
		}

		@Override
		public void handleInit(Collection<IIdType> theResourceIds) {
			myInitResourceIds = theResourceIds;
			myInitLatch.call(theResourceIds);
		}

		@Override
		public void clear() {
			myResourceChangeEvent = null;
			myInitResourceIds = null;
			myHandleLatch.clear();
			myInitLatch.clear();
		}

		@Override
		public void setExpectedCount(int theCount) {
			myHandleLatch.setExpectedCount(theCount);
		}

		@Override
		public List<HookParams> awaitExpected() throws InterruptedException {
			return myHandleLatch.awaitExpected();
		}

		public List<IIdType> getInitResourceIds() {
			return new ArrayList<>(myInitResourceIds);
		}

		public IResourceChangeEvent getResourceChangeEvent() {
			return myResourceChangeEvent;
		}

		public void setInitExpectedCount(int theCount) {
			myInitLatch.setExpectedCount(theCount);
		}

		public void awaitInitExpected() throws InterruptedException {
			myInitLatch.awaitExpected();
		}

		public IIdType getUpdateResourceId() {
			assertThat(myResourceChangeEvent.getUpdatedResourceIds(), hasSize(1));
			return myResourceChangeEvent.getUpdatedResourceIds().get(0);
		}

		public IIdType getDeletedResourceId() {
			assertThat(myResourceChangeEvent.getDeletedResourceIds(), hasSize(1));
			return myResourceChangeEvent.getDeletedResourceIds().get(0);
		}
	}
}
