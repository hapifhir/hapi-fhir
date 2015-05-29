package ca.uhn.fhir.jpa.provider;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.testutil.RandomServerPortProvider;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu.resource.Device;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticOrder;
import ca.uhn.fhir.model.dstu2.resource.DocumentManifest;
import ca.uhn.fhir.model.dstu2.resource.DocumentReference;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.ImagingStudy;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.dstu2.valueset.EncounterStateEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UnsignedIntDt;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

public class ResourceProviderDstu2Test {

	private static ClassPathXmlApplicationContext ourAppCtx;
	private static IGenericClient ourClient;
	private static FhirContext ourFhirCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderDstu2Test.class);
	// private static IFhirResourceDao<Observation> ourObservationDao;
	// private static IFhirResourceDao<Patient> ourPatientDao;
	// private static IFhirResourceDao<Questionnaire> ourQuestionnaireDao;
	private static Server ourServer;
	private static IFhirResourceDao<Organization> ourOrganizationDao;
	private static DaoConfig ourDaoConfig;
	private static CloseableHttpClient ourHttpClient;
	private static String ourServerBase;
	private static int ourPort;

	// private static JpaConformanceProvider ourConfProvider;

	/**
	 * Test for issue #60
	 */
	@Test
	public void testStoreUtf8Characters() throws Exception {
		Organization org = new Organization();
		org.setName("測試醫院");
		org.addIdentifier().setSystem("urn:system").setValue("testStoreUtf8Characters_01");
		IdDt orgId = ourClient.create().resource(org).prettyPrint().encodedXml().execute().getId();

		// Read back directly from the DAO
		{
			Organization returned = ourOrganizationDao.read(orgId);
			String val = ourFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(returned);
			ourLog.info(val);
			assertThat(val, containsString("<name value=\"測試醫院\"/>"));
		}
		// Read back through the HTTP API
		{
			Organization returned = ourClient.read(Organization.class, orgId);
			String val = ourFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(returned);
			ourLog.info(val);
			assertThat(val, containsString("<name value=\"測試醫院\"/>"));
		}
	}

	@Test
	public void testCreateResourceWithNumericId() throws IOException {
		String resource = "<Patient xmlns=\"http://hl7.org/fhir\"></Patient>";

		HttpPost post = new HttpPost(ourServerBase + "/Patient/2");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			assertEquals(400, response.getStatusLine().getStatusCode());
		} finally {
			response.close();
		}
	}

	@Test
	public void testCreateResourceConditional() throws IOException {
		String methodName = "testCreateResourceConditional";

		Patient pt = new Patient();
		pt.addName().addFamily(methodName);
		String resource = ourFhirCtx.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.addHeader(Constants.HEADER_IF_NONE_EXIST, "Patient?name=" + methodName);
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		IdDt id;
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id = new IdDt(newIdString);
		} finally {
			response.close();
		}

		post = new HttpPost(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		post.addHeader(Constants.HEADER_IF_NONE_EXIST, "Patient?name=" + methodName);
		response = ourHttpClient.execute(post);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertEquals(id.getValue(), newIdString); // version should match for conditional create
		} finally {
			response.close();
		}

	}

	@Test
	public void testUpdateResourceConditional() throws IOException {
		String methodName = "testUpdateResourceConditional";

		Patient pt = new Patient();
		pt.addName().addFamily(methodName);
		String resource = ourFhirCtx.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(ourServerBase + "/Patient?name=" + methodName);
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		IdDt id;
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id = new IdDt(newIdString);
		} finally {
			response.close();
		}

		HttpPut put = new HttpPut(ourServerBase + "/Patient?name=" + methodName);
		put.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		response = ourHttpClient.execute(put);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			IdDt newId = new IdDt(response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue());
			assertEquals(id.toVersionless(), newId.toVersionless()); // version shouldn't match for conditional update
			assertNotEquals(id, newId);
		} finally {
			response.close();
		}

	}

	@Test
	public void testDeleteResourceConditional1() throws IOException {
		String methodName = "testDeleteResourceConditional1";

		Patient pt = new Patient();
		pt.addName().addFamily(methodName);
		String resource = ourFhirCtx.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		IdDt id;
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id = new IdDt(newIdString);
		} finally {
			response.close();
		}

		HttpDelete delete = new HttpDelete(ourServerBase + "/Patient?name=" + methodName);
		response = ourHttpClient.execute(delete);
		try {
			assertEquals(204, response.getStatusLine().getStatusCode());
		} finally {
			response.close();
		}

		HttpGet read = new HttpGet(ourServerBase + "/Patient/" + id.getIdPart());
		response = ourHttpClient.execute(read);
		try {
			ourLog.info(response.toString());
			assertEquals(Constants.STATUS_HTTP_410_GONE, response.getStatusLine().getStatusCode());
		} finally {
			response.close();
		}

	}

	/**
	 * Based on email from Rene Spronk
	 */
	@Test
	public void testDeleteResourceConditional2() throws IOException, Exception {
		String methodName = "testDeleteResourceConditional2";

		Patient pt = new Patient();
		pt.addName().addFamily(methodName);
		pt.addIdentifier().setSystem("http://ghh.org/patient").setValue(methodName);
		String resource = ourFhirCtx.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		IdDt id;
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id = new IdDt(newIdString);
		} finally {
			response.close();
		}

		/*
		 * Try it with a raw socket call. The Apache client won't let us use the unescaped "|" in the URL
		 * but we want to make sure that works too..  
		 */
		Socket sock = new Socket();
		sock.setSoTimeout(3000);
		try {
			sock.connect(new InetSocketAddress("localhost", ourPort));
			sock.getOutputStream().write(("DELETE /fhir/context/Patient?identifier=http://ghh.org/patient|" + methodName + " HTTP/1.1\n").getBytes("UTF-8"));
			sock.getOutputStream().write("Host: localhost\n".getBytes("UTF-8"));
			sock.getOutputStream().write("\n".getBytes("UTF-8"));
			
			BufferedReader socketInput = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            //String response = ""; 
            StringBuilder b = new StringBuilder();
            char[] buf = new char[1000];
            while(socketInput.read(buf) != -1){
            	b.append(buf);
            }
			String resp = b.toString();
						
			ourLog.info("Resp: {}", resp);
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} finally {
			sock.close();
		}
		
		Thread.sleep(1000);
		
		HttpGet read = new HttpGet(ourServerBase + "/Patient/" + id.getIdPart());
		response = ourHttpClient.execute(read);
		try {
			ourLog.info(response.toString());
			assertEquals(Constants.STATUS_HTTP_410_GONE, response.getStatusLine().getStatusCode());
		} finally {
			response.close();
		}

	}

	/**
	 * Test for issue #60
	 */
	@Test
	public void testReadAllInstancesOfType() throws Exception {
		Patient pat;

		pat = new Patient();
		pat.addIdentifier().setSystem("urn:system").setValue("testReadAllInstancesOfType_01");
		ourClient.create().resource(pat).prettyPrint().encodedXml().execute().getId();

		pat = new Patient();
		pat.addIdentifier().setSystem("urn:system").setValue("testReadAllInstancesOfType_02");
		ourClient.create().resource(pat).prettyPrint().encodedXml().execute().getId();

		{
			Bundle returned = ourClient.search().forResource(Patient.class).encodedXml().execute();
			assertThat(returned.size(), greaterThan(1));
			assertEquals(BundleTypeEnum.SEARCHSET, returned.getType().getValueAsEnum());
		}
		{
			Bundle returned = ourClient.search().forResource(Patient.class).encodedJson().execute();
			assertThat(returned.size(), greaterThan(1));
		}
	}

	@Test
	public void testSearchBundleDoesntIncludeTextElement() throws Exception {
		HttpGet read = new HttpGet(ourServerBase + "/Patient?_format=json");
		CloseableHttpResponse response = ourHttpClient.execute(read);
		try {
			String text = IOUtils.toString(response.getEntity().getContent());
			ourLog.info(text);
			assertEquals(Constants.STATUS_HTTP_200_OK, response.getStatusLine().getStatusCode());
			assertThat(text, not(containsString("\"text\",\"type\"")));
		} finally {
			response.close();
		}
	}

	@Test
	public void testSearchWithInclude() throws Exception {
		Organization org = new Organization();
		org.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSearchWithInclude01");
		IdDt orgId = ourClient.create().resource(org).prettyPrint().encodedXml().execute().getId();

		Patient pat = new Patient();
		pat.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSearchWithInclude02");
		pat.getManagingOrganization().setReference(orgId);
		ourClient.create().resource(pat).prettyPrint().encodedXml().execute().getId();

		//@formatter:off
		Bundle found = ourClient
				.search()
				.forResource(Patient.class)
				.where(Patient.IDENTIFIER.exactly().systemAndIdentifier("urn:system:rpdstu2","testSearchWithInclude02"))
				.include(Patient.INCLUDE_ORGANIZATION)
				.prettyPrint()
				.execute();
		//@formatter:on

		assertEquals(2, found.size());
		assertEquals(Patient.class, found.getEntries().get(0).getResource().getClass());
		assertEquals(BundleEntrySearchModeEnum.MATCH, found.getEntries().get(0).getSearchMode().getValueAsEnum());
		assertEquals(BundleEntrySearchModeEnum.MATCH, found.getEntries().get(0).getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE));
		assertThat(found.getEntries().get(0).getResource().getText().getDiv().getValueAsString(), containsString("<table class=\"hapiPropertyTable"));
		assertEquals(Organization.class, found.getEntries().get(1).getResource().getClass());
		assertEquals(BundleEntrySearchModeEnum.INCLUDE, found.getEntries().get(1).getSearchMode().getValueAsEnum());
		assertEquals(BundleEntrySearchModeEnum.INCLUDE, found.getEntries().get(1).getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE));
	}

	@Test
	public void testSearchWithMissing() throws Exception {
		ourLog.info("Starting testSearchWithMissing");
		String methodName = "testSearchWithMissing";

		List<IResource> resources = new ArrayList<IResource>();
		for (int i = 0; i < 20; i++) {
			Organization org = new Organization();
			org.setName(methodName + "_0" + i);
			resources.add(org);
		}
		ourClient.transaction().withResources(resources).prettyPrint().encodedXml().execute();
		
		Organization org = new Organization();
		org.addIdentifier().setSystem("urn:system:rpdstu2").setValue(methodName + "01");
		org.setName(methodName + "name");
		IdDt orgNotMissing = ourClient.create().resource(org).prettyPrint().encodedXml().execute().getId().toUnqualifiedVersionless();

		org = new Organization();
		org.addIdentifier().setSystem("urn:system:rpdstu2").setValue(methodName + "01");
		IdDt orgMissing = ourClient.create().resource(org).prettyPrint().encodedXml().execute().getId().toUnqualifiedVersionless();

		{
			//@formatter:off
			Bundle found = ourClient
					.search()
					.forResource(Organization.class)
					.where(Organization.NAME.isMissing(false))
					.limitTo(100)
					.prettyPrint()
					.execute();
			//@formatter:on

			List<IdDt> list = toIdListUnqualifiedVersionless(found);
			ourLog.info(methodName + ": " + list.toString());
			assertThat("Wanted " + orgNotMissing + " but got: " + list, list, containsInRelativeOrder(orgNotMissing));
			assertThat(list, not(containsInRelativeOrder(orgMissing)));
		}
		
			//@formatter:off
			Bundle found = ourClient
					.search()
					.forResource(Organization.class)
					.where(Organization.NAME.isMissing(true))
					.limitTo(100)
					.prettyPrint()
					.execute();
			//@formatter:on

			List<IdDt> list = toIdListUnqualifiedVersionless(found);
			ourLog.info(methodName + " found: " + list.toString() + " - Wanted " + orgMissing + " but not " + orgNotMissing);
			assertThat(list, not(containsInRelativeOrder(orgNotMissing)));
			assertThat("Wanted " + orgMissing + " but found: " + list, list, containsInRelativeOrder(orgMissing));
	}

	private List<IdDt> toIdListUnqualifiedVersionless(Bundle found) {
		List<IdDt> list = new ArrayList<IdDt>();
		for (BundleEntry next : found.getEntries()) {
			list.add(next.getResource().getId().toUnqualifiedVersionless());
		}
		return list;
	}

	@Test
	public void testEverythingOperation() throws Exception {
		String methodName = "testEverythingOperation";

		Organization org1 = new Organization();
		org1.setName(methodName + "1");
		IdDt orgId1 = ourClient.create().resource(org1).execute().getId();

		Patient p = new Patient();
		p.addName().addFamily(methodName);
		p.getManagingOrganization().setReference(orgId1);
		IdDt patientId = ourClient.create().resource(p).execute().getId();

		Organization org2 = new Organization();
		org2.setName(methodName + "1");
		IdDt orgId2 = ourClient.create().resource(org2).execute().getId();

		Device dev = new Device();
		dev.setModel(methodName);
		dev.getOwner().setReference(orgId2);
		IdDt devId = ourClient.create().resource(dev).execute().getId();

		Observation obs = new Observation();
		obs.getSubject().setReference(patientId);
		obs.getDevice().setReference(devId);
		IdDt obsId = ourClient.create().resource(obs).execute().getId();

		Encounter enc = new Encounter();
		enc.getPatient().setReference(patientId);
		IdDt encId = ourClient.create().resource(enc).execute().getId();

		Parameters output = ourClient.operation().onInstance(patientId).named("everything").withNoParameters(Parameters.class).execute();
		ca.uhn.fhir.model.dstu2.resource.Bundle b = (ca.uhn.fhir.model.dstu2.resource.Bundle) output.getParameterFirstRep().getResource();

		Set<IdDt> ids = new HashSet<IdDt>();
		for (Entry next : b.getEntry()) {
			ids.add(next.getResource().getId());
		}

		assertThat(ids, containsInAnyOrder(patientId, devId, obsId, encId, orgId1, orgId2));

		// _revinclude's are counted but not _include's
		assertEquals(3, b.getTotal().intValue());

		ourLog.info(ids.toString());
	}

	/**
	 * See #147
	 */
	@Test
	public void testEverythingDoesntRepeatPatient() throws Exception {
		ca.uhn.fhir.model.dstu2.resource.Bundle b;
		b = ourFhirCtx.newJsonParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, new InputStreamReader(ResourceProviderDstu2Test.class.getResourceAsStream("/bug147-bundle.json")));

		ca.uhn.fhir.model.dstu2.resource.Bundle resp = ourClient.transaction().withBundle(b).execute();
		List<IdDt> ids = new ArrayList<IdDt>();
		for (Entry next : resp.getEntry()) {
			IdDt toAdd = new IdDt(next.getTransactionResponse().getLocation()).toUnqualifiedVersionless();
			ids.add(toAdd);
		}
		ourLog.info("Created: " + ids.toString());

		IdDt patientId = new IdDt(resp.getEntry().get(1).getTransactionResponse().getLocation());
		assertEquals("Patient", patientId.getResourceType());

		{
			Parameters output = ourClient.operation().onInstance(patientId).named("everything").withNoParameters(Parameters.class).execute();
			b = (ca.uhn.fhir.model.dstu2.resource.Bundle) output.getParameterFirstRep().getResource();

			ids = new ArrayList<IdDt>();
			boolean dupes = false;
			for (Entry next : b.getEntry()) {
				IdDt toAdd = next.getResource().getId().toUnqualifiedVersionless();
				dupes = dupes | ids.contains(toAdd);
				ids.add(toAdd);
			}
			ourLog.info("$everything: " + ids.toString());

			assertFalse(ids.toString(), dupes);

			// Default size
			assertEquals(10, ids.size());
		}

		/*
		 * Now try with a size specified
		 */
		{
			Parameters input = new Parameters();
			input.addParameter().setName(Constants.PARAM_COUNT).setValue(new UnsignedIntDt(100));
			Parameters output = ourClient.operation().onInstance(patientId).named("everything").withParameters(input).execute();
			b = (ca.uhn.fhir.model.dstu2.resource.Bundle) output.getParameterFirstRep().getResource();

			ids = new ArrayList<IdDt>();
			boolean dupes = false;
			for (Entry next : b.getEntry()) {
				IdDt toAdd = next.getResource().getId().toUnqualifiedVersionless();
				dupes = dupes | ids.contains(toAdd);
				ids.add(toAdd);
			}
			ourLog.info("$everything: " + ids.toString());

			assertFalse(ids.toString(), dupes);
			assertThat(ids.toString(), containsString("Condition"));
			assertThat(ids.size(), greaterThan(10));
		}
	}

	/**
	 * See #148
	 */
	@Test
	public void testEverythingIncludesCondition() throws Exception {
		ca.uhn.fhir.model.dstu2.resource.Bundle b = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		Patient p = new Patient();
		p.setId("1");
		b.addEntry().setResource(p).getTransaction().setMethod(HTTPVerbEnum.POST);

		Condition c = new Condition();
		c.getPatient().setReference("Patient/1");
		b.addEntry().setResource(c).getTransaction().setMethod(HTTPVerbEnum.POST);

		ca.uhn.fhir.model.dstu2.resource.Bundle resp = ourClient.transaction().withBundle(b).execute();

		ourLog.info(ourFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));

		IdDt patientId = new IdDt(resp.getEntry().get(1).getTransactionResponse().getLocation());
		assertEquals("Patient", patientId.getResourceType());

		Parameters output = ourClient.operation().onInstance(patientId).named("everything").withNoParameters(Parameters.class).execute();
		b = (ca.uhn.fhir.model.dstu2.resource.Bundle) output.getParameterFirstRep().getResource();

		List<IdDt> ids = new ArrayList<IdDt>();
		for (Entry next : b.getEntry()) {
			IdDt toAdd = next.getResource().getId().toUnqualifiedVersionless();
			ids.add(toAdd);
		}

		assertThat(ids.toString(), containsString("Patient/"));
		assertThat(ids.toString(), containsString("Condition/"));

	}

	@Test
	public void testCountParam() throws Exception {
		// NB this does not get used- The paging provider has its own limits built in
		ourDaoConfig.setHardSearchLimit(100);

		List<IBaseResource> resources = new ArrayList<IBaseResource>();
		for (int i = 0; i < 100; i++) {
			Organization org = new Organization();
			org.setName("rpdstu2_testCountParam_01");
			resources.add(org);
		}
		ourClient.transaction().withResources(resources).prettyPrint().encodedXml().execute();

		Bundle found = ourClient.search().forResource(Organization.class).where(Organization.NAME.matches().value("rpdstu2_testCountParam_01")).limitTo(10).execute();
		assertEquals(100, found.getTotalResults().getValue().intValue());
		assertEquals(10, found.getEntries().size());

		found = ourClient.search().forResource(Organization.class).where(Organization.NAME.matches().value("rpdstu2_testCountParam_01")).limitTo(999).execute();
		assertEquals(100, found.getTotalResults().getValue().intValue());
		assertEquals(50, found.getEntries().size());

	}

	/**
	 * See issue #52
	 */
	@Test
	public void testImagingStudyResources() throws Exception {
		IGenericClient client = ourClient;

		int initialSize = client.search().forResource(ImagingStudy.class).execute().size();

		String resBody = IOUtils.toString(ResourceProviderDstu2Test.class.getResource("/imagingstudy.json"));
		client.create().resource(resBody).execute();

		int newSize = client.search().forResource(ImagingStudy.class).execute().size();

		assertEquals(1, newSize - initialSize);

	}

	/**
	 * See issue #52
	 */
	@Test
	public void testDocumentManifestResources() throws Exception {
		ourFhirCtx.getResourceDefinition(Practitioner.class);
		ourFhirCtx.getResourceDefinition(ca.uhn.fhir.model.dstu.resource.DocumentManifest.class);

		IGenericClient client = ourClient;

		int initialSize = client.search().forResource(DocumentManifest.class).execute().size();

		String resBody = IOUtils.toString(ResourceProviderDstu2Test.class.getResource("/documentmanifest.json"));
		client.create().resource(resBody).execute();

		int newSize = client.search().forResource(DocumentManifest.class).execute().size();

		assertEquals(1, newSize - initialSize);

	}

	/**
	 * See issue #52
	 */
	@Test
	public void testDocumentReferenceResources() throws Exception {
		IGenericClient client = ourClient;

		int initialSize = client.search().forResource(DocumentReference.class).execute().size();

		String resBody = IOUtils.toString(ResourceProviderDstu2Test.class.getResource("/documentreference.json"));
		client.create().resource(resBody).execute();

		int newSize = client.search().forResource(DocumentReference.class).execute().size();

		assertEquals(1, newSize - initialSize);

	}

	/**
	 * See issue #52
	 */
	@Test
	public void testDiagnosticOrderResources() throws Exception {
		IGenericClient client = ourClient;

		int initialSize = client.search().forResource(DiagnosticOrder.class).execute().size();

		DiagnosticOrder res = new DiagnosticOrder();
		res.addIdentifier().setSystem("urn:foo").setValue("123");

		client.create().resource(res).execute();

		int newSize = client.search().forResource(DiagnosticOrder.class).execute().size();

		assertEquals(1, newSize - initialSize);

	}

	private void delete(String theResourceType, String theParamName, String theParamValue) {
		Bundle resources = ourClient.search().forResource(theResourceType).where(new StringClientParam(theParamName).matches().value(theParamValue)).execute();
		for (IResource next : resources.toListOfResources()) {
			ourLog.info("Deleting resource: {}", next.getId());
			ourClient.delete().resource(next).execute();
		}
	}

	private void deleteToken(String theResourceType, String theParamName, String theParamSystem, String theParamValue) {
		Bundle resources = ourClient.search().forResource(theResourceType).where(new TokenClientParam(theParamName).exactly().systemAndCode(theParamSystem, theParamValue)).execute();
		for (IResource next : resources.toListOfResources()) {
			ourLog.info("Deleting resource: {}", next.getId());
			ourClient.delete().resource(next).execute();
		}
	}

	@Test
	public void testDeepChaining() {
		delete("Location", Location.SP_NAME, "testDeepChainingL1");
		delete("Location", Location.SP_NAME, "testDeepChainingL2");
		deleteToken("Encounter", Encounter.SP_IDENTIFIER, "urn:foo", "testDeepChainingE1");

		Location l1 = new Location();
		l1.getNameElement().setValue("testDeepChainingL1");
		IdDt l1id = ourClient.create().resource(l1).execute().getId();

		Location l2 = new Location();
		l2.getNameElement().setValue("testDeepChainingL2");
		l2.getPartOf().setReference(l1id.toVersionless().toUnqualified());
		IdDt l2id = ourClient.create().resource(l2).execute().getId();

		Encounter e1 = new Encounter();
		e1.addIdentifier().setSystem("urn:foo").setValue("testDeepChainingE1");
		e1.getStatusElement().setValueAsEnum(EncounterStateEnum.IN_PROGRESS);
		e1.getClassElementElement().setValueAsEnum(EncounterClassEnum.HOME);
		ca.uhn.fhir.model.dstu2.resource.Encounter.Location location = e1.addLocation();
		location.getLocation().setReference(l2id.toUnqualifiedVersionless());
		location.setPeriod(new PeriodDt().setStartWithSecondsPrecision(new Date()).setEndWithSecondsPrecision(new Date()));
		IdDt e1id = ourClient.create().resource(e1).execute().getId();

		//@formatter:off
		Bundle res = ourClient.search()
			.forResource(Encounter.class)
			.where(Encounter.IDENTIFIER.exactly().systemAndCode("urn:foo", "testDeepChainingE1"))
			.include(Encounter.INCLUDE_LOCATION)
			.include(Location.INCLUDE_PARTOF)
			.execute();
		//@formatter:on

		assertEquals(3, res.size());
		assertEquals(1, res.getResources(Encounter.class).size());
		assertEquals(e1id.toUnqualifiedVersionless(), res.getResources(Encounter.class).get(0).getId().toUnqualifiedVersionless());

	}

	@Test
	public void testSaveAndRetrieveExistingNarrative() {
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testSaveAndRetrieveExistingNarrative01");

		Patient p1 = new Patient();
		p1.getText().setStatus(ca.uhn.fhir.model.dstu2.valueset.NarrativeStatusEnum.GENERATED);
		p1.getText().getDiv().setValueAsString("<div>HELLO WORLD</div>");
		p1.addIdentifier().setSystem("urn:system").setValue("testSaveAndRetrieveExistingNarrative01");

		IdDt newId = ourClient.create().resource(p1).execute().getId();

		Patient actual = ourClient.read(Patient.class, newId);
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">HELLO WORLD</div>", actual.getText().getDiv().getValueAsString());
	}

	@Test
	public void testSaveAndRetrieveWithContained() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSaveAndRetrieveWithContained01");

		Organization o1 = new Organization();
		o1.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSaveAndRetrieveWithContained02");

		p1.getManagingOrganization().setResource(o1);

		IdDt newId = ourClient.create().resource(p1).execute().getId();

		Patient actual = ourClient.read(Patient.class, newId);
		assertEquals(1, actual.getContained().getContainedResources().size());
		assertThat(actual.getText().getDiv().getValueAsString(), containsString("<td>Identifier</td><td>testSaveAndRetrieveWithContained01</td>"));

		Bundle b = ourClient.search().forResource("Patient").where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system:rpdstu2", "testSaveAndRetrieveWithContained01")).prettyPrint().execute();
		assertEquals(1, b.size());

	}

	@Test
	public void testSaveAndRetrieveWithoutNarrative() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByResourceChain01");

		IdDt newId = ourClient.create().resource(p1).execute().getId();

		Patient actual = ourClient.read(Patient.class, newId);
		assertThat(actual.getText().getDiv().getValueAsString(), containsString("<td>Identifier</td><td>testSearchByResourceChain01</td>"));
	}

	@Test
	public void testSearchByIdentifier() {
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testSearchByIdentifier01");
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testSearchByIdentifier02");

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByIdentifier01");
		p1.addName().addFamily("testSearchByIdentifierFamily01").addGiven("testSearchByIdentifierGiven01");
		IdDt p1Id = ourClient.create().resource(p1).execute().getId();

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue("testSearchByIdentifier02");
		p2.addName().addFamily("testSearchByIdentifierFamily01").addGiven("testSearchByIdentifierGiven02");
		ourClient.create().resource(p2).execute().getId();

		Bundle actual = ourClient.search().forResource(Patient.class).where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system", "testSearchByIdentifier01")).encodedJson().prettyPrint().execute();
		assertEquals(1, actual.size());
		assertEquals(p1Id.getIdPart(), actual.getEntries().get(0).getResource().getId().getIdPart());
		assertEquals(BundleEntrySearchModeEnum.MATCH, actual.getEntries().get(0).getSearchMode().getValueAsEnum());
	}

	@Test
	public void testSearchByIdentifierWithoutSystem() {
		deleteToken("Patient", Patient.SP_IDENTIFIER, "", "testSearchByIdentifierWithoutSystem01");

		Patient p1 = new Patient();
		p1.addIdentifier().setValue("testSearchByIdentifierWithoutSystem01");
		IdDt p1Id = ourClient.create().resource(p1).execute().getId();

		Bundle actual = ourClient.search().forResource(Patient.class).where(Patient.IDENTIFIER.exactly().systemAndCode(null, "testSearchByIdentifierWithoutSystem01")).encodedJson().prettyPrint().execute();
		assertEquals(1, actual.size());
		assertEquals(p1Id.getIdPart(), actual.getEntries().get(0).getResource().getId().getIdPart());

	}

	@Test
	public void testSearchByResourceChain() {
		delete("Organization", Organization.SP_NAME, "testSearchByResourceChainName01");
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testSearchByResourceChain01");

		Organization o1 = new Organization();
		o1.setName("testSearchByResourceChainName01");
		IdDt o1id = ourClient.create().resource(o1).execute().getId();

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByResourceChain01");
		p1.addName().addFamily("testSearchByResourceChainFamily01").addGiven("testSearchByResourceChainGiven01");
		p1.setManagingOrganization(new ResourceReferenceDt(o1id));
		IdDt p1Id = ourClient.create().resource(p1).execute().getId();

		//@formatter:off
		Bundle actual = ourClient.search()
				.forResource(Patient.class)
				.where(Patient.ORGANIZATION.hasId(o1id.getIdPart()))
				.encodedJson().prettyPrint().execute();
		//@formatter:on
		assertEquals(1, actual.size());
		assertEquals(p1Id.getIdPart(), actual.getEntries().get(0).getResource().getId().getIdPart());

		//@formatter:off
		actual = ourClient.search()
				.forResource(Patient.class)
				.where(Patient.ORGANIZATION.hasId(o1id.getValue()))
				.encodedJson().prettyPrint().execute();
		//@formatter:on
		assertEquals(1, actual.size());
		assertEquals(p1Id.getIdPart(), actual.getEntries().get(0).getResource().getId().getIdPart());

	}

	@Test
	public void testTryToCreateResourceWithReferenceThatDoesntExist() {
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testTryToCreateResourceWithReferenceThatDoesntExist01");

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testTryToCreateResourceWithReferenceThatDoesntExist01");
		p1.addName().addFamily("testTryToCreateResourceWithReferenceThatDoesntExistFamily01").addGiven("testTryToCreateResourceWithReferenceThatDoesntExistGiven01");
		p1.setManagingOrganization(new ResourceReferenceDt("Organization/1323123232349875324987529835"));

		try {
			ourClient.create().resource(p1).execute().getId();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Organization/1323123232349875324987529835"));
		}

	}

	@Test
	public void testUpdateRejectsInvalidTypes() throws InterruptedException {
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testUpdateRejectsInvalidTypes");

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateRejectsInvalidTypes");
		p1.addName().addFamily("Tester").addGiven("testUpdateRejectsInvalidTypes");
		IdDt p1id = ourClient.create().resource(p1).execute().getId();

		Organization p2 = new Organization();
		p2.getNameElement().setValue("testUpdateRejectsInvalidTypes");
		try {
			ourClient.update().resource(p2).withId("Organization/" + p1id.getIdPart()).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			// good
		}

		try {
			ourClient.update().resource(p2).withId("Patient/" + p1id.getIdPart()).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			// good
		}

	}

	@Test
	public void testUpdateWithClientSuppliedIdWhichDoesntExist() {
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testUpdateWithClientSuppliedIdWhichDoesntExistRpDstu2");

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateWithClientSuppliedIdWhichDoesntExistRpDstu2");
		MethodOutcome outcome = ourClient.update().resource(p1).withId("testUpdateWithClientSuppliedIdWhichDoesntExistRpDstu2").execute();
		assertEquals(true, outcome.getCreated().booleanValue());
		IdDt p1Id = outcome.getId();

		assertThat(p1Id.getValue(), containsString("Patient/testUpdateWithClientSuppliedIdWhichDoesntExistRpDstu2/_history"));

		Bundle actual = ourClient.search().forResource(Patient.class).where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system", "testUpdateWithClientSuppliedIdWhichDoesntExistRpDstu2")).encodedJson().prettyPrint().execute();
		assertEquals(1, actual.size());
		assertEquals(p1Id.getIdPart(), actual.getEntries().get(0).getResource().getId().getIdPart());

	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
		ourAppCtx.stop();
		ourHttpClient.close();
	}

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = RandomServerPortProvider.findFreePort();

		RestfulServer restServer = new RestfulServer();
		ourFhirCtx = FhirContext.forDstu2();
		restServer.setFhirContext(ourFhirCtx);

		ourServerBase = "http://localhost:" + ourPort + "/fhir/context";

		ourAppCtx = new ClassPathXmlApplicationContext("hapi-fhir-server-resourceproviders-dstu2.xml", "fhir-jpabase-spring-test-config.xml");

		ourDaoConfig = (DaoConfig) ourAppCtx.getBean(DaoConfig.class);

		ourOrganizationDao = (IFhirResourceDao<Organization>) ourAppCtx.getBean("myOrganizationDaoDstu2", IFhirResourceDao.class);

		List<IResourceProvider> rpsDev = (List<IResourceProvider>) ourAppCtx.getBean("myResourceProvidersDstu2", List.class);
		restServer.setResourceProviders(rpsDev);

		restServer.getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		JpaSystemProviderDstu2 systemProv = ourAppCtx.getBean(JpaSystemProviderDstu2.class, "mySystemProviderDstu2");
		restServer.setPlainProviders(systemProv);

		restServer.setPagingProvider(new FifoMemoryPagingProvider(10));

		ourServer = new Server(ourPort);

		ServletContextHandler proxyHandler = new ServletContextHandler();
		proxyHandler.setContextPath("/");

		ServletHolder servletHolder = new ServletHolder();
		servletHolder.setServlet(restServer);
		proxyHandler.addServlet(servletHolder, "/fhir/context/*");

		ourServer.setHandler(proxyHandler);
		ourServer.start();

		ourFhirCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		ourFhirCtx.getRestfulClientFactory().setSocketTimeout(1200 * 1000);
		ourClient = ourFhirCtx.newRestfulGenericClient(ourServerBase);
//		ourClient.registerInterceptor(new LoggingInterceptor(true));

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourHttpClient = builder.build();

	}

}
