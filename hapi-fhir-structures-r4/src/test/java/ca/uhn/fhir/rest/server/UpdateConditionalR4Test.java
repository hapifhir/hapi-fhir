package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies what the server framework hands to an {@link Update} provider for a conditional PUT, in particular that
 * a client-supplied id in the resource body survives the trip through {@code UpdateMethodBinding}
 * (https://github.com/hapifhir/hapi-fhir/issues/8389).
 */
// Created by Claude Fable 5.1
public class UpdateConditionalR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(UpdateConditionalR4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static IdType ourLastIdParam;
	private static String ourLastConditionalUrl;
	private static IdType ourLastBodyId;

	@RegisterExtension
	private final RestfulServerExtension myServer = new RestfulServerExtension(ourCtx)
		.registerProvider(new PatientProvider())
		.setDefaultResponseEncoding(EncodingEnum.JSON);

	@RegisterExtension
	private final HttpClientExtension myClient = new HttpClientExtension();

	@BeforeEach
	void beforeEach() {
		ourLastIdParam = null;
		ourLastConditionalUrl = null;
		ourLastBodyId = null;
	}

	@Test
	void testConditionalUpdate_bodyId_isPassedToProvider() throws IOException {
		// setup
		Patient patient = new Patient();
		patient.setId("Patient/client-assigned");
		patient.addIdentifier().setSystem("http://acme.org/mrn").setValue("001");

		// execute
		int statusCode = executePut("/Patient?identifier=http://acme.org/mrn%7C001", patient);

		// verify
		assertThat(statusCode).isEqualTo(201);
		assertThat(ourLastConditionalUrl).isEqualTo("Patient?identifier=http://acme.org/mrn%7C001");
		assertThat(ourLastIdParam).isNull();
		assertThat(ourLastBodyId.getIdPart())
			.as("the body id must reach the provider so storage can honour or reject it")
			.isEqualTo("client-assigned");
	}

	@Test
	void testConditionalUpdate_noBodyId_providerSeesNoId() throws IOException {
		// setup
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://acme.org/mrn").setValue("001");

		// execute
		int statusCode = executePut("/Patient?identifier=http://acme.org/mrn%7C001", patient);

		// verify
		assertThat(statusCode).isEqualTo(201);
		assertThat(ourLastIdParam).isNull();
		assertThat(ourLastBodyId.isEmpty()).isTrue();
	}

	@Test
	void testUpdate_urlIdAndIfMatch_overrideBodyId() throws IOException {
		// setup
		Patient patient = new Patient();
		patient.setId("Patient/abc/_history/7");
		patient.addIdentifier().setSystem("http://acme.org/mrn").setValue("001");

		HttpPut httpPut = newPut("/Patient/abc", patient);
		httpPut.addHeader(Constants.HEADER_IF_MATCH, "W/\"3\"");

		// execute
		int statusCode = execute(httpPut);

		// verify
		assertThat(statusCode).isEqualTo(200);
		assertThat(ourLastConditionalUrl).isNull();
		assertThat(ourLastIdParam.getValue()).isEqualTo("Patient/abc/_history/3");
		assertThat(ourLastBodyId.getValue())
			.as("for a plain update the URL id (with the If-Match version) is authoritative")
			.isEqualTo("Patient/abc/_history/3");
	}

	@Test
	void testUpdate_mismatchedBodyId_rejectedWith420() throws IOException {
		// setup
		Patient patient = new Patient();
		patient.setId("Patient/other");
		patient.addIdentifier().setSystem("http://acme.org/mrn").setValue("001");

		// execute
		HttpPut httpPut = newPut("/Patient/abc", patient);
		try (CloseableHttpResponse response = myClient.execute(httpPut)) {
			String responseContent = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("{}\n{}", response.getStatusLine(), responseContent);

			// verify
			assertThat(response.getStatusLine().getStatusCode()).isEqualTo(400);
			assertThat(responseContent).contains("HAPI-0420");
		}
		assertThat(ourLastBodyId).as("the provider must not be invoked").isNull();
	}

	private int executePut(String thePath, Patient thePatient) throws IOException {
		return execute(newPut(thePath, thePatient));
	}

	private HttpPut newPut(String thePath, Patient thePatient) {
		HttpPut httpPut = new HttpPut(myServer.getBaseUrl() + thePath);
		httpPut.setEntity(new StringEntity(
			ourCtx.newJsonParser().encodeResourceToString(thePatient),
			ContentType.create(Constants.CT_FHIR_JSON_NEW, StandardCharsets.UTF_8)));
		return httpPut;
	}

	private int execute(HttpPut theRequest) throws IOException {
		try (CloseableHttpResponse response = myClient.execute(theRequest)) {
			String responseContent = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("{}\n{}", response.getStatusLine(), responseContent);
			return response.getStatusLine().getStatusCode();
		}
	}

	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Update
		public MethodOutcome update(
				@IdParam IdType theId, @ConditionalUrlParam String theConditionalUrl, @ResourceParam Patient thePatient) {
			ourLastIdParam = theId;
			ourLastConditionalUrl = theConditionalUrl;
			ourLastBodyId = thePatient.getIdElement().copy();

			boolean created = theId == null;
			IdType outcomeId = created ? new IdType("Patient", "server-assigned", "1") : theId.withVersion("1");
			return new MethodOutcome(outcomeId, created);
		}
	}
}
