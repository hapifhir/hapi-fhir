package ca.uhn.fhir.jaxrs.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.test.BaseFhirVersionParameterizedTest;
import com.google.common.net.MediaType;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.Response;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Created by Sebastien Riviere on 31/07/2017.
 */
public class JaxRsRestfulClientFactoryTest extends BaseFhirVersionParameterizedTest {

	private static final Logger ourLog = LoggerFactory.getLogger(JaxRsRestfulClientFactoryTest.class);
	private final FhirContext context = FhirContext.forDstu2();
	private JaxRsRestfulClientFactory factory;

	@BeforeEach
	public void beforeEach() throws Exception {
		factory = new JaxRsRestfulClientFactory(context);
	}

	@Test
	public void emptyConstructorTest() {
		assertNotNull(new JaxRsRestfulClientFactory());
	}

	@Test
	public void getDefaultNativeClientTest() {
		assertNotNull(factory.getNativeClientClient());
	}

	@Test
	public void getNativeClientEmptyRegisteredComponentListTest() {
		factory.register(new ArrayList<>());
		final Client result = factory.getNativeClientClient();
		assertNotNull(result);
		ourLog.info("Classes: {}", result.getConfiguration().getClasses());
		assertThat(result.getConfiguration().getClasses()).doesNotContain(ca.uhn.fhir.jaxrs.client.MyFilter.class);
	}

	@Test
	public void getNativeClientRegisteredComponentListTest() {
		factory.register(Arrays.asList(MyFilter.class, String.class));
		final Client result = factory.getNativeClientClient();
		assertNotNull(result);
		ourLog.info("Classes: {}", result.getConfiguration().getClasses());
		assertThat(result.getConfiguration().getClasses()).contains(ca.uhn.fhir.jaxrs.client.MyFilter.class);
	}

	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testNativeClientHttp(FhirVersionEnum theFhirVersion) {
		FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
		JaxRsRestfulClientFactory factory = new JaxRsRestfulClientFactory(fhirVersionParams.getFhirContext());
		Client client = factory.getNativeClientClient();

		Response response = client
			.target(fhirVersionParams.getPatientEndpoint())
			.request(MediaType.JSON_UTF_8.toString())
			.get(Response.class);

		assertEquals(200, response.getStatus());
		String json = response.readEntity(String.class);
		IBaseResource bundle = fhirVersionParams.parseResource(json);
		assertEquals(fhirVersionParams.getFhirVersion(), bundle.getStructureFhirVersionEnum());
	}

	@ParameterizedTest
	@MethodSource("baseParamsProvider")
	public void testNativeClientHttpsNoCredentials(FhirVersionEnum theFhirVersion) {
		FhirVersionParams fhirVersionParams = getFhirVersionParams(theFhirVersion);
		JaxRsRestfulClientFactory factory = new JaxRsRestfulClientFactory(fhirVersionParams.getFhirContext());
		Client unauthenticatedClient = factory.getNativeClientClient();

		try {
			unauthenticatedClient
				.target(fhirVersionParams.getSecuredPatientEndpoint())
				.request(MediaType.JSON_UTF_8.toString())
				.get(Response.class);
			fail();
		} catch (Exception e) {
			assertTrue(e.getCause() instanceof SSLException);
		}
	}
}
