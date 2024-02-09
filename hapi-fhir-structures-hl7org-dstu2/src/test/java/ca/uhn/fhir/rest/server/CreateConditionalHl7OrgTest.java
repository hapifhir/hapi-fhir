package ca.uhn.fhir.rest.server;

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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.dstu2.model.IdType;
import org.hl7.fhir.dstu2.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class CreateConditionalHl7OrgTest {
	private static String ourLastConditionalUrl;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CreateConditionalHl7OrgTest.class);
	private static IdType ourLastId;
	private static IdType ourLastIdParam;
	private static boolean ourLastRequestWasSearch;
	private static final FhirContext ourCtx = FhirContext.forDstu2Hl7OrgCached();

  @RegisterExtension
  public static RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
      .registerProvider(new PatientProvider())
      .withPagingProvider(new FifoMemoryPagingProvider(100))
      .setDefaultResponseEncoding(EncodingEnum.JSON)
      .setDefaultPrettyPrint(false);

  @RegisterExtension
  public static HttpClientExtension ourClient = new HttpClientExtension();

	
	@BeforeEach
	public void before() {
		ourLastId = null;
		ourLastConditionalUrl = null;
		ourLastIdParam = null;
		ourLastRequestWasSearch = false;
	}

	@Test
	public void testCreateWithConditionalUrl() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.addHeader(Constants.HEADER_IF_NONE_EXIST, "Patient?identifier=system%7C001");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(201);
		assertThat(status.getFirstHeader("location").getValue()).isEqualTo(ourServer.getBaseUrl() + "/Patient/001/_history/002");
		assertThat(status.getFirstHeader("content-location").getValue()).isEqualTo(ourServer.getBaseUrl() + "/Patient/001/_history/002");

		assertThat(ourLastId.getValue()).isNull();
		assertThat(ourLastIdParam).isNull();
		assertThat(ourLastConditionalUrl).isEqualTo("Patient?identifier=system%7C001");

	}

	@Test
	public void testCreateWithoutConditionalUrl() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient?_format=true&_pretty=true");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(201);
		assertThat(status.getFirstHeader("location").getValue()).isEqualTo(ourServer.getBaseUrl() + "/Patient/001/_history/002");
		assertThat(status.getFirstHeader("content-location").getValue()).isEqualTo(ourServer.getBaseUrl() + "/Patient/001/_history/002");

		assertThat(ourLastId.getValue()).isNull();
		assertThat(ourLastIdParam).isNull();
		assertThat(ourLastConditionalUrl).isNull();

	}

	@Test
	public void testSearchStillWorks() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_pretty=true");

		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(ourLastRequestWasSearch).isTrue();
		assertThat(ourLastId).isNull();
		assertThat(ourLastIdParam).isNull();
		assertThat(ourLastConditionalUrl).isNull();

	}

	
	@AfterAll
	public static void afterClass() throws Exception {
    TestUtil.randomizeLocaleAndTimezone();
	}
		
	
	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Search
		public List<IResource> search(@OptionalParam(name="foo") StringDt theString) {
			ourLastRequestWasSearch = true;
			return new ArrayList<>();
		}
		
		@Create()
		public MethodOutcome createPatient(@ResourceParam Patient thePatient, @ConditionalUrlParam String theConditional, @IdParam IdType theIdParam) {
			ourLastConditionalUrl = theConditional;
			ourLastId = thePatient.getIdElement();
			ourLastIdParam = theIdParam;
			return new MethodOutcome(new IdType("Patient/001/_history/002"));
		}

	}

}
