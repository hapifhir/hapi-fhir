package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.client.apache.ApacheRestfulClientFactory;
import ca.uhn.fhir.test.BaseFhirVersionParameterizedTest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.net.ssl.SSLHandshakeException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class ApacheRestfulClientFactoryTest extends BaseFhirVersionParameterizedTest {

	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testNativeClientHttp(FhirVersionEnum theFhirVersion) throws Exception {
		FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
		ApacheRestfulClientFactory clientFactory = new ApacheRestfulClientFactory(fhirVersionParams.getFhirContext());
		HttpClient client = clientFactory.getNativeHttpClient();

		HttpUriRequest request = new HttpGet(fhirVersionParams.getPatientEndpoint());
		HttpResponse response = client.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());

		String json = EntityUtils.toString(response.getEntity());
		IBaseResource bundle = fhirVersionParams.parseResource(json);
		assertEquals(fhirVersionParams.getFhirVersion(), bundle.getStructureFhirVersionEnum());
	}

	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testNativeClientHttpsNoCredentials(FhirVersionEnum theFhirVersion) {
		FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
		ApacheRestfulClientFactory clientFactory = new ApacheRestfulClientFactory(fhirVersionParams.getFhirContext());
		HttpClient unauthenticatedClient = clientFactory.getNativeHttpClient();

		try{
			HttpUriRequest request = new HttpGet(fhirVersionParams.getSecuredPatientEndpoint());
			unauthenticatedClient.execute(request);
			fail();		}
		catch(Exception e){
			assertEquals(SSLHandshakeException.class, e.getClass());
		}
	}

	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testGenericClientHttp(FhirVersionEnum theFhirVersion) {
		FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
		String base = fhirVersionParams.getBase();
		FhirContext context = fhirVersionParams.getFhirContext();
		context.setRestfulClientFactory(new ApacheRestfulClientFactory(context));
		IBaseResource bundle = context.newRestfulGenericClient(base).search().forResource("Patient").execute();
		assertEquals(theFhirVersion, bundle.getStructureFhirVersionEnum());
	}

	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testGenericClientHttpsNoCredentials(FhirVersionEnum theFhirVersion) {
		FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
		String secureBase = fhirVersionParams.getSecureBase();
		FhirContext context = fhirVersionParams.getFhirContext();
		context.setRestfulClientFactory(new ApacheRestfulClientFactory(context));
		try {
			context.newRestfulGenericClient(secureBase).search().forResource("Patient").execute();
			fail();		} catch (Exception e) {
			assertThat(e.getMessage()).contains("HAPI-1357: Failed to retrieve the server metadata statement during client initialization");
			assertEquals(SSLHandshakeException.class, e.getCause().getCause().getClass());
		}
	}
}
