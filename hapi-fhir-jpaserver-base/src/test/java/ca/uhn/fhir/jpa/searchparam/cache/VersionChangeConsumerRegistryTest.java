package ca.uhn.fhir.jpa.searchparam.cache;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.cache.IVersionChangeConsumer;
import ca.uhn.fhir.jpa.cache.IVersionChangeConsumerRegistry;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import ca.uhn.test.concurrency.IPointcutLatch;
import ca.uhn.test.concurrency.PointcutLatch;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VersionChangeConsumerRegistryTest extends BaseJpaR4Test {
	@Autowired
	IVersionChangeConsumerRegistry myVersionChangeConsumerRegistry;

	private final TestCallback myTestCallback = new TestCallback();

	@BeforeEach
	public void before() {
		myTestCallback.clear();
		myVersionChangeConsumerRegistry.registerResourceVersionChangeConsumer("Patient", SearchParameterMap.newSynchronous(), myTestCallback);
	}

	@AfterEach
	public void after() {
		myVersionChangeConsumerRegistry.clearConsumersForUnitTest();
	}

	@Test
	public void testRegisterInterceptor() throws InterruptedException {
		Patient patient = new Patient();
		patient.setActive(true);

		IIdType patientId = createPatientWithLatch(patient);
		assertEquals(myTestCallback.getResourceId().toString(), patientId.toString());
		assertEquals(1L, myTestCallback.getResourceId().getVersionIdPartAsLong());
		assertEquals(myTestCallback.getOperationTypeEnum(), BaseResourceMessage.OperationTypeEnum.CREATE);

		patient.setActive(false);
		patient.setGender(Enumerations.AdministrativeGender.FEMALE);
		myTestCallback.setExpectedCount(1);
		myPatientDao.update(patient);
		myVersionChangeConsumerRegistry.refreshAllCachesIfNecessary();
		myTestCallback.awaitExpected();
		assertEquals(2L, myTestCallback.getResourceId().getVersionIdPartAsLong());
		assertEquals(myTestCallback.getOperationTypeEnum(), BaseResourceMessage.OperationTypeEnum.UPDATE);

		myTestCallback.setExpectedCount(1);
		myPatientDao.delete(patientId.toVersionless());
		myTestCallback.awaitExpected();
		myVersionChangeConsumerRegistry.refreshAllCachesIfNecessary();
		assertEquals(2L, myTestCallback.getResourceId().getVersionIdPartAsLong());
		assertEquals(myTestCallback.getOperationTypeEnum(), BaseResourceMessage.OperationTypeEnum.DELETE);
	}

	private IIdType createPatientWithLatch(Patient thePatient) throws InterruptedException {
		myTestCallback.setExpectedCount(1);
		IIdType retval = myPatientDao.create(thePatient).getId();
		myVersionChangeConsumerRegistry.refreshAllCachesIfNecessary();
		myTestCallback.awaitExpected();
		return retval;
	}

	@Test
	public void testRegisterPolling() throws InterruptedException {
		Patient patient = new Patient();
		patient.setActive(true);
		IIdType patientId = createPatientWithLatch(patient);

		assertEquals(myTestCallback.getResourceId().toString(), patientId.toString());
		assertEquals(1L, myTestCallback.getResourceId().getVersionIdPartAsLong());
		assertEquals(myTestCallback.getOperationTypeEnum(), BaseResourceMessage.OperationTypeEnum.CREATE);

		myTestCallback.setExpectedCount(1);
		myVersionChangeConsumerRegistry.forceRefresh();
		List<HookParams> calledWith = myTestCallback.awaitExpected();
		IIdType calledWithId  = (IIdType) PointcutLatch.getLatchInvocationParameter(calledWith);
		assertEquals(patientId, calledWithId);
	}

	private static class TestCallback implements IVersionChangeConsumer, IPointcutLatch {
		private final PointcutLatch myLatch = new PointcutLatch("VersionChangeConsumer called");

		private IIdType myResourceId;
		private BaseResourceMessage.OperationTypeEnum myOperationTypeEnum;

		@Override
		public void accept(IIdType theResourceId, BaseResourceMessage.OperationTypeEnum theOperationType) {
			myResourceId = theResourceId;
			myOperationTypeEnum = theOperationType;
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
