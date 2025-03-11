package ca.uhn.fhir.jpa.dao.expunge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.test.concurrency.PointcutLatch;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.fail;


public class ExpungeHookTest extends BaseJpaDstu3Test {
	@Autowired
	private IFhirResourceDaoPatient<Patient> myPatientDao;
	@Autowired
	private ExpungeService myExpungeService;
	@Autowired
	private IInterceptorService myInterceptorService;

	PointcutLatch myEverythingLatch = new PointcutLatch(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_EVERYTHING);
	PointcutLatch myExpungeResourceLatch = new PointcutLatch(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE);

	@BeforeEach
	public void before() {
		myStorageSettings.setExpungeEnabled(true);
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC);
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_EVERYTHING, myEverythingLatch);
		myInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE, myExpungeResourceLatch);
	}

	@AfterEach
	public void after() {
		assertTrue(myInterceptorService.unregisterInterceptor(myEverythingLatch));
		assertTrue(myInterceptorService.unregisterInterceptor(myExpungeResourceLatch));
		myStorageSettings.setExpungeEnabled(new JpaStorageSettings().isExpungeEnabled());
		myStorageSettings.setResourceClientIdStrategy(new JpaStorageSettings().getResourceClientIdStrategy());
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(new JpaStorageSettings().isAutoCreatePlaceholderReferenceTargets());
	}

	@Test
	public void expungeEverythingHook() throws InterruptedException {
		IIdType id = myPatientDao.create(new Patient()).getId();
		assertNotNull(myPatientDao.read(id));

		myEverythingLatch.setExpectedCount(1);
		ExpungeOptions options = new ExpungeOptions();
		options.setExpungeEverything(true);
		myExpungeService.expunge(null, null, options, null);
		myEverythingLatch.awaitExpected();

		assertPatientGone(id);
	}

	@Test
	public void expungeEverythingAndRecreate() throws InterruptedException {
		// Create a patient.
		Patient thePatient = new Patient();
		thePatient.setId("ABC123");
		Meta theMeta = new Meta();
		theMeta.addProfile("http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient");
		thePatient.setMeta(theMeta);

		IIdType id = myPatientDao.update(thePatient, mySrd).getId();
		assertNotNull(myPatientDao.read(id));

		// Expunge it directly.
		myPatientDao.delete(id);
		ExpungeOptions options = new ExpungeOptions();
		options.setExpungeEverything(true);
		options.setExpungeDeletedResources(true);
		options.setExpungeOldVersions(true);
		// TODO KHS shouldn't this be 1?  Investigate why is it 2?
		myExpungeResourceLatch.setExpectedCount(2);
		myPatientDao.expunge(id.toUnqualifiedVersionless(), options, mySrd);
		myExpungeResourceLatch.awaitExpected();
		assertPatientGone(id);

		// Create it a second time.
		myPatientDao.update(thePatient, mySrd);
		assertNotNull(myPatientDao.read(id));

		// Expunge everything with the service.
		myEverythingLatch.setExpectedCount(1);
		myExpungeService.expunge(null, null, options, mySrd);
		myEverythingLatch.awaitExpected();
		assertPatientGone(id);

		// Create it a third time.
		myPatientDao.update(thePatient, mySrd);
		assertNotNull(myPatientDao.read(id));
	}

	private void assertPatientGone(IIdType theId) {
		try {
			myPatientDao.read(theId);
			fail("");
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage()).contains("is not known");
		}
	}

	@Test
	public void expungeResourceHook() throws InterruptedException {
		IIdType expungeId = myPatientDao.create(new Patient()).getId();
		assertNotNull(myPatientDao.read(expungeId));
		myPatientDao.delete(expungeId);

		ExpungeOptions options = new ExpungeOptions();
		options.setExpungeDeletedResources(true);

		myExpungeResourceLatch.setExpectedCount(2);
		myExpungeService.expunge("Patient", JpaPid.fromId(expungeId.getIdPartAsLong()), options, null);
		HookParams hookParams = myExpungeResourceLatch.awaitExpected().get(0);

		IIdType hookId = hookParams.get(IIdType.class);
		assertEquals(expungeId.getValue(), hookId.getValue());
	}
}
