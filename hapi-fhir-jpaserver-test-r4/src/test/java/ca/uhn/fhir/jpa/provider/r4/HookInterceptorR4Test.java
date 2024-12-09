package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ResolveIdentityMode;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HookInterceptorR4Test extends BaseResourceProviderR4Test {

	@Autowired
	IIdHelperService<JpaPid> myIdHelperService;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myStorageSettings.setExpungeEnabled(true);
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		myStorageSettings.setExpungeEnabled(new JpaStorageSettings().isExpungeEnabled());
		myInterceptorRegistry.unregisterAllAnonymousInterceptors();
		super.after();
	}

	@Test
	public void testOP_PRESTORAGE_RESOURCE_CREATED_ModifyResource() {
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, (thePointcut, t) -> {
			Patient contents = (Patient) t.get(IBaseResource.class, 0);
			contents.getNameFirstRep().setFamily("NEWFAMILY");
		});

		Patient p = new Patient();
		p.getNameFirstRep().setFamily("OLDFAMILY");
		MethodOutcome outcome = myClient.create().resource(p).execute();

		// Response reflects change, stored resource also does
		Patient responsePatient = (Patient) outcome.getResource();
		assertEquals("NEWFAMILY", responsePatient.getNameFirstRep().getFamily());
		responsePatient = myClient.read().resource(Patient.class).withId(outcome.getId()).execute();
		assertEquals("NEWFAMILY", responsePatient.getNameFirstRep().getFamily());

	}

	@Test
	public void testOP_PRECOMMIT_RESOURCE_CREATED_ModifyResource() {
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED, (thePointcut, t) -> {
			Patient contents = (Patient) t.get(IBaseResource.class, 0);
			contents.getNameFirstRep().setFamily("NEWFAMILY");
		});

		Patient p = new Patient();
		p.getNameFirstRep().setFamily("OLDFAMILY");
		MethodOutcome outcome = myClient.create().resource(p).execute();

		// Response reflects change, stored resource does not
		Patient responsePatient = (Patient) outcome.getResource();
		assertEquals("NEWFAMILY", responsePatient.getNameFirstRep().getFamily());
		responsePatient = myClient.read().resource(Patient.class).withId(outcome.getId()).execute();
		assertEquals("OLDFAMILY", responsePatient.getNameFirstRep().getFamily());

	}

	@Test
	public void testSTORAGE_PRECOMMIT_RESOURCE_CREATED_hasPid() {
		AtomicLong pid = new AtomicLong();
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED, (thePointcut, t) -> {
			IAnyResource resource = (IAnyResource) t.get(IBaseResource.class, 0);
			JpaPid resourcePid = (JpaPid) resource.getUserData(IDao.RESOURCE_PID_KEY);
			assertNotNull(resourcePid, "Expecting RESOURCE_PID to be set on resource user data.");
			pid.set(resourcePid.getId());
		});
		myClient.create().resource(new Patient()).execute();
		assertTrue(pid.get() > 0);
	}


	@Test
	public void testSTORAGE_PRECOMMIT_RESOURCE_CREATED_hasCorrectPid() {
		AtomicLong pid = new AtomicLong();
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED, (thePointcut, t) -> {
			IAnyResource resource = (IAnyResource) t.get(IBaseResource.class, 0);
			JpaPid resourcePid = (JpaPid) resource.getUserData(IDao.RESOURCE_PID_KEY);
			assertNotNull(resourcePid, "Expecting RESOURCE_PID to be set on resource user data.");
			pid.set(resourcePid.getId());
		});
		IIdType savedPatientId = myClient.create().resource(new Patient()).execute().getId();

		runInTransaction(() -> {
			List<JpaPid> pids = myIdHelperService.resolveResourcePids(RequestPartitionId.allPartitions(),
				Collections.singletonList(savedPatientId), ResolveIdentityMode.includeDeleted().cacheOk());
			Long savedPatientPid = pids.get(0).getId();
			assertEquals(savedPatientPid.longValue(), pid.get());
		});
	}

	@Test
	public void testSTORAGE_PRESTORAGE_EXPUNGE_RESOURCE_hasCorrectPid() {
		AtomicLong pid = new AtomicLong();
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE, (thePointcut, t) -> {
			IAnyResource resource = (IAnyResource) t.get(IBaseResource.class, 0);
			JpaPid resourcePid = (JpaPid) resource.getUserData(IDao.RESOURCE_PID_KEY);
			assertNotNull(resourcePid, "Expecting RESOURCE_PID to be set on resource user data.");
			pid.set(resourcePid.getId());
		});
		IIdType savedPatientId = myClient.create().resource(new Patient()).execute().getId();
		Long savedPatientPid = runInTransaction(() ->
			myIdHelperService.resolveResourceIdentityPid(RequestPartitionId.allPartitions(), savedPatientId.getResourceType(), savedPatientId.getIdPart(), ResolveIdentityMode.includeDeleted().cacheOk()).getId()
		);

		myClient.delete().resourceById(savedPatientId).execute();
		Parameters parameters = new Parameters();

		parameters.addParameter().setName(ProviderConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_DELETED_RESOURCES).setValue(new BooleanType(true));
		myClient
			.operation()
			.onInstance(savedPatientId)
			.named(ProviderConstants.OPERATION_EXPUNGE)
			.withParameters(parameters)
			.execute();

		assertEquals(savedPatientPid.longValue(), pid.get());
	}


	@Test
	public void testSTORAGE_PRECOMMIT_RESOURCE_UPDATED_hasCorrectPid() {
		AtomicLong pidOld = new AtomicLong();
		AtomicLong pidNew = new AtomicLong();
		Patient patient = new Patient();
		IIdType savedPatientId = myClient.create().resource(patient).execute().getId();
		patient.setId(savedPatientId);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, (thePointcut, t) -> {
			IAnyResource resourceOld = (IAnyResource) t.get(IBaseResource.class, 0);
			IAnyResource resourceNew = (IAnyResource) t.get(IBaseResource.class, 1);
			JpaPid resourceOldPid = (JpaPid) resourceOld.getUserData(IDao.RESOURCE_PID_KEY);
			JpaPid resourceNewPid = (JpaPid) resourceNew.getUserData(IDao.RESOURCE_PID_KEY);
			assertNotNull(resourceOldPid, "Expecting RESOURCE_PID to be set on resource user data.");
			assertNotNull(resourceNewPid, "Expecting RESOURCE_PID to be set on resource user data.");
			pidOld.set(resourceOldPid.getId());
			pidNew.set(resourceNewPid.getId());
		});
		patient.setActive(true);
		myClient.update().resource(patient).execute();
		runInTransaction(() -> {
			Long savedPatientPid = runInTransaction(() ->
				myIdHelperService.resolveResourceIdentityPid(RequestPartitionId.allPartitions(), savedPatientId.getResourceType(), savedPatientId.getIdPart(), ResolveIdentityMode.includeDeleted().cacheOk()).getId()
			);
			assertEquals(savedPatientPid.longValue(), pidOld.get());
			assertEquals(savedPatientPid.longValue(), pidNew.get());
		});
	}

	@Test
	public void testSTORAGE_PRECOMMIT_RESOURCE_UPDATED_hasPid() {
		AtomicLong oldPid = new AtomicLong();
		AtomicLong newPid = new AtomicLong();
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, (thePointcut, t) -> {

			IAnyResource oldResource = (IAnyResource) t.get(IBaseResource.class, 0);
			JpaPid oldResourcePid = (JpaPid) oldResource.getUserData(IDao.RESOURCE_PID_KEY);
			assertNotNull(oldResourcePid, "Expecting RESOURCE_PID to be set on resource user data.");
			oldPid.set(oldResourcePid.getId());

			IAnyResource newResource = (IAnyResource) t.get(IBaseResource.class, 1);
			JpaPid newResourcePid = (JpaPid) newResource.getUserData(IDao.RESOURCE_PID_KEY);
			assertNotNull(newResourcePid, "Expecting RESOURCE_PID to be set on resource user data.");
			newPid.set(newResourcePid.getId());
		});
		Patient patient = new Patient();
		IIdType id = myClient.create().resource(patient).execute().getId();
		patient.setId(id);
		patient.getNameFirstRep().setFamily("SOMECHANGE");
		myClient.update().resource(patient).execute();
		assertTrue(oldPid.get() > 0);
		assertTrue(newPid.get() > 0);
	}

	@Test
	public void testOP_PRESTORAGE_RESOURCE_UPDATED_ModifyResource() {
		Patient p = new Patient();
		p.setActive(true);
		IIdType id = myClient.create().resource(p).execute().getId();

		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, (thePointcut, t) -> {
			Patient contents = (Patient) t.get(IBaseResource.class, 1);
			contents.getNameFirstRep().setFamily("NEWFAMILY");
		});

		p = new Patient();
		p.setId(id);
		p.getNameFirstRep().setFamily("OLDFAMILY");
		MethodOutcome outcome = myClient.update().resource(p).execute();

		// Response reflects change, stored resource also does
		Patient responsePatient = (Patient) outcome.getResource();
		assertEquals("NEWFAMILY", responsePatient.getNameFirstRep().getFamily());
		responsePatient = myClient.read().resource(Patient.class).withId(outcome.getId()).execute();
		assertEquals("NEWFAMILY", responsePatient.getNameFirstRep().getFamily());

	}

	@Test
	public void testOP_PRECOMMIT_RESOURCE_UPDATED_ModifyResource() {
		Patient p = new Patient();
		p.setActive(true);
		IIdType id = myClient.create().resource(p).execute().getId();

		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, (thePointcut, t) -> {
			Patient contents = (Patient) t.get(IBaseResource.class, 1);
			contents.getNameFirstRep().setFamily("NEWFAMILY");
		});

		p = new Patient();
		p.setId(id);
		p.getNameFirstRep().setFamily("OLDFAMILY");
		MethodOutcome outcome = myClient.update().resource(p).execute();

		// Response reflects change, stored resource does not
		Patient responsePatient = (Patient) outcome.getResource();
		assertEquals("NEWFAMILY", responsePatient.getNameFirstRep().getFamily());
		responsePatient = myClient.read().resource(Patient.class).withId(outcome.getId()).execute();
		assertEquals("OLDFAMILY", responsePatient.getNameFirstRep().getFamily());

	}


}
