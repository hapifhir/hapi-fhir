package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ConformanceMethodBindingTest {

	private FhirContext fhirContext;
	private ConformanceMethodBinding conformanceMethodBinding;

	@Before
	public void setUp() {
		fhirContext = mock(FhirContext.class);
	}

	private <T> T init(T theCapabilityStatementProvider) throws NoSuchMethodException {
		T provider = spy(theCapabilityStatementProvider);
		Method method = provider.getClass().getDeclaredMethod("getServerConformance", HttpServletRequest.class, RequestDetails.class);
		conformanceMethodBinding = new ConformanceMethodBinding(method, fhirContext, provider);
		return provider;
	}

	@Test
	public void invokeServerCached() throws NoSuchMethodException {
		TestResourceProvider provider = init(new TestResourceProvider());

		conformanceMethodBinding.invokeServer(mock(IRestfulServer.class, RETURNS_DEEP_STUBS), mock(RequestDetails.class, RETURNS_DEEP_STUBS), new Object[]{mock(HttpServletRequest.class), mock(RequestDetails.class)});
		verify(provider, times(1)).getServerConformance(any(), any());
		conformanceMethodBinding.invokeServer(mock(IRestfulServer.class, RETURNS_DEEP_STUBS), mock(RequestDetails.class, RETURNS_DEEP_STUBS), new Object[]{mock(HttpServletRequest.class), mock(RequestDetails.class)});
		verify(provider, times(1)).getServerConformance(any(), any());
	}

	@Test
	public void invokeServerCacheExpires() throws NoSuchMethodException {
		TestResourceProviderSmallCache provider = init(new TestResourceProviderSmallCache());

		conformanceMethodBinding.invokeServer(mock(IRestfulServer.class, RETURNS_DEEP_STUBS), mock(RequestDetails.class, RETURNS_DEEP_STUBS), new Object[]{mock(HttpServletRequest.class), mock(RequestDetails.class)});
		verify(provider, times(1)).getServerConformance(any(), any());

		sleepAtLeast(20);

		conformanceMethodBinding.invokeServer(mock(IRestfulServer.class, RETURNS_DEEP_STUBS), mock(RequestDetails.class, RETURNS_DEEP_STUBS), new Object[]{mock(HttpServletRequest.class), mock(RequestDetails.class)});
		verify(provider, times(2)).getServerConformance(any(), any());
	}

	@Test
	public void invokeServerCacheDisabled() throws NoSuchMethodException {
		TestResourceProviderNoCache provider = init(new TestResourceProviderNoCache());

		conformanceMethodBinding.invokeServer(mock(IRestfulServer.class, RETURNS_DEEP_STUBS), mock(RequestDetails.class, RETURNS_DEEP_STUBS), new Object[]{mock(HttpServletRequest.class), mock(RequestDetails.class)});
		verify(provider, times(1)).getServerConformance(any(), any());

		conformanceMethodBinding.invokeServer(mock(IRestfulServer.class, RETURNS_DEEP_STUBS), mock(RequestDetails.class, RETURNS_DEEP_STUBS), new Object[]{mock(HttpServletRequest.class), mock(RequestDetails.class)});
		verify(provider, times(2)).getServerConformance(any(), any());
	}

	@Test
	public void invokeServerCacheDisabledInSuperclass() throws NoSuchMethodException {
		TestResourceProviderNoCache2 provider = init(new TestResourceProviderNoCache2());

		conformanceMethodBinding.invokeServer(mock(IRestfulServer.class, RETURNS_DEEP_STUBS), mock(RequestDetails.class, RETURNS_DEEP_STUBS), new Object[]{mock(HttpServletRequest.class), mock(RequestDetails.class)});
		verify(provider, times(1)).getServerConformance(any(), any());

		// We currently don't scan the annotation on the superclass...Perhaps we should
		conformanceMethodBinding.invokeServer(mock(IRestfulServer.class, RETURNS_DEEP_STUBS), mock(RequestDetails.class, RETURNS_DEEP_STUBS), new Object[]{mock(HttpServletRequest.class), mock(RequestDetails.class)});
		verify(provider, times(1)).getServerConformance(any(), any());
	}

	@Test
	public void invokeServerNotCached_ClientControlled() throws NoSuchMethodException {
		TestResourceProvider provider = init(new TestResourceProvider());

		RequestDetails requestDetails = mock(RequestDetails.class, RETURNS_DEEP_STUBS);
		when(requestDetails.getHeaders(Constants.HEADER_CACHE_CONTROL)).thenReturn(Lists.newArrayList(Constants.CACHE_CONTROL_NO_CACHE));
		conformanceMethodBinding.invokeServer(mock(IRestfulServer.class, RETURNS_DEEP_STUBS), requestDetails, new Object[]{mock(HttpServletRequest.class), mock(RequestDetails.class)});
		verify(provider, times(1)).getServerConformance(any(), any());
		conformanceMethodBinding.invokeServer(mock(IRestfulServer.class, RETURNS_DEEP_STUBS), requestDetails, new Object[]{mock(HttpServletRequest.class), mock(RequestDetails.class)});
		verify(provider, times(2)).getServerConformance(any(), any());
	}

	@SuppressWarnings("unused")
	static class TestResourceProvider {

		@Metadata
		public IBaseConformance getServerConformance(HttpServletRequest theRequest, RequestDetails theRequestDetails) {

			return mock(IBaseConformance.class, RETURNS_DEEP_STUBS);
		}
	}

	@SuppressWarnings("unused")
	static class TestResourceProviderSmallCache {

		@Metadata(cacheMillis = 10)
		public IBaseConformance getServerConformance(HttpServletRequest theRequest, RequestDetails theRequestDetails) {

			return mock(IBaseConformance.class, RETURNS_DEEP_STUBS);
		}
	}

	@SuppressWarnings("unused")
	static class TestResourceProviderNoCache {

		@Metadata(cacheMillis = 0)
		public IBaseConformance getServerConformance(HttpServletRequest theRequest, RequestDetails theRequestDetails) {

			return mock(IBaseConformance.class, RETURNS_DEEP_STUBS);
		}

	}

	@SuppressWarnings("unused")
	static class TestResourceProviderNoCache2 extends TestResourceProviderNoCache {

		// No @Metadata
		@Override
		public IBaseConformance getServerConformance(HttpServletRequest theRequest, RequestDetails theRequestDetails) {
			return mock(IBaseConformance.class, RETURNS_DEEP_STUBS);
		}

	}

	private static void sleepAtLeast(long theMillis) {
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() <= start + theMillis) {
			try {
				long timeSinceStarted = System.currentTimeMillis() - start;
				long timeToSleep = Math.max(0, theMillis - timeSinceStarted);
				Thread.sleep(timeToSleep);
			} catch (InterruptedException theE) {
				// ignore
			}
		}
	}

}
