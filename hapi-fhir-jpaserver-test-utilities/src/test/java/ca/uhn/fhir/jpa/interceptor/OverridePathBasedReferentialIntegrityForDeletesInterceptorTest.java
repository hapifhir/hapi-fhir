package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class OverridePathBasedReferentialIntegrityForDeletesInterceptorTest extends BaseJpaR4Test {

	@Autowired
	private OverridePathBasedReferentialIntegrityForDeletesInterceptor mySvc;

	@Autowired
	private CascadingDeleteInterceptor myCascadingDeleteInterceptor;

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterInterceptor(mySvc);
		mySvc.clearPaths();
	}

	@Test
	public void testDeleteBlockedIfNoInterceptorInPlace() {
		Patient patient = new Patient();
		patient.setId("P");
		patient.setActive(true);
		myPatientDao.update(patient);

		AuditEvent audit = new AuditEvent();
		audit.setId("A");
		audit.addAgent().getWho().setReference("Patient/P");
		myAuditEventDao.update(audit);

		try {
			myPatientDao.delete(new IdType("Patient/P"));
			fail();
		} catch (ResourceVersionConflictException e) {
			// good
		}
	}


	@Test
	public void testAllowDelete() {
		mySvc.addPath("AuditEvent.agent.who");
		myInterceptorRegistry.registerInterceptor(mySvc);

		Patient patient = new Patient();
		patient.setId("P");
		patient.setActive(true);
		myPatientDao.update(patient);

		AuditEvent audit = new AuditEvent();
		audit.setId("A");
		audit.addAgent().getWho().setReference("Patient/P");
		myAuditEventDao.update(audit);

		// Delete should proceed
		myPatientDao.delete(new IdType("Patient/P"));

		// Make sure we're deleted
		try {
			myPatientDao.read(new IdType("Patient/P"));
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		// Search should still work
		IBundleProvider searchOutcome = myAuditEventDao.search(SearchParameterMap.newSynchronous(AuditEvent.SP_AGENT, new ReferenceParam("Patient/P")));
		assertEquals(1, searchOutcome.size());


	}

	@Test
	public void testWrongPath() {
		mySvc.addPath("AuditEvent.identifier");
		mySvc.addPath("Patient.agent.who");
		myInterceptorRegistry.registerInterceptor(mySvc);

		Patient patient = new Patient();
		patient.setId("P");
		patient.setActive(true);
		myPatientDao.update(patient);

		AuditEvent audit = new AuditEvent();
		audit.setId("A");
		audit.addAgent().getWho().setReference("Patient/P");
		myAuditEventDao.update(audit);

		// Delete should proceed
		try {
			myPatientDao.delete(new IdType("Patient/P"));
			fail();
		} catch (ResourceVersionConflictException e) {
			// good
		}


	}

	@Test
	public void testCombineWithCascadeDeleteInterceptor() {
		try {
			myInterceptorRegistry.registerInterceptor(myCascadingDeleteInterceptor);

			mySvc.addPath("AuditEvent.agent.who");
			myInterceptorRegistry.registerInterceptor(mySvc);

			Patient patient = new Patient();
			patient.setId("P");
			patient.setActive(true);
			myPatientDao.update(patient);

			AuditEvent audit = new AuditEvent();
			audit.setId("A");
			audit.addAgent().getWho().setReference("Patient/P");
			myAuditEventDao.update(audit);

			// Delete should proceed
			myPatientDao.delete(new IdType("Patient/P"));

		} finally {
			myInterceptorRegistry.unregisterInterceptor(myCascadingDeleteInterceptor);
		}

	}

}
