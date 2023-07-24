package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.batch2.jobs.expunge.DeleteExpungeProvider;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexProvider;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.batch2.jobs.export.BulkDataExportProvider;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.fql.provider.HfqlRestProvider;
import ca.uhn.fhir.jpa.graphql.GraphQLProvider;
import ca.uhn.fhir.jpa.provider.DiffProvider;
import ca.uhn.fhir.jpa.provider.JpaCapabilityStatementProvider;
import ca.uhn.fhir.jpa.provider.ProcessMessageProvider;
import ca.uhn.fhir.jpa.provider.ServerConfiguration;
import ca.uhn.fhir.jpa.provider.SubscriptionTriggeringProvider;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.provider.ValueSetOperationProvider;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionLoader;
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
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Parameters.ParametersParameterComponent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.cors.CorsConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ContextConfiguration(classes = ServerConfiguration.class)
public abstract class BaseResourceProviderR5Test extends BaseJpaR5Test {
	protected int myPort;
	protected String myServerBase;
	protected RestfulServer myRestServer;
	protected IGenericClient myClient;

	@RegisterExtension
	protected static HttpClientExtension ourHttpClient = new HttpClientExtension();

	@Autowired
	@RegisterExtension
	protected RestfulServerExtension myServer;

	@RegisterExtension
	protected RestfulServerConfigurerExtension myServerConfigurer = new RestfulServerConfigurerExtension(() -> myServer)
		.withServerBeforeAll(s -> {
			s.registerProviders(myResourceProviders.createProviders());
			s.setDefaultResponseEncoding(EncodingEnum.XML);
			s.setDefaultPrettyPrint(false);

			myFhirContext.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

			s.registerProvider(mySystemProvider);
			s.registerProvider(myBinaryAccessProvider);
			s.registerProvider(myAppCtx.getBean(BulkDataExportProvider.class));
			s.registerProvider(myAppCtx.getBean(DeleteExpungeProvider.class));
			s.registerProvider(myAppCtx.getBean(DiffProvider.class));
			s.registerProvider(myAppCtx.getBean(GraphQLProvider.class));
			s.registerProvider(myAppCtx.getBean(ProcessMessageProvider.class));
			s.registerProvider(myAppCtx.getBean(ReindexProvider.class));
			s.registerProvider(myAppCtx.getBean(SubscriptionTriggeringProvider.class));
			s.registerProvider(myAppCtx.getBean(TerminologyUploaderProvider.class));
			s.registerProvider(myAppCtx.getBean(ValueSetOperationProvider.class));
			s.registerProvider(myAppCtx.getBean(HfqlRestProvider.class));

			s.setPagingProvider(myAppCtx.getBean(DatabaseBackedPagingProvider.class));

			s.getInterceptorService().registerInterceptor(myBinaryStorageInterceptor);

			JpaCapabilityStatementProvider confProvider = new JpaCapabilityStatementProvider(s, mySystemDao, myStorageSettings, mySearchParamRegistry, myValidationSupport);
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

		}).withServerBeforeEach(s -> {
			myPort = myServer.getPort();
			myServerBase = myServer.getBaseUrl();
			myClient = myServer.getFhirClient();
			myRestServer = myServer.getRestfulServer();

			myClient.getInterceptorService().unregisterInterceptorsIf(t -> t instanceof LoggingInterceptor);
			if (shouldLogClient()) {
				myClient.registerInterceptor(new LoggingInterceptor());
			}

		});
	@Autowired
	protected SubscriptionLoader mySubscriptionLoader;
	@Autowired
	protected DaoRegistry myDaoRegistry;

	public BaseResourceProviderR5Test() {
		super();
	}

	@AfterEach
	public void after() throws Exception {
		myFhirCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		if (myRestServer != null) {
			myRestServer.getInterceptorService().unregisterAllInterceptors();
		}
	}

	protected boolean shouldLogClient() {
		return true;
	}

	public static int getNumberOfParametersByName(Parameters theParameters, String theName) {
		int retVal = 0;

		for (ParametersParameterComponent param : theParameters.getParameter()) {
			if (param.getName().equals(theName)) {
				retVal++;
			}
		}

		return retVal;
	}

	public static ParametersParameterComponent getParameterByName(Parameters theParameters, String theName) {
		for (ParametersParameterComponent param : theParameters.getParameter()) {
			if (param.getName().equals(theName)) {
				return param;
			}
		}

		return new ParametersParameterComponent();
	}

	public static List<ParametersParameterComponent> getParametersByName(Parameters theParameters, String theName) {
		List<ParametersParameterComponent> params = new ArrayList<>();
		for (ParametersParameterComponent param : theParameters.getParameter()) {
			if (param.getName().equals(theName)) {
				params.add(param);
			}
		}

		return params;
	}

	public static ParametersParameterComponent getPartByName(ParametersParameterComponent theParameter, String theName) {
		for (ParametersParameterComponent part : theParameter.getPart()) {
			if (part.getName().equals(theName)) {
				return part;
			}
		}

		return new ParametersParameterComponent();
	}

}
