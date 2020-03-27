package ca.uhn.fhir.jpa.empi.interceptor;

import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import ca.uhn.fhir.jpa.empi.svc.EmpiInterceptorRule;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class EmpiInterceptorTest extends BaseEmpiR4Test {
	@Autowired
	IEmpiLinkDao myEmpiLinkDao;

	@Rule
	@Autowired
	public EmpiInterceptorRule myEmpiInterceptorRule;

	@Test
	public void testCreatePatient() throws InterruptedException {
		Patient patient = new Patient();

		long initialCount = myEmpiLinkDao.count();
		myAfterEmpiLatch.setExpectAtLeast(1);
		myPatientDao.create(patient);
		myAfterEmpiLatch.awaitExpected();
		assertEquals(initialCount + 1, myEmpiLinkDao.count());
	}

}
