package ca.uhn.fhir.jaxrs.server.util;

import static org.junit.jupiter.api.Assertions.assertTrue;
import ca.uhn.fhir.jaxrs.server.test.TestJaxRsDummyPatientProvider;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.specimpl.ResteasyHttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class JaxRsRequestTest {
	
	private static final String RESOURCE_STRING = "</Patient>";
	private static final String BASEURI = "http://baseuri";
	private static final String REQUESTURI = "http://baseuri/test";
	
	private JaxRsRequest details;
	private MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
	private ResteasyHttpHeaders headers;
	private TestJaxRsDummyPatientProvider provider;
	
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
		assertThat(details.getHeader(headerKey)).isEqualTo(headerValue);
		assertThat(details.getHeaders(headerKey)).isEqualTo(Arrays.asList(headerValue));
		
		queryParameters.add(headerKey, headerValue2);
		assertThat(details.getHeader(headerKey)).isEqualTo(headerValue);
		assertThat(details.getHeaders(headerKey)).isEqualTo(Arrays.asList(headerValue, headerValue2));
	}
	
	@Test
	public void testGetByteStreamRequestContents() {
		assertThat(new String(details.getByteStreamRequestContents())).isEqualTo(RESOURCE_STRING);
	}
	
	@Test
	public void testServerBaseForRequest() {
		assertThat(new String(details.getServerBaseForRequest())).isEqualTo(BASEURI);
	}
	
	@Test
	public void testGetResponse() {
		JaxRsResponse response = (JaxRsResponse) details.getResponse();
		assertThat(response.getRequestDetails()).isEqualTo(details);
		assertTrue(response == details.getResponse());
	}

	@Test
	public void testGetReader() throws IOException {
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> {
			details.getReader();
		});
	}

	@Test
	public void testGetInputStream() {
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> {
			details.getInputStream();
		});
	}

	@Test
	public void testGetServerBaseForRequest() {
		assertThat(details.getFhirServerBase()).isEqualTo(JaxRsRequestTest.BASEURI);
	}

	@Test
	public void testGetServer() {
		assertThat(details.getServer()).isEqualTo(this.provider);
	}

	public JaxRsRequest createRequestDetails() throws URISyntaxException {
		// headers
//		headers = new ContainerRequest(new URI(BASEURI), new URI(REQUESTURI), HttpMethod.GET, null,
//				new MapPropertiesDelegate());
		headers = new ResteasyHttpHeaders(queryParameters);
		
		//uri info
		UriInfo uriInfo = mock(UriInfo.class);
		when(uriInfo.getQueryParameters()).thenReturn(queryParameters);
		
		//mocks
		provider = spy(TestJaxRsDummyPatientProvider.class);
		doReturn(uriInfo).when(provider).getUriInfo();
		doReturn(BASEURI).when(provider).getBaseForRequest();
		doReturn(BASEURI).when(provider).getBaseForServer();
		doReturn(headers).when(provider).getHeaders();
		
		return new JaxRsRequest(provider, RESOURCE_STRING, RequestTypeEnum.GET, RestOperationTypeEnum.HISTORY_TYPE);
	}	

}
