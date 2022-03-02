package ca.uhn.fhir.jpa.reindex;

import ca.uhn.fhir.jpa.api.svc.IResourceReindexSvc;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceReindexSvcImplTest extends BaseJpaR4Test {

	@Autowired
	private IResourceReindexSvc mySvc;

	@Test
	public void testGetOldestTimestamp_Resource() {
		// Setup

		createOrganization(withActiveFalse());
		sleepUntilTimeChanges();
		IIdType oldestId = createPatient(withActiveTrue());
		sleepUntilTimeChanges();
		createPatient(withActiveFalse());


		Patient patient = myPatientDao.read(oldestId, mySrd);
		Date expected = patient.getMeta().getLastUpdated();

		// Execute

		Date actual = mySvc.getOldestTimestamp("Patient");

		// Verify
		assertEquals(expected, actual);
	}


	@Test
	public void testGetOldestTimestamp_Global() {
		// Setup

		IIdType oldestId = createOrganization(withActiveFalse());
		sleepUntilTimeChanges();
		createPatient(withActiveTrue());
		sleepUntilTimeChanges();
		createPatient(withActiveFalse());


		Organization org = myOrganizationDao.read(oldestId, mySrd);
		Date expected = org.getMeta().getLastUpdated();

		// Execute

		Date actual = mySvc.getOldestTimestamp(null);

		// Verify
		assertEquals(expected, actual);
	}

	@Test
	public void testGetOldestTimestamp_NoData_Resource() {
		// Setup

		// Execute

		Date actual = mySvc.getOldestTimestamp("Patient");

		// Verify
		assertEquals(null, actual);
	}


	@Test
	public void testGetOldestTimestamp_NoData_Global() {
		// Setup

		// Execute

		Date actual = mySvc.getOldestTimestamp(null);

		// Verify
		assertEquals(null, actual);
	}
}
