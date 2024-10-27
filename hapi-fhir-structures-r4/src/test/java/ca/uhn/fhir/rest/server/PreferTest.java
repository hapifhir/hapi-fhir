package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
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
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PreferTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PreferTest.class);
	private static IBaseOperationOutcome ourReturnOperationOutcome;

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new PatientProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .setDefaultPrettyPrint(false);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourReturnOperationOutcome = null;
	}

	@Test
	public void testCreatePreferMinimalNoOperationOutcome() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		
		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDiagnostics("DIAG");
		ourReturnOperationOutcome = oo;

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_MINIMAL);
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(Constants.STATUS_HTTP_201_CREATED, status.getStatusLine().getStatusCode());
		assertThat(responseContent).isNullOrEmpty();
		// assertThat(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).doesNotContain("fhir");
		assertNull(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE));
		assertEquals(ourServer.getBaseUrl() + "/Patient/001/_history/002", status.getFirstHeader("location").getValue());
		assertEquals(ourServer.getBaseUrl() + "/Patient/001/_history/002", status.getFirstHeader("content-location").getValue());

	}

	@Test
	public void testCreatePreferOperationOutcomeWithOperationOutcome() throws Exception {

		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDiagnostics("DIAG");
		ourReturnOperationOutcome = oo;

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(Constants.STATUS_HTTP_201_CREATED, status.getStatusLine().getStatusCode());
		assertThat(responseContent).contains("DIAG");
		assertEquals("application/xml+fhir;charset=utf-8", status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue().toLowerCase().replace(" ", ""));
		assertEquals(ourServer.getBaseUrl() + "/Patient/001/_history/002", status.getFirstHeader("location").getValue());
		assertEquals(ourServer.getBaseUrl() + "/Patient/001/_history/002", status.getFirstHeader("content-location").getValue());

	}

	@Test
	public void testCreatePreferRepresentation() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_REPRESENTATION);
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(Constants.STATUS_HTTP_201_CREATED, status.getStatusLine().getStatusCode());
		assertThat(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).contains(Constants.CT_FHIR_XML);
		assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"001\"/><meta><versionId value=\"002\"/></meta><identifier><value value=\"002\"/></identifier></Patient>", responseContent);
		assertEquals(ourServer.getBaseUrl() + "/Patient/001/_history/002", status.getFirstHeader("location").getValue());
		assertEquals(ourServer.getBaseUrl() + "/Patient/001/_history/002", status.getFirstHeader("content-location").getValue());

	}

	@Test
	public void testCreateWithNoPrefer() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertEquals(ourServer.getBaseUrl() + "/Patient/001/_history/002", status.getFirstHeader("location").getValue());
		assertEquals(ourServer.getBaseUrl() + "/Patient/001/_history/002", status.getFirstHeader("content-location").getValue());

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}


	public static class PatientProvider implements IResourceProvider {

		@Create()
		public MethodOutcome createPatient(@ResourceParam Patient thePatient) {
			IdType id = new IdType("Patient/001/_history/002");
			MethodOutcome retVal = new MethodOutcome(id);

			thePatient.setId(id);
			retVal.setResource(thePatient);

			retVal.setOperationOutcome(ourReturnOperationOutcome);

			return retVal;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Update()
		public MethodOutcome updatePatient(@ResourceParam Patient thePatient, @IdParam IdType theIdParam) {
			IdDt id = new IdDt("Patient/001/_history/002");
			MethodOutcome retVal = new MethodOutcome(id);

			thePatient.setId(id);
			retVal.setResource(thePatient);

			return retVal;
		}

	}

}
