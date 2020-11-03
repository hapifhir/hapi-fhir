package ca.uhn.fhir.jpa.searchparam.cache;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VersionChangeConsumerRegistryTest extends BaseJpaR4Test {
	@Autowired
	IVersionChangeConsumerRegistry myVersionChangeConsumerRegistry;

	IIdType myResourceId;
	BaseResourceMessage.OperationTypeEnum myOperationTypeEnum;

	@BeforeEach
	public void before() {
		myResourceId = null;
		myOperationTypeEnum = null;

		IVersionChangeConsumer callback = (theResourceId, theOperationType) -> {
			myResourceId = theResourceId;
			myOperationTypeEnum = theOperationType;
		};
		myVersionChangeConsumerRegistry.registerResourceVersionChangeConsumer("Patient", SearchParameterMap.newSynchronous(), callback);
	}

	@AfterEach
	public void after() {
		myVersionChangeConsumerRegistry.clearConsumersForUnitTest();
	}

	@Test
	public void testRegisterInterceptor() {
		Patient patient = new Patient();
		patient.setActive(true);
		IIdType patientId = myPatientDao.create(patient).getId();
		assertEquals(myResourceId, patientId);
		assertEquals(1L, myResourceId.getVersionIdPartAsLong());
		assertEquals(myOperationTypeEnum, BaseResourceMessage.OperationTypeEnum.CREATE);

		patient.setActive(false);
		patient.setGender(Enumerations.AdministrativeGender.FEMALE);
		myPatientDao.update(patient);
		assertEquals(2L, myResourceId.getVersionIdPartAsLong());
		assertEquals(myOperationTypeEnum, BaseResourceMessage.OperationTypeEnum.UPDATE);

		myPatientDao.delete(patientId);
		assertEquals(2L, myResourceId.getVersionIdPartAsLong());
		assertEquals(myOperationTypeEnum, BaseResourceMessage.OperationTypeEnum.DELETE);
	}

	@Test
	public void testRegisterPolling() {
		Patient patient = new Patient();
		patient.setActive(true);
		IIdType patientId = myPatientDao.create(patient).getId();
		assertEquals(myResourceId, patientId);
		assertEquals(1L, myResourceId.getVersionIdPartAsLong());
		assertEquals(myOperationTypeEnum, BaseResourceMessage.OperationTypeEnum.CREATE);

		myVersionChangeConsumerRegistry.forceRefresh();
		// FIXME KHS switch to latch and assert latch got called by forced refresh
	}
}
