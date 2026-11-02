package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.test.utilities.TestHttpClientFactory;
import ca.uhn.fhir.test.utilities.client.RequestCountingFilter;
import ca.uhn.fhir.test.utilities.client.UnboundedRequestEntity;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.net.SocketException;

import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerRequestOverloadDefenseR4Test {

	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	private final RequestCountingFilter myRequestCountingFilter = new RequestCountingFilter();

	@RegisterExtension
	@Order(0)
	private final RestfulServerExtension myServer = new RestfulServerExtension(myFhirContext)
		.withServletFilter(myRequestCountingFilter)
		.withServer(s -> s.setRequestBodyHardLimit(1000L));

	@RegisterExtension
	@Order(1)
	private final HashMapResourceProviderExtension<Patient> myPatientProvider = new HashMapResourceProviderExtension<>(myServer, Patient.class);

	@Test
	void testSmallRequestAllowed() {
		long initialCount = myPatientProvider.getCountCreate();

		Patient resource = new Patient();
		resource.getNameFirstRep().setText(leftPad("", 900, 'a'));
		myServer.getFhirClient().create().resource(resource).execute();

		assertEquals(initialCount + 1, myPatientProvider.getCountCreate());
	}

	@Test
	void testLargeCreateRequestBlocked() throws IOException {
		try (CloseableHttpClient client = TestHttpClientFactory.create(false, 10000, true)) {
			String url = myServer.getBaseUrl() + "/Patient";
			HttpPost post = new HttpPost(url);
			UnboundedRequestEntity requestEntity = new UnboundedRequestEntity();
			post.setEntity(requestEntity);
			assertThatThrownBy(() -> client.execute(post))
				.isInstanceOf(SocketException.class)
				.hasMessageContaining("Broken pipe");
			assertThat(myRequestCountingFilter.getRequestReadByteCount()).isLessThan(1000);
		}

	}

	@ParameterizedTest
	@ValueSource(strings = {
		"/Patient/_search",
		"/Patient/_search?_format=json"
	})
	void testRequestParametersBlocked(String theRequestPath) {
		myServer.fhirRequest(theRequestPath)
			.withFormParam("name", leftPad("", 1000000, 'a'))
			.postForm()
			.assertStatus(413);
	}

}
