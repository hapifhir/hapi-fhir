package ca.uhn.fhir.jaxrs.server;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.uhn.fhir.jaxrs.server.interceptor.JaxRsResponseException;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest;
import ca.uhn.fhir.jaxrs.server.util.JaxRsResponse;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IRestfulResponse;
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
    public void testWithStackTrace() {
        assertFalse(provider.withStackTrace());
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
    public void testHandleExceptionDataFormatException() throws IOException {
        final DataFormatException theException = new DataFormatException();
        final Response result = provider.handleException(theRequest, theException);
        assertNotNull(result);
        assertEquals(Constants.STATUS_HTTP_400_BAD_REQUEST, result.getStatus());
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
}
