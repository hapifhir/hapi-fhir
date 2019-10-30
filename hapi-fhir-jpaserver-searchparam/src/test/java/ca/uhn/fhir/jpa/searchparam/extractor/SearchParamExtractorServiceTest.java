package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SearchParamExtractorServiceTest {

	private SearchParamExtractorService mySvc;
	@Autowired
	private IInterceptorBroadcaster myRequestInterceptorBroadcaster;
	@Autowired
	private IInterceptorBroadcaster myJpaInterceptorBroadcaster;

	@Before
	public void before() {
		mySvc = new SearchParamExtractorService();
		mySvc.setInterceptorBroadcasterForUnitTest(myJpaInterceptorBroadcaster);
	}

	@Test
	public void testHandleWarnings() {
		ISearchParamExtractor.SearchParamSet<Object> searchParamSet = new ISearchParamExtractor.SearchParamSet<>();
		searchParamSet.addWarning("help i'm a bug");
		searchParamSet.addWarning("Spiff");

		mySvc.handleWarnings(new ServletRequestDetails(myRequestInterceptorBroadcaster), searchParamSet);

		verify(myJpaInterceptorBroadcaster, times(2)).callHooks(eq(Pointcut.JPA_PERFTRACE_WARNING), any());
		verify(myRequestInterceptorBroadcaster, times(2)).callHooks(eq(Pointcut.JPA_PERFTRACE_WARNING), any());
	}

}
