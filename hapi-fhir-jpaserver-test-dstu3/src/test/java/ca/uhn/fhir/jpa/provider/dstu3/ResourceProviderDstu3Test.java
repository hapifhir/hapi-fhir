package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.BaseValidatingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.AuditEvent;
import org.hl7.fhir.dstu3.model.BaseResource;
import org.hl7.fhir.dstu3.model.Basic;
import org.hl7.fhir.dstu3.model.Binary;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleLinkComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.Bundle.SearchEntryMode;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.Device;
import org.hl7.fhir.dstu3.model.DocumentManifest;
import org.hl7.fhir.dstu3.model.DocumentReference;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Encounter.EncounterLocationComponent;
import org.hl7.fhir.dstu3.model.Encounter.EncounterStatus;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.EpisodeOfCare;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.ImagingStudy;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Media;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.MedicationAdministration;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Narrative.NarrativeStatus;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.PlanDefinition;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.ProcedureRequest;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.RelatedArtifact;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.Subscription;
import org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType;
import org.hl7.fhir.dstu3.model.Subscription.SubscriptionStatus;
import org.hl7.fhir.dstu3.model.Task;
import org.hl7.fhir.dstu3.model.UnsignedIntType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.codesystems.DeviceStatus;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.AopTestUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
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
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class ResourceProviderDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderDstu3Test.class);
	private SearchCoordinatorSvcImpl mySearchCoordinatorSvcRaw;
	@Autowired
	private ISearchDao mySearchEntityDao;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myStorageSettings.setAllowMultipleDelete(new JpaStorageSettings().isAllowMultipleDelete());
		myStorageSettings.setAllowExternalReferences(new JpaStorageSettings().isAllowExternalReferences());
		myStorageSettings.setReuseCachedSearchResultsForMillis(new JpaStorageSettings().getReuseCachedSearchResultsForMillis());

		mySearchCoordinatorSvcRaw.setLoadingThrottleForUnitTests(null);
		mySearchCoordinatorSvcRaw.setSyncSizeForUnitTests(QueryParameterUtils.DEFAULT_SYNC_SIZE);
		mySearchCoordinatorSvcRaw.setNeverUseLocalSearchForUnitTests(false);
	}

	@Test
	public void testSearchBySourceTransactionId() {

		Patient p1 = new Patient();
		p1.setActive(true);
		myClient
			.create()
			.resource(p1)
			.withAdditionalHeader(Constants.HEADER_REQUEST_ID, "11111")
			.execute();

		Patient p2 = new Patient();
		p2.setActive(true);
		myClient
			.create()
			.resource(p2)
			.withAdditionalHeader(Constants.HEADER_REQUEST_ID, "22222")
			.execute();

		Bundle results = myClient
			.search()
			.byUrl(myServerBase + "/Patient?_source=%2311111")
			.returnBundle(Bundle.class)
			.execute();

		assertThat(results.getEntry()).hasSize(1);

	}


	@Test
	public void testSearchByExternalReference() {
		myStorageSettings.setAllowExternalReferences(true);

		Patient patient = new Patient();
		patient.addName().setFamily("FooName");
		IIdType patientId = myClient.create().resource(patient).execute().getId();

		//Reference patientReference = new Reference("Patient/" + patientId.getIdPart()); <--- this works
		Reference patientReference = new Reference(patientId); // <--- this is seen as an external reference

		Media media = new Media();
		Attachment attachment = new Attachment();
		attachment.setLanguage("ENG");
		media.setContent(attachment);
		media.setSubject(patientReference);
		media.setType(Media.DigitalMediaType.AUDIO);
		IIdType mediaId = myClient.create().resource(media).execute().getId();


		// Act
		Bundle returnedBundle = myClient.search()
			.forResource(Observation.class)
			.where(Observation.CONTEXT.hasId(patientReference.getReference()))
			.returnBundle(Bundle.class)
			.execute();

		// Assert
		assertThat(returnedBundle.getEntry()).isEmpty();
	}

	@Test
	public void createResourceSearchParameter_withExpressionMetaSecurity_succeeds(){
		String spCallingName = "securitySP";
		String secCode = "secCode";

		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		searchParameter.setCode(spCallingName);
		searchParameter.addBase("Patient").addBase("Account");
		searchParameter.setType(Enumerations.SearchParamType.TOKEN);
		searchParameter.setExpression("meta.security");

		myClient.create().resource(searchParameter).execute();
		mySearchParamRegistry.forceRefresh();

		IIdType expectedPatientId = createPatientWithMeta(new Meta().addSecurity(new Coding().setCode(secCode)));
		IIdType dontCare = createPatientWithMeta(new Meta().addSecurity(new Coding().setCode("L")));

		Bundle searchResultBundle = myClient.search().forResource(Patient.class).where(new StringClientParam(spCallingName).matches().value(secCode)).returnBundle(Bundle.class).execute();

		List<String> foundPatients = toUnqualifiedVersionlessIdValues(searchResultBundle);

		assertThat(foundPatients).hasSize(1);

		assertEquals(foundPatients.get(0), expectedPatientId.getValue());

	}

	@Test
	public void createSearchParameter_with2Expressions_succeeds(){
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		searchParameter.setCode("myGender");
		searchParameter.addBase("Patient").addBase("Person");
		searchParameter.setType(Enumerations.SearchParamType.TOKEN);
		searchParameter.setExpression("Patient.gender|Person.gender");

		MethodOutcome result= myClient.create().resource(searchParameter).execute();

		assertEquals(true, result.getCreated());

	}

	@Test
	public void testSuppressNoExtensibleWarnings() {
		RequestValidatingInterceptor interceptor = new RequestValidatingInterceptor();
		interceptor.setFailOnSeverity(ResultSeverityEnum.INFORMATION);
		FhirInstanceValidator val = new FhirInstanceValidator(myValidationSupport);
		val.setNoExtensibleWarnings(true);
		interceptor.addValidatorModule(val);

		myRestServer.registerInterceptor(interceptor);
		try {
			CodeableConcept codeableConcept = new CodeableConcept();
			Coding codingCode = codeableConcept.addCoding();
			codingCode.setCode(DeviceStatus.ACTIVE.toCode());
			codingCode.setSystem(DeviceStatus.ACTIVE.getSystem());

			Device device = new Device();
			Identifier identifier = device.addIdentifier();
			identifier.setType(codeableConcept); // Not valid against valueset with 'Extensible' binding strength
			myClient.create().resource(device).execute().getId();
		} finally {
			myRestServer.unregisterInterceptor(interceptor);
		}
	}

	@Test
	public void testSuppressNoBindingMessage() {
		RequestValidatingInterceptor interceptor = new RequestValidatingInterceptor();
		interceptor.setFailOnSeverity(ResultSeverityEnum.INFORMATION);
		FhirInstanceValidator val = new FhirInstanceValidator(myValidationSupport);
		val.setNoBindingMsgSuppressed(true);
		interceptor.addValidatorModule(val);

		myRestServer.registerInterceptor(interceptor);
		try {
			CodeableConcept codeableConcept = new CodeableConcept();
			Coding codingCode = codeableConcept.addCoding();
			codingCode.setCode(DeviceStatus.ACTIVE.toCode());
			codingCode.setSystem(DeviceStatus.ACTIVE.getSystem());

			Task task = new Task();
			task.setStatus(Task.TaskStatus.DRAFT);
			task.setIntent(Task.TaskIntent.FILLERORDER);
			task.setCode(codeableConcept); // Task.code has no source/binding

			myClient.create().resource(task).execute().getId();
		} finally {
			myRestServer.unregisterInterceptor(interceptor);
		}
	}

	/**
	 * See #872
	 */
	@Test
	public void testExtensionUrlWithHl7UrlPost() throws IOException {

		ValueSet vs = myValidationSupport.fetchResource(ValueSet.class, "http://hl7.org/fhir/ValueSet/v3-ActInvoiceGroupCode");
		myValueSetDao.create(vs);


		RequestValidatingInterceptor interceptor = new RequestValidatingInterceptor();
		FhirInstanceValidator val = new FhirInstanceValidator(myValidationSupport);
		interceptor.addValidatorModule(val);

		myRestServer.registerInterceptor(interceptor);
		try {
			String input = IOUtils.toString(ResourceProviderDstu3Test.class.getResourceAsStream("/bug872-ext-with-hl7-url.json"), Charsets.UTF_8);

			HttpPost post = new HttpPost(myServerBase + "/Patient/aaa");
			post.setEntity(new StringEntity(input, ContentType.APPLICATION_JSON));

			CloseableHttpResponse resp = ourHttpClient.execute(post);
			try {
				String respString = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
				ourLog.debug(respString);
				assertEquals(400, resp.getStatusLine().getStatusCode());
			} finally {
				IOUtils.closeQuietly(resp);
			}
		} finally {
			myRestServer.unregisterInterceptor(interceptor);
		}
	}

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		myStorageSettings.setAllowMultipleDelete(true);

		myStorageSettings.setReuseCachedSearchResultsForMillis(null);
		mySearchCoordinatorSvcRaw = AopTestUtils.getTargetObject(mySearchCoordinatorSvc);
	}

	private void checkParamMissing(String paramName) throws IOException {
		HttpGet get = new HttpGet(myServerBase + "/Observation?" + paramName + ":missing=false");
		CloseableHttpResponse resp = ourHttpClient.execute(get);
		IOUtils.closeQuietly(resp.getEntity().getContent());
		assertEquals(200, resp.getStatusLine().getStatusCode());
	}

	private ArrayList<IBaseResource> genResourcesOfType(Bundle theRes, Class<? extends IBaseResource> theClass) {
		ArrayList<IBaseResource> retVal = new ArrayList<>();
		for (BundleEntryComponent next : theRes.getEntry()) {
			if (next.getResource() != null) {
				if (theClass.isAssignableFrom(next.getResource().getClass())) {
					retVal.add(next.getResource());
				}
			}
		}
		return retVal;
	}

	/**
	 * See #484
	 */
	@Test
	public void saveAndRetrieveBasicResource() throws IOException {
		String input = ClasspathUtil.loadResource("/basic-stu3.xml");

		String respString = myClient.transaction().withBundle(input).prettyPrint().execute();
		ourLog.debug(respString);
		Bundle bundle = myFhirContext.newJsonParser().parseResource(Bundle.class, respString);
		IdType id = new IdType(bundle.getEntry().get(0).getResponse().getLocation());

		Basic basic = myClient.read().resource(Basic.class).withId(id).execute();
		List<Extension> exts = basic.getExtensionsByUrl("http://localhost:1080/hapi-fhir-jpaserver-example/baseDstu2/StructureDefinition/DateID");
		assertThat(exts).hasSize(1);
	}

	private List<String> searchAndReturnUnqualifiedIdValues(String uri) throws IOException {
		List<String> ids;
		HttpGet get = new HttpGet(uri);

		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, resp);
			ids = toUnqualifiedIdValues(bundle);
		} finally {
			IOUtils.closeQuietly(response);
		}
		return ids;
	}

	private List<String> searchAndReturnUnqualifiedVersionlessIdValues(String uri) throws IOException {
		List<String> ids;
		HttpGet get = new HttpGet(uri);

		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, resp);
			ids = toUnqualifiedVersionlessIdValues(bundle);
		} finally {
			IOUtils.closeQuietly(response);
		}
		return ids;
	}

	// Y
	@Test
	public void testBundleCreate() throws Exception {
		IGenericClient client = myClient;

		String resBody = IOUtils.toString(ResourceProviderDstu3Test.class.getResource("/document-father-dstu3.json"), StandardCharsets.UTF_8);
		IIdType id = client.create().resource(resBody).execute().getId();

		ourLog.info("Created: {}", id);

		Bundle bundle = client.read().resource(Bundle.class).withId(id).execute();

		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
	}

	@Test
	public void testSearchWithIncludeAllWithNotResolvableReference() {
		// Arrange
		myStorageSettings.setAllowExternalReferences(true);

		Patient patient = new Patient();
		patient.addName().setFamily(UUID.randomUUID().toString());
		IIdType createdPatientId = myClient.create().resource(patient).execute().getId();

		RelatedArtifact relatedArtifactInternalReference = new RelatedArtifact();
		relatedArtifactInternalReference.setDisplay(UUID.randomUUID().toString());
		relatedArtifactInternalReference.setType(RelatedArtifact.RelatedArtifactType.PREDECESSOR);
		relatedArtifactInternalReference.setResource(new Reference(createdPatientId.toUnqualifiedVersionless()));

		RelatedArtifact relatedArtifactExternalReference = new RelatedArtifact();
		relatedArtifactExternalReference.setDisplay(UUID.randomUUID().toString());
		relatedArtifactExternalReference.setType(RelatedArtifact.RelatedArtifactType.PREDECESSOR);
		relatedArtifactExternalReference.setResource(new Reference("http://not-local-host.dk/hapi-fhir-jpaserver/fhir/Patient/2"));

		PlanDefinition planDefinition = new PlanDefinition();
		planDefinition.setStatus(Enumerations.PublicationStatus.ACTIVE);
		planDefinition.setName(UUID.randomUUID().toString());
		planDefinition.setRelatedArtifact(Arrays.asList(relatedArtifactInternalReference, relatedArtifactExternalReference));
		IIdType createdPlanDefinitionId = myClient.create().resource(planDefinition).execute().getId();

		// Act
		Bundle returnedBundle = myClient.search()
			.forResource(PlanDefinition.class)
			.include(PlanDefinition.INCLUDE_ALL)
			.where(PlanDefinition.NAME.matches().value(planDefinition.getName()))
			.returnBundle(Bundle.class)
			.execute();

		// Assert
		assertEquals(returnedBundle.getEntry().size(), 2);
		assertEquals(createdPlanDefinitionId, genResourcesOfType(returnedBundle, PlanDefinition.class).get(0).getIdElement());
		assertEquals(createdPatientId, genResourcesOfType(returnedBundle, Patient.class).get(0).getIdElement());
	}

	@Test
	public void testBundleCreateWithTypeTransaction() throws Exception {
		IGenericClient client = myClient;

		String resBody = IOUtils.toString(ResourceProviderDstu3Test.class.getResource("/document-father-dstu3.json"), StandardCharsets.UTF_8);
		resBody = resBody.replace("\"type\": \"document\"", "\"type\": \"transaction\"");
		try {
			client.create().resource(resBody).execute().getId();
			fail("");
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).contains("Unable to store a Bundle resource on this server with a Bundle.type value of: transaction. Note that if you are trying to perform a FHIR transaction or batch operation you should POST the Bundle resource to the Base URL of the server, not to the /Bundle endpoint.");
		}
	}

	@Test
	public void testSearchChainedReference() {

		Patient p = new Patient();
		p.addName().setFamily("SMITH");
		IIdType pid = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.getSubject().setReference(pid.getValue());
		myClient.create().resource(qr).execute();

		Subscription subs = new Subscription();
		subs.setStatus(SubscriptionStatus.ACTIVE);
		subs.getChannel().setType(SubscriptionChannelType.WEBSOCKET);
		subs.setCriteria("Observation?");
		IIdType id = myClient.create().resource(subs).execute().getId().toUnqualifiedVersionless();

		// Unqualified (doesn't work because QuestionnaireRespone.subject is a Refercence(Any))
		try {
			myClient
				.search()
				.forResource(QuestionnaireResponse.class)
				.where(QuestionnaireResponse.SUBJECT.hasChainedProperty(Patient.FAMILY.matches().value("SMITH")))
				.returnBundle(Bundle.class)
				.execute();
			fail("");
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1245) + "Unable to perform search for unqualified chain 'subject' as this SearchParameter does not declare any target types. Add a qualifier of the form 'subject:[ResourceType]' to perform this search.", e.getMessage());
		}

		// Qualified
		Bundle resp = myClient
			.search()
			.forResource(QuestionnaireResponse.class)
			.where(QuestionnaireResponse.SUBJECT.hasChainedProperty("Patient", Patient.FAMILY.matches().value("SMITH")))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(resp.getEntry()).hasSize(1);

		// Qualified With an invalid name
		try {
			myClient
				.search()
				.forResource(QuestionnaireResponse.class)
				.where(QuestionnaireResponse.SUBJECT.hasChainedProperty("FOO", Patient.FAMILY.matches().value("SMITH")))
				.returnBundle(Bundle.class)
				.execute();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1250) + "Invalid/unsupported resource type: \"FOO\"", e.getMessage());
		}

	}

	@Test
	public void testCodeSearch() {
		Subscription subs = new Subscription();
		subs.setStatus(SubscriptionStatus.ACTIVE);
		subs.getChannel().setType(SubscriptionChannelType.WEBSOCKET);
		subs.setCriteria("Observation?");
		IIdType id = myClient.create().resource(subs).execute().getId().toUnqualifiedVersionless();

		//@formatter:off
		Bundle resp = myClient
			.search()
			.forResource(Subscription.class)
			.where(Subscription.TYPE.exactly().code(SubscriptionChannelType.WEBSOCKET.toCode()))
			.and(Subscription.STATUS.exactly().code(SubscriptionStatus.ACTIVE.toCode()))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:off

		assertThat(toUnqualifiedVersionlessIds(resp)).containsExactly(id);

		//@formatter:off
		resp = myClient
			.search()
			.forResource(Subscription.class)
			.where(Subscription.TYPE.exactly().systemAndCode(SubscriptionChannelType.WEBSOCKET.getSystem(), SubscriptionChannelType.WEBSOCKET.toCode()))
			.and(Subscription.STATUS.exactly().systemAndCode(SubscriptionStatus.ACTIVE.getSystem(), SubscriptionStatus.ACTIVE.toCode()))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:off

		assertThat(toUnqualifiedVersionlessIds(resp)).containsExactly(id);

		//@formatter:off
		resp = myClient
			.search()
			.forResource(Subscription.class)
			.where(Subscription.TYPE.exactly().systemAndCode(SubscriptionChannelType.WEBSOCKET.getSystem(), SubscriptionChannelType.WEBSOCKET.toCode()))
			.and(Subscription.STATUS.exactly().systemAndCode("foo", SubscriptionStatus.ACTIVE.toCode()))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:off

		assertThat(toUnqualifiedVersionlessIds(resp)).isEmpty();

	}

	@Test
	public void testCountParam() {
		List<IBaseResource> resources = new ArrayList<IBaseResource>();
		for (int i = 0; i < 100; i++) {
			Organization org = new Organization();
			org.setName("rpdstu2_testCountParam_01");
			resources.add(org);
		}
		myClient.transaction().withResources(resources).prettyPrint().encodedXml().execute();

		Bundle found = myClient.search().forResource(Organization.class)
			.where(Organization.NAME.matches().value("rpdstu2_testCountParam_01"))
			.totalMode(SearchTotalModeEnum.ACCURATE)
			.count(10).returnBundle(Bundle.class).execute();
		assertEquals(100, found.getTotal());
		assertThat(found.getEntry()).hasSize(10);

		found = myClient.search().forResource(Organization.class).where(Organization.NAME.matches().value("rpdstu2_testCountParam_01")).count(50).returnBundle(Bundle.class).execute();
		assertThat(found.getEntry()).hasSize(50);

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

		IIdType resource = myClient.create().resource(binary).execute().getId();

		Binary fromDB = myClient.read().resource(Binary.class).withId(resource.toVersionless()).execute();
		assertEquals("1", fromDB.getIdElement().getVersionIdPart());

		arr[0] = 2;
		HttpPut putRequest = new HttpPut(myServerBase + "/Binary/" + resource.getIdPart());
		putRequest.setEntity(new ByteArrayEntity(arr, ContentType.parse("dansk")));
		CloseableHttpResponse resp = ourHttpClient.execute(putRequest);
		try {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertEquals(resource.withVersion("2").getValue(), resp.getFirstHeader("Content-Location").getValue());
		} finally {
			IOUtils.closeQuietly(resp);
		}

		fromDB = myClient.read().resource(Binary.class).withId(resource.toVersionless()).execute();
		assertEquals("2", fromDB.getIdElement().getVersionIdPart());

		arr[0] = 3;
		fromDB.setContent(arr);
		String encoded = myFhirContext.newJsonParser().encodeResourceToString(fromDB);
		putRequest = new HttpPut(myServerBase + "/Binary/" + resource.getIdPart());
		putRequest.setEntity(new StringEntity(encoded, ContentType.parse("application/json+fhir")));
		resp = ourHttpClient.execute(putRequest);
		try {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertEquals(resource.withVersion("3").getValue(), resp.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		} finally {
			IOUtils.closeQuietly(resp);
		}

		fromDB = myClient.read().resource(Binary.class).withId(resource.toVersionless()).execute();
		assertEquals("3", fromDB.getIdElement().getVersionIdPart());

		// Now an update with the wrong ID in the body

		arr[0] = 4;
		binary.setId("");
		encoded = myFhirContext.newJsonParser().encodeResourceToString(binary);
		putRequest = new HttpPut(myServerBase + "/Binary/" + resource.getIdPart());
		putRequest.setEntity(new StringEntity(encoded, ContentType.parse("application/json+fhir")));
		resp = ourHttpClient.execute(putRequest);
		try {
			assertEquals(400, resp.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(resp);
		}

		fromDB = myClient.read().resource(Binary.class).withId(resource.toVersionless()).execute();
		assertEquals("3", fromDB.getIdElement().getVersionIdPart());

	}

	/**
	 * Issue submitted by Bryn
	 */
	@Test
	public void testCreateBundle() throws IOException {
		String input = IOUtils.toString(getClass().getResourceAsStream("/bryn-bundle.json"), StandardCharsets.UTF_8);
		Validate.notNull(input);
		myClient.create().resource(input).execute().getResource();
	}

	@Test
	public void testUrlSearchWithNoStoreHeader() throws IOException {
		submitBundle("/dstu3/no-store-header/patient-bundle.json");
		submitBundle("/dstu3/no-store-header/practitioner-bundle.json");
		submitBundle("/dstu3/no-store-header/episodeofcare-bundle.json");

		Bundle responseBundle;

		responseBundle = myClient
			.search()
			.forResource(EpisodeOfCare.class)
			.where(new StringClientParam("_id").matches().value("ECC19005O3"))
			.where(new StringClientParam("_include").matches().value("*"))
			.where(new StringClientParam("_revinclude").matches().value("*"))
			.where(new StringClientParam("_count").matches().value("300"))
			.returnBundle(Bundle.class)
			.encodedJson()
			.execute();
		String bundleContents = "\n * " + responseBundle.getEntry().stream().map(t->t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.joining("\n * "));
		assertThat(responseBundle.getEntry().size()).as(bundleContents).isEqualTo(3);

		runInTransaction(()->{
			ourLog.info("Resources:\n * {}", myResourceTableDao
				.findAll()
				.stream()
				.sorted(((o1, o2) -> (int) (o1.getId().getId() - o2.getId().getId())))
				.map(t->t.getId() + " - " + t.getIdDt().toUnqualifiedVersionless().getValue())
				.collect(Collectors.joining("\n * ")));
		});

		// Now try the exact same search again but using the Cache-Control no-store Header
		responseBundle = myClient
			.search()
			.forResource(EpisodeOfCare.class)
			.where(new StringClientParam("_id").matches().value("ECC19005O3"))
			.where(new StringClientParam("_include").matches().value("*"))
			.where(new StringClientParam("_revinclude").matches().value("*"))
			.where(new StringClientParam("_count").matches().value("300"))
			.cacheControl(new CacheControlDirective().setNoStore(true))
			.returnBundle(Bundle.class)
			.encodedJson()
			.execute();
		bundleContents = "\n * " + responseBundle.getEntry().stream().map(t->t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.joining("\n * "));
		assertThat(responseBundle.getEntry().size()).as(bundleContents).isEqualTo(3);
	}

	private void submitBundle(String bundleName) throws IOException {
		String input = IOUtils.toString(getClass().getResourceAsStream(bundleName), StandardCharsets.UTF_8);
		Validate.notNull(input);
		myClient.transaction().withBundle(input).execute();
	}

	@Test
	public void testCreateConditional() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");

		MethodOutcome output1 = myClient.update().resource(patient).conditionalByUrl("Patient?identifier=http://uhn.ca/mrns|100").execute();

		patient = new Patient();
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");

		MethodOutcome output2 = myClient.update().resource(patient).conditionalByUrl("Patient?identifier=http://uhn.ca/mrns|100").execute();

		assertEquals(output1.getId().getIdPart(), output2.getId().getIdPart());
	}

	@Test
	public void testCreateIncludesRequestValidatorInterceptorOutcome() {
		RequestValidatingInterceptor interceptor = new RequestValidatingInterceptor();
		assertTrue(interceptor.isAddValidationResultsToResponseOperationOutcome());
		interceptor.setFailOnSeverity(null);

		myRestServer.registerInterceptor(interceptor);
		myClient.registerInterceptor(new IClientInterceptor() {
			@Override
			public void interceptRequest(IHttpRequest theRequest) {
				theRequest.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);
			}

			@Override
			public void interceptResponse(IHttpResponse theResponse) {               // TODO Auto-generated method stu
			}

		});
		try {
			// Missing status, which is mandatory
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:foo").setValue("bar");
			IBaseResource outcome = myClient.create().resource(obs).execute().getOperationOutcome();

			String encodedOo = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
			ourLog.info(encodedOo);
			assertThat(encodedOo).contains("cvc-complex-type.2.4.b");
			assertThat(encodedOo).contains("Successfully created resource \\\"Observation/");

			interceptor.setAddValidationResultsToResponseOperationOutcome(false);
			outcome = myClient.create().resource(obs).execute().getOperationOutcome();
			encodedOo = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
			ourLog.info(encodedOo);
			assertThat(encodedOo).doesNotContain("cvc-complex-type.2.4.b");
			assertThat(encodedOo).contains("Successfully created resource \\\"Observation/");

		} finally {
			myRestServer.unregisterInterceptor(interceptor);
		}
	}

	@Test
	@Disabled
	public void testCreateQuestionnaireResponseWithValidation() {
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://urn/system");
		cs.addConcept().setCode("code0");
		myClient.create().resource(cs).execute();

		ValueSet options = new ValueSet();
		options.getCompose().addInclude().setSystem("http://urn/system");
		IIdType optId = myClient.create().resource(options).execute().getId();

		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.CHOICE).setOptions(new Reference(optId));
		IIdType qId = myClient.create().resource(q).execute().getId();

		QuestionnaireResponse qa;

		// Good code

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(qId.toUnqualifiedVersionless().getValue());
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code0"));
		myClient.create().resource(qa).execute();

		// Bad code

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(qId.toUnqualifiedVersionless().getValue());
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code1"));
		try {
			myClient.create().resource(qa).execute();
			fail("");
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).contains("Question with linkId[link0]");
		}
	}

	@Test
	public void testCreateResourceConditional() throws IOException {
		String methodName = "testCreateResourceConditional";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(myServerBase + "/Patient");
		post.addHeader(Constants.HEADER_IF_NONE_EXIST, "Patient?name=" + methodName);
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		IdType id;
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString).startsWith(myServerBase + "/Patient/");
			id = new IdType(newIdString);
		} finally {
			response.close();
		}

		post = new HttpPost(myServerBase + "/Patient");
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

		HttpPost post = new HttpPost(myServerBase + "/Patient");
		post.addHeader(Constants.HEADER_IF_NONE_EXIST, "Patient?identifier=http://general-hospital.co.uk/Identifiers|09832345234543876876");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		IdType id;
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString).startsWith(myServerBase + "/Patient/");
			id = new IdType(newIdString);
		} finally {
			response.close();
		}

		IdType id2;
		response = ourHttpClient.execute(post);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString).startsWith(myServerBase + "/Patient/");
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
	public void testCreateResourceReturnsRepresentationByDefault() throws IOException {
		String resource = "<Patient xmlns=\"http://hl7.org/fhir\"></Patient>";

		HttpPost post = new HttpPost(myServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String respString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response.toString());
			ourLog.debug(respString);
			assertThat(respString).startsWith("<Patient xmlns=\"http://hl7.org/fhir\">");
			assertThat(respString).endsWith("</Patient>");
		} finally {
			response.getEntity().getContent().close();
			response.close();
		}
	}

	@Test
	public void testCreateResourceReturnsOperationOutcome() throws IOException {
		String resource = "<Patient xmlns=\"http://hl7.org/fhir\"></Patient>";

		HttpPost post = new HttpPost(myServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String respString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response.toString());
			ourLog.debug(respString);
			assertThat(respString).contains("<OperationOutcome xmlns=\"http://hl7.org/fhir\">");
		} finally {
			response.getEntity().getContent().close();
			response.close();
		}
	}

	@Test
	public void testCreateResourceWithNumericId() throws IOException {
		String resource = "<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"2\"/></Patient>";

		HttpPost post = new HttpPost(myServerBase + "/Patient/2");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertEquals(400, response.getStatusLine().getStatusCode());
			OperationOutcome oo = myFhirContext.newXmlParser().parseResource(OperationOutcome.class, responseString);
			assertEquals(Msg.code(365) + "Can not create resource with ID \"2\", ID must not be supplied on a create (POST) operation (use an HTTP PUT / update operation if you wish to supply an ID)", oo.getIssue().get(0).getDiagnostics());

		} finally {
			response.getEntity().getContent().close();
			response.close();
		}
	}

	@Test
	public void testCreateWithForcedId() {
		String methodName = "testCreateWithForcedId";

		Patient p = new Patient();
		p.addName().setFamily(methodName);
		p.setId(methodName);

		IIdType optId = myClient.update().resource(p).execute().getId();
		assertEquals(methodName, optId.getIdPart());
		assertEquals("1", optId.getVersionIdPart());
	}

	@Test
	public void testDeepChaining() {
		Location l1 = new Location();
		l1.getNameElement().setValue("testDeepChainingL1");
		IIdType l1id = myClient.create().resource(l1).execute().getId();

		Location l2 = new Location();
		l2.getNameElement().setValue("testDeepChainingL2");
		l2.getPartOf().setReferenceElement(l1id.toVersionless().toUnqualified());
		IIdType l2id = myClient.create().resource(l2).execute().getId();

		Encounter e1 = new Encounter();
		e1.addIdentifier().setSystem("urn:foo").setValue("testDeepChainingE1");
		e1.getStatusElement().setValue(EncounterStatus.INPROGRESS);
		EncounterLocationComponent location = e1.addLocation();
		location.getLocation().setReferenceElement(l2id.toUnqualifiedVersionless());
		location.setPeriod(new Period().setStart(new Date(), TemporalPrecisionEnum.SECOND).setEnd(new Date(), TemporalPrecisionEnum.SECOND));
		IIdType e1id = myClient.create().resource(e1).execute().getId();

		//@formatter:off
		Bundle res = myClient.search()
			.forResource(Encounter.class)
			.where(Encounter.IDENTIFIER.exactly().systemAndCode("urn:foo", "testDeepChainingE1"))
			.include(Encounter.INCLUDE_LOCATION.asRecursive())
			.include(Location.INCLUDE_PARTOF.asRecursive())
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertThat(res.getEntry()).hasSize(3);
		assertThat(genResourcesOfType(res, Encounter.class)).hasSize(1);
		assertEquals(e1id.toUnqualifiedVersionless(), genResourcesOfType(res, Encounter.class).get(0).getIdElement().toUnqualifiedVersionless());

	}

	@Test
	public void testDeleteConditionalMultiple() {
		String methodName = "testDeleteConditionalMultiple";

		myStorageSettings.setAllowMultipleDelete(false);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("FAM1");
		IIdType id1 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("FAM2");
		IIdType id2 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		try {
			//@formatter:off
			myClient
				.delete()
				.resourceConditionalByType(Patient.class)
				.where(Patient.IDENTIFIER.exactly().code(methodName))
				.execute();
			//@formatter:on
			fail("");
		} catch (PreconditionFailedException e) {
			assertEquals("HTTP 412 Precondition Failed: " + Msg.code(962) + "Failed to DELETE Patient with match URL \"Patient?identifier=testDeleteConditionalMultiple&_format=json\" because this search matched 2 resources", e.getMessage());
		}

		// Not deleted yet..
		myClient.read().resource("Patient").withId(id1).execute();
		myClient.read().resource("Patient").withId(id2).execute();

		myStorageSettings.setAllowMultipleDelete(true);

		MethodOutcome response = myClient
			.delete()
			.resourceConditionalByType(Patient.class)
			.where(Patient.IDENTIFIER.exactly().code(methodName))
			.execute();

		String encoded = myFhirContext.newXmlParser().encodeResourceToString(response.getOperationOutcome());
		ourLog.info(encoded);
		assertThat(encoded).contains("<diagnostics value=\"Successfully deleted 2 resource(s)");
		try {
			myClient.read().resource("Patient").withId(id1).execute();
			fail("");
		} catch (ResourceGoneException e) {
			// good
		}
		try {
			myClient.read().resource("Patient").withId(id2).execute();
			fail("");
		} catch (ResourceGoneException e) {
			// good
		}
	}

	@Test
	public void testDeleteConditionalNoMatches() throws Exception {
		String methodName = "testDeleteConditionalNoMatches";

		HttpDelete delete = new HttpDelete(myServerBase + "/Patient?identifier=" + methodName);
		CloseableHttpResponse resp = ourHttpClient.execute(delete);
		try {
			ourLog.info(resp.toString());
			String response = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response);
			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertThat(response).contains("<diagnostics value=\"Unable to find resource matching URL &quot;Patient?identifier=testDeleteConditionalNoMatches&quot;. Nothing has been deleted.\"/>");
		} finally {
			IOUtils.closeQuietly(resp);
		}

	}

	@Test
	public void testDeleteInvalidReference() throws IOException {
		HttpDelete delete = new HttpDelete(myServerBase + "/Patient");
		CloseableHttpResponse response = ourHttpClient.execute(delete);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertEquals(400, response.getStatusLine().getStatusCode());
			assertThat(responseString).contains("Can not perform delete, no ID provided");
		} finally {
			response.close();
		}
	}

	/**
	 * Test for #345
	 */
	@Test
	public void testDeleteNormal() {
		Patient p = new Patient();
		p.addName().setFamily("FAM");
		IIdType id = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		myClient.read().resource(Patient.class).withId(id).execute();

		myClient.delete().resourceById(id).execute();

		try {
			myClient.read().resource(Patient.class).withId(id).execute();
			fail("");
		} catch (ResourceGoneException e) {
			// good
		}
	}

	@Test
	public void testDeleteResourceConditional1() throws IOException {
		String methodName = "testDeleteResourceConditional1";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(myServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		IdType id;
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString).startsWith(myServerBase + "/Patient/");
			id = new IdType(newIdString);
		} finally {
			response.close();
		}

		HttpDelete delete = new HttpDelete(myServerBase + "/Patient?name=" + methodName);
		response = ourHttpClient.execute(delete);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			OperationOutcome oo = myFhirContext.newXmlParser().parseResource(OperationOutcome.class, resp);
			assertThat(oo.getIssueFirstRep().getDiagnostics()).startsWith("Successfully deleted 1 resource(s). Took ");
		} finally {
			response.close();
		}

		HttpGet read = new HttpGet(myServerBase + "/Patient/" + id.getIdPart());
		response = ourHttpClient.execute(read);
		try {
			ourLog.info(response.toString());
			assertEquals(Constants.STATUS_HTTP_410_GONE, response.getStatusLine().getStatusCode());
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			OperationOutcome oo = myFhirContext.newXmlParser().parseResource(OperationOutcome.class, resp);
			assertThat(oo.getIssueFirstRep().getDiagnostics()).startsWith("Resource was deleted at");
		} finally {
			response.close();
		}

		// Delete should now have no matches

		delete = new HttpDelete(myServerBase + "/Patient?name=" + methodName);
		response = ourHttpClient.execute(delete);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			OperationOutcome oo = myFhirContext.newXmlParser().parseResource(OperationOutcome.class, resp);
			assertThat(oo.getIssueFirstRep().getDiagnostics()).startsWith("Unable to find resource matching URL \"Patient?name=testDeleteResourceConditional1\". Nothing has been deleted.");
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
		pt.addName().setFamily(methodName);
		pt.addIdentifier().setSystem("http://ghh.org/patient").setValue(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(myServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		IdType id;
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString).startsWith(myServerBase + "/Patient/");
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
			sock.connect(new InetSocketAddress("localhost", myPort));
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

			ourLog.debug("Resp: {}", resp);
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} finally {
			sock.close();
		}

		Thread.sleep(1000);

		HttpGet read = new HttpGet(myServerBase + "/Patient/" + id.getIdPart());
		response = ourHttpClient.execute(read);
		try {
			ourLog.info(response.toString());
			assertEquals(Constants.STATUS_HTTP_410_GONE, response.getStatusLine().getStatusCode());
		} finally {
			response.close();
		}

	}

	@Test
	public void testDeleteReturnsOperationOutcome() {
		Patient p = new Patient();
		p.addName().setFamily("FAM");
		IIdType id = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		MethodOutcome resp = myClient.delete().resourceById(id).execute();
		OperationOutcome oo = (OperationOutcome) resp.getOperationOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics()).startsWith("Successfully deleted 1 resource(s). Took");
	}

	/**
	 * See issue #52
	 */
	@Test
	public void testDocumentManifestResources() throws Exception {
		myFhirContext.getResourceDefinition(Practitioner.class);
		myFhirContext.getResourceDefinition(DocumentManifest.class);

		IGenericClient client = myClient;

		int initialSize = client.search().forResource(DocumentManifest.class).returnBundle(Bundle.class).execute().getEntry().size();

		String resBody = IOUtils.toString(ResourceProviderDstu3Test.class.getResource("/documentmanifest.json"), StandardCharsets.UTF_8);
		client.create().resource(resBody).execute();

		int newSize = client.search().forResource(DocumentManifest.class).returnBundle(Bundle.class).execute().getEntry().size();

		assertEquals(1, newSize - initialSize);

	}

	/**
	 * See issue #52
	 */
	@Test
	public void testDocumentReferenceResources() throws Exception {
		IGenericClient client = myClient;

		int initialSize = client.search().forResource(DocumentReference.class).returnBundle(Bundle.class).execute().getEntry().size();

		String resBody = IOUtils.toString(ResourceProviderDstu3Test.class.getResource("/documentreference.json"), StandardCharsets.UTF_8);
		client.create().resource(resBody).execute();

		int newSize = client.search().forResource(DocumentReference.class).returnBundle(Bundle.class).execute().getEntry().size();

		assertEquals(1, newSize - initialSize);

	}

	@Test
	public void testEmptySearch() {
		myStorageSettings.setHibernateSearchIndexFullText(true);
		mySearchParamRegistry.forceRefresh();

		Bundle responseBundle;

		responseBundle = myClient.search().forResource(Patient.class).returnBundle(Bundle.class).execute();
		assertEquals(0, responseBundle.getTotal());

		responseBundle = myClient.search().forResource(Patient.class).where(Patient.NAME.matches().value("AAA")).returnBundle(Bundle.class).execute();
		assertEquals(0, responseBundle.getTotal());

		responseBundle = myClient.search().forResource(Patient.class).where(new StringClientParam("_content").matches().value("AAA")).returnBundle(Bundle.class).execute();
		assertEquals(0, responseBundle.getTotal());

	}

	@Test
	public void testEverythingEncounterInstance() {
		String methodName = "testEverythingEncounterInstance";

		Organization org1parent = new Organization();
		org1parent.setId("org1parent");
		org1parent.setName(methodName + "1parent");
		IIdType orgId1parent = myClient.update().resource(org1parent).execute().getId().toUnqualifiedVersionless();

		Organization org1 = new Organization();
		org1.setName(methodName + "1");
		org1.getPartOf().setReferenceElement(orgId1parent);
		IIdType orgId1 = myClient.create().resource(org1).execute().getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addName().setFamily(methodName);
		p.getManagingOrganization().setReferenceElement(orgId1);
		IIdType patientId = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		Organization org2 = new Organization();
		org2.setName(methodName + "1");
		IIdType orgId2 = myClient.create().resource(org2).execute().getId().toUnqualifiedVersionless();

		Device dev = new Device();
		dev.setModel(methodName);
		dev.getOwner().setReferenceElement(orgId2);
		IIdType devId = myClient.create().resource(dev).execute().getId().toUnqualifiedVersionless();

		Location locParent = new Location();
		locParent.setName(methodName + "Parent");
		IIdType locPId = myClient.create().resource(locParent).execute().getId().toUnqualifiedVersionless();

		Location locChild = new Location();
		locChild.setName(methodName);
		locChild.getPartOf().setReferenceElement(locPId);
		IIdType locCId = myClient.create().resource(locChild).execute().getId().toUnqualifiedVersionless();

		Encounter encU = new Encounter();
		encU.getSubject().setReferenceElement(patientId);
		encU.addLocation().getLocation().setReferenceElement(locCId);
		IIdType encUId = myClient.create().resource(encU).execute().getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.getSubject().setReferenceElement(patientId);
		enc.addLocation().getLocation().setReferenceElement(locCId);
		IIdType encId = myClient.create().resource(enc).execute().getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getSubject().setReferenceElement(patientId);
		obs.getDevice().setReferenceElement(devId);
		obs.getContext().setReferenceElement(encId);
		IIdType obsId = myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();

		ourLog.info("IDs: EncU:" + encUId.getIdPart() + " Enc:" + encId.getIdPart() + "  " + patientId.toUnqualifiedVersionless());

		Parameters output = myClient.operation().onInstance(encId).named("everything").withNoParameters(Parameters.class).execute();
		Bundle b = (Bundle) output.getParameter().get(0).getResource();
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);
		assertThat(ids).containsExactlyInAnyOrder(patientId, encId, orgId1, orgId2, orgId1parent, locPId, locCId, obsId, devId);
		assertThat(ids).doesNotContain(encUId);

		ourLog.info(ids.toString());
	}

	@Test
	public void testEverythingEncounterType() {
		String methodName = "testEverythingEncounterInstance";

		Organization org1parent = new Organization();
		org1parent.setId("org1parent");
		org1parent.setName(methodName + "1parent");
		IIdType orgId1parent = myClient.update().resource(org1parent).execute().getId().toUnqualifiedVersionless();

		Organization org1 = new Organization();
		org1.setName(methodName + "1");
		org1.getPartOf().setReferenceElement(orgId1parent);
		IIdType orgId1 = myClient.create().resource(org1).execute().getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addName().setFamily(methodName);
		p.getManagingOrganization().setReferenceElement(orgId1);
		IIdType patientId = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		Organization org2 = new Organization();
		org2.setName(methodName + "1");
		IIdType orgId2 = myClient.create().resource(org2).execute().getId().toUnqualifiedVersionless();

		Device dev = new Device();
		dev.setModel(methodName);
		dev.getOwner().setReferenceElement(orgId2);
		IIdType devId = myClient.create().resource(dev).execute().getId().toUnqualifiedVersionless();

		Location locParent = new Location();
		locParent.setName(methodName + "Parent");
		IIdType locPId = myClient.create().resource(locParent).execute().getId().toUnqualifiedVersionless();

		Location locChild = new Location();
		locChild.setName(methodName);
		locChild.getPartOf().setReferenceElement(locPId);
		IIdType locCId = myClient.create().resource(locChild).execute().getId().toUnqualifiedVersionless();

		Encounter encU = new Encounter();
		encU.addIdentifier().setValue(methodName);
		IIdType encUId = myClient.create().resource(encU).execute().getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.getSubject().setReferenceElement(patientId);
		enc.addLocation().getLocation().setReferenceElement(locCId);
		IIdType encId = myClient.create().resource(enc).execute().getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getSubject().setReferenceElement(patientId);
		obs.getDevice().setReferenceElement(devId);
		obs.getContext().setReferenceElement(encId);
		IIdType obsId = myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();

		Parameters output = myClient.operation().onType(Encounter.class).named("everything").withNoParameters(Parameters.class).execute();
		Bundle b = (Bundle) output.getParameter().get(0).getResource();
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);
		assertThat(ids).containsExactlyInAnyOrder(patientId, encUId, encId, orgId1, orgId2, orgId1parent, locPId, locCId, obsId, devId);

		ourLog.info(ids.toString());
	}


		@Test
		public void testEverythingInstanceWithContentFilter() {
            myStorageSettings.setHibernateSearchIndexFullText(true);
			mySearchParamRegistry.forceRefresh();

			Patient pt1 = new Patient();
			pt1.addName().setFamily("Everything").addGiven("Arthur");
			IIdType ptId1 = myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();

			Patient pt2 = new Patient();
			pt2.addName().setFamily("Everything").addGiven("Arthur");
			IIdType ptId2 = myPatientDao.create(pt2, mySrd).getId().toUnqualifiedVersionless();

			Device dev1 = new Device();
			dev1.setManufacturer("Some Manufacturer");
			IIdType devId1 = myDeviceDao.create(dev1, mySrd).getId().toUnqualifiedVersionless();

			Device dev2 = new Device();
			dev2.setManufacturer("Some Manufacturer 2");
			myDeviceDao.create(dev2, mySrd).getId().toUnqualifiedVersionless();

			// create an observation that links to Dev1 and Patient1
			Observation obs1 = new Observation();
			obs1.getText().setDivAsString("<div>OBSTEXT1</div>");
			obs1.getSubject().setReferenceElement(ptId1);
			obs1.getCode().addCoding().setCode("CODE1");
			obs1.setValue(new StringType("obsvalue1"));
			obs1.getDevice().setReferenceElement(devId1);
			IIdType obsId1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

			Observation obs2 = new Observation();
			obs2.getSubject().setReferenceElement(ptId1);
			obs2.getCode().addCoding().setCode("CODE2");
			obs2.setValue(new StringType("obsvalue2"));
			IIdType obsId2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

			Observation obs3 = new Observation();
			obs3.getSubject().setReferenceElement(ptId2);
			obs3.getCode().addCoding().setCode("CODE3");
			obs3.setValue(new StringType("obsvalue3"));
			IIdType obsId3 = myObservationDao.create(obs3, mySrd).getId().toUnqualifiedVersionless();

			List<IIdType> actual;

			ourLog.info("Pt1:{} Pt2:{} Obs1:{} Obs2:{} Obs3:{}", ptId1.getIdPart(), ptId2.getIdPart(), obsId1.getIdPart(), obsId2.getIdPart(), obsId3.getIdPart());

			//@formatter:off
		Parameters response = myClient
			.operation()
			.onInstance(ptId1)
			.named("everything")
			.withParameter(Parameters.class, Constants.PARAM_CONTENT, new StringType("obsvalue1"))
			.execute();
		//@formatter:on

			actual = toUnqualifiedVersionlessIds((Bundle) response.getParameter().get(0).getResource());
			assertThat(actual).containsExactlyInAnyOrder(ptId1, obsId1, devId1);
		}

	/**
	 * See #147"Patient"
	 */
	@Test
	public void testEverythingPatientDoesntRepeatPatient() {
		Bundle b;
		IParser parser = myFhirContext.newJsonParser();
		b = parser.parseResource(Bundle.class, new InputStreamReader(ResourceProviderDstu3Test.class.getResourceAsStream("/bug147-bundle-dstu3.json")));

		Bundle resp = myClient.transaction().withBundle(b).execute();
		List<IdType> ids = new ArrayList<IdType>();
		for (BundleEntryComponent next : resp.getEntry()) {
			IdType toAdd = new IdType(next.getResponse().getLocation()).toUnqualifiedVersionless();
			ids.add(toAdd);
		}
		ourLog.info("Created: " + ids.toString());

		IdType patientId = new IdType(resp.getEntry().get(0).getResponse().getLocation());
		assertEquals("Patient", patientId.getResourceType());

		{
			Parameters output = myClient.operation().onInstance(patientId).named("everything").withNoParameters(Parameters.class).execute();
			b = (Bundle) output.getParameter().get(0).getResource();

			ids = new ArrayList<IdType>();
			boolean dupes = false;
			for (BundleEntryComponent next : b.getEntry()) {
				IdType toAdd = next.getResource().getIdElement().toUnqualifiedVersionless();
				dupes = dupes | ids.contains(toAdd);
				ids.add(toAdd);
			}
			ourLog.info("$everything: " + ids.toString());

			assertThat(dupes).as(ids.toString()).isFalse();
		}

		/*
		 * Now try with a size specified
		 */
		{
			Parameters input = new Parameters();
			input.addParameter().setName(Constants.PARAM_COUNT).setValue(new UnsignedIntType(100));
			Parameters output = myClient.operation().onInstance(patientId).named("everything").withParameters(input).execute();
			b = (Bundle) output.getParameter().get(0).getResource();

			ids = new ArrayList<IdType>();
			boolean dupes = false;
			for (BundleEntryComponent next : b.getEntry()) {
				IdType toAdd = next.getResource().getIdElement().toUnqualifiedVersionless();
				dupes = dupes | ids.contains(toAdd);
				ids.add(toAdd);
			}
			ourLog.info("$everything: " + ids.toString());

			assertThat(dupes).as(ids.toString()).isFalse();
			assertThat(ids.toString()).contains("Condition");
			assertThat(ids.size()).isGreaterThan(10);
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

		MedicationRequest mo = new MedicationRequest();
		mo.getSubject().setReferenceElement(patId);
		mo.setMedication(new Reference(medId));
		IIdType moId = myMedicationRequestDao.create(mo, mySrd).getId().toUnqualifiedVersionless();

		Parameters output = myClient.operation().onInstance(patId).named("everything").withNoParameters(Parameters.class).execute();
		Bundle b = (Bundle) output.getParameter().get(0).getResource();
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);
		ourLog.info(ids.toString());
		assertThat(ids).containsExactlyInAnyOrder(patId, medId, moId);
	}

	/**
	 * See #148
	 */
	@Test
	public void testEverythingPatientIncludesCondition() {
		Bundle b = new Bundle();
		Patient p = new Patient();
		p.setId("1");
		b.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		Condition c = new Condition();
		c.getSubject().setReference("Patient/1");
		b.addEntry().setResource(c).getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = myClient.transaction().withBundle(b).execute();

		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));

		IdType patientId = new IdType(resp.getEntry().get(0).getResponse().getLocation());
		assertEquals("Patient", patientId.getResourceType());

		Parameters output = myClient.operation().onInstance(patientId).named("everything").withNoParameters(Parameters.class).execute();
		b = (Bundle) output.getParameter().get(0).getResource();

		List<IdType> ids = new ArrayList<IdType>();
		for (BundleEntryComponent next : b.getEntry()) {
			IdType toAdd = next.getResource().getIdElement().toUnqualifiedVersionless();
			ids.add(toAdd);
		}

		assertThat(ids.toString()).contains("Patient/");
		assertThat(ids.toString()).contains("Condition/");
	}


	@Test
	public void testEverythingPatientOperation() {
		String methodName = "testEverythingOperation";

		Organization org1parent = new Organization();
		org1parent.setId("org1parent");
		org1parent.setName(methodName + "1parent");
		IIdType orgId1parent = myClient.update().resource(org1parent).execute().getId().toUnqualifiedVersionless();

		Organization org1 = new Organization();
		org1.setName(methodName + "1");
		org1.getPartOf().setReferenceElement(orgId1parent);
		IIdType orgId1 = myClient.create().resource(org1).execute().getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addName().setFamily(methodName);
		p.getManagingOrganization().setReferenceElement(orgId1);
		IIdType patientId = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		Organization org2 = new Organization();
		org2.setName(methodName + "1");
		IIdType orgId2 = myClient.create().resource(org2).execute().getId().toUnqualifiedVersionless();

		Device dev = new Device();
		dev.setModel(methodName);
		dev.getOwner().setReferenceElement(orgId2);
		IIdType devId = myClient.create().resource(dev).execute().getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getSubject().setReferenceElement(patientId);
		obs.getDevice().setReferenceElement(devId);
		IIdType obsId = myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.getSubject().setReferenceElement(patientId);
		IIdType encId = myClient.create().resource(enc).execute().getId().toUnqualifiedVersionless();

		Parameters output = myClient.operation().onInstance(patientId).named("everything").withNoParameters(Parameters.class).execute();
		Bundle b = (Bundle) output.getParameter().get(0).getResource();
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);
		assertThat(ids).containsExactlyInAnyOrder(patientId, devId, obsId, encId, orgId1, orgId2, orgId1parent);

		ourLog.info(ids.toString());
	}

	@Test
	public void testEverythingPatientType() {
		String methodName = "testEverythingPatientType";

		Organization o1 = new Organization();
		o1.setName(methodName + "1");
		IIdType o1Id = myClient.create().resource(o1).execute().getId().toUnqualifiedVersionless();
		Organization o2 = new Organization();
		o2.setName(methodName + "2");
		IIdType o2Id = myClient.create().resource(o2).execute().getId().toUnqualifiedVersionless();

		Patient p1 = new Patient();
		p1.addName().setFamily(methodName + "1");
		p1.getManagingOrganization().setReferenceElement(o1Id);
		IIdType p1Id = myClient.create().resource(p1).execute().getId().toUnqualifiedVersionless();
		Patient p2 = new Patient();
		p2.addName().setFamily(methodName + "2");
		p2.getManagingOrganization().setReferenceElement(o2Id);
		IIdType p2Id = myClient.create().resource(p2).execute().getId().toUnqualifiedVersionless();

		Condition c1 = new Condition();
		c1.getSubject().setReferenceElement(p1Id);
		IIdType c1Id = myClient.create().resource(c1).execute().getId().toUnqualifiedVersionless();
		Condition c2 = new Condition();
		c2.getSubject().setReferenceElement(p2Id);
		IIdType c2Id = myClient.create().resource(c2).execute().getId().toUnqualifiedVersionless();

		Condition c3 = new Condition();
		c3.addIdentifier().setValue(methodName + "3");
		IIdType c3Id = myClient.create().resource(c3).execute().getId().toUnqualifiedVersionless();

		Parameters output = myClient.operation().onType(Patient.class).named("everything").withNoParameters(Parameters.class).execute();
		Bundle b = (Bundle) output.getParameter().get(0).getResource();

		assertEquals(BundleType.SEARCHSET, b.getType());
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);

		assertThat(ids).containsExactlyInAnyOrder(o1Id, o2Id, p1Id, p2Id, c1Id, c2Id);
		assertThat(ids).doesNotContain(c3Id);
	}

	@Test
	public void testEverythingPatientInstanceWithTypeParameter() {
		String methodName = "testEverythingPatientInstanceWithTypeParameter";

		//Patient 1 stuff.
		IIdType o1Id = createOrganization(methodName, "1");
		IIdType p1Id = createPatientWithIndexAtOrganization(methodName, "1", o1Id);
		IIdType c1Id = createConditionForPatient(methodName, "1", p1Id);
		IIdType obs1Id = createObservationForPatient(p1Id, "1");
		IIdType m1Id = createMedicationRequestForPatient(p1Id, "1");

		//Test for only one patient
		Parameters parameters = new Parameters();
		parameters.addParameter().setName(Constants.PARAM_TYPE).setValue(new StringType("Condition, Observation"));

		myCaptureQueriesListener.clear();

		Parameters output = myClient.operation().onInstance(p1Id).named("everything").withParameters(parameters).execute();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		Bundle b = (Bundle) output.getParameter().get(0).getResource();

		myCaptureQueriesListener.logSelectQueries();

		assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);

		assertThat(ids).containsExactlyInAnyOrder(p1Id, c1Id, obs1Id);
		assertThat(ids).doesNotContain(o1Id);
		assertThat(ids).doesNotContain(m1Id);
	}

	@Test
	public void testEverythingPatientTypeWithTypeParameter() {
		String methodName = "testEverythingPatientTypeWithTypeParameter";

		//Patient 1 stuff.
		IIdType o1Id = createOrganization(methodName, "1");
		IIdType p1Id = createPatientWithIndexAtOrganization(methodName, "1", o1Id);
		IIdType c1Id = createConditionForPatient(methodName, "1", p1Id);
		IIdType obs1Id = createObservationForPatient(p1Id, "1");
		IIdType m1Id = createMedicationRequestForPatient(p1Id, "1");

		//Test for only one patient
		Parameters parameters = new Parameters();
		parameters.addParameter().setName(Constants.PARAM_TYPE).setValue(new StringType("Condition, Observation"));

		myCaptureQueriesListener.clear();

		Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		Bundle b = (Bundle) output.getParameter().get(0).getResource();

		myCaptureQueriesListener.logSelectQueries();

		assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);

		assertThat(ids).containsExactlyInAnyOrder(p1Id, c1Id, obs1Id);
		assertThat(ids).doesNotContain(o1Id);
		assertThat(ids).doesNotContain(m1Id);
	}

	@Test
	public void testEverythingPatientTypeWithTypeAndIdParameter() {
		String methodName = "testEverythingPatientTypeWithTypeAndIdParameter";

		//Patient 1 stuff.
		IIdType o1Id = createOrganization(methodName, "1");
		IIdType p1Id = createPatientWithIndexAtOrganization(methodName, "1", o1Id);
		IIdType c1Id = createConditionForPatient(methodName, "1", p1Id);
		IIdType obs1Id = createObservationForPatient(p1Id, "1");
		IIdType m1Id = createMedicationRequestForPatient(p1Id, "1");

		//Patient 2 stuff.
		IIdType o2Id = createOrganization(methodName, "2");
		IIdType p2Id = createPatientWithIndexAtOrganization(methodName, "2", o2Id);
		IIdType c2Id = createConditionForPatient(methodName, "2", p2Id);
		IIdType obs2Id = createObservationForPatient(p2Id, "2");
		IIdType m2Id = createMedicationRequestForPatient(p2Id, "2");

		//Test for only patient 1
		Parameters parameters = new Parameters();
		parameters.addParameter().setName(Constants.PARAM_TYPE).setValue(new StringType("Condition, Observation"));
		parameters.addParameter().setName(Constants.PARAM_ID).setValue(new IdType(p1Id.getIdPart()));

		myCaptureQueriesListener.clear();

		Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		Bundle b = (Bundle) output.getParameter().get(0).getResource();

		myCaptureQueriesListener.logSelectQueries();

		assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);

		assertThat(ids).containsExactlyInAnyOrder(p1Id, c1Id, obs1Id);
		assertThat(ids).doesNotContain(o1Id);
		assertThat(ids).doesNotContain(m1Id);
		assertThat(ids).doesNotContain(p2Id);
		assertThat(ids).doesNotContain(o2Id);
	}

	private IIdType createOrganization(String methodName, String s) {
		Organization o1 = new Organization();
		o1.setName(methodName + s);
		return myClient.create().resource(o1).execute().getId().toUnqualifiedVersionless();
	}

	private IIdType createPatientWithIndexAtOrganization(String theMethodName, String theIndex, IIdType theOrganizationId) {
		Patient p1 = new Patient();
		p1.addName().setFamily(theMethodName + theIndex);
		p1.getManagingOrganization().setReferenceElement(theOrganizationId);
		IIdType p1Id = myClient.create().resource(p1).execute().getId().toUnqualifiedVersionless();
		return p1Id;
	}

	private IIdType createConditionForPatient(String theMethodName, String theIndex, IIdType thePatientId) {
		Condition c = new Condition();
		c.addIdentifier().setValue(theMethodName + theIndex);
		if (thePatientId != null) {
			c.getSubject().setReferenceElement(thePatientId);
		}
		IIdType cId = myClient.create().resource(c).execute().getId().toUnqualifiedVersionless();
		return cId;
	}

	private IIdType createMedicationRequestForPatient(IIdType thePatientId, String theIndex) {
		MedicationRequest m = new MedicationRequest();
		m.addIdentifier().setValue(theIndex);
		if (thePatientId != null) {
			m.getSubject().setReferenceElement(thePatientId);
		}
		IIdType mId = myClient.create().resource(m).execute().getId().toUnqualifiedVersionless();
		return mId;
	}

	private IIdType createObservationForPatient(IIdType thePatientId, String theIndex) {
		Observation o = new Observation();
		o.addIdentifier().setValue(theIndex);
		if (thePatientId != null) {
			o.getSubject().setReferenceElement(thePatientId);
		}
		IIdType oId = myClient.create().resource(o).execute().getId().toUnqualifiedVersionless();
		return oId;
	}

	@Test
	public void testEverythingPatientWithLastUpdatedAndSort() throws Exception {
		String methodName = "testEverythingWithLastUpdatedAndSort";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType oId = myClient.create().resource(org).execute().getId().toUnqualifiedVersionless();

		long time1 = System.currentTimeMillis();
		Thread.sleep(10);

		Patient p = new Patient();
		p.addName().setFamily(methodName);
		p.getManagingOrganization().setReferenceElement(oId);
		IIdType pId = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		long time2 = System.currentTimeMillis();
		Thread.sleep(10);

		Condition c = new Condition();
		c.getCode().setText(methodName);
		c.getSubject().setReferenceElement(pId);
		IIdType cId = myClient.create().resource(c).execute().getId().toUnqualifiedVersionless();

		Thread.sleep(10);
		long time3 = System.currentTimeMillis();

		// %3E=> %3C=<

		HttpGet get = new HttpGet(myServerBase + "/Patient/" + pId.getIdPart() + "/$everything?_lastUpdated=%3E" + new InstantType(new Date(time1)).getValueAsString());
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			IOUtils.closeQuietly(response.getEntity().getContent());
			ourLog.info(output);
			List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirContext.newXmlParser().parseResource(Bundle.class, output));
			ourLog.info(ids.toString());
			assertThat(ids).containsExactlyInAnyOrder(pId, cId, oId);
		} finally {
			response.close();
		}

		get = new HttpGet(myServerBase + "/Patient/" + pId.getIdPart() + "/$everything?_lastUpdated=%3E" + new InstantType(new Date(time2)).getValueAsString() + "&_lastUpdated=%3C"
			+ new InstantType(new Date(time3)).getValueAsString());
		response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			IOUtils.closeQuietly(response.getEntity().getContent());
			ourLog.info(output);
			List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirContext.newXmlParser().parseResource(Bundle.class, output));
			ourLog.info(ids.toString());
			assertThat(ids).containsExactlyInAnyOrder(pId, cId, oId);
		} finally {
			response.close();
		}

		/*
		 * Sorting is not working since the performance enhancements in 2.4 but
		 * sorting for lastupdated is non-standard anyhow.. Hopefully at some point
		 * we can bring this back
		 */
		// get = new HttpGet(ourServerBase + "/Patient/" + pId.getIdPart() + "/$everything?_lastUpdated=%3E" + new InstantType(new Date(time1)).getValueAsString() + "&_sort=_lastUpdated");
		// response = ourHttpClient.execute(get);
		// try {
		// assertEquals(200, response.getStatusLine().getStatusCode());
		// String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
		// IOUtils.closeQuietly(response.getEntity().getContent());
		// ourLog.info(output);
		// List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirCtx.newXmlParser().parseResource(Bundle.class, output));
		// ourLog.info(ids.toString());
		// assertThat(ids).contains(pId, cId);
		// } finally {
		// response.close();
		// }
		//
		// get = new HttpGet(ourServerBase + "/Patient/" + pId.getIdPart() + "/$everything?_sort:desc=_lastUpdated");
		// response = ourHttpClient.execute(get);
		// try {
		// assertEquals(200, response.getStatusLine().getStatusCode());
		// String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
		// IOUtils.closeQuietly(response.getEntity().getContent());
		// ourLog.info(output);
		// List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirCtx.newXmlParser().parseResource(Bundle.class, output));
		// ourLog.info(ids.toString());
		// assertThat(ids).contains(cId, pId, oId);
		// } finally {
		// response.close();
		// }

	}

	/**
	 * Per message from David Hay on Skype
	 */
	@Test
	public void testEverythingWithLargeSet() throws Exception {

		String inputString = IOUtils.toString(getClass().getResourceAsStream("/david_big_bundle.json"), StandardCharsets.UTF_8);
		Bundle inputBundle = myFhirContext.newJsonParser().parseResource(Bundle.class, inputString);
		inputBundle.setType(BundleType.TRANSACTION);

		assertThat(inputBundle.getEntry()).hasSize(53);

		Set<String> allIds = new TreeSet<String>();
		for (BundleEntryComponent nextEntry : inputBundle.getEntry()) {
			nextEntry.getRequest().setMethod(HTTPVerb.PUT);
			UrlUtil.UrlParts parts = UrlUtil.parseUrl(nextEntry.getResource().getId());
			nextEntry.getRequest().setUrl(parts.getResourceType() + "/" + parts.getResourceId());
			allIds.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		assertThat(allIds).hasSize(53);

		mySystemDao.transaction(mySrd, inputBundle);

		Bundle responseBundle = myClient
			.operation()
			.onInstance(new IdType("Patient/A161443"))
			.named("everything")
			.withParameter(Parameters.class, "_count", new IntegerType(20))
			.useHttpGet()
			.returnResourceType(Bundle.class)
			.execute();

		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(responseBundle));

		List<String> ids = new ArrayList<String>();
		for (BundleEntryComponent nextEntry : responseBundle.getEntry()) {
			ids.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}
		Collections.sort(ids);
		ourLog.info("{} ids: {}", ids.size(), ids);

		assertThat(responseBundle.getEntry().size()).isLessThanOrEqualTo(25);

		TreeSet<String> idsSet = new TreeSet<String>();
		for (int i = 0; i < responseBundle.getEntry().size(); i++) {
			for (BundleEntryComponent nextEntry : responseBundle.getEntry()) {
				idsSet.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
			}
		}

		String nextUrl = responseBundle.getLink("next").getUrl();
		responseBundle = myClient.fetchResourceFromUrl(Bundle.class, nextUrl);
		for (int i = 0; i < responseBundle.getEntry().size(); i++) {
			for (BundleEntryComponent nextEntry : responseBundle.getEntry()) {
				idsSet.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
			}
		}

		nextUrl = responseBundle.getLink("next").getUrl();
		responseBundle = myClient.fetchResourceFromUrl(Bundle.class, nextUrl);
		for (int i = 0; i < responseBundle.getEntry().size(); i++) {
			for (BundleEntryComponent nextEntry : responseBundle.getEntry()) {
				idsSet.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
			}
		}

		assertNull(responseBundle.getLink("next"));

		assertThat(idsSet).contains("List/A161444");
		assertThat(idsSet).contains("List/A161468");
		assertThat(idsSet).contains("List/A161500");

		ourLog.info("Expected {} - {}", allIds.size(), allIds);
		ourLog.info("Actual   {} - {}", idsSet.size(), idsSet);
		assertEquals(allIds, idsSet);

	}

	/**
	 * Per message from David Hay on Skype
	 */
	@Test
	public void testEverythingWithLargeSet2() {
		Patient p = new Patient();
		p.setActive(true);
		IIdType id = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		for (int i = 1; i < 77; i++) {
			Observation obs = new Observation();
			obs.setId("A" + StringUtils.leftPad(Integer.toString(i), 2, '0'));
			obs.setSubject(new Reference(id));
			myClient.update().resource(obs).execute();
		}

		Bundle responseBundle = myClient.operation().onInstance(id).named("everything").withParameter(Parameters.class, "_count", new IntegerType(50)).useHttpGet().returnResourceType(Bundle.class)
			.execute();

		TreeSet<String> ids = new TreeSet<String>();
		for (int i = 0; i < responseBundle.getEntry().size(); i++) {
			for (BundleEntryComponent nextEntry : responseBundle.getEntry()) {
				ids.add(nextEntry.getResource().getIdElement().getIdPart());
			}
		}

		ourLog.info("Have {} IDs: {}", ids.size(), ids);

		BundleLinkComponent nextLink = responseBundle.getLink("next");
		while (nextLink != null) {
			String nextUrl = nextLink.getUrl();
			responseBundle = myClient.fetchResourceFromUrl(Bundle.class, nextUrl);
			for (int i = 0; i < responseBundle.getEntry().size(); i++) {
				for (BundleEntryComponent nextEntry : responseBundle.getEntry()) {
					ids.add(nextEntry.getResource().getIdElement().getIdPart());
				}
			}

			ourLog.info("Have {} IDs: {}", ids.size(), ids);
			nextLink = responseBundle.getLink("next");
		}

		assertThat(ids).contains(id.getIdPart());
		for (int i = 1; i < 77; i++) {
			assertThat(ids).contains("A" + StringUtils.leftPad(Integer.toString(i), 2, '0'));
		}
		assertThat(ids).hasSize(77);
	}

	@Test
	public void testEverythingWithOnlyPatient() {
		Patient p = new Patient();
		p.setActive(true);
		IIdType id = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		myFhirContext.getRestfulClientFactory().setSocketTimeout(300 * 1000);

		Bundle response = myClient
			.operation()
			.onInstance(id)
			.named("everything")
			.withNoParameters(Parameters.class)
			.returnResourceType(Bundle.class)
			.execute();

		assertThat(response.getEntry()).hasSize(1);
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

	@SuppressWarnings("unused")
	@Test
	public void testFullTextSearch() throws Exception {
		myStorageSettings.setHibernateSearchIndexFullText(true);
		mySearchParamRegistry.forceRefresh();

		Observation obs1 = new Observation();
		obs1.getCode().setText("Systolic Blood Pressure");
		obs1.setStatus(ObservationStatus.FINAL);
		obs1.setValue(new Quantity(123));
		obs1.setComment("obs1");
		IIdType id1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.getCode().setText("Diastolic Blood Pressure");
		obs2.setStatus(ObservationStatus.FINAL);
		obs2.setValue(new Quantity(81));
		IIdType id2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

		HttpGet get = new HttpGet(myServerBase + "/Observation?_content=systolic&_pretty=true");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertThat(responseString).contains(id1.getIdPart());
		} finally {
			response.close();
		}
	}

	@Test
	public void testGetResourceCountsOperation() throws Exception {
		String methodName = "testMetaOperations";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		myClient.create().resource(pt).execute().getId().toUnqualifiedVersionless();

		myResourceCountsCache.clear();
		myResourceCountsCache.update();

		HttpGet get = new HttpGet(myServerBase + "/$get-resource-counts");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			IOUtils.closeQuietly(response.getEntity().getContent());
			ourLog.info(output);
			assertThat(output).contains("<parameter><name value=\"Patient\"/><valueInteger value=\"");
		} finally {
			response.close();
		}
	}

	@Test
	public void testHasParameter() throws Exception {
		IIdType pid0;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			myObservationDao.create(obs, mySrd);
		}
		{
			Device device = new Device();
			device.addIdentifier().setValue("DEVICEID");
			IIdType devId = myDeviceDao.create(device, mySrd).getId().toUnqualifiedVersionless();

			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("NOLINK");
			obs.setDevice(new Reference(devId));
			myObservationDao.create(obs, mySrd);
		}

		String uri = myServerBase + "/Patient?_has:Observation:subject:identifier=" + UrlUtil.escapeUrlParam("urn:system|FOO");
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertThat(ids).containsExactly(pid0.getValue());
	}

	@Test
	public void testHasParameterNoResults() throws Exception {

		HttpGet get = new HttpGet(myServerBase + "/AllergyIntolerance?_has=Provenance:target:userID=12345");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertThat(resp).contains("Invalid _has parameter syntax: _has");
		} finally {
			IOUtils.closeQuietly(response);
		}

	}

	@Test
	public void testHistoryWithAtParameter() throws Exception {
		String methodName = "testHistoryWithFromAndTo";

		Patient patient = new Patient();
		patient.addName().setFamily(methodName);

		List<Date> preDates = Lists.newArrayList();
		List<String> ids = Lists.newArrayList();

		IIdType idCreated = myPatientDao.create(patient, mySrd).getId();
		ids.add(idCreated.toUnqualified().getValue());
		IIdType id = idCreated.toUnqualifiedVersionless();

		for (int i = 0; i < 10; i++) {
			Thread.sleep(100);
			preDates.add(new Date());
			Thread.sleep(100);
			patient.setId(id);
			patient.getName().get(0).getFamilyElement().setValue(methodName + "_i" + i);
			ids.add(myPatientDao.update(patient, mySrd).getId().toUnqualified().getValue());
		}

		List<String> idValues;

		idValues = searchAndReturnUnqualifiedIdValues(myServerBase + "/Patient/" + id.getIdPart() + "/_history?_at=gt" + toStr(preDates.get(0)) + "&_at=lt" + toStr(preDates.get(3)));
		assertThat(idValues).as(idValues.toString()).containsExactly(ids.get(3), ids.get(2), ids.get(1), ids.get(0));

		idValues = searchAndReturnUnqualifiedIdValues(myServerBase + "/Patient/_history?_at=gt" + toStr(preDates.get(0)) + "&_at=lt" + toStr(preDates.get(3)));
		assertThat(idValues).as(idValues.toString()).containsExactly(ids.get(3), ids.get(2), ids.get(1));

		idValues = searchAndReturnUnqualifiedIdValues(myServerBase + "/_history?_at=gt" + toStr(preDates.get(0)) + "&_at=lt" + toStr(preDates.get(3)));
		assertThat(idValues).as(idValues.toString()).containsExactly(ids.get(3), ids.get(2), ids.get(1));

		idValues = searchAndReturnUnqualifiedIdValues(myServerBase + "/_history?_at=gt2060");
		assertThat(idValues).as(idValues.toString()).isEmpty();

		idValues = searchAndReturnUnqualifiedIdValues(myServerBase + "/_history?_at=" + InstantDt.withCurrentTime().getYear());
		assertThat(idValues).as(idValues.toString()).hasSize(10); // 10 is the page size

		idValues = searchAndReturnUnqualifiedIdValues(myServerBase + "/_history?_at=ge" + InstantDt.withCurrentTime().getYear());
		assertThat(idValues).as(idValues.toString()).hasSize(10);

		idValues = searchAndReturnUnqualifiedIdValues(myServerBase + "/_history?_at=gt" + (InstantDt.withCurrentTime().getYear() + 1));
		assertThat(idValues).hasSize(0);
	}

	@Test
	public void testHistoryWithDeletedResource() {
		String methodName = "testHistoryWithDeletedResource";

		Patient patient = new Patient();
		patient.addName().setFamily(methodName);
		IIdType id = myClient.create().resource(patient).execute().getId().toVersionless();
		myClient.delete().resourceById(id).execute();
		patient.setId(id);
		myClient.update().resource(patient).execute();

		Bundle history = myClient.history().onInstance(id).andReturnBundle(Bundle.class).prettyPrint().summaryMode(SummaryEnum.DATA).execute();
		assertThat(history.getEntry()).hasSize(3);
		assertEquals(id.withVersion("3").getValue(), history.getEntry().get(0).getResource().getId());
		assertThat(((Patient) history.getEntry().get(0).getResource()).getName()).hasSize(1);

		assertEquals(HTTPVerb.DELETE, history.getEntry().get(1).getRequest().getMethodElement().getValue());
		assertEquals("Patient/" + id.getIdPart() + "/_history/2", history.getEntry().get(1).getRequest().getUrl());
		assertNull(history.getEntry().get(1).getResource());

		assertEquals(id.withVersion("1").getValue(), history.getEntry().get(2).getResource().getId());
		assertThat(((Patient) history.getEntry().get(2).getResource()).getName()).hasSize(1);

		try {
			myBundleDao.validate(history, null, null, null, null, null, mySrd);
		} catch (PreconditionFailedException e) {
			ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
			throw e;
		}
	}

	@Test
	public void testIdAndVersionInBodyForCreate() throws IOException {
		String methodName = "testIdAndVersionInBodyForCreate";

		Patient pt = new Patient();
		pt.setId("Patient/AAA/_history/4");
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(pt);

		ourLog.info("Input: {}", resource);

		HttpPost post = new HttpPost(myServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		IdType id;
		try {
			String respString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", respString);
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString).startsWith(myServerBase + "/Patient/");
			id = new IdType(newIdString);
		} finally {
			response.close();
		}

		assertEquals("1", id.getVersionIdPart());
		assertThat(id.getIdPart()).isNotEqualTo("AAA");

		HttpGet get = new HttpGet(myServerBase + "/Patient/" + id.getIdPart());
		response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String respString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", respString);
			assertThat(respString).contains("<id value=\"" + id.getIdPart() + "\"/>");
			assertThat(respString).contains("<versionId value=\"1\"/>");
		} finally {
			response.close();
		}
	}

	@Test
	public void testIdAndVersionInBodyForUpdate() throws IOException {
		String methodName = "testIdAndVersionInBodyForUpdate";

		Patient pt = new Patient();
		pt.setId("Patient/AAA/_history/4");
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(pt);

		ourLog.info("Input: {}", resource);

		HttpPost post = new HttpPost(myServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		IdType id;
		try {
			String respString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", respString);
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString).startsWith(myServerBase + "/Patient/");
			id = new IdType(newIdString);
		} finally {
			response.close();
		}

		assertEquals("1", id.getVersionIdPart());
		assertThat(id.getIdPart()).isNotEqualTo("AAA");

		HttpPut put = new HttpPut(myServerBase + "/Patient/" + id.getIdPart() + "/_history/1");
		put.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		response = ourHttpClient.execute(put);
		try {
			String respString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", respString);
			assertEquals(400, response.getStatusLine().getStatusCode());
			OperationOutcome oo = myFhirContext.newXmlParser().parseResource(OperationOutcome.class, respString);
			assertThat(oo.getIssue().get(0).getDiagnostics()).isEqualTo(Msg.code(420) + "Can not update resource, resource body must contain an ID element which matches the request URL for update (PUT) operation - Resource body ID of \"AAA\" does not match URL ID of \""
				+ id.getIdPart() + "\"");
		} finally {
			response.close();
		}

	}

	/**
	 * See issue #52
	 */
	@Test
	public void testImagingStudyResources() throws Exception {
		IGenericClient client = myClient;

		int initialSize = client.search().forResource(ImagingStudy.class).returnBundle(Bundle.class).execute().getEntry().size();

		String resBody = IOUtils.toString(ResourceProviderDstu3Test.class.getResource("/imagingstudy.json"), StandardCharsets.UTF_8);
		client.create().resource(resBody).execute();

		int newSize = client.search().forResource(ImagingStudy.class).returnBundle(Bundle.class).execute().getEntry().size();

		assertEquals(1, newSize - initialSize);

	}

	@Test
	public void testIncludeWithExternalReferences() {
		myStorageSettings.setAllowExternalReferences(true);

		Patient p = new Patient();
		p.getManagingOrganization().setReference("http://example.com/Organization/123");
		myClient.create().resource(p).execute();

		Bundle b = myClient.search().forResource("Patient").include(Patient.INCLUDE_ORGANIZATION).returnBundle(Bundle.class).execute();
		assertThat(b.getEntry()).hasSize(1);
	}

	@Test
	public void testMetadata() throws Exception {
		HttpGet get = new HttpGet(myServerBase + "/metadata");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp).contains("THIS IS THE DESC");
		} finally {
			IOUtils.closeQuietly(response.getEntity().getContent());
			response.close();
		}
	}

	@SuppressWarnings("unused")
	@Test
	public void testMetadataSuperParamsAreIncluded() {
		StructureDefinition p = new StructureDefinition();
		p.setAbstract(true);
		p.setUrl("http://example.com/foo");
		IIdType id = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		Bundle resp = myClient
			.search()
			.forResource(StructureDefinition.class)
			.where(StructureDefinition.URL.matches().value("http://example.com/foo"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(1, resp.getTotal());
	}

	@Test
	public void testMetaOperations() {
		String methodName = "testMetaOperations";
		myClient.registerInterceptor(new LoggingInterceptor(true));
		myClient.setPrettyPrint(true);
		IValidatorModule module = new FhirInstanceValidator(myFhirContext);
		BaseValidatingInterceptor<String> validatingInterceptor = new RequestValidatingInterceptor().addValidatorModule(module);
		myRestServer.registerInterceptor(validatingInterceptor);
		try {
			Patient pt = new Patient();
			pt.addName().setFamily(methodName);
			IIdType id = myClient.create().resource(pt).execute().getId().toUnqualifiedVersionless();

			Meta meta = myClient.meta().get(Meta.class).fromResource(id).execute();
			assertThat(meta.getTag()).isEmpty();

			Meta inMeta = new Meta();
			inMeta.addTag().setSystem("urn:system1").setCode("urn:code1");
			meta = myClient.meta().add().onResource(id).meta(inMeta).execute();
			assertThat(meta.getTag()).hasSize(1);

			inMeta = new Meta();
			inMeta.addTag().setSystem("urn:system1").setCode("urn:code1");
			meta = myClient.meta().delete().onResource(id).meta(inMeta).execute();
			assertThat(meta.getTag()).isEmpty();
		} finally {
			myRestServer.unregisterInterceptor(validatingInterceptor);
		}
	}

	@Test
	public void testMetaOperationWithNoMetaParameter() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("testMetaAddInvalid");
		IIdType id = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		String input = """
			<Parameters>
			  <meta>
			    <tag>
			      <system value="http://example.org/codes/tags"/>
			      <code value="record-lost"/>
			      <display value="Patient File Lost"/>
			    </tag>
			  </meta>
			</Parameters>""";

		HttpPost post = new HttpPost(myServerBase + "/Patient/" + id.getIdPart() + "/$meta-add");
		post.setEntity(new StringEntity(input, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(output);
			assertEquals(400, response.getStatusLine().getStatusCode());
			assertThat(output).contains("Input contains no parameter with name 'meta'");
		} finally {
			response.close();
		}

		post = new HttpPost(myServerBase + "/Patient/" + id.getIdPart() + "/$meta-delete");
		post.setEntity(new StringEntity(input, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		response = ourHttpClient.execute(post);
		try {
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(output);
			assertEquals(400, response.getStatusLine().getStatusCode());
			assertThat(output).contains("Input contains no parameter with name 'meta'");
		} finally {
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

		Bundle response = myClient
			.operation()
			.onInstance(new IdType(pid))
			.named("everything")
			.withSearchParameter(Parameters.class, "_count", new NumberParam(10))
			.returnResourceType(Bundle.class)
			.useHttpGet()
			.execute();

		assertThat(response.getEntry()).hasSize(10);
		if (response.getTotalElement().getValueAsString() != null) {
			assertEquals("21", response.getTotalElement().getValueAsString());
		}
		assertThat(response.getLink("next").getUrl()).isNotEmpty();

		// Load page 2

		String nextUrl = response.getLink("next").getUrl();
		response = myClient.fetchResourceFromUrl(Bundle.class, nextUrl);

		assertThat(response.getEntry()).hasSize(10);
		if (response.getTotalElement().getValueAsString() != null) {
			assertEquals("21", response.getTotalElement().getValueAsString());
		}
		assertThat(response.getLink("next").getUrl()).isNotEmpty();

		// Load page 3
		Thread.sleep(2000);

		nextUrl = response.getLink("next").getUrl();
		response = myClient.fetchResourceFromUrl(Bundle.class, nextUrl);

		assertThat(response.getEntry()).hasSize(1);
		assertEquals("21", response.getTotalElement().getValueAsString());
		assertNull(response.getLink("next"));

	}

	@Test
	public void testEverythingWithNoPagingProvider() {
		IPagingProvider pagingProvider = myRestServer.getPagingProvider();
		try {
			myRestServer.setPagingProvider(null);

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

			Bundle response = myClient
				.operation()
				.onInstance(new IdType(pid))
				.named("everything")
				.withSearchParameter(Parameters.class, "_count", new NumberParam(10))
				.returnResourceType(Bundle.class)
				.useHttpGet()
				.execute();

			assertThat(response.getEntry()).hasSize(10);
			assertNull(response.getTotalElement().getValue());
			assertNull(response.getLink("next"));
		} finally {
			myRestServer.setPagingProvider(pagingProvider);
		}
	}

	@Test
	public void testPreserveVersionsOnAuditEvent() {
		Organization org = new Organization();
		org.setName("ORG");
		IIdType orgId = myClient.create().resource(org).execute().getId();
		assertEquals("1", orgId.getVersionIdPart());

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.getManagingOrganization().setReference(orgId.toUnqualified().getValue());
		IIdType patientId = myClient.create().resource(patient).execute().getId();
		assertEquals("1", patientId.getVersionIdPart());

		AuditEvent ae = new org.hl7.fhir.dstu3.model.AuditEvent();
		ae.addEntity().getReference().setReference(patientId.toUnqualified().getValue());
		IIdType aeId = myClient.create().resource(ae).execute().getId();
		assertEquals("1", aeId.getVersionIdPart());

		patient = myClient.read().resource(Patient.class).withId(patientId).execute();
		assertTrue(patient.getManagingOrganization().getReferenceElement().hasIdPart());
		assertFalse(patient.getManagingOrganization().getReferenceElement().hasVersionIdPart());

		ae = myClient.read().resource(AuditEvent.class).withId(aeId).execute();
		assertTrue(ae.getEntityFirstRep().getReference().getReferenceElement().hasIdPart());
		assertTrue(ae.getEntityFirstRep().getReference().getReferenceElement().hasVersionIdPart());

	}

	/**
	 * See issue #52
	 */
	@Test
	public void testProcedureRequestResources() {
		IGenericClient client = myClient;

		int initialSize = client.search().forResource(ProcedureRequest.class).returnBundle(Bundle.class).execute().getEntry().size();

		ProcedureRequest res = new ProcedureRequest();
		res.addIdentifier().setSystem("urn:foo").setValue("123");

		client.create().resource(res).execute();

		int newSize = client.search().forResource(ProcedureRequest.class).returnBundle(Bundle.class).execute().getEntry().size();

		assertEquals(1, newSize - initialSize);

	}

	/**
	 * Test for issue #60
	 */
	@Test
	public void testReadAllInstancesOfType() {
		Patient pat;

		pat = new Patient();
		pat.addIdentifier().setSystem("urn:system").setValue("testReadAllInstancesOfType_01");
		myClient.create().resource(pat).prettyPrint().encodedXml().execute().getId();

		pat = new Patient();
		pat.addIdentifier().setSystem("urn:system").setValue("testReadAllInstancesOfType_02");
		myClient.create().resource(pat).prettyPrint().encodedXml().execute().getId();

		{
			Bundle returned = myClient.search().forResource(Patient.class).encodedXml().returnBundle(Bundle.class).execute();
			assertThat(returned.getEntry().size()).isGreaterThan(1);
			assertEquals(BundleType.SEARCHSET, returned.getType());
		}
		{
			Bundle returned = myClient.search().forResource(Patient.class).encodedJson().returnBundle(Bundle.class).execute();
			assertThat(returned.getEntry().size()).isGreaterThan(1);
		}
	}

	@Test
	public void testSaveAndRetrieveExistingNarrativeJson() {
		Patient p1 = new Patient();
		p1.getText().setStatus(NarrativeStatus.GENERATED);
		p1.getText().getDiv().setValueAsString("<div>HELLO WORLD</div>");
		p1.addIdentifier().setSystem("urn:system").setValue("testSaveAndRetrieveExistingNarrative01");

		IIdType newId = myClient.create().resource(p1).encodedJson().execute().getId();

		Patient actual = myClient.read().resource(Patient.class).withId(newId).encodedJson().execute();
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">HELLO WORLD</div>", actual.getText().getDiv().getValueAsString());
	}

	@Test
	public void testSaveAndRetrieveExistingNarrativeXml() {
		Patient p1 = new Patient();
		p1.getText().setStatus(NarrativeStatus.GENERATED);
		p1.getText().getDiv().setValueAsString("<div>HELLO WORLD</div>");
		p1.addIdentifier().setSystem("urn:system").setValue("testSaveAndRetrieveExistingNarrative01");

		IIdType newId = myClient.create().resource(p1).encodedXml().execute().getId();

		Patient actual = myClient.read().resource(Patient.class).withId(newId).encodedXml().execute();
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">HELLO WORLD</div>", actual.getText().getDiv().getValueAsString());
	}

	@Test
	public void testSaveAndRetrieveResourceWithExtension() {
		Patient nextPatient = new Patient();
		nextPatient.setId("Patient/B");
		nextPatient
			.addExtension()
			.setUrl("http://foo")
			.setValue(new Reference("Practitioner/A"));

		myClient.update().resource(nextPatient).execute();

		Patient p = myClient.read().resource(Patient.class).withId("B").execute();

		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(encoded);

		assertThat(encoded).contains("http://foo");
	}

	@Test
	public void testSaveAndRetrieveWithContained() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSaveAndRetrieveWithContained01");

		Organization o1 = new Organization();
		o1.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSaveAndRetrieveWithContained02");

		p1.getManagingOrganization().setResource(o1);

		IIdType newId = myClient.create().resource(p1).execute().getId();

		Patient actual = myClient.read(Patient.class, new UriDt(newId.getValue()));
		assertThat(actual.getContained()).hasSize(1);

		//@formatter:off
		Bundle b = myClient
			.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system:rpdstu2", "testSaveAndRetrieveWithContained01"))
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertThat(b.getEntry()).hasSize(1);

	}

	@Test
	public void testSaveAndRetrieveWithoutNarrative() {
		Patient p1 = new Patient();
		p1.getText().setDivAsString("<div><td>Identifier</td><td>testSearchByResourceChain01</td></div>");
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByResourceChain01");

		IdType newId = (IdType) myClient.create().resource(p1).execute().getId();

		Patient actual = myClient.read(Patient.class, newId.getIdPart());
		assertThat(actual.getText().getDiv().getValueAsString()).contains("<td>Identifier</td><td>testSearchByResourceChain01</td>");
	}

	@Test
	public void testSearchBundleDoesntIncludeTextElement() throws Exception {
		HttpGet read = new HttpGet(myServerBase + "/Patient?_format=json");
		CloseableHttpResponse response = ourHttpClient.execute(read);
		try {
			String text = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(text);
			assertEquals(Constants.STATUS_HTTP_200_OK, response.getStatusLine().getStatusCode());
			assertThat(text).doesNotContain("\"text\",\"type\"");
		} finally {
			response.close();
		}
	}

	@Test
	public void testSearchByExtendedChars() throws Exception {
		for (int i = 0; i < 10; i++) {
			Patient p = new Patient();
			p.addName().setFamily("Jernelöv");
			p.addIdentifier().setValue("ID" + i);
			myPatientDao.create(p, mySrd);
		}

		String uri = myServerBase + "/Patient?name=" + UrlUtil.escapeUrlParam("Jernelöv") + "&_count=5&_pretty=true";
		ourLog.info("URI: {}", uri);
		HttpGet get = new HttpGet(uri);
		CloseableHttpResponse resp = ourHttpClient.execute(get);
		try {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			String output = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(output);

			Bundle b = myFhirContext.newXmlParser().parseResource(Bundle.class, output);

			assertEquals("http://localhost:" + myPort + "/fhir/context/Patient?_count=5&_pretty=true&name=Jernel%C3%B6v", b.getLink("self").getUrl());

			Patient p = (Patient) b.getEntry().get(0).getResource();
			assertEquals("Jernelöv", p.getName().get(0).getFamily());

		} finally {
			IOUtils.closeQuietly(resp.getEntity().getContent());
		}

	}

	@Test
	public void testSearchByIdentifier() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByIdentifier01");
		p1.addName().setFamily("testSearchByIdentifierFamily01").addGiven("testSearchByIdentifierGiven01");
		IdType p1Id = (IdType) myClient.create().resource(p1).execute().getId();

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue("testSearchByIdentifier02");
		p2.addName().setFamily("testSearchByIdentifierFamily01").addGiven("testSearchByIdentifierGiven02");
		myClient.create().resource(p2).execute().getId();

		//@formatter:off
		Bundle actual = myClient
			.search()
			.forResource(Patient.class)
			.where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system", "testSearchByIdentifier01"))
			.encodedJson()
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertThat(actual.getEntry()).hasSize(1);
		assertEquals(myServerBase + "/Patient/" + p1Id.getIdPart(), actual.getEntry().get(0).getFullUrl());
		assertEquals(p1Id.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());
		assertEquals(SearchEntryMode.MATCH, actual.getEntry().get(0).getSearch().getModeElement().getValue());
	}

	@Test
	public void testSearchByIdentifierWithoutSystem() {

		Patient p1 = new Patient();
		p1.addIdentifier().setValue("testSearchByIdentifierWithoutSystem01");
		IdType p1Id = (IdType) myClient.create().resource(p1).execute().getId();

		//@formatter:off
		Bundle actual = myClient
			.search()
			.forResource(Patient.class)
			.where(Patient.IDENTIFIER.exactly().systemAndCode(null, "testSearchByIdentifierWithoutSystem01"))
			.encodedJson()
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertThat(actual.getEntry()).hasSize(1);
		assertEquals(p1Id.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());

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

		Bundle found;

		found = myClient
			.search()
			.forResource(Patient.class)
			.where(BaseResource.RES_ID.exactly().systemAndValues(null, id1.getIdPart(), id2.getIdPart()))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(toUnqualifiedVersionlessIds(found)).containsExactlyInAnyOrder(id1, id2);

		found = myClient
			.search()
			.forResource(Patient.class)
			.where(BaseResource.RES_ID.exactly().systemAndValues(null, Arrays.asList(id1.getIdPart(), id2.getIdPart(), "FOOOOO")))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(toUnqualifiedVersionlessIds(found)).containsExactlyInAnyOrder(id1, id2);

		found = myClient
			.search()
			.forResource(Patient.class)
			.where(BaseResource.RES_ID.exactly().systemAndCode(null, id1.getIdPart()))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(toUnqualifiedVersionlessIds(found)).containsExactlyInAnyOrder(id1);

		found = myClient
			.search()
			.forResource(Patient.class)
			.where(BaseResource.RES_ID.exactly().codes(id1.getIdPart(), id2.getIdPart()))
			.and(BaseResource.RES_ID.exactly().code(id1.getIdPart()))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(toUnqualifiedVersionlessIds(found)).as(toUnqualifiedVersionlessIdValues(found).toString()).containsExactlyInAnyOrder(id1);

		found = myClient
			.search()
			.forResource(Patient.class)
			.where(BaseResource.RES_ID.exactly().codes(Arrays.asList(id1.getIdPart(), id2.getIdPart(), "FOOOOO")))
			.and(BaseResource.RES_ID.exactly().code(id1.getIdPart()))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(toUnqualifiedVersionlessIds(found)).containsExactlyInAnyOrder(id1);

		found = myClient
			.search()
			.forResource(Patient.class)
			.where(BaseResource.RES_ID.exactly().codes(id1.getIdPart(), id2.getIdPart(), "FOOO"))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(toUnqualifiedVersionlessIds(found)).containsExactlyInAnyOrder(id1, id2);

		found = myClient
			.search()
			.forResource(Patient.class)
			.where(BaseResource.RES_ID.exactly().codes("FOOO"))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(toUnqualifiedVersionlessIds(found)).isEmpty();

	}

	@Test
	public void testSearchByLastUpdated() throws Exception {
		String methodName = "testSearchByLastUpdated";

		Patient p = new Patient();
		p.addName().setFamily(methodName + "1");
		IIdType pid1 = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		Thread.sleep(10);
		long time1 = System.currentTimeMillis();
		Thread.sleep(10);

		Patient p2 = new Patient();
		p2.addName().setFamily(methodName + "2");
		IIdType pid2 = myClient.create().resource(p2).execute().getId().toUnqualifiedVersionless();

		HttpGet get = new HttpGet(myServerBase + "/Patient?_lastUpdated=lt" + new InstantType(new Date(time1)).getValueAsString());
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			IOUtils.closeQuietly(response.getEntity().getContent());
			ourLog.info(output);
			List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirContext.newXmlParser().parseResource(Bundle.class, output));
			ourLog.info(ids.toString());
			assertThat(ids).containsExactlyInAnyOrder(pid1);
		} finally {
			response.close();
		}

		get = new HttpGet(myServerBase + "/Patient?_lastUpdated=gt" + new InstantType(new Date(time1)).getValueAsString());
		response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			IOUtils.closeQuietly(response.getEntity().getContent());
			ourLog.info(output);
			List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirContext.newXmlParser().parseResource(Bundle.class, output));
			ourLog.info(ids.toString());
			assertThat(ids).containsExactlyInAnyOrder(pid2);
		} finally {
			response.close();
		}

	}

	@Test
	public void testSearchByReferenceIds() {
		Organization o1 = new Organization();
		o1.setName("testSearchByResourceChainName01");
		IIdType o1id = myClient.create().resource(o1).execute().getId().toUnqualifiedVersionless();
		Organization o2 = new Organization();
		o2.setName("testSearchByResourceChainName02");
		IIdType o2id = myClient.create().resource(o2).execute().getId().toUnqualifiedVersionless();

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByReferenceIds01");
		p1.addName().setFamily("testSearchByReferenceIdsFamily01").addGiven("testSearchByReferenceIdsGiven01");
		p1.setManagingOrganization(new Reference(o1id.toUnqualifiedVersionless()));
		IIdType p1Id = myClient.create().resource(p1).execute().getId();

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue("testSearchByReferenceIds02");
		p2.addName().setFamily("testSearchByReferenceIdsFamily02").addGiven("testSearchByReferenceIdsGiven02");
		p2.setManagingOrganization(new Reference(o2id.toUnqualifiedVersionless()));
		IIdType p2Id = myClient.create().resource(p2).execute().getId();

		//@formatter:off
		Bundle actual = myClient.search()
			.forResource(Patient.class)
			.where(Patient.ORGANIZATION.hasAnyOfIds(Arrays.asList(o1id.getIdPart(), o2id.getIdPart())))
			.encodedJson().prettyPrint().returnBundle(Bundle.class).execute();
		//@formatter:on
		Set<String> expectedIds = new HashSet<String>();
		expectedIds.add(p1Id.getIdPart());
		expectedIds.add(p2Id.getIdPart());
		Set<String> actualIds = new HashSet<String>();
		for (BundleEntryComponent ele : actual.getEntry()) {
			actualIds.add(ele.getResource().getIdElement().getIdPart());
		}
		assertThat(actualIds).as("Expects to retrieve the 2 patients which reference the two different organizations").isEqualTo(expectedIds);
	}

	@Test
	public void testSearchByResourceChain() {

		Organization o1 = new Organization();
		o1.setName("testSearchByResourceChainName01");
		IdType o1id = (IdType) myClient.create().resource(o1).execute().getId().toUnqualifiedVersionless();

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByResourceChain01");
		p1.addName().setFamily("testSearchByResourceChainFamily01").addGiven("testSearchByResourceChainGiven01");
		p1.setManagingOrganization(new Reference(o1id.toUnqualifiedVersionless()));
		IdType p1Id = (IdType) myClient.create().resource(p1).execute().getId();

		//@formatter:off
		Bundle actual = myClient.search()
			.forResource(Patient.class)
			.where(Patient.ORGANIZATION.hasId(o1id.getIdPart()))
			.encodedJson().prettyPrint().returnBundle(Bundle.class).execute();
		//@formatter:on
		assertThat(actual.getEntry()).hasSize(1);
		assertEquals(p1Id.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());

		//@formatter:off
		actual = myClient.search()
			.forResource(Patient.class)
			.where(Patient.ORGANIZATION.hasId(o1id.getValue()))
			.encodedJson().prettyPrint().returnBundle(Bundle.class).execute();
		//@formatter:on
		assertThat(actual.getEntry()).hasSize(1);
		assertEquals(p1Id.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());

	}

	@Test
	public void testSearchInvalidParam() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("0");
		patient.addName().setFamily("testSearchWithMixedParams").addGiven("Joe");
		myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		// should be subject._id
		HttpGet httpPost = new HttpGet(myServerBase + "/Observation?subject.id=FOO");

		CloseableHttpResponse resp = ourHttpClient.execute(httpPost);
		try {
			String respString = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.debug(respString);
			assertThat(respString).contains("Invalid parameter chain: subject.id");
			assertEquals(400, resp.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(resp.getEntity().getContent());
		}
		ourLog.info("Outgoing post: {}", httpPost);
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
			patient.addName().setFamily(methodName).addGiven("Joe");
			id1a = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();
		}
		IIdType id1b;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().setFamily(methodName + "XXXX").addGiven("Joe");
			id1b = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();
		}

		Thread.sleep(1100);
		DateTimeType beforeR2 = new DateTimeType(new Date(), TemporalPrecisionEnum.MILLI);
		Thread.sleep(1100);

		IIdType id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().setFamily(methodName).addGiven("John");
			id2 = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();
		}

		{
			//@formatter:off
			Bundle found = myClient.search()
				.forResource(Patient.class)
				.where(Patient.NAME.matches().value("testSearchLastUpdatedParamRp"))
				.returnBundle(Bundle.class)
				.execute();
			//@formatter:on
			List<IIdType> patients = toUnqualifiedVersionlessIds(found);
			assertThat(patients).contains(id1a, id1b, id2);
		}
		{
			//@formatter:off
			Bundle found = myClient.search()
				.forResource(Patient.class)
				.where(Patient.NAME.matches().value("testSearchLastUpdatedParamRp"))
				.lastUpdated(new DateRangeParam(beforeAny, null))
				.returnBundle(Bundle.class)
				.execute();
			//@formatter:on
			List<IIdType> patients = toUnqualifiedVersionlessIds(found);
			assertThat(patients).contains(id1a, id1b, id2);
		}
		{
			//@formatter:off
			Bundle found = myClient.search()
				.forResource(Patient.class)
				.where(Patient.NAME.matches().value("testSearchLastUpdatedParamRp"))
				.lastUpdated(new DateRangeParam(beforeR2, null))
				.returnBundle(Bundle.class)
				.execute();
			//@formatter:on
			List<IIdType> patients = toUnqualifiedVersionlessIds(found);
			assertThat(patients).contains(id2);
			assertThat(patients).doesNotContain(id1a,id1b);
		}
		{
			//@formatter:off
			Bundle found = myClient.search()
				.forResource(Patient.class)
				.where(Patient.NAME.matches().value("testSearchLastUpdatedParamRp"))
				.lastUpdated(new DateRangeParam(beforeAny, beforeR2))
				.returnBundle(Bundle.class)
				.execute();
			//@formatter:on
			List<IIdType> patients = toUnqualifiedVersionlessIds(found);
			assertThat(patients).doesNotContain(id2);
			assertThat(patients).contains(id1a, id1b);
		}
		{
			//@formatter:off
			Bundle found = myClient.search()
				.forResource(Patient.class)
				.where(Patient.NAME.matches().value("testSearchLastUpdatedParamRp"))
				.lastUpdated(new DateRangeParam(null, beforeR2))
				.returnBundle(Bundle.class)
				.execute();
			//@formatter:on
			List<IIdType> patients = toUnqualifiedVersionlessIds(found);
			assertThat(patients).doesNotContain(id2);
			assertThat(patients).contains(id1a, id1b);
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
		ma.setMedication(new Reference(medId));
		IIdType moId = myMedicationAdministrationDao.create(ma).getId().toUnqualifiedVersionless();

		HttpGet get = new HttpGet(myServerBase + "/MedicationAdministration?medication.code=04823543");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertThat(responseString).contains(moId.getIdPart());
		} finally {
			response.close();
		}

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchPagingKeepsOldSearches() {
		String methodName = "testSearchPagingKeepsOldSearches";
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		for (int i = 1; i <= 20; i++) {
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(Integer.toString(i));
			patient.addName().setFamily(methodName).addGiven("Joe");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		List<String> linkNext = Lists.newArrayList();
		for (int i = 0; i < 100; i++) {
			Bundle bundle = myClient.search().forResource(Patient.class).where(Patient.NAME.matches().value("testSearchPagingKeepsOldSearches")).count(5).returnBundle(Bundle.class).execute();
			assertTrue(isNotBlank(bundle.getLink("next").getUrl()));
			assertThat(bundle.getEntry()).hasSize(5);
			linkNext.add(bundle.getLink("next").getUrl());
		}

		int index = 0;
		for (String nextLink : linkNext) {
			ourLog.info("Fetching index {}", index++);
			Bundle b = myClient.fetchResourceFromUrl(Bundle.class, nextLink);
			assertThat(b.getEntry()).hasSize(5);
		}
	}

	private void testSearchReturnsResults(String search) throws IOException {
		int matches;
		HttpGet get = new HttpGet(myServerBase + search);
		CloseableHttpResponse response = ourHttpClient.execute(get);
		String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(response.getEntity().getContent());
		ourLog.info(resp);
		Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, resp);
		matches = bundle.getTotal();

		assertThat(matches).isGreaterThan(0);
	}

	@Test
	public void testSearchReturnsSearchDate() throws Exception {
		Date before = new Date();
		Thread.sleep(1);

		//@formatter:off
		Bundle found = myClient
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
		assertThat(value.before(after)).as(new InstantDt(value) + " should be before " + new InstantDt(after)).isTrue();
	}

	@Test
	public void testSearchReusesNoParams() {
		List<IBaseResource> resources = new ArrayList<IBaseResource>();
		for (int i = 0; i < 50; i++) {
			Organization org = new Organization();
			org.setName("HELLO");
			resources.add(org);
		}
		myClient.transaction().withResources(resources).prettyPrint().encodedXml().execute();

		myStorageSettings.setReuseCachedSearchResultsForMillis(1000L);

		Bundle result1 = myClient
			.search()
			.forResource("Organization")
			.returnBundle(Bundle.class)
			.execute();

		final String uuid1 = toSearchUuidFromLinkNext(result1);

		Bundle result2 = myClient
			.search()
			.forResource("Organization")
			.returnBundle(Bundle.class)
			.execute();

		final String uuid2 = toSearchUuidFromLinkNext(result2);

		assertEquals(uuid1, uuid2);
	}

	@Test
	public void testSearchReusesResultsDisabled() {
		List<IBaseResource> resources = new ArrayList<IBaseResource>();
		for (int i = 0; i < 50; i++) {
			Organization org = new Organization();
			org.setName("HELLO");
			resources.add(org);
		}
		myClient.transaction().withResources(resources).prettyPrint().encodedXml().execute();

		myStorageSettings.setReuseCachedSearchResultsForMillis(null);

		Bundle result1 = myClient
			.search()
			.forResource("Organization")
			.where(Organization.NAME.matches().value("HELLO"))
			.count(5)
			.returnBundle(Bundle.class)
			.execute();

		final String uuid1 = toSearchUuidFromLinkNext(result1);

		Bundle result2 = myClient
			.search()
			.forResource("Organization")
			.where(Organization.NAME.matches().value("HELLO"))
			.count(5)
			.returnBundle(Bundle.class)
			.execute();

		final String uuid2 = toSearchUuidFromLinkNext(result2);

		Bundle result3 = myClient
			.search()
			.forResource("Organization")
			.where(Organization.NAME.matches().value("HELLO"))
			.count(5)
			.returnBundle(Bundle.class)
			.execute();

		String uuid3 = toSearchUuidFromLinkNext(result3);

		assertThat(uuid2).isNotEqualTo(uuid1);
		assertThat(uuid3).isNotEqualTo(uuid1);
	}

	@Test
	public void testSearchReusesResultsEnabled() throws Exception {
		List<IBaseResource> resources = new ArrayList<IBaseResource>();
		for (int i = 0; i < 50; i++) {
			Organization org = new Organization();
			org.setName("HELLO");
			resources.add(org);
		}
		myClient.transaction().withResources(resources).prettyPrint().encodedXml().execute();

		myStorageSettings.setReuseCachedSearchResultsForMillis(1000L);

		Bundle result1 = myClient
			.search()
			.forResource("Organization")
			.where(Organization.NAME.matches().value("HELLO"))
			.count(5)
			.returnBundle(Bundle.class)
			.execute();

		final String uuid1 = toSearchUuidFromLinkNext(result1);
		Search search1 = newTxTemplate().execute(new TransactionCallback<Search>() {
			@Override
			public Search doInTransaction(TransactionStatus theStatus) {
				return mySearchEntityDao.findByUuidAndFetchIncludes(uuid1).orElseThrow(() -> new InternalErrorException(""));
			}
		});
		Date created1 = search1.getCreated();

		Bundle result2 = myClient
			.search()
			.forResource("Organization")
			.where(Organization.NAME.matches().value("HELLO"))
			.count(5)
			.returnBundle(Bundle.class)
			.execute();

		final String uuid2 = toSearchUuidFromLinkNext(result2);
		Search search2 = newTxTemplate().execute(new TransactionCallback<Search>() {
			@Override
			public Search doInTransaction(TransactionStatus theStatus) {
				return mySearchEntityDao.findByUuidAndFetchIncludes(uuid2).orElseThrow(() -> new InternalErrorException(""));
			}
		});
		Date created2 = search2.getCreated();

		assertEquals(created2.getTime(), created1.getTime());

		Thread.sleep(1500);

		Bundle result3 = myClient
			.search()
			.forResource("Organization")
			.where(Organization.NAME.matches().value("HELLO"))
			.count(5)
			.returnBundle(Bundle.class)
			.execute();

		String uuid3 = toSearchUuidFromLinkNext(result3);

		assertEquals(uuid1, uuid2);
		assertThat(uuid3).isNotEqualTo(uuid1);
	}

	@Test
	public void testSearchReusesResultsEnabledNoParams() {
		List<IBaseResource> resources = new ArrayList<IBaseResource>();
		for (int i = 0; i < 50; i++) {
			Organization org = new Organization();
			org.setName("HELLO");
			resources.add(org);
		}
		myClient.transaction().withResources(resources).prettyPrint().encodedXml().execute();

		myStorageSettings.setReuseCachedSearchResultsForMillis(100000L);

		Bundle result1 = myClient
			.search()
			.forResource("Organization")
			.returnBundle(Bundle.class)
			.execute();

		final String uuid1 = toSearchUuidFromLinkNext(result1);
		Search search1 = newTxTemplate().execute(theStatus -> mySearchEntityDao.findByUuidAndFetchIncludes(uuid1).orElseThrow(() -> new InternalErrorException("")));
		Date created1 = search1.getCreated();

		Bundle result2 = myClient
			.search()
			.forResource("Organization")
			.returnBundle(Bundle.class)
			.execute();

		final String uuid2 = toSearchUuidFromLinkNext(result2);
		Search search2 = newTxTemplate().execute(theStatus -> mySearchEntityDao.findByUuidAndFetchIncludes(uuid2).orElseThrow(() -> new InternalErrorException("")));
		Date created2 = search2.getCreated();

		assertEquals(created2.getTime(), created1.getTime());

		assertEquals(uuid1, uuid2);
	}

	/**
	 * See #316
	 */
	@Test
	public void testSearchThenTagThenSearch() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system2").setValue("testSearchTokenParam002");
		patient.addName().setFamily("Tester").addGiven("testSearchTokenParam2");
		myClient.create().resource(patient).execute();

		//@formatter:off
		Bundle response = myClient
			.search()
			.forResource(Patient.class)
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		patient = (Patient) response.getEntry().get(0).getResource();

		//@formatter:off
		myClient
			.meta()
			.add()
			.onResource(patient.getIdElement())
			.meta(new Meta().addTag("http://system", "tag1", "display"))
			.execute();
		//@formatter:on

		//@formatter:off
		response = myClient
			.search()
			.forResource(Patient.class)
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		patient = (Patient) response.getEntry().get(0).getResource();
		assertThat(patient.getMeta().getTag()).hasSize(1);
	}

	@Test
	public void testSearchTokenParamNoValue() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchTokenParam001");
		patient.addName().setFamily("Tester").addGiven("testSearchTokenParam1");
		patient.addCommunication().getLanguage().setText("testSearchTokenParamComText").addCoding().setCode("testSearchTokenParamCode").setSystem("testSearchTokenParamSystem")
			.setDisplay("testSearchTokenParamDisplay");
		myPatientDao.create(patient, mySrd);

		patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchTokenParam002");
		patient.addName().setFamily("Tester").addGiven("testSearchTokenParam2");
		myPatientDao.create(patient, mySrd);

		patient = new Patient();
		patient.addIdentifier().setSystem("urn:system2").setValue("testSearchTokenParam002");
		patient.addName().setFamily("Tester").addGiven("testSearchTokenParam2");
		myPatientDao.create(patient, mySrd);

		//@formatter:off
		Bundle response = myClient
			.search()
			.forResource(Patient.class)
			.where(Patient.IDENTIFIER.hasSystemWithAnyCode("urn:system"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertThat(response.getEntry()).hasSize(2);
	}

	@Test
	public void testSearchWithEmptyParameter() throws Exception {
		Observation obs = new Observation();
		obs.setStatus(ObservationStatus.FINAL);
		obs.getCode().addCoding().setSystem("foo").setCode("bar");
		myClient.create().resource(obs).execute();

		testSearchWithEmptyParameter("/Observation?value-quantity=");
		testSearchWithEmptyParameter("/Observation?code=bar&value-quantity=");
		testSearchWithEmptyParameter("/Observation?value-date=");
		testSearchWithEmptyParameter("/Observation?code=bar&value-date=");
		testSearchWithEmptyParameter("/Observation?value-concept=");
		testSearchWithEmptyParameter("/Observation?code=bar&value-concept=");
	}

	private void testSearchWithEmptyParameter(String theUrl) throws IOException {
		HttpGet get = new HttpGet(myServerBase + theUrl);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			String respString = IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8);
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, respString);
			assertThat(bundle.getEntry()).hasSize(1);
		}
	}

	@Test
	public void testSearchWithInclude() {
		Organization org = new Organization();
		org.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSearchWithInclude01");
		IdType orgId = (IdType) myClient.create().resource(org).prettyPrint().encodedXml().execute().getId();

		Patient pat = new Patient();
		pat.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSearchWithInclude02");
		pat.getManagingOrganization().setReferenceElement(orgId.toUnqualifiedVersionless());
		myClient.create().resource(pat).prettyPrint().encodedXml().execute().getId();

		//@formatter:off
		Bundle found = myClient
			.search()
			.forResource(Patient.class)
			.where(Patient.IDENTIFIER.exactly().systemAndIdentifier("urn:system:rpdstu2", "testSearchWithInclude02"))
			.include(Patient.INCLUDE_ORGANIZATION)
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertThat(found.getEntry()).hasSize(2);
		assertEquals(Patient.class, found.getEntry().get(0).getResource().getClass());
		assertEquals(SearchEntryMode.MATCH, found.getEntry().get(0).getSearch().getMode());
		assertEquals(Organization.class, found.getEntry().get(1).getResource().getClass());
		assertEquals(SearchEntryMode.INCLUDE, found.getEntry().get(1).getSearch().getMode());
	}

	@Test
	public void testOffsetSearchWithInclude() {
		Organization org = new Organization();
		org.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSearchWithInclude01");
		IdType orgId = (IdType) myClient.create().resource(org).prettyPrint().encodedXml().execute().getId();

		Patient pat = new Patient();
		pat.addIdentifier().setSystem("urn:system:rpdstu2").setValue("testSearchWithInclude02");
		pat.getManagingOrganization().setReferenceElement(orgId.toUnqualifiedVersionless());
		myClient.create().resource(pat).prettyPrint().encodedXml().execute().getId();

		//@formatter:off
		Bundle found = myClient
			.search()
			.forResource(Patient.class)
			.where(Patient.IDENTIFIER.exactly().systemAndIdentifier("urn:system:rpdstu2", "testSearchWithInclude02"))
			.include(Patient.INCLUDE_ORGANIZATION)
			.offset(0)
			.count(1)
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertThat(found.getEntry()).hasSize(2);
		assertEquals(Patient.class, found.getEntry().get(0).getResource().getClass());
		assertEquals(SearchEntryMode.MATCH, found.getEntry().get(0).getSearch().getMode());
		assertEquals(Organization.class, found.getEntry().get(1).getResource().getClass());
		assertEquals(SearchEntryMode.INCLUDE, found.getEntry().get(1).getSearch().getMode());
	}

	@Test()
	public void testSearchWithInvalidNumberPrefix() {
		try {
			//@formatter:off
			myClient
				.search()
				.forResource(Encounter.class)
				.where(Encounter.LENGTH.withPrefix(ParamPrefixEnum.ENDS_BEFORE).number(100))
				.prettyPrint()
				.returnBundle(Bundle.class)
				.execute();
			//@formatter:on
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("Unable to handle number prefix \"eb\" for value: eb100");
		}
	}

	@Test()
	public void testSearchWithInvalidQuantityPrefix() {
		Observation o = new Observation();
		o.getCode().setText("testSearchWithInvalidSort");
		myObservationDao.create(o, mySrd);
		try {
			//@formatter:off
			myClient
				.search()
				.forResource(Observation.class)
				.where(Observation.VALUE_QUANTITY.withPrefix(ParamPrefixEnum.ENDS_BEFORE).number(100).andNoUnits())
				.prettyPrint()
				.returnBundle(Bundle.class)
				.execute();
			//@formatter:on
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("Unable to handle quantity prefix \"eb\" for value: eb100||");
		}
	}

	@Test()
	public void testSearchNegativeNumbers() throws Exception {
		Observation o = new Observation();
		o.setValue(new Quantity().setValue(new BigDecimal("-10")));
		String oid1 = myObservationDao.create(o, mySrd).getId().toUnqualifiedVersionless().getValue();

		Observation o2 = new Observation();
		o2.setValue(new Quantity().setValue(new BigDecimal("-20")));
		String oid2 = myObservationDao.create(o2, mySrd).getId().toUnqualifiedVersionless().getValue();

		HttpGet get = new HttpGet(myServerBase + "/Observation?value-quantity=gt-15");
		CloseableHttpResponse resp = ourHttpClient.execute(get);
		try {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8));

			List<String> ids = toUnqualifiedVersionlessIdValues(bundle);
			assertThat(ids).containsExactly(oid1);
			assertThat(ids).doesNotContain(oid2);
		} finally {
			IOUtils.closeQuietly(resp);
		}

	}

	@Test
	public void testSearchWithCompositeSort_CodeValueDate() throws IOException {
		
		IIdType pid0;
		IIdType oid1;
		IIdType oid2;
		IIdType oid3;
		IIdType oid4;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			obs.getCode().addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new DateTimeType("2020-02-01"));
			
			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
			
			ourLog.debug("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			obs.getCode().addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new DateTimeType("2020-04-01"));
			
			oid2 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
			
			ourLog.debug("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			obs.getCode().addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new DateTimeType("2020-01-01"));
			
			oid3 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
			
			ourLog.debug("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			obs.getCode().addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new DateTimeType("2020-03-01"));
			
			oid4 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
			
			ourLog.debug("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		
		String uri = myServerBase + "/Observation?_sort=code-value-date";
		Bundle found;
		
		HttpGet get = new HttpGet(uri);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			String output = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			found = myFhirContext.newXmlParser().parseResource(Bundle.class, output);
		}
		
		ourLog.debug("Bundle: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(found));
		
		List<IIdType> list = toUnqualifiedVersionlessIds(found);
		assertThat(found.getEntry()).hasSize(4);
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
		IIdType deletedIdMissingTrue = myClient.create().resource(org).execute().getId().toUnqualifiedVersionless();
		myClient.delete().resourceById(deletedIdMissingTrue).execute();

		org = new Organization();
		org.setName("Help I'm a Bug");
		IIdType deletedIdMissingFalse = myClient.create().resource(org).execute().getId().toUnqualifiedVersionless();
		myClient.delete().resourceById(deletedIdMissingFalse).execute();

		List<IBaseResource> resources = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			org = new Organization();
			org.setName(methodName + "_0" + i);
			resources.add(org);
		}
		myClient.transaction().withResources(resources).prettyPrint().encodedXml().execute();

		org = new Organization();
		org.addIdentifier().setSystem("urn:system:rpdstu2").setValue(methodName + "01");
		org.setName(methodName + "name");
		IIdType orgNotMissing = myClient.create().resource(org).prettyPrint().encodedXml().execute().getId().toUnqualifiedVersionless();

		org = new Organization();
		org.addIdentifier().setSystem("urn:system:rpdstu2").setValue(methodName + "01");
		IIdType orgMissing = myClient.create().resource(org).prettyPrint().encodedXml().execute().getId().toUnqualifiedVersionless();

		{
			//@formatter:off
			Bundle found = myClient
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
			assertThat(list).as("Wanted " + orgNotMissing + " but got " + list.size() + ": " + list).contains(orgNotMissing);
			assertThat(list).doesNotContain(deletedIdMissingFalse);
			assertThat(list).doesNotContain(orgMissing);
		}

		//@formatter:off
		Bundle found = myClient
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
		assertThat(list).doesNotContain(orgNotMissing);
		assertThat(list).doesNotContain(deletedIdMissingTrue);
		assertThat(list).as("Wanted " + orgMissing + " but found: " + list).contains(orgMissing);
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
	public void testSearchWithMissingDate2() throws Exception {
		MedicationRequest mr1 = new MedicationRequest();
		mr1.getCategory().addCoding().setSystem("urn:medicationroute").setCode("oral");
		mr1.addDosageInstruction().getTiming().addEventElement().setValueAsString("2017-01-01");
		IIdType id1 = myMedicationRequestDao.create(mr1).getId().toUnqualifiedVersionless();

		MedicationRequest mr2 = new MedicationRequest();
		mr2.getCategory().addCoding().setSystem("urn:medicationroute").setCode("oral");
		IIdType id2 = myMedicationRequestDao.create(mr2).getId().toUnqualifiedVersionless();

		HttpGet get = new HttpGet(myServerBase + "/MedicationRequest?date:missing=false");
		CloseableHttpResponse resp = ourHttpClient.execute(get);
		try {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8));

			List<String> ids = toUnqualifiedVersionlessIdValues(bundle);
			assertThat(ids).containsExactly(id1.getValue());
			assertThat(ids).doesNotContain(id2.getValue());
		} finally {
			IOUtils.closeQuietly(resp);
		}

	}

	/**
	 * See #411
	 * <p>
	 * Let's see if we can reproduce this issue in JPA
	 */
	@Test
	public void testSearchWithMixedParams() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("0");
		patient.addName().setFamily("testSearchWithMixedParams").addGiven("Joe");
		myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		HttpPost httpPost = new HttpPost(myServerBase + "/Patient/_search?_format=application/xml");
		httpPost.addHeader("Cache-Control", "no-cache");
		List<NameValuePair> parameters = Lists.newArrayList();
		parameters.add(new BasicNameValuePair("name", "Smith"));
		httpPost.setEntity(new UrlEncodedFormEntity(parameters));

		ourLog.info("Outgoing post: {}", httpPost);

		CloseableHttpResponse status = ourHttpClient.execute(httpPost);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	@Test
	public void testSearchWithTextInexactMatch() throws Exception {
		Observation obs = new Observation();
		obs.getCode().setText("THIS_IS_THE_TEXT");
		obs.getCode().addCoding().setSystem("SYSTEM").setCode("CODE").setDisplay("THIS_IS_THE_DISPLAY");
		myClient.create().resource(obs).execute();

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
		myClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Daniel").setFamily("Adams");
		myClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Aaron").setFamily("Alexis");
		myClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Carol").setFamily("Allen");
		myClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Ruth").setFamily("Black");
		myClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Brian").setFamily("Brooks");
		myClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Susan").setFamily("Clark");
		myClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Amy").setFamily("Clark");
		myClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Anthony").setFamily("Coleman");
		myClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Steven").setFamily("Coleman");
		myClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Lisa").setFamily("Coleman");
		myClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Ruth").setFamily("Cook");
		myClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Betty").setFamily("Davis");
		myClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Joshua").setFamily("Diaz");
		myClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Brian").setFamily("Gracia");
		myClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Stephan").setFamily("Graham");
		myClient.create().resource(p).execute();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addGiven("Sarah").setFamily("Graham");
		myClient.create().resource(p).execute();

		Bundle resp = myClient
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

		assertThat(names).containsExactly("Daniel Adams", "Aaron Alexis", "Carol Allen", "Ruth Black", "Brian Brooks", "Amy Clark", "Susan Clark", "Anthony Coleman", "Lisa Coleman", "Steven Coleman", "Ruth Cook", "Betty Davis", "Joshua Diaz", "Brian Gracia", "Sarah Graham", "Stephan Graham");
	}

	/**
	 * Test for issue #60
	 */
	@Test
	public void testStoreUtf8Characters() {
		Organization org = new Organization();
		org.setName("測試醫院");
		org.addIdentifier().setSystem("urn:system").setValue("testStoreUtf8Characters_01");
		IdType orgId = (IdType) myClient.create().resource(org).prettyPrint().encodedXml().execute().getId();

		// Read back directly from the DAO
		{
			Organization returned = myOrganizationDao.read(orgId, mySrd);
			String val = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(returned);
			ourLog.info(val);
			assertThat(val).contains("<name value=\"測試醫院\"/>");
		}
		// Read back through the HTTP API
		{
			Organization returned = myClient.read(Organization.class, orgId.getIdPart());
			String val = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(returned);
			ourLog.info(val);
			assertThat(val).contains("<name value=\"測試醫院\"/>");
		}
	}

	@Test
	public void testTransaction() throws Exception {
		String contents = ClasspathUtil.loadResource("/update.xml");
		HttpPost post = new HttpPost(myServerBase);
		post.setEntity(new StringEntity(contents, ContentType.create("application/xml+fhir", "UTF-8")));
		CloseableHttpResponse resp = ourHttpClient.execute(post);
		try {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			String output = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(output);
		} finally {
			resp.close();
		}
	}

	@Test
	public void testTryToCreateResourceWithReferenceThatDoesntExist() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testTryToCreateResourceWithReferenceThatDoesntExist01");
		p1.addName().setFamily("testTryToCreateResourceWithReferenceThatDoesntExistFamily01").addGiven("testTryToCreateResourceWithReferenceThatDoesntExistGiven01");
		p1.setManagingOrganization(new Reference("Organization/99999999999"));

		try {
			myClient.create().resource(p1).execute().getId();
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("Organization/99999999999");
		}

	}

	@Test
	public void testUpdateInvalidReference() throws Exception {
		String methodName = "testUpdateInvalidReference";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPut post = new HttpPut(myServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertEquals(400, response.getStatusLine().getStatusCode());
			OperationOutcome oo = myFhirContext.newXmlParser().parseResource(OperationOutcome.class, responseString);
			assertThat(oo.getIssue().get(0).getDiagnostics()).contains("Can not update resource, request URL must contain an ID element for update (PUT) operation (it must be of the form [base]/[resource type]/[id])");
		} finally {
			response.close();
		}
	}

	@Test
	public void testUpdateInvalidReference2() throws Exception {
		String methodName = "testUpdateInvalidReference2";

		Patient pt = new Patient();
		pt.setId("2");
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPut post = new HttpPut(myServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertThat(responseString).contains("Can not update resource, request URL must contain an ID element for update (PUT) operation (it must be of the form [base]/[resource type]/[id])");
			assertThat(responseString).contains("<OperationOutcome");
			assertEquals(400, response.getStatusLine().getStatusCode());
		} finally {
			response.close();
		}
	}

	/**
	 * This does not currently cause an error, so this test is disabled
	 */
	@Test
	@Disabled
	public void testUpdateNoIdInBody() throws Exception {
		String methodName = "testUpdateNoIdInBody";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPut post = new HttpPut(myServerBase + "/Patient/FOO");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertThat(responseString).contains("Can not update resource, request URL must contain an ID element for update (PUT) operation (it must be of the form [base]/[resource type]/[id])");
			assertThat(responseString).contains("<OperationOutcome");
			assertEquals(400, response.getStatusLine().getStatusCode());
		} finally {
			response.close();
		}
	}

	@Test
	public void testUpdateRejectsIncorrectIds() throws Exception {

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateRejectsInvalidTypes");
		p1.addName().setFamily("Tester").addGiven("testUpdateRejectsInvalidTypes");
		IdType p1id = (IdType) myClient.create().resource(p1).execute().getId();

		// Try to update with the wrong ID in the resource body
		p1.setId("FOO");
		p1.addAddress().addLine("NEWLINE");

		String encoded = myFhirContext.newJsonParser().encodeResourceToString(p1);
		ourLog.info(encoded);

		HttpPut put = new HttpPut(myServerBase + "/Patient/" + p1id.getIdPart());
		put.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));
		put.addHeader("Accept", Constants.CT_FHIR_JSON);
		CloseableHttpResponse response = ourHttpClient.execute(put);
		try {
			assertEquals(400, response.getStatusLine().getStatusCode());
			OperationOutcome oo = myFhirContext.newJsonParser().parseResource(OperationOutcome.class, new InputStreamReader(response.getEntity().getContent()));
			assertThat(oo.getIssue().get(0).getDiagnostics()).isEqualTo(Msg.code(420) + "Can not update resource, resource body must contain an ID element which matches the request URL for update (PUT) operation - Resource body ID of \"FOO\" does not match URL ID of \""
				+ p1id.getIdPart() + "\"");
		} finally {
			response.close();
		}

		// Try to update with the no ID in the resource body
		p1.setId((String) null);

		encoded = myFhirContext.newJsonParser().encodeResourceToString(p1);
		put = new HttpPut(myServerBase + "/Patient/" + p1id.getIdPart());
		put.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));
		put.addHeader("Accept", Constants.CT_FHIR_JSON);
		response = ourHttpClient.execute(put);
		try {
			assertEquals(400, response.getStatusLine().getStatusCode());
			OperationOutcome oo = myFhirContext.newJsonParser().parseResource(OperationOutcome.class, new InputStreamReader(response.getEntity().getContent()));
			assertEquals(Msg.code(419) + "Can not update resource, resource body must contain an ID element for update (PUT) operation", oo.getIssue().get(0).getDiagnostics());
		} finally {
			response.close();
		}

		// Try to update with the to correct ID in the resource body
		p1.setId(p1id.getIdPart());

		encoded = myFhirContext.newJsonParser().encodeResourceToString(p1);
		put = new HttpPut(myServerBase + "/Patient/" + p1id.getIdPart());
		put.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));
		response = ourHttpClient.execute(put);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
		} finally {
			response.close();
		}

	}

	@Test
	public void testUpdateRejectsInvalidTypes() {

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateRejectsInvalidTypes");
		p1.addName().setFamily("Tester").addGiven("testUpdateRejectsInvalidTypes");
		IdType p1id = (IdType) myClient.create().resource(p1).execute().getId();

		Organization p2 = new Organization();
		p2.setId(p1id.getIdPart());
		p2.getNameElement().setValue("testUpdateRejectsInvalidTypes");
		try {
			myClient.update().resource(p2).withId("Organization/" + p1id.getIdPart()).execute();
			fail("");
		} catch (InvalidRequestException e) {
			// good
		}

		try {
			myClient.update().resource(p2).withId("Patient/" + p1id.getIdPart()).execute();
			fail("");
		} catch (InvalidRequestException e) {
			// good
		}

	}

	@Test
	public void testUpdateResourceConditional() throws IOException {
		String methodName = "testUpdateResourceConditional";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(myServerBase + "/Patient?name=" + methodName);
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		IdType id;
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString).startsWith(myServerBase + "/Patient/");
			id = new IdType(newIdString);
		} finally {
			response.close();
		}

		pt.addAddress().addLine("AAAAAAAAAAAAAAAAAAA");
		resource = myFhirContext.newXmlParser().encodeResourceToString(pt);
		HttpPut put = new HttpPut(myServerBase + "/Patient?name=" + methodName);
		put.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		response = ourHttpClient.execute(put);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			IdType newId = new IdType(response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC).getValue());
			assertEquals(id.toVersionless(), newId.toVersionless()); // version shouldn't match for conditional update
			assertThat(newId).isNotEqualTo(id);
		} finally {
			response.close();
		}

	}

	@Test
	public void testUpdateResourceConditionalComplex() throws IOException {
		Patient pt = new Patient();
		pt.addIdentifier().setSystem("http://general-hospital.co.uk/Identifiers").setValue("09832345234543876876");
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(myServerBase + "/Patient");
		post.addHeader(Constants.HEADER_IF_NONE_EXIST, "Patient?identifier=http://general-hospital.co.uk/Identifiers|09832345234543876876");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		IdType id;
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString).startsWith(myServerBase + "/Patient/");
			id = new IdType(newIdString);
		} finally {
			response.close();
		}

		pt.addName().setFamily("FOO");
		resource = myFhirContext.newXmlParser().encodeResourceToString(pt);
		HttpPut put = new HttpPut(myServerBase + "/Patient?identifier=" + ("http://general-hospital.co.uk/Identifiers|09832345234543876876".replace("|", UrlUtil.escapeUrlParam("|"))));
		put.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		IdType id2;
		response = ourHttpClient.execute(put);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC).getValue();
			assertThat(newIdString).startsWith(myServerBase + "/Patient/");
			id2 = new IdType(newIdString);
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
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(myServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		IdType id;
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString).startsWith(myServerBase + "/Patient/");
			id = new IdType(newIdString);
		} finally {
			response.close();
		}

		Date before = new Date();
		Thread.sleep(100);

		pt = new Patient();
		pt.setId(id.getIdPart());
		resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPut put = new HttpPut(myServerBase + "/Patient/" + id.getIdPart());
		put.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_REPRESENTATION);
		put.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		response = ourHttpClient.execute(put);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			IOUtils.closeQuietly(response.getEntity().getContent());

			Patient respPt = myFhirContext.newXmlParser().parseResource(Patient.class, responseString);
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

		MethodOutcome outcome = myClient.update().resource(p1).withId("testUpdateWithClientSuppliedIdWhichDoesntExistRpDstu2").execute();
		assertEquals(true, outcome.getCreated().booleanValue());
		IdType p1Id = (IdType) outcome.getId();

		assertThat(p1Id.getValue()).contains("Patient/testUpdateWithClientSuppliedIdWhichDoesntExistRpDstu2/_history");

		//@formatter:off
		Bundle actual = myClient
			.search()
			.forResource(Patient.class)
			.where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system", "testUpdateWithClientSuppliedIdWhichDoesntExistRpDstu2"))
			.encodedJson()
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertThat(actual.getEntry()).hasSize(1);
		assertEquals(p1Id.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());

	}

	@Test
	public void testUpdateWithSource() {
		Patient patient = new Patient();
		patient.setActive(false);
		IIdType patientid = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();

		{
			Patient readPatient = (Patient) myClient.read().resource("Patient").withId(patientid).execute();
			assertThat(readPatient.getMeta().getExtensionString(HapiExtensions.EXT_META_SOURCE)).matches("#[a-zA-Z0-9]+");
		}

		patient.setId(patientid);
		patient.setActive(true);
		myClient.update().resource(patient).execute();
		{
			Patient readPatient = (Patient) myClient.read().resource("Patient").withId(patientid).execute();
			assertThat(readPatient.getMeta().getExtensionString(HapiExtensions.EXT_META_SOURCE)).matches("#[a-zA-Z0-9]+");

			readPatient.addName().setFamily("testUpdateWithSource");
			myClient.update().resource(readPatient).execute();
			readPatient = (Patient) myClient.read().resource("Patient").withId(patientid).execute();
			assertThat(readPatient.getMeta().getExtensionString(HapiExtensions.EXT_META_SOURCE)).matches("#[a-zA-Z0-9]+");
		}
	}

	@Test
	public void testUpdateWithETag() throws Exception {
		String methodName = "testUpdateWithETag";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		IIdType id = myClient.create().resource(pt).execute().getId().toUnqualifiedVersionless();

		pt.addName().setFamily("FAM2");
		pt.setId(id.toUnqualifiedVersionless());
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPut put = new HttpPut(myServerBase + "/Patient/" + id.getIdPart());
		put.addHeader(Constants.HEADER_IF_MATCH, "W/\"44\"");
		put.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(put);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertEquals(409, response.getStatusLine().getStatusCode());
			OperationOutcome oo = myFhirContext.newXmlParser().parseResource(OperationOutcome.class, responseString);
			assertThat(oo.getIssue().get(0).getDiagnostics()).contains("Trying to update Patient/" + id.getIdPart() + "/_history/44 but this is not the current version");
		} finally {
			response.close();
		}

		// Now a good one
		put = new HttpPut(myServerBase + "/Patient/" + id.getIdPart());
		put.addHeader(Constants.HEADER_IF_MATCH, "W/\"1\"");
		put.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		response = ourHttpClient.execute(put);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertEquals(200, response.getStatusLine().getStatusCode());
		} finally {
			response.close();
		}

	}

	@Test
	public void testUpdateWrongIdInBody() throws Exception {
		String methodName = "testUpdateWrongIdInBody";

		Patient pt = new Patient();
		pt.setId("333");
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPut post = new HttpPut(myServerBase + "/Patient/A2");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertEquals(400, response.getStatusLine().getStatusCode());
			OperationOutcome oo = myFhirContext.newXmlParser().parseResource(OperationOutcome.class, responseString);
			assertEquals(Msg.code(420) + "Can not update resource, resource body must contain an ID element which matches the request URL for update (PUT) operation - Resource body ID of \"333\" does not match URL ID of \"A2\"", oo.getIssue().get(0).getDiagnostics());
		} finally {
			response.close();
		}
	}

	@Test
	public void testUpdateResourceAfterReadOperationAndNoChangesShouldNotChangeVersion(){
		// Create Patient
		Patient patient = new Patient();
		patient = (Patient) myClient.create().resource(patient).execute().getResource();
		assertEquals(1, patient.getIdElement().getVersionIdPartAsLong());

		// Read Patient
		patient = (Patient) myClient.read().resource("Patient").withId(patient.getIdElement()).execute();
		assertEquals(1, patient.getIdElement().getVersionIdPartAsLong());

		// Update Patient with no changes
		patient = (Patient) myClient.update().resource(patient).execute().getResource();
		assertEquals(1, patient.getIdElement().getVersionIdPartAsLong());
	}

	@Test
	public void testValidateBadInputViaGet() throws IOException {

		HttpGet get = new HttpGet(myServerBase + "/Patient/$validate?mode=create");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertThat(resp).contains("No resource supplied for $validate operation (resource is required unless mode is &quot;delete&quot;)");
			assertEquals(400, response.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(response.getEntity().getContent());
			response.close();
		}
	}

	@Test
	public void testValidateBadInputViaPost() throws IOException {

		Parameters input = new Parameters();
		input.addParameter().setName("mode").setValue(new CodeType("create"));

		String inputStr = myFhirContext.newXmlParser().encodeResourceToString(input);
		ourLog.info(inputStr);

		HttpPost post = new HttpPost(myServerBase + "/Patient/$validate");
		post.setEntity(new StringEntity(inputStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertThat(resp).contains("No resource supplied for $validate operation (resource is required unless mode is &quot;delete&quot;)");
			assertEquals(400, response.getStatusLine().getStatusCode());
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

		String inputStr = myFhirContext.newXmlParser().encodeResourceToString(patient);
		HttpPost post = new HttpPost(myServerBase + "/Patient/$validate");
		post.setEntity(new StringEntity(inputStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp).doesNotContain("Resource has no id");
		}
	}

	// Y
	@Test
	public void testValidateResourceHuge() throws IOException {

		Patient patient = new Patient();
		patient.addName().addGiven("James" + StringUtils.leftPad("James", 1000000, 'A'));
		patient.setBirthDateElement(new DateType("2011-02-02"));

		Parameters input = new Parameters();
		input.addParameter().setName("resource").setResource(patient);

		String inputStr = myFhirContext.newXmlParser().encodeResourceToString(input);
		ourLog.debug(inputStr);

		HttpPost post = new HttpPost(myServerBase + "/Patient/$validate");
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
	public void testValidateResourceInstanceOnServer() throws IOException {

		Patient patient = new Patient();
		patient.addName().addGiven("James");
		patient.setBirthDateElement(new DateType("2011-02-02"));
		patient.addContact().setGender(AdministrativeGender.MALE);
		patient.addCommunication().setPreferred(true); // missing language

		IIdType id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		HttpGet get = new HttpGet(myServerBase + "/Patient/" + id.getIdPart() + "/$validate");
		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp).contains("SHALL at least contain a contact's details or a reference to an organization");
		}
	}

	@Test
	public void testValidateResourceWithId() throws IOException {

		Patient patient = new Patient();
		patient.setId("A123");
		patient.addName().addGiven("James");
		patient.setBirthDateElement(new DateType("2011-02-02"));
		myPatientDao.update(patient, mySrd);

		Parameters input = new Parameters();
		input.addParameter().setName("resource").setResource(patient);

		String inputStr = myFhirContext.newXmlParser().encodeResourceToString(input);
		ourLog.info(inputStr);

		HttpPost post = new HttpPost(myServerBase + "/Patient/A123/$validate");
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

	// Y
	@Test
	public void testValidateResourceWithNoIdParameters() throws IOException {

		Patient patient = new Patient();
		patient.addName().addGiven("James");
		patient.setBirthDateElement(new DateType("2011-02-02"));

		Parameters input = new Parameters();
		input.addParameter().setName("resource").setResource(patient);

		String inputStr = myFhirContext.newXmlParser().encodeResourceToString(input);
		HttpPost post = new HttpPost(myServerBase + "/Patient/$validate?_pretty=true");
		post.setEntity(new StringEntity(inputStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp).doesNotContain("Resource has no id");
			assertThat(resp).contains("<td>No issues detected during validation</td>");
			assertThat(resp).contains("<issue>", "<severity value=\"information\"/>", "<code value=\"informational\"/>", "<diagnostics value=\"No issues detected during validation\"/>",
				"</issue>");
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

		String inputStr = myFhirContext.newXmlParser().encodeResourceToString(patient);
		HttpPost post = new HttpPost(myServerBase + "/Patient/$validate");
		post.setEntity(new StringEntity(inputStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp).doesNotContain("Resource has no id");
			assertThat(resp).contains("<td>No issues detected during validation</td>");
			assertThat(resp).contains("<issue>", "<severity value=\"information\"/>", "<code value=\"informational\"/>", "<diagnostics value=\"No issues detected during validation\"/>",
				"</issue>");
		} finally {
			IOUtils.closeQuietly(response.getEntity().getContent());
			response.close();
		}
	}

	@Test
	public void testValueSetExpandOperation() throws IOException {
		CodeSystem cs = myFhirContext.newXmlParser().parseResource(CodeSystem.class, new InputStreamReader(ResourceProviderDstu3Test.class.getResourceAsStream("/extensional-case-3-cs.xml")));
		myClient.create().resource(cs).execute();

		ValueSet upload = myFhirContext.newXmlParser().parseResource(ValueSet.class, new InputStreamReader(ResourceProviderDstu3Test.class.getResourceAsStream("/extensional-case-3-vs.xml")));
		IIdType vsid = myClient.create().resource(upload).execute().getId().toUnqualifiedVersionless();

		HttpGet get = new HttpGet(myServerBase + "/ValueSet/" + vsid.getIdPart() + "/$expand");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp).contains("<ValueSet xmlns=\"http://hl7.org/fhir\">");
			assertThat(resp).contains("<expansion>");
			assertThat(resp).contains("<contains>");
			assertThat(resp).contains("<system value=\"http://acme.org\"/>");
			assertThat(resp).contains("<code value=\"8450-9\"/>");
			assertThat(resp).contains("<display value=\"Systolic blood pressure--expiration\"/>");
			assertThat(resp).contains("</contains>");
			assertThat(resp).contains("<contains>");
			assertThat(resp).contains("<system value=\"http://acme.org\"/>");
			assertThat(resp).contains("<code value=\"11378-7\"/>");
			assertThat(resp).contains("<display value=\"Systolic blood pressure at First encounter\"/>");
			assertThat(resp).contains("</contains>");
			assertThat(resp).contains("</expansion>");
		} finally {
			IOUtils.closeQuietly(response.getEntity().getContent());
			response.close();
		}

		/*
		 * Filter with display name
		 */

		get = new HttpGet(myServerBase + "/ValueSet/" + vsid.getIdPart() + "/$expand?filter=systolic");
		response = ourHttpClient.execute(get);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			//@formatter:off
			assertThat(resp).contains(
				"<code value=\"11378-7\"/>",
				"<display value=\"Systolic blood pressure at First encounter\"/>");
			//@formatter:on
		} finally {
			IOUtils.closeQuietly(response.getEntity().getContent());
			response.close();
		}

	}

	@Test
	public void testDocumentReferenceWith500CharAttachmentUrl() throws IOException {
		final DocumentReference.ReferredDocumentStatus docStatus = DocumentReference.ReferredDocumentStatus.FINAL;
		final String longUrl = StringUtils.repeat("a", 500);

		DocumentReference submittedDocumentReference = new DocumentReference();
		submittedDocumentReference.setDocStatus(docStatus);

		Attachment attachment = new Attachment();
		attachment.setUrl(longUrl);
		submittedDocumentReference.getContentFirstRep().setAttachment(attachment);

		String json = myFhirContext.newJsonParser().encodeResourceToString(submittedDocumentReference);
		HttpPost post = new HttpPost(myServerBase + "/DocumentReference");
		post.setEntity(new StringEntity(json, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(HttpStatus.CREATED.value(), response.getStatusLine().getStatusCode());

			DocumentReference createdDocumentReferenced = myFhirContext.newJsonParser().parseResource(DocumentReference.class, resp);
			assertEquals(docStatus, createdDocumentReferenced.getDocStatus());
			assertEquals(longUrl, createdDocumentReferenced.getContentFirstRep().getAttachment().getUrl());
		}
	}

	private String toStr(Date theDate) {
		return new InstantDt(theDate).getValueAsString();
	}

	private IIdType createPatientWithMeta(Meta theMeta){

		Patient patient = new Patient();
		patient.setMeta(theMeta);
		return myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();
	}

}
