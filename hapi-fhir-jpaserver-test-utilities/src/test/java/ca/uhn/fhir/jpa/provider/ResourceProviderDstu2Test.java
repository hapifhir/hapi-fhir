package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.BaseResource;
import ca.uhn.fhir.model.dstu2.resource.Basic;
import ca.uhn.fhir.model.dstu2.resource.Binary;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.CarePlan;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.Device;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticOrder;
import ca.uhn.fhir.model.dstu2.resource.DocumentManifest;
import ca.uhn.fhir.model.dstu2.resource.DocumentReference;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.ImagingStudy;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.MedicationAdministration;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import ca.uhn.fhir.model.dstu2.resource.SearchParameter;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.dstu2.valueset.EncounterStateEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.dstu2.valueset.SearchEntryModeEnum;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionChannelTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.XPathUsageTypeEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UnsignedIntDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.NumberClientParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.AopTestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceProviderDstu2Test extends BaseResourceProviderDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderDstu2Test.class);
	private SearchCoordinatorSvcImpl mySearchCoordinatorSvcRaw;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());
		myDaoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());

		mySearchCoordinatorSvcRaw.setLoadingThrottleForUnitTests(null);
		mySearchCoordinatorSvcRaw.setSyncSizeForUnitTests(SearchCoordinatorSvcImpl.DEFAULT_SYNC_SIZE);
		mySearchCoordinatorSvcRaw.setNeverUseLocalSearchForUnitTests(false);

	}

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();

		myDaoConfig.setAllowMultipleDelete(true);
	}

	@BeforeEach
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
		mySearchCoordinatorSvcRaw = AopTestUtils.getTargetObject(mySearchCoordinatorSvc);
	}

	private void checkParamMissing(String paramName) throws IOException {
		HttpGet get = new HttpGet(ourServerBase + "/Observation?" + paramName + ":missing=false");
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
		}
	}

	/**
	 * See #2023
	 */
	@Test
	public void testCustomNumberSearchParam() {
		SearchParameter numberParameter = new SearchParameter();
		numberParameter.setId("future-appointment-count");
		numberParameter.setName("Future Appointment Count");
		numberParameter.setCode("future-appointment-count");
		numberParameter.setDescription("Count of future appointments for the patient");
		numberParameter.setUrl("http://integer");
		numberParameter.setStatus(ca.uhn.fhir.model.dstu2.valueset.ConformanceResourceStatusEnum.ACTIVE);
		numberParameter.setBase(ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum.PATIENT);
		numberParameter.setType(ca.uhn.fhir.model.dstu2.valueset.SearchParamTypeEnum.NUMBER);
		numberParameter.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		numberParameter.setXpath("Patient.extension('http://integer')");
		ourClient.update().resource(numberParameter).execute();

		// This fires every 10 seconds
		mySearchParamRegistryController.refreshCacheIfNecessary();

		Patient patient = new Patient();
		patient.setId("future-appointment-count-pt");
		patient.setActive(true);
		patient.addUndeclaredExtension(false,  "http://integer", new IntegerDt(2));
		ourClient.update().resource(patient).execute();

		Bundle futureAppointmentCountBundle2 = ourClient
			.search()
			.forResource(Patient.class)
			.where(new NumberClientParam("future-appointment-count").greaterThan().number(1))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(futureAppointmentCountBundle2.getTotal().intValue(), 1);

		Bundle futureAppointmentCountBundle3 = ourClient
			.search()
			.forResource(Patient.class)
			.where(new NumberClientParam("future-appointment-count").exactly().number(2))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(futureAppointmentCountBundle3.getTotal().intValue(), 1);

	}


	/**
	 * See #484
	 */
	@Test
	public void saveAndRetrieveBasicResource() throws IOException {
		String input = IOUtils.toString(getClass().getResourceAsStream("/basic-stu3.xml"), StandardCharsets.UTF_8);

		String respString = ourClient.transaction().withBundle(input).prettyPrint().execute();
		ourLog.info(respString);
		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = myFhirContext.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, respString);
		IdDt id = new IdDt(bundle.getEntry().get(0).getResponse().getLocation());

		Basic basic = ourClient.read().resource(Basic.class).withId(id).execute();
		List<ExtensionDt> exts = basic.getUndeclaredExtensionsByUrl("http://localhost:1080/hapi-fhir-jpaserver-example/baseDstu2/StructureDefinition/DateID");
		assertEquals(1, exts.size());
	}

	@Test
	public void testBundleCreate() throws Exception {
		IGenericClient client = ourClient;

		String resBody = IOUtils.toString(ResourceProviderDstu2Test.class.getResource("/document-father.json"), StandardCharsets.UTF_8);
		IIdType id = client.create().resource(resBody).execute().getId();

		ourLog.info("Created: {}", id);

		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = client.read().resource(ca.uhn.fhir.model.dstu2.resource.Bundle.class).withId(id).execute();

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
	}

	@Test
	public void testBundleCreateWithTypeTransaction() throws Exception {
		IGenericClient client = ourClient;

		String resBody = IOUtils.toString(ResourceProviderDstu2Test.class.getResource("/document-father.json"), StandardCharsets.UTF_8);
		resBody = resBody.replace("\"type\": \"document\"", "\"type\": \"transaction\"");
		try {
			client.create().resource(resBody).execute().getId();
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Unable to store a Bundle resource on this server with a Bundle.type value of: transaction. Note that if you are trying to perform a FHIR transaction or batch operation you should POST the Bundle resource to the Base URL of the server, not to the /Bundle endpoint."));
		}
	}

	@Test
	public void testCodeSearch() {
		Subscription subs = new Subscription();
		subs.setStatus(SubscriptionStatusEnum.ACTIVE);
		subs.getChannel().setType(SubscriptionChannelTypeEnum.WEBSOCKET);
		subs.setCriteria("Observation?");
		IIdType id = ourClient.create().resource(subs).execute().getId().toUnqualifiedVersionless();

		//@formatter:off
		ca.uhn.fhir.model.dstu2.resource.Bundle resp = ourClient
			.search()
			.forResource(Subscription.class)
			.where(Subscription.TYPE.exactly().code(SubscriptionChannelTypeEnum.WEBSOCKET.getCode()))
			.and(Subscription.STATUS.exactly().code(SubscriptionStatusEnum.ACTIVE.getCode()))
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();
		//@formatter:off

		assertThat(toUnqualifiedVersionlessIds(resp), contains(id));

		//@formatter:off
		resp = ourClient
			.search()
			.forResource(Subscription.class)
			.where(Subscription.TYPE.exactly().systemAndCode(SubscriptionChannelTypeEnum.WEBSOCKET.getSystem(), SubscriptionChannelTypeEnum.WEBSOCKET.getCode()))
			.and(Subscription.STATUS.exactly().systemAndCode(SubscriptionStatusEnum.ACTIVE.getSystem(), SubscriptionStatusEnum.ACTIVE.getCode()))
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();
		//@formatter:off

		assertThat(toUnqualifiedVersionlessIds(resp), contains(id));

		//@formatter:off
		resp = ourClient
			.search()
			.forResource(Subscription.class)
			.where(Subscription.TYPE.exactly().systemAndCode(SubscriptionChannelTypeEnum.WEBSOCKET.getSystem(), SubscriptionChannelTypeEnum.WEBSOCKET.getCode()))
			.and(Subscription.STATUS.exactly().systemAndCode("foo", SubscriptionStatusEnum.ACTIVE.getCode()))
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();
		//@formatter:off

		assertThat(toUnqualifiedVersionlessIds(resp), empty());

	}

	@Test
	public void testCountParam() {
		List<IBaseResource> resources = new ArrayList<IBaseResource>();
		for (int i = 0; i < 100; i++) {
			Organization org = new Organization();
			org.setName("rpdstu2_testCountParam_01");
			resources.add(org);
		}
		ourClient.transaction().withResources(resources).prettyPrint().encodedXml().execute();

		Bundle found = ourClient
			.search()
			.forResource(Organization.class)
			.where(Organization.NAME.matches().value("rpdstu2_testCountParam_01"))
			.count(10)
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(100, found.getTotalElement().getValue().intValue());
		assertEquals(10, found.getEntry().size());

		found = ourClient
			.search()
			.forResource(Organization.class)
			.where(Organization.NAME.matches().value("rpdstu2_testCountParam_01"))
			.count(999)
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(100, found.getTotalElement().getValue().intValue());
		assertEquals(50, found.getEntry().size());

	}

	/**
	 * See #438
	 */
	@Test
	public void testCreateAndUpdateBinary() throws Exception {
		byte[] arr = {1, 21, 74, 123, -44};
		Binary binary = new Binary();
		binary.setContent(arr);
		binary.setContentType("dansk");
		binary.addUndeclaredExtension(true, "bobobo", new StringDt("hey there"));


		IIdType resource = ourClient.create().resource(binary).execute().getId();

		Binary fromDB = ourClient.read().resource(Binary.class).withId(resource.toVersionless()).execute();
		assertEquals("1", fromDB.getId().getVersionIdPart());

		arr[0] = 2;
		binary.setContent(arr);
		HttpPut putRequest = new HttpPut(ourServerBase + "/Binary/" + resource.getIdPart());
		putRequest.setEntity(new ByteArrayEntity(arr, ContentType.parse("dansk")));
		CloseableHttpResponse resp = ourHttpClient.execute(putRequest);
		try {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertEquals(resource.withVersion("2").getValue(), resp.getFirstHeader("Content-Location").getValue());
		} finally {
			IOUtils.closeQuietly(resp);
		}

		fromDB = ourClient.read().resource(Binary.class).withId(resource.toVersionless()).execute();
		assertEquals("2", fromDB.getId().getVersionIdPart());

		arr[0] = 3;
		fromDB.setContent(arr);
		String encoded = myFhirContext.newJsonParser().encodeResourceToString(fromDB);
		putRequest = new HttpPut(ourServerBase + "/Binary/" + resource.getIdPart());
		putRequest.setEntity(new StringEntity(encoded, ContentType.parse("application/json+fhir")));
		resp = ourHttpClient.execute(putRequest);
		try {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertEquals(resource.withVersion("3").getValue(), resp.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		} finally {
			IOUtils.closeQuietly(resp);
		}

		fromDB = ourClient.read().resource(Binary.class).withId(resource.toVersionless()).execute();
		assertEquals("3", fromDB.getId().getVersionIdPart());

		// Now an update with the wrong ID in the body

		arr[0] = 4;
		binary.setContent(arr);
		binary.setId("");
		encoded = myFhirContext.newJsonParser().encodeResourceToString(binary);
		putRequest = new HttpPut(ourServerBase + "/Binary/" + resource.getIdPart());
		putRequest.setEntity(new StringEntity(encoded, ContentType.parse("application/json+fhir")));
		resp = ourHttpClient.execute(putRequest);
		try {
			assertEquals(400, resp.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(resp);
		}

		fromDB = ourClient.read().resource(Binary.class).withId(resource.toVersionless()).execute();
		assertEquals("3", fromDB.getId().getVersionIdPart());

	}

	@Test
	public void testCreateResourceConditional() throws IOException {
		String methodName = "testCreateResourceConditional";

		Patient pt = new Patient();
		pt.addName().addFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

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
	public void testCreateResourceConditionalComplex() throws IOException {
		Patient pt = new Patient();
		pt.addIdentifier().setSystem("http://general-hospital.co.uk/Identifiers").setValue("09832345234543876876");
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.addHeader(Constants.HEADER_IF_NONE_EXIST, "Patient?identifier=http://general-hospital.co.uk/Identifiers|09832345234543876876");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		IdDt id;
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id = new IdDt(newIdString);
		} finally {
			response.close();
		}

		IdDt id2;
		response = ourHttpClient.execute(post);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id2 = new IdDt(newIdString);
		} finally {
			response.close();
		}

//		//@formatter:off
//		IIdType id3 = ourClient
//			.update()
//			.resource(pt)
//			.conditionalByUrl("Patient?identifier=http://general-hospital.co.uk/Identifiers|09832345234543876876")
//			.execute().getId();
//		//@formatter:on

		assertEquals(id.getValue(), id2.getValue());
	}

	@Test
	public void testCreateResourceReturnsRepresentationByDefault() throws IOException {
		String resource = "<Patient xmlns=\"http://hl7.org/fhir\"></Patient>";

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String respString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response.toString());
			ourLog.info(respString);
			assertThat(respString, startsWith("<Patient xmlns=\"http://hl7.org/fhir\">"));
			assertThat(respString, endsWith("</Patient>"));
			//assertThat(respString, containsString("<OperationOutcome xmlns=\"http://hl7.org/fhir\">"));
		} finally {
			response.getEntity().getContent().close();
			response.close();
		}
	}
	
	@Test
	public void testCreateResourceReturnsOperationOutcome() throws IOException {
		String resource = "<Patient xmlns=\"http://hl7.org/fhir\"></Patient>";

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String respString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response.toString());
			ourLog.info(respString);
			assertThat(respString, containsString("<OperationOutcome xmlns=\"http://hl7.org/fhir\">"));
		} finally {
			response.getEntity().getContent().close();
			response.close();
		}
	}

	@Test
	public void testCreateResourceWithNumericId() throws IOException {
		String resource = "<Patient xmlns=\"http://hl7.org/fhir\"></Patient>";

		HttpPost post = new HttpPost(ourServerBase + "/Patient/2");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertEquals(400, response.getStatusLine().getStatusCode());
			OperationOutcome oo = myFhirContext.newXmlParser().parseResource(OperationOutcome.class, responseString);
			assertEquals(Msg.code(365) + "Can not create resource with ID \"2\", ID must not be supplied on a create (POST) operation (use an HTTP PUT / update operation if you wish to supply an ID)",
				oo.getIssue().get(0).getDiagnostics());
		} finally {
			response.getEntity().getContent().close();
			response.close();
		}
	}

	// private void delete(String theResourceType, String theParamName, String theParamValue) {
	// Bundle resources;
	// do {
	// IQuery<Bundle> forResource = ourClient.search().forResource(theResourceType);
	// if (theParamName != null) {
	// forResource = forResource.where(new StringClientParam(theParamName).matches().value(theParamValue));
	// }
	// resources = forResource.execute();
	// for (IResource next : resources.toListOfResources()) {
	// ourLog.info("Deleting resource: {}", next.getId());
	// ourClient.delete().resource(next).execute();
	// }
	// } while (resources.size() > 0);
	// }
	//
	// private void deleteToken(String theResourceType, String theParamName, String theParamSystem, String theParamValue)
	// {
	// Bundle resources = ourClient.search().forResource(theResourceType).where(new
	// TokenClientParam(theParamName).exactly().systemAndCode(theParamSystem, theParamValue)).execute();
	// for (IResource next : resources.toListOfResources()) {
	// ourLog.info("Deleting resource: {}", next.getId());
	// ourClient.delete().resource(next).execute();
	// }
	// }

	@Test
	public void testCreateWithForcedId() {
		String methodName = "testCreateWithForcedId";

		Patient p = new Patient();
		p.addName().addFamily(methodName);
		p.setId(methodName);

		IIdType optId = ourClient.update().resource(p).execute().getId();
		assertEquals(methodName, optId.getIdPart());
		assertEquals("1", optId.getVersionIdPart());
	}

	@Test
	public void testDeepChaining() {
		Location l1 = new Location();
		l1.getNameElement().setValue("testDeepChainingL1");
		IIdType l1id = ourClient.create().resource(l1).execute().getId();

		Location l2 = new Location();
		l2.getNameElement().setValue("testDeepChainingL2");
		l2.getPartOf().setReference(l1id.toVersionless().toUnqualified());
		IIdType l2id = ourClient.create().resource(l2).execute().getId();

		Encounter e1 = new Encounter();
		e1.addIdentifier().setSystem("urn:foo").setValue("testDeepChainingE1");
		e1.getStatusElement().setValueAsEnum(EncounterStateEnum.IN_PROGRESS);
		e1.getClassElementElement().setValueAsEnum(EncounterClassEnum.HOME);
		ca.uhn.fhir.model.dstu2.resource.Encounter.Location location = e1.addLocation();
		location.getLocation().setReference(l2id.toUnqualifiedVersionless());
		location.setPeriod(new PeriodDt().setStartWithSecondsPrecision(new Date()).setEndWithSecondsPrecision(new Date()));
		IIdType e1id = ourClient.create().resource(e1).execute().getId();

		Bundle res = ourClient.search()
			.forResource(Encounter.class)
			.where(Encounter.IDENTIFIER.exactly().systemAndCode("urn:foo", "testDeepChainingE1"))
			.include(Encounter.INCLUDE_LOCATION.asRecursive())
			.include(Location.INCLUDE_PARTOF.asRecursive())
			.returnBundle(Bundle.class)
			.execute();

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(res));

		assertEquals(3, res.getEntry().size());
		assertEquals(1, BundleUtil.toListOfResourcesOfType(myFhirContext, res, Encounter.class).size());
		assertEquals(e1id.toUnqualifiedVersionless(), BundleUtil.toListOfResourcesOfType(myFhirContext, res, Encounter.class).get(0).getIdElement().toUnqualifiedVersionless());

	}

	@Test
	public void testDeleteConditionalMultiple() {
		String methodName = "testDeleteConditionalMultiple";

		myDaoConfig.setAllowMultipleDelete(false);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("FAM1");
		IIdType id1 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("FAM2");
		IIdType id2 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		try {
			//@formatter:off
			ourClient
				.delete()
				.resourceConditionalByType(Patient.class)
				.where(Patient.IDENTIFIER.exactly().code(methodName))
				.execute();
			//@formatter:on
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals("HTTP 412 Precondition Failed: " + Msg.code(962) + "Failed to DELETE resource with match URL \"Patient?identifier=testDeleteConditionalMultiple\" because this search matched 2 resources",
				e.getMessage());
		}

		// Not deleted yet..
		ourClient.read().resource("Patient").withId(id1).execute();
		ourClient.read().resource("Patient").withId(id2).execute();

		myDaoConfig.setAllowMultipleDelete(true);

		//@formatter:off
		ourClient
			.delete()
			.resourceConditionalByType(Patient.class)
			.where(Patient.IDENTIFIER.exactly().code(methodName))
			.execute();
		//@formatter:on

		try {
			ourClient.read().resource("Patient").withId(id1).execute();
			fail();
		} catch (ResourceGoneException e) {
			// good
		}
		try {
			ourClient.read().resource("Patient").withId(id2).execute();
			fail();
		} catch (ResourceGoneException e) {
			// good
		}
	}

	@Test
	public void testDeleteInvalidReference() throws IOException {
		HttpDelete delete = new HttpDelete(ourServerBase + "/Patient");
		CloseableHttpResponse response = ourHttpClient.execute(delete);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertEquals(400, response.getStatusLine().getStatusCode());
			assertThat(responseString, containsString("Can not perform delete, no ID provided"));
		} finally {
			response.close();
		}
	}

	@Test
	public void testDeleteResourceConditional1() throws IOException {
		String methodName = "testDeleteResourceConditional1";

		Patient pt = new Patient();
		pt.addName().addFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

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
			assertEquals(200, response.getStatusLine().getStatusCode());
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
	public void testDeleteResourceConditional2() throws Exception {
		String methodName = "testDeleteResourceConditional2";

		Patient pt = new Patient();
		pt.addName().addFamily(methodName);
		pt.addIdentifier().setSystem("http://ghh.org/patient").setValue(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

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
		 * Try it with a raw socket call. The Apache client won't let us use the unescaped "|" in the URL but we want to
		 * make sure that works too..
		 */
		Socket sock = new Socket();
		sock.setSoTimeout(3000);
		try {
			sock.connect(new InetSocketAddress("localhost", ourPort));
			sock.getOutputStream().write(("DELETE /fhir/context/Patient?identifier=http://ghh.org/patient|" + methodName + " HTTP/1.1\n").getBytes("UTF-8"));
			sock.getOutputStream().write("Host: localhost\n".getBytes("UTF-8"));
			sock.getOutputStream().write("\n".getBytes("UTF-8"));

			BufferedReader socketInput = new BufferedReader(new InputStreamReader(sock.getInputStream()));

			// String response = "";
			StringBuilder b = new StringBuilder();
			char[] buf = new char[1000];
			while (socketInput.read(buf) != -1) {
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
	 * See issue #52
	 */
	@Test
	public void testDiagnosticOrderResources() {
		IGenericClient client = ourClient;

		int initialSize = client
			.search()
			.forResource(DiagnosticOrder.class)
			.returnBundle(Bundle.class)
			.execute()
			.getEntry()
			.size();

		DiagnosticOrder res = new DiagnosticOrder();
		res.addIdentifier().setSystem("urn:foo").setValue("123");

		client.create().resource(res).execute();

		int newSize = client
			.search()
			.forResource(DiagnosticOrder.class)
			.returnBundle(Bundle.class)
			.execute()
			.getEntry()
			.size();

		assertEquals(1, newSize - initialSize);

	}

	/**
	 * See issue #52
	 */
	@Test
	public void testDocumentManifestResources() throws Exception {
		myFhirContext.getResourceDefinition(Practitioner.class);
		myFhirContext.getResourceDefinition(DocumentManifest.class);

		IGenericClient client = ourClient;

		int initialSize = client
			.search()
			.forResource(DocumentManifest.class)
			.returnBundle(Bundle.class)
			.execute()
			.getEntry()
			.size();

		String resBody = IOUtils.toString(ResourceProviderDstu2Test.class.getResource("/documentmanifest.json"), StandardCharsets.UTF_8);
		client.create().resource(resBody).execute();

		int newSize = client
			.search()
			.forResource(DocumentManifest.class)
			.returnBundle(Bundle.class)
			.execute()
			.getEntry()
			.size();

		assertEquals(1, newSize - initialSize);

	}

	/**
	 * See issue #52
	 */
	@Test
	public void testDocumentReferenceResources() throws Exception {
		IGenericClient client = ourClient;

		int initialSize = client
			.search()
			.forResource(DocumentReference.class)
			.returnBundle(Bundle.class)
			.execute()
			.getEntry()
			.size();

		String resBody = IOUtils.toString(ResourceProviderDstu2Test.class.getResource("/documentreference.json"), Charsets.UTF_8);
		client.create().resource(resBody).execute();

		int newSize = client
			.search()
			.forResource(DocumentReference.class)
			.returnBundle(Bundle.class)
			.execute()
			.getEntry()
			.size();

		assertEquals(1, newSize - initialSize);

	}

	@Test
	public void testEverythingEncounterInstance() {
		String methodName = "testEverythingEncounterInstance";

		Organization org1parent = new Organization();
		org1parent.setId("org1parent");
		org1parent.setName(methodName + "1parent");
		IIdType orgId1parent = ourClient.update().resource(org1parent).execute().getId().toUnqualifiedVersionless();

		Organization org1 = new Organization();
		org1.setName(methodName + "1");
		org1.getPartOf().setReference(orgId1parent);
		IIdType orgId1 = ourClient.create().resource(org1).execute().getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addName().addFamily(methodName);
		p.getManagingOrganization().setReference(orgId1);
		IIdType patientId = ourClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		Organization org2 = new Organization();
		org2.setName(methodName + "1");
		IIdType orgId2 = ourClient.create().resource(org2).execute().getId().toUnqualifiedVersionless();

		Device dev = new Device();
		dev.setModel(methodName);
		dev.getOwner().setReference(orgId2);
		IIdType devId = ourClient.create().resource(dev).execute().getId().toUnqualifiedVersionless();

		Location locParent = new Location();
		locParent.setName(methodName + "Parent");
		IIdType locPId = ourClient.create().resource(locParent).execute().getId().toUnqualifiedVersionless();

		Location locChild = new Location();
		locChild.setName(methodName);
		locChild.getPartOf().setReference(locPId);
		IIdType locCId = ourClient.create().resource(locChild).execute().getId().toUnqualifiedVersionless();

		Encounter encU = new Encounter();
		encU.getPatient().setReference(patientId);
		encU.addLocation().getLocation().setReference(locCId);
		IIdType encUId = ourClient.create().resource(encU).execute().getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.getPatient().setReference(patientId);
		enc.addLocation().getLocation().setReference(locCId);
		IIdType encId = ourClient.create().resource(enc).execute().getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getSubject().setReference(patientId);
		obs.getDevice().setReference(devId);
		obs.getEncounter().setReference(encId);
		IIdType obsId = ourClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();

		ourLog.info("IDs: EncU:" + encUId.getIdPart() + " Enc:" + encId.getIdPart() + "  " + patientId.toUnqualifiedVersionless());

		Parameters output = ourClient.operation().onInstance(encId).named("everything").withNoParameters(Parameters.class).execute();
		ca.uhn.fhir.model.dstu2.resource.Bundle b = (ca.uhn.fhir.model.dstu2.resource.Bundle) output.getParameterFirstRep().getResource();
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);
		assertThat(ids, containsInAnyOrder(patientId, encId, orgId1, orgId2, orgId1parent, locPId, locCId, obsId, devId));
		assertThat(ids, not(containsInRelativeOrder(encUId)));

		ourLog.info(ids.toString());
	}

	@Test
	public void testEverythingEncounterType() {
		String methodName = "testEverythingEncounterInstance";

		Organization org1parent = new Organization();
		org1parent.setId("org1parent");
		org1parent.setName(methodName + "1parent");
		IIdType orgId1parent = ourClient.update().resource(org1parent).execute().getId().toUnqualifiedVersionless();

		Organization org1 = new Organization();
		org1.setName(methodName + "1");
		org1.getPartOf().setReference(orgId1parent);
		IIdType orgId1 = ourClient.create().resource(org1).execute().getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addName().addFamily(methodName);
		p.getManagingOrganization().setReference(orgId1);
		IIdType patientId = ourClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		Organization org2 = new Organization();
		org2.setName(methodName + "1");
		IIdType orgId2 = ourClient.create().resource(org2).execute().getId().toUnqualifiedVersionless();

		Device dev = new Device();
		dev.setModel(methodName);
		dev.getOwner().setReference(orgId2);
		IIdType devId = ourClient.create().resource(dev).execute().getId().toUnqualifiedVersionless();

		Location locParent = new Location();
		locParent.setName(methodName + "Parent");
		IIdType locPId = ourClient.create().resource(locParent).execute().getId().toUnqualifiedVersionless();

		Location locChild = new Location();
		locChild.setName(methodName);
		locChild.getPartOf().setReference(locPId);
		IIdType locCId = ourClient.create().resource(locChild).execute().getId().toUnqualifiedVersionless();

		Encounter encU = new Encounter();
		encU.addIdentifier().setValue(methodName);
		IIdType encUId = ourClient.create().resource(encU).execute().getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.getPatient().setReference(patientId);
		enc.addLocation().getLocation().setReference(locCId);
		IIdType encId = ourClient.create().resource(enc).execute().getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getSubject().setReference(patientId);
		obs.getDevice().setReference(devId);
		obs.getEncounter().setReference(encId);
		IIdType obsId = ourClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();

		Parameters output = ourClient.operation().onType(Encounter.class).named("everything").withNoParameters(Parameters.class).execute();
		ca.uhn.fhir.model.dstu2.resource.Bundle b = (ca.uhn.fhir.model.dstu2.resource.Bundle) output.getParameterFirstRep().getResource();
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);
		assertThat(ids, containsInAnyOrder(patientId, encUId, encId, orgId1, orgId2, orgId1parent, locPId, locCId, obsId, devId));

		ourLog.info(ids.toString());
	}

	@Test
	public void testEverythingInstanceWithContentFilter() {
		Patient pt1 = new Patient();
		pt1.addName().addFamily("Everything").addGiven("Arthur");
		IIdType ptId1 = myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.addName().addFamily("Everything").addGiven("Arthur");
		IIdType ptId2 = myPatientDao.create(pt2, mySrd).getId().toUnqualifiedVersionless();

		Device dev1 = new Device();
		dev1.setManufacturer("Some Manufacturer");
		IIdType devId1 = myDeviceDao.create(dev1, mySrd).getId().toUnqualifiedVersionless();

		Device dev2 = new Device();
		dev2.setManufacturer("Some Manufacturer 2");
		myDeviceDao.create(dev2, mySrd).getId().toUnqualifiedVersionless();

		Observation obs1 = new Observation();
		obs1.getText().setDiv("<div>OBSTEXT1</div>");
		obs1.getSubject().setReference(ptId1);
		obs1.getCode().addCoding().setCode("CODE1");
		obs1.setValue(new StringDt("obsvalue1"));
		obs1.getDevice().setReference(devId1);
		IIdType obsId1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

		IIdType obsId2;
		Observation obs2 = new Observation();
		obs2.getSubject().setReference(ptId1);
		obs2.getCode().addCoding().setCode("CODE2");
		obs2.setValue(new StringDt("obsvalue2"));
		obsId2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

		Observation obs3 = new Observation();
		obs3.getSubject().setReference(ptId2);
		obs3.getCode().addCoding().setCode("CODE3");
		obs3.setValue(new StringDt("obsvalue3"));
		IIdType obsId3 = myObservationDao.create(obs3, mySrd).getId().toUnqualifiedVersionless();

		List<IIdType> actual;
		StringAndListParam param;

		ourLog.info("Pt1:{} Pt2:{} Obs1:{} Obs2:{} Obs3:{}", ptId1.getIdPart(), ptId2.getIdPart(), obsId1.getIdPart(), obsId2.getIdPart(), obsId3.getIdPart());

		param = new StringAndListParam();
		param.addAnd(new StringOrListParam().addOr(new StringParam("obsvalue1")));

		Parameters response = ourClient
			.operation()
			.onInstance(ptId1)
			.named("everything")
			.withParameter(Parameters.class, Constants.PARAM_CONTENT, new StringDt("obsvalue1"))
			.execute();

		actual = toUnqualifiedVersionlessIds((ca.uhn.fhir.model.dstu2.resource.Bundle) response.getParameter().get(0).getResource());
		assertThat(actual.toString(), actual, containsInAnyOrder(ptId1, obsId1, devId1));

	}

	/**
	 * See #147
	 */
	@Test
	public void testEverythingPatientDoesntRepeatPatient() {
		ca.uhn.fhir.model.dstu2.resource.Bundle b;
		b = myFhirContext.newJsonParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, new InputStreamReader(ResourceProviderDstu2Test.class.getResourceAsStream("/bug147-bundle.json")));

		ca.uhn.fhir.model.dstu2.resource.Bundle resp = ourClient.transaction().withBundle(b).execute();
		List<IdDt> ids = new ArrayList<IdDt>();
		for (Entry next : resp.getEntry()) {
			IdDt toAdd = new IdDt(next.getResponse().getLocation()).toUnqualifiedVersionless();
			ids.add(toAdd);
		}
		ourLog.info("Created: " + ids.toString());

		IdDt patientId = new IdDt(resp.getEntry().get(0).getResponse().getLocation());
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

			assertFalse(dupes, ids.toString());
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

			assertFalse(dupes, ids.toString());
			assertThat(ids.toString(), containsString("Condition"));
			assertThat(ids.size(), greaterThan(10));
		}
	}

	/**
	 * Test for #226
	 */
	@Test
	public void testEverythingPatientIncludesBackReferences() {
		String methodName = "testEverythingIncludesBackReferences";

		Medication med = new Medication();
		med.getCode().setText(methodName);
		IIdType medId = myMedicationDao.create(med, mySrd).getId().toUnqualifiedVersionless();

		Patient pat = new Patient();
		pat.addAddress().addLine(methodName);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		MedicationOrder mo = new MedicationOrder();
		mo.getPatient().setReference(patId);
		mo.setMedication(new ResourceReferenceDt(medId));
		IIdType moId = myMedicationOrderDao.create(mo, mySrd).getId().toUnqualifiedVersionless();

		Parameters output = ourClient.operation().onInstance(patId).named("everything").withNoParameters(Parameters.class).execute();
		ca.uhn.fhir.model.dstu2.resource.Bundle b = (ca.uhn.fhir.model.dstu2.resource.Bundle) output.getParameterFirstRep().getResource();
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);
		ourLog.info(ids.toString());
		assertThat(ids, containsInAnyOrder(patId, medId, moId));
	}

	/**
	 * See #148
	 */
	@Test
	public void testEverythingPatientIncludesCondition() {
		ca.uhn.fhir.model.dstu2.resource.Bundle b = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		Patient p = new Patient();
		p.setId("1");
		b.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST);

		Condition c = new Condition();
		c.getPatient().setReference("Patient/1");
		b.addEntry().setResource(c).getRequest().setMethod(HTTPVerbEnum.POST);

		ca.uhn.fhir.model.dstu2.resource.Bundle resp = ourClient.transaction().withBundle(b).execute();

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));

		IdDt patientId = new IdDt(resp.getEntry().get(0).getResponse().getLocation());
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
	public void testEverythingPatientOperation() {
		String methodName = "testEverythingOperation";

		Organization org1parent = new Organization();
		org1parent.setId("org1parent");
		org1parent.setName(methodName + "1parent");
		IIdType orgId1parent = ourClient.update().resource(org1parent).execute().getId().toUnqualifiedVersionless();

		Organization org1 = new Organization();
		org1.setName(methodName + "1");
		org1.getPartOf().setReference(orgId1parent);
		IIdType orgId1 = ourClient.create().resource(org1).execute().getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addName().addFamily(methodName);
		p.getManagingOrganization().setReference(orgId1);
		IIdType patientId = ourClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		Organization org2 = new Organization();
		org2.setName(methodName + "1");
		IIdType orgId2 = ourClient.create().resource(org2).execute().getId().toUnqualifiedVersionless();

		Device dev = new Device();
		dev.setModel(methodName);
		dev.getOwner().setReference(orgId2);
		IIdType devId = ourClient.create().resource(dev).execute().getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getSubject().setReference(patientId);
		obs.getDevice().setReference(devId);
		IIdType obsId = ourClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.getPatient().setReference(patientId);
		IIdType encId = ourClient.create().resource(enc).execute().getId().toUnqualifiedVersionless();

		Parameters output = ourClient.operation().onInstance(patientId).named("everything").withNoParameters(Parameters.class).execute();
		ca.uhn.fhir.model.dstu2.resource.Bundle b = (ca.uhn.fhir.model.dstu2.resource.Bundle) output.getParameterFirstRep().getResource();
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);
		assertThat(ids, containsInAnyOrder(patientId, devId, obsId, encId, orgId1, orgId2, orgId1parent));

		ourLog.info(ids.toString());
	}

	@Test
	public void testEverythingPatientType() {
		String methodName = "testEverythingPatientType";

		Organization o1 = new Organization();
		o1.setName(methodName + "1");
		IIdType o1Id = ourClient.create().resource(o1).execute().getId().toUnqualifiedVersionless();
		Organization o2 = new Organization();
		o2.setName(methodName + "2");
		IIdType o2Id = ourClient.create().resource(o2).execute().getId().toUnqualifiedVersionless();

		Patient p1 = new Patient();
		p1.addName().addFamily(methodName + "1");
		p1.getManagingOrganization().setReference(o1Id);
		IIdType p1Id = ourClient.create().resource(p1).execute().getId().toUnqualifiedVersionless();
		Patient p2 = new Patient();
		p2.addName().addFamily(methodName + "2");
		p2.getManagingOrganization().setReference(o2Id);
		IIdType p2Id = ourClient.create().resource(p2).execute().getId().toUnqualifiedVersionless();

		Condition c1 = new Condition();
		c1.getPatient().setReference(p1Id);
		IIdType c1Id = ourClient.create().resource(c1).execute().getId().toUnqualifiedVersionless();
		Condition c2 = new Condition();
		c2.getPatient().setReference(p2Id);
		IIdType c2Id = ourClient.create().resource(c2).execute().getId().toUnqualifiedVersionless();

		Condition c3 = new Condition();
		c3.addIdentifier().setValue(methodName + "3");
		IIdType c3Id = ourClient.create().resource(c3).execute().getId().toUnqualifiedVersionless();

		Parameters output = ourClient.operation().onType(Patient.class).named("everything").withNoParameters(Parameters.class).execute();
		ca.uhn.fhir.model.dstu2.resource.Bundle b = (ca.uhn.fhir.model.dstu2.resource.Bundle) output.getParameterFirstRep().getResource();
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);

		assertThat(ids, containsInAnyOrder(o1Id, o2Id, p1Id, p2Id, c1Id, c2Id));
		assertThat(ids, not(containsInRelativeOrder(c3Id)));
	}

	// retest
	@Test
	public void testEverythingPatientWithLastUpdatedAndSort() throws Exception {
		String methodName = "testEverythingWithLastUpdatedAndSort";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType oId = ourClient.create().resource(org).execute().getId().toUnqualifiedVersionless();

		long time1 = System.currentTimeMillis();
		Thread.sleep(10);

		Patient p = new Patient();
		p.addName().addFamily(methodName);
		p.getManagingOrganization().setReference(oId);
		IIdType pId = ourClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		long time2 = System.currentTimeMillis();
		Thread.sleep(10);

		Condition c = new Condition();
		c.getCode().setText(methodName);
		c.getPatient().setReference(pId);
		IIdType cId = ourClient.create().resource(c).execute().getId().toUnqualifiedVersionless();

		Thread.sleep(10);
		long time3 = System.currentTimeMillis();

		// %3E=> %3C=<

		HttpGet get = new HttpGet(ourServerBase + "/Patient/" + pId.getIdPart() + "/$everything?_lastUpdated=%3E" + new InstantDt(new Date(time1)).getValueAsString());
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			IOUtils.closeQuietly(response.getEntity().getContent());
			ourLog.info(output);
			List<IdDt> ids = toIdListUnqualifiedVersionless(myFhirContext.newXmlParser().parseResource(Bundle.class, output));
			ourLog.info(ids.toString());
			assertThat(ids, containsInAnyOrder(pId, cId, oId));
		} finally {
			response.close();
		}

		get = new HttpGet(ourServerBase + "/Patient/" + pId.getIdPart() + "/$everything?_lastUpdated=%3E" + new InstantDt(new Date(time2)).getValueAsString() + "&_lastUpdated=%3C"
			+ new InstantDt(new Date(time3)).getValueAsString());
		response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			IOUtils.closeQuietly(response.getEntity().getContent());
			ourLog.info(output);
			List<IdDt> ids = toIdListUnqualifiedVersionless(myFhirContext.newXmlParser().parseResource(Bundle.class, output));
			ourLog.info(ids.toString());
			assertThat(ids, containsInAnyOrder(pId, cId, oId));
		} finally {
			response.close();
		}

		/*
		 * Sorting is not working since the performance enhancements in 2.4 but
		 * sorting for lastupdated is non-standard anyhow.. Hopefully at some point
		 * we can bring this back
		 */
//		get = new HttpGet(ourServerBase + "/Patient/" + pId.getIdPart() + "/$everything?" + "_sort=_lastUpdated");
//		response = ourHttpClient.execute(get);
//		try {
//			assertEquals(200, response.getStatusLine().getStatusCode());
//			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
//			IOUtils.closeQuietly(response.getEntity().getContent());
//			ourLog.info(output);
//			List<IdDt> ids = toIdListUnqualifiedVersionless(myFhirCtx.newXmlParser().parseBundle(output));
//			ourLog.info(ids.toString());
//			assertThat(ids, contains(pId, cId));
//		} finally {
//			response.close();
//		}
//
//		get = new HttpGet(ourServerBase + "/Patient/" + pId.getIdPart() + "/$everything?_sort:desc=_lastUpdated");
//		response = ourHttpClient.execute(get);
//		try {
//			assertEquals(200, response.getStatusLine().getStatusCode());
//			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
//			IOUtils.closeQuietly(response.getEntity().getContent());
//			ourLog.info(output);
//			List<IdDt> ids = toIdListUnqualifiedVersionless(myFhirCtx.newXmlParser().parseBundle(output));
//			ourLog.info(ids.toString());
//			assertThat(ids, contains(cId, pId, oId));
//		} finally {
//			response.close();
//		}

	}

	@Test
	public void testForResourcesWithProfile() {
		Organization o1 = new Organization();
		o1.setName("testSearchByResourceChainName01");
		o1.getMeta().addProfile("http://profile1").addProfile("http://profile2");
		IdDt o1id = (IdDt) ourClient.create().resource(o1).execute().getId().toUnqualifiedVersionless();
		Organization o2 = new Organization();
		o2.setName("testSearchByResourceChainName02");
		o2.getMeta().addProfile("http://profile1").addProfile("http://profile3");
		IdDt o2id = (IdDt) ourClient.create().resource(o2).execute().getId().toUnqualifiedVersionless();

		Bundle actual = ourClient.search()
			.forResource(Organization.class)
			.withProfile("http://profile1")
			.withProfile("http://profileX")
			.encodedJson()
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(Collections.emptyList(), actual.getEntry(), "nothing matches profile x");

		actual = ourClient.search()
			.forResource(Organization.class)
			.withProfile("http://profile1")
			.withProfile("http://profile2")
			.returnBundle(Bundle.class)
			.encodedJson()
			.prettyPrint()
			.execute();

		Set<String> expectedIds = new HashSet<String>();
		expectedIds.add(o1id.getIdPart());
		Set<String> actualIds = new HashSet<String>();
		for (Entry ele : actual.getEntry()) {
			actualIds.add(ele.getResource().getId().getIdPart());
		}
		assertEquals(expectedIds, actualIds, "Expects to retrieve the 1 orgination matching on Org1's profiles");

		actual = ourClient.search()
			.forResource(Organization.class)
			.withProfile("http://profile1")
			.withAnyProfile(Arrays.asList("http://profile3", "http://profile2"))
			.encodedJson()
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();

		expectedIds = new HashSet<String>();
		expectedIds.add(o1id.getIdPart());
		expectedIds.add(o2id.getIdPart());
		actualIds = new HashSet<String>();
		for (Entry ele : actual.getEntry()) {
			actualIds.add(ele.getResource().getId().getIdPart());
		}
		assertEquals(expectedIds, actualIds, "Expects to retrieve the 2 orginations, since we match on (the common profile AND (Org1's second profile OR org2's second profile))");
	}

	@Test
	public void testGetResourceCountsOperation() throws Exception {
		String methodName = "testMetaOperations";

		Patient pt = new Patient();
		pt.addName().addFamily(methodName);
		ourClient.create().resource(pt).execute().getId().toUnqualifiedVersionless();

		myResourceCountsCache.clear();
		myResourceCountsCache.update();

		HttpGet get = new HttpGet(ourServerBase + "/$get-resource-counts");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			IOUtils.closeQuietly(response.getEntity().getContent());
			ourLog.info(output);
			assertThat(output, containsString("<parameter><name value=\"Patient\"/><valueInteger value=\""));
		} finally {
			response.close();
		}
	}

	@Test
	public void testHistoryWithDeletedResource() {
		String methodName = "testHistoryWithDeletedResource";

		Patient patient = new Patient();
		patient.addName().addFamily(methodName);
		IIdType id = ourClient.create().resource(patient).execute().getId().toVersionless();
		ourClient.delete().resourceById(id).execute();
		patient.setId(id);
		ourClient.update().resource(patient).execute();

		ca.uhn.fhir.model.dstu2.resource.Bundle history = ourClient.history().onInstance(id).andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class).prettyPrint().summaryMode(SummaryEnum.DATA)
			.execute();
		assertEquals(3, history.getEntry().size());
		assertEquals(id.withVersion("3"), history.getEntry().get(0).getResource().getId());
		assertEquals(1, ((Patient) history.getEntry().get(0).getResource()).getName().size());

		assertEquals(id.withVersion("2"), history.getEntry().get(1).getResource().getId());
		assertEquals(HTTPVerbEnum.DELETE, history.getEntry().get(1).getRequest().getMethodElement().getValueAsEnum());
		assertEquals(0, ((Patient) history.getEntry().get(1).getResource()).getName().size());

		assertEquals(id.withVersion("1"), history.getEntry().get(2).getResource().getId());
		assertEquals(1, ((Patient) history.getEntry().get(2).getResource()).getName().size());
	}

	/**
	 * See issue #52
	 */
	@Test
	public void testImagingStudyResources() throws Exception {
		IGenericClient client = ourClient;

		int initialSize = client
			.search()
			.forResource(ImagingStudy.class)
			.returnBundle(Bundle.class)
			.execute()
			.getEntry()
			.size();

		String resBody = IOUtils.toString(ResourceProviderDstu2Test.class.getResource("/imagingstudy.json"), StandardCharsets.UTF_8);
		client.create().resource(resBody).execute();

		int newSize = client
			.search()
			.forResource(ImagingStudy.class)
			.returnBundle(Bundle.class)
			.execute()
			.getEntry()
			.size();

		assertEquals(1, newSize - initialSize);

	}

	@Test
	public void testMetaOperationWithNoMetaParameter() throws Exception {
		Patient p = new Patient();
		p.addName().addFamily("testMetaAddInvalid");
		IIdType id = ourClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		//@formatter:off
		String input = "<Parameters>\n" +
			"  <meta>\n" +
			"    <tag>\n" +
			"      <system value=\"http://example.org/codes/tags\"/>\n" +
			"      <code value=\"record-lost\"/>\n" +
			"      <display value=\"Patient File Lost\"/>\n" +
			"    </tag>\n" +
			"  </meta>\n" +
			"</Parameters>";
		//@formatter:on

		HttpPost post = new HttpPost(ourServerBase + "/Patient/" + id.getIdPart() + "/$meta-add");
		post.setEntity(new StringEntity(input, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(output);
			assertEquals(400, response.getStatusLine().getStatusCode());
			assertThat(output, containsString("Input contains no parameter with name 'meta'"));
		} finally {
			response.close();
		}

		post = new HttpPost(ourServerBase + "/Patient/" + id.getIdPart() + "/$meta-delete");
		post.setEntity(new StringEntity(input, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		response = ourHttpClient.execute(post);
		try {
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(output);
			assertEquals(400, response.getStatusLine().getStatusCode());
			assertThat(output, containsString("Input contains no parameter with name 'meta'"));
		} finally {
			response.close();
		}

	}

	@Test
	public void testMetaOperations() {
		String methodName = "testMetaOperations";

		Patient pt = new Patient();
		pt.addName().addFamily(methodName);
		IIdType id = ourClient.create().resource(pt).execute().getId().toUnqualifiedVersionless();

		MetaDt meta = ourClient.meta().get(MetaDt.class).fromResource(id).execute();
		assertEquals(0, meta.getTag().size());

		MetaDt inMeta = new MetaDt();
		inMeta.addTag().setSystem("urn:system1").setCode("urn:code1");
		meta = ourClient.meta().add().onResource(id).meta(inMeta).execute();
		assertEquals(1, meta.getTag().size());

		inMeta = new MetaDt();
		inMeta.addTag().setSystem("urn:system1").setCode("urn:code1");
		meta = ourClient.meta().delete().onResource(id).meta(inMeta).execute();
		assertEquals(0, meta.getTag().size());

	}

	@Test
	public void testMetadata() throws Exception {
		HttpGet get = new HttpGet(ourServerBase + "/metadata");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp, stringContainsInOrder("THIS IS THE DESC"));
		} finally {
			IOUtils.closeQuietly(response.getEntity().getContent());
			response.close();
		}
	}

	@Test
	public void testPagingOverEverythingSet() throws InterruptedException {
		Patient p = new Patient();
		p.setActive(true);
		String pid = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		for (int i = 0; i < 20; i++) {
			Observation o = new Observation();
			o.getSubject().setReference(pid);
			o.addIdentifier().setSystem("foo").setValue(Integer.toString(i));
			myObservationDao.create(o);
		}

		mySearchCoordinatorSvcRaw.setLoadingThrottleForUnitTests(50);
		mySearchCoordinatorSvcRaw.setSyncSizeForUnitTests(10);
		mySearchCoordinatorSvcRaw.setNeverUseLocalSearchForUnitTests(true);

		StopWatch sw = new StopWatch();
		ca.uhn.fhir.model.dstu2.resource.Bundle response = ourClient
			.operation()
			.onInstance(new IdDt(pid))
			.named("everything")
			.withSearchParameter(Parameters.class, "_count", new NumberParam(10))
			.returnResourceType(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.useHttpGet()
			.execute();

		assertEquals(10, response.getEntry().size());
		assertThat(response.getLink("next").getUrl(), not(emptyString()));

		// Load page 2

		String nextUrl = response.getLink("next").getUrl();
		response = ourClient.fetchResourceFromUrl(ca.uhn.fhir.model.dstu2.resource.Bundle.class, nextUrl);

		assertEquals(10, response.getEntry().size());
		assertThat(response.getLink("next").getUrl(), not(emptyString()));

		// Load page 3
		Thread.sleep(2000);

		nextUrl = response.getLink("next").getUrl();
		response = ourClient.fetchResourceFromUrl(ca.uhn.fhir.model.dstu2.resource.Bundle.class, nextUrl);

		assertEquals(1, response.getEntry().size());
//		assertEquals(21, response.getTotal().intValue());
//		assertEquals(null, response.getLink("next"));

		// Again

		response = ourClient.fetchResourceFromUrl(ca.uhn.fhir.model.dstu2.resource.Bundle.class, nextUrl);

		assertEquals(1, response.getEntry().size());
		assertEquals(21, response.getTotal().intValue());
		assertEquals(null, response.getLink("next"));
	}

	@Test
	public void testEverythingWithNoPagingProvider() {
		ourRestServer.setPagingProvider(null);

		Patient p = new Patient();
		p.setActive(true);
		String pid = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		for (int i = 0; i < 20; i++) {
			Observation o = new Observation();
			o.getSubject().setReference(pid);
			o.addIdentifier().setSystem("foo").setValue(Integer.toString(i));
			myObservationDao.create(o);
		}

		mySearchCoordinatorSvcRaw.setLoadingThrottleForUnitTests(50);
		mySearchCoordinatorSvcRaw.setSyncSizeForUnitTests(10);
		mySearchCoordinatorSvcRaw.setNeverUseLocalSearchForUnitTests(true);

		ca.uhn.fhir.model.dstu2.resource.Bundle response = ourClient
			.operation()
			.onInstance(new IdDt(pid))
			.named("everything")
			.withSearchParameter(Parameters.class, "_count", new NumberParam(10))
			.returnResourceType(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.useHttpGet()
			.execute();

		assertEquals(10, response.getEntry().size());
		assertEquals(null, response.getTotalElement().getValue());
		assertEquals(null, response.getLink("next"));
	}

	@Test
	public void testProcessMessage() {

		Bundle bundle = new Bundle();
		bundle.setType(BundleTypeEnum.MESSAGE);

		Parameters parameters = new Parameters();
		parameters.addParameter()
			.setName("content")
			.setResource(bundle);
		try {
			ourClient.operation().onServer().named(JpaConstants.OPERATION_PROCESS_MESSAGE).withParameters(parameters).execute();
			fail();
		} catch (NotImplementedOperationException e) {
			assertThat(e.getMessage(), containsString("This operation is not yet implemented on this server"));
		}

	}

	/**
	 * Test for issue #60
	 */
	@Test
	public void testReadAllInstancesOfType() {
		Patient pat;

		pat = new Patient();
		pat.addIdentifier().setSystem("urn:system").setValue("testReadAllInstancesOfType_01");
		ourClient.create().resource(pat).prettyPrint().encodedXml().execute().getId();

		pat = new Patient();
		pat.addIdentifier().setSystem("urn:system").setValue("testReadAllInstancesOfType_02");
		ourClient.create().resource(pat).prettyPrint().encodedXml().execute().getId();
		{
			Bundle returned = ourClient
				.search()
				.forResource(Patient.class)
				.returnBundle(Bundle.class)
				.encodedXml()
				.execute();
			assertThat(returned.getEntry().size(), greaterThan(1));
			assertEquals(ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum.SEARCH_RESULTS, returned.getTypeElement().getValueAsEnum());
		}
		{
			Bundle returned = ourClient
				.search()
				.forResource(Patient.class)
				.encodedJson()
				.returnBundle(Bundle.class)
				.execute();
			assertThat(returned.getEntry().size(), greaterThan(1));
		}
	}

	@Test
	public void testSaveAndRetrieveExistingNarrativeJson() {
		Patient p1 = new Patient();
		p1.getText().setStatus(ca.uhn.fhir.model.dstu2.valueset.NarrativeStatusEnum.GENERATED);
		p1.getText().getDiv().setValueAsString("<div>HELLO WORLD</div>");
		p1.addIdentifier().setSystem("urn:system").setValue("testSaveAndRetrieveExistingNarrative01");

		IIdType newId = ourClient.create().resource(p1).encodedJson().execute().getId();

		Patient actual = ourClient.read().resource(Patient.class).withId(newId).encodedJson().execute();
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">HELLO WORLD</div>", actual.getText().getDiv().getValueAsString());
	}

	@Test
	public void testSaveAndRetrieveExistingNarrativeXml() {
		Patient p1 = new Patient();
		p1.getText().setStatus(ca.uhn.fhir.model.dstu2.valueset.NarrativeStatusEnum.GENERATED);
		p1.getText().getDiv().setValueAsString("<div>HELLO WORLD</div>");
		p1.addIdentifier().setSystem("urn:system").setValue("testSaveAndRetrieveExistingNarrative01");

		IIdType newId = ourClient.create().resource(p1).encodedXml().execute().getId();

		Patient actual = ourClient.read().resource(Patient.class).withId(newId).encodedXml().execute();
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">HELLO WORLD</div>", actual.getText().getDiv().getValueAsString());
	}

	@Test
	public void testSaveAndRetrieveWithContained() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSaveAndRetrieveWithContained01");

		Organization o1 = new Organization();
		o1.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSaveAndRetrieveWithContained02");

		p1.getManagingOrganization().setResource(o1);

		IIdType newId = ourClient.create().resource(p1).execute().getId();

		Patient actual = ourClient.read(Patient.class, (UriDt) newId);
		assertEquals(1, actual.getContained().getContainedResources().size());
		assertThat(actual.getText().getDiv().getValueAsString(), containsString("<td>Identifier</td><td>testSaveAndRetrieveWithContained01</td>"));

		Bundle b = ourClient
			.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system:rpdstu2", "testSaveAndRetrieveWithContained01"))
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(1, b.getEntry().size());

	}

	@Test
	public void testSaveAndRetrieveWithoutNarrative() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByResourceChain01");

		IdDt newId = (IdDt) ourClient.create().resource(p1).execute().getId();

		Patient actual = ourClient.read(Patient.class, newId);
		assertThat(actual.getText().getDiv().getValueAsString(), containsString("<td>Identifier</td><td>testSearchByResourceChain01</td>"));
	}

	@Test
	public void testSearchBundleDoesntIncludeTextElement() throws Exception {
		HttpGet read = new HttpGet(ourServerBase + "/Patient?_format=json");
		CloseableHttpResponse response = ourHttpClient.execute(read);
		try {
			String text = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(text);
			assertEquals(Constants.STATUS_HTTP_200_OK, response.getStatusLine().getStatusCode());
			assertThat(text, not(containsString("\"text\",\"type\"")));
		} finally {
			response.close();
		}
	}

	@Test
	public void testSearchByIdOr() {
		IIdType id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id2 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		Bundle found = ourClient
			.search()
			.forResource(Patient.class)
			.where(BaseResource.RES_ID.matches().values(id1.getIdPart(), id2.getIdPart()))
			.and(BaseResource.RES_ID.matches().value(id1.getIdPart()))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(toIdListUnqualifiedVersionless(found), containsInAnyOrder(id1));
	}

	@Test
	public void testSearchByIdentifier() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByIdentifier01");
		p1.addName().addFamily("testSearchByIdentifierFamily01").addGiven("testSearchByIdentifierGiven01");
		IdDt p1Id = (IdDt) ourClient.create().resource(p1).execute().getId();

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue("testSearchByIdentifier02");
		p2.addName().addFamily("testSearchByIdentifierFamily01").addGiven("testSearchByIdentifierGiven02");
		ourClient.create().resource(p2).execute().getId();

		//@formatter:off
		ca.uhn.fhir.model.dstu2.resource.Bundle actual = ourClient
			.search()
			.forResource(Patient.class)
			.where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system", "testSearchByIdentifier01"))
			.encodedJson()
			.prettyPrint()
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();
		//@formatter:on

		assertEquals(1, actual.getEntry().size());
		assertEquals(ourServerBase + "/Patient/" + p1Id.getIdPart(), actual.getEntry().get(0).getFullUrl());
		assertEquals(p1Id.getIdPart(), actual.getEntry().get(0).getResource().getId().getIdPart());
		assertEquals(SearchEntryModeEnum.MATCH, actual.getEntry().get(0).getSearch().getModeElement().getValueAsEnum());
	}

	@Test
	public void testSearchByIdentifierWithoutSystem() {

		Patient p1 = new Patient();
		p1.addIdentifier().setValue("testSearchByIdentifierWithoutSystem01");
		IdDt p1Id = (IdDt) ourClient.create().resource(p1).execute().getId();

		Bundle actual = ourClient
			.search()
			.forResource(Patient.class)
			.where(Patient.IDENTIFIER.exactly().systemAndCode(null, "testSearchByIdentifierWithoutSystem01"))
			.encodedJson()
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(1, actual.getEntry().size());
		assertEquals(p1Id.getIdPart(), actual.getEntry().get(0).getResource().getId().getIdPart());

	}

	@Test
	public void testSearchByReferenceIds() {
		Organization o1 = new Organization();
		o1.setName("testSearchByResourceChainName01");
		IdDt o1id = (IdDt) ourClient.create().resource(o1).execute().getId().toUnqualifiedVersionless();
		Organization o2 = new Organization();
		o2.setName("testSearchByResourceChainName02");
		IdDt o2id = (IdDt) ourClient.create().resource(o2).execute().getId().toUnqualifiedVersionless();

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByReferenceIds01");
		p1.addName().addFamily("testSearchByReferenceIdsFamily01").addGiven("testSearchByReferenceIdsGiven01");
		p1.setManagingOrganization(new ResourceReferenceDt(o1id.toUnqualifiedVersionless()));
		IdDt p1Id = (IdDt) ourClient.create().resource(p1).execute().getId();

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue("testSearchByReferenceIds02");
		p2.addName().addFamily("testSearchByReferenceIdsFamily02").addGiven("testSearchByReferenceIdsGiven02");
		p2.setManagingOrganization(new ResourceReferenceDt(o2id.toUnqualifiedVersionless()));
		IdDt p2Id = (IdDt) ourClient.create().resource(p2).execute().getId();

		Bundle actual = ourClient.search()
			.forResource(Patient.class)
			.where(Patient.ORGANIZATION.hasAnyOfIds(Arrays.asList(o1id.getIdPart(), o2id.getIdPart())))
			.encodedJson()
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();

		Set<String> expectedIds = new HashSet<String>();
		expectedIds.add(p1Id.getIdPart());
		expectedIds.add(p2Id.getIdPart());
		Set<String> actualIds = new HashSet<String>();
		for (Entry ele : actual.getEntry()) {
			actualIds.add(ele.getResource().getId().getIdPart());
		}
		assertEquals(expectedIds, actualIds, "Expects to retrieve the 2 patients which reference the two different organizations");
	}

	@Test
	public void testSearchByResourceChain() {

		Organization o1 = new Organization();
		o1.setName("testSearchByResourceChainName01");
		IdDt o1id = (IdDt) ourClient.create().resource(o1).execute().getId().toUnqualifiedVersionless();

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByResourceChain01");
		p1.addName().addFamily("testSearchByResourceChainFamily01").addGiven("testSearchByResourceChainGiven01");
		p1.setManagingOrganization(new ResourceReferenceDt(o1id.toUnqualifiedVersionless()));
		IdDt p1Id = (IdDt) ourClient.create().resource(p1).execute().getId();

		Bundle actual = ourClient.search()
			.forResource(Patient.class)
			.where(Patient.ORGANIZATION.hasId(o1id.getIdPart()))
			.encodedJson()
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(1, actual.getEntry().size());
		assertEquals(p1Id.getIdPart(), actual.getEntry().get(0).getResource().getId().getIdPart());

		actual = ourClient
			.search()
			.forResource(Patient.class)
			.where(Patient.ORGANIZATION.hasId(o1id.getValue()))
			.encodedJson()
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(1, actual.getEntry().size());
		assertEquals(p1Id.getIdPart(), actual.getEntry().get(0).getResource().getId().getIdPart());

	}

	@Test
	public void testSearchLastUpdatedParamRp() throws InterruptedException {
		String methodName = "testSearchLastUpdatedParamRp";
		ourLog.info("Starting " + methodName);

		int sleep = 100;
		Thread.sleep(sleep);

		DateTimeDt beforeAny = new DateTimeDt(new Date(), TemporalPrecisionEnum.MILLI);
		IdDt id1a;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addFamily(methodName).addGiven("Joe");
			id1a = (IdDt) ourClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();
		}
		IdDt id1b;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addFamily(methodName + "XXXX").addGiven("Joe");
			id1b = (IdDt) ourClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();
		}

		Thread.sleep(1100);
		DateTimeDt beforeR2 = new DateTimeDt(new Date(), TemporalPrecisionEnum.MILLI);
		Thread.sleep(1100);

		IdDt id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addFamily(methodName).addGiven("John");
			id2 = (IdDt) ourClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();
		}

		ourLog.info("Before: {}", beforeAny.getValue());
		{
			Bundle found = ourClient
				.search()
				.forResource(Patient.class)
				.where(Patient.NAME.matches().value("testSearchLastUpdatedParamRp"))
				.lastUpdated(new DateRangeParam(beforeAny, null))
				.returnBundle(Bundle.class)
				.execute();
			List<IdDt> patients = toIdListUnqualifiedVersionless(found);
			assertThat(patients, hasItems(id1a, id1b, id2));
		}

		{
			Bundle found = ourClient.search()
				.forResource(Patient.class)
				.where(Patient.NAME.matches().value("testSearchLastUpdatedParamRp"))
				.returnBundle(Bundle.class)
				.execute();
			List<IdDt> patients = toIdListUnqualifiedVersionless(found);
			assertThat(patients, hasItems(id1a, id1b, id2));
		}

		{
			Bundle found = ourClient.search()
				.forResource(Patient.class)
				.where(Patient.NAME.matches().value("testSearchLastUpdatedParamRp"))
				.lastUpdated(new DateRangeParam(beforeR2, null))
				.returnBundle(Bundle.class)
				.execute();
			List<IdDt> patients = toIdListUnqualifiedVersionless(found);
			assertThat(patients, hasItems(id2));
			assertThat(patients, not(hasItems(id1a, id1b)));
		}
		{
			Bundle found = ourClient.search()
				.forResource(Patient.class)
				.where(Patient.NAME.matches().value("testSearchLastUpdatedParamRp"))
				.lastUpdated(new DateRangeParam(beforeAny, beforeR2))
				.returnBundle(Bundle.class)
				.execute();

			List<IdDt> patients = toIdListUnqualifiedVersionless(found);
			assertThat(patients.toString(), patients, not(hasItems(id2)));
			assertThat(patients.toString(), patients, (hasItems(id1a, id1b)));
		}
		{
			Bundle found = ourClient.search()
				.forResource(Patient.class)
				.where(Patient.NAME.matches().value("testSearchLastUpdatedParamRp"))
				.lastUpdated(new DateRangeParam(null, beforeR2))
				.returnBundle(Bundle.class)
				.execute();

			List<IdDt> patients = toIdListUnqualifiedVersionless(found);
			assertThat(patients, (hasItems(id1a, id1b)));
			assertThat(patients, not(hasItems(id2)));
		}
	}

	/**
	 * See #441
	 */
	@Test
	public void testSearchMedicationChain() throws Exception {
		Medication medication = new Medication();
		medication.getCode().addCoding().setSystem("SYSTEM").setCode("04823543");
		IIdType medId = myMedicationDao.create(medication).getId().toUnqualifiedVersionless();

		MedicationAdministration ma = new MedicationAdministration();
		ma.setMedication(new ResourceReferenceDt(medId));
		IIdType moId = myMedicationAdministrationDao.create(ma).getId().toUnqualifiedVersionless();

		HttpGet get = new HttpGet(ourServerBase + "/MedicationAdministration?medication.code=04823543");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString(moId.getIdPart()));
		} finally {
			response.close();
		}

	}

	private void testSearchReturnsResults(String search) throws IOException {
		int matches;
		HttpGet get = new HttpGet(ourServerBase + search);
		CloseableHttpResponse response = ourHttpClient.execute(get);
		String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(response.getEntity().getContent());
		ourLog.info(resp);
		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = myFhirContext.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, resp);
		matches = bundle.getEntry().size();

		assertThat(matches, greaterThan(0));
	}

	@Test
	public void testSearchReturnsSearchDate() throws Exception {
		ourLog.info("Starting testSearchReturnsSearchDate");

		Date before = new Date();
		Thread.sleep(100);

		ca.uhn.fhir.model.dstu2.resource.Bundle found = ourClient
			.search()
			.forResource(Patient.class)
			.prettyPrint()
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();

		Thread.sleep(100);
		Date after = new Date();

		InstantDt updated = ResourceMetadataKeyEnum.UPDATED.get(found);
		assertNotNull(updated);
		Date value = updated.getValue();
		assertNotNull(value);
		ourLog.info(value.getTime() + "");
		ourLog.info(before.getTime() + "");
		assertTrue(value.after(before));
		assertTrue(value.before(after));
	}

	@Test
	public void testSearchWithInclude() {
		Organization org = new Organization();
		org.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSearchWithInclude01");
		IdDt orgId = (IdDt) ourClient.create().resource(org).prettyPrint().encodedXml().execute().getId();

		Patient pat = new Patient();
		pat.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSearchWithInclude02");
		pat.getManagingOrganization().setReference(orgId.toUnqualifiedVersionless());
		ourClient.create().resource(pat).prettyPrint().encodedXml().execute().getId();

		Bundle found = ourClient
			.search()
			.forResource(Patient.class)
			.where(Patient.IDENTIFIER.exactly().systemAndIdentifier("urn:system:rpdstu2", "testSearchWithInclude02"))
			.include(Patient.INCLUDE_ORGANIZATION)
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(2, found.getEntry().size());
		assertEquals(Patient.class, found.getEntry().get(0).getResource().getClass());
		assertEquals(SearchEntryModeEnum.MATCH, found.getEntry().get(0).getSearch().getModeElement().getValueAsEnum());
		assertThat(found.getEntry().get(0).getResource().getText().getDiv().getValueAsString(), containsString("<table class=\"hapiPropertyTable"));
		assertEquals(Organization.class, found.getEntry().get(1).getResource().getClass());
		assertEquals(SearchEntryModeEnum.INCLUDE, found.getEntry().get(1).getSearch().getModeElement().getValueAsEnum());
	}

	@Test
	public void testOffsetSearchWithInclude() {
		Organization org = new Organization();
		org.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSearchWithInclude01");
		IdDt orgId = (IdDt) ourClient.create().resource(org).prettyPrint().encodedXml().execute().getId();

		Patient pat = new Patient();
		pat.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSearchWithInclude02");
		pat.getManagingOrganization().setReference(orgId.toUnqualifiedVersionless());
		ourClient.create().resource(pat).prettyPrint().encodedXml().execute().getId();

		Bundle found = ourClient
			.search()
			.forResource(Patient.class)
			.where(Patient.IDENTIFIER.exactly().systemAndIdentifier("urn:system:rpdstu2", "testSearchWithInclude02"))
			.include(Patient.INCLUDE_ORGANIZATION)
			.offset(0)
			.count(1)
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(2, found.getEntry().size());
		assertEquals(Patient.class, found.getEntry().get(0).getResource().getClass());
		assertEquals(SearchEntryModeEnum.MATCH, found.getEntry().get(0).getSearch().getModeElement().getValueAsEnum());
		assertThat(found.getEntry().get(0).getResource().getText().getDiv().getValueAsString(), containsString("<table class=\"hapiPropertyTable"));
		assertEquals(Organization.class, found.getEntry().get(1).getResource().getClass());
		assertEquals(SearchEntryModeEnum.INCLUDE, found.getEntry().get(1).getSearch().getModeElement().getValueAsEnum());
	}

	@Test
	public void testSearchWithCompositeSortWith_CodeValueQuantity() throws IOException {
		
		IIdType pid0;
		IIdType oid1;
		IIdType oid2;
		IIdType oid3;
		IIdType oid4;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addFamily("Tester").addGiven("Joe");
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference(pid0);
			obs.getCode().addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new QuantityDt().setValue(200));
			
			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
			
			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference(pid0);
			obs.getCode().addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new QuantityDt().setValue(300));
			
			oid2 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
			
			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference(pid0);
			obs.getCode().addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new QuantityDt().setValue(150));
			
			oid3 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
			
			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference(pid0);
			obs.getCode().addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new QuantityDt().setValue(250));
			
			oid4 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
			
			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		
		String uri = ourServerBase + "/Observation?_sort=code-value-quantity";
		Bundle found;
		
		HttpGet get = new HttpGet(uri);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			String output = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			found = myFhirContext.newXmlParser().parseResource(Bundle.class, output);
		}
		
		ourLog.info("Bundle: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(found));
		
		List<IIdType> list = toUnqualifiedVersionlessIds(found);
		assertEquals(4, found.getEntry().size());
		assertEquals(oid3, list.get(0));
		assertEquals(oid1, list.get(1));
		assertEquals(oid4, list.get(2));
		assertEquals(oid2, list.get(3));
	}
	
	@Test
	public void testSearchWithMissing() {
		ourLog.info("Starting testSearchWithMissing");

		String methodName = "testSearchWithMissing";

		Organization org = new Organization();
		IdDt deletedIdMissingTrue = (IdDt) ourClient.create().resource(org).execute().getId().toUnqualifiedVersionless();
		ourClient.delete().resourceById(deletedIdMissingTrue).execute();

		org = new Organization();
		org.setName("Help I'm a Bug");
		IdDt deletedIdMissingFalse = (IdDt) ourClient.create().resource(org).execute().getId().toUnqualifiedVersionless();
		ourClient.delete().resourceById(deletedIdMissingFalse).execute();

		List<IResource> resources = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			org = new Organization();
			org.setName(methodName + "_0" + i);
			resources.add(org);
		}
		ourClient.transaction().withResources(resources).prettyPrint().encodedXml().execute();

		org = new Organization();
		org.addIdentifier().setSystem("urn:system:rpdstu2").setValue(methodName + "01");
		org.setName(methodName + "name");
		IdDt orgNotMissing = (IdDt) ourClient.create().resource(org).prettyPrint().encodedXml().execute().getId().toUnqualifiedVersionless();

		org = new Organization();
		org.addIdentifier().setSystem("urn:system:rpdstu2").setValue(methodName + "01");
		IdDt orgMissing = (IdDt) ourClient.create().resource(org).prettyPrint().encodedXml().execute().getId().toUnqualifiedVersionless();

		{
			Bundle found = ourClient
				.search()
				.forResource(Organization.class)
				.where(Organization.NAME.isMissing(false))
				.count(100)
				.prettyPrint()
				.returnBundle(Bundle.class)
				.execute();

			List<IdDt> list = toIdListUnqualifiedVersionless(found);
			ourLog.info(methodName + ": " + list.toString());
			ourLog.info("Wanted " + orgNotMissing + " and not " + deletedIdMissingFalse + " but got " + list.size() + ": " + list);
			assertThat("Wanted " + orgNotMissing + " but got " + list.size() + ": " + list, list, containsInRelativeOrder(orgNotMissing));
			assertThat(list, not(containsInRelativeOrder(deletedIdMissingFalse)));
			assertThat(list, not(containsInRelativeOrder(orgMissing)));
		}

		Bundle found = ourClient
			.search()
			.forResource(Organization.class)
			.where(Organization.NAME.isMissing(true))
			.count(100)
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();

		List<IdDt> list = toIdListUnqualifiedVersionless(found);
		ourLog.info(methodName + " found: " + list.toString() + " - Wanted " + orgMissing + " but not " + orgNotMissing);
		assertThat(list, not(containsInRelativeOrder(orgNotMissing)));
		assertThat(list, not(containsInRelativeOrder(deletedIdMissingTrue)));
		assertThat("Wanted " + orgMissing + " but found: " + list, list, containsInRelativeOrder(orgMissing));
	}

	@Test
	public void testSearchWithMissing2() throws Exception {
		checkParamMissing(Observation.SP_CODE);
		checkParamMissing(Observation.SP_CATEGORY);
		checkParamMissing(Observation.SP_VALUE_STRING);
		checkParamMissing(Observation.SP_ENCOUNTER);
		checkParamMissing(Observation.SP_DATE);
	}

	@Test
	public void testSearchWithTextInexactMatch() throws Exception {
		Observation obs = new Observation();
		obs.getCode().setText("THIS_IS_THE_TEXT");
		obs.getCode().addCoding().setSystem("SYSTEM").setCode("CODE").setDisplay("THIS_IS_THE_DISPLAY");
		ourClient.create().resource(obs).execute();

		testSearchReturnsResults("/Observation?code%3Atext=THIS_IS_THE_TEXT");
		testSearchReturnsResults("/Observation?code%3Atext=THIS_IS_THE_");
		testSearchReturnsResults("/Observation?code%3Atext=this_is_the_");
		testSearchReturnsResults("/Observation?code%3Atext=THIS_IS_THE_DISPLAY");
		testSearchReturnsResults("/Observation?code%3Atext=THIS_IS_THE_disp");
	}

	@Test
	public void testSmallResultIncludes() {
		Patient p = new Patient();
		p.setId("p");
		p.setActive(true);
		ourClient.update().resource(p).execute();

		CarePlan cp = new CarePlan();
		cp.setId("cp");
		cp.getSubject().setResource(p);
		cp.addActivity().getDetail().getCode().addCoding().setSystem("FOO").setCode("BAR");
		ourClient.update().resource(cp).execute();

		Bundle b = ourClient
			.search()
			.forResource(CarePlan.class)
			.where(CarePlan.ACTIVITYCODE.exactly().systemAndCode("FOO", "BAR"))
			.sort().ascending(CarePlan.SP_ACTIVITYDATE)
			.include(CarePlan.INCLUDE_SUBJECT)
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(2, b.getEntry().size());

	}

	/**
	 * See #198
	 */
	@Test
	public void testSortFromResourceProvider() {
		Patient p;
		String methodName = "testSortFromResourceProvider";

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		ourClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Daniel").addFamily("Adams");
		ourClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Aaron").addFamily("Alexis");
		ourClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Carol").addFamily("Allen");
		ourClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Ruth").addFamily("Black");
		ourClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Brian").addFamily("Brooks");
		ourClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Susan").addFamily("Clark");
		ourClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Amy").addFamily("Clark");
		ourClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Anthony").addFamily("Coleman");
		ourClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Steven").addFamily("Coleman");
		ourClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Lisa").addFamily("Coleman");
		ourClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Ruth").addFamily("Cook");
		ourClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Betty").addFamily("Davis");
		ourClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Joshua").addFamily("Diaz");
		ourClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Brian").addFamily("Gracia");
		ourClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Stephan").addFamily("Graham");
		ourClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Sarah").addFamily("Graham");
		ourClient.create().resource(p).execute();

		Bundle resp = ourClient
			.search()
			.forResource(Patient.class)
			.where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system", methodName))
			.sort().ascending(Patient.FAMILY)
			.sort().ascending(Patient.GIVEN)
			.count(100)
			.returnBundle(Bundle.class)
			.execute();

		List<String> names = toNameList(resp);

		ourLog.info(StringUtils.join(names, '\n'));

		assertThat(names, contains( // this matches in order only
			"Daniel Adams",
			"Aaron Alexis",
			"Carol Allen",
			"Ruth Black",
			"Brian Brooks",
			"Amy Clark",
			"Susan Clark",
			"Anthony Coleman",
			"Lisa Coleman",
			"Steven Coleman",
			"Ruth Cook",
			"Betty Davis",
			"Joshua Diaz",
			"Brian Gracia",
			"Sarah Graham",
			"Stephan Graham"));

	}

	/**
	 * Test for issue #60
	 */
	@Test
	public void testStoreUtf8Characters() {
		Organization org = new Organization();
		org.setName("測試醫院");
		org.addIdentifier().setSystem("urn:system").setValue("testStoreUtf8Characters_01");
		IdDt orgId = (IdDt) ourClient.create().resource(org).prettyPrint().encodedXml().execute().getId();

		// Read back directly from the DAO
		{
			Organization returned = myOrganizationDao.read(orgId, mySrd);
			String val = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(returned);
			ourLog.info(val);
			assertThat(val, containsString("<name value=\"測試醫院\"/>"));
		}
		// Read back through the HTTP API
		{
			Organization returned = ourClient.read(Organization.class, orgId);
			String val = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(returned);
			ourLog.info(val);
			assertThat(val, containsString("<name value=\"測試醫院\"/>"));
		}
	}

	@Test
	public void testTransaction() throws Exception {
		String contents = ClasspathUtil.loadResource("/update.xml");
		HttpPost post = new HttpPost(ourServerBase);
		post.setEntity(new StringEntity(contents, ContentType.create("application/xml+fhir", "UTF-8")));
		CloseableHttpResponse resp = ourHttpClient.execute(post);
		try {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			String output = IOUtils.toString(resp.getEntity().getContent());
			ourLog.info(output);
		} finally {
			resp.close();
		}
	}

	@Test
	public void testTryToCreateResourceWithReferenceThatDoesntExist() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testTryToCreateResourceWithReferenceThatDoesntExist01");
		p1.addName().addFamily("testTryToCreateResourceWithReferenceThatDoesntExistFamily01").addGiven("testTryToCreateResourceWithReferenceThatDoesntExistGiven01");
		p1.setManagingOrganization(new ResourceReferenceDt("Organization/99999999999"));

		try {
			ourClient.create().resource(p1).execute().getId();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Organization/99999999999"));
		}

	}

	@Test
	public void testUpdateInvalidUrl() throws Exception {
		String methodName = "testUpdateInvalidReference";

		Patient pt = new Patient();
		pt.addName().addFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPut post = new HttpPut(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertEquals(400, response.getStatusLine().getStatusCode());
			OperationOutcome oo = myFhirContext.newXmlParser().parseResource(OperationOutcome.class, responseString);
			assertThat(oo.getIssue().get(0).getDiagnostics(), containsString("Can not update resource, request URL must contain an ID element for update (PUT) operation (it must be of the form [base]/[resource type]/[id])"));
		} finally {
			response.close();
		}
	}

	@Test
	public void testUpdateRejectsInvalidTypes() {

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateRejectsInvalidTypes");
		p1.addName().addFamily("Tester").addGiven("testUpdateRejectsInvalidTypes");
		IdDt p1id = (IdDt) ourClient.create().resource(p1).execute().getId();

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
	public void testUpdateResourceConditional() throws IOException {
		String methodName = "testUpdateResourceConditional";

		Patient pt = new Patient();
		pt.addName().addFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

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

		pt.addName().addFamily(methodName + "2");
		resource = myFhirContext.newXmlParser().encodeResourceToString(pt);
		HttpPut put = new HttpPut(ourServerBase + "/Patient?name=" + methodName);
		put.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		response = ourHttpClient.execute(put);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			IdDt newId = new IdDt(response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC).getValue());
			assertEquals(id.toVersionless(), newId.toVersionless()); // version shouldn't match for conditional update
			assertNotEquals(id, newId);
		} finally {
			response.close();
		}

	}

	@Test
	public void testUpdateResourceConditionalComplex() throws IOException {
		Patient pt = new Patient();
		pt.addIdentifier().setSystem("http://general-hospital.co.uk/Identifiers").setValue("09832345234543876876");
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.addHeader(Constants.HEADER_IF_NONE_EXIST, "Patient?identifier=http://general-hospital.co.uk/Identifiers|09832345234543876876");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		IdDt id;
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id = new IdDt(newIdString);
		} finally {
			response.close();
		}

		pt.addName().addFamily("FOO");
		resource = myFhirContext.newXmlParser().encodeResourceToString(pt);
		HttpPut put = new HttpPut(ourServerBase + "/Patient?identifier=" + ("http://general-hospital.co.uk/Identifiers|09832345234543876876".replace("|", UrlUtil.escapeUrlParam("|"))));
		put.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		IdDt id2;
		response = ourHttpClient.execute(put);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id2 = new IdDt(newIdString);
		} finally {
			response.close();
		}

		assertEquals(id.getIdPart(), id2.getIdPart());
		assertEquals("1", id.getVersionIdPart());
		assertEquals("2", id2.getVersionIdPart());
	}

	@Test
	public void testUpdateResourceWithPrefer() throws Exception {
		String methodName = "testUpdateResourceWithPrefer";

		Patient pt = new Patient();
		pt.addName().addFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

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

		Date before = new Date();
		Thread.sleep(100);

		pt.setId(id);
		pt.addAddress().addLine("AAAAAAAAAAAAAAAAAAAAAA");
		resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPut put = new HttpPut(ourServerBase + "/Patient/" + id.getIdPart());
		put.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_REPRESENTATION);
		put.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		response = ourHttpClient.execute(put);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			IOUtils.closeQuietly(response.getEntity().getContent());

			Patient respPt = myFhirContext.newXmlParser().parseResource(Patient.class, responseString);
			assertEquals("2", respPt.getId().getVersionIdPart());

			InstantDt updateTime = ResourceMetadataKeyEnum.UPDATED.get(respPt);
			assertTrue(updateTime.getValue().after(before));

		} finally {
			response.close();
		}

	}

	@Test
	public void testUpdateWithClientSuppliedIdWhichDoesntExist() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateWithClientSuppliedIdWhichDoesntExistRpDstu2");
		MethodOutcome outcome = ourClient.update().resource(p1).withId("testUpdateWithClientSuppliedIdWhichDoesntExistRpDstu2").execute();
		assertEquals(true, outcome.getCreated().booleanValue());
		IdDt p1Id = (IdDt) outcome.getId();

		assertThat(p1Id.getValue(), containsString("Patient/testUpdateWithClientSuppliedIdWhichDoesntExistRpDstu2/_history"));

		Bundle actual = ourClient
			.search()
			.forResource(Patient.class)
			.where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system", "testUpdateWithClientSuppliedIdWhichDoesntExistRpDstu2"))
			.encodedJson()
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(1, actual.getEntry().size());
		assertEquals(p1Id.getIdPart(), actual.getEntry().get(0).getResource().getId().getIdPart());

	}

	/**
	 * From example from david hay - moved to the hl7org_dstu2 project
	 */
	@Test
	@Disabled
	public void testValidateDavidsAllergyIntolerance() throws Exception {
		myDaoConfig.setAllowExternalReferences(true);

		/*
		 * Upload structurredef
		 */

		String contents = IOUtils.toString(getClass().getResourceAsStream("/allergyintolerance-sd-david.json"), "UTF-8");
		HttpEntityEnclosingRequestBase post = new HttpPut(ourServerBase + "/StructureDefinition/ohAllergyIntolerance");
		post.setEntity(new StringEntity(contents, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(201, response.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(response.getEntity().getContent());
			response.close();
		}

		/*
		 * Validate
		 */

		contents = IOUtils.toString(getClass().getResourceAsStream("/allergyintolerance-david.json"), "UTF-8");

		post = new HttpPost(ourServerBase + "/AllergyIntolerance/$validate?_pretty=true");
		post.setEntity(new StringEntity(contents, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp, not(containsString("Resource has no id")));
		} finally {
			IOUtils.closeQuietly(response.getEntity().getContent());
			response.close();
		}
	}

	@Test
	public void testValidateOnNoId() throws Exception {
		HttpGet get = new HttpGet(ourServerBase + "/QuestionnaireResponse/$validate");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", responseString);
			assertThat(responseString, containsString("No resource supplied for $validate operation"));
			assertEquals(400, response.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(response);
		}

	}

	/**
	 * From a Skype message from Brian Postlethwaite
	 */
	@Test
	public void testValidateQuestionnaireResponseWithNoIdForCreate() throws Exception {

		String input = "{\"resourceType\":\"Parameters\",\"parameter\":[{\"name\":\"mode\",\"valueString\":\"create\"},{\"name\":\"resource\",\"resource\":{\"resourceType\":\"QuestionnaireResponse\",\"questionnaire\":{\"reference\":\"http://fhirtest.uhn.ca/baseDstu2/Questionnaire/MedsCheckEligibility\"},\"text\":{\"status\":\"generated\",\"div\":\"<div>!-- populated from the rendered HTML below --></div>\"},\"status\":\"completed\",\"authored\":\"2017-02-10T00:02:58.098Z\",\"group\":{\"question\":[{\"linkId\":\"d94b4f57-1ca0-4d65-acba-8bd9a3926c8c\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"The patient has a valid Medicare or DVA entitlement card\"},{\"linkId\":\"0cbe66db-ff12-473a-940e-4672fb82de44\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"The patient has received a MedsCheck, Diabetes MedsCheck, Home Medicines Review (HMR) otr Restidential Medication Management Review (RMMR) in the past 12 months\"},{\"linkId\":\"35790cfd-2d98-4721-963e-9663e1897a17\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"The patient is living at home in a community setting\"},{\"linkId\":\"3ccc8304-76cd-41ff-9360-2c8755590bae\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"The patient has been recently diagnosed with type 3 diabetes (in the last 12 months) AND is unable to gain timely access to existing diabetes education or health services in the community OR \"},{\"linkId\":\"b05f6f09-49ec-40f9-a889-9a3fdff9e0da\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"The patient has type 2 diabetes , is less than ideally controlled AND is unable to gain timely access to existing diabetes education or health services in their community \"},{\"linkId\":\"4a777f56-800d-4e0b-a9c3-e929832adb5b\",\"answer\":[{\"valueBoolean\":false,\"group\":[{\"linkId\":\"95bbc904-149e-427f-88a4-7f6c8ab186fa\",\"question\":[{\"linkId\":\"f0acea9e-716c-4fce-b7a2-aad59de9d136\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"Patient has had an Acute or Adverse Event\"},{\"linkId\":\"e1629159-6dea-4295-a93e-e7c2829ce180\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"Exacerbation of a Chronic Disease or Condition\"},{\"linkId\":\"2ce526fa-edaa-44b3-8d5a-6e97f6379ce8\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"New Diagnosis\"},{\"linkId\":\"9d6ffa9f-0110-418c-9ed0-f04910fda2ed\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"Recent hospital admission (<3 months)\"},{\"linkId\":\"d2803ff7-25f7-4c7b-ab92-356c49910478\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"Major change to regular medication regime\"},{\"linkId\":\"b34af32d-c69d-4d44-889f-5b6d420a7d08\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"Suspected non-adherence to the patient's medication regime \"},{\"linkId\":\"74bad553-c273-41e6-8647-22b860430bc2\",\"answer\":[],\"text\":\"Other\"}]}]}],\"text\":\"The patient has experienced one or more of the following recent significant medical events\"},{\"linkId\":\"ecbf4e5a-d4d1-43eb-9f43-0c0e35fc09c7\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"The Pharmacist has obtained patient consent to take part in the MedsCheck Service or Diabetes MedsCheck Service&nbsp; and share information obtained during the services with other nominated members of the patients healthcare team (such as their GP, diabetes educator) if required\"},{\"linkId\":\"8ef66774-43b0-4190-873f-cfbb6e980aa9\",\"answer\":[],\"text\":\"Question\"}]}}}]}";

		IParser p = myFhirContext.newJsonParser().setPrettyPrint(true);
		ourLog.info(p.encodeResourceToString(p.parseResource(input)));


		HttpPost post = new HttpPost(ourServerBase + "/QuestionnaireResponse/$validate?_pretty=true");
		post.setEntity(new StringEntity(input, ContentType.APPLICATION_JSON));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", responseString);
			assertThat(responseString, not(containsString("Resource has no id")));
		} finally {
			IOUtils.closeQuietly(response);
		}

	}

	/**
	 * From a Skype message from Brian Postlethwaite
	 */
	@Test
	public void testValidateQuestionnaireResponseWithNoIdForUpdate() throws Exception {

		String input = "{\"resourceType\":\"Parameters\",\"parameter\":[{\"name\":\"mode\",\"valueString\":\"update\"},{\"name\":\"resource\",\"resource\":{\"resourceType\":\"QuestionnaireResponse\",\"questionnaire\":{\"reference\":\"http://fhirtest.uhn.ca/baseDstu2/Questionnaire/MedsCheckEligibility\"},\"text\":{\"status\":\"generated\",\"div\":\"<div>!-- populated from the rendered HTML below --></div>\"},\"status\":\"completed\",\"authored\":\"2017-02-10T00:02:58.098Z\",\"group\":{\"question\":[{\"linkId\":\"d94b4f57-1ca0-4d65-acba-8bd9a3926c8c\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"The patient has a valid Medicare or DVA entitlement card\"},{\"linkId\":\"0cbe66db-ff12-473a-940e-4672fb82de44\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"The patient has received a MedsCheck, Diabetes MedsCheck, Home Medicines Review (HMR) otr Restidential Medication Management Review (RMMR) in the past 12 months\"},{\"linkId\":\"35790cfd-2d98-4721-963e-9663e1897a17\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"The patient is living at home in a community setting\"},{\"linkId\":\"3ccc8304-76cd-41ff-9360-2c8755590bae\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"The patient has been recently diagnosed with type 3 diabetes (in the last 12 months) AND is unable to gain timely access to existing diabetes education or health services in the community OR \"},{\"linkId\":\"b05f6f09-49ec-40f9-a889-9a3fdff9e0da\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"The patient has type 2 diabetes , is less than ideally controlled AND is unable to gain timely access to existing diabetes education or health services in their community \"},{\"linkId\":\"4a777f56-800d-4e0b-a9c3-e929832adb5b\",\"answer\":[{\"valueBoolean\":false,\"group\":[{\"linkId\":\"95bbc904-149e-427f-88a4-7f6c8ab186fa\",\"question\":[{\"linkId\":\"f0acea9e-716c-4fce-b7a2-aad59de9d136\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"Patient has had an Acute or Adverse Event\"},{\"linkId\":\"e1629159-6dea-4295-a93e-e7c2829ce180\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"Exacerbation of a Chronic Disease or Condition\"},{\"linkId\":\"2ce526fa-edaa-44b3-8d5a-6e97f6379ce8\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"New Diagnosis\"},{\"linkId\":\"9d6ffa9f-0110-418c-9ed0-f04910fda2ed\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"Recent hospital admission (<3 months)\"},{\"linkId\":\"d2803ff7-25f7-4c7b-ab92-356c49910478\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"Major change to regular medication regime\"},{\"linkId\":\"b34af32d-c69d-4d44-889f-5b6d420a7d08\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"Suspected non-adherence to the patient's medication regime \"},{\"linkId\":\"74bad553-c273-41e6-8647-22b860430bc2\",\"answer\":[],\"text\":\"Other\"}]}]}],\"text\":\"The patient has experienced one or more of the following recent significant medical events\"},{\"linkId\":\"ecbf4e5a-d4d1-43eb-9f43-0c0e35fc09c7\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"The Pharmacist has obtained patient consent to take part in the MedsCheck Service or Diabetes MedsCheck Service&nbsp; and share information obtained during the services with other nominated members of the patients healthcare team (such as their GP, diabetes educator) if required\"},{\"linkId\":\"8ef66774-43b0-4190-873f-cfbb6e980aa9\",\"answer\":[],\"text\":\"Question\"}]}}}]}";
		HttpPost post = new HttpPost(ourServerBase + "/QuestionnaireResponse/$validate?_pretty=true");
		post.setEntity(new StringEntity(input, ContentType.APPLICATION_JSON));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", responseString);
			assertThat(responseString, containsString("Resource has no ID"));
			assertEquals(422, response.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(response);
		}

	}

	@Test
	public void testValidateResource() throws IOException {

		Patient patient = new Patient();
		patient.addName().addGiven("James");
		patient.setBirthDate(new DateDt("2011-02-02"));

		Parameters input = new Parameters();
		input.addParameter().setName("resource").setResource(patient);

		String inputStr = myFhirContext.newXmlParser().encodeResourceToString(input);
		ourLog.info(inputStr);

		HttpPost post = new HttpPost(ourServerBase + "/Patient/$validate?_pretty=true");
		post.setEntity(new StringEntity(inputStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp, not(containsString("Resource has no id")));
		} finally {
			IOUtils.closeQuietly(response.getEntity().getContent());
			response.close();
		}
	}

	@Test
	public void testValidateResourceHuge() throws IOException {

		Patient patient = new Patient();
		patient.addName().addGiven("James" + StringUtils.leftPad("James", 1000000, 'A'));
		patient.setBirthDate(new DateDt("2011-02-02"));

		Parameters input = new Parameters();
		input.addParameter().setName("resource").setResource(patient);

		String inputStr = myFhirContext.newXmlParser().encodeResourceToString(input);
//		ourLog.info(inputStr);

		HttpPost post = new HttpPost(ourServerBase + "/Patient/$validate");
		post.setEntity(new StringEntity(inputStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(response.getEntity().getContent());
			response.close();
		}
	}

	@Test
	public void testValidateResourceWithId() throws IOException {

		Patient patient = new Patient();
		patient.addName().addGiven("James");
		patient.setBirthDate(new DateDt("2011-02-02"));

		Parameters input = new Parameters();
		input.addParameter().setName("resource").setResource(patient);

		String inputStr = myFhirContext.newXmlParser().encodeResourceToString(input);
		ourLog.info(inputStr);

		HttpPost post = new HttpPost(ourServerBase + "/Patient/$validate");
		post.setEntity(new StringEntity(inputStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(response.getEntity().getContent());
			response.close();
		}
	}

	@Test
	public void testValueSetExpandOperation() throws IOException {

		ValueSet upload = myFhirContext.newXmlParser().parseResource(ValueSet.class, new InputStreamReader(ResourceProviderDstu2Test.class.getResourceAsStream("/extensional-case-2.xml")));
		IIdType vsid = ourClient.create().resource(upload).execute().getId().toUnqualifiedVersionless();

		HttpGet get = new HttpGet(ourServerBase + "/ValueSet/" + vsid.getIdPart() + "/$expand");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			// @formatter:off
			assertThat(resp,
				stringContainsInOrder("<ValueSet xmlns=\"http://hl7.org/fhir\">",
					"<expansion>",
					"<contains>",
					"<system value=\"http://loinc.org\"/>",
					"<code value=\"11378-7\"/>",
					"<display value=\"Systolic blood pressure at First encounter\"/>",
					"</contains>",
					"<contains>",
					"<system value=\"http://loinc.org\"/>",
					"<code value=\"8450-9\"/>",
					"<display value=\"Systolic blood pressure--expiration\"/>",
					"</contains>",
					"</expansion>"
				));
			//@formatter:on
		} finally {
			IOUtils.closeQuietly(response.getEntity().getContent());
			response.close();
		}

		/*
		 * Filter with display name
		 */

		get = new HttpGet(ourServerBase + "/ValueSet/" + vsid.getIdPart() + "/$expand?filter=systolic");
		response = ourHttpClient.execute(get);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			//@formatter:off
			assertThat(resp, stringContainsInOrder(
				"<code value=\"11378-7\"/>",
				"<display value=\"Systolic blood pressure at First encounter\"/>"));
			//@formatter:on
		} finally {
			IOUtils.closeQuietly(response.getEntity().getContent());
			response.close();
		}

		/*
		 * Filter with code
		 */

		get = new HttpGet(ourServerBase + "/ValueSet/" + vsid.getIdPart() + "/$expand?filter=11378");
		response = ourHttpClient.execute(get);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			//@formatter:off
			assertThat(resp, stringContainsInOrder(
				"<code value=\"11378-7\"/>",
				"<display value=\"Systolic blood pressure at First encounter\"/>"
			));
			//@formatter:on
		} finally {
			IOUtils.closeQuietly(response.getEntity().getContent());
			response.close();
		}

	}


}
