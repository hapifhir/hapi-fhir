package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.api.IEmpiMatchSvc;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class EmpiMatchSvcImplTest extends BaseEmpiR4Test {
	@Autowired
	private IEmpiMatchSvc myEmpiMatchSvc;
	@Autowired
	IEmpiLinkDao myEmpiLinkDao;

	@Test
	public void testAddPatient() {
		Patient patient1 = new Patient();
		patient1.addName().setFamily("Smith");
		myPatientDao.create(patient1);

		long initialCount = myEmpiLinkDao.count();
		myEmpiMatchSvc.updatePatientLinks(myPatient);
		assertEquals(initialCount + 1, myEmpiLinkDao.count());
	}
}
