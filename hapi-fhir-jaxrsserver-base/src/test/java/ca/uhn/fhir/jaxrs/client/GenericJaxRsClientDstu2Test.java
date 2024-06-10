package ca.uhn.fhir.jaxrs.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Link;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
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
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import ca.uhn.fhir.test.utilities.server.RequestCaptureServlet;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
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

public class GenericJaxRsClientDstu2Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GenericJaxRsClientDstu2Test.class);

	private static final FhirContext ourCtx = FhirContext.forDstu2();

	private static final RequestCaptureServlet CAPTURE_SERVLET = new RequestCaptureServlet();

	@RegisterExtension
	public static final HttpServletExtension ourServer = new HttpServletExtension()
		.withServlet(CAPTURE_SERVLET)
		.keepAliveBetweenTests();

	@BeforeEach
	public void before() {
		JaxRsRestfulClientFactory clientFactory = new JaxRsRestfulClientFactory(ourCtx);
		clientFactory.setServerValidationMode(ServerValidationModeEnum.NEVER);

		ourCtx.setRestfulClientFactory(clientFactory);
		CAPTURE_SERVLET.reset();
	}

	private String getPatientFeedWithOneResult() {

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

		return msg;
	}

	@Test
	public void testAcceptHeaderFetchConformance() {
		IParser p = ourCtx.newXmlParser();

		Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final String respString = p.encodeResourceToString(conf);
		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		client.fetchConformance().ofType(Conformance.class).execute();
		assertEquals(ourServer.getBaseUrl() + "/fhir/metadata", CAPTURE_SERVLET.ourRequestUri);
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get("Accept")).hasSize(1);
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get("Accept").get(0).getValue()).contains(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_LEGACY);


		client.fetchConformance().ofType(Conformance.class).encodedJson().execute();
		assertEquals(ourServer.getBaseUrl() + "/fhir/metadata?_format=json", CAPTURE_SERVLET.ourRequestUri);
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get("Accept")).hasSize(1);
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get("Accept").get(0).getValue()).contains(Constants.CT_FHIR_JSON);


		client.fetchConformance().ofType(Conformance.class).encodedXml().execute();
		assertEquals(ourServer.getBaseUrl() + "/fhir/metadata?_format=xml", CAPTURE_SERVLET.ourRequestUri);
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get("Accept")).hasSize(1);
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get("Accept").get(0).getValue()).contains(Constants.CT_FHIR_XML);

	}

	@Test
	public void testAcceptHeaderPreflightConformance() {
		final IParser p = ourCtx.newXmlParser();

		final Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final Patient patient = new Patient();
		patient.addName().addFamily("FAMILY");

		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBodies = new String[]{p.encodeResourceToString(conf), p.encodeResourceToString(patient)};

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		Patient resp = client.read(Patient.class, new IdDt("123"));
		assertEquals("FAMILY", resp.getName().get(0).getFamily().get(0).getValue());
		assertEquals(ourServer.getBaseUrl() + "/fhir/metadata", CAPTURE_SERVLET.ourRequestUriAll.get(0));
		assertThat(CAPTURE_SERVLET.ourRequestHeadersAll.get(0).get("Accept")).hasSize(1);
		assertThat(CAPTURE_SERVLET.ourRequestHeadersAll.get(0).get("Accept").get(0).getValue()).contains(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_LEGACY);
		assertThat(CAPTURE_SERVLET.ourRequestHeadersAll.get(0).get("Accept").get(0).getValue()).contains(Constants.CT_FHIR_XML);
		assertThat(CAPTURE_SERVLET.ourRequestHeadersAll.get(0).get("Accept").get(0).getValue()).contains(Constants.CT_FHIR_JSON);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123", CAPTURE_SERVLET.ourRequestUriAll.get(1));
		assertThat(CAPTURE_SERVLET.ourRequestHeadersAll.get(1).get("Accept")).hasSize(1);
		assertThat(CAPTURE_SERVLET.ourRequestHeadersAll.get(1).get("Accept").get(0).getValue()).contains(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_LEGACY);
		assertThat(CAPTURE_SERVLET.ourRequestHeadersAll.get(1).get("Accept").get(0).getValue()).contains(Constants.CT_FHIR_XML);
		assertThat(CAPTURE_SERVLET.ourRequestHeadersAll.get(1).get("Accept").get(0).getValue()).contains(Constants.CT_FHIR_JSON);
	}

	@Test
	public void testAcceptHeaderPreflightConformancePreferJson() {
		final IParser p = ourCtx.newXmlParser();

		final Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final Patient patient = new Patient();
		patient.addName().addFamily("FAMILY");

		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBodies = new String[]{p.encodeResourceToString(conf), p.encodeResourceToString(patient)};

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");
		client.setEncoding(EncodingEnum.JSON);

		Patient resp = client.read(Patient.class, new IdDt("123"));
		assertEquals("FAMILY", resp.getName().get(0).getFamily().get(0).getValue());
		assertEquals(ourServer.getBaseUrl() + "/fhir/metadata?_format=json", CAPTURE_SERVLET.ourRequestUriAll.get(0));
		assertThat(CAPTURE_SERVLET.ourRequestHeadersAll.get(0).get("Accept")).hasSize(1);
		assertThat(CAPTURE_SERVLET.ourRequestHeadersAll.get(0).get("Accept").get(0).getValue()).contains(Constants.CT_FHIR_JSON);
		assertThat(CAPTURE_SERVLET.ourRequestHeadersAll.get(0).get("Accept").get(0).getValue()).doesNotContain(Constants.CT_FHIR_XML);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123?_format=json", CAPTURE_SERVLET.ourRequestUriAll.get(1));
		assertThat(CAPTURE_SERVLET.ourRequestHeadersAll.get(1).get("Accept")).hasSize(1);
		assertThat(CAPTURE_SERVLET.ourRequestHeadersAll.get(1).get("Accept").get(0).getValue()).contains(Constants.CT_FHIR_JSON);
		assertThat(CAPTURE_SERVLET.ourRequestHeadersAll.get(1).get("Accept").get(0).getValue()).doesNotContain(Constants.CT_FHIR_XML);
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testConformance() {
		IParser p = ourCtx.newXmlParser();

		Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final String respString = p.encodeResourceToString(conf);

		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		Conformance resp = client.capabilities().ofType(Conformance.class).execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/metadata", CAPTURE_SERVLET.ourRequestUri);
		assertEquals("COPY", resp.getCopyright());
		assertEquals("GET", CAPTURE_SERVLET.ourRequestMethod);


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
		p.addName().addFamily("FOOFAMILY");

		client.create().resource(p).encodedXml().execute();

		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(CAPTURE_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient", CAPTURE_SERVLET.ourRequestUri);
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);


		p.setId("123");

		client.create().resource(p).encodedXml().execute();
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		String body = CAPTURE_SERVLET.ourRequestBodyString;
		assertThat(body).contains("<family value=\"FOOFAMILY\"/>");
		assertThat(body).doesNotContain("123");
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient", CAPTURE_SERVLET.ourRequestUri);
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);


	}

	@Test
	public void testCreateConditional() {


		CAPTURE_SERVLET.ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Patient p = new Patient();
		p.addName().addFamily("FOOFAMILY");

		client.create().resource(p).conditionalByUrl("Patient?name=foo").encodedXml().execute();
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(CAPTURE_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=foo", CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_IF_NONE_EXIST).getValue());
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);


		client.create().resource(p).conditionalByUrl("Patient?name=http://foo|bar").encodedXml().execute();
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(CAPTURE_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=http%3A//foo%7Cbar", CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_IF_NONE_EXIST).getValue());
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);


		client.create().resource(p).conditional().where(Patient.NAME.matches().value("foo")).encodedXml().execute();
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(CAPTURE_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=foo", CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_IF_NONE_EXIST).getValue());
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);


	}

	@Test
	public void testCreatePrefer() {


		CAPTURE_SERVLET.ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Patient p = new Patient();
		p.addName().addFamily("FOOFAMILY");

		client.create().resource(p).prefer(PreferReturnEnum.MINIMAL).execute();
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_PREFER)).hasSize(1);
		assertEquals(Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_MINIMAL, CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_PREFER).get(0).getValue());


		client.create().resource(p).prefer(PreferReturnEnum.REPRESENTATION).execute();
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_PREFER)).hasSize(1);
		assertEquals(Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_REPRESENTATION, CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_PREFER).get(0).getValue());


	}

	@Test
	public void testCreateReturningResourceBody() {
		Patient p = new Patient();
		p.setId("123");
		final String formatted = ourCtx.newXmlParser().encodeResourceToString(p);


		CAPTURE_SERVLET.ourResponseStatus = Constants.STATUS_HTTP_200_OK;
		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = formatted;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		p = new Patient();
		p.setId(new IdDt("1"));
		p.addName().addFamily("FOOFAMILY");

		MethodOutcome output = client.create().resource(p).execute();
		assertNotNull(output.getResource());
		assertEquals("Patient/123", output.getResource().getIdElement().toUnqualifiedVersionless().getValue());
	}

	@Test
	public void testDeleteConditional() {
		CAPTURE_SERVLET.ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		client.delete().resourceById(new IdDt("Patient/123")).execute();
		assertEquals("DELETE", CAPTURE_SERVLET.ourRequestMethod);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123", CAPTURE_SERVLET.ourRequestUri);


		client.delete().resourceConditionalByUrl("Patient?name=foo").execute();
		assertEquals("DELETE", CAPTURE_SERVLET.ourRequestMethod);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=foo", CAPTURE_SERVLET.ourRequestUri);


		client.delete().resourceConditionalByType("Patient").where(Patient.NAME.matches().value("foo")).execute();
		assertEquals("DELETE", CAPTURE_SERVLET.ourRequestMethod);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=foo", CAPTURE_SERVLET.ourRequestUri);


	}

	@Test
	public void testDelete() {
		CAPTURE_SERVLET.ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		client.delete().resourceById(new IdDt("Patient/123")).execute();
		assertEquals("DELETE", CAPTURE_SERVLET.ourRequestMethod);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123", CAPTURE_SERVLET.ourRequestUri);

	}

	@Test
	public void testHistory() {

		final String msg = getPatientFeedWithOneResult();

		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		ca.uhn.fhir.model.dstu2.resource.Bundle response;


		response = client
			.history()
			.onServer()
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/_history", CAPTURE_SERVLET.ourRequestUri);
		assertThat(response.getEntry()).hasSize(1);


		response = client
			.history()
			.onServer()
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.since((Date) null)
			.count(null)
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/_history", CAPTURE_SERVLET.ourRequestUri);
		assertThat(response.getEntry()).hasSize(1);


		response = client
			.history()
			.onServer()
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.since(new InstantDt())
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/_history", CAPTURE_SERVLET.ourRequestUri);
		assertThat(response.getEntry()).hasSize(1);


		response = client
			.history()
			.onType(Patient.class)
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/_history", CAPTURE_SERVLET.ourRequestUri);
		assertThat(response.getEntry()).hasSize(1);


		response = client
			.history()
			.onInstance(new IdDt("Patient", "123"))
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/_history", CAPTURE_SERVLET.ourRequestUri);
		assertThat(response.getEntry()).hasSize(1);


		response = client
			.history()
			.onInstance(new IdDt("Patient", "123"))
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.count(123)
			.since(new InstantDt("2001-01-02T11:22:33Z"))
			.execute();

		assertThat(CAPTURE_SERVLET.ourRequestUri).isIn(
			ourServer.getBaseUrl() + "/fhir/Patient/123/_history?_since=2001-01-02T11:22:33Z&_count=123",
			ourServer.getBaseUrl() + "/fhir/Patient/123/_history?_count=123&_since=2001-01-02T11:22:33Z");
		assertThat(response.getEntry()).hasSize(1);


		response = client
			.history()
			.onInstance(new IdDt("Patient", "123"))
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.since(new InstantDt("2001-01-02T11:22:33Z").getValue())
			.execute();

		assertThat(CAPTURE_SERVLET.ourRequestUri).contains("_since=2001-01");
		assertThat(response.getEntry()).hasSize(1);

	}

	@Test
	public void testMetaAdd() {
		IParser p = ourCtx.newXmlParser();

		MetaDt inMeta = new MetaDt().addProfile("urn:profile:in");

		Parameters outParams = new Parameters();
		outParams.addParameter().setName("meta").setValue(new MetaDt().addProfile("urn:profile:out"));
		final String respString = p.encodeResourceToString(outParams);

		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		MetaDt resp = client
			.meta()
			.add()
			.onResource(new IdDt("Patient/123"))
			.meta(inMeta)
			.encodedXml()
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$meta-add", CAPTURE_SERVLET.ourRequestUri);
		assertEquals("urn:profile:out", resp.getProfile().get(0).getValue());
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);
		assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"meta\"/><valueMeta><profile value=\"urn:profile:in\"/></valueMeta></parameter></Parameters>", CAPTURE_SERVLET.ourRequestBodyString);


	}

	@Test
	public void testMetaGet() {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("meta").setValue(new MetaDt().addProfile("urn:profile:in"));

		Parameters outParams = new Parameters();
		outParams.addParameter().setName("meta").setValue(new MetaDt().addProfile("urn:profile:out"));
		final String respString = p.encodeResourceToString(outParams);

		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		MetaDt resp = client
			.meta()
			.get(MetaDt.class)
			.fromServer()
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/$meta", CAPTURE_SERVLET.ourRequestUri);
		assertEquals("urn:profile:out", resp.getProfile().get(0).getValue());
		assertEquals("GET", CAPTURE_SERVLET.ourRequestMethod);


		resp = client
			.meta()
			.get(MetaDt.class)
			.fromType("Patient")
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$meta", CAPTURE_SERVLET.ourRequestUri);
		assertEquals("urn:profile:out", resp.getProfile().get(0).getValue());
		assertEquals("GET", CAPTURE_SERVLET.ourRequestMethod);


		resp = client
			.meta()
			.get(MetaDt.class)
			.fromResource(new IdDt("Patient/123"))
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$meta", CAPTURE_SERVLET.ourRequestUri);
		assertEquals("urn:profile:out", resp.getProfile().get(0).getValue());
		assertEquals("GET", CAPTURE_SERVLET.ourRequestMethod);


	}

	@Test
	public void testOperationAsGetWithInParameters() {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("param1").setValue(new StringDt("STRINGVALIN1"));
		inParams.addParameter().setName("param1").setValue(new StringDt("STRINGVALIN1b"));
		inParams.addParameter().setName("param2").setValue(new StringDt("STRINGVALIN2"));

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.useHttpGet()
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", CAPTURE_SERVLET.ourRequestMethod);


		resp = client
			.operation()
			.onType(Patient.class)
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.useHttpGet()
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", CAPTURE_SERVLET.ourRequestMethod);


		resp = client
			.operation()
			.onInstance(new IdDt("Patient", "123"))
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.useHttpGet()
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", CAPTURE_SERVLET.ourRequestMethod);


		// @formatter:off
		resp = client
			.operation()
			.onInstance(new IdDt("http://foo.com/bar/baz/Patient/123/_history/22"))
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.useHttpGet()
			.execute();
		// @formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2", CAPTURE_SERVLET.ourRequestUri);

	}

	@Test
	public void testOperationAsGetWithNoInParameters() {
		IParser p = ourCtx.newXmlParser();

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);

		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.useHttpGet()
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/$SOMEOPERATION", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", CAPTURE_SERVLET.ourRequestMethod);


		resp = client
			.operation()
			.onType(Patient.class)
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.useHttpGet()
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$SOMEOPERATION", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", CAPTURE_SERVLET.ourRequestMethod);


		resp = client
			.operation()
			.onInstance(new IdDt("Patient", "123"))
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.useHttpGet()
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$SOMEOPERATION", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", CAPTURE_SERVLET.ourRequestMethod);


		// @formatter:off
		resp = client
			.operation()
			.onInstance(new IdDt("http://foo.com/bar/baz/Patient/123/_history/22"))
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.useHttpGet()
			.execute();
		// @formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$SOMEOPERATION", CAPTURE_SERVLET.ourRequestUri);

	}

	@Test
	public void testOperationWithBundleResponseJson() {

		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON;
		final String respString = "{\n" + "    \"resourceType\":\"Bundle\",\n" + "    \"id\":\"8cef5f2a-0ba9-43a5-be26-c8dde9ff0e19\",\n" + "    \"base\":\"" + ourServer.getBaseUrl() + "/fhir\"\n" + "}";
		CAPTURE_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		client.registerInterceptor(new LoggingInterceptor(true));

		// Create the input parameters to pass to the server
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("start").setValue(new DateDt("2001-01-01"));
		inParams.addParameter().setName("end").setValue(new DateDt("2015-03-01"));

		// Invoke $everything on "Patient/1"
		Parameters outParams = client.operation().onInstance(new IdDt("Patient", "18066")).named("$everything").withParameters(inParams).execute();

		/*
		 * Note that the $everything operation returns a Bundle instead of a Parameters resource. The client operation
		 * methods return a Parameters instance however, so HAPI creates a Parameters object
		 * with a single parameter containing the value.
		 */
		ca.uhn.fhir.model.dstu2.resource.Bundle responseBundle = (ca.uhn.fhir.model.dstu2.resource.Bundle) outParams.getParameter().get(0).getResource();

		// Print the response bundle
		assertEquals("8cef5f2a-0ba9-43a5-be26-c8dde9ff0e19", responseBundle.getId().getIdPart());
	}

	@Test
	public void testOperationWithBundleResponseXml() {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		inParams.addParameter().setValue(new StringDt("STRINGVALIN1"));
		inParams.addParameter().setValue(new StringDt("STRINGVALIN2"));
		String reqString = p.encodeResourceToString(inParams);

		ca.uhn.fhir.model.dstu2.resource.Bundle outParams = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		outParams.setTotal(123);
		final String respString = p.encodeResourceToString(outParams);

		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameters(inParams).encodedXml().execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/$SOMEOPERATION", CAPTURE_SERVLET.ourRequestUri);
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals(CAPTURE_SERVLET.ourRequestBodyString, reqString);
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);
		assertThat(resp.getParameter()).hasSize(1);
		assertEquals(ca.uhn.fhir.model.dstu2.resource.Bundle.class, resp.getParameter().get(0).getResource().getClass());

	}

	@Test
	public void testOperationWithInlineParams() {
		IParser p = ourCtx.newXmlParser();

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);

		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameter(Parameters.class, "name1", new StringDt("value1"))
			.andParameter("name2", new StringDt("value1"))
			.encodedXml()
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/$SOMEOPERATION", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);
		assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"name1\"/><valueString value=\"value1\"/></parameter><parameter><name value=\"name2\"/><valueString value=\"value1\"/></parameter></Parameters>", (CAPTURE_SERVLET.ourRequestBodyString));


		/*
		 * Composite type
		 */


		resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameter(Parameters.class, "name1", new IdentifierDt("system1", "value1"))
			.andParameter("name2", new StringDt("value1"))
			.encodedXml()
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/$SOMEOPERATION", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);
		assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"name1\"/><valueIdentifier><system value=\"system1\"/><value value=\"value1\"/></valueIdentifier></parameter><parameter><name value=\"name2\"/><valueString value=\"value1\"/></parameter></Parameters>", (CAPTURE_SERVLET.ourRequestBodyString));


		/*
		 * Resource
		 */


		resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameter(Parameters.class, "name1", new IdentifierDt("system1", "value1"))
			.andParameter("name2", new Patient().setActive(true))
			.encodedXml()
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/$SOMEOPERATION", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);
		assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"name1\"/><valueIdentifier><system value=\"system1\"/><value value=\"value1\"/></valueIdentifier></parameter><parameter><name value=\"name2\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><active value=\"true\"/></Patient></resource></parameter></Parameters>", (CAPTURE_SERVLET.ourRequestBodyString));


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
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		client
			.operation()
			.onInstance(new IdDt("http://foo/Patient/1"))
			.named("validate-code")
			.withParameter(Parameters.class, "code", new CodeDt("8495-4"))
			.andParameter("system", new UriDt("http://loinc.org"))
			.useHttpGet()
			.execute();


		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/1/$validate-code?code=8495-4&system=http%3A%2F%2Floinc.org", CAPTURE_SERVLET.ourRequestUri);


		client
			.operation()
			.onInstance(new IdDt("http://foo/Patient/1"))
			.named("validate-code")
			.withParameter(Parameters.class, "code", new CodeDt("8495-4"))
			.andParameter("system", new UriDt("http://loinc.org"))
			.encodedXml()
			.encodedXml()
			.execute();


		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/1/$validate-code", CAPTURE_SERVLET.ourRequestUri);
		ourLog.info(CAPTURE_SERVLET.ourRequestBodyString);
		assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"code\"/><valueCode value=\"8495-4\"/></parameter><parameter><name value=\"system\"/><valueUri value=\"http://loinc.org\"/></parameter></Parameters>", CAPTURE_SERVLET.ourRequestBodyString);

	}

	@Test
	public void testOperationWithListOfParameterResponse() {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		inParams.addParameter().setValue(new StringDt("STRINGVALIN1"));
		inParams.addParameter().setValue(new StringDt("STRINGVALIN2"));
		String reqString = p.encodeResourceToString(inParams);

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameters(inParams).encodedXml().execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/$SOMEOPERATION", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals(CAPTURE_SERVLET.ourRequestBodyString, reqString);
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);


		resp = client
			.operation()
			.onType(Patient.class)
			.named("$SOMEOPERATION")
			.withParameters(inParams).encodedXml().execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$SOMEOPERATION", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals(CAPTURE_SERVLET.ourRequestBodyString, reqString);
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);


		resp = client
			.operation()
			.onInstance(new IdDt("Patient", "123"))
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.encodedXml()
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$SOMEOPERATION", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals(CAPTURE_SERVLET.ourRequestBodyString, reqString);
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);


		resp = client.operation().onInstance(new IdDt("http://foo.com/bar/baz/Patient/123/_history/22")).named("$SOMEOPERATION").withParameters(inParams).execute();
		// @formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$SOMEOPERATION", CAPTURE_SERVLET.ourRequestUri);

	}

	@Test
	public void testOperationWithNoInParameters() {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		final String reqString = p.encodeResourceToString(inParams);

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class).encodedXml().execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/$SOMEOPERATION", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals(CAPTURE_SERVLET.ourRequestBodyString, reqString);
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);


		resp = client
			.operation()
			.onType(Patient.class)
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class).encodedXml().execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$SOMEOPERATION", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals(CAPTURE_SERVLET.ourRequestBodyString, reqString);
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);


		resp = client
			.operation()
			.onInstance(new IdDt("Patient", "123"))
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class).encodedXml().execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$SOMEOPERATION", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertEquals(CAPTURE_SERVLET.ourRequestBodyString, reqString);
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);


		// @formatter:off
		resp = client
			.operation()
			.onInstance(new IdDt("http://foo.com/bar/baz/Patient/123/_history/22"))
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.execute();
		// @formatter:on
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123/$SOMEOPERATION", CAPTURE_SERVLET.ourRequestUri);

	}

	@Test
	public void testPageNext() {

		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = getPatientFeedWithOneResult();

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		ca.uhn.fhir.model.dstu2.resource.Bundle sourceBundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		sourceBundle.getLinkOrCreate(IBaseBundle.LINK_PREV).setUrl(ourServer.getBaseUrl() + "/fhir/prev");
		sourceBundle.getLinkOrCreate(IBaseBundle.LINK_NEXT).setUrl(ourServer.getBaseUrl() + "/fhir/next");


		ca.uhn.fhir.model.dstu2.resource.Bundle resp = client
			.loadPage()
			.next(sourceBundle)
			.execute();


		assertThat(resp.getEntry()).hasSize(1);
		assertEquals(ourServer.getBaseUrl() + "/fhir/next", CAPTURE_SERVLET.ourRequestUri);


	}

	@Test
	public void testPageNextNoLink() {
		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		ca.uhn.fhir.model.dstu2.resource.Bundle sourceBundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		try {
			client.loadPage().next(sourceBundle).execute();
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).contains("Can not perform paging operation because no link was found in Bundle with relation \"next\"");
		}
	}

	@Test
	public void testPagePrev() {


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = getPatientFeedWithOneResult();

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		ca.uhn.fhir.model.dstu2.resource.Bundle sourceBundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		sourceBundle.getLinkOrCreate("previous").setUrl(ourServer.getBaseUrl() + "/fhir/prev");


		ca.uhn.fhir.model.dstu2.resource.Bundle resp = client
			.loadPage()
			.previous(sourceBundle)
			.execute();


		assertThat(resp.getEntry()).hasSize(1);
		assertEquals(ourServer.getBaseUrl() + "/fhir/prev", CAPTURE_SERVLET.ourRequestUri);


		/*
		 * Try with "prev" instead of "previous"
		 */

		sourceBundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		sourceBundle.getLinkOrCreate("prev").setUrl(ourServer.getBaseUrl() + "/fhir/prev");


		resp = client
			.loadPage()
			.previous(sourceBundle)
			.execute();


		assertThat(resp.getEntry()).hasSize(1);
		assertEquals(ourServer.getBaseUrl() + "/fhir/prev", CAPTURE_SERVLET.ourRequestUri);


	}

	@Test
	public void testReadByUri() {

		Patient patient = new Patient();
		patient.addName().addFamily("FAM");
		final String respString = ourCtx.newXmlParser().encodeResourceToString(patient);

		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		Patient response;


		response = (Patient) client.read(new UriDt(ourServer.getBaseUrl() + "/fhir/Patient/123"));
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123", CAPTURE_SERVLET.ourRequestUri);
		assertEquals("FAM", response.getName().get(0).getFamily().get(0).getValue());
	}

	@Test
	public void testReadFluentByUri() {

		Patient patient = new Patient();
		patient.addName().addFamily("FAM");
		final String respString = ourCtx.newXmlParser().encodeResourceToString(patient);

		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		Patient response;

		response = client.read().resource(Patient.class).withUrl(new IdDt(ourServer.getBaseUrl() + "/AAA/Patient/123")).execute();
		assertEquals(ourServer.getBaseUrl() + "/AAA/Patient/123", CAPTURE_SERVLET.ourRequestUri);
		assertEquals("FAM", response.getName().get(0).getFamily().get(0).getValue());
	}

	@Test
	public void testReadUpdatedHeaderDoesntOverwriteResourceValue() {


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


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = input;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		ca.uhn.fhir.model.dstu2.resource.Bundle response;

		response = client
			.search()
			.forResource(Patient.class)
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();

		assertEquals("2015-06-22T15:48:57.554-04:00", ResourceMetadataKeyEnum.UPDATED.get(response).getValueAsString());
	}

	@Test
	public void testReadWithElementsParam() {
		String msg = "{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}";


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		IBaseResource response = client.read()
			.resource("Patient")
			.withId("123")
			.elementsSubset("name", "identifier")
			.execute();

		assertThat(CAPTURE_SERVLET.ourRequestUri).isIn(ourServer.getBaseUrl() + "/fhir/Patient/123?_elements=name%2Cidentifier",
			ourServer.getBaseUrl() + "/fhir/Patient/123?_elements=identifier%2Cname");
		assertEquals(Patient.class, response.getClass());

	}

	@Test
	public void testReadWithSummaryInvalid() {
		String msg = "<>>>><<<<>";


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_HTML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		try {
			client.read()
				.resource(Patient.class)
				.withId("123")
				.summaryMode(SummaryEnum.TEXT)
				.execute();
			fail();
		} catch (InvalidResponseException e) {
			assertThat(e.getMessage()).contains("String does not appear to be valid");
		}
	}

	@Test
	public void testReadWithSummaryParamHtml() {
		String msg = "<div>HELP IM A DIV</div>";


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_HTML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		Patient response = client.read()
			.resource(Patient.class)
			.withId("123")
			.summaryMode(SummaryEnum.TEXT)
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123?_summary=text", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(Patient.class, response.getClass());
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">HELP IM A DIV</div>", response.getText().getDiv().getValueAsString());

	}

	@Test
	public void testSearchByString() {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=james", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

	}

	@Test
	public void testSearchByUrl() {

		final String msg = getPatientFeedWithOneResult();


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		ca.uhn.fhir.model.dstu2.resource.Bundle response = client.search()
			.byUrl(ourServer.getBaseUrl() + "/AAA?name=http://foo|bar")
			.encodedJson()
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/AAA?name=http%3A//foo%7Cbar&_format=json", CAPTURE_SERVLET.ourRequestUri);
		assertNotNull(response);


		response = client.search()
			.byUrl("Patient?name=http://foo|bar")
			.encodedJson()
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=http%3A//foo%7Cbar&_format=json", CAPTURE_SERVLET.ourRequestUri);
		assertNotNull(response);


		response = client.search()
			.byUrl("/Patient?name=http://foo|bar")
			.encodedJson()
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=http%3A//foo%7Cbar&_format=json", CAPTURE_SERVLET.ourRequestUri);
		assertNotNull(response);


		response = client.search()
			.byUrl("Patient")
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient", CAPTURE_SERVLET.ourRequestUri);
		assertNotNull(response);


		response = client.search()
			.byUrl("Patient?")
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();

		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient", CAPTURE_SERVLET.ourRequestUri);
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
		String msg = IOUtils.toString(GenericJaxRsClientDstu2Test.class.getResourceAsStream("/bundle_orion.xml"));


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		ca.uhn.fhir.model.dstu2.resource.Bundle response = client.search()
			.forResource("Observation")
			.where(Patient.NAME.matches().value("FOO"))
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();


		Link link = response.getLink().get(0);
		assertEquals("just trying add link", link.getRelation());
		assertEquals("blarion", link.getUrl());

		Entry entry = response.getEntry().get(0);
		link = entry.getLink().get(0);
		assertEquals("orionhealth.edit", link.getRelation());
		assertEquals("Observation", link.getUrl());
	}

	@Test
	public void testSearchWithElementsParam() {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.elementsSubset("name", "identifier")
			.returnBundle(Bundle.class)
			.execute();


		assertThat(CAPTURE_SERVLET.ourRequestUri).isIn(ourServer.getBaseUrl() + "/fhir/Patient?name=james&_elements=name%2Cidentifier",
			ourServer.getBaseUrl() + "/fhir/Patient?name=james&_elements=identifier%2Cname");
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

	}

	@Test
	public void testSearchByPost() {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.elementsSubset("name", "identifier")
			.usingStyle(SearchStyleEnum.POST)
			.returnBundle(Bundle.class)
			.execute();


		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/_search?_elements=identifier%2Cname", CAPTURE_SERVLET.ourRequestUri);

		//		assertThat(MY_SERVLET.ourRequestUri,
		//				either(equalTo(ourServer.getBaseUrl() + "/fhir/Patient?name=james&_elements=name%2Cidentifier")).or(equalTo(ourServer.getBaseUrl() + "/fhir/Patient?name=james&_elements=identifier%2Cname")));

		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

		assertEquals("name=james", CAPTURE_SERVLET.ourRequestBodyString);

		assertEquals("application/x-www-form-urlencoded", CAPTURE_SERVLET.ourRequestContentType.replace(";char", "; char").toLowerCase());
		assertEquals(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_LEGACY, CAPTURE_SERVLET.ourRequestFirstHeaders.get("Accept").getValue());
		assertThat(CAPTURE_SERVLET.ourRequestFirstHeaders.get("User-Agent").getValue()).isNotEmpty();
	}

	@Test
	public void testSearchByPostUseJson() {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.elementsSubset("name", "identifier")
			.usingStyle(SearchStyleEnum.POST)
			.encodedJson()
			.returnBundle(Bundle.class)
			.execute();


		assertThat(CAPTURE_SERVLET.ourRequestUri).contains(ourServer.getBaseUrl() + "/fhir/Patient/_search?");
		assertThat(CAPTURE_SERVLET.ourRequestUri).contains("_elements=identifier%2Cname");

		//		assertThat(MY_SERVLET.ourRequestUri,
		//				either(equalTo(ourServer.getBaseUrl() + "/fhir/Patient?name=james&_elements=name%2Cidentifier")).or(equalTo(ourServer.getBaseUrl() + "/fhir/Patient?name=james&_elements=identifier%2Cname")));

		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

		assertEquals("name=james", CAPTURE_SERVLET.ourRequestBodyString);

		assertEquals("application/x-www-form-urlencoded", CAPTURE_SERVLET.ourRequestContentType);
		assertEquals(Constants.CT_FHIR_JSON, CAPTURE_SERVLET.ourRequestFirstHeaders.get("Accept").getValue());
	}

	@Test
	public void testSearchWithLastUpdated() {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.returnBundle(Bundle.class)
			.lastUpdated(new DateRangeParam("2011-01-01", "2012-01-01"))
			.execute();


		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=james&_lastUpdated=ge2011-01-01&_lastUpdated=le2012-01-01", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

	}

	@Test
	public void testSearchWithProfileAndSecurity() {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Bundle response = client.search()
			.forResource("Patient")
			.withProfile("http://foo1")
			.withProfile("http://foo2")
			.withSecurity("system1", "code1")
			.withSecurity("system2", "code2")
			.returnBundle(Bundle.class)
			.execute();


		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?_security=system1%7Ccode1&_security=system2%7Ccode2&_profile=http%3A%2F%2Ffoo1&_profile=http%3A%2F%2Ffoo2", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithReverseInclude() {

		String msg = getPatientFeedWithOneResult();


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Bundle response = client.search()
			.forResource(Patient.class)
			.encodedJson()
			.revInclude(new Include("Provenance:target"))
			.returnBundle(Bundle.class)
			.execute();


		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?_revinclude=Provenance%3Atarget&_format=json", CAPTURE_SERVLET.ourRequestUri);

	}

	@Test
	public void testSearchWithSummaryParam() {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = msg;


		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.summaryMode(SummaryEnum.FALSE)
			.returnBundle(Bundle.class)
			.execute();


		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=james&_summary=false", CAPTURE_SERVLET.ourRequestUri);
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

	}

	@Test
	public void testTransactionWithListOfResources() {

		ca.uhn.fhir.model.dstu2.resource.Bundle resp = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		resp.addEntry().getResponse().setLocation("Patient/1/_history/1");
		resp.addEntry().getResponse().setLocation("Patient/2/_history/2");
		String respString = ourCtx.newJsonParser().encodeResourceToString(resp);


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		List<IBaseResource> input = new ArrayList<>();

		Patient p1 = new Patient(); // No ID
		p1.addName().addFamily("PATIENT1");
		input.add(p1);

		Patient p2 = new Patient(); // Yes ID
		p2.addName().addFamily("PATIENT2");
		p2.setId("http://example.com/Patient/2");
		input.add(p2);


		List<IBaseResource> response = client.transaction()
			.withResources(input)
			.encodedJson()
			.execute();


		assertEquals(ourServer.getBaseUrl() + "/fhir", CAPTURE_SERVLET.ourRequestUri);
		assertThat(response).hasSize(2);

		String requestString = CAPTURE_SERVLET.ourRequestBodyString;
		ca.uhn.fhir.model.dstu2.resource.Bundle requestBundle = ourCtx.newJsonParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, requestString);
		assertThat(requestBundle.getEntry()).hasSize(2);
		assertEquals("POST", requestBundle.getEntry().get(0).getRequest().getMethod());
		assertEquals("PUT", requestBundle.getEntry().get(1).getRequest().getMethod());
		assertEquals("http://example.com/Patient/2", requestBundle.getEntry().get(1).getFullUrl());

		p1 = (Patient) response.get(0);
		assertEquals(new IdDt("Patient/1/_history/1"), p1.getId().toUnqualified());
		// assertEquals("PATIENT1", p1.getName().get(0).getFamily().get(0).getValue());

		p2 = (Patient) response.get(1);
		assertEquals(new IdDt("Patient/2/_history/2"), p2.getId().toUnqualified());
		// assertEquals("PATIENT2", p2.getName().get(0).getFamily().get(0).getValue());
	}

	@Test
	public void testTransactionWithString() {

		ca.uhn.fhir.model.dstu2.resource.Bundle req = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		Patient patient = new Patient();
		patient.addName().addFamily("PAT_FAMILY");
		req.addEntry().setResource(patient);
		Observation observation = new Observation();
		observation.getCode().setText("OBS_TEXT");
		req.addEntry().setResource(observation);
		String reqString = ourCtx.newJsonParser().encodeResourceToString(req);

		ca.uhn.fhir.model.dstu2.resource.Bundle resp = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		resp.addEntry().getResponse().setLocation("Patient/1/_history/1");
		resp.addEntry().getResponse().setLocation("Patient/2/_history/2");


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = reqString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		String response = client.transaction()
			.withBundle(reqString)
			.execute();


		assertEquals(ourServer.getBaseUrl() + "/fhir/", CAPTURE_SERVLET.ourRequestUri);
		assertThat(response).contains("\"Bundle\"");
		assertEquals("application/json+fhir;charset=UTF-8", CAPTURE_SERVLET.ourRequestFirstHeaders.get("Content-Type").getValue());


		response = client.transaction()
			.withBundle(reqString)
			.encodedXml()
			.execute();


		assertEquals(ourServer.getBaseUrl() + "/fhir/", CAPTURE_SERVLET.ourRequestUri);
		assertEquals("application/xml+fhir;charset=UTF-8", CAPTURE_SERVLET.ourRequestFirstHeaders.get("Content-Type").getValue());

	}

	@Test
	public void testTransactionWithTransactionResource() {

		ca.uhn.fhir.model.dstu2.resource.Bundle resp = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		resp.addEntry().getResponse().setLocation("Patient/1/_history/1");
		resp.addEntry().getResponse().setLocation("Patient/2/_history/2");
		String respString = ourCtx.newJsonParser().encodeResourceToString(resp);


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		ca.uhn.fhir.model.dstu2.resource.Bundle input = new ca.uhn.fhir.model.dstu2.resource.Bundle();

		Patient p1 = new Patient(); // No ID
		p1.addName().addFamily("PATIENT1");
		input.addEntry().setResource(p1);

		Patient p2 = new Patient(); // Yes ID
		p2.addName().addFamily("PATIENT2");
		p2.setId("Patient/2");
		input.addEntry().setResource(p2);


		ca.uhn.fhir.model.dstu2.resource.Bundle response = client.transaction()
			.withBundle(input)
			.encodedJson()
			.execute();


		assertEquals(ourServer.getBaseUrl() + "/fhir", CAPTURE_SERVLET.ourRequestUri);
		assertThat(response.getEntry()).hasSize(2);

		assertEquals("Patient/1/_history/1", response.getEntry().get(0).getResponse().getLocation());
		assertEquals("Patient/2/_history/2", response.getEntry().get(1).getResponse().getLocation());
	}

	@Test
	public void testUpdateConditional() {


		CAPTURE_SERVLET.ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Patient p = new Patient();
		p.addName().addFamily("FOOFAMILY");

		client.update().resource(p).conditionalByUrl("Patient?name=foo").encodedXml().execute();
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(CAPTURE_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals("PUT", CAPTURE_SERVLET.ourRequestMethod);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=foo", CAPTURE_SERVLET.ourRequestUri);


		client.update().resource(p).conditionalByUrl("Patient?name=http://foo|bar").encodedXml().execute();
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(CAPTURE_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals("PUT", CAPTURE_SERVLET.ourRequestMethod);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=http%3A//foo%7Cbar", CAPTURE_SERVLET.ourRequestUri);


		client.update().resource(ourCtx.newXmlParser().encodeResourceToString(p)).conditionalByUrl("Patient?name=foo").encodedXml().execute();
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(CAPTURE_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals("PUT", CAPTURE_SERVLET.ourRequestMethod);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=foo", CAPTURE_SERVLET.ourRequestUri);


		client.update().resource(p).conditional().where(Patient.NAME.matches().value("foo")).and(Patient.ADDRESS.matches().value("AAA|BBB")).encodedXml().execute();
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(CAPTURE_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals("PUT", CAPTURE_SERVLET.ourRequestMethod);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=foo&address=AAA%5C%7CBBB", CAPTURE_SERVLET.ourRequestUri);


		client.update().resource(ourCtx.newXmlParser().encodeResourceToString(p)).conditional().where(Patient.NAME.matches().value("foo")).and(Patient.ADDRESS.matches().value("AAA|BBB")).encodedXml().execute();
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(CAPTURE_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals("PUT", CAPTURE_SERVLET.ourRequestMethod);
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient?name=foo&address=AAA%5C%7CBBB", CAPTURE_SERVLET.ourRequestUri);


	}

	@Test
	public void testUpdateNonFluent() {


		CAPTURE_SERVLET.ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");
		client.setEncoding(EncodingEnum.XML);


		Patient p = new Patient();
		p.addName().addFamily("FOOFAMILY");

		client.update(new IdDt("Patient/123"), p);
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(CAPTURE_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123?_format=xml", CAPTURE_SERVLET.ourRequestUri);
		assertEquals("PUT", CAPTURE_SERVLET.ourRequestMethod);


		client.update("123", p);
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, CAPTURE_SERVLET.ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char"));
		assertThat(CAPTURE_SERVLET.ourRequestBodyString).contains("<family value=\"FOOFAMILY\"/>");
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/123?_format=xml", CAPTURE_SERVLET.ourRequestUri);
		assertEquals("PUT", CAPTURE_SERVLET.ourRequestMethod);

	}

	@Test
	public void testUpdatePrefer() {


		CAPTURE_SERVLET.ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");


		Patient p = new Patient();
		p.setId(new IdDt("1"));
		p.addName().addFamily("FOOFAMILY");

		client.update().resource(p).prefer(PreferReturnEnum.MINIMAL).execute();
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_PREFER)).hasSize(1);
		assertEquals(Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_MINIMAL, CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_PREFER).get(0).getValue());


		client.update().resource(p).prefer(PreferReturnEnum.REPRESENTATION).execute();
		assertThat(CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_PREFER)).hasSize(1);
		assertEquals(Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_REPRESENTATION, CAPTURE_SERVLET.ourRequestHeaders.get(Constants.HEADER_PREFER).get(0).getValue());


	}

	@Test
	public void testUpdateReturningResourceBody() {
		Patient p = new Patient();
		p.setId("123");
		final String formatted = ourCtx.newXmlParser().encodeResourceToString(p);


		CAPTURE_SERVLET.ourResponseStatus = Constants.STATUS_HTTP_200_OK;
		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = formatted;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		p = new Patient();
		p.setId(new IdDt("1"));
		p.addName().addFamily("FOOFAMILY");

		MethodOutcome output = client.update().resource(p).execute();
		assertNotNull(output.getResource());
		assertEquals("Patient/123", output.getResource().getIdElement().toUnqualifiedVersionless().getValue());
	}

	@Test
	public void testValidateFluent() {

		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDiagnostics("FOOBAR");
		final String msg = ourCtx.newXmlParser().encodeResourceToString(oo);


		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");

		Patient p = new Patient();
		p.addName().addGiven("GIVEN");


		MethodOutcome response;

		response = client.validate().resource(p).encodedXml().execute();
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$validate", CAPTURE_SERVLET.ourRequestUri);
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);
		assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"resource\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><name><given value=\"GIVEN\"/></name></Patient></resource></parameter></Parameters>", CAPTURE_SERVLET.ourRequestBodyString);
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", toOo(response.getOperationOutcome()).getIssueFirstRep().getDiagnosticsElement().getValue());


		response = client.validate().resource(ourCtx.newXmlParser().encodeResourceToString(p)).encodedXml().execute();
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$validate", CAPTURE_SERVLET.ourRequestUri);
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);
		assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"resource\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><name><given value=\"GIVEN\"/></name></Patient></resource></parameter></Parameters>", CAPTURE_SERVLET.ourRequestBodyString);
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", toOo(response.getOperationOutcome()).getIssueFirstRep().getDiagnosticsElement().getValue());


		response = client.validate().resource(ourCtx.newJsonParser().encodeResourceToString(p)).execute();
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$validate", CAPTURE_SERVLET.ourRequestUri);
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);
		assertEquals("{\"resourceType\":\"Parameters\",\"parameter\":[{\"name\":\"resource\",\"resource\":{\"resourceType\":\"Patient\",\"name\":[{\"given\":[\"GIVEN\"]}]}}]}", CAPTURE_SERVLET.ourRequestBodyString);
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", toOo(response.getOperationOutcome()).getIssueFirstRep().getDiagnosticsElement().getValue());


		response = client.validate().resource(ourCtx.newJsonParser().encodeResourceToString(p)).prettyPrint().execute();
		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$validate?_pretty=true", CAPTURE_SERVLET.ourRequestUri);
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);
		assertThat(CAPTURE_SERVLET.ourRequestBodyString).contains("\"resourceType\": \"Parameters\",\n");
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", toOo(response.getOperationOutcome()).getIssueFirstRep().getDiagnosticsElement().getValue());

	}

	@Test
	public void testValidateNonFluent() {
		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDiagnostics("FOOBAR");
		final String msg = ourCtx.newXmlParser().encodeResourceToString(oo);

		CAPTURE_SERVLET.ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		CAPTURE_SERVLET.ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/fhir");
		client.setEncoding(EncodingEnum.XML);

		Patient p = new Patient();
		p.addName().addGiven("GIVEN");


		MethodOutcome response;


		response = client.validate(p);


		assertEquals(ourServer.getBaseUrl() + "/fhir/Patient/$validate?_format=xml", CAPTURE_SERVLET.ourRequestUri);
		assertEquals("POST", CAPTURE_SERVLET.ourRequestMethod);
		assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"resource\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><name><given value=\"GIVEN\"/></name></Patient></resource></parameter></Parameters>", CAPTURE_SERVLET.ourRequestBodyString);
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", toOo(response.getOperationOutcome()).getIssueFirstRep().getDiagnosticsElement().getValue());

	}

	private OperationOutcome toOo(IBaseOperationOutcome theOperationOutcome) {
		return (OperationOutcome) theOperationOutcome;
	}

}
