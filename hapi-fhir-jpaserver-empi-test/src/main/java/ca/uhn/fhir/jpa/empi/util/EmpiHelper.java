package ca.uhn.fhir.jpa.empi.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.IEmpiInterceptor;
import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.test.concurrency.PointcutLatch;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.rules.ExternalResource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.when;

/**
 * How to use this Rule:
 *
 * This rule is to be used whenever you want to have the EmpiInterceptor loaded, and be able
 * to execute creates/updates/deletes while being assured that all EMPI work has been done before exiting.
 * Provides two types of method:
 *
 * 1. doUpdate/doCreate. These methods do not wait for Asynchronous EMPI work to be done. Use these when you are expecting
 * the calls to fail, as those hooks will never be called.
 *
 * 2. createWithLatch/updateWithLatch. These methods will await the EMPI hooks, which are only triggered post-EMPI processing
 * You should use these when you are expecting successful processing of the resource, and need to wait for async EMPI linking
 * work to be done.
 *
 * Note: all create/update functions take an optional isExternalHttpRequest boolean, to make it appear as though the request's
 * origin is an HTTP request.
 */
@Component
public class EmpiHelper extends ExternalResource {
	@Autowired
	private IInterceptorService myInterceptorService;
	@Autowired
	private IEmpiInterceptor myEmpiInterceptor;
	@Autowired
	protected IFhirResourceDao<Patient> myPatientDao;
	@Autowired
	protected IFhirResourceDao<Person> myPersonDao;
	@Mock
	protected ServletRequestDetails myMockSrd;
	@Mock
	protected HttpServletRequest myMockServletRequest;
	@Mock
	protected RestfulServer myMockRestfulServer;
	@Mock
	protected FhirContext myMockFhirContext;

	private PointcutLatch myAfterEmpiLatch = new PointcutLatch(Pointcut.EMPI_AFTER_PERSISTED_RESOURCE_CHECKED);

	@Override
	protected void before() throws Throwable {
		super.before();
		//This sets up mock servlet request details, which allows our DAO requests to appear as though
		//they are coming from an external HTTP Request.
		MockitoAnnotations.initMocks(this);
		when(myMockSrd.getInterceptorBroadcaster()).thenReturn(myInterceptorService);
		when(myMockSrd.getServletRequest()).thenReturn(myMockServletRequest);
		when(myMockSrd.getServer()).thenReturn(myMockRestfulServer);
		when(myMockRestfulServer.getFhirContext()).thenReturn(myMockFhirContext);

		//This sets up our basic interceptor, and also attached the latch so we can await the hook calls.
		myEmpiInterceptor.start();
		myInterceptorService.registerInterceptor(myEmpiInterceptor);
		myInterceptorService.registerAnonymousInterceptor(Pointcut.EMPI_AFTER_PERSISTED_RESOURCE_CHECKED, myAfterEmpiLatch);
	}

	@Override
	protected void after() {
		myInterceptorService.unregisterAllInterceptors();
		myAfterEmpiLatch.clear();
	}

	public DaoMethodOutcome createWithLatch(Patient thePatient, boolean isExternalHttpRequest) throws InterruptedException {
		myAfterEmpiLatch.setExpectedCount(1);
		DaoMethodOutcome daoMethodOutcome = doCreatePatient(thePatient, isExternalHttpRequest);
		myAfterEmpiLatch.awaitExpected();
		return daoMethodOutcome;
	}

	public DaoMethodOutcome createWithLatch(Patient thePatient) throws InterruptedException {
		return createWithLatch(thePatient, true);
	}

	public DaoMethodOutcome createWithLatch(Person thePerson, boolean isExternalHttpRequest) throws InterruptedException {
		myAfterEmpiLatch.setExpectedCount(1);
		DaoMethodOutcome daoMethodOutcome = doCreatePerson(thePerson, isExternalHttpRequest);
		myAfterEmpiLatch.awaitExpected();
		return daoMethodOutcome;
	}

	public DaoMethodOutcome createWithLatch(Person thePerson) throws InterruptedException {
		return createWithLatch(thePerson, true);
	}

	public DaoMethodOutcome updateWithLatch(Person thePerson) throws InterruptedException {
		return updateWithLatch(thePerson, true);
	}
	public DaoMethodOutcome updateWithLatch(Person thePerson, boolean isExternalHttpRequest) throws InterruptedException {
		myAfterEmpiLatch.setExpectedCount(1);
		DaoMethodOutcome daoMethodOutcome =  doUpdatePerson(thePerson, isExternalHttpRequest);
		myAfterEmpiLatch.awaitExpected();
		return daoMethodOutcome;
	}
	public DaoMethodOutcome doCreatePatient(Patient thePatient, boolean isExternalHttpRequest) {
		return isExternalHttpRequest ? myPatientDao.create(thePatient, myMockSrd): myPatientDao.create(thePatient);
	}
	public DaoMethodOutcome doUpdatePatient(Patient thePatient, boolean isExternalHttpRequest) {
		return isExternalHttpRequest ? myPatientDao.update(thePatient, myMockSrd): myPatientDao.update(thePatient);
	}

	public DaoMethodOutcome doCreatePerson(Person thePerson, boolean isExternalHttpRequest) {
		return isExternalHttpRequest ? myPersonDao.create(thePerson, myMockSrd): myPersonDao.create(thePerson);
	}
	public DaoMethodOutcome doUpdatePerson(Person thePerson, boolean isExternalHttpRequest) {
		return isExternalHttpRequest ? myPersonDao.update(thePerson, myMockSrd): myPersonDao.update(thePerson);
	}

}
