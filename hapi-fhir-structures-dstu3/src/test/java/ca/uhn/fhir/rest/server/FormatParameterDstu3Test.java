package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.assertj.core.api.Assertions.assertThat;

public class FormatParameterDstu3Test {

	private static final String VALUE_XML = "<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"p1ReadId\"/><meta><profile value=\"http://foo_profile\"/></meta><identifier><value value=\"p1ReadValue\"/></identifier></Patient>";
	private static final String VALUE_JSON = "{\"resourceType\":\"Patient\",\"id\":\"p1ReadId\",\"meta\":{\"profile\":[\"http://foo_profile\"]},\"identifier\":[{\"value\":\"p1ReadValue\"}]}";
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FormatParameterDstu3Test.class);

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);

	/**
	 * See #346
	 */
	@Test
	public void testFormatXml() throws Exception {
		ourServer.setDefaultResponseEncoding(EncodingEnum.JSON);

		String responseContent = ourServer.fhirRequest("/Patient/123?_format=xml").get().assertStatus(200).getBody();
		ourLog.info(responseContent);

		assertEquals(VALUE_XML, responseContent);
	}

	/**
	 * See #346
	 */
	@Test
	public void testFormatApplicationXml() throws Exception {
		ourServer.setDefaultResponseEncoding(EncodingEnum.JSON);

		String responseContent = ourServer.fhirRequest("/Patient/123?_format=application/xml").get().assertStatus(200).getBody();
		ourLog.info(responseContent);

		assertEquals(VALUE_XML, responseContent);
	}

	/**
	 * See #346
	 */
	@Test
	public void testFormatApplicationXmlFhir() throws Exception {
		ourServer.setDefaultResponseEncoding(EncodingEnum.JSON);

		String responseContent = ourServer.fhirRequest("/Patient/123?_format=application/xml%2Bfhir").get().assertStatus(200).getBody();
		ourLog.info(responseContent);

		assertEquals(VALUE_XML, responseContent);
	}

	/**
	 * See #346
	 */
	@Test
	public void testFormatApplicationXmlFhirUnescaped() throws Exception {
		ourServer.setDefaultResponseEncoding(EncodingEnum.JSON);

		// The plus isn't escaped here, and it should be.. but we'll be lenient
		String responseContent = ourServer.fhirRequest("/Patient/123?_format=application/xml+fhir").get().assertStatus(200).getBody();
		ourLog.info(responseContent);

		assertEquals(VALUE_XML, responseContent);
	}

	/**
	 * See #346
	 */
	@Test
	public void testFormatJson() throws Exception {
		ourServer.setDefaultResponseEncoding(EncodingEnum.XML);

		String responseContent = ourServer.fhirRequest("/Patient/123?_format=json").get().assertStatus(200).getBody();
		ourLog.info(responseContent);

		assertEquals(VALUE_JSON, responseContent);
	}

	/**
	 * See #346
	 */
	@Test
	public void testFormatApplicationJson() throws Exception {
		ourServer.setDefaultResponseEncoding(EncodingEnum.XML);

		String responseContent = ourServer.fhirRequest("/Patient/123?_format=application/json").get().assertStatus(200).getBody();
		ourLog.info(responseContent);

		assertEquals(VALUE_JSON, responseContent);
	}

	/**
	 * See #346
	 */
	@Test
	public void testFormatApplicationJsonFhir() throws Exception {
		ourServer.setDefaultResponseEncoding(EncodingEnum.XML);

		String responseContent = ourServer.fhirRequest("/Patient/123?_format=application/json%2Bfhir").get().assertStatus(200).getBody();
		ourLog.info(responseContent);

		assertEquals(VALUE_JSON, responseContent);
	}

	/**
	 * See #346
	 */
	@Test
	public void testFormatApplicationJsonFhirUnescaped() throws Exception {
		ourServer.setDefaultResponseEncoding(EncodingEnum.XML);

		// The plus isn't escaped here, and it should be.. but we'll be lenient
		String responseContent = ourServer.fhirRequest("/Patient/123?_format=application/json+fhir").get().assertStatus(200).getBody();
		ourLog.info(responseContent);

		assertEquals(VALUE_JSON, responseContent);
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	private static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Read(version = true)
		public Patient read(@IdParam IdType theId) {
			Patient p1 = new MyPatient();
			p1.setId("p1ReadId");
			p1.addIdentifier().setValue("p1ReadValue");
			return p1;
		}

	}

	@ResourceDef(name = "Patient", profile = "http://foo_profile")
	public static class MyPatient extends Patient {

		private static final long serialVersionUID = 1L;

	}

}
