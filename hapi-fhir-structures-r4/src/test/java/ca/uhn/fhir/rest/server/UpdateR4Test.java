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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies what the server framework hands to an {@link Update} provider for plain and conditional PUTs, in
 * particular that a client-supplied id in the resource body survives a conditional PUT's trip through
 * {@code UpdateMethodBinding}.
 */
// Created by Claude Fable 5.1
public class UpdateR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(UpdateR4Test.class);
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

	/**
	 * What the framework hands to the provider, per request shape. For a conditional PUT the URL carries no id, so
	 * the {@code @IdParam} is null and the body id (if any) is passed through untouched. For a plain PUT the URL id,
	 * with the If-Match version folded in, is authoritative and replaces the body id.
	 */
	@ParameterizedTest(name = "{0}")
	@CsvSource(
		textBlock = """
		# name,                                                                path,                                          ifMatch, bodyId,                  status, expectedIdParam,        expectedConditionalUrl,                       expectedBodyId
		'conditional PUT, body id: passed through to the provider',            /Patient?identifier=http://acme.org/mrn%7C001,        , Patient/client-assigned, 201,    ,                       Patient?identifier=http://acme.org/mrn%7C001, Patient/client-assigned
		'conditional PUT, no body id: provider sees no id',                    /Patient?identifier=http://acme.org/mrn%7C001,        ,                        , 201,    ,                       Patient?identifier=http://acme.org/mrn%7C001,
		'plain PUT with If-Match: URL id and ETag version override body id',   /Patient/abc,                                  W/"3",   Patient/abc/_history/7,  200,    Patient/abc/_history/3, ,                                             Patient/abc/_history/3
		""")
	void testUpdate_idsHandedToProvider(
			String theName,
			String thePath,
			String theIfMatch,
			String theBodyId,
			int theExpectedStatus,
			String theExpectedIdParam,
			String theExpectedConditionalUrl,
			String theExpectedBodyId)
			throws IOException {
		// execute
		PutResponse response = put(thePath, theBodyId, theIfMatch);

		// verify
		assertThat(response.statusCode()).isEqualTo(theExpectedStatus);
		assertThat(ourLastConditionalUrl).isEqualTo(theExpectedConditionalUrl);
		if (theExpectedIdParam == null) {
			assertThat(ourLastIdParam).isNull();
		} else {
			assertThat(ourLastIdParam.getValue()).isEqualTo(theExpectedIdParam);
		}
		if (theExpectedBodyId == null) {
			assertThat(ourLastBodyId.isEmpty()).isTrue();
		} else {
			assertThat(ourLastBodyId.getValue()).isEqualTo(theExpectedBodyId);
		}
	}

	@Test
	void testUpdate_mismatchedBodyId_rejectedWith420() throws IOException {
		// execute
		PutResponse response = put("/Patient/abc", "Patient/other", null);

		// verify
		assertThat(response.statusCode()).isEqualTo(400);
		assertThat(response.body()).contains("HAPI-0420");
		assertThat(ourLastBodyId).as("the provider must not be invoked").isNull();
	}

	private record PutResponse(int statusCode, String body) {}

	private PutResponse put(String thePath, String theBodyId, String theIfMatch) throws IOException {
		Patient patient = new Patient();
		patient.setId(theBodyId);
		patient.addIdentifier().setSystem("http://acme.org/mrn").setValue("001");

		HttpPut httpPut = new HttpPut(myServer.getBaseUrl() + thePath);
		httpPut.setEntity(new StringEntity(
			ourCtx.newJsonParser().encodeResourceToString(patient),
			ContentType.create(Constants.CT_FHIR_JSON_NEW, StandardCharsets.UTF_8)));
		if (theIfMatch != null) {
			httpPut.addHeader(Constants.HEADER_IF_MATCH, theIfMatch);
		}

		try (CloseableHttpResponse response = myClient.execute(httpPut)) {
			String responseContent = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("{}\n{}", response.getStatusLine(), responseContent);
			return new PutResponse(response.getStatusLine().getStatusCode(), responseContent);
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
