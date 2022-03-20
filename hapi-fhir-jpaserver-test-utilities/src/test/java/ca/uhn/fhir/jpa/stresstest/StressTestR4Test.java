package ca.uhn.fhir.jpa.stresstest;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.test.utilities.BatchJobHelper;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hamcrest.Matchers;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ListResource;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.AopTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestPropertySource(properties = {
	"max_db_connections=10"
})
@DirtiesContext
@Disabled
public class StressTestR4Test extends BaseResourceProviderR4Test {

	static {
		TestR4Config.ourMaxThreads = 10;
	}

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(StressTestR4Test.class);
	private RequestValidatingInterceptor myRequestValidatingInterceptor;
	@Autowired
	private DatabaseBackedPagingProvider myPagingProvider;
	private int myPreviousMaxPageSize;
	@Autowired
	private BatchJobHelper myBatchJobHelper;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		ourRestServer.unregisterInterceptor(myRequestValidatingInterceptor);
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);

		myPagingProvider.setMaximumPageSize(myPreviousMaxPageSize);

		SearchCoordinatorSvcImpl searchCoordinator = AopTestUtils.getTargetObject(mySearchCoordinatorSvc);
		searchCoordinator.setLoadingThrottleForUnitTests(null);
		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());

	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myRequestValidatingInterceptor = new RequestValidatingInterceptor();
		FhirInstanceValidator module = new FhirInstanceValidator(myFhirContext);
		module.setValidationSupport(myValidationSupport);
		myRequestValidatingInterceptor.addValidatorModule(module);

		myPreviousMaxPageSize = myPagingProvider.getMaximumPageSize();
		myPagingProvider.setMaximumPageSize(300);
	}

	@Autowired
	private ISearchCoordinatorSvc mySearchCoordinatorSvc;


	@Test
	public void testNoDuplicatesInSearchResults() throws Exception {
		int resourceCount = 1000;
		int queryCount = 30;
		myDaoConfig.setSearchPreFetchThresholds(Lists.newArrayList(50, 200, -1));

		SearchCoordinatorSvcImpl searchCoordinator = AopTestUtils.getTargetObject(mySearchCoordinatorSvc);
		searchCoordinator.setLoadingThrottleForUnitTests(10);

		Bundle bundle = new Bundle();

		for (int i = 0; i < resourceCount; i++) {
			Observation o = new Observation();
			o.setId("A" + leftPad(Integer.toString(i), 4, '0'));
			o.setEffective( DateTimeType.now());
			o.setStatus(Observation.ObservationStatus.FINAL);
			bundle.addEntry().setFullUrl(o.getId()).setResource(o).getRequest().setMethod(HTTPVerb.PUT).setUrl("Observation/A" + i);
		}
		StopWatch sw = new StopWatch();
		ourLog.info("Saving {} resources", bundle.getEntry().size());
		mySystemDao.transaction(null, bundle);
		ourLog.info("Saved {} resources in {}", bundle.getEntry().size(), sw.toString());

		Map<String, IBaseResource> ids = new HashMap<>();

		IGenericClient fhirClient = this.myClient;

		String url = ourServerBase + "/Observation?date=gt2000&_sort=-_lastUpdated";

		int pageIndex = 0;
		ourLog.info("Loading page {}", pageIndex);
		Bundle searchResult = fhirClient
			.search()
			.byUrl(url)
			.count(queryCount)
			.returnBundle(Bundle.class)
			.execute();
		while(true) {
			List<String> passIds = searchResult
				.getEntry()
				.stream()
				.map(t -> t.getResource().getIdElement().getValue())
				.collect(Collectors.toList());

			int index = 0;
			for (String nextId : passIds) {
				Resource nextResource = searchResult.getEntry().get(index).getResource();

				if (ids.containsKey(nextId)) {
					String previousContent = fhirClient.getFhirContext().newJsonParser().encodeResourceToString(ids.get(nextId));
					String newContent = fhirClient.getFhirContext().newJsonParser().encodeResourceToString(nextResource);
					throw new Exception("Duplicate ID " + nextId + " found at index " + index + " of page " + pageIndex + "\n\nPrevious: " + previousContent + "\n\nNew: " + newContent);
				}
				ids.put(nextId, nextResource);
				index++;
			}

			if (searchResult.getLink(Constants.LINK_NEXT) == null) {
				break;
			} else {
				if (searchResult.getEntry().size() != queryCount) {
					throw new Exception("Page had " + searchResult.getEntry().size() + " resources");
				}
				if (passIds.size() != queryCount) {
					throw new Exception("Page had " + passIds.size() + " unique ids");
				}
			}

			pageIndex++;
			ourLog.info("Loading page {}: {}", pageIndex, searchResult.getLink(Constants.LINK_NEXT).getUrl());
			searchResult = fhirClient.loadPage().next(searchResult).execute();
		}

		assertEquals(resourceCount, ids.size());
	}


	@Test
	public void testPageThroughLotsOfPages() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);

		/*
		 * This test creates a really huge number of resources to make sure that even large scale
		 * searches work correctly. 5000 is arbitrary, this test was intended to demonstrate an
		 * issue that occurred with 1600 resources but I'm using a huge number here just to
		 * hopefully catch future issues.
		 */
		int count = 5000;


		Bundle bundle = new Bundle();

		DiagnosticReport dr = new DiagnosticReport();
		dr.setId(IdType.newRandomUuid());
		bundle.addEntry().setFullUrl(dr.getId()).setResource(dr).getRequest().setMethod(HTTPVerb.POST).setUrl("DiagnosticReport");
		for (int i = 0; i < count; i++) {
			Observation o = new Observation();
			o.setId("A" + leftPad(Integer.toString(i), 4, '0'));
			o.setStatus(Observation.ObservationStatus.FINAL);
			bundle.addEntry().setFullUrl(o.getId()).setResource(o).getRequest().setMethod(HTTPVerb.PUT).setUrl("Observation/A" + i);
		}
		StopWatch sw = new StopWatch();
		ourLog.info("Saving {} resources", bundle.getEntry().size());
		mySystemDao.transaction(null, bundle);
		ourLog.info("Saved {} resources in {}", bundle.getEntry().size(), sw.toString());

		// Load from DAOs
		List<String> ids = new ArrayList<>();
		Bundle resultBundle = myClient.search().forResource("Observation").count(100).returnBundle(Bundle.class).execute();
		int pageIndex = 0;
		while (true) {
			ids.addAll(resultBundle.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList()));
			if (resultBundle.getLink("next") == null) {
				break;
			}
			ourLog.info("Loading page {} - Have {} results: {}", pageIndex++, ids.size(), resultBundle.getLink("next").getUrl());
			resultBundle = myClient.loadPage().next(resultBundle).execute();
		}
		assertEquals(count, ids.size());
		assertEquals(count, Sets.newHashSet(ids).size());

		// Load from DAOs
		ids = new ArrayList<>();
		SearchParameterMap map = new SearchParameterMap();
		map.add("status", new TokenOrListParam().add("final").add("aaa")); // add some noise to guarantee we don't reuse a previous query
		IBundleProvider results = myObservationDao.search(map);
		for (int i = 0; i <= count; i += 100) {
			List<IBaseResource> resultsAndIncludes = results.getResources(i, i + 100);
			ids.addAll(toUnqualifiedVersionlessIdValues(resultsAndIncludes));
			results = myPagingProvider.retrieveResultList(null, results.getUuid());
		}
		assertEquals(count, ids.size());
		assertEquals(count, Sets.newHashSet(ids).size());

		// Load from DAOs starting half way through
		ids = new ArrayList<>();
		map = new SearchParameterMap();
		map.add("status", new TokenOrListParam().add("final").add("aaa")); // add some noise to guarantee we don't reuse a previous query
		results = myObservationDao.search(map);
		for (int i = 1000; i <= count; i += 100) {
			List<IBaseResource> resultsAndIncludes = results.getResources(i, i + 100);
			ids.addAll(toUnqualifiedVersionlessIdValues(resultsAndIncludes));
			results = myPagingProvider.retrieveResultList(null, results.getUuid());
		}
		assertEquals(count - 1000, ids.size());
		assertEquals(count - 1000, Sets.newHashSet(ids).size());
	}

	@Test
	public void testPageThroughLotsOfPages2() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);

		Bundle bundle = new Bundle();

		int count = 1603;
		for (int i = 0; i < count; i++) {
			Observation o = new Observation();
			o.setId("A" + leftPad(Integer.toString(i), 4, '0'));
			o.setStatus(Observation.ObservationStatus.FINAL);
			bundle.addEntry().setFullUrl(o.getId()).setResource(o).getRequest().setMethod(HTTPVerb.PUT).setUrl("Observation/A" + i);
		}
		StopWatch sw = new StopWatch();
		ourLog.info("Saving {} resources", bundle.getEntry().size());
		mySystemDao.transaction(null, bundle);
		ourLog.info("Saved {} resources in {}", bundle.getEntry().size(), sw.toString());

		// Load from DAOs
		List<String> ids = new ArrayList<>();
		Bundle resultBundle = myClient.search().forResource("Observation").count(300).returnBundle(Bundle.class).execute();
		int pageIndex = 0;
		while (true) {
			ids.addAll(resultBundle.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList()));
			if (resultBundle.getLink("next") == null) {
				break;
			}
			ourLog.info("Loading page {} - Have {} results: {}", pageIndex++, ids.size(), resultBundle.getLink("next").getUrl());
			resultBundle = myClient.loadPage().next(resultBundle).execute();
		}
		assertEquals(count, ids.size());
		assertEquals(count, Sets.newHashSet(ids).size());

	}

	@Test
	public void testSearchWithLargeNumberOfIncludes() {

		Bundle bundle = new Bundle();

		DiagnosticReport dr = new DiagnosticReport();
		dr.setId(IdType.newRandomUuid());
		bundle.addEntry().setFullUrl(dr.getId()).setResource(dr).getRequest().setMethod(HTTPVerb.POST).setUrl("DiagnosticReport");

		for (int i = 0; i < 1200; i++) {
			Observation o = new Observation();
			o.setId(IdType.newRandomUuid());
			o.setStatus(Observation.ObservationStatus.FINAL);
			bundle.addEntry().setFullUrl(o.getId()).setResource(o).getRequest().setMethod(HTTPVerb.POST).setUrl("Observation");
			dr.addResult().setReference(o.getId());

			if (i == 0) {
				Observation o2 = new Observation();
				o2.setId(IdType.newRandomUuid());
				o2.setStatus(Observation.ObservationStatus.FINAL);
				bundle.addEntry().setFullUrl(o2.getId()).setResource(o2).getRequest().setMethod(HTTPVerb.POST).setUrl("Observation");
				o.addHasMember(new Reference(o2.getId()));
			}
		}

		StopWatch sw = new StopWatch();
		ourLog.info("Saving {} resources", bundle.getEntry().size());
		mySystemDao.transaction(null, bundle);
		ourLog.info("Saved {} resources in {}", bundle.getEntry().size(), sw.toString());

		// Using _include=*
		SearchParameterMap map = new SearchParameterMap();
		map.addInclude(IBaseResource.INCLUDE_ALL.asRecursive());
		map.setLoadSynchronous(true);
		IBundleProvider results = myDiagnosticReportDao.search(map);
		List<IBaseResource> resultsAndIncludes = results.getResources(0, 999999);
		assertEquals(1202, resultsAndIncludes.size());

		// Using focused includes
		map = new SearchParameterMap();
		map.addInclude(DiagnosticReport.INCLUDE_RESULT.asRecursive());
		map.addInclude(Observation.INCLUDE_HAS_MEMBER.asRecursive());
		map.setLoadSynchronous(true);
		results = myDiagnosticReportDao.search(map);
		resultsAndIncludes = results.getResources(0, 999999);
		assertEquals(1202, resultsAndIncludes.size());
	}

	@Disabled
	@Test
	public void testUpdateListWithLargeNumberOfEntries() {
		int numPatients = 3000;

		ListResource lr = new ListResource();
		lr.setId(IdType.newRandomUuid());

		{
			Bundle bundle = new Bundle();
			for (int i = 0; i < numPatients; ++i) {
				Patient patient = new Patient();
				patient.setId(IdType.newRandomUuid());
				bundle.addEntry().setFullUrl(patient.getId()).setResource(patient).getRequest().setMethod(HTTPVerb.POST).setUrl("Patient");
				lr.addEntry().setItem(new Reference(patient.getId()));
			}
			bundle.addEntry().setFullUrl(lr.getId()).setResource(lr).getRequest().setMethod(HTTPVerb.POST).setUrl("List");

			StopWatch sw = new StopWatch();
			ourLog.info("Saving list with {} entries", lr.getEntry().size());
			mySystemDao.transaction(null, bundle);
			ourLog.info("Saved {} resources in {}", bundle.getEntry().size(), sw);
		}

		{
			Bundle bundle = new Bundle();

			Patient newPatient = new Patient();
			newPatient.setId(IdType.newRandomUuid());
			bundle.addEntry().setFullUrl(newPatient.getId()).setResource(newPatient).getRequest().setMethod(HTTPVerb.POST).setUrl("Patient");
			lr.addEntry().setItem(new Reference(newPatient.getId()));
			bundle.addEntry().setFullUrl(lr.getId()).setResource(lr).getRequest().setMethod(HTTPVerb.PUT).setUrl(lr.getIdElement().toUnqualifiedVersionless().getValue());

			StopWatch sw = new StopWatch();
			ourLog.info("Updating list with {} entries", lr.getEntry().size());
			mySystemDao.transaction(null, bundle);
			ourLog.info("Updated {} resources in {}", bundle.getEntry().size(), sw);
		}
	}

	@Test
	public void testMultithreadedSearch() throws Exception {
		Bundle input = new Bundle();
		input.setType(BundleType.TRANSACTION);
		for (int i = 0; i < 500; i++) {
			Patient p = new Patient();
			p.addIdentifier().setSystem("http://test").setValue("BAR");
			input.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setUrl("Patient");
		}
		myClient.transaction().withBundle(input).execute();


		List<BaseTask> tasks = Lists.newArrayList();
		try {
			for (int threadIndex = 0; threadIndex < 10; threadIndex++) {
				SearchTask task = new SearchTask();
				tasks.add(task);
				task.start();
			}
		} finally {
			for (BaseTask next : tasks) {
				next.join();
			}
		}

		validateNoErrors(tasks);

	}

	@Test
	public void testMultiThreadedCreateWithDuplicateClientAssignedIdsInTransaction() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(20);

		List<Future<String>> futures = new ArrayList<>();
		for (int i = 0; i < 100; i++) {

			int finalI = i;

			Callable<String> task = () -> {
				Bundle input = new Bundle();
				input.setType(BundleType.TRANSACTION);

				Patient p = new Patient();
				p.setId("A" + finalI);
				p.addIdentifier().setValue("A"+finalI);
				input.addEntry().setResource(p).setFullUrl("Patient/A" + finalI).getRequest().setMethod(HTTPVerb.PUT).setUrl("Patient/A" + finalI);

				try {
					myClient.transaction().withBundle(input).execute();
					return null;
				} catch (ResourceVersionConflictException e) {
					assertThat(e.toString(), containsString("Error flushing transaction with resource types: [Patient] - The operation has failed with a client-assigned ID constraint failure"));
					return e.toString();
				}
			};
			for (int j = 0; j < 2; j++) {
				Future<String> future = executor.submit(task);
				futures.add(future);
			}

		}

		List<String> results = new ArrayList<>();
		for (Future<String> next : futures) {
			String nextOutcome = next.get();
			if (isNotBlank(nextOutcome)) {
				results.add(nextOutcome);
			}
		}

		ourLog.info("Results: {}", results);
		assertThat(results, not(Matchers.empty()));
		assertThat(results.get(0), containsString("HTTP 409 Conflict: Error flushing transaction with resource types: [Patient]"));
	}

	@Test
	public void testMultiThreadedUpdateSameResourceInTransaction() throws Exception {

		Patient p = new Patient();
		p.setActive(true);
		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		ExecutorService executor = Executors.newFixedThreadPool(20);

		List<Future<String>> futures = new ArrayList<>();
		for (int i = 0; i < 100; i++) {

			int finalI = i;

			Callable<String> task = () -> {
				Bundle input = new Bundle();
				input.setType(BundleType.TRANSACTION);

				Patient updatePatient = new Patient();
				updatePatient.setId(id);
				updatePatient.addIdentifier().setValue("A"+finalI);
				input.addEntry().setResource(updatePatient).setFullUrl(updatePatient.getId()).getRequest().setMethod(HTTPVerb.PUT).setUrl(updatePatient.getId());

				try {
					myClient.transaction().withBundle(input).execute();
					return null;
				} catch (ResourceVersionConflictException e) {
					assertThat(e.toString(), containsString("Error flushing transaction with resource types: [Patient] - The operation has failed with a version constraint failure. This generally means that two clients/threads were trying to update the same resource at the same time, and this request was chosen as the failing request."));
					return e.toString();
				}
			};
			for (int j = 0; j < 2; j++) {
				Future<String> future = executor.submit(task);
				futures.add(future);
			}

		}

		List<String> results = new ArrayList<>();
		for (Future<String> next : futures) {
			String nextOutcome = next.get();
			if (isNotBlank(nextOutcome)) {
				results.add(nextOutcome);
			}
		}

		ourLog.info("Results: {}", results);
		assertThat(results, not(Matchers.empty()));
		assertThat(results.get(0), containsString("HTTP 409 Conflict: Error flushing transaction with resource types: [Patient]"));
	}

	/**
	 * This test prevents a deadlock that was detected with a large number of
	 * threads creating resources and blocking on the searchparamcache refreshing
	 * (since this is a synchronized method) while the instance that was actually
	 * executing was waiting on a DB connection. This was solved by making
	 * JpaValidationSupportDstuXX be transactional, which it should have been
	 * anyhow.
	 */
	@Test
	public void testMultithreadedSearchWithValidation() throws Exception {
		ourRestServer.registerInterceptor(myRequestValidatingInterceptor);

		Bundle input = new Bundle();
		input.setType(BundleType.TRANSACTION);
		for (int i = 0; i < 500; i++) {
			Patient p = new Patient();
			p.addIdentifier().setSystem("http://test").setValue("BAR");
			input.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setUrl("Patient");
		}
		myClient.transaction().withBundle(input).execute();

		try (CloseableHttpResponse getMeta = ourHttpClient.execute(new HttpGet(ourServerBase + "/metadata"))) {
			assertEquals(200, getMeta.getStatusLine().getStatusCode());
		}

		List<BaseTask> tasks = Lists.newArrayList();
		try {
			for (int threadIndex = 0; threadIndex < 5; threadIndex++) {
				SearchTask task = new SearchTask();
				tasks.add(task);
				task.start();
			}
			for (int threadIndex = 0; threadIndex < 5; threadIndex++) {
				CreateTask task = new CreateTask();
				tasks.add(task);
				task.start();
			}
		} finally {
			for (BaseTask next : tasks) {
				next.join();
			}
		}

		validateNoErrors(tasks);
	}
	@Test
	public void testDeleteExpungeOperationOverLargeDataset() {
		myDaoConfig.setAllowMultipleDelete(true);
		myDaoConfig.setExpungeEnabled(true);
		myDaoConfig.setDeleteExpungeEnabled(true);
	// setup
		Patient patient = new Patient();
		patient.setId("tracer");
		patient.setActive(true);
		patient.getMeta().addTag().setSystem(UUID.randomUUID().toString()).setCode(UUID.randomUUID().toString());
		MethodOutcome result = myClient.update().resource(patient).execute();

		patient.setId(result.getId());
		patient.getMeta().addTag().setSystem(UUID.randomUUID().toString()).setCode(UUID.randomUUID().toString());
		result = myClient.update().resource(patient).execute();


		Parameters input = new Parameters();
		input.addParameter(ProviderConstants.OPERATION_DELETE_EXPUNGE_URL, "/Patient?active=true");
		int batchSize = 2;
		input.addParameter(ProviderConstants.OPERATION_DELETE_BATCH_SIZE, new DecimalType(batchSize));

		// execute
		Parameters response = myClient
			.operation()
			.onServer()
			.named(ProviderConstants.OPERATION_DELETE_EXPUNGE)
			.withParameters(input)
			.execute();

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		myBatchJobHelper.awaitAllBulkJobCompletions(BatchConstants.DELETE_EXPUNGE_JOB_NAME);
		int deleteCount = myCaptureQueriesListener.countDeleteQueries();

		myCaptureQueriesListener.logDeleteQueries();
		assertThat(deleteCount, is(equalTo(19)));
	}

	private void validateNoErrors(List<BaseTask> tasks) {
		int total = 0;
		for (BaseTask next : tasks) {
			if (next.getError() != null) {
				fail(next.getError().toString());
			}
			total += next.getTaskCount();
		}

		ourLog.info("Loaded {} searches", total);
	}

	public class BaseTask extends Thread {
		protected Throwable myError;
		protected int myTaskCount = 0;

		public BaseTask() {
			setDaemon(true);
		}

		public Throwable getError() {
			return myError;
		}

		public int getTaskCount() {
			return myTaskCount;
		}

	}

	private final class SearchTask extends BaseTask {

		@Override
		public void run() {
			CloseableHttpResponse getResp = null;
			for (int i = 0; i < 10; i++) {
				try {
					Bundle respBundle;

					// Load search
					HttpGet get = new HttpGet(ourServerBase + "/Patient?identifier=http%3A%2F%2Ftest%7CBAR," + UUID.randomUUID().toString());
					get.addHeader(Constants.HEADER_CONTENT_TYPE, Constants.CT_FHIR_JSON_NEW);
					getResp = ourHttpClient.execute(get);
					try {
						String respBundleString = IOUtils.toString(getResp.getEntity().getContent(), Charsets.UTF_8);
						assertEquals(200, getResp.getStatusLine().getStatusCode(), respBundleString);
						respBundle = myFhirContext.newJsonParser().parseResource(Bundle.class, respBundleString);
						myTaskCount++;
					} finally {
						IOUtils.closeQuietly(getResp);
					}

					// Load page 2
					get = new HttpGet(respBundle.getLink("next").getUrl());
					get.addHeader(Constants.HEADER_CONTENT_TYPE, Constants.CT_FHIR_JSON_NEW);
					getResp = ourHttpClient.execute(get);
					try {
						assertEquals(200, getResp.getStatusLine().getStatusCode());
						String respBundleString = IOUtils.toString(getResp.getEntity().getContent(), Charsets.UTF_8);
						respBundle = myFhirContext.newJsonParser().parseResource(Bundle.class, respBundleString);
						myTaskCount++;
					} finally {
						IOUtils.closeQuietly(getResp);
					}

				} catch (Throwable e) {
					ourLog.error("Failure during search", e);
					myError = e;
					return;
				}
			}
		}
	}

	private final class CreateTask extends BaseTask {

		@Override
		public void run() {
			for (int i = 0; i < 50; i++) {
				try {
					Patient p = new Patient();
					p.addIdentifier().setSystem("http://test").setValue("BAR").setType(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("bar")));
					p.setGender(org.hl7.fhir.r4.model.Enumerations.AdministrativeGender.MALE);
					myClient.create().resource(p).execute();

					ourSearchParamRegistry.forceRefresh();

				} catch (Throwable e) {
					ourLog.error("Failure during search", e);
					myError = e;
					return;
				}
			}
		}
	}


}
