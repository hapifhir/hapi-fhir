package embedded.example;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.*;

import com.google.common.base.Strings;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import ca.uhn.fhir.rest.server.util.ITestingUiClientFactory;
import ca.uhn.fhir.to.FhirTesterMvcConfig;
import ca.uhn.fhir.to.TesterConfig;

//@formatter:off
/**
 * This spring config file configures the web testing module. It serves two
 * purposes: 1. It imports FhirTesterMvcConfig, which is the spring config for
 * the tester itself 2. It tells the tester which server(s) to talk to, via the
 * testerConfig() method below
 */
@Configuration
@Import(FhirTesterMvcConfig.class)
public class FhirTesterConfig {

	/**
	 * This bean tells the testing webpage which servers it should configure
	 * itself to communicate with. In this example we configure it to talk to
	 * the local server, as well as one public server. If you are creating a
	 * project to deploy somewhere else, you might choose to only put your own
	 * server's address here.
	 *
	 * Note the use of the ${serverBase} variable below. This will be replaced
	 * with the base URL as reported by the server itself. Often for a simple
	 * Tomcat (or other container) installation, this will end up being
	 * something like "http://localhost:8080/hapi-fhir-jpaserver-example". If
	 * you are deploying your server to a place with a fully qualified domain
	 * name, you might want to use that instead of using the variable.
	 */
	@Bean
	public TesterConfig testerConfig() {
		final TesterConfig retVal = new TesterConfig();
		retVal.addServer().withId("Test-Server").withFhirVersion(FhirVersionEnum.DSTU2)
				.withBaseUrl(ContextListener.serverAddress).withName("FHIR Server Test Front End");

		if (!Strings.isNullOrEmpty(ContextListener.username)) {
			ITestingUiClientFactory clientFactory = new ITestingUiClientFactory() {

				@Override
				public IGenericClient newClient(FhirContext theFhirContext, HttpServletRequest theRequest,
						String theServerBaseUrl) {
					// Create a client
					IGenericClient client = theFhirContext.newRestfulGenericClient(theServerBaseUrl);

					// Register an interceptor which adds credentials
					client.registerInterceptor(
							new BasicAuthInterceptor(ContextListener.username, ContextListener.password));

					return client;
				}

			};
			retVal.setClientFactory(clientFactory);
		}
		return retVal;
	}

}
// @formatter:on