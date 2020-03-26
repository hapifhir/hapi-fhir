package ca.uhn.fhir.jpa.empi.interceptor;

import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class EmpiInterceptorTest extends BaseEmpiR4Test {
	@Autowired
	IEmpiLinkDao myEmpiLinkDao;

	@Test
	public void testCreatePatient() {
		Patient patient = new Patient();

		long initialCount = myEmpiLinkDao.count();
		myPatientDao.create(patient);
		assertEquals(initialCount + 1, myEmpiLinkDao.count());
	}

}
