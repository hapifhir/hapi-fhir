package ca.uhn.fhir.cli.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
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


public class HapiFhirCliRestfulClientFactoryTest extends BaseFhirVersionParameterizedTest{

	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testNativeClientHttp(FhirVersionEnum theFhirVersion) throws Exception {
		FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
		HapiFhirCliRestfulClientFactory clientFactory = new HapiFhirCliRestfulClientFactory(fhirVersionParams.getFhirContext());
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
	public void testNativeClientHttps(FhirVersionEnum theFhirVersion) throws Exception {
		FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
		HapiFhirCliRestfulClientFactory clientFactory = new HapiFhirCliRestfulClientFactory(fhirVersionParams.getFhirContext(), getTlsAuthentication());
		HttpClient authenticatedClient = clientFactory.getNativeHttpClient();

		HttpUriRequest request = new HttpGet(fhirVersionParams.getSecuredPatientEndpoint());
		HttpResponse response = authenticatedClient.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());

		String json = EntityUtils.toString(response.getEntity());
		IBaseResource bundle = fhirVersionParams.parseResource(json);
		assertEquals(fhirVersionParams.getFhirVersion(), bundle.getStructureFhirVersionEnum());
	}

	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testNativeClientHttpsNoCredentials(FhirVersionEnum theFhirVersion) {
		FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
		HapiFhirCliRestfulClientFactory clientFactory = new HapiFhirCliRestfulClientFactory(fhirVersionParams.getFhirContext());
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
		context.setRestfulClientFactory(new HapiFhirCliRestfulClientFactory(context));
		IBaseResource bundle = context.newRestfulGenericClient(base).search().forResource("Patient").execute();
		assertEquals(theFhirVersion, bundle.getStructureFhirVersionEnum());
	}

	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testGenericClientHttps(FhirVersionEnum theFhirVersion) {
		FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
		String secureBase = fhirVersionParams.getSecureBase();
		FhirContext context = fhirVersionParams.getFhirContext();
		context.setRestfulClientFactory(new HapiFhirCliRestfulClientFactory(context, getTlsAuthentication()));
		IBaseResource bundle = context.newRestfulGenericClient(secureBase).search().forResource("Patient").execute();
		assertEquals(theFhirVersion, bundle.getStructureFhirVersionEnum());
	}

	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testGenericClientHttpsNoCredentials(FhirVersionEnum theFhirVersion) {
		FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
		String secureBase = fhirVersionParams.getSecureBase();
		FhirContext context = fhirVersionParams.getFhirContext();
		context.setRestfulClientFactory(new HapiFhirCliRestfulClientFactory(context));
		try {
			context.newRestfulGenericClient(secureBase).search().forResource("Patient").execute();
			fail();		} catch (Exception e) {
			assertThat(e.getMessage()).contains("HAPI-1357: Failed to retrieve the server metadata statement during client initialization");
			assertEquals(SSLHandshakeException.class, e.getCause().getCause().getClass());
		}
	}

	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testGenericClientProtocolChanges(FhirVersionEnum theFhirVersion) {
		FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
		FhirContext context = fhirVersionParams.getFhirContext();
		String secureBase = fhirVersionParams.getSecureBase();
		String base = fhirVersionParams.getBase();

		// https
		HapiFhirCliRestfulClientFactory restfulClientFactory = new HapiFhirCliRestfulClientFactory(context, getTlsAuthentication());
		context.setRestfulClientFactory(restfulClientFactory);
		IBaseResource bundle = context.newRestfulGenericClient(secureBase).search().forResource("Patient").execute();
		assertEquals(theFhirVersion, bundle.getStructureFhirVersionEnum());

		// http
		restfulClientFactory.useHttp();
		bundle = context.newRestfulGenericClient(base).search().forResource("Patient").execute();
		assertEquals(theFhirVersion, bundle.getStructureFhirVersionEnum());

		// https
		restfulClientFactory.useHttps(getTlsAuthentication());
		bundle = context.newRestfulGenericClient(secureBase).search().forResource("Patient").execute();
		assertEquals(theFhirVersion, bundle.getStructureFhirVersionEnum());
	}


	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testSetHttpClient(FhirVersionEnum theFhirVersion){
		try {
			FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
			FhirContext fhirContext = fhirVersionParams.getFhirContext();
			HapiFhirCliRestfulClientFactory hapiFhirCliRestfulClientFactory = new HapiFhirCliRestfulClientFactory(fhirContext);
			hapiFhirCliRestfulClientFactory.setHttpClient(new Object());
		} catch (UnsupportedOperationException e){
			assertEquals(Msg.code(2119), e.getMessage());
		}
	}

	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testSetProxy(FhirVersionEnum theFhirVersion){
		try {
			FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
			FhirContext fhirContext = fhirVersionParams.getFhirContext();
			HapiFhirCliRestfulClientFactory hapiFhirCliRestfulClientFactory = new HapiFhirCliRestfulClientFactory(fhirContext);
			hapiFhirCliRestfulClientFactory.setProxy("proxy", 1);
		} catch (UnsupportedOperationException e){
			assertEquals(Msg.code(2120), e.getMessage());
		}
	}

}
