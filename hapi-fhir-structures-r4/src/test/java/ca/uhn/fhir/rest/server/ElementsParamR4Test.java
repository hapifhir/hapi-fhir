package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.Elements;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElementsParamR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ElementsParamR4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static Set<String> ourLastElements;
	private static Procedure ourNextProcedure;
	private static Observation ourNextObservation;

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 		.registerProvider(new DummyPatientResourceProvider())
		.registerProvider(new DummyProcedureResourceProvider())
		.registerProvider(new DummyObservationResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.XML);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourLastElements = null;
		ourNextProcedure = null;
		ourServer.getRestfulServer().setElementsSupport(new RestfulServer().getElementsSupport());
	}

	@Test
	public void testElementsOnChoiceWithGenericName() throws IOException {
		createObservationWithQuantity();
		verifyXmlAndJson(
			ourServer.getBaseUrl() + "/Observation?_elements=value,status",
			bundle -> {
				Observation obs = (Observation) bundle.getEntry().get(0).getResource();
				assertEquals("SUBSETTED", obs.getMeta().getTag().get(0).getCode());
				assertEquals(Observation.ObservationStatus.FINAL, obs.getStatus());
				assertEquals("222", obs.getValueQuantity().getValueElement().getValueAsString());
				assertEquals("mg", obs.getValueQuantity().getCode());
			});
	}

	@Test
	public void testElementsOnChoiceWithSpecificName() throws IOException {
		createObservationWithQuantity();
		verifyXmlAndJson(
			ourServer.getBaseUrl() + "/Observation?_elements=valueQuantity,status",
			bundle -> {
				Observation obs = (Observation) bundle.getEntry().get(0).getResource();
				assertEquals("SUBSETTED", obs.getMeta().getTag().get(0).getCode());
				assertEquals(Observation.ObservationStatus.FINAL, obs.getStatus());
				assertEquals("222", obs.getValueQuantity().getValueElement().getValueAsString());
				assertEquals("mg", obs.getValueQuantity().getCode());
			});
	}

	@Test
	@Disabled
	public void testElementsOnChoiceWithSpecificNameNotMatching() throws IOException {
		createObservationWithQuantity();
		verifyXmlAndJson(
			ourServer.getBaseUrl() + "/Observation?_elements=valueString,status",
			bundle -> {
				Observation obs = (Observation) bundle.getEntry().get(0).getResource();
				assertEquals("SUBSETTED", obs.getMeta().getTag().get(0).getCode());
				assertEquals(Observation.ObservationStatus.FINAL, obs.getStatus());
				assertEquals(null, obs.getValueQuantity());
			});
	}

	@Test
	public void testExcludeResources() throws IOException {
		createProcedureWithLongChain();
		verifyXmlAndJson(
			ourServer.getBaseUrl() + "/Procedure?_include=*&_elements:exclude=Procedure,DiagnosticReport,*.meta",
			bundle -> {
				assertEquals(null, bundle.getEntry().get(0).getResource());
				assertEquals(null, bundle.getEntry().get(1).getResource());

				Observation obs = (Observation) bundle.getEntry().get(2).getResource();
				assertEquals(true, obs.getMeta().isEmpty());
				assertEquals(Observation.ObservationStatus.FINAL, obs.getStatus());
				assertEquals(1, obs.getCode().getCoding().size());
				assertEquals("STRING VALUE", obs.getValueStringType().getValue());
			});
	}

	@Test
	public void testInvalidInclude() throws IOException {
		createProcedureWithLongChain();
		EncodingEnum encodingEnum;
		HttpGet httpGet;

		encodingEnum = EncodingEnum.JSON;
		httpGet = new HttpGet((ourServer.getBaseUrl() + "/Procedure?_include=*&_elements=DiagnosticReport:foo") + "&_pretty=true&_format=" + encodingEnum.getFormatContentType());
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(400, status.getStatusLine().getStatusCode());
		}

	}

	private void createObservationWithQuantity() {
		ourNextObservation = new Observation();
		ourNextObservation.setId("Observation/123/_history/456");
		ourNextObservation.setStatus(Observation.ObservationStatus.FINAL);
		ourNextObservation.setSubject(new Reference("Patient/AAA"));
		ourNextObservation.setValue(new Quantity()
			.setValue(222)
			.setCode("mg")
			.setSystem("http://unitsofmeasure.org"));
	}

	@Test
	public void testReadSummaryData() throws Exception {
		verifyXmlAndJson(
			ourServer.getBaseUrl() + "/Patient/1?_elements=name,maritalStatus",
			Patient.class,
			patient -> {
				String responseContent = ourCtx.newXmlParser().encodeResourceToString(patient);
				assertThat(responseContent).doesNotContain("<Bundle");
				assertThat(responseContent).contains("<Patient");
				assertThat(responseContent).doesNotContain("<div>THE DIV</div>");
				assertThat(responseContent).contains("family");
				assertThat(responseContent).contains("maritalStatus");
				assertThat(ourLastElements).containsExactlyInAnyOrder("name", "maritalStatus");
			}
		);
	}

	@Test
	public void testReadSummaryTrue() throws Exception {
		verifyXmlAndJson(
			ourServer.getBaseUrl() + "/Patient/1?_elements=name",
			Patient.class,
			patient -> {
				String responseContent = ourCtx.newXmlParser().encodeResourceToString(patient);
				assertThat(responseContent).doesNotContain("<div>THE DIV</div>");
				assertThat(responseContent).contains("family");
				assertThat(responseContent).doesNotContain("maritalStatus");
				assertThat(ourLastElements).containsExactlyInAnyOrder("name");
			}
		);
	}

	@Test
	public void testSearchSummaryData() throws Exception {
		verifyXmlAndJson(
			ourServer.getBaseUrl() + "/Patient?_elements=name,maritalStatus",
			bundle -> {
				assertEquals("1", bundle.getTotalElement().getValueAsString());
				String responseContent = ourCtx.newXmlParser().encodeResourceToString(bundle.getEntry().get(0).getResource());
				assertThat(responseContent).contains("<Patient");
				assertThat(responseContent).doesNotContain("THE DIV");
				assertThat(responseContent).contains("family");
				assertThat(responseContent).contains("maritalStatus");
				assertThat(ourLastElements).containsExactlyInAnyOrder("name", "maritalStatus");
			}
		);
	}

	@Test
	public void testSearchSummaryText() throws Exception {
		verifyXmlAndJson(
			ourServer.getBaseUrl() + "/Patient?_elements=text&_pretty=true",
			bundle -> {
				assertEquals("1", bundle.getTotalElement().getValueAsString());
				String responseContent = ourCtx.newXmlParser().encodeResourceToString(bundle.getEntry().get(0).getResource());
				assertThat(responseContent).contains("THE DIV");
				assertThat(responseContent).doesNotContain("family");
				assertThat(responseContent).doesNotContain("maritalStatus");
				assertThat(ourLastElements).containsExactlyInAnyOrder("text");
			}
		);
	}

	/**
	 * By default the elements apply only to the focal resource in a search
	 * and not any included resources
	 */
	@Test
	public void testStandardElementsFilter() throws IOException {
		createProcedureWithLongChain();
		verifyXmlAndJson(
			ourServer.getBaseUrl() + "/Procedure?_include=*&_elements=reasonCode,status",
			bundle -> {
				Procedure procedure = (Procedure) bundle.getEntry().get(0).getResource();
				assertEquals("SUBSETTED", procedure.getMeta().getTag().get(0).getCode());
				assertEquals("REASON_CODE", procedure.getReasonCode().get(0).getCoding().get(0).getCode());
				assertEquals(0, procedure.getUsedCode().size());

				DiagnosticReport dr = (DiagnosticReport) bundle.getEntry().get(1).getResource();
				assertEquals(0, dr.getMeta().getTag().size());
				assertEquals("Observation/OBSA", dr.getResult().get(0).getReference());

				Observation obs = (Observation) bundle.getEntry().get(2).getResource();
				assertEquals(0, obs.getMeta().getTag().size());
				assertEquals(Observation.ObservationStatus.FINAL, obs.getStatus());
				assertEquals("1234-5", obs.getCode().getCoding().get(0).getCode());
			});
	}

	@Test
	public void testMultiResourceElementsFilter() throws IOException {
		createProcedureWithLongChain();
		verifyXmlAndJson(
			ourServer.getBaseUrl() + "/Procedure?_include=*&_elements=Procedure.reasonCode,Observation.status,Observation.subject,Observation.value",
			bundle -> {
				Procedure procedure = (Procedure) bundle.getEntry().get(0).getResource();
				assertEquals("SUBSETTED", procedure.getMeta().getTag().get(0).getCode());
				assertEquals("REASON_CODE", procedure.getReasonCode().get(0).getCoding().get(0).getCode());
				assertEquals(0, procedure.getUsedCode().size());

				DiagnosticReport dr = (DiagnosticReport) bundle.getEntry().get(1).getResource();
				assertEquals(0, dr.getMeta().getTag().size());

				Observation obs = (Observation) bundle.getEntry().get(2).getResource();
				assertEquals("SUBSETTED", obs.getMeta().getTag().get(0).getCode());
				assertEquals(Observation.ObservationStatus.FINAL, obs.getStatus());
				assertEquals(0, obs.getCode().getCoding().size());
				assertEquals("STRING VALUE", obs.getValueStringType().getValue());
			});
	}

	@Test
	public void testMultiResourceElementsOnExtension() throws IOException {
		ourNextProcedure = new Procedure();
		ourNextProcedure.setId("Procedure/PROC");
		ourNextProcedure.addExtension()
			.setUrl("http://quantity")
			.setValue(Quantity.fromUcum("1.1", "mg"));
		verifyXmlAndJson(
			ourServer.getBaseUrl() + "/Procedure?_elements=Procedure.extension",
			bundle -> {
				Procedure procedure = (Procedure) bundle.getEntry().get(0).getResource();
				assertEquals("SUBSETTED", procedure.getMeta().getTag().get(0).getCode());
				assertEquals(0, procedure.getReasonCode().size());
				assertEquals("http://quantity", procedure.getExtension().get(0).getUrl());
				assertEquals("mg", ((Quantity) procedure.getExtension().get(0).getValue()).getCode());
			});

		verifyXmlAndJson(
			ourServer.getBaseUrl() + "/Procedure?_elements=Procedure.extension.value",
			bundle -> {
				Procedure procedure = (Procedure) bundle.getEntry().get(0).getResource();
				assertEquals("SUBSETTED", procedure.getMeta().getTag().get(0).getCode());
				assertEquals(0, procedure.getReasonCode().size());
				assertEquals("1.1", ((Quantity) procedure.getExtension().get(0).getValue()).getValueElement().getValueAsString());
				assertEquals("mg", ((Quantity) procedure.getExtension().get(0).getValue()).getCode());
			});

		verifyXmlAndJson(
			ourServer.getBaseUrl() + "/Procedure?_elements=Procedure.extension.value.value",
			bundle -> {
				Procedure procedure = (Procedure) bundle.getEntry().get(0).getResource();
				assertEquals("SUBSETTED", procedure.getMeta().getTag().get(0).getCode());
				assertEquals(0, procedure.getReasonCode().size());
				assertEquals("1.1", ((Quantity) procedure.getExtension().get(0).getValue()).getValueElement().getValueAsString());
				assertEquals(null, ((Quantity) procedure.getExtension().get(0).getValue()).getCode());
			});

		verifyXmlAndJson(
			ourServer.getBaseUrl() + "/Procedure?_elements=Procedure.reason",
			bundle -> {
				Procedure procedure = (Procedure) bundle.getEntry().get(0).getResource();
				assertEquals("SUBSETTED", procedure.getMeta().getTag().get(0).getCode());
				assertEquals(0, procedure.getExtension().size());
			});
	}

	@Test
	public void testMultiResourceElementsFilterWithMetadataExcluded() throws IOException {
		createProcedureWithLongChain();
		verifyXmlAndJson(
			ourServer.getBaseUrl() + "/Procedure?_include=*&_elements=Procedure.reasonCode,Observation.status,Observation.subject,Observation.value&_elements:exclude=*.meta",
			bundle -> {
				Procedure procedure = (Procedure) bundle.getEntry().get(0).getResource();
				assertEquals(true, procedure.getMeta().isEmpty());
				assertEquals("REASON_CODE", procedure.getReasonCode().get(0).getCoding().get(0).getCode());
				assertEquals(0, procedure.getUsedCode().size());

				DiagnosticReport dr = (DiagnosticReport) bundle.getEntry().get(1).getResource();
				assertEquals(true, dr.getMeta().isEmpty());

				Observation obs = (Observation) bundle.getEntry().get(2).getResource();
				assertEquals(true, obs.getMeta().isEmpty());
				assertEquals(Observation.ObservationStatus.FINAL, obs.getStatus());
				assertEquals(0, obs.getCode().getCoding().size());
				assertEquals("STRING VALUE", obs.getValueStringType().getValue());
			});
	}

	/**
	 * A search on procedure, with only resource specific elements filters that are specifically
	 * on other resources but Procedure, should not affect the output of the procedure resource.
	 */
	@Test
	public void testMultiResourceElementsFilterDoesntAffectFocalResource() throws IOException {
		createProcedureWithLongChain();
		verifyXmlAndJson(
			ourServer.getBaseUrl() + "/Procedure?_include=*&_elements=Observation.subject",
			bundle -> {
				Procedure procedure = (Procedure) bundle.getEntry().get(0).getResource();
				assertEquals(true, procedure.getMeta().isEmpty());
				assertEquals("REASON_CODE", procedure.getReasonCode().get(0).getCoding().get(0).getCode());
				assertEquals("USED_CODE", procedure.getUsedCode().get(0).getCoding().get(0).getCode());

				DiagnosticReport dr = (DiagnosticReport) bundle.getEntry().get(1).getResource();
				assertEquals(true, dr.getMeta().isEmpty());
				assertEquals(1, dr.getResult().size());

				Observation obs = (Observation) bundle.getEntry().get(2).getResource();
				assertEquals("SUBSETTED", obs.getMeta().getTag().get(0).getCode());
				assertEquals(null, obs.getStatus());
				assertEquals(0, obs.getCode().getCoding().size());
				assertEquals(false, obs.hasValue());
				assertEquals("Patient/123", obs.getSubject().getReference());
			});
	}

	@Test
	public void testMultiResourceElementsFilterWithMetadataExcludedStandardMode() throws IOException {
		ourServer.getRestfulServer().setElementsSupport(ElementsSupportEnum.STANDARD);
		createProcedureWithLongChain();
		verifyXmlAndJson(
			ourServer.getBaseUrl() + "/Procedure?_include=*&_elements=Procedure.reasonCode,Observation.status,Observation.subject,Observation.value&_elements:exclude=*.meta",
			bundle -> {
				Procedure procedure = (Procedure) bundle.getEntry().get(0).getResource();
				assertEquals(true, procedure.getMeta().isEmpty());
				assertEquals("REASON_CODE", procedure.getReasonCode().get(0).getCoding().get(0).getCode());
				assertEquals(1, procedure.getUsedCode().size());

				DiagnosticReport dr = (DiagnosticReport) bundle.getEntry().get(1).getResource();
				assertEquals(true, dr.getMeta().isEmpty());
				assertEquals(1, dr.getResult().size());

				Observation obs = (Observation) bundle.getEntry().get(2).getResource();
				assertEquals(true, obs.getMeta().isEmpty());
				assertEquals(Observation.ObservationStatus.FINAL, obs.getStatus());
				assertEquals(1, obs.getCode().getCoding().size());
				assertEquals("STRING VALUE", obs.getValueStringType().getValue());
			});
	}

	@Test
	public void testElementsFilterWithComplexPath() throws IOException {
		createProcedureWithLongChain();
		verifyXmlAndJson(
			ourServer.getBaseUrl() + "/Procedure?_elements=Procedure.reasonCode.coding.code",
			bundle -> {
				Procedure procedure = (Procedure) bundle.getEntry().get(0).getResource();
				assertEquals("SUBSETTED", procedure.getMeta().getTag().get(0).getCode());
				assertEquals("REASON_CODE", procedure.getReasonCode().get(0).getCoding().get(0).getCode());
				assertEquals(null, procedure.getReasonCode().get(0).getCoding().get(0).getSystem());
				assertEquals(null, procedure.getReasonCode().get(0).getCoding().get(0).getDisplay());
				assertEquals(0, procedure.getUsedCode().size());
			});
	}

	private void createProcedureWithLongChain() {
		ourNextProcedure = new Procedure();
		ourNextProcedure.setId("Procedure/PROC");
		ourNextProcedure.addReasonCode().addCoding().setCode("REASON_CODE").setSystem("REASON_SYSTEM").setDisplay("REASON_DISPLAY");
		ourNextProcedure.addUsedCode().addCoding().setCode("USED_CODE");

		DiagnosticReport dr = new DiagnosticReport();
		dr.setId("DiagnosticReport/DRA");
		ourNextProcedure.addReport().setResource(dr);

		Observation obs = new Observation();
		obs.setId("Observation/OBSA");
		obs.setStatus(Observation.ObservationStatus.FINAL);
		obs.setSubject(new Reference("Patient/123"));
		obs.getCode().addCoding().setSystem("http://loinc.org").setCode("1234-5");
		obs.setValue(new StringType("STRING VALUE"));
		dr.addResult().setResource(obs);
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

	public static class DummyObservationResourceProvider implements IResourceProvider {


		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Observation.class;
		}

		@Search
		public Observation search(@IncludeParam(allow = {"*"}) Collection<Include> theIncludes) {
			return ourNextObservation;
		}

	}

	public static class DummyProcedureResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Procedure.class;
		}

		@Search
		public Procedure search(@IncludeParam(allow = {"*"}) Collection<Include> theIncludes) {
			return ourNextProcedure;
		}

	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdType theId, @Elements Set<String> theElements) {
			ourLastElements = theElements;
			Patient patient = new Patient();
			patient.setId("Patient/1/_history/1");
			patient.getText().getDiv().setValueAsString("<div>THE DIV</div>");
			patient.addName().setFamily("FAMILY");
			patient.getMaritalStatus().addCoding().setCode("D");
			return patient;
		}

		@Search()
		public Patient search(@Elements Set<String> theElements) {
			ourLastElements = theElements;
			Patient patient = new Patient();
			patient.setId("Patient/1/_history/1");
			patient.getText().getDiv().setValueAsString("<div>THE DIV</div>");
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
