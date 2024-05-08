package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.jpa.test.config.DelayListener;
import ca.uhn.fhir.jpa.test.config.TestR4WithDelayConfig;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.test.utilities.JettyUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestR4WithDelayConfig.class})
public class CascadingDeleteInterceptorMultiThreadTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CascadingDeleteInterceptorMultiThreadTest.class);

	@Autowired
	private CascadingDeleteInterceptor myDeleteInterceptor;
	@Autowired
	@Qualifier("myResourceProvidersR4")
	protected ResourceProviderFactory myResourceProviders;
	@Autowired
	protected ApplicationContext myAppCtx;
	@Autowired
	protected FhirContext myFhirContext;
	@Autowired
	protected JpaStorageSettings myStorageSettings;
	@Autowired
	@Qualifier("mySystemDaoR4")
	protected IFhirSystemDao<Bundle, Meta> mySystemDao;
	@Autowired
	protected IResourceReindexingSvc myResourceReindexingSvc;
	@Autowired
	protected ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired
	protected SearchParamRegistryImpl mySearchParamRegistry;
	@Autowired
	private IBulkDataExportJobSchedulingHelper myBulkDataScheduleHelper;
	@Autowired
	DelayListener myDelayListener;
	@Autowired
	protected CircularQueueCaptureQueriesListener myCaptureQueriesListener;


	private static Server ourServer;
	private static RestfulServer ourRestServer;
	private static String ourServerBase;
	private IIdType myOrganizationId;
	private IIdType myPractitionerId;
	private IGenericClient myClient;
	private CloseableHttpClient myHttpClient1;
	private CloseableHttpClient myHttpClient2;

	@BeforeEach
	public void before() throws Exception {
		myFhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		myFhirContext.getRestfulClientFactory().setSocketTimeout(1200 * 1000);
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		if (ourServer == null) {
			ourRestServer = new RestfulServer(myFhirContext);
			ourRestServer.registerProviders(myResourceProviders.createProviders());
			ourRestServer.setDefaultResponseEncoding(EncodingEnum.XML);

			Server server = new Server(0);

			ServletContextHandler proxyHandler = new ServletContextHandler();
			proxyHandler.setContextPath("/");

			ServletHolder servletHolder = new ServletHolder();
			servletHolder.setServlet(ourRestServer);
			proxyHandler.addServlet(servletHolder, "/fhir/context/*");

			GenericWebApplicationContext ourWebApplicationContext = new GenericWebApplicationContext();
			ourWebApplicationContext.setParent(myAppCtx);
			ourWebApplicationContext.refresh();
			proxyHandler.getServletContext().setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ourWebApplicationContext);

			server.setHandler(proxyHandler);
			JettyUtil.startServer(server);
			int port = JettyUtil.getPortForStartedServer(server);
			ourServerBase = "http://localhost:" + port + "/fhir/context";

			myFhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
			myFhirContext.getRestfulClientFactory().setSocketTimeout(400000);

			ourServer = server;
		}

		myClient = myFhirContext.newRestfulGenericClient(ourServerBase);
		myClient.registerInterceptor(new LoggingInterceptor());

		myHttpClient1 = getHttpClient();
		myHttpClient2 = getHttpClient();
	}

	@AfterEach
	public void afterTest() throws IOException {
		purgeDatabase(myStorageSettings, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataScheduleHelper);
		if (myCaptureQueriesListener != null) {
			myCaptureQueriesListener.clear();
		}
		myDelayListener.reset();
		myHttpClient1.close();
		myHttpClient2.close();
	}

	protected static void purgeDatabase(JpaStorageSettings theStorageSettings, IFhirSystemDao<?, ?> theSystemDao, IResourceReindexingSvc theResourceReindexingSvc, ISearchCoordinatorSvc theSearchCoordinatorSvc, ISearchParamRegistry theSearchParamRegistry, IBulkDataExportJobSchedulingHelper theBulkDataJobActivator) {
		theSearchCoordinatorSvc.cancelAllActiveSearches();
		theResourceReindexingSvc.cancelAndPurgeAllJobs();
		theBulkDataJobActivator.cancelAndPurgeAllJobs();

		boolean expungeEnabled = theStorageSettings.isExpungeEnabled();
		boolean multiDeleteEnabled = theStorageSettings.isAllowMultipleDelete();
		theStorageSettings.setExpungeEnabled(true);
		theStorageSettings.setAllowMultipleDelete(true);

		for (int count = 0; ; count++) {
			try {
				theSystemDao.expunge(new ExpungeOptions().setExpungeEverything(true), null);
				break;
			} catch (Exception e) {
				if (count >= 3) {
					ourLog.error("Failed during expunge", e);
					fail(e.toString());
				} else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e2) {
						fail(e2.toString());
					}
				}
			}
		}
		theStorageSettings.setExpungeEnabled(expungeEnabled);
		theStorageSettings.setAllowMultipleDelete(multiDeleteEnabled);

		theSearchParamRegistry.forceRefresh();
	}


	@AfterAll
	public static void afterClassClearContextBaseResourceProviderR4Test() throws Exception {
		JettyUtil.closeServer(ourServer);
		ourServer = null;
	}

	public void createResources() {
		Practitioner prac = new Practitioner();
		prac.setActive(true);
		prac.setId("test-prac");
		myPractitionerId = myClient.update().resource(prac).withId("Practitioner/test-prac").execute().getId().toUnqualifiedVersionless();

		Organization org = new Organization();
		org.setActive(true);
		org.setId("test-org");
		myOrganizationId = myClient.update().resource(org).withId("Organization/test-org").execute().getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.addParticipant().setIndividual(new Reference(myPractitionerId));
		enc.setServiceProvider(new Reference(myOrganizationId));
		enc.setId("test-enc-1");
		myClient.update().resource(enc).withId("Encounter/test-enc-1").execute();

		enc = new Encounter();
		enc.addParticipant().setIndividual(new Reference(myPractitionerId));
		enc.setServiceProvider(new Reference(myOrganizationId));
		enc.setId("test-enc-2");
		myClient.update().resource(enc).withId("Encounter/test-enc-2").execute();

		enc = new Encounter();
		enc.addParticipant().setIndividual(new Reference(myPractitionerId));
		enc.setServiceProvider(new Reference(myOrganizationId));
		enc.setId("test-enc-3");
		myClient.update().resource(enc).withId("Encounter/test-enc-3").execute();

		enc = new Encounter();
		enc.addParticipant().setIndividual(new Reference(myPractitionerId));
		enc.setServiceProvider(new Reference(myOrganizationId));
		enc.setId("test-enc-4");
		myClient.update().resource(enc).withId("Encounter/test-enc-4").execute();

	}

	@Test
	public void testDeleteCascadingConcurrentThreadsWithOneDelayed() {
		myDelayListener.enable();

		myStorageSettings.setRespectVersionsForSearchIncludes(false);
		createResources();

		ourRestServer.getInterceptorService().registerInterceptor(myDeleteInterceptor);

		ExecutorService executor = Executors.newFixedThreadPool(2);
		Callable<Boolean> job1 = () -> {
			try {
				return deleteOrganization(myHttpClient1);
			} catch (IOException theE) {
				theE.printStackTrace();
			}
			return false;
		};
		Callable<Boolean> job2 = () -> {
			try {
				return deletePractitioner(myHttpClient2);
			} catch (IOException theE) {
				theE.printStackTrace();
			}
			return false;
		};

		try {
			List<Future<Boolean>> futures = new ArrayList<>();
			futures.add(executor.submit(job1));
			futures.add(executor.submit(job2));
			myCaptureQueriesListener.logAllQueriesForCurrentThread();
			List<Boolean> results = new ArrayList<>();
			for (Future<Boolean> next : futures) {
				results.add(next.get());
			}
			for (Boolean next : results) {
				assert(next);
			}
		} catch (ExecutionException | InterruptedException theE) {
			theE.printStackTrace();
		} finally {
			executor.shutdown();
		}

	}

	@Test
	public void testDeleteCascadingConcurrentThreads() {
		myStorageSettings.setRespectVersionsForSearchIncludes(false);
		createResources();

		ourRestServer.getInterceptorService().registerInterceptor(myDeleteInterceptor);

		ExecutorService executor = Executors.newFixedThreadPool(2);
		Callable<Boolean> job1 = () -> {
			try {
				return deleteOrganization(myHttpClient1);
			} catch (IOException theE) {
				theE.printStackTrace();
			}
			return false;
		};
		Callable<Boolean> job2 = () -> {
			try {
				return deletePractitioner(myHttpClient2);
			} catch (IOException theE) {
				theE.printStackTrace();
			}
			return false;
		};

		try {
			List<Future<Boolean>> futures = new ArrayList<>();
			futures.add(executor.submit(job1));
			futures.add(executor.submit(job2));
			List<Boolean> results = new ArrayList<>();
			for (Future<Boolean> next : futures) {
				results.add(next.get());
			}
			for (Boolean next : results) {
				assert(next);
			}
		} catch (ExecutionException | InterruptedException theE) {
			theE.printStackTrace();
		} finally {
			executor.shutdown();
		}

	}

	@Test
	public void testDeleteCascadingSequentialThreads() {
		myStorageSettings.setRespectVersionsForSearchIncludes(false);
		createResources();

		ourRestServer.getInterceptorService().registerInterceptor(myDeleteInterceptor);

		ExecutorService executor = Executors.newFixedThreadPool(2);
		Callable<Boolean> job1 = () -> {
			try {
				return deleteOrganization(myHttpClient1);
			} catch (IOException theE) {
				theE.printStackTrace();
			}
			return false;
		};
		Callable<Boolean> job2 = () -> {
			try {
				return deletePractitioner(myHttpClient2);
			} catch (IOException theE) {
				theE.printStackTrace();
			}
			return false;
		};

		try {
			Future<Boolean> future1 = executor.submit(job1);
			// 100ms seems to be too short
			Thread.sleep(300L);
			Future<Boolean> future2 = executor.submit(job2);
			List<Boolean> results = new ArrayList<>();
			results.add(future1.get());
			results.add(future2.get());
			for (Boolean next : results) {
				assert(next);
			}
		} catch (ExecutionException | InterruptedException theE) {
			theE.printStackTrace();
		} finally {
			executor.shutdown();
		}
	}

	@Test
	public void testDeleteCascadingSingleThread() {
		myStorageSettings.setRespectVersionsForSearchIncludes(false);
		createResources();

		ourRestServer.getInterceptorService().registerInterceptor(myDeleteInterceptor);

		boolean deleteOrganizationSucceeded = false;
		boolean deletePractitionerSucceeded = false;
		try {
			deleteOrganizationSucceeded = deleteOrganization(myHttpClient1);
			deletePractitionerSucceeded = deletePractitioner(myHttpClient2);
		} catch (IOException theE) {
			theE.printStackTrace();
		}
		assert(deleteOrganizationSucceeded && deletePractitionerSucceeded);

	}

	private CloseableHttpClient getHttpClient() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		connectionManager.setMaxTotal(10);
		connectionManager.setDefaultMaxPerRoute(10);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		builder.setMaxConnPerRoute(99);

		return builder.build();

	}

	private boolean deletePractitioner(CloseableHttpClient theCloseableHttpClient) throws IOException {
		ourLog.info("Starting deletePractitioner");
		HttpDelete delete = new HttpDelete(ourServerBase + "/" + myPractitionerId.getValue() + "?" + Constants.PARAMETER_CASCADE_DELETE + "=" + Constants.CASCADE_DELETE + "&_pretty=true");
		delete.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW);
		try (CloseableHttpResponse response = theCloseableHttpClient.execute(delete)) {
			if (response.getStatusLine().getStatusCode() != 200) {
				ourLog.error("Unexpected status on practitioner delete = " + response.getStatusLine().getStatusCode());
				return false;
			}
			String deleteResponse = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", deleteResponse);
			if (!deleteResponse.contains("Cascaded delete to ") && !deleteResponse.contains("Successfully deleted 1 resource(s)")) {
				ourLog.error("Unexpected response on practitioner delete = " + deleteResponse);
				return false;
			}
		}

		ourLog.info("Delete of Practitioner completed");

		try {
			ourLog.info("Reading {}", myPractitionerId);
			myClient.read().resource(Practitioner.class).withId(myPractitionerId).execute();
			fail();
		} catch (ResourceGoneException ignored) {
			ourLog.info("Practitioner resource gone as expected");
		}
		try {
			ourLog.info("Searching for encounters after deleting Practitioner");
			myClient.read().resource(Encounter.class).withId("Encounter/test-enc-3").execute();
			fail();
		} catch (ResourceGoneException ignored) {
			ourLog.info("Encounter resource gone as expected after Practitioner deleted");
		}
		return true;
	}

	private boolean deleteOrganization(CloseableHttpClient theCloseableHttpClient) throws IOException {
		ourLog.info("Starting deleteOrganization");
		HttpDelete delete = new HttpDelete(ourServerBase + "/" + myOrganizationId.getValue() + "?" + Constants.PARAMETER_CASCADE_DELETE + "=" + Constants.CASCADE_DELETE + "&_pretty=true");
		delete.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW);
		ourLog.info("HttpDelete : {}", delete);
		try (CloseableHttpResponse response = theCloseableHttpClient.execute(delete)) {
			if (response.getStatusLine().getStatusCode() != 200) {
				ourLog.error("Unexpected status on organization delete = " + response.getStatusLine().getStatusCode());
				return false;
			}
			String deleteResponse = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", deleteResponse);
			if (!deleteResponse.contains("Cascaded delete to ") && !deleteResponse.contains("Successfully deleted 1 resource(s)")) {
				ourLog.error("Unexpected response organization delete = " + deleteResponse);
				return false;
			}
		}

		ourLog.info("Delete of organization resource completed.");

		try {
			ourLog.info("Reading {}", myOrganizationId);
			myClient.read().resource(Organization.class).withId(myOrganizationId).execute();
			fail();
		} catch (ResourceGoneException ignored) {
			ourLog.info("Organization resource gone as expected");
		}
		try {
			ourLog.info("Searching for encounters after deleting Organization");
			myClient.read().resource(Encounter.class).withId("Encounter/test-enc-3").execute();
			fail();
		} catch (ResourceGoneException ignored) {
			ourLog.info("Encounter resource gone as expected after Organization deleted");
		}
		return true;
	}

}
