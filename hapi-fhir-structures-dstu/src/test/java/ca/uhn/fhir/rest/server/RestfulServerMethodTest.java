package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.resource.AdverseReaction;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Profile;
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
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.provider.ServerProfileProvider;
import ca.uhn.fhir.util.PortUtil;

public class RestfulServerMethodTest {
	private static CloseableHttpClient ourClient;
	private static final FhirContext ourCtx = FhirContext.forDstu1();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulServerMethodTest.class);
	private static int ourPort;
	private static DummyDiagnosticReportResourceProvider ourReportProvider;
	private static Server ourServer;
	private static RestfulServer ourRestfulServer;

	@Test
	public void testCreateBundleDoesntCreateDoubleEntries() {
		
		List<IBaseResource> resources = new ArrayList<IBaseResource>();
		
		Patient p = new Patient();
		p.setId("Patient/1");
		resources.add(p);
		
		Organization o = new Organization();
		o.setId("Organization/2");
		resources.add(o);
		
		p.getManagingOrganization().setResource(o);
		
		IVersionSpecificBundleFactory factory = ourCtx.newBundleFactory();
		factory.initializeBundleFromResourceList("", resources, "http://foo", "http://foo", 2, null);
		assertEquals(2, factory.getDstu1Bundle().getEntries().size());
	}
	
	@Test
	public void test404IsPropagatedCorrectly() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/DiagnosticReport?throw404=true");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(404, status.getStatusLine().getStatusCode());
		assertThat(responseContent, StringContains.containsString("AAAABBBB"));
	}

	@Test
	public void testInvalidResourceTriggers400() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/FooResource?blah=bar");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(400, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testDateRangeParam() throws Exception {

		// HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
		// "/Patient/1");
		// httpPost.setEntity(new StringEntity("test",
		// ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?dateRange=%3E%3D2011-01-01&dateRange=%3C%3D2021-01-01");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());

		Patient patient = (Patient) ourCtx.newXmlParser().parseBundle(responseContent).getEntries().get(0).getResource();
		assertEquals(">=2011-01-01", patient.getName().get(0).getSuffix().get(0).getValue());
		assertEquals("<=2021-01-01", patient.getName().get(0).getSuffix().get(1).getValue());

	}

	@Test
	public void testDelete() throws Exception {

		HttpDelete httpGet = new HttpDelete("http://localhost:" + ourPort + "/Patient/1234");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());

		OperationOutcome patient = ourCtx.newXmlParser().parseResource(OperationOutcome.class, responseContent);
		assertEquals("Patient/1234", patient.getIssueFirstRep().getDetails().getValue());

	}

	@Test
	public void testDeleteNoResponse() throws Exception {

		// HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
		// "/Patient/1");
		// httpPost.setEntity(new StringEntity("test",
		// ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpDelete httpGet = new HttpDelete("http://localhost:" + ourPort + "/DiagnosticReport/1234");
		HttpResponse status = ourClient.execute(httpGet);

		assertEquals(204, status.getStatusLine().getStatusCode());

	}

	@Test
	public void testEntryLinkSelf() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?withIncludes=include1&_include=include2&_include=include3");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		BundleEntry entry0 = bundle.getEntries().get(0);
		assertEquals("http://localhost:" + ourPort + "/Patient/1", entry0.getLinkSelf().getValue());
		assertEquals("http://localhost:" + ourPort + "/Patient/1", entry0.getId().getValue());

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?withIncludes=include1&_include=include2&_include=include3&_format=json");
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
	public void testFormatParamJson() throws Exception {

		// HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
		// "/Patient/1");
		// httpPost.setEntity(new StringEntity("test",
		// ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=json");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());

		Patient patient = (Patient) ourCtx.newJsonParser().parseResource(responseContent);
		// assertEquals("PatientOne",
		// patient.getName().get(0).getGiven().get(0).getValue());

		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient));

	}

	@Test
	public void testFormatParamXml() throws Exception {

		// HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
		// "/Patient/1");
		// httpPost.setEntity(new StringEntity("test",
		// ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=xml");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Patient patient = (Patient) ourCtx.newXmlParser().parseResource(responseContent);
		assertEquals("PatientOne", patient.getName().get(0).getGiven().get(0).getValue());

	}

	@Test
	public void testGetById() throws Exception {

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
	public void testGetByVersionId() throws Exception {

		// HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
		// "/Patient/1");
		// httpPost.setEntity(new StringEntity("test",
		// ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/_history/999");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Patient patient = (Patient) ourCtx.newXmlParser().parseResource(responseContent);
		assertEquals("PatientOne", patient.getName().get(0).getGiven().get(0).getValue());
		assertEquals("999", patient.getName().get(0).getText().getValue());

	}

	@Test
	public void testGetMetadata() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/metadata");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		// ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);
		Conformance bundle = parser.parseResource(Conformance.class, responseContent);

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
	public void testHistoryFailsIfResourcesAreIncorrectlyPopulated() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/999/_history");
			HttpResponse status = ourClient.execute(httpGet);

			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			ourLog.info("Response was:\n{}", responseContent);

			assertEquals(500, status.getStatusLine().getStatusCode());
		}
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/998/_history");
			HttpResponse status = ourClient.execute(httpGet);

			String responseContent = IOUtils.toString(status.getEntity().getContent());
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
	public void testHistoryResourceInstance() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/222/_history");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		assertEquals(2, bundle.getEntries().size());

		// Older resource
		{
			BundleEntry olderEntry = bundle.getEntries().get(0);
			assertEquals("http://localhost:" + ourPort + "/Patient/222", olderEntry.getId().getValue());
			assertEquals("http://localhost:" + ourPort + "/Patient/222/_history/1", olderEntry.getLinkSelf().getValue());
			InstantDt pubExpected = new InstantDt(new Date(10000L));
			InstantDt pubActualRes = (InstantDt) olderEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
			InstantDt pubActualBundle = olderEntry.getPublished();
			assertEquals(pubExpected.getValueAsString(), pubActualRes.getValueAsString());
			assertEquals(pubExpected.getValueAsString(), pubActualBundle.getValueAsString());
			InstantDt updExpected = new InstantDt(new Date(20000L));
			InstantDt updActualRes = (InstantDt) olderEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
			InstantDt updActualBundle = olderEntry.getUpdated();
			assertEquals(updExpected.getValueAsString(), updActualRes.getValueAsString());
			assertEquals(updExpected.getValueAsString(), updActualBundle.getValueAsString());
		}
		// Newer resource
		{
			BundleEntry newerEntry = bundle.getEntries().get(1);
			assertEquals("http://localhost:" + ourPort + "/Patient/222", newerEntry.getId().getValue());
			assertEquals("http://localhost:" + ourPort + "/Patient/222/_history/2", newerEntry.getLinkSelf().getValue());
			InstantDt pubExpected = new InstantDt(new Date(10000L));
			InstantDt pubActualRes = (InstantDt) newerEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
			InstantDt pubActualBundle = newerEntry.getPublished();
			assertEquals(pubExpected.getValueAsString(), pubActualRes.getValueAsString());
			assertEquals(pubExpected.getValueAsString(), pubActualBundle.getValueAsString());
			InstantDt updExpected = new InstantDt(new Date(30000L));
			InstantDt updActualRes = (InstantDt) newerEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
			InstantDt updActualBundle = newerEntry.getUpdated();
			assertEquals(updExpected.getValueAsString(), updActualRes.getValueAsString());
			assertEquals(updExpected.getValueAsString(), updActualBundle.getValueAsString());
		}

	}

	@Test
	public void testHistoryResourceType() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/_history");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		assertEquals(2, bundle.getEntries().size());

		// Older resource
		{
			BundleEntry olderEntry = bundle.getEntries().get(0);
			assertEquals("http://localhost:" + ourPort + "/Patient/1", olderEntry.getId().getValue());
			assertThat(olderEntry.getLinkSelf().getValue(), StringEndsWith.endsWith("/Patient/1/_history/1"));
			InstantDt pubExpected = new InstantDt(new Date(10000L));
			InstantDt pubActualRes = (InstantDt) olderEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
			InstantDt pubActualBundle = olderEntry.getPublished();
			assertEquals(pubExpected.getValueAsString(), pubActualRes.getValueAsString());
			assertEquals(pubExpected.getValueAsString(), pubActualBundle.getValueAsString());
			InstantDt updExpected = new InstantDt(new Date(20000L));
			InstantDt updActualRes = (InstantDt) olderEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
			InstantDt updActualBundle = olderEntry.getUpdated();
			assertEquals(updExpected.getValueAsString(), updActualRes.getValueAsString());
			assertEquals(updExpected.getValueAsString(), updActualBundle.getValueAsString());
		}
		// Newer resource
		{
			BundleEntry newerEntry = bundle.getEntries().get(1);
			assertEquals("http://localhost:" + ourPort + "/Patient/1", newerEntry.getId().getValue());
			assertThat(newerEntry.getLinkSelf().getValue(), StringEndsWith.endsWith("/Patient/1/_history/2"));
			InstantDt pubExpected = new InstantDt(new Date(10000L));
			InstantDt pubActualRes = (InstantDt) newerEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
			InstantDt pubActualBundle = newerEntry.getPublished();
			assertEquals(pubExpected.getValueAsString(), pubActualRes.getValueAsString());
			assertEquals(pubExpected.getValueAsString(), pubActualBundle.getValueAsString());
			InstantDt updExpected = new InstantDt(new Date(30000L));
			InstantDt updActualRes = (InstantDt) newerEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
			InstantDt updActualBundle = newerEntry.getUpdated();
			assertEquals(updExpected.getValueAsString(), updActualRes.getValueAsString());
			assertEquals(updExpected.getValueAsString(), updActualBundle.getValueAsString());
		}

	}

	@Test
	public void testReadOnTypeThatDoesntSupportRead() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/AdverseReaction/223");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(Constants.STATUS_HTTP_400_BAD_REQUEST, status.getStatusLine().getStatusCode());

	}

	@Test
	public void testSearchAll() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/AdverseReaction");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		assertEquals(2, bundle.getEntries().size());

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/AdverseReaction/_search");
		status = ourClient.execute(httpPost);

		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		assertEquals(2, bundle.getEntries().size());

	}

	@Test
	public void testSearchAllProfiles() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Profile?");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		// ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);
		Bundle bundle = parser.parseBundle(responseContent);

		ourLog.info("Response:\n{}", parser.encodeBundleToString(bundle));

	}

	@Test
	public void testSearchByDob() throws Exception {

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
	public void testSearchByDobWithSearchActionAndPost() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/_search?dob=2011-01-02");
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
		List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
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
	public void testSearchByMultipleIdentifiers() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?ids=urn:aaa%7Caaa,urn:bbb%7Cbbb");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		assertEquals(1, bundle.getEntries().size());

		Patient patient = (Patient) bundle.getEntries().get(0).getResource();
		assertEquals("urn:aaa|aaa", patient.getIdentifier().get(1).getValueAsQueryToken(ourCtx));
		assertEquals("urn:bbb|bbb", patient.getIdentifier().get(2).getValueAsQueryToken(ourCtx));
	}

	@Test
	public void testSearchByParamIdentifier() throws Exception {

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
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_search?identifier=urn:hapitest:mrns%7C00001");
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
	public void testSearchNamedNoParams() throws Exception {

		// HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
		// "/Patient/1");
		// httpPost.setEntity(new StringEntity("test",
		// ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/?_query=someQueryNoParams");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Patient patient = (Patient) ourCtx.newXmlParser().parseBundle(responseContent).getEntries().get(0).getResource();
		assertEquals("someQueryNoParams", patient.getName().get(1).getFamilyAsSingleString());

		InstantDt lm = (InstantDt) patient.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
		assertEquals("2011-01-02T22:01:02", lm.getValueAsString());

	}

	@Test
	public void testSearchNamedOneParam() throws Exception {

		// HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
		// "/Patient/1");
		// httpPost.setEntity(new StringEntity("test",
		// ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/?_query=someQueryOneParam&param1=AAAA");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Patient patient = (Patient) ourCtx.newXmlParser().parseBundle(responseContent).getEntries().get(0).getResource();
		assertEquals("AAAA", patient.getName().get(1).getFamilyAsSingleString());

	}

	@Test
	public void testSearchQuantityParam() throws Exception {

		// HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
		// "/Patient/1");
		// httpPost.setEntity(new StringEntity("test",
		// ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/?quantityParam=%3E%3D123%7Cfoo%7Cbar");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Patient patient = (Patient) ourCtx.newXmlParser().parseBundle(responseContent).getEntries().get(0).getResource();
		assertEquals(">=", patient.getName().get(1).getFamily().get(0).getValue());
		assertEquals("123", patient.getName().get(1).getFamily().get(1).getValue());
		assertEquals("foo", patient.getName().get(1).getFamily().get(2).getValue());
		assertEquals("bar", patient.getName().get(1).getFamily().get(3).getValue());

	}

	@Test
	public void testSearchWithIncludes() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?withIncludes=include1&_include=include2&_include=include3");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		BundleEntry entry0 = bundle.getEntries().get(0);
		Patient patient = (Patient) entry0.getResource();
		assertEquals("include1", patient.getCommunication().get(0).getText().getValue());
		assertEquals("include2", patient.getAddress().get(0).getLine().get(0).getValue());
		assertEquals("include3", patient.getAddress().get(1).getLine().get(0).getValue());
	}

	@Test
	public void testSearchWithIncludesBad() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?withIncludes=include1&_include=include2&_include=include4");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(400, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testSearchWithIncludesNone() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?withIncludes=include1");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		// Make sure there is no crash
		assertEquals(200, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testSearchWithOptionalParam() throws Exception {

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
	public void testValidate() throws Exception {

		Patient patient = new Patient();
		patient.addName().addFamily("FOO");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_validate");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);
		assertThat(responseContent, not(containsString("\n  ")));

		assertEquals(200, status.getStatusLine().getStatusCode());
		OperationOutcome oo = ourCtx.newXmlParser().parseResource(OperationOutcome.class, responseContent);
		assertEquals("it passed", oo.getIssueFirstRep().getDetails().getValue());

		// Now should fail

		patient = new Patient();
		patient.addName().addFamily("BAR");

		httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_validate");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		status = ourClient.execute(httpPost);

		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());
		oo = ourCtx.newXmlParser().parseResource(OperationOutcome.class, responseContent);
		assertEquals("it failed", oo.getIssueFirstRep().getDetails().getValue());

		// Should fail with outcome

		patient = new Patient();
		patient.addName().addFamily("BAZ");

		httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_validate");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		status = ourClient.execute(httpPost);

		// responseContent = IOUtils.toString(status.getEntity().getContent());
		// ourLog.info("Response was:\n{}", responseContent);

		assertEquals(204, status.getStatusLine().getStatusCode());
		assertNull(status.getEntity());
		// assertEquals("", responseContent);

	}

	@Test
	public void testValidateWithPrettyPrintResponse() throws Exception {

		Patient patient = new Patient();
		patient.addName().addFamily("FOO");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_validate?_pretty=true");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);
		assertThat(responseContent, containsString("\n  "));
	}

	@Test
	public void testWithAdditionalParams() throws Exception {

		HttpDelete httpGet = new HttpDelete("http://localhost:" + ourPort + "/Patient/1234?_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());

		OperationOutcome patient = ourCtx.newXmlParser().parseResource(OperationOutcome.class, responseContent);
		assertEquals("Patient/1234", patient.getIssueFirstRep().getDetails().getValue());

	}

	@Test
	public void testServerProfileProviderFindsProfiles() {
		ServerProfileProvider profileProvider = (ServerProfileProvider)ourRestfulServer.getServerProfilesProvider();
		IdDt id = new IdDt("Profile", "observation");
		Profile profile = profileProvider.getProfileById(createHttpServletRequest(), id);
		assertNotNull(profile);
	}

	private HttpServletRequest createHttpServletRequest() {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getRequestURI()).thenReturn("/FhirStorm/fhir/Patient/_search");
		when(req.getServletPath()).thenReturn("/fhir");
		when(req.getRequestURL()).thenReturn(new StringBuffer().append("http://fhirstorm.dyndns.org:8080/FhirStorm/fhir/Patient/_search"));
		when(req.getContextPath()).thenReturn("/FhirStorm");
		return req;
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();
		ourReportProvider = new DummyDiagnosticReportResourceProvider();
		DummyAdverseReactionResourceProvider adv = new DummyAdverseReactionResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		DummyRestfulServer dummyServer = new DummyRestfulServer(patientProvider, ourReportProvider, adv);
		ourRestfulServer = dummyServer;
		ServerProfileProvider profProvider = new ServerProfileProvider(ourRestfulServer);
		dummyServer.addResourceProvider(profProvider);
		ServletHolder servletHolder = new ServletHolder(ourRestfulServer);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
        ourRestfulServer.setBundleInclusionRule(BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE);

        ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyAdverseReactionResourceProvider implements IResourceProvider {

		@Search()
		public Collection<AdverseReaction> getAllResources() {
			ArrayList<AdverseReaction> retVal = new ArrayList<AdverseReaction>();

			AdverseReaction ar1 = new AdverseReaction();
			ar1.setId("1");
			retVal.add(ar1);

			AdverseReaction ar2 = new AdverseReaction();
			ar2.setId("2");
			retVal.add(ar2);

			return retVal;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return AdverseReaction.class;
		}

	}

	public static class DummyDiagnosticReportResourceProvider implements IResourceProvider {



		/**
		 * @param theValue
		 */
		@Search
		public DiagnosticReport alwaysThrow404(@RequiredParam(name = "throw404") StringDt theValue) {
			throw new ResourceNotFoundException("AAAABBBB");
		}


		@Delete()
		public void deleteDiagnosticReport(@IdParam IdDt theId) {
			// do nothing
		}

	

		@Override
		public Class<? extends IResource> getResourceType() {
			return DiagnosticReport.class;
		}

	

	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyPatientResourceProvider implements IResourceProvider {


		@Delete()
		public MethodOutcome deletePatient(@IdParam IdDt theId) {
			MethodOutcome retVal = new MethodOutcome();
			retVal.setOperationOutcome(new OperationOutcome());
			((OperationOutcome)retVal.getOperationOutcome()).addIssue().setDetails(theId.getValue());
			return retVal;
		}

		public List<Patient> findDiagnosticReportsByPatient(@RequiredParam(name = "Patient.identifier") IdentifierDt thePatientId,
				@RequiredParam(name = DiagnosticReport.SP_NAME) TokenOrListParam theNames, @OptionalParam(name = DiagnosticReport.SP_DATE) DateRangeParam theDateRange) throws Exception {
			return Collections.emptyList();
		}

		@History
		public List<Patient> getHistoryResourceInstance(@IdParam IdDt theId) {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			IdDt id = theId;
			if (id.getIdPart().equals("999")) {
				id = null; // to test the error when no ID is present
			}

			Patient older = createPatient1();
			older.setId(id);
			older.getNameFirstRep().getFamilyFirstRep().setValue("OlderFamily");
			older.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, "1");
			older.getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, new Date(10000L));
			older.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, new InstantDt(new Date(20000L)));
			if (id != null && !id.getIdPart().equals("998")) {
				older.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, "1");
			}
			retVal.add(older);

			Patient newer = createPatient1();
			newer.setId(theId);
			newer.getNameFirstRep().getFamilyFirstRep().setValue("NewerFamily");
			if (id != null && !id.getIdPart().equals("998")) {
				newer.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, "2");
			}
			newer.getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, new Date(10000L));
			newer.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, new InstantDt(new Date(30000L)));
			retVal.add(newer);

			return retVal;
		}

		@History
		public List<Patient> getHistoryResourceType() {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			Patient older = createPatient1();
			older.getNameFirstRep().getFamilyFirstRep().setValue("OlderFamily");
			older.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, "1");
			older.getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, new Date(10000L));
			older.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, new InstantDt(new Date(20000L)));
			older.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, "1");
			retVal.add(older);

			Patient newer = createPatient1();
			newer.getNameFirstRep().getFamilyFirstRep().setValue("NewerFamily");
			newer.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, "2");
			newer.getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, new Date(10000L));
			newer.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, new InstantDt(new Date(30000L)));
			retVal.add(newer);

			return retVal;
		}

		public Map<String, Patient> getIdToPatient() {
			Map<String, Patient> idToPatient = new HashMap<String, Patient>();
			{
				Patient patient = createPatient1();
				idToPatient.put("1", patient);
			}
			{
				Patient patient = new Patient();
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
		public Patient getPatient(@RequiredParam(name = Patient.SP_IDENTIFIER) IdentifierDt theIdentifier) {
			for (Patient next : getIdToPatient().values()) {
				for (IdentifierDt nextId : next.getIdentifier()) {
					if (nextId.matchesSystemAndValue(theIdentifier)) {
						return next;
					}
				}
			}
			return null;
		}

		@Search()
		public Patient getPatientByDateRange(@RequiredParam(name = "dateRange") DateRangeParam theIdentifiers) {
			Patient retVal = getIdToPatient().get("1");
			retVal.getName().get(0).addSuffix().setValue(theIdentifiers.getLowerBound().getValueAsQueryToken(ourCtx));
			retVal.getName().get(0).addSuffix().setValue(theIdentifiers.getUpperBound().getValueAsQueryToken(ourCtx));
			return retVal;
		}

		@Search()
		public List<Patient> getPatientMultipleIdentifiers(@RequiredParam(name = "ids") TokenOrListParam theIdentifiers) {
			List<Patient> retVal = new ArrayList<Patient>();
			Patient next = getIdToPatient().get("1");

			for (BaseCodingDt nextId : theIdentifiers.getListAsCodings()) {
				next.getIdentifier().add(new IdentifierDt(nextId.getSystemElement().getValueAsString(), nextId.getCodeElement().getValue()));
			}

			retVal.add(next);

			return retVal;
		}

		@Search(queryName = "someQueryNoParams")
		public Patient getPatientNoParams() {
			Patient next = getIdToPatient().get("1");
			next.addName().addFamily("someQueryNoParams");
			next.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, new InstantDt("2011-01-02T22:01:02"));
			return next;
		}

		@Search(queryName = "someQueryOneParam")
		public Patient getPatientOneParam(@RequiredParam(name = "param1") StringDt theParam) {
			Patient next = getIdToPatient().get("1");
			next.addName().addFamily(theParam.getValue());
			return next;
		}

		@Search()
		public Patient getPatientQuantityParam(@RequiredParam(name = "quantityParam") QuantityDt theParam) {
			Patient next = getIdToPatient().get("1");
			next.addName().addFamily(theParam.getComparator().getValueAsString()).addFamily(theParam.getValue().getValueAsString()).addFamily(theParam.getSystem().getValueAsString())
					.addFamily(theParam.getUnits().getValueAsString());
			return next;
		}

		@Search()
		public Patient getPatientWithDOB(@RequiredParam(name = "dob") DateParam theDob) {
			Patient next = getIdToPatient().get("1");
			if (theDob.getComparator() != null) {
				next.addIdentifier().setValue(theDob.getComparator().getCode());
			} else {
				next.addIdentifier().setValue("NONE");
			}
			next.addIdentifier().setValue(theDob.getValueAsString());
			return next;
		}

		@Search()
		public Patient getPatientWithIncludes(@RequiredParam(name = "withIncludes") StringDt theString,
				@IncludeParam(allow = { "include1", "include2", "include3" }) List<Include> theIncludes) {
			Patient next = getIdToPatient().get("1");

			next.addCommunication().setText(theString.getValue());

			for (Include line : theIncludes) {
				next.addAddress().addLine(line.getValue());
			}

			return next;
		}

		@Search()
		public List<Patient> getPatientWithOptionalName(@RequiredParam(name = "name1") StringDt theName1, @OptionalParam(name = "name2") StringDt theName2) {
			List<Patient> retVal = new ArrayList<Patient>();
			Patient next = getIdToPatient().get("1");
			next.getName().get(0).getFamily().set(0, theName1);
			if (theName2 != null) {
				next.getName().get(0).getGiven().set(0, theName2);
			}
			retVal.add(next);

			return retVal;
		}

		/**
		 * @param theName3
		 */
		@Search()
		public List<Patient> getPatientWithOptionalName(@RequiredParam(name = "aaa") StringDt theName1, @OptionalParam(name = "bbb") StringDt theName2, @OptionalParam(name = "ccc") StringDt theName3) {
			List<Patient> retVal = new ArrayList<Patient>();
			Patient next = getIdToPatient().get("1");
			next.getName().get(0).getFamily().set(0, theName1);
			if (theName2 != null) {
				next.getName().get(0).getGiven().set(0, theName2);
			}
			retVal.add(next);

			return retVal;
		}

		/**
		 * Retrieve the resource by its identifier
		 * 
		 * @param theId
		 *            The resource identity
		 * @return The resource
		 */
		@Read()
		public Patient read(@IdParam IdDt theId) {
			return getIdToPatient().get(theId.getIdPart());
		}

		@Read(version=true)
		public Patient vread(@IdParam IdDt theId) {
			Patient retVal = getIdToPatient().get(theId.getIdPart());
			if (retVal == null) {
				throw new ResourceNotFoundException("Couldn't find ID " + theId.getIdPart() + " - Valid IDs are: " + getIdToPatient().keySet());
			}
			
			List<HumanNameDt> name = retVal.getName();
			HumanNameDt nameDt = name.get(0);
			String value = theId.getVersionIdPart();
			nameDt.setText(value);
			return retVal;
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		

	

		@Validate()
		public MethodOutcome validatePatient(@ResourceParam Patient thePatient) {
			if (thePatient.getNameFirstRep().getFamilyFirstRep().getValueNotNull().equals("FOO")) {
				MethodOutcome methodOutcome = new MethodOutcome();
				OperationOutcome oo = new OperationOutcome();
				oo.addIssue().setDetails("it passed");
				methodOutcome.setOperationOutcome(oo);
				return methodOutcome;
			}
			if (thePatient.getNameFirstRep().getFamilyFirstRep().getValueNotNull().equals("BAR")) {
				OperationOutcome oo = new OperationOutcome();
				oo.addIssue().setDetails("it failed");
				throw new UnprocessableEntityException(oo);
			}
			return new MethodOutcome();
		}

		private Patient createPatient1() {
			Patient patient = new Patient();
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

	public static class DummyRestfulServer extends RestfulServer {

		private static final long serialVersionUID = 1L;

		private Collection<IResourceProvider> myResourceProviders;

		public DummyRestfulServer(IResourceProvider... theResourceProviders) {
			super(ourCtx);
			myResourceProviders = new ArrayList<IResourceProvider>(Arrays.asList(theResourceProviders));
		}

		public void addResourceProvider(IResourceProvider theResourceProvider) {
			myResourceProviders.add(theResourceProvider);
		}

		@Override
		public Collection<IResourceProvider> getResourceProviders() {
			return myResourceProviders;
		}

	}

}
