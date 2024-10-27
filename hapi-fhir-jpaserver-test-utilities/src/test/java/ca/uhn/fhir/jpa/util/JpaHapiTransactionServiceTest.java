package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	TestR4Config.class
})
public class JpaHapiTransactionServiceTest extends BaseJpaTest {
	@Autowired PlatformTransactionManager myTxManager;
	@Autowired IFhirResourceDao<Patient> myPatientDao;
	@Autowired IFhirResourceDao<Observation> myObservationDao;
	SystemRequestDetails 				myRequestDetails = new SystemRequestDetails();
    final AtomicReference<IIdType> myObservationId = new AtomicReference<>();
	final AtomicReference<IIdType> myPatientId = new AtomicReference<>();

	@Override
	protected FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@Autowired
	HapiTransactionService myHapiTransactionService;

	@Test
	void testNewTransactionCommitInsideOldTransactionRollback() {

        try {
			myHapiTransactionService.withSystemRequest().withPropagation(Propagation.REQUIRED).execute(()->{
				myObservationId.set(myObservationDao.create(new Observation(), myRequestDetails).getId());

				myHapiTransactionService.withSystemRequest().withPropagation(Propagation.REQUIRES_NEW)
					.execute(()-> myPatientId.set(myPatientDao.create(new Patient(), myRequestDetails).getId()));
				// roll back the Observation.  The Patient has committed
				throw new RuntimeException("roll back the Observation.");
			});
		} catch (RuntimeException e) {
			// expected
		}

		assertNotFound(myObservationDao, myObservationId.get());
		assertFound(myPatientDao, myPatientId.get());
	}



	@Test
	void testRequiredTransactionCommitInsideExistingTx_rollsBackWithMainTx() {
		// given

		try {
			myHapiTransactionService.withSystemRequest().withPropagation(Propagation.REQUIRED).execute(()->{
				myObservationId.set(myObservationDao.create(new Observation(), myRequestDetails).getId());

				myHapiTransactionService.withSystemRequest().withPropagation(Propagation.REQUIRED).execute(()-> myPatientId.set(myPatientDao.create(new Patient(), myRequestDetails).getId()));
				throw new RuntimeException("roll back both.");
			});
		} catch (RuntimeException e) {
			// expected
		}

		assertNotFound(myObservationDao, myObservationId.get());
		assertNotFound(myPatientDao, myPatientId.get());
	}

	@Test
	void testTransactionCommitRespectsRollbackOnly() {

		try {
			myHapiTransactionService.withSystemRequest().withPropagation(Propagation.REQUIRED).execute((theTransactionStatus)->{
				myObservationId.set(myObservationDao.create(new Observation(), myRequestDetails).getId());
				theTransactionStatus.setRollbackOnly();
				return null;
			});
		} catch (RuntimeException e) {
			// expected
		}

		assertNotFound(myObservationDao, myObservationId.get());
	}

	void assertNotFound(IFhirResourceDao<?> theDao, IIdType id) {
		assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> theDao.read(id, myRequestDetails));
	}

	void assertFound(IFhirResourceDao<?> theDao, IIdType theId) {
		assertNotNull(theDao.read(theId, myRequestDetails));
	}
}
