package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.empi.interceptor.EmpiInterceptor;
import org.junit.rules.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmpiInterceptorRule extends ExternalResource {

	@Autowired
	private IInterceptorService myIInterceptorService;

	@Autowired
	private EmpiInterceptor myEmpiInterceptor;

	@Override
	protected void before() throws Throwable {
		super.before();
		myEmpiInterceptor.start();
		myIInterceptorService.registerInterceptor(myEmpiInterceptor);
	}

	@Override
	protected void after() {
		myIInterceptorService.unregisterInterceptor(myEmpiInterceptor);
	}
}
