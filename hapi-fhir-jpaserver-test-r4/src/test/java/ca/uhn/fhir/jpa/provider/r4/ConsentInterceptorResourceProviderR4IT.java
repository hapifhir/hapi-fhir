package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentInterceptor;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentOperationStatusEnum;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentOutcome;
import ca.uhn.fhir.rest.server.interceptor.consent.DelegatingConsentService;
import ca.uhn.fhir.rest.server.interceptor.consent.IConsentContextServices;
import ca.uhn.fhir.rest.server.interceptor.consent.IConsentService;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.hapi.converters.server.VersionedApiConverterInterceptor;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConsentInterceptorResourceProviderR4IT extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ConsentInterceptorResourceProviderR4IT.class);
	private List<String> myObservationIds;
	private List<String> myPatientIds;
	private List<String> myObservationIdsOddOnly;
	private List<String> myObservationIdsEvenOnly;
	private List<String> myObservationIdsEvenOnlyBackwards;
	private ConsentInterceptor myConsentInterceptor;
	@Autowired
	@Qualifier(JpaConfig.GRAPHQL_PROVIDER_NAME)
	private Object myGraphQlProvider;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		Validate.notNull(myConsentInterceptor);
		myStorageSettings.setSearchPreFetchThresholds(new JpaStorageSettings().getSearchPreFetchThresholds());
		myServer.getRestfulServer().getInterceptorService().unregisterInterceptor(myConsentInterceptor);
		myServer.getRestfulServer().unregisterProvider(myGraphQlProvider);
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myStorageSettings.setSearchPreFetchThresholds(Arrays.asList(20, 50, 190));
		myServer.getRestfulServer().registerProvider(myGraphQlProvider);
	}

	@Test
	public void testConsentServiceWhichReadsDoesNotThrowNpe() {
		myStorageSettings.setAllowAutoInflateBinaries(true);
		IConsentService consentService = new ReadingBackResourcesConsentSvc(myDaoRegistry);
		myConsentInterceptor = new ConsentInterceptor(consentService, IConsentContextServices.NULL_IMPL);
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(myConsentInterceptor);
		myInterceptorRegistry.registerInterceptor(myBinaryStorageInterceptor);

		BundleBuilder builder = new BundleBuilder(myFhirContext);
		for (int i = 0; i <10 ;i++) {
			Observation o = new Observation();
			o.setId("obs-" + i);
			builder.addTransactionUpdateEntry(o);
		}
		for (int i = 0; i <10 ;i++) {
			Observation o = new Observation();
			o.setIdentifier(Lists.newArrayList(new Identifier().setSystem("http://foo").setValue("bar")));
			builder.addTransactionCreateEntry(o);
		}

		Bundle execute = (Bundle) myClient.transaction().withBundle(builder.getBundle()).execute();
		assertThat(execute.getEntry()).hasSize(20);

		myInterceptorRegistry.unregisterInterceptor(myBinaryStorageInterceptor);
	}
	@Test
	public void testSearchAndBlockSomeWithReject() {
		create50Observations();

		IConsentService consentService = new ConsentSvcCantSeeOddNumbered();
		myConsentInterceptor = new ConsentInterceptor(consentService, IConsentContextServices.NULL_IMPL);
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(myConsentInterceptor);

		// Perform a search
		Bundle result = myClient
			.search()
			.forResource("Observation")
			.sort()
			.ascending(Observation.SP_IDENTIFIER)
			.returnBundle(Bundle.class)
			.count(15)
			.execute();
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirContext, result);
		List<String> returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		assertThat(returnedIdValues).hasSize(15);
		assertEquals(myObservationIdsEvenOnly.subList(0, 15), returnedIdValues);

		// Fetch the next page
		result = myClient
			.loadPage()
			.next(result)
			.execute();
		resources = BundleUtil.toListOfResources(myFhirContext, result);
		returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		assertThat(returnedIdValues).hasSize(10);
		assertEquals(myObservationIdsEvenOnly.subList(15, 25), returnedIdValues);
	}

	/**
	 * Make sure that the query cache doesn't get used at all if the consent
	 * service wants to inspect a request
	 */
	@Test
	public void testSearchAndBlockSome_DontReuseSearches() {
		create50Observations();

		CapturingInterceptor capture = new CapturingInterceptor();
		myClient.registerInterceptor(capture);

		DelegatingConsentService consentService = new DelegatingConsentService();
		myConsentInterceptor = new ConsentInterceptor(consentService, IConsentContextServices.NULL_IMPL);
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(myConsentInterceptor);

		// Perform a search and only allow even
		String context = "active consent - hide odd";
		consentService.setTarget(new ConsentSvcCantSeeOddNumbered());
		List<String> returnedIdValues = searchForObservations();
		assertEquals(myObservationIdsEvenOnly.subList(0, 15), returnedIdValues);
		assertResponseIsNotFromCache(context, capture.getLastResponse());

		// Perform a search and only allow odd
		context = "active consent - hide even";
		consentService.setTarget(new ConsentSvcCantSeeEvenNumbered());
		returnedIdValues = searchForObservations();
		assertEquals(myObservationIdsOddOnly.subList(0, 15), returnedIdValues);
		assertResponseIsNotFromCache(context, capture.getLastResponse());

		// Perform a search and allow all with a PROCEED
		context = "active consent - PROCEED on cache";
		consentService.setTarget(new ConsentSvcNop(ConsentOperationStatusEnum.PROCEED));
		returnedIdValues = searchForObservations();
		assertEquals(myObservationIds.subList(0, 15), returnedIdValues);
		assertResponseIsNotFromCache(context, capture.getLastResponse());

		// Perform a search and allow all with an AUTHORIZED (no further checking)
		context = "active consent - AUTHORIZED after a PROCEED";
		consentService.setTarget(new ConsentSvcNop(ConsentOperationStatusEnum.AUTHORIZED));
		returnedIdValues = searchForObservations();
		assertEquals(myObservationIds.subList(0, 15), returnedIdValues);

		// Perform a second search and allow all with an AUTHORIZED (no further checking)
		// which means we should finally get one from the cache
		context = "active consent - AUTHORIZED after AUTHORIZED";
		consentService.setTarget(new ConsentSvcNop(ConsentOperationStatusEnum.AUTHORIZED));
		returnedIdValues = searchForObservations();
		assertEquals(myObservationIds.subList(0, 15), returnedIdValues);
		assertResponseIsFromCache(context, capture.getLastResponse());

		// Perform another search, now with an active consent interceptor that promises not to use canSeeResource.
		// Should re-use cache result
		context = "active consent - canSeeResource disabled, after AUTHORIZED - should reuse cache";
		consentService.setTarget(new ConsentSvcNop(ConsentOperationStatusEnum.PROCEED, false));
		returnedIdValues = searchForObservations();
		assertEquals(myObservationIds.subList(0, 15), returnedIdValues);
		assertResponseIsFromCache(context, capture.getLastResponse());

		myClient.unregisterInterceptor(capture);
	}

	private static void assertResponseIsNotFromCache(String theContext, IHttpResponse lastResponse) {
		List<String> cacheOutcome= lastResponse.getHeaders(Constants.HEADER_X_CACHE);
		assertThat(cacheOutcome).as(theContext + " - No cache response headers").isEmpty();
	}

	private static void assertResponseIsFromCache(String theContext, IHttpResponse lastResponse) {
		List<String> cacheOutcome = lastResponse.getHeaders(Constants.HEADER_X_CACHE);
		assertThat(cacheOutcome)
			.as(theContext + " - Response came from cache")
			.anyMatch(item -> item.matches("^HIT from .*"));
	}

	private List<String> searchForObservations() {
		Bundle result;
		result = myClient
			.search()
			.forResource("Observation")
			.sort()
			.ascending(Observation.SP_IDENTIFIER)
			.returnBundle(Bundle.class)
			.count(15)
			.execute();
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirContext, result);
		return toUnqualifiedVersionlessIdValues(resources);
	}

	@Test
	public void testSearchMaskSubject() {
		create50Observations();

		ConsentSvcMaskObservationSubjects consentService = new ConsentSvcMaskObservationSubjects();
		myConsentInterceptor = new ConsentInterceptor(consentService, IConsentContextServices.NULL_IMPL);
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(myConsentInterceptor);

		// Perform a search
		Bundle result = myClient
			.search()
			.forResource("Observation")
			.sort()
			.ascending(Observation.SP_IDENTIFIER)
			.returnBundle(Bundle.class)
			.count(15)
			.execute();
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirContext, result);
		assertThat(resources).hasSize(15);
		assertEquals(16, consentService.getSeeCount());
		resources.forEach(t -> {
			assertEquals(null, ((Observation) t).getSubject().getReference());
		});

		// Fetch the next page
		result = myClient
			.loadPage()
			.next(result)
			.execute();
		resources = BundleUtil.toListOfResources(myFhirContext, result);
		assertThat(resources).hasSize(15);
		assertEquals(32, consentService.getSeeCount());
		resources.forEach(t -> {
			assertEquals(null, ((Observation) t).getSubject().getReference());
		});
	}

	@Test
	public void testConsentWorksWithVersionedApiConverterInterceptor() {
		myConsentInterceptor = new ConsentInterceptor(new IConsentService() {
		});
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(myConsentInterceptor);
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(new VersionedApiConverterInterceptor());

		myClient.create().resource(new Patient().setGender(Enumerations.AdministrativeGender.MALE).addName(new HumanName().setFamily("1"))).execute();
		Bundle response = myClient.search().forResource(Patient.class).count(1).accept("application/fhir+json; fhirVersion=3.0").returnBundle(Bundle.class).execute();

		assertThat(response.getEntry()).hasSize(1);
		assertNull(response.getTotalElement().getValue());

	}

	@Test
	public void testHistoryAndBlockSome() {
		create50Observations();

		IConsentService consentService = new ConsentSvcCantSeeOddNumbered();
		myConsentInterceptor = new ConsentInterceptor(consentService, IConsentContextServices.NULL_IMPL);
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(myConsentInterceptor);

		// Perform a search
		Bundle result = myClient
			.history()
			.onServer()
			.returnBundle(Bundle.class)
			.count(10)
			.execute();
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirContext, result);
		List<String> returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
		assertEquals(myObservationIdsEvenOnlyBackwards.subList(0, 5), returnedIdValues);

		// Per #2012
		assertNull(result.getTotalElement().getValue());
	}

	@Test
	public void testReadAndBlockSome() {
		create50Observations();

		IConsentService consentService = new ConsentSvcCantSeeOddNumbered();
		myConsentInterceptor = new ConsentInterceptor(consentService, IConsentContextServices.NULL_IMPL);
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(myConsentInterceptor);

		myClient.read().resource("Observation").withId(new IdType(myObservationIdsEvenOnly.get(0))).execute();
		myClient.read().resource("Observation").withId(new IdType(myObservationIdsEvenOnly.get(1))).execute();

		try {
			myClient.read().resource("Observation").withId(new IdType(myObservationIdsOddOnly.get(0))).execute();
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}
		try {
			myClient.read().resource("Observation").withId(new IdType(myObservationIdsOddOnly.get(1))).execute();
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

	}

	@Test
	public void testCreateBlockResponse() throws IOException {
		create50Observations();

		DelegatingConsentService consentService = new DelegatingConsentService();
		myConsentInterceptor = new ConsentInterceptor(consentService, IConsentContextServices.NULL_IMPL);
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(myConsentInterceptor);

		Patient patient = new Patient();
		patient.setActive(true);

		// Reject output
		consentService.setTarget(new ConsentSvcRejectCanSeeAnything());
		HttpPost post = new HttpPost(myServerBase + "/Patient");
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_REPRESENTATION);
		post.setEntity(toEntity(patient));
		try (CloseableHttpResponse status = ourHttpClient.execute(post)) {
			String id = status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue();
			assertThat(id).matches("^.*/Patient/[0-9]+/_history/[0-9]+$");
			assertEquals(201, status.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			assertThat(responseString).isBlank();
			assertNull(status.getEntity().getContentType());
		}

		// Accept output
		consentService.setTarget(new ConsentSvcNop(ConsentOperationStatusEnum.PROCEED));
		post = new HttpPost(myServerBase + "/Patient");
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_REPRESENTATION);
		post.setEntity(toEntity(patient));
		try (CloseableHttpResponse status = ourHttpClient.execute(post)) {
			String id = status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue();
			assertThat(id).matches("^.*/Patient/[0-9]+/_history/[0-9]+$");
			assertEquals(201, status.getStatusLine().getStatusCode());
			assertNotNull(status.getEntity());
			String responseString = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			assertThat(responseString).isNotBlank();
			assertThat(status.getEntity().getContentType().getValue().toLowerCase()).matches(".*json.*");
		}

	}

	@Test
	public void testUpdateBlockResponse() throws IOException {
		create50Observations();

		Patient patient = new Patient();
		patient.setActive(true);
		IIdType id = myClient.create().resource(patient).prefer(PreferReturnEnum.REPRESENTATION).execute().getId().toUnqualifiedVersionless();

		DelegatingConsentService consentService = new DelegatingConsentService();
		myConsentInterceptor = new ConsentInterceptor(consentService, IConsentContextServices.NULL_IMPL);
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(myConsentInterceptor);

		// Reject output
		consentService.setTarget(new ConsentSvcRejectCanSeeAnything());
		patient = new Patient();
		patient.setId(id);
		patient.setActive(true);
		patient.addIdentifier().setValue("VAL1");
		HttpPut put = new HttpPut(myServerBase + "/Patient/" + id.getIdPart());
		put.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_REPRESENTATION);
		put.setEntity(toEntity(patient));
		try (CloseableHttpResponse status = ourHttpClient.execute(put)) {
			String idVal = status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue();
			assertThat(idVal).matches("^.*/Patient/[0-9]+/_history/[0-9]+$");
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			assertThat(responseString).isBlank();
			assertNull(status.getEntity().getContentType());
		}

		// Accept output
		consentService.setTarget(new ConsentSvcNop(ConsentOperationStatusEnum.PROCEED));
		patient = new Patient();
		patient.setId(id);
		patient.setActive(true);
		patient.addIdentifier().setValue("VAL2");
		put = new HttpPut(myServerBase + "/Patient/" + id.getIdPart());
		put.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_REPRESENTATION);
		put.setEntity(toEntity(patient));
		try (CloseableHttpResponse status = ourHttpClient.execute(put)) {
			String idVal = status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue();
			assertThat(idVal).matches("^.*/Patient/[0-9]+/_history/[0-9]+$");
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertNotNull(status.getEntity());
			String responseString = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			assertThat(responseString).isNotBlank();
			assertThat(status.getEntity().getContentType().getValue().toLowerCase()).matches(".*json.*");
		}

	}

	@Test
	public void testRejectWillSeeResource() throws IOException {
		create50Observations();

		ConsentSvcRejectWillSeeEvenNumbered consentService = new ConsentSvcRejectWillSeeEvenNumbered();
		myConsentInterceptor = new ConsentInterceptor(consentService, IConsentContextServices.NULL_IMPL);
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(myConsentInterceptor);

		// Search for all
		String url = myServerBase + "/Observation?_pretty=true&_count=10";
		ourLog.info("HTTP GET {}", url);
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_ACCEPT, Constants.CT_JSON);
		try (CloseableHttpResponse status = ourHttpClient.execute(get)) {
			String responseString = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseString);
			assertEquals(200, status.getStatusLine().getStatusCode());

			Bundle result = myFhirContext.newJsonParser().parseResource(Bundle.class, responseString);
			List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirContext, result);
			List<String> returnedIdValues = toUnqualifiedVersionlessIdValues(resources);
			assertEquals(myObservationIdsOddOnly.subList(0, 5), returnedIdValues);
		}

	}

	@Test
	public void testGraphQL_Proceed() throws IOException {
		createPatientAndOrg();

		DelegatingConsentService consentService = new DelegatingConsentService();
		myConsentInterceptor = new ConsentInterceptor(consentService, IConsentContextServices.NULL_IMPL);
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(myConsentInterceptor);

		// Proceed everything
		consentService.setTarget(new ConsentSvcNop(ConsentOperationStatusEnum.PROCEED));
		String query = "{ name { family, given }, managingOrganization { reference, resource {name} } }";
		String url = myServerBase + "/" + myPatientIds.get(0) + "/$graphql?query=" + UrlUtil.escapeUrlParam(query);
		ourLog.info("HTTP GET {}", url);
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_ACCEPT, Constants.CT_JSON);
		try (CloseableHttpResponse status = ourHttpClient.execute(get)) {
			String responseString = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseString);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertThat(responseString).contains("\"family\":\"PATIENT_FAMILY\"");
			assertThat(responseString).contains("\"given\":[\"PATIENT_GIVEN1\",\"PATIENT_GIVEN2\"]");
			assertThat(responseString).contains("\"name\":\"ORG_NAME\"");
		}

	}

	@Test
	public void testGraphQL_RejectResource() throws IOException {
		createPatientAndOrg();

		DelegatingConsentService consentService = new DelegatingConsentService();
		myConsentInterceptor = new ConsentInterceptor(consentService, IConsentContextServices.NULL_IMPL);
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(myConsentInterceptor);

		IConsentService svc = mock(IConsentService.class);
		when(svc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(svc.shouldProcessCanSeeResource(any(), any())).thenReturn(true);
		when(svc.canSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.REJECT);

		consentService.setTarget(svc);
		String query = "{ name { family, given }, managingOrganization { reference, resource {name} } }";
		String url = myServerBase + "/" + myPatientIds.get(0) + "/$graphql?query=" + UrlUtil.escapeUrlParam(query);
		ourLog.info("HTTP GET {}", url);
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_ACCEPT, Constants.CT_JSON);
		try (CloseableHttpResponse status = ourHttpClient.execute(get)) {
			String responseString = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseString);
			assertEquals(404, status.getStatusLine().getStatusCode());
			assertThat(responseString).doesNotContain("\"family\":\"PATIENT_FAMILY\"");
			assertThat(responseString).doesNotContain("\"given\":[\"PATIENT_GIVEN1\",\"PATIENT_GIVEN2\"]");
			assertThat(responseString).doesNotContain("\"name\":\"ORG_NAME\"");

			OperationOutcome oo = myFhirContext.newJsonParser().parseResource(OperationOutcome.class, responseString);
			assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(Msg.code(1147) + "Unable to execute GraphQL Expression: HTTP 404 " + Msg.code(1995) + "Resource Patient/[0-9]+ is not known");
		}

	}

	@Test
	public void testGraphQL_RejectLinkedResource() throws IOException {
		createPatientAndOrg();

		DelegatingConsentService consentService = new DelegatingConsentService();
		myConsentInterceptor = new ConsentInterceptor(consentService, IConsentContextServices.NULL_IMPL);
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(myConsentInterceptor);

		IConsentService svc = mock(IConsentService.class);
		when(svc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(svc.shouldProcessCanSeeResource(any(), any())).thenReturn(true);
		when(svc.canSeeResource(any(RequestDetails.class), any(IBaseResource.class), any())).thenAnswer(t -> {
			IBaseResource resource = t.getArgument(1, IBaseResource.class);
			if (resource instanceof Organization) {
				return ConsentOutcome.REJECT;
			}
			return ConsentOutcome.PROCEED;
		});
		when(svc.willSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);

		consentService.setTarget(svc);
		String query = "{ name { family, given }, managingOrganization { reference, resource {name} } }";
		String url = myServerBase + "/" + myPatientIds.get(0) + "/$graphql?query=" + UrlUtil.escapeUrlParam(query);
		ourLog.info("HTTP GET {}", url);
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_ACCEPT, Constants.CT_JSON);
		try (CloseableHttpResponse status = ourHttpClient.execute(get)) {
			String responseString = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseString);
			assertEquals(404, status.getStatusLine().getStatusCode());
			assertThat(responseString).doesNotContain("\"family\":\"PATIENT_FAMILY\"");
			assertThat(responseString).doesNotContain("\"given\":[\"PATIENT_GIVEN1\",\"PATIENT_GIVEN2\"]");
			assertThat(responseString).doesNotContain("\"name\":\"ORG_NAME\"");

			OperationOutcome oo = myFhirContext.newJsonParser().parseResource(OperationOutcome.class, responseString);
			assertThat(oo.getIssueFirstRep().getDiagnostics()).matches(Msg.code(1147) + "Unable to execute GraphQL Expression: HTTP 404 " + Msg.code(1995) + "Resource Organization/[0-9]+ is not known");
		}

	}

	@Test
	public void testBundleTotalIsStripped() {
		myConsentInterceptor = new ConsentInterceptor(new ConsentSvcCantSeeFemales());
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(myConsentInterceptor);

		myClient.create().resource(new Patient().setGender(Enumerations.AdministrativeGender.MALE).addName(new HumanName().setFamily("1"))).execute();
		myClient.create().resource(new Patient().setGender(Enumerations.AdministrativeGender.MALE).addName(new HumanName().setFamily("2"))).execute();
		myClient.create().resource(new Patient().setGender(Enumerations.AdministrativeGender.FEMALE).addName(new HumanName().setFamily("3"))).execute();

		runInTransaction(() -> {
			assertEquals(3, myResourceTableDao.count());
		});

		Bundle response = myClient.search().forResource(Patient.class).count(1).returnBundle(Bundle.class).execute();
		String searchId = response.getIdElement().getIdPart();

		// 2 results returned, but no total since it's stripped
		assertThat(response.getEntry()).hasSize(1);
		assertNull(response.getTotalElement().getValue());

		StopWatch sw = new StopWatch();
		while (true) {
			SearchStatusEnum status = runInTransaction(() -> {
				Search search = mySearchEntityDao.findByUuidAndFetchIncludes(searchId).orElseThrow(() -> new IllegalStateException());
				return search.getStatus();
			});
			if (status == SearchStatusEnum.FINISHED) {
				break;
			}
			if (sw.getMillis() > 60000) {
				fail("Status is still " + status);
			}
		}

		// Load next page
		response = myClient.loadPage().next(response).execute();
		assertThat(response.getEntry()).hasSize(1);
		assertNull(response.getTotalElement().getValue());

		runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuidAndFetchIncludes(searchId).orElseThrow(() -> new IllegalStateException());
			assertEquals(3, search.getNumFound());
			assertEquals(1, search.getNumBlocked());
			assertEquals(2, search.getTotalCount());
		});

		// The paging should have ended now - but the last redacted female result is an empty existing page which should never have been there.
		String next = BundleUtil.getLinkUrlOfType(myFhirContext, response, "next");
		if (next != null) {
			response = myClient.loadPage().next(response).execute();
			fail(myFhirContext.newJsonParser().encodeResourceToString(response));
		}

	}

	/**
	 * Make sure the default methods all work and allow the response to proceed
	 */
	@Test
	public void testDefaultInterceptorAllowsAll() {
		myConsentInterceptor = new ConsentInterceptor(new IConsentService() {
		});
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(myConsentInterceptor);

		myClient.create().resource(new Patient().setGender(Enumerations.AdministrativeGender.MALE).addName(new HumanName().setFamily("1"))).execute();
		myClient.create().resource(new Patient().setGender(Enumerations.AdministrativeGender.MALE).addName(new HumanName().setFamily("2"))).execute();
		myClient.create().resource(new Patient().setGender(Enumerations.AdministrativeGender.FEMALE).addName(new HumanName().setFamily("3"))).execute();

		Bundle response = myClient.search().forResource(Patient.class).count(1).returnBundle(Bundle.class).execute();
		String searchId = response.getIdElement().getIdPart();

		assertThat(response.getEntry()).hasSize(1);
		assertNull(response.getTotalElement().getValue());

		// Load next page
		response = myClient.loadPage().next(response).execute();
		assertThat(response.getEntry()).hasSize(1);
		assertNull(response.getTotalElement().getValue());

		// The paging should have ended now - but the last redacted female result is an empty existing page which should never have been there.
		assertNotNull(BundleUtil.getLinkUrlOfType(myFhirContext, response, "next"));

		await()
			.until(
				() -> runInTransaction(() -> mySearchEntityDao.findByUuidAndFetchIncludes(searchId).orElseThrow(() -> new IllegalStateException()).getStatus()) ==
				SearchStatusEnum.FINISHED
			);

		runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuidAndFetchIncludes(searchId).orElseThrow(() -> new IllegalStateException());
			assertEquals(3, search.getNumFound());
			assertEquals(0, search.getNumBlocked());
			assertEquals(3, search.getTotalCount());
		});
	}

	/**
	 * Make sure the default methods all work and allow the response to proceed
	 */
	@Test
	public void testDefaultInterceptorAllowsFailure() {
		myConsentInterceptor = new ConsentInterceptor(new IConsentService() {
		});
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(myConsentInterceptor);

		myClient.create().resource(new Patient().setGender(Enumerations.AdministrativeGender.MALE).addName(new HumanName().setFamily("1"))).execute();
		myClient.create().resource(new Patient().setGender(Enumerations.AdministrativeGender.MALE).addName(new HumanName().setFamily("2"))).execute();
		myClient.create().resource(new Patient().setGender(Enumerations.AdministrativeGender.FEMALE).addName(new HumanName().setFamily("3"))).execute();

		try {
			myClient.search().forResource(Patient.class).where(new StringClientParam("INVALID_PARAM").matchesExactly().value("value")).returnBundle(Bundle.class).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("INVALID_PARAM");
		}
	}

	@Test
	public void testGraphQL_MaskLinkedResource() throws IOException {
		createPatientAndOrg();

		DelegatingConsentService consentService = new DelegatingConsentService();
		myConsentInterceptor = new ConsentInterceptor(consentService, IConsentContextServices.NULL_IMPL);
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(myConsentInterceptor);

		IConsentService svc = mock(IConsentService.class);
		when(svc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(svc.canSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(svc.willSeeResource(any(RequestDetails.class), any(IBaseResource.class), any())).thenAnswer(t -> {
			IBaseResource resource = t.getArgument(1, IBaseResource.class);
			if (resource instanceof Organization) {
				Organization org = new Organization();
				org.addIdentifier().setSystem("ORG_SYSTEM").setValue("ORG_VALUE");
				return new ConsentOutcome(ConsentOperationStatusEnum.PROCEED, org);
			}
			return ConsentOutcome.PROCEED;
		});

		consentService.setTarget(svc);
		String query = "{ name { family, given }, managingOrganization { reference, resource {name, identifier { system } } } }";
		String url = myServerBase + "/" + myPatientIds.get(0) + "/$graphql?query=" + UrlUtil.escapeUrlParam(query);
		ourLog.info("HTTP GET {}", url);
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_ACCEPT, Constants.CT_JSON);
		try (CloseableHttpResponse status = ourHttpClient.execute(get)) {
			String responseString = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseString);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertThat(responseString).contains("\"family\":\"PATIENT_FAMILY\"");
			assertThat(responseString).contains("\"given\":[\"PATIENT_GIVEN1\",\"PATIENT_GIVEN2\"]");
			assertThat(responseString).doesNotContain("\"name\":\"ORG_NAME\"");
			assertThat(responseString).contains("\"system\":\"ORG_SYSTEM\"");
		}

	}

	@Test
	public void testPaging_whenResourceViewingIsRejected_responseBundleWillHaveNextLink(){
		// given
		create50Observations();

		myConsentInterceptor = new ConsentInterceptor(new ConsentSvcRejectWillSeeResource());
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(myConsentInterceptor);

		// when
		Bundle results = myClient.search().forResource(Observation.class).count(10).returnBundle(Bundle.class).execute();
		assertThat(results.getEntry()).hasSize(0);

		// then
		String nextUrl = BundleUtil.getLinkUrlOfType(myFhirContext, results, "next");
		assertThat(nextUrl).contains("_getpagesoffset=10");

	}

	@Test
	public void testPaging_whenResourceViewingIsRejected_secondPageWillHavePreviousLink(){
		// given
		create50Observations();

		myConsentInterceptor = new ConsentInterceptor(new ConsentSvcRejectWillSeeResource());
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(myConsentInterceptor);

		// when
		Bundle results = myClient.search().forResource(Observation.class).count(10).returnBundle(Bundle.class).execute();
		Bundle nextResults = myClient.loadPage().next(results).execute();

		// then
		String previous = BundleUtil.getLinkUrlOfType(myFhirContext, nextResults, "previous");
		assertThat(previous).contains("_getpagesoffset=0");

	}

	private void createPatientAndOrg() {
		myPatientIds = new ArrayList<>();

		Organization org = new Organization();
		org.setName("ORG_NAME");
		IIdType orgId = myOrganizationDao.create(org).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.setActive(true);
		p.addName().setFamily("PATIENT_FAMILY").addGiven("PATIENT_GIVEN1").addGiven("PATIENT_GIVEN2");
		p.getManagingOrganization().setReference(orgId.getValue());
		String pid = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();
		myPatientIds.add(pid);
	}

	private void create50Observations() {
		myPatientIds = new ArrayList<>();
		myObservationIds = new ArrayList<>();

		Patient p = new Patient();
		p.setActive(true);
		String pid = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();
		myPatientIds.add(pid);

		for (int i = 0; i < 50; i++) {
			final Observation obs1 = new Observation();
			obs1.setStatus(Observation.ObservationStatus.FINAL);
			obs1.addIdentifier().setSystem("urn:system").setValue("I" + leftPad("" + i, 5, '0'));
			obs1.getSubject().setReference(pid);
			IIdType obs1id = myObservationDao.create(obs1).getId().toUnqualifiedVersionless();
			myObservationIds.add(obs1id.toUnqualifiedVersionless().getValue());
		}

		myObservationIdsEvenOnly =
			myObservationIds
				.stream()
				.filter(t -> Long.parseLong(t.substring(t.indexOf('/') + 1)) % 2 == 0)
				.collect(Collectors.toList());

		myObservationIdsOddOnly = ListUtils.removeAll(myObservationIds, myObservationIdsEvenOnly);
		myObservationIdsEvenOnlyBackwards = Lists.reverse(myObservationIdsEvenOnly);
	}

	private HttpEntity toEntity(Patient thePatient) {
		String encoded = myFhirContext.newJsonParser().encodeResourceToString(thePatient);
		ContentType cs = ContentType.create(Constants.CT_FHIR_JSON, Constants.CHARSET_UTF8);
		return new StringEntity(encoded, cs);
	}

	private class ConsentSvcMaskObservationSubjects implements IConsentService {

		private int mySeeCount = 0;

		@Override
		public ConsentOutcome startOperation(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
			return ConsentOutcome.PROCEED;
		}

		@Override
		public ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			return ConsentOutcome.PROCEED;
		}

		int getSeeCount() {
			return mySeeCount;
		}

		@Override
		public ConsentOutcome willSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			mySeeCount++;
			String resourceId = theResource.getIdElement().toUnqualifiedVersionless().getValue();
			ourLog.info("** SEE: {}", resourceId);
			if (theResource instanceof Observation) {
				((Observation) theResource).getSubject().setReference("");
				((Observation) theResource).getSubject().setResource(null);
				return new ConsentOutcome(ConsentOperationStatusEnum.PROCEED, theResource);
			}
			return ConsentOutcome.PROCEED;
		}

		@Override
		public void completeOperationSuccess(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
			// nothing
		}

		@Override
		public void completeOperationFailure(RequestDetails theRequestDetails, BaseServerResponseException theException, IConsentContextServices theContextServices) {
			// nothing
		}


	}

	private static class ReadingBackResourcesConsentSvc implements  IConsentService {
		private final DaoRegistry myDaoRegistry;

		public ReadingBackResourcesConsentSvc(DaoRegistry theDaoRegistry)   {
			myDaoRegistry = theDaoRegistry;
		}
		@Override
		public ConsentOutcome startOperation(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
			return new ConsentOutcome(ConsentOperationStatusEnum.PROCEED);

		}

		@Override
		public ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			String fhirType = theResource.fhirType();
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(fhirType);
			String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();
			dao.read(theResource.getIdElement());
			return ConsentOutcome.PROCEED;
		}

		@Override
		public ConsentOutcome willSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			return ConsentOutcome.PROCEED;
		}

		@Override
		public void completeOperationSuccess(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
			// nothing
		}

		@Override
		public void completeOperationFailure(RequestDetails theRequestDetails, BaseServerResponseException theException, IConsentContextServices theContextServices) {
			// nothing
		}
	}
	private static class ConsentSvcCantSeeOddNumbered implements IConsentService {

		@Override
		public ConsentOutcome startOperation(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
			return new ConsentOutcome(ConsentOperationStatusEnum.PROCEED);
		}

		@Override
		public ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			Long resIdLong = theResource.getIdElement().getIdPartAsLong();
			if (resIdLong % 2 == 1) {
				return new ConsentOutcome(ConsentOperationStatusEnum.REJECT);
			}
			return new ConsentOutcome(ConsentOperationStatusEnum.PROCEED);
		}

		@Override
		public ConsentOutcome willSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			return ConsentOutcome.PROCEED;
		}

		@Override
		public void completeOperationSuccess(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
			// nothing
		}

		@Override
		public void completeOperationFailure(RequestDetails theRequestDetails, BaseServerResponseException theException, IConsentContextServices theContextServices) {
			// nothing
		}


	}

	private static class ConsentSvcCantSeeFemales implements IConsentService {

		@Override
		public ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			if (theRequestDetails.getRestOperationType() != RestOperationTypeEnum.CREATE) {
				Patient patient = (Patient) theResource;
				if (patient.getGender() == Enumerations.AdministrativeGender.FEMALE) {
					return ConsentOutcome.REJECT;
				}
				return ConsentOutcome.PROCEED;

			}
			return ConsentOutcome.AUTHORIZED;
		}

	}

	private static class ConsentSvcCantSeeEvenNumbered implements IConsentService {

		@Override
		public ConsentOutcome startOperation(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
			return new ConsentOutcome(ConsentOperationStatusEnum.PROCEED);
		}

		@Override
		public ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			Long resIdLong = theResource.getIdElement().getIdPartAsLong();
			if (resIdLong % 2 == 0) {
				return new ConsentOutcome(ConsentOperationStatusEnum.REJECT);
			}
			return new ConsentOutcome(ConsentOperationStatusEnum.PROCEED);
		}

		@Override
		public ConsentOutcome willSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			return ConsentOutcome.PROCEED;
		}

		@Override
		public void completeOperationSuccess(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
			// nothing
		}

		@Override
		public void completeOperationFailure(RequestDetails theRequestDetails, BaseServerResponseException theException, IConsentContextServices theContextServices) {
			// nothing
		}


	}

	private static class ConsentSvcNop implements IConsentService {

		private final ConsentOperationStatusEnum myOperationStatus;
		private boolean myEnableCanSeeResource = true;

		private ConsentSvcNop(ConsentOperationStatusEnum theOperationStatus) {
			myOperationStatus = theOperationStatus;
		}

		private ConsentSvcNop(ConsentOperationStatusEnum theOperationStatus, boolean theEnableCanSeeResource) {
			myOperationStatus = theOperationStatus;
			myEnableCanSeeResource = theEnableCanSeeResource;
		}

		@Override
		public ConsentOutcome startOperation(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
			return new ConsentOutcome(myOperationStatus);
		}

		@Override
		public boolean shouldProcessCanSeeResource(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
			return myEnableCanSeeResource;
		}

		@Override
		public ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			return new ConsentOutcome(ConsentOperationStatusEnum.PROCEED);
		}

		@Override
		public ConsentOutcome willSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			return ConsentOutcome.PROCEED;
		}

		@Override
		public void completeOperationSuccess(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
			// nothing
		}

		@Override
		public void completeOperationFailure(RequestDetails theRequestDetails, BaseServerResponseException theException, IConsentContextServices theContextServices) {
			// nothing
		}


	}

	private static class ConsentSvcRejectCanSeeAnything implements IConsentService {

		@Override
		public ConsentOutcome startOperation(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
			return ConsentOutcome.PROCEED;
		}

		@Override
		public ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			return ConsentOutcome.REJECT;
		}

		@Override
		public ConsentOutcome willSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			return ConsentOutcome.PROCEED;
		}

		@Override
		public void completeOperationSuccess(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
			// nothing
		}

		@Override
		public void completeOperationFailure(RequestDetails theRequestDetails, BaseServerResponseException theException, IConsentContextServices theContextServices) {
			// nothing
		}


	}


	private static class ConsentSvcRejectWillSeeEvenNumbered implements IConsentService {

		@Override
		public ConsentOutcome startOperation(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
			return ConsentOutcome.PROCEED;
		}

		@Override
		public ConsentOutcome canSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			return ConsentOutcome.PROCEED;
		}

		@Override
		public ConsentOutcome willSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			if (theResource.getIdElement().isIdPartValidLong()) {
				Long resIdLong = theResource.getIdElement().getIdPartAsLong();
				if (resIdLong % 2 == 0) {
					return new ConsentOutcome(ConsentOperationStatusEnum.REJECT);
				}
			}
			return new ConsentOutcome(ConsentOperationStatusEnum.PROCEED);
		}

		@Override
		public void completeOperationSuccess(RequestDetails theRequestDetails, IConsentContextServices theContextServices) {
			// nothing
		}

		@Override
		public void completeOperationFailure(RequestDetails theRequestDetails, BaseServerResponseException theException, IConsentContextServices theContextServices) {
			// nothing
		}


	}

	private static class ConsentSvcRejectWillSeeResource implements IConsentService {
		@Override
		public ConsentOutcome willSeeResource(RequestDetails theRequestDetails, IBaseResource theResource, IConsentContextServices theContextServices) {
			if("Bundle".equals(theResource.fhirType())){
				return new ConsentOutcome(ConsentOperationStatusEnum.PROCEED);
			}
			return new ConsentOutcome(ConsentOperationStatusEnum.REJECT);
		}

	}

}
