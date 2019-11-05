package ca.uhn.fhir.jaxrs.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.test.TestJaxRsMockPatientRestProviderDstu3;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.jboss.resteasy.specimpl.ResteasyHttpHeaders;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.uhn.fhir.jaxrs.server.test.TestJaxRsDummyPatientProviderDstu3;

public class AbstractJaxRsConformanceProviderDstu3Test {

	private static final String BASEURI = "http://basiuri";
	private static final String REQUESTURI = BASEURI + "/metadata";
	AbstractJaxRsConformanceProvider provider;
	private ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> providers;
	private ResteasyHttpHeaders headers;
	private MultivaluedHashMap<String, String> queryParameters;
	
	@Before
	public void setUp() throws Exception {
		// uri info
		queryParameters = new MultivaluedHashMap<>();
		// headers
//		headers = new ContainerRequest(new URI(BASEURI), new URI(REQUESTURI), HttpMethod.GET, null,
//				new MapPropertiesDelegate());
		headers = new ResteasyHttpHeaders(queryParameters);
		
		
		providers = new ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider>();
		provider = createConformanceProvider(providers);
	}

	@Test
	public void testConformance() throws Exception {
		providers.put(AbstractJaxRsConformanceProvider.class, provider);
		providers.put(TestJaxRsDummyPatientProviderDstu3.class, new TestJaxRsDummyPatientProviderDstu3());
		Response response = createConformanceProvider(providers).conformance();
		System.out.println(response);
	}
	
	@Test
	public void testConformanceUsingOptions() throws Exception {
		providers.put(AbstractJaxRsConformanceProvider.class, provider);
		providers.put(TestJaxRsDummyPatientProviderDstu3.class, new TestJaxRsDummyPatientProviderDstu3());
		Response response = createConformanceProvider(providers).conformanceUsingOptions();
		System.out.println(response);
	}

	@Test
	public void testConformanceWithMethods() throws Exception {
		providers.put(AbstractJaxRsConformanceProvider.class, provider);
		providers.put(TestJaxRsMockPatientRestProviderDstu3.class, new TestJaxRsMockPatientRestProviderDstu3());
		Response response = createConformanceProvider(providers).conformance();
		assertEquals(Constants.STATUS_HTTP_200_OK, response.getStatus());
		assertTrue(response.getEntity().toString().contains("\"type\": \"Patient\""));
		assertTrue(response.getEntity().toString().contains("\"someCustomOperation"));
		System.out.println(response);
		System.out.println(response.getEntity());
	}
	
	@Test
	public void testConformanceInXml() throws Exception {
		queryParameters.put(Constants.PARAM_FORMAT, Arrays.asList(Constants.CT_XML));		
		providers.put(AbstractJaxRsConformanceProvider.class, provider);
		providers.put(TestJaxRsMockPatientRestProviderDstu3.class, new TestJaxRsMockPatientRestProviderDstu3());
		Response response = createConformanceProvider(providers).conformance();
		assertEquals(Constants.STATUS_HTTP_200_OK, response.getStatus());
		System.out.println(response.getEntity());
		assertTrue(response.getEntity().toString().contains(" <type value=\"Patient\"/>"));
		assertTrue(response.getEntity().toString().contains("\"someCustomOperation"));
		System.out.println(response.getEntity());
	}
	
	private AbstractJaxRsConformanceProvider createConformanceProvider(final ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> providers) 
			throws Exception {
		AbstractJaxRsConformanceProvider result = new AbstractJaxRsConformanceProvider(FhirContext.forDstu3(), null, null, null) {
			@Override
			protected ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> getProviders() {
				return providers;
			}
		};
		// mocks
		UriInfo uriInfo = mock(UriInfo.class);
		when(uriInfo.getQueryParameters()).thenReturn(queryParameters);
		when(uriInfo.getBaseUri()).thenReturn(new URI(BASEURI));		
		when(uriInfo.getRequestUri()).thenReturn(new URI(BASEURI + "/foo"));		
		result.setUriInfo(uriInfo);
		result.setHeaders(headers);
		result.setUpPostConstruct();
		return result;
	}	

}
