package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hl7.fhir.dstu2.model.IdType;
import org.hl7.fhir.dstu2.model.Parameters;
import org.hl7.fhir.dstu2.model.Patient;
import org.hl7.fhir.dstu2.model.StringType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenericClientDstu2Hl7OrgTest {
  private static FhirContext ourCtx;
  private HttpClient myHttpClient;
  private HttpResponse myHttpResponse;

  @BeforeAll
  public static void beforeClass() {
    ourCtx = FhirContext.forDstu2Hl7Org();
  }

  @BeforeEach
  public void before() {
    myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
    ourCtx.getRestfulClientFactory().setHttpClient(myHttpClient);
    ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
    myHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
  }

  @Test
  public void testReadUpdatedHeaderDoesntOverwriteResourceValue() throws Exception {

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

    ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
    when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
    when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
    when(myHttpResponse.getAllHeaders()).thenReturn(new Header[] {
        new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Sat, 20 Jun 2015 19:32:17 GMT")
    });
    when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
      @Override
      public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
        return new ReaderInputStream(new StringReader(input), Charset.forName("UTF-8"));
      }
    });

    IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

    org.hl7.fhir.dstu2.model.Bundle response;

    //@formatter:off
		response = client
				.search()
				.forResource(Patient.class)
				.returnBundle(org.hl7.fhir.dstu2.model.Bundle.class)
				.execute();
		//@formatter:on

		assertThat(response.getMeta().getLastUpdatedElement().getValueAsString()).isEqualTo("2015-06-22T15:48:57.554-04:00");
  }

  @SuppressWarnings("unused")
  @Test
  public void testSearchWithReverseInclude() throws Exception {

    String msg = getPatientFeedWithOneResult();

    ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
    when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
    when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
    when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

    IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

    //@formatter:off
		org.hl7.fhir.dstu2.model.Bundle response = client.search()
				.forResource(Patient.class)
				.encodedJson()
				.revInclude(new Include("Provenance:target"))
				.returnBundle(org.hl7.fhir.dstu2.model.Bundle.class)
				.execute();
		//@formatter:on

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient?_revinclude=Provenance%3Atarget&_format=json");

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
  public void testHistory() throws Exception {

    final String msg = getPatientFeedWithOneResult();

    ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
    when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
    when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
    when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
      @Override
      public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
        return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
      }
    });

    IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

    int idx = 0;
    org.hl7.fhir.dstu2.model.Bundle response;

    //@formatter:off
		response = client
				.history()
				.onServer()
				.andReturnBundle(org.hl7.fhir.dstu2.model.Bundle.class)
				.execute();
		//@formatter:on
	assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/_history");
		assertThat(response.getEntry()).hasSize(1);
    idx++;

    //@formatter:off
		response = client
				.history()
				.onType(Patient.class)
				.andReturnBundle(org.hl7.fhir.dstu2.model.Bundle.class)
				.execute();
		//@formatter:on
	assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient/_history");
		assertThat(response.getEntry()).hasSize(1);
    idx++;

    //@formatter:off
		response = client
				.history()
				.onInstance(new IdType("Patient", "123"))
				.andReturnBundle(org.hl7.fhir.dstu2.model.Bundle.class)
				.execute();
		//@formatter:on
	assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient/123/_history");
		assertThat(response.getEntry()).hasSize(1);
    idx++;
  }

  @Test
  public void testSearchByString() throws Exception {
    String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

    ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
    when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
    when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
    when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

    IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

    //@formatter:off
        org.hl7.fhir.dstu2.model.Bundle response = client.search()
                .forResource("Patient")
                .where(new StringClientParam("name").matches().value("james"))
                .returnBundle(org.hl7.fhir.dstu2.model.Bundle.class)
                .execute();
		//@formatter:on

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir/Patient?name=james");
		assertThat(response.getEntry().get(0).getResource().getClass()).isEqualTo(Patient.class);

  }


  @Test
  public void testOperationWithListOfParameterResponse() throws Exception {
    IParser p = ourCtx.newJsonParser();

    Parameters inParams = new Parameters();
    inParams.addParameter().setValue(new StringType("STRINGVALIN1"));
    inParams.addParameter().setValue(new StringType("STRINGVALIN2"));
    String reqString = p.encodeResourceToString(inParams);

    Parameters outParams = new Parameters();
    outParams.addParameter().setValue(new StringType("STRINGVALOUT1"));
    outParams.addParameter().setValue(new StringType("STRINGVALOUT2"));
    final String respString = p.encodeResourceToString(outParams);

    ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
    when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
    when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
    when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
      @Override
      public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
        return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
      }
    });

    IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

    int idx = 0;

		Parameters resp = client
				.operation()
				.onServer()
				.named("$SOMEOPERATION")
				.withParameters(inParams)
        .execute();
		assertThat(capt.getAllValues().get(idx).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/$SOMEOPERATION");
		assertThat(p.encodeResourceToString(resp)).isEqualTo(respString);
		assertThat(capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.JSON.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(reqString).isEqualTo(extractBody(capt, idx));
		assertThat(capt.getAllValues().get(idx).getRequestLine().getMethod()).isEqualTo("POST");
    idx++;

    //@formatter:off
		resp = client
				.operation()
				.onType(Patient.class)
				.named("$SOMEOPERATION")
				.withParameters(inParams).execute();
		//@formatter:on		
	assertThat(capt.getAllValues().get(idx).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/$SOMEOPERATION");
		assertThat(p.encodeResourceToString(resp)).isEqualTo(respString);
		assertThat(capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.JSON.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(reqString).isEqualTo(extractBody(capt, idx));
		assertThat(capt.getAllValues().get(idx).getRequestLine().getMethod()).isEqualTo("POST");
    idx++;

    //@formatter:off
		resp = client
				.operation()
				.onInstance(new IdType("Patient", "123"))
				.named("$SOMEOPERATION")
				.withParameters(inParams).execute();
		//@formatter:on		
	assertThat(capt.getAllValues().get(idx).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/123/$SOMEOPERATION");
		assertThat(p.encodeResourceToString(resp)).isEqualTo(respString);
		assertThat(capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.JSON.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(reqString).isEqualTo(extractBody(capt, idx));
		assertThat(capt.getAllValues().get(idx).getRequestLine().getMethod()).isEqualTo("POST");
    idx++;

    resp = client.operation().onInstance(new IdType("http://foo.com/bar/baz/Patient/123/_history/22")).named("$SOMEOPERATION").withParameters(inParams).execute();
		// @formatter:on
		assertThat(capt.getAllValues().get(idx).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/123/$SOMEOPERATION");
    idx++;
  }

  @Test
  public void testOperationWithNoInParameters() throws Exception {
    IParser p = ourCtx.newJsonParser();

    Parameters inParams = new Parameters();
    final String reqString = p.encodeResourceToString(inParams);

    Parameters outParams = new Parameters();
    outParams.addParameter().setValue(new StringType("STRINGVALOUT1"));
    outParams.addParameter().setValue(new StringType("STRINGVALOUT2"));
    final String respString = p.encodeResourceToString(outParams);

    ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
    when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
    when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
    when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
      @Override
      public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
        return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
      }
    });

    IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

    int idx = 0;

    //@formatter:off
		Parameters resp = client
				.operation()
				.onServer()
				.named("$SOMEOPERATION")
				.withNoParameters(Parameters.class).execute();
		//@formatter:on
	assertThat(capt.getAllValues().get(idx).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/$SOMEOPERATION");
		assertThat(p.encodeResourceToString(resp)).isEqualTo(respString);
		assertThat(capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.JSON.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(reqString).isEqualTo(extractBody(capt, idx));
		assertThat(capt.getAllValues().get(idx).getRequestLine().getMethod()).isEqualTo("POST");
    idx++;

    //@formatter:off
		resp = client
				.operation()
				.onType(Patient.class)
				.named("$SOMEOPERATION")
				.withNoParameters(Parameters.class).execute();
		//@formatter:on		
	assertThat(capt.getAllValues().get(idx).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/$SOMEOPERATION");
		assertThat(p.encodeResourceToString(resp)).isEqualTo(respString);
		assertThat(capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.JSON.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(reqString).isEqualTo(extractBody(capt, idx));
		assertThat(capt.getAllValues().get(idx).getRequestLine().getMethod()).isEqualTo("POST");
    idx++;

    //@formatter:off
		resp = client
				.operation()
				.onInstance(new IdType("Patient", "123"))
				.named("$SOMEOPERATION")
				.withNoParameters(Parameters.class).execute();
		//@formatter:on		
	assertThat(capt.getAllValues().get(idx).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/123/$SOMEOPERATION");
		assertThat(p.encodeResourceToString(resp)).isEqualTo(respString);
		assertThat(capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.JSON.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(reqString).isEqualTo(extractBody(capt, idx));
		assertThat(capt.getAllValues().get(idx).getRequestLine().getMethod()).isEqualTo("POST");
    idx++;

    // @formatter:off
		resp = client
				.operation()
				.onInstance(new IdType("http://foo.com/bar/baz/Patient/123/_history/22"))
				.named("$SOMEOPERATION")
				.withNoParameters(Parameters.class)
				.execute();
		// @formatter:on
	assertThat(capt.getAllValues().get(idx).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/123/$SOMEOPERATION");
    idx++;
  }

  @Test
  public void testOperationAsGetWithNoInParameters() throws Exception {
    IParser p = ourCtx.newXmlParser();

    Parameters outParams = new Parameters();
    outParams.addParameter().setValue(new StringType("STRINGVALOUT1"));
    outParams.addParameter().setValue(new StringType("STRINGVALOUT2"));
    final String respString = p.encodeResourceToString(outParams);

    ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
    when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
    when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
    when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
      @Override
      public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
        return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
      }
    });

    IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

    int idx = 0;

    //@formatter:off
		Parameters resp = client
				.operation()
				.onServer()
				.named("$SOMEOPERATION")
				.withNoParameters(Parameters.class)
				.useHttpGet()
				.execute();
		//@formatter:on
	assertThat(capt.getAllValues().get(idx).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/$SOMEOPERATION");
		assertThat(p.encodeResourceToString(resp)).isEqualTo(respString);
		assertThat(capt.getAllValues().get(idx).getRequestLine().getMethod()).isEqualTo("GET");
    idx++;

    //@formatter:off
		resp = client
				.operation()
				.onType(Patient.class)
				.named("$SOMEOPERATION")
				.withNoParameters(Parameters.class)
				.useHttpGet()
				.execute();
		//@formatter:on		
	assertThat(capt.getAllValues().get(idx).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/$SOMEOPERATION");
		assertThat(p.encodeResourceToString(resp)).isEqualTo(respString);
		assertThat(capt.getAllValues().get(idx).getRequestLine().getMethod()).isEqualTo("GET");
    idx++;

    //@formatter:off
		resp = client
				.operation()
				.onInstance(new IdType("Patient", "123"))
				.named("$SOMEOPERATION")
				.withNoParameters(Parameters.class)
				.useHttpGet()
				.execute();
		//@formatter:on		
	assertThat(capt.getAllValues().get(idx).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/123/$SOMEOPERATION");
		assertThat(p.encodeResourceToString(resp)).isEqualTo(respString);
		assertThat(capt.getAllValues().get(idx).getRequestLine().getMethod()).isEqualTo("GET");
    idx++;

    // @formatter:off
		resp = client
				.operation()
				.onInstance(new IdType("http://foo.com/bar/baz/Patient/123/_history/22"))
				.named("$SOMEOPERATION")
				.withNoParameters(Parameters.class)
				.useHttpGet()
				.execute();
		// @formatter:on
	assertThat(capt.getAllValues().get(idx).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/123/$SOMEOPERATION");
    idx++;
  }

  @Test
  public void testOperationAsGetWithInParameters() throws Exception {
    IParser p = ourCtx.newXmlParser();

    Parameters inParams = new Parameters();
    inParams.addParameter().setName("param1").setValue(new StringType("STRINGVALIN1"));
    inParams.addParameter().setName("param1").setValue(new StringType("STRINGVALIN1b"));
    inParams.addParameter().setName("param2").setValue(new StringType("STRINGVALIN2"));

    Parameters outParams = new Parameters();
    outParams.addParameter().setValue(new StringType("STRINGVALOUT1"));
    outParams.addParameter().setValue(new StringType("STRINGVALOUT2"));
    final String respString = p.encodeResourceToString(outParams);

    ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
    when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
    when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
    when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
      @Override
      public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
        return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
      }
    });

    IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

    int idx = 0;

    //@formatter:off
		Parameters resp = client
				.operation()
				.onServer()
				.named("$SOMEOPERATION")
				.withParameters(inParams)
				.useHttpGet()
				.execute();
		//@formatter:on
	assertThat(capt.getAllValues().get(idx).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2");
		assertThat(p.encodeResourceToString(resp)).isEqualTo(respString);
		assertThat(capt.getAllValues().get(idx).getRequestLine().getMethod()).isEqualTo("GET");
    idx++;

    //@formatter:off
		resp = client
				.operation()
				.onType(Patient.class)
				.named("$SOMEOPERATION")
				.withParameters(inParams)
				.useHttpGet()
				.execute();
		//@formatter:on		
	assertThat(capt.getAllValues().get(idx).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2");
		assertThat(p.encodeResourceToString(resp)).isEqualTo(respString);
		assertThat(capt.getAllValues().get(idx).getRequestLine().getMethod()).isEqualTo("GET");
    idx++;

    //@formatter:off
		resp = client
				.operation()
				.onInstance(new IdType("Patient", "123"))
				.named("$SOMEOPERATION")
				.withParameters(inParams)
				.useHttpGet()
				.execute();
		//@formatter:on		
	assertThat(capt.getAllValues().get(idx).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/123/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2");
		assertThat(p.encodeResourceToString(resp)).isEqualTo(respString);
		assertThat(capt.getAllValues().get(idx).getRequestLine().getMethod()).isEqualTo("GET");
    idx++;

    // @formatter:off
		resp = client
				.operation()
				.onInstance(new IdType("http://foo.com/bar/baz/Patient/123/_history/22"))
				.named("$SOMEOPERATION")
				.withParameters(inParams)
				.useHttpGet()
				.execute();
		// @formatter:on
	assertThat(capt.getAllValues().get(idx).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/123/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2");
    idx++;
  }

  @Test
  public void testOperationWithBundleResponse() throws Exception {
    IParser p = ourCtx.newJsonParser();

    Parameters inParams = new Parameters();
    inParams.addParameter().setValue(new StringType("STRINGVALIN1"));
    inParams.addParameter().setValue(new StringType("STRINGVALIN2"));
    String reqString = p.encodeResourceToString(inParams);

    org.hl7.fhir.dstu2.model.Bundle outParams = new org.hl7.fhir.dstu2.model.Bundle();
    outParams.setTotal(123);
    final String respString = p.encodeResourceToString(outParams);

    ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
    when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
    when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
    when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
      @Override
      public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
        return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
      }
    });

    IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

    int idx = 0;

    //@formatter:off
		Parameters resp = client
				.operation()
				.onServer()
				.named("$SOMEOPERATION")
				.withParameters(inParams).execute();
		//@formatter:on
	assertThat(capt.getAllValues().get(idx).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/$SOMEOPERATION");
		assertThat(capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.JSON.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(reqString).isEqualTo(extractBody(capt, idx));
		assertThat(capt.getAllValues().get(idx).getRequestLine().getMethod()).isEqualTo("POST");
		assertThat(resp.getParameter()).hasSize(1);
		assertThat(resp.getParameter().get(0).getResource().getClass()).isEqualTo(org.hl7.fhir.dstu2.model.Bundle.class);
    idx++;
  }

  @Test
  public void testTransactionWithListOfResources() throws Exception {

    org.hl7.fhir.dstu2.model.Bundle resp = new org.hl7.fhir.dstu2.model.Bundle();
    resp.addEntry().getResponse().setLocation("Patient/1/_history/1");
    resp.addEntry().getResponse().setLocation("Patient/2/_history/2");
    String respString = ourCtx.newJsonParser().encodeResourceToString(resp);

    ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
    when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
    when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
    when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8")));

    IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

    List<IBaseResource> input = new ArrayList<IBaseResource>();

    Patient p1 = new Patient(); // No ID
    p1.addName().addFamily("PATIENT1");
    input.add(p1);

    Patient p2 = new Patient(); // Yes ID
    p2.addName().addFamily("PATIENT2");
    p2.setId("http://example.com/Patient/2");
    input.add(p2);

    //@formatter:off
        List<IBaseResource> response = client.transaction()
                .withResources(input)
                .encodedJson()
                .execute();
		//@formatter:on

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir");
		assertThat(response).hasSize(2);

    String requestString = IOUtils.toString(((HttpEntityEnclosingRequest) capt.getValue()).getEntity().getContent());
    org.hl7.fhir.dstu2.model.Bundle requestBundle = ourCtx.newJsonParser().parseResource(org.hl7.fhir.dstu2.model.Bundle.class, requestString);
		assertThat(requestBundle.getEntry()).hasSize(2);
		assertThat(requestBundle.getEntry().get(0).getRequest().getMethod().name()).isEqualTo("POST");
		assertThat(requestBundle.getEntry().get(1).getRequest().getMethod().name()).isEqualTo("PUT");
		assertThat(requestBundle.getEntry().get(1).getFullUrl()).isEqualTo("http://example.com/Patient/2");

    p1 = (Patient) response.get(0);
		assertThat(p1.getIdElement().toUnqualified()).isEqualTo(new IdType("Patient/1/_history/1"));
    // assertEquals("PATIENT1", p1.getName().get(0).getFamily().get(0).getValue());

    p2 = (Patient) response.get(1);
		assertThat(p2.getIdElement().toUnqualified()).isEqualTo(new IdType("Patient/2/_history/2"));
    // assertEquals("PATIENT2", p2.getName().get(0).getFamily().get(0).getValue());
  }

  @Test
  public void testTransactionWithTransactionResource() throws Exception {

    org.hl7.fhir.dstu2.model.Bundle resp = new org.hl7.fhir.dstu2.model.Bundle();
    resp.addEntry().getResponse().setLocation("Patient/1/_history/1");
    resp.addEntry().getResponse().setLocation("Patient/2/_history/2");
    String respString = ourCtx.newJsonParser().encodeResourceToString(resp);

    ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
    when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
    when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
    when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8")));

    IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

    org.hl7.fhir.dstu2.model.Bundle input = new org.hl7.fhir.dstu2.model.Bundle();

    Patient p1 = new Patient(); // No ID
    p1.addName().addFamily("PATIENT1");
    input.addEntry().setResource(p1);

    Patient p2 = new Patient(); // Yes ID
    p2.addName().addFamily("PATIENT2");
    p2.setId("Patient/2");
    input.addEntry().setResource(p2);

    //@formatter:off
        org.hl7.fhir.dstu2.model.Bundle response = client.transaction()
                .withBundle(input)
                .encodedJson()
                .execute();
		//@formatter:on

		assertThat(capt.getValue().getURI().toString()).isEqualTo("http://example.com/fhir");
		assertThat(response.getEntry()).hasSize(2);

		assertThat(response.getEntry().get(0).getResponse().getLocation()).isEqualTo("Patient/1/_history/1");
		assertThat(response.getEntry().get(1).getResponse().getLocation()).isEqualTo("Patient/2/_history/2");
  }

  @Test
  public void testDeleteConditional() throws Exception {
    ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
    when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), Constants.STATUS_HTTP_204_NO_CONTENT, ""));
    // when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type",
    // Constants.CT_TEXT + "; charset=UTF-8"));
    when(myHttpResponse.getEntity().getContent()).then(new Answer<ReaderInputStream>() {
      @Override
      public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
        return new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8"));
      }
    });

    IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

    int idx = 0;

    client.delete().resourceById(new IdType("Patient/123")).execute();
		assertThat(capt.getAllValues().get(idx).getMethod()).isEqualTo("DELETE");
		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient/123");
    idx++;

    client.delete().resourceConditionalByUrl("Patient?name=foo").execute();
		assertThat(capt.getAllValues().get(idx).getMethod()).isEqualTo("DELETE");
		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?name=foo");
    idx++;

    client.delete().resourceConditionalByType("Patient").where(new StringClientParam("name").matches().value("foo")).execute();
		assertThat(capt.getAllValues().get(idx).getMethod()).isEqualTo("DELETE");
		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?name=foo");
    idx++;

  }

  @Test
  public void testCreateConditional() throws Exception {
    ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
    when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), Constants.STATUS_HTTP_204_NO_CONTENT, ""));
    when(myHttpResponse.getEntity().getContent()).then(new Answer<ReaderInputStream>() {
      @Override
      public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
        return new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8"));
      }
    });

    IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

    int idx = 0;

    Patient p = new Patient();
    p.addName().addFamily("FOOFAMILY");

    client.create().resource(p).conditionalByUrl("Patient?name=foo").execute();
		assertThat(capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.JSON.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(extractBody(capt, idx)).contains("{\"family\":[\"FOOFAMILY\"]}");
		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient");
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_IF_NONE_EXIST).getValue()).isEqualTo("http://example.com/fhir/Patient?name=foo");
		assertThat(capt.getAllValues().get(idx).getRequestLine().getMethod()).isEqualTo("POST");
    idx++;

    client.create().resource(p).conditional().where(new StringClientParam("name").matches().value("foo")).execute();
		assertThat(capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.JSON.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(extractBody(capt, idx)).contains("{\"family\":[\"FOOFAMILY\"]}");
		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient");
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_IF_NONE_EXIST).getValue()).isEqualTo("http://example.com/fhir/Patient?name=foo");
		assertThat(capt.getAllValues().get(idx).getRequestLine().getMethod()).isEqualTo("POST");
    idx++;

  }

  @Test
  public void testUpdateConditional() throws Exception {
    ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
    when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), Constants.STATUS_HTTP_204_NO_CONTENT, ""));
    when(myHttpResponse.getEntity().getContent()).then(new Answer<ReaderInputStream>() {
      @Override
      public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
        return new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8"));
      }
    });

    IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

    int idx = 0;

    Patient p = new Patient();
    p.addName().addFamily("FOOFAMILY");

    client.update().resource(p).conditionalByUrl("Patient?name=foo").encodedXml().execute();
		assertThat(capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(extractBody(capt, idx)).contains("<family value=\"FOOFAMILY\"/>");
		assertThat(capt.getAllValues().get(idx).getRequestLine().getMethod()).isEqualTo("PUT");
		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?name=foo");
    idx++;

    client.update().resource(ourCtx.newXmlParser().encodeResourceToString(p)).conditionalByUrl("Patient?name=foo").encodedXml().execute();
		assertThat(capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(extractBody(capt, idx)).contains("<family value=\"FOOFAMILY\"/>");
		assertThat(capt.getAllValues().get(idx).getRequestLine().getMethod()).isEqualTo("PUT");
		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?name=foo");
    idx++;

    client.update().resource(p).conditional().where(new StringClientParam("name").matches().value("foo")).and(new StringClientParam("address").matches().value("AAA|BBB")).encodedXml().execute();
		assertThat(capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(extractBody(capt, idx)).contains("<family value=\"FOOFAMILY\"/>");
		assertThat(capt.getAllValues().get(idx).getRequestLine().getMethod()).isEqualTo("PUT");
		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?name=foo&address=AAA%5C%7CBBB");
    idx++;

    client.update().resource(ourCtx.newXmlParser().encodeResourceToString(p)).conditional().where(new StringClientParam("name").matches().value("foo"))
        .and(new StringClientParam("address").matches().value("AAA|BBB")).encodedXml().execute();
		assertThat(capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length).isEqualTo(1);
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).isEqualTo(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		assertThat(extractBody(capt, idx)).contains("<family value=\"FOOFAMILY\"/>");
		assertThat(capt.getAllValues().get(idx).getRequestLine().getMethod()).isEqualTo("PUT");
		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?name=foo&address=AAA%5C%7CBBB");
    idx++;

  }

  private String extractBody(ArgumentCaptor<HttpUriRequest> capt, int count) throws IOException {
    String body = IOUtils.toString(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(count)).getEntity().getContent(), "UTF-8");
    return body;
  }

}
