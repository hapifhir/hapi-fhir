package ca.uhn.fhir.jpa.provider.r4b;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.config.r4b.FhirContextR4BConfig;
import ca.uhn.fhir.jpa.dao.r4b.BaseJpaR4BTest;
import ca.uhn.fhir.jpa.graphql.GraphQLProvider;
import ca.uhn.fhir.jpa.provider.JpaCapabilityStatementProvider;
import ca.uhn.fhir.jpa.provider.SubscriptionTriggeringProvider;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.provider.ValueSetOperationProvider;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.subscription.match.config.WebsocketDispatcherConfig;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionLoader;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

public abstract class BaseResourceProviderR4BTest extends BaseJpaR4BTest {

	@RegisterExtension
	protected static HttpClientExtension ourHttpClient = new HttpClientExtension();
	protected static int ourPort;
	protected static String ourServerBase;
	protected static IGenericClient ourClient;
	protected static RestfulServer ourRestServer;

	@RegisterExtension
	protected static RestfulServerExtension ourServer = new RestfulServerExtension(FhirContextR4BConfig.configureFhirContext(FhirContext.forR4BCached()))
		.keepAliveBetweenTests()
		.withValidationMode(ServerValidationModeEnum.NEVER)
		.withContextPath("/fhir")
		.withServletPath("/context/*")
		.withSpringWebsocketSupport(WEBSOCKET_CONTEXT, WebsocketDispatcherConfig.class);

	@RegisterExtension
	protected RestfulServerConfigurerExtension myServerConfigurer = new RestfulServerConfigurerExtension(ourServer)
		.withServerBeforeEach(s -> {
			s.registerProviders(myResourceProviders.createProviders());
			s.getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());
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
			ourPort = ourServer.getPort();
			ourServerBase = ourServer.getBaseUrl();
			ourClient = ourServer.getFhirClient();
			ourRestServer = ourServer.getRestfulServer();

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

	@BeforeEach
	public void before() throws Exception {
	}

	protected boolean shouldLogClient() {
		return true;
	}

}
