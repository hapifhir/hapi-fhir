package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.hamcrest.Matchers;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Basic;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleLinkComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.hl7.fhir.r4.model.Bundle.SearchEntryMode;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.DocumentManifest;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Encounter.EncounterLocationComponent;
import org.hl7.fhir.r4.model.Encounter.EncounterStatus;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ImagingStudy;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Media;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.MolecularSequence;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Narrative.NarrativeStatus;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationComponentComponent;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.Questionnaire.QuestionnaireItemType;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType;
import org.hl7.fhir.r4.model.Subscription.SubscriptionStatus;
import org.hl7.fhir.r4.model.UnsignedIntType;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.AopTestUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

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
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.config.r4.FhirContextR4Config.DEFAULT_PRESERVE_VERSION_REFS;
import static ca.uhn.fhir.jpa.util.TestUtil.sleepOneClick;
import static ca.uhn.fhir.rest.param.BaseParamWithPrefix.MSG_PREFIX_INVALID_FORMAT;
import static ca.uhn.fhir.util.TestUtil.sleepAtLeast;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("Duplicates")
public class ResourceProviderR4Test extends BaseResourceProviderR4Test {

	public static final int LARGE_NUMBER = 77;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4Test.class);
	private SearchCoordinatorSvcImpl mySearchCoordinatorSvcRaw;
	private CapturingInterceptor myCapturingInterceptor = new CapturingInterceptor();
	@Autowired
	private ISearchDao mySearchEntityDao;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());
		myDaoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());
		myDaoConfig.setCountSearchResultsUpTo(new DaoConfig().getCountSearchResultsUpTo());
		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
		myDaoConfig.setAllowContainsSearches(new DaoConfig().isAllowContainsSearches());
		myDaoConfig.setIndexMissingFields(new DaoConfig().getIndexMissingFields());

		mySearchCoordinatorSvcRaw.setLoadingThrottleForUnitTests(null);
		mySearchCoordinatorSvcRaw.setSyncSizeForUnitTests(SearchCoordinatorSvcImpl.DEFAULT_SYNC_SIZE);
		mySearchCoordinatorSvcRaw.setNeverUseLocalSearchForUnitTests(false);
		mySearchCoordinatorSvcRaw.cancelAllActiveSearches();
		myDaoConfig.getModelConfig().setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED);

		myClient.unregisterInterceptor(myCapturingInterceptor);
	}

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());
		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths(DEFAULT_PRESERVE_VERSION_REFS);

		myDaoConfig.setAllowMultipleDelete(true);
		myClient.registerInterceptor(myCapturingInterceptor);
		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
	}

	@Test
	public void testParameterWithNoValueThrowsError_InvalidChainOnCustomSearch() throws IOException {
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.addBase("BodySite").addBase("Procedure");
		searchParameter.setCode("focalAccess");
		searchParameter.setType(Enumerations.SearchParamType.REFERENCE);
		searchParameter.setExpression("Procedure.extension('Procedure#focalAccess')");
		searchParameter.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myClient.create().resource(searchParameter).execute();

		mySearchParamRegistry.forceRefresh();

		HttpGet get = new HttpGet(ourServerBase + "/Procedure?focalAccess.a%20ne%20e");
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			String output = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			assertThat(output, containsString("Invalid parameter chain: focalAccess.a ne e"));
			assertEquals(400, resp.getStatusLine().getStatusCode());
		}

	}

	@Test
	public void testParameterWithNoValueThrowsError_InvalidRootParam() throws IOException {
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.addBase("BodySite").addBase("Procedure");
		searchParameter.setCode("focalAccess");
		searchParameter.setType(Enumerations.SearchParamType.REFERENCE);
		searchParameter.setExpression("Procedure.extension('Procedure#focalAccess')");
		searchParameter.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myClient.create().resource(searchParameter).execute();

		mySearchParamRegistry.forceRefresh();

		HttpGet get = new HttpGet(ourServerBase + "/Procedure?a");
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			String output = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			assertThat(output, containsString("Unknown search parameter &quot;a&quot;"));
			assertEquals(400, resp.getStatusLine().getStatusCode());
		}
	}

	@Test
	public void testSearchForTokenValueOnlyUsesValueHash() {

		myCaptureQueriesListener.clear();

		myClient
			.loadPage()
			.byUrl(ourServerBase + "/Practitioner?identifier=" + UrlUtil.escapeUrlParam("ABC|,DEF"))
			.andReturnBundle(Bundle.class)
			.execute();

		myCaptureQueriesListener.logSelectQueries();
	}

	@Test
	public void testSearchWithContainsLowerCase() {
		myDaoConfig.setAllowContainsSearches(true);

		Patient pt1 = new Patient();
		pt1.addName().setFamily("Elizabeth");
		String pt1id = myPatientDao.create(pt1).getId().toUnqualifiedVersionless().getValue();

		Patient pt2 = new Patient();
		pt2.addName().setFamily("fghijk");
		String pt2id = myPatientDao.create(pt2).getId().toUnqualifiedVersionless().getValue();

		Patient pt3 = new Patient();
		pt3.addName().setFamily("zzzzz");
		myPatientDao.create(pt3).getId().toUnqualifiedVersionless().getValue();


		Bundle output = myClient
			.search()
			.forResource("Patient")
			.where(Patient.NAME.contains().value("ZAB"))
			.returnBundle(Bundle.class)
			.execute();
		List<String> ids = output.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList());
		assertThat(ids, containsInAnyOrder(pt1id));

		output = myClient
			.search()
			.forResource("Patient")
			.where(Patient.NAME.contains().value("zab"))
			.returnBundle(Bundle.class)
			.execute();
		ids = output.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList());
		assertThat(ids, containsInAnyOrder(pt1id));

	}

	@Test
	public void testSearchWithPercentSign() {
		myDaoConfig.setAllowContainsSearches(true);

		Patient pt1 = new Patient();
		pt1.addName().setFamily("Smith%");
		String pt1id = myPatientDao.create(pt1).getId().toUnqualifiedVersionless().getValue();

		Bundle output = myClient
			.search()
			.forResource("Patient")
			.where(Patient.NAME.contains().value("Smith%"))
			.returnBundle(Bundle.class)
			.execute();
		List<String> ids = output.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList());
		assertThat(ids, containsInAnyOrder(pt1id));

		Patient pt2 = new Patient();
		pt2.addName().setFamily("Sm%ith");
		String pt2id = myPatientDao.create(pt2).getId().toUnqualifiedVersionless().getValue();

		output = myClient
			.search()
			.forResource("Patient")
			.where(Patient.NAME.contains().value("Sm%ith"))
			.returnBundle(Bundle.class)
			.execute();
		ids = output.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList());
		assertThat(ids, containsInAnyOrder(pt2id));
	}

	@Test
	public void testSearchWithDateInvalid() throws IOException {
		HttpGet get = new HttpGet(ourServerBase + "/Condition?onset-date=junk");
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			String output = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			assertThat(output, containsString(MSG_PREFIX_INVALID_FORMAT + "&quot;junk&quot;"));
			assertEquals(400, resp.getStatusLine().getStatusCode());
		}

		get = new HttpGet(ourServerBase + "/Condition?onset-date=ge");
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			String output = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			assertThat(output, containsString(MSG_PREFIX_INVALID_FORMAT + "&quot;ge&quot;"));
			assertEquals(400, resp.getStatusLine().getStatusCode());
		}

		get = new HttpGet(ourServerBase + "/Condition?onset-date=" + UrlUtil.escapeUrlParam(">"));
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			String output = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			assertThat(output, containsString(MSG_PREFIX_INVALID_FORMAT + "&quot;&gt;&quot;"));
			assertEquals(400, resp.getStatusLine().getStatusCode());
		}

	}


	@Test
	public void testSearchWithSlashes() {
		myDaoConfig.setSearchPreFetchThresholds(Lists.newArrayList(10, 50, 10000));

		Procedure procedure = new Procedure();
		procedure.setStatus(Procedure.ProcedureStatus.COMPLETED);
		String procedureId = myClient.create().resource(procedure).execute().getId().toUnqualifiedVersionless().getValue();

		DocumentReference dr = new DocumentReference();
		dr.addContent().getAttachment().setContentType("application/vnd.mfer");
		String drId = myClient.create().resource(dr).execute().getId().toUnqualifiedVersionless().getValue();

		for (int i = 0; i < 60; i++) {
			Observation obs = new Observation();
			obs.addPartOf().setReference(procedureId);
			obs.addDerivedFrom().setReference(drId);
			myClient.create().resource(obs).execute();
		}

		ourLog.info("Starting search");

		Bundle response = myClient
			.search()
			.byUrl("Observation?part-of=" + procedureId + "&derived-from:DocumentReference.contenttype=application/vnd.mfer&_total=accurate&_count=2")
			.returnBundle(Bundle.class)
			.execute();

		int obsCount = 0;
		int pageCount = 0;
		while (response != null) {
			obsCount += response.getEntry().size();
			pageCount++;
			if (response.getLink("next") != null) {
				response = myClient.loadPage().next(response).execute();
			} else {
				response = null;
			}


			ourLog.info("Have loaded {} pages and {} reources", pageCount, obsCount);
		}

		assertEquals(60, obsCount);
		assertEquals(30, pageCount);

	}


	@Test
	public void testManualPagingLinkOffsetDoesntReturnBeyondEnd() {
		myDaoConfig.setSearchPreFetchThresholds(Lists.newArrayList(10, 1000));

		for (int i = 0; i < 50; i++) {
			Organization o = new Organization();
			o.setId("O" + i);
			o.setName("O" + i);
			myClient.update().resource(o).execute().getId().toUnqualifiedVersionless();
		}

		Bundle output = myClient
			.search()
			.forResource("Organization")
			.count(3)
			.returnBundle(Bundle.class)
			.execute();

		assertNull(output.getTotalElement().getValue());

		output = myClient
			.search()
			.forResource("Organization")
			.count(3)
			.totalMode(SearchTotalModeEnum.ACCURATE)
			.returnBundle(Bundle.class)
			.execute();

		assertNotNull(output.getTotalElement().getValue());

		String linkNext = output.getLink("next").getUrl();
		linkNext = linkNext.replaceAll("_getpagesoffset=[0-9]+", "_getpagesoffset=3300");
		assertThat(linkNext, containsString("_getpagesoffset=3300"));

		Bundle nextPageBundle = myClient.loadPage().byUrl(linkNext).andReturnBundle(Bundle.class).execute();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(nextPageBundle));
		assertEquals(null, nextPageBundle.getLink("next"));
	}

	@Test
	public void testSearchLinksWorkWithIncludes() {
		for (int i = 0; i < 5; i++) {

			Organization o = new Organization();
			o.setId("O" + i);
			o.setName("O" + i);
			IIdType oid = myClient.update().resource(o).execute().getId().toUnqualifiedVersionless();

			Patient p = new Patient();
			p.setId("P" + i);
			p.getManagingOrganization().setReference(oid.getValue());
			myClient.update().resource(p).execute();

		}

		Bundle output = myClient
			.search()
			.forResource("Patient")
			.include(IBaseResource.INCLUDE_ALL)
			.count(3)
			.returnBundle(Bundle.class)
			.execute();

		List<String> ids = output.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList());
		ourLog.info("Ids: {}", ids);
		assertEquals(6, output.getEntry().size());
		assertNotNull(output.getLink("next"));

		// Page 2
		output = myClient
			.loadPage()
			.next(output)
			.execute();

		ids = output.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList());
		ourLog.info("Ids: {}", ids);
		assertEquals(4, output.getEntry().size());
		assertNull(output.getLink("next"));

	}

	@Test
	public void testSearchWithDeepChain() throws IOException {

		SearchParameter sp = new SearchParameter();
		sp.addBase("Patient");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode("extpatorg");
		sp.setName("extpatorg");
		sp.setExpression("Patient.extension('http://patext').value.as(Reference)");
		myClient.create().resource(sp).execute();

		sp = new SearchParameter();
		sp.addBase("Organization");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode("extorgorg");
		sp.setName("extorgorg");
		sp.setExpression("Organization.extension('http://orgext').value.as(Reference)");
		myClient.create().resource(sp).execute();

		mySearchParamRegistry.forceRefresh();

		Organization grandParent = new Organization();
		grandParent.setName("GRANDPARENT");
		IIdType grandParentId = myClient.create().resource(grandParent).execute().getId().toUnqualifiedVersionless();

		Organization parent = new Organization();
		parent.setName("PARENT");
		parent.getPartOf().setReference(grandParentId.getValue());
		parent.addExtension("http://orgext", new Reference().setReference(grandParentId.getValue()));
		IIdType parentId = myClient.create().resource(parent).execute().getId().toUnqualifiedVersionless();

		Organization org = new Organization();
		org.setName("ORGANIZATION");
		org.getPartOf().setReference(parentId.getValue());
		org.addExtension("http://orgext", new Reference().setReference(parentId.getValue()));
		IIdType orgId = myClient.create().resource(org).execute().getId().toUnqualifiedVersionless();

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		myCaptureQueriesListener.clear();

		Patient p = new Patient();
		p.getManagingOrganization().setReference(orgId.getValue());
		p.addExtension("http://patext", new Reference().setReference(orgId.getValue()));
		String pid = myClient.create().resource(p).execute().getId().toUnqualified().getValue();

		List<String> idValues;

		// Regular search param
		idValues = searchAndReturnUnqualifiedIdValues(ourServerBase + "/Patient?organization=" + orgId.getValue());
		assertThat(idValues, contains(pid));

		idValues = searchAndReturnUnqualifiedIdValues(ourServerBase + "/Patient?organization.name=ORGANIZATION");
		assertThat(idValues, contains(pid));

		idValues = searchAndReturnUnqualifiedIdValues(ourServerBase + "/Patient?organization.partof.name=PARENT");
		assertThat(idValues, contains(pid));

		idValues = searchAndReturnUnqualifiedIdValues(ourServerBase + "/Patient?organization.partof.partof.name=GRANDPARENT");
		assertThat(idValues, contains(pid));

		// Search param on extension
		idValues = searchAndReturnUnqualifiedIdValues(ourServerBase + "/Patient?extpatorg=" + orgId.getValue());
		assertThat(idValues, contains(pid));

		idValues = searchAndReturnUnqualifiedIdValues(ourServerBase + "/Patient?extpatorg.name=ORGANIZATION");
		assertThat(idValues, contains(pid));

		myCaptureQueriesListener.clear();
		idValues = searchAndReturnUnqualifiedIdValues(ourServerBase + "/Patient?extpatorg.extorgorg.name=PARENT");
		myCaptureQueriesListener.logSelectQueries();
		assertThat(idValues, contains(pid));

		idValues = searchAndReturnUnqualifiedIdValues(ourServerBase + "/Patient?extpatorg.extorgorg.extorgorg.name=GRANDPARENT");
		assertThat(idValues, contains(pid));

	}

	@Test
	public void testSearchFetchPageBeyondEnd() {
		for (int i = 0; i < 10; i++) {
			Organization o = new Organization();
			o.setId("O" + i);
			o.setName("O" + i);
			IIdType oid = myClient.update().resource(o).execute().getId().toUnqualifiedVersionless();
		}

		Bundle output = myClient
			.search()
			.forResource("Organization")
			.count(3)
			.returnBundle(Bundle.class)
			.execute();

		String nextPageUrl = output.getLink("next").getUrl();
		String url = nextPageUrl.replace("_getpagesoffset=3", "_getpagesoffset=999");
		ourLog.info("Going to request URL: {}", url);

		output = myClient
			.loadPage()
			.byUrl(url)
			.andReturnBundle(Bundle.class)
			.execute();
		assertEquals(0, output.getEntry().size());

	}

	@Test
	public void testDeleteConditional() {

		Patient p = new Patient();
		p.addName().setFamily("FAM").addGiven("GIV");
		IIdType id = myPatientDao.create(p).getId();

		myClient.read().resource("Patient").withId(id.toUnqualifiedVersionless()).execute();

		myClient
			.delete()
			.resourceConditionalByUrl("Patient?family=FAM&given=giv")
			.execute();

		try {
			myClient.read().resource("Patient").withId(id.toUnqualifiedVersionless()).execute();
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

	}

	@Test
	public void testResourceGoneIncludesVersion() {

		Patient p = new Patient();
		p.addName().setFamily("FAM").addGiven("GIV");
		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		myClient
			.delete()
			.resourceById(id)
			.execute();

		CapturingInterceptor captureInterceptor = new CapturingInterceptor();
		myClient.registerInterceptor(captureInterceptor);

		try {
			myClient.read().resource("Patient").withId(id.toUnqualifiedVersionless()).execute();
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		List<String> locationHeader = captureInterceptor.getLastResponse().getHeaders(Constants.HEADER_LOCATION);
		assertEquals(1, locationHeader.size());
		assertThat(locationHeader.get(0), containsString(id.getValue() + "/_history/2"));
	}

	@Test
	public void testUpdateWithNoBody() throws IOException {

		HttpPut httpPost = new HttpPut(ourServerBase + "/Patient/AAA");
		try (CloseableHttpResponse status = ourHttpClient.execute(httpPost)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(status.getStatusLine().toString());
			ourLog.info(responseContent);

			assertEquals(400, status.getStatusLine().getStatusCode());
			assertThat(responseContent, containsString("No body was supplied in request"));
		}

	}


	@Test
	public void testCreateWithNoBody() throws IOException {

		HttpPost httpPost = new HttpPost(ourServerBase + "/Patient");
		try (CloseableHttpResponse status = ourHttpClient.execute(httpPost)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(status.getStatusLine().toString());
			ourLog.info(responseContent);

			assertEquals(400, status.getStatusLine().getStatusCode());
			assertThat(responseContent, containsString("No body was supplied in request"));
		}

	}

	@Test
	public void testCreateWithClientAssignedId() throws IOException {

		// Create with client assigned ID
		Patient p = new Patient();
		p.setActive(true);
		p.setId("AAA");
		String encoded = myFhirContext.newJsonParser().encodeResourceToString(p);
		HttpPut httpPut = new HttpPut(ourServerBase + "/Patient/AAA");
		httpPut.setEntity(new StringEntity(encoded, ContentType.parse("application/json+fhir")));
		try (CloseableHttpResponse status = ourHttpClient.execute(httpPut)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(status.getStatusLine().toString());
			ourLog.info(responseContent);

			assertEquals(201, status.getStatusLine().getStatusCode());
			assertThat(responseContent, containsString("true"));
		}

		// Delete
		HttpDelete httpDelete = new HttpDelete(ourServerBase + "/Patient/AAA");
		try (CloseableHttpResponse status = ourHttpClient.execute(httpDelete)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
		}

		// Create it again
		p = new Patient();
		p.setActive(true);
		p.setId("AAA");
		encoded = myFhirContext.newJsonParser().encodeResourceToString(p);
		httpPut = new HttpPut(ourServerBase + "/Patient/AAA");
		httpPut.setEntity(new StringEntity(encoded, ContentType.parse("application/json+fhir")));
		try (CloseableHttpResponse status = ourHttpClient.execute(httpPut)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(status.getStatusLine().toString());
			ourLog.info(responseContent);

			assertEquals(201, status.getStatusLine().getStatusCode());
			assertThat(responseContent, containsString("true"));
		}


	}


	@BeforeEach
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
		mySearchCoordinatorSvcRaw = AopTestUtils.getTargetObject(mySearchCoordinatorSvc);
	}

	private void checkParamMissing(String paramName) throws IOException {
		HttpGet get = new HttpGet(ourServerBase + "/Observation?" + paramName + ":missing=false");
		CloseableHttpResponse resp = ourHttpClient.execute(get);
		resp.getEntity().getContent().close();
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
		String input = IOUtils.toString(getClass().getResourceAsStream("/basic-stu3.xml"), StandardCharsets.UTF_8);

		String respString = myClient.transaction().withBundle(input).prettyPrint().execute();
		ourLog.info(respString);
		Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, respString);
		IdType id = new IdType(bundle.getEntry().get(0).getResponse().getLocation());

		Basic basic = myClient.read().resource(Basic.class).withId(id).execute();
		List<Extension> exts = basic.getExtensionsByUrl("http://localhost:1080/hapi-fhir-jpaserver-example/baseDstu2/StructureDefinition/DateID");
		assertEquals(1, exts.size());
	}

	private List<String> searchAndReturnUnqualifiedIdValues(String theUri) throws IOException {
		List<String> ids;
		HttpGet get = new HttpGet(theUri);

		ourLog.info("About to perform search for: {}", theUri);

		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, resp);
			ids = toUnqualifiedIdValues(bundle);
		}
		return ids;
	}

	private List<String> searchAndReturnUnqualifiedVersionlessIdValues(String uri) throws IOException {
		List<String> ids;
		HttpGet get = new HttpGet(uri);

		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, resp);
			ids = toUnqualifiedVersionlessIdValues(bundle);
			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
		}

		return ids;
	}

	@Test
	@Disabled
	public void test() throws IOException {
		HttpGet get = new HttpGet(ourServerBase + "/QuestionnaireResponse?_count=50&status=completed&questionnaire=ARIncenterAbsRecord&_lastUpdated=%3E" + UrlUtil.escapeUrlParam("=2018-01-01") + "&context.organization=O3435");
		ourLog.info("*** MAKING QUERY");
		ourHttpClient.execute(get);
		System.exit(0);
	}

	@Test
	public void testBundleCreate() throws Exception {
		IGenericClient client = myClient;

		String resBody = IOUtils.toString(ResourceProviderR4Test.class.getResource("/r4/document-father.json"), StandardCharsets.UTF_8);
		IIdType id = client.create().resource(resBody).execute().getId();

		ourLog.info("Created: {}", id);

		Bundle bundle = client.read().resource(Bundle.class).withId(id).execute();

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
	}

	@Test
	public void testBundleCreateWithTypeTransaction() throws Exception {
		IGenericClient client = myClient;

		String resBody = IOUtils.toString(ResourceProviderR4Test.class.getResource("/r4/document-father.json"), StandardCharsets.UTF_8);
		resBody = resBody.replace("\"type\": \"document\"", "\"type\": \"transaction\"");
		try {
			client.create().resource(resBody).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Unable to store a Bundle resource on this server with a Bundle.type value of: transaction. Note that if you are trying to perform a FHIR transaction or batch operation you should POST the Bundle resource to the Base URL of the server, not to the /Bundle endpoint."));
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

		assertThat(toUnqualifiedVersionlessIds(resp), contains(id));

		//@formatter:off
		resp = myClient
			.search()
			.forResource(Subscription.class)
			.where(Subscription.TYPE.exactly().systemAndCode(SubscriptionChannelType.WEBSOCKET.getSystem(), SubscriptionChannelType.WEBSOCKET.toCode()))
			.and(Subscription.STATUS.exactly().systemAndCode(SubscriptionStatus.ACTIVE.getSystem(), SubscriptionStatus.ACTIVE.toCode()))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:off

		assertThat(toUnqualifiedVersionlessIds(resp), contains(id));

		//@formatter:off
		resp = myClient
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
	public void testCreateAndReadBackResourceWithContainedReferenceToContainer() {
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		String input = "{\n" +
			"  \"resourceType\": \"Organization\",\n" +
			"  \"id\": \"1\",\n" +
			"  \"meta\": {\n" +
			"    \"tag\": [\n" +
			"      {\n" +
			"        \"system\": \"https://blah.org/deployment\",\n" +
			"        \"code\": \"e69414dd-b5c2-462d-bcfd-9d04d6b16596\",\n" +
			"        \"display\": \"DEPLOYMENT\"\n" +
			"      },\n" +
			"      {\n" +
			"        \"system\": \"https://blah.org/region\",\n" +
			"        \"code\": \"b47d7a5b-b159-4bed-a8f8-3258e6603adb\",\n" +
			"        \"display\": \"REGION\"\n" +
			"      },\n" +
			"      {\n" +
			"        \"system\": \"https://blah.org/provider\",\n" +
			"        \"code\": \"28c30004-0333-40cf-9e7f-3f9e080930bd\",\n" +
			"        \"display\": \"PROVIDER\"\n" +
			"      }\n" +
			"    ]\n" +
			"  },\n" +
			"  \"contained\": [\n" +
			"    {\n" +
			"      \"resourceType\": \"Location\",\n" +
			"      \"id\": \"2\",\n" +
			"      \"position\": {\n" +
			"        \"longitude\": 51.443238301454289,\n" +
			"        \"latitude\": 7.34196905697293\n" +
			"      },\n" +
			"      \"managingOrganization\": {\n" +
			"        \"reference\": \"#\"\n" +
			"      }\n" +
			"    }\n" +
			"  ],\n" +
			"  \"type\": [\n" +
			"    {\n" +
			"      \"coding\": [\n" +
			"        {\n" +
			"          \"system\": \"https://blah.org/fmc/OrganizationType\",\n" +
			"          \"code\": \"CLINIC\",\n" +
			"          \"display\": \"Clinic\"\n" +
			"        }\n" +
			"      ]\n" +
			"    }\n" +
			"  ],\n" +
			"  \"name\": \"testOrg\"\n" +
			"}";

		Organization org = myFhirContext.newJsonParser().parseResource(Organization.class, input);
		IIdType id = myOrganizationDao.create(org).getId();
		org = myOrganizationDao.read(id);

		String output = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(org);
		ourLog.info(output);

		Location loc = (Location) org.getContained().get(0);
		assertEquals("#", loc.getManagingOrganization().getReference());
	}


	@Test
	public void testCountParam() {
		List<IBaseResource> resources = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Organization org = new Organization();
			org.setName("rpr4_testCountParam_01");
			resources.add(org);
		}
		List<IBaseResource> outcome = myClient.transaction().withResources(resources).prettyPrint().encodedXml().execute();

		runInTransaction(() -> {
			assertEquals(100, myResourceTableDao.count());
		});

		Bundle found = myClient
			.search()
			.forResource(Organization.class)
			.where(Organization.NAME.matches().value("rpr4_testCountParam_01"))
			.count(10)
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(10, found.getEntry().size());

		found = myClient.search().forResource(Organization.class).where(Organization.NAME.matches().value("rpr4_testCountParam_01")).count(999).returnBundle(Bundle.class).execute();
		assertEquals(50, found.getEntry().size());

	}

	@Test
	public void testCreateConditionalWithPreferRepresentation() {
		Patient p = new Patient();
		p.setActive(false);
		p.addIdentifier().setSystem("foo").setValue("bar");
		IIdType id = myClient.create().resource(p).execute().getId();

		p = new Patient();
		p.setId(id);
		p.setActive(true);
		p.addIdentifier().setSystem("foo").setValue("bar");
		myClient.update().resource(p).execute();

		// Now conditional create
		p = new Patient();
		p.setActive(true);
		p.setBirthDateElement(new DateType("2011-01-01"));
		p.addIdentifier().setSystem("foo").setValue("bar");
		MethodOutcome outcome = myClient
			.create()
			.resource(p)
			.conditionalByUrl("Patient?identifier=foo|bar")
			.prefer(PreferReturnEnum.REPRESENTATION)
			.execute();

		assertEquals(id.getIdPart(), outcome.getId().getIdPart());
		assertEquals("2", outcome.getId().getVersionIdPart());
		p = (Patient) outcome.getResource();
		assertNull(p.getBirthDate());
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
		HttpPut putRequest = new HttpPut(ourServerBase + "/Binary/" + resource.getIdPart());
		putRequest.setEntity(new ByteArrayEntity(arr, ContentType.parse("dansk")));
		CloseableHttpResponse resp = ourHttpClient.execute(putRequest);
		try {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertEquals(resource.withVersion("2").getValue(), resp.getFirstHeader("Content-Location").getValue());
		} finally {
			resp.close();
		}

		fromDB = myClient.read().resource(Binary.class).withId(resource.toVersionless()).execute();
		assertEquals("2", fromDB.getIdElement().getVersionIdPart());

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
			resp.close();
		}

		fromDB = myClient.read().resource(Binary.class).withId(resource.toVersionless()).execute();
		assertEquals("3", fromDB.getIdElement().getVersionIdPart());

		// Now an update with the wrong ID in the body

		arr[0] = 4;
		binary.setId("");
		encoded = myFhirContext.newJsonParser().encodeResourceToString(binary);
		putRequest = new HttpPut(ourServerBase + "/Binary/" + resource.getIdPart());
		putRequest.setEntity(new StringEntity(encoded, ContentType.parse("application/json+fhir")));
		resp = ourHttpClient.execute(putRequest);
		try {
			assertEquals(400, resp.getStatusLine().getStatusCode());
		} finally {
			resp.close();
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
		myClient.create().resource(input).execute();
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

		ourRestServer.registerInterceptor(interceptor);
		myClient.registerInterceptor(new IClientInterceptor() {
			@Override
			public void interceptRequest(IHttpRequest theRequest) {
				theRequest.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);
			}

			@Override
			public void interceptResponse(IHttpResponse theResponse) {               // TODO Auto-generated method stub
			}

		});
		try {
			// Missing status, which is mandatory
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:foo").setValue("bar");
			IBaseResource outcome = myClient.create().resource(obs).execute().getOperationOutcome();

			String encodedOo = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
			ourLog.info(encodedOo);
			assertThat(encodedOo, containsString("cvc-complex-type.2.4.b"));
			assertThat(encodedOo, containsString("Successfully created resource \\\"Observation/"));

			interceptor.setAddValidationResultsToResponseOperationOutcome(false);
			outcome = myClient.create().resource(obs).execute().getOperationOutcome();
			encodedOo = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
			ourLog.info(encodedOo);
			assertThat(encodedOo, not(containsString("cvc-complex-type.2.4.b")));
			assertThat(encodedOo, containsString("Successfully created resource \\\"Observation/"));

		} finally {
			ourRestServer.unregisterInterceptor(interceptor);
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
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.CHOICE).setAnswerValueSet((optId.getValue()));
		IIdType qId = myClient.create().resource(q).execute().getId();

		QuestionnaireResponse qa;

		// Good code

		qa = new QuestionnaireResponse();
		qa.setQuestionnaire(qId.toUnqualifiedVersionless().getValue());
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code0"));
		myClient.create().resource(qa).execute();

		// Bad code

		qa = new QuestionnaireResponse();
		qa.setQuestionnaire(qId.toUnqualifiedVersionless().getValue());
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code1"));
		try {
			myClient.create().resource(qa).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Question with linkId[link0]"));
		}
	}

	@Test
	public void testSearchByExternalReference() {
		myDaoConfig.setAllowExternalReferences(true);

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
		IIdType mediaId = myClient.create().resource(media).execute().getId();

		// Search for wrong type
		Bundle returnedBundle = myClient.search()
			.forResource(Observation.class)
			.where(Observation.ENCOUNTER.hasId(patientReference.getReference()))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(0, returnedBundle.getEntry().size());

		// Search for right type
		returnedBundle = myClient.search()
			.forResource(Media.class)
			.where(Media.SUBJECT.hasId(patientReference.getReference()))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(mediaId, returnedBundle.getEntryFirstRep().getResource().getIdElement());
	}

	@Test
	public void testCreateResourceConditional() throws IOException {
		String methodName = "testCreateResourceConditional";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

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
	public void testCreateResourceConditionalComplex() throws IOException {
		Patient pt = new Patient();
		pt.addIdentifier().setSystem("http://general-hospital.co.uk/Identifiers").setValue("09832345234543876876");
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

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
		String resource = "<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"2\"/></Patient>";

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

		Bundle res = myClient.search()
			.forResource(Encounter.class)
			.where(Encounter.IDENTIFIER.exactly().systemAndCode("urn:foo", "testDeepChainingE1"))
			.include(Encounter.INCLUDE_LOCATION.asRecursive())
			.include(Location.INCLUDE_PARTOF.asRecursive())
			.returnBundle(Bundle.class)
			.execute();

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(res));

		assertEquals(3, res.getEntry().size());
		assertEquals(1, genResourcesOfType(res, Encounter.class).size());
		assertEquals(e1id.toUnqualifiedVersionless(), genResourcesOfType(res, Encounter.class).get(0).getIdElement().toUnqualifiedVersionless());

	}

	@Test
	public void testReferenceOfWrongType() {
		Group l2 = new Group();
		l2.setActive(true);
		IIdType l2id = myClient.create().resource(l2).execute().getId();

		Encounter e1 = new Encounter();
		e1.getStatusElement().setValue(EncounterStatus.INPROGRESS);
		e1.getSubject().setReference(l2id.toUnqualifiedVersionless().getValue());
		IIdType e1id = myClient.create().resource(e1).execute().getId();

		// Wrong type
		Bundle res = myClient.search()
			.forResource(Encounter.class)
			.include(Encounter.INCLUDE_PATIENT.asRecursive())
			.returnBundle(Bundle.class)
			.execute();

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(res));

		assertEquals(1, res.getEntry().size());
		assertEquals(1, genResourcesOfType(res, Encounter.class).size());
		assertEquals(e1id.toUnqualifiedVersionless(), genResourcesOfType(res, Encounter.class).get(0).getIdElement().toUnqualifiedVersionless());

		// Right type
		res = myClient.search()
			.forResource(Encounter.class)
			.include(Encounter.INCLUDE_SUBJECT.asRecursive())
			.returnBundle(Bundle.class)
			.execute();

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(res));

		assertEquals(2, res.getEntry().size());
		assertEquals(1, genResourcesOfType(res, Encounter.class).size());
		assertEquals(1, genResourcesOfType(res, Group.class).size());

	}

	@Test
	public void testDeleteConditionalMultiple() {
		String methodName = "testDeleteConditionalMultiple";

		myDaoConfig.setAllowMultipleDelete(false);

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
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals("HTTP 412 Precondition Failed: " + Msg.code(962) + "Failed to DELETE resource with match URL \"Patient?identifier=testDeleteConditionalMultiple\" because this search matched 2 resources",
				e.getMessage());
		}

		// Not deleted yet..
		myClient.read().resource("Patient").withId(id1).execute();
		myClient.read().resource("Patient").withId(id2).execute();

		myDaoConfig.setAllowMultipleDelete(true);

		MethodOutcome response = myClient
			.delete()
			.resourceConditionalByType(Patient.class)
			.where(Patient.IDENTIFIER.exactly().code(methodName))
			.execute();

		String encoded = myFhirContext.newXmlParser().encodeResourceToString(response.getOperationOutcome());
		ourLog.info(encoded);
		assertThat(encoded, containsString(
			"<issue><severity value=\"information\"/><code value=\"informational\"/><diagnostics value=\"Successfully deleted 2 resource(s) in "));
		try {
			myClient.read().resource("Patient").withId(id1).execute();
			fail();
		} catch (ResourceGoneException e) {
			// good
		}
		try {
			myClient.read().resource("Patient").withId(id2).execute();
			fail();
		} catch (ResourceGoneException e) {
			// good
		}
	}

	@Test
	public void testDeleteConditionalNoMatches() throws Exception {
		String methodName = "testDeleteConditionalNoMatches";

		HttpDelete delete = new HttpDelete(ourServerBase + "/Patient?identifier=" + methodName);
		try (CloseableHttpResponse resp = ourHttpClient.execute(delete)) {
			ourLog.info(resp.toString());
			String response = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response);
			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertThat(response, containsString(
				"<issue><severity value=\"warning\"/><code value=\"not-found\"/><diagnostics value=\"Unable to find resource matching URL &quot;Patient?identifier=testDeleteConditionalNoMatches&quot;. Deletion failed.\"/></issue>"));
		}

	}

	@Test
	public void testDeleteInvalidReference() throws IOException {
		HttpDelete delete = new HttpDelete(ourServerBase + "/Patient");
		try (CloseableHttpResponse response = ourHttpClient.execute(delete)) {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertEquals(400, response.getStatusLine().getStatusCode());
			assertThat(responseString, containsString("Can not perform delete, no ID provided"));
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
			fail();
		} catch (ResourceGoneException e) {
			// good
		}
	}


	@Test
	@Disabled
	public void testQuery() throws IOException {
		ourLog.info("** Performing Search");
		HttpGet read = new HttpGet(ourServerBase + "/MedicationRequest?category=community&identifier=urn:oid:2.16.840.1.113883.3.7418.12.3%7C&intent=order&medication.code:text=calcitriol,hectorol,Zemplar,rocaltrol,vectical,vitamin%20D,doxercalciferol,paricalcitol&status=active,completed");
		try (CloseableHttpResponse response = ourHttpClient.execute(read)) {
			ourLog.info(response.toString());
		}
		ourLog.info("** DONE Performing Search");

	}

	@Test
	public void testDeleteResourceConditional1() throws IOException {
		String methodName = "testDeleteResourceConditional1";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

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
			assertEquals(200, response.getStatusLine().getStatusCode());
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			OperationOutcome oo = myFhirContext.newXmlParser().parseResource(OperationOutcome.class, resp);
			assertThat(oo.getIssueFirstRep().getDiagnostics(), startsWith("Successfully deleted 1 resource(s) in "));
		} finally {
			response.close();
		}

		HttpGet read = new HttpGet(ourServerBase + "/Patient/" + id.getIdPart());
		response = ourHttpClient.execute(read);
		try {
			ourLog.info(response.toString());
			assertEquals(Constants.STATUS_HTTP_410_GONE, response.getStatusLine().getStatusCode());
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			OperationOutcome oo = myFhirContext.newXmlParser().parseResource(OperationOutcome.class, resp);
			assertThat(oo.getIssueFirstRep().getDiagnostics(), startsWith("Resource was deleted at"));
		} finally {
			response.close();
		}

		// Delete should now have no matches

		delete = new HttpDelete(ourServerBase + "/Patient?name=" + methodName);
		response = ourHttpClient.execute(delete);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			OperationOutcome oo = myFhirContext.newXmlParser().parseResource(OperationOutcome.class, resp);
			assertThat(oo.getIssueFirstRep().getDiagnostics(), startsWith("Unable to find resource matching URL \"Patient?name=testDeleteResourceConditional1\". Deletion failed."));
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
			sock.getOutputStream().write(("DELETE /fhir/context/Patient?identifier=http://ghh.org/patient|" + methodName + " HTTP/1.1\n").getBytes(StandardCharsets.UTF_8));
			sock.getOutputStream().write("Host: localhost\n".getBytes(StandardCharsets.UTF_8));
			sock.getOutputStream().write("\n".getBytes(StandardCharsets.UTF_8));

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

	@Test
	public void testDeleteReturnsOperationOutcome() {
		Patient p = new Patient();
		p.addName().setFamily("FAM");
		IIdType id = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		MethodOutcome resp = myClient.delete().resourceById(id).execute();
		OperationOutcome oo = (OperationOutcome) resp.getOperationOutcome();
		assertThat(oo.getIssueFirstRep().getDiagnostics(), startsWith("Successfully deleted 1 resource(s) in "));
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

		String resBody = IOUtils.toString(ResourceProviderR4Test.class.getResource("/r4/documentmanifest.json"), StandardCharsets.UTF_8);
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

		String resBody = IOUtils.toString(ResourceProviderR4Test.class.getResource("/r4/documentreference.json"), StandardCharsets.UTF_8);
		client.create().resource(resBody).execute();

		int newSize = client.search().forResource(DocumentReference.class).returnBundle(Bundle.class).execute().getEntry().size();

		assertEquals(1, newSize - initialSize);

	}

	@Test
	public void testElements() throws IOException {
		DiagnosticReport dr = new DiagnosticReport();
		dr.setStatus(DiagnosticReport.DiagnosticReportStatus.FINAL);
		dr.getCode().setText("CODE TEXT");
		myClient.create().resource(dr).execute();

		HttpGet get = new HttpGet(ourServerBase + "/DiagnosticReport?_include=DiagnosticReport:result&_elements:exclude=DiagnosticReport&_elements=DiagnosticReport.status,Observation.value,Observation.code,Observation.subject&_pretty=true");
		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(output, not(containsString("<Diagn")));
			ourLog.info(output);
		}
	}

	@Test
	public void testEmptySearch() {
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

		IIdType orgId2 = createOrganization(methodName, "1");

		Device dev = new Device();
		dev.setManufacturer(methodName);
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
		obs.getEncounter().setReferenceElement(encId);
		IIdType obsId = myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();

		ourLog.info("IDs: EncU:" + encUId.getIdPart() + " Enc:" + encId.getIdPart() + "  " + patientId.toUnqualifiedVersionless());

		Parameters output = myClient.operation().onInstance(encId).named("everything").withNoParameters(Parameters.class).execute();
		Bundle b = (Bundle) output.getParameter().get(0).getResource();
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
		IIdType orgId1parent = myClient.update().resource(org1parent).execute().getId().toUnqualifiedVersionless();

		Organization org1 = new Organization();
		org1.setName(methodName + "1");
		org1.getPartOf().setReferenceElement(orgId1parent);
		IIdType orgId1 = myClient.create().resource(org1).execute().getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addName().setFamily(methodName);
		p.getManagingOrganization().setReferenceElement(orgId1);
		IIdType patientId = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		IIdType orgId2 = createOrganization(methodName, "1");

		Device dev = new Device();
		dev.setManufacturer(methodName);
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
		obs.getEncounter().setReferenceElement(encId);
		IIdType obsId = myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();

		Parameters output = myClient.operation().onType(Encounter.class).named("everything").withNoParameters(Parameters.class).execute();
		Bundle b = (Bundle) output.getParameter().get(0).getResource();
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);
		assertThat(ids, containsInAnyOrder(patientId, encUId, encId, orgId1, orgId2, orgId1parent, locPId, locCId, obsId, devId));

		ourLog.info(ids.toString());
	}

	@Test
	public void testEverythingInstanceWithContentFilter() {
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
		StringAndListParam param;

		ourLog.info("Pt1:{} Pt2:{} Obs1:{} Obs2:{} Obs3:{}", ptId1.getIdPart(), ptId2.getIdPart(), obsId1.getIdPart(), obsId2.getIdPart(), obsId3.getIdPart());

		param = new StringAndListParam();
		param.addAnd(new StringOrListParam().addOr(new StringParam("obsvalue1")));

		//@formatter:off
		Parameters response = myClient
			.operation()
			.onInstance(ptId1)
			.named("everything")
			.withParameter(Parameters.class, Constants.PARAM_CONTENT, new StringType("obsvalue1"))
			.execute();
		//@formatter:on

		actual = toUnqualifiedVersionlessIds((Bundle) response.getParameter().get(0).getResource());
		assertThat(actual, containsInAnyOrder(ptId1, obsId1, devId1));

	}

	/**
	 * See #147
	 */
	@Test
	public void testEverythingPatientDoesntRepeatPatient() {
		Bundle b;
		IParser parser = myFhirContext.newJsonParser();
		b = parser.parseResource(Bundle.class, new InputStreamReader(ResourceProviderR4Test.class.getResourceAsStream("/r4/bug147-bundle.json")));

		Bundle resp = myClient.transaction().withBundle(b).execute();
		List<IdType> ids = new ArrayList<>();
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

			ids = new ArrayList<>();
			boolean dupes = false;
			for (BundleEntryComponent next : b.getEntry()) {
				IdType toAdd = next.getResource().getIdElement().toUnqualifiedVersionless();
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
			input.addParameter().setName(Constants.PARAM_COUNT).setValue(new UnsignedIntType(100));
			Parameters output = myClient.operation().onInstance(patientId).named("everything").withParameters(input).execute();
			b = (Bundle) output.getParameter().get(0).getResource();

			ids = new ArrayList<>();
			boolean dupes = false;
			for (BundleEntryComponent next : b.getEntry()) {
				IdType toAdd = next.getResource().getIdElement().toUnqualifiedVersionless();
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

		MedicationRequest mo = new MedicationRequest();
		mo.getSubject().setReferenceElement(patId);
		mo.setMedication(new Reference(medId));
		IIdType moId = myMedicationRequestDao.create(mo, mySrd).getId().toUnqualifiedVersionless();

		Parameters output = myClient.operation().onInstance(patId).named("everything").withNoParameters(Parameters.class).execute();
		Bundle b = (Bundle) output.getParameter().get(0).getResource();
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);
		ourLog.info(ids.toString());
		assertThat(ids, containsInAnyOrder(patId, medId, moId));
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

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));

		IdType patientId = new IdType(resp.getEntry().get(0).getResponse().getLocation());
		assertEquals("Patient", patientId.getResourceType());

		Parameters output = myClient.operation().onInstance(patientId).named("everything").withNoParameters(Parameters.class).execute();
		b = (Bundle) output.getParameter().get(0).getResource();

		List<IdType> ids = new ArrayList<>();
		for (BundleEntryComponent next : b.getEntry()) {
			IdType toAdd = next.getResource().getIdElement().toUnqualifiedVersionless();
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
		IIdType orgId1parent = myClient.update().resource(org1parent).execute().getId().toUnqualifiedVersionless();

		Organization org1 = new Organization();
		org1.setName(methodName + "1");
		org1.getPartOf().setReferenceElement(orgId1parent);
		IIdType orgId1 = myClient.create().resource(org1).execute().getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addName().setFamily(methodName);
		p.getManagingOrganization().setReferenceElement(orgId1);
		IIdType patientId = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		IIdType orgId2 = createOrganization(methodName, "1");

		Device dev = new Device();
		dev.setManufacturer(methodName);
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
		assertThat(ids, containsInAnyOrder(patientId, devId, obsId, encId, orgId1, orgId2, orgId1parent));

		ourLog.info(ids.toString());
	}

	@Test
	public void testEverythingPatientType() {
		String methodName = "testEverythingPatientType";

		IIdType o1Id = createOrganization(methodName, "1");
		IIdType o2Id = createOrganization(methodName, "2");

		IIdType p1Id = createPatientWithIndexAtOrganization(methodName, "1", o1Id);
		IIdType c1Id = createConditionForPatient(methodName, "1", p1Id);

		IIdType p2Id = createPatientWithIndexAtOrganization(methodName, "2", o2Id);
		IIdType c2Id = createConditionForPatient(methodName, "2", p2Id);

		IIdType c3Id = createConditionForPatient(methodName, "3", null);

		Parameters output = myClient.operation().onType(Patient.class).named("everything").withNoParameters(Parameters.class).execute();
		Bundle b = (Bundle) output.getParameter().get(0).getResource();

		assertEquals(BundleType.SEARCHSET, b.getType());
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);

		assertThat(ids, containsInAnyOrder(o1Id, o2Id, p1Id, p2Id, c1Id, c2Id));
		assertThat(ids, not(containsInRelativeOrder(c3Id)));
	}


	@Test
	public void testEverythingPatientTypeWithIdParameter() {
		String methodName = "testEverythingPatientTypeWithIdParameter";

		//Patient 1 stuff.
		IIdType o1Id = createOrganization(methodName, "1");
		IIdType p1Id = createPatientWithIndexAtOrganization(methodName, "1", o1Id);
		IIdType c1Id = createConditionForPatient(methodName, "1", p1Id);

		//Patient 2 stuff.
		IIdType o2Id = createOrganization(methodName, "2");
		IIdType p2Id = createPatientWithIndexAtOrganization(methodName, "2", o2Id);
		IIdType c2Id = createConditionForPatient(methodName, "2", p2Id);

		//Patient 3 stuff.
		IIdType o3Id = createOrganization(methodName, "3");
		IIdType p3Id = createPatientWithIndexAtOrganization(methodName, "3", o3Id);
		IIdType c3Id = createConditionForPatient(methodName, "3", p3Id);

		//Patient 4 stuff.
		IIdType o4Id = createOrganization(methodName, "4");
		IIdType p4Id = createPatientWithIndexAtOrganization(methodName, "4", o4Id);
		IIdType c4Id = createConditionForPatient(methodName, "4", p4Id);

		//No Patient Stuff
		IIdType c5Id = createConditionForPatient(methodName, "4", null);


		{
			//Test for only one patient
			Parameters parameters = new Parameters();
			parameters.addParameter("_id", p1Id.getIdPart());

			Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
			Bundle b = (Bundle) output.getParameter().get(0).getResource();

			assertEquals(BundleType.SEARCHSET, b.getType());
			List<IIdType> ids = toUnqualifiedVersionlessIds(b);

			assertThat(ids, containsInAnyOrder(o1Id, p1Id, c1Id));
			assertThat(ids, not((o2Id)));
			assertThat(ids, not(contains(c2Id)));
			assertThat(ids, not(contains(p2Id)));
		}

		{
			// Test for Patient 1 and 2
			// e.g. _id=1&_id=2
			Parameters parameters = new Parameters();
			parameters.addParameter("_id", p1Id.getIdPart());
			parameters.addParameter("_id", p2Id.getIdPart());

			Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
			Bundle b = (Bundle) output.getParameter().get(0).getResource();

			assertEquals(BundleType.SEARCHSET, b.getType());
			List<IIdType> ids = toUnqualifiedVersionlessIds(b);

			assertThat(ids, containsInAnyOrder(o1Id, p1Id, c1Id, o2Id, c2Id, p2Id));
		}

		{
			// Test for both patients using orList
			// e.g. _id=1,2
			Parameters parameters = new Parameters();
			parameters.addParameter("_id", p1Id.getIdPart() + "," + p2Id.getIdPart());

			Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
			Bundle b = (Bundle) output.getParameter().get(0).getResource();

			assertEquals(BundleType.SEARCHSET, b.getType());
			List<IIdType> ids = toUnqualifiedVersionlessIds(b);

			assertThat(ids, containsInAnyOrder(o1Id, p1Id, c1Id, o2Id, c2Id, p2Id));
			assertThat(ids, not(contains(c5Id)));
		}

		{
			// Test combining 2 or-listed params
			// e.g. _id=1,2&_id=3,4
			Parameters parameters = new Parameters();
			parameters.addParameter("_id", "Patient/" + p1Id.getIdPart() + "," + p2Id.getIdPart());
			parameters.addParameter("_id", p3Id.getIdPart() + "," + p4Id.getIdPart());
			parameters.addParameter(new Parameters.ParametersParameterComponent().setName("_count").setValue(new UnsignedIntType(20)));

			Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
			Bundle b = (Bundle) output.getParameter().get(0).getResource();

			assertEquals(BundleType.SEARCHSET, b.getType());
			List<IIdType> ids = toUnqualifiedVersionlessIds(b);

			assertThat(ids, containsInAnyOrder(o1Id, p1Id, c1Id, o2Id, c2Id, p2Id, p3Id, o3Id, c3Id, p4Id, c4Id, o4Id));
			assertThat(ids, not(contains(c5Id)));
		}

		{
			// Test paging works.
			// There are 12 results, lets make 2 pages of 6.
			Parameters parameters = new Parameters();
			parameters.addParameter("_id", "Patient/" + p1Id.getIdPart() + "," + p2Id.getIdPart());
			parameters.addParameter("_id", p3Id.getIdPart() + "," + p4Id.getIdPart());
			parameters.addParameter(new Parameters.ParametersParameterComponent().setName("_count").setValue(new UnsignedIntType(6)));

			Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
			Bundle bundle = (Bundle) output.getParameter().get(0).getResource();

			String next = bundle.getLink("next").getUrl();
			Bundle nextBundle = myClient.loadPage().byUrl(next).andReturnBundle(Bundle.class).execute();
			assertEquals(BundleType.SEARCHSET, bundle.getType());

			assertThat(bundle.getEntry(), hasSize(6));
			assertThat(nextBundle.getEntry(), hasSize(6));

			List<IIdType> firstBundle = toUnqualifiedVersionlessIds(bundle);
			List<IIdType> secondBundle = toUnqualifiedVersionlessIds(nextBundle);
			List<IIdType> allresults = new ArrayList<>();
			allresults.addAll(firstBundle);
			allresults.addAll(secondBundle);

			assertThat(allresults, containsInAnyOrder(o1Id, p1Id, c1Id, o2Id, c2Id, p2Id, p3Id, o3Id, c3Id, p4Id, c4Id, o4Id));
			assertThat(allresults, not(contains(c5Id)));
		}
	}

	@Test
	public void testEverythingPatientWorksWithForcedId() {
		String methodName = "testEverythingPatientType";

		//Given
		IIdType o1Id = createOrganization(methodName, "1");
		//Patient ABC stuff.
		Patient patientABC = new Patient();
		patientABC.setId("abc");
		patientABC.setManagingOrganization(new Reference(o1Id));
		IIdType pabcId = myPatientDao.update(patientABC).getId().toUnqualifiedVersionless();
		IIdType c1Id = createConditionForPatient(methodName, "1", pabcId);

		//Patient DEF stuff.
		IIdType o2Id = createOrganization(methodName, "2");
		Patient patientDEF = new Patient();
		patientDEF.setId("def");
		patientDEF.setManagingOrganization(new Reference(o2Id));
		IIdType pdefId = myPatientDao.update(patientDEF).getId().toUnqualifiedVersionless();
		IIdType c2Id = createConditionForPatient(methodName, "2", pdefId);

		IIdType c3Id = createConditionForPatient(methodName, "2", null);

		{
			Parameters parameters = new Parameters();
			parameters.addParameter("_id", "Patient/abc,Patient/def");

			//When
			Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
			Bundle b = (Bundle) output.getParameter().get(0).getResource();

			//Then
			assertEquals(BundleType.SEARCHSET, b.getType());
			List<IIdType> ids = toUnqualifiedVersionlessIds(b);
			assertThat(ids, containsInAnyOrder(o1Id, pabcId, c1Id, pdefId, o2Id, c2Id));
			assertThat(ids, not(contains(c3Id)));
		}


	}

	private IIdType createOrganization(String methodName, String s) {
		Organization o1 = new Organization();
		o1.setName(methodName + s);
		return myClient.create().resource(o1).execute().getId().toUnqualifiedVersionless();
	}

	// retest
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

		ourLog.info("Resource IDs:\n * {}\n * {}\n * {}", oId, pId, cId);
		runInTransaction(() -> {
			ourLog.info("Resource Links:\n * {}", myResourceLinkDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
			ourLog.info("Resources:\n * {}", myResourceTableDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
		});

		Thread.sleep(10);
		long time3 = System.currentTimeMillis();

		// %3E=> %3C=<

		myCaptureQueriesListener.clear();
		HttpGet get = new HttpGet(ourServerBase + "/Patient/" + pId.getIdPart() + "/$everything?_lastUpdated=%3E" + new InstantType(new Date(time1)).getValueAsString());
		CloseableHttpResponse response = ourHttpClient.execute(get);
		myCaptureQueriesListener.logSelectQueries();
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			response.getEntity().getContent().close();
			ourLog.info(output);
			List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirContext.newXmlParser().parseResource(Bundle.class, output));
			ourLog.info(ids.toString());
			assertThat(ids, containsInAnyOrder(pId, cId, oId));
		} finally {
			response.close();
		}

		get = new HttpGet(ourServerBase + "/Patient/" + pId.getIdPart() + "/$everything?_lastUpdated=%3E" + new InstantType(new Date(time2)).getValueAsString() + "&_lastUpdated=%3C"
			+ new InstantType(new Date(time3)).getValueAsString());
		response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			response.getEntity().getContent().close();
			ourLog.info(output);
			List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirContext.newXmlParser().parseResource(Bundle.class, output));
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
		// get = new HttpGet(ourServerBase + "/Patient/" + pId.getIdPart() + "/$everything?_lastUpdated=%3E" + new InstantType(new Date(time1)).getValueAsString() + "&_sort=_lastUpdated");
		// response = ourHttpClient.execute(get);
		// try {
		// assertEquals(200, response.getStatusLine().getStatusCode());
		// String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
		// response.getEntity().getContent().close();
		// ourLog.info(output);
		// List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirCtx.newXmlParser().parseResource(Bundle.class, output));
		// ourLog.info(ids.toString());
		// assertThat(ids, contains(pId, cId));
		// } finally {
		// response.close();
		// }
		//
		// get = new HttpGet(ourServerBase + "/Patient/" + pId.getIdPart() + "/$everything?_sort:desc=_lastUpdated");
		// response = ourHttpClient.execute(get);
		// try {
		// assertEquals(200, response.getStatusLine().getStatusCode());
		// String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
		// response.getEntity().getContent().close();
		// ourLog.info(output);
		// List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirCtx.newXmlParser().parseResource(Bundle.class, output));
		// ourLog.info(ids.toString());
		// assertThat(ids, contains(cId, pId, oId));
		// } finally {
		// response.close();
		// }

	}

	/**
	 * Per message from David Hay on Skype
	 */
	@Test
	@Disabled
	public void testEverythingWithLargeSet() throws Exception {

		String inputString = IOUtils.toString(getClass().getResourceAsStream("/david_big_bundle.json"), StandardCharsets.UTF_8);
		Bundle inputBundle = myFhirContext.newJsonParser().parseResource(Bundle.class, inputString);
		inputBundle.setType(BundleType.TRANSACTION);

		assertEquals(53, inputBundle.getEntry().size());

		Set<String> allIds = new TreeSet<>();
		for (BundleEntryComponent nextEntry : inputBundle.getEntry()) {
			nextEntry.getRequest().setMethod(HTTPVerb.PUT);
			nextEntry.getRequest().setUrl(nextEntry.getResource().getId());
			allIds.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		assertEquals(53, allIds.size());

		mySystemDao.transaction(mySrd, inputBundle);

		Bundle responseBundle = myClient
			.operation()
			.onInstance(new IdType("Patient/A161443"))
			.named("everything")
			.withParameter(Parameters.class, "_count", new IntegerType(20))
			.useHttpGet()
			.returnResourceType(Bundle.class)
			.execute();

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(responseBundle));

		List<String> ids = new ArrayList<>();
		for (BundleEntryComponent nextEntry : responseBundle.getEntry()) {
			ids.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}
		Collections.sort(ids);
		ourLog.info("{} ids: {}", ids.size(), ids);

		assertThat(responseBundle.getEntry().size(), lessThanOrEqualTo(25));

		TreeSet<String> idsSet = new TreeSet<>();
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

		assertEquals(null, responseBundle.getLink("next"));

		assertThat(idsSet, hasItem("List/A161444"));
		assertThat(idsSet, hasItem("List/A161468"));
		assertThat(idsSet, hasItem("List/A161500"));

		ourLog.info("Expected {} - {}", allIds.size(), allIds);
		ourLog.info("Actual   {} - {}", idsSet.size(), idsSet);
		assertEquals(allIds, idsSet);

	}

	/**
	 * Per message from David Hay on Skype
	 */
	@Test
	public void testEverythingWithLargeSet2() {
		myDaoConfig.setSearchPreFetchThresholds(Arrays.asList(15, 30, -1));
		myPagingProvider.setDefaultPageSize(500);
		myPagingProvider.setMaximumPageSize(1000);

		Patient p = new Patient();
		p.setActive(true);
		IIdType id = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		for (int i = 1; i < LARGE_NUMBER; i++) {
			Observation obs = new Observation();
			obs.setId("A" + StringUtils.leftPad(Integer.toString(i), 2, '0'));
			obs.setSubject(new Reference(id));
			myClient.update().resource(obs).execute();
		}

		Bundle responseBundle = myClient
			.operation()
			.onInstance(id)
			.named("everything")
			.withParameter(Parameters.class, "_count", new IntegerType(50))
			.useHttpGet()
			.returnResourceType(Bundle.class)
			.execute();

		ArrayList<String> ids = new ArrayList<>();
		for (int i = 0; i < responseBundle.getEntry().size(); i++) {
			BundleEntryComponent nextEntry = responseBundle.getEntry().get(i);
			ids.add(nextEntry.getResource().getIdElement().getIdPart());
		}

		BundleLinkComponent nextLink = responseBundle.getLink("next");
		ourLog.info("Have {} IDs with next link[{}] : {}", ids.size(), nextLink, ids);

		while (nextLink != null) {
			String nextUrl = nextLink.getUrl();
			responseBundle = myClient.fetchResourceFromUrl(Bundle.class, nextUrl);
			for (int i = 0; i < responseBundle.getEntry().size(); i++) {
				BundleEntryComponent nextEntry = responseBundle.getEntry().get(i);
				ids.add(nextEntry.getResource().getIdElement().getIdPart());
			}

			nextLink = responseBundle.getLink("next");
			ourLog.info("Have {} IDs with next link[{}] : {}", ids.size(), nextLink, ids);
		}

		assertThat(ids, hasItem(id.getIdPart()));

		// TODO KHS this fails intermittently with 53 instead of 77
		assertEquals(LARGE_NUMBER, ids.size());
		for (int i = 1; i < LARGE_NUMBER; i++) {
			assertThat(ids.size() + " ids: " + ids, ids, hasItem("A" + StringUtils.leftPad(Integer.toString(i), 2, '0')));
		}
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

		assertEquals(1, response.getEntry().size());
	}

	/**
	 * See #872
	 */
	@Test
	public void testExtensionUrlWithHl7Url() throws IOException {
		String input = IOUtils.toString(ResourceProviderR4Test.class.getResourceAsStream("/bug872-ext-with-hl7-url.json"), Charsets.UTF_8);

		HttpPost post = new HttpPost(ourServerBase + "/Patient/" + JpaConstants.OPERATION_VALIDATE);
		post.setEntity(new StringEntity(input, ContentType.APPLICATION_JSON));

		try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {
			String respString = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(respString);
			assertEquals(200, resp.getStatusLine().getStatusCode());
		}
	}

	/**
	 * See #872
	 */
	@Test
	public void testValidateExtensionUrlWithHl7UrlPost() throws IOException {
		RequestValidatingInterceptor interceptor = new RequestValidatingInterceptor();
		FhirInstanceValidator val = new FhirInstanceValidator(myValidationSupport);
		interceptor.addValidatorModule(val);

		ourRestServer.registerInterceptor(interceptor);
		try {
			String input = IOUtils.toString(ResourceProviderR4Test.class.getResourceAsStream("/bug872-ext-with-hl7-url.json"), Charsets.UTF_8);

			HttpPost post = new HttpPost(ourServerBase + "/Patient/$validate");
			post.setEntity(new StringEntity(input, ContentType.APPLICATION_JSON));

			try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {
				String respString = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
				ourLog.info(respString);
				assertThat(respString, containsString("Unknown extension http://hl7.org/fhir/ValueSet/v3-ActInvoiceGroupCode"));
				assertEquals(200, resp.getStatusLine().getStatusCode());
			}
		} finally {
			ourRestServer.unregisterInterceptor(interceptor);
		}
	}

	@Test
	public void testValidateGeneratedCapabilityStatement() throws IOException {

		String input;
		HttpGet get = new HttpGet(ourServerBase + "/metadata?_format=json");
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			input = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(input);
		}


		HttpPost post = new HttpPost(ourServerBase + "/CapabilityStatement/$validate?_pretty=true");
		post.setEntity(new StringEntity(input, ContentType.APPLICATION_JSON));

		try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {
			String respString = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(respString);
			assertEquals(200, resp.getStatusLine().getStatusCode());
		}
	}


	@Test
	public void testValidateResourceContainingProfileDeclarationDoesntResolve() throws IOException {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		input.getMeta().addProfile("http://foo/structuredefinition/myprofile");

		input.getCode().setText("Hello");
		input.setStatus(ObservationStatus.FINAL);

		HttpPost post = new HttpPost(ourServerBase + "/Observation/$validate?_pretty=true");
		post.setEntity(new ResourceEntity(myFhirContext, input));

		try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {
			String respString = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(respString);
			assertEquals(412, resp.getStatusLine().getStatusCode());
			assertThat(respString, containsString("Profile reference 'http://foo/structuredefinition/myprofile' has not been checked because it is unknown"));
		}
	}


	@SuppressWarnings("unused")
	@Test
	public void testFullTextSearch() throws Exception {
		Observation obs1 = new Observation();
		obs1.getCode().setText("Systolic Blood Pressure");
		obs1.setStatus(ObservationStatus.FINAL);
		obs1.setValue(new Quantity(123));
		IIdType id1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.getCode().setText("Diastolic Blood Pressure");
		obs2.setStatus(ObservationStatus.FINAL);
		obs2.setValue(new Quantity(81));
		IIdType id2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

		HttpGet get = new HttpGet(ourServerBase + "/Observation?_content=systolic&_pretty=true");
		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertThat(responseString, containsString(id1.getIdPart()));
		}
	}

	@Test
	public void testFulltextEverythingWithIdAndContent() throws IOException {
		Patient p = new Patient();
		p.setId("FOO");
		p.addName().setFamily("FAMILY");
		myClient.update().resource(p).execute();

		p = new Patient();
		p.setId("BAR");
		p.addName().setFamily("HELLO");
		myClient.update().resource(p).execute();

		Observation o = new Observation();
		o.setId("BAZ");
		o.getSubject().setReference("Patient/FOO");
		o.getCode().setText("GOODBYE");
		myClient.update().resource(o).execute();

		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(ourServerBase + "/Patient/FOO/$everything?_content=White");
		assertThat(ids, contains("Patient/FOO"));

		ids = searchAndReturnUnqualifiedVersionlessIdValues(ourServerBase + "/Patient/FOO/$everything?_content=HELLO");
		assertThat(ids, contains("Patient/FOO"));

		ids = searchAndReturnUnqualifiedVersionlessIdValues(ourServerBase + "/Patient/FOO/$everything?_content=GOODBYE");
		assertThat(ids, containsInAnyOrder("Patient/FOO", "Observation/BAZ"));
	}

	@Test
	public void testFulltextSearchWithIdAndContent() throws IOException {
		Patient p = new Patient();
		p.setId("FOO");
		p.addName().setFamily("FAMILY");
		myClient.update().resource(p).execute();

		p = new Patient();
		p.setId("BAR");
		p.addName().setFamily("HELLO");
		myClient.update().resource(p).execute();

		Observation o = new Observation();
		o.setId("BAZ");
		o.getSubject().setReference("Patient/FOO");
		o.getCode().setText("GOODBYE");
		myClient.update().resource(o).execute();

		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(ourServerBase + "/Patient?_id=FOO&_content=family");
		assertThat(ids, contains("Patient/FOO"));

		ids = searchAndReturnUnqualifiedVersionlessIdValues(ourServerBase + "/Patient?_id=FOO&_content=HELLO");
		assertThat(ids, empty());
	}

	@Test
	public void testGetResourceCountsOperation() throws Exception {
		String methodName = "testMetaOperations";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		myClient.create().resource(pt).execute().getId().toUnqualifiedVersionless();

		myResourceCountsCache.clear();
		myResourceCountsCache.update();

		HttpGet get = new HttpGet(ourServerBase + "/$get-resource-counts");
		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			response.getEntity().getContent().close();
			ourLog.info(output);
			assertThat(output, containsString("<parameter><name value=\"Patient\"/><valueInteger value=\""));
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

		String uri = ourServerBase + "/Patient?_has:Observation:subject:identifier=" + UrlUtil.escapeUrlParam("urn:system|FOO");
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertThat(ids, contains(pid0.getValue()));
	}

	@Test
	public void testHasParameterNoResults() throws Exception {

		HttpGet get = new HttpGet(ourServerBase + "/AllergyIntolerance?_has=Provenance:target:userID=12345");
		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertThat(resp, containsString("Invalid _has parameter syntax: _has"));
		}

	}

	@Test
	public void testHistoryWithAtParameter() throws Exception {
		String methodName = "testHistoryWithFromAndTo";

		Patient patient = new Patient();
		patient.addName().setFamily(methodName);
		IIdType id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		List<Date> preDates = Lists.newArrayList();
		List<String> ids = Lists.newArrayList();
		for (int i = 0; i < 10; i++) {
			sleepOneClick();
			preDates.add(new Date());
			sleepOneClick();
			patient.setId(id);
			patient.getName().get(0).getFamilyElement().setValue(methodName + "_i" + i);
			ids.add(myPatientDao.update(patient, mySrd).getId().toUnqualified().getValue());
			sleepOneClick();
		}

		List<String> idValues;

		idValues = searchAndReturnUnqualifiedIdValues(ourServerBase + "/Patient/" + id.getIdPart() + "/_history?_at=gt" + toStr(preDates.get(0)) + "&_at=lt" + toStr(preDates.get(3)));
		assertThat(idValues.toString(), idValues, contains(ids.get(2), ids.get(1), ids.get(0)));

		idValues = searchAndReturnUnqualifiedIdValues(ourServerBase + "/Patient/_history?_at=gt" + toStr(preDates.get(0)) + "&_at=lt" + toStr(preDates.get(3)));
		assertThat(idValues.toString(), idValues, contains(ids.get(2), ids.get(1), ids.get(0)));

		idValues = searchAndReturnUnqualifiedIdValues(ourServerBase + "/_history?_at=gt" + toStr(preDates.get(0)) + "&_at=lt" + toStr(preDates.get(3)));
		assertThat(idValues.toString(), idValues, contains(ids.get(2), ids.get(1), ids.get(0)));

		idValues = searchAndReturnUnqualifiedIdValues(ourServerBase + "/_history?_at=gt2060");
		assertThat(idValues.toString(), idValues, empty());

		idValues = searchAndReturnUnqualifiedIdValues(ourServerBase + "/_history?_at=" + InstantDt.withCurrentTime().getYear());
		assertThat(idValues.toString(), idValues, hasSize(10)); // 10 is the page size

		idValues = searchAndReturnUnqualifiedIdValues(ourServerBase + "/_history?_at=ge" + InstantDt.withCurrentTime().getYear());
		assertThat(idValues.toString(), idValues, hasSize(10));

		idValues = searchAndReturnUnqualifiedIdValues(ourServerBase + "/_history?_at=gt" + InstantDt.withCurrentTime().getYear());
		assertThat(idValues, hasSize(0));
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

		ourLog.info("Res ID: {}", id);

		Bundle history = myClient.history().onInstance(id.getValue()).andReturnBundle(Bundle.class).prettyPrint().summaryMode(SummaryEnum.DATA).execute();
		assertEquals(3, history.getEntry().size());
		assertEquals(id.withVersion("3").getValue(), history.getEntry().get(0).getResource().getId());
		assertEquals(1, ((Patient) history.getEntry().get(0).getResource()).getName().size());

		assertEquals(HTTPVerb.DELETE, history.getEntry().get(1).getRequest().getMethodElement().getValue());
		assertEquals("http://localhost:" + ourPort + "/fhir/context/Patient/" + id.getIdPart() + "/_history/2", history.getEntry().get(1).getRequest().getUrl());
		assertEquals(null, history.getEntry().get(1).getResource());

		assertEquals(id.withVersion("1").getValue(), history.getEntry().get(2).getResource().getId());
		assertEquals(1, ((Patient) history.getEntry().get(2).getResource()).getName().size());

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(history));

		try {
			myBundleDao.validate(history, null, null, null, null, null, mySrd);
		} catch (PreconditionFailedException e) {
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
			throw e;
		}
	}

	@Test
	public void testHistoryPaging() {

		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.setActive(true);
		myClient.update().resource(patient).execute();
		patient.setActive(false);
		myClient.update().resource(patient).execute();

		sleepAtLeast(100);
		Date midDate = new Date();
		sleepAtLeast(100);

		patient.setActive(true);
		myClient.update().resource(patient).execute();
		patient.setActive(false);
		myClient.update().resource(patient).execute();
		patient.setActive(true);
		myClient.update().resource(patient).execute();
		patient.setActive(false);
		myClient.update().resource(patient).execute();

		Bundle history = myClient
			.history()
			.onInstance("Patient/A")
			.returnBundle(Bundle.class)
			.prettyPrint()
			.count(2)
			.execute();

		assertThat(toUnqualifiedIdValues(history).toString(), toUnqualifiedIdValues(history), contains(
			"Patient/A/_history/6",
			"Patient/A/_history/5"
		));

		history = myClient
			.loadPage()
			.next(history)
			.execute();

		assertThat(toUnqualifiedIdValues(history).toString(), toUnqualifiedIdValues(history), contains(
			"Patient/A/_history/4",
			"Patient/A/_history/3"
		));

		history = myClient
			.loadPage()
			.next(history)
			.execute();

		assertThat(toUnqualifiedIdValues(history).toString(), toUnqualifiedIdValues(history), contains(
			"Patient/A/_history/2",
			"Patient/A/_history/1"
		));

		history = myClient
			.loadPage()
			.next(history)
			.execute();

		assertEquals(0, history.getEntry().size());

		/*
		 * Try with a date offset
		 */

		history = myClient
			.history()
			.onInstance("Patient/A")
			.returnBundle(Bundle.class)
			.since(midDate)
			.prettyPrint()
			.count(2)
			.execute();

		assertThat(toUnqualifiedIdValues(history).toString(), toUnqualifiedIdValues(history), contains(
			"Patient/A/_history/6",
			"Patient/A/_history/5"
		));

		history = myClient
			.loadPage()
			.next(history)
			.execute();

		assertThat(toUnqualifiedIdValues(history).toString(), toUnqualifiedIdValues(history), contains(
			"Patient/A/_history/4",
			"Patient/A/_history/3"
		));

		history = myClient
			.loadPage()
			.next(history)
			.execute();

		assertEquals(0, history.getEntry().size());

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
	public void testIdAndVersionInBodyForCreate() throws IOException {
		String methodName = "testIdAndVersionInBodyForCreate";

		Patient pt = new Patient();
		pt.setId("Patient/AAA/_history/4");
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(pt);

		ourLog.info("Input: {}", resource);

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		IdType id;
		try {
			String respString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", respString);
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id = new IdType(newIdString);
		} finally {
			response.close();
		}

		assertEquals("1", id.getVersionIdPart());
		assertNotEquals("AAA", id.getIdPart());

		HttpGet get = new HttpGet(ourServerBase + "/Patient/" + id.getIdPart());
		response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String respString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", respString);
			assertThat(respString, containsString("<id value=\"" + id.getIdPart() + "\"/>"));
			assertThat(respString, containsString("<versionId value=\"1\"/>"));
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

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		IdType id;
		try {
			String respString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", respString);
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id = new IdType(newIdString);
		} finally {
			response.close();
		}

		assertEquals("1", id.getVersionIdPart());
		assertNotEquals("AAA", id.getIdPart());

		HttpPut put = new HttpPut(ourServerBase + "/Patient/" + id.getIdPart() + "/_history/1");
		put.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		response = ourHttpClient.execute(put);
		try {
			String respString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", respString);
			assertEquals(400, response.getStatusLine().getStatusCode());
			OperationOutcome oo = myFhirContext.newXmlParser().parseResource(OperationOutcome.class, respString);
			assertEquals(
				Msg.code(420) + "Can not update resource, resource body must contain an ID element which matches the request URL for update (PUT) operation - Resource body ID of \"AAA\" does not match URL ID of \""
					+ id.getIdPart() + "\"",
				oo.getIssue().get(0).getDiagnostics());
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

		String resBody = IOUtils.toString(ResourceProviderR4Test.class.getResource("/r4/imagingstudy.json"), StandardCharsets.UTF_8);
		client.create().resource(resBody).execute();

		int newSize = client.search().forResource(ImagingStudy.class).returnBundle(Bundle.class).execute().getEntry().size();

		assertEquals(1, newSize - initialSize);

	}

	/**
	 * See #793
	 */
	@Test
	public void testIncludeCountDoesntIncludeIncludes() {
		Organization org = new Organization();
		org.setName("ORG");
		IIdType orgId = myOrganizationDao.create(org).getId().toUnqualifiedVersionless();

		for (int i = 0; i < 10; i++) {
			Patient pt = new Patient();
			pt.getManagingOrganization().setReference(orgId.getValue());
			pt.addName().setFamily("FAM" + i);
			myPatientDao.create(pt);
		}

		Bundle bundle = myClient
			.search()
			.forResource(Patient.class)
			.include(Patient.INCLUDE_ORGANIZATION)
			.count(2)
			.returnBundle(Bundle.class)
			.execute();

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertEquals(3, bundle.getEntry().size());
		assertEquals("Patient", bundle.getEntry().get(0).getResource().getIdElement().getResourceType());
		assertEquals("Patient", bundle.getEntry().get(1).getResource().getIdElement().getResourceType());
		assertEquals("Organization", bundle.getEntry().get(2).getResource().getIdElement().getResourceType());
		assertEquals(10, bundle.getTotal());
	}

	@Test
	public void testIncludeWithExternalReferences() {
		myDaoConfig.setAllowExternalReferences(true);

		Patient p = new Patient();
		p.getManagingOrganization().setReference("http://example.com/Organization/123");
		myClient.create().resource(p).execute();

		Bundle b = myClient.search().forResource("Patient").include(Patient.INCLUDE_ORGANIZATION).returnBundle(Bundle.class).execute();
		assertEquals(1, b.getEntry().size());
	}

	@Test
	public void testMetaOperationWithNoMetaParameter() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("testMetaAddInvalid");
		IIdType id = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

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
		pt.addName().setFamily(methodName);
		IIdType id = myClient.create().resource(pt).execute().getId().toUnqualifiedVersionless();

		Meta meta = myClient.meta().get(Meta.class).fromResource(id).execute();
		assertEquals(0, meta.getTag().size());

		Meta inMeta = new Meta();
		inMeta.addTag().setSystem("urn:system1").setCode("urn:code1");
		meta = myClient.meta().add().onResource(id).meta(inMeta).execute();
		assertEquals(1, meta.getTag().size());

		inMeta = new Meta();
		inMeta.addTag().setSystem("urn:system1").setCode("urn:code1");
		meta = myClient.meta().delete().onResource(id).meta(inMeta).execute();
		assertEquals(0, meta.getTag().size());

	}

	@Test
	public void testMetadata() throws Exception {
		HttpGet get = new HttpGet(ourServerBase + "/metadata");
		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp, stringContainsInOrder("THIS IS THE DESC"));
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

		assertEquals(10, response.getEntry().size());
		if (response.getTotalElement().getValueAsString() != null) {
			assertEquals("21", response.getTotalElement().getValueAsString());
		}
		assertThat(response.getLink("next").getUrl(), not(emptyString()));

		// Load page 2

		String nextUrl = response.getLink("next").getUrl();
		response = myClient.fetchResourceFromUrl(Bundle.class, nextUrl);

		assertEquals(10, response.getEntry().size());
		if (response.getTotalElement().getValueAsString() != null) {
			assertEquals("21", response.getTotalElement().getValueAsString());
		}
		assertThat(response.getLink("next").getUrl(), not(emptyString()));

		// Load page 3
		Thread.sleep(2000);

		nextUrl = response.getLink("next").getUrl();
		response = myClient.fetchResourceFromUrl(Bundle.class, nextUrl);

		assertEquals(1, response.getEntry().size());
		assertEquals("21", response.getTotalElement().getValueAsString());
		assertEquals(null, response.getLink("next"));

	}

	@Disabled
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

		Bundle response = myClient
			.operation()
			.onInstance(new IdType(pid))
			.named("everything")
			.withSearchParameter(Parameters.class, "_count", new NumberParam(10))
			.returnResourceType(Bundle.class)
			.useHttpGet()
			.execute();

		assertEquals(10, response.getEntry().size());
		assertEquals(null, response.getTotalElement().getValue());
		assertEquals(null, response.getLink("next"));
	}

	@Test
	public void testParseAndEncodeExtensionWithValueWithExtension() throws IOException {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" +
			"    <extension url=\"https://purl.org/elab/fhir/network/StructureDefinition/1/BirthWeight\">\n" +
			"       <valueDecimal>\n" +
			"          <extension url=\"http://www.hl7.org/fhir/extension-data-absent-reason.html\">\n" +
			"            <valueCoding>\n" +
			"                <system value=\"http://hl7.org/fhir/ValueSet/birthweight\"/>\n" +
			"                <code value=\"Underweight\"/>\n" +
			"                <userSelected value=\"false\"/>\n" +
			"            </valueCoding>\n" +
			"          </extension>\n" +
			"       </valueDecimal>\n" +
			"    </extension>\n" +
			"    <identifier>\n" +
			"       <system value=\"https://purl.org/elab/fhir/network/StructureDefinition/1/EuroPrevallStudySubjects\"/>\n" +
			"       <value value=\"1\"/>\n" +
			"    </identifier>\n" +
			"    <gender value=\"female\"/>\n" +
			"</Patient>";

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(input, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
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

		HttpGet get = new HttpGet(ourServerBase + "/Patient/" + id.getIdPart() + "?_pretty=true");
		response = ourHttpClient.execute(get);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp, containsString("Underweight"));
		} finally {
			response.getEntity().getContent().close();
			response.close();
		}

	}

	@Test
	public void testPatchUsingJsonPatch() throws Exception {
		String methodName = "testPatchUsingJsonPatch";
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		HttpPatch patch = new HttpPatch(ourServerBase + "/Patient/" + pid1.getIdPart());
		patch.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);
		patch.setEntity(new StringEntity("[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]", ContentType.parse(Constants.CT_JSON_PATCH + Constants.CHARSET_UTF8_CTSUFFIX)));

		try (CloseableHttpResponse response = ourHttpClient.execute(patch)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("<OperationOutcome"));
			assertThat(responseString, containsString("INFORMATION"));
		}

		Patient newPt = myClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("2", newPt.getIdElement().getVersionIdPart());
		assertEquals(false, newPt.getActive());
	}

	@Test
	public void testPatchUsingJsonPatchWithContentionCheckBad() throws Exception {
		String methodName = "testPatchUsingJsonPatchWithContentionCheckBad";
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		HttpPatch patch = new HttpPatch(ourServerBase + "/Patient/" + pid1.getIdPart());
		patch.setEntity(new StringEntity("[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]", ContentType.parse(Constants.CT_JSON_PATCH + Constants.CHARSET_UTF8_CTSUFFIX)));
		patch.addHeader("If-Match", "W/\"9\"");

		try (CloseableHttpResponse response = ourHttpClient.execute(patch)) {
			assertEquals(409, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("<OperationOutcome"));
			assertThat(responseString, containsString("<diagnostics value=\"" + Msg.code(550) + Msg.code(974) + "Version 9 is not the most recent version of this resource, unable to apply patch\"/>"));
		}

		Patient newPt = myClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("1", newPt.getIdElement().getVersionIdPart());
		assertEquals(true, newPt.getActive());
	}

	@Test
	public void testPatchUsingJsonPatchWithContentionCheckGood() throws Exception {
		String methodName = "testPatchUsingJsonPatchWithContentionCheckGood";
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		HttpPatch patch = new HttpPatch(ourServerBase + "/Patient/" + pid1.getIdPart());
		patch.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);
		patch.addHeader("If-Match", "W/\"1\"");
		patch.setEntity(new StringEntity("[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]", ContentType.parse(Constants.CT_JSON_PATCH + Constants.CHARSET_UTF8_CTSUFFIX)));

		try (CloseableHttpResponse response = ourHttpClient.execute(patch)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("<OperationOutcome"));
			assertThat(responseString, containsString("INFORMATION"));
		}

		Patient newPt = myClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("2", newPt.getIdElement().getVersionIdPart());
		assertEquals(false, newPt.getActive());
	}

	@Test
	public void testPatchUsingXmlPatch() throws Exception {
		String methodName = "testPatchUsingXmlPatch";
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		HttpPatch patch = new HttpPatch(ourServerBase + "/Patient/" + pid1.getIdPart());
		String patchString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><diff xmlns:fhir=\"http://hl7.org/fhir\"><replace sel=\"fhir:Patient/fhir:active/@value\">false</replace></diff>";
		patch.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);
		patch.setEntity(new StringEntity(patchString, ContentType.parse(Constants.CT_XML_PATCH + Constants.CHARSET_UTF8_CTSUFFIX)));

		try (CloseableHttpResponse response = ourHttpClient.execute(patch)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("<OperationOutcome"));
			assertThat(responseString, containsString("INFORMATION"));
		}

		Patient newPt = myClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("2", newPt.getIdElement().getVersionIdPart());
		assertEquals(false, newPt.getActive());
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

		AuditEvent ae = new org.hl7.fhir.r4.model.AuditEvent();
		ae.addEntity().getWhat().setReference(patientId.toUnqualified().getValue());
		IIdType aeId = myClient.create().resource(ae).execute().getId();
		assertEquals("1", aeId.getVersionIdPart());

		patient = myClient.read().resource(Patient.class).withId(patientId).execute();
		assertTrue(patient.getManagingOrganization().getReferenceElement().hasIdPart());
		assertFalse(patient.getManagingOrganization().getReferenceElement().hasVersionIdPart());

		ae = myClient.read().resource(AuditEvent.class).withId(aeId).execute();
		assertTrue(ae.getEntityFirstRep().getWhat().getReferenceElement().hasIdPart());
		assertTrue(ae.getEntityFirstRep().getWhat().getReferenceElement().hasVersionIdPart());

	}

	/**
	 * See issue #52
	 */
	@Test
	public void testProcedureRequestResources() {
		IGenericClient client = myClient;

		int initialSize = client.search().forResource(ServiceRequest.class).returnBundle(Bundle.class).execute().getEntry().size();

		ServiceRequest res = new ServiceRequest();
		res.addIdentifier().setSystem("urn:foo").setValue("123");

		client.create().resource(res).execute();

		int newSize = client.search().forResource(ServiceRequest.class).returnBundle(Bundle.class).execute().getEntry().size();

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
		myClient.create().resource(pat).prettyPrint().encodedXml().execute();

		pat = new Patient();
		pat.addIdentifier().setSystem("urn:system").setValue("testReadAllInstancesOfType_02");
		myClient.create().resource(pat).prettyPrint().encodedXml().execute();

		{
			Bundle returned = myClient.search().forResource(Patient.class).encodedXml().returnBundle(Bundle.class).execute();
			assertThat(returned.getEntry().size(), greaterThan(1));
			assertEquals(BundleType.SEARCHSET, returned.getType());
		}
		{
			Bundle returned = myClient.search().forResource(Patient.class).encodedJson().returnBundle(Bundle.class).execute();
			assertThat(returned.getEntry().size(), greaterThan(1));
		}
	}

	@Test
	public void testRetrieveMissingVersionsDoesntCrashHistory() {
		Patient p1 = new Patient();
		p1.setActive(true);
		final IIdType id1 = myClient.create().resource(p1).execute().getId();

		Patient p2 = new Patient();
		p2.setActive(false);
		IIdType id2 = myClient.create().resource(p2).execute().getId();

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				ResourceHistoryTable version = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(id1.getIdPartAsLong(), 1);
				myResourceHistoryTableDao.delete(version);
			}
		});

		Bundle bundle = myClient.history().onServer().andReturnBundle(Bundle.class).execute();
		assertEquals(1, bundle.getTotal());
		assertEquals(1, bundle.getEntry().size());
		assertEquals(id2.getIdPart(), bundle.getEntry().get(0).getResource().getIdElement().getIdPart());
	}

	@Test
	public void testRetrieveMissingVersionsDoesntCrashSearch() {
		Patient p1 = new Patient();
		p1.setActive(true);
		final IIdType id1 = myClient.create().resource(p1).execute().getId();

		Patient p2 = new Patient();
		p2.setActive(false);
		IIdType id2 = myClient.create().resource(p2).execute().getId();

		myCaptureQueriesListener.clear();
		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				ResourceHistoryTable version = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(id1.getIdPartAsLong(), 1);
				myResourceHistoryTableDao.delete(version);
			}
		});
		myCaptureQueriesListener.logAllQueriesForCurrentThread();

		Bundle bundle = myClient.search().forResource("Patient").returnBundle(Bundle.class).execute();
		ourLog.info("Result: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
		assertEquals(2, bundle.getTotal());
		assertEquals(1, bundle.getEntry().size());
		assertEquals(id2.getIdPart(), bundle.getEntry().get(0).getResource().getIdElement().getIdPart());
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

		assertThat(encoded, containsString("http://foo"));
	}

	@Test
	public void testSaveAndRetrieveWithContained() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system:rpr4").setValue("testSaveAndRetrieveWithContained01");

		Organization o1 = new Organization();
		o1.addIdentifier().setSystem("urn:system:rpr4").setValue("testSaveAndRetrieveWithContained02");

		p1.getManagingOrganization().setResource(o1);

		IIdType newId = myClient.create().resource(p1).execute().getId();

		Patient actual = myClient.read(Patient.class, new UriDt(newId.getValue()));
		assertEquals(1, actual.getContained().size());

		Bundle b = myClient
			.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system:rpr4", "testSaveAndRetrieveWithContained01"))
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(1, b.getEntry().size());

	}

	@Test
	public void testSaveAndRetrieveWithoutNarrative() {
		Patient p1 = new Patient();
		p1.getText().setDivAsString("<div><td>Identifier</td><td>testSearchByResourceChain01</td></div>");
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByResourceChain01");

		IdType newId = (IdType) myClient.create().resource(p1).execute().getId();

		Patient actual = myClient.read(Patient.class, newId.getIdPart());
		assertThat(actual.getText().getDiv().getValueAsString(), containsString("<td>Identifier</td><td>testSearchByResourceChain01</td>"));
	}

	@Test
	public void testTerminologyWithCompleteCs_Expand() throws Exception {

		CodeSystem cs = new CodeSystem();
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setUrl("http://cs");
		CodeSystem.ConceptDefinitionComponent a = cs.addConcept()
			.setCode("A");
		a.addConcept().setCode("A1");
		a.addConcept().setCode("A2");
		CodeSystem.ConceptDefinitionComponent b = cs.addConcept()
			.setCode("B");
		b.addConcept().setCode("B1");
		b.addConcept().setCode("B2");
		myClient.create().resource(cs).execute();

		ValueSet vs = new ValueSet();
		vs.setUrl("http://vs");
		vs.getCompose()
			.addInclude()
			.setSystem("http://cs")
			.addFilter()
			.setProperty("concept")
			.setOp(ValueSet.FilterOperator.ISA)
			.setValue("A");
		IIdType vsid = myClient.create().resource(vs).execute().getId().toUnqualifiedVersionless();

		HttpGet read = new HttpGet(ourServerBase + "/" + vsid.getValue() + "/$expand");
		try (CloseableHttpResponse response = ourHttpClient.execute(read)) {
			String text = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(text);
			assertEquals(Constants.STATUS_HTTP_200_OK, response.getStatusLine().getStatusCode());
			assertThat(text, containsString("\"A\""));
			assertThat(text, containsString("\"A1\""));
			assertThat(text, not(containsString("\"B\"")));
			assertThat(text, not(containsString("\"B1\"")));
		}


//		HttpGet read = new HttpGet(ourServerBase + "/Observation?patient=P5000000302&_sort:desc=code&code:in=http://fkcfhir.org/fhir/vs/ccdacapddialysisorder");
//		try (CloseableHttpResponse response = ourHttpClient.execute(read)) {
//			String text = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
//			ourLog.info(text);
//			assertEquals(Constants.STATUS_HTTP_200_OK, response.getStatusLine().getStatusCode());
//			assertThat(text, not(containsString("\"text\",\"type\"")));
//		}
	}

	@Test
	public void testEncounterWithReason() {
		Encounter enc = new Encounter();
		enc.addReasonCode()
			.addCoding().setSystem("http://myorg").setCode("hugs").setDisplay("Hugs for better wellness");
		enc.getPeriod().setStartElement(new DateTimeType("2012"));
		IIdType id = myClient.create().resource(enc).execute().getId().toUnqualifiedVersionless();

		enc = myClient.read().resource(Encounter.class).withId(id).execute();
		assertEquals("hugs", enc.getReasonCodeFirstRep().getCodingFirstRep().getCode());
	}


	@Test
	public void testTerminologyWithCompleteCs_SearchForConceptIn() throws Exception {

		CodeSystem cs = new CodeSystem();
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setUrl("http://cs");
		CodeSystem.ConceptDefinitionComponent a = cs.addConcept()
			.setCode("A");
		a.addConcept().setCode("A1");
		a.addConcept().setCode("A2");
		CodeSystem.ConceptDefinitionComponent b = cs.addConcept()
			.setCode("B");
		b.addConcept().setCode("B1");
		b.addConcept().setCode("B2");
		myClient.create().resource(cs).execute();

		ValueSet vs = new ValueSet();
		vs.setUrl("http://vs");
		vs.getCompose()
			.addInclude()
			.setSystem("http://cs")
			.addFilter()
			.setProperty("concept")
			.setOp(ValueSet.FilterOperator.ISA)
			.setValue("A");
		myClient.create().resource(vs).execute().getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("http://cs").setCode("A1");
		obs.setValue(new StringType("OBS1"));
		obs.setStatus(ObservationStatus.FINAL);
		myClient.create().resource(obs).execute();

		Observation obs2 = new Observation();
		obs2.getCode().addCoding().setSystem("http://cs").setCode("B1");
		obs2.setStatus(ObservationStatus.FINAL);
		obs2.setValue(new StringType("OBS2"));
		myClient.create().resource(obs2).execute();

		HttpGet read = new HttpGet(ourServerBase + "/Observation?code:in=http://vs");
		try (CloseableHttpResponse response = ourHttpClient.execute(read)) {
			String text = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(text);
			assertEquals(Constants.STATUS_HTTP_200_OK, response.getStatusLine().getStatusCode());
			assertThat(text, containsString("\"OBS1\""));
			assertThat(text, not(containsString("\"OBS2\"")));
		}


	}

	@Test
	public void testSearchBundleDoesntIncludeTextElement() throws Exception {
		HttpGet read = new HttpGet(ourServerBase + "/Patient?_format=json");
		try (CloseableHttpResponse response = ourHttpClient.execute(read)) {
			String text = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(text);
			assertEquals(Constants.STATUS_HTTP_200_OK, response.getStatusLine().getStatusCode());
			assertThat(text, not(containsString("\"text\",\"type\"")));
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

		String uri = ourServerBase + "/Patient?name=" + UrlUtil.escapeUrlParam("Jernelöv") + "&_count=5&_pretty=true";
		ourLog.info("URI: {}", uri);
		HttpGet get = new HttpGet(uri);
		CloseableHttpResponse resp = ourHttpClient.execute(get);
		try {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			String output = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(output);

			Bundle b = myFhirContext.newXmlParser().parseResource(Bundle.class, output);

			assertEquals("http://localhost:" + ourPort + "/fhir/context/Patient?_count=5&_pretty=true&name=Jernel%C3%B6v", b.getLink("self").getUrl());

			Patient p = (Patient) b.getEntry().get(0).getResource();
			assertEquals("Jernelöv", p.getName().get(0).getFamily());

		} finally {
			resp.getEntity().getContent().close();
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

		Bundle found;

		found = myClient
			.search()
			.forResource(Patient.class)
			.where(IAnyResource.RES_ID.exactly().systemAndValues(null, id1.getIdPart(), id2.getIdPart()))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(toUnqualifiedVersionlessIds(found), containsInAnyOrder(id1, id2));

		found = myClient
			.search()
			.forResource(Patient.class)
			.where(IAnyResource.RES_ID.exactly().systemAndValues(null, Arrays.asList(id1.getIdPart(), id2.getIdPart(), "FOOOOO")))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(toUnqualifiedVersionlessIds(found), containsInAnyOrder(id1, id2));

		found = myClient
			.search()
			.forResource(Patient.class)
			.where(IAnyResource.RES_ID.exactly().systemAndCode(null, id1.getIdPart()))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(toUnqualifiedVersionlessIds(found), containsInAnyOrder(id1));

		found = myClient
			.search()
			.forResource(Patient.class)
			.where(IAnyResource.RES_ID.exactly().codes(id1.getIdPart(), id2.getIdPart()))
			.and(IAnyResource.RES_ID.exactly().code(id1.getIdPart()))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(toUnqualifiedVersionlessIds(found), containsInAnyOrder(id1));

		found = myClient
			.search()
			.forResource(Patient.class)
			.where(IAnyResource.RES_ID.exactly().codes(Arrays.asList(id1.getIdPart(), id2.getIdPart(), "FOOOOO")))
			.and(IAnyResource.RES_ID.exactly().code(id1.getIdPart()))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(toUnqualifiedVersionlessIds(found), containsInAnyOrder(id1));

		found = myClient
			.search()
			.forResource(Patient.class)
			.where(IAnyResource.RES_ID.exactly().codes(id1.getIdPart(), id2.getIdPart(), "FOOO"))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(toUnqualifiedVersionlessIds(found), containsInAnyOrder(id1, id2));

		found = myClient
			.search()
			.forResource(Patient.class)
			.where(IAnyResource.RES_ID.exactly().codes("FOOO"))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(toUnqualifiedVersionlessIds(found), empty());

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
		myClient.create().resource(p2).execute();

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

		assertEquals(1, actual.getEntry().size());
		assertEquals(ourServerBase + "/Patient/" + p1Id.getIdPart(), actual.getEntry().get(0).getFullUrl());
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
		assertEquals(1, actual.getEntry().size());
		assertEquals(p1Id.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());

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

		myCaptureQueriesListener.clear();

		HttpGet get = new HttpGet(ourServerBase + "/Patient?_lastUpdated=lt" + new InstantType(new Date(time1)).getValueAsString());
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			response.getEntity().getContent().close();
			ourLog.info(output);
			List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirContext.newXmlParser().parseResource(Bundle.class, output));
			ourLog.info(ids.toString());
			assertThat(ids, containsInAnyOrder(pid1));
		} finally {
			response.close();
		}

		myCaptureQueriesListener.logSelectQueries();

		get = new HttpGet(ourServerBase + "/Patient?_lastUpdated=gt" + new InstantType(new Date(time1)).getValueAsString());
		response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			response.getEntity().getContent().close();
			ourLog.info(output);
			List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirContext.newXmlParser().parseResource(Bundle.class, output));
			ourLog.info(ids.toString());
			assertThat(ids, containsInAnyOrder(pid2));
		} finally {
			response.close();
		}

	}

	/**
	 * https://chat.fhir.org/#narrow/stream/implementers/topic/Internal.20error.2C.20when.20executing.20the.20following.20query.20on.20HAPI
	 */
	@Test
	public void testSearchByLastUpdatedGe() throws Exception {
		Patient p2 = new Patient();
		p2.setId("P2");
		p2.addName().setFamily("P2");
		myClient.update().resource(p2).execute().getId().toUnqualifiedVersionless();

		Practitioner pract = new Practitioner();
		pract.setId("PRAC");
		pract.addName().setFamily("PRACT");
		myClient.update().resource(pract).execute().getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.setId("E2");
		enc.setStatus(EncounterStatus.ARRIVED);
		enc.setPeriod(new Period().setStart(new Date()).setEnd(new Date()));
		enc.getSubject().setReference("Patient/P2");
		enc.addParticipant().getIndividual().setReference("Practitioner/PRAC");
		myClient.update().resource(enc).execute().getId().toUnqualifiedVersionless();

		HttpGet get = new HttpGet(ourServerBase + "/Encounter?patient=P2&date=ge2017-01-01&_include:recurse=Encounter:practitioner&_lastUpdated=ge2017-11-10");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			response.getEntity().getContent().close();
			ourLog.info(output);
			List<String> ids = toUnqualifiedVersionlessIdValues(myFhirContext.newXmlParser().parseResource(Bundle.class, output));
			ourLog.info(ids.toString());
			assertThat(ids, containsInAnyOrder("Practitioner/PRAC", "Encounter/E2"));
		} finally {
			response.close();
		}

		get = new HttpGet(ourServerBase + "/Encounter?patient=P2&date=ge2017-01-01&_include:recurse=Encounter:practitioner&_lastUpdated=ge2099-11-10");
		response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			response.getEntity().getContent().close();
			ourLog.info(output);
			List<String> ids = toUnqualifiedVersionlessIdValues(myFhirContext.newXmlParser().parseResource(Bundle.class, output));
			ourLog.info(ids.toString());
			assertThat(ids, empty());
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
		Set<String> expectedIds = new HashSet<>();
		expectedIds.add(p1Id.getIdPart());
		expectedIds.add(p2Id.getIdPart());
		Set<String> actualIds = new HashSet<>();
		for (BundleEntryComponent ele : actual.getEntry()) {
			actualIds.add(ele.getResource().getIdElement().getIdPart());
		}
		assertEquals(expectedIds, actualIds, "Expects to retrieve the 2 patients which reference the two different organizations");
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
		assertEquals(1, actual.getEntry().size());
		assertEquals(p1Id.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());

		//@formatter:off
		actual = myClient.search()
			.forResource(Patient.class)
			.where(Patient.ORGANIZATION.hasId(o1id.getValue()))
			.encodedJson().prettyPrint().returnBundle(Bundle.class).execute();
		//@formatter:on
		assertEquals(1, actual.getEntry().size());
		assertEquals(p1Id.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());

	}

	@Test
	public void testSearchInvalidParam() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("0");
		patient.addName().setFamily("testSearchWithMixedParams").addGiven("Joe");
		myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		// should be subject._id
		HttpGet httpPost = new HttpGet(ourServerBase + "/Observation?subject.id=FOO");

		CloseableHttpResponse resp = ourHttpClient.execute(httpPost);
		try {
			String respString = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(respString);
			assertThat(respString, containsString("Invalid parameter chain: subject.id"));
			assertEquals(400, resp.getStatusLine().getStatusCode());
		} finally {
			resp.getEntity().getContent().close();
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
			assertThat(patients, hasItems(id1a, id1b, id2));
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
			assertThat(patients, hasItems(id1a, id1b, id2));
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
			assertThat(patients, hasItems(id2));
			assertThat(patients, not(hasItems(id1a, id1b)));
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
			assertThat(patients.toString(), patients, not(hasItems(id2)));
			assertThat(patients.toString(), patients, (hasItems(id1a, id1b)));
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
		ma.setMedication(new Reference(medId));
		IIdType moId = myMedicationAdministrationDao.create(ma).getId().toUnqualifiedVersionless();

		HttpGet get = new HttpGet(ourServerBase + "/MedicationAdministration?medication.code=04823543");
		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertThat(responseString, containsString(moId.getIdPart()));
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

		HttpGet get = new HttpGet(ourServerBase + "/Observation?value-quantity=gt-15");
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8));

			List<String> ids = toUnqualifiedVersionlessIdValues(bundle);
			assertThat(ids, contains(oid1));
			assertThat(ids, not(contains(oid2)));
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
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
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
			assertEquals(5, bundle.getEntry().size());
			linkNext.add(bundle.getLink("next").getUrl());
		}

		int index = 0;
		for (String nextLink : linkNext) {
			ourLog.info("Fetching index {}", index++);
			Bundle b = myClient.fetchResourceFromUrl(Bundle.class, nextLink);
			assertEquals(5, b.getEntry().size());
		}
	}

	private void testSearchReturnsResults(String search) throws IOException {
		int matches;
		HttpGet get = new HttpGet(ourServerBase + search);
		CloseableHttpResponse response = ourHttpClient.execute(get);
		String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
		response.getEntity().getContent().close();
		ourLog.info(resp);
		Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, resp);
		matches = bundle.getEntry().size();

		assertThat(matches, greaterThan(0));
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
		assertTrue(value.before(after), new InstantDt(value) + " should be before " + new InstantDt(after));
	}

	@Test
	public void testSearchWithNormalizedQuantitySearchSupported() throws Exception {

		myDaoConfig.getModelConfig().setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		IIdType pid0;
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
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new Quantity().setValueElement(new DecimalType(125.12)).setUnit("CM").setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("cm"));

			myObservationDao.create(obs, mySrd);

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new Quantity().setValueElement(new DecimalType(13.45)).setUnit("DM").setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("dm"));

			myObservationDao.create(obs, mySrd);

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new Quantity().setValueElement(new DecimalType(1.45)).setUnit("M").setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("m"));

			myObservationDao.create(obs, mySrd);

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new Quantity().setValueElement(new DecimalType(25)).setUnit("CM").setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("cm"));

			myObservationDao.create(obs, mySrd);

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		// > 1m
		String uri = ourServerBase + "/Observation?code-value-quantity=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-7$gt1|http://unitsofmeasure.org|m");
		ourLog.info("uri = " + uri);
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertEquals(3, ids.size());

		//>= 100cm
		uri = ourServerBase + "/Observation?code-value-quantity=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-7$gt100|http://unitsofmeasure.org|cm");
		ourLog.info("uri = " + uri);
		ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertEquals(3, ids.size());

		//>= 10dm
		uri = ourServerBase + "/Observation?code-value-quantity=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-7$gt10|http://unitsofmeasure.org|dm");
		ourLog.info("uri = " + uri);
		ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertEquals(3, ids.size());
	}

	@Test
	public void testSearchWithNormalizedQuantitySearchSupported_CombineUCUMOrNonUCUM() throws Exception {

		myDaoConfig.getModelConfig().setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		IIdType pid0;
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
			obs.setValue(new Quantity().setValueElement(new DecimalType(1)).setUnit("M").setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("m"));

			myObservationDao.create(obs, mySrd);

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			obs.setValue(new Quantity().setValueElement(new DecimalType(13.45)).setUnit("DM").setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("dm"));

			myObservationDao.create(obs, mySrd);

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			obs.setValue(new Quantity().setValueElement(new DecimalType(1.45)).setUnit("M").setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("m"));

			myObservationDao.create(obs, mySrd);

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			CodeableConcept cc = obs.getCode();
			obs.setValue(new Quantity().setValueElement(new DecimalType(100)).setUnit("CM").setSystem("http://foo").setCode("cm"));

			myObservationDao.create(obs, mySrd);

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		String uri;
		List<String> ids;

		// With non-normalized
		uri = ourServerBase + "/Observation?value-quantity=" + UrlUtil.escapeUrlParam("100|http://unitsofmeasure.org|cm,100|http://foo|cm");
		ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertEquals(1, ids.size());

		// With normalized
		uri = ourServerBase + "/Observation?value-quantity=" + UrlUtil.escapeUrlParam("1|http://unitsofmeasure.org|m,100|http://foo|cm");
		ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertEquals(2, ids.size());
	}


	@Test
	public void testSearchWithNormalizedQuantitySearchSupported_DegreeFahrenheit() throws Exception {

		myDaoConfig.getModelConfig().setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		IIdType pid0;
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
			obs.setValue(new Quantity().setValueElement(new DecimalType(99.82)).setUnit("F").setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("[degF]"));

			myObservationDao.create(obs, mySrd);

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			obs.setValue(new Quantity().setValueElement(new DecimalType(97.6)).setUnit("F").setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("[degF]"));

			myObservationDao.create(obs, mySrd);

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			// missing value
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			CodeableConcept cc = obs.getCode();
			obs.setValue(new Quantity().setUnit("CM").setSystem("http://foo").setCode("cm"));

			myObservationDao.create(obs, mySrd);

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		myCaptureQueriesListener.clear();
		Bundle returnedBundle = myClient
			.search()
			.forResource(Observation.class)
			.where(Observation.VALUE_QUANTITY.withPrefix(ParamPrefixEnum.EQUAL).number("99.82").andUnits("http://unitsofmeasure.org", "[degF]"))
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(1, returnedBundle.getEntry().size());

		//-- check only use original quantity table to search
		String searchSql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, true);
		assertThat(searchSql, containsString("HFJ_SPIDX_QUANTITY t0"));
		assertThat(searchSql, not(containsString("HFJ_SPIDX_QUANTITY_NRML")));
	}

	@Test
	public void testSearchReusesNoParams() {
		List<IBaseResource> resources = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			Organization org = new Organization();
			org.setName("HELLO");
			resources.add(org);
		}
		myClient.transaction().withResources(resources).prettyPrint().encodedXml().execute();

		myDaoConfig.setReuseCachedSearchResultsForMillis(10000L);

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
	public void testSearchReusesBeforeExpiry() {
		List<IBaseResource> resources = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			Organization org = new Organization();
			org.setName("HELLO");
			resources.add(org);
		}
		myClient.transaction().withResources(resources).prettyPrint().encodedXml().execute();


		{
			myDaoConfig.setReuseCachedSearchResultsForMillis(10L);
			Bundle result1 = myClient
				.search()
				.forResource("Organization")
				.returnBundle(Bundle.class)
				.execute();
			final String uuid1 = toSearchUuidFromLinkNext(result1);
			sleepAtLeast(11L);
			Bundle result2 = myClient
				.search()
				.forResource("Organization")
				.returnBundle(Bundle.class)
				.execute();
			final String uuid2 = toSearchUuidFromLinkNext(result2);
			assertNotEquals(uuid1, uuid2);
		}

		{
			myDaoConfig.setReuseCachedSearchResultsForMillis(1000L);
			Bundle result1 = myClient
				.search()
				.forResource("Organization")
				.returnBundle(Bundle.class)
				.execute();
			final String uuid1 = toSearchUuidFromLinkNext(result1);
			runInTransaction(() -> {
				Search search = mySearchEntityDao.findByUuidAndFetchIncludes(uuid1).orElseThrow(() -> new IllegalStateException());
				search.setExpiryOrNull(DateUtils.addSeconds(new Date(), -2));
				mySearchEntityDao.save(search);
			});
			sleepOneClick();
			Bundle result2 = myClient
				.search()
				.forResource("Organization")
				.returnBundle(Bundle.class)
				.execute();

			final String uuid2 = toSearchUuidFromLinkNext(result2);
			assertEquals(uuid1, uuid2);

		}
	}


	@Test
	public void testSearchReusesResultsDisabled() {
		List<IBaseResource> resources = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			Organization org = new Organization();
			org.setName("HELLO");
			resources.add(org);
		}
		myClient.transaction().withResources(resources).prettyPrint().encodedXml().execute();

		myDaoConfig.setReuseCachedSearchResultsForMillis(null);

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

		assertNotEquals(uuid1, uuid2);
		assertNotEquals(uuid1, uuid3);
	}

	@Test
	public void testSearchReusesResultsEnabled() throws Exception {
		List<IBaseResource> resources = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			Organization org = new Organization();
			org.setName("HELLO");
			resources.add(org);
		}
		myClient.transaction().withResources(resources).prettyPrint().encodedXml().execute();

		myDaoConfig.setReuseCachedSearchResultsForMillis(1000L);

		Bundle result1 = myClient
			.search()
			.forResource("Organization")
			.where(Organization.NAME.matches().value("HELLO"))
			.count(5)
			.returnBundle(Bundle.class)
			.execute();

		final String uuid1 = toSearchUuidFromLinkNext(result1);
		Search search1 = newTxTemplate().execute(theStatus -> mySearchEntityDao.findByUuidAndFetchIncludes(uuid1).orElseThrow(() -> new InternalErrorException("")));
		Date created1 = search1.getCreated();

		Bundle result2 = myClient
			.search()
			.forResource("Organization")
			.where(Organization.NAME.matches().value("HELLO"))
			.count(5)
			.returnBundle(Bundle.class)
			.execute();

		final String uuid2 = toSearchUuidFromLinkNext(result2);
		Search search2 = newTxTemplate().execute(theStatus -> mySearchEntityDao.findByUuidAndFetchIncludes(uuid2).orElseThrow(() -> new InternalErrorException("")));
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
		assertNotEquals(uuid1, uuid3);
	}

	@Test
	public void testSearchReusesResultsEnabledNoParams() {
		List<IBaseResource> resources = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			Organization org = new Organization();
			org.setName("HELLO");
			resources.add(org);
		}
		myClient.transaction().withResources(resources).prettyPrint().encodedXml().execute();

		myDaoConfig.setReuseCachedSearchResultsForMillis(100000L);

		Bundle result1 = myClient
			.search()
			.forResource("Organization")
			.returnBundle(Bundle.class)
			.execute();

		final String uuid1 = toSearchUuidFromLinkNext(result1);
		Search search1 = newTxTemplate().execute(theStatus -> mySearchEntityDao.findByUuidAndFetchIncludes(uuid1).orElseThrow(() -> new InternalErrorException("")));
		Date created1 = search1.getCreated();

		sleepOneClick();

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

		Bundle response = myClient
			.search()
			.forResource(Patient.class)
			.returnBundle(Bundle.class)
			.execute();

		patient = (Patient) response.getEntry().get(0).getResource();

		myClient
			.meta()
			.add()
			.onResource(patient.getIdElement())
			.meta(new Meta().addTag("http://system", "tag1", "display"))
			.execute();

		response = myClient
			.search()
			.forResource(Patient.class)
			.returnBundle(Bundle.class)
			.execute();

		patient = (Patient) response.getEntry().get(0).getResource();
		assertEquals(1, patient.getMeta().getTag().size());
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

		Bundle response = myClient
			.search()
			.forResource(Patient.class)
			.where(Patient.IDENTIFIER.hasSystemWithAnyCode("urn:system"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(2, response.getEntry().size());
	}

	@Test
	@Disabled("Not useful with the search coordinator thread pool removed")
	public void testSearchWithCountNotSet() {
		mySearchCoordinatorSvcRaw.setSyncSizeForUnitTests(1);
		mySearchCoordinatorSvcRaw.setLoadingThrottleForUnitTests(200);

		for (int i = 0; i < 10; i++) {
			Patient pat = new Patient();
			pat.addIdentifier().setSystem("urn:system:rpr4").setValue("test" + i);
			myClient.create().resource(pat).execute();
		}

		StopWatch sw = new StopWatch();

		ourLog.info("** About to search with count of 1");

		Bundle found = myClient
			.search()
			.forResource(Patient.class)
			.returnBundle(Bundle.class)
			.count(1)
			.execute();

		ourLog.info("** Done searching in {}ms with count of 1", sw.getMillis());

		ourLog.info(myCapturingInterceptor.getLastResponse().getAllHeaders().toString());
		assertThat(myCapturingInterceptor.getLastResponse().getHeaders(Constants.HEADER_X_CACHE), Matchers.empty());
		assertThat(myCapturingInterceptor.getLastResponse().getHeaders(Constants.HEADER_X_CACHE.toLowerCase()), Matchers.empty());

		// When we've only got one DB connection available, we are forced to wait for the
		// search to finish before returning
		if (TestR4Config.getMaxThreads() > 1) {
			assertEquals(null, found.getTotalElement().getValue());
			assertEquals(1, found.getEntry().size());
			assertThat(sw.getMillis(), lessThan(1000L));
		} else {
			assertThat(sw.getMillis(), greaterThan(1000L));
		}

	}

	@Test
	public void testSearchWithCountSearchResultsUpTo20() {
		mySearchCoordinatorSvcRaw.setSyncSizeForUnitTests(1);
		mySearchCoordinatorSvcRaw.setLoadingThrottleForUnitTests(200);
		myDaoConfig.setCountSearchResultsUpTo(20);

		for (int i = 0; i < 10; i++) {
			Patient pat = new Patient();
			pat.addIdentifier().setSystem("urn:system:rpr4").setValue("test" + i);
			myClient.create().resource(pat).execute();
		}

		StopWatch sw = new StopWatch();

		Bundle found = myClient
			.search()
			.forResource(Patient.class)
			.returnBundle(Bundle.class)
			.count(1)
			.execute();

		assertThat(sw.getMillis(), not(lessThan(1000L)));

		assertEquals(10, found.getTotalElement().getValue().intValue());
		assertEquals(1, found.getEntry().size());

	}

	@Test
	@Disabled("Not useful with the search coordinator thread pool removed")
	public void testSearchWithCountSearchResultsUpTo5() {
		mySearchCoordinatorSvcRaw.setSyncSizeForUnitTests(1);
		mySearchCoordinatorSvcRaw.setLoadingThrottleForUnitTests(200);
		myDaoConfig.setCountSearchResultsUpTo(5);

		for (int i = 0; i < 10; i++) {
			Patient pat = new Patient();
			pat.addIdentifier().setSystem("urn:system:rpr4").setValue("test" + i);
			myClient.create().resource(pat).execute();
		}

		StopWatch sw = new StopWatch();

		Bundle found = myClient
			.search()
			.forResource(Patient.class)
			.returnBundle(Bundle.class)
			.count(1)
			.execute();

		assertThat(myCapturingInterceptor.getLastResponse().getHeaders(Constants.HEADER_X_CACHE), Matchers.empty());
		assertThat(myCapturingInterceptor.getLastResponse().getHeaders(Constants.HEADER_X_CACHE.toLowerCase()), Matchers.empty());

		// WHen we've only got one DB connection available, we are forced to wait for the
		// search to finish before returning
		if (TestR4Config.getMaxThreads() > 1) {
			assertEquals(null, found.getTotalElement().getValue());
			assertEquals(1, found.getEntry().size());
			assertThat(sw.getMillis(), lessThan(1500L));
		} else {
			assertThat(sw.getMillis(), greaterThan(1500L));
		}
	}

	@Test
	public void testSearchWithEmptyParameter() throws Exception {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);

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

	private void testSearchWithEmptyParameter(String url) throws IOException {
		HttpGet get = new HttpGet(ourServerBase + url);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			String respString = IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8);
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, respString);
			assertEquals(1, bundle.getEntry().size());
		}
	}

	@Test
	public void testSearchWithInclude() {
		Organization org = new Organization();
		org.addIdentifier().setSystem("urn:system:rpr4").setValue("testSearchWithInclude01");
		IdType orgId = (IdType) myClient.create().resource(org).prettyPrint().encodedXml().execute().getId();

		Patient pat = new Patient();
		pat.addIdentifier().setSystem("urn:system:rpr4").setValue("testSearchWithInclude02");
		pat.getManagingOrganization().setReferenceElement(orgId.toUnqualifiedVersionless());
		myClient.create().resource(pat).prettyPrint().encodedXml().execute();

		Bundle found = myClient
			.search()
			.forResource(Patient.class)
			.where(Patient.IDENTIFIER.exactly().systemAndIdentifier("urn:system:rpr4", "testSearchWithInclude02"))
			.include(Patient.INCLUDE_ORGANIZATION)
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(2, found.getEntry().size());
		assertEquals(Patient.class, found.getEntry().get(0).getResource().getClass());
		assertEquals(SearchEntryMode.MATCH, found.getEntry().get(0).getSearch().getMode());
		assertEquals(Organization.class, found.getEntry().get(1).getResource().getClass());
		assertEquals(SearchEntryMode.INCLUDE, found.getEntry().get(1).getSearch().getMode());
	}

	@Test
	public void testOffsetSearchWithInclude() {
		Organization org = new Organization();
		org.addIdentifier().setSystem("urn:system:rpr4").setValue("testSearchWithInclude01");
		IdType orgId = (IdType) myClient.create().resource(org).prettyPrint().encodedXml().execute().getId();

		Patient pat = new Patient();
		pat.addIdentifier().setSystem("urn:system:rpr4").setValue("testSearchWithInclude02");
		pat.getManagingOrganization().setReferenceElement(orgId.toUnqualifiedVersionless());
		myClient.create().resource(pat).prettyPrint().encodedXml().execute();

		Bundle found = myClient
			.search()
			.forResource(Patient.class)
			.where(Patient.IDENTIFIER.exactly().systemAndIdentifier("urn:system:rpr4", "testSearchWithInclude02"))
			.include(Patient.INCLUDE_ORGANIZATION)
			.offset(0)
			.count(1)
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(2, found.getEntry().size());
		assertEquals(Patient.class, found.getEntry().get(0).getResource().getClass());
		assertEquals(SearchEntryMode.MATCH, found.getEntry().get(0).getSearch().getMode());
		assertEquals(Organization.class, found.getEntry().get(1).getResource().getClass());
		assertEquals(SearchEntryMode.INCLUDE, found.getEntry().get(1).getSearch().getMode());
	}

	@Test()
	public void testSearchWithInvalidNumberPrefix() {
		try {
			myClient
				.search()
				.forResource(MolecularSequence.class)
				.where(MolecularSequence.VARIANT_END.withPrefix(ParamPrefixEnum.ENDS_BEFORE).number(100))
				.prettyPrint()
				.returnBundle(Bundle.class)
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Unable to handle number prefix \"eb\" for value: eb100"));
		}

		try {
			myClient
				.search()
				.forResource(MolecularSequence.class)
				.where(MolecularSequence.VARIANT_END.withPrefix(ParamPrefixEnum.STARTS_AFTER).number(100))
				.prettyPrint()
				.returnBundle(Bundle.class)
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Unable to handle number prefix \"sa\" for value: sa100"));
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
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Unable to handle quantity prefix \"eb\" for value: eb100||"));
		}
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
			patient.addName().setFamily("Tester").addGiven("Joe");
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			obs.getCode().addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new Quantity().setValue(200));

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			obs.getCode().addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new Quantity().setValue(300));

			oid2 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			obs.getCode().addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new Quantity().setValue(150));

			oid3 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			obs.getCode().addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new Quantity().setValue(250));

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
	public void testSearchWithCompositeSortWith_CompCodeValueQuantity() throws IOException {

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

			ObservationComponentComponent comp = obs.addComponent();
			CodeableConcept cc = new CodeableConcept();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			comp.setCode(cc);
			comp.setValue(new Quantity().setValue(200));

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);

			ObservationComponentComponent comp = obs.addComponent();
			CodeableConcept cc = new CodeableConcept();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			comp.setCode(cc);
			comp.setValue(new Quantity().setValue(300));

			oid2 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);

			ObservationComponentComponent comp = obs.addComponent();
			CodeableConcept cc = new CodeableConcept();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			comp.setCode(cc);
			comp.setValue(new Quantity().setValue(150));

			oid3 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);

			ObservationComponentComponent comp = obs.addComponent();
			CodeableConcept cc = new CodeableConcept();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			comp.setCode(cc);
			comp.setValue(new Quantity().setValue(250));
			oid4 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		String uri = ourServerBase + "/Observation?_sort=combo-code-value-quantity";
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
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);
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
		org.addIdentifier().setSystem("urn:system:rpr4").setValue(methodName + "01");
		org.setName(methodName + "name");
		IIdType orgNotMissing = myClient.create().resource(org).prettyPrint().encodedXml().execute().getId().toUnqualifiedVersionless();

		org = new Organization();
		org.addIdentifier().setSystem("urn:system:rpr4").setValue(methodName + "01");
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
			assertThat("Wanted " + orgNotMissing + " but got " + list.size() + ": " + list, list, containsInRelativeOrder(orgNotMissing));
			assertThat(list, not(containsInRelativeOrder(deletedIdMissingFalse)));
			assertThat(list, not(containsInRelativeOrder(orgMissing)));
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
		assertThat(list, not(containsInRelativeOrder(orgNotMissing)));
		assertThat(list, not(containsInRelativeOrder(deletedIdMissingTrue)));
		assertThat("Wanted " + orgMissing + " but found: " + list, list, containsInRelativeOrder(orgMissing));
	}

	@Test
	public void testSearchWithMissing2() throws Exception {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);
		checkParamMissing(Observation.SP_CODE);
		checkParamMissing(Observation.SP_CATEGORY);
		checkParamMissing(Observation.SP_VALUE_STRING);
		checkParamMissing(Observation.SP_ENCOUNTER);
		checkParamMissing(Observation.SP_DATE);
	}

	@Test
	public void testSearchWithMissingDate2() throws Exception {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);

		MedicationRequest mr1 = new MedicationRequest();
		mr1.addCategory().addCoding().setSystem("urn:medicationroute").setCode("oral");
		mr1.addDosageInstruction().getTiming().addEventElement().setValueAsString("2017-01-01");
		IIdType id1 = myMedicationRequestDao.create(mr1).getId().toUnqualifiedVersionless();

		MedicationRequest mr2 = new MedicationRequest();
		mr2.addCategory().addCoding().setSystem("urn:medicationroute").setCode("oral");
		IIdType id2 = myMedicationRequestDao.create(mr2).getId().toUnqualifiedVersionless();

		HttpGet get = new HttpGet(ourServerBase + "/MedicationRequest?date:missing=false");
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8));

			List<String> ids = toUnqualifiedVersionlessIdValues(bundle);
			assertThat(ids, contains(id1.getValue()));
			assertThat(ids, not(contains(id2.getValue())));
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

		HttpPost httpPost = new HttpPost(ourServerBase + "/Patient/_search?_format=application/xml");
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
			status.getEntity().getContent().close();
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

	@Test
	public void testSelfReferentialInclude() {
		Location loc1 = new Location();
		loc1.setName("loc1");
		IIdType loc1id = myClient.create().resource(loc1).execute().getId().toUnqualifiedVersionless();

		Location loc2 = new Location();
		loc2.setName("loc2");
		IIdType loc2id = myClient.create().resource(loc2).execute().getId().toUnqualifiedVersionless();

		loc1 = new Location();
		loc1.setId(loc1id);
		loc1.setName("loc1");
		loc1.getPartOf().setReference(loc2id.getValue());
		myClient.update().resource(loc1).execute().getId().toUnqualifiedVersionless();

		loc2 = new Location();
		loc2.setId(loc2id);
		loc2.setName("loc2");
		loc2.getPartOf().setReference(loc1id.getValue());
		myClient.update().resource(loc2).execute().getId().toUnqualifiedVersionless();

		IBaseBundle result = myClient
			.search()
			.forResource(Location.class)
			.where(Location.NAME.matches().value("loc1"))
			.include(Location.INCLUDE_PARTOF.asRecursive())
			.execute();
		assertThat(toUnqualifiedVersionlessIdValues(result), contains(loc1id.getValue(), loc2id.getValue()));
	}

	@Test
	public void testSelfReferentialRevInclude() {
		Location loc1 = new Location();
		loc1.setName("loc1");
		IIdType loc1id = myClient.create().resource(loc1).execute().getId().toUnqualifiedVersionless();

		Location loc2 = new Location();
		loc2.setName("loc2");
		IIdType loc2id = myClient.create().resource(loc2).execute().getId().toUnqualifiedVersionless();

		loc1 = new Location();
		loc1.setId(loc1id);
		loc1.setName("loc1");
		loc1.getPartOf().setReference(loc2id.getValue());
		myClient.update().resource(loc1).execute().getId().toUnqualifiedVersionless();

		loc2 = new Location();
		loc2.setId(loc2id);
		loc2.setName("loc2");
		loc2.getPartOf().setReference(loc1id.getValue());
		myClient.update().resource(loc2).execute().getId().toUnqualifiedVersionless();

		IBaseBundle result = myClient
			.search()
			.forResource(Location.class)
			.where(Location.NAME.matches().value("loc1"))
			.revInclude(Location.INCLUDE_PARTOF.asRecursive())
			.execute();
		assertThat(toUnqualifiedVersionlessIdValues(result), contains(loc1id.getValue(), loc2id.getValue()));
	}

	@Test
	public void testSmallResultIncludes() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);

		Patient p = new Patient();
		p.setId("p");
		p.setActive(true);
		myClient.update().resource(p).execute();

		CarePlan cp = new CarePlan();
		cp.setId("cp");
		cp.getSubject().setResource(p);
		cp.addActivity().getDetail().getCode().addCoding().setSystem("FOO").setCode("BAR");
		myClient.update().resource(cp).execute();

		Bundle b = myClient
			.search()
			.forResource(CarePlan.class)
			.where(CarePlan.ACTIVITY_CODE.exactly().systemAndCode("FOO", "BAR"))
			.sort().ascending(CarePlan.SP_ACTIVITY_DATE)
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
		IdType orgId = (IdType) myClient.create().resource(org).prettyPrint().encodedXml().execute().getId();

		// Read back directly from the DAO
		{
			Organization returned = myOrganizationDao.read(orgId, mySrd);
			String val = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(returned);
			ourLog.info(val);
			assertThat(val, containsString("<name value=\"測試醫院\"/>"));
		}
		// Read back through the HTTP API
		{
			Organization returned = myClient.read(Organization.class, orgId.getIdPart());
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
		try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			String output = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(output);
		}
	}

	@Test
	public void testTryToCreateResourceWithReferenceThatDoesntExist() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testTryToCreateResourceWithReferenceThatDoesntExist01");
		p1.addName().setFamily("testTryToCreateResourceWithReferenceThatDoesntExistFamily01").addGiven("testTryToCreateResourceWithReferenceThatDoesntExistGiven01");
		p1.setManagingOrganization(new Reference("Organization/99999999999"));

		try {
			myClient.create().resource(p1).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Organization/99999999999"));
		}

	}

	@Test
	public void testUpdateInvalidReference() throws Exception {
		String methodName = "testUpdateInvalidReference";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPut post = new HttpPut(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertEquals(400, response.getStatusLine().getStatusCode());
			OperationOutcome oo = myFhirContext.newXmlParser().parseResource(OperationOutcome.class, responseString);
			assertThat(oo.getIssue().get(0).getDiagnostics(),
				containsString("Can not update resource, request URL must contain an ID element for update (PUT) operation (it must be of the form [base]/[resource type]/[id])"));
		}
	}

	@Test
	public void testUpdateInvalidReference2() throws Exception {
		String methodName = "testUpdateInvalidReference2";

		Patient pt = new Patient();
		pt.setId("2");
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPut post = new HttpPut(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertThat(responseString, containsString("Can not update resource, request URL must contain an ID element for update (PUT) operation (it must be of the form [base]/[resource type]/[id])"));
			assertThat(responseString, containsString("<OperationOutcome"));
			assertEquals(400, response.getStatusLine().getStatusCode());
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

		HttpPut post = new HttpPut(ourServerBase + "/Patient/FOO");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertThat(responseString, containsString("Can not update resource, request URL must contain an ID element for update (PUT) operation (it must be of the form [base]/[resource type]/[id])"));
			assertThat(responseString, containsString("<OperationOutcome"));
			assertEquals(400, response.getStatusLine().getStatusCode());
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

		HttpPut put = new HttpPut(ourServerBase + "/Patient/" + p1id.getIdPart());
		put.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));
		put.addHeader("Accept", Constants.CT_FHIR_JSON);
		CloseableHttpResponse response = ourHttpClient.execute(put);
		try {
			assertEquals(400, response.getStatusLine().getStatusCode());
			OperationOutcome oo = myFhirContext.newJsonParser().parseResource(OperationOutcome.class, new InputStreamReader(response.getEntity().getContent()));
			assertEquals(
				Msg.code(420) + "Can not update resource, resource body must contain an ID element which matches the request URL for update (PUT) operation - Resource body ID of \"FOO\" does not match URL ID of \""
					+ p1id.getIdPart() + "\"",
				oo.getIssue().get(0).getDiagnostics());
		} finally {
			response.close();
		}

		// Try to update with the no ID in the resource body
		p1.setId((String) null);

		encoded = myFhirContext.newJsonParser().encodeResourceToString(p1);
		put = new HttpPut(ourServerBase + "/Patient/" + p1id.getIdPart());
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
		put = new HttpPut(ourServerBase + "/Patient/" + p1id.getIdPart());
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
			fail();
		} catch (UnprocessableEntityException e) {
			// good
		}

		try {
			myClient.update().resource(p2).withId("Patient/" + p1id.getIdPart()).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			// good
		}

	}

	@Test
	public void testUpdateResourceConditional() throws IOException {
		String methodName = "testUpdateResourceConditional";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

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

		pt.addAddress().addLine("AAAAAAAAAAAAAAAAAAA");
		resource = myFhirContext.newXmlParser().encodeResourceToString(pt);
		HttpPut put = new HttpPut(ourServerBase + "/Patient?name=" + methodName);
		put.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		response = ourHttpClient.execute(put);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			IdType newId = new IdType(response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC).getValue());
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

		pt.addName().setFamily("FOO");
		resource = myFhirContext.newXmlParser().encodeResourceToString(pt);
		HttpPut put = new HttpPut(ourServerBase + "/Patient?identifier=" + ("http://general-hospital.co.uk/Identifiers|09832345234543876876".replace("|", UrlUtil.escapeUrlParam("|"))));
		put.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		IdType id2;
		response = ourHttpClient.execute(put);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC).getValue();
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
	public void testUpdateResourceWithPrefer() throws Exception {
		String methodName = "testUpdateResourceWithPrefer";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

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

		pt = new Patient();
		pt.setId(id.getIdPart());
		resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPut put = new HttpPut(ourServerBase + "/Patient/" + id.getIdPart());
		put.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_REPRESENTATION);
		put.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		response = ourHttpClient.execute(put);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			response.getEntity().getContent().close();

			Patient respPt = myFhirContext.newXmlParser().parseResource(Patient.class, responseString);
			assertEquals("2", respPt.getIdElement().getVersionIdPart());

			InstantType updateTime = respPt.getMeta().getLastUpdatedElement();
			assertTrue(updateTime.getValue().after(before));

		} finally {
			response.close();
		}

	}

	@Test
	public void testUpdateThatCreatesReturnsHttp201() throws IOException {

		Patient p = new Patient();
		p.setId("A");
		p.setActive(true);
		String encoded = myFhirContext.newJsonParser().encodeResourceToString(p);

		HttpPut put = new HttpPut(ourServerBase + "/Patient/A");
		put.setEntity(new StringEntity(encoded, ContentType.create("application/fhir+json", "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(put);
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
		} finally {
			response.close();
		}

		p = new Patient();
		p.setId("A");
		p.setActive(false);
		encoded = myFhirContext.newJsonParser().encodeResourceToString(p);

		put = new HttpPut(ourServerBase + "/Patient/A");
		put.setEntity(new StringEntity(encoded, ContentType.create("application/fhir+json", "UTF-8")));

		response = ourHttpClient.execute(put);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
		} finally {
			response.close();
		}

	}

	@Test
	public void testDeleteNonExistentResourceReturns200() throws IOException {
		HttpDelete delete = new HttpDelete(ourServerBase + "/Patient/someIdHereThatIsUnique");

		try (CloseableHttpResponse response = ourHttpClient.execute(delete)) {
			Assertions.assertEquals(200, response.getStatusLine().getStatusCode());
		}
	}

	@Test
	public void testDeleteSameResourceTwice() throws IOException {
		String id = "mySecondUniqueId";

		Patient p = new Patient();
		p.setId(id);
		String encoded = myFhirContext.newJsonParser().encodeResourceToString(p);

		HttpPut put = new HttpPut(ourServerBase + "/Patient/" + id);
		put.setEntity(new StringEntity(encoded, ContentType.create("application/fhir+json", "UTF-8")));
		try (CloseableHttpResponse response = ourHttpClient.execute(put)) {
			assertEquals(201, response.getStatusLine().getStatusCode());
		}

		HttpDelete delete = new HttpDelete(ourServerBase + "/Patient/" + id);

		for (int i = 0; i < 2; i++) {
			// multiple deletes of the same resource
			// should always succeed
			try (CloseableHttpResponse response = ourHttpClient.execute(delete)) {
				Assertions.assertEquals(200, response.getStatusLine().getStatusCode());
			}
		}
	}

	@Test
	public void testUpdateWithClientSuppliedIdWhichDoesntExist() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateWithClientSuppliedIdWhichDoesntExistrpr4");

		MethodOutcome outcome = myClient.update().resource(p1).withId("testUpdateWithClientSuppliedIdWhichDoesntExistrpr4").execute();
		assertEquals(true, outcome.getCreated().booleanValue());
		IdType p1Id = (IdType) outcome.getId();

		assertThat(p1Id.getValue(), containsString("Patient/testUpdateWithClientSuppliedIdWhichDoesntExistrpr4/_history"));

		//@formatter:off
		Bundle actual = myClient
			.search()
			.forResource(Patient.class)
			.where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system", "testUpdateWithClientSuppliedIdWhichDoesntExistrpr4"))
			.encodedJson()
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertEquals(1, actual.getEntry().size());
		assertEquals(p1Id.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());

	}

	@Test
	public void testUpdateWithSource() {
		Patient patient = new Patient();
		patient.setActive(false);
		IIdType patientid = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();

		{
			Patient readPatient = (Patient) myClient.read().resource("Patient").withId(patientid).execute();
			assertThat(readPatient.getMeta().getSource(), matchesPattern("#[a-zA-Z0-9]+"));
		}

		patient.setId(patientid);
		patient.setActive(true);
		myClient.update().resource(patient).execute();
		{
			Patient readPatient = (Patient) myClient.read().resource("Patient").withId(patientid).execute();
			assertThat(readPatient.getMeta().getSource(), matchesPattern("#[a-zA-Z0-9]+"));

			readPatient.addName().setFamily("testUpdateWithSource");
			myClient.update().resource(readPatient).execute();
			readPatient = (Patient) myClient.read().resource("Patient").withId(patientid).execute();
			assertThat(readPatient.getMeta().getSource(), matchesPattern("#[a-zA-Z0-9]+"));
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

		HttpPut put = new HttpPut(ourServerBase + "/Patient/" + id.getIdPart());
		put.addHeader(Constants.HEADER_IF_MATCH, "W/\"44\"");
		put.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(put);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertEquals(409, response.getStatusLine().getStatusCode());
			OperationOutcome oo = myFhirContext.newXmlParser().parseResource(OperationOutcome.class, responseString);
			assertThat(oo.getIssue().get(0).getDiagnostics(), containsString("Trying to update Patient/" + id.getIdPart() + "/_history/44 but this is not the current version"));
		} finally {
			response.close();
		}

		// Now a good one
		put = new HttpPut(ourServerBase + "/Patient/" + id.getIdPart());
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

		HttpPut post = new HttpPut(ourServerBase + "/Patient/A2");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertEquals(400, response.getStatusLine().getStatusCode());
			OperationOutcome oo = myFhirContext.newXmlParser().parseResource(OperationOutcome.class, responseString);
			assertEquals(
				Msg.code(420) + "Can not update resource, resource body must contain an ID element which matches the request URL for update (PUT) operation - Resource body ID of \"333\" does not match URL ID of \"A2\"",
				oo.getIssue().get(0).getDiagnostics());
		}
	}

	@Test
	public void testUpdateWrongResourceType() throws IOException {
		String input = IOUtils.toString(getClass().getResourceAsStream("/dstu3-person.json"), StandardCharsets.UTF_8);

		try {
			myClient.update().resource(input).withId("Patient/PERSON1").execute();
		} catch (InvalidRequestException e) {
			assertEquals("", e.getMessage());
		}

		MethodOutcome resp = myClient.update().resource(input).withId("Person/PERSON1").execute();
		assertEquals("Person/PERSON1/_history/1", resp.getId().toUnqualified().getValue());
	}

	@Test
	public void testValidateBadInputViaGet() throws IOException {

		HttpGet get = new HttpGet(ourServerBase + "/Patient/$validate?mode=create");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertThat(resp, containsString("No resource supplied for $validate operation (resource is required unless mode is &quot;delete&quot;)"));
			assertEquals(400, response.getStatusLine().getStatusCode());
		} finally {
			response.getEntity().getContent().close();
			response.close();
		}
	}

	@Test
	public void testValidateBadInputViaPost() throws IOException {

		Parameters input = new Parameters();
		input.addParameter().setName("mode").setValue(new CodeType("create"));

		String inputStr = myFhirContext.newXmlParser().encodeResourceToString(input);
		ourLog.info(inputStr);

		HttpPost post = new HttpPost(ourServerBase + "/Patient/$validate");
		post.setEntity(new StringEntity(inputStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertThat(resp, containsString("No resource supplied for $validate operation (resource is required unless mode is &quot;delete&quot;)"));
			assertEquals(400, response.getStatusLine().getStatusCode());
		} finally {
			response.getEntity().getContent().close();
			response.close();
		}
	}

	@Test
	public void testValidateResourceBaseWithNoIdRaw() throws IOException {

		Patient patient = new Patient();
		patient.addName().addGiven("James");
		patient.setBirthDateElement(new DateType("2011-02-02"));

		String inputStr = myFhirContext.newXmlParser().encodeResourceToString(patient);
		HttpPost post = new HttpPost(ourServerBase + "/Patient/$validate");
		post.setEntity(new StringEntity(inputStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp, not(containsString("Resource has no id")));
		}
	}

	/**
	 * Make sure we don't filter keys
	 */
	@Test
	public void testValidateJsonWithDuplicateKey() throws IOException {

		String inputStr = "{\"resourceType\":\"Patient\", \"name\":[{\"text\":\"foo\"}], \"name\":[{\"text\":\"foo\"}] }";
		HttpPost post = new HttpPost(ourServerBase + "/Patient/$validate");
		post.setEntity(new StringEntity(inputStr, ContentType.create(Constants.CT_FHIR_JSON_NEW, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(412, response.getStatusLine().getStatusCode());

			assertThat(resp, stringContainsInOrder("Duplicated property name: name"));
		} finally {
			response.getEntity().getContent().close();
			response.close();
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

		HttpPost post = new HttpPost(ourServerBase + "/Patient/$validate");
		post.setEntity(new StringEntity(inputStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
		} finally {
			response.getEntity().getContent().close();
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

		HttpGet get = new HttpGet(ourServerBase + "/Patient/" + id.getIdPart() + "/$validate");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(412, response.getStatusLine().getStatusCode());
			assertThat(resp, containsString("SHALL at least contain a contact's details or a reference to an organization"));
		} finally {
			response.getEntity().getContent().close();
			response.close();
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

		HttpPost post = new HttpPost(ourServerBase + "/Patient/A123/$validate");
		post.setEntity(new StringEntity(inputStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
		} finally {
			response.getEntity().getContent().close();
			response.close();
		}
	}

	@Test
	public void testValidateResourceWithNoIdParameters() throws IOException {

		Patient patient = new Patient();
		patient.addName().addGiven("James");
		patient.setBirthDateElement(new DateType("2011-02-02"));

		Parameters input = new Parameters();
		input.addParameter().setName("resource").setResource(patient);

		String inputStr = myFhirContext.newXmlParser().encodeResourceToString(input);
		HttpPost post = new HttpPost(ourServerBase + "/Patient/$validate?_pretty=true");
		post.setEntity(new StringEntity(inputStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp, not(containsString("Resource has no id")));
			assertThat(resp, not(containsString("warn")));
			assertThat(resp, not(containsString("error")));
		} finally {
			response.getEntity().getContent().close();
			response.close();
		}
	}

	@Test
	public void testValidateResourceWithNoIdRaw() throws IOException {

		Patient patient = new Patient();
		patient.addName().addGiven("James");
		patient.setBirthDateElement(new DateType("2011-02-02"));

		String inputStr = myFhirContext.newXmlParser().encodeResourceToString(patient);
		HttpPost post = new HttpPost(ourServerBase + "/Patient/$validate");
		post.setEntity(new StringEntity(inputStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp, not(containsString("Resource has no id")));
			assertThat(resp, containsString("<pre>No issues detected during validation</pre>"));
			assertThat(resp,
				stringContainsInOrder("<issue>", "<severity value=\"information\"/>", "<code value=\"informational\"/>", "<diagnostics value=\"No issues detected during validation\"/>",
					"</issue>"));
		} finally {
			response.getEntity().getContent().close();
			response.close();
		}
	}

	@Test
	public void testValueSetExpandOperation() throws IOException {
		CodeSystem cs = myFhirContext.newXmlParser().parseResource(CodeSystem.class, new InputStreamReader(ResourceProviderR4Test.class.getResourceAsStream("/extensional-case-3-cs.xml")));
		myClient.create().resource(cs).execute();

		ValueSet upload = myFhirContext.newXmlParser().parseResource(ValueSet.class, new InputStreamReader(ResourceProviderR4Test.class.getResourceAsStream("/extensional-case-3-vs.xml")));
		IIdType vsid = myClient.create().resource(upload).execute().getId().toUnqualifiedVersionless();

		HttpGet get = new HttpGet(ourServerBase + "/ValueSet/" + vsid.getIdPart() + "/$expand");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp, containsString("<ValueSet xmlns=\"http://hl7.org/fhir\">"));
			assertThat(resp, containsString("<expansion>"));
			assertThat(resp, containsString("<contains>"));
			assertThat(resp, containsString("<system value=\"http://acme.org\"/>"));
			assertThat(resp, containsString("<code value=\"8450-9\"/>"));
			assertThat(resp, containsString("<display value=\"Systolic blood pressure--expiration\"/>"));
			assertThat(resp, containsString("</contains>"));
			assertThat(resp, containsString("<contains>"));
			assertThat(resp, containsString("<system value=\"http://acme.org\"/>"));
			assertThat(resp, containsString("<code value=\"11378-7\"/>"));
			assertThat(resp, containsString("<display value=\"Systolic blood pressure at First encounter\"/>"));
			assertThat(resp, containsString("</contains>"));
			assertThat(resp, containsString("</expansion>"));
		} finally {
			response.getEntity().getContent().close();
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
			response.getEntity().getContent().close();
			response.close();
		}

	}

	@Test
	public void testUpdateWithNormalizedQuantitySearchSupported() throws Exception {

		myDaoConfig.getModelConfig().setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
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
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new Quantity().setValueElement(new DecimalType(125.12)).setUnit("CM").setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("cm"));

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

			IIdType opid1 = myObservationDao.create(obs, mySrd).getId();

			//-- update quantity
			obs = new Observation();
			obs.setId(opid1);
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new Quantity().setValueElement(new DecimalType(24.12)).setUnit("CM").setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("cm"));

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

			myObservationDao.update(obs, mySrd);
		}


		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new Quantity().setValueElement(new DecimalType(13.45)).setUnit("DM").setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("dm"));

			myObservationDao.create(obs, mySrd);

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new Quantity().setValueElement(new DecimalType(1.45)).setUnit("M").setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("m"));

			myObservationDao.create(obs, mySrd);

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new Quantity().setValueElement(new DecimalType(25)).setUnit("CM").setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("cm"));

			myObservationDao.create(obs, mySrd);

			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		// > 1m
		String uri = ourServerBase + "/Observation?code-value-quantity=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-7$gt1|http://unitsofmeasure.org|m");
		ourLog.info("uri = " + uri);
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertEquals(2, ids.size());


		//>= 100cm
		uri = ourServerBase + "/Observation?code-value-quantity=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-7$gt100|http://unitsofmeasure.org|cm");
		ourLog.info("uri = " + uri);
		ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertEquals(2, ids.size());

		//>= 10dm
		uri = ourServerBase + "/Observation?code-value-quantity=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-7$gt10|http://unitsofmeasure.org|dm");
		ourLog.info("uri = " + uri);
		ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertEquals(2, ids.size());
	}

	@Test
	public void testSearchWithLowerBoundDate() throws Exception {

		// Issue 2424 test case
		IIdType pid0;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			patient.setBirthDateElement(new DateType("2073"));
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("Patient: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

			System.out.println("pid0 " + pid0);
		}

		String uri = ourServerBase + "/Patient?_total=accurate&birthdate=gt2072";

		List<String> ids;
		HttpGet get = new HttpGet(uri);

		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, resp);
			ids = toUnqualifiedVersionlessIdValues(bundle);
			ourLog.info("Patient: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
		}

		uri = ourServerBase + "/Patient?_total=accurate&birthdate=gt2072-01-01";

		get = new HttpGet(uri);

		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, resp);
			ids = toUnqualifiedVersionlessIdValues(bundle);
			ourLog.info("Patient: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
		}

	}

	private String toStr(Date theDate) {
		return new InstantDt(theDate).getValueAsString();
	}


	public IIdType createPatientWithIndexAtOrganization(String theMethodName, String theIndex, IIdType theOrganizationId) {
		Patient p1 = new Patient();
		p1.addName().setFamily(theMethodName + theIndex);
		p1.getManagingOrganization().setReferenceElement(theOrganizationId);
		IIdType p1Id = myClient.create().resource(p1).execute().getId().toUnqualifiedVersionless();
		return p1Id;
	}

	public IIdType createConditionForPatient(String theMethodName, String theIndex, IIdType thePatientId) {
		Condition c = new Condition();
		c.addIdentifier().setValue(theMethodName + theIndex);
		if (thePatientId != null) {
			c.getSubject().setReferenceElement(thePatientId);
		}
		IIdType cId = myClient.create().resource(c).execute().getId().toUnqualifiedVersionless();
		return cId;
	}

}
