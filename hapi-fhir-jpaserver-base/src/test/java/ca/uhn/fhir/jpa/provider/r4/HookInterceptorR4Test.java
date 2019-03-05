package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.model.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HookInterceptorR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(HookInterceptorR4Test.class);

	@Test
	public void testOP_PRESTORAGE_RESOURCE_CREATED_ModifyResource() {
		myInterceptorRegistry.registerAnonymousHookForUnitTest(Pointcut.OP_PRESTORAGE_RESOURCE_CREATED, t->{
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
		myInterceptorRegistry.registerAnonymousHookForUnitTest(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED, t->{
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
	public void testOP_PRESTORAGE_RESOURCE_UPDATED_ModifyResource() {
		Patient p = new Patient();
		p.setActive(true);
		IIdType id = ourClient.create().resource(p).execute().getId();

		myInterceptorRegistry.registerAnonymousHookForUnitTest(Pointcut.OP_PRESTORAGE_RESOURCE_UPDATED, t->{
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

		myInterceptorRegistry.registerAnonymousHookForUnitTest(Pointcut.OP_PRECOMMIT_RESOURCE_UPDATED, t->{
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
