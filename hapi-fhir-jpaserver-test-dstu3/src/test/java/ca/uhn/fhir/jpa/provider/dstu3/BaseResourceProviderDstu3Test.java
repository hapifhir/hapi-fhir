package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.jpa.graphql.GraphQLProvider;
import ca.uhn.fhir.jpa.provider.ValueSetOperationProvider;
import ca.uhn.fhir.jpa.provider.ProcessMessageProvider;
import ca.uhn.fhir.jpa.provider.ServerConfiguration;
import ca.uhn.fhir.jpa.provider.SubscriptionTriggeringProvider;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.provider.ValueSetOperationProvider;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
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
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.cors.CorsConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@ContextConfiguration(classes = ServerConfiguration.class)
public abstract class BaseResourceProviderDstu3Test extends BaseJpaDstu3Test {

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

			myFhirContext.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

			s.registerProvider(mySystemProvider);
			s.registerProvider(myAppCtx.getBean(GraphQLProvider.class));
			s.registerProvider(myAppCtx.getBean(ProcessMessageProvider.class));
			s.registerProvider(myAppCtx.getBean(SubscriptionTriggeringProvider.class));
			s.registerProvider(myAppCtx.getBean(TerminologyUploaderProvider.class));
			s.registerProvider(myAppCtx.getBean(ValueSetOperationProvider.class));

			s.setPagingProvider(myAppCtx.getBean(DatabaseBackedPagingProvider.class));

			JpaConformanceProviderDstu3 confProvider = new JpaConformanceProviderDstu3(s, mySystemDao, myDaoConfig, mySearchParamRegistry);
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


	public BaseResourceProviderDstu3Test() {
		super();
	}

	@AfterEach
	public void after() throws Exception {
		myFhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		myResourceCountsCache.clear();
		myServer.getRestfulServer().getInterceptorService().unregisterAllInterceptors();
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myResourceCountsCache.clear();
	}

	protected boolean shouldLogClient() {
		return true;
	}

	protected List<String> toNameList(Bundle resp) {
		List<String> names = new ArrayList<>();
		for (BundleEntryComponent next : resp.getEntry()) {
			Patient nextPt = (Patient) next.getResource();
			String nextStr = nextPt.getName().size() > 0 ? nextPt.getName().get(0).getGivenAsSingleString() + " " + nextPt.getName().get(0).getFamily() : "";
			if (isNotBlank(nextStr)) {
				names.add(nextStr);
			}
		}
		return names;
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
