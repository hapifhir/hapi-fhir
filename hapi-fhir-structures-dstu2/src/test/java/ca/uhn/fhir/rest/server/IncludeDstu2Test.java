package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import ca.uhn.fhir.model.dstu2.valueset.SearchEntryModeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.ElementUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IncludeDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(IncludeDstu2Test.class);
	private static final FhirContext ourCtx = FhirContext.forDstu2Cached();

	@RegisterExtension
	public static final RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		.setDefaultResponseEncoding(EncodingEnum.XML)
		.registerProvider(new DummyPatientResourceProvider())
		.registerProvider(new DummyDiagnosticReportResourceProvider())
		.withPagingProvider(new FifoMemoryPagingProvider(100))
		.withServer(s->s.setBundleInclusionRule(BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE))
		.setDefaultPrettyPrint(false);

	@RegisterExtension
	public static final HttpClientExtension ourClient = new HttpClientExtension();

	@Test
	public void testBadInclude() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?name=Hello&_include=foo&_include=baz");
		HttpResponse status = ourClient.execute(httpGet);
		assertEquals(400, status.getStatusLine().getStatusCode());
	}


	@Test
	public void testIIncludedResourcesNonContained() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=normalInclude&_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);

		ourLog.info(responseContent);

		assertThat(bundle.getEntry()).hasSize(3);

		assertEquals(new IdDt("Patient/p1"), BundleUtil.toListOfResources(ourCtx, bundle).get(0).getIdElement().toUnqualifiedVersionless());
		assertEquals(new IdDt("Patient/p2"), BundleUtil.toListOfResources(ourCtx, bundle).get(1).getIdElement().toUnqualifiedVersionless());
		assertEquals(new IdDt("Organization/o1"), BundleUtil.toListOfResources(ourCtx, bundle).get(2).getIdElement().toUnqualifiedVersionless());
		assertEquals(SearchEntryModeEnum.INCLUDE, bundle.getEntry().get(2).getSearch().getModeElement().getValueAsEnum());

		Patient p1 = (Patient) BundleUtil.toListOfResources(ourCtx, bundle).get(0);
		assertThat(p1.getContained().getContainedResources()).isEmpty();

		Patient p2 = (Patient) BundleUtil.toListOfResources(ourCtx, bundle).get(1);
		assertThat(p2.getContained().getContainedResources()).isEmpty();

	}

	@Test
	public void testIIncludedResourcesNonContainedInDeclaredExtension() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=declaredExtInclude&_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);

		ourLog.info(responseContent);

		assertThat(bundle.getEntry()).hasSize(4);
		assertEquals(new IdDt("Patient/p1"), BundleUtil.toListOfResources(ourCtx, bundle).get(0).getIdElement().toUnqualifiedVersionless());
		assertEquals(new IdDt("Patient/p2"), BundleUtil.toListOfResources(ourCtx, bundle).get(1).getIdElement().toUnqualifiedVersionless());
		assertEquals(new IdDt("Organization/o1"), BundleUtil.toListOfResources(ourCtx, bundle).get(2).getIdElement().toUnqualifiedVersionless());
		assertEquals(new IdDt("Organization/o2"), BundleUtil.toListOfResources(ourCtx, bundle).get(3).getIdElement().toUnqualifiedVersionless());
		assertEquals(SearchEntryModeEnum.INCLUDE, bundle.getEntry().get(2).getSearch().getModeElement().getValueAsEnum());
		assertEquals(SearchEntryModeEnum.INCLUDE, bundle.getEntry().get(3).getSearch().getModeElement().getValueAsEnum());

		Patient p1 = (Patient) BundleUtil.toListOfResources(ourCtx, bundle).get(0);
		assertThat(p1.getContained().getContainedResources()).isEmpty();

		Patient p2 = (Patient) BundleUtil.toListOfResources(ourCtx, bundle).get(1);
		assertThat(p2.getContained().getContainedResources()).isEmpty();

	}

	@Test
	public void testIIncludedResourcesNonContainedInExtension() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=extInclude&_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);

		ourLog.info(responseContent);

		assertThat(bundle.getEntry()).hasSize(3);
		assertEquals(new IdDt("Patient/p1"), BundleUtil.toListOfResources(ourCtx, bundle).get(0).getIdElement().toUnqualifiedVersionless());
		assertEquals(new IdDt("Patient/p2"), BundleUtil.toListOfResources(ourCtx, bundle).get(1).getIdElement().toUnqualifiedVersionless());
		assertEquals(new IdDt("Organization/o1"), BundleUtil.toListOfResources(ourCtx, bundle).get(2).getIdElement().toUnqualifiedVersionless());
		assertEquals(SearchEntryModeEnum.INCLUDE, bundle.getEntry().get(2).getSearch().getModeElement().getValueAsEnum());

		Patient p1 = (Patient) BundleUtil.toListOfResources(ourCtx, bundle).get(0);
		assertThat(p1.getContained().getContainedResources()).isEmpty();

		Patient p2 = (Patient) BundleUtil.toListOfResources(ourCtx, bundle).get(1);
		assertThat(p2.getContained().getContainedResources()).isEmpty();

	}

	@Test
	public void testIIncludedResourcesNonContainedInExtensionJson() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=extInclude&_pretty=true&_format=json");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);

		ourLog.info(responseContent);

		assertThat(bundle.getEntry()).hasSize(3);
		assertEquals(new IdDt("Patient/p1"), BundleUtil.toListOfResources(ourCtx, bundle).get(0).getIdElement().toUnqualifiedVersionless());
		assertEquals(new IdDt("Patient/p2"), BundleUtil.toListOfResources(ourCtx, bundle).get(1).getIdElement().toUnqualifiedVersionless());
		assertEquals(new IdDt("Organization/o1"), BundleUtil.toListOfResources(ourCtx, bundle).get(2).getIdElement().toUnqualifiedVersionless());
		assertEquals(SearchEntryModeEnum.INCLUDE, bundle.getEntry().get(2).getSearch().getModeElement().getValueAsEnum());

		Patient p1 = (Patient) BundleUtil.toListOfResources(ourCtx, bundle).get(0);
		assertThat(p1.getContained().getContainedResources()).isEmpty();

		Patient p2 = (Patient) BundleUtil.toListOfResources(ourCtx, bundle).get(1);
		assertThat(p2.getContained().getContainedResources()).isEmpty();

	}

	@Test
	@Disabled
	public void testMixedContainedAndNonContained() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/DiagnosticReport?_query=stitchedInclude&_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
		assertThat(bundle.getEntry()).hasSize(4);
	}

	@Test
	public void testNoIncludes() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?name=Hello");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
		assertThat(bundle.getEntry()).hasSize(1);

		Patient p = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class).get(0);
		assertThat(p.getName()).isEmpty();
		assertEquals("Hello", p.getId().getIdPart());
	}

	@Test
	public void testOneIncludeJson() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?name=Hello&_include=foo&_format=json");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		ourLog.info(responseContent);

		Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);
		assertThat(bundle.getEntry()).hasSize(1);

		Patient p = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class).get(0);
		assertThat(p.getName()).hasSize(1);
		assertEquals("Hello", p.getId().getIdPart());
		assertEquals("foo", p.getName().get(0).getFamilyFirstRep().getValue());
	}

	@Test
	public void testOneIncludeXml() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?name=Hello&_include=foo");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		ourLog.info(responseContent);

		Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
		assertThat(bundle.getEntry()).hasSize(1);

		Patient p = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class).get(0);
		assertThat(p.getName()).hasSize(1);
		assertEquals("Hello", p.getId().getIdPart());
		assertEquals("foo", p.getName().get(0).getFamilyFirstRep().getValue());
	}

	@Test
	public void testTwoInclude() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?name=Hello&_include=foo&_include=bar&_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
		assertThat(bundle.getEntry()).hasSize(1);

		Patient p = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class).get(0);
		assertThat(p.getName()).hasSize(2);
		assertEquals("Hello", p.getId().getIdPart());

		Set<String> values = new HashSet<String>();
		values.add(p.getName().get(0).getFamilyFirstRep().getValue());
		values.add(p.getName().get(1).getFamilyFirstRep().getValue());
		assertThat(values).containsExactlyInAnyOrder("foo", "bar");

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

	public static class DummyPatientResourceProvider implements IResourceProvider {

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

		@Search(queryName = "extInclude")
		public List<Patient> extInclude() {
			Organization o1 = new Organization();
			o1.getNameElement().setValue("o1");
			o1.setId("o1");

			Patient p1 = new Patient();
			p1.setId("p1");
			p1.addIdentifier().setValue("p1");
			p1.addUndeclaredExtension(false, "http://foo", new ResourceReferenceDt(o1));

			Patient p2 = new Patient();
			p2.setId("p2");
			p2.addIdentifier().setValue("p2");
			p2.addUndeclaredExtension(false, "http://foo", new ResourceReferenceDt(o1));

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

		public void setSecondOrg(ResourceReferenceDt theSecondOrg) {
			mySecondOrg = theSecondOrg;
		}

		@Override
		public boolean isEmpty() {
			return super.isEmpty() && ElementUtil.isEmpty(mySecondOrg);
		}

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}


}
