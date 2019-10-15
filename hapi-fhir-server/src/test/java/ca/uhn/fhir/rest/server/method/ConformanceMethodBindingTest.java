package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.util.BaseServerCapabilityStatementProvider;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ConformanceMethodBindingTest {

	private FhirContext fhirContext;
	private ConformanceMethodBinding conformanceMethodBinding;
	private TestResourceProvider dummy;

	@Before
	public void setUp() throws NoSuchMethodException {
		fhirContext = mock(FhirContext.class);
		dummy = spy(new TestResourceProvider());
		Method method = dummy.getClass().getDeclaredMethod("getServerConformance", HttpServletRequest.class, RequestDetails.class);
		conformanceMethodBinding = new ConformanceMethodBinding(method, fhirContext, dummy);

	}

	@Test
	public void invokeServerCached() {
		conformanceMethodBinding.invokeServer(mock(IRestfulServer.class, RETURNS_DEEP_STUBS), mock(RequestDetails.class, RETURNS_DEEP_STUBS), new Object[]{mock(HttpServletRequest.class), mock(RequestDetails.class)});
		verify(dummy, times(1)).getServerConformance(any(), any());
		conformanceMethodBinding.invokeServer(mock(IRestfulServer.class, RETURNS_DEEP_STUBS), mock(RequestDetails.class, RETURNS_DEEP_STUBS), new Object[]{mock(HttpServletRequest.class), mock(RequestDetails.class)});
		verify(dummy, times(1)).getServerConformance(any(), any());
	}

	@Test
	public void invokeServerNotCached_ClientControlled() {
		RequestDetails requestDetails = mock(RequestDetails.class, RETURNS_DEEP_STUBS);
		when(requestDetails.getHeaders(Constants.HEADER_CACHE_CONTROL)).thenReturn(Lists.newArrayList(Constants.CACHE_CONTROL_NO_CACHE));
		conformanceMethodBinding.invokeServer(mock(IRestfulServer.class, RETURNS_DEEP_STUBS), requestDetails, new Object[]{mock(HttpServletRequest.class), mock(RequestDetails.class)});
		verify(dummy, times(1)).getServerConformance(any(), any());
		conformanceMethodBinding.invokeServer(mock(IRestfulServer.class, RETURNS_DEEP_STUBS), requestDetails, new Object[]{mock(HttpServletRequest.class), mock(RequestDetails.class)});
		verify(dummy, times(2)).getServerConformance(any(), any());
	}

	class TestResourceProvider {

		public IBaseConformance getServerConformance(HttpServletRequest theRequest, RequestDetails theRequestDetails) {

			return mock(IBaseConformance.class, RETURNS_DEEP_STUBS);
		}
	}
}
