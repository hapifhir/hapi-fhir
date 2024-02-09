package ca.uhn.fhir.jaxrs.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.test.BaseFhirVersionParameterizedTest;
import com.google.common.net.MediaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.assertj.core.api.Assertions.fail;


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
		assertThat(new JaxRsRestfulClientFactory()).isNotNull();
	}

	@Test
	public void getDefaultNativeClientTest() {
		assertThat(factory.getNativeClientClient()).isNotNull();
	}

	@Test
	public void getNativeClientEmptyRegisteredComponentListTest() {
		factory.register(new ArrayList<>());
		final Client result = factory.getNativeClientClient();
		assertThat(result).isNotNull();
		ourLog.info("Classes: {}", result.getConfiguration().getClasses());
		assertThat(result.getConfiguration().getClasses(), not(hasItem(ca.uhn.fhir.jaxrs.client.MyFilter.class)));
	}

	@Test
	public void getNativeClientRegisteredComponentListTest() {
		factory.register(Arrays.asList(MyFilter.class, String.class));
		final Client result = factory.getNativeClientClient();
		assertThat(result).isNotNull();
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

		assertThat(response.getStatus()).isEqualTo(200);
		String json = response.readEntity(String.class);
		IBaseResource bundle = fhirVersionParams.parseResource(json);
		assertThat(bundle.getStructureFhirVersionEnum()).isEqualTo(fhirVersionParams.getFhirVersion());
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
			fail("");
		} catch (Exception e) {
			assertThat(e.getCause() instanceof SSLException).isTrue();
		}
	}
}
