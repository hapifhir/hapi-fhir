package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.ElementUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu2.model.Bundle;
import org.hl7.fhir.dstu2.model.Bundle.SearchEntryMode;
import org.hl7.fhir.dstu2.model.DiagnosticReport;
import org.hl7.fhir.dstu2.model.IdType;
import org.hl7.fhir.dstu2.model.Observation;
import org.hl7.fhir.dstu2.model.Organization;
import org.hl7.fhir.dstu2.model.Patient;
import org.hl7.fhir.dstu2.model.Practitioner;
import org.hl7.fhir.dstu2.model.Reference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IncludeHl7OrgDstu2Test {

  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(IncludeHl7OrgDstu2Test.class);
  private static CloseableHttpClient ourClient;
  private static int ourPort;
  private static Server ourServer;
  private static FhirContext ourCtx;

  @Test
  public void testNoIncludes() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?name=Hello");
    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
    assertEquals(1, bundle.getEntry().size());

    Patient p = (Patient) bundle.getEntry().get(0).getResource();
    assertEquals(0, p.getName().size());
    assertEquals("Hello", p.getIdElement().getIdPart());
  }

  @Test
  public void testOneIncludeXml() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?name=Hello&_include=foo");
    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    assertEquals(200, status.getStatusLine().getStatusCode());

    ourLog.info(responseContent);

    Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
    assertEquals(1, bundle.getEntry().size());

    Patient p = (Patient) bundle.getEntry().get(0).getResource();
    assertEquals(1, p.getName().size());
    assertEquals("Hello", p.getIdElement().getIdPart());
    assertEquals("foo", p.getName().get(0).getFamily().get(0).getValue());
  }

  @Test
  public void testOneIncludeJson() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?name=Hello&_include=foo&_format=json");
    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    assertEquals(200, status.getStatusLine().getStatusCode());

    ourLog.info(responseContent);

    Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);
    assertEquals(1, bundle.getEntry().size());

    Patient p = (Patient) bundle.getEntry().get(0).getResource();
    assertEquals(1, p.getName().size());
    assertEquals("Hello", p.getIdElement().getIdPart());
    assertEquals("foo", p.getName().get(0).getFamily().get(0).getValue());
  }

  @Test
  public void testIIncludedResourcesNonContained() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=normalInclude&_pretty=true");
    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);

    ourLog.info(responseContent);

    assertEquals(3, bundle.getEntry().size());
    assertEquals(new IdType("Patient/p1"), bundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless());
    assertEquals(new IdType("Patient/p2"), bundle.getEntry().get(1).getResource().getIdElement().toUnqualifiedVersionless());
    assertEquals(new IdType("Organization/o1"), bundle.getEntry().get(2).getResource().getIdElement().toUnqualifiedVersionless());
    assertEquals(SearchEntryMode.INCLUDE, bundle.getEntry().get(2).getSearch().getMode());

    Patient p1 = (Patient) bundle.getEntry().get(0).getResource();
    assertEquals(0, p1.getContained().size());

    Patient p2 = (Patient) bundle.getEntry().get(1).getResource();
    assertEquals(0, p2.getContained().size());

  }

  @Test
  public void testIIncludedResourcesNonContainedInExtension() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=extInclude&_pretty=true");
    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);

    ourLog.info(responseContent);

    assertEquals(3, bundle.getEntry().size());
    assertEquals(new IdType("Patient/p1"), bundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless());
    assertEquals(new IdType("Patient/p2"), bundle.getEntry().get(1).getResource().getIdElement().toUnqualifiedVersionless());
    assertEquals(new IdType("Organization/o1"), bundle.getEntry().get(2).getResource().getIdElement().toUnqualifiedVersionless());
    assertEquals(SearchEntryMode.INCLUDE, bundle.getEntry().get(2).getSearch().getMode());

    Patient p1 = (Patient) bundle.getEntry().get(0).getResource();
    assertEquals(0, p1.getContained().size());

    Patient p2 = (Patient) bundle.getEntry().get(1).getResource();
    assertEquals(0, p2.getContained().size());

  }

  @Test
  public void testIIncludedResourcesNonContainedInExtensionJson() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=extInclude&_pretty=true&_format=json");
    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);

    ourLog.info(responseContent);

    assertEquals(3, bundle.getEntry().size());
    assertEquals(new IdType("Patient/p1"), bundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless());
    assertEquals(new IdType("Patient/p2"), bundle.getEntry().get(1).getResource().getIdElement().toUnqualifiedVersionless());
    assertEquals(new IdType("Organization/o1"), bundle.getEntry().get(2).getResource().getIdElement().toUnqualifiedVersionless());
    assertEquals(SearchEntryMode.INCLUDE, bundle.getEntry().get(2).getSearch().getMode());

    Patient p1 = (Patient) bundle.getEntry().get(0).getResource();
    assertEquals(0, p1.getContained().size());

    Patient p2 = (Patient) bundle.getEntry().get(1).getResource();
    assertEquals(0, p2.getContained().size());

  }

  @Test
  public void testIIncludedResourcesNonContainedInDeclaredExtension() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=declaredExtInclude&_pretty=true");
    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);

    ourLog.info(responseContent);

    assertEquals(4, bundle.getEntry().size());
    assertEquals(new IdType("Patient/p1"), bundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless());
    assertEquals(new IdType("Patient/p2"), bundle.getEntry().get(1).getResource().getIdElement().toUnqualifiedVersionless());
    assertEquals(new IdType("Organization/o1"), bundle.getEntry().get(2).getResource().getIdElement().toUnqualifiedVersionless());
    assertEquals(new IdType("Organization/o2"), bundle.getEntry().get(3).getResource().getIdElement().toUnqualifiedVersionless());
    assertEquals(SearchEntryMode.INCLUDE, bundle.getEntry().get(2).getSearch().getMode());
    assertEquals(SearchEntryMode.INCLUDE, bundle.getEntry().get(3).getSearch().getMode());

    Patient p1 = (Patient) bundle.getEntry().get(0).getResource();
    assertEquals(0, p1.getContained().size());

    Patient p2 = (Patient) bundle.getEntry().get(1).getResource();
    assertEquals(0, p2.getContained().size());

  }

  @Test
  public void testTwoInclude() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?name=Hello&_include=foo&_include=bar&_pretty=true");
    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info(responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
    assertEquals(1, bundle.getEntry().size());

    Patient p = (Patient) bundle.getEntry().get(0).getResource();
    assertEquals(2, p.getName().size());
    assertEquals("Hello", p.getIdElement().getIdPart());

    Set<String> values = new HashSet<String>();
    values.add(p.getName().get(0).getFamily().get(0).getValue());
    values.add(p.getName().get(1).getFamily().get(0).getValue());
    assertThat(values, containsInAnyOrder("foo", "bar"));

  }

  @Test
  public void testBadInclude() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?name=Hello&_include=foo&_include=baz");
    HttpResponse status = ourClient.execute(httpGet);
    assertEquals(400, status.getStatusLine().getStatusCode());
  }

  @ResourceDef(name = "Patient")
  public static class ExtPatient extends Patient {
    private static final long serialVersionUID = 1L;

    @Child(name = "secondOrg")
    @Extension(url = "http://foo#secondOrg", definedLocally = false, isModifier = false)
    private Reference mySecondOrg;

    @Override
    public boolean isEmpty() {
      return super.isEmpty() && ElementUtil.isEmpty(mySecondOrg);
    }

    public Reference getSecondOrg() {
      if (mySecondOrg == null) {
        mySecondOrg = new Reference();
      }
      return mySecondOrg;
    }

    public void setSecondOrg(Reference theSecondOrg) {
      mySecondOrg = theSecondOrg;
    }

  }

  /**
   * Created by dsotnikov on 2/25/2014.
   */
  public static class DummyDiagnosticReportResourceProvider implements IResourceProvider {

    @Override
    public Class<DiagnosticReport> getResourceType() {
      return DiagnosticReport.class;
    }

    @Search(queryName = "stitchedInclude")
    public List<DiagnosticReport> stitchedInclude() {
      Practitioner pr1 = new Practitioner();
      pr1.setId("Practitioner/001");
      pr1.getName().addFamily("Pract1");

      Practitioner pr2 = new Practitioner();
      pr2.setId("Practitioner/002");
      pr2.getName().addFamily("Pract2");

      Practitioner pr3 = new Practitioner();
      pr3.setId("Practitioner/003");
      pr3.getName().addFamily("Pract3");

      Observation o1 = new Observation();
      o1.getCode().setText("Obs1");
      o1.addPerformer().setResource(pr1);

      Observation o2 = new Observation();
      o2.getCode().setText("Obs2");
      o2.addPerformer().setResource(pr2);

      Observation o3 = new Observation();
      o3.getCode().setText("Obs3");
      o3.addPerformer().setResource(pr3);

      DiagnosticReport rep = new DiagnosticReport();
      rep.setId("DiagnosticReport/999");
      rep.getCode().setText("Rep");
      rep.addResult().setResource(o1);
      rep.addResult().setResource(o2);
      rep.addResult().setResource(o3);

      return Collections.singletonList(rep);
    }

  }

  /**
   * Created by dsotnikov on 2/25/2014.
   */
  public static class DummyPatientResourceProvider implements IResourceProvider {

    @Search(queryName = "normalInclude")
    public List<Patient> normalInclude() {
      Organization o1 = new Organization();
      o1.getNameElement().setValue("o1");
      o1.setId("o1");

      Patient p1 = new Patient();
      p1.setId("p1");
      p1.addIdentifier().setValue("p1");
      p1.getManagingOrganization().setResource(o1);

      Patient p2 = new Patient();
      p2.setId("p2");
      p2.addIdentifier().setValue("p2");
      p2.getManagingOrganization().setResource(o1);

      return Arrays.asList(p1, p2);
    }

    @Search(queryName = "extInclude")
    public List<Patient> extInclude() {
      Organization o1 = new Organization();
      o1.getNameElement().setValue("o1");
      o1.setId("o1");

      Patient p1 = new Patient();
      p1.setId("p1");
      p1.addIdentifier().setValue("p1");
      p1.addExtension().setUrl("http://foo").setValue(new Reference(o1));

      Patient p2 = new Patient();
      p2.setId("p2");
      p2.addIdentifier().setValue("p2");
      p2.addExtension().setUrl("http://foo").setValue(new Reference(o1));

      return Arrays.asList(p1, p2);
    }

    @Search(queryName = "declaredExtInclude")
    public List<ExtPatient> declaredExtInclude() {
      Organization o1 = new Organization();
      o1.getNameElement().setValue("o1");
      o1.setId("o1");

      Organization o2 = new Organization();
      o2.getNameElement().setValue("o2");
      o2.setId("o2");
      o1.getPartOf().setResource(o2);

      ExtPatient p1 = new ExtPatient();
      p1.setId("p1");
      p1.addIdentifier().setValue("p1");
      p1.getSecondOrg().setResource(o1);

      ExtPatient p2 = new ExtPatient();
      p2.setId("p2");
      p2.addIdentifier().setValue("p2");
      p2.getSecondOrg().setResource(o1);

      return Arrays.asList(p1, p2);
    }

    @Search(queryName = "containedInclude")
    public List<Patient> containedInclude() {
      Organization o1 = new Organization();
      o1.getNameElement().setValue("o1");

      Patient p1 = new Patient();
      p1.setId("p1");
      p1.addIdentifier().setValue("p1");
      p1.getManagingOrganization().setResource(o1);

      Patient p2 = new Patient();
      p2.setId("p2");
      p2.addIdentifier().setValue("p2");
      p2.getManagingOrganization().setResource(o1);

      return Arrays.asList(p1, p2);
    }

    @Search
    public List<Patient> findPatient(@RequiredParam(name = Patient.SP_NAME) StringDt theName, @IncludeParam(allow = {"foo", "bar"}) Set<Include> theIncludes) {
      ArrayList<Patient> retVal = new ArrayList<Patient>();

      Patient p = new Patient();
      p.addIdentifier().setSystem("foo").setValue("bar");

      p.setId(theName.getValue());

      if (theIncludes != null) {
        for (Include next : theIncludes) {
          p.addName().addFamily(next.getValue());
        }
      }
      retVal.add(p);

      return retVal;
    }

    @Override
    public Class<Patient> getResourceType() {
      return Patient.class;
    }

  }

  @AfterAll
  public static void afterClass() throws Exception {
    JettyUtil.closeServer(ourServer);
  }

  @BeforeAll
  public static void beforeClass() throws Exception {

    ourCtx = FhirContext.forDstu2Hl7Org();
    ourServer = new Server(0);

    DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

    ServletHandler proxyHandler = new ServletHandler();
    RestfulServer servlet = new RestfulServer(ourCtx);
    servlet.setResourceProviders(patientProvider, new DummyDiagnosticReportResourceProvider());
    servlet.setBundleInclusionRule(BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE);
    servlet.setDefaultResponseEncoding(EncodingEnum.XML);
    ServletHolder servletHolder = new ServletHolder(servlet);
    proxyHandler.addServletWithMapping(servletHolder, "/*");
    ourServer.setHandler(proxyHandler);
    JettyUtil.startServer(ourServer);
    ourPort = JettyUtil.getPortForStartedServer(ourServer);

    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
    HttpClientBuilder builder = HttpClientBuilder.create();
    builder.setConnectionManager(connectionManager);
    ourClient = builder.build();

  }

}
