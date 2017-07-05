package ca.uhn.fhir.jpa.provider.dstu3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.dstu3.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.junit.*;

import com.google.common.collect.Lists;

import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.util.TestUtil;

public class StressTestDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(StressTestDstu3Test.class);
	private RequestValidatingInterceptor myRequestValidatingInterceptor;

	@Before
	public void before() throws Exception {
		super.before();
		
		myRequestValidatingInterceptor = new RequestValidatingInterceptor();
		FhirInstanceValidator module = new FhirInstanceValidator();
		module.setValidationSupport(myValidationSupport);
		myRequestValidatingInterceptor.addValidatorModule(module);
	}

	@After
	public void after() throws Exception {
		super.after();
		
		ourRestServer.unregisterInterceptor(myRequestValidatingInterceptor);
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
			for (int threadIndex = 0; threadIndex < 8; threadIndex++) {
				SearchTask task = new SearchTask();
				tasks.add(task);
				task.start();
			}
			for (int threadIndex = 0; threadIndex < 8; threadIndex++) {
				CreateTask task = new CreateTask();
				tasks.add(task);
				task.start();
			}
		} finally {
			for (BaseTask next : tasks) {
				next.join();
			}
		}

		int total = 0;
		for (BaseTask next : tasks) {
			if (next.getError() != null) {
				fail(next.getError().toString());
			}
			total += next.getTaskCount();
		}
		
		ourLog.info("Loaded {} searches", total);
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	public class BaseTask extends Thread  {
		protected Throwable myError;
		protected int myTaskCount = 0;
		
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
			CloseableHttpResponse get = null;
			for (int i = 0; i < 20; i++) {
				try {
					get = ourHttpClient.execute(new HttpGet(ourServerBase + "/Patient?identifier=http%3A%2F%2Ftest%7CBAR," + UUID.randomUUID().toString()));
					try {
						assertEquals(200, get.getStatusLine().getStatusCode());
						myTaskCount++;
					} finally {
						IOUtils.closeQuietly(get);
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
					p.setGender(org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender.MALE);
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

}
