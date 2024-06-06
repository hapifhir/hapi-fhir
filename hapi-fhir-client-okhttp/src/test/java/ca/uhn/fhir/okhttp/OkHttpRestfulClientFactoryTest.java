package ca.uhn.fhir.okhttp;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.okhttp.client.OkHttpRestfulClientFactory;
import ca.uhn.fhir.test.BaseFhirVersionParameterizedTest;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.net.ssl.SSLHandshakeException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;


public class OkHttpRestfulClientFactoryTest extends BaseFhirVersionParameterizedTest {

	private OkHttpRestfulClientFactory clientFactory;

	@BeforeEach
	public void setUp() {
		clientFactory = new OkHttpRestfulClientFactory();
	}

	@Test
	public void testGetNativeClient_noClientSet_returnsADefault() throws Exception {
		Call.Factory actualNativeClient = clientFactory.getNativeClient();

		assertNotNull(actualNativeClient);
	}

	@Test
	public void testGetNativeClient_noProxySet_defaultHasNoProxySet() throws Exception {
		OkHttpClient actualNativeClient = (OkHttpClient) clientFactory.getNativeClient();

		assertNull(actualNativeClient.proxy());
	}

	@Test
	public void testSetHttpClient() {
		OkHttpClient okHttpClient = new OkHttpClient.Builder().writeTimeout(5000, TimeUnit.MILLISECONDS).build();

		clientFactory.setHttpClient(okHttpClient);

		assertThat(clientFactory.getNativeClient()).isSameAs(okHttpClient);
	}

	@Test
	public void testSocketTimeout() {
		clientFactory.setSocketTimeout(1515);

		assertEquals(1515, ((OkHttpClient) clientFactory.getNativeClient()).readTimeoutMillis());
		assertEquals(1515, ((OkHttpClient) clientFactory.getNativeClient()).writeTimeoutMillis());
	}

	@Test
	public void testConnectTimeout() {
		clientFactory.setConnectTimeout(1516);

		assertEquals(1516, ((OkHttpClient) clientFactory.getNativeClient()).connectTimeoutMillis());
	}

	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testNativeClientHttp(FhirVersionEnum theFhirVersion) throws Exception {
		FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
		OkHttpRestfulClientFactory clientFactory = new OkHttpRestfulClientFactory(fhirVersionParams.getFhirContext());
		OkHttpClient client = (OkHttpClient) clientFactory.getNativeClient();

		Request request = new Request.Builder()
			.url(fhirVersionParams.getPatientEndpoint())
			.build();

		Response response = client.newCall(request).execute();
		assertEquals(200, response.code());
		String json = response.body().string();
		IBaseResource bundle = fhirVersionParams.getFhirContext().newJsonParser().parseResource(json);
		assertEquals(fhirVersionParams.getFhirVersion(), bundle.getStructureFhirVersionEnum());
	}

	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testNativeClientHttpsNoCredentials(FhirVersionEnum theFhirVersion) {
		FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
		OkHttpRestfulClientFactory clientFactory = new OkHttpRestfulClientFactory(fhirVersionParams.getFhirContext());
		OkHttpClient unauthenticatedClient = (OkHttpClient) clientFactory.getNativeClient();

		try {
			Request request = new Request.Builder()
				.url(fhirVersionParams.getSecuredPatientEndpoint())
				.build();
			unauthenticatedClient.newCall(request).execute();
			fail();
		} catch (Exception e) {
			assertEquals(SSLHandshakeException.class, e.getClass());
		}
	}

	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testGenericClientHttp(FhirVersionEnum theFhirVersion) {
		FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
		String base = fhirVersionParams.getBase();
		FhirContext context = fhirVersionParams.getFhirContext();
		context.setRestfulClientFactory(new OkHttpRestfulClientFactory(context));
		IBaseResource bundle = context.newRestfulGenericClient(base).search().forResource("Patient").execute();
		assertEquals(theFhirVersion, bundle.getStructureFhirVersionEnum());
	}

	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testGenericClientHttpsNoCredentials(FhirVersionEnum theFhirVersion) {
		FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
		String secureBase = fhirVersionParams.getSecureBase();
		FhirContext context = fhirVersionParams.getFhirContext();
		context.setRestfulClientFactory(new OkHttpRestfulClientFactory(context));
		try {
			context.newRestfulGenericClient(secureBase).search().forResource("Patient").execute();
			fail();
		} catch (Exception e) {
			assertThat(e.getMessage()).contains("HAPI-1357: Failed to retrieve the server metadata statement during client initialization");
			assertEquals(SSLHandshakeException.class, e.getCause().getCause().getClass());
		}
	}
}
