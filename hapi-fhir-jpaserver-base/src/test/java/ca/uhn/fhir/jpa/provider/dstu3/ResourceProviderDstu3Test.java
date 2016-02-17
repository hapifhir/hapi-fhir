package ca.uhn.fhir.jpa.provider.dstu3;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.dstu3.model.BaseResource;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.Bundle.SearchEntryMode;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.Device;
import org.hl7.fhir.dstu3.model.DiagnosticOrder;
import org.hl7.fhir.dstu3.model.DocumentManifest;
import org.hl7.fhir.dstu3.model.DocumentReference;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Encounter.EncounterClass;
import org.hl7.fhir.dstu3.model.Encounter.EncounterLocationComponent;
import org.hl7.fhir.dstu3.model.Encounter.EncounterState;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.ImagingStudy;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.MedicationOrder;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Narrative.NarrativeStatus;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Subscription;
import org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType;
import org.hl7.fhir.dstu3.model.Subscription.SubscriptionStatus;
import org.hl7.fhir.dstu3.model.TemporalPrecisionEnum;
import org.hl7.fhir.dstu3.model.UnsignedIntType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.Ignore;
import org.junit.Test;

import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.UrlUtil;

public class ResourceProviderDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderDstu3Test.class);

	@Override
	public void before() throws Exception {
		super.before();

		myDaoConfig.setAllowMultipleDelete(true);
	}

	private void checkParamMissing(String paramName) throws IOException, ClientProtocolException {
		HttpGet get = new HttpGet(ourServerBase + "/Observation?" + paramName + ":missing=false");
		CloseableHttpResponse resp = ourHttpClient.execute(get);
		IOUtils.closeQuietly(resp.getEntity().getContent());
		assertEquals(200, resp.getStatusLine().getStatusCode());
	}

	@Test
	public void testEverythingInstanceWithContentFilter() {
		Patient pt1 = new Patient();
		pt1.addName().addFamily("Everything").addGiven("Arthur");
		IIdType ptId1 = myPatientDao.create(pt1, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.addName().addFamily("Everything").addGiven("Arthur");
		IIdType ptId2 = myPatientDao.create(pt2, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		Device dev1 = new Device();
		dev1.setManufacturer("Some Manufacturer");
		IIdType devId1 = myDeviceDao.create(dev1, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		Device dev2 = new Device();
		dev2.setManufacturer("Some Manufacturer 2");
		myDeviceDao.create(dev2, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		Observation obs1 = new Observation();
		obs1.getText().setDivAsString("<div>OBSTEXT1</div>");
		obs1.getSubject().setReferenceElement(ptId1);
		obs1.getCode().addCoding().setCode("CODE1");
		obs1.setValue(new StringType("obsvalue1"));
		obs1.getDevice().setReferenceElement(devId1);
		IIdType obsId1 = myObservationDao.create(obs1, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.getSubject().setReferenceElement(ptId1);
		obs2.getCode().addCoding().setCode("CODE2");
		obs2.setValue(new StringType("obsvalue2"));
		IIdType obsId2 = myObservationDao.create(obs2, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		Observation obs3 = new Observation();
		obs3.getSubject().setReferenceElement(ptId2);
		obs3.getCode().addCoding().setCode("CODE3");
		obs3.setValue(new StringType("obsvalue3"));
		IIdType obsId3 = myObservationDao.create(obs3, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		List<IIdType> actual;
		StringAndListParam param;

		ourLog.info("Pt1:{} Pt2:{} Obs1:{} Obs2:{} Obs3:{}", new Object[] { ptId1.getIdPart(), ptId2.getIdPart(), obsId1.getIdPart(), obsId2.getIdPart(), obsId3.getIdPart() });

		param = new StringAndListParam();
		param.addAnd(new StringOrListParam().addOr(new StringParam("obsvalue1")));

		//@formatter:off
		Parameters response = ourClient
			.operation()
			.onInstance(ptId1)
			.named("everything")
			.withParameter(Parameters.class, Constants.PARAM_CONTENT, new StringType("obsvalue1"))
			.execute();
		//@formatter:on

		actual = toUnqualifiedVersionlessIds((Bundle) response.getParameter().get(0).getResource());
		assertThat(actual, containsInAnyOrder(ptId1, obsId1, devId1));

	}

	@Test
	public void testBundleCreate() throws Exception {
		IGenericClient client = ourClient;

		String resBody = IOUtils.toString(ResourceProviderDstu3Test.class.getResource("/document-father.json"));
		IIdType id = client.create().resource(resBody).execute().getId();

		ourLog.info("Created: {}", id);

		Bundle bundle = client.read().resource(Bundle.class).withId(id).execute();

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
	}

	@Test
	public void testBundleCreateWithTypeTransaction() throws Exception {
		IGenericClient client = ourClient;

		String resBody = IOUtils.toString(ResourceProviderDstu3Test.class.getResource("/document-father.json"));
		resBody = resBody.replace("\"type\": \"document\"", "\"type\": \"transaction\"");
		try {
			client.create().resource(resBody).execute().getId();
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Unable to store a Bundle resource on this server with a Bundle.type value other than 'document' - Value was: transaction"));
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
	public void testCodeSearch() {
		Subscription subs = new Subscription();
		subs.setStatus(SubscriptionStatus.ACTIVE);
		subs.getChannel().setType(SubscriptionChannelType.WEBSOCKET);
		subs.setCriteria("Observation?");
		IIdType id = ourClient.create().resource(subs).execute().getId().toUnqualifiedVersionless();

		//@formatter:off
		Bundle resp = ourClient
			.search()
			.forResource(Subscription.class)
			.where(Subscription.TYPE.exactly().code(SubscriptionChannelType.WEBSOCKET.toCode()))
			.and(Subscription.STATUS.exactly().code(SubscriptionStatus.ACTIVE.toCode()))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:off

		assertThat(toUnqualifiedVersionlessIds(resp), contains(id));
		
		//@formatter:off
		resp = ourClient
			.search()
			.forResource(Subscription.class)
			.where(Subscription.TYPE.exactly().systemAndCode(SubscriptionChannelType.WEBSOCKET.getSystem(), SubscriptionChannelType.WEBSOCKET.toCode()))
			.and(Subscription.STATUS.exactly().systemAndCode(SubscriptionStatus.ACTIVE.getSystem(), SubscriptionStatus.ACTIVE.toCode()))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:off

		assertThat(toUnqualifiedVersionlessIds(resp), contains(id));

		//@formatter:off
		resp = ourClient
			.search()
			.forResource(Subscription.class)
			.where(Subscription.TYPE.exactly().systemAndCode(SubscriptionChannelType.WEBSOCKET.getSystem(), SubscriptionChannelType.WEBSOCKET.toCode()))
			.and(Subscription.STATUS.exactly().systemAndCode("foo", SubscriptionStatus.ACTIVE.toCode()))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:off

		assertThat(toUnqualifiedVersionlessIds(resp), empty());

	}

	@Test
	public void testCountParam() throws Exception {
		// NB this does not get used- The paging provider has its own limits built in
		myDaoConfig.setHardSearchLimit(100);

		List<IBaseResource> resources = new ArrayList<IBaseResource>();
		for (int i = 0; i < 100; i++) {
			Organization org = new Organization();
			org.setName("rpdstu2_testCountParam_01");
			resources.add(org);
		}
		ourClient.transaction().withResources(resources).prettyPrint().encodedXml().execute();

		//@formatter:on
		Bundle found = ourClient.search().forResource(Organization.class).where(Organization.NAME.matches().value("rpdstu2_testCountParam_01")).count(10).returnBundle(Bundle.class).execute();
		assertEquals(100, found.getTotal());
		assertEquals(10, found.getEntry().size());

		found = ourClient.search().forResource(Organization.class).where(Organization.NAME.matches().value("rpdstu2_testCountParam_01")).count(999).returnBundle(Bundle.class).execute();
		//@formatter:on
		assertEquals(100, found.getTotal());
		assertEquals(50, found.getEntry().size());

	}

	@Test
	@Ignore
	public void testCreateQuestionnaireResponseWithValidation() throws IOException {
		ValueSet options = new ValueSet();
		options.getCodeSystem().setSystem("urn:system").addConcept().setCode("code0");
		IIdType optId = ourClient.create().resource(options).execute().getId();

		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.CHOICE).setOptions(new Reference(optId));
		IIdType qId = ourClient.create().resource(q).execute().getId();

		QuestionnaireResponse qa;

		// Good code

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(qId.toUnqualifiedVersionless().getValue());
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code0"));
		ourClient.create().resource(qa).execute();

		// Bad code

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(qId.toUnqualifiedVersionless().getValue());
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code1"));
		try {
			ourClient.create().resource(qa).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Question with linkId[link0]"));
		}
	}

	@Test
	public void testUpdateResourceConditionalComplex() throws IOException {
		Patient pt = new Patient();
		pt.addIdentifier().setSystem("http://general-hospital.co.uk/Identifiers").setValue("09832345234543876876");
		String resource = myFhirCtx.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.addHeader(Constants.HEADER_IF_NONE_EXIST, "Patient?identifier=http://general-hospital.co.uk/Identifiers|09832345234543876876");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		IdType id;
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id = new IdType(newIdString);
		} finally {
			response.close();
		}

		pt.addName().addFamily("FOO");
		resource = myFhirCtx.newXmlParser().encodeResourceToString(pt);
		HttpPut put = new HttpPut(ourServerBase + "/Patient?identifier=" + ("http://general-hospital.co.uk/Identifiers|09832345234543876876".replace("|", UrlUtil.escape("|"))));
		put.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		IdType id2;
		response = ourHttpClient.execute(put);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id2 = new IdType(newIdString);
		} finally {
			response.close();
		}

		assertEquals(id.getIdPart(), id2.getIdPart());
		assertEquals("1", id.getVersionIdPart());
		assertEquals("2", id2.getVersionIdPart());
	}

	@Test
	public void testCreateResourceConditionalComplex() throws IOException {
		Patient pt = new Patient();
		pt.addIdentifier().setSystem("http://general-hospital.co.uk/Identifiers").setValue("09832345234543876876");
		String resource = myFhirCtx.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.addHeader(Constants.HEADER_IF_NONE_EXIST, "Patient?identifier=http://general-hospital.co.uk/Identifiers|09832345234543876876");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		IdType id;
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id = new IdType(newIdString);
		} finally {
			response.close();
		}

		IdType id2;
		response = ourHttpClient.execute(post);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id2 = new IdType(newIdString);
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
	public void testCreateResourceConditional() throws IOException {
		String methodName = "testCreateResourceConditional";

		Patient pt = new Patient();
		pt.addName().addFamily(methodName);
		String resource = myFhirCtx.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.addHeader(Constants.HEADER_IF_NONE_EXIST, "Patient?name=" + methodName);
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		IdType id;
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id = new IdType(newIdString);
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
	public void testCreateResourceReturnsOperationOutcomeByDefault() throws IOException {
		String resource = "<Patient xmlns=\"http://hl7.org/fhir\"></Patient>";

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String respString = IOUtils.toString(response.getEntity().getContent());
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
			assertEquals(400, response.getStatusLine().getStatusCode());
			String respString = IOUtils.toString(response.getEntity().getContent());
			ourLog.info(respString);
			assertThat(respString, containsString("<OperationOutcome xmlns=\"http://hl7.org/fhir\">"));
			assertThat(respString, containsString("Can not create resource with ID[2], ID must not be supplied on a create (POST) operation"));
		} finally {
			response.getEntity().getContent().close();
			response.close();
		}
	}

	@Test
	public void testCreateWithForcedId() throws IOException {
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
		l2.getPartOf().setReferenceElement(l1id.toVersionless().toUnqualified());
		IIdType l2id = ourClient.create().resource(l2).execute().getId();

		Encounter e1 = new Encounter();
		e1.addIdentifier().setSystem("urn:foo").setValue("testDeepChainingE1");
		e1.getStatusElement().setValue(EncounterState.INPROGRESS);
		e1.getClass_Element().setValue(EncounterClass.HOME);
		EncounterLocationComponent location = e1.addLocation();
		location.getLocation().setReferenceElement(l2id.toUnqualifiedVersionless());
		location.setPeriod(new Period().setStart(new Date(), TemporalPrecisionEnum.SECOND).setEnd(new Date(), TemporalPrecisionEnum.SECOND));
		IIdType e1id = ourClient.create().resource(e1).execute().getId();

		//@formatter:off
		Bundle res = ourClient.search()
			.forResource(Encounter.class)
			.where(Encounter.IDENTIFIER.exactly().systemAndCode("urn:foo", "testDeepChainingE1"))
			.include(Encounter.INCLUDE_LOCATION.asRecursive())
			.include(Location.INCLUDE_PARTOF.asRecursive())
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertEquals(3, res.getEntry().size());
		assertEquals(1, genResourcesOfType(res, Encounter.class).size());
		assertEquals(e1id.toUnqualifiedVersionless(), genResourcesOfType(res, Encounter.class).get(0).getIdElement().toUnqualifiedVersionless());

	}

	private ArrayList<IBaseResource> genResourcesOfType(Bundle theRes, Class<? extends IBaseResource> theClass) {
		ArrayList<IBaseResource> retVal = new ArrayList<IBaseResource>();
		for (BundleEntryComponent next : theRes.getEntry()) {
			if (next.getResource() != null) {
				if (theClass.isAssignableFrom(next.getResource().getClass())) {
					retVal.add(next.getResource());
				}
			}
		}
		return retVal;
	}

	@Test
	public void testDeleteConditionalMultiple() {
		String methodName = "testDeleteConditionalMultiple";

		myDaoConfig.setAllowMultipleDelete(false);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("FAM1");
		IIdType id1 = myPatientDao.create(p, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("FAM2");
		IIdType id2 = myPatientDao.create(p, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

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
			assertEquals("HTTP 412 Precondition Failed: Failed to DELETE resource with match URL \"Patient?identifier=testDeleteConditionalMultiple\" because this search matched 2 resources",
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
			String responseString = IOUtils.toString(response.getEntity().getContent());
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
		String resource = myFhirCtx.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		IdType id;
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id = new IdType(newIdString);
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
		String resource = myFhirCtx.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		IdType id;
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id = new IdType(newIdString);
		} finally {
			response.close();
		}

		/*
		 * Try it with a raw socket call. The Apache client won't let us use the unescaped "|" in the URL but we want to make sure that works too..
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
	public void testDiagnosticOrderResources() throws Exception {
		IGenericClient client = ourClient;

		int initialSize = client.search().forResource(DiagnosticOrder.class).returnBundle(Bundle.class).execute().getEntry().size();

		DiagnosticOrder res = new DiagnosticOrder();
		res.addIdentifier().setSystem("urn:foo").setValue("123");

		client.create().resource(res).execute();

		int newSize = client.search().forResource(DiagnosticOrder.class).returnBundle(Bundle.class).execute().getEntry().size();

		assertEquals(1, newSize - initialSize);

	}

	/**
	 * See issue #52
	 */
	@Test
	public void testDocumentManifestResources() throws Exception {
		myFhirCtx.getResourceDefinition(Practitioner.class);
		myFhirCtx.getResourceDefinition(ca.uhn.fhir.model.dstu.resource.DocumentManifest.class);

		IGenericClient client = ourClient;

		int initialSize = client.search().forResource(DocumentManifest.class).returnBundle(Bundle.class).execute().getEntry().size();

		String resBody = IOUtils.toString(ResourceProviderDstu3Test.class.getResource("/documentmanifest.json"));
		client.create().resource(resBody).execute();

		int newSize = client.search().forResource(DocumentManifest.class).returnBundle(Bundle.class).execute().getEntry().size();

		assertEquals(1, newSize - initialSize);

	}

	/**
	 * See issue #52
	 */
	@Test
	public void testDocumentReferenceResources() throws Exception {
		IGenericClient client = ourClient;

		int initialSize = client.search().forResource(DocumentReference.class).returnBundle(Bundle.class).execute().getEntry().size();

		String resBody = IOUtils.toString(ResourceProviderDstu3Test.class.getResource("/documentreference.json"));
		client.create().resource(resBody).execute();

		int newSize = client.search().forResource(DocumentReference.class).returnBundle(Bundle.class).execute().getEntry().size();

		assertEquals(1, newSize - initialSize);

	}

	@Test
	public void testEverythingEncounterInstance() throws Exception {
		String methodName = "testEverythingEncounterInstance";

		Organization org1parent = new Organization();
		org1parent.setId("org1parent");
		org1parent.setName(methodName + "1parent");
		IIdType orgId1parent = ourClient.update().resource(org1parent).execute().getId().toUnqualifiedVersionless();

		Organization org1 = new Organization();
		org1.setName(methodName + "1");
		org1.getPartOf().setReferenceElement(orgId1parent);
		IIdType orgId1 = ourClient.create().resource(org1).execute().getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addName().addFamily(methodName);
		p.getManagingOrganization().setReferenceElement(orgId1);
		IIdType patientId = ourClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		Organization org2 = new Organization();
		org2.setName(methodName + "1");
		IIdType orgId2 = ourClient.create().resource(org2).execute().getId().toUnqualifiedVersionless();

		Device dev = new Device();
		dev.setModel(methodName);
		dev.getOwner().setReferenceElement(orgId2);
		IIdType devId = ourClient.create().resource(dev).execute().getId().toUnqualifiedVersionless();

		Location locParent = new Location();
		locParent.setName(methodName + "Parent");
		IIdType locPId = ourClient.create().resource(locParent).execute().getId().toUnqualifiedVersionless();

		Location locChild = new Location();
		locChild.setName(methodName);
		locChild.getPartOf().setReferenceElement(locPId);
		IIdType locCId = ourClient.create().resource(locChild).execute().getId().toUnqualifiedVersionless();

		Encounter encU = new Encounter();
		encU.getPatient().setReferenceElement(patientId);
		encU.addLocation().getLocation().setReferenceElement(locCId);
		IIdType encUId = ourClient.create().resource(encU).execute().getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.getPatient().setReferenceElement(patientId);
		enc.addLocation().getLocation().setReferenceElement(locCId);
		IIdType encId = ourClient.create().resource(enc).execute().getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getSubject().setReferenceElement(patientId);
		obs.getDevice().setReferenceElement(devId);
		obs.getEncounter().setReferenceElement(encId);
		IIdType obsId = ourClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();

		ourLog.info("IDs: EncU:" + encUId.getIdPart() + " Enc:" + encId.getIdPart() + "  " + patientId.toUnqualifiedVersionless());

		Parameters output = ourClient.operation().onInstance(encId).named("everything").withNoParameters(Parameters.class).execute();
		Bundle b = (Bundle) output.getParameter().get(0).getResource();
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);
		assertThat(ids, containsInAnyOrder(patientId, encId, orgId1, orgId2, orgId1parent, locPId, locCId, obsId, devId));
		assertThat(ids, not(containsInRelativeOrder(encUId)));

		ourLog.info(ids.toString());
	}

	@Test
	public void testEverythingEncounterType() throws Exception {
		String methodName = "testEverythingEncounterInstance";

		Organization org1parent = new Organization();
		org1parent.setId("org1parent");
		org1parent.setName(methodName + "1parent");
		IIdType orgId1parent = ourClient.update().resource(org1parent).execute().getId().toUnqualifiedVersionless();

		Organization org1 = new Organization();
		org1.setName(methodName + "1");
		org1.getPartOf().setReferenceElement(orgId1parent);
		IIdType orgId1 = ourClient.create().resource(org1).execute().getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addName().addFamily(methodName);
		p.getManagingOrganization().setReferenceElement(orgId1);
		IIdType patientId = ourClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		Organization org2 = new Organization();
		org2.setName(methodName + "1");
		IIdType orgId2 = ourClient.create().resource(org2).execute().getId().toUnqualifiedVersionless();

		Device dev = new Device();
		dev.setModel(methodName);
		dev.getOwner().setReferenceElement(orgId2);
		IIdType devId = ourClient.create().resource(dev).execute().getId().toUnqualifiedVersionless();

		Location locParent = new Location();
		locParent.setName(methodName + "Parent");
		IIdType locPId = ourClient.create().resource(locParent).execute().getId().toUnqualifiedVersionless();

		Location locChild = new Location();
		locChild.setName(methodName);
		locChild.getPartOf().setReferenceElement(locPId);
		IIdType locCId = ourClient.create().resource(locChild).execute().getId().toUnqualifiedVersionless();

		Encounter encU = new Encounter();
		encU.addIdentifier().setValue(methodName);
		IIdType encUId = ourClient.create().resource(encU).execute().getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.getPatient().setReferenceElement(patientId);
		enc.addLocation().getLocation().setReferenceElement(locCId);
		IIdType encId = ourClient.create().resource(enc).execute().getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getSubject().setReferenceElement(patientId);
		obs.getDevice().setReferenceElement(devId);
		obs.getEncounter().setReferenceElement(encId);
		IIdType obsId = ourClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();

		Parameters output = ourClient.operation().onType(Encounter.class).named("everything").withNoParameters(Parameters.class).execute();
		Bundle b = (Bundle) output.getParameter().get(0).getResource();
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);
		assertThat(ids, containsInAnyOrder(patientId, encUId, encId, orgId1, orgId2, orgId1parent, locPId, locCId, obsId, devId));

		ourLog.info(ids.toString());
	}

	/**
	 * See #147
	 */
	@Test
	public void testEverythingPatientDoesntRepeatPatient() throws Exception {
		Bundle b;
		b = myFhirCtx.newJsonParser().parseResource(Bundle.class, new InputStreamReader(ResourceProviderDstu3Test.class.getResourceAsStream("/bug147-bundle.json")));

		Bundle resp = ourClient.transaction().withBundle(b).execute();
		List<IdType> ids = new ArrayList<IdType>();
		for (BundleEntryComponent next : resp.getEntry()) {
			IdType toAdd = new IdType(next.getResponse().getLocation()).toUnqualifiedVersionless();
			ids.add(toAdd);
		}
		ourLog.info("Created: " + ids.toString());

		IdType patientId = new IdType(resp.getEntry().get(0).getResponse().getLocation());
		assertEquals("Patient", patientId.getResourceType());

		{
			Parameters output = ourClient.operation().onInstance(patientId).named("everything").withNoParameters(Parameters.class).execute();
			b = (Bundle) output.getParameter().get(0).getResource();

			ids = new ArrayList<IdType>();
			boolean dupes = false;
			for (BundleEntryComponent next : b.getEntry()) {
				IdType toAdd = next.getResource().getIdElement().toUnqualifiedVersionless();
				dupes = dupes | ids.contains(toAdd);
				ids.add(toAdd);
			}
			ourLog.info("$everything: " + ids.toString());

			assertFalse(ids.toString(), dupes);
		}

		/*
		 * Now try with a size specified
		 */
		{
			Parameters input = new Parameters();
			input.addParameter().setName(Constants.PARAM_COUNT).setValue(new UnsignedIntType(100));
			Parameters output = ourClient.operation().onInstance(patientId).named("everything").withParameters(input).execute();
			b = (Bundle) output.getParameter().get(0).getResource();

			ids = new ArrayList<IdType>();
			boolean dupes = false;
			for (BundleEntryComponent next : b.getEntry()) {
				IdType toAdd = next.getResource().getIdElement().toUnqualifiedVersionless();
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
	 * Test for #226
	 */
	@Test
	public void testEverythingPatientIncludesBackReferences() throws Exception {
		String methodName = "testEverythingIncludesBackReferences";

		Medication med = new Medication();
		med.getCode().setText(methodName);
		IIdType medId = myMedicationDao.create(med, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		Patient pat = new Patient();
		pat.addAddress().addLine(methodName);
		IIdType patId = myPatientDao.create(pat, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		MedicationOrder mo = new MedicationOrder();
		mo.getPatient().setReferenceElement(patId);
		mo.setMedication(new Reference(medId));
		IIdType moId = myMedicationOrderDao.create(mo, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		Parameters output = ourClient.operation().onInstance(patId).named("everything").withNoParameters(Parameters.class).execute();
		Bundle b = (Bundle) output.getParameter().get(0).getResource();
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);
		ourLog.info(ids.toString());
		assertThat(ids, containsInAnyOrder(patId, medId, moId));
	}

	/**
	 * See #148
	 */
	@Test
	public void testEverythingPatientIncludesCondition() throws Exception {
		Bundle b = new Bundle();
		Patient p = new Patient();
		p.setId("1");
		b.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		Condition c = new Condition();
		c.getPatient().setReference("Patient/1");
		b.addEntry().setResource(c).getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = ourClient.transaction().withBundle(b).execute();

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));

		IdType patientId = new IdType(resp.getEntry().get(0).getResponse().getLocation());
		assertEquals("Patient", patientId.getResourceType());

		Parameters output = ourClient.operation().onInstance(patientId).named("everything").withNoParameters(Parameters.class).execute();
		b = (Bundle) output.getParameter().get(0).getResource();

		List<IdType> ids = new ArrayList<IdType>();
		for (BundleEntryComponent next : b.getEntry()) {
			IdType toAdd = next.getResource().getIdElement().toUnqualifiedVersionless();
			ids.add(toAdd);
		}

		assertThat(ids.toString(), containsString("Patient/"));
		assertThat(ids.toString(), containsString("Condition/"));

	}

	@Test
	public void testEverythingPatientOperation() throws Exception {
		String methodName = "testEverythingOperation";

		Organization org1parent = new Organization();
		org1parent.setId("org1parent");
		org1parent.setName(methodName + "1parent");
		IIdType orgId1parent = ourClient.update().resource(org1parent).execute().getId().toUnqualifiedVersionless();

		Organization org1 = new Organization();
		org1.setName(methodName + "1");
		org1.getPartOf().setReferenceElement(orgId1parent);
		IIdType orgId1 = ourClient.create().resource(org1).execute().getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addName().addFamily(methodName);
		p.getManagingOrganization().setReferenceElement(orgId1);
		IIdType patientId = ourClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		Organization org2 = new Organization();
		org2.setName(methodName + "1");
		IIdType orgId2 = ourClient.create().resource(org2).execute().getId().toUnqualifiedVersionless();

		Device dev = new Device();
		dev.setModel(methodName);
		dev.getOwner().setReferenceElement(orgId2);
		IIdType devId = ourClient.create().resource(dev).execute().getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getSubject().setReferenceElement(patientId);
		obs.getDevice().setReferenceElement(devId);
		IIdType obsId = ourClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.getPatient().setReferenceElement(patientId);
		IIdType encId = ourClient.create().resource(enc).execute().getId().toUnqualifiedVersionless();

		Parameters output = ourClient.operation().onInstance(patientId).named("everything").withNoParameters(Parameters.class).execute();
		Bundle b = (Bundle) output.getParameter().get(0).getResource();
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);
		assertThat(ids, containsInAnyOrder(patientId, devId, obsId, encId, orgId1, orgId2, orgId1parent));

		ourLog.info(ids.toString());
	}

	@Test
	public void testEverythingPatientType() throws Exception {
		String methodName = "testEverythingPatientType";

		Organization o1 = new Organization();
		o1.setName(methodName + "1");
		IIdType o1Id = ourClient.create().resource(o1).execute().getId().toUnqualifiedVersionless();
		Organization o2 = new Organization();
		o2.setName(methodName + "2");
		IIdType o2Id = ourClient.create().resource(o2).execute().getId().toUnqualifiedVersionless();

		Patient p1 = new Patient();
		p1.addName().addFamily(methodName + "1");
		p1.getManagingOrganization().setReferenceElement(o1Id);
		IIdType p1Id = ourClient.create().resource(p1).execute().getId().toUnqualifiedVersionless();
		Patient p2 = new Patient();
		p2.addName().addFamily(methodName + "2");
		p2.getManagingOrganization().setReferenceElement(o2Id);
		IIdType p2Id = ourClient.create().resource(p2).execute().getId().toUnqualifiedVersionless();

		Condition c1 = new Condition();
		c1.getPatient().setReferenceElement(p1Id);
		IIdType c1Id = ourClient.create().resource(c1).execute().getId().toUnqualifiedVersionless();
		Condition c2 = new Condition();
		c2.getPatient().setReferenceElement(p2Id);
		IIdType c2Id = ourClient.create().resource(c2).execute().getId().toUnqualifiedVersionless();

		Condition c3 = new Condition();
		c3.addIdentifier().setValue(methodName + "3");
		IIdType c3Id = ourClient.create().resource(c3).execute().getId().toUnqualifiedVersionless();

		Parameters output = ourClient.operation().onType(Patient.class).named("everything").withNoParameters(Parameters.class).execute();
		Bundle b = (Bundle) output.getParameter().get(0).getResource();
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
		p.getManagingOrganization().setReferenceElement(oId);
		IIdType pId = ourClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		long time2 = System.currentTimeMillis();
		Thread.sleep(10);

		Condition c = new Condition();
		c.getCode().setText(methodName);
		c.getPatient().setReferenceElement(pId);
		IIdType cId = ourClient.create().resource(c).execute().getId().toUnqualifiedVersionless();

		Thread.sleep(10);
		long time3 = System.currentTimeMillis();

		// %3E=> %3C=<

		HttpGet get = new HttpGet(ourServerBase + "/Patient/" + pId.getIdPart() + "/$everything?_lastUpdated=%3E" + new InstantType(new Date(time1)).getValueAsString());
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent());
			IOUtils.closeQuietly(response.getEntity().getContent());
			ourLog.info(output);
			List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirCtx.newXmlParser().parseResource(Bundle.class, output));
			ourLog.info(ids.toString());
			assertThat(ids, containsInAnyOrder(pId, cId));
		} finally {
			response.close();
		}

		get = new HttpGet(ourServerBase + "/Patient/" + pId.getIdPart() + "/$everything?_lastUpdated=%3E" + new InstantType(new Date(time2)).getValueAsString() + "&_lastUpdated=%3C"
				+ new InstantType(new Date(time3)).getValueAsString());
		response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent());
			IOUtils.closeQuietly(response.getEntity().getContent());
			ourLog.info(output);
			List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirCtx.newXmlParser().parseResource(Bundle.class, output));
			ourLog.info(ids.toString());
			assertThat(ids, containsInAnyOrder(cId));
		} finally {
			response.close();
		}

		get = new HttpGet(ourServerBase + "/Patient/" + pId.getIdPart() + "/$everything?_lastUpdated=%3E" + new InstantType(new Date(time1)).getValueAsString() + "&_sort=_lastUpdated");
		response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent());
			IOUtils.closeQuietly(response.getEntity().getContent());
			ourLog.info(output);
			List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirCtx.newXmlParser().parseResource(Bundle.class, output));
			ourLog.info(ids.toString());
			assertThat(ids, contains(pId, cId));
		} finally {
			response.close();
		}

		get = new HttpGet(ourServerBase + "/Patient/" + pId.getIdPart() + "/$everything?_sort:desc=_lastUpdated");
		response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent());
			IOUtils.closeQuietly(response.getEntity().getContent());
			ourLog.info(output);
			List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirCtx.newXmlParser().parseResource(Bundle.class, output));
			ourLog.info(ids.toString());
			assertThat(ids, contains(cId, pId, oId));
		} finally {
			response.close();
		}

	}

	@Test
	public void testGetResourceCountsOperation() throws Exception {
		String methodName = "testMetaOperations";

		Patient pt = new Patient();
		pt.addName().addFamily(methodName);
		ourClient.create().resource(pt).execute().getId().toUnqualifiedVersionless();

		HttpGet get = new HttpGet(ourServerBase + "/$get-resource-counts");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent());
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

		Bundle history = ourClient.history().onInstance(id).andReturnBundle(Bundle.class).prettyPrint().summaryMode(SummaryEnum.DATA).execute();
		assertEquals(3, history.getEntry().size());
		assertEquals(id.withVersion("3").getValue(), history.getEntry().get(0).getResource().getId());
		assertEquals(1, ((Patient) history.getEntry().get(0).getResource()).getName().size());

		assertEquals(id.withVersion("2").getValue(), history.getEntry().get(1).getResource().getId());
		assertEquals(HTTPVerb.DELETE, history.getEntry().get(1).getRequest().getMethodElement().getValue());
		assertEquals(0, ((Patient) history.getEntry().get(1).getResource()).getName().size());

		assertEquals(id.withVersion("1").getValue(), history.getEntry().get(2).getResource().getId());
		assertEquals(1, ((Patient) history.getEntry().get(2).getResource()).getName().size());
	}

	/**
	 * See issue #52
	 */
	@Test
	public void testImagingStudyResources() throws Exception {
		IGenericClient client = ourClient;

		int initialSize = client.search().forResource(ImagingStudy.class).returnBundle(Bundle.class).execute().getEntry().size();

		String resBody = IOUtils.toString(ResourceProviderDstu3Test.class.getResource("/imagingstudy.json"));
		client.create().resource(resBody).execute();

		int newSize = client.search().forResource(ImagingStudy.class).returnBundle(Bundle.class).execute().getEntry().size();

		assertEquals(1, newSize - initialSize);

	}

	@Test
	public void testMetadata() throws Exception {
		HttpGet get = new HttpGet(ourServerBase + "/metadata");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent());
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp, stringContainsInOrder("THIS IS THE DESC"));
		} finally {
			IOUtils.closeQuietly(response.getEntity().getContent());
			response.close();
		}
	}

	@Test
	public void testMetaOperations() throws Exception {
		String methodName = "testMetaOperations";

		Patient pt = new Patient();
		pt.addName().addFamily(methodName);
		IIdType id = ourClient.create().resource(pt).execute().getId().toUnqualifiedVersionless();

		Meta meta = ourClient.meta().get(Meta.class).fromResource(id).execute();
		assertEquals(0, meta.getTag().size());

		Meta inMeta = new Meta();
		inMeta.addTag().setSystem("urn:system1").setCode("urn:code1");
		meta = ourClient.meta().add().onResource(id).meta(inMeta).execute();
		assertEquals(1, meta.getTag().size());

		inMeta = new Meta();
		inMeta.addTag().setSystem("urn:system1").setCode("urn:code1");
		meta = ourClient.meta().delete().onResource(id).meta(inMeta).execute();
		assertEquals(0, meta.getTag().size());

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
			String output = IOUtils.toString(response.getEntity().getContent());
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
			String output = IOUtils.toString(response.getEntity().getContent());
			ourLog.info(output);
			assertEquals(400, response.getStatusLine().getStatusCode());
			assertThat(output, containsString("Input contains no parameter with name 'meta'"));
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
			Bundle returned = ourClient.search().forResource(Patient.class).encodedXml().returnBundle(Bundle.class).execute();
			assertThat(returned.getEntry().size(), greaterThan(1));
			assertEquals(BundleType.SEARCHSET, returned.getType());
		}
		{
			Bundle returned = ourClient.search().forResource(Patient.class).encodedJson().returnBundle(Bundle.class).execute();
			assertThat(returned.getEntry().size(), greaterThan(1));
		}
	}

	@Test
	public void testSaveAndRetrieveExistingNarrativeJson() {
		Patient p1 = new Patient();
		p1.getText().setStatus(NarrativeStatus.GENERATED);
		p1.getText().getDiv().setValueAsString("<div>HELLO WORLD</div>");
		p1.addIdentifier().setSystem("urn:system").setValue("testSaveAndRetrieveExistingNarrative01");

		IIdType newId = ourClient.create().resource(p1).encodedJson().execute().getId();

		Patient actual = ourClient.read().resource(Patient.class).withId(newId).encodedJson().execute();
		assertEquals("<div>HELLO WORLD</div>", actual.getText().getDiv().getValueAsString());
	}

	@Test
	public void testSaveAndRetrieveExistingNarrativeXml() {
		Patient p1 = new Patient();
		p1.getText().setStatus(NarrativeStatus.GENERATED);
		p1.getText().getDiv().setValueAsString("<div>HELLO WORLD</div>");
		p1.addIdentifier().setSystem("urn:system").setValue("testSaveAndRetrieveExistingNarrative01");

		IIdType newId = ourClient.create().resource(p1).encodedXml().execute().getId();

		Patient actual = ourClient.read().resource(Patient.class).withId(newId).encodedXml().execute();
		assertEquals("<div>HELLO WORLD</div>", actual.getText().getDiv().getValueAsString());
	}

	@Test
	public void testSaveAndRetrieveWithContained() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSaveAndRetrieveWithContained01");

		Organization o1 = new Organization();
		o1.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSaveAndRetrieveWithContained02");

		p1.getManagingOrganization().setResource(o1);

		IIdType newId = ourClient.create().resource(p1).execute().getId();

		Patient actual = ourClient.read(Patient.class, new UriDt(newId.getValue()));
		assertEquals(1, actual.getContained().size());

		//@formatter:off
		Bundle b = ourClient
				.search()
				.forResource("Patient")
				.where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system:rpdstu2", "testSaveAndRetrieveWithContained01"))
				.prettyPrint()
				.returnBundle(Bundle.class)
				.execute();
		//@formatter:on
		assertEquals(1, b.getEntry().size());

	}

	@Test
	public void testSaveAndRetrieveWithoutNarrative() {
		Patient p1 = new Patient();
		p1.getText().setDivAsString("<div><td>Identifier</td><td>testSearchByResourceChain01</td></div>");
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByResourceChain01");

		IdType newId = (IdType) ourClient.create().resource(p1).execute().getId();

		Patient actual = ourClient.read(Patient.class, newId.getIdPart());
		assertThat(actual.getText().getDiv().getValueAsString(), containsString("<td>Identifier</td><td>testSearchByResourceChain01</td>"));
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
	public void testSearchByIdentifier() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByIdentifier01");
		p1.addName().addFamily("testSearchByIdentifierFamily01").addGiven("testSearchByIdentifierGiven01");
		IdType p1Id = (IdType) ourClient.create().resource(p1).execute().getId();

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue("testSearchByIdentifier02");
		p2.addName().addFamily("testSearchByIdentifierFamily01").addGiven("testSearchByIdentifierGiven02");
		ourClient.create().resource(p2).execute().getId();

		//@formatter:off
		Bundle actual = ourClient
				.search()
				.forResource(Patient.class)
				.where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system", "testSearchByIdentifier01"))
				.encodedJson()
				.prettyPrint()
				.returnBundle(Bundle.class)
				.execute();
		//@formatter:on

		assertEquals(1, actual.getEntry().size());
		assertEquals(ourServerBase + "/Patient/" + p1Id.getIdPart(), actual.getEntry().get(0).getFullUrl());
		assertEquals(p1Id.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());
		assertEquals(SearchEntryMode.MATCH, actual.getEntry().get(0).getSearch().getModeElement().getValue());
	}

	@Test
	public void testSearchByIdentifierWithoutSystem() {

		Patient p1 = new Patient();
		p1.addIdentifier().setValue("testSearchByIdentifierWithoutSystem01");
		IdType p1Id = (IdType) ourClient.create().resource(p1).execute().getId();

		//@formatter:off
		Bundle actual = ourClient
				.search()
				.forResource(Patient.class)
				.where(Patient.IDENTIFIER.exactly().systemAndCode(null, "testSearchByIdentifierWithoutSystem01"))
				.encodedJson()
				.prettyPrint()
				.returnBundle(Bundle.class)
				.execute();
		//@formatter:on
		assertEquals(1, actual.getEntry().size());
		assertEquals(p1Id.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());

	}

	@Test
	public void testSearchByIdOr() {
		IIdType id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id1 = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		IIdType id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id2 = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}

		//@formatter:off
		Bundle found = ourClient
			.search()
			.forResource(Patient.class)
			.where(BaseResource.RES_ID.matches().values(id1.getIdPart(), id2.getIdPart()))
			.and(BaseResource.RES_ID.matches().value(id1.getIdPart()))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertThat(toUnqualifiedVersionlessIds(found), containsInAnyOrder(id1));
	}

	@Test
	public void testSearchByResourceChain() {

		Organization o1 = new Organization();
		o1.setName("testSearchByResourceChainName01");
		IdType o1id = (IdType) ourClient.create().resource(o1).execute().getId();

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByResourceChain01");
		p1.addName().addFamily("testSearchByResourceChainFamily01").addGiven("testSearchByResourceChainGiven01");
		p1.setManagingOrganization(new Reference(o1id));
		IdType p1Id = (IdType) ourClient.create().resource(p1).execute().getId();

		//@formatter:off
		Bundle actual = ourClient.search()
				.forResource(Patient.class)
				.where(Patient.ORGANIZATION.hasId(o1id.getIdPart()))
				.encodedJson().prettyPrint().returnBundle(Bundle.class).execute();
		//@formatter:on
		assertEquals(1, actual.getEntry().size());
		assertEquals(p1Id.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());

		//@formatter:off
		actual = ourClient.search()
				.forResource(Patient.class)
				.where(Patient.ORGANIZATION.hasId(o1id.getValue()))
				.encodedJson().prettyPrint().returnBundle(Bundle.class).execute();
		//@formatter:on
		assertEquals(1, actual.getEntry().size());
		assertEquals(p1Id.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());

	}

	@Test
	public void testSearchLastUpdatedParamRp() throws InterruptedException {
		String methodName = "testSearchLastUpdatedParamRp";

		int sleep = 100;
		Thread.sleep(sleep);

		DateTimeType beforeAny = new DateTimeType(new Date(), TemporalPrecisionEnum.MILLI);
		IIdType id1a;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addFamily(methodName).addGiven("Joe");
			id1a = (IdType) ourClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();
		}
		IIdType id1b;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addFamily(methodName + "XXXX").addGiven("Joe");
			id1b = (IdType) ourClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();
		}

		Thread.sleep(1100);
		DateTimeType beforeR2 = new DateTimeType(new Date(), TemporalPrecisionEnum.MILLI);
		Thread.sleep(1100);

		IIdType id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addFamily(methodName).addGiven("John");
			id2 = (IdType) ourClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();
		}

		{
			//@formatter:off
			Bundle found = ourClient.search()
					.forResource(Patient.class)
					.where(Patient.NAME.matches().value("testSearchLastUpdatedParamRp"))
					.returnBundle(Bundle.class)
					.execute();
			//@formatter:on
			List<IIdType> patients = toUnqualifiedVersionlessIds(found);
			assertThat(patients, hasItems(id1a, id1b, id2));
		}
		{
			//@formatter:off
			Bundle found = ourClient.search()
					.forResource(Patient.class)
					.where(Patient.NAME.matches().value("testSearchLastUpdatedParamRp"))
					.lastUpdated(new DateRangeParam(beforeAny, null))
					.returnBundle(Bundle.class)
					.execute();
			//@formatter:on
			List<IIdType> patients = toUnqualifiedVersionlessIds(found);
			assertThat(patients, hasItems(id1a, id1b, id2));
		}
		{
			//@formatter:off
			Bundle found = ourClient.search()
					.forResource(Patient.class)
					.where(Patient.NAME.matches().value("testSearchLastUpdatedParamRp"))
					.lastUpdated(new DateRangeParam(beforeR2, null))
					.returnBundle(Bundle.class)
					.execute();
			//@formatter:on
			List<IIdType> patients = toUnqualifiedVersionlessIds(found);
			assertThat(patients, hasItems(id2));
			assertThat(patients, not(hasItems(id1a, id1b)));
		}
		{
			//@formatter:off
			Bundle found = ourClient.search()
					.forResource(Patient.class)
					.where(Patient.NAME.matches().value("testSearchLastUpdatedParamRp"))
					.lastUpdated(new DateRangeParam(beforeAny, beforeR2))
					.returnBundle(Bundle.class)
					.execute();
			//@formatter:on
			List<IIdType> patients = toUnqualifiedVersionlessIds(found);
			assertThat(patients.toString(), patients, not(hasItems(id2)));
			assertThat(patients.toString(), patients, (hasItems(id1a, id1b)));
		}
		{
			//@formatter:off
			Bundle found = ourClient.search()
					.forResource(Patient.class)
					.where(Patient.NAME.matches().value("testSearchLastUpdatedParamRp"))
					.lastUpdated(new DateRangeParam(null, beforeR2))
					.returnBundle(Bundle.class)
					.execute();
			//@formatter:on
			List<IIdType> patients = toUnqualifiedVersionlessIds(found);
			assertThat(patients, (hasItems(id1a, id1b)));
			assertThat(patients, not(hasItems(id2)));
		}
	}

	private void testSearchReturnsResults(String search) throws IOException, ClientProtocolException {
		int matches;
		HttpGet get = new HttpGet(ourServerBase + search);
		CloseableHttpResponse response = ourHttpClient.execute(get);
		String resp = IOUtils.toString(response.getEntity().getContent());
		IOUtils.closeQuietly(response.getEntity().getContent());
		ourLog.info(resp);
		Bundle bundle = myFhirCtx.newXmlParser().parseResource(Bundle.class, resp);
		matches = bundle.getTotal();

		assertThat(matches, greaterThan(0));
	}

	@Test
	public void testSearchReturnsSearchDate() throws Exception {
		Date before = new Date();
		Thread.sleep(1);

		//@formatter:off
		Bundle found = ourClient
				.search()
				.forResource(Patient.class)
				.prettyPrint()
				.returnBundle(Bundle.class)
				.execute();
		//@formatter:on

		Thread.sleep(1);
		Date after = new Date();

		InstantType updated = found.getMeta().getLastUpdatedElement();
		assertNotNull(updated);
		Date value = updated.getValue();
		assertNotNull(value);
		ourLog.info(value.getTime() + "");
		ourLog.info(before.getTime() + "");
		assertTrue(value.after(before));
		assertTrue(value.before(after));
	}

	@Test
	public void testSearchWithInclude() throws Exception {
		Organization org = new Organization();
		org.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSearchWithInclude01");
		IdType orgId = (IdType) ourClient.create().resource(org).prettyPrint().encodedXml().execute().getId();

		Patient pat = new Patient();
		pat.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSearchWithInclude02");
		pat.getManagingOrganization().setReferenceElement(orgId);
		ourClient.create().resource(pat).prettyPrint().encodedXml().execute().getId();

		//@formatter:off
		Bundle found = ourClient
				.search()
				.forResource(Patient.class)
				.where(Patient.IDENTIFIER.exactly().systemAndIdentifier("urn:system:rpdstu2","testSearchWithInclude02"))
				.include(Patient.INCLUDE_ORGANIZATION)
				.prettyPrint()
				.returnBundle(Bundle.class)
				.execute();
		//@formatter:on

		assertEquals(2, found.getEntry().size());
		assertEquals(Patient.class, found.getEntry().get(0).getResource().getClass());
		assertEquals(SearchEntryMode.MATCH, found.getEntry().get(0).getSearch().getMode());
		assertEquals(Organization.class, found.getEntry().get(1).getResource().getClass());
		assertEquals(SearchEntryMode.INCLUDE, found.getEntry().get(1).getSearch().getMode());
	}

	@Test(expected = InvalidRequestException.class)
	public void testSearchWithInvalidSort() throws Exception {
		Observation o = new Observation();
		o.getCode().setText("testSearchWithInvalidSort");
		myObservationDao.create(o, new ServletRequestDetails());
		//@formatter:off
		ourClient
				.search()
				.forResource(Observation.class)
				.sort().ascending(Observation.CODE_VALUE_QUANTITY) // composite sort not supported yet
				.prettyPrint()
				.returnBundle(Bundle.class)
				.execute();
		//@formatter:on
	}

	@Test()
	public void testSearchWithInvalidQuantityPrefix() throws Exception {
		Observation o = new Observation();
		o.getCode().setText("testSearchWithInvalidSort");
		myObservationDao.create(o, new ServletRequestDetails());
		try {
			//@formatter:off
			ourClient
					.search()
					.forResource(Observation.class)
					.where(Observation.VALUE_QUANTITY.withPrefix(ParamPrefixEnum.ENDS_BEFORE).number(100).andNoUnits())
					.prettyPrint()
					.returnBundle(Bundle.class)
					.execute();
			//@formatter:on
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Unable to handle quantity prefix \"eb\" for value: eb100||"));
		}
	}

	@Test()
	public void testSearchWithInvalidNumberPrefix() throws Exception {
		try {
			//@formatter:off
			ourClient
					.search()
					.forResource(Encounter.class)
					.where(Encounter.LENGTH.withPrefix(ParamPrefixEnum.ENDS_BEFORE).number(100))
					.prettyPrint()
					.returnBundle(Bundle.class)
					.execute();
			//@formatter:on
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Unable to handle number prefix \"eb\" for value: eb100"));
		}
	}

	@Test
	public void testSearchWithMissing() throws Exception {
		ourLog.info("Starting testSearchWithMissing");

		String methodName = "testSearchWithMissing";

		Organization org = new Organization();
		IIdType deletedIdMissingTrue = ourClient.create().resource(org).execute().getId().toUnqualifiedVersionless();
		ourClient.delete().resourceById(deletedIdMissingTrue).execute();

		org = new Organization();
		org.setName("Help I'm a Bug");
		IIdType deletedIdMissingFalse = ourClient.create().resource(org).execute().getId().toUnqualifiedVersionless();
		ourClient.delete().resourceById(deletedIdMissingFalse).execute();

		List<IBaseResource> resources = new ArrayList<IBaseResource>();
		for (int i = 0; i < 20; i++) {
			org = new Organization();
			org.setName(methodName + "_0" + i);
			resources.add(org);
		}
		ourClient.transaction().withResources(resources).prettyPrint().encodedXml().execute();

		org = new Organization();
		org.addIdentifier().setSystem("urn:system:rpdstu2").setValue(methodName + "01");
		org.setName(methodName + "name");
		IIdType orgNotMissing = ourClient.create().resource(org).prettyPrint().encodedXml().execute().getId().toUnqualifiedVersionless();

		org = new Organization();
		org.addIdentifier().setSystem("urn:system:rpdstu2").setValue(methodName + "01");
		IIdType orgMissing = ourClient.create().resource(org).prettyPrint().encodedXml().execute().getId().toUnqualifiedVersionless();

		{
			//@formatter:off
			Bundle found = ourClient
					.search()
					.forResource(Organization.class)
					.where(Organization.NAME.isMissing(false))
					.count(100)
					.prettyPrint()
					.returnBundle(Bundle.class)
					.execute();
			//@formatter:on

			List<IIdType> list = toUnqualifiedVersionlessIds(found);
			ourLog.info(methodName + ": " + list.toString());
			ourLog.info("Wanted " + orgNotMissing + " and not " + deletedIdMissingFalse + " but got " + list.size() + ": " + list);
			assertThat("Wanted " + orgNotMissing + " but got " + list.size() + ": " + list, list, containsInRelativeOrder(orgNotMissing));
			assertThat(list, not(containsInRelativeOrder(deletedIdMissingFalse)));
			assertThat(list, not(containsInRelativeOrder(orgMissing)));
		}

		//@formatter:off
			Bundle found = ourClient
					.search()
					.forResource(Organization.class)
					.where(Organization.NAME.isMissing(true))
					.count(100)
					.prettyPrint()
					.returnBundle(Bundle.class)
					.execute();
			//@formatter:on

		List<IIdType> list = toUnqualifiedVersionlessIds(found);
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

		//@formatter:off
		Bundle resp = ourClient
			.search()
			.forResource(Patient.class)
			.where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system", methodName))
			.sort().ascending(Patient.FAMILY)
			.sort().ascending(Patient.GIVEN)
			.limitTo(100)
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		List<String> names = toNameList(resp);

		ourLog.info(StringUtils.join(names, '\n'));

		//@formatter:off
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
		//@formatter:om
			
	}

	/**
	 * Test for issue #60
	 */
	@Test
	public void testStoreUtf8Characters() throws Exception {
		Organization org = new Organization();
		org.setName("測試醫院");
		org.addIdentifier().setSystem("urn:system").setValue("testStoreUtf8Characters_01");
		IdType orgId = (IdType) ourClient.create().resource(org).prettyPrint().encodedXml().execute().getId();

		// Read back directly from the DAO
		{
			Organization returned = myOrganizationDao.read(orgId, new ServletRequestDetails());
			String val = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(returned);
			ourLog.info(val);
			assertThat(val, containsString("<name value=\"測試醫院\"/>"));
		}
		// Read back through the HTTP API
		{
			Organization returned = ourClient.read(Organization.class, orgId.getIdPart());
			String val = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(returned);
			ourLog.info(val);
			assertThat(val, containsString("<name value=\"測試醫院\"/>"));
		}
	}

	@Test
	public void testTransaction() throws Exception {
		String contents = loadClasspath("/update.xml");
		HttpPost post = new HttpPost(ourServerBase);
		post.setEntity(new StringEntity(contents, ContentType.create("application/xml+fhir", "UTF-8")));
		CloseableHttpResponse resp = ourHttpClient.execute(post);
		try {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			String output= IOUtils.toString(resp.getEntity().getContent());
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
		p1.setManagingOrganization(new Reference("Organization/99999999999"));

		try {
			ourClient.create().resource(p1).execute().getId();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Organization/99999999999"));
		}

	}

	@Test
	public void testUpdateInvalidReference() throws IOException, Exception {
		String methodName = "testUpdateInvalidReference";

		Patient pt = new Patient();
		pt.addName().addFamily(methodName);
		String resource = myFhirCtx.newXmlParser().encodeResourceToString(pt);

		HttpPut post = new HttpPut(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent());
			ourLog.info(responseString);
			assertThat(responseString, containsString("Can not update a resource with no ID"));
			assertThat(responseString, containsString("<OperationOutcome"));
			assertEquals(400, response.getStatusLine().getStatusCode());
		} finally {
			response.close();
		}
	}

	@Test
	public void testUpdateRejectsInvalidTypes() throws InterruptedException {

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateRejectsInvalidTypes");
		p1.addName().addFamily("Tester").addGiven("testUpdateRejectsInvalidTypes");
		IdType p1id = (IdType) ourClient.create().resource(p1).execute().getId();

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
		String resource = myFhirCtx.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(ourServerBase + "/Patient?name=" + methodName);
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		IdType id;
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id = new IdType(newIdString);
		} finally {
			response.close();
		}

		HttpPut put = new HttpPut(ourServerBase + "/Patient?name=" + methodName);
		put.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		response = ourHttpClient.execute(put);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			IdType newId = new IdType(response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue());
			assertEquals(id.toVersionless(), newId.toVersionless()); // version shouldn't match for conditional update
			assertNotEquals(id, newId);
		} finally {
			response.close();
		}

	}
	
	@Test
	public void testUpdateResourceWithPrefer() throws IOException, Exception {
		String methodName = "testUpdateResourceWithPrefer";

		Patient pt = new Patient();
		pt.addName().addFamily(methodName);
		String resource = myFhirCtx.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		IdType id;
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id = new IdType(newIdString);
		} finally {
			response.close();
		}

		Date before = new Date();
		Thread.sleep(100);

		HttpPut put = new HttpPut(ourServerBase + "/Patient/" + id.getIdPart());
		put.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_REPRESENTATION);
		put.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		response = ourHttpClient.execute(put);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent());
			IOUtils.closeQuietly(response.getEntity().getContent());

			Patient respPt = myFhirCtx.newXmlParser().parseResource(Patient.class, responseString);
			assertEquals("2", respPt.getIdElement().getVersionIdPart());

			InstantType updateTime = respPt.getMeta().getLastUpdatedElement();
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
		IdType p1Id = (IdType) outcome.getId();

		assertThat(p1Id.getValue(), containsString("Patient/testUpdateWithClientSuppliedIdWhichDoesntExistRpDstu2/_history"));

		//@formatter:off
		Bundle actual = ourClient
				.search()
				.forResource(Patient.class)
				.where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system", "testUpdateWithClientSuppliedIdWhichDoesntExistRpDstu2"))
				.encodedJson()
				.prettyPrint()
				.returnBundle(Bundle.class)
				.execute();
		//@formatter:on

		assertEquals(1, actual.getEntry().size());
		assertEquals(p1Id.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());

	}

	@Test
	public void testValidateResourceWithNoIdParameters() throws IOException {

		Patient patient = new Patient();
		patient.addName().addGiven("James");
		patient.setBirthDateElement(new DateType("2011-02-02"));

		Parameters input = new Parameters();
		input.addParameter().setName("resource").setResource(patient);

		String inputStr = myFhirCtx.newXmlParser().encodeResourceToString(input);
		HttpPost post = new HttpPost(ourServerBase + "/Patient/$validate?_pretty=true");
		post.setEntity(new StringEntity(inputStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent());
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp, not(containsString("Resource has no id")));
			assertThat(resp, containsString("<pre>No issues detected during validation</pre>"));
			assertThat(resp,
					stringContainsInOrder("<issue>", "<severity value=\"information\"/>", "<code value=\"informational\"/>", "<diagnostics value=\"No issues detected during validation\"/>", "</issue>"));
		} finally {
			IOUtils.closeQuietly(response.getEntity().getContent());
			response.close();
		}
	}

	@Test
	public void testValidateResourceWithNoIdRaw() throws IOException {

		Patient patient = new Patient();
		patient.addName().addGiven("James");
		patient.setBirthDateElement(new DateType("2011-02-02"));

		String inputStr = myFhirCtx.newXmlParser().encodeResourceToString(patient);
		HttpPost post = new HttpPost(ourServerBase + "/Patient/$validate");
		post.setEntity(new StringEntity(inputStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent());
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp, not(containsString("Resource has no id")));
			assertThat(resp, containsString("<pre>No issues detected during validation</pre>"));
			assertThat(resp,
					stringContainsInOrder("<issue>", "<severity value=\"information\"/>", "<code value=\"informational\"/>", "<diagnostics value=\"No issues detected during validation\"/>", "</issue>"));
		} finally {
			IOUtils.closeQuietly(response.getEntity().getContent());
			response.close();
		}
	}

	@Test
	public void testValidateResourceBaseWithNoIdRaw() throws IOException {

		Patient patient = new Patient();
		patient.addName().addGiven("James");
		patient.setBirthDateElement(new DateType("2011-02-02"));
		patient.addContact().setGender(AdministrativeGender.MALE);

		String inputStr = myFhirCtx.newXmlParser().encodeResourceToString(patient);
		HttpPost post = new HttpPost(ourServerBase + "/Patient/$validate");
		post.setEntity(new StringEntity(inputStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent());
			ourLog.info(resp);
			assertEquals(412, response.getStatusLine().getStatusCode());
			assertThat(resp, not(containsString("Resource has no id")));
			assertThat(resp,
					stringContainsInOrder(">ERROR<","/f:Patient/f:contact","<pre>SHALL at least contain a contact's details or a reference to an organization</pre>","<issue><severity value=\"error\"/>","<code value=\"processing\"/>","<diagnostics value=\"SHALL at least contain a contact's details or a reference to an organization\"/>","<location value=\"/f:Patient/f:contact\"/>"));
		} finally {
			IOUtils.closeQuietly(response.getEntity().getContent());
			response.close();
		}
	}

	@Test
	public void testValidateResourceHuge() throws IOException {

		Patient patient = new Patient();
		patient.addName().addGiven("James" + StringUtils.leftPad("James", 1000000, 'A'));
		;
		patient.setBirthDateElement(new DateType("2011-02-02"));

		Parameters input = new Parameters();
		input.addParameter().setName("resource").setResource(patient);

		String inputStr = myFhirCtx.newXmlParser().encodeResourceToString(input);
		ourLog.info(inputStr);

		HttpPost post = new HttpPost(ourServerBase + "/Patient/$validate");
		post.setEntity(new StringEntity(inputStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent());
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
		patient.setBirthDateElement(new DateType("2011-02-02"));

		Parameters input = new Parameters();
		input.addParameter().setName("resource").setResource(patient);

		String inputStr = myFhirCtx.newXmlParser().encodeResourceToString(input);
		ourLog.info(inputStr);

		HttpPost post = new HttpPost(ourServerBase + "/Patient/123/$validate");
		post.setEntity(new StringEntity(inputStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent());
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(response.getEntity().getContent());
			response.close();
		}
	}

	@Test
	public void testValueSetExpandOperation() throws IOException {

		ValueSet upload = myFhirCtx.newXmlParser().parseResource(ValueSet.class, new InputStreamReader(ResourceProviderDstu3Test.class.getResourceAsStream("/extensional-case-2.1.xml")));
		IIdType vsid = ourClient.create().resource(upload).execute().getId().toUnqualifiedVersionless();

		HttpGet get = new HttpGet(ourServerBase + "/ValueSet/" + vsid.getIdPart() + "/$expand");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent());
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			// @formatter:off
			assertThat(resp,
				stringContainsInOrder("<ValueSet xmlns=\"http://hl7.org/fhir\">", 
					"<expansion>", 
						"<contains>", 
							"<system value=\"http://acme.org\"/>",
							"<code value=\"8450-9\"/>", 
							"<display value=\"Systolic blood pressure--expiration\"/>", 
						"</contains>",
						"<contains>", 
							"<system value=\"http://acme.org\"/>",
							"<code value=\"11378-7\"/>",
							"<display value=\"Systolic blood pressure at First encounter\"/>", 
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
			String resp = IOUtils.toString(response.getEntity().getContent());
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

	}

}
