package ca.uhn.fhir.jaxrs.server.interceptor;

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsProvider;
import ca.uhn.fhir.jaxrs.server.test.TestJaxRsDummyPatientProvider;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.interceptor.ExceptionHandlingInterceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.servlet.ServletException;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class JaxRsExceptionInterceptorTest {

	JaxRsExceptionInterceptor interceptor = new JaxRsExceptionInterceptor();
	private InvocationContext context;

	@BeforeEach
	public void setUp() throws Exception {
		interceptor = new JaxRsExceptionInterceptor();
		context = mock(InvocationContext.class);
		TestJaxRsDummyPatientProvider provider = spy(TestJaxRsDummyPatientProvider.class);
		when(context.getTarget()).thenReturn(provider);
		doReturn("http://baseUri").when(provider).getBaseForServer();
		doReturn(new HashMap<String, String[]>()).when(provider).getParameters();
		doReturn(mock(HttpHeaders.class)).when(provider).getHeaders();
		
		UriInfo uriInfo = mock(UriInfo.class);
		when(uriInfo.getRequestUri()).thenReturn(new URI("http://base/foo"));		
		provider.setUriInfo(uriInfo);

	}

	@Test
	public void testInterceptWithBaseServerError() throws Throwable {
		NotImplementedOperationException thrownException = new NotImplementedOperationException("not implemented");
		when(context.proceed()).thenThrow(thrownException);
		try {
			interceptor.intercept(context);
			fail();
		} catch (BaseServerResponseException e) {
			assertEquals(e.getMessage(), thrownException.getMessage());
		}
	}

	@Test
	public void testInterceptorWithServletError() throws Throwable {
		ExceptionHandlingInterceptor exceptionHandler = mock(ExceptionHandlingInterceptor.class);
		when(exceptionHandler.preProcessOutgoingException(any(RequestDetails.class), any(Throwable.class),
				isNull())).thenThrow(new ServletException("someMessage"));
		interceptor = new JaxRsExceptionInterceptor(exceptionHandler);
		when(context.proceed()).thenThrow(new ServletException());
		try {
			interceptor.intercept(context);
			fail();
		} catch (BaseServerResponseException e) {
			assertThat(e.getMessage()).contains("someMessage");
		}
	}

	@Test
	public void testInterceptServletWithoutError() throws Throwable {
		Object expected = new Object();
		when(context.proceed()).thenReturn(expected);
		Object result = interceptor.intercept(context);
		assertThat(result).isSameAs(expected);
	}
	
	@Test
	public void testHandleExceptionWithServletError() throws Throwable {
		JaxRsRequest request = ((AbstractJaxRsProvider) context.getTarget()).getRequest(null, null).build();
		
		ExceptionHandlingInterceptor exceptionHandler = spy(ExceptionHandlingInterceptor.class);
		
		interceptor = new JaxRsExceptionInterceptor(exceptionHandler);
		
		when(context.proceed()).thenThrow(new ServletException());		
		
		JaxRsResponseException thrownException = new JaxRsResponseException(new NotImplementedOperationException("not implemented"));
		doThrow(new jakarta.servlet.ServletException("someMessage")).when(exceptionHandler).handleException(request, thrownException);
		Response result = interceptor.convertExceptionIntoResponse(request, thrownException);
		assertEquals(InternalErrorException.STATUS_CODE, result.getStatus());
	}

}
