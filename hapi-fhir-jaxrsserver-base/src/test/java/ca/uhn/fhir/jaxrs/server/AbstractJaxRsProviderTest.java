package ca.uhn.fhir.jaxrs.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import javax.ws.rs.core.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.uhn.fhir.jaxrs.server.interceptor.JaxRsResponseException;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest;
import ca.uhn.fhir.jaxrs.server.util.JaxRsResponse;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IRestfulResponse;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

@SuppressWarnings("javadoc")
@RunWith(MockitoJUnitRunner.class)
public class AbstractJaxRsProviderTest {

    private AbstractJaxRsProviderMock provider;
    @Mock
    private JaxRsRequest theRequest;

    @Before
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

    @Test
    public void testWithStackTrace() {
        assertFalse(provider.withStackTrace());
    }
}
