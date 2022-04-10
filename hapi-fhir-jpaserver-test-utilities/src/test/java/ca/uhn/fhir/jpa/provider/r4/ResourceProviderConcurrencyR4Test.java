package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("Duplicates")
public class ResourceProviderConcurrencyR4Test extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderConcurrencyR4Test.class);
	private ExecutorService myExecutor;
	private List<String> myReceivedNames = Collections.synchronizedList(new ArrayList<>());
	private List<Throwable> myExceptions = Collections.synchronizedList(new ArrayList<>());

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myExecutor = Executors.newFixedThreadPool(10);
		myReceivedNames.clear();
		myExceptions.clear();
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof SearchBlockingInterceptor);
		myExecutor.shutdown();

		assertThat(myExceptions, empty());
	}

	/**
	 * This test is intended to verify that we are in fact executing searches in parallel
	 * when two different searches come in.
	 * <p>
	 * We execute two identical searches (which should result in only one actual
	 * execution that will be reused by both) and one other search. We use an
	 * interceptor to artifically delay the execution of the first search in order
	 * to verify that the last search completes before the first one can finish.
	 */
	@Test
	public void testSearchesExecuteConcurrently() {
		if (TestR4Config.getMaxThreads() == 1) {
			ourLog.info("Skipping this test because the test thread pool only has one max connection");
			return;
		}

		createPatient(withFamily("FAMILY1"));
		createPatient(withFamily("FAMILY2"));
		createPatient(withFamily("FAMILY3"));

		SearchBlockingInterceptor searchBlockingInterceptorFamily1 = new SearchBlockingInterceptor("FAMILY1");
		myInterceptorRegistry.registerInterceptor(searchBlockingInterceptorFamily1);

		// Submit search 1 (should block because of interceptor semaphore)
		{
			String uri = ourServerBase + "/Patient?_format=json&family=FAMILY1";
			ourLog.info("Submitting GET " + uri);
			HttpGet get = new HttpGet(uri);
			myExecutor.submit(() -> {
				try (CloseableHttpResponse outcome = ourHttpClient.execute(get)) {
					assertEquals(200, outcome.getStatusLine().getStatusCode());
					String outcomeString = IOUtils.toString(outcome.getEntity().getContent(), StandardCharsets.UTF_8);
					Bundle bundle = myFhirContext.newJsonParser().parseResource(Bundle.class, outcomeString);
					assertEquals(1, bundle.getEntry().size());
					Patient pt = (Patient) bundle.getEntry().get(0).getResource();
					String family = pt.getNameFirstRep().getFamily();
					ourLog.info("Received response with family name: {}", family);
					myReceivedNames.add(family);
				} catch (Exception e) {
					ourLog.error("Client failure", e);
					myExceptions.add(e);
				}
			});
		}

		await().until(() -> searchBlockingInterceptorFamily1.getHits(), equalTo(1));

		// Submit search 2 (should also block because it will reuse the first search - same name being searched)
		{
			String uri = ourServerBase + "/Patient?_format=json&family=FAMILY1";
			HttpGet get = new HttpGet(uri);
			myExecutor.submit(() -> {
				ourLog.info("Submitting GET " + uri);
				try (CloseableHttpResponse outcome = ourHttpClient.execute(get)) {
					assertEquals(200, outcome.getStatusLine().getStatusCode());
					String outcomeString = IOUtils.toString(outcome.getEntity().getContent(), StandardCharsets.UTF_8);
					Bundle bundle = myFhirContext.newJsonParser().parseResource(Bundle.class, outcomeString);
					assertEquals(1, bundle.getEntry().size());
					Patient pt = (Patient) bundle.getEntry().get(0).getResource();
					String family = pt.getNameFirstRep().getFamily();
					ourLog.info("Received response with family name: {}", family);
					myReceivedNames.add(family);
				} catch (Exception e) {
					ourLog.error("Client failure", e);
					myExceptions.add(e);
				}
			});
		}

		// Submit search 3 (should not block - different name being searched, so it should actually finish first)
		{
			String uri = ourServerBase + "/Patient?_format=json&family=FAMILY3";
			HttpGet get = new HttpGet(uri);
			myExecutor.submit(() -> {
				ourLog.info("Submitting GET " + uri);
				try (CloseableHttpResponse outcome = ourHttpClient.execute(get)) {
					assertEquals(200, outcome.getStatusLine().getStatusCode());
					String outcomeString = IOUtils.toString(outcome.getEntity().getContent(), StandardCharsets.UTF_8);
					Bundle bundle = myFhirContext.newJsonParser().parseResource(Bundle.class, outcomeString);
					assertEquals(1, bundle.getEntry().size());
					Patient pt = (Patient) bundle.getEntry().get(0).getResource();
					String family = pt.getNameFirstRep().getFamily();
					ourLog.info("Received response with family name: {}", family);
					myReceivedNames.add(family);
				} catch (Exception e) {
					ourLog.error("Client failure", e);
					myExceptions.add(e);
				}
			});
		}

		ourLog.info("About to wait for FAMILY3 to complete");
		await().until(() -> myReceivedNames, contains("FAMILY3"));
		ourLog.info("Got FAMILY3");

		searchBlockingInterceptorFamily1.getLatch().countDown();

		ourLog.info("About to wait for FAMILY1 to complete");
		await().until(() -> myReceivedNames, contains("FAMILY3", "FAMILY1", "FAMILY1"));
		ourLog.info("Got FAMILY1");

		assertEquals(1, searchBlockingInterceptorFamily1.getHits());
	}


	@Interceptor
	public static class SearchBlockingInterceptor {

		private final CountDownLatch myLatch = new CountDownLatch(1);
		private final String myFamily;
		private AtomicInteger myHits = new AtomicInteger(0);

		SearchBlockingInterceptor(String theFamily) {
			myFamily = theFamily;
		}

		@Hook(Pointcut.JPA_PERFTRACE_SEARCH_FIRST_RESULT_LOADED)
		public void firstResultLoaded(RequestDetails theRequestDetails) {
			String family = theRequestDetails.getParameters().get("family")[0];
			ourLog.info("See a hit for: {}", family);
			if (family.equals(myFamily)) {
				try {
					myHits.incrementAndGet();
					if (!myLatch.await(1, TimeUnit.MINUTES)) {
						throw new InternalErrorException("Timed out waiting for " + Pointcut.JPA_PERFTRACE_SEARCH_FIRST_RESULT_LOADED);
					}
				} catch (InterruptedException e) {
					throw new InternalErrorException(e);
				}
			}
		}

		public Integer getHits() {
			return myHits.get();
		}

		public CountDownLatch getLatch() {
			return myLatch;
		}
	}


}
