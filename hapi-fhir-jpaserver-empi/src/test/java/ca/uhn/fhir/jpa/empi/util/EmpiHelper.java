package ca.uhn.fhir.jpa.empi.util;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.empi.interceptor.EmpiInterceptor;
import ca.uhn.test.concurrency.PointcutLatch;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.rules.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmpiHelper extends ExternalResource {

	@Autowired
	private IInterceptorService myIInterceptorService;

	@Autowired
	private EmpiInterceptor myEmpiInterceptor;
	@Autowired
	protected IFhirResourceDao<Patient> myPatientDao;
	@Autowired
	protected IFhirResourceDao<Person> myPersonDao;

	private PointcutLatch myAfterEmpiLatch = new PointcutLatch(Pointcut.EMPI_AFTER_PERSISTED_RESOURCE_CHECKED);

	@Override
	protected void before() throws Throwable {
		super.before();
		myEmpiInterceptor.start();
		myIInterceptorService.registerInterceptor(myEmpiInterceptor);
		myIInterceptorService.registerAnonymousInterceptor(Pointcut.EMPI_AFTER_PERSISTED_RESOURCE_CHECKED, myAfterEmpiLatch);
	}

	@Override
	protected void after() {
		myIInterceptorService.unregisterInterceptor(myEmpiInterceptor);
		//FIXME EMPI how do i unregister an anonymous interceptor??
	}

	public void createWithLatch(Patient thePatient) throws InterruptedException {
		myAfterEmpiLatch.setExpectAtLeast(1);
		myPatientDao.create(thePatient);
		myAfterEmpiLatch.awaitExpected();
	}

	public void createWithLatch(Person thePerson) throws InterruptedException {
		myAfterEmpiLatch.setExpectAtLeast(1);
		myPersonDao.create(thePerson);
		myAfterEmpiLatch.awaitExpected();
	}

}
