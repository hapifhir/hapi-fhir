package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.MockInvoker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchParamExtractorServiceTest {

	private SearchParamExtractorService mySvc;
	@Mock
	private IInterceptorBroadcaster myRequestInterceptorBroadcaster;
	@Mock
	private IInterceptorBroadcaster myJpaInterceptorBroadcaster;

	@BeforeEach
	public void before() {
		mySvc = new SearchParamExtractorService();
		mySvc.setInterceptorBroadcasterForUnitTest(myJpaInterceptorBroadcaster);
	}

	@Test
	public void testHandleWarnings() {
		ISearchParamExtractor.SearchParamSet<Object> searchParamSet = new ISearchParamExtractor.SearchParamSet<>();
		searchParamSet.addWarning("help i'm a bug");
		searchParamSet.addWarning("Spiff");

		AtomicInteger counter = new AtomicInteger();

		when(myJpaInterceptorBroadcaster.hasHooks(eq(Pointcut.JPA_PERFTRACE_WARNING))).thenReturn(true);
		when(myJpaInterceptorBroadcaster.getInvokersForPointcut(eq(Pointcut.JPA_PERFTRACE_WARNING))).thenReturn(MockInvoker.list((Consumer<HookParams>) params->counter.incrementAndGet()));

		ServletRequestDetails requestDetails = new ServletRequestDetails(myRequestInterceptorBroadcaster);
		SearchParamExtractorService.handleWarnings(requestDetails, myJpaInterceptorBroadcaster, searchParamSet);

		verify(myJpaInterceptorBroadcaster, times(3)).hasHooks(eq(Pointcut.JPA_PERFTRACE_WARNING));
		verify(myRequestInterceptorBroadcaster, times(2)).hasHooks(eq(Pointcut.JPA_PERFTRACE_WARNING));
		assertEquals(2, counter.get());
	}

}
