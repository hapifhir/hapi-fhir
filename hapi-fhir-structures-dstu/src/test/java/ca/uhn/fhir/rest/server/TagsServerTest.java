package ca.uhn.fhir.rest.server;

import static org.apache.commons.lang.StringUtils.defaultString;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.AddTags;
import ca.uhn.fhir.rest.annotation.DeleteTags;
import ca.uhn.fhir.rest.annotation.GetTags;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.TagListParam;
import ca.uhn.fhir.util.PortUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class TagsServerTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx;
	private static String ourLastOutcome;
	private static TagList ourLastTagList;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TagsServerTest.class);
	private static int ourPort;
	private static DummyProvider ourProvider;
	private static Server ourServer;

	@Before
	public void before() {
		ourLastOutcome = null;
		ourLastTagList = null;
	}

	@Test
	public void testAddTagsById() throws Exception {

		TagList tagList = new TagList();
		tagList.addTag("scheme", "term", "label");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/111/_tags");
		httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeTagListToString(tagList), ContentType.create(EncodingEnum.JSON.getResourceContentType(), "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("add111", ourLastOutcome);
		assertEquals(tagList, ourLastTagList);
	}

	@Test
	public void testAddTagsByIdAndVersion() throws Exception {

		TagList tagList = new TagList();
		tagList.addTag("scheme", "term", "label");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/111/_history/222/_tags");
		httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeTagListToString(tagList), ContentType.create(EncodingEnum.JSON.getResourceContentType(), "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("add111222", ourLastOutcome);
		assertEquals(tagList, ourLastTagList);
	}


	@Test
	public void testGetAllTags() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/_tags");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		TagList tagList = ourCtx.newXmlParser().parseTagList(responseContent);
		assertEquals(ourProvider.getAllTags(), tagList);
	}

	@Test
	public void testGetAllTagsPatient() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/_tags");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());

		TagList actual = ourCtx.newXmlParser().parseTagList(responseContent);
		
		TagList expected = new TagList();
		expected.addTag(null, "Patient", "DogLabel");
		expected.addTag("http://cats", "AllCat", "CatLabel");
		assertEquals(expected, actual);
	}

	@Test
	public void testGetAllTagsPatientId() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/111/_tags");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());

		TagList actual = ourCtx.newXmlParser().parseTagList(responseContent);

		TagList expected = new TagList();
		expected.addTag(null, "Patient111", "DogLabel");
		expected.addTag("http://cats", "AllCat", "CatLabel");
		assertEquals(expected, actual);
	}

	@Test
	public void testGetAllTagsPatientIdVersion() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/111/_history/222/_tags");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());

		TagList actual = ourCtx.newXmlParser().parseTagList(responseContent);
		TagList expected = new TagList();
		expected.addTag(null, "Patient111222", "DogLabel");
		expected.addTag("http://cats", "AllCat", "CatLabel");
		assertEquals(expected, actual);
	}

	@Test
	public void testGetAllTagsObservationIdVersion() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/111/_history/222/_tags");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());

		TagList actual = ourCtx.newXmlParser().parseTagList(responseContent);
		TagList expected = new TagList();
		expected.addTag(null, "Patient111222", "DogLabel");
		expected.addTag("http://cats", "AllCat", "CatLabel");
		assertEquals(expected, actual);
	}
	
	@Test
	public void testRemoveTagsById() throws Exception {

		TagList tagList = new TagList();
		tagList.addTag("scheme", "term", "label");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/111/_tags/_delete");
		httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeTagListToString(tagList), ContentType.create(EncodingEnum.JSON.getResourceContentType(), "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("Remove111", ourLastOutcome);
		assertEquals(tagList, ourLastTagList);
	}

	@Test
	public void testRemoveTagsByIdAndVersion() throws Exception {

		TagList tagList = new TagList();
		tagList.addTag("scheme", "term", "label");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/111/_history/222/_tags/_delete");
		httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeTagListToString(tagList), ContentType.create(EncodingEnum.JSON.getResourceContentType(), "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("Remove111222", ourLastOutcome);
		assertEquals(tagList, ourLastTagList);
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);
		ourCtx = new FhirContext(Patient.class);

		ourProvider = new DummyProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setPlainProviders(ourProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	public static class DummyProvider {

		@AddTags(type = Patient.class)
		public void addTagsPatient(@IdParam IdDt theId, @TagListParam TagList theTagList) {
			ourLastOutcome = "add" + theId.getIdPart() + StringUtils.defaultString(theId.getVersionIdPart());
			ourLastTagList=theTagList;
		}

		@GetTags
		public TagList getAllTags() {
			TagList tagList = new TagList();
			tagList.add(new Tag((String) null, "AllDog", "DogLabel"));
			tagList.add(new Tag("http://cats", "AllCat", "CatLabel"));
			return tagList;
		}

		@GetTags(type = Patient.class)
		public TagList getAllTagsPatient() {
			TagList tagList = new TagList();
			tagList.add(new Tag((String) null, "Patient", "DogLabel"));
			tagList.add(new Tag("http://cats", "AllCat", "CatLabel"));
			return tagList;
		}

		@GetTags(type = Patient.class)
		public TagList getAllTagsPatientId(@IdParam IdDt theId) {
			TagList tagList = new TagList();
			tagList.add(new Tag((String) null, "Patient" + theId.getIdPart() + defaultString(theId.getVersionIdPart()), "DogLabel"));
			tagList.add(new Tag("http://cats", "AllCat", "CatLabel"));
			return tagList;
		}

//		@GetTags(type = Patient.class)
//		public TagList getAllTagsPatientIdVersion(@IdParam IdDt theId, @VersionIdParam IdDt theVersion) {
//			TagList tagList = new TagList();
//			tagList.add(new Tag((String) null, "Patient" + theId.getIdPart() + theVersion.getVersionIdPart(), "DogLabel"));
//			tagList.add(new Tag("http://cats", "AllCat", "CatLabel"));
//			return tagList;
//		}

		@GetTags(type = Observation.class)
		public TagList getAllTagsObservationIdVersion(@IdParam IdDt theId) {
			TagList tagList = new TagList();
			tagList.add(new Tag((String) null, "Patient" + theId.getIdPart() + theId.getVersionIdPart(), "DogLabel"));
			tagList.add(new Tag("http://cats", "AllCat", "CatLabel"));
			return tagList;
		}

//		@DeleteTags(type = Patient.class)
//		public void RemoveTagsPatient(@IdParam IdDt theId, @VersionIdParam IdDt theVersion, @TagListParam TagList theTagList) {
//			ourLastOutcome = "Remove" + theId.getIdPart() + theVersion.getVersionIdPart();
//			ourLastTagList=theTagList;
//		}

		@DeleteTags(type = Patient.class)
		public void RemoveTagsPatient(@IdParam IdDt theId, @TagListParam TagList theTagList) {
			ourLastOutcome = "Remove" + theId.getIdPart()+StringUtils.defaultString(theId.getVersionIdPart());
			ourLastTagList=theTagList;
		}

	}

}
