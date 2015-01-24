package ca.uhn.fhir.rest.client;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.StringReader;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.AddTags;
import ca.uhn.fhir.rest.annotation.DeleteTags;
import ca.uhn.fhir.rest.annotation.GetTags;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.TagListParam;
import ca.uhn.fhir.rest.annotation.VersionIdParam;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.server.Constants;

public class TagsClientTest {

	private FhirContext ctx;
	private HttpClient httpClient;
	private HttpResponse httpResponse;

	// atom-document-large.xml

	@Before
	public void before() {
		ctx = new FhirContext(Patient.class, Conformance.class);

		httpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ctx.getRestfulClientFactory().setHttpClient(httpClient);
		ctx.getRestfulClientFactory().setServerValidationModeEnum(ServerValidationModeEnum.NEVER);

		httpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	}

	@Test
	public void testGetAllTags() throws Exception {

		TagList tagList = new TagList();
		tagList.add(new Tag("CCC", "AAA", "BBB"));
		String ser = ctx.newXmlParser().encodeTagListToString(tagList);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(ser), Charset.forName("UTF-8")));

		IClient client = ctx.newRestfulClient(IClient.class, "http://foo");
		TagList response = client.getAllTags();
		assertEquals(tagList, response);

		assertEquals(HttpGet.class, capt.getValue().getClass());
		HttpGet get = (HttpGet) capt.getValue();
		assertEquals("http://foo/_tags", get.getURI().toString());
	}

	@Test
	public void testGetAllTagsPatient() throws Exception {

		TagList tagList = new TagList();
		tagList.add(new Tag("CCC", "AAA", "BBB"));
		String ser = ctx.newXmlParser().encodeTagListToString(tagList);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(ser), Charset.forName("UTF-8")));

		IClient client = ctx.newRestfulClient(IClient.class, "http://foo");
		TagList response = client.getAllTagsPatient();
		assertEquals(tagList, response);

		assertEquals(HttpGet.class, capt.getValue().getClass());
		HttpGet get = (HttpGet) capt.getValue();
		assertEquals("http://foo/Patient/_tags", get.getURI().toString());
	}

	@Test
	public void testDeleteTagsPatient() throws Exception {

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType().getElements()).thenReturn(new HeaderElement[0]);
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));

		TagList tagList = new TagList();
		tagList.add(new Tag("CCC", "AAA", "BBB"));

		IClient client = ctx.newRestfulClient(IClient.class, "http://foo");
		client.deleteTags(new IdDt("111"), tagList);

		assertEquals(HttpPost.class, capt.getValue().getClass());
		HttpPost post = (HttpPost) capt.getValue();
		assertEquals("http://foo/Patient/111/_tags/_delete", post.getURI().toString());

		String ser = IOUtils.toString(post.getEntity().getContent());
		TagList actualTagList = ctx.newXmlParser().parseTagList(ser);
		assertEquals(tagList, actualTagList);
	}

	@Test
	public void testDeleteTagsPatientVersion() throws Exception {

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType().getElements()).thenReturn(new HeaderElement[0]);
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));

		TagList tagList = new TagList();
		tagList.add(new Tag("CCC", "AAA", "BBB"));

		IClient client = ctx.newRestfulClient(IClient.class, "http://foo");
		client.deleteTags(new IdDt("111"), new IdDt("222"),tagList);

		assertEquals(HttpPost.class, capt.getValue().getClass());
		HttpPost post = (HttpPost) capt.getValue();
		assertEquals("http://foo/Patient/111/_history/222/_tags/_delete", post.getURI().toString());

		String ser = IOUtils.toString(post.getEntity().getContent());
		TagList actualTagList = ctx.newXmlParser().parseTagList(ser);
		assertEquals(tagList, actualTagList);
	}

	@Test
	public void testAddTagsPatient() throws Exception {

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType().getElements()).thenReturn(new HeaderElement[0]);
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));

		TagList tagList = new TagList();
		tagList.add(new Tag("CCC", "AAA", "BBB"));

		IClient client = ctx.newRestfulClient(IClient.class, "http://foo");
		client.addTags(new IdDt("111"), tagList);

		assertEquals(HttpPost.class, capt.getValue().getClass());
		HttpPost post = (HttpPost) capt.getValue();
		assertEquals("http://foo/Patient/111/_tags", post.getURI().toString());

		String ser = IOUtils.toString(post.getEntity().getContent());
		TagList actualTagList = ctx.newXmlParser().parseTagList(ser);
		assertEquals(tagList, actualTagList);
	}

	@Test
	public void testAddTagsPatientVersion() throws Exception {

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType().getElements()).thenReturn(new HeaderElement[0]);
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));

		TagList tagList = new TagList();
		tagList.add(new Tag("CCC", "AAA", "BBB"));

		IClient client = ctx.newRestfulClient(IClient.class, "http://foo");
		client.addTags(new IdDt("111"), new IdDt("222"),tagList);

		assertEquals(HttpPost.class, capt.getValue().getClass());
		HttpPost post = (HttpPost) capt.getValue();
		assertEquals("http://foo/Patient/111/_history/222/_tags", post.getURI().toString());

		String ser = IOUtils.toString(post.getEntity().getContent());
		TagList actualTagList = ctx.newXmlParser().parseTagList(ser);
		assertEquals(tagList, actualTagList);
	}

	@Test
	public void testGetAllTagsPatientId() throws Exception {

		TagList tagList = new TagList();
		tagList.add(new Tag("CCC", "AAA", "BBB"));
		String ser = ctx.newXmlParser().encodeTagListToString(tagList);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(ser), Charset.forName("UTF-8")));

		IClient client = ctx.newRestfulClient(IClient.class, "http://foo");
		TagList response = client.getAllTagsPatientId(new IdDt("111"));
		assertEquals(tagList, response);

		assertEquals(HttpGet.class, capt.getValue().getClass());
		HttpGet get = (HttpGet) capt.getValue();
		assertEquals("http://foo/Patient/111/_tags", get.getURI().toString());
	}

	@Test
	public void testGetAllTagsPatientIdVersion() throws Exception {

		TagList tagList = new TagList();
		tagList.add(new Tag("CCC", "AAA", "BBB"));
		String ser = ctx.newXmlParser().encodeTagListToString(tagList);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(ser), Charset.forName("UTF-8")));

		IClient client = ctx.newRestfulClient(IClient.class, "http://foo");
		TagList response = client.getAllTagsPatientId(new IdDt("Patient", "111", "222"));
		assertEquals(tagList, response);

		assertEquals(HttpGet.class, capt.getValue().getClass());
		HttpGet get = (HttpGet) capt.getValue();
		assertEquals("http://foo/Patient/111/_history/222/_tags", get.getURI().toString());
	}

	@Test
	public void testGetAllTagsPatientIdVersionOld() throws Exception {

		TagList tagList = new TagList();
		tagList.add(new Tag("CCC", "AAA", "BBB"));
		String ser = ctx.newXmlParser().encodeTagListToString(tagList);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(ser), Charset.forName("UTF-8")));

		IClient client = ctx.newRestfulClient(IClient.class, "http://foo");
		TagList response = client.getAllTagsPatientIdVersion(new IdDt("111"), new IdDt("222"));
		assertEquals(tagList, response);

		assertEquals(HttpGet.class, capt.getValue().getClass());
		HttpGet get = (HttpGet) capt.getValue();
		assertEquals("http://foo/Patient/111/_history/222/_tags", get.getURI().toString());
	}

	private interface IClient extends IBasicClient {

		@GetTags
		public TagList getAllTags();

		@GetTags(type = Patient.class)
		public TagList getAllTagsPatient();

		@GetTags(type = Patient.class)
		public TagList getAllTagsPatientId(@IdParam IdDt theId);

		@GetTags(type = Patient.class)
		public TagList getAllTagsPatientIdVersion(@IdParam IdDt theId, @VersionIdParam IdDt theVersion);

		@AddTags(type = Patient.class)
		public void addTags(@IdParam IdDt theId, @TagListParam TagList theTagList);

		@AddTags(type = Patient.class)
		public void addTags(@IdParam IdDt theId, @VersionIdParam IdDt theVersionId, @TagListParam TagList theTagList);

		@DeleteTags(type = Patient.class)
		public void deleteTags(@IdParam IdDt theId, @TagListParam TagList theTagList);

		@DeleteTags(type = Patient.class)
		public void deleteTags(@IdParam IdDt theId, @VersionIdParam IdDt theVersionId, @TagListParam TagList theTagList);

	}

}
