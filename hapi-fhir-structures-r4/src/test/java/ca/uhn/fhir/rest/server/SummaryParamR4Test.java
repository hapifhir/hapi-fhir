package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SummaryParamR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SummaryParamR4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static SummaryEnum ourLastSummary;
	private static List<SummaryEnum> ourLastSummaryList;

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new DummyPatientResourceProvider())
		 .registerProvider(new DummyMedicationRequestProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.XML);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourLastSummary = null;
		ourLastSummaryList = null;
	}

	@Test
	public void testReadSummaryData() throws Exception {
		verifyXmlAndJson(
			ourServer.getBaseUrl() + "/Patient/1?_summary=" + SummaryEnum.DATA.getCode(),
			Patient.class,
			patient -> {
				String responseContent = ourCtx.newXmlParser().encodeResourceToString(patient);
				assertThat(responseContent).doesNotContain("<Bundle");
				assertThat(responseContent).contains("<Patien");
				assertThat(responseContent).doesNotContain("<div>THE DIV</div>");
				assertThat(responseContent).contains("family");
				assertThat(responseContent).contains("maritalStatus");
				assertEquals(SummaryEnum.DATA, ourLastSummary);
			}
		);
	}


	@Test
	public void testReadSummaryText() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1?_summary=" + SummaryEnum.TEXT.getCode());
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(Constants.CT_HTML_WITH_UTF8.replace(" ", "").toLowerCase(), status.getEntity().getContentType().getValue().replace(" ", "").replace("UTF", "utf"));
			assertThat(responseContent).doesNotContain("<Bundle");
			assertThat(responseContent).doesNotContain("<Medic");
			assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">THE DIV</div>", responseContent);
			assertThat(responseContent).doesNotContain("efer");
			assertEquals(SummaryEnum.TEXT, ourLastSummary);
		}
	}

	@Test
	public void testReadSummaryTextWithMandatory() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/MedicationRequest/1?_summary=" + SummaryEnum.TEXT.getCode());
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(Constants.CT_HTML_WITH_UTF8.replace(" ", "").toLowerCase(), status.getEntity().getContentType().getValue().replace(" ", "").replace("UTF", "utf"));
			assertThat(responseContent).doesNotContain("<Bundle");
			assertThat(responseContent).doesNotContain("<Patien");
			assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">TEXT</div>", responseContent);
			assertThat(responseContent).doesNotContain("family");
			assertThat(responseContent).doesNotContain("maritalStatus");
		}
	}

	@Test
	public void testReadSummaryTrue() throws Exception {
		String url = ourServer.getBaseUrl() + "/Patient/1?_summary=" + SummaryEnum.TRUE.getCode();
		verifyXmlAndJson(
			url,
			Patient.class,
			patient -> {
				String responseContent = ourCtx.newXmlParser().encodeResourceToString(patient);
				assertThat(responseContent).doesNotContain("<Bundle");
				assertThat(responseContent).contains("<Patien");
				assertThat(responseContent).doesNotContain("<div>THE DIV</div>");
				assertThat(responseContent).contains("family");
				assertThat(responseContent).doesNotContain("maritalStatus");
				assertEquals(SummaryEnum.TRUE, ourLastSummary);
			}
		);

	}

	@Test
	public void testSearchSummaryCount() throws Exception {
		String url = ourServer.getBaseUrl() + "/Patient?_pretty=true&_summary=" + SummaryEnum.COUNT.getCode();
		verifyXmlAndJson(
			url,
			bundle -> {
				String responseContent = ourCtx.newXmlParser().encodeResourceToString(bundle);
				assertThat(responseContent).contains("<total value=\"1\"/>");
				assertThat(responseContent).doesNotContain("entry");
				assertThat(responseContent).doesNotContain("THE DIV");
				assertThat(responseContent).doesNotContain("family");
				assertThat(responseContent).doesNotContain("maritalStatus");
				assertEquals(SummaryEnum.COUNT, ourLastSummary);
			}
		);
	}

	@Test
	public void testSearchSummaryCountAndData() throws Exception {
		String url = ourServer.getBaseUrl() + "/Patient?_pretty=true&_summary=" + SummaryEnum.COUNT.getCode() + "," + SummaryEnum.DATA.getCode();
		verifyXmlAndJson(
			url,
			bundle -> {
				String responseContent = ourCtx.newXmlParser().encodeResourceToString(bundle);
				assertThat(responseContent).contains("<total value=\"1\"/>");
				assertThat(responseContent).contains("entry");
				assertThat(responseContent).doesNotContain("THE DIV");
				assertThat(responseContent).contains("family");
				assertThat(responseContent).contains("maritalStatus");
			}
		);
	}

	@Test
	public void testSearchSummaryData() throws Exception {
		String url = ourServer.getBaseUrl() + "/Patient?_summary=" + SummaryEnum.DATA.getCode();
		verifyXmlAndJson(
			url,
			bundle -> {
				String responseContent = ourCtx.newXmlParser().encodeResourceToString(bundle);
				assertThat(responseContent).contains("<Patient");
				assertThat(responseContent).doesNotContain("THE DIV");
				assertThat(responseContent).contains("family");
				assertThat(responseContent).contains("maritalStatus");
				assertEquals(SummaryEnum.DATA, ourLastSummary);
			}
		);

	}

	@Test
	public void testSearchSummaryFalse() throws Exception {
		String url = ourServer.getBaseUrl() + "/Patient?_summary=false";
		verifyXmlAndJson(
			url,
			bundle -> {
				String responseContent = ourCtx.newXmlParser().encodeResourceToString(bundle);
				assertThat(responseContent).contains("<Patient");
				assertThat(responseContent).contains("THE DIV");
				assertThat(responseContent).contains("family");
				assertThat(responseContent).contains("maritalStatus");
				assertEquals(SummaryEnum.FALSE, ourLastSummary);
			}
		);
	}

	@Test
	public void testSearchSummaryText() throws Exception {
		String url = ourServer.getBaseUrl() + "/Patient?_summary=" + SummaryEnum.TEXT.getCode();
		verifyXmlAndJson(
			url,
			bundle -> {
				String responseContent = ourCtx.newXmlParser().encodeResourceToString(bundle);
				assertThat(responseContent).contains("<total value=\"1\"/>");
				assertThat(responseContent).contains("entry");
				assertThat(responseContent).contains("THE DIV");
				assertThat(responseContent).doesNotContain("family");
				assertThat(responseContent).doesNotContain("maritalStatus");
				assertEquals(SummaryEnum.TEXT, ourLastSummary);
			}
		);
	}

	@Test
	public void testSearchSummaryTextWithMandatory() throws Exception {
		String url = ourServer.getBaseUrl() + "/MedicationRequest?_summary=" + SummaryEnum.TEXT.getCode() + "&_pretty=true";
		verifyXmlAndJson(
			url,
			bundle -> {
				assertEquals(0, bundle.getMeta().getTag().size());
				String responseContent = ourCtx.newXmlParser().encodeResourceToString(bundle);
				assertThat(responseContent).contains("<total value=\"1\"/>");
				assertThat(responseContent).contains("entry");
				assertThat(responseContent).contains(">TEXT<");
				assertThat(responseContent).contains("Medication/123");
				assertThat(responseContent).doesNotContainIgnoringCase("note");
			}
		);

	}

	@Test
	public void testSearchSummaryTextMulti() throws Exception {
		String url = ourServer.getBaseUrl() + "/Patient?_query=multi&_summary=" + SummaryEnum.TEXT.getCode();
		verifyXmlAndJson(
			url,
			bundle -> {
				String responseContent = ourCtx.newXmlParser().encodeResourceToString(bundle);
				assertThat(responseContent).contains("<total value=\"1\"/>");
				assertThat(responseContent).contains("entry");
				assertThat(responseContent).contains("THE DIV");
				assertThat(responseContent).doesNotContain("family");
				assertThat(responseContent).doesNotContain("maritalStatus");
				assertThat(ourLastSummaryList).containsExactly(SummaryEnum.TEXT);
			}
		);
	}

	@Test
	public void testSearchSummaryTrue() throws Exception {
		String url = ourServer.getBaseUrl() + "/Patient?_summary=" + SummaryEnum.TRUE.getCode();
		verifyXmlAndJson(
			url,
			bundle -> {
				String responseContent = ourCtx.newXmlParser().encodeResourceToString(bundle);
				assertThat(responseContent).contains("<Patient");
				assertThat(responseContent).doesNotContain("THE DIV");
				assertThat(responseContent).contains("family");
				assertThat(responseContent).doesNotContain("maritalStatus");
				assertEquals(SummaryEnum.TRUE, ourLastSummary);
			}
		);

	}

	@Test
	public void testSearchSummaryWithTextAndOthers() throws Exception {
		String url = ourServer.getBaseUrl() + "/Patient?_summary=text&_summary=data";
		try (CloseableHttpResponse status = ourClient.execute(new HttpGet(url))) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertEquals(400, status.getStatusLine().getStatusCode());
			assertThat(responseContent).contains("Can not combine _summary=text with other values for _summary");
		}
	}

	private void verifyXmlAndJson(String theUri, Consumer<Bundle> theVerifier) throws IOException {
		verifyXmlAndJson(theUri, Bundle.class, theVerifier);
	}

	private <T extends IBaseResource> void verifyXmlAndJson(String theUri, Class<T> theType, Consumer<T> theVerifier) throws IOException {
		EncodingEnum encodingEnum;
		HttpGet httpGet;

		encodingEnum = EncodingEnum.JSON;
		httpGet = new HttpGet(theUri + "&_pretty=true&_format=" + encodingEnum.getFormatContentType());
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);
			T response = encodingEnum.newParser(ourCtx).parseResource(theType, responseContent);
			theVerifier.accept(response);
		}

		encodingEnum = EncodingEnum.XML;
		httpGet = new HttpGet(theUri + "&_pretty=true&_format=" + encodingEnum.getFormatContentType());
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);
			T response = encodingEnum.newParser(ourCtx).parseResource(theType, responseContent);
			theVerifier.accept(response);
		}
	}

	public static class DummyMedicationRequestProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return MedicationRequest.class;
		}

		@Read
		public MedicationRequest read(@IdParam IdType theId) {
			MedicationRequest retVal = new MedicationRequest();
			retVal.getText().setDivAsString("<div>TEXT</div>");
			retVal.addNote().setText("NOTE");
			retVal.setMedication(new Reference("Medication/123"));
			retVal.setId(theId);
			return retVal;
		}

		@Search
		public List<MedicationRequest> read() {
			return Collections.singletonList(read(new IdType("999")));
		}

	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdType theId, SummaryEnum theSummary) {
			ourLastSummary = theSummary;
			Patient patient = new Patient();
			patient.setId("Patient/1/_history/1");
			patient.getText().setDivAsString("<div>THE DIV</div>");
			patient.addName().setFamily("FAMILY");
			patient.getMaritalStatus().addCoding().setCode("D");
			return patient;
		}

		@Search(queryName = "multi")
		public Patient search(List<SummaryEnum> theSummary) {
			ourLastSummaryList = theSummary;
			Patient patient = new Patient();
			patient.setId("Patient/1/_history/1");
			patient.getText().setDivAsString("<div>THE DIV</div>");
			patient.addName().setFamily("FAMILY");
			patient.getMaritalStatus().addCoding().setCode("D");
			return patient;
		}

		@Search()
		public Patient search(SummaryEnum theSummary) {
			ourLastSummary = theSummary;
			Patient patient = new Patient();
			patient.setId("Patient/1/_history/1");
			patient.getText().setDivAsString("<div>THE DIV</div>");
			patient.addName().setFamily("FAMILY");
			patient.getMaritalStatus().addCoding().setCode("D");
			return patient;
		}

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
