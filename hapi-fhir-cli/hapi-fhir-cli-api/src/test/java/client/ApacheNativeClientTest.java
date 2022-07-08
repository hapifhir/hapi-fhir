package client;

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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApacheNativeClientTest extends BaseFhirVersionParameterizedTest {


	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testNativeClientHttp(FhirVersionEnum theFhirVersion) {
		FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
		ApacheRestfulClientFactory clientFactory = new ApacheRestfulClientFactory(fhirVersionParams.getFhirContext());
		HttpClient client = clientFactory.getNativeHttpClient();

		assertDoesNotThrow(() -> {
			HttpUriRequest request = new HttpGet(fhirVersionParams.getPatientEndpoint());
			HttpResponse response = client.execute(request);
			assertEquals(200, response.getStatusLine().getStatusCode());

			String json = EntityUtils.toString(response.getEntity());
			IBaseResource bundle = fhirVersionParams.parseResource(json);
			assertEquals(fhirVersionParams.getFhirVersion(), bundle.getStructureFhirVersionEnum());
		});
	}

	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testNativeClientHttps(FhirVersionEnum theFhirVersion) {
		FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
		ApacheRestfulClientFactory clientFactory = new ApacheRestfulClientFactory(fhirVersionParams.getFhirContext());
		HttpClient authenticatedClient = clientFactory.getNativeHttpClient(getTlsAuthentication());

		assertDoesNotThrow(() -> {
			HttpUriRequest request = new HttpGet(fhirVersionParams.getSecuredPatientEndpoint());
			HttpResponse response = authenticatedClient.execute(request);
			assertEquals(200, response.getStatusLine().getStatusCode());

			String json = EntityUtils.toString(response.getEntity());
			IBaseResource bundle = fhirVersionParams.parseResource(json);
			assertEquals(fhirVersionParams.getFhirVersion(), bundle.getStructureFhirVersionEnum());
		});
	}

	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testNativeClientHttpsNoCredentials(FhirVersionEnum theFhirVersion) {
		FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
		ApacheRestfulClientFactory clientFactory = new ApacheRestfulClientFactory(fhirVersionParams.getFhirContext());
		HttpClient unauthenticatedClient = clientFactory.getNativeHttpClient();

		assertThrows(SSLHandshakeException.class, () -> {
			HttpUriRequest request = new HttpGet(fhirVersionParams.getSecuredPatientEndpoint());
			unauthenticatedClient.execute(request);
		});
	}

}
