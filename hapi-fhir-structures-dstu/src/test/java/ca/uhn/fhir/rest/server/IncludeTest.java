package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.util.ElementUtil;
import ca.uhn.fhir.util.PortUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class IncludeTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(IncludeTest.class);
	private static int ourPort;
	private static Server ourServer;

	@Test
	public void testBadInclude() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?name=Hello&_include=foo&_include=baz");
		HttpResponse status = ourClient.execute(httpGet);
		assertEquals(400, status.getStatusLine().getStatusCode());
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info(responseContent);
		assertThat(responseContent, containsString("Invalid _include parameter value"));
	}

	@Test
	public void testIIncludedResourcesNonContained() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=normalInclude&_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		ourLog.info(responseContent);

		assertEquals(3, bundle.size());
		assertEquals(new IdDt("Patient/p1"), bundle.toListOfResources().get(0).getId().toUnqualifiedVersionless());
		assertEquals(new IdDt("Patient/p2"), bundle.toListOfResources().get(1).getId().toUnqualifiedVersionless());
		assertEquals(new IdDt("Organization/o1"), bundle.toListOfResources().get(2).getId().toUnqualifiedVersionless());

		Patient p1 = (Patient) bundle.toListOfResources().get(0);
		assertEquals(0, p1.getContained().getContainedResources().size());

		Patient p2 = (Patient) bundle.toListOfResources().get(1);
		assertEquals(0, p2.getContained().getContainedResources().size());

	}

	@Test
	public void testIIncludedResourcesNonContainedInDeclaredExtension() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=declaredExtInclude&_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		ourLog.info(responseContent);

		assertEquals(4, bundle.size());
		assertEquals(new IdDt("Patient/p1"), bundle.toListOfResources().get(0).getId().toUnqualifiedVersionless());
		assertEquals(new IdDt("Patient/p2"), bundle.toListOfResources().get(1).getId().toUnqualifiedVersionless());
		assertEquals(new IdDt("Organization/o1"), bundle.toListOfResources().get(2).getId().toUnqualifiedVersionless());
		assertEquals(new IdDt("Organization/o2"), bundle.toListOfResources().get(3).getId().toUnqualifiedVersionless());

		Patient p1 = (Patient) bundle.toListOfResources().get(0);
		assertEquals(0, p1.getContained().getContainedResources().size());

		Patient p2 = (Patient) bundle.toListOfResources().get(1);
		assertEquals(0, p2.getContained().getContainedResources().size());

	}

	@Test
	public void testIIncludedResourcesNonContainedInExtension() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=extInclude&_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		ourLog.info(responseContent);

		assertEquals(3, bundle.size());
		assertEquals(new IdDt("Patient/p1"), bundle.toListOfResources().get(0).getId().toUnqualifiedVersionless());
		assertEquals(new IdDt("Patient/p2"), bundle.toListOfResources().get(1).getId().toUnqualifiedVersionless());
		assertEquals(new IdDt("Organization/o1"), bundle.toListOfResources().get(2).getId().toUnqualifiedVersionless());

		Patient p1 = (Patient) bundle.toListOfResources().get(0);
		assertEquals(0, p1.getContained().getContainedResources().size());

		Patient p2 = (Patient) bundle.toListOfResources().get(1);
		assertEquals(0, p2.getContained().getContainedResources().size());

	}

	@Test
	public void testIIncludedResourcesNonContainedInExtensionJson() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=extInclude&_pretty=true&_format=json");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newJsonParser().parseBundle(responseContent);

		ourLog.info(responseContent);

		assertEquals(3, bundle.size());
		assertEquals(new IdDt("Patient/p1"), bundle.toListOfResources().get(0).getId().toUnqualifiedVersionless());
		assertEquals(new IdDt("Patient/p2"), bundle.toListOfResources().get(1).getId().toUnqualifiedVersionless());
		assertEquals(new IdDt("Organization/o1"), bundle.toListOfResources().get(2).getId().toUnqualifiedVersionless());

		Patient p1 = (Patient) bundle.toListOfResources().get(0);
		assertEquals(0, p1.getContained().getContainedResources().size());

		Patient p2 = (Patient) bundle.toListOfResources().get(1);
		assertEquals(0, p2.getContained().getContainedResources().size());

	}

	@Test
	public void testIncludeWithType() {
		assertEquals("Patient:careProvider:Practitioner", new Include("Patient:careProvider", true).withType("Practitioner").getValue());
		assertEquals(true, new Include("Patient:careProvider", true).withType("Practitioner").isRecurse());
		assertEquals(false, new Include("Patient:careProvider:Organization", true).withType("Practitioner").isLocked());
		assertEquals("Practitioner", new Include("Patient:careProvider", true).withType("Practitioner").getParamTargetType());
		assertEquals(null, new Include("Patient:careProvider", true).getParamTargetType());

		assertEquals("Patient:careProvider:Practitioner", new Include("Patient:careProvider:Organization", true).withType("Practitioner").getValue());
		assertEquals(true, new Include("Patient:careProvider:Organization", true).toLocked().withType("Practitioner").isLocked());

		try {
			new Include("").withType("Patient");
			fail();
		} catch (IllegalStateException e) {
			// good
		}
		try {
			new Include("Patient").withType("Patient");
			fail();
		} catch (IllegalStateException e) {
			// good
		}
		try {
			new Include("Patient:").withType("Patient");
			fail();
		} catch (IllegalStateException e) {
			// good
		}
	}

	// @Test
	public void testMixedContainedAndNonContained() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/DiagnosticReport?_query=stitchedInclude&_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(4, bundle.size());
	}

	@Test
	public void testNoIncludes() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?name=Hello");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.size());

		Patient p = bundle.getResources(Patient.class).get(0);
		assertEquals(0, p.getName().size());
		assertEquals("Hello", p.getId().getIdPart());
	}

	@Test
	public void testOneInclude() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?name=Hello&_include=foo");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.size());

		Patient p = bundle.getResources(Patient.class).get(0);
		assertEquals(1, p.getName().size());
		assertEquals("Hello", p.getId().getIdPart());
		assertEquals("foo", p.getName().get(0).getFamilyFirstRep().getValue());
	}

	@Test
	public void testTwoInclude() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?name=Hello&_include=foo&_include=bar");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.size());

		Patient p = bundle.getResources(Patient.class).get(0);
		assertEquals(2, p.getName().size());
		assertEquals("Hello", p.getId().getIdPart());

		Set<String> values = new HashSet<String>();
		values.add(p.getName().get(0).getFamilyFirstRep().getValue());
		values.add(p.getName().get(1).getFamilyFirstRep().getValue());
		assertThat(values, containsInAnyOrder("foo", "bar"));

	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {

		ourCtx = FhirContext.forDstu1();
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setBundleInclusionRule(BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE);
		servlet.setResourceProviders(patientProvider, new DummyDiagnosticReportResourceProvider());
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	public static void main(String[] args) {

		Organization org = new Organization();
		org.setId("Organization/65546");
		org.getName().setValue("Contained Test Organization");

		Patient patient = new Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier("urn:mrns", "253345");
		patient.getManagingOrganization().setResource(patient);

		System.out.println(FhirContext.forDstu1().newXmlParser().setPrettyPrint(true).encodeResourceToString(patient));

		patient.getManagingOrganization().getReference();

	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyDiagnosticReportResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
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
			o1.getName().setText("Obs1");
			o1.addPerformer().setResource(pr1);

			Observation o2 = new Observation();
			o2.getName().setText("Obs2");
			o2.addPerformer().setResource(pr2);

			Observation o3 = new Observation();
			o3.getName().setText("Obs3");
			o3.addPerformer().setResource(pr3);

			DiagnosticReport rep = new DiagnosticReport();
			rep.setId("DiagnosticReport/999");
			rep.getName().setText("Rep");
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

		@Search(queryName = "containedInclude")
		public List<Patient> containedInclude() {
			Organization o1 = new Organization();
			o1.getName().setValue("o1");

			Patient p1 = new Patient();
			p1.setId("p1");
			p1.addIdentifier().setLabel("p1");
			p1.getManagingOrganization().setResource(o1);

			Patient p2 = new Patient();
			p2.setId("p2");
			p2.addIdentifier().setLabel("p2");
			p2.getManagingOrganization().setResource(o1);

			return Arrays.asList(p1, p2);
		}

		@Search(queryName = "declaredExtInclude")
		public List<ExtPatient> declaredExtInclude() {
			Organization o1 = new Organization();
			o1.getName().setValue("o1");
			o1.setId("o1");

			Organization o2 = new Organization();
			o2.getName().setValue("o2");
			o2.setId("o2");
			o1.getPartOf().setResource(o2);

			ExtPatient p1 = new ExtPatient();
			p1.setId("p1");
			p1.addIdentifier().setLabel("p1");
			p1.getSecondOrg().setResource(o1);

			ExtPatient p2 = new ExtPatient();
			p2.setId("p2");
			p2.addIdentifier().setLabel("p2");
			p2.getSecondOrg().setResource(o1);

			return Arrays.asList(p1, p2);
		}

		@Search(queryName = "extInclude")
		public List<Patient> extInclude() {
			Organization o1 = new Organization();
			o1.getName().setValue("o1");
			o1.setId("o1");

			Patient p1 = new Patient();
			p1.setId("p1");
			p1.addIdentifier().setLabel("p1");
			p1.addUndeclaredExtension(false, "http://foo", new ResourceReferenceDt(o1));

			Patient p2 = new Patient();
			p2.setId("p2");
			p2.addIdentifier().setLabel("p2");
			p2.addUndeclaredExtension(false, "http://foo", new ResourceReferenceDt(o1));

			return Arrays.asList(p1, p2);
		}

		@Search
		public List<Patient> findPatientWithSimpleNames(@RequiredParam(name = Patient.SP_NAME) StringDt theName, @IncludeParam(allow = { "foo", "bar" }) Set<Include> theIncludes) {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			Patient p = new Patient();
			p.addIdentifier("Mr", "Test");

			p.setId(theName.getValue());

			if (theIncludes != null) {
				for (Include next : theIncludes) {
					p.addName().addFamily().setValue(next.getValue());
				}
			}
			retVal.add(p);

			return retVal;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Search(queryName = "normalInclude")
		public List<Patient> normalInclude() {
			Organization o1 = new Organization();
			o1.getName().setValue("o1");
			o1.setId("o1");

			Patient p1 = new Patient();
			p1.setId("p1");
			p1.addIdentifier().setLabel("p1");
			p1.getManagingOrganization().setResource(o1);

			Patient p2 = new Patient();
			p2.setId("p2");
			p2.addIdentifier().setLabel("p2");
			p2.getManagingOrganization().setResource(o1);

			return Arrays.asList(p1, p2);
		}

	}

	@ResourceDef(name = "Patient")
	public static class ExtPatient extends Patient {
		@Child(name = "secondOrg")
		@Extension(url = "http://foo#secondOrg", definedLocally = false, isModifier = false)
		private ResourceReferenceDt mySecondOrg;

		public ResourceReferenceDt getSecondOrg() {
			if (mySecondOrg == null) {
				mySecondOrg = new ResourceReferenceDt();
			}
			return mySecondOrg;
		}

		@Override
		public boolean isEmpty() {
			return super.isEmpty() && ElementUtil.isEmpty(mySecondOrg);
		}

		public void setSecondOrg(ResourceReferenceDt theSecondOrg) {
			mySecondOrg = theSecondOrg;
		}

	}

}
