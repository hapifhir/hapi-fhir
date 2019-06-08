package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.*;

public class DeleteConflictServiceR4Test extends BaseJpaR4Test {


	private DeleteConflictInterceptor myDeleteInterceptor = new DeleteConflictInterceptor();

	@Before
	public void beforeRegisterInterceptor() {
		myInterceptorRegistry.registerInterceptor(myDeleteInterceptor);
	}

	@After
	public void afterUnregisterInterceptor() {
		myDeleteInterceptor.clear();
		myInterceptorRegistry.unregisterAllInterceptors();
	}

	@Test
	public void testDeleteFailCallsHook() throws Exception {
		Organization o = new Organization();
		o.setName("FOO");
		IIdType oid = myOrganizationDao.create(o).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.setManagingOrganization(new Reference(oid));
		IIdType pid = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		myDeleteInterceptor.deleteConflictFunction = list -> false;
		try {
			myOrganizationDao.delete(oid);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertEquals("Unable to delete Organization/" + oid.getIdPart() + " because at least one resource has a reference to this resource. First reference found was resource Patient/" + pid.getIdPart() + " in path Patient.managingOrganization", e.getMessage());
		}
		assertNotNull(myDeleteInterceptor.myDeleteConflictList);

		myPatientDao.delete(pid);
		myOrganizationDao.delete(oid);
	}

	private static class DeleteConflictInterceptor {
		DeleteConflictList myDeleteConflictList;
		Function<DeleteConflictList, Boolean> deleteConflictFunction;

		@Hook(Pointcut.STORAGE_PRESTORAGE_DELETE_CONFLICTS)
		public boolean deleteConflicts(DeleteConflictList theDeleteConflictList) {
			myDeleteConflictList = theDeleteConflictList;
			return deleteConflictFunction.apply(theDeleteConflictList);
		}

		public void clear() {
			myDeleteConflictList = null;
		}
	}

}
