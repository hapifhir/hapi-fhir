package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.test.concurrency.IPointcutLatch;
import ca.uhn.test.concurrency.PointcutLatch;
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

public class ResourceChangeListenerRegistryImplTest extends BaseJpaR4Test {
	@Autowired
	ResourceChangeListenerRegistryImpl myResourceChangeListenerRegistry;

	private final static String RESOURCE_NAME = "Patient";
	private TestCallback myTestCallback = new TestCallback();

	@BeforeEach
	public void before() {
		myTestCallback.clear();
	}

	@AfterEach
	public void after() {
		myResourceChangeListenerRegistry.clearListenersForUnitTest();
		myResourceChangeListenerRegistry.clearCacheForUnitTest();
		myResourceChangeListenerRegistry.refreshAllCachesIfNecessary();
	}

	@Test
	public void testRegisterInterceptor() throws InterruptedException {
		assertEquals(0, myResourceChangeListenerRegistry.getResourceVersionCacheSizeForUnitTest("Patient"));

		setupResourceChangeRegistry(RESOURCE_NAME, SearchParameterMap.newSynchronous());

		Patient patient = setupPatientAndEnsureCallbacksOccur(null);
		assertEquals(1, myResourceChangeListenerRegistry.getResourceVersionCacheSizeForUnitTest("Patient"));

		IdDt patientId = new IdDt(patient.getIdElement().toUnqualifiedVersionless());

		patient.setActive(false);
		patient.setGender(Enumerations.AdministrativeGender.FEMALE);
		myTestCallback.setExpectedCount(1);
		myPatientDao.update(patient);

		ResourceChangeResult result = myResourceChangeListenerRegistry.forceRefresh(RESOURCE_NAME);
		assertResult(result, 0, 1, 0);
		myTestCallback.awaitExpected();
		assertEquals(2L, myTestCallback.getUpdateResourceId().getVersionIdPartAsLong());
		assertEquals(1, myResourceChangeListenerRegistry.getResourceVersionCacheSizeForUnitTest("Patient"));

		myTestCallback.setExpectedCount(1);
		myPatientDao.delete(patientId.toVersionless());
		result = myResourceChangeListenerRegistry.forceRefresh(RESOURCE_NAME);
		assertResult(result, 0, 0, 1);
		myTestCallback.awaitExpected();
		assertEquals(patientId, myTestCallback.getDeletedResourceId());
		assertEquals(0, myResourceChangeListenerRegistry.getResourceVersionCacheSizeForUnitTest("Patient"));
	}

	@Test
	public void testSearchParamNotInMemoryFails() {
		try {
			SearchParameterMap map = new SearchParameterMap();
			map.setLastUpdated(new DateRangeParam("1965", "1970"));
			myResourceChangeListenerRegistry.registerResourceResourceChangeListener(RESOURCE_NAME, map, myTestCallback);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("SearchParameterMap SearchParameterMap[] cannot be evaluated in-memory: Parameter: <_lastUpdated> Reason: Standard parameters not supported.  Only search parameter maps that can be evaluated in-memory may be registered.", e.getMessage());
		}
	}

	private void assertResult(ResourceChangeResult theResult, long theExpectedAdded, long theExpectedUpdated, long theExpectedRemoved) {
		assertEquals(theExpectedAdded, theResult.created, "added results");
		assertEquals(theExpectedUpdated, theResult.updated, "updated results");
		assertEquals(theExpectedRemoved, theResult.deleted, "removed results");
	}

	private void assertEmptyResult(ResourceChangeResult theResult) {
		assertResult(theResult, 0, 0, 0);
	}

	private void setupResourceChangeRegistry(String theResourceName, SearchParameterMap theSearchParameterMap) {
		myResourceChangeListenerRegistry.registerResourceResourceChangeListener(theResourceName, theSearchParameterMap, myTestCallback);
	}

	private Patient setupPatientAndEnsureCallbacksOccur(Enumerations.AdministrativeGender theGender) throws InterruptedException {
		Patient patient = new Patient();
		patient.setActive(true);
		if (theGender != null) {
			patient.setGender(theGender);
		}
		myTestCallback.setInitExpectedCount(1);
		IdDt patientId = createPatientAndRefreshCache(patient, myTestCallback, 1);
		myTestCallback.awaitInitExpected();

		List<IIdType> resourceIds = myTestCallback.getInitResourceIds();
		assertThat(resourceIds, hasSize(1));
		IIdType resourceId = resourceIds.get(0);
		assertEquals(patientId.toString(), resourceId.toString());
		assertEquals(1L, resourceId.getVersionIdPartAsLong());

		return patient;
	}

	private IdDt createPatientAndRefreshCache(Patient thePatient, TestCallback theTestCallback, long theExpectedCount) throws InterruptedException {
		IIdType retval = myPatientDao.create(thePatient).getId();
		ResourceChangeResult result = myResourceChangeListenerRegistry.forceRefresh(RESOURCE_NAME);
		assertResult(result, theExpectedCount, 0, 0);
		return new IdDt(retval);
	}

	@Test
	public void testRegisterPolling() throws InterruptedException {
		setupResourceChangeRegistry(RESOURCE_NAME, SearchParameterMap.newSynchronous());

		Patient patient = setupPatientAndEnsureCallbacksOccur(null);
		IdDt patientId = new IdDt(patient.getIdElement());

		// Pretend we're on a different process in the cluster and so our cache doesn't have the entry yet
		myResourceChangeListenerRegistry.clearCacheForUnitTest();
		myTestCallback.setExpectedCount(1);
		ResourceChangeResult result = myResourceChangeListenerRegistry.forceRefresh(RESOURCE_NAME);
		assertResult(result, 1, 0, 0);
		List<HookParams> calledWith = myTestCallback.awaitExpected();
		IdDt calledWithId = (IdDt) PointcutLatch.getLatchInvocationParameter(calledWith);
		assertEquals(patientId, calledWithId);
	}

	@Test
	public void testRegisterInterceptorFor2Patients() throws InterruptedException {
		setupResourceChangeRegistry(RESOURCE_NAME, createSearchParameterMap(Enumerations.AdministrativeGender.MALE));

		Patient patientMale = setupPatientAndEnsureCallbacksOccur(Enumerations.AdministrativeGender.MALE);
		IdDt patientIdMale = new IdDt(patientMale.getIdElement());

		myTestCallback.clear();

		Patient patientFemale = new Patient();
		patientFemale.setActive(true);
		patientFemale.setGender(Enumerations.AdministrativeGender.FEMALE);
		// NOTE: This scenario does not invoke the myTestCallback listener so just call the DAO directly
		IIdType patientIdFemale = new IdDt(myPatientDao.create(patientFemale).getId());
		ResourceChangeResult result = myResourceChangeListenerRegistry.forceRefresh(RESOURCE_NAME);
		assertEmptyResult(result);
		assertNotNull(patientIdFemale.toString());
		assertNull(myTestCallback.getResourceChangeEvent());
	}

	// FIXME KHS review
	@Test
	public void testRegisterPollingFor2Patients() throws InterruptedException {
		setupResourceChangeRegistry(RESOURCE_NAME, createSearchParameterMap(Enumerations.AdministrativeGender.MALE));

		Patient patientMale = setupPatientAndEnsureCallbacksOccur(Enumerations.AdministrativeGender.MALE);
		IdDt patientIdMale = new IdDt(patientMale.getIdElement());

		Patient patientFemale = new Patient();
		patientFemale.setActive(true);
		patientFemale.setGender(Enumerations.AdministrativeGender.FEMALE);
		// NOTE: This scenario does not invoke the myTestCallback listener so just call the DAO directly
		IIdType patientIdFemale = new IdDt(myPatientDao.create(patientFemale).getId());
		ResourceChangeResult result = myResourceChangeListenerRegistry.forceRefresh(RESOURCE_NAME);
		assertEmptyResult(result);
		assertNotNull(patientIdFemale.toString());
		assertNull(myTestCallback.getResourceChangeEvent());

		// Pretend we're on a different process in the cluster and so our cache doesn't have the entry yet
		myResourceChangeListenerRegistry.clearCacheForUnitTest();
		myTestCallback.setExpectedCount(1);
		result = myResourceChangeListenerRegistry.forceRefresh(RESOURCE_NAME);
		assertResult(result, 1, 0, 0);
		List<HookParams> calledWith = myTestCallback.awaitExpected();
		IdDt calledWithId = (IdDt) PointcutLatch.getLatchInvocationParameter(calledWith);
		assertEquals(patientIdMale, calledWithId);
	}

	private SearchParameterMap createSearchParameterMap(Enumerations.AdministrativeGender theGender) {
		return SearchParameterMap.newSynchronous().add(Patient.SP_GENDER, new TokenParam(null, theGender.toCode()));
	}

	private static class TestCallback implements IResourceChangeListener, IPointcutLatch {
		private static final Logger ourLog = LoggerFactory.getLogger(TestCallback.class);
		private final PointcutLatch mySingleLatch = new PointcutLatch("ResourceChangeListener single resource called");
		private final PointcutLatch myInitLatch = new PointcutLatch("ResourceChangeListener init called");

		private ResourceChangeEvent myResourceChangeEvent;
		private Collection<IIdType> myInitResourceIds;

		@Override
		public void handleChange(ResourceChangeEvent theResourceChangeEvent) {
			ourLog.debug("TestCallback.handleChange() called with {}", theResourceChangeEvent);
			myResourceChangeEvent = theResourceChangeEvent;
			mySingleLatch.call(theResourceChangeEvent);
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
			mySingleLatch.clear();
			myInitLatch.clear();
		}

		@Override
		public void setExpectedCount(int theCount) {
			mySingleLatch.setExpectedCount(theCount);
		}

		@Override
		public List<HookParams> awaitExpected() throws InterruptedException {
			return mySingleLatch.awaitExpected();
		}

		public List<IIdType> getInitResourceIds() {
			return new ArrayList<>(myInitResourceIds);
		}

		public ResourceChangeEvent getResourceChangeEvent() {
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
