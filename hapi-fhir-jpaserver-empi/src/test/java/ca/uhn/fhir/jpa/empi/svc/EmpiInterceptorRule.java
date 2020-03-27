package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.empi.interceptor.EmpiInterceptor;
import ca.uhn.test.concurrency.PointcutLatch;
import org.junit.rules.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmpiInterceptorRule extends ExternalResource {

	@Autowired
	private IInterceptorService myIInterceptorService;

	@Autowired
	private EmpiInterceptor myEmpiInterceptor;

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
		//@FIXME EMPI how do i unregister an anonymous interceptor??
	}

	public PointcutLatch getAfterEmpiLatch() {
		return myAfterEmpiLatch;
	}
}
