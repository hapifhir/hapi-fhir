package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

public class HookInterceptorR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(HookInterceptorR4Test.class);

//	@Override
//	@After
//	public void after( ) throws Exception {
//		super.after();
//
//		myInterceptorRegistry.unregisterAllInterceptors();
//	}

	@Test
	public void testOP_PRESTORAGE_RESOURCE_CREATED_ModifyResource() {
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, (thePointcut, t) -> {
			Patient contents = (Patient) t.get(IBaseResource.class, 0);
			contents.getNameFirstRep().setFamily("NEWFAMILY");
		});

		Patient p = new Patient();
		p.getNameFirstRep().setFamily("OLDFAMILY");
		MethodOutcome outcome = ourClient.create().resource(p).execute();

		// Response reflects change, stored resource also does
		Patient responsePatient = (Patient) outcome.getResource();
		assertEquals("NEWFAMILY", responsePatient.getNameFirstRep().getFamily());
		responsePatient = ourClient.read().resource(Patient.class).withId(outcome.getId()).execute();
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
		MethodOutcome outcome = ourClient.create().resource(p).execute();

		// Response reflects change, stored resource does not
		Patient responsePatient = (Patient) outcome.getResource();
		assertEquals("NEWFAMILY", responsePatient.getNameFirstRep().getFamily());
		responsePatient = ourClient.read().resource(Patient.class).withId(outcome.getId()).execute();
		assertEquals("OLDFAMILY", responsePatient.getNameFirstRep().getFamily());

	}

	@Test
	public void testSTORAGE_PRECOMMIT_RESOURCE_CREATED_hasPid() {
		AtomicLong pid = new AtomicLong();
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED, (thePointcut, t) -> {
			IAnyResource resource = (IAnyResource) t.get(IBaseResource.class, 0);
			Long resourcePid = (Long) resource.getUserData("RESOURCE_PID");
			assertNotNull("Expecting RESOURCE_PID to be set on resource user data.", resourcePid);
			pid.set(resourcePid);
		});
		ourClient.create().resource(new Patient()).execute();
		assertTrue(pid.get() > 0);
	}

	@Test
	public void testSTORAGE_PRECOMMIT_RESOURCE_UPDATED_hasPid() {
		AtomicLong oldPid = new AtomicLong();
		AtomicLong newPid = new AtomicLong();
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, (thePointcut, t) -> {

			IAnyResource oldResource = (IAnyResource) t.get(IBaseResource.class, 0);
			Long oldResourcePid = (Long) oldResource.getUserData("RESOURCE_PID");
			assertNotNull("Expecting RESOURCE_PID to be set on resource user data.", oldResourcePid);
			oldPid.set(oldResourcePid);

			IAnyResource newResource = (IAnyResource) t.get(IBaseResource.class, 1);
			Long newResourcePid = (Long) newResource.getUserData("RESOURCE_PID");
			assertNotNull("Expecting RESOURCE_PID to be set on resource user data.", newResourcePid);
			newPid.set(newResourcePid);
		});
		Patient patient = new Patient();
		IIdType id = ourClient.create().resource(patient).execute().getId();
		patient.setId(id);
		patient.getNameFirstRep().setFamily("SOMECHANGE");
		ourClient.update().resource(patient).execute();
		assertTrue(oldPid.get() > 0);
		assertTrue(newPid.get() > 0);
	}

	@Test
	public void testOP_PRESTORAGE_RESOURCE_UPDATED_ModifyResource() {
		Patient p = new Patient();
		p.setActive(true);
		IIdType id = ourClient.create().resource(p).execute().getId();

		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, (thePointcut, t) -> {
			Patient contents = (Patient) t.get(IBaseResource.class, 1);
			contents.getNameFirstRep().setFamily("NEWFAMILY");
		});

		p = new Patient();
		p.setId(id);
		p.getNameFirstRep().setFamily("OLDFAMILY");
		MethodOutcome outcome = ourClient.update().resource(p).execute();

		// Response reflects change, stored resource also does
		Patient responsePatient = (Patient) outcome.getResource();
		assertEquals("NEWFAMILY", responsePatient.getNameFirstRep().getFamily());
		responsePatient = ourClient.read().resource(Patient.class).withId(outcome.getId()).execute();
		assertEquals("NEWFAMILY", responsePatient.getNameFirstRep().getFamily());

	}

	@Test
	public void testOP_PRECOMMIT_RESOURCE_UPDATED_ModifyResource() {
		Patient p = new Patient();
		p.setActive(true);
		IIdType id = ourClient.create().resource(p).execute().getId();

		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, (thePointcut, t) -> {
			Patient contents = (Patient) t.get(IBaseResource.class, 1);
			contents.getNameFirstRep().setFamily("NEWFAMILY");
		});

		p = new Patient();
		p.setId(id);
		p.getNameFirstRep().setFamily("OLDFAMILY");
		MethodOutcome outcome = ourClient.update().resource(p).execute();

		// Response reflects change, stored resource does not
		Patient responsePatient = (Patient) outcome.getResource();
		assertEquals("NEWFAMILY", responsePatient.getNameFirstRep().getFamily());
		responsePatient = ourClient.read().resource(Patient.class).withId(outcome.getId()).execute();
		assertEquals("OLDFAMILY", responsePatient.getNameFirstRep().getFamily());

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


}
