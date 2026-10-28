package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.test.utilities.TestHttpClientFactory;
import ca.uhn.fhir.test.utilities.client.RequestCountingFilter;
import ca.uhn.fhir.test.utilities.client.UnboundedRequestEntity;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerRequestOverloadDefenseR4Test {

	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	private final RequestCountingFilter myRequestCountingFilter = new RequestCountingFilter();

	@RegisterExtension
	@Order(0)
	private final RestfulServerExtension myServer = new RestfulServerExtension(myFhirContext)
		.withServletFilter(myRequestCountingFilter);

	@RegisterExtension
	@Order(1)
	private HashMapResourceProviderExtension<Patient> myPatientProvider = new HashMapResourceProviderExtension<>(myServer, Patient.class);

	@Test
	void testLargeRequestBlocked() throws IOException {
		try (CloseableHttpClient client = TestHttpClientFactory.create(false, 10000, true)) {
			String url = myServer.getBaseUrl() + "/Patient";
			HttpPost post = new HttpPost(url);
			UnboundedRequestEntity requestEntity = new UnboundedRequestEntity();
			post.setEntity(requestEntity);
			try (CloseableHttpResponse outcome = client.execute(post)) {
				assertThat(outcome.getStatusLine().getStatusCode()).isEqualTo(413);
			}
			assertEquals(0, myRequestCountingFilter.getRequestReadByteCount());
		}

	}

}
