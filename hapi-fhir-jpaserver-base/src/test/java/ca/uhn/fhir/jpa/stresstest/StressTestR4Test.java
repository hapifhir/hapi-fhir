package ca.uhn.fhir.jpa.stresstest;

import ca.uhn.fhir.jpa.config.TestR4Config;
import ca.uhn.fhir.jpa.config.UnregisterScheduledProcessor;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@TestPropertySource(properties = {
	// Since scheduled tasks can cause searches, which messes up the
	// value returned by SearchBuilder.getLastHandlerMechanismForUnitTest()
	UnregisterScheduledProcessor.SCHEDULING_DISABLED + "=true",
	"max_db_connections=10"
})
public class StressTestR4Test extends BaseResourceProviderR4Test {

	static {
		TestR4Config.ourMaxThreads = 10;
	}

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(StressTestR4Test.class);
	private RequestValidatingInterceptor myRequestValidatingInterceptor;
	@Autowired
	private IPagingProvider myPagingProvider;

	@Override
	@After
	public void after() throws Exception {
		super.after();

		ourRestServer.unregisterInterceptor(myRequestValidatingInterceptor);
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);

	}

	@Override
	@Before
	public void before() throws Exception {
		super.before();

		myRequestValidatingInterceptor = new RequestValidatingInterceptor();
		FhirInstanceValidator module = new FhirInstanceValidator();
		module.setValidationSupport(myValidationSupport);
		myRequestValidatingInterceptor.addValidatorModule(module);
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
		Bundle resultBundle = ourClient.search().forResource("Observation").count(100).returnBundle(Bundle.class).execute();
		int pageIndex = 0;
		while (true) {
			ids.addAll(resultBundle.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList()));
			if (resultBundle.getLink("next") == null) {
				break;
			}
			ourLog.info("Loading page {} - Have {} results: {}", pageIndex++, ids.size(), resultBundle.getLink("next").getUrl());
			resultBundle = ourClient.loadPage().next(resultBundle).execute();
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
			results = myPagingProvider.retrieveResultList(results.getUuid());
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
			results = myPagingProvider.retrieveResultList(results.getUuid());
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
		Bundle resultBundle = ourClient.search().forResource("Observation").count(300).returnBundle(Bundle.class).execute();
		int pageIndex = 0;
		while (true) {
			ids.addAll(resultBundle.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList()));
			if (resultBundle.getLink("next") == null) {
				break;
			}
			ourLog.info("Loading page {} - Have {} results: {}", pageIndex++, ids.size(), resultBundle.getLink("next").getUrl());
			resultBundle = ourClient.loadPage().next(resultBundle).execute();
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


	@Test
	public void testMultithreadedSearch() throws Exception {
		Bundle input = new Bundle();
		input.setType(BundleType.TRANSACTION);
		for (int i = 0; i < 500; i++) {
			Patient p = new Patient();
			p.addIdentifier().setSystem("http://test").setValue("BAR");
			input.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setUrl("Patient");
		}
		ourClient.transaction().withBundle(input).execute();


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
					ourClient.transaction().withBundle(input).execute();
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
					ourClient.transaction().withBundle(input).execute();
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
		ourClient.transaction().withBundle(input).execute();

		CloseableHttpResponse getMeta = ourHttpClient.execute(new HttpGet(ourServerBase + "/metadata"));
		try {
			assertEquals(200, getMeta.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(getMeta);
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
						assertEquals(200, getResp.getStatusLine().getStatusCode());
						String respBundleString = IOUtils.toString(getResp.getEntity().getContent(), Charsets.UTF_8);
						respBundle = myFhirCtx.newJsonParser().parseResource(Bundle.class, respBundleString);
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
						respBundle = myFhirCtx.newJsonParser().parseResource(Bundle.class, respBundleString);
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
					ourClient.create().resource(p).execute();

					ourSearchParamRegistry.forceRefresh();

				} catch (Throwable e) {
					ourLog.error("Failure during search", e);
					myError = e;
					return;
				}
			}
		}
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


}
