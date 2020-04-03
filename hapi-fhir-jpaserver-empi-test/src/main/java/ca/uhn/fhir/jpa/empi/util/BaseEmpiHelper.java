package ca.uhn.fhir.jpa.empi.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiInterceptor;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.test.concurrency.PointcutLatch;
import org.junit.rules.ExternalResource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.when;

/**
 * How to use this Rule:
 * <p>
 * This rule is to be used whenever you want to have the EmpiInterceptor loaded, and be able
 * to execute creates/updates/deletes while being assured that all EMPI work has been done before exiting.
 * Provides two types of method:
 * <p>
 * 1. doUpdate/doCreate. These methods do not wait for Asynchronous EMPI work to be done. Use these when you are expecting
 * the calls to fail, as those hooks will never be called.
 * <p>
 * 2. createWithLatch/updateWithLatch. These methods will await the EMPI hooks, which are only triggered post-EMPI processing
 * You should use these when you are expecting successful processing of the resource, and need to wait for async EMPI linking
 * work to be done.
 * <p>
 * Note: all create/update functions take an optional isExternalHttpRequest boolean, to make it appear as though the request's
 * origin is an HTTP request.
 */
public abstract class BaseEmpiHelper extends ExternalResource {
	@Autowired
	private IInterceptorService myInterceptorService;
	@Autowired
	private IEmpiInterceptor myEmpiInterceptor;
	@Mock
	protected ServletRequestDetails myMockSrd;
	@Mock
	protected HttpServletRequest myMockServletRequest;
	@Mock
	protected RestfulServer myMockRestfulServer;
	@Mock
	protected FhirContext myMockFhirContext;
	@Mock
	private IInterceptorBroadcaster myMockInterceptorBroadcaster;

	protected PointcutLatch myAfterEmpiLatch = new PointcutLatch(Pointcut.EMPI_AFTER_PERSISTED_RESOURCE_CHECKED);

	@Override
	protected void before() throws Throwable {
		super.before();
		//This sets up mock servlet request details, which allows our DAO requests to appear as though
		//they are coming from an external HTTP Request.
		MockitoAnnotations.initMocks(this);
		when(myMockSrd.getInterceptorBroadcaster()).thenReturn(myMockInterceptorBroadcaster);
		when(myMockSrd.getServletRequest()).thenReturn(myMockServletRequest);
		when(myMockSrd.getServer()).thenReturn(myMockRestfulServer);
		when(myMockRestfulServer.getFhirContext()).thenReturn(myMockFhirContext);

		//This sets up our basic interceptor, and also attached the latch so we can await the hook calls.
		myInterceptorService.registerAnonymousInterceptor(Pointcut.EMPI_AFTER_PERSISTED_RESOURCE_CHECKED, myAfterEmpiLatch);
		myInterceptorService.registerInterceptor(myEmpiInterceptor);
		myEmpiInterceptor.start();
	}

	@Override
	protected void after() {
		myInterceptorService.unregisterInterceptor(myEmpiInterceptor);
		myEmpiInterceptor.stopForUnitTest();
		myInterceptorService.unregisterInterceptor(myAfterEmpiLatch);
		myAfterEmpiLatch.clear();
	}
}
