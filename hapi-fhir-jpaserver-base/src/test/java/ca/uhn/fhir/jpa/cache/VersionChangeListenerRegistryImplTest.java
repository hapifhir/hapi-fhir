package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
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

public class VersionChangeListenerRegistryImplTest extends BaseJpaR4Test {
	@Autowired
	IVersionChangeListenerRegistry myVersionChangeListenerRegistry;

	private final static String RESOURCE_TYPE = "Patient";
	private TestCallback myTestCallback = new TestCallback();

	@BeforeEach
	public void before() {
		myTestCallback.clear();
	}

	@AfterEach
	public void after() {
		myVersionChangeListenerRegistry.clearListenersForUnitTest();
		myVersionChangeListenerRegistry.clearCacheForUnitTest();
		myVersionChangeListenerRegistry.refreshAllCachesIfNecessary();
	}

	@Test
	public void testRegisterInterceptor() throws InterruptedException {
		setupVersionChangeRegistry(RESOURCE_TYPE, SearchParameterMap.newSynchronous());

		Patient patient = setupPatientAndEnsureCallbacksOccur(null);
		IdDt patientId = new IdDt(patient.getIdElement().toUnqualifiedVersionless());

		patient.setActive(false);
		patient.setGender(Enumerations.AdministrativeGender.FEMALE);
		myTestCallback.setExpectedCount(1);
		myPatientDao.update(patient);

		long count = myVersionChangeListenerRegistry.forceRefresh(RESOURCE_TYPE);
		assertEquals(1, count);
		myTestCallback.awaitExpected();
		assertEquals(2L, myTestCallback.getResourceId().getVersionIdPartAsLong());
		assertEquals(BaseResourceMessage.OperationTypeEnum.UPDATE, myTestCallback.getOperationTypeEnum());

		myTestCallback.setExpectedCount(1);
		myPatientDao.delete(patientId.toVersionless());
		count = myVersionChangeListenerRegistry.forceRefresh(RESOURCE_TYPE);
		assertEquals(1, count);
		myTestCallback.awaitExpected();
		assertEquals(patientId, myTestCallback.getResourceId());
		assertEquals(BaseResourceMessage.OperationTypeEnum.DELETE, myTestCallback.getOperationTypeEnum());
	}

	private void setupVersionChangeRegistry(String theResourceType, SearchParameterMap theSearchParameterMap) {
		myVersionChangeListenerRegistry.registerResourceVersionChangeListener(theResourceType, theSearchParameterMap, myTestCallback);
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

		List<IdDt> resourceIds = myTestCallback.getInitResourceIds();
		assertThat(resourceIds, hasSize(1));
		IdDt resourceId = resourceIds.get(0);
		assertEquals(patientId.toString(), resourceId.toString());
		assertEquals(1L, resourceId.getVersionIdPartAsLong());

		return patient;
	}

	private IdDt createPatientAndRefreshCache(Patient thePatient, TestCallback theTestCallback, long theExpectedCount) throws InterruptedException {
		IIdType retval = myPatientDao.create(thePatient).getId();
		long count = myVersionChangeListenerRegistry.forceRefresh(RESOURCE_TYPE);
		assertEquals(theExpectedCount, count);
		return new IdDt(retval);
	}

	@Test
	public void testRegisterPolling() throws InterruptedException {
		setupVersionChangeRegistry(RESOURCE_TYPE, SearchParameterMap.newSynchronous());

		Patient patient = setupPatientAndEnsureCallbacksOccur(null);
		IdDt patientId = new IdDt(patient.getIdElement());

		// Pretend we're on a different process in the cluster and so our cache doesn't have the entry yet
		myVersionChangeListenerRegistry.clearCacheForUnitTest();
		myTestCallback.setExpectedCount(1);
		long count = myVersionChangeListenerRegistry.forceRefresh(RESOURCE_TYPE);
		assertEquals(1, count);
		List<HookParams> calledWith = myTestCallback.awaitExpected();
		IdDt calledWithId  = (IdDt) PointcutLatch.getLatchInvocationParameter(calledWith);
		assertEquals(patientId, calledWithId);
	}

	@Test
	public void testRegisterInterceptorFor2Patients() throws InterruptedException {
		setupVersionChangeRegistry(RESOURCE_TYPE, createSearchParameterMap(Enumerations.AdministrativeGender.MALE));

		Patient patientMale = setupPatientAndEnsureCallbacksOccur(Enumerations.AdministrativeGender.MALE);
		IdDt patientIdMale = new IdDt(patientMale.getIdElement());

		myTestCallback.clear();

		Patient patientFemale = new Patient();
		patientFemale.setActive(true);
		patientFemale.setGender(Enumerations.AdministrativeGender.FEMALE);
		// NOTE: This scenario does not invoke the myTestCallback listener so just call the DAO directly
		IIdType patientIdFemale = new IdDt(myPatientDao.create(patientFemale).getId());
		long count = myVersionChangeListenerRegistry.forceRefresh(RESOURCE_TYPE);
		assertEquals(0, count);
		assertNotNull(patientIdFemale.toString());
		assertNull(myTestCallback.getResourceId());
		assertNull(myTestCallback.getOperationTypeEnum());
	}

	// FIXME KHS review
	@Test
	public void testRegisterPollingFor2Patients() throws InterruptedException {
		setupVersionChangeRegistry(RESOURCE_TYPE, createSearchParameterMap(Enumerations.AdministrativeGender.MALE));

		Patient patientMale = setupPatientAndEnsureCallbacksOccur(Enumerations.AdministrativeGender.MALE);
		IdDt patientIdMale = new IdDt(patientMale.getIdElement());

		Patient patientFemale = new Patient();
		patientFemale.setActive(true);
		patientFemale.setGender(Enumerations.AdministrativeGender.FEMALE);
		// NOTE: This scenario does not invoke the myTestCallback listener so just call the DAO directly
		IIdType patientIdFemale = new IdDt(myPatientDao.create(patientFemale).getId());
		long count = myVersionChangeListenerRegistry.forceRefresh(RESOURCE_TYPE);
		assertEquals(0, count);
		assertNotNull(patientIdFemale.toString());
		assertNull(myTestCallback.getResourceId());
		assertNull(myTestCallback.getOperationTypeEnum());

		// Pretend we're on a different process in the cluster and so our cache doesn't have the entry yet
		myVersionChangeListenerRegistry.clearCacheForUnitTest();
		myTestCallback.setExpectedCount(1);
		count = myVersionChangeListenerRegistry.forceRefresh(RESOURCE_TYPE);
		assertEquals(1, count);
		List<HookParams> calledWith = myTestCallback.awaitExpected();
		IdDt calledWithId  = (IdDt) PointcutLatch.getLatchInvocationParameter(calledWith);
		assertEquals(patientIdMale, calledWithId);
	}

	private SearchParameterMap createSearchParameterMap(Enumerations.AdministrativeGender theGender) {
		SearchParameterMap theSearchParamMap = new SearchParameterMap();
		theSearchParamMap.setLoadSynchronous(true);
		theSearchParamMap.add(Patient.SP_GENDER,
			new TokenParam(null, org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender.MALE.toCode()));
		return theSearchParamMap;
	}

	private static class TestCallback implements IVersionChangeListener, IPointcutLatch {
		private static final Logger ourLog = LoggerFactory.getLogger(TestCallback.class);
		private final PointcutLatch mySingleLatch = new PointcutLatch("VersionChangeListener single resource called");
		private final PointcutLatch myInitLatch = new PointcutLatch("VersionChangeListener init called");

		private IIdType myResourceId;
		private BaseResourceMessage.OperationTypeEnum myOperationTypeEnum;
		private Collection<IdDt> myInitResourceIds;

		@Override
		public void handleCreate(IdDt theResourceId) {
			handle(theResourceId, BaseResourceMessage.OperationTypeEnum.CREATE);
		}

		@Override
		public void handleUpdate(IdDt theResourceId) {
			handle(theResourceId, BaseResourceMessage.OperationTypeEnum.UPDATE);
		}

		@Override
		public void handleDelete(IdDt theResourceId) {
			handle(theResourceId, BaseResourceMessage.OperationTypeEnum.DELETE);
		}

		@Override
		public void handleInit(Collection<IdDt> theResourceIds) {
			myInitResourceIds = theResourceIds;
			myInitLatch.call(theResourceIds);
		}

		private void handle(IIdType theResourceId, BaseResourceMessage.OperationTypeEnum theOperationTypeEnum) {
			ourLog.debug("TestCallback.handle('{}', '{}') called...", theResourceId, theOperationTypeEnum);
			myResourceId = theResourceId;
			myOperationTypeEnum = theOperationTypeEnum;
			mySingleLatch.call(theResourceId);
		}

		@Override
		public void clear() {
			myResourceId = null;
			myOperationTypeEnum = null;
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

		public IIdType getResourceId() {
			return myResourceId;
		}

		public BaseResourceMessage.OperationTypeEnum getOperationTypeEnum() {
			return myOperationTypeEnum;
		}

		public List<IdDt> getInitResourceIds() {
			return new ArrayList<>(myInitResourceIds);
		}

		public void setInitExpectedCount(int theCount) {
			myInitLatch.setExpectedCount(theCount);
		}

		public void awaitInitExpected() throws InterruptedException {
			myInitLatch.awaitExpected();
		}
	}
}
