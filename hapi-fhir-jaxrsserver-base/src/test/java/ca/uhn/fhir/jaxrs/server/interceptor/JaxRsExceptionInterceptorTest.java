package ca.uhn.fhir.jaxrs.server.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.HashMap;

import javax.interceptor.InvocationContext;
import javax.servlet.ServletException;
import javax.ws.rs.core.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsProvider;
import ca.uhn.fhir.jaxrs.server.test.TestJaxRsDummyPatientProvider;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.*;
import ca.uhn.fhir.rest.server.interceptor.ExceptionHandlingInterceptor;

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
			assertTrue(e.getMessage().contains("someMessage"));
		}
	}

	@Test
	public void testInterceptServletWithoutError() throws Throwable {
		Object expected = new Object();
		when(context.proceed()).thenReturn(expected);
		Object result = interceptor.intercept(context);
		assertSame(expected, result);
	}
	
	@Test
	public void testHandleExceptionWithServletError() throws Throwable {
		JaxRsRequest request = ((AbstractJaxRsProvider) context.getTarget()).getRequest(null, null).build();
		
		ExceptionHandlingInterceptor exceptionHandler = spy(ExceptionHandlingInterceptor.class);
		
		interceptor = new JaxRsExceptionInterceptor(exceptionHandler);
		
		when(context.proceed()).thenThrow(new ServletException());		
		
		JaxRsResponseException thrownException = new JaxRsResponseException(new NotImplementedOperationException("not implemented"));
		doThrow(new javax.servlet.ServletException("someMessage")).when(exceptionHandler).handleException(request, thrownException);
		Response result = interceptor.convertExceptionIntoResponse(request, thrownException);
		assertEquals(InternalErrorException.STATUS_CODE, result.getStatus());
	}

}
