package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VersionChangeListenerRegistryImplTest extends BaseJpaR4Test {
	@Autowired
	IVersionChangeListenerRegistry myVersionChangeListenerRegistry;

	private final TestCallback myTestCallback = new TestCallback();

	@BeforeEach
	public void before() {
		myTestCallback.clear();
	}

	@AfterEach
	public void after() {
		myVersionChangeListenerRegistry.clearListenersForUnitTest();
		myVersionChangeListenerRegistry.clearCacheForUnitTest();
		myVersionChangeListenerRegistry.requestRefresh();
	}

	@Test
	public void testThatAddingANewResourceAlsoAddsANewCacheEntry() {
		myVersionChangeListenerRegistry.clearCacheForUnitTest();

		Patient patientMale = new Patient();
		patientMale.setActive(true);
		patientMale.setGender(Enumerations.AdministrativeGender.FEMALE);
		IIdType patientIdMale = new IdDt(myPatientDao.create(patientMale).getId());

		myVersionChangeListenerRegistry.requestRefresh(patientIdMale.getResourceType());
		// TODO KBD 
		IIdType iIdType = patientIdMale.toUnqualifiedVersionless();
		assertTrue(myVersionChangeListenerRegistry.cacheContainsKey(new IdDt(iIdType)));
	}

	@Test
	public void testRegisterInterceptor() throws InterruptedException {
		myVersionChangeListenerRegistry.registerResourceVersionChangeListener("Patient", SearchParameterMap.newSynchronous(), myTestCallback);

		Patient patient = new Patient();
		patient.setActive(true);

		IdDt patientId = createPatientWithLatch(patient);
		assertEquals(myTestCallback.getResourceId().toString(), patientId.toString());
		assertEquals(1L, myTestCallback.getResourceId().getVersionIdPartAsLong());
		assertEquals(myTestCallback.getOperationTypeEnum(), BaseResourceMessage.OperationTypeEnum.CREATE);

		patient.setActive(false);
		patient.setGender(Enumerations.AdministrativeGender.FEMALE);
		myTestCallback.setExpectedCount(1);
		myPatientDao.update(patient);
		myVersionChangeListenerRegistry.refreshAllCachesIfNecessary();
		myTestCallback.awaitExpected();
		assertEquals(2L, myTestCallback.getResourceId().getVersionIdPartAsLong());
		assertEquals(myTestCallback.getOperationTypeEnum(), BaseResourceMessage.OperationTypeEnum.UPDATE);

		myTestCallback.setExpectedCount(1);
		myPatientDao.delete(patientId.toVersionless());
		myVersionChangeListenerRegistry.refreshAllCachesIfNecessary();
		myTestCallback.awaitExpected();
		assertEquals(patientId.toVersionless(), myTestCallback.getResourceId());
		assertEquals(myTestCallback.getOperationTypeEnum(), BaseResourceMessage.OperationTypeEnum.DELETE);
	}

	private IdDt createPatientWithLatch(Patient thePatient) throws InterruptedException {
		myTestCallback.setExpectedCount(1);
		IIdType retval = myPatientDao.create(thePatient).getId();
		myVersionChangeListenerRegistry.refreshAllCachesIfNecessary();
		myTestCallback.awaitExpected();
		return new IdDt(retval);
	}

	@Test
	public void testRegisterPolling() throws InterruptedException {
		myVersionChangeListenerRegistry.registerResourceVersionChangeListener("Patient", SearchParameterMap.newSynchronous(), myTestCallback);

		Patient patient = new Patient();
		patient.setActive(true);
		IdDt patientId = createPatientWithLatch(patient);

		assertEquals(myTestCallback.getResourceId().toString(), patientId.toString());
		assertEquals(1L, myTestCallback.getResourceId().getVersionIdPartAsLong());
		assertEquals(myTestCallback.getOperationTypeEnum(), BaseResourceMessage.OperationTypeEnum.CREATE);

		// Pretend we're on a different process in the cluster and so our cache doesn't have the entry yet
		myVersionChangeListenerRegistry.clearCacheForUnitTest();
		myTestCallback.setExpectedCount(1);
		myVersionChangeListenerRegistry.forceRefresh();
		List<HookParams> calledWith = myTestCallback.awaitExpected();
		IdDt calledWithId  = (IdDt) PointcutLatch.getLatchInvocationParameter(calledWith);
		assertEquals(patientId, calledWithId);
	}

	// FIXME KBD Add 2 more tests (Polling & Interceptor) for a non-empty searchparametermap and confirm listener is only called when matching resources come through.
	// FIXME KBD Add this test for both interceptor and polling cases
	@Test
	public void testRegisterInterceptorFor2Patients() throws InterruptedException {
		myVersionChangeListenerRegistry
			.registerResourceVersionChangeListener("Patient",
				createSearchParameterMap(Enumerations.AdministrativeGender.MALE),
				myTestCallback);

		Patient patientMale = new Patient();
		patientMale.setActive(true);
		patientMale.setGender(Enumerations.AdministrativeGender.MALE);
		IdDt patientIdMale = createPatientWithLatch(patientMale);
		assertEquals(myTestCallback.getResourceId().toString(), patientIdMale.toString());
		assertEquals(1L, myTestCallback.getResourceId().getVersionIdPartAsLong());
		assertEquals(myTestCallback.getOperationTypeEnum(), BaseResourceMessage.OperationTypeEnum.CREATE);

		myTestCallback.clear();

		Patient patientFemale = new Patient();
		patientFemale.setActive(true);
		patientFemale.setGender(Enumerations.AdministrativeGender.FEMALE);
		// NOTE: This scenario does not invoke the myTestCallback listener so just call the DAO directly
		IIdType patientIdFemale = new IdDt(myPatientDao.create(patientFemale).getId());
		assertNotNull(patientIdFemale.toString());
		assertNull(myTestCallback.getResourceId());
		assertNull(myTestCallback.getOperationTypeEnum());
	}

	private IdDt deletePatientWithLatch(Patient thePatient) throws InterruptedException {
		myTestCallback.setExpectedCount(1);
		DaoMethodOutcome retval = myPatientDao.delete(thePatient.getIdElement());
		myVersionChangeListenerRegistry.refreshAllCachesIfNecessary();
		myTestCallback.awaitExpected();
		return new IdDt(retval.getId());
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
		private final PointcutLatch myLatch = new PointcutLatch("VersionChangeListener called");

		private IIdType myResourceId;
		private BaseResourceMessage.OperationTypeEnum myOperationTypeEnum;

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

		private void handle(IIdType theResourceId, BaseResourceMessage.OperationTypeEnum theOperationTypeEnum) {
			ourLog.debug("TestCallback.handle('{}', '{}') called...", theResourceId, theOperationTypeEnum);
			myResourceId = theResourceId;
			myOperationTypeEnum = theOperationTypeEnum;
			myLatch.call(theResourceId);
		}

		@Override
		public void clear() {
			myResourceId = null;
			myOperationTypeEnum = null;
			myLatch.clear();
		}

		@Override
		public void setExpectedCount(int count) {
			myLatch.setExpectedCount(count);
		}

		@Override
		public List<HookParams> awaitExpected() throws InterruptedException {
			return myLatch.awaitExpected();
		}

		public IIdType getResourceId() {
			return myResourceId;
		}

		public BaseResourceMessage.OperationTypeEnum getOperationTypeEnum() {
			return myOperationTypeEnum;
		}
	}
}
