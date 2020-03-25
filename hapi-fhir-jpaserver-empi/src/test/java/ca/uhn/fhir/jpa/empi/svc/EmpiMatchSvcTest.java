package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class EmpiMatchSvcTest extends BaseEmpiR4Test {
	@Autowired
	private EmpiMatchSvc myEmpiMatchSvc;
	@Autowired
	IEmpiLinkDao myEmpiLinkDao;

	@Test
	public void testAddPatientNoMatchFields() {
		Patient patient1 = new Patient();
		myPatientDao.create(patient1);

		long initialCount = myEmpiLinkDao.count();
		myEmpiMatchSvc.updatePatientLinks(patient1);
		assertEquals(initialCount, myEmpiLinkDao.count());
	}

	@Test
	public void testAddPatientWithMatchFields() {
		Patient patient1 = new Patient();
		patient1.addName().setFamily("Smith");
		myPatientDao.create(patient1);

		long initialCount = myEmpiLinkDao.count();
		myEmpiMatchSvc.updatePatientLinks(patient1);
		assertEquals(initialCount + 1, myEmpiLinkDao.count());
	}

}
