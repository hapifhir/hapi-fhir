package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hamcrest.core.StringContains;
import org.hamcrest.core.StringEndsWith;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.resource.AdverseReaction;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.CodingListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.provider.ServerProfileProvider;
import ca.uhn.fhir.testutil.RandomServerPortProvider;


/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ResfulServerMethodTest
{


  private static CloseableHttpClient ourClient;

  private static FhirContext ourCtx;

  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResfulServerMethodTest.class);

  private static int ourPort;

  private static DummyDiagnosticReportResourceProvider ourReportProvider;

  private static Server ourServer;



  @Test
  public void test404IsPropagatedCorrectly() throws Exception
  {

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/DiagnosticReport?throw404=true");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(404, status.getStatusLine().getStatusCode());
    assertThat(responseContent, StringContains.containsString("AAAABBBB"));
  }



  @Test
  public void testDateRangeParam() throws Exception
  {

    // HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
    // "/Patient/1");
    // httpPost.setEntity(new StringEntity("test",
    // ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort
        + "/Patient?dateRange=%3E%3D2011-01-01&dateRange=%3C%3D2021-01-01");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());

    final Patient patient = (Patient) ourCtx.newXmlParser().parseBundle(responseContent).getEntries().get(0)
        .getResource();
    assertEquals(">=2011-01-01", patient.getName().get(0).getSuffix().get(0).getValue());
    assertEquals("<=2021-01-01", patient.getName().get(0).getSuffix().get(1).getValue());

  }



  @Test
  public void testDelete() throws Exception
  {

    final HttpDelete httpGet = new HttpDelete("http://localhost:" + ourPort + "/Patient/1234");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());

    final OperationOutcome patient = ourCtx.newXmlParser().parseResource(OperationOutcome.class, responseContent);
    assertEquals("Patient/1234", patient.getIssueFirstRep().getDetails().getValue());

  }



  @Test
  public void testDeleteNoResponse() throws Exception
  {

    // HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
    // "/Patient/1");
    // httpPost.setEntity(new StringEntity("test",
    // ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

    final HttpDelete httpGet = new HttpDelete("http://localhost:" + ourPort + "/DiagnosticReport/1234");
    final HttpResponse status = ourClient.execute(httpGet);

    assertEquals(204, status.getStatusLine().getStatusCode());

  }



  @Test
  public void testEntryLinkSelf() throws Exception
  {

    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort
        + "/Patient?withIncludes=include1&_include=include2&_include=include3");
    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
    BundleEntry entry0 = bundle.getEntries().get(0);
    assertEquals("http://localhost:" + ourPort + "/Patient/1", entry0.getLinkSelf().getValue());
    assertEquals("http://localhost:" + ourPort + "/Patient/1", entry0.getId().getValue());

    httpGet = new HttpGet("http://localhost:" + ourPort
        + "/Patient?withIncludes=include1&_include=include2&_include=include3&_format=json");
    status = ourClient.execute(httpGet);
    responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    assertEquals(200, status.getStatusLine().getStatusCode());
    ourLog.info(responseContent);
    bundle = ourCtx.newJsonParser().parseBundle(responseContent);
    entry0 = bundle.getEntries().get(0);
    // Should not include params such as _format=json
    assertEquals("http://localhost:" + ourPort + "/Patient/1", entry0.getLinkSelf().getValue());

  }



  @Test
  public void testFormatParamJson() throws Exception
  {

    // HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
    // "/Patient/1");
    // httpPost.setEntity(new StringEntity("test",
    // ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=json");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());

    final Patient patient = (Patient) ourCtx.newJsonParser().parseResource(responseContent);
    // assertEquals("PatientOne",
    // patient.getName().get(0).getGiven().get(0).getValue());

    ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient));

  }



  @Test
  public void testFormatParamXml() throws Exception
  {

    // HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
    // "/Patient/1");
    // httpPost.setEntity(new StringEntity("test",
    // ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=xml");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    final Patient patient = (Patient) ourCtx.newXmlParser().parseResource(responseContent);
    assertEquals("PatientOne", patient.getName().get(0).getGiven().get(0).getValue());

  }



  // @Test
  public void testGetById() throws Exception
  {

    // HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
    // "/Patient/1");
    // httpPost.setEntity(new StringEntity("test",
    // ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
    HttpResponse status = ourClient.execute(httpGet);

    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    Patient patient = (Patient) ourCtx.newXmlParser().parseResource(responseContent);
    assertEquals("PatientOne", patient.getName().get(0).getGiven().get(0).getValue());

    /*
     * Different ID
     */

    httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2");
    status = ourClient.execute(httpGet);

    responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.debug("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    patient = (Patient) ourCtx.newXmlParser().parseResource(responseContent);
    assertEquals("PatientTwo", patient.getName().get(0).getGiven().get(0).getValue());

    /*
     * Bad ID
     */

    httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/9999999");
    status = ourClient.execute(httpGet);

    responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.debug("Response was:\n{}", responseContent);

    assertEquals(404, status.getStatusLine().getStatusCode());

  }



  @Test
  public void testGetByVersionId() throws Exception
  {

    // HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
    // "/Patient/1");
    // httpPost.setEntity(new StringEntity("test",
    // ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/_history/999");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    final Patient patient = (Patient) ourCtx.newXmlParser().parseResource(responseContent);
    assertEquals("PatientOne", patient.getName().get(0).getGiven().get(0).getValue());
    assertEquals("999", patient.getName().get(0).getText().getValue());

  }



  @Test
  public void testGetMetadata() throws Exception
  {

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/metadata");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    // ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    final IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);
    final Conformance bundle = parser.parseResource(Conformance.class, responseContent);

    {
      IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
      String enc = p.encodeResourceToString(bundle);
      ourLog.info("Response:\n{}", enc);

      p = ourCtx.newXmlParser().setPrettyPrint(false);
      enc = p.encodeResourceToString(bundle);
      ourLog.info("Response:\n{}", enc);
      assertThat(enc, StringContains.containsString("<name value=\"quantityParam\"/><type value=\"quantity\"/>"));
    }
    // {
    // IParser p = ourCtx.newJsonParser().setPrettyPrint(true);
    //
    // p.encodeResourceToWriter(bundle, new OutputStreamWriter(System.out));
    //
    // String enc = p.encodeResourceToString(bundle);
    // ourLog.info("Response:\n{}", enc);
    // assertTrue(enc.contains(ExtensionConstants.CONF_ALSO_CHAIN));
    //
    // }
  }



  @Test
  public void testHistoryFailsIfResourcesAreIncorrectlyPopulated() throws Exception
  {
    {
      final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/999/_history");
      final HttpResponse status = ourClient.execute(httpGet);

      final String responseContent = IOUtils.toString(status.getEntity().getContent());
      IOUtils.closeQuietly(status.getEntity().getContent());

      ourLog.info("Response was:\n{}", responseContent);

      assertEquals(500, status.getStatusLine().getStatusCode());
    }
    {
      final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/998/_history");
      final HttpResponse status = ourClient.execute(httpGet);

      final String responseContent = IOUtils.toString(status.getEntity().getContent());
      IOUtils.closeQuietly(status.getEntity().getContent());

      ourLog.info("Response was:\n{}", responseContent);

      assertEquals(500, status.getStatusLine().getStatusCode());
    }
  }



  // @Test
  // public void testSearchByComplex() throws Exception {
  //
  // HttpGet httpGet = new HttpGet("http://localhost:" + ourPort +
  // "/Patient?Patient.identifier=urn:oid:2.16.840.1.113883.3.239.18.148%7C7000135&name=urn:oid:1.3.6.1.4.1.12201.102.5%7C522&date=");
  // HttpResponse status = ourClient.execute(httpGet);
  //
  // String responseContent =
  // IOUtils.toString(status.getEntity().getContent());
  // ourLog.info("Response was:\n{}", responseContent);
  //
  // assertEquals(200, status.getStatusLine().getStatusCode());
  // }

  @Test
  public void testHistoryResourceInstance() throws Exception
  {

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/222/_history");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    final Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

    assertEquals(2, bundle.getEntries().size());

    // Older resource
    {
      final BundleEntry olderEntry = bundle.getEntries().get(0);
      assertEquals("http://localhost:" + ourPort + "/Patient/222", olderEntry.getId().getValue());
      assertEquals("http://localhost:" + ourPort + "/Patient/222/_history/1", olderEntry.getLinkSelf().getValue());
      final InstantDt pubExpected = new InstantDt(new Date(10000L));
      final InstantDt pubActualRes = (InstantDt) olderEntry.getResource().getResourceMetadata()
          .get(ResourceMetadataKeyEnum.PUBLISHED);
      final InstantDt pubActualBundle = olderEntry.getPublished();
      assertEquals(pubExpected.getValueAsString(), pubActualRes.getValueAsString());
      assertEquals(pubExpected.getValueAsString(), pubActualBundle.getValueAsString());
      final InstantDt updExpected = new InstantDt(new Date(20000L));
      final InstantDt updActualRes = (InstantDt) olderEntry.getResource().getResourceMetadata()
          .get(ResourceMetadataKeyEnum.UPDATED);
      final InstantDt updActualBundle = olderEntry.getUpdated();
      assertEquals(updExpected.getValueAsString(), updActualRes.getValueAsString());
      assertEquals(updExpected.getValueAsString(), updActualBundle.getValueAsString());
    }
    // Newer resource
    {
      final BundleEntry newerEntry = bundle.getEntries().get(1);
      assertEquals("http://localhost:" + ourPort + "/Patient/222", newerEntry.getId().getValue());
      assertEquals("http://localhost:" + ourPort + "/Patient/222/_history/2", newerEntry.getLinkSelf().getValue());
      final InstantDt pubExpected = new InstantDt(new Date(10000L));
      final InstantDt pubActualRes = (InstantDt) newerEntry.getResource().getResourceMetadata()
          .get(ResourceMetadataKeyEnum.PUBLISHED);
      final InstantDt pubActualBundle = newerEntry.getPublished();
      assertEquals(pubExpected.getValueAsString(), pubActualRes.getValueAsString());
      assertEquals(pubExpected.getValueAsString(), pubActualBundle.getValueAsString());
      final InstantDt updExpected = new InstantDt(new Date(30000L));
      final InstantDt updActualRes = (InstantDt) newerEntry.getResource().getResourceMetadata()
          .get(ResourceMetadataKeyEnum.UPDATED);
      final InstantDt updActualBundle = newerEntry.getUpdated();
      assertEquals(updExpected.getValueAsString(), updActualRes.getValueAsString());
      assertEquals(updExpected.getValueAsString(), updActualBundle.getValueAsString());
    }

  }



  @Test
  public void testHistoryResourceType() throws Exception
  {

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/_history");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    final Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

    assertEquals(2, bundle.getEntries().size());

    // Older resource
    {
      final BundleEntry olderEntry = bundle.getEntries().get(0);
      assertEquals("http://localhost:" + ourPort + "/Patient/1", olderEntry.getId().getValue());
      assertThat(olderEntry.getLinkSelf().getValue(), StringEndsWith.endsWith("/Patient/1/_history/1"));
      final InstantDt pubExpected = new InstantDt(new Date(10000L));
      final InstantDt pubActualRes = (InstantDt) olderEntry.getResource().getResourceMetadata()
          .get(ResourceMetadataKeyEnum.PUBLISHED);
      final InstantDt pubActualBundle = olderEntry.getPublished();
      assertEquals(pubExpected.getValueAsString(), pubActualRes.getValueAsString());
      assertEquals(pubExpected.getValueAsString(), pubActualBundle.getValueAsString());
      final InstantDt updExpected = new InstantDt(new Date(20000L));
      final InstantDt updActualRes = (InstantDt) olderEntry.getResource().getResourceMetadata()
          .get(ResourceMetadataKeyEnum.UPDATED);
      final InstantDt updActualBundle = olderEntry.getUpdated();
      assertEquals(updExpected.getValueAsString(), updActualRes.getValueAsString());
      assertEquals(updExpected.getValueAsString(), updActualBundle.getValueAsString());
    }
    // Newer resource
    {
      final BundleEntry newerEntry = bundle.getEntries().get(1);
      assertEquals("http://localhost:" + ourPort + "/Patient/1", newerEntry.getId().getValue());
      assertThat(newerEntry.getLinkSelf().getValue(), StringEndsWith.endsWith("/Patient/1/_history/2"));
      final InstantDt pubExpected = new InstantDt(new Date(10000L));
      final InstantDt pubActualRes = (InstantDt) newerEntry.getResource().getResourceMetadata()
          .get(ResourceMetadataKeyEnum.PUBLISHED);
      final InstantDt pubActualBundle = newerEntry.getPublished();
      assertEquals(pubExpected.getValueAsString(), pubActualRes.getValueAsString());
      assertEquals(pubExpected.getValueAsString(), pubActualBundle.getValueAsString());
      final InstantDt updExpected = new InstantDt(new Date(30000L));
      final InstantDt updActualRes = (InstantDt) newerEntry.getResource().getResourceMetadata()
          .get(ResourceMetadataKeyEnum.UPDATED);
      final InstantDt updActualBundle = newerEntry.getUpdated();
      assertEquals(updExpected.getValueAsString(), updActualRes.getValueAsString());
      assertEquals(updExpected.getValueAsString(), updActualBundle.getValueAsString());
    }

  }



  @Test
  public void testReadOnTypeThatDoesntSupportRead() throws Exception
  {

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/AdverseReaction/223");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(Constants.STATUS_HTTP_400_BAD_REQUEST, status.getStatusLine().getStatusCode());

  }



  @Test
  public void testSearchAll() throws Exception
  {

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/AdverseReaction");
    HttpResponse status = ourClient.execute(httpGet);

    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

    assertEquals(2, bundle.getEntries().size());

    final HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/AdverseReaction/_search");
    status = ourClient.execute(httpPost);

    responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    bundle = ourCtx.newXmlParser().parseBundle(responseContent);

    assertEquals(2, bundle.getEntries().size());

  }



  @Test
  public void testSearchAllProfiles() throws Exception
  {

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Profile?");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    // ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    final IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);
    final Bundle bundle = parser.parseBundle(responseContent);

    ourLog.info("Response:\n{}", parser.encodeBundleToString(bundle));

  }



  @Test
  public void testSearchByDob() throws Exception
  {

    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?dob=2011-01-02");
    HttpResponse status = ourClient.execute(httpGet);

    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

    assertEquals(1, bundle.getEntries().size());

    Patient patient = (Patient) bundle.getEntries().get(0).getResource();
    assertEquals("NONE", patient.getIdentifier().get(1).getValue().getValue());
    assertEquals("2011-01-02", patient.getIdentifier().get(2).getValue().getValue());

    /*
     * With comparator
     */

    httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?dob=%3E%3D2011-01-02");
    status = ourClient.execute(httpGet);

    responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    bundle = ourCtx.newXmlParser().parseBundle(responseContent);

    assertEquals(1, bundle.getEntries().size());

    patient = (Patient) bundle.getEntries().get(0).getResource();
    assertEquals(">=", patient.getIdentifier().get(1).getValue().getValue());
    assertEquals("2011-01-02", patient.getIdentifier().get(2).getValue().getValue());

  }



  @Test
  public void testSearchByDobWithSearchActionAndPost() throws Exception
  {

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/_search?dob=2011-01-02");
    HttpResponse status = ourClient.execute(httpGet);

    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

    assertEquals(1, bundle.getEntries().size());

    Patient patient = (Patient) bundle.getEntries().get(0).getResource();
    assertEquals("NONE", patient.getIdentifier().get(1).getValue().getValue());
    assertEquals("2011-01-02", patient.getIdentifier().get(2).getValue().getValue());

    // POST

    HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_search?dob=2011-01-02");
    status = ourClient.execute(httpPost);

    responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    bundle = ourCtx.newXmlParser().parseBundle(responseContent);

    assertEquals(1, bundle.getEntries().size());

    patient = (Patient) bundle.getEntries().get(0).getResource();
    assertEquals("NONE", patient.getIdentifier().get(1).getValue().getValue());
    assertEquals("2011-01-02", patient.getIdentifier().get(2).getValue().getValue());

    // POST with form encoded

    httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_search");
    final List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
    urlParameters.add(new BasicNameValuePair("dob", "2011-01-02"));
    httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
    status = ourClient.execute(httpPost);

    responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    bundle = ourCtx.newXmlParser().parseBundle(responseContent);

    assertEquals(1, bundle.getEntries().size());

    patient = (Patient) bundle.getEntries().get(0).getResource();
    assertEquals("NONE", patient.getIdentifier().get(1).getValue().getValue());
    assertEquals("2011-01-02", patient.getIdentifier().get(2).getValue().getValue());

  }



  @Test
  public void testSearchByMultipleIdentifiers() throws Exception
  {

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?ids=urn:aaa%7Caaa,urn:bbb%7Cbbb");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    final Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

    assertEquals(1, bundle.getEntries().size());

    final Patient patient = (Patient) bundle.getEntries().get(0).getResource();
    assertEquals("urn:aaa|aaa", patient.getIdentifier().get(1).getValueAsQueryToken());
    assertEquals("urn:bbb|bbb", patient.getIdentifier().get(2).getValueAsQueryToken());
  }



  @Test
  public void testSearchByParamIdentifier() throws Exception
  {

    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?identifier=urn:hapitest:mrns%7C00001");
    HttpResponse status = ourClient.execute(httpGet);

    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

    assertEquals(1, bundle.getEntries().size());

    Patient patient = (Patient) bundle.getEntries().get(0).getResource();
    assertEquals("PatientOne", patient.getName().get(0).getGiven().get(0).getValue());

    /**
     * Alternate form
     */
    final HttpPost httpPost = new HttpPost("http://localhost:" + ourPort
        + "/Patient/_search?identifier=urn:hapitest:mrns%7C00001");
    status = ourClient.execute(httpPost);

    responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    bundle = ourCtx.newXmlParser().parseBundle(responseContent);

    assertEquals(1, bundle.getEntries().size());

    patient = (Patient) bundle.getEntries().get(0).getResource();
    assertEquals("PatientOne", patient.getName().get(0).getGiven().get(0).getValue());

    /**
     * failing form
     */
    httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/_search?identifier=urn:hapitest:mrns%7C00001");
    status = ourClient.execute(httpGet);

    responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());

  }



  @Test
  public void testSearchNamedNoParams() throws Exception
  {

    // HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
    // "/Patient/1");
    // httpPost.setEntity(new StringEntity("test",
    // ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/?_query=someQueryNoParams");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    final Patient patient = (Patient) ourCtx.newXmlParser().parseBundle(responseContent).getEntries().get(0)
        .getResource();
    assertEquals("someQueryNoParams", patient.getName().get(1).getFamilyAsSingleString());

    final InstantDt lm = (InstantDt) patient.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
    assertEquals("2011-01-02T22:01:02", lm.getValueAsString());

  }



  @Test
  public void testSearchNamedOneParam() throws Exception
  {

    // HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
    // "/Patient/1");
    // httpPost.setEntity(new StringEntity("test",
    // ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort
        + "/Patient/?_query=someQueryOneParam&param1=AAAA");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    final Patient patient = (Patient) ourCtx.newXmlParser().parseBundle(responseContent).getEntries().get(0)
        .getResource();
    assertEquals("AAAA", patient.getName().get(1).getFamilyAsSingleString());

  }



  @Test
  public void testSearchQuantityParam() throws Exception
  {

    // HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
    // "/Patient/1");
    // httpPost.setEntity(new StringEntity("test",
    // ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/?quantityParam=%3E%3D123%7Cfoo%7Cbar");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    final Patient patient = (Patient) ourCtx.newXmlParser().parseBundle(responseContent).getEntries().get(0)
        .getResource();
    assertEquals(">=", patient.getName().get(1).getFamily().get(0).getValue());
    assertEquals("123", patient.getName().get(1).getFamily().get(1).getValue());
    assertEquals("foo", patient.getName().get(1).getFamily().get(2).getValue());
    assertEquals("bar", patient.getName().get(1).getFamily().get(3).getValue());

  }



  @Test
  public void testSearchWithIncludes() throws Exception
  {

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort
        + "/Patient?withIncludes=include1&_include=include2&_include=include3");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    final Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

    final BundleEntry entry0 = bundle.getEntries().get(0);
    final Patient patient = (Patient) entry0.getResource();
    assertEquals("include1", patient.getCommunication().get(0).getText().getValue());
    assertEquals("include2", patient.getAddress().get(0).getLine().get(0).getValue());
    assertEquals("include3", patient.getAddress().get(1).getLine().get(0).getValue());
  }



  @Test
  public void testSearchWithIncludesBad() throws Exception
  {

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort
        + "/Patient?withIncludes=include1&_include=include2&_include=include4");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(400, status.getStatusLine().getStatusCode());
  }



  @Test
  public void testSearchWithIncludesNone() throws Exception
  {

    final HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?withIncludes=include1");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    // Make sure there is no crash
    assertEquals(200, status.getStatusLine().getStatusCode());
  }



  @Test
  public void testSearchWithOptionalParam() throws Exception
  {

    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?name1=AAA");
    HttpResponse status = ourClient.execute(httpGet);

    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

    assertEquals(1, bundle.getEntries().size());

    Patient patient = (Patient) bundle.getEntries().get(0).getResource();
    assertEquals("AAA", patient.getName().get(0).getFamily().get(0).getValue());
    assertEquals("PatientOne", patient.getName().get(0).getGiven().get(0).getValue());

    /*
     * Now with optional value populated
     */

    httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?name1=AAA&name2=BBB");
    status = ourClient.execute(httpGet);

    responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    bundle = ourCtx.newXmlParser().parseBundle(responseContent);

    assertEquals(1, bundle.getEntries().size());

    patient = (Patient) bundle.getEntries().get(0).getResource();
    assertEquals("AAA", patient.getName().get(0).getFamily().get(0).getValue());
    assertEquals("BBB", patient.getName().get(0).getGiven().get(0).getValue());

  }



  @Test
  public void testValidate() throws Exception
  {

    Patient patient = new Patient();
    patient.addName().addFamily("FOO");

    HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_validate");
    httpPost.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(patient), ContentType
        .create(Constants.CT_FHIR_XML, "UTF-8")));

    HttpResponse status = ourClient.execute(httpPost);

    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);
    assertThat(responseContent, not(containsString("\n  ")));

    assertEquals(200, status.getStatusLine().getStatusCode());
    OperationOutcome oo = new FhirContext().newXmlParser().parseResource(OperationOutcome.class, responseContent);
    assertEquals("it passed", oo.getIssueFirstRep().getDetails().getValue());

    // Now should fail

    patient = new Patient();
    patient.addName().addFamily("BAR");

    httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_validate");
    httpPost.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(patient), ContentType
        .create(Constants.CT_FHIR_XML, "UTF-8")));

    status = ourClient.execute(httpPost);

    responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(422, status.getStatusLine().getStatusCode());
    oo = new FhirContext().newXmlParser().parseResource(OperationOutcome.class, responseContent);
    assertEquals("it failed", oo.getIssueFirstRep().getDetails().getValue());

    // Should fail with outcome

    patient = new Patient();
    patient.addName().addFamily("BAZ");

    httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_validate");
    httpPost.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(patient), ContentType
        .create(Constants.CT_FHIR_XML, "UTF-8")));

    status = ourClient.execute(httpPost);

    // responseContent = IOUtils.toString(status.getEntity().getContent());
    // ourLog.info("Response was:\n{}", responseContent);

    assertEquals(204, status.getStatusLine().getStatusCode());
    assertNull(status.getEntity());
    // assertEquals("", responseContent);

  }



  @Test
  public void testValidateWithPrettyPrintResponse() throws Exception
  {

    final Patient patient = new Patient();
    patient.addName().addFamily("FOO");

    final HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_validate?_pretty=true");
    httpPost.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(patient), ContentType
        .create(Constants.CT_FHIR_XML, "UTF-8")));

    final HttpResponse status = ourClient.execute(httpPost);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);
    assertThat(responseContent, containsString("\n  "));
  }



  @Test
  public void testWithAdditionalParams() throws Exception
  {

    final HttpDelete httpGet = new HttpDelete("http://localhost:" + ourPort + "/Patient/1234?_pretty=true");
    final HttpResponse status = ourClient.execute(httpGet);

    final String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info("Response was:\n{}", responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());

    final OperationOutcome patient = ourCtx.newXmlParser().parseResource(OperationOutcome.class, responseContent);
    assertEquals("Patient/1234", patient.getIssueFirstRep().getDetails().getValue());

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

    final DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();
    final ServerProfileProvider profProvider = new ServerProfileProvider(ourCtx);
    ourReportProvider = new DummyDiagnosticReportResourceProvider();
    final DummyAdverseReactionResourceProvider adv = new DummyAdverseReactionResourceProvider();

    final ServletHandler proxyHandler = new ServletHandler();
    final DummyRestfulServer servlet = new DummyRestfulServer(patientProvider, profProvider, ourReportProvider, adv);
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





  /**
   * Created by dsotnikov on 2/25/2014.
   */
  public static class DummyAdverseReactionResourceProvider implements IResourceProvider
  {


    @Search()
    public Collection<AdverseReaction> getAllResources()
    {
      final ArrayList<AdverseReaction> retVal = new ArrayList<AdverseReaction>();

      final AdverseReaction ar1 = new AdverseReaction();
      ar1.setId("1");
      retVal.add(ar1);

      final AdverseReaction ar2 = new AdverseReaction();
      ar2.setId("2");
      retVal.add(ar2);

      return retVal;
    }



    @Override
    public Class<? extends IResource> getResourceType()
    {
      return AdverseReaction.class;
    }

  }





  public static class DummyDiagnosticReportResourceProvider implements IResourceProvider
  {


    /**
     * @param theValue
     */
    @Search
    public DiagnosticReport alwaysThrow404(@RequiredParam(name = "throw404") final StringDt theValue)
    {
      throw new ResourceNotFoundException("AAAABBBB");
    }



    @Delete()
    public void deleteDiagnosticReport(@IdParam final IdDt theId)
    {
      // do nothing
    }



    @Override
    public Class<? extends IResource> getResourceType()
    {
      return DiagnosticReport.class;
    }

  }





  /**
   * Created by dsotnikov on 2/25/2014.
   */
  public static class DummyPatientResourceProvider implements IResourceProvider
  {


    @Delete()
    public MethodOutcome deletePatient(@IdParam final IdDt theId)
    {
      final MethodOutcome retVal = new MethodOutcome();
      retVal.setOperationOutcome(new OperationOutcome());
      retVal.getOperationOutcome().addIssue().setDetails(theId.getValue());
      return retVal;
    }



    public List<Patient> findDiagnosticReportsByPatient(
        @RequiredParam(name = "Patient.identifier") final IdentifierDt thePatientId,
        @RequiredParam(name = DiagnosticReport.SP_NAME) final CodingListParam theNames,
        @OptionalParam(name = DiagnosticReport.SP_DATE) final DateRangeParam theDateRange) throws Exception
    {
      return Collections.emptyList();
    }



    @History
    public List<Patient> getHistoryResourceInstance(@IdParam final IdDt theId)
    {
      final ArrayList<Patient> retVal = new ArrayList<Patient>();

      IdDt id = theId;
      if (id.getIdPart().equals("999"))
      {
        id = null; // to test the error when no ID is present
      }

      final Patient older = createPatient1();
      older.setId(id);
      older.getNameFirstRep().getFamilyFirstRep().setValue("OlderFamily");
      older.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, "1");
      older.getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, new Date(10000L));
      older.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, new InstantDt(new Date(20000L)));
      if (id != null && !id.getIdPart().equals("998"))
      {
        older.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, "1");
      }
      retVal.add(older);

      final Patient newer = createPatient1();
      newer.setId(theId);
      newer.getNameFirstRep().getFamilyFirstRep().setValue("NewerFamily");
      if (id != null && !id.getIdPart().equals("998"))
      {
        newer.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, "2");
      }
      newer.getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, new Date(10000L));
      newer.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, new InstantDt(new Date(30000L)));
      retVal.add(newer);

      return retVal;
    }



    @History
    public List<Patient> getHistoryResourceType()
    {
      final ArrayList<Patient> retVal = new ArrayList<Patient>();

      final Patient older = createPatient1();
      older.getNameFirstRep().getFamilyFirstRep().setValue("OlderFamily");
      older.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, "1");
      older.getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, new Date(10000L));
      older.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, new InstantDt(new Date(20000L)));
      older.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, "1");
      retVal.add(older);

      final Patient newer = createPatient1();
      newer.getNameFirstRep().getFamilyFirstRep().setValue("NewerFamily");
      newer.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, "2");
      newer.getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, new Date(10000L));
      newer.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, new InstantDt(new Date(30000L)));
      retVal.add(newer);

      return retVal;
    }



    public Map<String, Patient> getIdToPatient()
    {
      final Map<String, Patient> idToPatient = new HashMap<String, Patient>();
      {
        final Patient patient = createPatient1();
        idToPatient.put("1", patient);
      }
      {
        final Patient patient = new Patient();
        patient.getIdentifier().add(new IdentifierDt());
        patient.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
        patient.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
        patient.getIdentifier().get(0).setValue("00002");
        patient.getName().add(new HumanNameDt());
        patient.getName().get(0).addFamily("Test");
        patient.getName().get(0).addGiven("PatientTwo");
        patient.getGender().setText("F");
        patient.getId().setValue("2");
        idToPatient.put("2", patient);
      }
      return idToPatient;
    }



    @Search()
    public Patient getPatient(@RequiredParam(name = Patient.SP_IDENTIFIER) final IdentifierDt theIdentifier)
    {
      for (final Patient next : getIdToPatient().values())
      {
        for (final IdentifierDt nextId : next.getIdentifier())
        {
          if (nextId.matchesSystemAndValue(theIdentifier))
          {
            return next;
          }
        }
      }
      return null;
    }



    @Search()
    public Patient getPatientByDateRange(@RequiredParam(name = "dateRange") final DateRangeParam theIdentifiers)
    {
      final Patient retVal = getIdToPatient().get("1");
      retVal.getName().get(0).addSuffix().setValue(theIdentifiers.getLowerBound().getValueAsQueryToken());
      retVal.getName().get(0).addSuffix().setValue(theIdentifiers.getUpperBound().getValueAsQueryToken());
      return retVal;
    }



    @Search()
    public List<Patient> getPatientMultipleIdentifiers(@RequiredParam(name = "ids") final CodingListParam theIdentifiers)
    {
      final List<Patient> retVal = new ArrayList<Patient>();
      final Patient next = getIdToPatient().get("1");

      for (final CodingDt nextId : theIdentifiers.getCodings())
      {
        next.getIdentifier().add(new IdentifierDt(nextId.getSystem().getValueAsString(), nextId.getCode().getValue()));
      }

      retVal.add(next);

      return retVal;
    }



    @Search(queryName = "someQueryNoParams")
    public Patient getPatientNoParams()
    {
      final Patient next = getIdToPatient().get("1");
      next.addName().addFamily("someQueryNoParams");
      next.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, new InstantDt("2011-01-02T22:01:02"));
      return next;
    }



    @Search(queryName = "someQueryOneParam")
    public Patient getPatientOneParam(@RequiredParam(name = "param1") final StringDt theParam)
    {
      final Patient next = getIdToPatient().get("1");
      next.addName().addFamily(theParam.getValue());
      return next;
    }



    @Search()
    public Patient getPatientQuantityParam(@RequiredParam(name = "quantityParam") final QuantityDt theParam)
    {
      final Patient next = getIdToPatient().get("1");
      next.addName().addFamily(theParam.getComparator().getValueAsString())
          .addFamily(theParam.getValue().getValueAsString()).addFamily(theParam.getSystem().getValueAsString())
          .addFamily(theParam.getUnits().getValueAsString());
      return next;
    }



    @Search()
    public Patient getPatientWithDOB(@RequiredParam(name = "dob") final DateParam theDob)
    {
      final Patient next = getIdToPatient().get("1");
      if (theDob.getComparator() != null)
      {
        next.addIdentifier().setValue(theDob.getComparator().getCode());
      }
      else
      {
        next.addIdentifier().setValue("NONE");
      }
      next.addIdentifier().setValue(theDob.getValueAsString());
      return next;
    }



    @Search()
    public Patient getPatientWithIncludes(@RequiredParam(name = "withIncludes") final StringDt theString,
        @IncludeParam(allow = { "include1", "include2", "include3" }) final List<Include> theIncludes)
    {
      final Patient next = getIdToPatient().get("1");

      next.addCommunication().setText(theString.getValue());

      for (final Include line : theIncludes)
      {
        next.addAddress().addLine(line.getValue());
      }

      return next;
    }



    @Search()
    public List<Patient> getPatientWithOptionalName(@RequiredParam(name = "name1") final StringDt theName1,
        @OptionalParam(name = "name2") final StringDt theName2)
    {
      final List<Patient> retVal = new ArrayList<Patient>();
      final Patient next = getIdToPatient().get("1");
      next.getName().get(0).getFamily().set(0, theName1);
      if (theName2 != null)
      {
        next.getName().get(0).getGiven().set(0, theName2);
      }
      retVal.add(next);

      return retVal;
    }



    /**
     * @param theName3
     */
    @Search()
    public List<Patient> getPatientWithOptionalName(@RequiredParam(name = "aaa") final StringDt theName1,
        @OptionalParam(name = "bbb") final StringDt theName2, @OptionalParam(name = "ccc") final StringDt theName3)
    {
      final List<Patient> retVal = new ArrayList<Patient>();
      final Patient next = getIdToPatient().get("1");
      next.getName().get(0).getFamily().set(0, theName1);
      if (theName2 != null)
      {
        next.getName().get(0).getGiven().set(0, theName2);
      }
      retVal.add(next);

      return retVal;
    }



    /**
     * Retrieve the resource by its identifier
     * 
     * @param theId
     *          The resource identity
     * @return The resource
     */
    @Read()
    public Patient read(@IdParam final IdDt theId)
    {
      return getIdToPatient().get(theId.getIdPart());
    }



    @Read(version = true)
    public Patient vread(@IdParam final IdDt theId)
    {
      final Patient retVal = getIdToPatient().get(theId.getIdPart());
      final List<HumanNameDt> name = retVal.getName();
      final HumanNameDt nameDt = name.get(0);
      final String value = theId.getVersionIdPart();
      nameDt.setText(value);
      return retVal;
    }



    @Override
    public Class<Patient> getResourceType()
    {
      return Patient.class;
    }



    @Validate()
    public MethodOutcome validatePatient(@ResourceParam final Patient thePatient)
    {
      if (thePatient.getNameFirstRep().getFamilyFirstRep().getValueNotNull().equals("FOO"))
      {
        final MethodOutcome methodOutcome = new MethodOutcome();
        final OperationOutcome oo = new OperationOutcome();
        oo.addIssue().setDetails("it passed");
        methodOutcome.setOperationOutcome(oo);
        return methodOutcome;
      }
      if (thePatient.getNameFirstRep().getFamilyFirstRep().getValueNotNull().equals("BAR"))
      {
        throw new UnprocessableEntityException("it failed");
      }
      return new MethodOutcome();
    }



    private Patient createPatient1()
    {
      final Patient patient = new Patient();
      patient.addIdentifier();
      patient.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
      patient.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
      patient.getIdentifier().get(0).setValue("00001");
      patient.addName();
      patient.getName().get(0).addFamily("Test");
      patient.getName().get(0).addGiven("PatientOne");
      patient.getGender().setText("M");
      patient.getId().setValue("1");
      return patient;
    }

  }





  public static class DummyRestfulServer extends RestfulServer
  {


    private static final long serialVersionUID = 1L;

    private final Collection<IResourceProvider> myResourceProviders;



    public DummyRestfulServer(final IResourceProvider... theResourceProviders)
    {
      this.myResourceProviders = Arrays.asList(theResourceProviders);
    }



    @Override
    public Collection<IResourceProvider> getResourceProviders()
    {
      return this.myResourceProviders;
    }



    @Override
    public ISecurityManager getSecurityManager()
    {
      return null;
    }

  }

}
