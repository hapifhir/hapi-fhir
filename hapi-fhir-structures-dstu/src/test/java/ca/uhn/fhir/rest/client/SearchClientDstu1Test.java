package ca.uhn.fhir.rest.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.TestUtil;

public class SearchClientDstu1Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchClientDstu1Test.class);

	private FhirContext ourCtx;
	private HttpClient ourHttpClient;
	private HttpResponse ourHttpResponse;

	@Before
	public void before() {
		ourCtx = new FhirContext(Patient.class, Conformance.class);

		ourHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.getRestfulClientFactory().setHttpClient(ourHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

		ourHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	}

	@Test
	public void testPostOnLongParamsList() throws Exception {
		String retVal = "<feed xmlns=\"http://www.w3.org/2005/Atom\"><title/><id>bc59fca7-0a8f-4caf-abef-45c8d53ece6a</id><link rel=\"self\" href=\"http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2/Encounter?identifier=urn%3Aoid%3A1.3.6.1.4.1.12201.2%7C11410000159&amp;_include=Encounter.participant&amp;_include=Encounter.location.location&amp;_include=Encounter.subject\"/><link rel=\"fhir-base\" href=\"http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2\"/><os:totalResults xmlns:os=\"http://a9.com/-/spec/opensearch/1.1/\">1</os:totalResults><author><name>HAPI FHIR Server</name></author>"
				+ "<entry><title>Encounter 5994268</title><id>http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2/Encounter/5994268</id><updated>2014-08-05T12:00:11.000-04:00</updated><published>2014-08-05T11:59:21.000-04:00</published><link rel=\"self\" href=\"http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2/Encounter/5994268\"/><content type=\"text/xml\"><Encounter xmlns=\"http://hl7.org/fhir\"><extension url=\"http://fhir.connectinggta.ca/Profile/encounter#rehabrecheck\"><valueCoding><system value=\"urn:tri:qcpr:rehab_recheck_codes\"/><code value=\"N\"/></valueCoding></extension><text><status value=\"empty\"/><div xmlns=\"http://www.w3.org/1999/xhtml\">No narrative template available for resource profile: http://fhir.connectinggta.ca/Profile/encounter</div></text><contained><Organization xmlns=\"http://hl7.org/fhir\" id=\"1\"><name value=\"General Internal Medicine\"/></Organization></contained><identifier><use value=\"official\"/><label value=\"UHN Visit Number 11410000159\"/><system value=\"urn:oid:1.3.6.1.4.1.12201.2\"/><value value=\"11410000159\"/></identifier><status value=\"finished\"/><class value=\"I\"/><subject><reference value=\"Patient/5993715\"/></subject><participant><type><coding><code value=\"attending\"/></coding></type><individual><reference value=\"Practitioner/5738815\"/></individual></participant><participant><type><coding><code value=\"referring\"/></coding></type><individual><reference value=\"Practitioner/5738815\"/></individual></participant><period><start value=\"2014-08-05T11:59:00-04:00\"/><end value=\"2014-08-05T11:59:00-04:00\"/></period><reason><coding><display value=\"sick\"/></coding><text value=\"sick\"/></reason><location><location><reference value=\"Location/5994269\"/></location></location><serviceProvider><reference value=\"#1\"/></serviceProvider></Encounter></content></entry>"
				+ "<entry><title>Patient 5993715</title><id>http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2/Patient/5993715</id><published>2014-08-08T14:46:16-04:00</published><link rel=\"self\" href=\"http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2/Patient/5993715\"/><content type=\"text/xml\"><Patient xmlns=\"http://hl7.org/fhir\"><text><status value=\"generated\"/><div xmlns=\"http://www.w3.org/1999/xhtml\"><div class=\"hapiHeaderText\"> Person <b>CHA </b></div><table class=\"hapiPropertyTable\"><tbody><tr><td>Identifier</td><td>UHN MRN 7018614</td></tr><tr><td>Address</td><td><span>100 Dundas street west </span><br/><span>Toronto </span><span>ON </span><span>Can </span></td></tr><tr><td>Date of birth</td><td><span>01 January 1988</span></td></tr></tbody></table></div></text><identifier><use value=\"official\"/><label value=\"UHN MRN 7018614\"/><system value=\"urn:oid:2.16.840.1.113883.3.239.18.148\"/><value value=\"7018614\"/><assigner><reference value=\"Organization/1.3.6.1.4.1.12201\"/></assigner></identifier><identifier><use value=\"secondary\"/><label value=\"OHIP 2341213425\"/><system value=\"urn:oid:2.16.840.1.113883.4.595\"/><value value=\"2341213425\"/></identifier><name><family value=\"Cha\"/><given value=\"Person\"/></name><telecom><system value=\"phone\"/><value value=\"(416)221-4324\"/><use value=\"home\"/></telecom><telecom><system value=\"phone\"/><use value=\"work\"/></telecom><telecom><system value=\"phone\"/><use value=\"mobile\"/></telecom><telecom><system value=\"email\"/><use value=\"home\"/></telecom><gender><coding><system value=\"http://hl7.org/fhir/v3/AdministrativeGender\"/><code value=\"F\"/></coding></gender><birthDate value=\"1988-01-01T00:00:00\"/><address><use value=\"home\"/><line value=\"100 Dundas street west\"/><city value=\"Toronto\"/><state value=\"ON\"/><zip value=\"L4A 3M9\"/><country value=\"Can\"/></address><managingOrganization><reference value=\"Organization/1.3.6.1.4.1.12201\"/></managingOrganization></Patient></content></entry>"
				+ "<entry><title>Practitioner Practitioner/5738815</title><id>http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2/Practitioner/5738815</id><updated>2014-08-08T13:53:52.000-04:00</updated><published>2009-12-04T13:43:11.000-05:00</published><link rel=\"self\" href=\"http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2/Practitioner/5738815\"/><content type=\"text/xml\"><Practitioner xmlns=\"http://hl7.org/fhir\"><text><status value=\"empty\"/><div xmlns=\"http://www.w3.org/1999/xhtml\">No narrative template available for resource profile: http://hl7.org/fhir/profiles/Practitioner</div></text><identifier><system value=\"urn:uhn:qcpr:user_ids\"/><value value=\"5186\"/></identifier><name><family value=\"Generic\"/><given value=\"Physician\"/></name></Practitioner></content></entry>"
				+ "<entry><title>Location Location/5994269</title><id>http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2/Location/5994269</id><published>2014-08-08T14:46:16-04:00</published><link rel=\"self\" href=\"http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2/Location/5994269\"/><content type=\"text/xml\"><Location xmlns=\"http://hl7.org/fhir\"><text><status value=\"empty\"/><div xmlns=\"http://www.w3.org/1999/xhtml\">No narrative template available for resource profile: http://hl7.org/fhir/profiles/Location</div></text><name value=\"ES14 414 2\"/><type><coding><code value=\"bed\"/></coding></type></Location></content></entry></feed>";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(ourHttpClient.execute(capt.capture())).thenReturn(ourHttpResponse);
		when(ourHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(ourHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_ATOM_XML + "; charset=UTF-8"));
		when(ourHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(retVal), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		Set<Include> includes = new HashSet<Include>();
		includes.add(new Include("one"));
		includes.add(new Include("two"));
		TokenOrListParam params = new TokenOrListParam();
		for (int i = 0; i < 1000; i++) {
			params.add(new TokenParam("system", "value"));
		}
		List<Encounter> found = client.searchByList(params, includes);

		assertEquals(1, found.size());

		Encounter encounter = found.get(0);
		assertNotNull(encounter.getSubject().getResource());
		HttpUriRequest value = capt.getValue();

		assertTrue("Expected request of type POST on long params list", value instanceof HttpPost);
		HttpPost post = (HttpPost) value;
		String body = IOUtils.toString(post.getEntity().getContent());
		ourLog.info(body);
		assertThat(body, Matchers.containsString("_include=one"));
		assertThat(body, Matchers.containsString("_include=two"));
	}

	@Test
	public void testReturnTypedList() throws Exception {
		String retVal = "<feed xmlns=\"http://www.w3.org/2005/Atom\"><title/><id>bc59fca7-0a8f-4caf-abef-45c8d53ece6a</id><link rel=\"self\" href=\"http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2/Encounter?identifier=urn%3Aoid%3A1.3.6.1.4.1.12201.2%7C11410000159&amp;_include=Encounter.participant&amp;_include=Encounter.location.location&amp;_include=Encounter.subject\"/><link rel=\"fhir-base\" href=\"http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2\"/><os:totalResults xmlns:os=\"http://a9.com/-/spec/opensearch/1.1/\">1</os:totalResults><author><name>HAPI FHIR Server</name></author><entry><title>Encounter 5994268</title><id>http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2/Encounter/5994268</id><updated>2014-08-05T12:00:11.000-04:00</updated><published>2014-08-05T11:59:21.000-04:00</published><link rel=\"self\" href=\"http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2/Encounter/5994268\"/><content type=\"text/xml\"><Encounter xmlns=\"http://hl7.org/fhir\"><extension url=\"http://fhir.connectinggta.ca/Profile/encounter#rehabrecheck\"><valueCoding><system value=\"urn:tri:qcpr:rehab_recheck_codes\"/><code value=\"N\"/></valueCoding></extension><text><status value=\"empty\"/><div xmlns=\"http://www.w3.org/1999/xhtml\">No narrative template available for resource profile: http://fhir.connectinggta.ca/Profile/encounter</div></text><contained><Organization xmlns=\"http://hl7.org/fhir\" id=\"1\"><name value=\"General Internal Medicine\"/></Organization></contained><identifier><use value=\"official\"/><label value=\"UHN Visit Number 11410000159\"/><system value=\"urn:oid:1.3.6.1.4.1.12201.2\"/><value value=\"11410000159\"/></identifier><status value=\"finished\"/><class value=\"I\"/><subject><reference value=\"Patient/5993715\"/></subject><participant><type><coding><code value=\"attending\"/></coding></type><individual><reference value=\"Practitioner/5738815\"/></individual></participant><participant><type><coding><code value=\"referring\"/></coding></type><individual><reference value=\"Practitioner/5738815\"/></individual></participant><period><start value=\"2014-08-05T11:59:00-04:00\"/><end value=\"2014-08-05T11:59:00-04:00\"/></period><reason><coding><display value=\"sick\"/></coding><text value=\"sick\"/></reason><location><location><reference value=\"Location/5994269\"/></location></location><serviceProvider><reference value=\"#1\"/></serviceProvider></Encounter></content></entry><entry><title>Patient 5993715</title><id>http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2/Patient/5993715</id><published>2014-08-08T14:46:16-04:00</published><link rel=\"self\" href=\"http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2/Patient/5993715\"/><content type=\"text/xml\"><Patient xmlns=\"http://hl7.org/fhir\"><text><status value=\"generated\"/><div xmlns=\"http://www.w3.org/1999/xhtml\"><div class=\"hapiHeaderText\"> Person <b>CHA </b></div><table class=\"hapiPropertyTable\"><tbody><tr><td>Identifier</td><td>UHN MRN 7018614</td></tr><tr><td>Address</td><td><span>100 Dundas street west </span><br/><span>Toronto </span><span>ON </span><span>Can </span></td></tr><tr><td>Date of birth</td><td><span>01 January 1988</span></td></tr></tbody></table></div></text><identifier><use value=\"official\"/><label value=\"UHN MRN 7018614\"/><system value=\"urn:oid:2.16.840.1.113883.3.239.18.148\"/><value value=\"7018614\"/><assigner><reference value=\"Organization/1.3.6.1.4.1.12201\"/></assigner></identifier><identifier><use value=\"secondary\"/><label value=\"OHIP 2341213425\"/><system value=\"urn:oid:2.16.840.1.113883.4.595\"/><value value=\"2341213425\"/></identifier><name><family value=\"Cha\"/><given value=\"Person\"/></name><telecom><system value=\"phone\"/><value value=\"(416)221-4324\"/><use value=\"home\"/></telecom><telecom><system value=\"phone\"/><use value=\"work\"/></telecom><telecom><system value=\"phone\"/><use value=\"mobile\"/></telecom><telecom><system value=\"email\"/><use value=\"home\"/></telecom><gender><coding><system value=\"http://hl7.org/fhir/v3/AdministrativeGender\"/><code value=\"F\"/></coding></gender><birthDate value=\"1988-01-01T00:00:00\"/><address><use value=\"home\"/><line value=\"100 Dundas street west\"/><city value=\"Toronto\"/><state value=\"ON\"/><zip value=\"L4A 3M9\"/><country value=\"Can\"/></address><managingOrganization><reference value=\"Organization/1.3.6.1.4.1.12201\"/></managingOrganization></Patient></content></entry><entry><title>Practitioner Practitioner/5738815</title><id>http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2/Practitioner/5738815</id><updated>2014-08-08T13:53:52.000-04:00</updated><published>2009-12-04T13:43:11.000-05:00</published><link rel=\"self\" href=\"http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2/Practitioner/5738815\"/><content type=\"text/xml\"><Practitioner xmlns=\"http://hl7.org/fhir\"><text><status value=\"empty\"/><div xmlns=\"http://www.w3.org/1999/xhtml\">No narrative template available for resource profile: http://hl7.org/fhir/profiles/Practitioner</div></text><identifier><system value=\"urn:uhn:qcpr:user_ids\"/><value value=\"5186\"/></identifier><name><family value=\"Generic\"/><given value=\"Physician\"/></name></Practitioner></content></entry><entry><title>Location Location/5994269</title><id>http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2/Location/5994269</id><published>2014-08-08T14:46:16-04:00</published><link rel=\"self\" href=\"http://uhnvesb01d.uhn.on.ca:25180/uhn-fhir-service-1.2/Location/5994269\"/><content type=\"text/xml\"><Location xmlns=\"http://hl7.org/fhir\"><text><status value=\"empty\"/><div xmlns=\"http://www.w3.org/1999/xhtml\">No narrative template available for resource profile: http://hl7.org/fhir/profiles/Location</div></text><name value=\"ES14 414 2\"/><type><coding><code value=\"bed\"/></coding></type></Location></content></entry></feed>";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(ourHttpClient.execute(capt.capture())).thenReturn(ourHttpResponse);
		when(ourHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(ourHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_ATOM_XML + "; charset=UTF-8"));
		when(ourHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(retVal), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		List<Encounter> found = client.search();
		assertEquals(1, found.size());

		Encounter encounter = found.get(0);
		assertNotNull(encounter.getSubject().getResource());
	}

	private interface ITestClient extends IBasicClient {

		@Search
		List<Encounter> search();

		@Search
		List<Encounter> searchByList(@RequiredParam(name = Encounter.SP_IDENTIFIER) TokenOrListParam tokenOrListParam, @IncludeParam Set<Include> theIncludes) throws BaseServerResponseException;

	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
