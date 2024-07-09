package ca.uhn.fhir.jaxrs.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.api.SearchStyleEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.exceptions.InvalidResponseException;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import ca.uhn.fhir.test.utilities.server.RequestCaptureServlet;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleLinkComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class GenericJaxRsClientDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GenericJaxRsClientDstu3Test.class);
	private static final FhirContext ourCtx = FhirContext.forDstu3();

	private static final RequestCaptureServlet MY_SERVLET = new RequestCaptureServlet();

	@RegisterExtension
	public static final HttpServletExtension ourServer = new HttpServletExtension()
		.withServlet(MY_SERVLET)
		.keepAliveBetweenTests();

	@BeforeEach
	public void before() {
		JaxRsRestfulClientFactory clientFactory = new JaxRsRestfulClientFactory(ourCtx);
		clientFactory.setServerValidationMode(ServerValidationModeEnum.NEVER);

		ourCtx.setRestfulClientFactory(clientFactory);
		MY_SERVLET.reset();
		HapiSystemProperties.enableHapiClientKeepResponses();
	}

	private String getPatientFeedWithOneResult() {
		//@formatter:off
		String msg = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" +
			"<id>d039f91a-cc3c-4013-988e-af4d8d0614bd</id>\n" +
			"<entry>\n" +
			"<resource>"
			+ "<Patient>"
			+ "<text><status value=\"generated\" /><div xmlns=\"http://www.w3.org/1999/xhtml\">John Cardinal:            444333333        </div></text>"
			+ "<identifier><label value=\"SSN\" /><system value=\"http://orionhealth.com/mrn\" /><value value=\"PRP1660\" /></identifier>"
			+ "<name><use value=\"official\" /><family value=\"Cardinal\" /><given value=\"John\" /></name>"
			+ "<name><family value=\"Kramer\" /><given value=\"Doe\" /></name>"
			+ "<telecom><system value=\"phone\" /><value value=\"555-555-2004\" /><use value=\"work\" /></telecom>"
			+ "<address><use value=\"home\" /><line value=\"2222 Home Street\" /></address><active value=\"true\" />"
			+ "</Patient>"
			+ "</resource>\n"
			+ "   </entry>\n"
			+ "</Bundle>";
		//@formatter:on
		return msg;
	}

	@Test
	public void testAcceptHeaderFetchConformance() {
		IParser p = ourCtx.newXmlParser();

		CapabilityStatement conf = new CapabilityStatement();
		conf.setCopyright("COPY");

		final String respString = p.encodeResourceToString(conf);
		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		client.fetchConformance().ofType(CapabilityStatement.class).execute();
		assertEquals(ourServer.getBaseUrl() + "/fhir/metadata", MY_SERVLET.ourRequestUri);
		assertThat(MY_SERVLET.ourRequestHeaders.get("Accept")).hasSize(1);
		assertThat(MY_SERVLET.ourRequestHeaders.get("Accept").get(0).getValue()).contains(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_NON_LEGACY);


		client.fetchConformance().ofType(CapabilityStatement.class).encodedJson().execute();
		assertEquals(ourServer.getBaseUrl() + "/fhir/metadata?_format=json", MY_SERVLET.ourRequestUri);
		assertThat(MY_SERVLET.ourRequestHeaders.get("Accept")).hasSize(1);
		assertThat(MY_SERVLET.ourRequestHeaders.get("Accept").get(0).getValue()).contains(Constants.CT_FHIR_JSON);


		client.fetchConformance().ofType(CapabilityStatement.class).encodedXml().execute();
		assertEquals(ourServer.getBaseUrl() + "/fhir/metadata?_format=xml", MY_SERVLET.ourRequestUri);
		assertThat(MY_SERVLET.ourRequestHeaders.get("Accept")).hasSize(1);
		assertThat(MY_SERVLET.ourRequestHeaders.get("Accept").get(0).getValue()).contains(Constants.CT_FHIR_XML);

	}

	@Test
	public void testAcceptHeaderPreflightConformance() {
		final IParser p = ourCtx.newXmlParser();

		final CapabilityStatement conf = new CapabilityStatement();
		conf.setCopyright("COPY");

		final Patient patient = new Patient();
		patient.addName().setFamily("FAMILY");

		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBodies = new String[]{p.encodeResourceToString(conf), p.encodeResourceToString(patient)};

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		Patient resp = client.read(Patient.class, new IdType("123").getValue());
		assertEquals("FAMILY", resp.getName().get(0).getFamily());
		assertEquals(ourServer.getBaseUrl() + "/fhir/metadata", MY_SERVLET.ourRequestUriAll.get(0));
		assertThat(MY_SERVLET.ourRequestHeadersAll.get(0).get("Accept")).hasSize(1);
		assertThat(MY_SERVLET.ourRequestHeadersAll.get(0).get("Accept").get(0).getValue()).contains(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_NON_LEGACY);
		assertThat(MY_SERVLET.ourRequestHeadersAll.get(0).get("Accept").get(0).getValue()).contains(Constants.CT_FHIR_XML);
		assertThat(MY_SERVLET.ourRequestHeadersAll.get(0).get("Accept").get(0).getValue()).contains(Constants.CT_FHIR_JSON);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123", MY_SERVLET.ourRequestUriAll.get(1));
		assertThat(MY_SERVLET.ourRequestHeadersAll.get(1).get("Accept")).hasSize(1);
		assertThat(MY_SERVLET.ourRequestHeadersAll.get(1).get("Accept").get(0).getValue()).contains(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_NON_LEGACY);
		assertThat(MY_SERVLET.ourRequestHeadersAll.get(1).get("Accept").get(0).getValue()).contains(Constants.CT_FHIR_XML);
		assertThat(MY_SERVLET.ourRequestHeadersAll.get(1).get("Accept").get(0).getValue()).contains(Constants.CT_FHIR_JSON);
	}

	@Test
	public void testAcceptHeaderPreflightConformancePreferJson() {
		final IParser p = ourCtx.newXmlParser();

		final CapabilityStatement conf = new CapabilityStatement();
		conf.setCopyright("COPY");

		final Patient patient = new Patient();
		patient.addName().setFamily("FAMILY");

		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBodies = new String[]{p.encodeResourceToString(conf), p.encodeResourceToString(patient)};

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");
		client.setEncoding(EncodingEnum.JSON);

		Patient resp = client.read(Patient.class, new IdType("123").getValue());
		assertEquals("FAMILY", resp.getName().get(0).getFamily());
		assertEquals(ourServer.getBaseUrl() + "/fhir/metadata?_format=json", MY_SERVLET.ourRequestUriAll.get(0));
		assertThat(MY_SERVLET.ourRequestHeadersAll.get(0).get("Accept")).hasSize(1);
		assertThat(MY_SERVLET.ourRequestHeadersAll.get(0).get("Accept").get(0).getValue()).contains(Constants.CT_FHIR_JSON);
		assertThat(MY_SERVLET.ourRequestHeadersAll.get(0).get("Accept").get(0).getValue()).doesNotContain(Constants.CT_FHIR_XML);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123?_format=json", MY_SERVLET.ourRequestUriAll.get(1));
		assertThat(MY_SERVLET.ourRequestHeadersAll.get(1).get("Accept")).hasSize(1);
		assertThat(MY_SERVLET.ourRequestHeadersAll.get(1).get("Accept").get(0).getValue()).contains(Constants.CT_FHIR_JSON);
		assertThat(MY_SERVLET.ourRequestHeadersAll.get(1).get("Accept").get(0).getValue()).doesNotContain(Constants.CT_FHIR_XML);
	}

	@Test
	public void testConformance() {
		IParser p = ourCtx.newXmlParser();

		CapabilityStatement conf = new CapabilityStatement();
		conf.setCopyright("COPY");

		final String respString = p.encodeResourceToString(conf);

		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		//@formatter:off
		CapabilityStatement resp = client.fetchConformance().ofType(CapabilityStatement.class).execute();

		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/metadata", MY_SERVLET.ourRequestUri);
		assertEquals("COPY", resp.getCopyright());
		assertEquals("GET", MY_SERVLET.ourRequestMethod);


	}

	@Test
	public void testProviderWhereWeForgotToSetTheContext() {
		JaxRsRestfulClientFactory clientFactory = new JaxRsRestfulClientFactory(); // no ctx
		clientFactory.setServerValidationMode(ServerValidationModeEnum.NEVER);

		ourCtx.setRestfulClientFactory(clientFactory);

		try {
			ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");
			fail();
		} catch (IllegalStateException e) {
			assertEquals(Msg.code(1355) + "JaxRsRestfulClientFactory does not have FhirContext defined. This must be set via JaxRsRestfulClientFactory#setFhirContext(FhirContext)", e.getMessage());
		}
	}

	@Test
	public void testCreate() {
		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Patient p = new Patient();
		p.addName().setFamily("FOOFAMILY");

		client.create().resource(p).encodedXml().execute();

		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(MY_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient", MY_SERVLET.ourRequestUri);
		assertEquals("POST", MY_SERVLET.ourRequestMethod);


		p.setId("123");

		client.create().resource(p).encodedXml().execute();
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		String body = MY_SERVLET.ourRequestBodyString;
		assertThat(body).contains("<family value=\"FOOFAMILY\"/>");
		assertThat(body).doesNotContain("123");
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient", MY_SERVLET.ourRequestUri);
		assertEquals("POST", MY_SERVLET.ourRequestMethod);


	}

	@Test
	public void testCreateConditional() {


		MY_SERVLET.ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Patient p = new Patient();
		p.addName().setFamily("FOOFAMILY");

		client.create().resource(p).conditionalByUrl("Patient?name=foo").encodedXml().execute();
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(MY_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient", MY_SERVLET.ourRequestUri);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=foo", MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_IF_NONE_EXIST).getValue());
		assertEquals("POST", MY_SERVLET.ourRequestMethod);


		client.create().resource(p).conditionalByUrl("Patient?name=http://foo|bar").encodedXml().execute();
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(MY_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient", MY_SERVLET.ourRequestUri);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=http%3A//foo%7Cbar", MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_IF_NONE_EXIST).getValue());
		assertEquals("POST", MY_SERVLET.ourRequestMethod);


		client.create().resource(p).conditional().where(Patient.NAME.matches().value("foo")).encodedXml().execute();
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(MY_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient", MY_SERVLET.ourRequestUri);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=foo", MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_IF_NONE_EXIST).getValue());
		assertEquals("POST", MY_SERVLET.ourRequestMethod);


	}

	@Test
	public void testCreate2() {
		MY_SERVLET.ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Patient p = new Patient();
		p.addName().setFamily("FOOFAMILY");

		client.create().resource(p).encodedXml().execute();

		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(MY_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient", MY_SERVLET.ourRequestUri);
		assertEquals("POST", MY_SERVLET.ourRequestMethod);

	}

	@Test
	public void testCreatePrefer() {


		MY_SERVLET.ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Patient p = new Patient();
		p.addName().setFamily("FOOFAMILY");

		client.create().resource(p).prefer(PreferReturnEnum.MINIMAL).execute();
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_PREFER)).hasSize(1);
		assertEquals(Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_MINIMAL, MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_PREFER).get(0).getValue());


		client.create().resource(p).prefer(PreferReturnEnum.REPRESENTATION).execute();
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_PREFER)).hasSize(1);
		assertEquals(Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_REPRESENTATION, MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_PREFER).get(0).getValue());


	}

	@Test
	public void testCreateReturningResourceBody() {
		Patient p = new Patient();
		p.setId("123");
		final String formatted = ourCtx.newXmlParser().encodeResourceToString(p);


		MY_SERVLET.ourResponseStatus = Constants.STATUS_HTTP_200_OK;
		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = formatted;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		p = new Patient();
		p.setId(new IdType("1"));
		p.addName().setFamily("FOOFAMILY");

		MethodOutcome output = client.create().resource(p).execute();
		assertNotNull(output.getResource());
		assertEquals("Patient/123", output.getResource().getIdElement().toUnqualifiedVersionless().getValue());
	}

	@Test
	public void testDeleteConditional() {
		MY_SERVLET.ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		client.delete().resourceById(new IdType("Patient/123")).execute();
		assertEquals("DELETE", MY_SERVLET.ourRequestMethod);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123", MY_SERVLET.ourRequestUri);


		client.delete().resourceConditionalByUrl("Patient?name=foo").execute();
		assertEquals("DELETE", MY_SERVLET.ourRequestMethod);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=foo", MY_SERVLET.ourRequestUri);


		client.delete().resourceConditionalByType("Patient").where(Patient.NAME.matches().value("foo")).execute();
		assertEquals("DELETE", MY_SERVLET.ourRequestMethod);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=foo", MY_SERVLET.ourRequestUri);


	}

	@SuppressWarnings("deprecation")
	@Test
	public void testDeleteNonFluent() {
		MY_SERVLET.ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		client.delete().resourceById(new IdType("Patient/123")).execute();
		assertEquals("DELETE", MY_SERVLET.ourRequestMethod);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123", MY_SERVLET.ourRequestUri);

	}

	@Test
	public void testHistory() {

		final String msg = getPatientFeedWithOneResult();

		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		org.hl7.fhir.dstu3.model.Bundle response;

		//@formatter:off
		response = client
			.history()
			.onServer()
			.andReturnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/_history", MY_SERVLET.ourRequestUri);
		assertThat(response.getEntry()).hasSize(1);


		//@formatter:off
		response = client
			.history()
			.onServer()
			.andReturnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
			.since((Date) null)
			.count(null)
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/_history", MY_SERVLET.ourRequestUri);
		assertThat(response.getEntry()).hasSize(1);


		//@formatter:off
		response = client
			.history()
			.onServer()
			.andReturnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
			.since(new InstantType())
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/_history", MY_SERVLET.ourRequestUri);
		assertThat(response.getEntry()).hasSize(1);


		//@formatter:off
		response = client
			.history()
			.onType(Patient.class)
			.andReturnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/_history", MY_SERVLET.ourRequestUri);
		assertThat(response.getEntry()).hasSize(1);


		//@formatter:off
		response = client
			.history()
			.onInstance(new IdType("Patient", "123"))
			.andReturnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/_history", MY_SERVLET.ourRequestUri);
		assertThat(response.getEntry()).hasSize(1);


		//@formatter:off
		response = client
			.history()
			.onInstance(new IdType("Patient", "123"))
			.andReturnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
			.count(123)
			.since(new InstantType("2001-01-02T11:22:33Z"))
			.execute();
		//@formatter:on
		assertThat(MY_SERVLET.ourRequestUri).isIn(ourServer.getBaseUrl() + "/fhir/Patient/123/_history?_since=2001-01-02T11:22:33Z&_count=123",
			ourServer.getBaseUrl() + "/fhir/Patient/123/_history?_count=123&_since=2001-01-02T11:22:33Z");
		assertThat(response.getEntry()).hasSize(1);


		//@formatter:off
		response = client
			.history()
			.onInstance(new IdType("Patient", "123"))
			.andReturnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
			.since(new InstantType("2001-01-02T11:22:33Z").getValue())
			.execute();
		//@formatter:on
		assertThat(MY_SERVLET.ourRequestUri).contains("_since=2001-01");
		assertThat(response.getEntry()).hasSize(1);

	}

	@Test
	public void testMetaAdd() {
		IParser p = ourCtx.newXmlParser();

		Meta inMeta = new Meta().addProfile("urn:profile:in");

		Parameters outParams = new Parameters();
		outParams.addParameter().setName("meta").setValue(new Meta().addProfile("urn:profile:out"));
		final String respString = p.encodeResourceToString(outParams);

		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		//@formatter:off
		Meta resp = client
			.meta()
			.add()
			.onResource(new IdType("Patient/123"))
			.meta(inMeta)
			.encodedXml()
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$meta-add", MY_SERVLET.ourRequestUri);
		assertEquals("urn:profile:out", resp.getProfile().get(0).getValue());
		assertEquals("POST", MY_SERVLET.ourRequestMethod);
		assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"meta\"/><valueMeta><profile value=\"urn:profile:in\"/></valueMeta></parameter></Parameters>", MY_SERVLET.ourRequestBodyString);


	}

	@Test
	public void testMetaGet() {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("meta").setValue(new Meta().addProfile("urn:profile:in"));

		Parameters outParams = new Parameters();
		outParams.addParameter().setName("meta").setValue(new Meta().addProfile("urn:profile:out"));
		final String respString = p.encodeResourceToString(outParams);

		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		//@formatter:off
		Meta resp = client
			.meta()
			.get(Meta.class)
			.fromServer()
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/$meta", MY_SERVLET.ourRequestUri);
		assertEquals("urn:profile:out", resp.getProfile().get(0).getValue());
		assertEquals("GET", MY_SERVLET.ourRequestMethod);


		//@formatter:off
		resp = client
			.meta()
			.get(Meta.class)
			.fromType("Patient")
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$meta", MY_SERVLET.ourRequestUri);
		assertEquals("urn:profile:out", resp.getProfile().get(0).getValue());
		assertEquals("GET", MY_SERVLET.ourRequestMethod);


		//@formatter:off
		resp = client
			.meta()
			.get(Meta.class)
			.fromResource(new IdType("Patient/123"))
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$meta", MY_SERVLET.ourRequestUri);
		assertEquals("urn:profile:out", resp.getProfile().get(0).getValue());
		assertEquals("GET", MY_SERVLET.ourRequestMethod);


	}

	@Test
	public void testOperationAsGetWithInParameters() {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("param1").setValue(new StringType("STRINGVALIN1"));
		inParams.addParameter().setName("param1").setValue(new StringType("STRINGVALIN1b"));
		inParams.addParameter().setName("param2").setValue(new StringType("STRINGVALIN2"));

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringType("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringType("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		//@formatter:off
		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.useHttpGet()
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2", MY_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", MY_SERVLET.ourRequestMethod);


		//@formatter:off
		resp = client
			.operation()
			.onType(Patient.class)
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.useHttpGet()
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2", MY_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", MY_SERVLET.ourRequestMethod);


		//@formatter:off
		resp = client
			.operation()
			.onInstance(new IdType("Patient", "123"))
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.useHttpGet()
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2", MY_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", MY_SERVLET.ourRequestMethod);


		// @formatter:off
		resp = client
			.operation()
			.onInstance(new IdType("http://foo.com/bar/baz/Patient/123/_history/22"))
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.useHttpGet()
			.execute();
		// @formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2", MY_SERVLET.ourRequestUri);

	}

	@Test
	public void testOperationAsGetWithNoInParameters() {
		IParser p = ourCtx.newXmlParser();

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringType("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringType("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);

		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		//@formatter:off
		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.useHttpGet()
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/$SOMEOPERATION", MY_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", MY_SERVLET.ourRequestMethod);


		//@formatter:off
		resp = client
			.operation()
			.onType(Patient.class)
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.useHttpGet()
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$SOMEOPERATION", MY_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", MY_SERVLET.ourRequestMethod);


		//@formatter:off
		resp = client
			.operation()
			.onInstance(new IdType("Patient", "123"))
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.useHttpGet()
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$SOMEOPERATION", MY_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", MY_SERVLET.ourRequestMethod);


		// @formatter:off
		resp = client
			.operation()
			.onInstance(new IdType("http://foo.com/bar/baz/Patient/123/_history/22"))
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.useHttpGet()
			.execute();
		// @formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$SOMEOPERATION", MY_SERVLET.ourRequestUri);

	}

	@Test
	public void testOperationWithBundleResponseJson() {

		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON;
		final String respString = "{\n" + "    \"resourceType\":\"Bundle\",\n" + "    \"id\":\"8cef5f2a-0ba9-43a5-be26-c8dde9ff0e19\",\n" + "    \"base\":\"" + ourServer.getBaseUrl() + "/fhir\"\n" + "}";
		MY_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		client.registerInterceptor(new LoggingInterceptor(true));

		// Create the input parameters to pass to the server
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("start").setValue(new DateType("2001-01-01"));
		inParams.addParameter().setName("end").setValue(new DateType("2015-03-01"));

		// Invoke $everything on "Patient/1"
		Parameters outParams = client.operation().onInstance(new IdType("Patient", "18066")).named("$everything").withParameters(inParams).execute();

		/*
		 * Note that the $everything operation returns a Bundle instead of a Parameters resource. The client operation
		 * methods return a Parameters instance however, so HAPI creates a Parameters object
		 * with a single parameter containing the value.
		 */
		org.hl7.fhir.dstu3.model.Bundle responseBundle = (org.hl7.fhir.dstu3.model.Bundle) outParams.getParameter().get(0).getResource();

		// Print the response bundle
		assertEquals("Bundle/8cef5f2a-0ba9-43a5-be26-c8dde9ff0e19", responseBundle.getId());
	}

	@Test
	public void testOperationWithBundleResponseXml() {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		inParams.addParameter().setValue(new StringType("STRINGVALIN1"));
		inParams.addParameter().setValue(new StringType("STRINGVALIN2"));
		String reqString = p.encodeResourceToString(inParams);

		org.hl7.fhir.dstu3.model.Bundle outParams = new org.hl7.fhir.dstu3.model.Bundle();
		outParams.setTotal(123);
		final String respString = p.encodeResourceToString(outParams);

		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		//@formatter:off
		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.encodedXml()
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/$SOMEOPERATION", MY_SERVLET.ourRequestUri);
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals(MY_SERVLET.ourRequestBodyString, reqString);
		assertEquals("POST", MY_SERVLET.ourRequestMethod);
		assertThat(resp.getParameter()).hasSize(1);
		assertEquals(org.hl7.fhir.dstu3.model.Bundle.class, resp.getParameter().get(0).getResource().getClass());

	}

	@Test
	public void testOperationWithInlineParams() {
		IParser p = ourCtx.newXmlParser();

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringType("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringType("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);

		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		//@formatter:off
		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameter(Parameters.class, "name1", new StringType("value1"))
			.andParameter("name2", new StringType("value1"))
			.encodedXml()
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/$SOMEOPERATION", MY_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals("POST", MY_SERVLET.ourRequestMethod);
		assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"name1\"/><valueString value=\"value1\"/></parameter><parameter><name value=\"name2\"/><valueString value=\"value1\"/></parameter></Parameters>", (MY_SERVLET.ourRequestBodyString));


		/*
		 * Composite type
		 */

		//@formatter:off
		resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameter(Parameters.class, "name1", new Identifier().setSystem("system1").setValue("value1"))
			.andParameter("name2", new StringType("value1"))
			.encodedXml()
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/$SOMEOPERATION", MY_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals("POST", MY_SERVLET.ourRequestMethod);
		assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"name1\"/><valueIdentifier><system value=\"system1\"/><value value=\"value1\"/></valueIdentifier></parameter><parameter><name value=\"name2\"/><valueString value=\"value1\"/></parameter></Parameters>", (MY_SERVLET.ourRequestBodyString));


		/*
		 * Resource
		 */

		//@formatter:off
		resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameter(Parameters.class, "name1", new Identifier().setSystem("system1").setValue("value1"))
			.andParameter("name2", new Patient().setActive(true))
			.encodedXml()
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/$SOMEOPERATION", MY_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals("POST", MY_SERVLET.ourRequestMethod);
		assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"name1\"/><valueIdentifier><system value=\"system1\"/><value value=\"value1\"/></valueIdentifier></parameter><parameter><name value=\"name2\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><active value=\"true\"/></Patient></resource></parameter></Parameters>", (MY_SERVLET.ourRequestBodyString));


	}

	@Test
	public void testOperationWithInvalidParam() {
		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		// Who knows what the heck this is!
		IBase weirdBase = new IBase() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEmpty() {
				return false;
			}

			@Override
			public boolean hasFormatComment() {
				return false;
			}

			@Override
			public List<String> getFormatCommentsPre() {
				return null;
			}

			@Override
			public List<String> getFormatCommentsPost() {
				return null;
			}

			@Override
			public Object getUserData(String theName) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void setUserData(String theName, Object theValue) {
				throw new UnsupportedOperationException();
			}
		};

		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
			client
				.operation()
				.onServer()
				.named("$SOMEOPERATION")
				.withParameter(Parameters.class, "name1", weirdBase)
				.execute();
		});
	}

	@Test
	public void testOperationWithProfiledDatatypeParam() {
		IParser p = ourCtx.newXmlParser();

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringType("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringType("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		//@formatter:off
		client
			.operation()
			.onInstance(new IdType("http://foo/Patient/1"))
			.named("validate-code")
			.withParameter(Parameters.class, "code", new CodeType("8495-4"))
			.andParameter("system", new UriType("http://loinc.org"))
			.useHttpGet()
			.execute();
		//@formatter:off

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/1/$validate-code?code=8495-4&system=http%3A%2F%2Floinc.org", MY_SERVLET.ourRequestUri);

		//@formatter:off

		client
			.operation()
			.onInstance(new IdType("http://foo/Patient/1"))
			.named("validate-code")
			.withParameter(Parameters.class, "code", new CodeType("8495-4"))
			.andParameter("system", new UriType("http://loinc.org"))
			.encodedXml()
			.execute();
		//@formatter:off

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/1/$validate-code", MY_SERVLET.ourRequestUri);
		ourLog.info(MY_SERVLET.ourRequestBodyString);
		assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"code\"/><valueCode value=\"8495-4\"/></parameter><parameter><name value=\"system\"/><valueUri value=\"http://loinc.org\"/></parameter></Parameters>", MY_SERVLET.ourRequestBodyString);

	}

	@Test
	public void testOperationWithListOfParameterResponse() {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		inParams.addParameter().setValue(new StringType("STRINGVALIN1"));
		inParams.addParameter().setValue(new StringType("STRINGVALIN2"));
		String reqString = p.encodeResourceToString(inParams);

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringType("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringType("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		//@formatter:off
		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.encodedXml()
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/$SOMEOPERATION", MY_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals(MY_SERVLET.ourRequestBodyString, reqString);
		assertEquals("POST", MY_SERVLET.ourRequestMethod);


		//@formatter:off
		resp = client
			.operation()
			.onType(Patient.class)
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.encodedXml()
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$SOMEOPERATION", MY_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals(MY_SERVLET.ourRequestBodyString, reqString);
		assertEquals("POST", MY_SERVLET.ourRequestMethod);


		//@formatter:off
		resp = client
			.operation()
			.onInstance(new IdType("Patient", "123"))
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.encodedXml()
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$SOMEOPERATION", MY_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals(MY_SERVLET.ourRequestBodyString, reqString);
		assertEquals("POST", MY_SERVLET.ourRequestMethod);


		resp = client.operation().onInstance(new IdType("http://foo.com/bar/baz/Patient/123/_history/22")).named("$SOMEOPERATION").withParameters(inParams).execute();
		// @formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$SOMEOPERATION", MY_SERVLET.ourRequestUri);

	}

	@Test
	public void testOperationWithNoInParameters() {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		final String reqString = p.encodeResourceToString(inParams);

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringType("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringType("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		//@formatter:off
		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class).encodedXml().execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/$SOMEOPERATION", MY_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals(MY_SERVLET.ourRequestBodyString, reqString);
		assertEquals("POST", MY_SERVLET.ourRequestMethod);


		//@formatter:off
		resp = client
			.operation()
			.onType(Patient.class)
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class).encodedXml().execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$SOMEOPERATION", MY_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals(MY_SERVLET.ourRequestBodyString, reqString);
		assertEquals("POST", MY_SERVLET.ourRequestMethod);


		//@formatter:off
		resp = client
			.operation()
			.onInstance(new IdType("Patient", "123"))
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.encodedXml()
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$SOMEOPERATION", MY_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals(MY_SERVLET.ourRequestBodyString, reqString);
		assertEquals("POST", MY_SERVLET.ourRequestMethod);


		// @formatter:off
		resp = client
			.operation()
			.onInstance(new IdType("http://foo.com/bar/baz/Patient/123/_history/22"))
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.execute();
		// @formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$SOMEOPERATION", MY_SERVLET.ourRequestUri);

	}

	@Test
	public void testPageNext() {

		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = getPatientFeedWithOneResult();

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		org.hl7.fhir.dstu3.model.Bundle sourceBundle = new org.hl7.fhir.dstu3.model.Bundle();
		sourceBundle.getLinkOrCreate(IBaseBundle.LINK_PREV).setUrl(ourServer.getBaseUrl() + "/fhir/prev");
		sourceBundle.getLinkOrCreate(IBaseBundle.LINK_NEXT).setUrl(ourServer.getBaseUrl() + "/fhir/next");

		//@formatter:off
		org.hl7.fhir.dstu3.model.Bundle resp = client
			.loadPage()
			.next(sourceBundle)
			.execute();
		//@formatter:on

		assertThat(resp.getEntry()).hasSize(1);
		assertEquals(ourServer.getBaseUrl() + "/fhir/next", MY_SERVLET.ourRequestUri);


	}

	@Test
	public void testPageNextNoLink() {
		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		org.hl7.fhir.dstu3.model.Bundle sourceBundle = new org.hl7.fhir.dstu3.model.Bundle();
		try {
			client.loadPage().next(sourceBundle).execute();
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).contains("Can not perform paging operation because no link was found in Bundle with relation \"next\"");
		}
	}

	@Test
	public void testPagePrev() {


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = getPatientFeedWithOneResult();

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		org.hl7.fhir.dstu3.model.Bundle sourceBundle = new org.hl7.fhir.dstu3.model.Bundle();
		sourceBundle.getLinkOrCreate("previous").setUrl(ourServer.getBaseUrl() + "/fhir/prev");

		//@formatter:off
		org.hl7.fhir.dstu3.model.Bundle resp = client
			.loadPage()
			.previous(sourceBundle)
			.execute();
		//@formatter:on

		assertThat(resp.getEntry()).hasSize(1);
		assertEquals(ourServer.getBaseUrl() + "/fhir/prev", MY_SERVLET.ourRequestUri);


		/*
		 * Try with "prev" instead of "previous"
		 */

		sourceBundle = new org.hl7.fhir.dstu3.model.Bundle();
		sourceBundle.getLinkOrCreate("prev").setUrl(ourServer.getBaseUrl() + "/fhir/prev");

		//@formatter:off
		resp = client
			.loadPage()
			.previous(sourceBundle)
			.execute();
		//@formatter:on

		assertThat(resp.getEntry()).hasSize(1);
		assertEquals(ourServer.getBaseUrl() + "/fhir/prev", MY_SERVLET.ourRequestUri);


	}

	@Test
	public void testReadByUri() {

		Patient patient = new Patient();
		patient.addName().setFamily("FAM");
		final String respString = ourCtx.newXmlParser().encodeResourceToString(patient);

		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		Patient response;


		response = (Patient) client.read(new UriDt(ourServer.getBaseUrl() + "/fhir/Patient/123"));
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123", MY_SERVLET.ourRequestUri);
		assertEquals("FAM", response.getName().get(0).getFamily());
	}

	@Test
	public void testReadFluentByUri() {

		Patient patient = new Patient();
		patient.addName().setFamily("FAM");
		final String respString = ourCtx.newXmlParser().encodeResourceToString(patient);

		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		Patient response;

		response = client.read().resource(Patient.class).withUrl(new IdType(ourServer.getBaseUrl() + "/AAA/Patient/123")).execute();
		assertEquals(ourServer.getBaseUrl() + "/AAA/Patient/123", MY_SERVLET.ourRequestUri);
		assertEquals("FAM", response.getName().get(0).getFamily());
	}

	@Test
	public void testReadUpdatedHeaderDoesntOverwriteResourceValue() {

		//@formatter:off
		final String input = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" +
			"   <id value=\"e2ee823b-ee4d-472d-b79d-495c23f16b99\"/>\n" +
			"   <meta>\n" +
			"      <lastUpdated value=\"2015-06-22T15:48:57.554-04:00\"/>\n" +
			"   </meta>\n" +
			"   <type value=\"searchset\"/>\n" +
			"   <base value=\"http://localhost:58109/fhir/context\"/>\n" +
			"   <total value=\"0\"/>\n" +
			"   <link>\n" +
			"      <relation value=\"self\"/>\n" +
			"      <url value=\"http://localhost:58109/fhir/context/Patient?_pretty=true\"/>\n" +
			"   </link>\n" +
			"</Bundle>";
		//@formatter:on


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = input;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		org.hl7.fhir.dstu3.model.Bundle response;

		//@formatter:off
		response = client
			.search()
			.forResource(Patient.class)
			.returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
			.execute();
		//@formatter:on

		assertEquals("2015-06-22T15:48:57.554-04:00", response.getMeta().getLastUpdatedElement().getValueAsString());
	}

	@Test
	public void testReadWithElementsParam() {
		String msg = "{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}";


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		//@formatter:off
		IBaseResource response = client.read()
			.resource("Patient")
			.withId("123")
			.elementsSubset("name", "identifier")
			.execute();
		//@formatter:on

		assertThat(MY_SERVLET.ourRequestUri).isIn(ourServer.getBaseUrl() + "/fhir/Patient/123?_elements=name%2Cidentifier",
			ourServer.getBaseUrl() + "/fhir/Patient/123?_elements=identifier%2Cname");
		assertEquals(Patient.class, response.getClass());

	}

	@Test
	public void testReadWithSummaryInvalid() {
		String msg = "<>>>><<<<>";


		MY_SERVLET.ourResponseContentType = Constants.CT_HTML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		//@formatter:off
		try {
			client.read()
				.resource(Patient.class)
				.withId("123")
				.summaryMode(SummaryEnum.TEXT)
				.execute();
			fail();
		} catch (InvalidResponseException e) {
			assertThat(e.getMessage()).contains("Unable to Parse HTML");
		}
		//@formatter:on
	}

	@Test
	public void testReadWithSummaryParamHtml() {
		String msg = "<div>HELP IM A DIV</div>";


		MY_SERVLET.ourResponseContentType = Constants.CT_HTML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		//@formatter:off
		Patient response = client.read()
			.resource(Patient.class)
			.withId("123")
			.summaryMode(SummaryEnum.TEXT)
			.execute();
		//@formatter:on

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123?_summary=text", MY_SERVLET.ourRequestUri);
		assertEquals(Patient.class, response.getClass());
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">HELP IM A DIV</div>", response.getText().getDiv().getValueAsString());

	}

	@Test
	public void testSearchByString() {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		//@formatter:off
		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=james", MY_SERVLET.ourRequestUri);
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

	}

	@Test
	public void testSearchByUrl() {

		final String msg = getPatientFeedWithOneResult();


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		//@formatter:off
		org.hl7.fhir.dstu3.model.Bundle response = client.search()
			.byUrl(ourServer.getBaseUrl() + "/AAA?name=http://foo|bar")
			.encodedJson()
			.returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/AAA?name=http%3A//foo%7Cbar&_format=json", MY_SERVLET.ourRequestUri);
		assertNotNull(response);


		//@formatter:off
		response = client.search()
			.byUrl("Patient?name=http://foo|bar")
			.encodedJson()
			.returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=http%3A//foo%7Cbar&_format=json", MY_SERVLET.ourRequestUri);
		assertNotNull(response);


		//@formatter:off
		response = client.search()
			.byUrl("/Patient?name=http://foo|bar")
			.encodedJson()
			.returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=http%3A//foo%7Cbar&_format=json", MY_SERVLET.ourRequestUri);
		assertNotNull(response);


		//@formatter:off
		response = client.search()
			.byUrl("Patient")
			.returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient", MY_SERVLET.ourRequestUri);
		assertNotNull(response);


		//@formatter:off
		response = client.search()
			.byUrl("Patient?")
			.returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient", MY_SERVLET.ourRequestUri);
		assertNotNull(response);


		try {
			client.search().byUrl("foo/bar?test=1");
		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(1393) + "Search URL must be either a complete URL starting with http: or https:, or a relative FHIR URL in the form [ResourceType]?[Params]", e.getMessage());
		}
	}

	/**
	 * See #191
	 */
	@Test
	public void testSearchReturningDstu2Bundle() throws Exception {
		String msg = IOUtils.toString(GenericJaxRsClientDstu3Test.class.getResourceAsStream("/bundle_orion.xml"));


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		//@formatter:off
		org.hl7.fhir.dstu3.model.Bundle response = client.search()
			.forResource("Observation")
			.where(Patient.NAME.matches().value("FOO"))
			.returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
			.execute();
		//@formatter:on

		BundleLinkComponent link = response.getLink().get(0);
		assertEquals("just trying add link", link.getRelation());
		assertEquals("blarion", link.getUrl());

		BundleEntryComponent entry = response.getEntry().get(0);
		link = entry.getLink().get(0);
		assertEquals("orionhealth.edit", link.getRelation());
		assertEquals("Observation", link.getUrl());
	}

	@Test
	public void testSearchWithElementsParam() {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		//@formatter:off
		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.elementsSubset("name", "identifier")
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertThat(MY_SERVLET.ourRequestUri).isIn(ourServer.getBaseUrl() + "/fhir/Patient?name=james&_elements=name%2Cidentifier",
			ourServer.getBaseUrl() + "/fhir/Patient?name=james&_elements=identifier%2Cname");
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

	}

	@Test
	public void testSearchByPost() {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		//@formatter:off
		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.elementsSubset("name", "identifier")
			.usingStyle(SearchStyleEnum.POST)
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/_search?_elements=identifier%2Cname", MY_SERVLET.ourRequestUri);

		//		assertThat(MY_SERVLET.ourRequestUri,
		//				either(equalTo(ourServer.getBaseUrl() + "/fhir/Patient?name=james&_elements=name%2Cidentifier")).or(equalTo(ourServer.getBaseUrl() + "/fhir/Patient?name=james&_elements=identifier%2Cname")));

		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

		assertEquals("name=james", MY_SERVLET.ourRequestBodyString);

		assertEquals("application/x-www-form-urlencoded", MY_SERVLET.ourRequestContentType.replace(";char", "; char").toLowerCase());
		assertEquals(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_NON_LEGACY, MY_SERVLET.ourRequestFirstHeaders.get("Accept").getValue());
		assertThat(MY_SERVLET.ourRequestFirstHeaders.get("User-Agent").getValue()).isNotEmpty();
	}

	@Test
	public void testSearchByPostUseJson() {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		//@formatter:off
		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.elementsSubset("name", "identifier")
			.usingStyle(SearchStyleEnum.POST)
			.encodedJson()
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertThat(MY_SERVLET.ourRequestUri).contains(ourServer.getBaseUrl() + "/fhir/Patient/_search?");
		assertThat(MY_SERVLET.ourRequestUri).contains("_elements=identifier%2Cname");

		//		assertThat(MY_SERVLET.ourRequestUri,
		//				either(equalTo(ourServer.getBaseUrl() + "/fhir/Patient?name=james&_elements=name%2Cidentifier")).or(equalTo(ourServer.getBaseUrl() + "/fhir/Patient?name=james&_elements=identifier%2Cname")));

		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

		assertEquals("name=james", MY_SERVLET.ourRequestBodyString);

		assertEquals("application/x-www-form-urlencoded", MY_SERVLET.ourRequestContentType);
		assertEquals(Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY, MY_SERVLET.ourRequestFirstHeaders.get("Accept").getValue());
	}

	@Test
	public void testSearchWithLastUpdated() {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		//@formatter:off
		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.lastUpdated(new DateRangeParam("2011-01-01", "2012-01-01"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=james&_lastUpdated=ge2011-01-01&_lastUpdated=le2012-01-01", MY_SERVLET.ourRequestUri);
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

	}

	@Test
	public void testSearchWithProfileAndSecurity() {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		//@formatter:off
		Bundle response = client.search()
			.forResource("Patient")
			.withProfile("http://foo1")
			.withProfile("http://foo2")
			.withSecurity("system1", "code1")
			.withSecurity("system2", "code2")
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?_security=system1%7Ccode1&_security=system2%7Ccode2&_profile=http%3A%2F%2Ffoo1&_profile=http%3A%2F%2Ffoo2", MY_SERVLET.ourRequestUri);
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithReverseInclude() {

		String msg = getPatientFeedWithOneResult();


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		//@formatter:off
		Bundle response = client.search()
			.forResource(Patient.class)
			.encodedJson()
			.revInclude(new Include("Provenance:target"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?_revinclude=Provenance%3Atarget&_format=json", MY_SERVLET.ourRequestUri);

	}

	@Test
	public void testSearchWithSummaryParam() {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		//@formatter:off
		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.summaryMode(SummaryEnum.FALSE)
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=james&_summary=false", MY_SERVLET.ourRequestUri);
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

	}

	@Test
	public void testTransactionWithListOfResources() {

		org.hl7.fhir.dstu3.model.Bundle resp = new org.hl7.fhir.dstu3.model.Bundle();
		resp.addEntry().getResponse().setLocation("Patient/1/_history/1");
		resp.addEntry().getResponse().setLocation("Patient/2/_history/2");
		String respString = ourCtx.newJsonParser().encodeResourceToString(resp);


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		List<IBaseResource> input = new ArrayList<IBaseResource>();

		Patient p1 = new Patient(); // No ID
		p1.addName().setFamily("PATIENT1");
		input.add(p1);

		Patient p2 = new Patient(); // Yes ID
		p2.addName().setFamily("PATIENT2");
		p2.setId("http://example.com/Patient/2");
		input.add(p2);

		//@formatter:off
		List<IBaseResource> response = client.transaction()
			.withResources(input)
			.encodedJson()
			.execute();
		//@formatter:on

		assertEquals(ourServer.getBaseUrl() + "/fhir", MY_SERVLET.ourRequestUri);
		assertThat(response).hasSize(2);

		String requestString = MY_SERVLET.ourRequestBodyString;
		org.hl7.fhir.dstu3.model.Bundle requestBundle = ourCtx.newJsonParser().parseResource(org.hl7.fhir.dstu3.model.Bundle.class, requestString);
		assertThat(requestBundle.getEntry()).hasSize(2);
		assertEquals(HTTPVerb.POST, requestBundle.getEntry().get(0).getRequest().getMethod());
		assertEquals(HTTPVerb.PUT, requestBundle.getEntry().get(1).getRequest().getMethod());
		assertEquals("http://example.com/Patient/2", requestBundle.getEntry().get(1).getFullUrl());

		p1 = (Patient) response.get(0);
		assertEquals(new IdType("Patient/1/_history/1"), p1.getIdElement());
		// assertEquals("PATIENT1", p1.getName().get(0).getFamily().get(0).getValue());

		p2 = (Patient) response.get(1);
		assertEquals(new IdType("Patient/2/_history/2"), p2.getIdElement());
		// assertEquals("PATIENT2", p2.getName().get(0).getFamily().get(0).getValue());
	}

	@Test
	public void testTransactionWithString() {

		Bundle req = new Bundle();
		req.setType(BundleType.TRANSACTION);

		Patient patient = new Patient();
		patient.setId("C01");
		patient.addName().setFamily("Smith").addGiven("John");
		req.addEntry()
			.setFullUrl("Patient/C01")
			.setResource(patient).getRequest().setMethod(HTTPVerb.PUT).setUrl("Patient/C01");

		Observation observation = new Observation();
		observation.setId("C02");
		observation.setStatus(Observation.ObservationStatus.FINAL);
		observation.setEffective(new DateTimeType("2019-02-21T13:35:00-05:00"));
		observation.getSubject().setReference("Patient/C01");
		observation.getCode().addCoding().setSystem("http://loinc.org").setCode("3141-9").setDisplay("Body Weight Measured");
		observation.setValue(new Quantity(null, 190, "http://unitsofmeaure.org", "{lb_av}", "{lb_av}"));
		req.addEntry()
			.setFullUrl("Observation/C02")
			.setResource(observation).getRequest().setMethod(HTTPVerb.PUT).setUrl("Observation/C02");
		String reqString = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(req);
		ourLog.info(reqString);
		reqString = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(req);
		ourLog.info(reqString);

		org.hl7.fhir.dstu3.model.Bundle resp = new org.hl7.fhir.dstu3.model.Bundle();
		resp.addEntry().getResponse().setLocation("Patient/1/_history/1");
		resp.addEntry().getResponse().setLocation("Patient/2/_history/2");


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = reqString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		//@formatter:off
		String response = client.transaction()
			.withBundle(reqString)
			.execute();
		//@formatter:on

		assertEquals(ourServer.getBaseUrl() + "/fhir/", MY_SERVLET.ourRequestUri);
		assertThat(response).contains("\"Bundle\"");
		assertEquals("application/fhir+json;charset=UTF-8", MY_SERVLET.ourRequestFirstHeaders.get("Content-Type").getValue());

		//@formatter:off
		response = client.transaction()
			.withBundle(reqString)
			.encodedXml()
			.execute();
		//@formatter:on

		assertEquals(ourServer.getBaseUrl() + "/fhir/", MY_SERVLET.ourRequestUri);
		assertEquals("application/fhir+xml;charset=UTF-8", MY_SERVLET.ourRequestFirstHeaders.get("Content-Type").getValue());

	}

	@Test
	public void testTransactionWithTransactionResource() {

		org.hl7.fhir.dstu3.model.Bundle resp = new org.hl7.fhir.dstu3.model.Bundle();
		resp.addEntry().getResponse().setLocation("Patient/1/_history/1");
		resp.addEntry().getResponse().setLocation("Patient/2/_history/2");
		String respString = ourCtx.newJsonParser().encodeResourceToString(resp);


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		org.hl7.fhir.dstu3.model.Bundle input = new org.hl7.fhir.dstu3.model.Bundle();

		Patient p1 = new Patient(); // No ID
		p1.addName().setFamily("PATIENT1");
		input.addEntry().setResource(p1);

		Patient p2 = new Patient(); // Yes ID
		p2.addName().setFamily("PATIENT2");
		p2.setId("Patient/2");
		input.addEntry().setResource(p2);

		//@formatter:off
		org.hl7.fhir.dstu3.model.Bundle response = client.transaction()
			.withBundle(input)
			.encodedJson()
			.execute();
		//@formatter:on

		assertEquals(ourServer.getBaseUrl() + "/fhir", MY_SERVLET.ourRequestUri);
		assertThat(response.getEntry()).hasSize(2);

		assertEquals("Patient/1/_history/1", response.getEntry().get(0).getResponse().getLocation());
		assertEquals("Patient/2/_history/2", response.getEntry().get(1).getResponse().getLocation());
	}

	@Test
	public void testUpdateConditional() {


		MY_SERVLET.ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Patient p = new Patient();
		p.addName().setFamily("FOOFAMILY");

		client.update().resource(p).conditionalByUrl("Patient?name=foo").encodedXml().execute();
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(MY_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals("PUT", MY_SERVLET.ourRequestMethod);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=foo", MY_SERVLET.ourRequestUri);


		client.update().resource(p).conditionalByUrl("Patient?name=http://foo|bar").encodedXml().execute();
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(MY_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals("PUT", MY_SERVLET.ourRequestMethod);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=http%3A//foo%7Cbar", MY_SERVLET.ourRequestUri);


		client.update().resource(ourCtx.newXmlParser().encodeResourceToString(p)).conditionalByUrl("Patient?name=foo").execute();
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(MY_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals("PUT", MY_SERVLET.ourRequestMethod);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=foo", MY_SERVLET.ourRequestUri);


		client.update().resource(p).conditional().where(Patient.NAME.matches().value("foo")).and(Patient.ADDRESS.matches().value("AAA|BBB")).encodedXml().execute();
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(MY_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals("PUT", MY_SERVLET.ourRequestMethod);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=foo&address=AAA%5C%7CBBB", MY_SERVLET.ourRequestUri);


		client.update().resource(ourCtx.newXmlParser().encodeResourceToString(p)).conditional().where(Patient.NAME.matches().value("foo")).and(Patient.ADDRESS.matches().value("AAA|BBB")).encodedXml().execute();
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(MY_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals("PUT", MY_SERVLET.ourRequestMethod);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=foo&address=AAA%5C%7CBBB", MY_SERVLET.ourRequestUri);


	}

	@Test
	public void testUpdateNonFluent() {


		MY_SERVLET.ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");
		client.setEncoding(EncodingEnum.XML);


		Patient p = new Patient();
		p.addName().setFamily("FOOFAMILY");

		client.update(new IdType("Patient/123").getValue(), p);
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(MY_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123?_format=xml", MY_SERVLET.ourRequestUri);
		assertEquals("PUT", MY_SERVLET.ourRequestMethod);


		client.update("123", p);
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, MY_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(MY_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123?_format=xml", MY_SERVLET.ourRequestUri);
		assertEquals("PUT", MY_SERVLET.ourRequestMethod);

	}

	@Test
	public void testUpdatePrefer() {
		MY_SERVLET.ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Patient p = new Patient();
		p.setId(new IdType("1"));
		p.addName().setFamily("FOOFAMILY");

		client.update().resource(p).prefer(PreferReturnEnum.MINIMAL).execute();
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_PREFER)).hasSize(1);
		assertEquals(Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_MINIMAL, MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_PREFER).get(0).getValue());


		client.update().resource(p).prefer(PreferReturnEnum.REPRESENTATION).execute();
		assertThat(MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_PREFER)).hasSize(1);
		assertEquals(Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_REPRESENTATION, MY_SERVLET.ourRequestHeaders.get(Constants.HEADER_PREFER).get(0).getValue());


	}

	@Test
	public void testUpdateReturningResourceBody() {
		Patient p = new Patient();
		p.setId("123");
		final String formatted = ourCtx.newXmlParser().encodeResourceToString(p);


		MY_SERVLET.ourResponseStatus = Constants.STATUS_HTTP_200_OK;
		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = formatted;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		p = new Patient();
		p.setId(new IdType("1"));
		p.addName().setFamily("FOOFAMILY");

		MethodOutcome output = client.update().resource(p).execute();
		assertNotNull(output.getResource());
		assertEquals("Patient/123", output.getResource().getIdElement().toUnqualifiedVersionless().getValue());
	}

	@Test
	public void testValidateFluent() {

		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDiagnostics("FOOBAR");
		final String msg = ourCtx.newXmlParser().encodeResourceToString(oo);


		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		Patient p = new Patient();
		p.addName().addGiven("GIVEN");


		MethodOutcome response;

		response = client.validate().resource(p).encodedXml().execute();
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$validate", MY_SERVLET.ourRequestUri);
		assertEquals("POST", MY_SERVLET.ourRequestMethod);
		assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"resource\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><name><given value=\"GIVEN\"/></name></Patient></resource></parameter></Parameters>", MY_SERVLET.ourRequestBodyString);
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", toOo(response.getOperationOutcome()).getIssue().get(0).getDiagnosticsElement().getValue());


		response = client.validate().resource(ourCtx.newXmlParser().encodeResourceToString(p)).execute();
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$validate", MY_SERVLET.ourRequestUri);
		assertEquals("POST", MY_SERVLET.ourRequestMethod);
		assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"resource\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><name><given value=\"GIVEN\"/></name></Patient></resource></parameter></Parameters>", MY_SERVLET.ourRequestBodyString);
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", toOo(response.getOperationOutcome()).getIssue().get(0).getDiagnosticsElement().getValue());


		response = client.validate().resource(ourCtx.newJsonParser().encodeResourceToString(p)).execute();
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$validate", MY_SERVLET.ourRequestUri);
		assertEquals("POST", MY_SERVLET.ourRequestMethod);
		assertEquals("{\"resourceType\":\"Parameters\",\"parameter\":[{\"name\":\"resource\",\"resource\":{\"resourceType\":\"Patient\",\"name\":[{\"given\":[\"GIVEN\"]}]}}]}", MY_SERVLET.ourRequestBodyString);
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", toOo(response.getOperationOutcome()).getIssue().get(0).getDiagnosticsElement().getValue());


		response = client.validate().resource(ourCtx.newJsonParser().encodeResourceToString(p)).prettyPrint().execute();
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$validate?_pretty=true", MY_SERVLET.ourRequestUri);
		assertEquals("POST", MY_SERVLET.ourRequestMethod);
		assertThat(MY_SERVLET.ourRequestBodyString).contains("\"resourceType\": \"Parameters\",\n");
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", toOo(response.getOperationOutcome()).getIssue().get(0).getDiagnosticsElement().getValue());

	}

	@Test
	public void testValidateNonFluent() {
		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDiagnostics("FOOBAR");
		final String msg = ourCtx.newXmlParser().encodeResourceToString(oo);

		MY_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		MY_SERVLET.ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");
		client.setEncoding(EncodingEnum.XML);

		Patient p = new Patient();
		p.addName().addGiven("GIVEN");


		MethodOutcome response;

		//@formatter:off
		response = client.validate(p);
		//@formatter:on

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$validate?_format=xml", MY_SERVLET.ourRequestUri);
		assertEquals("POST", MY_SERVLET.ourRequestMethod);
		assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"resource\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><name><given value=\"GIVEN\"/></name></Patient></resource></parameter></Parameters>", MY_SERVLET.ourRequestBodyString);
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", ((OperationOutcome) response.getOperationOutcome()).getIssue().get(0).getDiagnosticsElement().getValue());

	}

	private OperationOutcome toOo(IBaseResource theOperationOutcome) {
		return (OperationOutcome) theOperationOutcome;
	}

}
