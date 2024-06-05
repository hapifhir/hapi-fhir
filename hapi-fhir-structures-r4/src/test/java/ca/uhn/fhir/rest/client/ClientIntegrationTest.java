package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.apache.ApacheRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.client.impl.HttpBasicAuthInterceptor;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.Validate;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientIntegrationTest {
	private MyPatientResourceProvider myPatientProvider = new MyPatientResourceProvider();
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(myPatientProvider)
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.XML);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@SuppressWarnings("deprecation")
	@Test
	public void testClientSecurity() throws Exception {

		HttpClientBuilder builder = HttpClientBuilder.create();
		// PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		// builder.setConnectionManager(connectionManager);
		builder.addInterceptorFirst(new HttpBasicAuthInterceptor("foobar", "boobear"));

		CloseableHttpClient httpClient = builder.build();

		ourCtx.getRestfulClientFactory().setHttpClient(httpClient);

		PatientClient client = ourCtx.newRestfulClient(PatientClient.class, ourServer.getBaseUrl() + "/");

		List<Patient> actualPatients = client.searchForPatients(new StringDt("AAAABBBB"));
		assertThat(actualPatients).hasSize(1);
		assertEquals("AAAABBBB", actualPatients.get(0).getNameFirstRep().getFamily());

		assertEquals("Basic Zm9vYmFyOmJvb2JlYXI=", myPatientProvider.getAuthorizationHeader());
	}

	@AfterEach
	public void after() throws Exception {
		ourCtx.setRestfulClientFactory(new ApacheRestfulClientFactory(ourCtx));
	}

	public static class MyPatientResourceProvider implements IResourceProvider {
		private String myAuthorizationHeader;

		public String getAuthorizationHeader() {
			return myAuthorizationHeader;
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Search
		public List<Patient> searchForPatients(@RequiredParam(name = "fooParam") StringDt theFooParam, HttpServletRequest theRequest, HttpServletResponse theResponse) {
			Validate.notNull(theRequest);
			Validate.notNull(theResponse);

			myAuthorizationHeader = theRequest.getHeader("authorization");

			Patient retVal = new Patient();
			retVal.setId("1");
			retVal.addName().setFamily(theFooParam.getValue());
			return Collections.singletonList(retVal);
		}

	}

	private static interface PatientClient extends IBasicClient {

		@Search
		public List<Patient> searchForPatients(@RequiredParam(name = "fooParam") StringDt theFooParam);

	}


	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
