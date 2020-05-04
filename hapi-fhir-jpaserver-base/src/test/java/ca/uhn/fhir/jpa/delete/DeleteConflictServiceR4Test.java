package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.*;

public class DeleteConflictServiceR4Test extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(DeleteConflictServiceR4Test.class);

	private DeleteConflictInterceptor myDeleteInterceptor = new DeleteConflictInterceptor();
	private int myInterceptorDeleteCount;

	@Before
	public void beforeRegisterInterceptor() {
		myInterceptorRegistry.registerInterceptor(myDeleteInterceptor);
		myInterceptorDeleteCount = 0;
		myDeleteInterceptor.clear();
	}

	@After
	public void afterUnregisterInterceptor() {
		myInterceptorRegistry.unregisterAllInterceptors();
	}

	@Test
	public void testDeleteFailCallsHook() {
		Organization organization = new Organization();
		organization.setName("FOO");
		IIdType organizationId = myOrganizationDao.create(organization).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.setManagingOrganization(new Reference(organizationId));
		IIdType patientId = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		myDeleteInterceptor.deleteConflictFunction = t -> new DeleteConflictOutcome().setShouldRetryCount(0);
		try {
			myOrganizationDao.delete(organizationId);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertEquals("Unable to delete Organization/" + organizationId.getIdPart() + " because at least one resource has a reference to this resource. First reference found was resource Patient/" + patientId.getIdPart() + " in path Patient.managingOrganization", e.getMessage());
		}
		assertEquals(1, myDeleteInterceptor.myDeleteConflictList.size());
		assertEquals(1, myDeleteInterceptor.myCallCount);
		assertEquals(0, myInterceptorDeleteCount);
		myPatientDao.delete(patientId);
		myOrganizationDao.delete(organizationId);
	}

	@Test
	public void testDeleteHookDeletesConflict() {
		Organization organization = new Organization();
		organization.setName("FOO");
		IIdType organizationId = myOrganizationDao.create(organization).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.setManagingOrganization(new Reference(organizationId));
		myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		myDeleteInterceptor.deleteConflictFunction = this::deleteConflicts;
		myOrganizationDao.delete(organizationId);

		assertNotNull(myDeleteInterceptor.myDeleteConflictList);
		assertEquals(1, myDeleteInterceptor.myCallCount);
		assertEquals(1, myInterceptorDeleteCount);
	}

	@Test
	public void testDeleteHookDeletesTwoConflicts() {
		Organization organization = new Organization();
		organization.setName("FOO");
		IIdType organizationId = myOrganizationDao.create(organization).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.setManagingOrganization(new Reference(organizationId));
		myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		patient = new Patient();
		patient.setManagingOrganization(new Reference(organizationId));
		myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		myDeleteInterceptor.deleteConflictFunction = this::deleteConflicts;
		myOrganizationDao.delete(organizationId);

		assertNotNull(myDeleteInterceptor.myDeleteConflictList);
		assertEquals(2, myDeleteInterceptor.myCallCount);
		assertEquals(2, myInterceptorDeleteCount);
	}

	@Test
	public void testDeleteHookDeletesThreeConflicts() {
		Organization organization = new Organization();
		organization.setName("FOO");
		IIdType organizationId = myOrganizationDao.create(organization).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.setManagingOrganization(new Reference(organizationId));
		myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		patient = new Patient();
		patient.setManagingOrganization(new Reference(organizationId));
		myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		patient = new Patient();
		patient.setManagingOrganization(new Reference(organizationId));
		myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		myDeleteInterceptor.deleteConflictFunction = this::deleteConflicts;
		myOrganizationDao.delete(organizationId);

		assertNotNull(myDeleteInterceptor.myDeleteConflictList);
		assertEquals(2, myDeleteInterceptor.myCallCount);
		assertEquals(3, myInterceptorDeleteCount);
	}

	@Test
	public void testBadInterceptorNoInfiniteLoop() {
		Organization organization = new Organization();
		organization.setName("FOO");
		IIdType organizationId = myOrganizationDao.create(organization).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.setManagingOrganization(new Reference(organizationId));
		IIdType patientId = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		// Always returning true is bad behaviour.  Our infinite loop checker should halt it
		myDeleteInterceptor.deleteConflictFunction = t -> new DeleteConflictOutcome().setShouldRetryCount(Integer.MAX_VALUE);

		try {
			myOrganizationDao.delete(organizationId);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertEquals("Unable to delete Organization/" + organizationId.getIdPart() + " because at least one resource has a reference to this resource. First reference found was resource Patient/" + patientId.getIdPart() + " in path Patient.managingOrganization", e.getMessage());
		}
		assertEquals(1 + DeleteConflictService.MAX_RETRY_ATTEMPTS, myDeleteInterceptor.myCallCount);
	}

	@Test
	public void testNoDuplicateConstraintReferences() {
		Patient patient = new Patient();
		patient.setActive(true);
		IIdType patientId = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		Condition condition = new Condition();
		condition.setSubject(new Reference(patientId));
		condition.setAsserter(new Reference(patientId));
		myConditionDao.create(condition);

		List<DeleteConflict> conflicts = new ArrayList<>();
		myDeleteInterceptor.deleteConflictFunction = t -> {
			for (DeleteConflict next : t) {
				conflicts.add(next);
			}
			return new DeleteConflictOutcome().setShouldRetryCount(0);
		};

		try {
			myPatientDao.delete(patientId);
			fail();
		} catch (ResourceVersionConflictException e) {
			// good
		}

		assertEquals(1, conflicts.size());
	}

	private DeleteConflictOutcome deleteConflicts(DeleteConflictList theList) {
		Iterator<DeleteConflict> iterator = theList.iterator();
		while (iterator.hasNext()) {
			DeleteConflict next = iterator.next();
			IdDt source = next.getSourceId();
			if ("Patient".equals(source.getResourceType())) {
				ourLog.info("Deleting {}", source);
				myPatientDao.delete(source);
				++myInterceptorDeleteCount;
			}
		}
		return new DeleteConflictOutcome().setShouldRetryCount(myInterceptorDeleteCount);
	}

	private static class DeleteConflictInterceptor {
		int myCallCount;
		DeleteConflictList myDeleteConflictList;
		Function<DeleteConflictList, DeleteConflictOutcome> deleteConflictFunction;

		@Hook(Pointcut.STORAGE_PRESTORAGE_DELETE_CONFLICTS)
		public DeleteConflictOutcome deleteConflicts(DeleteConflictList theDeleteConflictList) {
			++myCallCount;
			myDeleteConflictList = theDeleteConflictList;
			return deleteConflictFunction.apply(theDeleteConflictList);
		}

		public void clear() {
			myDeleteConflictList = null;
			myCallCount = 0;
		}
	}

}
