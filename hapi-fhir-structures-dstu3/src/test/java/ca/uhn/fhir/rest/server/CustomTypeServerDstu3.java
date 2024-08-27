package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomTypeServerDstu3 {

	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static String ourLastConditionalUrl;
	private static IdType ourLastId;
	private static IdType ourLastIdParam;
	private static boolean ourLastRequestWasSearch;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CustomTypeServerDstu3.class);

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .registerProvider(new PatientProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourLastId = null;
		ourLastConditionalUrl = null;
		ourLastIdParam = null;
		ourLastRequestWasSearch = false;
	}


	@Test
	public void testCreateWithIdInBody() throws Exception {

		Patient patient = new Patient();
		patient.setId("2");
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
//		httpPost.addHeader(Constants.HEADER_IF_NONE_EXIST, "Patient?identifier=system%7C001");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testCreateWithIdInUrl() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/2");
//		httpPost.addHeader(Constants.HEADER_IF_NONE_EXIST, "Patient?identifier=system%7C001");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(400, status.getStatusLine().getStatusCode());
		OperationOutcome oo = ourCtx.newXmlParser().parseResource(OperationOutcome.class, responseContent);
		assertEquals("HAPI-0365: Can not create resource with ID \"2\", ID must not be supplied on a create (POST) operation (use an HTTP PUT / update operation if you wish to supply an ID)", oo.getIssue().get(0).getDiagnostics());
	}

	@Test
	public void testCreateWithIdInUrlForConditional() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/2");
		httpPost.addHeader(Constants.HEADER_IF_NONE_EXIST, "Patient?identifier=system%7C001");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(400, status.getStatusLine().getStatusCode());
		OperationOutcome oo = ourCtx.newXmlParser().parseResource(OperationOutcome.class, responseContent);
		assertEquals("HAPI-0365: Can not create resource with ID \"2\", ID must not be supplied on a create (POST) operation (use an HTTP PUT / update operation if you wish to supply an ID)", oo.getIssue().get(0).getDiagnostics());
	}


	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class PatientProvider implements IResourceProvider {

		@Create()
		public MethodOutcome createPatient(@ResourceParam Patient thePatient, @ConditionalUrlParam String theConditional, @IdParam IdType theIdParam) {
			ourLastConditionalUrl = theConditional;
			ourLastId = thePatient.getIdElement();
			ourLastIdParam = theIdParam;
			return new MethodOutcome(new IdType("Patient/001/_history/002"));
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Search
		public List<IResource> search(@OptionalParam(name = "foo") StringDt theString) {
			ourLastRequestWasSearch = true;
			return new ArrayList<>();
		}

	}

}
