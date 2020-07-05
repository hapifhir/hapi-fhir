package ca.uhn.fhir.jaxrs.server.util;

import ca.uhn.fhir.jaxrs.server.test.TestJaxRsDummyPatientProviderDstu3;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.specimpl.ResteasyHttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JaxRsRequestDstu3Test {
	
	private static final String RESOURCE_STRING = "</Patient>";
	private static final String BASEURI = "http://baseuri";
	private static final String REQUESTURI = "http://baseuri/test";
	
	private JaxRsRequest details;
	private MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
	private ResteasyHttpHeaders headers;
	private TestJaxRsDummyPatientProviderDstu3 provider;
	
	@BeforeEach
	public void setUp() throws URISyntaxException {
		details = createRequestDetails();
	}

	@Test
	public void testGetHeader() {
		String headerKey = "key";
		String headerValue = "location_value";
		String headerValue2 = "location_value_2";
		assertTrue(StringUtils.isBlank(details.getHeader(headerKey)));
		queryParameters.add(headerKey, headerValue);
		assertEquals(headerValue, details.getHeader(headerKey));
		assertEquals(Arrays.asList(headerValue), details.getHeaders(headerKey));
		
		queryParameters.add(headerKey, headerValue2);
		assertEquals(headerValue, details.getHeader(headerKey));
		assertEquals(Arrays.asList(headerValue, headerValue2), details.getHeaders(headerKey));
	}
	
	@Test
	public void testGetByteStreamRequestContents() {
		assertEquals(RESOURCE_STRING, new String(details.getByteStreamRequestContents()));
	}
	
	@Test
	public void testServerBaseForRequest() {
		assertEquals(BASEURI, new String(details.getServerBaseForRequest()));
	}
	
	@Test
	public void testGetResponse() {
		JaxRsResponse response = (JaxRsResponse) details.getResponse();
		assertEquals(details, response.getRequestDetails());
		assertTrue(response == details.getResponse());
	}

	@Test
	public void testGetReader() {
		assertThrows(UnsupportedOperationException.class, ()->{
			details.getReader();
		});
	}

	@Test
	public void testGetInputStream() {
		assertThrows(UnsupportedOperationException.class, ()->{
			details.getInputStream();
		});
	}

	@Test
	public void testGetServerBaseForRequest() {
		assertEquals(JaxRsRequestDstu3Test.BASEURI, details.getFhirServerBase());
	}

	@Test
	public void testGetServer() {
		assertEquals(this.provider, details.getServer());
	}

	public JaxRsRequest createRequestDetails() {
		// headers
		headers = new ResteasyHttpHeaders(queryParameters);
		
		//uri info
		UriInfo uriInfo = mock(UriInfo.class);
		when(uriInfo.getQueryParameters()).thenReturn(queryParameters);
		
		//mocks
		provider = spy(TestJaxRsDummyPatientProviderDstu3.class);
		doReturn(uriInfo).when(provider).getUriInfo();
		doReturn(BASEURI).when(provider).getBaseForRequest();
		doReturn(BASEURI).when(provider).getBaseForServer();
		doReturn(headers).when(provider).getHeaders();
		
		return new JaxRsRequest(provider, RESOURCE_STRING, RequestTypeEnum.GET, RestOperationTypeEnum.HISTORY_TYPE);
	}	

}
