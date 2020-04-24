package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FhirResourceDaoR4ReferentialIntegrityTest extends BaseJpaR4Test {

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@After
	public void afterResetConfig() {
		myDaoConfig.setEnforceReferentialIntegrityOnWrite(new DaoConfig().isEnforceReferentialIntegrityOnWrite());
		myDaoConfig.setEnforceReferentialIntegrityOnDelete(new DaoConfig().isEnforceReferentialIntegrityOnDelete());
	}

	@Test
	public void testCreateUnknownReferenceFail() throws Exception {

		Patient p = new Patient();
		p.setManagingOrganization(new Reference("Organization/AAA"));
		try {
			myPatientDao.create(p);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Resource Organization/AAA not found, specified in path: Patient.managingOrganization", e.getMessage());
		}

	}

	@Test
	public void testCreateUnknownReferenceAllow() throws Exception {
		myDaoConfig.setEnforceReferentialIntegrityOnWrite(false);

		Patient p = new Patient();
		p.setManagingOrganization(new Reference("Organization/AAA"));
		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = myPatientDao.read(id);
		assertEquals("Organization/AAA", p.getManagingOrganization().getReference());

	}

	@Test
	public void testDeleteFail() throws Exception {
		Organization o = new Organization();
		o.setName("FOO");
		IIdType oid = myOrganizationDao.create(o).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.setManagingOrganization(new Reference(oid));
		IIdType pid = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		try {
			myOrganizationDao.delete(oid);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertEquals("Unable to delete Organization/"+oid.getIdPart()+" because at least one resource has a reference to this resource. First reference found was resource Patient/"+pid.getIdPart()+" in path Patient.managingOrganization", e.getMessage());
		}

		myPatientDao.delete(pid);
		myOrganizationDao.delete(oid);

	}

	@Test
	public void testDeleteAllow() throws Exception {
		myDaoConfig.setEnforceReferentialIntegrityOnDelete(false);

		Organization o = new Organization();
		o.setName("FOO");
		IIdType oid = myOrganizationDao.create(o).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.setManagingOrganization(new Reference(oid));
		IIdType pid = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		myOrganizationDao.delete(oid);
		myPatientDao.delete(pid);

	}


}
