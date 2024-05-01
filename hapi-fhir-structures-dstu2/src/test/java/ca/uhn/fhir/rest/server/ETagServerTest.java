package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class ETagServerTest {

	private static final FhirContext ourCtx = FhirContext.forDstu2Cached();
	private static IdDt ourLastId;

	private static Date ourLastModifiedDate;

	@RegisterExtension
	public static final RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		.setDefaultResponseEncoding(EncodingEnum.XML)
		.registerProvider(new PatientProvider())
		.withPagingProvider(new FifoMemoryPagingProvider(100))
		.setDefaultPrettyPrint(false);

	@RegisterExtension
	public static final HttpClientExtension ourClient = new HttpClientExtension();


	@BeforeEach
	public void before() throws IOException {
		ourLastId = null;
	}

	@Test
	public void testAutomaticNotModified() throws Exception {
		ourLastModifiedDate = new InstantDt("2012-11-25T02:34:45.2222Z").getValue();

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/2");
		httpGet.addHeader(Constants.HEADER_IF_NONE_MATCH, "\"222\"");
		HttpResponse status = ourClient.execute(httpGet);
		try {
			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(Constants.STATUS_HTTP_304_NOT_MODIFIED);
		} finally {
			if (status.getEntity() != null) {
				IOUtils.closeQuietly(status.getEntity().getContent());
			}
		}

	}

	@Test
	public void testETagHeader() throws Exception {
		ourLastModifiedDate = new InstantDt("2012-11-25T02:34:45.2222Z").getValue();

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/2/_history/3");
		HttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent());

			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
			IdentifierDt dt = ourCtx.newXmlParser().parseResource(Patient.class, responseContent).getIdentifierFirstRep();
			assertThat(dt.getSystemElement().getValueAsString()).isEqualTo("2");
			assertThat(dt.getValue()).isEqualTo("3");

			Header cl = status.getFirstHeader(Constants.HEADER_ETAG_LC);
			assertNotNull(cl);
			assertThat(cl.getValue()).isEqualTo("W/\"222\"");
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}
	}

	@Test
	public void testLastModifiedHeader() throws Exception {
		ourLastModifiedDate = new InstantDt("2012-11-25T02:34:45.222Z").getValue();

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/2/_history/3");
		HttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent());

			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
			IdentifierDt dt = ourCtx.newXmlParser().parseResource(Patient.class, responseContent).getIdentifierFirstRep();
			assertThat(dt.getSystemElement().getValueAsString()).isEqualTo("2");
			assertThat(dt.getValue()).isEqualTo("3");

			Header cl = status.getFirstHeader(Constants.HEADER_LAST_MODIFIED_LOWERCASE);
			assertNotNull(cl);
			assertThat(cl.getValue()).isEqualTo("Sun, 25 Nov 2012 02:34:45 GMT");
		} finally {
			if (status.getEntity() != null) {
				IOUtils.closeQuietly(status.getEntity().getContent());
			}
		}
	}

	@Test
	public void testUpdateWithIfMatch() throws Exception {
		Patient p = new Patient();
		p.setId("2");
		p.addIdentifier().setSystem("urn:system").setValue("001");
		String resBody = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPut http;
		http = new HttpPut(ourServer.getBaseUrl() + "/Patient/2");
		http.setEntity(new StringEntity(resBody, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		http.addHeader(Constants.HEADER_IF_MATCH, "\"221\"");
		CloseableHttpResponse status = ourClient.execute(http);
		try {
			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
			assertThat(ourLastId.toUnqualified().getValue()).isEqualTo("Patient/2/_history/221");
		} finally {
			if (status.getEntity() != null) {
				IOUtils.closeQuietly(status.getEntity().getContent());
			}
		}
	}

	@Test
	public void testUpdateWithIfMatchPreconditionFailed() throws Exception {
		Patient p = new Patient();
		p.setId("2");
		p.addIdentifier().setSystem("urn:system").setValue("001");
		String resBody = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPut http;
		http = new HttpPut(ourServer.getBaseUrl() + "/Patient/2");
		http.setEntity(new StringEntity(resBody, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		http.addHeader(Constants.HEADER_IF_MATCH, "\"222\"");
		CloseableHttpResponse status = ourClient.execute(http);
		try {
			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(Constants.STATUS_HTTP_412_PRECONDITION_FAILED);
			assertThat(ourLastId.toUnqualified().getValue()).isEqualTo("Patient/2/_history/222");
		} finally {
			if (status.getEntity() != null) {
				IOUtils.closeQuietly(status.getEntity().getContent());
			}
		}
	}

	@Test
	public void testUpdateWithNoVersion() throws Exception {
		Patient p = new Patient();
		p.setId("2");
		p.addIdentifier().setSystem("urn:system").setValue("001");
		String resBody = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPut http;
		http = new HttpPut(ourServer.getBaseUrl() + "/Patient/2");
		http.setEntity(new StringEntity(resBody, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(http);
		try {
			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		} finally {
			if (status.getEntity() != null) {
				IOUtils.closeQuietly(status.getEntity().getContent());
			}
		}

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class PatientProvider implements IResourceProvider {

		@Read(version = true)
		public Patient findPatient(@IdParam IdDt theId) {
			Patient patient = new Patient();
			ResourceMetadataKeyEnum.UPDATED.put(patient, new InstantDt(ourLastModifiedDate));
			patient.addIdentifier().setSystem(theId.getIdPart()).setValue(theId.getVersionIdPart());
			patient.setId(theId.withVersion("222"));
			return patient;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Update
		public MethodOutcome updatePatient(@IdParam IdDt theId, @ResourceParam Patient theResource) {
			ourLastId = theId;

			if ("222".equals(theId.getVersionIdPart())) {
				throw new PreconditionFailedException("Bad version");
			}

			return new MethodOutcome(theId.withVersion(theId.getVersionIdPart() + "0"));
		}

	}

}
