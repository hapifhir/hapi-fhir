package ca.uhn.fhir.jaxrs.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.*;

import ca.uhn.fhir.jaxrs.server.test.TestJaxRsDummyPatientProviderDstu2_1;
import ca.uhn.fhir.jaxrs.server.test.TestJaxRsMockPatientRestProviderDstu2_1;
import org.glassfish.jersey.internal.MapPropertiesDelegate;
import org.glassfish.jersey.server.ContainerRequest;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.IResourceProvider;

public class AbstractJaxRsConformanceProviderDstu2_1Test {

	private static final String BASEURI = "http://basiuri";
	private static final String REQUESTURI = BASEURI + "/metadata";
	AbstractJaxRsConformanceProvider provider;
	private ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> providers;
	private ContainerRequest headers;
	private MultivaluedHashMap<String, String> queryParameters;

	@Before
	public void setUp() throws Exception {
		// headers
		headers = new ContainerRequest(new URI(BASEURI), new URI(REQUESTURI), HttpMethod.GET, null,
				new MapPropertiesDelegate());
		// uri info
		queryParameters = new MultivaluedHashMap<String, String>();


		providers = new ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider>();
		provider = createConformanceProvider(providers);
	}

	@Test
	public void testConformance() throws Exception {
		providers.put(AbstractJaxRsConformanceProvider.class, provider);
		providers.put(TestJaxRsDummyPatientProviderDstu2_1.class, new TestJaxRsDummyPatientProviderDstu2_1());
		Response response = createConformanceProvider(providers).conformance();
		System.out.println(response);
	}

	@Test
	public void testConformanceUsingOptions() throws Exception {
		providers.put(AbstractJaxRsConformanceProvider.class, provider);
		providers.put(TestJaxRsDummyPatientProviderDstu2_1.class, new TestJaxRsDummyPatientProviderDstu2_1());
		Response response = createConformanceProvider(providers).conformanceUsingOptions();
		System.out.println(response);
	}

	@Test
	public void testConformanceWithMethods() throws Exception {
		providers.put(AbstractJaxRsConformanceProvider.class, provider);
		providers.put(TestJaxRsMockPatientRestProviderDstu2_1.class, new TestJaxRsMockPatientRestProviderDstu2_1());
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
		providers.put(TestJaxRsMockPatientRestProviderDstu2_1.class, new TestJaxRsMockPatientRestProviderDstu2_1());
		Response response = createConformanceProvider(providers).conformance();
		assertEquals(Constants.STATUS_HTTP_200_OK, response.getStatus());
		System.out.println(response.getEntity());
		assertTrue(response.getEntity().toString().contains(" <type value=\"Patient\"/>"));
		assertTrue(response.getEntity().toString().contains("\"someCustomOperation"));
		System.out.println(response.getEntity());
	}

	private AbstractJaxRsConformanceProvider createConformanceProvider(final ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> providers)
			throws Exception {
		AbstractJaxRsConformanceProvider result = new AbstractJaxRsConformanceProvider(FhirContext.forDstu2_1(), null, null, null) {
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
