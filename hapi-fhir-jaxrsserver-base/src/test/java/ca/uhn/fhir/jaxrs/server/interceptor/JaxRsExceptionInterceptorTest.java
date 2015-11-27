package ca.uhn.fhir.jaxrs.server.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import javax.interceptor.InvocationContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsProvider;
import ca.uhn.fhir.jaxrs.server.test.TestJaxRsDummyPatientProvider;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.interceptor.ExceptionHandlingInterceptor;

public class JaxRsExceptionInterceptorTest {

	JaxRsExceptionInterceptor interceptor = new JaxRsExceptionInterceptor();
	private InvocationContext context;

	@Before
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
	public void testIntercepWithServletError() throws Throwable {
		ExceptionHandlingInterceptor exceptionHandler = mock(ExceptionHandlingInterceptor.class);
		when(exceptionHandler.preProcessOutgoingException(any(RequestDetails.class), any(Throwable.class),
				isNull(HttpServletRequest.class))).thenThrow(new ServletException("someMessage"));
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
