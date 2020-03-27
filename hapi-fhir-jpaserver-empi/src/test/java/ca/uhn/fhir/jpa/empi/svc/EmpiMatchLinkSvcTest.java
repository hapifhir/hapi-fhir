package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class EmpiMatchLinkSvcTest extends BaseEmpiR4Test {
	@Autowired
	private EmpiMatchLinkSvc myEmpiMatchLinkSvc;
	@Autowired
	IEmpiLinkDao myEmpiLinkDao;

	@Test
	public void testAddPatientLinksToNewPersonIfNoneFound() {
		Patient patient = new Patient();
		myPatientDao.create(patient);

		long initialCount = myEmpiLinkDao.count();
		myEmpiMatchLinkSvc.updateEmpiLinksForPatient(patient);
		assertEquals(initialCount + 1, myEmpiLinkDao.count());
	}
}
