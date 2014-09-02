package ca.uhn.fhir.rest.server;

import static org.apache.commons.lang.StringUtils.defaultString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
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
import ca.uhn.fhir.rest.annotation.VersionIdParam;
import ca.uhn.fhir.testutil.RandomServerPortProvider;


/**
 * Created by dsotnikov on 2/25/2014.
 */
public class TagsServerTest
{


  private static CloseableHttpClient ourClient;

  private static FhirContext ourCtx;

  private static String ourLastOutcome;

  private static TagList ourLastTagList;

  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TagsServerTest.class);

  private static int ourPort;

  private static DummyProvider ourProvider;

  private static Server ourServer;



  @Before
  public void before()
  {
    ourLastOutcome = null;
    ourLastTagList = null;
  }



  @Test
  public void testAddTagsById() throws Exception
  {

    final TagList tagList = new TagList();
    tagList.addTag("scheme", "term", "label");

    final HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/111/_tags");
    httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeTagListToString(tagList), ContentType.create(
        EncodingEnum.JSON.getResourceContentType(), "UTF-8")));
    final HttpResponse status = ourClient.execute(httpPost);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    assertEquals("add111", ourLastOutcome);
    assertEquals(tagList, ourLastTagList);
  }



  @Test
  public void testAddTagsByIdAndVersion() throws Exception
  {

    final TagList tagList = new TagList();
    tagList.addTag("scheme", "term", "label");

    final HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/111/_history/222/_tags");
    httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeTagListToString(tagList), ContentType.create(
        EncodingEnum.JSON.getResourceContentType(), "UTF-8")));
    final HttpResponse status = ourClient.execute(httpPost);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    assertEquals("add111222", ourLastOutcome);
    assertEquals(tagList, ourLastTagList);
  }



  @Test
  public void testEquals()
  {
    TagList list1 = ourProvider.getAllTagsPatient();
    TagList list2 = ourProvider.getAllTagsPatient();
    assertEquals(list1, list2);

    list1 = ourProvider.getAllTagsPatient();
    list2 = ourProvider.getAllTagsPatient();
    list2.get(0).setTerm("!!!!!");
    assertNotEquals(list1, list2);
  }



  @Test
  public void testGetAllTags() throws Exception
  {

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/_tags");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    final TagList tagList = ourCtx.newXmlParser().parseTagList(responseContent);
    assertEquals(ourProvider.getAllTags(), tagList);
  }



  @Test
  public void testGetAllTagsPatient() throws Exception
  {

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/_tags");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());

    final TagList actual = ourCtx.newXmlParser().parseTagList(responseContent);
    final TagList expected = ourProvider.getAllTags();
    expected.get(0).setTerm("Patient");
    assertEquals(expected, actual);
  }



  // @Test
  public void testGetAllTagsPatientId() throws Exception
  {

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/111/_tags");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());

    final TagList actual = ourCtx.newXmlParser().parseTagList(responseContent);
    final TagList expected = ourProvider.getAllTags();
    expected.get(0).setTerm("Patient111");
    assertEquals(expected, actual);
  }



  @Test
  public void testGetAllTagsPatientIdVersion() throws Exception
  {

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/111/_history/222/_tags");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());

    final TagList actual = ourCtx.newXmlParser().parseTagList(responseContent);
    final TagList expected = ourProvider.getAllTags();
    expected.get(0).setTerm("Patient111222");
    assertEquals(expected, actual);
  }



  @Test
  public void testGetAllTagsObservationIdVersion() throws Exception
  {

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/111/_history/222/_tags");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());

    final TagList actual = ourCtx.newXmlParser().parseTagList(responseContent);
    final TagList expected = ourProvider.getAllTags();
    expected.get(0).setTerm("Patient111222");
    assertEquals(expected, actual);
  }



  @Test
  public void testRemoveTagsById() throws Exception
  {

    final TagList tagList = new TagList();
    tagList.addTag("scheme", "term", "label");

    final HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/111/_tags/_delete");
    httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeTagListToString(tagList), ContentType.create(
        EncodingEnum.JSON.getResourceContentType(), "UTF-8")));
    final HttpResponse status = ourClient.execute(httpPost);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    assertEquals("Remove111", ourLastOutcome);
    assertEquals(tagList, ourLastTagList);
  }



  @Test
  public void testRemoveTagsByIdAndVersion() throws Exception
  {

    final TagList tagList = new TagList();
    tagList.addTag("scheme", "term", "label");

    final HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/111/_history/222/_tags/_delete");
    httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeTagListToString(tagList), ContentType.create(
        EncodingEnum.JSON.getResourceContentType(), "UTF-8")));
    final HttpResponse status = ourClient.execute(httpPost);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    assertEquals("Remove111222", ourLastOutcome);
    assertEquals(tagList, ourLastTagList);
  }



  @AfterClass
  public static void afterClass() throws Exception
  {
    ourServer.stop();
  }



  @BeforeClass
  public static void beforeClass() throws Exception
  {
    ourPort = RandomServerPortProvider.findFreePort();
    ourServer = new Server(ourPort);
    ourCtx = new FhirContext(Patient.class);

    ourProvider = new DummyProvider();

    final ServletHandler proxyHandler = new ServletHandler();
    final RestfulServer servlet = new RestfulServer();
    servlet.setPlainProviders(ourProvider);
    final ServletHolder servletHolder = new ServletHolder(servlet);
    proxyHandler.addServletWithMapping(servletHolder, "/*");
    ourServer.setHandler(proxyHandler);
    ourServer.start();

    final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000,
        TimeUnit.MILLISECONDS);
    final HttpClientBuilder builder = HttpClientBuilder.create();
    builder.setConnectionManager(connectionManager);
    ourClient = builder.build();

  }





  public static class DummyProvider
  {


    @AddTags(type = Patient.class)
    public void addTagsPatient(@IdParam final IdDt theId, @VersionIdParam final IdDt theVersion,
        @TagListParam final TagList theTagList)
    {
      ourLastOutcome = "add" + theId.getIdPart() + theVersion.getVersionIdPart();
      ourLastTagList = theTagList;
    }



    @AddTags(type = Patient.class)
    public void addTagsPatient(@IdParam final IdDt theId, @TagListParam final TagList theTagList)
    {
      ourLastOutcome = "add" + theId.getIdPart();
      ourLastTagList = theTagList;
    }



    @GetTags
    public TagList getAllTags()
    {
      final TagList tagList = new TagList();
      tagList.add(new Tag((String) null, "AllDog", "DogLabel"));
      tagList.add(new Tag("http://cats", "AllCat", "CatLabel"));
      return tagList;
    }



    @GetTags(type = Patient.class)
    public TagList getAllTagsPatient()
    {
      final TagList tagList = new TagList();
      tagList.add(new Tag((String) null, "Patient", "DogLabel"));
      tagList.add(new Tag("http://cats", "AllCat", "CatLabel"));
      return tagList;
    }



    @GetTags(type = Patient.class)
    public TagList getAllTagsPatientId(@IdParam final IdDt theId)
    {
      final TagList tagList = new TagList();
      tagList.add(new Tag((String) null, "Patient" + theId.getIdPart() + defaultString(theId.getVersionIdPart()),
          "DogLabel"));
      tagList.add(new Tag("http://cats", "AllCat", "CatLabel"));
      return tagList;
    }



    @GetTags(type = Patient.class)
    public TagList getAllTagsPatientIdVersion(@IdParam final IdDt theId, @VersionIdParam final IdDt theVersion)
    {
      final TagList tagList = new TagList();
      tagList.add(new Tag((String) null, "Patient" + theId.getIdPart() + theVersion.getVersionIdPart(), "DogLabel"));
      tagList.add(new Tag("http://cats", "AllCat", "CatLabel"));
      return tagList;
    }



    @GetTags(type = Observation.class)
    public TagList getAllTagsObservationIdVersion(@IdParam final IdDt theId)
    {
      final TagList tagList = new TagList();
      tagList.add(new Tag((String) null, "Patient" + theId.getIdPart() + theId.getVersionIdPart(), "DogLabel"));
      tagList.add(new Tag("http://cats", "AllCat", "CatLabel"));
      return tagList;
    }



    @DeleteTags(type = Patient.class)
    public void RemoveTagsPatient(@IdParam final IdDt theId, @VersionIdParam final IdDt theVersion,
        @TagListParam final TagList theTagList)
    {
      ourLastOutcome = "Remove" + theId.getIdPart() + theVersion.getVersionIdPart();
      ourLastTagList = theTagList;
    }



    @DeleteTags(type = Patient.class)
    public void RemoveTagsPatient(@IdParam final IdDt theId, @TagListParam final TagList theTagList)
    {
      ourLastOutcome = "Remove" + theId.getIdPart();
      ourLastTagList = theTagList;
    }

  }

}
