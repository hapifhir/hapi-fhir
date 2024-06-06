package ca.uhn.fhir.jpa.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.provider.r4.BaseMultitenantResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * <p>Multitenant test version of {@link OverridePathBasedReferentialIntegrityForDeletesInterceptorTest}.</p>
 *
 * <p>Ensures the tenant is properly propagated down to the {@link ca.uhn.fhir.jpa.api.dao.DaoRegistry} when requesting
 * the conflicting resource.</p>
 *
 * <p>This test runs a subset of tests from {@link OverridePathBasedReferentialIntegrityForDeletesInterceptorTest}
 * against the {@link BaseMultitenantResourceProviderR4Test}</p>
 */
public class MultitenantOverridePathBasedReferentialIntegrityForDeletesInterceptorTest extends BaseMultitenantResourceProviderR4Test {

	@Autowired
	private OverridePathBasedReferentialIntegrityForDeletesInterceptor mySvc;

	@Autowired
	private CascadingDeleteInterceptor myCascadingDeleteInterceptor;

	RequestDetails requestDetails = new SystemRequestDetails();

	@BeforeEach
	public void beforeEach() {
		requestDetails.setTenantId(TENANT_A);
	}

	@AfterEach
	public void after() throws Exception {
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.NOT_ALLOWED);
		assertFalse(myPartitionSettings.isAllowUnqualifiedCrossPartitionReference());

		myInterceptorRegistry.unregisterInterceptor(mySvc);
		mySvc.clearPaths();

		super.after();
	}

	@Test
	public void testAllowDelete() {

		mySvc.addPath("AuditEvent.agent.who");
		myInterceptorRegistry.registerInterceptor(mySvc);

		Patient patient = new Patient();
		patient.setId("P");
		patient.setActive(true);
		myPatientDao.update(patient, requestDetails);

		AuditEvent audit = new AuditEvent();
		audit.setId("A");
		audit.addAgent().getWho().setReference("Patient/P");
		myAuditEventDao.update(audit, requestDetails);

		// Delete should proceed
		myPatientDao.delete(new IdType("Patient/P"), requestDetails);

		// Make sure we're deleted
		try {
			myPatientDao.read(new IdType("Patient/P"), requestDetails);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		// Search should still work
		IBundleProvider searchOutcome = myAuditEventDao.search(SearchParameterMap.newSynchronous(AuditEvent.SP_AGENT, new ReferenceParam("Patient/P")), requestDetails);
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
		myPatientDao.update(patient, requestDetails);

		AuditEvent audit = new AuditEvent();
		audit.setId("A");
		audit.addAgent().getWho().setReference("Patient/P");
		myAuditEventDao.update(audit, requestDetails);

		// Delete should proceed
		try {
			myPatientDao.delete(new IdType("Patient/P"), requestDetails);
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
			myPatientDao.update(patient, requestDetails);

			AuditEvent audit = new AuditEvent();
			audit.setId("A");
			audit.addAgent().getWho().setReference("Patient/P");
			myAuditEventDao.update(audit, requestDetails);

			// Delete should proceed
			myPatientDao.delete(new IdType("Patient/P"), requestDetails);

		} finally {
			myInterceptorRegistry.unregisterInterceptor(myCascadingDeleteInterceptor);
		}

	}

}
