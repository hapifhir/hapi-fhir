package ca.uhn.fhir.jpa.provider.r4b;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.r4b.BaseJpaR4BTest;
import ca.uhn.fhir.jpa.graphql.GraphQLProvider;
import ca.uhn.fhir.jpa.provider.JpaCapabilityStatementProvider;
import ca.uhn.fhir.jpa.provider.ServerConfiguration;
import ca.uhn.fhir.jpa.provider.SubscriptionTriggeringProvider;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.provider.ValueSetOperationProvider;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionLoader;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerConfigurerExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@ContextConfiguration(classes = ServerConfiguration.class)
public abstract class BaseResourceProviderR4BTest extends BaseJpaR4BTest {

	@RegisterExtension
	protected static HttpClientExtension ourHttpClient = new HttpClientExtension();

	// TODO: JA2 These are no longer static but are named like static. I'm going to
	// rename them in a separate PR that only makes that change so that it's easier to review
	protected int ourPort;
	protected String ourServerBase;
	protected IGenericClient ourClient;
	protected RestfulServer ourRestServer;

	@Autowired
	@RegisterExtension
	protected RestfulServerExtension myServer;

	@RegisterExtension
	protected RestfulServerConfigurerExtension myServerConfigurer = new RestfulServerConfigurerExtension(() -> myServer)
		.withServerBeforeEach(s -> {
			s.registerProviders(myResourceProviders.createProviders());
			s.setDefaultResponseEncoding(EncodingEnum.XML);
			s.setDefaultPrettyPrint(false);

			s.registerProvider(mySystemProvider);
			s.registerProvider(myAppCtx.getBean(GraphQLProvider.class));
			s.registerProvider(myAppCtx.getBean(SubscriptionTriggeringProvider.class));
			s.registerProvider(myAppCtx.getBean(TerminologyUploaderProvider.class));
			s.registerProvider(myAppCtx.getBean(ValueSetOperationProvider.class));

			s.setPagingProvider(myAppCtx.getBean(DatabaseBackedPagingProvider.class));

			s.registerProvider(myBinaryAccessProvider);
			s.getInterceptorService().registerInterceptor(myBinaryStorageInterceptor);

			JpaCapabilityStatementProvider confProvider = new JpaCapabilityStatementProvider(ourRestServer, mySystemDao, myDaoConfig, mySearchParamRegistry, myValidationSupport);
			confProvider.setImplementationDescription("THIS IS THE DESC");
			s.setServerConformanceProvider(confProvider);

			// Register a CORS filter
			CorsConfiguration config = new CorsConfiguration();
			CorsInterceptor corsInterceptor = new CorsInterceptor(config);
			config.addAllowedHeader("Accept");
			config.addAllowedHeader("Access-Control-Request-Headers");
			config.addAllowedHeader("Access-Control-Request-Method");
			config.addAllowedHeader("Cache-Control");
			config.addAllowedHeader("Content-Type");
			config.addAllowedHeader("Origin");
			config.addAllowedHeader("Prefer");
			config.addAllowedHeader("x-fhir-starter");
			config.addAllowedHeader("X-Requested-With");
			config.addAllowedOrigin("*");
			config.addExposedHeader("Location");
			config.addExposedHeader("Content-Location");
			config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
			s.registerInterceptor(corsInterceptor);

		}).withServerBeforeAll(s -> {

			// TODO: JA-2 These don't need to be static variables, should just inline all of the uses of these
			ourPort = myServer.getPort();
			ourServerBase = myServer.getBaseUrl();
			ourClient = myServer.getFhirClient();
			ourRestServer = myServer.getRestfulServer();

			ourClient.getInterceptorService().unregisterInterceptorsIf(t -> t instanceof LoggingInterceptor);
			if (shouldLogClient()) {
				ourClient.registerInterceptor(new LoggingInterceptor());
			}
		});

	protected IGenericClient myClient;
	@Autowired
	protected SubscriptionLoader mySubscriptionLoader;
	@Autowired
	protected DaoRegistry myDaoRegistry;
	@Autowired
	ResourceCountCache ourResourceCountsCache;

	public BaseResourceProviderR4BTest() {
		super();
	}

	@AfterEach
	public void after() throws Exception {
		myFhirCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		if (ourRestServer != null) {
			ourRestServer.getInterceptorService().unregisterAllInterceptors();
		}
	}

	protected boolean shouldLogClient() {
		return true;
	}

}
