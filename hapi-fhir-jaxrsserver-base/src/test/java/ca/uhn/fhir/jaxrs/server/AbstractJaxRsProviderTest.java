package ca.uhn.fhir.jaxrs.server;

import ca.uhn.fhir.jaxrs.server.interceptor.JaxRsResponseException;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest;
import ca.uhn.fhir.jaxrs.server.util.JaxRsResponse;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IRestfulResponse;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("javadoc")
@ExtendWith(MockitoExtension.class)
public class AbstractJaxRsProviderTest {

	private AbstractJaxRsProviderMock provider;
	@Mock
	private JaxRsRequest theRequest;

	@BeforeEach
	public void setUp() {
		provider = new AbstractJaxRsProviderMock();
		final IRestfulResponse response = new JaxRsResponse(theRequest);
		doReturn(provider).when(theRequest).getServer();
		doReturn(response).when(theRequest).getResponse();
	}

	@Test
	public void testHandleExceptionDataFormatException() throws IOException, URISyntaxException {
		final DataFormatException theException = new DataFormatException();
		UriInfo uriInfo = mock(UriInfo.class);
		when(uriInfo.getRequestUri()).thenReturn(new URI("http://example.com"));
		when(uriInfo.getBaseUri()).thenReturn(new URI("http://example.com"));
		when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<String, String>());
		provider.setUriInfo(uriInfo);
		final Response result = provider.handleException(theRequest, theException);
		assertNotNull(result);
		assertEquals(Constants.STATUS_HTTP_400_BAD_REQUEST, result.getStatus());
	}

	@Test
	public void testHandleExceptionJaxRsResponseException() throws IOException {
		final ResourceNotFoundException base = new ResourceNotFoundException(new IdDt(1L));
		final JaxRsResponseException theException = new JaxRsResponseException(base);
		final Response result = provider.handleException(theRequest, theException);
		assertNotNull(result);
		assertEquals(base.getStatusCode(), result.getStatus());
	}

	@Test
	public void testHandleExceptionRuntimeException() throws IOException, URISyntaxException {
		assertFalse(provider.withStackTrace());

		final RuntimeException theException = new RuntimeException();
		final UriInfo mockUriInfo = mock(UriInfo.class);
		final MultivaluedMap<String, String> mockMap = mock(MultivaluedMap.class);
		when(mockUriInfo.getBaseUri()).thenReturn(new URI("http://www.test.com"));
		when(mockUriInfo.getRequestUri()).thenReturn(new URI("http://www.test.com/test"));
		when(mockUriInfo.getQueryParameters()).thenReturn(mockMap);

		provider.setUriInfo(mockUriInfo);
		final Response result = provider.handleException(theRequest, theException);
		assertNotNull(result);
		assertEquals(Constants.STATUS_HTTP_500_INTERNAL_ERROR, result.getStatus());
	}

}
