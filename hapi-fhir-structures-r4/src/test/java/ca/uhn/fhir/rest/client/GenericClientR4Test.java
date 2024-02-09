package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.CustomTypeR4Test;
import ca.uhn.fhir.parser.CustomTypeR4Test.MyCustomPatient;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.DeleteCascadeModeEnum;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PagingHttpMethodEnum;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.api.SearchStyleEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.client.exceptions.NonFhirResponseException;
import ca.uhn.fhir.rest.client.impl.GenericClient;
import ca.uhn.fhir.rest.client.interceptor.CookieInterceptor;
import ca.uhn.fhir.rest.client.interceptor.UserInfoInterceptor;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import com.helger.commons.io.stream.StringInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hamcrest.Matchers;
import org.hamcrest.core.StringContains;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.EpisodeOfCare;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.MessageHeader;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.assertj.core.api.Assertions.fail;

import static org.mockito.Mockito.when;

public class GenericClientR4Test extends BaseGenericClientR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GenericClientR4Test.class);

	@Test
	public void testAcceptHeaderCustom() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = prepareClientForSearchResponse();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		// Custom accept value

		client.setEncoding(EncodingEnum.XML);
		client.search()
			.forResource("Device")
			.returnBundle(Bundle.class)
			.accept("application/json")
			.execute();
		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Device");
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_ACCEPT).getValue()).isEqualTo("application/json");
		idx++;

		// Empty accept value

		client.setEncoding(EncodingEnum.XML);
		client.search()
			.forResource("Device")
			.returnBundle(Bundle.class)
			.accept("")
			.execute();
		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Device?_format=xml");
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_ACCEPT).getValue()).isEqualTo(Constants.HEADER_ACCEPT_VALUE_XML_NON_LEGACY);
		idx++;

		// Null accept value

		client.setEncoding(EncodingEnum.XML);
		client.search()
			.forResource("Device")
			.returnBundle(Bundle.class)
			.accept(null)
			.execute();
		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Device?_format=xml");
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_ACCEPT).getValue()).isEqualTo(Constants.HEADER_ACCEPT_VALUE_XML_NON_LEGACY);
		idx++;
	}

	@Test
	public void testBinaryCreateWithFhirContentType() throws Exception {
		IParser p = ourCtx.newXmlParser();

		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");

		final String respString = p.encodeResourceToString(conf);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		Binary bin = new Binary();
		bin.setContent(ourCtx.newJsonParser().encodeResourceToString(pt).getBytes(StandardCharsets.UTF_8));
		bin.setContentType(Constants.CT_FHIR_JSON);
		client.create().resource(bin).execute();

		ourLog.info(Arrays.asList(capt.getAllValues().get(0).getAllHeaders()).toString());

		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Binary");
		validateUserAgent(capt);

		assertThat(capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", "")).isEqualTo("application/fhir+json;charset=utf-8");
		assertThat(capt.getAllValues().get(0).getHeaders("Accept")[0].getValue()).isEqualTo(Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY);
		Binary output = ourCtx.newJsonParser().parseResource(Binary.class, extractBodyAsString(capt));
		assertThat(output.getContentType()).isEqualTo(Constants.CT_FHIR_JSON);

		Patient outputPt = (Patient) ourCtx.newJsonParser().parseResource(new String(output.getContent(), StandardCharsets.UTF_8));
		assertThat(outputPt.getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">A PATIENT</div>");
	}

	@Test
	public void testBinaryCreateWithNoContentType() throws Exception {
		IParser p = ourCtx.newXmlParser();

		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");

		final String respString = p.encodeResourceToString(conf);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Binary bin = new Binary();
		bin.setContent(new byte[]{0, 1, 2, 3, 4});
		client.create().resource(bin).execute();

		ourLog.info(Arrays.asList(capt.getAllValues().get(0).getAllHeaders()).toString());

		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Binary");
		validateUserAgent(capt);

		assertThat(capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", "")).isEqualTo("application/fhir+json;charset=utf-8");
		assertThat(capt.getAllValues().get(0).getHeaders("Accept")[0].getValue()).isEqualTo(Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY);
		assertThat(ourCtx.newJsonParser().parseResource(Binary.class, extractBodyAsString(capt)).getContent()).containsExactly(new byte[]{0, 1, 2, 3, 4});

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testClientFailures() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenThrow(IllegalStateException.class, RuntimeException.class, IOException.class);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail("");		} catch (FhirClientConnectionException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1360) + "java.lang.IllegalStateException");
		}

		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail("");		} catch (RuntimeException e) {
			assertThat(e.toString()).isEqualTo("java.lang.RuntimeException");
		}

		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail("");		} catch (FhirClientConnectionException e) {
			assertThat(e.getMessage()).contains("java.io.IOException");
		}
	}

	@Test
	public void testCookieInterceptor() throws Exception {
		final String respString = CustomTypeR4Test.createBundle(CustomTypeR4Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		client.registerInterceptor(new CookieInterceptor("foo=bar"));

		Bundle resp = client
			.history()
			.onType(Patient.class)
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getAllValues().get(0).getFirstHeader("Cookie").getValue()).isEqualTo("foo=bar");
	}

	@Test
	public void testCreateWithPreferRepresentationServerReturnsOO() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final OperationOutcome resp0 = new OperationOutcome();
		resp0.getText().setDivAsString("OK!");

		final Patient resp1 = new Patient();
		resp1.getText().setDivAsString("FINAL VALUE");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3")};
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				if (myAnswerCount++ == 0) {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp0)), StandardCharsets.UTF_8);
				} else {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), StandardCharsets.UTF_8);
				}
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.create().resource(pt).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertThat(myAnswerCount).isEqualTo(2);
		assertThat(outcome.getOperationOutcome()).isNotNull();
		assertThat(outcome.getResource()).isNotNull();

		assertThat(((OperationOutcome) outcome.getOperationOutcome()).getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">OK!</div>");
		assertThat(((Patient) outcome.getResource()).getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>");

		assertThat(capt.getAllValues()).hasSize(myAnswerCount);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient");
		assertThat(capt.getAllValues().get(0).getFirstHeader("content-type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_FHIR_JSON_NEW);

		assertThat(capt.getAllValues().get(1).getURI().toASCIIString()).isEqualTo("http://foo.com/base/Patient/222/_history/3");
	}

	@Test
	public void testCreateWithPreferRepresentationServerReturnsResource() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final Patient resp1 = new Patient();
		resp1.getText().setDivAsString("FINAL VALUE");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3")};
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				myAnswerCount++;
				return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.create().resource(pt).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertThat(myAnswerCount).isEqualTo(1);
		assertThat(outcome.getOperationOutcome()).isNull();
		assertThat(outcome.getResource()).isNotNull();

		assertThat(((Patient) outcome.getResource()).getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>");

		assertThat(capt.getAllValues()).hasSize(myAnswerCount);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient");
	}

	@Test
	public void testDeleteCascade() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		OperationOutcome oo = new OperationOutcome();
		oo.getText().setDivAsString("FINAL VALUE");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3")};
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				myAnswerCount++;
				return new ReaderInputStream(new StringReader(p.encodeResourceToString(oo)), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		MethodOutcome outcome;

		// Regular delete
		outcome = client
			.delete()
			.resourceById(new IdType("Patient/222"))
			.execute();
		assertThat(outcome).isNotNull();
		assertThat(capt.getAllValues()).hasSize(1);
		assertThat(capt.getAllValues().get(myAnswerCount - 1).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/222");
		assertThat(capt.getAllValues().get(myAnswerCount - 1).getMethod()).isEqualTo("DELETE");

		// NONE Cascading delete
		outcome = client
			.delete()
			.resourceById(new IdType("Patient/222"))
			.cascade(DeleteCascadeModeEnum.NONE)
			.execute();
		assertThat(outcome).isNotNull();
		assertThat(capt.getAllValues()).hasSize(2);
		assertThat(capt.getAllValues().get(myAnswerCount - 1).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/222");
		assertThat(capt.getAllValues().get(myAnswerCount - 1).getMethod()).isEqualTo("DELETE");

		// DELETE Cascading delete
		outcome = client
			.delete()
			.resourceById(new IdType("Patient/222"))
			.cascade(DeleteCascadeModeEnum.DELETE)
			.execute();
		assertThat(outcome).isNotNull();
		assertThat(capt.getAllValues()).hasSize(myAnswerCount);
		assertThat(capt.getAllValues().get(myAnswerCount - 1).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/222?" + Constants.PARAMETER_CASCADE_DELETE + "=" + Constants.CASCADE_DELETE);
		assertThat(capt.getAllValues().get(myAnswerCount - 1).getMethod()).isEqualTo("DELETE");

		// DELETE Cascading delete on search URL
		outcome = client
			.delete()
			.resourceConditionalByUrl("Patient?identifier=sys|val")
			.cascade(DeleteCascadeModeEnum.DELETE)
			.execute();
		assertThat(outcome).isNotNull();
		assertThat(capt.getAllValues()).hasSize(myAnswerCount);
		assertThat(capt.getAllValues().get(myAnswerCount - 1).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient?identifier=sys%7Cval&" + Constants.PARAMETER_CASCADE_DELETE + "=" + Constants.CASCADE_DELETE);
		assertThat(capt.getAllValues().get(myAnswerCount - 1).getMethod()).isEqualTo("DELETE");
	}

	@Test
	public void testExplicitCustomTypeHistoryType() throws Exception {
		final String respString = CustomTypeR4Test.createBundle(CustomTypeR4Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle resp = client
			.history()
			.onType(CustomTypeR4Test.MyCustomPatient.class)
			.returnBundle(Bundle.class)
			.execute();

		assertThat(resp.getEntry()).hasSize(1);
		assertThat(resp.getEntry().get(0).getResource().getClass()).isEqualTo(CustomTypeR4Test.MyCustomPatient.class);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/_history");
	}

	@Test
	public void testExplicitCustomTypeLoadPage() throws Exception {
		final String respString = CustomTypeR4Test.createBundle(CustomTypeR4Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		Bundle bundle = new Bundle();
		bundle.addLink().setRelation("next").setUrl("http://foo/next");

		Bundle resp = client
			.loadPage()
			.next(bundle)
			.preferResponseType(MyCustomPatient.class)
			.execute();

		assertThat(resp.getEntry()).hasSize(1);
		assertThat(resp.getEntry().get(0).getResource().getClass()).isEqualTo(CustomTypeR4Test.MyCustomPatient.class);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://foo/next");

		resp = client
			.loadPage()
			.next(bundle)
			.preferResponseTypes(toTypeList(MyCustomPatient.class))
			.execute();

		assertThat(resp.getEntry()).hasSize(1);
		assertThat(resp.getEntry().get(0).getResource().getClass()).isEqualTo(CustomTypeR4Test.MyCustomPatient.class);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://foo/next");
	}

	@Test
	public void testExplicitCustomTypeOperation() throws Exception {

		Parameters param = new Parameters();
		Patient patient = new Patient();
		patient.addName().setFamily("FOO");
		param.addParameter().setName("foo").setResource(patient);
		final String respString = ourCtx.newXmlParser().encodeResourceToString(param);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Parameters resp = client
			.operation()
			.onServer()
			.named("foo")
			.withNoParameters(Parameters.class)
			.preferResponseType(MyCustomPatient.class)
			.execute();

		assertThat(resp.getParameter()).hasSize(1);
		assertThat(resp.getParameter().get(0).getResource().getClass()).isEqualTo(CustomTypeR4Test.MyCustomPatient.class);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/$foo");

		resp = client
			.operation()
			.onType(MyCustomPatient.class)
			.named("foo")
			.withNoParameters(Parameters.class)
			.execute();

		assertThat(resp.getParameter()).hasSize(1);
		assertThat(resp.getParameter().get(0).getResource().getClass()).isEqualTo(CustomTypeR4Test.MyCustomPatient.class);
		assertThat(capt.getAllValues().get(1).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/$foo");
	}

	@Test
	public void testExplicitCustomTypeSearch() throws Exception {
		final String respString = CustomTypeR4Test.createBundle(CustomTypeR4Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle resp = client
			.search()
			.forResource(CustomTypeR4Test.MyCustomPatient.class)
			.returnBundle(Bundle.class)
			.execute();

		assertThat(resp.getEntry()).hasSize(1);
		assertThat(resp.getEntry().get(0).getResource().getClass()).isEqualTo(CustomTypeR4Test.MyCustomPatient.class);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient");
	}

	@Test
	public void testFetchCapabilityStatementReceiveCapabilityStatement() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final CapabilityStatement conf = new CapabilityStatement();
		conf.setCopyright("COPY");

		final Patient patient = new Patient();
		patient.addName().setFamily("FAM");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			private int myCount = 0;

			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				final String respString;
				if (myCount == 1 || myCount == 2) {
					ourLog.info("Encoding patient");
					respString = p.encodeResourceToString(patient);
				} else {
					ourLog.info("Encoding conformance");
					respString = p.encodeResourceToString(conf);
				}
				myCount++;
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		IGenericClient client = ourCtx.newRestfulGenericClient("http://testForceConformanceCapabilityStatement.com/fhir");

		client.read().resource("Patient").withId("1").execute();
		assertThat(capt.getAllValues()).hasSize(2);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://testForceConformanceCapabilityStatement.com/fhir/metadata");
		assertThat(capt.getAllValues().get(1).getURI().toASCIIString()).isEqualTo("http://testForceConformanceCapabilityStatement.com/fhir/Patient/1");

		client.read().resource("Patient").withId("1").execute();
		assertThat(capt.getAllValues()).hasSize(3);
		assertThat(capt.getAllValues().get(2).getURI().toASCIIString()).isEqualTo("http://testForceConformanceCapabilityStatement.com/fhir/Patient/1");

		client.forceConformanceCheck();
		assertThat(capt.getAllValues()).hasSize(4);
		assertThat(capt.getAllValues().get(3).getURI().toASCIIString()).isEqualTo("http://testForceConformanceCapabilityStatement.com/fhir/metadata");
	}

	@Test
	public void testForceConformance() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final CapabilityStatement conf = new CapabilityStatement();
		conf.setCopyright("COPY");

		final Patient patient = new Patient();
		patient.addName().setFamily("FAM");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {

			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				final String respString;
				if (myAnswerCount >= 1) {
					ourLog.info("Encoding patient");
					respString = p.encodeResourceToString(patient);
				} else {
					ourLog.info("Encoding conformance");
					respString = p.encodeResourceToString(conf);
				}
				myAnswerCount++;
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		IGenericClient client = ourCtx.newRestfulGenericClient("http://testForceConformance.com/fhir");

		client.read().resource("Patient").withId("1").execute();
		assertThat(capt.getAllValues()).hasSize(2);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://testForceConformance.com/fhir/metadata");
		assertThat(capt.getAllValues().get(1).getURI().toASCIIString()).isEqualTo("http://testForceConformance.com/fhir/Patient/1");

		client.read().resource("Patient").withId("1").execute();
		assertThat(capt.getAllValues()).hasSize(3);
		assertThat(capt.getAllValues().get(2).getURI().toASCIIString()).isEqualTo("http://testForceConformance.com/fhir/Patient/1");

		myAnswerCount = 0;
		client.forceConformanceCheck();
		assertThat(capt.getAllValues()).hasSize(4);
		assertThat(capt.getAllValues().get(3).getURI().toASCIIString()).isEqualTo("http://testForceConformance.com/fhir/metadata");
	}

	@Test
	public void testHistoryTypeWithAt() throws Exception {

		final Bundle resp1 = new Bundle();
		resp1.setTotal(0);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[0];
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(t -> {
			IParser p = ourCtx.newXmlParser();
			return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), StandardCharsets.UTF_8);
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle outcome = client
			.history()
			.onServer().returnBundle(Bundle.class)
			.at(new DateRangeParam().setLowerBound("2011").setUpperBound("2018"))
			.execute();

		assertThat(outcome.getTotal()).isEqualTo(0);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/_history?_at=ge2011&_at=le2018");
	}


	@Test
	public void testHistoryOnTypeString() throws Exception {

		final Bundle resp1 = new Bundle();
		resp1.setTotal(0);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[0];
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(t -> {
			IParser p = ourCtx.newXmlParser();
			return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), StandardCharsets.UTF_8);
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle outcome = client
			.history()
			.onType("Patient")
			.returnBundle(Bundle.class)
			.execute();

		assertThat(outcome.getTotal()).isEqualTo(0);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/_history");
	}

	@Test
	public void testHttp499() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 499, "Wacky Message"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public StringInputStream answer(InvocationOnMock theInvocation) {
				return new StringInputStream("HELLO", Charsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail("");		} catch (UnclassifiedServerFailureException e) {
			assertThat(e.toString()).isEqualTo("ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException: HTTP 499 Wacky Message");
			assertThat(e.getResponseBody()).isEqualTo("HELLO");
		}

	}

	@Test
	public void testHttp501() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 501, "Not Implemented"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public StringInputStream answer(InvocationOnMock theInvocation) {
				return new StringInputStream("not implemented", Charsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail("");		} catch (NotImplementedOperationException e) {
			assertThat(e.getMessage()).isEqualTo("HTTP 501 Not Implemented");
		}

	}

	/**
	 * See #150
	 */
	@Test
	public void testNullAndEmptyParamValuesAreIgnored() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = prepareClientForSearchResponse();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client
			.search()
			.forResource(Patient.class)
			.where(Patient.FAMILY.matches().value((String) null))
			.and(Patient.BIRTHDATE.exactly().day((Date) null))
			.and(Patient.GENDER.exactly().code(null))
			.and(Patient.ORGANIZATION.hasId((String) null))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient");
		idx++;

		client
			.search()
			.forResource(Encounter.class)
			.where(Encounter.LENGTH.exactly().number(null).andNoUnits())
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Encounter");
		idx++;

		client
			.search()
			.forResource(Observation.class)
			.where(Observation.VALUE_QUANTITY.exactly().number(null).andUnits(null))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Observation");
		idx++;

	}

	@Test
	public void testOperationInstance() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inputParams = new Parameters();
		inputParams.addParameter().setName("name").setValue(new BooleanType(true));

		Parameters outputParams = new Parameters();
		outputParams.addParameter().setName("name").setValue(new BooleanType(false));

		final String respString = p.encodeResourceToString(outputParams);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Parameters result = client
			.operation()
			.onInstance(new IdType("Patient/123/_history/456"))
			.named("opname")
			.withParameters(inputParams)
			.execute();

		assertThat(result.getParameterFirstRep().getName()).isEqualTo("name");
		assertThat(((IPrimitiveType<?>) result.getParameterFirstRep().getValue()).getValueAsString()).isEqualTo("false");

		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/123/$opname");
		validateUserAgent(capt);

		assertThat(capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", "")).isEqualTo("application/fhir+json;charset=utf-8");
		assertThat(capt.getAllValues().get(0).getHeaders("Accept")[0].getValue()).isEqualTo(Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY);
		Parameters output = ourCtx.newJsonParser().parseResource(Parameters.class, extractBodyAsString(capt));
		assertThat(output.getParameterFirstRep().getName()).isEqualTo("name");
		assertThat(((IPrimitiveType<?>) output.getParameterFirstRep().getValue()).getValueAsString()).isEqualTo("true");
	}

	@Test
	public void testOperationInstanceVersion() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inputParams = new Parameters();
		inputParams.addParameter().setName("name").setValue(new BooleanType(true));

		Parameters outputParams = new Parameters();
		outputParams.addParameter().setName("name").setValue(new BooleanType(false));

		final String respString = p.encodeResourceToString(outputParams);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Parameters result = client
			.operation()
			.onInstanceVersion(new IdType("Patient/123/_history/456"))
			.named("opname")
			.withParameters(inputParams)
			.execute();

		assertThat(result.getParameterFirstRep().getName()).isEqualTo("name");
		assertThat(((IPrimitiveType<?>) result.getParameterFirstRep().getValue()).getValueAsString()).isEqualTo("false");

		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/123/_history/456/$opname");
		validateUserAgent(capt);

		assertThat(capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", "")).isEqualTo("application/fhir+json;charset=utf-8");
		assertThat(capt.getAllValues().get(0).getHeaders("Accept")[0].getValue()).isEqualTo(Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY);
		Parameters output = ourCtx.newJsonParser().parseResource(Parameters.class, extractBodyAsString(capt));
		assertThat(output.getParameterFirstRep().getName()).isEqualTo("name");
		assertThat(((IPrimitiveType<?>) output.getParameterFirstRep().getValue()).getValueAsString()).isEqualTo("true");
	}

	@Test
	public void testOperationServer() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inputParams = new Parameters();
		inputParams.addParameter().setName("name").setValue(new BooleanType(true));

		Parameters outputParams = new Parameters();
		outputParams.addParameter().setName("name").setValue(new BooleanType(false));

		final String respString = p.encodeResourceToString(outputParams);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Parameters result = client
			.operation()
			.onServer()
			.named("opname")
			.withParameters(inputParams)
			.execute();

		assertThat(result.getParameterFirstRep().getName()).isEqualTo("name");
		assertThat(((IPrimitiveType<?>) result.getParameterFirstRep().getValue()).getValueAsString()).isEqualTo("false");

		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/$opname");
		validateUserAgent(capt);

		assertThat(capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", "")).isEqualTo("application/fhir+json;charset=utf-8");
		assertThat(capt.getAllValues().get(0).getHeaders("Accept")[0].getValue()).isEqualTo(Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY);
		Parameters output = ourCtx.newJsonParser().parseResource(Parameters.class, extractBodyAsString(capt));
		assertThat(output.getParameterFirstRep().getName()).isEqualTo("name");
		assertThat(((IPrimitiveType<?>) output.getParameterFirstRep().getValue()).getValueAsString()).isEqualTo("true");
	}

	/**
	 * Invoke an operation that returns HTML
	 * as a response (a HAPI FHIR server could accomplish this by returning
	 * a Binary resource)
	 */
	@Test
	public void testOperationReturningArbitraryBinaryContentTextual() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inputParams = new Parameters();
		inputParams.addParameter().setName("name").setValue(new BooleanType(true));

		final String respString = "<html>VALUE</html>";
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", "text/html"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(t -> new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{
			new BasicHeader("content-type", "text/html")
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		MethodOutcome result = client
			.operation()
			.onServer()
			.named("opname")
			.withParameters(inputParams)
			.returnMethodOutcome()
			.execute();

		assertThat(result.getResource().getClass()).isEqualTo(Binary.class);
		Binary binary = (Binary) result.getResource();
		assertThat(new String(binary.getContent(), Charsets.UTF_8)).isEqualTo(respString);
		assertThat(binary.getContentType()).isEqualTo("text/html");

		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/$opname");
	}

	@Test
	public void testOperationReturningArbitraryBinaryContentTextual_ReturnResourceType() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inputParams = new Parameters();
		inputParams.addParameter().setName("name").setValue(new BooleanType(true));

		final String respString = "<html>VALUE</html>";
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", "text/html"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(t -> new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{
			new BasicHeader("content-type", "text/html")
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Binary binary = client
			.operation()
			.onServer()
			.named("opname")
			.withParameters(inputParams)
			.returnResourceType(Binary.class)
			.execute();

		assertThat(new String(binary.getContent(), Charsets.UTF_8)).isEqualTo(respString);
		assertThat(binary.getContentType()).isEqualTo("text/html");

		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/$opname");
	}


	/**
	 * Invoke an operation that returns HTML
	 * as a response (a HAPI FHIR server could accomplish this by returning
	 * a Binary resource)
	 */
	@Test
	public void testOperationReturningArbitraryBinaryContentNonTextual() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inputParams = new Parameters();
		inputParams.addParameter().setName("name").setValue(new BooleanType(true));

		final byte[] respBytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 100};
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", "application/weird-numbers"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(t -> new ByteArrayInputStream(respBytes));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{
			new BasicHeader("content-Type", "application/weird-numbers")
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		MethodOutcome result = client
			.operation()
			.onServer()
			.named("opname")
			.withParameters(inputParams)
			.returnMethodOutcome()
			.execute();

		assertThat(result.getResource().getClass()).isEqualTo(Binary.class);
		Binary binary = (Binary) result.getResource();
		assertThat(binary.getContentType()).isEqualTo("application/weird-numbers");
		assertThat(binary.getContent()).containsExactly(respBytes);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/$opname");
	}

	@Test
	public void testOperationType() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inputParams = new Parameters();
		inputParams.addParameter().setName("name").setValue(new BooleanType(true));

		Parameters outputParams = new Parameters();
		outputParams.addParameter().setName("name").setValue(new BooleanType(false));

		final String respString = p.encodeResourceToString(outputParams);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Parameters result = client
			.operation()
			.onType(Patient.class)
			.named("opname")
			.withParameters(inputParams)
			.execute();

		assertThat(result.getParameterFirstRep().getName()).isEqualTo("name");
		assertThat(((IPrimitiveType<?>) result.getParameterFirstRep().getValue()).getValueAsString()).isEqualTo("false");

		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/$opname");
		validateUserAgent(capt);

		assertThat(capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", "")).isEqualTo("application/fhir+json;charset=utf-8");
		assertThat(capt.getAllValues().get(0).getHeaders("Accept")[0].getValue()).isEqualTo(Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY);
		Parameters output = ourCtx.newJsonParser().parseResource(Parameters.class, extractBodyAsString(capt));
		assertThat(output.getParameterFirstRep().getName()).isEqualTo("name");
		assertThat(((IPrimitiveType<?>) output.getParameterFirstRep().getValue()).getValueAsString()).isEqualTo("true");
	}

	@Test
	public void testPatchInvalid() {
		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client
				.patch()
				.withBody("AA")
				.withId("Patient/123")
				.execute();
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1386) + "Unable to determine encoding of patch");
		}
	}

	@Test
	public void testPatchJsonByConditionalParam() throws Exception {
		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");
		final String respString = ourCtx.newJsonParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		String patch = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";

		MethodOutcome outcome = client
			.patch()
			.withBody(patch)
			.conditional("Patient").where(Patient.NAME.matches().value("TEST"))
			.and(Patient.FAMILY.matches().value("TEST2"))
			.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient?name=TEST&family=TEST2");
		assertThat(capt.getAllValues().get(0).getRequestLine().getMethod()).isEqualTo("PATCH");
		assertThat(extractBodyAsString(capt)).isEqualTo(patch);
		assertThat(capt.getAllValues().get(idx).getFirstHeader("Content-Type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_JSON_PATCH);
		idx++;

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(oo.getText().getDivAsString()).contains("OK!");
	}

	@Test
	public void testPatchJsonByConditionalParamResourceType() throws Exception {
		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");
		final String respString = ourCtx.newJsonParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		String patch = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";

		MethodOutcome outcome = client
			.patch()
			.withBody(patch)
			.conditional(Patient.class).where(Patient.NAME.matches().value("TEST"))
			.and(Patient.FAMILY.matches().value("TEST2"))
			.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient?name=TEST&family=TEST2");
		assertThat(capt.getAllValues().get(0).getRequestLine().getMethod()).isEqualTo("PATCH");
		assertThat(extractBodyAsString(capt)).isEqualTo(patch);
		assertThat(capt.getAllValues().get(idx).getFirstHeader("Content-Type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_JSON_PATCH);
		idx++;

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(oo.getText().getDivAsString()).contains("OK!");
	}

	@Test
	public void testPatchJsonByConditionalString() throws Exception {
		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");
		final String respString = ourCtx.newJsonParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		String patch = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";

		MethodOutcome outcome = client
			.patch()
			.withBody(patch)
			.conditionalByUrl("Patient?foo=bar")
			.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient?foo=bar");
		assertThat(capt.getAllValues().get(0).getRequestLine().getMethod()).isEqualTo("PATCH");
		assertThat(extractBodyAsString(capt)).isEqualTo(patch);
		assertThat(capt.getAllValues().get(idx).getFirstHeader("Content-Type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_JSON_PATCH);
		idx++;

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(oo.getText().getDivAsString()).contains("OK!");
	}

	@Test
	public void testPatchJsonByIdString() throws Exception {
		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");
		final String respString = ourCtx.newJsonParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		String patch = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";

		MethodOutcome outcome = client
			.patch()
			.withBody(patch)
			.withId("Patient/123")
			.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient/123");
		assertThat(capt.getAllValues().get(0).getRequestLine().getMethod()).isEqualTo("PATCH");
		assertThat(extractBodyAsString(capt)).isEqualTo(patch);
		assertThat(capt.getAllValues().get(idx).getFirstHeader("Content-Type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_JSON_PATCH);
		idx++;

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(oo.getText().getDivAsString()).contains("OK!");
	}

	@Test
	public void testPatchJsonByIdType() throws Exception {
		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");
		final String respString = ourCtx.newJsonParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		String patch = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";

		MethodOutcome outcome = client
			.patch()
			.withBody(patch)
			.withId(new IdType("http://localhost/fhir/Patient/123/_history/234"))
			.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient/123");
		assertThat(capt.getAllValues().get(0).getRequestLine().getMethod()).isEqualTo("PATCH");
		assertThat(extractBodyAsString(capt)).isEqualTo(patch);
		assertThat(capt.getAllValues().get(idx).getFirstHeader("Content-Type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_JSON_PATCH);
		idx++;

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(oo.getText().getDivAsString()).contains("OK!");
	}

	@Test
	public void testPatchXmlByIdString() throws Exception {
		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");
		final String respString = ourCtx.newJsonParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		String patch = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><diff xmlns:fhir=\"http://hl7.org/fhir\"><replace sel=\"fhir:Patient/fhir:active/@value\">false</replace></diff>";

		MethodOutcome outcome = client
			.patch()
			.withBody(patch)
			.withId("Patient/123")
			.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient/123");
		assertThat(capt.getAllValues().get(0).getRequestLine().getMethod()).isEqualTo("PATCH");
		assertThat(extractBodyAsString(capt)).isEqualTo(patch);
		assertThat(capt.getAllValues().get(idx).getFirstHeader("Content-Type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_XML_PATCH);
		idx++;

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(oo.getText().getDivAsString()).contains("OK!");
	}

	@Test
	public void testPutDoesntForceAllIdsJson() throws Exception {
		IParser p = ourCtx.newJsonParser();

		Patient patient = new Patient();
		patient.setId("PATIENT1");
		patient.addName().setFamily("PATIENT1");

		Bundle bundle = new Bundle();
		bundle.setId("BUNDLE1");
		bundle.addEntry().setResource(patient);

		final String encoded = p.encodeResourceToString(bundle);
		assertThat(encoded).isEqualTo("{\"resourceType\":\"Bundle\",\"id\":\"BUNDLE1\",\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"PATIENT1\",\"name\":[{\"family\":\"PATIENT1\"}]}}]}");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON_NEW + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(encoded), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client
			.update()
			.resource(bundle)
			.prefer(PreferReturnEnum.REPRESENTATION)
			.encodedJson()
			.execute();

		HttpPut httpRequest = (HttpPut) capt.getValue();
		assertThat(httpRequest.getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Bundle/BUNDLE1");

		String requestString = IOUtils.toString(httpRequest.getEntity().getContent(), StandardCharsets.UTF_8);
		assertThat(requestString).isEqualTo(encoded);
	}

	@Test
	public void testPutDoesntForceAllIdsXml() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Patient patient = new Patient();
		patient.setId("PATIENT1");
		patient.addName().setFamily("PATIENT1");

		Bundle bundle = new Bundle();
		bundle.setId("BUNDLE1");
		bundle.addEntry().setResource(patient);

		final String encoded = p.encodeResourceToString(bundle);
		assertThat(encoded).isEqualTo("<Bundle xmlns=\"http://hl7.org/fhir\"><id value=\"BUNDLE1\"/><entry><resource><Patient xmlns=\"http://hl7.org/fhir\"><id value=\"PATIENT1\"/><name><family value=\"PATIENT1\"/></name></Patient></resource></entry></Bundle>");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(encoded), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client
			.update()
			.resource(bundle)
			.prefer(PreferReturnEnum.REPRESENTATION)
			.encodedXml()
			.execute();

		HttpPut httpRequest = (HttpPut) capt.getValue();
		assertThat(httpRequest.getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Bundle/BUNDLE1");

		String requestString = IOUtils.toString(httpRequest.getEntity().getContent(), StandardCharsets.UTF_8);
		assertThat(requestString).isEqualTo(encoded);
	}

	@Test
	public void testReadWithUnparseableResponse() throws Exception {
		String msg = "{\"resourceTypeeeee\":\"Patient\"}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.read().resource("Patient").withId("123").elementsSubset("name", "identifier").execute();
			fail("");		} catch (FhirClientConnectionException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1359) + "Failed to parse response from server when performing GET to URL http://example.com/fhir/Patient/123?_elements=identifier%2Cname - ca.uhn.fhir.parser.DataFormatException: " + Msg.code(1838) + "Invalid JSON content detected, missing required element: 'resourceType'");
		}
	}

	@Test
	public void testResponseHasContentTypeMissing() throws Exception {
		IParser p = ourCtx.newXmlParser();
		Patient patient = new Patient();
		patient.addName().setFamily("FAM");
		final String respString = p.encodeResourceToString(patient);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		// when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(null);
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail("");		} catch (NonFhirResponseException e) {
			assertThat(e.getMessage()).isEqualTo("Response contains no Content-Type");
		}

		// Patient resp = client.read().resource(Patient.class).withId("1").execute();
		// assertEquals("FAM", resp.getNameFirstRep().getFamilyAsSingleString());
	}

	@Test
	public void testResponseHasContentTypeNonFhir() throws Exception {
		IParser p = ourCtx.newXmlParser();
		Patient patient = new Patient();
		patient.addName().setFamily("FAM");
		final String respString = p.encodeResourceToString(patient);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", "text/plain"));
		// when(myHttpResponse.getEntity().getContentType()).thenReturn(null);
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail("");		} catch (NonFhirResponseException e) {
			assertThat(e.getMessage()).isEqualTo("Response contains non FHIR Content-Type 'text/plain' : <Patient xmlns=\"http://hl7.org/fhir\"><name><family value=\"FAM\"/></name></Patient>");
		}

		// Patient resp = client.read().resource(Patient.class).withId("1").execute();
		// assertEquals("FAM", resp.getNameFirstRep().getFamilyAsSingleString());
	}

	@Test
	public void testRevIncludeRecursive() throws IOException {
		ArgumentCaptor<HttpUriRequest> capt = prepareClientForSearchResponse();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client.search()
			.forResource(EpisodeOfCare.class)
			.where(EpisodeOfCare.PATIENT.hasId("123"))
			.revInclude(Encounter.INCLUDE_EPISODE_OF_CARE)
			.revInclude(Observation.INCLUDE_ENCOUNTER.asRecursive())
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/EpisodeOfCare?patient=123&_revinclude=Encounter%3Aepisode-of-care&_revinclude%3Aiterate=Observation%3Aencounter");
		idx++;

	}

	@Test
	public void testSearchByUrl() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = prepareClientForSearchResponse();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client.search()
			.forResource("Device")
			.where(Device.URL.matches().value("http://foo.com"))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Device?url=http://foo.com");
		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Device?url=http%3A%2F%2Ffoo.com");
		idx++;

		client.search()
			.forResource("Device")
			.where(Device.URL.matches().value(new StringDt("http://foo.com")))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Device?url=http://foo.com");
		idx++;

	}

	@Test
	public void testSearchWithMap() throws Exception {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		HashMap<String, List<IQueryParameterType>> params = new HashMap<String, List<IQueryParameterType>>();
		params.put("foo", Arrays.asList(new DateParam("2001")));
		Bundle response = client
			.search()
			.forResource(Patient.class)
			.where(params)
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient?foo=2001");
		assertThat(response.getEntry().get(0).getResource().getClass()).isEqualTo(Patient.class);

	}

	/**
	 * See #2426
	 */
	@Test
	public void testSearchWithMap_StringModifier() throws Exception {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		HashMap<String, List<IQueryParameterType>> params = new HashMap<>();
		params.put("foo", Collections.singletonList(new StringParam("Smith", true)));
		Bundle response = client
			.search()
			.forResource(Patient.class)
			.where(params)
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient?foo%3Aexact=Smith");
		assertThat(response.getEntry().get(0).getResource().getClass()).isEqualTo(Patient.class);

	}


	@Test
	public void testSearchWithMultipleTokens() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = prepareClientForSearchResponse();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		Collection<String> values = Arrays.asList("VAL1", "VAL2", "VAL3A,B");

		client.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.exactly().systemAndValues("SYS", values))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?identifier=SYS%7CVAL1%2CSYS%7CVAL2%2CSYS%7CVAL3A%5C%2CB");
		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient?identifier=SYS|VAL1,SYS|VAL2,SYS|VAL3A\\,B");
		idx++;

	}

	@Test
	public void testSearchWithNoExplicitBundleReturnType() throws Exception {

		String msg = ClientR4Test.getPatientFeedWithOneResult(ourCtx);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		// httpResponse = new BasicHttpResponse(statusline, catalog, locale)
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://foo");
		Bundle response = (Bundle) client.search().forResource(Patient.class).execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://foo/Patient");
		Patient patient = (Patient) response.getEntry().get(0).getResource();
		assertThat(patient.getIdentifier().get(0).getValueElement().getValue()).isEqualTo("PRP1660");

	}

	@Test
	public void testSearchWithNullParameters() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = prepareClientForSearchResponse();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		DateTimeDt now = DateTimeDt.withCurrentTime();
		String dateString = now.getValueAsString().substring(0, 10);

		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value((String) null))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient");
		idx++;
	}

	@Test
	public void testSearchWithParameterMap() throws Exception {

		final Bundle resp1 = new Bundle();
		resp1.setTotal(0);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[0];
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(t -> {
			IParser p = ourCtx.newXmlParser();
			return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), StandardCharsets.UTF_8);
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Map<String, List<String>> rawMap = new HashMap<>();
		rawMap.put("param1", Arrays.asList("val1a,val1b", "<html>"));

		Bundle outcome = client
			.search()
			.forResource(Patient.class)
			.whereMap(rawMap)
			.returnBundle(Bundle.class)
			.execute();

		assertThat(outcome.getTotal()).isEqualTo(0);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient?param1=val1a%2Cval1b&param1=%3Chtml%3E");
		assertThat(UrlUtil.unescape(capt.getAllValues().get(0).getURI().toASCIIString())).isEqualTo("http://example.com/fhir/Patient?param1=val1a,val1b&param1=<html>");
	}

	/**
	 * See #371
	 */
	@Test
	public void testSortR4Test() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Bundle b = new Bundle();
		b.setType(BundleType.SEARCHSET);

		final String respString = p.encodeResourceToString(b);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client
			.search()
			.forResource(Patient.class)
			.sort().ascending("address")
			.returnBundle(Bundle.class)
			.execute();
		assertThat(capt.getAllValues().get(idx++).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient?_sort=address");

		client
			.search()
			.forResource(Patient.class)
			.sort().descending("address")
			.returnBundle(Bundle.class)
			.execute();
		assertThat(capt.getAllValues().get(idx++).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient?_sort=-address");

		client
			.search()
			.forResource(Patient.class)
			.sort().descending("address")
			.sort().ascending("name")
			.sort().descending(Patient.BIRTHDATE)
			.returnBundle(Bundle.class)
			.execute();
		assertThat(capt.getAllValues().get(idx++).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient?_sort=-address%2Cname%2C-birthdate");

	}

	@Test
	public void testSortUsingSortSpec() throws Exception {

		final Bundle resp1 = new Bundle();
		resp1.setTotal(0);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[0];
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(t -> {
			IParser p = ourCtx.newXmlParser();
			return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), StandardCharsets.UTF_8);
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		SortSpec sortSpec = new SortSpec();
		sortSpec.setParamName("BDESC");
		sortSpec.setOrder(SortOrderEnum.DESC);
		sortSpec.setChain(new SortSpec());
		sortSpec.getChain().setParamName("CASC");
		sortSpec.getChain().setOrder(SortOrderEnum.ASC);

		Bundle outcome = client
			.search()
			.forResource(Patient.class)
			.returnBundle(Bundle.class)
			.sort().ascending("AASC")
			.sort(sortSpec)
			.sort().defaultOrder("DDEF")
			.execute();

		assertThat(outcome.getTotal()).isEqualTo(0);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient?_sort=AASC%2C-BDESC%2CCASC%2CDDEF");
		assertThat(UrlUtil.unescape(capt.getAllValues().get(0).getURI().toASCIIString())).isEqualTo("http://example.com/fhir/Patient?_sort=AASC,-BDESC,CASC,DDEF");
	}

	@Test
	public void testTransactionWithInvalidBody() {
		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		// Transaction
		try {
			client.transaction().withBundle("FOO");
			fail("");		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1395) + "Unable to determing encoding of request (body does not appear to be valid XML or JSON)");
		}

		// Create
		try {
			client.create().resource("FOO").execute();
			fail("");		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1368) + "Unable to determing encoding of request (body does not appear to be valid XML or JSON)");
		}

		// Update
		try {
			client.update().resource("FOO").execute();
			fail("");		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1368) + "Unable to determing encoding of request (body does not appear to be valid XML or JSON)");
		}

		// Validate
		try {
			client.validate().resource("FOO").execute();
			fail("");		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1368) + "Unable to determing encoding of request (body does not appear to be valid XML or JSON)");
		}

	}

	@Test
	public void testTransactionWithResponseHttp204NoContent() throws IOException {
		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 204, "No Content"));
		when(myHttpResponse.getEntity()).thenReturn(null);
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[0];
			}
		});

		BundleBuilder builder = new BundleBuilder(ourCtx);
		builder.addTransactionCreateEntry(new Patient().setActive(true));

		IBaseBundle outcome = client.transaction().withBundle(builder.getBundle()).execute();
		assertThat(outcome).isNull();

		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir");

	}


	@Test
	public void testUpdateById() throws Exception {
		IParser p = ourCtx.newXmlParser();

		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");

		final String respString = p.encodeResourceToString(conf);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.setId("222");
		pt.getText().setDivAsString("A PATIENT");

		client.update().resource(pt).withId("111").execute();

		ourLog.info(Arrays.asList(capt.getAllValues().get(0).getAllHeaders()).toString());

		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/111");
		validateUserAgent(capt);

		assertThat(capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", "")).isEqualTo("application/fhir+json;charset=utf-8");
		assertThat(capt.getAllValues().get(0).getHeaders("Accept")[0].getValue()).isEqualTo(Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY);
		String body = extractBodyAsString(capt);
		assertThat(body).contains("\"id\":\"111\"");
	}

	@Test
	public void testUpdateWithPreferRepresentationServerReturnsOO() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final OperationOutcome resp0 = new OperationOutcome();
		resp0.getText().setDivAsString("OK!");

		final Patient resp1 = new Patient();
		resp1.getText().setDivAsString("FINAL VALUE");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3")};
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				if (myAnswerCount++ == 0) {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp0)), StandardCharsets.UTF_8);
				} else {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), StandardCharsets.UTF_8);
				}
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.setId("Patient/222");
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.update().resource(pt).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertThat(myAnswerCount).isEqualTo(2);
		assertThat(outcome.getOperationOutcome()).isNotNull();
		assertThat(outcome.getResource()).isNotNull();

		assertThat(((OperationOutcome) outcome.getOperationOutcome()).getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">OK!</div>");
		assertThat(((Patient) outcome.getResource()).getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>");

		assertThat(capt.getAllValues()).hasSize(myAnswerCount);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/222");
		assertThat(capt.getAllValues().get(1).getURI().toASCIIString()).isEqualTo("http://foo.com/base/Patient/222/_history/3");
	}

	@Test
	public void testUpdateWithPreferRepresentationServerReturnsResource() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final Patient resp1 = new Patient();
		resp1.getText().setDivAsString("FINAL VALUE");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3")};
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				myAnswerCount++;
				return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.setId("Patient/222");
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.update().resource(pt).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertThat(myAnswerCount).isEqualTo(1);
		assertThat(outcome.getOperationOutcome()).isNull();
		assertThat(outcome.getResource()).isNotNull();

		assertThat(((Patient) outcome.getResource()).getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>");

		assertThat(capt.getAllValues()).hasSize(myAnswerCount);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/222");
	}

	@Test
	public void testUserAgentForBinary() throws Exception {
		IParser p = ourCtx.newXmlParser();

		CapabilityStatement conf = new CapabilityStatement();
		conf.setCopyright("COPY");

		final String respString = p.encodeResourceToString(conf);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Binary bin = new Binary();
		bin.setContentType("application/foo");
		bin.setContent(new byte[]{0, 1, 2, 3, 4});
		client.create().resource(bin).execute();

		ourLog.info(Arrays.asList(capt.getAllValues().get(0).getAllHeaders()).toString());

		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Binary");
		validateUserAgent(capt);

		assertThat(capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue()).isEqualTo("application/foo");
		assertThat(capt.getAllValues().get(0).getHeaders("Accept")[0].getValue()).isEqualTo(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_NON_LEGACY);
		assertThat(extractBodyAsByteArray(capt)).containsExactly(new byte[]{0, 1, 2, 3, 4});

	}

	@Test
	public void testUserAgentForConformance() throws Exception {
		IParser p = ourCtx.newXmlParser();

		CapabilityStatement conf = new CapabilityStatement();
		conf.setCopyright("COPY");

		final String respString = p.encodeResourceToString(conf);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		client.capabilities().ofType(CapabilityStatement.class).execute();
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/metadata");
		validateUserAgent(capt);
	}

	@Test
	public void testUserInfoInterceptor() throws Exception {
		final String respString = CustomTypeR4Test.createBundle(CustomTypeR4Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		client.registerInterceptor(new UserInfoInterceptor("user_id", "user_name", "app-name"));

		Bundle resp = client
			.history()
			.onType(Patient.class)
			.returnBundle(Bundle.class)
			.execute();

	}

	@Test
	public void testValidate() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final OperationOutcome resp0 = new OperationOutcome();
		resp0.getText().setDivAsString("OK!");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[]{};
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp0)), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.setId("Patient/222");
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.validate().resource(pt).execute();

		assertThat(outcome.getOperationOutcome()).isNotNull();
		assertThat(((OperationOutcome) outcome.getOperationOutcome()).getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">OK!</div>");

	}

	@Test
	public void testValidateCustomTypeFromClientRead() throws Exception {
		IParser p = ourCtx.newXmlParser();

		MyPatientWithExtensions patient = new MyPatientWithExtensions();
		patient.setId("123");
		patient.getText().setDivAsString("OK!");

		final String respString = p.encodeResourceToString(patient);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		MyPatientWithExtensions read = client.read().resource(MyPatientWithExtensions.class).withId(new IdType("1")).execute();
		assertThat(read.getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">OK!</div>");

		// Ensure that we haven't overridden the default type for the name
		assertThat(MyPatientWithExtensions.class.isAssignableFrom(Patient.class)).isFalse();
		assertThat(Patient.class.isAssignableFrom(MyPatientWithExtensions.class)).isFalse();
		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");
		IParser parser = ourCtx.newXmlParser();
		String encoded = parser.encodeResourceToString(pt);
		pt = (Patient) parser.parseResource(encoded);

	}

	@Test
	public void testValidateCustomTypeFromClientSearch() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Bundle b = new Bundle();

		MyPatientWithExtensions patient = new MyPatientWithExtensions();
		patient.setId("123");
		patient.getText().setDivAsString("OK!");
		b.addEntry().setResource(patient);

		final String respString = p.encodeResourceToString(b);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		Bundle bundle = client.search().forResource(MyPatientWithExtensions.class).returnBundle(Bundle.class).execute();

		assertThat(bundle.getEntry()).hasSize(1);
		assertThat(bundle.getEntry().get(0).getResource().getClass()).isEqualTo(MyPatientWithExtensions.class);
	}


	@SuppressWarnings("unused")
	@Test
	public void testCacheControlNoStore() throws Exception {

		String msg = ourCtx.newXmlParser().encodeResourceToString(new Bundle());

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Observation.class)
			.returnBundle(Bundle.class)
			.cacheControl(new CacheControlDirective().setNoStore(true))
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Observation");
		assertThat(capt.getValue().getHeaders("Cache-Control").length).isEqualTo(1);
		assertThat(capt.getValue().getHeaders("Cache-Control")[0].getValue()).isEqualTo("no-store");
	}

	@SuppressWarnings("unused")
	@Test
	public void testCacheControlNoStoreMaxResults() throws Exception {

		String msg = ourCtx.newXmlParser().encodeResourceToString(new Bundle());

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Observation.class)
			.returnBundle(Bundle.class)
			.cacheControl(new CacheControlDirective().setNoStore(true).setMaxResults(100))
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Observation");
		assertThat(capt.getValue().getHeaders("Cache-Control").length).isEqualTo(1);
		assertThat(capt.getValue().getHeaders("Cache-Control")[0].getValue()).isEqualTo("no-store, max-results=100");
	}

	@SuppressWarnings("unused")
	@Test
	public void testCacheControlNoStoreNoCache() throws Exception {

		String msg = ourCtx.newXmlParser().encodeResourceToString(new Bundle());

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Observation.class)
			.returnBundle(Bundle.class)
			.cacheControl(new CacheControlDirective().setNoStore(true).setNoCache(true))
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Observation");
		assertThat(capt.getValue().getHeaders("Cache-Control").length).isEqualTo(1);
		assertThat(capt.getValue().getHeaders("Cache-Control")[0].getValue()).isEqualTo("no-cache, no-store");
	}

	private Patient createPatientP1() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("foo:bar").setValue("12345");
		p1.addName().setFamily("Smith").addGiven("John");
		return p1;
	}


	@Test
	public void testCreatePopulatesIsCreated() throws Exception {

		Patient p1 = createPatientP1();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		MethodOutcome resp = client.create().resource(ourCtx.newXmlParser().encodeResourceToString(p1)).execute();
		assertThat(resp.getCreated()).isTrue();

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		resp = client.create().resource(ourCtx.newXmlParser().encodeResourceToString(p1)).execute();
		assertThat(resp.getCreated()).isNull();

		ourLog.info("lastRequest: {}", ((GenericClient) client).getLastRequest());
		ourLog.info("lastResponse: {}", ((GenericClient) client).getLastResponse());
		ourLog.info("lastResponseBody: {}", ((GenericClient) client).getLastResponseBody());
	}

	private Bundle createTransactionBundleInput() {
		Bundle input = new Bundle();
		input.setType(BundleType.TRANSACTION);
		input
			.addEntry()
			.setResource(createPatientP1())
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST);
		return input;
	}

	private Bundle createTransactionBundleOutput() {
		Bundle output = new Bundle();
		output.setType(BundleType.TRANSACTIONRESPONSE);
		output
			.addEntry()
			.setResource(createPatientP1())
			.getResponse()
			.setLocation(createPatientP1().getId());
		return output;
	}

	private String extractBody(ArgumentCaptor<HttpUriRequest> capt, int count) throws IOException {
		String body = IOUtils.toString(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(count)).getEntity().getContent(), StandardCharsets.UTF_8);
		return body;
	}

	private String getResourceResult() {
		String msg =
			"<Patient xmlns=\"http://hl7.org/fhir\">"
				+ "<text><status value=\"generated\" /><div xmlns=\"http://www.w3.org/1999/xhtml\">John Cardinal:            444333333        </div></text>"
				+ "<identifier><label value=\"SSN\" /><system value=\"http://orionhealth.com/mrn\" /><value value=\"PRP1660\" /></identifier>"
				+ "<name><use value=\"official\" /><family value=\"Cardinal\" /><given value=\"John\" /></name>"
				+ "<name><family value=\"Kramer\" /><given value=\"Doe\" /></name>"
				+ "<telecom><system value=\"phone\" /><value value=\"555-555-2004\" /><use value=\"work\" /></telecom>"
				+ "<gender><coding><system value=\"http://hl7.org/fhir/v3/AdministrativeGender\" /><code value=\"M\" /></coding></gender>"
				+ "<address><use value=\"home\" /><line value=\"2222 Home Street\" /></address><active value=\"true\" />"
				+ "</Patient>";
		return msg;
	}

	@Test
	public void testCreateWithStringAutoDetectsEncoding() throws Exception {

		Patient p1 = createPatientP1();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int count = 0;
		client.create().resource(ourCtx.newXmlParser().encodeResourceToString(p1)).execute();
		assertThat(capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(extractBody(capt, count)).contains("value=\"John\"");
		count++;

		String resourceAsString = ourCtx.newJsonParser().encodeResourceToString(p1);
		client
			.create()
			.resource(resourceAsString)
			.execute();

		assertThat(capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.JSON.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(extractBody(capt, count)).contains("[\"John\"]");
		count++;

		/*
		 * e.g. Now try with reversed encoding (provide a string that's in JSON and ask the client to use XML)
		 */

		client.create().resource(ourCtx.newXmlParser().encodeResourceToString(p1)).encodedJson().execute();
		assertThat(capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.JSON.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(extractBody(capt, count)).contains("[\"John\"]");
		count++;

		client.create().resource(ourCtx.newJsonParser().encodeResourceToString(p1)).encodedXml().execute();
		assertThat(capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(extractBody(capt, count)).contains("value=\"John\"");

	}

	@Test
	public void testCreateWithTag() throws Exception {

		Patient p1 = createPatientP1();
		p1.getMeta().addTag("http://hl7.org/fhir/tag", "urn:happytag", "This is a happy resource");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		MethodOutcome outcome = client.create().resource(p1).withAdditionalHeader("myHeaderName", "myHeaderValue").execute();
		assertThat(outcome.getId().getIdPart()).isEqualTo("44");
		assertThat(outcome.getId().getVersionIdPart()).isEqualTo("22");

		int count = 0;

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient");
		assertThat(capt.getValue().getMethod()).isEqualTo("POST");
		assertThat(capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.JSON.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(capt.getValue().getFirstHeader("myHeaderName").getValue()).isEqualTo("myHeaderValue");
		count++;

		/*
		 * Try fluent options
		 */
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));
		client.create().resource(p1).execute();
		assertThat(capt.getAllValues().get(1).getURI().toString()).isEqualTo("http://example.com/fhir/Patient");
		assertThat(capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.JSON.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8);
		count++;

		String resourceText = "<Patient xmlns=\"http://hl7.org/fhir\">    </Patient>";
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));
		client.create().resource(resourceText).execute();
		assertThat(capt.getAllValues().get(2).getURI().toString()).isEqualTo("http://example.com/fhir/Patient");
		assertThat(IOUtils.toString(((HttpPost) capt.getAllValues().get(2)).getEntity().getContent())).isEqualTo(resourceText);
		assertThat(capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8);
		count++;
	}

	@Test
	public void testCreateWithTagNonFluent() throws Exception {

		Patient p1 = createPatientP1();
		p1.getMeta().addTag("http://hl7.org/fhir/tag", "urn:happytag", "This is a happy resource");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		MethodOutcome outcome = client.create().resource(p1).execute();
		assertThat(outcome.getId().getIdPart()).isEqualTo("44");
		assertThat(outcome.getId().getVersionIdPart()).isEqualTo("22");

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient");
		assertThat(capt.getValue().getMethod()).isEqualTo("POST");
		Header catH = capt.getValue().getFirstHeader("Category");
		assertThat(catH).isNull();
	}

	/**
	 * Test for issue #60
	 */
	@Test
	public void testCreateWithUtf8Characters() throws Exception {
		String name = "測試醫院";
		Organization org = new Organization();
		org.setName(name);
		org.addIdentifier().setSystem("urn:system").setValue("testCreateWithUtf8Characters_01");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int count = 0;
		client.create().resource(org).prettyPrint().encodedXml().execute();
		assertThat(capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(extractBody(capt, count)).contains("<name value=\"測試醫院\"/>");
		count++;

	}

	@Test
	public void testDelete() throws Exception {
		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().addLocation("testDelete01");
		String ooStr = ourCtx.newXmlParser().encodeResourceToString(oo);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(ooStr), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		MethodOutcome outcome = client
			.delete()
			.resourceById("Patient", "123")
			.withAdditionalHeader("myHeaderName", "myHeaderValue")
			.execute();

		oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient/123");
		assertThat(capt.getValue().getMethod()).isEqualTo("DELETE");
		assertThat(oo.getIssueFirstRep().getLocation().get(0).getValue()).isEqualTo("testDelete01");
		assertThat(capt.getValue().getFirstHeader("myHeaderName").getValue()).isEqualTo("myHeaderValue");

	}


	@Test
	public void testDeleteInvalidResponse() throws Exception {
		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().addLocation("testDelete01");
		String ooStr = ourCtx.newXmlParser().encodeResourceToString(oo);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("LKJHLKJGLKJKLL"), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		// Try with invalid response
		try {
			client
				.delete()
				.resourceById(new IdType("Location", "123", "456"))
				.prettyPrint()
				.encodedJson()
				.execute();
		} catch (FhirClientConnectionException e) {
			assertThat(e.getStatusCode()).isEqualTo(500);
			assertThat(e.getMessage()).contains("Failed to parse response from server when performing DELETE to URL");
		}

	}


	@Test
	public void testDeleteNoResponse() throws Exception {
		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().addLocation("testDelete01");
		String ooStr = ourCtx.newXmlParser().encodeResourceToString(oo);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(ooStr), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		MethodOutcome outcome = client
			.delete()
			.resourceById("Patient", "123")
			.withAdditionalHeader("myHeaderName", "myHeaderValue")
			.execute();

		oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient/123");
		assertThat(capt.getValue().getMethod()).isEqualTo("DELETE");
		assertThat(oo.getIssueFirstRep().getLocation().get(0).getValue()).isEqualTo("testDelete01");
		assertThat(capt.getValue().getFirstHeader("myHeaderName").getValue()).isEqualTo("myHeaderValue");

	}


	@Test
	public void testHistory() throws Exception {

		final String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(t ->
			new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;
		Bundle response;

		response = client
			.history()
			.onServer()
			.returnBundle(Bundle.class)
			.withAdditionalHeader("myHeaderName", "myHeaderValue")
			.execute();
		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/_history");
		assertThat(capt.getValue().getFirstHeader("myHeaderName").getValue()).isEqualTo("myHeaderValue");
		assertThat(response.getEntry()).hasSize(1);
		idx++;

		response = client
			.history()
			.onType(Patient.class)
			.returnBundle(Bundle.class)
			.withAdditionalHeader("myHeaderName", "myHeaderValue1")
			.withAdditionalHeader("myHeaderName", "myHeaderValue2")
			.execute();
		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient/_history");
		assertThat(response.getEntry()).hasSize(1);
		assertThat(capt.getValue().getHeaders("myHeaderName")[0].getValue()).isEqualTo("myHeaderValue1");
		assertThat(capt.getValue().getHeaders("myHeaderName")[1].getValue()).isEqualTo("myHeaderValue2");
		idx++;

		response = client
			.history()
			.onInstance(new IdType("Patient", "123"))
			.andReturnBundle(Bundle.class)
			.execute();
		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient/123/_history");
		assertThat(response.getEntry()).hasSize(1);
		idx++;
	}

	@Test
	@Disabled
	public void testInvalidCalls() {
		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.meta();
			fail("");		} catch (IllegalStateException e) {
			assertThat(e.getMessage()).isEqualTo("Can not call $meta operations on a DSTU1 client");
		}
		try {
			client.operation();
			fail("");		} catch (IllegalStateException e) {
			assertThat(e.getMessage()).isEqualTo("Operations are only supported in FHIR DSTU2 and later. This client was created using a context configured for DSTU1");
		}
	}

	@Test
	public void testLoadPageAndReturnDstu1Bundle() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://foo");
		client
			.loadPage()
			.byUrl("http://example.com/page1")
			.andReturnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/page1");
	}

	@Test
	public void testLoadPage_usingPostPagingStyle_executesPostHttpRequest() throws Exception {
		String msg = getPatientFeedWithOneResult();
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://foo");
		client
			.loadPage()
			.byUrl("http://example.com/page1")
			.andReturnBundle(Bundle.class)
			.usingMethod(PagingHttpMethodEnum.POST)
			.execute();

		assertThat(capt.getValue().getMethod()).isEqualTo("POST");
	}

	@Test
	public void testMissing() throws Exception {

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return (new ReaderInputStream(new StringReader(getPatientFeedWithOneResult()), StandardCharsets.UTF_8));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));

		client.search().forResource("Patient").where(Patient.NAME.isMissing(true)).returnBundle(Bundle.class).execute();
		assertThat(capt.getValue().getRequestLine().getUri()).isEqualTo("http://example.com/fhir/Patient?name%3Amissing=true");

		client.search().forResource("Patient").where(Patient.NAME.isMissing(false)).returnBundle(Bundle.class).execute();
		assertThat(capt.getValue().getRequestLine().getUri()).isEqualTo("http://example.com/fhir/Patient?name%3Amissing=false");
	}

	@Test
	public void testProcessMessage() throws IOException {
		Bundle respBundle = new Bundle();
		respBundle.setType(BundleType.MESSAGE);
		String respString = ourCtx.newJsonParser().encodeResourceToString(respBundle);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[0]);

		Bundle bundle = new Bundle();
		bundle.setType(BundleType.MESSAGE);

		Parameters parameters = new Parameters();
		parameters.addParameter()
			.setName("content")
			.setResource(bundle);

		int count = 0;
		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		client.operation().onType(MessageHeader.class).named("$process-message").withParameters(parameters).execute();

		assertThat(capt.getAllValues().get(count).getURI().toString()).isEqualTo("http://example.com/fhir/MessageHeader/$process-message");
		String requestContent = IOUtils.toString(((HttpPost) capt.getAllValues().get(count)).getEntity().getContent(), Charsets.UTF_8);
		assertThat(requestContent).contains("{\"resourceType\":\"Parameters\"");
		count++;
	}

	@Test
	public void testRead() throws Exception {

		String msg = getResourceResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		Header[] headers = new Header[]{
			new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08 GMT"),
			new BasicHeader(Constants.HEADER_CONTENT_LOCATION, "http://foo.com/Patient/123/_history/2333"),
		};
		when(myHttpResponse.getAllHeaders()).thenReturn(headers);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient response = client
			.read()
			.resource(Patient.class)
			.withId(new IdType("Patient/1234"))
			.execute();

		assertThat(response.getNameFirstRep().getFamily(), StringContains.containsString("Cardinal"));

		assertThat(response.getIdElement().getValue()).isEqualTo("http://foo.com/Patient/123/_history/2333");

		InstantType lm = response.getMeta().getLastUpdatedElement();
		lm.setTimeZoneZulu(true);
		assertThat(lm.getValueAsString()).isEqualTo("1995-11-15T04:58:08.000Z");

	}

	@Test
	public void testReadFluent() throws Exception {

		String msg = getResourceResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		Header[] headers = new Header[]{
			new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08 GMT"),
			new BasicHeader(Constants.HEADER_CONTENT_LOCATION, "http://foo.com/Patient/123/_history/2333"),
		};
		when(myHttpResponse.getAllHeaders()).thenReturn(headers);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int count = 0;

		Patient response = client.read().resource(Patient.class).withId(new IdType("Patient/1234")).execute();
		assertThat(response.getNameFirstRep().getFamily(), StringContains.containsString("Cardinal"));
		assertThat(capt.getAllValues().get(count++).getURI().toString()).isEqualTo("http://example.com/fhir/Patient/1234");

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		response = (Patient) client.read().resource("Patient").withId("1234").execute();
		assertThat(response.getNameFirstRep().getFamily(), StringContains.containsString("Cardinal"));
		assertThat(capt.getAllValues().get(count++).getURI().toString()).isEqualTo("http://example.com/fhir/Patient/1234");

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		response = (Patient) client.read().resource("Patient").withId(567L).execute();
		assertThat(response.getNameFirstRep().getFamily(), StringContains.containsString("Cardinal"));
		assertThat(capt.getAllValues().get(count++).getURI().toString()).isEqualTo("http://example.com/fhir/Patient/567");

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		response = client.read().resource(Patient.class).withIdAndVersion("1234", "22").execute();
		assertThat(response.getNameFirstRep().getFamily(), StringContains.containsString("Cardinal"));
		assertThat(capt.getAllValues().get(count++).getURI().toString()).isEqualTo("http://example.com/fhir/Patient/1234/_history/22");

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		response = client.read().resource(Patient.class).withUrl("http://foo/Patient/22").execute();
		assertThat(response.getNameFirstRep().getFamily(), StringContains.containsString("Cardinal"));
		assertThat(capt.getAllValues().get(count++).getURI().toString()).isEqualTo("http://foo/Patient/22");

	}

	@Test
	public void testReadWithAbsoluteUrl() throws Exception {

		String msg = getResourceResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		Header[] headers = new Header[]{new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08 GMT"),
			new BasicHeader(Constants.HEADER_CONTENT_LOCATION, "http://foo.com/Patient/123/_history/2333"),
		};
		when(myHttpResponse.getAllHeaders()).thenReturn(headers);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient response = client
			.read()
			.resource(Patient.class)
			.withUrl(new IdType("http://somebase.com/path/to/base/Patient/1234"))
			.execute();
		assertThat(response.getNameFirstRep().getFamily(), StringContains.containsString("Cardinal"));
		assertThat(capt.getAllValues().get(0).getURI().toString()).isEqualTo("http://somebase.com/path/to/base/Patient/1234");

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		response = client
			.read()
			.resource(Patient.class)
			.withUrl(new IdType("http://somebase.com/path/to/base/Patient/1234/_history/222"))
			.execute();
		assertThat(response.getNameFirstRep().getFamily(), StringContains.containsString("Cardinal"));
		assertThat(capt.getAllValues().get(1).getURI().toString()).isEqualTo("http://somebase.com/path/to/base/Patient/1234/_history/222");

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchAllResources() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forAllResources()
			.where(Patient.NAME.matches().value("james"))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/?name=james");

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchAutomaticallyUsesPost() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		String longValue = StringUtils.leftPad("", 20000, 'B');

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value(longValue))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient/_search");

		HttpEntityEnclosingRequestBase enc = (HttpEntityEnclosingRequestBase) capt.getValue();
		UrlEncodedFormEntity ent = (UrlEncodedFormEntity) enc.getEntity();
		String string = IOUtils.toString(ent.getContent());
		ourLog.info(string);
		assertThat(string).isEqualTo("name=" + longValue);
	}

	@Test
	public void testSearchByCompartment() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://foo");
		Bundle response = client
			.search()
			.forResource(Patient.class)
			.withIdAndCompartment("123", "fooCompartment")
			.where(Patient.BIRTHDATE.afterOrEquals().day("2011-01-02"))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://foo/Patient/123/fooCompartment?birthdate=ge2011-01-02");

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));

		assertThat(BundleUtil.toListOfResourcesOfType(ourCtx, response, Patient.class).get(0).getIdentifier().get(0).getValue()).isEqualTo("PRP1660");

		try {
			client
				.search()
				.forResource(Patient.class)
				.withIdAndCompartment("", "fooCompartment")
				.where(Patient.BIRTHDATE.afterOrEquals().day("2011-01-02"))
				.returnBundle(Bundle.class)
				.execute();
			fail("");		} catch (InvalidRequestException e) {
			assertThat(e.toString()).contains("null or empty for compartment");
		}

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByComposite() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://foo");

		Bundle response = client.search()
			.forResource("Observation")
			.where(Observation.CODE_VALUE_DATE
				.withLeft(Observation.CODE.exactly().code("FOO$BAR"))
				.withRight(Observation.VALUE_DATE.exactly().day("2001-01-01")))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://foo/Observation?" + Observation.SP_CODE_VALUE_DATE + "=" + UrlUtil.escapeUrlParam("FOO\\$BAR$2001-01-01"));

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByDate() throws Exception {

		final String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		@SuppressWarnings("deprecation")
		Bundle response = client.search()
			.forResource(Patient.class)
			.encodedJson()
			.where(Patient.BIRTHDATE.beforeOrEquals().day("2012-01-22"))
			.and(Patient.BIRTHDATE.after().day("2011-01-01"))
			.include(Patient.INCLUDE_ORGANIZATION)
			.sort().ascending(Patient.BIRTHDATE)
			.sort().descending(Patient.NAME)
			.sort().defaultOrder(Patient.ADDRESS)
			.count(123)
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getAllValues().get(idx++).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?birthdate=le2012-01-22&birthdate=gt2011-01-01&_include=Patient%3Aorganization&_sort=birthdate%2C-name%2Caddress&_count=123&_format=json");

		response = client.search()
			.forResource(Patient.class)
			.encodedJson()
			.where(Patient.BIRTHDATE.beforeOrEquals().day("2012-01-22"))
			.and(Patient.BIRTHDATE.after().day("2011-01-01"))
			.include(Patient.INCLUDE_ORGANIZATION)
			.sort().ascending(Patient.BIRTHDATE)
			.sort().descending(Patient.NAME)
			.sort().defaultOrder(Patient.ADDRESS)
			.count(123)
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getAllValues().get(idx++).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?birthdate=le2012-01-22&birthdate=gt2011-01-01&_include=Patient%3Aorganization&_sort=birthdate%2C-name%2Caddress&_count=123&_format=json");

		response = client.search()
			.forResource(Patient.class)
			.encodedJson()
			.where(Patient.BIRTHDATE.beforeOrEquals().day("2012-01-22").orAfter().day("2020-01-01"))
			.and(Patient.BIRTHDATE.after().day("2011-01-01"))
			.returnBundle(Bundle.class)
			.execute();

		String comma = "%2C";
		assertThat(capt.getAllValues().get(idx++).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?birthdate=le2012-01-22" + comma + "gt2020-01-01&birthdate=gt2011-01-01&_format=json");
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByNumberExact() throws Exception {

		String msg = ourCtx.newXmlParser().encodeResourceToString(new Bundle());

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Observation.class)
			.where(Observation.VALUE_QUANTITY.greaterThan().number(123).andUnits("foo", "bar"))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Observation?value-quantity=gt123%7Cfoo%7Cbar");
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByProfile() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Patient.class)
			.withProfile("http://1")
			.withProfile("http://2")
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient?_profile=http%3A%2F%2F1&_profile=http%3A%2F%2F2");

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByQuantity() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Patient.class)
			.where(Encounter.LENGTH.exactly().number(123).andNoUnits())
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient?length=123%7C%7C");

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByReferenceProperty() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Patient.class)
			.where(Patient.GENERAL_PRACTITIONER.hasChainedProperty(Organization.NAME.matches().value("ORG0")))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient?general-practitioner.name=ORG0");

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByReferenceSimple() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.GENERAL_PRACTITIONER.hasId("123"))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient?general-practitioner=123");

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchBySecurity() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Patient.class)
			.withSecurity("urn:foo", "123")
			.withSecurity("urn:bar", "456")
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient?_security=urn%3Afoo%7C123&_security=urn%3Abar%7C456");

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByString() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient?name=james");

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().values("AAA", "BBB", "C,C"))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getAllValues().get(1).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?name=" + UrlUtil.escapeUrlParam("AAA,BBB,C\\,C"));

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByStringContains() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(t -> new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.contains().value("FOO"))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient?name%3Acontains=FOO");

		response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.contains().values("FOO", "BAR"))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient?name%3Acontains=FOO%2CBAR");

		response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.contains().values(Arrays.asList("FOO", "BAR")))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient?name%3Acontains=FOO%2CBAR");

		response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.contains().value(new StringType("FOO")))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient?name%3Acontains=FOO");
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByStringExact() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matchesExactly().value("james"))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient?name%3Aexact=james");

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByTag() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Patient.class)
			.withTag("urn:foo", "123")
			.withTag("urn:bar", "456")
			.withAdditionalHeader("myHeaderName", "myHeaderValue")
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient?_tag=urn%3Afoo%7C123&_tag=urn%3Abar%7C456");
		assertThat(capt.getValue().getFirstHeader("myHeaderName").getValue()).isEqualTo("myHeaderValue");

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByToken() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.exactly().systemAndCode("http://example.com/fhir", "ZZZ"))
			.withAdditionalHeader("myHeaderName", "myHeaderValue")
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient?identifier=http%3A%2F%2Fexample.com%2Ffhir%7CZZZ");
		assertThat(capt.getValue().getFirstHeader("myHeaderName").getValue()).isEqualTo("myHeaderValue");

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		response = client.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.exactly().code("ZZZ"))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getAllValues().get(1).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?identifier=ZZZ");

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		response = client.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.exactly().codings(new Coding("A", "B", "ZZZ"), new Coding("C", "D", "ZZZ")))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getAllValues().get(2).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?identifier=" + UrlUtil.escapeUrlParam("A|B,C|D"));

	}

	/**
	 * Test for #192
	 */
	@SuppressWarnings("unused")
	@Test
	public void testSearchByTokenWithEscaping() throws Exception {
		final String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://foo");
		int index = 0;
		String wantPrefix = "http://foo/Patient?identifier=";

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.exactly().systemAndCode("1", "2"))
			.returnBundle(Bundle.class)
			.execute();
		String wantValue = "1|2";
		String url = capt.getAllValues().get(index).getURI().toString();
		assertThat(url).startsWith(wantPrefix);
		assertThat(UrlUtil.unescape(url.substring(wantPrefix.length()))).isEqualTo(wantValue);
		assertThat(url.substring(wantPrefix.length())).isEqualTo(UrlUtil.escapeUrlParam(wantValue));
		index++;

		response = client.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.exactly().systemAndCode("1,2", "3,4"))
			.returnBundle(Bundle.class)
			.execute();
		wantValue = "1\\,2|3\\,4";
		url = capt.getAllValues().get(index).getURI().toString();
		assertThat(url).startsWith(wantPrefix);
		assertThat(UrlUtil.unescape(url.substring(wantPrefix.length()))).isEqualTo(wantValue);
		assertThat(url.substring(wantPrefix.length())).isEqualTo(UrlUtil.escapeUrlParam(wantValue));
		index++;
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByTokenWithSystemAndNoCode() throws Exception {

		final String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.hasSystemWithAnyCode("urn:foo"))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(capt.getAllValues().get(idx++).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?identifier=urn%3Afoo%7C");

		response = client.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.exactly().systemAndCode("urn:foo", null))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(capt.getAllValues().get(idx++).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?identifier=urn%3Afoo%7C");

		response = client.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.exactly().systemAndCode("urn:foo", ""))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(capt.getAllValues().get(idx++).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?identifier=urn%3Afoo%7C");
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchIncludeRecursive() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Patient.class)
			.include(Patient.INCLUDE_ORGANIZATION)
			.include(Patient.INCLUDE_LINK.asRecursive())
			.include(IBaseResource.INCLUDE_ALL.asNonRecursive())
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).contains("http://example.com/fhir/Patient?");
		assertThat(capt.getValue().getURI().toString()).contains("_include=" + UrlUtil.escapeUrlParam(Patient.INCLUDE_ORGANIZATION.getValue()));
		assertThat(capt.getValue().getURI().toString()).contains("_include%3Aiterate=" + UrlUtil.escapeUrlParam(Patient.INCLUDE_LINK.getValue()));
		assertThat(capt.getValue().getURI().toString()).contains("_include=*");

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchUsingGetSearch() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.usingStyle(SearchStyleEnum.GET_WITH_SEARCH)
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient/_search?name=james");
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchUsingPost() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.usingStyle(SearchStyleEnum.POST)
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient/_search");

		HttpEntityEnclosingRequestBase enc = (HttpEntityEnclosingRequestBase) capt.getValue();
		UrlEncodedFormEntity ent = (UrlEncodedFormEntity) enc.getEntity();
		String string = IOUtils.toString(ent.getContent());
		ourLog.info(string);
		assertThat(string).isEqualTo("name=james");
	}

	@Test
	public void testSearchWithAbsoluteUrl() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client
			.search()
			.byUrl("http://example.com/fhir/Patient?birthdate=le2012-01-22&birthdate=gt2011-01-01&_include=Patient%3Aorganization&_sort%3Aasc=birthdate&_sort%3Adesc=name&_count=123&_format=json")
			.returnBundle(Bundle.class)
			.execute();

		assertThat(response.getEntry()).hasSize(1);
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithClientEncodingAndPrettyPrintConfig() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		client.setPrettyPrint(true);
		client.setEncoding(EncodingEnum.JSON);

		Bundle response = client.search()
			.forResource(Patient.class)
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient?_format=json&_pretty=true");

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithEscapedParameters() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().values("NE,NE", "NE,NE"))
			.where(Patient.NAME.matchesExactly().values("E$E"))
			.where(Patient.NAME.matches().values("NE\\NE"))
			.where(Patient.NAME.matchesExactly().values("E|E"))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).contains("%3A");
		assertThat(UrlUtil.unescape(capt.getValue().getURI().toString())).isEqualTo("http://example.com/fhir/Patient?name=NE\\,NE,NE\\,NE&name=NE\\\\NE&name:exact=E\\$E&name:exact=E\\|E");
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithInternalServerError() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 500, "INTERNAL ERRORS"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_TEXT + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("Server Issues!"), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client
				.search()
				.forResource(Patient.class)
				.returnBundle(Bundle.class)
				.execute();
			fail("");		} catch (InternalErrorException e) {
			assertThat("HTTP 500 INTERNAL ERRORS: Server Issues!").isEqualTo(e.getMessage());
			assertThat("Server Issues!").isEqualTo(e.getResponseBody());
		}

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithNonFhirResponse() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_TEXT + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("Server Issues!"), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.search().forResource(Patient.class).returnBundle(Bundle.class).execute();
			fail("");		} catch (NonFhirResponseException e) {
			assertThat(e.getMessage(), StringContains.containsString("Server Issues!"));
		}

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithReverseInclude() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Patient.class)
			.encodedJson()
			.revInclude(Provenance.INCLUDE_TARGET)
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient?_revinclude=Provenance%3Atarget&_format=json");

	}

	@Test
	public void testSetDefaultEncoding() throws Exception {

		String msg = ourCtx.newJsonParser().encodeResourceToString(new Patient());

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		// Header[] headers = new Header[] { new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08
		// GMT"),
		// new BasicHeader(Constants.HEADER_CONTENT_LOCATION, "http://foo.com/Patient/123/_history/2333"),
		// new BasicHeader(Constants.HEADER_CATEGORY, "http://foo/tagdefinition.html; scheme=\"http://hl7.org/fhir/tag\";
		// label=\"Some tag\"") };
		// when(myHttpResponse.getAllHeaders()).thenReturn(headers);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		(client).setEncoding(EncodingEnum.JSON);
		int count = 0;

		client
			.read()
			.resource(Patient.class)
			.withId(new IdType("Patient/1234"))
			.execute();
		assertThat(capt.getAllValues().get(count).getURI().toString()).isEqualTo("http://example.com/fhir/Patient/1234?_format=json");
		count++;

	}

	@Test
	public void testTransactionJson() throws Exception {
		Bundle input = createTransactionBundleInput();
		Bundle output = createTransactionBundleOutput();

		String msg = ourCtx.newJsonParser().encodeResourceToString(output);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.transaction()
			.withBundle(input)
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir");
		assertThat(response.getEntry().get(0).getResource().getId()).isEqualTo(input.getEntry().get(0).getResource().getId());
		assertThat(capt.getAllValues().get(0).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.JSON.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8);

	}

	@Test
	public void testTransactionXml() throws Exception {
		Bundle input = createTransactionBundleInput();
		Bundle output = createTransactionBundleOutput();

		String msg = ourCtx.newXmlParser().encodeResourceToString(output);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.transaction()
			.withBundle(input)
			.encodedXml()
			.execute();

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir");
		assertThat(response.getEntry().get(0).getResource().getId()).isEqualTo(input.getEntry().get(0).getResource().getId());
		assertThat(capt.getAllValues().get(0).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8);

	}

	@Test
	public void testUpdate() throws Exception {

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("foo:bar").setValue("12345");
		p1.addName().setFamily("Smith").addGiven("John");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.update().resource(p1).execute();
			fail("");		} catch (InvalidRequestException e) {
			// should happen because no ID set
		}

		assertThat(capt.getAllValues()).isEmpty();

		p1.setId("44");
		client.update().resource(p1).execute();

		int count = 0;

		assertThat(capt.getAllValues()).hasSize(1);
		assertThat(capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.JSON.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8);
		count++;

		MethodOutcome outcome = client.update().resource(p1).execute();
		assertThat(outcome.getId().getIdPart()).isEqualTo("44");
		assertThat(outcome.getId().getVersionIdPart()).isEqualTo("22");

		assertThat(capt.getAllValues()).hasSize(2);

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient/44");
		assertThat(capt.getValue().getMethod()).isEqualTo("PUT");

		/*
		 * Try fluent options
		 */
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));
		client.update().resource(p1).withId("123").execute();
		assertThat(capt.getAllValues()).hasSize(3);
		assertThat(capt.getAllValues().get(2).getURI().toString()).isEqualTo("http://example.com/fhir/Patient/123");

		String resourceText = "<Patient xmlns=\"http://hl7.org/fhir\">    </Patient>";
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));
		client.update().resource(resourceText).withId("123").execute();
		assertThat(capt.getAllValues().get(3).getURI().toString()).isEqualTo("http://example.com/fhir/Patient/123");
		assertThat(IOUtils.toString(((HttpPut) capt.getAllValues().get(3)).getEntity().getContent())).isEqualTo(resourceText);
		assertThat(capt.getAllValues()).hasSize(4);

	}

	@Test
	public void testUpdateWithStringAutoDetectsEncoding() throws Exception {

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("foo:bar").setValue("12345");
		p1.addName().setFamily("Smith").addGiven("John");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int count = 0;
		client.update().resource(ourCtx.newXmlParser().encodeResourceToString(p1)).withId("1").execute();
		assertThat(capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(extractBody(capt, count)).contains("value=\"John\"");
		count++;

		client.update().resource(ourCtx.newJsonParser().encodeResourceToString(p1)).withId("1").execute();
		assertThat(capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.JSON.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(extractBody(capt, count)).contains("[\"John\"]");
		count++;

		/*
		 * e.g. Now try with reversed encoding (provide a string that's in JSON and ask the client to use XML)
		 */

		client.update().resource(ourCtx.newXmlParser().encodeResourceToString(p1)).withId("1").encodedJson().execute();
		assertThat(capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.JSON.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(extractBody(capt, count)).contains("[\"John\"]");
		count++;

		client.update().resource(ourCtx.newJsonParser().encodeResourceToString(p1)).withId("1").encodedXml().execute();
		assertThat(capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(extractBody(capt, count)).contains("value=\"John\"");
		count++;
	}

	@Test
	public void testVReadWithAbsoluteUrl() throws Exception {

		String msg = getResourceResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		Header[] headers = new Header[]{
			new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08 GMT"),
			new BasicHeader(Constants.HEADER_CONTENT_LOCATION, "http://foo.com/Patient/123/_history/2333"),
		};
		when(myHttpResponse.getAllHeaders()).thenReturn(headers);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient response = client
			.read()
			.resource(Patient.class)
			.withUrl("http://somebase.com/path/to/base/Patient/1234/_history/2222")
			.execute();

		assertThat(response.getNameFirstRep().getFamily(), StringContains.containsString("Cardinal"));
		assertThat(capt.getAllValues().get(0).getURI().toString()).isEqualTo("http://somebase.com/path/to/base/Patient/1234/_history/2222");

	}

	@Test
	public void testValidateNonFluent() throws Exception {

		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDiagnostics("OOOK");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(ourCtx.newXmlParser().encodeResourceToString(oo)), StandardCharsets.UTF_8));
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("foo:bar").setValue("12345");
		p1.addName().setFamily("Smith").addGiven("John");

		MethodOutcome resp = client.validate(p1);
		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient/$validate");
		oo = (OperationOutcome) resp.getOperationOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics()).isEqualTo("OOOK");

	}

	private String getPatientFeedWithOneResult() {

		Bundle retVal = new Bundle();

		Patient p = new Patient();
		p.addName().setFamily("Cardinal").addGiven("John");
		p.addIdentifier().setValue("PRP1660");
		retVal.addEntry().setResource(p);

		return ourCtx.newXmlParser().encodeResourceToString(retVal);
	}





}
