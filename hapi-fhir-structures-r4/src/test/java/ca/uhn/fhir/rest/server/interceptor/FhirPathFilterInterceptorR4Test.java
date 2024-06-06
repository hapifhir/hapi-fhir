package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirPathFilterInterceptorR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirPathFilterInterceptorR4Test.class);
	private static FhirContext ourCtx = FhirContext.forR4();
	@Order(0)
	@RegisterExtension
	public HttpClientExtension myHttpClientExtension = new HttpClientExtension();
	@Order(0)
	@RegisterExtension
	public RestfulServerExtension myServerExtension = new RestfulServerExtension(ourCtx);
	@Order(1)
	@RegisterExtension
	public HashMapResourceProviderExtension<Patient> myPatientProvider = new HashMapResourceProviderExtension<>(myServerExtension, Patient.class);
	@Order(1)
	@RegisterExtension
	public HashMapResourceProviderExtension<MedicationAdministration> myMedicationAdministrationProvider = new HashMapResourceProviderExtension<>(myServerExtension, MedicationAdministration.class);
	@Order(1)
	@RegisterExtension
	public HashMapResourceProviderExtension<Bundle> myBundleProvider = new HashMapResourceProviderExtension<>(myServerExtension, Bundle.class);
	private IGenericClient myClient;
	private String myBaseUrl;
	private CloseableHttpClient myHttpClient;

	@BeforeEach
	public void before() {
		myServerExtension.getRestfulServer().getInterceptorService().unregisterAllInterceptors();
		myServerExtension.getRestfulServer().getInterceptorService().registerInterceptor(new FhirPathFilterInterceptor());

		myClient = myServerExtension.getFhirClient();
		myBaseUrl = "http://localhost:" + myServerExtension.getPort();
		myHttpClient = myHttpClientExtension.getClient();
	}

	@Test
	public void testUnfilteredResponse() throws IOException {
		IIdType patientId = createPatient();

		HttpGet request = new HttpGet(patientId.getValue());
		try (CloseableHttpResponse response = myHttpClient.execute(request)) {
			String responseText = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response:\n{}", responseText);
			assertThat(responseText).contains("\"system\": \"http://identifiers/1\"");
			assertThat(responseText).contains("\"given\": [ \"Homer\", \"Jay\" ]");
		}
	}


	@Test
	public void testUnfilteredResponse_WithResponseHighlightingInterceptor() throws IOException {
		myServerExtension.getRestfulServer().registerInterceptor(new ResponseHighlighterInterceptor());
		final IIdType patientId = createPatient();

		HttpGet request = new HttpGet(patientId.getValue() + "?_format=" + Constants.FORMATS_HTML_JSON);
		try (CloseableHttpResponse response = myHttpClient.execute(request)) {
			String responseText = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response:\n{}", responseText);
			assertThat(responseText).contains("<span class='hlTagName'>&quot;system&quot;</span>: <span class='hlQuot'>&quot;http://identifiers/1&quot;");
			assertThat(responseText).contains("<span class='hlTagName'>&quot;given&quot;</span>: <span class='hlControl'>[</span> <span class='hlTagName'>&quot;Homer&quot;</span><span class='hlControl'>,</span> <span class='hlTagName'>&quot;Jay&quot;</span> ]</div>");
		}
	}

	@Test
	public void testFilteredResponse() throws IOException {
		final IIdType patientId = createPatient();

		HttpGet request = new HttpGet(patientId + "?_fhirpath=Patient.identifier&_pretty=true");
		try (CloseableHttpResponse response = myHttpClient.execute(request)) {
			String responseText = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response:\n{}", responseText);
			assertThat(responseText).contains("\"system\": \"http://identifiers/1\"");
			assertThat(responseText).doesNotContain("\"given\": [ \"Homer\", \"Jay\" ]");
		}

	}

	@Test
	public void testFilteredResponse_ExpressionReturnsExtension() throws IOException {
		final IIdType patientId = createPatient();

		HttpGet request = new HttpGet(patientId + "?_fhirpath=Patient.extension('http://hl7.org/fhir/us/core/StructureDefinition/us-core-race')&_pretty=true");
		try (CloseableHttpResponse response = myHttpClient.execute(request)) {
			String responseText = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response:\n{}", responseText);
			assertThat(responseText).contains("\"url\": \"http://hl7.org/fhir/us/core/StructureDefinition/us-core-race\"");
		}

	}

	@Test
	public void testFilteredResponse_ExpressionReturnsResource() throws IOException {
		final IIdType patientId = createPatient();

		HttpGet request = new HttpGet(patientId + "?_fhirpath=Patient&_pretty=true");
		try (CloseableHttpResponse response = myHttpClient.execute(request)) {
			String responseText = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response:\n{}", responseText);
			assertThat(responseText).contains("\"resource\": {");
			assertThat(responseText).contains("\"system\": \"http://identifiers/1\"");
			assertThat(responseText).contains("\"given\": [ \"Homer\", \"Jay\" ]");
		}

	}

	@Test
	public void testFilteredResponse_ExpressionIsInvalid() throws IOException {
		final IIdType patientId = createPatient();

		HttpGet request = new HttpGet(patientId + "?_fhirpath=" + UrlUtil.escapeUrlParam("***"));
		try (CloseableHttpResponse response = myHttpClient.execute(request)) {
			String responseText = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response:\n{}", responseText);
			assertEquals(400, response.getStatusLine().getStatusCode());
			assertThat(responseText).contains("left operand to * can only have 1 value, but has 8 values");
		}

	}

	@Test
	public void testFilteredResponseBundle() throws IOException {
		createPatient();

		HttpGet request = new HttpGet(myBaseUrl + "/Patient?_fhirpath=Bundle.entry.resource.as(Patient).name&_pretty=true");
		try (CloseableHttpResponse response = myHttpClient.execute(request)) {
			String responseText = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response:\n{}", responseText);
			assertThat(responseText).contains("      \"valueHumanName\": {\n" +
				"        \"family\": \"Simpson\",\n" +
				"        \"given\": [ \"Homer\", \"Jay\" ]\n" +
				"      }");
		}

	}

	@Test
	public void testFilteredResponse_WithResponseHighlightingInterceptor() throws IOException {
		myServerExtension.getRestfulServer().registerInterceptor(new ResponseHighlighterInterceptor());
		final IIdType patientId = createPatient();

		HttpGet request = new HttpGet(patientId + "?_fhirpath=Patient.identifier&_format=" + Constants.FORMATS_HTML_JSON);
		try (CloseableHttpResponse response = myHttpClient.execute(request)) {
			String responseText = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response:\n{}", responseText);
			assertThat(responseText).contains("<span class='hlTagName'>&quot;system&quot;</span>: <span class='hlQuot'>&quot;http://identifiers/1&quot;");
			assertThat(responseText).doesNotContain("<span class='hlTagName'>&quot;given&quot;</span>: <span class='hlControl'>[</span> <span class='hlTagName'>&quot;Homer&quot;</span><span class='hlControl'>,</span> <span class='hlTagName'>&quot;Jay&quot;</span> ]</div>");
		}

	}

	public static Stream<Arguments> getBundleParameters() {
		return Stream.of(
				Arguments.of("Bundle.entry.resource.type", "valueCodeableConcept"),
				Arguments.of("Bundle.entry.resource.ofType(Patient).identifier", "valueIdentifier"),
				Arguments.of("Bundle.entry.resource.ofType(MedicationAdministration).effective", "valuePeriod"),
				Arguments.of("Bundle.entry[0].resource.as(Composition).type", "valueCodeableConcept"),
				Arguments.of("Bundle.entry[0].resource.as(Composition).subject.resolve().as(Patient).identifier", "valueIdentifier"),
				Arguments.of("Bundle.entry[0].resource.as(Composition).section.entry.resolve().as(MedicationAdministration).effective", "valuePeriod")
		);
	}

	@ParameterizedTest
	@MethodSource(value = "getBundleParameters")
	public void testFilteredResponse_withBundleComposition_returnsResult(final String theFhirPathExpression, final String expectedResult) throws IOException {
		IIdType bundle = createBundleDocument();

		HttpGet request = new HttpGet(bundle.getValue() + "?_fhirpath=" + theFhirPathExpression);
		try (CloseableHttpResponse response = myHttpClient.execute(request)) {
			String responseText = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response:\n{}", responseText);
			IBaseResource resource = ourCtx.newJsonParser().parseResource(responseText);
			assertTrue(resource instanceof Parameters);
			Parameters parameters = (Parameters)resource;
			Parameters.ParametersParameterComponent parameterComponent = parameters.getParameter("result");
			assertNotNull(parameterComponent);
			assertThat(parameterComponent.getPart()).hasSize(2);
			Parameters.ParametersParameterComponent resultComponent = parameterComponent.getPart().get(1);
			assertEquals("result", resultComponent.getName());
			assertThat(responseText).contains(expectedResult);
		}

	}

	private IIdType createPatient() {
		Patient p = new Patient();
		p.addExtension()
				.setUrl("http://hl7.org/fhir/us/core/StructureDefinition/us-core-race")
				.addExtension()
				.setUrl("ombCategory")
				.setValue(new Coding("urn:oid:2.16.840.1.113883.6.238", "2106-3", "White"));
		p.setActive(true);
		p.addIdentifier().setSystem("http://identifiers/1").setValue("value-1");
		p.addIdentifier().setSystem("http://identifiers/2").setValue("value-2");
		p.addName().setFamily("Simpson").addGiven("Homer").addGiven("Jay");
		p.addName().setFamily("Simpson").addGiven("Grandpa");
		return myClient.create().resource(p).execute().getId().withServerBase(myBaseUrl, "Patient");
	}

	private IIdType createBundleDocument() {
		Patient patient = new Patient();
		patient.setActive(true);
		patient.addIdentifier().setSystem("http://identifiers/1").setValue("value-1");
		patient.addName().setFamily("Simpson").addGiven("Homer").addGiven("Jay");
		patient = (Patient) myClient.create().resource(patient).execute().getResource();

		MedicationAdministration medicationAdministration = new MedicationAdministration();
		medicationAdministration.setEffective(new Period().setStartElement(DateTimeType.now()));
		medicationAdministration = (MedicationAdministration) myClient.create().resource(medicationAdministration).execute().getResource();

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.DOCUMENT);
		Composition composition = new Composition();
		composition.setType(new CodeableConcept().addCoding(new Coding().setCode("code").setSystem("http://example.org")));
		bundle.addEntry().setResource(composition);
		composition.getSubject().setReference(patient.getIdElement().getValue());
		composition.addSection().addEntry(new Reference(medicationAdministration.getIdElement()));
		bundle.addEntry().setResource(patient);
		bundle.addEntry().setResource(medicationAdministration);

		return myClient.create().resource(bundle).execute().getId().withServerBase(myBaseUrl, "Bundle");
	}
}
