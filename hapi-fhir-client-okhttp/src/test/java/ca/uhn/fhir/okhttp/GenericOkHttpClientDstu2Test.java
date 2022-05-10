package ca.uhn.fhir.okhttp;

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
import ca.uhn.fhir.okhttp.client.OkHttpRestfulClientFactory;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.api.SearchStyleEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.exceptions.InvalidResponseException;
import ca.uhn.fhir.rest.client.impl.RestfulClientFactory;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.test.utilities.JettyUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class GenericOkHttpClientDstu2Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GenericOkHttpClientDstu2Test.class);
	private static FhirContext ourCtx;
	private static int ourPort;
	private static Server ourServer;
	private static int ourResponseCount = 0;
	private static String[] ourResponseBodies;
	private static String ourResponseBody;
	private static String ourResponseContentType;
	private static int ourResponseStatus;
	private static String ourRequestUri;
	private static List<String> ourRequestUriAll;
	private static String ourRequestMethod;
	private static String ourRequestContentType;
	private static byte[] ourRequestBodyBytes;
	private static String ourRequestBodyString;
	private static ArrayListMultimap<String, Header> ourRequestHeaders;
	private static List<ArrayListMultimap<String, Header>> ourRequestHeadersAll;
	private static Map<String, Header> ourRequestFirstHeaders;

	/**
	 * This suite of tests can be reconfigured to test a different RestfulClientFactory implementation by
	 * changing the instance returned here.
	 *
	 * @param context FhirContext or null
	 * @return RestfulClientFactory implementation to run tests against
	 */
	private RestfulClientFactory createNewClientFactoryForTesting(FhirContext context) {
		if (context == null) {
			return new OkHttpRestfulClientFactory();
		} else {
			return new OkHttpRestfulClientFactory(ourCtx);
		}
	}

	@BeforeEach
	public void before() {
		RestfulClientFactory clientFactory = createNewClientFactoryForTesting(ourCtx);
		clientFactory.setServerValidationMode(ServerValidationModeEnum.NEVER);

		ourCtx.setRestfulClientFactory(clientFactory);
		ourResponseCount = 0;
	}

	@Test
	public void testProviderWhereWeForgotToSetTheContext() throws Exception {
		RestfulClientFactory clientFactory = createNewClientFactoryForTesting(null);
		clientFactory.setServerValidationMode(ServerValidationModeEnum.NEVER);

		ourCtx.setRestfulClientFactory(clientFactory);

		try {
			ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");
			fail();
		} catch (IllegalStateException e) {
			String factoryClassName = clientFactory.getClass().getSimpleName();
			assertEquals(Msg.code(1355) + factoryClassName + " does not have FhirContext defined. This must be set via " + factoryClassName + "#setFhirContext(FhirContext)", e.getMessage());
		}
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
	public void testAcceptHeaderFetchConformance() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final String respString = p.encodeResourceToString(conf);
		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		client.fetchConformance().ofType(Conformance.class).execute();
		assertEquals("http://localhost:" + ourPort + "/fhir/metadata", ourRequestUri);
		assertEquals(1, ourRequestHeaders.get("Accept").size());
		assertThat(ourRequestHeaders.get("Accept").get(0).getValue(), containsString(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_LEGACY));

		client.fetchConformance().ofType(Conformance.class).encodedJson().execute();
		assertEquals("http://localhost:" + ourPort + "/fhir/metadata?_format=json", ourRequestUri);
		assertEquals(1, ourRequestHeaders.get("Accept").size());
		assertThat(ourRequestHeaders.get("Accept").get(0).getValue(), containsString(Constants.CT_FHIR_JSON));

		client.fetchConformance().ofType(Conformance.class).encodedXml().execute();
		assertEquals("http://localhost:" + ourPort + "/fhir/metadata?_format=xml", ourRequestUri);
		assertEquals(1, ourRequestHeaders.get("Accept").size());
		assertThat(ourRequestHeaders.get("Accept").get(0).getValue(), containsString(Constants.CT_FHIR_XML));
	}

	@Test
	public void testAcceptHeaderPreflightConformance() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final Patient patient = new Patient();
		patient.addName().addFamily("FAMILY");

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBodies = new String[]{p.encodeResourceToString(conf), p.encodeResourceToString(patient)};

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Patient resp = client.read(Patient.class, new IdDt("123"));
		assertEquals("FAMILY", resp.getName().get(0).getFamily().get(0).getValue());
		assertEquals("http://localhost:" + ourPort + "/fhir/metadata", ourRequestUriAll.get(0));
		assertEquals(1, ourRequestHeadersAll.get(0).get("Accept").size());
		assertThat(ourRequestHeadersAll.get(0).get("Accept").get(0).getValue(), containsString(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_LEGACY));
		assertThat(ourRequestHeadersAll.get(0).get("Accept").get(0).getValue(), containsString(Constants.CT_FHIR_XML));
		assertThat(ourRequestHeadersAll.get(0).get("Accept").get(0).getValue(), containsString(Constants.CT_FHIR_JSON));
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/123", ourRequestUriAll.get(1));
		assertEquals(1, ourRequestHeadersAll.get(1).get("Accept").size());
		assertThat(ourRequestHeadersAll.get(1).get("Accept").get(0).getValue(), containsString(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_LEGACY));
		assertThat(ourRequestHeadersAll.get(1).get("Accept").get(0).getValue(), containsString(Constants.CT_FHIR_XML));
		assertThat(ourRequestHeadersAll.get(1).get("Accept").get(0).getValue(), containsString(Constants.CT_FHIR_JSON));
	}

	@Test
	public void testAcceptHeaderPreflightConformancePreferJson() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final Patient patient = new Patient();
		patient.addName().addFamily("FAMILY");

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBodies = new String[]{p.encodeResourceToString(conf), p.encodeResourceToString(patient)};

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");
		client.setEncoding(EncodingEnum.JSON);

		Patient resp = client.read(Patient.class, new IdDt("123"));
		assertEquals("FAMILY", resp.getName().get(0).getFamily().get(0).getValue());
		assertEquals("http://localhost:" + ourPort + "/fhir/metadata?_format=json", ourRequestUriAll.get(0));
		assertEquals(1, ourRequestHeadersAll.get(0).get("Accept").size());
		assertThat(ourRequestHeadersAll.get(0).get("Accept").get(0).getValue(), containsString(Constants.CT_FHIR_JSON));
		assertThat(ourRequestHeadersAll.get(0).get("Accept").get(0).getValue(), not(containsString(Constants.CT_FHIR_XML)));
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/123?_format=json", ourRequestUriAll.get(1));
		assertEquals(1, ourRequestHeadersAll.get(1).get("Accept").size());
		assertThat(ourRequestHeadersAll.get(1).get("Accept").get(0).getValue(), containsString(Constants.CT_FHIR_JSON));
		assertThat(ourRequestHeadersAll.get(1).get("Accept").get(0).getValue(), not(containsString(Constants.CT_FHIR_XML)));
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testConformance() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final String respString = p.encodeResourceToString(conf);

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Conformance resp = (Conformance) client.capabilities().ofType(Conformance.class).execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/metadata", ourRequestUri);
		assertEquals("COPY", resp.getCopyright());
		assertEquals("GET", ourRequestMethod);
	}

	@Test
	public void testCreate() throws Exception {
		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Patient p = new Patient();
		p.addName().addFamily("FOOFAMILY");

		client.create().resource(p).encodedXml().execute();

		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertContentTypeEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(ourRequestBodyString, containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient", ourRequestUri);
		assertEquals("POST", ourRequestMethod);

		p.setId("123");

		client.create().resource(p).encodedXml().execute();
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertContentTypeEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		String body = ourRequestBodyString;
		assertThat(body, containsString("<family value=\"FOOFAMILY\"/>"));
		assertThat(body, not(containsString("123")));
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient", ourRequestUri);
		assertEquals("POST", ourRequestMethod);
	}

	@Test
	public void testCreateConditional() throws Exception {
		ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Patient p = new Patient();
		p.addName().addFamily("FOOFAMILY");

		client.create().resource(p).conditionalByUrl("Patient?name=foo").encodedXml().execute();
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		String expectedContentTypeHeader = EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8;
		assertContentTypeEquals(expectedContentTypeHeader);
		assertThat(ourRequestBodyString, containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient", ourRequestUri);
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient?name=foo", ourRequestFirstHeaders.get(Constants.HEADER_IF_NONE_EXIST).getValue());
		assertEquals("POST", ourRequestMethod);

		client.create().resource(p).conditionalByUrl("Patient?name=http://foo|bar").encodedXml().execute();
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertContentTypeEquals(expectedContentTypeHeader);
		assertThat(ourRequestBodyString, containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient", ourRequestUri);
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient?name=http%3A//foo%7Cbar", ourRequestFirstHeaders.get(Constants.HEADER_IF_NONE_EXIST).getValue());
		assertEquals("POST", ourRequestMethod);

		client.create().resource(p).conditional().where(Patient.NAME.matches().value("foo")).encodedXml().execute();
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertContentTypeEquals(expectedContentTypeHeader);
		assertThat(ourRequestBodyString, containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient", ourRequestUri);
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient?name=foo", ourRequestFirstHeaders.get(Constants.HEADER_IF_NONE_EXIST).getValue());
		assertEquals("POST", ourRequestMethod);
	}

	private void assertContentTypeEquals(String expectedContentTypeHeader) {
		// charsets are case-insensitive according to the HTTP spec (e.g. utf-8 == UTF-8):
		// https://tools.ietf.org/html/rfc2616#section-3.4
		assertThat(getActualContentTypeHeader(), equalToIgnoringCase(expectedContentTypeHeader));
	}

	@Test
	public void testCreatePrefer() throws Exception {
		ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Patient p = new Patient();
		p.addName().addFamily("FOOFAMILY");

		client.create().resource(p).prefer(PreferReturnEnum.MINIMAL).execute();
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_PREFER).size());
		assertEquals(Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_MINIMAL, ourRequestHeaders.get(Constants.HEADER_PREFER).get(0).getValue());

		client.create().resource(p).prefer(PreferReturnEnum.REPRESENTATION).execute();
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_PREFER).size());
		assertEquals(Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_REPRESENTATION, ourRequestHeaders.get(Constants.HEADER_PREFER).get(0).getValue());
	}

	@Test
	public void testCreateReturningResourceBody() throws Exception {
		Patient p = new Patient();
		p.setId("123");
		final String formatted = ourCtx.newXmlParser().encodeResourceToString(p);

		ourResponseStatus = Constants.STATUS_HTTP_200_OK;
		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = formatted;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		p = new Patient();
		p.setId(new IdDt("1"));
		p.addName().addFamily("FOOFAMILY");

		MethodOutcome output = client.create().resource(p).execute();
		assertNotNull(output.getResource());
		assertEquals("Patient/123", output.getResource().getIdElement().toUnqualifiedVersionless().getValue());
	}

	@Test
	public void testDeleteConditional() throws Exception {
		ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		client.delete().resourceById(new IdDt("Patient/123")).execute();
		assertEquals("DELETE", ourRequestMethod);
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/123", ourRequestUri);

		client.delete().resourceConditionalByUrl("Patient?name=foo").execute();
		assertEquals("DELETE", ourRequestMethod);
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient?name=foo", ourRequestUri);

		client.delete().resourceConditionalByType("Patient").where(Patient.NAME.matches().value("foo")).execute();
		assertEquals("DELETE", ourRequestMethod);
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient?name=foo", ourRequestUri);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testDeleteNonFluent() throws Exception {
		ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		client.delete().resourceById(new IdDt("Patient/123")).execute();
		assertEquals("DELETE", ourRequestMethod);
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/123", ourRequestUri);

	}

	@Test
	public void testHistory() throws Exception {
		final String msg = getPatientFeedWithOneResult();

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		ca.uhn.fhir.model.dstu2.resource.Bundle response;

		response = client
			.history()
			.onServer()
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/_history", ourRequestUri);
		assertEquals(1, response.getEntry().size());

		response = client
			.history()
			.onServer()
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.since((Date) null)
			.count(null)
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/_history", ourRequestUri);
		assertEquals(1, response.getEntry().size());

		response = client
			.history()
			.onServer()
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.since(new InstantDt())
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/_history", ourRequestUri);
		assertEquals(1, response.getEntry().size());

		response = client
			.history()
			.onType(Patient.class)
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/_history", ourRequestUri);
		assertEquals(1, response.getEntry().size());

		response = client
			.history()
			.onInstance(new IdDt("Patient", "123"))
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/123/_history", ourRequestUri);
		assertEquals(1, response.getEntry().size());

		response = client
			.history()
			.onInstance(new IdDt("Patient", "123"))
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.count(123)
			.since(new InstantDt("2001-01-02T11:22:33Z"))
			.execute();

		assertThat(ourRequestUri, either(equalTo("http://localhost:" + ourPort + "/fhir/Patient/123/_history?_since=2001-01-02T11:22:33Z&_count=123"))
			.or(equalTo("http://localhost:" + ourPort + "/fhir/Patient/123/_history?_count=123&_since=2001-01-02T11:22:33Z")));
		assertEquals(1, response.getEntry().size());

		response = client
			.history()
			.onInstance(new IdDt("Patient", "123"))
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.since(new InstantDt("2001-01-02T11:22:33Z").getValue())
			.execute();

		assertThat(ourRequestUri, containsString("_since=2001-01"));
		assertEquals(1, response.getEntry().size());
	}

	@Test
	public void testMetaAdd() throws Exception {
		IParser p = ourCtx.newXmlParser();

		MetaDt inMeta = new MetaDt().addProfile("urn:profile:in");

		Parameters outParams = new Parameters();
		outParams.addParameter().setName("meta").setValue(new MetaDt().addProfile("urn:profile:out"));
		final String respString = p.encodeResourceToString(outParams);

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		MetaDt resp = client
			.meta()
			.add()
			.onResource(new IdDt("Patient/123"))
			.meta(inMeta)
			.encodedXml()
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/123/$meta-add", ourRequestUri);
		assertEquals("urn:profile:out", resp.getProfile().get(0).getValue());
		assertEquals("POST", ourRequestMethod);
		assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"meta\"/><valueMeta><profile value=\"urn:profile:in\"/></valueMeta></parameter></Parameters>",
			ourRequestBodyString);
	}

	@Test
	public void testMetaGet() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("meta").setValue(new MetaDt().addProfile("urn:profile:in"));

		Parameters outParams = new Parameters();
		outParams.addParameter().setName("meta").setValue(new MetaDt().addProfile("urn:profile:out"));
		final String respString = p.encodeResourceToString(outParams);

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		MetaDt resp = client
			.meta()
			.get(MetaDt.class)
			.fromServer()
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/$meta", ourRequestUri);
		assertEquals("urn:profile:out", resp.getProfile().get(0).getValue());
		assertEquals("GET", ourRequestMethod);

		resp = client
			.meta()
			.get(MetaDt.class)
			.fromType("Patient")
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/$meta", ourRequestUri);
		assertEquals("urn:profile:out", resp.getProfile().get(0).getValue());
		assertEquals("GET", ourRequestMethod);

		resp = client
			.meta()
			.get(MetaDt.class)
			.fromResource(new IdDt("Patient/123"))
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/123/$meta", ourRequestUri);
		assertEquals("urn:profile:out", resp.getProfile().get(0).getValue());
		assertEquals("GET", ourRequestMethod);
	}

	@Test
	public void testOperationAsGetWithInParameters() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("param1").setValue(new StringDt("STRINGVALIN1"));
		inParams.addParameter().setName("param1").setValue(new StringDt("STRINGVALIN1b"));
		inParams.addParameter().setName("param2").setValue(new StringDt("STRINGVALIN2"));

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.useHttpGet()
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2", ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", ourRequestMethod);

		resp = client
			.operation()
			.onType(Patient.class)
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.useHttpGet()
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2", ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", ourRequestMethod);

		resp = client
			.operation()
			.onInstance(new IdDt("Patient", "123"))
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.useHttpGet()
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/123/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2", ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", ourRequestMethod);

		resp = client
			.operation()
			.onInstance(new IdDt("http://foo.com/bar/baz/Patient/123/_history/22"))
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.useHttpGet()
			.execute();
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/123/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2", ourRequestUri);
	}

	@Test
	public void testOperationAsGetWithNoInParameters() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.useHttpGet()
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/$SOMEOPERATION", ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", ourRequestMethod);

		resp = client
			.operation()
			.onType(Patient.class)
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.useHttpGet()
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/$SOMEOPERATION", ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", ourRequestMethod);

		resp = client
			.operation()
			.onInstance(new IdDt("Patient", "123"))
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.useHttpGet()
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/123/$SOMEOPERATION", ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", ourRequestMethod);

		resp = client
			.operation()
			.onInstance(new IdDt("http://foo.com/bar/baz/Patient/123/_history/22"))
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.useHttpGet()
			.execute();
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/123/$SOMEOPERATION", ourRequestUri);
	}

	@Test
	public void testOperationWithBundleResponseJson() throws Exception {
		ourResponseContentType = Constants.CT_FHIR_JSON;
		final String respString = "{\n" + "    \"resourceType\":\"Bundle\",\n" + "    \"id\":\"8cef5f2a-0ba9-43a5-be26-c8dde9ff0e19\",\n" + "    \"base\":\"http://localhost:" + ourPort + "/fhir\"\n"
			+ "}";
		ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

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
	public void testOperationWithBundleResponseXml() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		inParams.addParameter().setValue(new StringDt("STRINGVALIN1"));
		inParams.addParameter().setValue(new StringDt("STRINGVALIN2"));
		String reqString = p.encodeResourceToString(inParams);

		ca.uhn.fhir.model.dstu2.resource.Bundle outParams = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		outParams.setTotal(123);
		final String respString = p.encodeResourceToString(outParams);

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameters(inParams).encodedXml().execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/$SOMEOPERATION", ourRequestUri);
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertContentTypeEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertEquals(ourRequestBodyString, reqString);
		assertEquals("POST", ourRequestMethod);
		assertEquals(1, resp.getParameter().size());
		assertEquals(ca.uhn.fhir.model.dstu2.resource.Bundle.class, resp.getParameter().get(0).getResource().getClass());
	}

	@Test
	public void testOperationWithInlineParams() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameter(Parameters.class, "name1", new StringDt("value1"))
			.andParameter("name2", new StringDt("value1"))
			.encodedXml()
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/$SOMEOPERATION", ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertContentTypeEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertEquals("POST", ourRequestMethod);
		assertEquals(
			"<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"name1\"/><valueString value=\"value1\"/></parameter><parameter><name value=\"name2\"/><valueString value=\"value1\"/></parameter></Parameters>",
			(ourRequestBodyString));

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

		assertEquals("http://localhost:" + ourPort + "/fhir/$SOMEOPERATION", ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertContentTypeEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertEquals("POST", ourRequestMethod);
		assertEquals(
			"<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"name1\"/><valueIdentifier><system value=\"system1\"/><value value=\"value1\"/></valueIdentifier></parameter><parameter><name value=\"name2\"/><valueString value=\"value1\"/></parameter></Parameters>",
			(ourRequestBodyString));

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

		assertEquals("http://localhost:" + ourPort + "/fhir/$SOMEOPERATION", ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertContentTypeEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertEquals("POST", ourRequestMethod);
		assertEquals(
			"<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"name1\"/><valueIdentifier><system value=\"system1\"/><value value=\"value1\"/></valueIdentifier></parameter><parameter><name value=\"name2\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><active value=\"true\"/></Patient></resource></parameter></Parameters>",
			(ourRequestBodyString));
	}

	@Test
	public void testOperationWithInvalidParam() {
		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

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

		assertThrows(IllegalArgumentException.class, () -> {
			client
				.operation()
				.onServer()
				.named("$SOMEOPERATION")
				.withParameter(Parameters.class, "name1", weirdBase)
				.execute();
		});

	}

	@Test
	public void testOperationWithProfiledDatatypeParam() throws IOException, Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		client
			.operation()
			.onInstance(new IdDt("http://foo/Patient/1"))
			.named("validate-code")
			.withParameter(Parameters.class, "code", new CodeDt("8495-4"))
			.andParameter("system", new UriDt("http://loinc.org"))
			.useHttpGet()
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/1/$validate-code?code=8495-4&system=http%3A%2F%2Floinc.org", ourRequestUri);

		client
			.operation()
			.onInstance(new IdDt("http://foo/Patient/1"))
			.named("validate-code")
			.withParameter(Parameters.class, "code", new CodeDt("8495-4"))
			.andParameter("system", new UriDt("http://loinc.org"))
			.encodedXml()
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/1/$validate-code", ourRequestUri);
		ourLog.info(ourRequestBodyString);
		assertEquals(
			"<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"code\"/><valueCode value=\"8495-4\"/></parameter><parameter><name value=\"system\"/><valueUri value=\"http://loinc.org\"/></parameter></Parameters>",
			ourRequestBodyString);
	}

	@Test
	public void testOperationWithListOfParameterResponse() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		inParams.addParameter().setValue(new StringDt("STRINGVALIN1"));
		inParams.addParameter().setValue(new StringDt("STRINGVALIN2"));
		String reqString = p.encodeResourceToString(inParams);

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameters(inParams).encodedXml().execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/$SOMEOPERATION", ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertContentTypeEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertEquals(ourRequestBodyString, reqString);
		assertEquals("POST", ourRequestMethod);

		resp = client
			.operation()
			.onType(Patient.class)
			.named("$SOMEOPERATION")
			.withParameters(inParams).encodedXml().execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/$SOMEOPERATION", ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertContentTypeEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertEquals(ourRequestBodyString, reqString);
		assertEquals("POST", ourRequestMethod);

		resp = client
			.operation()
			.onInstance(new IdDt("Patient", "123"))
			.named("$SOMEOPERATION")
			.withParameters(inParams).encodedXml().execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/123/$SOMEOPERATION", ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertContentTypeEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertEquals(ourRequestBodyString, reqString);
		assertEquals("POST", ourRequestMethod);

		resp = client.operation().onInstance(new IdDt("http://foo.com/bar/baz/Patient/123/_history/22")).named("$SOMEOPERATION").withParameters(inParams).execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/123/$SOMEOPERATION", ourRequestUri);
	}

	@Test
	public void testOperationWithNoInParameters() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		final String reqString = p.encodeResourceToString(inParams);

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class).encodedXml().execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/$SOMEOPERATION", ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertContentTypeEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertEquals(ourRequestBodyString, reqString);
		assertEquals("POST", ourRequestMethod);

		resp = client
			.operation()
			.onType(Patient.class)
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class).encodedXml().execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/$SOMEOPERATION", ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertContentTypeEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertEquals(ourRequestBodyString, reqString);
		assertEquals("POST", ourRequestMethod);

		resp = client
			.operation()
			.onInstance(new IdDt("Patient", "123"))
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class).encodedXml().execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/123/$SOMEOPERATION", ourRequestUri);
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertContentTypeEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertEquals(ourRequestBodyString, reqString);
		assertEquals("POST", ourRequestMethod);

		resp = client
			.operation()
			.onInstance(new IdDt("http://foo.com/bar/baz/Patient/123/_history/22"))
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/123/$SOMEOPERATION", ourRequestUri);
	}

	@Test
	public void testPageNext() throws Exception {
		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = getPatientFeedWithOneResult();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		ca.uhn.fhir.model.dstu2.resource.Bundle sourceBundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		sourceBundle.getLinkOrCreate(IBaseBundle.LINK_PREV).setUrl("http://localhost:" + ourPort + "/fhir/prev");
		sourceBundle.getLinkOrCreate(IBaseBundle.LINK_NEXT).setUrl("http://localhost:" + ourPort + "/fhir/next");

		ca.uhn.fhir.model.dstu2.resource.Bundle resp = client
			.loadPage()
			.next(sourceBundle)
			.execute();

		assertEquals(1, resp.getEntry().size());
		assertEquals("http://localhost:" + ourPort + "/fhir/next", ourRequestUri);
	}

	@Test
	public void testPageNextNoLink() throws Exception {
		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		ca.uhn.fhir.model.dstu2.resource.Bundle sourceBundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		try {
			client.loadPage().next(sourceBundle).execute();
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), containsString("Can not perform paging operation because no link was found in Bundle with relation \"next\""));
		}
	}

	@Test
	public void testPagePrev() throws Exception {
		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = getPatientFeedWithOneResult();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		ca.uhn.fhir.model.dstu2.resource.Bundle sourceBundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		sourceBundle.getLinkOrCreate("previous").setUrl("http://localhost:" + ourPort + "/fhir/prev");

		ca.uhn.fhir.model.dstu2.resource.Bundle resp = client
			.loadPage()
			.previous(sourceBundle)
			.execute();

		assertEquals(1, resp.getEntry().size());
		assertEquals("http://localhost:" + ourPort + "/fhir/prev", ourRequestUri);

		/*
		 * Try with "prev" instead of "previous"
		 */

		sourceBundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		sourceBundle.getLinkOrCreate("prev").setUrl("http://localhost:" + ourPort + "/fhir/prev");

		resp = client
			.loadPage()
			.previous(sourceBundle)
			.execute();

		assertEquals(1, resp.getEntry().size());
		assertEquals("http://localhost:" + ourPort + "/fhir/prev", ourRequestUri);
	}

	@Test
	public void testReadByUri() throws Exception {
		Patient patient = new Patient();
		patient.addName().addFamily("FAM");
		final String respString = ourCtx.newXmlParser().encodeResourceToString(patient);

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Patient response;

		response = (Patient) client.read(new UriDt("http://localhost:" + ourPort + "/fhir/Patient/123"));
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/123", ourRequestUri);
		assertEquals("FAM", response.getName().get(0).getFamily().get(0).getValue());
	}

	@Test
	public void testReadFluentByUri() throws Exception {
		Patient patient = new Patient();
		patient.addName().addFamily("FAM");
		final String respString = ourCtx.newXmlParser().encodeResourceToString(patient);

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Patient response;

		response = (Patient) client.read().resource(Patient.class).withUrl(new IdDt("http://localhost:" + ourPort + "/AAA/Patient/123")).execute();
		assertEquals("http://localhost:" + ourPort + "/AAA/Patient/123", ourRequestUri);
		assertEquals("FAM", response.getName().get(0).getFamily().get(0).getValue());
	}

	@Test
	public void testReadUpdatedHeaderDoesntOverwriteResourceValue() throws Exception {

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

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = input;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		ca.uhn.fhir.model.dstu2.resource.Bundle response;

		response = client
			.search()
			.forResource(Patient.class)
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();

		assertEquals("2015-06-22T15:48:57.554-04:00", ResourceMetadataKeyEnum.UPDATED.get(response).getValueAsString());
	}

	@Test
	public void testReadWithElementsParam() throws Exception {
		String msg = "{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}";

		ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		IBaseResource response = client.read()
			.resource("Patient")
			.withId("123")
			.elementsSubset("name", "identifier")
			.execute();

		assertThat(ourRequestUri, either(equalTo("http://localhost:" + ourPort + "/fhir/Patient/123?_elements=name%2Cidentifier"))
			.or(equalTo("http://localhost:" + ourPort + "/fhir/Patient/123?_elements=identifier%2Cname")));
		assertEquals(Patient.class, response.getClass());
	}

	@Test
	public void testReadWithSummaryInvalid() throws Exception {
		String msg = "<>>>><<<<>";

		ourResponseContentType = Constants.CT_HTML + "; charset=UTF-8";
		ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		try {
			client.read()
				.resource(Patient.class)
				.withId("123")
				.summaryMode(SummaryEnum.TEXT)
				.execute();
			fail();
		} catch (InvalidResponseException e) {
			assertThat(e.getMessage(), containsString("String does not appear to be valid"));
		}

	}

	@Test
	public void testReadWithSummaryParamHtml() throws Exception {
		String msg = "<div>HELP IM A DIV</div>";

		ourResponseContentType = Constants.CT_HTML + "; charset=UTF-8";
		ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Patient response = client.read()
			.resource(Patient.class)
			.withId("123")
			.summaryMode(SummaryEnum.TEXT)
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/123?_summary=text", ourRequestUri);
		assertEquals(Patient.class, response.getClass());
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">HELP IM A DIV</div>", response.getText().getDiv().getValueAsString());
	}

	@Test
	public void testSearchByString() throws Exception {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient?name=james", ourRequestUri);
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());
	}

	@Test
	public void testSearchByUrl() throws Exception {
		final String msg = getPatientFeedWithOneResult();

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		ca.uhn.fhir.model.dstu2.resource.Bundle response = client.search()
			.byUrl("http://localhost:" + ourPort + "/AAA?name=http://foo|bar")
			.encodedJson()
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();

		assertEquals("http://localhost:" + ourPort + "/AAA?name=http%3A//foo%7Cbar&_format=json", ourRequestUri);
		assertNotNull(response);

		response = client.search()
			.byUrl("Patient?name=http://foo|bar")
			.encodedJson()
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient?name=http%3A//foo%7Cbar&_format=json", ourRequestUri);
		assertNotNull(response);

		response = client.search()
			.byUrl("/Patient?name=http://foo|bar")
			.encodedJson()
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient?name=http%3A//foo%7Cbar&_format=json", ourRequestUri);
		assertNotNull(response);

		response = client.search()
			.byUrl("Patient")
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient", ourRequestUri);
		assertNotNull(response);

		response = client.search()
			.byUrl("Patient?")
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient", ourRequestUri);
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
		String msg = IOUtils.toString(GenericOkHttpClientDstu2Test.class.getResourceAsStream("/bundle_orion.xml"));

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

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
	public void testSearchWithElementsParam() throws Exception {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.elementsSubset("name", "identifier")
			.returnBundle(Bundle.class)
			.execute();

		assertThat(ourRequestUri, either(equalTo("http://localhost:" + ourPort + "/fhir/Patient?name=james&_elements=name%2Cidentifier"))
			.or(equalTo("http://localhost:" + ourPort + "/fhir/Patient?name=james&_elements=identifier%2Cname")));
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());
	}

	@Test
	public void testSearchByPost() throws Exception {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.elementsSubset("name", "identifier")
			.usingStyle(SearchStyleEnum.POST)
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/_search?_elements=identifier%2Cname", ourRequestUri);

		// assertThat(ourRequestUri,
		// either(equalTo("http://localhost:" + ourPort + "/fhir/Patient?name=james&_elements=name%2Cidentifier")).or(equalTo("http://localhost:" + ourPort +
		// "/fhir/Patient?name=james&_elements=identifier%2Cname")));

		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

		assertEquals("name=james", ourRequestBodyString);

		assertEquals("application/x-www-form-urlencoded", ourRequestContentType.replace(";char", "; char").toLowerCase());
		assertEquals(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_LEGACY, ourRequestFirstHeaders.get("Accept").getValue());
		assertThat(ourRequestFirstHeaders.get("User-Agent").getValue(), not(emptyString()));
	}

	@Test
	public void testSearchByPostUseJson() throws Exception {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.elementsSubset("name", "identifier")
			.usingStyle(SearchStyleEnum.POST)
			.encodedJson()
			.returnBundle(Bundle.class)
			.execute();

		assertThat(ourRequestUri, containsString("http://localhost:" + ourPort + "/fhir/Patient/_search?"));
		assertThat(ourRequestUri, containsString("_elements=identifier%2Cname"));
		assertThat(ourRequestUri, not(containsString("_format=json")));

		// assertThat(ourRequestUri,
		// either(equalTo("http://localhost:" + ourPort + "/fhir/Patient?name=james&_elements=name%2Cidentifier")).or(equalTo("http://localhost:" + ourPort +
		// "/fhir/Patient?name=james&_elements=identifier%2Cname")));

		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

		assertEquals("name=james", ourRequestBodyString);

		assertEquals("application/x-www-form-urlencoded", ourRequestContentType);
		assertEquals(Constants.CT_FHIR_JSON, ourRequestFirstHeaders.get("Accept").getValue());
	}

	@Test
	public void testSearchWithLastUpdated() throws Exception {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.lastUpdated(new DateRangeParam("2011-01-01", "2012-01-01"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient?name=james&_lastUpdated=ge2011-01-01&_lastUpdated=le2012-01-01", ourRequestUri);
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());
	}

	@Test
	public void testSearchWithProfileAndSecurity() throws Exception {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.withProfile("http://foo1")
			.withProfile("http://foo2")
			.withSecurity("system1", "code1")
			.withSecurity("system2", "code2")
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient?_security=system1%7Ccode1&_security=system2%7Ccode2&_profile=http%3A%2F%2Ffoo1&_profile=http%3A%2F%2Ffoo2", ourRequestUri);
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithReverseInclude() throws Exception {
		String msg = getPatientFeedWithOneResult();

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Bundle response = client.search()
			.forResource(Patient.class)
			.encodedJson()
			.revInclude(new Include("Provenance:target"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient?_revinclude=Provenance%3Atarget&_format=json", ourRequestUri);
	}

	@Test
	public void testSearchWithSummaryParam() throws Exception {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.summaryMode(SummaryEnum.FALSE)
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient?name=james&_summary=false", ourRequestUri);
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());
	}

	@Test
	public void testTransactionWithString() throws Exception {
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

		ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		ourResponseBody = reqString;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		String response = client.transaction()
			.withBundle(reqString)
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/", ourRequestUri);
		assertThat(response, containsString("\"Bundle\""));
		assertContentTypeEquals("application/json+fhir; charset=UTF-8");

		response = client.transaction()
			.withBundle(reqString)
			.encodedXml()
			.execute();

		assertEquals("http://localhost:" + ourPort + "/fhir/", ourRequestUri);
		assertContentTypeEquals("application/xml+fhir; charset=UTF-8");
	}

	@Test
	public void testTransactionWithTransactionResource() throws Exception {
		ca.uhn.fhir.model.dstu2.resource.Bundle resp = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		resp.addEntry().getResponse().setLocation("Patient/1/_history/1");
		resp.addEntry().getResponse().setLocation("Patient/2/_history/2");
		String respString = ourCtx.newJsonParser().encodeResourceToString(resp);

		ourResponseContentType = Constants.CT_FHIR_JSON + "; charset=UTF-8";
		ourResponseBody = respString;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

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

		assertEquals("http://localhost:" + ourPort + "/fhir", ourRequestUri);
		assertEquals(2, response.getEntry().size());

		assertEquals("Patient/1/_history/1", response.getEntry().get(0).getResponse().getLocation());
		assertEquals("Patient/2/_history/2", response.getEntry().get(1).getResponse().getLocation());
	}

	@Test
	public void testUpdateConditional() throws Exception {
		ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Patient p = new Patient();
		p.addName().addFamily("FOOFAMILY");

		client.update().resource(p).conditionalByUrl("Patient?name=foo").encodedXml().execute();
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertContentTypeEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(ourRequestBodyString, containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("PUT", ourRequestMethod);
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient?name=foo", ourRequestUri);

		client.update().resource(p).conditionalByUrl("Patient?name=http://foo|bar").encodedXml().execute();
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertContentTypeEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(ourRequestBodyString, containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("PUT", ourRequestMethod);
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient?name=http%3A//foo%7Cbar", ourRequestUri);

		client.update().resource(ourCtx.newXmlParser().encodeResourceToString(p)).conditionalByUrl("Patient?name=foo").encodedXml().execute();
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertContentTypeEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(ourRequestBodyString, containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("PUT", ourRequestMethod);
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient?name=foo", ourRequestUri);

		client.update().resource(p).conditional().where(Patient.NAME.matches().value("foo")).and(Patient.ADDRESS.matches().value("AAA|BBB")).encodedXml().execute();
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertContentTypeEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(ourRequestBodyString, containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("PUT", ourRequestMethod);
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient?name=foo&address=AAA%5C%7CBBB", ourRequestUri);

		client.update().resource(ourCtx.newXmlParser().encodeResourceToString(p)).conditional().where(Patient.NAME.matches().value("foo")).and(Patient.ADDRESS.matches().value("AAA|BBB")).encodedXml().execute();
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertContentTypeEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(ourRequestBodyString, containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("PUT", ourRequestMethod);
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient?name=foo&address=AAA%5C%7CBBB", ourRequestUri);
	}

	private String getActualContentTypeHeader() {
		return ourRequestFirstHeaders.get(Constants.HEADER_CONTENT_TYPE).getValue().replace(";char", "; char");
	}

	@Test
	public void testUpdateNonFluent() throws Exception {
		ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");
		client.setEncoding(EncodingEnum.XML);

		Patient p = new Patient();
		p.addName().addFamily("FOOFAMILY");

		client.update(new IdDt("Patient/123"), p);
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());

		String expectedContentTypeHeader = EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8;
		assertThat(getActualContentTypeHeader(), equalToIgnoringCase(expectedContentTypeHeader));
		assertThat(ourRequestBodyString, containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/123?_format=xml", ourRequestUri);
		assertEquals("PUT", ourRequestMethod);

		client.update("123", p);
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertThat(getActualContentTypeHeader(), equalToIgnoringCase(expectedContentTypeHeader));
		assertThat(ourRequestBodyString, containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/123?_format=xml", ourRequestUri);
		assertEquals("PUT", ourRequestMethod);
	}

	@Test
	public void testUpdatePrefer() throws Exception {
		ourResponseStatus = Constants.STATUS_HTTP_204_NO_CONTENT;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Patient p = new Patient();
		p.setId(new IdDt("1"));
		p.addName().addFamily("FOOFAMILY");

		client.update().resource(p).prefer(PreferReturnEnum.MINIMAL).execute();
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_PREFER).size());
		assertEquals(Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_MINIMAL, ourRequestHeaders.get(Constants.HEADER_PREFER).get(0).getValue());

		client.update().resource(p).prefer(PreferReturnEnum.REPRESENTATION).execute();
		assertEquals(1, ourRequestHeaders.get(Constants.HEADER_PREFER).size());
		assertEquals(Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_REPRESENTATION, ourRequestHeaders.get(Constants.HEADER_PREFER).get(0).getValue());
	}

	@Test
	public void testUpdateReturningResourceBody() throws Exception {
		Patient p = new Patient();
		p.setId("123");
		final String formatted = ourCtx.newXmlParser().encodeResourceToString(p);

		ourResponseStatus = Constants.STATUS_HTTP_200_OK;
		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = formatted;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		p = new Patient();
		p.setId(new IdDt("1"));
		p.addName().addFamily("FOOFAMILY");

		MethodOutcome output = client.update().resource(p).execute();
		assertNotNull(output.getResource());
		assertEquals("Patient/123", output.getResource().getIdElement().toUnqualifiedVersionless().getValue());
	}

	@Test
	public void testValidateFluent() throws Exception {
		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDiagnostics("FOOBAR");
		final String msg = ourCtx.newXmlParser().encodeResourceToString(oo);

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");

		Patient p = new Patient();
		p.addName().addGiven("GIVEN");

		MethodOutcome response;

		response = client.validate().resource(p).encodedXml().execute();
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/$validate", ourRequestUri);
		assertEquals("POST", ourRequestMethod);
		assertEquals(
			"<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"resource\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><name><given value=\"GIVEN\"/></name></Patient></resource></parameter></Parameters>",
			ourRequestBodyString);
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", toOo(response.getOperationOutcome()).getIssueFirstRep().getDiagnosticsElement().getValue());

		response = client.validate().resource(ourCtx.newXmlParser().encodeResourceToString(p)).execute();
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/$validate", ourRequestUri);
		assertEquals("POST", ourRequestMethod);
		assertEquals(
			"<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"resource\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><name><given value=\"GIVEN\"/></name></Patient></resource></parameter></Parameters>",
			ourRequestBodyString);
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", toOo(response.getOperationOutcome()).getIssueFirstRep().getDiagnosticsElement().getValue());

		response = client.validate().resource(ourCtx.newJsonParser().encodeResourceToString(p)).execute();
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/$validate", ourRequestUri);
		assertEquals("POST", ourRequestMethod);
		assertEquals("{\"resourceType\":\"Parameters\",\"parameter\":[{\"name\":\"resource\",\"resource\":{\"resourceType\":\"Patient\",\"name\":[{\"given\":[\"GIVEN\"]}]}}]}", ourRequestBodyString);
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", toOo(response.getOperationOutcome()).getIssueFirstRep().getDiagnosticsElement().getValue());

		response = client.validate().resource(ourCtx.newJsonParser().encodeResourceToString(p)).prettyPrint().execute();
		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/$validate?_pretty=true", ourRequestUri);
		assertEquals("POST", ourRequestMethod);
		assertThat(ourRequestBodyString, containsString("\"resourceType\": \"Parameters\",\n"));
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", toOo(response.getOperationOutcome()).getIssueFirstRep().getDiagnosticsElement().getValue());
	}

	@Test
	public void testValidateNonFluent() throws Exception {
		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDiagnostics("FOOBAR");
		final String msg = ourCtx.newXmlParser().encodeResourceToString(oo);

		ourResponseContentType = Constants.CT_FHIR_XML + "; charset=UTF-8";
		ourResponseBody = msg;

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/fhir");
		client.setEncoding(EncodingEnum.XML);

		Patient p = new Patient();
		p.addName().addGiven("GIVEN");

		MethodOutcome response;

		response = client.validate(p);

		assertEquals("http://localhost:" + ourPort + "/fhir/Patient/$validate?_format=xml", ourRequestUri);
		assertEquals("POST", ourRequestMethod);
		assertEquals(
			"<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"resource\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><name><given value=\"GIVEN\"/></name></Patient></resource></parameter></Parameters>",
			ourRequestBodyString);
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", toOo(response.getOperationOutcome()).getIssueFirstRep().getDiagnosticsElement().getValue());
	}

	private OperationOutcome toOo(IBaseOperationOutcome theOperationOutcome) {
		return (OperationOutcome) theOperationOutcome;
	}

	@BeforeEach
	public void beforeReset() {
		ourRequestUri = null;
		ourRequestUriAll = Lists.newArrayList();
		ourResponseStatus = 200;
		ourResponseBody = null;
		ourResponseBodies = null;
		ourResponseCount = 0;

		ourResponseContentType = null;
		ourRequestContentType = null;
		ourRequestBodyBytes = null;
		ourRequestBodyString = null;
		ourRequestHeaders = null;
		ourRequestFirstHeaders = null;
		ourRequestMethod = null;
		ourRequestHeadersAll = Lists.newArrayList();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourCtx = FhirContext.forDstu2();

		ourServer = new Server(0);
		ourServer.setHandler(new AbstractHandler() {

			@Override
			public void handle(String theArg0, Request theRequest, HttpServletRequest theServletRequest, HttpServletResponse theResp) throws IOException, ServletException {
				theRequest.setHandled(true);
				ourRequestUri = "http:" + theRequest.getHttpURI().toString();
				ourRequestUriAll.add(ourRequestUri);
				ourRequestMethod = theRequest.getMethod();
				ourRequestContentType = theServletRequest.getContentType();
				ourRequestBodyBytes = IOUtils.toByteArray(theServletRequest.getInputStream());
				ourRequestBodyString = new String(ourRequestBodyBytes, Charsets.UTF_8);

				ourRequestHeaders = ArrayListMultimap.create();
				ourRequestHeadersAll.add(ourRequestHeaders);
				ourRequestFirstHeaders = Maps.newHashMap();

				for (Enumeration<String> headerNameEnum = theRequest.getHeaderNames(); headerNameEnum.hasMoreElements(); ) {
					String nextName = headerNameEnum.nextElement();
					for (Enumeration<String> headerValueEnum = theRequest.getHeaders(nextName); headerValueEnum.hasMoreElements(); ) {
						String nextValue = headerValueEnum.nextElement();
						if (ourRequestFirstHeaders.containsKey(nextName) == false) {
							ourRequestFirstHeaders.put(nextName, new Header(nextName, nextValue));
						}
						ourRequestHeaders.put(nextName, new Header(nextName, nextValue));
					}
				}

				theResp.setStatus(ourResponseStatus);

				if (ourResponseBody != null) {
					theResp.setContentType(ourResponseContentType);
					theResp.getWriter().write(ourResponseBody);
				} else if (ourResponseBodies != null) {
					theResp.setContentType(ourResponseContentType);
					theResp.getWriter().write(ourResponseBodies[ourResponseCount]);
				}

				ourResponseCount++;
			}
		});

		JettyUtil.startServer(ourServer);
		ourPort = JettyUtil.getPortForStartedServer(ourServer);
	}

	@AfterAll
	public static void afterClass() throws Exception {
		JettyUtil.closeServer(ourServer);
	}
}
